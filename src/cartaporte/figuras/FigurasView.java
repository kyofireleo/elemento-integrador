package cartaporte.figuras;

import cartaporte.CartaPorte;
import cartaporte.CartaPorteView;
import elemento.TextPrompt;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author abe
 */
public class FigurasView extends javax.swing.JFrame {

    private TextPrompt holder;
    private CartaPorte.FiguraTransporte figura;
    private CartaPorteView cartaPorteView;

    public FigurasView(CartaPorte cp, CartaPorteView cartaPorteView) {
        initComponents();
        this.setSize(new Dimension(1028,702));
        this.setLocationRelativeTo(null);
        
        figura = cp.new FiguraTransporte();
        this.cartaPorteView = cartaPorteView;
        setHolders();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtFiguraNombre = new javax.swing.JTextField();
        txtFiguraRFC = new javax.swing.JTextField();
        txtFiguraNumLicencia = new javax.swing.JTextField();
        comboFiguraPais = new javax.swing.JComboBox<>();
        txtDomicilioCalle = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        txtDomicilioNumExt = new javax.swing.JTextField();
        txtDomicilioNumInt = new javax.swing.JTextField();
        txtDomicilioColonia = new javax.swing.JTextField();
        txtDomicilioCP = new javax.swing.JTextField();
        txtDomicilioLocalidad = new javax.swing.JTextField();
        txtDomicilioMunicipio = new javax.swing.JTextField();
        comboDomicilioEstado = new javax.swing.JComboBox<>();
        comboDomicilioPais = new javax.swing.JComboBox<>();
        txtDomicilioReferencia = new javax.swing.JTextField();
        btnAgregar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableFiguras = new javax.swing.JTable();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Figuras Transporte");
        setResizable(false);

        txtFiguraNombre.setToolTipText("Nombre Figura");

        comboFiguraPais.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecciona un pais", "MEX" }));

        jLabel4.setText("Domicilio");

        comboDomicilioEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecciona un estado", "Aguascalientes", "Baja California", "Baja California Sur", "Campeche", "Chiapas", "Chihuahua", "Coahuila", "Colima", "Ciudad de México", "Durango", "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Estado de México", "Michoacán", "Morelos", "Nayarit", "Nuevo León", "Oaxaca", "Puebla", "Querétaro", "Quintana Roo", "San Luis Potosí", "Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatán", "Zacatecas" }));

        comboDomicilioPais.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecciona un pais", "MEX", "USA", "CAN" }));

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/add.png"))); // NOI18N
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        tableFiguras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "RFC", "Num Licencia", "Pais"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tableFiguras);

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/edit-29.png"))); // NOI18N
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnEliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/DeleteRed.png"))); // NOI18N
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
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
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txtDomicilioCalle, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtDomicilioNumExt)
                        .addGap(18, 18, 18)
                        .addComponent(txtDomicilioNumInt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtDomicilioColonia, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtDomicilioCP, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtFiguraNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtFiguraRFC, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtFiguraNumLicencia, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(comboFiguraPais, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 58, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(894, 894, 894)
                                .addComponent(btnEditar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEliminar))
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnCancelar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAceptar)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtDomicilioReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAgregar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(txtDomicilioLocalidad, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtDomicilioMunicipio, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comboDomicilioPais, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(comboDomicilioEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFiguraNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFiguraRFC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFiguraNumLicencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboFiguraPais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDomicilioCalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDomicilioNumExt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDomicilioNumInt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDomicilioColonia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDomicilioCP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDomicilioLocalidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDomicilioMunicipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboDomicilioEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboDomicilioPais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAgregar)
                    .addComponent(txtDomicilioReferencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEliminar)
                    .addComponent(btnEditar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAceptar)
                    .addComponent(btnCancelar))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        if(validarDatos()){
            CartaPorte.FiguraTransporte.TiposFigura tipo = figura.new TiposFigura();
            tipo.setNombreFigura(txtFiguraNombre.getText().trim());
            tipo.setRFCFigura(txtFiguraRFC.getText().trim());
            tipo.setNumLicencia(txtFiguraNumLicencia.getText().trim());
            tipo.setResidenciaFiscalFigura(comboFiguraPais.getSelectedItem().toString());
            
            CartaPorte.FiguraTransporte.TiposFigura.Domicilio dom = tipo.new Domicilio();
            dom.setCalle(txtDomicilioCalle.getText().trim());
            dom.setNumeroExterior(txtDomicilioNumExt.getText().trim());
            dom.setNumeroInterior(txtDomicilioNumInt.getText().trim());
            dom.setColonia(txtDomicilioColonia.getText().trim());
            dom.setCodigoPostal(txtDomicilioCP.getText().trim());
            dom.setLocalidad(txtDomicilioLocalidad.getText().trim());
            dom.setMunicipio(txtDomicilioMunicipio.getText().trim());
            dom.setEstado(comboDomicilioEstado.getSelectedItem().toString());
            dom.setPais(comboDomicilioPais.getSelectedItem().toString());
            dom.setReferencia(txtDomicilioReferencia.getText().trim());
            
            tipo.setDomicilio(dom);
            figura.getTiposFigura().add(tipo);
            llenarTabla(tipo);
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        this.setVisible(false);
        this.cartaPorteView.returnFromFigura(this.figura);
        this.dispose();
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        this.cartaPorteView.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        int row = this.tableFiguras.getSelectedRow();
        CartaPorte.FiguraTransporte.TiposFigura tipo = this.figura.getTiposFigura().get(row);
        
        this.txtFiguraNombre.setText(tipo.getNombreFigura());
        this.txtFiguraNumLicencia.setText(tipo.getNumLicencia());
        this.txtFiguraRFC.setText(tipo.getRFCFigura());
        this.comboFiguraPais.setSelectedItem(tipo.getResidenciaFiscalFigura());
        
        this.txtDomicilioCalle.setText(tipo.getDomicilio().getCalle());
        this.txtDomicilioNumExt.setText(tipo.getDomicilio().getNumeroExterior());
        this.txtDomicilioNumInt.setText(tipo.getDomicilio().getNumeroInterior());
        this.txtDomicilioColonia.setText(tipo.getDomicilio().getColonia());
        this.txtDomicilioCP.setText(tipo.getDomicilio().getCodigoPostal());
        this.txtDomicilioLocalidad.setText(tipo.getDomicilio().getLocalidad());
        this.txtDomicilioMunicipio.setText(tipo.getDomicilio().getMunicipio());
        this.txtDomicilioReferencia.setText(tipo.getDomicilio().getReferencia());
        this.comboDomicilioEstado.setSelectedItem(tipo.getDomicilio().getEstado());
        this.comboDomicilioPais.setSelectedItem(tipo.getDomicilio().getPais());
        
        eliminarTipoFiguraSeleccionada(row);
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int row = this.tableFiguras.getSelectedRow();
        eliminarTipoFiguraSeleccionada(row);
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void eliminarTipoFiguraSeleccionada(int row){
        DefaultTableModel model = (DefaultTableModel)this.tableFiguras.getModel();
        model.removeRow(row);
        
        this.figura.getTiposFigura().remove(row);
    }
    
    private boolean validarDatos(){
        
        return true;
    }
    
    private void llenarTabla(CartaPorte.FiguraTransporte.TiposFigura tipo){
        DefaultTableModel model = (DefaultTableModel)tableFiguras.getModel();
        Object[] row = new Object[4];
        row[0] = tipo.getNombreFigura();
        row[1] = tipo.getRFCFigura();
        row[2] = tipo.getNumLicencia();
        row[3] = tipo.getResidenciaFiscalFigura();
        
        model.addRow(row);
    }
    
    public CartaPorte.FiguraTransporte getFiguraTransporte(){
        return this.figura;
    }
    
    private void setHolders(){
        holder = new TextPrompt("Nombre", txtFiguraNombre);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
        
        holder = new TextPrompt("RFC", txtFiguraRFC);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
        
        holder = new TextPrompt("Num Licencia", txtFiguraNumLicencia);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
        
        holder = new TextPrompt("Calle", txtDomicilioCalle);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
        txtDomicilioCalle.setToolTipText("Calle");
        
        holder = new TextPrompt("Num Exterior", txtDomicilioNumExt);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
        txtDomicilioNumExt.setToolTipText("Num Exterior");
        
        holder = new TextPrompt("Num Interior", txtDomicilioNumInt);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
        txtDomicilioNumInt.setToolTipText("Num Interior");
        
        holder = new TextPrompt("Colonia", txtDomicilioColonia);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
        txtDomicilioColonia.setToolTipText("Colonia");
        
        holder = new TextPrompt("CP", txtDomicilioCP);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
        txtDomicilioCP.setToolTipText("Codigo Postal");
        
        holder = new TextPrompt("Localidad", txtDomicilioLocalidad);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
        txtDomicilioLocalidad.setToolTipText("Localidad");
        
        holder = new TextPrompt("Municipio", txtDomicilioMunicipio);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
        txtDomicilioMunicipio.setToolTipText("Municipio");
        
        holder = new TextPrompt("Referencia", txtDomicilioReferencia);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
        txtDomicilioReferencia.setToolTipText("Referencia");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JComboBox<String> comboDomicilioEstado;
    private javax.swing.JComboBox<String> comboDomicilioPais;
    private javax.swing.JComboBox<String> comboFiguraPais;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable tableFiguras;
    private javax.swing.JTextField txtDomicilioCP;
    private javax.swing.JTextField txtDomicilioCalle;
    private javax.swing.JTextField txtDomicilioColonia;
    private javax.swing.JTextField txtDomicilioLocalidad;
    private javax.swing.JTextField txtDomicilioMunicipio;
    private javax.swing.JTextField txtDomicilioNumExt;
    private javax.swing.JTextField txtDomicilioNumInt;
    private javax.swing.JTextField txtDomicilioReferencia;
    private javax.swing.JTextField txtFiguraNombre;
    private javax.swing.JTextField txtFiguraNumLicencia;
    private javax.swing.JTextField txtFiguraRFC;
    // End of variables declaration//GEN-END:variables
}
