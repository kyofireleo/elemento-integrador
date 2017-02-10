/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nominas.configuracion;

import elemento.ConnectionFactory;
import elemento.Elemento;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Abe
 */
public class EmpleadoUpdate extends javax.swing.JFrame {

    DefaultComboBoxModel combo;
    List<String> listaEstados = new ArrayList();
    ConnectionFactory factory = new ConnectionFactory();;
    String idEmpleado;
    ConfigNominas nomi;
    
    public EmpleadoUpdate() {
        initComponents();
        setLocationRelativeTo(null);
    }
    
    public EmpleadoUpdate(String idEmpleado){
        initComponents();
        setLocationRelativeTo(null);
        this.idEmpleado = idEmpleado;
        this.consulCtes("SELECT * FROM EmpleadosRec WHERE id = "+idEmpleado);
    }
    
    public EmpleadoUpdate(String idEmpleado, ConfigNominas nomi){
        initComponents();
        setLocationRelativeTo(null);
        this.idEmpleado = idEmpleado;
        this.nomi = nomi;
        this.consulCtes("SELECT * FROM EmpleadosRec WHERE id = "+idEmpleado);
    }
    
    private String[] consultarCtes(){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        List<String> items = new ArrayList();
        try{
            rs = stmt.executeQuery("SELECT nombre FROM EmpleadosRec");
            while(rs.next()){
                items.add(rs.getString("nombre"));
            }
        }catch(SQLException ex){
            ex.printStackTrace();
            Elemento.log.error("Excepcion al consultar el empleado: " + ex.getMessage(),ex);
        }
        String [] c = items.toArray(new String[items.size()]);
        return c;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        municipio = new javax.swing.JTextField();
        localidad = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        colonia = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        noInterior = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cp = new javax.swing.JTextField();
        guardar = new javax.swing.JButton();
        cancelar = new javax.swing.JButton();
        borrar = new javax.swing.JButton();
        nombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        calle = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        rfc = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        noExterior = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        pais = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        estado = new javax.swing.JComboBox();
        infoEmpleado = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Informacion de Empleado");

        jLabel8.setText("Municipio");

        jLabel7.setText("Localidad");

        jLabel6.setText("Colonia");

        jLabel10.setText("País");

        jLabel11.setText("C.P.");

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

        borrar.setText("Eliminar");
        borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarActionPerformed(evt);
            }
        });

        jLabel2.setText("Nombre");

        jLabel5.setText("No. Interior");

        jLabel3.setText("Calle");

        jLabel4.setText("RFC");

        jLabel12.setText("No. Exterior");

        jLabel13.setText("e-mail");

        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });

        pais.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mexico", "Pais Extranjero" }));
        pais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paisActionPerformed(evt);
            }
        });

        jLabel9.setText("Estado");

        estado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione un Estado", "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Cd. de México", "Chiapas", "Chihuahua", "Coahuila", "Colima", "Durango", "Estado de México", "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Michoacán", "Morelos", "Nayarit", "Nuevo León", "Oaxaca", "Puebla", "Querétaro", "Quintana Roo", "San Luis Potosí", "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatán", "Zacatecas" }));

        infoEmpleado.setText("Info Empleado");
        infoEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoEmpleadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(calle, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rfc, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(26, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel9))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addComponent(noExterior, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel5))
                                            .addComponent(colonia, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(localidad, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(municipio, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(noInterior, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(infoEmpleado))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(pais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(guardar)
                                .addGap(32, 32, 32)
                                .addComponent(borrar)
                                .addGap(31, 31, 31)
                                .addComponent(cancelar)))
                        .addGap(40, 40, 40))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(rfc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(calle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(noExterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(noInterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(colonia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(localidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(municipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(pais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(infoEmpleado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(guardar)
                    .addComponent(cancelar)
                    .addComponent(borrar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed
        // TODO add your handling code here:
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        String id = this.idEmpleado;
        String country = pais.getSelectedItem().toString().trim();
        try {
            stmt.executeUpdate("UPDATE EmpleadosRec SET "
                    + "nombre = \'" + nombre.getText().trim() + "\', rfc = \'" + rfc.getText().trim() + "\', calle = \'" + calle.getText().trim() + "\', noExterior = \'" + noExterior.getText().trim() + "\', noInterior = \'" + noInterior.getText().trim() + "\', colonia = \'" + colonia.getText().trim() + "\', localidad = \'" + localidad.getText().trim() + "\', municipio = \'" + municipio.getText().trim() + "\', estado = \'" + estado.getSelectedItem().toString() + "\', pais = \'" + country + "\', cp = \'" + cp.getText().trim() + "\', email = \'"+email.getText().trim()+"\'"
                    + " WHERE id = "+id);
            
            JOptionPane.showMessageDialog(null, "El empleado " + nombre.getText().trim() + " fue actualizado correctamente");
            Elemento.log.info("El empleado " + nombre.getText().trim() + " fue actualizado correctamente");
            borrarTodo();
            if(nomi!=null)
                nomi.consultarEmpleados();
            this.dispose();
        }catch (SQLException e) {
            e.printStackTrace();
            String msg = "Excepcion: No se pudo actualizar al empleado a la base de datos: " + e.getLocalizedMessage();
            Elemento.log.error(msg,e);
            JOptionPane.showMessageDialog(null, msg);
        }catch (Error err) {
            err.printStackTrace();
            String msg = "Error: No se pudo actualizar al empleado a la base de datos: " + err.getLocalizedMessage();
            Elemento.log.error(msg,err);
            JOptionPane.showMessageDialog(null, msg);
        }
    }//GEN-LAST:event_guardarActionPerformed
    
    private int selecEstado(String state){
        for (int i = 0; i < estado.getItemCount(); i++) {
            if(state.equalsIgnoreCase(estado.getItemAt(i).toString())){
                return i;
            }
        }
        return 0;
    }
    
    private void consulCtes(String query){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        try {
            ResultSet rs = stmt.executeQuery(query);
            if(rs.next()){
                    idEmpleado = rs.getString("id");
                    nombre.setText(rs.getString("nombre"));
                    rfc.setText(rs.getString("rfc"));
                    calle.setText(rs.getString("calle"));
                    noExterior.setText(rs.getString("noExterior"));
                    noInterior.setText(rs.getString("noInterior"));
                    colonia.setText(rs.getString("colonia"));
                    localidad.setText(rs.getString("localidad"));
                    municipio.setText(rs.getString("municipio"));
                    pais.setSelectedItem(rs.getString("pais"));
                    estado.setSelectedIndex(selecEstado(rs.getString("estado")));
                    cp.setText(rs.getString("cp"));
                    email.setText(rs.getString("email"));
                    rfc.transferFocusBackward();
            }else
                borrarTodo();
            
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception ex) {
            Elemento.log.error("Excepcion: No se pudo consultar los EmpleadosRec: " + ex.getLocalizedMessage(),ex);
            ex.printStackTrace();
        }
    }
    
    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_cancelarActionPerformed
    
    private void borrarTodo(){
        nombre.setText("");
        rfc.setText("");
        calle.setText("");
        noExterior.setText("");
        noInterior.setText("");
        colonia.setText("");
        localidad.setText("");
        municipio.setText("");
        estado.setSelectedIndex(0);
        pais.setSelectedIndex(0);
        cp.setText("");
        email.setText("");
        rfc.transferFocusBackward();
        idEmpleado = "";
    }
    
    private void borrarCliente(){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        try{
            stmt.executeUpdate("DELETE FROM EmpleadosRec WHERE id = "+idEmpleado);
            JOptionPane.showMessageDialog(null,"El empleado" + nombre.getText() + " fue eliminado correctamente");
            stmt.close();
            con.close();
            this.borrarTodo();
        }catch(SQLException | HeadlessException ex){
            ex.printStackTrace();
            Elemento.log.error("Excepcion al eliminar el empleado " + nombre.getText() + " : " + ex.getMessage(),ex);
            JOptionPane.showMessageDialog(null, "Excepcion al eliminar el empleado " + nombre.getText() + " : " + ex.getMessage());
        }
    }
    
    private void borrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarActionPerformed
        // TODO add your handling code here:
        int opc = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el empleado?", "Eliminar Cliente", JOptionPane.YES_NO_OPTION);
        switch(opc){
            case JOptionPane.YES_OPTION:
                borrarCliente();
                borrarTodo();
                break;
            case JOptionPane.NO_OPTION:
                break;
        }
    }//GEN-LAST:event_borrarActionPerformed

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailActionPerformed

    private void paisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paisActionPerformed
        // TODO add your handling code here:
        DefaultComboBoxModel comboE = (DefaultComboBoxModel) estado.getModel();
        
        if(listaEstados.isEmpty()){
            for (int i = 0; i < comboE.getSize(); i++) {
                listaEstados.add(comboE.getElementAt(i).toString());
            }
        }
        
        if(pais.getSelectedIndex() > 0){
            comboE.removeAllElements();
            comboE.addElement("Estado Extranjero");
        }else{
            comboE.removeAllElements();
            for (int i = 0; i < listaEstados.size(); i++) {
                comboE.addElement(listaEstados.get(i));
            }
        }
    }//GEN-LAST:event_paisActionPerformed

    private void infoEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoEmpleadoActionPerformed
        // TODO add your handling code here:
        int id = Integer.parseInt(idEmpleado);
        Empleados emp = new Empleados(id,id,nombre.getText());
        emp.setVisible(true);
    }//GEN-LAST:event_infoEmpleadoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Windows look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Windows (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EmpleadoUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmpleadoUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmpleadoUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmpleadoUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new EmpleadoUpdate().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton borrar;
    private javax.swing.JTextField calle;
    private javax.swing.JButton cancelar;
    private javax.swing.JTextField colonia;
    private javax.swing.JTextField cp;
    private javax.swing.JTextField email;
    private javax.swing.JComboBox estado;
    private javax.swing.JButton guardar;
    private javax.swing.JButton infoEmpleado;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
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
    private javax.swing.JTextField noExterior;
    private javax.swing.JTextField noInterior;
    private javax.swing.JTextField nombre;
    private javax.swing.JComboBox pais;
    private javax.swing.JTextField rfc;
    // End of variables declaration//GEN-END:variables
}
