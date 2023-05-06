/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import elemento.Elemento;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import reportes.*;

/**
 *
 * @author Abe
 */
public class ReporteadorView extends javax.swing.JFrame {

    /**
     * Creates new form ReporteadorView
     */
    public ReporteadorView() {
        initComponents();
        this.setLocationRelativeTo(null);
        llenarRfcs();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        comboDesdeMes = new javax.swing.JComboBox();
        comboDesdeAno = new javax.swing.JComboBox();
        comboHastaMes = new javax.swing.JComboBox();
        comboHastaAno = new javax.swing.JComboBox();
        ejecutar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        rfcEmisor = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        rfcReceptor = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("REPORTES");

        jLabel1.setText("Desde");

        jLabel2.setText("Hasta");

        comboDesdeMes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" }));

        comboDesdeAno.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027" }));
        comboDesdeAno.setSelectedIndex(7);

        comboHastaMes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" }));

        comboHastaAno.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025", "2026", "2027" }));
        comboHastaAno.setSelectedIndex(7);

        ejecutar.setText("Ejecutar");
        ejecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ejecutarActionPerformed(evt);
            }
        });

        jLabel3.setText("Cliente");

        jLabel4.setText("Emisor");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboDesdeMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboDesdeAno, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rfcEmisor, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(comboHastaMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboHastaAno, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(rfcReceptor, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ejecutar)
                .addGap(184, 184, 184))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(comboDesdeMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboDesdeAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboHastaMes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboHastaAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(rfcEmisor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(rfcReceptor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ejecutar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ejecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ejecutarActionPerformed
        // TODO add your handling code here:
        utils.Utils util = new utils.Utils(elemento.Elemento.log);
        Connection con = elemento.Elemento.odbc();
        Statement stmt = this.stmtEscritura(con);
        ResultSet rs;

        int desdeMes = comboDesdeMes.getSelectedIndex() + 1;
        int hastaMes = comboHastaMes.getSelectedIndex() + 1;
        int desdeAno = Integer.parseInt(comboDesdeAno.getSelectedItem().toString());
        int hastaAno = Integer.parseInt(comboHastaAno.getSelectedItem().toString());
        Comprobante comp;
        Emisor emi;
        Receptor rec;
        List<Comprobante> lista = new ArrayList();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String desde, hasta;

        if (desdeMes < 10) {
            desde = "01/0" + desdeMes + "/" + desdeAno;
        } else {
            desde = "01/" + desdeMes + "/" + desdeAno;
        }

        int day = this.diaMaximo(hastaMes, hastaAno);

        if (hastaMes < 10) {
            hasta = day + "/0" + hastaMes + "/" + hastaAno;
        } else {
            hasta = day + "/" + hastaMes + "/" + hastaAno;
        }

        java.util.Date dateDesde = null;
        java.util.Date dateHasta = null;

        try {
            dateDesde = format.parse(desde);
            dateHasta = format.parse(hasta);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        java.sql.Date fechaDesde = new java.sql.Date(dateDesde.getTime());
        java.sql.Date fechaHasta = new java.sql.Date(dateHasta.getTime());
        String re = rfcEmisor.getSelectedItem().toString().trim();
        String rr = rfcReceptor.getSelectedItem().toString().trim();

        try {
            rs = stmt.executeQuery("SELECT * FROM Facturas WHERE rfcEmisor like \'%" + re + "%\' AND rfc like \'%" + rr + "%\' AND fecha_timbrado BETWEEN #" + fechaDesde + "# AND #" + fechaHasta + "# ORDER BY fecha_timbrado DESC, folio DESC");
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                String xmlString = rs.getString("xml");
                if (rs.getBoolean("timbrado") && !(xmlString == null || xmlString.trim().isEmpty())) {
                    comp = new Comprobante();
                    emi = new Emisor();
                    rec = new Receptor();

                    emi.setRfc(rs.getString("rfcEmisor"));
                    rec.setNombre(rs.getString("nombre"));
                    rec.setRfc(rs.getString("rfc"));

                    comp.setEmisor(emi);
                    comp.setReceptor(rec);
                    comp.setFechaTimbrado(rs.getDate("fecha_timbrado"));
                    comp.setFolio("" + rs.getInt("folio"));
                    comp.setSerie(rs.getString("serie"));
                    comp.setTipoDeComprobante("Factura");
                    comp.setTotal("" + rs.getDouble("total"));
                    comp.setStatus(rs.getString("status"));
                    comp.setUuid(rs.getString("uuid"));

                    String name = comp.getSerie() + "_" + comp.getFolio() + "_" + emi.getRfc() + "_" + rec.getRfc() + "_" + comp.getUuid();
                    File xml = new File(Elemento.pathXml + name + ".xml");
                    //int cont = 0;
                    while (!xml.exists()) {
//                        if (cont == 1) {
                            util.escribirArchivo(xmlString, Elemento.pathXml, name + ".xml");
//                        } else {
//                            cont++;
//                        }
                    }
                    
                    try {
                        comp.setTotalTraslados(getDatoComprobante("totalImpuestosTrasladados", xml));
                        comp.setSubtotal(getDatoComprobante("subTotal", xml));

                        lista.add(comp);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            rs.close();
            stmt.close();
            con.close();

            crearReporte(lista, dateDesde, dateHasta);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }//GEN-LAST:event_ejecutarActionPerformed

    private String getDatoComprobante(String dato, File x) throws Exception {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(x);

        doc.getDocumentElement().normalize();
        NodeList comprobante = doc.getElementsByTagName("cfdi:Comprobante");
        NodeList impuestos = doc.getElementsByTagName("cfdi:Impuestos");

        Node nodoComprobante = comprobante.item(0);
        Node nodoImpuestos = impuestos.item(0);

        Element elementoComprobante = (Element) nodoComprobante;
        Element elementoImpuestos = (Element) nodoImpuestos;

        switch (dato) {
            case "totalImpuestosTrasladados":
                return getTagValue(dato, elementoImpuestos);
            default:
                return getTagValue(dato, elementoComprobante);
        }

    }

    private static String getTagValue(String sTag, Element eElement) {
        String valor = eElement.getAttribute(sTag);
        return valor;
    }

    private int diaMaximo(int mes, int ano) {
        int day = 0;
        switch (mes) {
            case 1:
                day = 31;
                break;
            case 2:
                if (ano == 2012 || ano == 2016 || ano == 2020) {
                    day = 29;
                } else {
                    day = 28;
                }
                break;
            case 3:
                day = 31;
                break;
            case 4:
                day = 30;
                break;
            case 5:
                day = 31;
                break;
            case 6:
                day = 30;
                break;
            case 7:
                day = 31;
                break;
            case 8:
                day = 31;
                break;
            case 9:
                day = 30;
                break;
            case 10:
                day = 31;
                break;
            case 11:
                day = 30;
                break;
            case 12:
                day = 31;
                break;
        }

        return day;
    }

    private void crearReporte(List<Comprobante> lista, Date desde, Date hasta) throws Exception {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fecha;
        String fechaDesde = format.format(desde);
        String fechaHasta = format.format(hasta);
        double totalReporte = 0.0;
        BigDecimal totalizado;
        String linea = "----------------------------------------------------------------------------------------------------------------------------------------------\r\n";

        sb.append("\t\t\t*****REPORTE DE COMPROBANTES DESDE " + fechaDesde + " HASTA " + fechaHasta + "*****\r\n\r\n\r\n");
        sb.append(linea);
        sb.append("Serie\t\tFolio\t\tRFC Emisor\tRFC Receptor\tEstado\t\tFecha\t\tSubTotal\tIVA\t\tTotal\r\n");
        sb.append(linea);

        for (Comprobante comp : lista) {
            double total = Double.parseDouble(comp.getTotal());
            totalReporte += total;
            fecha = format.format(comp.getFechaTimbrado());
            String status = comp.getStatus();
            String tabs, tabsFolio, tabsSub;
            String folio = comp.getFolio();
            String subtotal = comp.getSubtotal();
            String iva = comp.getTotalTraslados();

            if (folio.length() > 7) {
                tabsFolio = "\t";
            } else {
                tabsFolio = "\t\t";
            }

            if (status.equalsIgnoreCase("VIGENTE")) {
                tabs = "\t\t";
            } else {
                tabs = "\t";
            }

            if (subtotal.length() >= 8) {
                tabsSub = "\t";
            } else {
                tabsSub = "\t\t";
            }

            sb.append(comp.getSerie() + "\t\t" + folio + tabsFolio + comp.getEmisor().getRfc() + "\t" + comp.getReceptor().getRfc() + "\t" + status + tabs + fecha + "\t" + subtotal + tabsSub + iva + "\t\t" + total + "\r\n");
        }
        totalizado = new BigDecimal(totalReporte);
        totalizado = totalizado.setScale(2, RoundingMode.HALF_UP);
        sb.append(linea);

        sb.append("TOTAL FACTURADO: " + totalizado.toString());
        String mesDesde = comboDesdeMes.getSelectedItem().toString();
        String anoDesde = comboDesdeAno.getSelectedItem().toString();
        String mesHasta = comboHastaMes.getSelectedItem().toString();
        String anoHasta = comboHastaAno.getSelectedItem().toString();

        String path = "C:\\Facturas\\";
        String name = "reporte " + mesDesde + "-" + anoDesde + " al " + mesHasta + "-" + anoHasta + ".txt";

        escribirArchivo(sb.toString(), path, name);
        //System.out.println(sb.toString());
        utils.Exe exe = new utils.Exe(elemento.Elemento.log);
        exe.exeSinTiempo(path + name);

    }

    private void escribirArchivo(String text, String path, String name) throws Exception {
        FileOutputStream file = new FileOutputStream(path + name);
        OutputStreamWriter fw = new OutputStreamWriter(file, "UTF8");
        Writer bw = new BufferedWriter(fw);
        bw.write(text);

        bw.close();
        fw.close();
    }

    private Statement stmtEscritura(Connection con) {
        try {
            return con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (SQLException ex) {
            elemento.Elemento.log.error("Excepcion: No se pudo crear el Statement de solo lectura: " + ex.getLocalizedMessage());
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ReporteadorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReporteadorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReporteadorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReporteadorView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox comboDesdeAno;
    private javax.swing.JComboBox comboDesdeMes;
    private javax.swing.JComboBox comboHastaAno;
    private javax.swing.JComboBox comboHastaMes;
    private javax.swing.JButton ejecutar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JComboBox rfcEmisor;
    private javax.swing.JComboBox rfcReceptor;
    // End of variables declaration//GEN-END:variables

    private void llenarRfcs() {
        Connection con = elemento.Elemento.odbc();
        Statement stmt = this.stmtEscritura(con);
        ResultSet rs, rs2;
        DefaultComboBoxModel comboE = (DefaultComboBoxModel) rfcEmisor.getModel();
        DefaultComboBoxModel comboR = (DefaultComboBoxModel) rfcReceptor.getModel();

        try {
            rs = stmt.executeQuery("SELECT rfc FROM Emisores");
            comboE.addElement("");
            while (rs.next()) {
                comboE.addElement(rs.getString("rfc"));
            }
            rs.close();

            rs2 = stmt.executeQuery("SELECT rfc FROM Clientes");
            comboR.addElement("");
            while (rs2.next()) {
                comboR.addElement(rs2.getString("rfc"));
            }
            rs2.close();

            stmt.close();
            con.close();
        } catch (Exception e) {
        }
    }
}
