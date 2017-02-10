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
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Abe
 */
public class NominaGeneral extends javax.swing.JFrame {

    utils.Utils util;
    utils.ConnectionFactory factory;
    int idEmisor;
    List<Integer> idEmpleados;
    String serie;
    String lugarExpedicion;

    public NominaGeneral() {
        initComponents();
        this.setLocationRelativeTo(null);
        util = new utils.Utils(Elemento.log);
        factory = new utils.ConnectionFactory(Elemento.log);
    }

    public NominaGeneral(List<Integer> numEmpleados, int idEmisor, List<Integer> idEmpleados) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.idEmisor = idEmisor;
        this.idEmpleados = idEmpleados;
        util = new utils.Utils(Elemento.log);
        factory = new utils.ConnectionFactory(Elemento.log);
        llenarComboMetodos();
        llenarTabla(numEmpleados);
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
        totalNetoLabel = new javax.swing.JLabel();
        metodoCombo = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nomina General");

        fechaPago.setDateFormatString("yyyy-MM-dd");

        fechaInicialPago.setDateFormatString("yyyy-MM-dd");

        fechaFinalPago.setDateFormatString("yyyy-MM-dd");
        fechaFinalPago.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fechaFinalPagoFocusLost(evt);
            }
        });
        fechaFinalPago.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                fechaFinalPagoMouseExited(evt);
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
        fechaFinalPago.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fechaFinalPagoPropertyChange(evt);
            }
        });

        jLabel6.setText("Fecha de Pago");

        jLabel7.setText("Fecha Inicial de Pago");

        jLabel8.setText("Fecha Final de Pago");

        tablaEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numero", "Empleado", "Antiguedad", "Dias Pagados", "Percepciones", "Deducciones"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Long.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
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
        jLabel1.setText("Total a Pagar: ");

        totalNetoLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalNetoLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        totalNetoLabel.setText("0.0");

        metodoCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01 Efectivo", "02 Cheque Nominativo", "03 Transferencia electrónica de fondos", "04 Tarjeta de Crédito", "05 Monederos Electrónicos", "06 Dinero Electrónico", "08 Vales de Despensa", "28 Tarjeta de Débito", "29 Tarjeta de Servicio", "98 NA", "99 Otros" }));
        metodoCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                metodoComboActionPerformed(evt);
            }
        });

        jLabel2.setText("Metodo de Pago");

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
                                .addComponent(generarNomina)
                                .addGap(26, 26, 26)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(metodoCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(totalNetoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addComponent(fechaFinalPago, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 20, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(fechaPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(fechaInicialPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(fechaFinalPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(metodoCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(totalNetoLabel)
                        .addComponent(generarNomina)))
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

    }//GEN-LAST:event_fechaFinalPagoInputMethodTextChanged

    private void fechaFinalPagoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fechaFinalPagoMouseExited
        // TODO add your handling code here:

    }//GEN-LAST:event_fechaFinalPagoMouseExited

    private void fechaFinalPagoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fechaFinalPagoPropertyChange
        // TODO add your handling code here:
        generarNomina.setEnabled(true);
        DefaultTableModel model = (DefaultTableModel) tablaEmpleados.getModel();
        int dias = calcularDiasPagados(fechaInicialPago.getDate(), fechaFinalPago.getDate());
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(dias, i, 3);
        }
    }//GEN-LAST:event_fechaFinalPagoPropertyChange

    private void generarNominaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generarNominaActionPerformed
        int r = JOptionPane.showConfirmDialog(null, "Esta seguro que desea generar la nomina?", "Generar Nomina", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (r == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) tablaEmpleados.getModel();
            complementos.nominas.Nominas nom;
            complementos.nominas.Deducciones dedu = new complementos.nominas.Deducciones();
            complementos.nominas.Percepciones perc = new complementos.nominas.Percepciones();
            Factura fact;
            Percepciones per;
            Deducciones dec;
            Layout lay;
            Emisor emi = this.getEmisor(idEmisor);
            int folio = getUltimoFolio(emi.getRfc());

            for (int i = 0; i < model.getRowCount(); i++) {
                int num = new Integer(model.getValueAt(i, 0).toString());
                int idE = idEmpleados.get(i);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                nom = new complementos.nominas.Nominas();

                Empleado emp = this.getEmpleadoCompleto(num, idE);
                fact = this.getEmpleadoRec(emp.getIdEmpleado());
                fact.emisor = emi;
                fact.folio = "" + folio;
                folio++;
                fact.serie = serie;
                fact.metodoPago = metodoCombo.getSelectedItem().toString().split(" ")[0];
                fact.cuentaBancaria = "";
                fact.leyenda = "";
                fact.tipoCfd = "recibo de nomina";
                fact.lugarExpedicion = lugarExpedicion;
                fact.moneda = "MXN";
                fact.tipoCambio = "1.0";
                fact.prefactura = "";

                per = new Percepciones(emp.getIdEmpleado(), false);
                dec = new Deducciones(emp.getIdEmpleado(), false);

                nom.setAntiguedad(new Integer(model.getValueAt(i, 2).toString()));
                nom.setNumDiasPagados(new Double(model.getValueAt(i, 3).toString()));
                nom.setEmpleado(emp.getEmpleado());
                nom.setFechaPago(format.format(fechaPago.getDate()));
                nom.setFechaInicialPago(format.format(fechaInicialPago.getDate()));
                nom.setFechaFinalPago(format.format(fechaFinalPago.getDate()));
                nom.setHorasExtra(null);
                nom.setIncapacidad(null);
                nom.setRegistroPatronal(fact.emisor.getRegistroPatronal());

                //Seteamos deducciones
                dedu.setDeducciones(getDeducciones(dec));
                dedu.setTotalExento(dec.getTotalExento());
                dedu.setTotalGravado(dec.getTotalGravado());
                nom.setDeducciones(dedu);

                //Seteamos percepciones
                perc.setPercepciones(getPercepciones(per));
                perc.setTotalExento(per.getTotalExento());
                perc.setTotalGravado(per.getTotalGravado());
                nom.setPercepciones(perc);

                fact.conceptos = getConceptos(perc);
                fact = getTotales(perc, dedu, fact);

                lay = new Layout(fact, emp, nom);
            }
        }

    }//GEN-LAST:event_generarNominaActionPerformed

    private void metodoComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_metodoComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_metodoComboActionPerformed

    private void llenarComboMetodos(){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        
        try {
            rs = stmt.executeQuery("SELECT idMetodo, nombre FROM MetodosPago");
            while(rs.next()){
                metodoCombo.addItem(rs.getString("nombre"));
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al obtener los metodos de pago", e);
        }
    }
    
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

    public long calcularAntiguedadSemanas(Date fechaInicial) {
        try {
            Date date = fechaInicial;
            Date date2 = new Date();

            GregorianCalendar cal = new GregorianCalendar(date.getYear(), date.getMonth(), date.getDate());
            GregorianCalendar cal2 = new GregorianCalendar(date2.getYear(), date2.getMonth(), date2.getDate());

            //long difms = (cal2.getTimeInMillis()*(-1)) - cal.getTimeInMillis();
            long difms = cal2.getTimeInMillis() - cal.getTimeInMillis();
            long difd = difms / (1000 * 60 * 60 * 24 * 7);
            return difd;
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
    private com.toedter.calendar.JDateChooser fechaFinalPago;
    private com.toedter.calendar.JDateChooser fechaInicialPago;
    private com.toedter.calendar.JDateChooser fechaPago;
    private javax.swing.JToggleButton generarNomina;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox metodoCombo;
    private javax.swing.JTable tablaEmpleados;
    private javax.swing.JLabel totalNetoLabel;
    // End of variables declaration//GEN-END:variables

    private void llenarTabla(List<Integer> numEmpleados) {
        DefaultTableModel model = (DefaultTableModel) tablaEmpleados.getModel();
        model.setRowCount(0);
        Object row[];
        BigDecimal totalNeto;
        double tn = 0.0;
        for (int j = 0; j < numEmpleados.size(); j++) {
            row = new Object[6];
            Empleado emp = getEmpleado(numEmpleados.get(j), idEmpleados.get(j));
            if (emp != null) {
                Percepciones per = new Percepciones(emp.getIdEmpleado(), true);
                Deducciones dec = new Deducciones(emp.getIdEmpleado(), true);
                double i = util.redondear(per.getTotalExento() + per.getTotalGravado());
                double d = util.redondear(dec.getTotalExento() + dec.getTotalGravado());
                row[0] = numEmpleados.get(j);
                row[1] = this.getNombreEmpleado(emp.getIdEmpleado());
                row[2] = this.calcularAntiguedadSemanas(emp.getFechaInicialRelLaboral());
                row[3] = 0;
                row[4] = i;
                row[5] = d;
                tn += (i - d);
                model.addRow(row);
            }
        }
        totalNeto = util.redondearBigDecimal(tn);
        DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
        totalNetoLabel.setText(df.format(totalNeto.doubleValue()));
    }

    private Empleado getEmpleado(int numEmpleado, int idEmpleado) {
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

    private Empleado getEmpleadoCompleto(int numEmpleado, int idEmpleado) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        Empleado emp = null;

        try {
            rs = stmt.executeQuery("SELECT * FROM Empleados WHERE idEmpleado = " + idEmpleado + " AND numEmpleado like \'" + numEmpleado + "\'");
            if (rs.next()) {
                emp = new Empleado();
                emp.setIdEmpleado(rs.getInt("idEmpleado"));
                emp.setNumEmpleado(rs.getInt("numEmpleado"));
                emp.setCurp(rs.getString("curp"));
                emp.setTipoRegimen(rs.getInt("tipoRegimen"));
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
                fact.calle = rs.getString("calle");
                fact.numExt = rs.getString("noExterior");
                fact.numInt = rs.getString("noInterior");
                fact.colonia = rs.getString("colonia");
                fact.localidad = rs.getString("localidad");
                fact.municipio = rs.getString("municipio");
                fact.estado = rs.getString("estado");
                fact.pais = rs.getString("pais");
                fact.cp = rs.getString("cp");
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

    private Double obtenerNetoAPagar(int idEmpleado) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        Double neto = 0.0;
        try {
            rs = stmt.executeQuery("SELECT * FROM ImportesPercepciones WHERE idEmpleado = " + idEmpleado);
            while (rs.next()) {
                double ig = rs.getDouble("importeGravado");
                double ie = rs.getDouble("importeExento");
                neto += (ig + ie);
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

    private Double obtenerNetoDeducciones(int idEmpleado) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        Double neto = 0.0;
        try {
            rs = stmt.executeQuery("SELECT * FROM ImportesDeducciones WHERE idEmpleado = " + idEmpleado);
            while (rs.next()) {
                double ig = rs.getDouble("importeGravado");
                double ie = rs.getDouble("importeExento");
                neto += (ig + ie);
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
                emi.setCalle(rs.getString("calle"));
                emi.setNoExterior(rs.getString("noExterior"));
                emi.setNoInterior(rs.getString("noInterior"));
                emi.setColonia(rs.getString("colonia"));
                emi.setLocalidad(rs.getString("localidad"));
                emi.setMunicipio(rs.getString("municipio"));
                emi.setEstado(rs.getString("estado"));
                emi.setPais(rs.getString("pais"));
                emi.setCp(rs.getString("cp"));
                emi.setRegistroPatronal(rs.getString("registroPatronal"));
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
            ded.setImporteGravado(new Double(model.getValueAt(i, 3).toString()));
            ded.setImporteExento(new Double(model.getValueAt(i, 4).toString()));

            lista.add(ded);
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
            perc.setImporteGravado(new Double(model.getValueAt(i, 3).toString()));
            perc.setImporteExento(new Double(model.getValueAt(i, 4).toString()));

            lista.add(perc);
        }

        return lista;
    }

    private List<String> getConceptos(complementos.nominas.Percepciones perc) {
        List<String> conceptos = new ArrayList();
        BigDecimal pers = util.redondearBigDecimal(perc.getTotalGravado() + perc.getTotalExento());
        String con = "C1: 001@1@SERVICIO@PAGO DE NOMINA@" + pers.toString() + "@" + pers.toString() + "@false@false\r\n";
        conceptos.add(con);
        return conceptos;
    }

    private Factura getTotales(complementos.nominas.Percepciones perc, complementos.nominas.Deducciones dedu, Factura fact) {
        double des = 0;
        double isR = 0;
        double sub = perc.getTotalExento() + perc.getTotalGravado();
        
        for (int i = 0; i < dedu.getDeducciones().size(); i++) {
            if (!dedu.getDeducciones().get(i).getTipoDeduccion().equals("002")) {
                des += dedu.getDeducciones().get(i).getImporteGravado() + dedu.getDeducciones().get(i).getImporteExento();
            } else {
                isR = dedu.getDeducciones().get(i).getImporteExento() + dedu.getDeducciones().get(i).getImporteGravado();
            }
        }

        fact.isrRetenido = util.redondear(isR);
        fact.descuento = util.redondear(des);
        fact.totalRetenidos = fact.isrRetenido;
        fact.totalTraslados = 0.0;
        fact.iva = 0.0;
        fact.ivaRetenido = 0.0;

        if (fact.descuento > 0) {
            fact.motivoDescuento = "Deducciones Nómina";
        } else {
            fact.motivoDescuento = "";
        }

        double to = (perc.getTotalGravado() + perc.getTotalExento()) - des - isR;
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