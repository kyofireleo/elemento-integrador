/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import elemento.ConnectionFactory;
import elemento.Elemento;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Abe
 */
public class AddProductos extends javax.swing.JFrame {

    /**
     * Creates new form AddProductos
     */
    ConnectionFactory factory = new ConnectionFactory();
    DefaultTableModel model;
    
    public AddProductos() {
        initComponents();
        setLocationRelativeTo(null);
        cargarProductos();
    }
    
    private void cargarProductos(){
        borrarCampos();
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs = null;
        try{
            rs = stmt.executeQuery("SELECT * FROM Productos");
            model = (DefaultTableModel) tablaProductos.getModel();
            model.setRowCount(0);
            String noIden,uni,desc,prec;
            Boolean iva;
            while(rs.next()){
                noIden = rs.getString("noIdentificacion");
                uni = rs.getString("unidad");
                desc = rs.getString("descripcion");
                prec = ""+rs.getDouble("precio");
                iva = rs.getBoolean("aplicaIva");
                
                Object [] row = {noIden,uni,desc,prec,iva};
                model.addRow(row);
            }
        }catch(Exception e){
            e.printStackTrace();
            Elemento.log.error("Excepcion al leer los productos de la base de datos: " + e.getMessage(), e);
        }finally{
            if(con != null){
                try{
                    if(rs != null) {
                        rs.close();
                    }
                    stmt.close();
                    con.close();
                }catch(SQLException ex){
                    ex.printStackTrace();
                }
            }
        }
    }
    
