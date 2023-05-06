/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nominas.configuracion;

import elemento.Elemento;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;

/**
 *
 * @author Abe
 */
public class NominaCorreos extends javax.swing.JFrame {

    /**
     * Creates new form NominaCorreos
     * @param emailO
     * @param passO
     * @param recs
     * @param pathXmls
     * @param pathPdfs
     * @param nameXmls
     * @param namePdfs
     */
    
    String emailO, passO;
    List<String> recs;
    String[] pathXmls, pathPdfs, nameXmls, namePdfs;
    
    public NominaCorreos(String emailO, String passO, List<String> recs, String[] pathXmls, String[] pathPdfs, String[] nameXmls, String[] namePdfs) {
        initComponents();
        this.setLocationRelativeTo(null);
        
        this.emailO = emailO;
        this.passO = passO;
        this.recs = recs;
        this.pathXmls = pathXmls;
        this.pathPdfs = pathPdfs;
        this.nameXmls = nameXmls;
        this.namePdfs = namePdfs;
    }
    
    private void enviar(){
        int contE = 0;
        int contN = 0;
        
        recibosEnviados.setText(""+contE);
        recibosSinEnviar.setText(""+contN);
        progreso.setMaximum(pathXmls.length);
        progreso.setMinimum(0);
        String conf = Elemento.getMailConfiguration(emailO);
        
        for (int i = 0; i < pathXmls.length; i++) {
            try {
                enviarEmail(emailO, passO, getEmail("EmpleadosRec", recs.get(i)), "Envio de Recibo de Nomina", pathXmls[i], pathPdfs[i], nameXmls[i], namePdfs[i], conf);
                contE++;
                recibosEnviados.setText(""+contE);
            } catch (Exception ex) {
                ex.printStackTrace();
                contN++;
                recibosSinEnviar.setText(""+contN);
            }
            progreso.setValue(i+1);
            progreso.repaint();
        }
    }
    
    private String getEmail(String tabla, String rfcr) { //Obteniene el email de la base de datos si es que se encuentra
        Connection con = Elemento.odbc();
        Statement stmt;
        ResultSet rs;
        if (con != null) {
            try {
                stmt = con.createStatement();
                rs = stmt.executeQuery("SELECT email FROM " + tabla + " WHERE rfc like \'" + rfcr + "\'");
                if (rs.next()) {
                    String tmp = rs.getString("email");
                    if (tmp == null) {
                        tmp = "";
                    }
                    rs.close();
                    stmt.close();
                    con.close();
                    return tmp.trim();
                } else {
                    return "";
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Elemento.log.error("Excepcion al obtener el correo del empleado: " + rfcr, ex);
                return "";
            }
        } else {
            return "";
        }
    }
    
    private void enviarEmail(String emailO, String passO, String email, String mensaje, String pathXml, String pathPdf, String nameXml, String namePdf, String conf) throws AddressException, MessagingException {
        //Envia un mail con multiples XMLs y PDFs
        if (emailO.trim().isEmpty() || passO.trim().isEmpty()) {
            emailO = "envioid@gmail.com";
            passO = "facturacionelectronica";
        }

            Properties props = new Properties();

            String[] correos;
            if (email.contains(",")) {
                correos = email.split(",");
            } else {
                correos = new String[1];
                correos[0] = email;
            }

            InternetAddress[] mails = new InternetAddress[correos.length];

            for (int i = 0; i < correos.length; i++) {
                mails[i] = new InternetAddress(correos[i].trim());
            }

            if(conf.trim().isEmpty()){
                if (emailO.toLowerCase().contains("hotmail") || emailO.toLowerCase().contains("live") || emailO.toLowerCase().contains("outlook") || emailO.toLowerCase().contains("feimpresoresdigitales") || emailO.toLowerCase().contains("prisinaloa.org.mx")) {
                    props.put("mail.smtp.host", "smtp.live.com");
                } else {
                    // Nombre del host de correo, es smtp.gmail.com
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    //SSL si esta disponible
                    props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
                }
                // Puerto de gmail para envio de correos
                props.put("mail.smtp.port", "587");
                
                // TLS si está disponible
                props.put("mail.smtp.starttls.enable", "true");
            }else{
                String cc [] = conf.split("\\|");
                props.put("mail.smtp.host", cc[0]);
                props.put("mail.smtp.ssl.trust", cc[0]);
                props.put("mail.smtp.port", cc[1]);
                props.put("mail.smtp.starttls.enable", cc[2]);
            }

            // Si requiere o no usuario y password para conectarse.
            props.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props);

            // Para obtener un log de salida más extenso
            session.setDebug(true);

            //Primero construimos la parte de texto
            BodyPart texto = new MimeBodyPart();
            // Texto del mensaje
            String aviso = "\r\n\r\n******FAVOR DE NO RESPONDER ESTE MAIL******";
            texto.setText(mensaje + aviso);

            //Luego construimos la parte del adjunto con el xml y el pdf
            BodyPart xml;
            BodyPart pdf;
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(texto);

                xml = new MimeBodyPart();
                pdf = new MimeBodyPart();

                xml.setDataHandler(new DataHandler(new FileDataSource(pathXml)));
                xml.setFileName(nameXml);
                pdf.setDataHandler(new DataHandler(new FileDataSource(pathPdf)));
                pdf.setFileName(namePdf);

                //Ahora juntamos ambas en una sola parte que luego debemos añadir al mensaje
                multiParte.addBodyPart(xml);
                multiParte.addBodyPart(pdf);

            //Finalmente construimos el mensaje, le ponemos este MimeMultiPart como contenido y rellenamos el resto de campos from, to y subject.
            MimeMessage message = new MimeMessage(session);

            // Se rellena el From
            message.setFrom(new InternetAddress(email));
            //message.addFrom(mails);

            // Se rellenan los destinatarios
            //message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.addRecipients(Message.RecipientType.TO, mails);

            // Se rellena el subject
            message.setSubject("Envio de CFDi");

            // Se mete el texto y los archivos adjuntos.
            message.setContent(multiParte);

            //Una vez construido el mensaje -el simple o el compuesto con adjunto- lo enviamos.
            //Para ello necesitamos una instancia de la clase Transport. Se hace de la siguiente manera
            Transport t = session.getTransport("smtp");

            t.connect(emailO, passO);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
            //print("Correo enviado satisfactoriamente!!");
            Elemento.log.info("Correo enviado satisfactoriamente!!");
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
        progreso = new javax.swing.JProgressBar();
        jButton1 = new javax.swing.JButton();
        recibosEnviados = new javax.swing.JLabel();
        recibosSinEnviar = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Progreso de envio");

        jLabel1.setText("Recibos Enviados");

        jLabel2.setText("Recibos Sin Enviar");

        jButton1.setText("Enviar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        recibosEnviados.setText("0");

        recibosSinEnviar.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(recibosEnviados, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE)
                            .addComponent(recibosSinEnviar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(jButton1)
                .addContainerGap(154, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(recibosEnviados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(recibosSinEnviar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progreso, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Thread t = new Thread(){
          @Override
          public void run(){
              enviar();
          }  
        };
        
        t.start();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(NominaCorreos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NominaCorreos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NominaCorreos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NominaCorreos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar progreso;
    private javax.swing.JLabel recibosEnviados;
    private javax.swing.JLabel recibosSinEnviar;
    // End of variables declaration//GEN-END:variables
}
