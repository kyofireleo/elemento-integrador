/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package elemento;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import conectordf.ConectorDF;
import conectordf.ConstruirXML;
import static elemento.Elemento.log;
import gui.Factura_View;
import gui.Folios;
import gui.SendMail;
import java.awt.HeadlessException;
import java.io.File;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import nominas.NominaGeneral;

/**
 *
 * @author Abe
 */
public class Listener {

    utils.Utils util = new utils.Utils(Elemento.log);
    utils.ConnectionFactory factory = new utils.ConnectionFactory(Elemento.log);
    HashMap<String, List<FoliosRegistrados>> folios = new HashMap();
    CertificadoSelloDigital sellos;
    
    public Listener() {
        
    }

    
    public void fileRenamed(int wd, String rootPath, String oldName,
            String newName) {
        print("renamed " + rootPath + " : " + oldName + " -> " + newName);
    }

    
    public void fileModified(int wd, String rootPath, String name) {
        print("modified " + rootPath + " : " + name);
    }

    
    public void fileDeleted(int wd, String rootPath, String name) {
        print("deleted " + rootPath + " : " + name);
    }

    
    public void fileCreated(int wd, String rootPath, String name) {
        if(name.toLowerCase().endsWith("xml") || name.toLowerCase().endsWith("txt")){
            Elemento.log.info("Layout Detectado: " + (rootPath + name));
            try {
                this.procesar(rootPath, name);
            } catch (Exception ex) {
                ex.printStackTrace();
                Elemento.log.error("Excepcion: Ocurrio un problema al timbrar el comprobante: " + ex.getMessage(), ex);
            }
        }
    }

    public void procesar(String rootPath, String name) throws Exception {
        boolean modificar;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Elemento.log.error("Error en el Thread.sleep de la clase Listener: " + ex.getMessage(), ex);
            ex.printStackTrace();
        }
        //traerFolios();

