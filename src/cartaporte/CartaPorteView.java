
package cartaporte;

import cartaporte.figuras.FigurasView;
import cartaporte.mercancias.MercanciasView;
import elemento.Emisor;
import elemento.TextPrompt;
import java.awt.Font;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author abe
 */
public class CartaPorteView extends javax.swing.JFrame {
    
    private TextPrompt holder;
    private Emisor emisor;
    private String folio;
    private CartaPorte cartaPorte;

    public CartaPorteView() {
        initComponents();
        this.setLocationRelativeTo(null);
        cartaPorte = new CartaPorte();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        lblEmisor = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFolio = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        btnAgregarFiguras = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableFiguras = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableMercancias = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        btnAgregarMercancias = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableUbicaciones = new javax.swing.JTable();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        btnAgregarUbicaciones = new javax.swing.JButton();
        btnEmitir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Carta Porte");
        setResizable(false);

        jLabel1.setText("Emisor");

        jLabel2.setText("Folio");

        jLabel3.setText("Figuras Transporte");

        btnAgregarFiguras.setText("Agregar");
        btnAgregarFiguras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarFigurasActionPerformed(evt);
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

        tableMercancias.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tableMercancias);

        jLabel4.setText("Mercancias");

        btnAgregarMercancias.setText("Agregar");
        btnAgregarMercancias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarMercanciasActionPerformed(evt);
            }
        });

        tableUbicaciones.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tableUbicaciones);

        jLabel5.setText("Ubicaciones");

        btnAgregarUbicaciones.setText("Agregar");
        btnAgregarUbicaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarUbicacionesActionPerformed(evt);
            }
        });

        btnEmitir.setText("Emitir");

        btnCancelar.setText("Cancelar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCancelar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEmitir))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(btnAgregarFiguras)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(lblEmisor, javax.swing.GroupLayout.PREFERRED_SIZE, 752, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 219, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtFolio, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(btnAgregarMercancias)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(btnAgregarUbicaciones)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3)))
                .addGap(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(lblEmisor, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtFolio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAgregarFiguras)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnAgregarMercancias)
                        .addGap(0, 157, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAgregarUbicaciones)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEmitir)
                    .addComponent(btnCancelar))
                .addGap(17, 17, 17))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarFigurasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarFigurasActionPerformed
        this.setVisible(false);
        FigurasView fig = new FigurasView(this.cartaPorte, this);
        fig.setVisible(true);
    }//GEN-LAST:event_btnAgregarFigurasActionPerformed

    private void btnAgregarMercanciasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarMercanciasActionPerformed
        this.setVisible(false);
        MercanciasView mv = new MercanciasView(this.cartaPorte, this);
        mv.setVisible(true);
    }//GEN-LAST:event_btnAgregarMercanciasActionPerformed

    private void btnAgregarUbicacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarUbicacionesActionPerformed
        
    }//GEN-LAST:event_btnAgregarUbicacionesActionPerformed

    public void returnFromFigura(CartaPorte.FiguraTransporte figura){
        DefaultTableModel model = (DefaultTableModel)tableFiguras.getModel();
        Object[] rowData;
        for (CartaPorte.FiguraTransporte.TiposFigura tipo : figura.getTiposFigura()) {
            rowData = new Object[4];
            rowData[0] = tipo.getNombreFigura();
            rowData[1] = tipo.getRFCFigura();
            rowData[2] = tipo.getNumLicencia();
            rowData[3] = tipo.getResidenciaFiscalFigura();
            
            model.addRow(rowData);
        }
        this.setVisible(true);
    }
    
    public void returnFromMercancias(CartaPorte.Mercancias mercancias){
        DefaultTableModel model = (DefaultTableModel)tableMercancias.getModel();
        Object[] rowData;
        for (CartaPorte.Mercancias.Mercancia merca : mercancias.getMercancias()) {
            rowData = new Object[4];
            rowData[0] = merca.getCantidad();
            rowData[1] = merca.getUnidad();
            rowData[2] = merca.getDescripcion();
            rowData[3] = merca.getValorMercancia();
            
            model.addRow(rowData);
        }
        this.setVisible(true);
    }
    
    private void setHolders(){
        //Holders
    }
    

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CartaPorteView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CartaPorteView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CartaPorteView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CartaPorteView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CartaPorteView().setVisible(true);
            }
        });
    }
        
    public Emisor getEmisor() {
        return emisor;
    }

    public void setEmisor(Emisor emisor) {
        this.emisor = emisor;
        this.lblEmisor.setText(emisor.getNombre());
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
        this.txtFolio.setText(folio);
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarFiguras;
    private javax.swing.JButton btnAgregarMercancias;
    private javax.swing.JButton btnAgregarUbicaciones;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEmitir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblEmisor;
    private javax.swing.JTable tableFiguras;
    private javax.swing.JTable tableMercancias;
    private javax.swing.JTable tableUbicaciones;
    private javax.swing.JTextField txtFolio;
    // End of variables declaration//GEN-END:variables
}