    private void borrarCampos(){
        noIdentificacion.setText("");
        unidad.setSelectedIndex(0);
        descripcion.setText("");
        precio.setText("");
        aplicaIva.setSelected(false);
        unidad.transferFocusBackward();
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
        noIdentificacion = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descripcion = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        precio = new javax.swing.JTextField();
        cancelar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaProductos = new javax.swing.JTable();
        aplicaIva = new javax.swing.JCheckBox();
        agregar = new javax.swing.JButton();
        actualizar = new javax.swing.JButton();
        borrar = new javax.swing.JButton();
        unidad = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("No Identificacion");

        noIdentificacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noIdentificacionActionPerformed(evt);
            }
        });
        noIdentificacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                noIdentificacionKeyPressed(evt);
            }
        });

        jLabel2.setText("Unidad");

        jLabel3.setText("Descripcion");

        descripcion.setColumns(20);
        descripcion.setLineWrap(true);
        descripcion.setRows(5);
        descripcion.setWrapStyleWord(true);
        descripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                descripcionKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(descripcion);

        jLabel4.setText("Precio");

        cancelar.setText("Cancelar");
        cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });

        tablaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No. Identificacion", "Unidad", "Descripcion", "Precio", "Aplica IVA"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaProductosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaProductos);

        aplicaIva.setText("Aplica IVA");

        agregar.setText("Agregar");
        agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarActionPerformed(evt);
            }
        });

        actualizar.setText("Actualizar");
        actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualizarActionPerformed(evt);
            }
        });

        borrar.setText("Borrar");
        borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarActionPerformed(evt);
            }
        });

        unidad.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "NA", "NO APLICA", "SERVICIO", "DONATIVO", "KILOGRAMO", "GRAMO", "METRO", "METRO CUADRADO", "METRO CUBICO", "PIEZA", "SEGUNDO", "CABEZA", "LITRO", "PAR", "KILOWATT", "MILLAR", "JUEGO", "KILOWATT/HORA", "TONELADA", "BARRIL", "GRAMO NETO", "DECENAS", "CIENTOS", "DOCENAS", "CAJA", "BOTELLA" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(noIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(precio, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(aplicaIva)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(unidad, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel3)))
                                .addGap(18, 18, 18))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(168, 168, 168)
                                .addComponent(agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(actualizar)
                                .addGap(18, 18, 18)
                                .addComponent(borrar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cancelar)
                        .addGap(13, 13, 13))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane2)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(noIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2)
                            .addComponent(unidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(aplicaIva))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(agregar)
                            .addComponent(actualizar)
                            .addComponent(borrar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(cancelar)
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void noIdentificacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noIdentificacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_noIdentificacionActionPerformed

    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_cancelarActionPerformed

    private void agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarActionPerformed
        // TODO add your handling code here:
        agregarProducto();
    }//GEN-LAST:event_agregarActionPerformed

    private void actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizarActionPerformed
        // TODO add your handling code here:
        String claveActual = noIdentificacion.getText().trim();
        actualizarProducto(claveActual);
    }//GEN-LAST:event_actualizarActionPerformed

    private void borrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarActionPerformed
        // TODO add your handling code here:
        String claveActual = noIdentificacion.getText().trim();
        borrarProducto(claveActual);
    }//GEN-LAST:event_borrarActionPerformed

    private void noIdentificacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_noIdentificacionKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == 10){
            consultarProducto("noIdentificacion",noIdentificacion.getText().trim());
        }
    }//GEN-LAST:event_noIdentificacionKeyPressed

    private void descripcionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descripcionKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_descripcionKeyPressed

    private void tablaProductosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaProductosMouseClicked
        // TODO add your handling code here:
        model = (DefaultTableModel) tablaProductos.getModel();
        int row = tablaProductos.getSelectedRow();
        noIdentificacion.setText(model.getValueAt(row, 0).toString());
        unidad.setSelectedItem(model.getValueAt(row, 1).toString().trim());
        descripcion.setText(model.getValueAt(row, 2).toString());
        precio.setText(model.getValueAt(row, 3).toString());
        aplicaIva.setSelected((Boolean)model.getValueAt(row, 4));
    }//GEN-LAST:event_tablaProductosMouseClicked

    private void agregarProducto(){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        String query = "INSERT INTO Productos VALUES ("
                    + "\'"+noIdentificacion.getText().trim()+"\',\'"+descripcion.getText().trim()+"\', \'"+unidad.getSelectedItem().toString().trim()+"\',\'"
                    +precio.getText().trim()+"\',";
        try{
            if(aplicaIva.isSelected()){
                query += Boolean.TRUE + ")";
            }else{
                query += Boolean.FALSE + ")";
            }
            stmt.executeUpdate(query);
            stmt.close();
            con.close();
            Elemento.log.info("El producto " + descripcion.getText().trim() + " fue agregado correctamente...");
            cargarProductos();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getCause().getMessage());
            Elemento.log.error("Excepcion al agregar un producto: " + e.getMessage(),e);
        }
    }
    
    private void actualizarProducto(String claveActual){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        String query = "UPDATE Productos SET "
                    + "noIdentificacion=\'"+noIdentificacion.getText().trim()+"\',descripcion=\'"+descripcion.getText().trim()+"\', unidad=\'"+unidad.getSelectedItem().toString().trim()+"\',"
                + "precio=\'"+precio.getText().trim()+"\',aplicaIva=";
        try{
            if(aplicaIva.isSelected()){
                query += Boolean.TRUE;
            }else{
                query += Boolean.FALSE;
            }
            query += " WHERE noIdentificacion like \'"+claveActual+"\'";
            stmt.executeUpdate(query);
            stmt.close();
            con.close();
            Elemento.log.info("El producto " + descripcion.getText().trim() + " fue actualizado correctamente...");
            cargarProductos();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getCause().getMessage());
            Elemento.log.error("Excepcion al agregar un producto: " + e.getMessage(),e);
        }
    }
    
    private void borrarProducto(String claveActual){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        try{
            stmt.executeUpdate("DELETE FROM Productos WHERE noIdentificacion like \'"+claveActual+"\'");
            Elemento.log.info("El producto con clave " + claveActual + " ha sido borrado");
            stmt.close();
            con.close();
            cargarProductos();
        }catch(Exception ex){
            ex.printStackTrace();
            Elemento.log.error("Excepcion al borrar el producto con clave: " + claveActual + " : " + ex.getMessage(),ex);
        }
    }
    
    private void consultarProducto(String columna,String dato){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs = null;
        try{
            rs = stmt.executeQuery("SELECT * FROM Productos WHERE "+columna+" like \'" + dato + "\'");
            if(rs.next()){
                noIdentificacion.setText(rs.getString(1));
                descripcion.setText(rs.getString(2));
                unidad.setSelectedItem(rs.getString(3).trim());
                precio.setText(""+rs.getDouble(4));
                aplicaIva.setSelected(rs.getBoolean(5));
            }
        }catch(Exception ex){
            ex.printStackTrace();
            Elemento.log.error("Excepcion al consultar el producto: " + dato + " : " + ex.getMessage(),ex);
        }finally{
            try{
                if(rs != null){
                    rs.close();
                }
                stmt.close();
                con.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
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
            java.util.logging.Logger.getLogger(AddProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddProductos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddProductos().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton actualizar;
    private javax.swing.JButton agregar;
    private javax.swing.JCheckBox aplicaIva;
    private javax.swing.JButton borrar;
    private javax.swing.JButton cancelar;
    private javax.swing.JTextArea descripcion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField noIdentificacion;
    private javax.swing.JTextField precio;
    private javax.swing.JTable tablaProductos;
    private javax.swing.JComboBox unidad;
    // End of variables declaration//GEN-END:variables
}