        if (name.toUpperCase().contains(".XML")) {
            //ConectorFES con = new ConectorFES(true, Elemento.user, Elemento.pass, util.leerXml(rootPath + name), Elemento.log);
            ConectorDF con = new ConectorDF(true, Elemento.user, Elemento.pass, util.leerXml(rootPath + name), Elemento.log);
            String pathXml = Elemento.pathXml;
            if (con.timbrar(Elemento.pathXml)) {
//                this.aumentarFolio(rfcEmi, cons.getTipoComprobanteLayout());
//                this.restarCredito(rfcEmi);
//
//                Elemento.log.info("Se agrega el folio timbrado " + folio + " en la base de datos");
//                Elemento.log.info("Se comienza la generación del PDF...");
                  int idTipoComprobante = getIdComprobante(con.getObjXml().getTipoComprobanteLayout());
                  
                try {
                    Factura_View.visualizar(pathXml, name, null, idTipoComprobante);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Elemento.log.error("Excepcion desde el Listener al generar el PDF", ex);
                }
            }
        } else {
            try {
                String rfcEmi;
                List<String> lay = util.leerTxt(rootPath + name);
                if (lay.get(3).contains("RFC1:")) {
                    rfcEmi = lay.get(3).split(":")[1].trim();
                } else {
                    rfcEmi = lay.get(4).split(":")[1].trim();
                }
                Elemento.leerConfig(rfcEmi);
                //Meter logica de lectura de folios a asociar
                //ConectorSP con = new ConectorSP(Elemento.log, lay);
                ConectorDF con = new ConectorDF(Elemento.produccion, Elemento.user, Elemento.pass, lay, Elemento.log, Elemento.unidad, false, Elemento.estructuraNombre);
                //JOptionPane.showMessageDialog(null,con.consultarTimbres());
                ConstruirXML cons = con.getObjXml();
                traerFolios(cons.getRfcEmisor());
                
                sellos = traerCertificados(cons.getRfcEmisor(), cons.getRegimenFiscalEmisor());
                if(sellos == null){
                    return;
                }
                cons.setPathCert(sellos.getPathCert());
                cons.setPathKey(sellos.getPathKey());
                cons.setKeyPass(sellos.getKeyPass());
                cons.setNoCertificado(sellos.getNoCert());
                
                Factura_View fv = new Factura_View("");

                cons.crearXml();
                BigDecimal total = cons.getTotal();
                String serie = cons.getSerie();
                String folio = "" + cons.getFolio();
                String nombreRe = cons.getNombreReceptor();
                String rfcRe = cons.getRfcReceptor();
                String nombreEmi = cons.getNombreEmisor();
                rfcEmi = cons.getRfcEmisor();
                String fecha = cons.getFechaExp();
                String datos = "";
                String layout = cons.getLayoutCadena();
                String leyenda = cons.getLeyenda().trim();

                String pathXml = Elemento.pathXml;
                String destinoXml = Elemento.pathXmlMod;
                String pathXmlST = Elemento.pathXmlST;
                
                int idTipoComprobante = this.getIdComprobante(cons.getTipoComprobanteLayout());

                if (lay.get(0).equalsIgnoreCase("PREFACTURA")) {
                    if (!leyenda.isEmpty()) {
                        Elemento.interpretarXML(pathXmlST, cons.getNameXml(), leyenda, destinoXml);
                        pathXmlST = destinoXml;
                    }
                    
                    this.aumentarFolio(rfcEmi, cons.getTipoComprobanteLayout());
                    Factura_View.visualizar(pathXmlST, cons.getNameXml(), cons.jsonDomicilios, idTipoComprobante);
                    String fechaT = "01/01/2000 00:00:00";
                    String uuid = "";
                    String xml = "";
                    Long transId = 0l;
                    Boolean tim = Boolean.FALSE;
                    fv.agregarFactura(serie, folio, rfcEmi, rfcRe, nombreRe, fecha, total, datos, layout, xml, tim, fechaT, uuid, transId, cons.getTipoComprobanteLayout());
                    Elemento.log.info("Se agrega PREFACTURA con el folio " + folio + " en la base de datos");
                    openSendEmailWindow(new Folios(""), pathXmlST,cons.getNameXml(), cons);
                } else {
                    String tipoComprobante = cons.getTipoComprobanteLayout();

                    fv.consultar("Folio", "SELECT * FROM Folios WHERE rfc = \'" + cons.getRfcEmisor() + "\' AND idComprobante = " + fv.getIdComprobante(tipoComprobante));

                    //con.crearLayout(layout, name);
                    if (!tipoComprobante.equalsIgnoreCase("N")) {
                        if (!verificarCliente(rfcRe)) {
                            agregarCliente(cons);
                        }
                    }
                    String msg = null;
                    
                    List<FoliosRegistrados> listaFolios = folios.get(rfcEmi);
                    if(!listaFolios.isEmpty()){
                        if (listaFolios.stream().filter(o -> o.getFolio().equalsIgnoreCase(folio) && o.getSerie().equalsIgnoreCase(serie) && o.isTimbrado()).findFirst().isPresent()) {
                            msg = "El comprobante " + cons.getNameXml() + "\nya habia sido timbrado anteriormente";
                            JOptionPane.showMessageDialog(null, msg);
                            Elemento.log.warn(msg);
                            fv.modificar = false;
                            
                            return;
                        } else if (listaFolios.stream().filter(o -> o.getFolio().equalsIgnoreCase(folio) && o.getSerie().equalsIgnoreCase(serie) && !o.isTimbrado()).findFirst().isPresent()) {
                            fv.modificar = true;
                        } else {
                            fv.modificar = false;
                        }
                    }else{
                        fv.modificar = false;
                    }

                    if (Elemento.checarCreditos(cons.getRfcEmisor())) {
                        if (con.timbrar(Elemento.pathXml)) {
                            util.fileMove(rootPath + name, Elemento.pathLayoutDone + name);
                            String text = "Archivo " + name + " movido a done.";
                            System.out.println(text);
                            log.info(text);

                            try {
                                Folios fol = new Folios("");
                                String fechaT = cons.getFechaTim();
                                String uuid = con.getUuid();
                                String nameXml = cons.getNameXmlTimbrado();
                                String xml = con.getXmlTimbrado();
                                String sello = cons.getSello();

                                if (lay.contains("NOMBRE_ADDENDA: Klyns")) {
                                    xml = xml.replace("</cfdi:Comprobante>", cons.getAddendaKlyns() + "</cfdi:Comprobante>");
                                    util.escribirArchivo(xml, pathXml, nameXml + ".xml");
                                }

                                Long transId = 0l;
                                Boolean tim = Boolean.TRUE;
                                fv.agregarFactura(serie, folio, rfcEmi, rfcRe, nombreRe, fecha, total, datos, layout, xml, tim, fechaT, uuid, transId, cons.getTipoComprobanteLayout());

                                this.aumentarFolio(rfcEmi, cons.getTipoComprobanteLayout());
                                this.restarCredito(rfcEmi);

                                Elemento.log.info("Se agrega el folio timbrado " + folio + " en la base de datos");
                                Elemento.log.info("Se comienza la generación del PDF...");

                                Elemento.leerConfig(rfcEmi);

                                if (leyenda.isEmpty()) {
                                    Factura_View.visualizar(pathXml, nameXml, fol.getEmail("Emisores", rfcEmi), cons.jsonDomicilios, idTipoComprobante);
                                } else {
                                    Elemento.interpretarXML(pathXml, nameXml, leyenda, destinoXml);
                                    Factura_View.visualizarInterpretado(pathXml, destinoXml, nameXml, fol.getEmail("Emisores", rfcEmi), cons.jsonDomicilios, idTipoComprobante);
                                }

                                this.crearQR(nameXml, "https://verificacfdi.facturaelectronica.sat.gob.mx/default.aspx?re=" + rfcEmi + "&rr=" + rfcRe + "&tt=" + total + "&id=" + uuid + "&fe=" + sello.substring(sello.length() - 8 , sello.length()));
                                if (!tipoComprobante.equalsIgnoreCase("N")) {
                                    openSendEmailWindow(fol, pathXml, nameXml, cons);
                                } 

                            } catch (NumberFormatException | SQLException | HeadlessException ex) {
                                ex.printStackTrace();
                                Elemento.log.error("Excepcion al crear PDF o al enviar email: " + ex.getMessage(), ex);
                            }

                        } else {
                            String fechaT = "01/01/2000 00:00:00";
                            String uuid = "";
                            String xml = "";
                            Long transId = 0l;
                            Boolean tim = Boolean.FALSE;
                            fv.agregarFactura(serie, folio, rfcEmi, rfcRe, nombreRe, fecha, total, datos, layout, xml, tim, fechaT, uuid, transId, cons.getTipoComprobanteLayout());
                            print(con.getMensajeError());
                            Elemento.log.info("Se agrega el folio no timbrado " + folio + " en la base de datos");
                            moveLayoutError(rootPath, name);
                        }
                    } else {
                        this.print("No cuenta con creditos, favor de comunicarse\nal 6672802966 o al 6672804444");
                        Elemento.log.warn("No cuenta con creditos");
                        moveLayoutError(rootPath, name);
                    }
                }

            } catch (Error err) {
                System.out.println("Error: " + err.getMessage());
                Elemento.log.error("Error al analizar el Layout: " + err.getMessage(), err);
                err.printStackTrace();
                moveLayoutError(rootPath, name);
            } catch (Exception ex) {
                Elemento.log.error("Excepcion al crear la factura: " + ex.getMessage(), ex);
                ex.printStackTrace();
                moveLayoutError(rootPath, name);
            }
        }
    }
    
    private void openSendEmailWindow(Folios fol, String pathXml, String nameXml, ConstruirXML cons) throws SQLException{
        int selec = JOptionPane.showConfirmDialog(null, "Desea enviar la factura por e-mail?", "Enviar", JOptionPane.YES_NO_OPTION);
        
        String rfcEmi = cons.getRfcEmisor();
        String emailO = fol.getEmail("Emisores", rfcEmi);

        String conf = Elemento.getMailConfiguration(emailO);

        switch (selec) {
            case JOptionPane.NO_OPTION:
                break;
            case JOptionPane.YES_OPTION:
                String args[] = new String[8];
                args[0] = pathXml + nameXml + ".xml";
                args[1] = Elemento.pathPdf + nameXml + ".pdf";
                args[2] = nameXml + ".xml";
                args[3] = nameXml + ".pdf";
                args[4] = fol.getEmail("Clientes", cons.getRfcReceptor());
                args[5] = emailO;
                args[6] = fol.getPass(rfcEmi);
                args[7] = conf;
                SendMail.main(args);
                break;
        }
    }
    
    void moveLayoutError(String rootPath, String name){
        util.fileMove(rootPath + name, Elemento.pathLayoutError + name);
        String text = "Archivo " + name + " movido a error.";
        System.out.println(text);
        log.info(text);
    }

    void print(String msg) {
        JTextArea textArea = new JTextArea();
        textArea.setColumns(120);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(msg); // A string of ~100 words "Lorem ipsum...\nFin."
        textArea.setSize(textArea.getPreferredSize().width, 1);
        JOptionPane.showMessageDialog(null, textArea, "Error al timbrar", JOptionPane.ERROR_MESSAGE);
    }

    void print(int msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    private boolean verificarCliente(String rfc) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        boolean re;

        try {
            rs = stmt.executeQuery("SELECT rfc FROM Clientes WHERE rfc = \'" + rfc + "\'");
            if (rs.next()) {
                re = true;
            } else {
                re = false;
            }
            rs.close();
            stmt.close();
            con.close();
            return re;
        } catch (Exception ex) {
            Elemento.log.error("Excepcion al verificar si existe el cliente: " + rfc, ex);
            return false;
        }
    }

    private void traerFolios(String rfcEmisor) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        Elemento.log.info("Buscando los folios registrados...");
        try {
            rs = stmt.executeQuery("SELECT nz(serie, '') as serie, nz(folio, 0) as folio, timbrado FROM Facturas WHERE rfcEmisor = '" + rfcEmisor + "'");
            List<FoliosRegistrados> listaFolios = new ArrayList();
            
            while (rs.next()) {
                FoliosRegistrados fr = new FoliosRegistrados();
                fr.setSerie(rs.getString("serie"));
                fr.setFolio(rs.getString("folio"));
                fr.setTimbrado(rs.getBoolean("timbrado"));
                /*
                emisores.add(rs.getString("rfcEmisor"));
                series.add(rs.getString("serie"));
                folios.add(rs.getString("folio"));
                timbrado.add(rs.getBoolean("timbrado"));
                */
                listaFolios.add(fr);
            }
            
            folios.put(rfcEmisor, listaFolios);
            
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private CertificadoSelloDigital traerCertificados(String rfcEmi, String regimenFiscal){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        Elemento.log.info("Buscando los certificados del RFC Emisor: " + rfcEmi);
        CertificadoSelloDigital csd = null;
        
        try {
            rs = stmt.executeQuery("SELECT pathCert, pathKey, keyPass, nocertificado FROM Cuentas WHERE rfc = '" + rfcEmi + "' AND regimenFiscal = '" + regimenFiscal + "'");
            if(rs.next()){
                csd = new CertificadoSelloDigital();
                csd.setPathCert(rs.getString("pathCert"));
                csd.setPathKey(rs.getString("pathKey"));
                csd.setKeyPass(rs.getString("keyPass"));
                csd.setNoCert(rs.getString("nocertificado"));
            }else{
                String msg = "No existe ninguna cuenta asociada al Emisor " + rfcEmi + " con Regimen Fiscal " + regimenFiscal;
                util.printError(msg);
                log.warn(msg);
            }
            
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return csd;
    }

    private void crearQR(String name, String text) throws Exception {
        File file = new File(Elemento.pathQR + name + ".png");
        Charset charset = Charset.forName("ISO-8859-1");
        CharsetEncoder encoder = charset.newEncoder();
        byte[] b = null;
        ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(text));
        b = bbuf.array();
        String data = new String(b, "ISO-8859-1");
        // get a byte matrix for the data
        BitMatrix matrix = null;
        QRCodeWriter writer = new QRCodeWriter();
        matrix = writer.encode(data, com.google.zxing.BarcodeFormat.QR_CODE, 300, 300);
        // matrix = generateVCardQRCode(null, "H");
        MatrixToImageWriter.writeToFile(matrix, "PNG", file);
    }

    private void aumentarFolio(String rfcE, String tipocfd) {
        Connection con = Elemento.odbc();
        //Statement stmtLeer = factory.stmtLectura(con);
        Statement stmtEscri = factory.stmtEscritura(con);
        //ResultSet rs;
        String nom = tipocfd.trim();
        int idComprobante = getIdComprobante(nom);
        Elemento.log.info("Se aumentara el folio actual");
        try {
            stmtEscri.executeUpdate("UPDATE Folios SET ultimo_folio = (ultimo_folio + 1) WHERE rfc = \'" + rfcE + "\' AND idComprobante = " + idComprobante);
            Elemento.log.info("Se ha actualizado el folio");

            stmtEscri.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al actualizar folio: " + e.getMessage(), e);
        }
    }

    private void restarCredito(String rfc) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        /*ResultSet rs;
        int creditosRestantes = 0;
        int creditosUsados = 0;*/
        try {
            /*rs = stmt.executeQuery("SELECT creditosRestantes,creditosUsados FROM Cuentas WHERE rfc = \'" + rfc + "\'");
            if (rs.next()) {
                creditosRestantes = rs.getInt("creditosRestantes");
                creditosUsados = rs.getInt("creditosUsados");
            }
            rs.close();

            creditosRestantes--;
            creditosUsados++;*/

            stmt.executeUpdate("UPDATE Cuentas SET creditosRestantes = (creditosRestantes-1) , creditosUsados = (creditosUsados+1) WHERE rfc = \'" + rfc + "\'");
            Elemento.log.info("Se resta credito");
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al restar creditos: " + ex.getMessage(), ex);
        }
    }

    public int getIdComprobante(String tipo) {
        int idComprobante = 0;
        switch (tipo) {
            case "I":
                idComprobante = 1;
                break;
            case "E":
                idComprobante = 2;
                break;
            case "P":
                idComprobante = 5;
                break;
            case "D":
                idComprobante = 6;
                break;
            case "N":
                idComprobante = 4;
                break;
        }
        return idComprobante;
    }

    private void agregarCliente(ConstruirXML cons) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        String email = JOptionPane.showInputDialog(null, "Ingrese el correo electronico del cliente", "Correo Electronico de Cliente", JOptionPane.INFORMATION_MESSAGE);
        try {
            stmt.execute("INSERT INTO Clientes (nombre,rfc,calle,noExterior,noInterior,colonia,localidad,municipio,estado,pais,cp,email) "
                    + "VALUES (\'" + cons.getNombreReceptor() + "\',\'" + cons.getRfcReceptor() + "\',\'" + cons.getCalleRe() + "\',\'" + cons.getNoExteriorRe() + "\',\'" + cons.getNoInteriorRe() + "\',\'" + cons.getColoniaRe() + "\',\'" + cons.getLocalidadRe() + "\',\'" + cons.getMunicipioRe() + "\',\'" + cons.getEstadoRe() + "\',\'" + cons.getPaisRe() + "\',\'" + cons.getCpRe() + "\',\'" + email.trim() + "\')");
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al agregar el cliente desde Listener", e);
        }
    }

    private class FoliosRegistrados {
        
        private String serie, folio;
        private boolean timbrado;
        
        public FoliosRegistrados() {
        }

        public String getSerie() {
            return serie;
        }

        public void setSerie(String serie) {
            this.serie = serie;
        }

        public String getFolio() {
            return folio;
        }

        public void setFolio(String folio) {
            this.folio = folio;
        }

        public boolean isTimbrado() {
            return timbrado;
        }

        public void setTimbrado(boolean timbrado) {
            this.timbrado = timbrado;
        }
        
    }
    
    private class CertificadoSelloDigital{
        private String pathCert;
        private String pathKey;
        private String keyPass;
        private String noCert;
        
        public CertificadoSelloDigital(){
            
        }
        
        public String getNoCert() {
            return noCert;
        }

        public void setNoCert(String noCert) {
            this.noCert = noCert;
        }

        public String getPathCert() {
            return pathCert;
        }

        public void setPathCert(String pathCert) {
            this.pathCert = pathCert;
        }

        public String getPathKey() {
            return pathKey;
        }

        public void setPathKey(String pathKey) {
            this.pathKey = pathKey;
        }

        public String getKeyPass() {
            return keyPass;
        }

        public void setKeyPass(String keyPass) {
            this.keyPass = keyPass;
        }
        
        
    }
}
