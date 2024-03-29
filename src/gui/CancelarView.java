/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import conectordf.ConectorDF;
import elemento.Elemento;
import elemento.Exe;
import java.awt.HeadlessException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import pagos.Documento;

/**
 *
 * @author Abe
 */
public class CancelarView extends javax.swing.JFrame {

    /**
     * Creates new form CancelarView
     */
    utils.Utils util = new utils.Utils(Elemento.log);
    utils.ConnectionFactory factory = new utils.ConnectionFactory(Elemento.log);
    String rfcE, rfcR, uu, total;
    String folio, xml, logo, serie;
    Long trans;
    private Folios fol = null;
    private String uuidRelacionado = null;

    public CancelarView(String folio, String xml, String logo, String uu, String rfcE, String rfcR, Long trans, String total, String serie) {
        this.rfcE = rfcE;
        this.rfcR = rfcR;
        this.folio = folio;
        this.xml = xml;
        this.logo = logo;
        this.uu = uu;
        this.trans = trans;
        this.total = total;
        this.serie = serie;

        initComponents();
        this.panelInfo.setVisible(false);
        this.lblComprobanteSel.setVisible(false);
        this.separador.setVisible(false);
        setLocationRelativeTo(null);
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
        quienCancela = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cancelar = new javax.swing.JButton();
        motivoCancelacion = new javax.swing.JComboBox<>();
        lblComprobanteSel = new javax.swing.JLabel();
        separador = new javax.swing.JSeparator();
        panelInfo = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblEmisor = new javax.swing.JLabel();
        lblReceptor = new javax.swing.JLabel();
        lblSerie = new javax.swing.JLabel();
        lblFolio = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblUuid = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        jLabel1.setText("Quien Cancela? ");

        quienCancela.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quienCancelaActionPerformed(evt);
            }
        });

        jLabel2.setText("Motivo de Cancelación");

        cancelar.setText("Cancelar CFDi");
        cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });

        motivoCancelacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Seleccione un motivo de cancelación --", "01 - Comprobante emitido con errores y con relación", "02 - Comprobante emitido con errores y sin relación", "03 - No se llevó a cabo la operación", "04 - Operación nominativa relacionada en una factura global" }));
        motivoCancelacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                motivoCancelacionActionPerformed(evt);
            }
        });

        lblComprobanteSel.setText("Comprobante Relacionado");

        panelInfo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Emisior");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Receptor");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Serie");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Folio");

        lblEmisor.setText(" ");

        lblReceptor.setText(" ");

        lblSerie.setText(" ");

        lblFolio.setText(" ");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("UUID");

        lblUuid.setText(" ");

        javax.swing.GroupLayout panelInfoLayout = new javax.swing.GroupLayout(panelInfo);
        panelInfo.setLayout(panelInfoLayout);
        panelInfoLayout.setHorizontalGroup(
            panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInfoLayout.createSequentialGroup()
                        .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblEmisor, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                            .addComponent(lblReceptor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
                        .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelInfoLayout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelInfoLayout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblSerie, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(58, 58, 58))
                    .addGroup(panelInfoLayout.createSequentialGroup()
                        .addComponent(lblUuid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        panelInfoLayout.setVerticalGroup(
            panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblEmisor)
                    .addComponent(jLabel6)
                    .addComponent(lblSerie))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblReceptor)
                    .addComponent(jLabel7)
                    .addComponent(lblFolio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lblUuid))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(202, 202, 202)
                .addComponent(cancelar)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(motivoCancelacion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(quienCancela))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lblComprobanteSel)
                        .addGap(5, 5, 5)
                        .addComponent(separador))
                    .addComponent(panelInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(quienCancela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(motivoCancelacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblComprobanteSel)
                    .addComponent(separador, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelar)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void quienCancelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quienCancelaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_quienCancelaActionPerformed

    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed
        // TODO add your handling code here:
        String texto = "";
        String ruta = Elemento.pathXml;
        String nameXml = xml + ".xml";
        String namePdf = xml + "_CANCELADA";
        String pathXml = ruta + nameXml;
        String pathPdf = Elemento.pathPdf;
        String motivo = motivoCancelacion.getSelectedItem().toString();
        String motivoClave = motivo.split("-")[0].trim();

        Elemento.leerConfig(rfcE);
        try {
            if (validar()) {
                String email = getEmail("Emisores", rfcE);
                String mensaje = "Se mando a cancelar la factura con folio: " + serie + folio
                        + "\r\nPor la persona: " + quienCancela.getText().trim()
                        + "\r\nPor el motivo: " + motivo;

                ConectorDF con = new ConectorDF(Elemento.produccion, Elemento.user, Elemento.pass, Elemento.log, Elemento.unidad);
                String respuesta = con.cancelarCfdi(rfcE, rfcR, total, uu, pathXml, nameXml, motivoClave, uuidRelacionado);

                JsonObject jsonRes = JsonParser.parseString(respuesta).getAsJsonObject();
                Boolean isCancelled = jsonRes.get("isCancelled").getAsBoolean();
                Boolean isRequested = jsonRes.get("isRequested").getAsBoolean();
                String estatus = jsonRes.get("estatus").getAsString();
                String msg = jsonRes.get("mensaje").getAsString();

                if (isCancelled) {
                    texto = "Se ha cancelado el comprobante con serie y folio: " + serie + folio;
                    Elemento.restarCredito(rfcE);
                    cambiarEstadoFactura(uu, null);
                    this.generarPdfCancelado(pathXml, pathPdf, namePdf, logo);
                    //new File(pathXml).renameTo(new File(ruta+namePdf+".xml"));
                    util.enviarEmail("", "", email, mensaje, pathXml, pathPdf + namePdf + ".pdf", nameXml, namePdf + ".pdf", "");
                    Exe.exeSinTiempo(pathPdf + namePdf + ".pdf");
                } else if (isRequested) {
                    texto = msg;
                    cambiarEstadoFactura(uu, estatus);
                } else {
                    texto = "No se pudo cancelar el comprobante:" + msg;
                }

                Elemento.log.info(texto);
                JOptionPane.showMessageDialog(null, texto);
            } else {

            }
        } catch (SQLException | HeadlessException | IOException | NullPointerException ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al cancelar: " + ex.getMessage() + " - Texto de Respuesta: " + texto, ex);
        } catch (Exception ex) {
            Elemento.log.error("Excepcion al cancelar: ", ex);
        }
    }//GEN-LAST:event_cancelarActionPerformed

    private boolean validar() {
        if (quienCancela.getText().trim().isEmpty()) {
            util.printError("Debe ingresar quien esta cancelando el comprobante");
            return false;
        }
        if (motivoCancelacion.getSelectedIndex() == 0) {
            util.printError("Debe de elegir un motivo de cancelación válido");
            return false;
        }
        if (motivoCancelacion.getSelectedIndex() == 1 && this.uuidRelacionado == null) {
            util.printError("Debes relacionar este comprobante con el que lo sustituyo");
            return false;
        }

        return true;
    }

    private void motivoCancelacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_motivoCancelacionActionPerformed
        // TODO add your handling code here:
        switch (motivoCancelacion.getSelectedIndex()) {
            case 1:
                this.uuidRelacionado = null;
                this.lblEmisor.setText("");
                this.lblReceptor.setText("");
                this.lblSerie.setText("");
                this.lblFolio.setText("");
                this.lblUuid.setText("");
                fol = new Folios(rfcE, rfcR, this);
                fol.setVisible(true);
                break;
            case 0:
                this.uuidRelacionado = null;
                this.panelInfo.setVisible(false);
                this.lblComprobanteSel.setVisible(false);
                this.separador.setVisible(false);
                break;
            default:
                this.uuidRelacionado = null;
                this.panelInfo.setVisible(false);
                this.lblComprobanteSel.setVisible(false);
                this.separador.setVisible(false);
                break;
        }
    }//GEN-LAST:event_motivoCancelacionActionPerformed

    //Obteniene el email de la base de datos si es que se encuentra
    private String getEmail(String tabla, String rfcr) throws SQLException {
        Connection con = Elemento.odbc();
        Statement stmt;
        ResultSet rs;
        if (con != null) {
            stmt = factory.stmtLectura(con);
            rs = stmt.executeQuery("SELECT email FROM " + tabla + " WHERE rfc like \'" + rfcr + "\'");
            if (rs.next()) {
                String tmp = rs.getString("email");
                rs.close();
                stmt.close();
                con.close();
                if (tmp != null) {
                    return tmp.trim();
                } else {
                    return "";
                }
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    private void cambiarEstadoFactura(String uu, String estatus) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        String strEstatus = estatus != null ? estatus : "CANCELADA";

        try {
            stmt.executeUpdate("UPDATE Facturas SET status=\'" + strEstatus + "\' WHERE uuid like \'" + uu + "\'");
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al cambiar el status de la factura " + serie + folio + " a Cancelado", ex);
        }
    }

    private void generarPdfCancelado(String pathXml, String pathPdf, String name, String logo) {
        try {
            util.crearPdfCancelado(pathXml, pathPdf, name, logo);
        } catch (Exception ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al generar el PDF Cancelado", ex);
        }
    }

    public void setUuidRelacionado(Documento doc) {
        this.uuidRelacionado = doc.getUuid();
        if (this.fol != null) {
            fol.dispose();
        }
        this.lblEmisor.setText(doc.getRfcEmisor());
        this.lblReceptor.setText(doc.getRfcReceptor());
        this.lblSerie.setText(doc.getSerie());
        this.lblFolio.setText(doc.getFolio());
        this.lblUuid.setText(doc.getUuid());
        this.panelInfo.setVisible(true);
        this.lblComprobanteSel.setVisible(true);
        this.separador.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String args[]) {
        /* Set the Windows look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Windows (introduced in Java SE 6) is not available, stay with the default look and feel.
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
            java.util.logging.Logger.getLogger(CancelarView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CancelarView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CancelarView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CancelarView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        final Long tr = Long.parseLong(args[6]);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CancelarView(args[0], args[1], args[2], args[3], args[4], args[5], tr, args[7], args[8]).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel lblComprobanteSel;
    private javax.swing.JLabel lblEmisor;
    private javax.swing.JLabel lblFolio;
    private javax.swing.JLabel lblReceptor;
    private javax.swing.JLabel lblSerie;
    private javax.swing.JLabel lblUuid;
    private javax.swing.JComboBox<String> motivoCancelacion;
    private javax.swing.JPanel panelInfo;
    private javax.swing.JTextField quienCancela;
    private javax.swing.JSeparator separador;
    // End of variables declaration//GEN-END:variables
}
