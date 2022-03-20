/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.mxrck.autocompleter.AutoCompleterCallback;
import com.mxrck.autocompleter.TextAutoCompleter;
import elemento.ConnectionFactory;
import elemento.Elemento;
import java.awt.Component;
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
public class CteUpdate extends javax.swing.JFrame {

    DefaultComboBoxModel combo;
    List<String> listaEstados = new ArrayList();
    ConnectionFactory factory = new ConnectionFactory();
    String idCliente;
    int idEmpleado;
    String[] ctes;
    TextAutoCompleter texter;
    private boolean isNew = false;
    
    public CteUpdate() {
        initComponents();
        setLocationRelativeTo(null);
        ctes = consultarCtes();

        texter = new TextAutoCompleter(nombreCte, new AutoCompleterCallback() {
            @Override
            public void callback(Object selectedItem) {
                System.out.println("El usuario seleccionó: " + selectedItem);
                obtenerCliente(selectedItem.toString());
            }
        });

        texter.setCaseSensitive(false);
        texter.setItems(ctes);
        texter.setMode(0);
    }
    
    public CteUpdate(String idCliente) {
        initComponents();
        setLocationRelativeTo(null);
        this.consulCtes(idCliente);
        this.crearComprobante.setVisible(false);
        this.nombreCte.setVisible(false);
        this.jLabel1.setVisible(false);
        this.nuevoCliente.setVisible(false);
    }
    
    private String[] consultarCtes() {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        List<String> items = new ArrayList();
        try {
            rs = stmt.executeQuery("SELECT id,nombre FROM Clientes");
            while (rs.next()) {
                items.add(rs.getInt("id") + "," + rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al consultar el cliente: " + ex.getMessage(), ex);
        }
        String[] c = items.toArray(new String[items.size()]);
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

        jLabel1 = new javax.swing.JLabel();
        guardar = new javax.swing.JButton();
        cancelar = new javax.swing.JButton();
        borrar = new javax.swing.JButton();
        nuevoCliente = new javax.swing.JButton();
        nombreCte = new javax.swing.JTextField();
        crearComprobante = new javax.swing.JButton();
        panelControles = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        noExterior = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        pais = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        estado = new javax.swing.JComboBox();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cp = new javax.swing.JTextField();
        nombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        calle = new javax.swing.JTextField();
        municipio = new javax.swing.JTextField();
        localidad = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        colonia = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        noInterior = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        rfc = new javax.swing.JTextField();
        editarCte = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Clientes");
        setResizable(false);

        jLabel1.setText("Escriba el nombre del cliente para buscarlo y presione Enter");

        guardar.setText("Guardar");
        guardar.setEnabled(false);
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
        borrar.setEnabled(false);
        borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarActionPerformed(evt);
            }
        });

