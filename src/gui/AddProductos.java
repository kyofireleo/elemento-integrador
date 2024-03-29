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
    Integer idClaveSat, idClaveUnidadSat;
    
    public AddProductos() {
        initComponents();
        setLocationRelativeTo(null);
        tablaProductos.getColumnModel().getColumn(7).setMaxWidth(0);
        tablaProductos.getColumnModel().getColumn(7).setMinWidth(0);
        tablaProductos.getColumnModel().getColumn(7).setPreferredWidth(0);
        
        tablaProductos.getColumnModel().getColumn(8).setMaxWidth(0);
        tablaProductos.getColumnModel().getColumn(8).setMinWidth(0);
        tablaProductos.getColumnModel().getColumn(8).setPreferredWidth(0);
        tablaProductos.doLayout();
        cargarProductos();
    }
    
    private void cargarProductos(){
        borrarCampos();
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs = null;
        String query = "SELECT p.noIdentificacion, p.c_claveunidad_id, cu.claveunidad, "
                    + "cu.nombre as unidad, p.descripcion, "
                    + "p.precio, p.aplicaIva, p.c_claveprodserv_id, cc.claveprodserv "
                    + "FROM (Productos p "
                    + "INNER JOIN c_claveprodserv cc ON p.c_claveprodserv_id = cc.c_claveprodserv_id) "
                    + "INNER JOIN c_claveunidad cu ON p.c_claveunidad_id = cu.c_claveunidad_id";
        System.out.println(query);
        try{
            rs = stmt.executeQuery(query);
            model = (DefaultTableModel) tablaProductos.getModel();
            model.setRowCount(0);
            String noIden,claveuni,uni,desc,prec,clave;
            Integer idClave, idClaveUni;
            Boolean iva;
            while(rs.next()){
                noIden = rs.getString("noIdentificacion");
                claveuni = rs.getString("claveunidad");
                uni = rs.getString("unidad");
                idClaveUni = rs.getInt("c_claveunidad_id");
                desc = rs.getString("descripcion");
                prec = ""+rs.getDouble("precio");
                iva = rs.getBoolean("aplicaIva");
                clave = rs.getString("claveprodserv");
                idClave = rs.getInt("c_claveprodserv_id");
                
                Object [] row = {noIden,claveuni,uni,desc,prec,clave,iva,idClave,idClaveUni};
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
        claveunidad.setText("");
        descripcion.setText("");
        precio.setText("");
        aplicaIva.setSelected(false);
        claveprodserv.setText("");
        idClaveSat = null;
        claveunidad.transferFocusBackward();
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
        claveprodserv = new javax.swing.JTextField();
        buscarClaveProd = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        claveunidad = new javax.swing.JTextField();
        buscarClaveUni = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Código");

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
                "Código", "Clave Unidad", "Unidad", "Descripcion", "Precio", "Clave SAT", "Aplica IVA", "idClaveSat", "idClaveUnidadSat"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false, false, false, false
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
        if (tablaProductos.getColumnModel().getColumnCount() > 0) {
            tablaProductos.getColumnModel().getColumn(0).setResizable(false);
            tablaProductos.getColumnModel().getColumn(0).setPreferredWidth(100);
            tablaProductos.getColumnModel().getColumn(1).setPreferredWidth(100);
            tablaProductos.getColumnModel().getColumn(2).setResizable(false);
            tablaProductos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tablaProductos.getColumnModel().getColumn(3).setResizable(false);
            tablaProductos.getColumnModel().getColumn(3).setPreferredWidth(200);
            tablaProductos.getColumnModel().getColumn(4).setResizable(false);
            tablaProductos.getColumnModel().getColumn(4).setPreferredWidth(100);
            tablaProductos.getColumnModel().getColumn(5).setResizable(false);
            tablaProductos.getColumnModel().getColumn(5).setPreferredWidth(100);
            tablaProductos.getColumnModel().getColumn(6).setResizable(false);
            tablaProductos.getColumnModel().getColumn(6).setPreferredWidth(100);
            tablaProductos.getColumnModel().getColumn(7).setResizable(false);
            tablaProductos.getColumnModel().getColumn(8).setResizable(false);
        }

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

        claveprodserv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                claveprodservActionPerformed(evt);
            }
        });

        buscarClaveProd.setText("...");
        buscarClaveProd.setToolTipText("Buscar Clave de Producto o Servicio");
        buscarClaveProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarClaveProdActionPerformed(evt);
            }
        });

        jLabel5.setText("Clave SAT");

        buscarClaveUni.setText("...");
        buscarClaveUni.setToolTipText("Buscar Clave de Unidad de Medida");
        buscarClaveUni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarClaveUniActionPerformed(evt);
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
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(precio, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12)
                                        .addComponent(jLabel5))
                                    .addComponent(noIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(claveunidad, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(buscarClaveUni)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel3))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addComponent(claveprodserv, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(buscarClaveProd)
                                        .addGap(18, 18, 18)
                                        .addComponent(aplicaIva))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(168, 168, 168)
                                .addComponent(agregar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(actualizar)
                                .addGap(18, 18, 18)
                                .addComponent(borrar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE)
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
                .addGap(15, 15, 15)
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
                            .addComponent(claveunidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buscarClaveUni))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(aplicaIva)
                            .addComponent(claveprodserv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buscarClaveProd)
                            .addComponent(jLabel5))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(agregar)
                            .addComponent(actualizar)
                            .addComponent(borrar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
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
        claveunidad.setText(model.getValueAt(row, 1).toString().trim());
        descripcion.setText(model.getValueAt(row, 3).toString());
        precio.setText(model.getValueAt(row, 4).toString());
        claveprodserv.setText(model.getValueAt(row, 5).toString());
        aplicaIva.setSelected((Boolean)model.getValueAt(row, 6));
        idClaveSat = new Integer(model.getValueAt(row,7).toString());
        idClaveUnidadSat = new Integer(model.getValueAt(row,8).toString());
    }//GEN-LAST:event_tablaProductosMouseClicked

    private void claveprodservActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_claveprodservActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_claveprodservActionPerformed

    private void buscarClaveProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarClaveProdActionPerformed
        // TODO add your handling code here:
        ClaveProdServ cl = new ClaveProdServ();
        cl.setVisible(true);
        cl.setVentanaActual(this);
    }//GEN-LAST:event_buscarClaveProdActionPerformed

    private void buscarClaveUniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarClaveUniActionPerformed
        // TODO add your handling code here:
        ClavesUnidad cu = new ClavesUnidad();
        cu.setVisible(true);
        cu.setVentanaActual(this);
    }//GEN-LAST:event_buscarClaveUniActionPerformed
    
    public void setClaveSat(String clave){
        claveprodserv.setText(clave);
    }
    
    public void setClaveUnidadSat(String clave){
        claveunidad.setText(clave);
    }
    
    private void agregarProducto(){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        String query = "INSERT INTO Productos (noIdentificacion,descripcion,c_claveunidad_id,precio,aplicaIva,c_claveprodserv_id) VALUES ("
                    + "\'"+noIdentificacion.getText().trim()+"\',\'"+descripcion.getText().trim()+"\', "+idClaveUnidadSat+", "
                    +precio.getText().trim()+",";
        try{
            if(aplicaIva.isSelected()){
                query += Boolean.TRUE;
            }else{
                query += Boolean.FALSE;
            }
            
            query += ", " + idClaveSat + ")";
            System.out.println(query);
            stmt.executeUpdate(query);
            stmt.close();
            con.close();
            Elemento.log.info("El producto " + descripcion.getText().trim() + " fue agregado correctamente...");
            cargarProductos();
        }catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getCause().getMessage());
            Elemento.log.error("Excepcion al agregar un producto: " + e.getMessage(),e);
        }
    }
    
    private void actualizarProducto(String claveActual){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        String query = "UPDATE Productos SET "
                    + "noIdentificacion=\'"+noIdentificacion.getText().trim()+"\',descripcion=\'"+descripcion.getText().trim()+"\', c_claveunidad_id="+this.idClaveUnidadSat+","
                + "precio="+precio.getText().trim()+",aplicaIva=";
        try{
            if(aplicaIva.isSelected()){
                query += Boolean.TRUE;
            }else{
                query += Boolean.FALSE;
            }
            query += ", c_claveprodserv_id=" + idClaveSat
                    + " WHERE noIdentificacion = \'"+claveActual+"\'";
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
            stmt.executeUpdate("DELETE FROM Productos WHERE noIdentificacion = \'"+claveActual+"\'");
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
        String query = "SELECT p.noIdentificacion, p.descripcion, u.claveunidad, p.precio, "
                    + "p.aplicaIva, s.claveprodserv, p.c_claveprodserv_id, p.c_claveunidad_id "
                    + "FROM (Productos p "
                    + "INNER JOIN c_claveunidad u ON p.c_claveunidad_id = u.c_claveunidad_id) "
                    + "INNER JOIN c_claveprodserv s ON p.c_claveprodserv_id = s.c_claveprodserv_id"
                    + " WHERE "+columna+" = \'" + dato + "\'";
        System.out.println(query);
        try{
            rs = stmt.executeQuery(query);
            if(rs.next()){
                noIdentificacion.setText(rs.getString("noIdentificacion"));
                descripcion.setText(rs.getString("descripcion"));
                claveunidad.setText(rs.getString("claveunidad"));
                precio.setText(""+rs.getDouble("precio"));
                aplicaIva.setSelected(rs.getBoolean("aplicaIva"));
                claveprodserv.setText((rs.getString("claveprodserv")));
                idClaveSat = rs.getInt("c_claveprodserv_id");
                idClaveUnidadSat = rs.getInt("c_claveunidad_id");
            }
        }catch(SQLException ex){
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
    private javax.swing.JButton buscarClaveProd;
    private javax.swing.JButton buscarClaveUni;
    private javax.swing.JButton cancelar;
    private javax.swing.JTextField claveprodserv;
    private javax.swing.JTextField claveunidad;
    private javax.swing.JTextArea descripcion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField noIdentificacion;
    private javax.swing.JTextField precio;
    private javax.swing.JTable tablaProductos;
    // End of variables declaration//GEN-END:variables

    public void setIdClaveSat(Integer idClaveSat) {
        this.idClaveSat = idClaveSat;
    }
    
    public void setIdClaveUnidadSat(Integer idClaveUnidadSat) {
        this.idClaveUnidadSat = idClaveUnidadSat;
    }
}
