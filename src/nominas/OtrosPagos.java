/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nominas;

import elemento.Elemento;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class OtrosPagos extends javax.swing.JFrame {

    private BigDecimal totalOtrosPagos = BigDecimal.ZERO;
    private utils.ConnectionFactory factory = new utils.ConnectionFactory(Elemento.log);
    private utils.Utils util = new utils.Utils(Elemento.log);
    Integer idEmpleado;
    boolean callNomina = false;
    boolean nuevo;
    private Integer tipoNomina = null;
    
    public OtrosPagos() {
        initComponents();
        this.setLocationRelativeTo(null);
    }
    
    public OtrosPagos(int idEmpleado){
        this.idEmpleado = idEmpleado;
        this.nuevo = true;
        initComponents();
        this.setLocationRelativeTo(null);
    }
    
    public OtrosPagos(int idEmpleado, boolean callNomina){
        this.idEmpleado = idEmpleado;
        this.callNomina = callNomina;
        this.nuevo = !callNomina;
        initComponents();
        this.setLocationRelativeTo(null);
        obtenerOtrosPagos(idEmpleado);
        calcular();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        clave = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        tipoOtroPago = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        agregar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        concepto = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        eliminar = new javax.swing.JButton();
        guardar = new javax.swing.JButton();
        importe = new javax.swing.JTextField();
        labelOtras = new javax.swing.JLabel();

        setTitle("Detalle Otros Pagos");

        clave.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                claveFocusLost(evt);
            }
        });

        jLabel4.setText("Importe");

        jLabel1.setText("Tipo Deduccion");

        tipoOtroPago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "001,Reintegro de ISR pagado en exceso (siempre que no haya sido enterado al SAT).", "002,Subsidio para el empleo (efectivamente entregado al trabajador).", "003,Viáticos (entregados al trabajador).", "004,Aplicación de saldo a favor por compensación anual.", "999,Pagos distintos a los listados y que no deben considerarse como ingreso por sueldos, salarios o ingresos asimilados." }));

        jLabel2.setText("Clave");

        agregar.setText("Agregar");
        agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarActionPerformed(evt);
            }
        });

        concepto.setColumns(20);
        concepto.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        concepto.setLineWrap(true);
        concepto.setRows(5);
        concepto.setWrapStyleWord(true);
        concepto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                conceptoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(concepto);

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo Otro Pago", "Clave", "Concepto", "Importe Importe"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        DefaultTableModel model = (DefaultTableModel)tabla.getModel();
        model.addTableModelListener(
            new javax.swing.event.TableModelListener() 
            {
                public void tableChanged(javax.swing.event.TableModelEvent evt) 
                {
                    calcular();
                }
            }
        );
        jScrollPane2.setViewportView(tabla);

        jLabel3.setText("Concepto");

        eliminar.setText("Eliminar");
        eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarActionPerformed(evt);
            }
        });

        guardar.setText("Guardar");
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });

        importe.setText("0.0");
        importe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                importeMouseClicked(evt);
            }
        });
        importe.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                importeFocusGained(evt);
            }
        });

        labelOtras.setText("Total Otros Pagos:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                            .addComponent(tipoOtroPago, 0, 1, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(clave))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(importe))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(agregar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(eliminar)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelOtras)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(guardar))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tipoOtroPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(clave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(agregar)
                            .addComponent(eliminar)))
                    .addComponent(jScrollPane1)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelOtras)
                    .addComponent(guardar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void claveFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_claveFocusLost
        // TODO add your handling code here:
        consultarConcepto(clave.getText());
    }//GEN-LAST:event_claveFocusLost

    private void agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarActionPerformed
        DefaultTableModel model = (DefaultTableModel)tabla.getModel();
        BigDecimal impG = new BigDecimal(importe.getText());

        String tipoPer = tipoOtroPago.getSelectedItem().toString().split(",")[0];
        String cla = clave.getText();
        String conce = concepto.getText().trim();

        Object[] x = {tipoPer,cla,conce,impG};
        model.addRow(x);

        //calcular();
        limpiar();
    }//GEN-LAST:event_agregarActionPerformed

    private void conceptoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_conceptoKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == 10){
            evt.consume();
            //this.consultarProducto("descripcion", descripcion.getText().trim());
        }
        if(evt.getKeyCode() == KeyEvent.VK_TAB){
            evt.consume();
            concepto.transferFocus();
        }
    }//GEN-LAST:event_conceptoKeyPressed

    private void eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)tabla.getModel();
        int row = tabla.getSelectedRow();
        model.removeRow(row);
        //calcular();
    }//GEN-LAST:event_eliminarActionPerformed

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed
        // TODO add your handling code here:
        guardar();
    }//GEN-LAST:event_guardarActionPerformed

    private void importeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_importeMouseClicked
        // TODO add your handling code here:
        importe.selectAll();
    }//GEN-LAST:event_importeMouseClicked

    private void importeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_importeFocusGained
        // TODO add your handling code here:
        importe.selectAll();
    }//GEN-LAST:event_importeFocusGained
    
    private void calcular(){
        DefaultTableModel model = (DefaultTableModel)tabla.getModel();
        BigDecimal imp;
        totalOtrosPagos = BigDecimal.ZERO;
        
        for (int i = 0; i < model.getRowCount(); i++) {
            imp = new BigDecimal(model.getValueAt(i, 3).toString());
            totalOtrosPagos = util.redondear(totalOtrosPagos.add(imp));
        }
        
        labelOtras.setText("Total Otros Pagos: " + totalOtrosPagos.toString());
    }
    
    public void setTipoNomina(int tipoNomina){
        this.tipoNomina = tipoNomina;
        if(tipoNomina == 1){ //Nomina Extraordinaria
            DefaultTableModel model = (DefaultTableModel) tabla.getModel();
            model.setRowCount(0);
        }else{
            obtenerOtrosPagos(idEmpleado);
        }
        calcular();
    }
    
    public Integer getTipoNomina(){
        return this.tipoNomina;
    }
    
    private void limpiar(){
        tipoOtroPago.setSelectedIndex(0);
        clave.setText("");
        concepto.setText("");
        importe.setText("0.0");
        
        clave.transferFocusBackward();
    }
    
    private void consultarConcepto(String clave){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM ConceptosOtrosPagos WHERE clave like \'"+clave+"\'");
            if(rs.next()){
                tipoOtroPago.setSelectedIndex(obtenerTipo(rs.getString("tipo")));
                this.clave.setText(rs.getString("clave"));
                concepto.setText(rs.getString("concepto"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al consultar el concepto con clave: "+clave,e);
        }
    }
    
    private int obtenerTipo(String tipo){
        for (int i = 0; i < tipoOtroPago.getItemCount(); i++) {
            String x = tipoOtroPago.getItemAt(i).toString();
            if(x.contains(tipo)){
                return i;
            }
        }
        return 0;
    }
    
    public BigDecimal getTotalOtrosPagos(){
        return totalOtrosPagos;
    }
    
    private void guardar(){
        if(!callNomina){
            guardarOtrosPagos();
            this.setVisible(false);
        }else{
            this.setVisible(false);
        }
    }
    
    private void guardarOtrosPagos(){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        DefaultTableModel model = (DefaultTableModel)tabla.getModel();
        if(nuevo){
            borrarOtrosPagos(idEmpleado);
        }
        try {
            for (int i = 0; i < model.getRowCount(); i++) {
                BigDecimal impOtr = new BigDecimal (model.getValueAt(i, 3).toString());
                String query = "INSERT INTO ImportesOtrosPagos (clave,idEmpleado,importe) "
                    + "VALUES (\'"+model.getValueAt(i, 1).toString()+"\',"+idEmpleado+","
                        + impOtr.toString() +")";
                stmt.executeUpdate(query);
                
            }
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al guardar las percepciones del empleado "+idEmpleado,e);
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
            java.util.logging.Logger.getLogger(OtrosPagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OtrosPagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OtrosPagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OtrosPagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OtrosPagos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregar;
    private javax.swing.JTextField clave;
    private javax.swing.JTextArea concepto;
    private javax.swing.JButton eliminar;
    private javax.swing.JButton guardar;
    private javax.swing.JTextField importe;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelOtras;
    public javax.swing.JTable tabla;
    private javax.swing.JComboBox tipoOtroPago;
    // End of variables declaration//GEN-END:variables
    private void obtenerOtrosPagos(int idEmpleado) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        Statement stmt2 = factory.stmtLectura(con);
        ResultSet rs, rs2;
        DefaultTableModel model = (DefaultTableModel)tabla.getModel();
        model.setRowCount(0);
        Object [] row = new Object[5];
        
        try {
            rs = stmt.executeQuery("SELECT * FROM ImportesOtrosPagos WHERE idEmpleado = "+idEmpleado);
            while(rs.next()){
                String claveD = rs.getString("clave");
                rs2 = stmt2.executeQuery("SELECT * FROM ConceptosOtrosPagos WHERE clave = \'" + claveD + "\'");
                if(rs2.next()){
                    row[0] = rs2.getString("tipo");
                    row[1] = claveD;
                    row[2] = rs2.getString("concepto");
                    row[3] = rs.getString("importe");
                    model.addRow(row);
                }
            }
            stmt.close();
            stmt2.close();
            con.close();
        }catch (SQLException e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al consultar las percepciones del empleado: "+ idEmpleado,e);
        }
    }
    
    private void borrarOtrosPagos(int idEmpleado){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        ResultSet rs;
        List<String> perp = new ArrayList();
        try {
            stmt.executeUpdate("DELETE FROM ImportesOtrosPagos WHERE idEmpleado="+idEmpleado);
            stmt.close();
            con.close();
        } catch (Exception e) {
        }
    }
}