        nuevoCliente.setText("Nuevo Cliente");
        nuevoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoClienteActionPerformed(evt);
            }
        });

        nombreCte.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nombreCteFocusGained(evt);
            }
        });
        nombreCte.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nombreCteKeyPressed(evt);
            }
        });

        crearComprobante.setText("Crear Comprobante");
        crearComprobante.setEnabled(false);
        crearComprobante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                crearComprobanteActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("RFC");

        noExterior.setEnabled(false);

        jLabel12.setText("No. Exterior");

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("e-mail");

        email.setEnabled(false);
        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });

        pais.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "México", "Estatos Unidos", "Canadá" }));
        pais.setEnabled(false);
        pais.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paisActionPerformed(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Estado");

        estado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione un Estado", "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Cd. de México", "Chiapas", "Chihuahua", "Coahuila", "Colima", "Durango", "Estado de México", "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Michoacán", "Morelos", "Nayarit", "Nuevo León", "Oaxaca", "Puebla", "Querétaro", "Quintana Roo", "San Luis Potosí", "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatán", "Zacatecas" }));
        estado.setEnabled(false);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("País");

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("C.P.");

        cp.setEnabled(false);

        nombre.setEnabled(false);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Nombre");

        jLabel5.setText("No. Interior");

        calle.setEnabled(false);

        municipio.setEnabled(false);

        localidad.setEnabled(false);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Municipio");

        colonia.setEnabled(false);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Localidad");

        noInterior.setEnabled(false);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Colonia");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Calle");

        rfc.setEnabled(false);

        javax.swing.GroupLayout panelControlesLayout = new javax.swing.GroupLayout(panelControles);
        panelControles.setLayout(panelControlesLayout);
        panelControlesLayout.setHorizontalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelControlesLayout.createSequentialGroup()
                        .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelControlesLayout.createSequentialGroup()
                                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelControlesLayout.createSequentialGroup()
                                        .addComponent(noExterior, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel5))
                                    .addComponent(colonia, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(localidad, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(municipio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(noInterior, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelControlesLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(calle, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelControlesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelControlesLayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rfc, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelControlesLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(31, 31, 31))
        );
        panelControlesLayout.setVerticalGroup(
            panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelControlesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelControlesLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rfc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(calle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(noExterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(noInterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(colonia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(localidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(municipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(pais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(estado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(11, 11, 11)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelControlesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        editarCte.setText("Editar Cliente");
        editarCte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarCteActionPerformed(evt);
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
                            .addComponent(panelControles, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(crearComprobante)
                                .addGap(18, 18, 18)
                                .addComponent(guardar)
                                .addGap(18, 18, 18)
                                .addComponent(borrar)
                                .addGap(18, 18, 18)
                                .addComponent(cancelar)))
                        .addGap(0, 15, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nombreCte)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(editarCte)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(nuevoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nombreCte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nuevoCliente)
                    .addComponent(editarCte))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelControles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(guardar)
                    .addComponent(cancelar)
                    .addComponent(borrar)
                    .addComponent(crearComprobante))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed
        // TODO add your handling code here:
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        
        String msg;
        String query;
        try {
            String country = pais.getSelectedItem().toString().trim();
            
            if (!isNew) {
                String id = this.idCliente;
                query = "UPDATE Clientes SET "
                    + "nombre = \'" + nombre.getText().trim() + "\', rfc = \'" + rfc.getText().trim() + "\', calle = \'" + calle.getText().trim() + "\', "
                    + "noExterior = \'" + noExterior.getText().trim() + "\', noInterior = \'" + noInterior.getText().trim() + "\', "
                    + "colonia = \'" + colonia.getText().trim() + "\', localidad = \'" + localidad.getText().trim() + "\', "
                    + "municipio = \'" + municipio.getText().trim() + "\', estado = \'" + estado.getSelectedItem().toString() + "\', "
                    + "pais = \'" + country + "\', cp = \'" + cp.getText().trim() + "\', email = \'" + email.getText().trim() + "\'"
                    + " WHERE id = " + id;
                System.out.println(query);
                stmt.executeUpdate(query);
                
                msg = "El cliente " + nombre.getText().trim() + " fue actualizado correctamente";
                
            } else {
                this.isNew = false;
                query = "INSERT INTO Clientes (nombre,rfc,calle,noExterior,noInterior,colonia,localidad,municipio,estado,pais,cp,email,tipoPersona,regimenFiscal) "
                        + "VALUES (\'" + nombre.getText().trim() + "\',\'" + rfc.getText().trim() + "\',\'" + calle.getText().trim() + "\',"
                        + "\'" + noExterior.getText().trim() + "\',\'" + noInterior.getText().trim() + "\',"
                        + "\'" + colonia.getText().trim() + "\',\'" + localidad.getText().trim() + "\',"
                        + "\'" + municipio.getText().trim() + "\',\'" + estado.getSelectedItem().toString() + "\',"
                        + "\'" + country + "\',\'" + cp.getText().trim() + "\',\'" + email.getText().trim() + "\')";
                System.out.println(query);
                stmt.execute(query);
            
                msg = "El cliente " + nombre.getText() + " fue agregado correctamente";
            }
            JOptionPane.showMessageDialog(null, msg);
            Elemento.log.info(msg);
            
            stmt.close();
            con.close();
            
            this.borrarTodo();
            ctes = consultarCtes();
            texter.setItems(ctes);
        } catch (SQLException e) {
            e.printStackTrace();
            msg = "Excepcion: No se pudo actualizar al cliente a la base de datos: " + e.getLocalizedMessage();
            Elemento.log.error(msg, e);
            JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Error err) {
            err.printStackTrace();
            msg = "Error: No se pudo actualizar al cliente a la base de datos: " + err.getLocalizedMessage();
            Elemento.log.error(msg, err);
            JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_guardarActionPerformed
    
    private int selecEstado(String state) {
        for (int i = 0; i < estado.getItemCount(); i++) {
            if (state.equalsIgnoreCase(estado.getItemAt(i).toString())) {
                return i;
            }
        }
        return 0;
    }
    
    private void consulCtes(String idCliente) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        String query = "SELECT * FROM Clientes WHERE id = " + idCliente;
        try {
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                rs.previous();
                if (rs.next()) {
                    this.idCliente = rs.getString("id");
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
                    
                }
            } else {
                this.borrarTodo();
            }
            
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            Elemento.log.error("Excepcion: No se pudo consultar los Clientes: " + ex.getLocalizedMessage(), ex);
            ex.printStackTrace();
        }
    }
    
    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_cancelarActionPerformed
    
    private void borrarTodo() {
        nombreCte.setText("");
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
        
        if(!isNew){
            nombreCte.requestFocus();
        }else{
            nombre.requestFocus();
        }
        
        this.idCliente = "";
        this.isNew = false;
        this.setEnabledControls(false);
    }
    
    private void borrarCliente() {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        try {
            stmt.executeUpdate("DELETE FROM Clientes WHERE id = " + idCliente);
            JOptionPane.showMessageDialog(null, "El cliente" + nombre.getText() + " fue eliminado correctamente");
            stmt.close();
            con.close();
            this.borrarTodo();
            ctes = consultarCtes();
            texter.setItems(ctes);
        } catch (SQLException | HeadlessException ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al eliminar el cliente " + nombre.getText() + " : " + ex.getMessage(), ex);
            JOptionPane.showMessageDialog(null, "Excepcion al eliminar el cliente " + nombre.getText() + " : " + ex.getMessage());
        }
    }
    
    private void borrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarActionPerformed
        // TODO add your handling code here:
        int opc = JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar el cliente?", "Eliminar Cliente", JOptionPane.YES_NO_OPTION);
        switch (opc) {
            case JOptionPane.YES_OPTION:
                borrarCliente();
                this.borrarTodo();
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
        
        if (listaEstados.isEmpty()) {
            for (int i = 0; i < comboE.getSize(); i++) {
                listaEstados.add(comboE.getElementAt(i).toString());
            }
        }
        
        if (pais.getSelectedIndex() > 0) {
            comboE.removeAllElements();
            comboE.addElement("Estado Extranjero");
        } else {
            comboE.removeAllElements();
            for (int i = 0; i < listaEstados.size(); i++) {
                comboE.addElement(listaEstados.get(i));
            }
        }
    }//GEN-LAST:event_paisActionPerformed

    private void nuevoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoClienteActionPerformed
        // TODO add your handling code here:
        this.borrarTodo();
        this.isNew = true;
        setEnabledControls(true);
    }//GEN-LAST:event_nuevoClienteActionPerformed

    private void nombreCteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreCteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_nombreCteKeyPressed

    private void nombreCteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nombreCteFocusGained
        // TODO add your handling code here:
        //nombreCte.selectAll();
    }//GEN-LAST:event_nombreCteFocusGained

    private void setEnabledControls(boolean enabled) {
        for (Component c : panelControles.getComponents()) {
            if (!(c instanceof javax.swing.JLabel)) {
                if (c != null) {
                    c.setEnabled(enabled);
                }
            }
        }

        this.crearComprobante.setEnabled(!this.isNew);
        this.borrar.setEnabled(!this.isNew);
        this.editarCte.setEnabled(!this.isNew);
        this.guardar.setEnabled(enabled);

        if (!this.isNew) {
            this.guardar.setText("Guardar");
        } else {
            this.guardar.setText("Agregar");
        }
        
        if(enabled){
            this.nombre.requestFocus();
        }
    }

    private void obtenerCliente(String cliente) {
        if (nombreCte.getText().length() >= 3) {
            this.isNew = false;
            setEnabledControls(false);
            this.consulCtes(cliente.split(",")[0]);
        } else {
            JOptionPane.showMessageDialog(null, "Tiene que haber por lo menos 3 caracteres en el nombre");
        }
    }

    private void crearComprobanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_crearComprobanteActionPerformed
        // TODO add your handling code here:
        if (!(idCliente == null || idCliente.isEmpty())) {
            Factura_View fw = new Factura_View(Integer.parseInt(idCliente));
            fw.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Debe consultar un cliente primero");
        }
    }//GEN-LAST:event_crearComprobanteActionPerformed

    private void editarCteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarCteActionPerformed
        // TODO add your handling code here:
        this.setEnabledControls(true);
    }//GEN-LAST:event_editarCteActionPerformed

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
            java.util.logging.Logger.getLogger(CteUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CteUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CteUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CteUpdate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new CteUpdate().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton borrar;
    private javax.swing.JTextField calle;
    private javax.swing.JButton cancelar;
    private javax.swing.JTextField colonia;
    private javax.swing.JTextField cp;
    public javax.swing.JButton crearComprobante;
    private javax.swing.JButton editarCte;
    private javax.swing.JTextField email;
    private javax.swing.JComboBox estado;
    private javax.swing.JButton guardar;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JTextField nombreCte;
    private javax.swing.JButton nuevoCliente;
    private javax.swing.JComboBox pais;
    private javax.swing.JPanel panelControles;
    private javax.swing.JTextField rfc;
    // End of variables declaration//GEN-END:variables
}
