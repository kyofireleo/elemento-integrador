/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nominas;

import elemento.Elemento;
import elemento.Emisor;
import elemento.Factura;
import elemento.Layout;
import java.awt.Component;
import java.awt.HeadlessException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Abe
 */
public class NominaGeneral extends javax.swing.JFrame {

    utils.Utils util;
    utils.ConnectionFactory factory;
    int idEmisor;
    List<Integer> idEmpleados;
    List<String> numEmpleados;
    Map<Integer, Percepciones> ventanasPer;
    Map<Integer, Deducciones> ventanasDec;
    Map<Integer, OtrosPagos> ventanasOtr;
    String serie;
    String lugarExpedicion;
    private int totalRecibos = 0;
    private boolean isPtu = false;
    private BigDecimal importeTotalPtu = BigDecimal.ZERO;
    private BigDecimal importePtu = BigDecimal.ZERO;
    private BigDecimal importeIsr = BigDecimal.ZERO;
    DecimalFormat df = new DecimalFormat("#,###,###,##0.00");

    public NominaGeneral() {
        initComponents();
        this.setLocationRelativeTo(null);
        util = new utils.Utils(Elemento.log);
        factory = new utils.ConnectionFactory(Elemento.log);
        ventanasPer = new HashMap();
        ventanasDec = new HashMap();
        ventanasOtr = new HashMap();
        df.setParseBigDecimal(true);
    }

