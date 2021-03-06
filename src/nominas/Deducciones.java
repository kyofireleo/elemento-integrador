/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nominas;

import elemento.Elemento;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Abe
 */
public class Deducciones extends javax.swing.JFrame {

    private Double totalGravado=0.0, totalExento=0.0;
    private utils.ConnectionFactory factory = new utils.ConnectionFactory(Elemento.log);
    private utils.Utils util = new utils.Utils(Elemento.log);
    Integer idEmpleado;
    boolean callNomina = false;
    
    public Deducciones() {
        initComponents();
        this.setLocationRelativeTo(null);
    }
    
    public Deducciones(int idEmpleado){
        this.idEmpleado = idEmpleado;
        initComponents();
        this.setLocationRelativeTo(null);
    }
    
    public Deducciones(int idEmpleado, boolean callNomina){
        this.idEmpleado = idEmpleado;
        this.callNomina = callNomina;
        initComponents();
        this.setLocationRelativeTo(null);
        obtenerDeducciones(idEmpleado);
        calcular();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        guardar = new javax.swing.JButton();
        labelExento = new javax.swing.JLabel();
        labelGravado = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        agregar = new javax.swing.JButton();
        importeExento = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        tipoDeduccion = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        concepto = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        clave = new javax.swing.JTextField();
        importeGravado = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        eliminar = new javax.swing.JButton();

        setTitle("Deducciones");

        guardar.setText("Guardar");
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });

        labelExento.setText("Total Exento:");

        labelGravado.setText("Total Gravado:");

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo Deduccion", "Clave", "Concepto", "Importe Gravado", "Importe Exento"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tabla);

        agregar.setText("Agregar");
        agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarActionPerformed(evt);
            }
        });

        importeExento.setText("0.0");
        importeExento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                importeExentoMouseClicked(evt);
            }
        });
        importeExento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                importeExentoFocusGained(evt);
            }
        });

        jLabel5.setText("Importe Exento");

        tipoDeduccion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "001,Seguridad social", "002,ISR", "003,Aportaciones a retiro, cesantía en edad avanzada y vejez", "004,Otros", "005,Aportaciones a Fondo de vivienda", "006,Descuento por incapacidad", "007,Pensión alimenticia", "008,Renta", "009,Préstamos provenientes del Fondo Nacional de la Vivienda para los Trabajadores", "010,Pago por crédito de vivienda", "011,Pago de abonos INFONACOT", "012,Anticipo de salarios", "013,Pagos hechos con exceso al trabajador", "014,Errores", "015,Pérdidas", "016,Averías", "017,Adquisición de artículos producidos por la empresa o establecimiento", "018,Cuotas para la constitución y fomento de sociedades cooperativas y de cajas de ahorro", "019,Cuotas sindicales", "020,Ausencia (Ausentismo)", "021,Cuotas obrero patronales" }));

        jLabel2.setText("Clave");

        jLabel1.setText("Tipo Deduccion");

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

        jLabel4.setText("Importe Gravado");

        clave.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                claveFocusLost(evt);
            }
        });

        importeGravado.setText("0.0");
        importeGravado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                importeGravadoMouseClicked(evt);
            }
        });
        importeGravado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                importeGravadoFocusGained(evt);
            }
        });

        jLabel3.setText("Concepto");

        eliminar.setText("Eliminar");
        eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarActionPerformed(evt);
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                            .addComponent(tipoDeduccion, 0, 1, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(clave, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(importeGravado, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(importeExento))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(agregar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(eliminar)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelGravado)
                        .addGap(119, 119, 119)
                        .addComponent(labelExento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(guardar))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tipoDeduccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(clave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(importeGravado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(importeExento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(agregar)
                            .addComponent(eliminar)))
                    .addComponent(jScrollPane1))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelGravado)
                    .addComponent(labelExento)
                    .addComponent(guardar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed
        // TODO add your handling code here:
        guardar();
    }//GEN-LAST:event_guardarActionPerformed

    private void agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarActionPerformed
        DefaultTableModel model = (DefaultTableModel)tabla.getModel();
        Double impG = new Double(importeGravado.getText());
        Double impE = new Double(importeExento.getText());
        
        String tipoPer = tipoDeduccion.getSelectedItem().toString().split(",")[0];
        String cla = clave.getText();
        String conce = concepto.getText().trim();

        Object[] x = {tipoPer,cla,conce,impG,impE};
        model.addRow(x);
        
        calcular();
        limpiar();
    }//GEN-LAST:event_agregarActionPerformed

    private void calcular(){
        DefaultTableModel model = (DefaultTableModel)tabla.getModel();
        Double impG, impE;
        totalGravado = 0.0;
        totalExento = 0.0;
        
        for (int i = 0; i < model.getRowCount(); i++) {
            impG = (Double)model.getValueAt(i, 3);
            impE = (Double)model.getValueAt(i, 4);
            totalGravado = util.redondear(totalGravado+impG);
            totalExento = util.redondear(totalExento+impE);
        }
        
        labelGravado.setText("Total Gravado: "+totalGravado);
        labelExento.setText("Total Exento: "+totalExento);
    }
    
    private void limpiar(){
        tipoDeduccion.setSelectedIndex(0);
        clave.setText("");
        concepto.setText("");
        importeGravado.setText("0.0");
        importeExento.setText("0.0");
        
        clave.transferFocusBackward();
    }
    
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

    private void importeGravadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_importeGravadoMouseClicked
        // TODO add your handling code here:
        importeGravado.selectAll();
    }//GEN-LAST:event_importeGravadoMouseClicked

    private void importeExentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_importeExentoMouseClicked
        // TODO add your handling code here:
        importeExento.selectAll();
    }//GEN-LAST:event_importeExentoMouseClicked

    private void importeGravadoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_importeGravadoFocusGained
        // TODO add your handling code here:
        importeGravado.selectAll();
    }//GEN-LAST:event_importeGravadoFocusGained

    private void importeExentoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_importeExentoFocusGained
        // TODO add your handling code here:
        importeExento.selectAll();
    }//GEN-LAST:event_importeExentoFocusGained

    private void eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)tabla.getModel();
        int row = tabla.getSelectedRow();
        model.removeRow(row);
        calcular();
    }//GEN-LAST:event_eliminarActionPerformed

    private void claveFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_claveFocusLost
        // TODO add your handling code here:
        consultarConcepto(clave.getText());
    }//GEN-LAST:event_claveFocusLost

    private void consultarConcepto(String clave){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT * FROM ConceptosDeducciones WHERE clave like \'"+clave+"\'");
            if(rs.next()){
                tipoDeduccion.setSelectedIndex(obtenerTipo(rs.getString("tipo")));
                this.clave.setText(rs.getString("clave"));
                concepto.setText(rs.getString("concepto"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al consultar el concepto con clave: "+clave,e);
        }
    }
    
    private int obtenerTipo(String tipo){
        for (int i = 0; i < tipoDeduccion.getItemCount(); i++) {
            String x = tipoDeduccion.getItemAt(i).toString();
            if(x.contains(tipo)){
                return i;
            }
        }
        return 0;
    }
    
    public Double getTotalGravado(){
        return totalGravado;
    }
    
    public Double getTotalExento(){
        return totalExento;
    }
    
    private void guardar(){
        if(!callNomina){
            guardarDeducciones();
            this.setVisible(false);
        }else{
            this.setVisible(false);
        }
    }
    
    private void guardarDeducciones(){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        DefaultTableModel model = (DefaultTableModel)tabla.getModel();
        if(idEmpleado != null)
            borrarDeducciones(idEmpleado);
        try {
            for (int i = 0; i < model.getRowCount(); i++) {
                String query = "INSERT INTO ImportesDeducciones (clave,idEmpleado,importeGravado,importeExento) "
                    + "VALUES (\'"+model.getValueAt(i, 1).toString()+"\',"+idEmpleado+","
                        + (Double)model.getValueAt(i, 3) +","+(Double)model.getValueAt(i, 4)+")";
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
            java.util.logging.Logger.getLogger(Deducciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Deducciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Deducciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Deducciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Deducciones().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregar;
    private javax.swing.JTextField clave;
    private javax.swing.JTextArea concepto;
    private javax.swing.JButton eliminar;
    private javax.swing.JButton guardar;
    private javax.swing.JTextField importeExento;
    private javax.swing.JTextField importeGravado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelExento;
    private javax.swing.JLabel labelGravado;
    public javax.swing.JTable tabla;
    private javax.swing.JComboBox tipoDeduccion;
    // End of variables declaration//GEN-END:variables
 
    private void obtenerDeducciones(int idEmpleado) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        Statement stmt2 = factory.stmtLectura(con);
        ResultSet rs, rs2;
        DefaultTableModel model = (DefaultTableModel)tabla.getModel();
        Object [] row = new Object[5];
        
        try {
            rs = stmt.executeQuery("SELECT * FROM ImportesDeducciones WHERE idEmpleado = "+idEmpleado);
            while(rs.next()){
                String claveD = rs.getString("clave");
                rs2 = stmt2.executeQuery("SELECT * FROM ConceptosDeducciones WHERE clave like \'" + claveD + "\'");
                if(rs2.next()){
                    row[0] = rs2.getString("tipo");
                    row[1] = claveD;
                    row[2] = rs2.getString("concepto");
                    row[3] = rs.getDouble("importeGravado");
                    row[4] = rs.getDouble("importeExento");
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
    
    private void borrarDeducciones(int idEmpleado){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        ResultSet rs;
        List<String> perp = new ArrayList();
        try {
            stmt.executeUpdate("DELETE FROM ImportesDeducciones WHERE idEmpleado="+idEmpleado);
            stmt.close();
            con.close();
        } catch (Exception e) {
        }
    }
}
