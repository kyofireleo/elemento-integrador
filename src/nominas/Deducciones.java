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

    private Double totalOtras=0.0, totalRet=0.0;
    private utils.ConnectionFactory factory = new utils.ConnectionFactory(Elemento.log);
    private utils.Utils util = new utils.Utils(Elemento.log);
    Integer idEmpleado;
    boolean callNomina = false;
    boolean nuevo;
    private Integer tipoNomina = null;
    
    public Deducciones() {
        initComponents();
        this.setLocationRelativeTo(null);
    }
    
    public Deducciones(int idEmpleado){
        this.idEmpleado = idEmpleado;
        this.nuevo = true;
        initComponents();
        this.setLocationRelativeTo(null);
    }
    
    public Deducciones(int idEmpleado, boolean callNomina){
        this.idEmpleado = idEmpleado;
        this.callNomina = callNomina;
        this.nuevo = !callNomina;
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
        labelOtras = new javax.swing.JLabel();
        labelRetenido = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        agregar = new javax.swing.JButton();
        tipoDeduccion = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        concepto = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        clave = new javax.swing.JTextField();
        importe = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        eliminar = new javax.swing.JButton();

        setTitle("Detalle Deducciones");

        guardar.setText("Guardar");
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });

        labelOtras.setText("Total Otras Deducciones:");

        labelRetenido.setText("Total Retenido:");

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo Deduccion", "Clave", "Concepto", "Importe Importe"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
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

        tipoDeduccion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "001,Seguridad social", "002,ISR", "003,Aportaciones a retiro, cesantía en edad avanzada y vejez.", "004,Otros", "005,Aportaciones a Fondo de vivienda", "006,Descuento por incapacidad", "007,Pensión alimenticia", "008,Renta", "009,Préstamos provenientes del Fondo Nacional de la Vivienda para los Trabajadores", "010,Pago por crédito de vivienda", "011,Pago de abonos INFONACOT", "012,Anticipo de salarios", "013,Pagos hechos con exceso al trabajador", "014,Errores", "015,Pérdidas", "016,Averías", "017,Adquisición de artículos producidos por la empresa o establecimiento", "018,Cuotas para la constitución y fomento de sociedades cooperativas y de cajas de ahorro", "19,Cuotas sindicales", "020,Ausencia (Ausentismo)", "021,Cuotas obrero patronales", "022,Impuestos Locales", "023,Aportaciones voluntarias", "024,Ajuste en Gratificación Anual (Aguinaldo) Exento", "025,Ajuste en Gratificación Anual (Aguinaldo) Gravado", "026,Ajuste en Participación de los Trabajadores en las Utilidades PTU Exento", "027,Ajuste en Participación de los Trabajadores en las Utilidades PTU Gravado", "028,Ajuste en Reembolso de Gastos Médicos Dentales y Hospitalarios Exento", "029,Ajuste en Fondo de ahorro Exento", "030,Ajuste en Caja de ahorro Exento", "031,Ajuste en Contribuciones a Cargo del Trabajador Pagadas por el Patrón Exento", "032,Ajuste en Premios por puntualidad Gravado", "033,Ajuste en Prima de Seguro de vida Exento", "034,Ajuste en Seguro de Gastos Médicos Mayores Exento", "035,Ajuste en Cuotas Sindicales Pagadas por el Patrón Exento", "036,Ajuste en Subsidios por incapacidad Exento", "037,Ajuste en Becas para trabajadores y/o hijos Exento", "038,Ajuste en Horas extra Exento", "039,Ajuste en Horas extra Gravado", "040,Ajuste en Prima dominical Exento", "041,Ajuste en Prima dominical Gravado", "042,Ajuste en Prima vacacional Exento", "043,Ajuste en Prima vacacional Gravado", "044,Ajuste en Prima por antigüedad Exento", "045,Ajuste en Prima por antigüedad Gravado", "046,Ajuste en Pagos por separación Exento", "047,Ajuste en Pagos por separación Gravado", "048,Ajuste en Seguro de retiro Exento", "049,Ajuste en Indemnizaciones Exento", "050,Ajuste en Indemnizaciones Gravado", "051,Ajuste en Reembolso por funeral Exento", "052,Ajuste en Cuotas de seguridad social pagadas por el patrón Exento", "053,Ajuste en Comisiones Gravado", "054,Ajuste en Vales de despensa Exento", "055,Ajuste en Vales de restaurante Exento", "056,Ajuste en Vales de gasolina Exento", "057,Ajuste en Vales de ropa Exento", "058,Ajuste en Ayuda para renta Exento", "059,Ajuste en Ayuda para artículos escolares Exento", "060,Ajuste en Ayuda para anteojos Exento", "061,Ajuste en Ayuda para transporte Exento", "062,Ajuste en Ayuda para gastos de funeral Exento", "063,Ajuste en Otros ingresos por salarios Exento", "064,Ajuste en Otros ingresos por salarios Gravado", "065,Ajuste en Jubilaciones, pensiones o haberes de retiro Exento", "066,Ajuste en Jubilaciones, pensiones o haberes de retiro Gravado", "067,Ajuste en Pagos por separación Acumulable", "068,Ajuste en Pagos por separación No acumulable", "069,Ajuste en Jubilaciones, pensiones o haberes de retiro Acumulable", "070,Ajuste en Jubilaciones, pensiones o haberes de retiro No acumulable", "071,Ajuste en Subsidio para el empleo (efectivamente entregado al trabajador)", "072,Ajuste en Ingresos en acciones o títulos valor que representan bienes Exento", "073,Ajuste en Ingresos en acciones o títulos valor que representan bienes Gravado", "074,Ajuste en Alimentación Exento", "075,Ajuste en Alimentación Gravado", "076,Ajuste en Habitación Exento", "077,Ajuste en Habitación Gravado", "078,Ajuste en Premios por asistencia", "079,Ajuste en Pagos distintos a los listados y que no deben considerarse como ingreso por sueldos, salarios o ingresos asimilados.", "080,Ajuste en Viáticos no comprobados", "081,Ajuste en Viáticos anticipados", "082,Ajuste en Fondo de ahorro Gravado", "083,Ajuste en Caja de ahorro Gravado", "084,Ajuste en Prima de Seguro de vida Gravado", "085,Ajuste en Seguro de Gastos Médicos Mayores Gravado", "086,Ajuste en Subsidios por incapacidad Gravado", "087,Ajuste en Becas para trabajadores y/o hijos Gravado", "088,Ajuste en Seguro de retiro Gravado", "089,Ajuste en Vales de despensa Gravado", "090,Ajuste en Vales de restaurante Gravado", "091,Ajuste en Vales de gasolina Gravado", "092,Ajuste en Vales de ropa Gravado", "093,Ajuste en Ayuda para renta Gravado", "094,Ajuste en Ayuda para artículos escolares Gravado", "095,Ajuste en Ayuda para anteojos Gravado", "096,Ajuste en Ayuda para transporte Gravado", "097,Ajuste en Ayuda para gastos de funeral Gravado", "098,Ajuste a ingresos asimilados a salarios gravados", "099,Ajuste a ingresos por sueldos y salarios gravados" }));

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

        jLabel4.setText("Importe");

        clave.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                claveFocusLost(evt);
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
                        .addComponent(labelRetenido)
                        .addGap(119, 119, 119)
                        .addComponent(labelOtras)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(guardar))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE))
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
                            .addComponent(importe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(agregar)
                            .addComponent(eliminar)))
                    .addComponent(jScrollPane1))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelRetenido)
                    .addComponent(labelOtras)
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
        Double impG = new Double(importe.getText());
        
        String tipoPer = tipoDeduccion.getSelectedItem().toString().split(",")[0];
        String cla = clave.getText();
        String conce = concepto.getText().trim();

        Object[] x = {tipoPer,cla,conce,impG};
        model.addRow(x);
        
        calcular();
        limpiar();
    }//GEN-LAST:event_agregarActionPerformed

    private void calcular(){
        DefaultTableModel model = (DefaultTableModel)tabla.getModel();
        Double imp;
        totalOtras = 0.0;
        totalRet = 0.0;
        
        for (int i = 0; i < model.getRowCount(); i++) {
            imp = (Double)model.getValueAt(i, 3);
            if(model.getValueAt(i, 0).toString().equals("002")){
                totalRet = util.redondear(totalRet+imp);
            }else{
                totalOtras = util.redondear(totalOtras+imp);
            }
        }
        
        labelRetenido.setText("Total Retenido: "+totalRet);
        labelOtras.setText("Total Otras Deducciones: "+totalOtras);
    }
    
    public void setTipoNomina(int tipoNomina){
        this.tipoNomina = tipoNomina;
        if(tipoNomina == 1){ //Nomina Extraordinaria
            DefaultTableModel model = (DefaultTableModel) tabla.getModel();
            model.setRowCount(0);
        }else{
            obtenerDeducciones(idEmpleado);
        }
        calcular();
    }
    
    public Integer getTipoNomina(){
        return this.tipoNomina;
    }
    
    private void limpiar(){
        tipoDeduccion.setSelectedIndex(0);
        clave.setText("");
        concepto.setText("");
        importe.setText("0.0");
        
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

    private void importeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_importeMouseClicked
        // TODO add your handling code here:
        importe.selectAll();
    }//GEN-LAST:event_importeMouseClicked

    private void importeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_importeFocusGained
        // TODO add your handling code here:
        importe.selectAll();
    }//GEN-LAST:event_importeFocusGained

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
    
    public Double getTotalOtras(){
        return totalOtras;
    }
    
    public Double getTotalRetenido(){
        return totalRet;
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
        if(nuevo){
            borrarDeducciones(idEmpleado);
        }
        try {
            for (int i = 0; i < model.getRowCount(); i++) {
                String query = "INSERT INTO ImportesDeducciones (clave,idEmpleado,importe) "
                    + "VALUES (\'"+model.getValueAt(i, 1).toString()+"\',"+idEmpleado+","
                        + (Double)model.getValueAt(i, 3) +")";
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
    private javax.swing.JTextField importe;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelOtras;
    private javax.swing.JLabel labelRetenido;
    public javax.swing.JTable tabla;
    private javax.swing.JComboBox tipoDeduccion;
    // End of variables declaration//GEN-END:variables
 
    private void obtenerDeducciones(int idEmpleado) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        Statement stmt2 = factory.stmtLectura(con);
        ResultSet rs, rs2;
        DefaultTableModel model = (DefaultTableModel)tabla.getModel();
        model.setRowCount(0);
        Object [] row = new Object[5];
        
        try {
            rs = stmt.executeQuery("SELECT * FROM ImportesDeducciones WHERE idEmpleado = "+idEmpleado);
            while(rs.next()){
                String claveD = rs.getString("clave");
                rs2 = stmt2.executeQuery("SELECT * FROM ConceptosDeducciones WHERE clave = \'" + claveD + "\'");
                if(rs2.next()){
                    row[0] = rs2.getString("tipo");
                    row[1] = claveD;
                    row[2] = rs2.getString("concepto");
                    row[3] = rs.getDouble("importe");
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
