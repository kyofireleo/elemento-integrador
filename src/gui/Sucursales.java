/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import elemento.Elemento;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Soporte
 */
public class Sucursales extends javax.swing.JFrame {

    utils.ConnectionFactory factory = new utils.ConnectionFactory(Elemento.log);
    private boolean modificar;
    private int idEmisor;
    private int idSucursal;
    private boolean termino = false;

    public boolean isTermino() {
        return termino;
    }

    public void setTermino(boolean termino) {
        this.termino = termino;
    }

    public int getIdEmisor() {
        return idEmisor;
    }

    public void setIdEmisor(int idEmisor) {
        this.idEmisor = idEmisor;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }
    
    public Sucursales(boolean modificar,Integer idSucursal, Integer idEmisor) {
        this.modificar = modificar;
        initComponents();
        this.setLocationRelativeTo(null);
        
        if(idSucursal != null){
            this.idSucursal = idSucursal.intValue();
            consultarSucursal(idSucursal.intValue());
        }else if(idEmisor != null){
            this.idEmisor = idEmisor.intValue();
            eliminar.setEnabled(false);
        }else{
            eliminar.setEnabled(false);
            guardar.setEnabled(false);
            JOptionPane.showMessageDialog(null, "Ocurrio un error al consultar la sucursal");
        }
        
        if(!modificar && idSucursal != null){
            eliminar.setEnabled(false);
            guardar.setEnabled(false);
            cancelar.setText("Cerrar");
        }
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
        nombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        calle = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        numExterior = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        numInterior = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        colonia = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        localidad = new javax.swing.JTextField();
        pais = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        estado = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        codigoPostal = new javax.swing.JTextField();
        guardar = new javax.swing.JButton();
        cancelar = new javax.swing.JButton();
        eliminar = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        municipio = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Sucursal");

        jLabel1.setText("Nombre");

        nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreActionPerformed(evt);
            }
        });

        jLabel2.setText("Calle");

        jLabel3.setText("Num. Exterior");

        jLabel4.setText("Num. Interior");

        jLabel5.setText("Colonia");

        jLabel6.setText("Localidad");

        pais.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mexico", "Pais Extranjero" }));

        jLabel7.setText("Pais");

        jLabel8.setText("Estado");

        estado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione un Estado", "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Cd. de México", "Chiapas", "Chihuahua", "Coahuila", "Colima", "Durango", "Estado de México", "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Michoacán", "Morelos", "Nayarit", "Nuevo León", "Oaxaca", "Puebla", "Querétaro", "Quintana Roo", "San Luis Potosí", "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatán", "Zacatecas" }));

        jLabel9.setText("C.P.");

        guardar.setText("Guardar");
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });

        cancelar.setText("Cancelar");
        cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });

        eliminar.setText("Eliminar Sucursal");
        eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarActionPerformed(evt);
            }
        });

        jLabel10.setText("Municipio");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(jLabel6)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel7)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(pais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nombre)
                            .addComponent(calle)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(numExterior, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(numInterior, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(colonia)
                            .addComponent(localidad)
                            .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(codigoPostal, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(municipio, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(eliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(guardar)
                        .addGap(18, 18, 18)
                        .addComponent(cancelar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(calle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(numExterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(numInterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(colonia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(localidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(municipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(codigoPostal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(guardar)
                    .addComponent(cancelar)
                    .addComponent(eliminar))
                .addGap(7, 7, 7))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nombreActionPerformed

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed
        // TODO add your handling code here:
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        termino = true;
        try {
            if (modificar) {
                stmt.executeUpdate("UPDATE Sucursales SET nombre = "+nombre.getText().trim()+
                        ", calle = "+calle.getText().trim()+", noExterior = "+numExterior.getText().trim()+", "
                        + "noInterior = "+numInterior.getText().trim()+", colonia = "+colonia.getText().trim()
                        + ", localidad = "+localidad.getText().trim()+", municipio = "+municipio.getText().trim()
                        + ", estado = "+estado.getSelectedItem().toString().trim()+", pais = "+pais.getSelectedItem().toString().trim()
                        + ", cp = "+codigoPostal.getText().trim()+" WHERE idSucursal = "+idSucursal);
            }else{
                stmt.executeUpdate("INSERT INTO Sucursales (idEmisor,nombre,calle,noExterior,noInterior,colonia,localidad,municipio,estado,pais,cp) "
                        + "VALUES ('"+idEmisor+"','"+nombre.getText().trim()+"','"+calle.getText().trim()+"','"+numExterior.getText().trim()+"',"
                        + "'"+numInterior.getText().trim()+"','"+colonia.getText().trim()+"','"+localidad.getText().trim()+"',"
                        + "'"+municipio.getText().trim()+"','"+estado.getSelectedItem().toString().trim()+"','"+pais.getSelectedItem().toString().trim()+"',"
                        + "'"+codigoPostal.getText().trim()+"')");
                
                idSucursal = obtenerUltimoId(con);
            }
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al guardar la Sucursal: ",e);
            JOptionPane.showMessageDialog(null, "Excepcion al guardar la Sucursal: "+e.getMessage());
        }
        this.dispose();
    }//GEN-LAST:event_guardarActionPerformed

    private void eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarActionPerformed
        // TODO add your handling code here:
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        String rfcE = "";
        try {
            ResultSet rs = stmt.executeQuery("SELECT rfc FROM Emisores WHERE id = "+idEmisor);
            if(rs.next()){
                rfcE = rs.getString("rfc");
            }
            rs.close();
            
            if(!rfcE.isEmpty()){
                stmt.executeUpdate("DELETE FROM Sucursales WHERE idSucursal = "+idSucursal);
                stmt.executeUpdate("UPDATE Cuentas SET esSucursal = "+Boolean.FALSE+", idSucursal = 0 WHERE rfc like \'"+rfcE+"\'");
            }else{
                JOptionPane.showMessageDialog(null, "No se pudo eliminar la sucursal debido ah que no fue encontrada\nuna relacion con algun Emisor.\nConsulte la base de datos.");
            }
            
            stmt.close();
            con.close();
            JOptionPane.showMessageDialog(null, "La sucursal ha sido eliminada");
            this.dispose();
        } catch (SQLException | HeadlessException e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al eliminar la sucursal: ",e);
            JOptionPane.showMessageDialog(null, "Excepcion al eliminar la sucursal: "+e.getMessage());
        }
    }//GEN-LAST:event_eliminarActionPerformed

    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed
        // TODO add your handling code here:
        termino = true;
        this.dispose();
    }//GEN-LAST:event_cancelarActionPerformed

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
            java.util.logging.Logger.getLogger(Sucursales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sucursales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sucursales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sucursales.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField calle;
    private javax.swing.JButton cancelar;
    private javax.swing.JTextField codigoPostal;
    private javax.swing.JTextField colonia;
    private javax.swing.JButton eliminar;
    private javax.swing.JComboBox estado;
    private javax.swing.JButton guardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField localidad;
    private javax.swing.JTextField municipio;
    private javax.swing.JTextField nombre;
    private javax.swing.JTextField numExterior;
    private javax.swing.JTextField numInterior;
    private javax.swing.JComboBox pais;
    // End of variables declaration//GEN-END:variables

    private void consultarSucursal(int idSucursal) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM Sucursales WHERE idSucursal = "+idSucursal);
            if(rs.next()){
                nombre.setText(rs.getString("nombre"));
                calle.setText(rs.getString("calle"));
                numExterior.setText(rs.getString("noExterior"));
                numInterior.setText(rs.getString("noInterior"));
                colonia.setText(rs.getString("colonia"));
                localidad.setText(rs.getString("localidad"));
                municipio.setText(rs.getString("municipio"));
                estado.setSelectedItem(rs.getString("estado").trim());
                pais.setSelectedItem(rs.getString("pais").trim());
                codigoPostal.setText(rs.getString("cp").trim());
                idEmisor = rs.getInt("idEmisor");
            }
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int obtenerUltimoId(Connection con) {
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        int idSucursal = 0;
        try {
            rs = stmt.executeQuery("SELECT idSucursal FROM Sucursales ORDER BY idSucursal ASC");
            if(rs.last()){
                idSucursal = rs.getInt("idSucursal");
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idSucursal;
    }
}
