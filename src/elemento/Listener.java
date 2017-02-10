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
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import net.contentobjects.jnotify.JNotifyListener;

/**
 *
 * @author Abe
 */
public class Listener implements JNotifyListener {

    utils.Utils util = new utils.Utils(Elemento.log);
    utils.ConnectionFactory factory = new utils.ConnectionFactory(Elemento.log);
    List<String> emisores = new ArrayList();
    List<String> folios = new ArrayList();
    List<String> series = new ArrayList();
    List<Boolean> timbrado = new ArrayList();

    public Listener() {
        Elemento.log.info("Buscando layout creado...");
    }

    @Override
    public void fileRenamed(int wd, String rootPath, String oldName,
            String newName) {
        print("renamed " + rootPath + " : " + oldName + " -> " + newName);
    }

    @Override
    public void fileModified(int wd, String rootPath, String name) {
        print("modified " + rootPath + " : " + name);
    }

    @Override
    public void fileDeleted(int wd, String rootPath, String name) {
        print("deleted " + rootPath + " : " + name);
    }

    @Override
    public void fileCreated(int wd, String rootPath, String name) {
        Elemento.log.info("Layout Detectado: " + name);
        try {
            this.procesar(rootPath, name);
        } catch (Exception ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion: Ocurrio un problema al timbrar el comprobante: " + ex.getMessage(), ex);
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
        traerFolios();

        if (name.toUpperCase().contains(".XML")) {
            //ConectorFES con = new ConectorFES(true, Elemento.user, Elemento.pass, util.leerXml(rootPath + name), Elemento.log);
            ConectorDF con = new ConectorDF(true, Elemento.user, Elemento.pass, util.leerXml(rootPath + name), Elemento.log);
            String pathXml = Elemento.pathXml;
            if (con.timbrar(Elemento.pathXml)) {
                try {
                    Factura_View.visualizar(pathXml, name);
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
                //ConectorSP con = new ConectorSP(Elemento.log, lay);
                ConectorDF con = new ConectorDF(Elemento.produccion, Elemento.user, Elemento.pass, rootPath + name, Elemento.log, Elemento.unidad, false, Elemento.estructuraNombre);
                ConstruirXML cons = con.getObjXml();
                Factura_View fv = new Factura_View("");
                cons.setNoCertificado(Elemento.noCertificado);

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

                if (lay.get(0).equalsIgnoreCase("PREFACTURA")) {
                    Factura_View.visualizar(Elemento.pathXmlST, cons.getNameXml());
                    String fechaT = "01/01/2000 00:00:00";
                    String uuid = "";
                    String xml = "";
                    Long transId = 0l;
                    Boolean tim = Boolean.FALSE;
                    fv.agregarFactura(serie, folio, rfcEmi, rfcRe, nombreRe, fecha, total, datos, layout, xml, tim, fechaT, uuid, transId, cons.getTipoComprobanteLayout());
                    Elemento.log.info("Se agrega PREFACTURA con el folio " + folio + " en la base de datos");
                } else {
                    String tipoComprobante = cons.getTipoComprobanteLayout();

                    fv.consultar("Folio", "SELECT * FROM Folios WHERE rfc like \'" + cons.getRfcEmisor() + "\' AND idComprobante = " + fv.getIdComprobante(tipoComprobante));

                    //con.crearLayout(layout, name);
                    if (tipoComprobante.equalsIgnoreCase("recibo de nomina")) {
                        if (!verificarCliente(rfcRe)) {
                            agregarCliente(cons);
                        }
                    }
                    String msg = null;

                    if (folios.contains(folio)) {
                        int index = folios.indexOf(folio);
                        String ser = series.get(index);
                        String rf = emisores.get(index);
                        Boolean tim = timbrado.get(index);

                        if (ser.equalsIgnoreCase(serie) && rf.equalsIgnoreCase(rfcEmi) && tim == Boolean.FALSE) {
                            modificar = true;
                        } else if (ser.equalsIgnoreCase(serie) && rf.equalsIgnoreCase(rfcEmi) && tim == Boolean.TRUE) {
                            msg = "El comprobante " + cons.getNameXml() + "\nya habia sido timbrado anteriormente";
                            JOptionPane.showMessageDialog(null, msg);
                            Elemento.log.warn(msg);
                            modificar = false;
                        } else {
                            modificar = false;
                        }
                    } else {
                        modificar = false;
                    }

                    fv.modificar = modificar;
                    if (msg == null) {
                        if (Elemento.checarCreditos(cons.getRfcEmisor())) {
                            if (con.timbrar(Elemento.pathXml)) {
                                try {
                                    Folios fol = new Folios("");
                                    String pathXml = Elemento.pathXml;
                                    String nameXml = cons.getNameXmlTimbrado();
                                    String fechaT = cons.getFechaTim();
                                    String uuid = con.getUuid();
                                    String xml = con.getXmlTimbrado();
                                    
                                    if(lay.contains("NOMBRE_ADDENDA: Klyns")){
                                        xml = xml.replace("</cfdi:Comprobante>", cons.getAddendaKlyns() + "</cfdi:Comprobante>");
                                        util.escribirArchivo(xml, pathXml, nameXml+".xml");
                                    }
                                    
                                    Long transId = 0l;
                                    Boolean tim = Boolean.TRUE;
                                    fv.agregarFactura(serie, folio, rfcEmi, rfcRe, nombreRe, fecha, total, datos, layout, xml, tim, fechaT, uuid, transId, cons.getTipoComprobanteLayout());
                                    
                                    this.aumentarFolio(cons.getFolio(), rfcEmi, cons.getTipoComprobanteLayout());
                                    this.restarCredito(rfcEmi);

                                    Elemento.log.info("Se agrega el folio timbrado " + folio + " en la base de datos");
                                    Elemento.log.info("Se comienza la generación del PDF...");

                                    Elemento.leerConfig(rfcEmi);

                                    if (leyenda.isEmpty()) {
                                        Factura_View.visualizar(pathXml, nameXml, fol.getEmail("Emisores", rfcEmi));
                                    } else {
                                        String destinoXml = Elemento.unidad + ":\\Facturas\\XmlModificados\\";
                                        Elemento.interpretarXML(pathXml, nameXml, leyenda, destinoXml);
                                        Factura_View.visualizarInterpretado(pathXml, destinoXml, nameXml, fol.getEmail("Emisores", rfcEmi));
                                    }
                                    
                                    this.crearQR(nameXml, "?re=" + rfcEmi + "&rr=" + rfcRe + "&tt=" + total + "&id=" + uuid);
                                    if(!cons.getTipoComprobanteLayout().equalsIgnoreCase("recibo de nomina")){
                                        int selec = JOptionPane.showConfirmDialog(null, "Desea enviar la factura por e-mail?", "Enviar", JOptionPane.YES_NO_OPTION);
                                        
                                        switch (selec) {
                                            case JOptionPane.NO_OPTION:
                                                break;
                                            case JOptionPane.YES_OPTION:
                                                String args[] = new String[7];
                                                args[0] = pathXml + nameXml + ".xml";
                                                args[1] = Elemento.pathPdf + nameXml + ".pdf";
                                                args[2] = nameXml + ".xml";
                                                args[3] = nameXml + ".pdf";
                                                args[4] = fol.getEmail("Clientes", cons.getRfcReceptor());
                                                args[5] = fol.getEmail("Emisores", rfcEmi);
                                                args[6] = fol.getPass(rfcEmi);
                                                SendMail.main(args);
                                                break;
                                        }
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
                            }
                        } else {
                            this.print("No cuenta con creditos, favor de comunicarse\nal 6672802966 o al 6672804444");
                            Elemento.log.warn("No cuenta con creditos");
                        }
                    }
                }

            } catch (Error err) {
                System.out.println("Error: " + err.getMessage());
                Elemento.log.error("Error al analizar el Layout: " + err.getMessage(), err);
                err.printStackTrace();
            } catch (Exception ex) {
                Elemento.log.error("Excepcion al crear la factura: " + ex.getMessage(), ex);
                ex.printStackTrace();
            }
        }
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
            rs = stmt.executeQuery("SELECT rfc FROM Clientes WHERE rfc like \'" + rfc + "\'");
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

    private void traerFolios() {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        Elemento.log.info("Buscando los folios registrados...");
        try {
            rs = stmt.executeQuery("SELECT rfcEmisor,serie,folio,timbrado FROM Facturas");
            while (rs.next()) {
                emisores.add(rs.getString("rfcEmisor"));
                series.add(rs.getString("serie"));
                folios.add(rs.getString("folio"));
                timbrado.add(rs.getBoolean("timbrado"));
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    private void aumentarFolio(int folio, String rfcE, String tipocfd) {
        Connection con = Elemento.odbc();
        Statement stmtLeer = factory.stmtLectura(con);
        Statement stmtEscri = factory.stmtEscritura(con);
        ResultSet rs;
        String nom = tipocfd.trim();
        int idComprobante = getIdComprobante(nom);

        try {
            rs = stmtLeer.executeQuery("SELECT * FROM Folios WHERE rfc like \'" + rfcE + "\' AND idComprobante = " + idComprobante);
            rs.next();
            int folioActual = rs.getInt("ultimo_folio");
            rs.close();
            stmtLeer.close();

            if (folio == folioActual) {
                folio++;
                stmtEscri.executeUpdate("UPDATE Folios SET ultimo_folio = \'" + folio + "\' WHERE rfc like \'" + rfcE + "\' AND idComprobante like \'" + idComprobante + "\'");
                Elemento.log.info("Se ha actualizado al folio: " + folio);
            } else {
                if (folioActual == (folio - 1)) {
                    stmtEscri.executeUpdate("UPDATE Folios SET ultimo_folio = \'" + folio + "\' WHERE rfc like \'" + rfcE + "\' AND idComprobante = \'" + idComprobante + "\'");
                    Elemento.log.info("Se ha actualizado al folio: " + folio);
                }
            }

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
        ResultSet rs;
        int creditosRestantes = 0;
        int creditosUsados = 0;
        try {
            rs = stmt.executeQuery("SELECT creditosRestantes,creditosUsados FROM Cuentas WHERE rfc like \'" + rfc + "\'");
            if (rs.next()) {
                creditosRestantes = rs.getInt("creditosRestantes");
                creditosUsados = rs.getInt("creditosUsados");
            }
            rs.close();

            creditosRestantes--;
            creditosUsados++;

            stmt.executeUpdate("UPDATE Cuentas SET creditosRestantes=" + creditosRestantes + ", creditosUsados=" + creditosUsados + " WHERE rfc like \'" + rfc + "\'");
            stmt.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getIdComprobante(String tipo) {
        int idComprobante = 0;
        switch (tipo.toLowerCase()) {
            case "factura":
                idComprobante = 1;
                break;
            case "nota de credito":
                idComprobante = 2;
                break;
            case "recibo de donativos":
                idComprobante = 3;
                break;
            case "recibo de nomina":
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
}