    public NominaGeneral(List<String> numEmpleados, int idEmisor, List<Integer> idEmpleados) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.idEmisor = idEmisor;
        this.idEmpleados = idEmpleados;
        this.numEmpleados = numEmpleados;
        util = new utils.Utils(Elemento.log);
        factory = new utils.ConnectionFactory(Elemento.log);
        ventanasPer = new HashMap();
        ventanasDec = new HashMap();
        ventanasOtr = new HashMap();
        df.setParseBigDecimal(true);
        llenarTabla();
    }
    
    public NominaGeneral(List<String> numEmpleados, int idEmisor, List<Integer> idEmpleados, boolean isPtu) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.idEmisor = idEmisor;
        this.idEmpleados = idEmpleados;
        this.numEmpleados = numEmpleados;
        this.isPtu = true;
        
        util = new utils.Utils(Elemento.log);
        factory = new utils.ConnectionFactory(Elemento.log);
        ventanasPer = new HashMap();
        ventanasDec = new HashMap();
        ventanasOtr = new HashMap();
        df.setParseBigDecimal(true);
        
        String cantidadTotalPtu = "";
        String cantidadIsr = "";
        int cont = 0;
        
        try{
            do{
                cantidadTotalPtu = JOptionPane.showInputDialog(null, cont > 0 
                        ? "La cantidad de PTU \"" + cantidadTotalPtu + "\" ingresada no es correcta, favor de introducirla nuevamente:" 
                        : "Ingrese la cantidad de PTU a repartir", "Cantidad de PTU", JOptionPane.QUESTION_MESSAGE
                );
                cont++;
            }while(cantidadTotalPtu.trim().isEmpty() && !util.isNumeric(util.quitarComas(cantidadTotalPtu)));
            
            cantidadTotalPtu = cantidadTotalPtu.trim();
            
            this.importeTotalPtu = (BigDecimal)df.parse(cantidadTotalPtu.contains(".") ? (cantidadTotalPtu.endsWith(".") ? (cantidadTotalPtu + "0") : cantidadTotalPtu) : (cantidadTotalPtu + ".0"));
            this.importePtu = util.redondear(importeTotalPtu.divide(new BigDecimal(idEmpleados.size()), 2, RoundingMode.HALF_UP));

            cont = 0;
            do{
                cantidadIsr = JOptionPane.showInputDialog(null, cont > 0 
                        ? "La cantidad de ISR \"" + cantidadIsr + "\" ingresada no es correcta, favor de introducirla nuevamente:" 
                        : "Ingresa el importe ISR segun el PTU individual de: " + df.format(importePtu), "Importe de ISR", JOptionPane.QUESTION_MESSAGE
                );
                cont++;
            }while(cantidadIsr.trim().isEmpty() && util.isNumeric(util.quitarComas(cantidadIsr)));
            
            this.importeIsr = (BigDecimal)df.parse(cantidadIsr);
            
        }catch(HeadlessException | ParseException e){
            e.printStackTrace();
            Elemento.log.error("Error al calcular el importe individual de PTU", e);
        }
        this.cmbTipoNomina.setSelectedIndex(1);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fechaPago = new com.toedter.calendar.JDateChooser();
        fechaInicialPago = new com.toedter.calendar.JDateChooser();
        fechaFinalPago = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEmpleados = new javax.swing.JTable();
        generarNomina = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        totalPer = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbTipoNomina = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        totalOtros = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        totalDed = new javax.swing.JLabel();
        lblNumRecibos = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nomina General");

        fechaPago.setDateFormatString("yyyy-MM-dd");
        fechaPago.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                fechaPagoInputMethodTextChanged(evt);
            }
        });
        fechaPago.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fechaPagoPropertyChange(evt);
            }
        });

        fechaInicialPago.setDateFormatString("yyyy-MM-dd");

        fechaFinalPago.setDateFormatString("yyyy-MM-dd");
        fechaFinalPago.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                fechaFinalPagoMouseExited(evt);
            }
        });
        fechaFinalPago.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fechaFinalPagoFocusLost(evt);
            }
        });
        fechaFinalPago.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fechaFinalPagoPropertyChange(evt);
            }
        });
        fechaFinalPago.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                fechaFinalPagoCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                fechaFinalPagoInputMethodTextChanged(evt);
            }
        });

        jLabel6.setText("Fecha de Pago");

        jLabel7.setText("Fecha Inicial de Pago");

        jLabel8.setText("Fecha Final de Pago");

        tablaEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numero", "Empleado", "Antiguedad", "Dias Pagados", "Percepciones", "Otros Pagos", "Deducciones"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Long.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaEmpleados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaEmpleadosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaEmpleados);

        generarNomina.setText("Timbrar");
        generarNomina.setEnabled(false);
        generarNomina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarNominaActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Total Percepciones: ");

        totalPer.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalPer.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        totalPer.setText("0.0");

        jLabel3.setText("Tipo Nomina");

        cmbTipoNomina.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ordinaria", "Extraordinaria" }));
        cmbTipoNomina.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoNominaItemStateChanged(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Total Otros Pagos: ");

        totalOtros.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalOtros.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        totalOtros.setText("0.0");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel4.setText("Total Deducciones: ");

        totalDed.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalDed.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        totalDed.setText("0.0");

        lblNumRecibos.setText("Número de Recibos: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(10, 10, 10)
                        .addComponent(fechaPago, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addGap(10, 10, 10)
                        .addComponent(fechaInicialPago, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addGap(10, 10, 10)
                        .addComponent(fechaFinalPago, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbTipoNomina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblNumRecibos, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(generarNomina)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addGap(2, 2, 2)
                                .addComponent(totalPer, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel2)
                                .addGap(2, 2, 2)
                                .addComponent(totalOtros, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(totalDed, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(fechaPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(fechaInicialPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(fechaFinalPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cmbTipoNomina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblNumRecibos)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(generarNomina, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalPer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalOtros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalDed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fechaFinalPagoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fechaFinalPagoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_fechaFinalPagoFocusLost

    private void fechaFinalPagoCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_fechaFinalPagoCaretPositionChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_fechaFinalPagoCaretPositionChanged

    private void fechaFinalPagoInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_fechaFinalPagoInputMethodTextChanged
        // TODO add your handling code here:
        llenarTabla();
    }//GEN-LAST:event_fechaFinalPagoInputMethodTextChanged

    private void fechaFinalPagoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fechaFinalPagoMouseExited
        // TODO add your handling code here:

    }//GEN-LAST:event_fechaFinalPagoMouseExited

    private void fechaFinalPagoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fechaFinalPagoPropertyChange
        // TODO add your handling code here:
        generarNomina.setEnabled(true);
        llenarTabla();
    }//GEN-LAST:event_fechaFinalPagoPropertyChange

    private void generarNominaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generarNominaActionPerformed
        int r = JOptionPane.showConfirmDialog(null, "Esta seguro que desea generar la nomina?", "Generar Nomina", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (r == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) tablaEmpleados.getModel();
            complementos.nominas.Nominas nom;
            complementos.nominas.OtrosPagos otro = new complementos.nominas.OtrosPagos();
            complementos.nominas.Deducciones dedu = new complementos.nominas.Deducciones();
            complementos.nominas.Percepciones perc = new complementos.nominas.Percepciones();
            Factura fact;
            OtrosPagos otr;
            Percepciones per;
            Deducciones dec;
            Layout lay;
            Emisor emi = this.getEmisor(idEmisor);
            int folio = getUltimoFolio(emi.getRfc());

            for (int i = 0; i < model.getRowCount(); i++) {
                String num = model.getValueAt(i, 0).toString();
                int idE = idEmpleados.get(i);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                nom = new complementos.nominas.Nominas();

                Empleado emp = this.getEmpleadoCompleto(num, idE);
                fact = this.getEmpleadoRec(emp.getIdEmpleado());
                fact.emisor = emi;
                fact.folio = "" + folio;
                folio++;
                fact.serie = serie;
                fact.metodoPago = "PUE";
                fact.formaPago = "99";
                fact.cuentaBancaria = "";
                fact.leyenda = "";
                fact.tipoCfd = "N";
                fact.lugarExpedicion = lugarExpedicion;
                fact.moneda = "MXN";
                fact.tipoCambio = "1.0";
                fact.prefactura = "";
                fact.usoCfdi = "CN01";
                
                if(!ventanasOtr.containsKey(emp.getIdEmpleado()))
                    otr = new OtrosPagos(emp.getIdEmpleado(), false);
                else
                    otr = ventanasOtr.get(emp.getIdEmpleado());
                
                if(!ventanasPer.containsKey(emp.getIdEmpleado()))
                    per = new Percepciones(emp.getIdEmpleado(), false);
                else
                    per = ventanasPer.get(emp.getIdEmpleado());
                
                if(!ventanasDec.containsKey(emp.getIdEmpleado()))
                    dec = new Deducciones(emp.getIdEmpleado(), false);
                else
                    dec = ventanasDec.get(emp.getIdEmpleado());
                
                nom.setTipoNomina(cmbTipoNomina.getSelectedItem().toString().substring(0,1));
                nom.setAntiguedad(new Integer(model.getValueAt(i, 2).toString()));
                nom.setNumDiasPagados(new Integer(model.getValueAt(i, 3).toString()));
                nom.setEmpleado(emp.getEmpleado());
                nom.setFechaPago(format.format(fechaPago.getDate()));
                nom.setFechaInicialPago(format.format(fechaInicialPago.getDate()));
                nom.setFechaFinalPago(format.format(fechaFinalPago.getDate()));
                nom.setHorasExtra(null);
                nom.setIncapacidad(null);
                nom.setRegistroPatronal(fact.emisor.getRegistroPatronal());
                
                //Seteamos otros pagos
                otro.setOtrosPagos(getOtrosPagos(otr));
                otro.setTotalOtrosPagos(otr.getTotalOtrosPagos());
                nom.setOtrosPagos(otro);
                nom.setTotalOtrosPagos(otro.getTotalOtrosPagos());
                
                //Seteamos deducciones
                dedu.setDeducciones(getDeducciones(dec));
                dedu.setTotalRetenido(dec.getTotalRetenido());
                dedu.setTotalOtras(dec.getTotalOtras());
                nom.setDeducciones(dedu);
                nom.setTotalDeducciones(util.redondear(dec.getTotalRetenido().add(dec.getTotalOtras())));

                //Seteamos percepciones
                perc.setPercepciones(getPercepciones(per));
                perc.setTotalExento(per.getTotalExento());
                perc.setTotalGravado(per.getTotalGravado());
                perc.setTotalSueldos(per.getTotalSueldos());
                nom.setPercepciones(perc);
                nom.setTotalPercepciones(per.getTotalSueldos());

                fact.conceptos = getConceptos(perc,otro,dedu);
                fact = getTotales(perc, dedu, otro, fact);

                lay = new Layout(fact, emp, nom);
            }
        }

    }//GEN-LAST:event_generarNominaActionPerformed

    private void fechaPagoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fechaPagoPropertyChange
        // TODO add your handling code here:
        if(this.cmbTipoNomina.getSelectedIndex() == 1){
            this.fechaInicialPago.setDate(this.fechaPago.getDate());
            this.fechaFinalPago.setDate(this.fechaPago.getDate());
        }
    }//GEN-LAST:event_fechaPagoPropertyChange

    private void fechaPagoInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_fechaPagoInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_fechaPagoInputMethodTextChanged

    private void tablaEmpleadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaEmpleadosMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 2 && !evt.isConsumed()){
            evt.consume();
            Percepciones per;
            Deducciones dec;
            OtrosPagos otr;
            
            int selectedRow = tablaEmpleados.getSelectedRow();
            int selectedCol = tablaEmpleados.getSelectedColumn();
            int idE = idEmpleados.get(selectedRow);
            
            switch(selectedCol){
                case 4:
                    if(!ventanasPer.containsKey(idE)){
                        per = new Percepciones(idE, true);
                        per.setTipoNomina(cmbTipoNomina.getSelectedIndex());
                        per.addComponentListener(new ComponentAdapter(){
                            public void componentHidden(ComponentEvent e){
                                llenarTabla();
                            }
                        });
                        ventanasPer.put(idE, per);
                    }else{
                        per = (Percepciones)ventanasPer.get(idE);
                    }
                    per.setVisible(true);
                    
                break;
                
                case 5:
                    if(!ventanasOtr.containsKey(idE)){
                        otr = new OtrosPagos(idE, true);
                        otr.setTipoNomina(cmbTipoNomina.getSelectedIndex());
                        otr.addComponentListener(new ComponentAdapter(){
                            public void componentHidden(ComponentEvent e){
                                llenarTabla();
                            }
                        });
                        ventanasOtr.put(idE, otr);
                    }else{
                        otr = ventanasOtr.get(idE);
                    }
                    otr.setVisible(true);
                break;
                
                case 6:
                    if(!ventanasDec.containsKey(idE)){
                        dec = new Deducciones(idE, true);
                        dec.setTipoNomina(cmbTipoNomina.getSelectedIndex());
                        dec.addComponentListener(new ComponentAdapter(){
                            public void componentHidden(ComponentEvent e){
                                llenarTabla();
                            }
                        });
                        ventanasDec.put(idE, dec);
                    }else{
                        dec = (Deducciones)ventanasDec.get(idE);
                    }
                    dec.setVisible(true);
                break;
            }
        }
    }//GEN-LAST:event_tablaEmpleadosMouseClicked

    private void cmbTipoNominaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbTipoNominaItemStateChanged
        // TODO add your handling code here:
        if(cmbTipoNomina.getSelectedIndex() == 1 && evt.getItem().equals(cmbTipoNomina.getSelectedItem())){
            if(fechaPago.getDate() == null){
                fechaPago.setDate(new Date());
            }
            fechaInicialPago.setDate(fechaPago.getDate());
            fechaFinalPago.setDate(fechaPago.getDate());
            fechaInicialPago.setEnabled(false);
            fechaFinalPago.setEnabled(false);
            if(!isPtu){
                util.print("Se cambio el valor de la fecha inicial y final al de la fecha de pago\r\nesto debido a que la nomina extraordinaria asi lo requiere.");
                DefaultTableModel model = (DefaultTableModel)tablaEmpleados.getModel();
                for (int i = 0; i < model.getRowCount(); i++) {
                    model.setValueAt(0, i, 3);
                    model.setValueAt(0.00, i, 4);
                    model.setValueAt(0.00, i, 5);
                    model.setValueAt(0.00, i, 6);

                    totalPer.setText("0.0");
                    totalDed.setText("0.0");
                    totalOtros.setText("0.0");
                }
            }
            
            ventanasPer.forEach((k,v) -> v.setTipoNomina(1));
            ventanasDec.forEach((k,v) -> v.setTipoNomina(1));
            ventanasOtr.forEach((k,v) -> v.setTipoNomina(1));
        }else if(cmbTipoNomina.getSelectedIndex() == 0 && evt.getItem().equals(cmbTipoNomina.getSelectedItem())){
            fechaInicialPago.setDate(null);
            fechaFinalPago.setDate(null);
            fechaInicialPago.setEnabled(true);
            fechaFinalPago.setEnabled(true);
            util.print("Se reinicio el valor de la fecha inicial y final, debe de registrarlas de nuevo.");
            ventanasPer.forEach((k,v) -> v.setTipoNomina(0));
            ventanasDec.forEach((k,v) -> v.setTipoNomina(0));
            ventanasOtr.forEach((k,v) -> v.setTipoNomina(0));
            llenarTabla();
        }
    }//GEN-LAST:event_cmbTipoNominaItemStateChanged

    
    public int calcularDiasPagados(Date date, Date date2) {
        try {
            if (!(date == null || date2 == null)) {
                java.util.GregorianCalendar cal = new java.util.GregorianCalendar(date.getYear(), date.getMonth(), date.getDate());
                java.util.GregorianCalendar cal2 = new java.util.GregorianCalendar(date2.getYear(), date2.getMonth(), date2.getDate());

                long difms = cal2.getTimeInMillis() - cal.getTimeInMillis();
                long difd = difms / (1000 * 60 * 60 * 24) + 1;
                return (int) difd;
            } else {
                return 0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al calcular los dias pagados: ", ex);
            return -1;
        }
    }

    public int calcularAntiguedadSemanas(Date fechaInicial, Date fechaFinalPay) {
        try {
            Date date = fechaInicial;
            Date date2;
            if(fechaFinalPay == null){
                date2 = new Date();
            }else{
                date2 = fechaFinalPay;
            }
            
            GregorianCalendar cal = new GregorianCalendar(date.getYear(), date.getMonth(), date.getDate());
            GregorianCalendar cal2 = new GregorianCalendar(date2.getYear(), date2.getMonth(), date2.getDate());

            //long difms = (cal2.getTimeInMillis()*(-1)) - cal.getTimeInMillis();
            long difms = cal2.getTimeInMillis() - cal.getTimeInMillis();
            long difd = difms / (((1000 * 60 * 60 * 24)+1) * 7);
            return (int) difd;
        } catch (Exception ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al calcular los dias pagados: ", ex);
            return -1;
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
            java.util.logging.Logger.getLogger(NominaGeneral.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NominaGeneral.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NominaGeneral.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NominaGeneral.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NominaGeneral().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbTipoNomina;
    private com.toedter.calendar.JDateChooser fechaFinalPago;
    private com.toedter.calendar.JDateChooser fechaInicialPago;
    private com.toedter.calendar.JDateChooser fechaPago;
    private javax.swing.JToggleButton generarNomina;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblNumRecibos;
    private javax.swing.JTable tablaEmpleados;
    private javax.swing.JLabel totalDed;
    private javax.swing.JLabel totalOtros;
    private javax.swing.JLabel totalPer;
    // End of variables declaration//GEN-END:variables

    private void llenarTabla() {
        DefaultTableModel model = (DefaultTableModel) tablaEmpleados.getModel();
        model.setRowCount(0);
        Object row[];
        
        BigDecimal tp = BigDecimal.ZERO;
        BigDecimal to = BigDecimal.ZERO;
        BigDecimal td = BigDecimal.ZERO;
        
        TableCellRenderer render = new TableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = new JLabel(value == null ? "" : value.toString());
                lbl.setHorizontalAlignment(SwingConstants.RIGHT);
                return lbl;
            }
        };
        
        if(numEmpleados != null){
            for (int j = 0; j < numEmpleados.size(); j++) {
                row = new Object[7];
                Empleado emp = getEmpleado(numEmpleados.get(j), idEmpleados.get(j));
                if (emp != null) {
                    Percepciones per;
                    OtrosPagos op;
                    Deducciones dec;
                    
                    if(!ventanasPer.containsKey(emp.getIdEmpleado())){
                        per = new Percepciones(emp.getIdEmpleado(), true, isPtu, importePtu);
                        per.addComponentListener(new ComponentAdapter(){
                            public void componentHidden(ComponentEvent e){
                                llenarTabla();
                            }
                        });
                        ventanasPer.put(emp.getIdEmpleado(), per);
                    }else{
                        per = ventanasPer.get(emp.getIdEmpleado());
                    }
                    
                    if(!ventanasOtr.containsKey(emp.getIdEmpleado())){
                        op = new OtrosPagos(emp.getIdEmpleado(), true);
                        op.addComponentListener(new ComponentAdapter(){
                            public void componentHidden(ComponentEvent e){
                                llenarTabla();
                            }
                        });
                        ventanasOtr.put(emp.getIdEmpleado(), op);
                    }else{
                        op = ventanasOtr.get(emp.getIdEmpleado());
                    }
                    
                    if(!ventanasDec.containsKey(emp.getIdEmpleado())){
                        dec = new Deducciones(emp.getIdEmpleado(), true, isPtu, importeIsr);
                        dec.addComponentListener(new ComponentAdapter(){
                            public void componentHidden(ComponentEvent e){
                                llenarTabla();
                            }
                        });
                        ventanasDec.put(emp.getIdEmpleado(), dec);
                    }else{
                        dec = ventanasDec.get(emp.getIdEmpleado());
                    }
                    
                    //Se igualan a cero cuando el tipo de nomina es Extraordinaria y que no exista un tipo de nomina existente
                    BigDecimal i = util.redondear(per.getTotalExento().add(per.getTotalGravado()));
                    BigDecimal o = util.redondear(op.getTotalOtrosPagos());
                    BigDecimal d = util.redondear(dec.getTotalRetenido().add(dec.getTotalOtras()));
                    row[0] = numEmpleados.get(j);
                    row[1] = this.getNombreEmpleado(emp.getIdEmpleado());
                    row[2] = this.calcularAntiguedadSemanas(emp.getFechaInicialRelLaboral(), fechaFinalPago.getDate());
                    row[3] = this.calcularDiasPagados(fechaInicialPago.getDate(), fechaFinalPago.getDate());
                    row[4] = df.format(i);
                    row[5] = df.format(o);
                    row[6] = df.format(d);
                    tp = tp.add(i);
                    to = to.add(o);
                    td = td.add(d);
                    model.addRow(row);
                }
            }
            
            tablaEmpleados.getColumnModel().getColumn(4).setCellRenderer(render);
            tablaEmpleados.getColumnModel().getColumn(5).setCellRenderer(render);
            tablaEmpleados.getColumnModel().getColumn(6).setCellRenderer(render);
            
            totalRecibos = numEmpleados.size();
            lblNumRecibos.setText("Número de Recibos: " + totalRecibos);
            totalPer.setText("$"+df.format(tp));
            totalOtros.setText("$"+df.format(to));
            totalDed.setText("$"+df.format(td));
        }
    }

    private Empleado getEmpleado(String numEmpleado, int idEmpleado) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        Empleado emp = null;

        try {
            rs = stmt.executeQuery("SELECT idEmpleado,fechaInicialRelLaboral FROM Empleados WHERE idEmpleado = " + idEmpleado + " AND numEmpleado like \'" + numEmpleado + "\'");
            if (rs.next()) {
                emp = new Empleado();
                emp.setNumEmpleado(numEmpleado);
                emp.setIdEmpleado(rs.getInt("idEmpleado"));
                emp.setFechaInicialRelLaboral(rs.getDate("fechaInicialRelLaboral"));
            }

            rs.close();
            stmt.close();
            con.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al obtener empleado: " + numEmpleado + ": ", ex);
        }

        return emp;
    }

    private Empleado getEmpleadoCompleto(String numEmpleado, int idEmpleado) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        Empleado emp = null;

        try {
            rs = stmt.executeQuery("SELECT * FROM Empleados WHERE idEmpleado = " + idEmpleado + " AND numEmpleado = \'" + numEmpleado + "\'");
            if (rs.next()) {
                emp = new Empleado();
                emp.setIdEmpleado(rs.getInt("idEmpleado"));
                emp.setNumEmpleado(rs.getString("numEmpleado"));
                emp.setCurp(rs.getString("curp"));
                emp.setTipoRegimen(rs.getString("tipoRegimen"));
                emp.setNss(rs.getString("nss"));
                emp.setDepartamento(rs.getString("departamento"));
                emp.setClabe(rs.getString("clabe"));
                emp.setBanco(rs.getString("banco"));
                emp.setFechaInicialRelLaboral(rs.getDate("fechaInicialRelLaboral"));
                emp.setPuesto(rs.getString("puesto"));
                emp.setTipoContrato(rs.getString("tipoContrato"));
                emp.setTipoJornada(rs.getString("tipoJornada"));
                emp.setPeriodicidadPago(rs.getString("periodicidadPago"));
                emp.setRiesgoPuesto(rs.getString("riesgoPuesto"));
                emp.setSalarioDiarioInt(rs.getBigDecimal("salarioDiarioInt"));
                emp.setSalarioBaseCotApor(rs.getBigDecimal("salarioBaseCotApor"));
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al obtener el empleado " + numEmpleado, e);
        }
        return emp;
    }

    private String getNombreEmpleado(int idEmpleado) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        String nombre = null;
        try {
            rs = stmt.executeQuery("SELECT nombre FROM EmpleadosRec WHERE id = " + idEmpleado);
            if (rs.next()) {
                nombre = rs.getString("nombre");
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al obtener el nombre del empleado con id: " + idEmpleado, e);
        }

        return nombre;
    }

    private Factura getEmpleadoRec(int idEmpleado) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        Factura fact = null;
        try {
            rs = stmt.executeQuery("SELECT * FROM EmpleadosRec WHERE id = " + idEmpleado);
            if (rs.next()) {
                fact = new Factura();
                fact.nombre = rs.getString("nombre");
                fact.rfc = rs.getString("rfc");
                /*fact.calle = rs.getString("calle");
                fact.numExt = rs.getString("noExterior");
                fact.numInt = rs.getString("noInterior");
                fact.colonia = rs.getString("colonia");
                fact.localidad = rs.getString("localidad");
                fact.municipio = rs.getString("municipio");
                fact.estado = rs.getString("estado");
                fact.pais = rs.getString("pais");*/
                fact.cp = rs.getString("cp");
                fact.regimenFiscalReceptor = rs.getString("regimenFiscal");
                fact.idEmpleado = rs.getInt("id");
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al obtener los datos fiscales del Empleado con ID: " + idEmpleado, e);
        }
        return fact;
    }

    private BigDecimal obtenerNetoAPagar(int idEmpleado) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        BigDecimal neto = BigDecimal.ZERO;
        try {
            rs = stmt.executeQuery("SELECT * FROM ImportesPercepciones WHERE idEmpleado = " + idEmpleado);
            while (rs.next()) {
                BigDecimal ig = rs.getBigDecimal("importeGravado");
                BigDecimal ie = rs.getBigDecimal("importeExento");
                neto = neto.add(ig.add(ie));
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al obtener el neto a pagar del empleado con id: " + idEmpleado, e);
        }
        return util.redondear(neto);
    }

    private BigDecimal obtenerNetoDeducciones(int idEmpleado) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        BigDecimal neto = BigDecimal.ZERO;
        try {
            rs = stmt.executeQuery("SELECT * FROM ImportesDeducciones WHERE idEmpleado = " + idEmpleado);
            while (rs.next()) {
                BigDecimal ig = rs.getBigDecimal("importeGravado");
                BigDecimal ie = rs.getBigDecimal("importeExento");
                neto = neto.add((ig.add(ie)));
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al obtener el neto a pagar del empleado con id: " + idEmpleado, e);
        }
        return util.redondear(neto);
    }

    private Emisor getEmisor(int idEmisor) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        Emisor emi = null;
        try {
            rs = stmt.executeQuery("SELECT * FROM Emisores WHERE id = " + idEmisor);
            if (rs.next()) {
                emi = new Emisor();
                emi.setNombre(rs.getString("nombre"));
                emi.setRfc(rs.getString("rfc"));
                /*emi.setCalle(rs.getString("calle"));
                emi.setNoExterior(rs.getString("noExterior"));
                emi.setNoInterior(rs.getString("noInterior"));
                emi.setColonia(rs.getString("colonia"));
                emi.setLocalidad(rs.getString("localidad"));
                emi.setMunicipio(rs.getString("municipio"));
                emi.setEstado(rs.getString("estado"));
                emi.setPais(rs.getString("pais"));
                emi.setCp(rs.getString("cp"));*/
                emi.setRegistroPatronal(rs.getString("registroPatronal"));
                emi.setCurp(rs.getString("curp"));
                emi.setEmitirNominas(rs.getBoolean("emiteNominas"));
                emi.setRegimenFiscal(getRegimenFiscal(con, emi.getRfc()));
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al obtener el Emisor con id: " + idEmisor, e);
        }
        return emi;
    }

    private String getRegimenFiscal(Connection con, String rfc) {
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        String regimenFiscal = "";
        try {
            rs = stmt.executeQuery("SELECT regimenFiscal,lugarExpedicion FROM Cuentas WHERE rfc like \'" + rfc + "\'");
            if (rs.next()) {
                regimenFiscal = rs.getString("regimenFiscal");
                lugarExpedicion = rs.getString("lugarExpedicion");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al consultar el regimen fiscal del RFC: " + rfc, e);
        }
        return regimenFiscal;
    }
    
    private List<complementos.nominas.Deducciones.Deduccion> getDeducciones(Deducciones dec) {
        DefaultTableModel model = (DefaultTableModel) dec.tabla.getModel();
        List<complementos.nominas.Deducciones.Deduccion> lista = new ArrayList();
        complementos.nominas.Deducciones.Deduccion ded;
        complementos.nominas.Deducciones dedus = new complementos.nominas.Deducciones();

        for (int i = 0; i < model.getRowCount(); i++) {
            ded = dedus.getClase();
            ded.setTipoDeduccion(model.getValueAt(i, 0).toString());
            ded.setClave(model.getValueAt(i, 1).toString());
            ded.setConcepto(model.getValueAt(i, 2).toString());
            ded.setImporte(new BigDecimal(model.getValueAt(i, 3).toString()));

            lista.add(ded);
        }

        return lista;
    }

    private List<complementos.nominas.OtrosPagos.OtroPago> getOtrosPagos(OtrosPagos otr) {
        DefaultTableModel model = (DefaultTableModel) otr.tabla.getModel();
        List<complementos.nominas.OtrosPagos.OtroPago> lista = new ArrayList();
        complementos.nominas.OtrosPagos.OtroPago otp;
        complementos.nominas.OtrosPagos otros = new complementos.nominas.OtrosPagos();

        for (int i = 0; i < model.getRowCount(); i++) {
            otp = otros.getClase();
            otp.setTipoOtroPago(model.getValueAt(i, 0).toString());
            otp.setClave(model.getValueAt(i, 1).toString());
            otp.setConcepto(model.getValueAt(i, 2).toString());
            otp.setImporte(new BigDecimal(model.getValueAt(i, 3).toString()));

            lista.add(otp);
        }

        return lista;
    }

    private List<complementos.nominas.Percepciones.Percepcion> getPercepciones(Percepciones per) {
        DefaultTableModel model = (DefaultTableModel) per.tabla.getModel();
        List<complementos.nominas.Percepciones.Percepcion> lista = new ArrayList();
        complementos.nominas.Percepciones.Percepcion perc;
        complementos.nominas.Percepciones percep = new complementos.nominas.Percepciones();

        for (int i = 0; i < model.getRowCount(); i++) {
            perc = percep.getClase();
            perc.setTipoPercepcion(model.getValueAt(i, 0).toString());
            perc.setClave(model.getValueAt(i, 1).toString());
            perc.setConcepto(model.getValueAt(i, 2).toString());
            perc.setImporteGravado(new BigDecimal(model.getValueAt(i, 3).toString()));
            perc.setImporteExento(new BigDecimal(model.getValueAt(i, 4).toString()));

            lista.add(perc);
        }

        return lista;
    }

    private List<String> getConceptos(complementos.nominas.Percepciones perc, complementos.nominas.OtrosPagos otro, complementos.nominas.Deducciones dec) {
        List<String> conceptos = new ArrayList();
        BigDecimal pers = util.redondear(perc.getTotalSueldos().add(otro.getTotalOtrosPagos()));
        BigDecimal deds = util.redondear(dec.getTotalRetenido().add(dec.getTotalOtras()));
        String con = "C1: 84111505@ACT@.@.@1@Pago de nómina@" + pers.toString() + "@"+deds.toString()+"@" + pers.toString() + "\r\n";
        conceptos.add(con);
        return conceptos;
    }

    private Factura getTotales(complementos.nominas.Percepciones perc, complementos.nominas.Deducciones dedu, complementos.nominas.OtrosPagos otro, Factura fact) {
        BigDecimal sub = perc.getTotalSueldos().add(otro.getTotalOtrosPagos());

        fact.isrRetenido = dedu.getTotalRetenido();
        fact.descuento = dedu.getTotalOtras().add(dedu.getTotalRetenido());
        fact.totalRetenidos = fact.isrRetenido;
        fact.totalTraslados = BigDecimal.ZERO;
        fact.iva = BigDecimal.ZERO;
        fact.ivaRetenido = BigDecimal.ZERO;

        if (fact.descuento.compareTo(BigDecimal.ZERO) == 1) {
            fact.motivoDescuento = "Deducciones Nómina";
        } else {
            fact.motivoDescuento = "";
        }

        BigDecimal to = sub.subtract(fact.descuento);
        fact.subtotal = util.redondear(sub);
        fact.total = util.redondear(to);

        return fact;
    }

    private int getUltimoFolio(String rfc) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        int folio = 0;

        try {
            rs = stmt.executeQuery("SELECT serie, ultimo_folio FROM Folios WHERE idComprobante = 4 AND rfc like \'" + rfc + "\'");
            if (rs.next()) {
                folio = rs.getInt("ultimo_folio");
                serie = rs.getString("serie");
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al consultar el ultimo folio del emisor con id: " + idEmisor, e);
        }
        return folio;
    }
}
