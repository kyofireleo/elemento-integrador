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
public class Aguinaldo extends javax.swing.JFrame {

    /**
     * Creates new form Aguinaldo
     */
    utils.Utils util;
    utils.ConnectionFactory factory;
    int idEmisor;
    List<Integer> idEmpleados;
    String serie;
    String lugarExpedicion;
    double calculo;
    List<Double> importes;

    public Aguinaldo() {
        initComponents();
        this.setLocationRelativeTo(null);
        util = new utils.Utils(Elemento.log);
        factory = new utils.ConnectionFactory(Elemento.log);
    }

    public Aguinaldo(List<String> numEmpleados, int idEmisor, List<Integer> idEmpleados) {
        initComponents();
        this.setLocationRelativeTo(null);
        this.idEmisor = idEmisor;
        this.idEmpleados = idEmpleados;
        util = new utils.Utils(Elemento.log);
        factory = new utils.ConnectionFactory(Elemento.log);
        importes = new ArrayList();
        llenarTabla(numEmpleados);
        calcularAguinaldo();
    }

    private void llenarTabla(List<String> numEmpleados) {
        DefaultTableModel model = (DefaultTableModel) tablaEmpleados.getModel();
        model.setRowCount(0);
        Object row[];
        BigDecimal totalNeto;
        double tn = 0.0;
        for (int j = 0; j < numEmpleados.size(); j++) {
            row = new Object[6];
            Empleado emp = getEmpleado(numEmpleados.get(j), idEmpleados.get(j));
            if (emp != null) {
                double i = /*obtenerNetoAPagar(emp.getIdEmpleado())*/ 0.0;
                double d = /*obtenerNetoDeducciones(emp.getIdEmpleado())*/ 0.0;
                row[0] = numEmpleados.get(j);
                row[1] = this.getNombreEmpleado(emp.getIdEmpleado());
                row[2] = this.calcularAntiguedadSemanas(emp.getFechaInicialRelLaboral(),this.fechaFinalPago.getDate());
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

    private void calcularAguinaldo() {
        if (importes.size() > 0) {
            importes.clear();
        }
        DefaultTableModel model = (DefaultTableModel) tablaEmpleados.getModel();
        int di = new Integer(model.getValueAt(0, 3).toString());
        int diasA = new Integer(JOptionPane.showInputDialog(null, "Ingrese el numero de dias de Aguinaldo:", di).trim());
        Empleado emp;
        BigDecimal totalA = BigDecimal.ZERO, totalI = BigDecimal.ZERO;
        BigDecimal total;
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(diasA, i, 3);
            int num = new Integer(model.getValueAt(i, 0).toString());
            int idE = idEmpleados.get(i);
            emp = this.getEmpleadoCompleto(num, idE);
            int anti = calcularAntiguedadSemanas(emp.getFechaInicialRelLaboral(), fechaFinalPago.getDate());
            BigDecimal sd = emp.getSalarioDiarioInt();
            BigDecimal agui = util.redondear(obtenerAguinaldo(diasA, sd));
            BigDecimal isr = new BigDecimal(model.getValueAt(i, 5).toString());
            importes.add(agui.doubleValue());
            model.setValueAt(anti, i, 2);
            model.setValueAt(agui, i, 4);
            totalA = totalA.add(agui);
            totalI = totalI.add(isr);
        }
        total = util.redondear(totalA.subtract(totalI));
        totalNetoLabel.setText(total.toString());
    }

    private void recalcularAguinaldo() {
        DefaultTableModel model = (DefaultTableModel) tablaEmpleados.getModel();
        Empleado emp;
        BigDecimal totalA = BigDecimal.ZERO, totalI = BigDecimal.ZERO;
        BigDecimal total;
        for (int i = 0; i < model.getRowCount(); i++) {
            int diasA = new Integer(model.getValueAt(i, 3).toString());
            model.setValueAt(diasA, i, 3);
            int num = new Integer(model.getValueAt(i, 0).toString());
            int idE = idEmpleados.get(i);
            emp = this.getEmpleadoCompleto(num, idE);
            int anti = calcularAntiguedadSemanas(emp.getFechaInicialRelLaboral(), this.fechaFinalPago.getDate());
            BigDecimal sd = emp.getSalarioDiarioInt();
            BigDecimal agui = util.redondear(obtenerAguinaldo(diasA, sd));
            BigDecimal isr = new BigDecimal(model.getValueAt(i, 5).toString());
            model.setValueAt(anti, i, 2);
            model.setValueAt(agui, i, 4);
            totalA = totalA.add(agui);
            totalI = totalI.add(isr);
            
        }
        total = util.redondear(totalA.subtract(totalI));
        totalNetoLabel.setText(total.toString());
    }

    private BigDecimal obtenerAguinaldo(int dias, BigDecimal sueldoDiario) {
        return sueldoDiario.multiply(new BigDecimal(dias));
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

    private Empleado getEmpleado(String numEmpleado, int idEmpleado) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        Empleado emp = null;

        try {
            rs = stmt.executeQuery("SELECT idEmpleado,fechaInicialRelLaboral FROM Empleados WHERE idEmpleado = " + idEmpleado + " AND numEmpleado = \'" + numEmpleado + "\'");
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
                emp.setPeriodicidadPago("99"); //Para el aguinaldo asi va
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
            long difms = date2.getTime() - date.getTime();
            long difd = (difms / 86400000) + 1;
            return (int) (difd / 7);
        } catch (Exception ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al calcular los dias pagados: ", ex);
            return -1;
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

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        fechaPago = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        fechaInicialPago = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        fechaFinalPago = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEmpleados = new javax.swing.JTable();
        timbrarNomina = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        totalNetoLabel = new javax.swing.JLabel();
        recalcularBoton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Calcular Aguinaldo");

        jLabel6.setText("Fecha de Pago");

        fechaPago.setDateFormatString("yyyy-MM-dd");
        fechaPago.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fechaPagoPropertyChange(evt);
            }
        });

        jLabel7.setText("Fecha Inicial de Pago");

        fechaInicialPago.setDateFormatString("yyyy-MM-dd");
        fechaInicialPago.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fechaInicialPagoPropertyChange(evt);
            }
        });

        jLabel8.setText("Fecha Final de Pago");

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
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                fechaFinalPagoInputMethodTextChanged(evt);
            }
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                fechaFinalPagoCaretPositionChanged(evt);
            }
        });

        tablaEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numero", "Empleado", "Antiguedad", "Dias Pagados", "Aguinaldo", "ISR"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Long.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaEmpleados.setCellSelectionEnabled(true);
        jScrollPane1.setViewportView(tablaEmpleados);

        timbrarNomina.setText("Timbrar");
        timbrarNomina.setEnabled(false);
        timbrarNomina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timbrarNominaActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Total a Pagar: ");

        totalNetoLabel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalNetoLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        totalNetoLabel.setText("0.0");

        recalcularBoton.setText("Recalcular");
        recalcularBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recalcularBotonActionPerformed(evt);
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
                        .addComponent(timbrarNomina)
                        .addGap(257, 257, 257)
                        .addComponent(recalcularBoton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalNetoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(recalcularBoton)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(totalNetoLabel)
                        .addComponent(timbrarNomina)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fechaFinalPagoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fechaFinalPagoMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_fechaFinalPagoMouseExited

    private void fechaFinalPagoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fechaFinalPagoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_fechaFinalPagoFocusLost

    private void fechaFinalPagoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fechaFinalPagoPropertyChange
        // TODO add your handling code here:
        if (fechaFinalPago.getDate() != null) {
            fechaInicialPago.setDate(fechaFinalPago.getDate());
            fechaPago.setDate(fechaFinalPago.getDate());
            this.recalcularAguinaldo();
        }
        timbrarNomina.setEnabled(true);
    }//GEN-LAST:event_fechaFinalPagoPropertyChange

    private void fechaFinalPagoCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_fechaFinalPagoCaretPositionChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_fechaFinalPagoCaretPositionChanged

    private void fechaFinalPagoInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_fechaFinalPagoInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_fechaFinalPagoInputMethodTextChanged

    private void timbrarNominaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timbrarNominaActionPerformed
        Date fp = fechaPago.getDate();
        Date fip = fechaInicialPago.getDate();
        Date ffp = fechaFinalPago.getDate();

        if (fp != null && fip != null && ffp != null) {
            int r = JOptionPane.showConfirmDialog(null, "Esta seguro que desea generar la nomina de aguinaldo?", "Timbrar Aguinaldo", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

            if (r == JOptionPane.YES_OPTION) {
                DefaultTableModel model = (DefaultTableModel) tablaEmpleados.getModel();
                complementos.nominas.Nominas nom;
                complementos.nominas.Deducciones dedu = new complementos.nominas.Deducciones();
                complementos.nominas.Percepciones perc = new complementos.nominas.Percepciones();
                Factura fact;
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
                    fact.formaPago = "99";
                    fact.metodoPago = "PUE";
                    fact.cuentaBancaria = "";
                    fact.leyenda = "";
                    fact.tipoCfd = "N";
                    fact.lugarExpedicion = lugarExpedicion;
                    fact.moneda = "MXN";
                    fact.tipoCambio = "1.0";
                    fact.prefactura = "";
                    fact.usoCfdi = "P01";

                    nom.setAntiguedad(new Integer(model.getValueAt(i, 2).toString()));
                    nom.setTipoNomina("E"); //Extraordinaria
                    nom.setNumDiasPagados(new Integer(model.getValueAt(i, 3).toString()));
                    nom.setEmpleado(emp.getEmpleado());
                    nom.setFechaPago(format.format(fechaPago.getDate()));
                    nom.setFechaInicialPago(format.format(fechaInicialPago.getDate()));
                    nom.setFechaFinalPago(format.format(fechaFinalPago.getDate()));
                    nom.setHorasExtra(null);
                    nom.setIncapacidad(null);
                    nom.setRegistroPatronal(fact.emisor.getRegistroPatronal());

                    //Seteamos deducciones
                    List<complementos.nominas.Deducciones.Deduccion> d = getDeducciones(i);
                    if(d.size() > 0){
                        dedu.setDeducciones(d);
                        dedu.setTotalRetenido(d.get(0).getImporte());
                        dedu.setTotalOtras(0.0);
                        nom.setDeducciones(dedu);
                        nom.setTotalDeducciones(util.redondearBigDecimal(dedu.getTotalRetenido()));
                    }

                    //Seteamos percepciones
                    perc.setPercepciones(getPercepciones(i));
                    perc.setTotalExento(0.0);
                    perc.setTotalGravado(perc.getPercepciones().get(0).getImporteGravado() + perc.getPercepciones().get(0).getImporteExento());
                    perc.setTotalSueldos(perc.getTotalGravado() + perc.getTotalExento());
                    nom.setPercepciones(perc);
                    nom.setTotalPercepciones(new BigDecimal(perc.getTotalSueldos()));
                    
                    nom.setTotalOtrosPagos(BigDecimal.ZERO);

                    fact.conceptos = getConceptos(perc, dedu);
                    fact = getTotales(perc, dedu, fact);

                    lay = new Layout(fact, emp, nom);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Algunas o todas las fechas estan sin asignar, verificalo", "Error en fechas", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_timbrarNominaActionPerformed

    private List<String> getConceptos(complementos.nominas.Percepciones perc, complementos.nominas.Deducciones dec) {
        List<String> conceptos = new ArrayList();
        BigDecimal pers = util.redondearBigDecimal(perc.getTotalSueldos());
        BigDecimal deds = util.redondearBigDecimal(dec.getTotalRetenido());
        String con = "C1: 84111505@ACT@.@.@1@Pago de nómina@" + pers.toString() + "@"+deds.toString()+"@" + pers.toString() + "\r\n";
        conceptos.add(con);
        return conceptos;
    }

    private Factura getTotales(complementos.nominas.Percepciones perc, complementos.nominas.Deducciones dedu, Factura fact) {
        double sub = perc.getTotalSueldos();

        fact.isrRetenido = dedu.getTotalRetenido();
        fact.descuento = dedu.getTotalOtras() + dedu.getTotalRetenido();
        fact.totalRetenidos = fact.isrRetenido;
        fact.totalTraslados = 0.0;
        fact.iva = 0.0;
        fact.ivaRetenido = 0.0;

        if (fact.descuento > 0) {
            fact.motivoDescuento = "Deducciones Nómina";
        } else {
            fact.motivoDescuento = "";
        }

        double to = sub - fact.descuento;
        fact.subtotal = util.redondear(sub);
        fact.total = util.redondear(to);

        return fact;
    }

    private List<complementos.nominas.Deducciones.Deduccion> getDeducciones(int pos) {
        Concepto cc = getConceptos("002", 'D');
        DefaultTableModel model = (DefaultTableModel) tablaEmpleados.getModel();
        List<complementos.nominas.Deducciones.Deduccion> lista = new ArrayList();
        complementos.nominas.Deducciones.Deduccion ded;
        complementos.nominas.Deducciones dedus = new complementos.nominas.Deducciones();
        double importe = new Double(model.getValueAt(pos, 5).toString());
        if(importe > 0){
            ded = dedus.getClase();
            ded.setTipoDeduccion(cc.getTipoConcepto());
            ded.setClave(cc.getCodigo());
            ded.setConcepto(cc.getDescripcion());
            ded.setImporte(importe);
            lista.add(ded);
        }
        return lista;
    }

    private List<complementos.nominas.Percepciones.Percepcion> getPercepciones(int pos) {
        Concepto cc = getConceptos("002", 'P');
        DefaultTableModel model = (DefaultTableModel) tablaEmpleados.getModel();
        List<complementos.nominas.Percepciones.Percepcion> lista = new ArrayList();
        complementos.nominas.Percepciones.Percepcion perc;
        complementos.nominas.Percepciones percep = new complementos.nominas.Percepciones();

        perc = percep.getClase();
        perc.setTipoPercepcion(cc.getTipoConcepto());
        perc.setClave(cc.getCodigo());
        perc.setConcepto(cc.getDescripcion());
        perc.setImporteGravado(new Double(model.getValueAt(pos, 4).toString()));
        perc.setImporteExento(0.0);
        lista.add(perc);

        return lista;
    }

    private Concepto getConceptos(String codigoSat, char tipo) {
        Concepto cc = null;
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        String query = "SELECT * FROM ";
        switch (tipo) {
            case 'P':
                query += "ConceptosPercepciones ";
                break;
            case 'D':
                query += "ConceptosDeducciones ";
                break;
            case 'O':
                query += "ConceptosOtrosPagos ";
                break;
        }
        query += "WHERE tipo = \'" + codigoSat + "\'";

        try {
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                cc = new Concepto();
                cc.setCodigo(rs.getString("clave"));
                cc.setDescripcion(rs.getString("concepto"));
                cc.setTipoConcepto(rs.getString("tipo"));
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al consultar los conceptos de tipo: " + tipo, e);
            JOptionPane.showMessageDialog(null, "Excepcion al consultar los conceptos de tipo: " + tipo, "Error", JOptionPane.ERROR_MESSAGE);
        }
        return cc;
    }

    private int getUltimoFolio(String rfc) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        int folio = 0;

        try {
            rs = stmt.executeQuery("SELECT serie, ultimo_folio FROM Folios WHERE idComprobante = 4 AND rfc = \'" + rfc + "\'");
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
                emi.setRegistroPatronal(rs.getString("registroPatronal"));
                emi.setEmitirNominas(rs.getBoolean("emiteNominas"));
                emi.setRegimenFiscal(getRegimenFiscal(con, emi.getRfc()));
                emi.setCurp(rs.getString("curp"));
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
            rs = stmt.executeQuery("SELECT regimenFiscal,lugarExpedicion FROM Cuentas WHERE rfc = \'" + rfc + "\'");
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

    private void recalcularBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recalcularBotonActionPerformed
        // TODO add your handling code here:
        if (isDiasTrabajadosIguales() && isImportesAguinaldosIguales()) {
            this.calcularAguinaldo();
        } else {
            this.recalcularAguinaldo();
        }

    }//GEN-LAST:event_recalcularBotonActionPerformed

    private void fechaPagoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fechaPagoPropertyChange
        // TODO add your handling code here:
        if (fechaPago.getDate() != null) {
            fechaInicialPago.setDate(fechaPago.getDate());
            fechaFinalPago.setDate(fechaPago.getDate());
        }
    }//GEN-LAST:event_fechaPagoPropertyChange

    private void fechaInicialPagoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fechaInicialPagoPropertyChange
        // TODO add your handling code here:
        if (fechaInicialPago.getDate() != null) {
            fechaPago.setDate(fechaInicialPago.getDate());
            fechaFinalPago.setDate(fechaInicialPago.getDate());
        }
    }//GEN-LAST:event_fechaInicialPagoPropertyChange

    private boolean isDiasTrabajadosIguales() {
        List<Integer> dias = new ArrayList();
        for (int i = 0; i < tablaEmpleados.getModel().getRowCount(); i++) {
            int d = new Integer(tablaEmpleados.getModel().getValueAt(i, 3).toString());
            if (i > 0) {
                if (dias.contains(d)) {
                    dias.add(d);
                } else {
                    return false;
                }
            } else {
                dias.add(d);
            }
        }
        return true;
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
            java.util.logging.Logger.getLogger(Aguinaldo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Aguinaldo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Aguinaldo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Aguinaldo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Aguinaldo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser fechaFinalPago;
    private com.toedter.calendar.JDateChooser fechaInicialPago;
    private com.toedter.calendar.JDateChooser fechaPago;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton recalcularBoton;
    private javax.swing.JTable tablaEmpleados;
    private javax.swing.JToggleButton timbrarNomina;
    private javax.swing.JLabel totalNetoLabel;
    // End of variables declaration//GEN-END:variables

    private boolean isImportesAguinaldosIguales() {
        DefaultTableModel model = (DefaultTableModel) tablaEmpleados.getModel();
        for (int i = 0; i < importes.size(); i++) {
            double v = new Double(model.getValueAt(i, 4).toString());
            if (importes.get(i) != v) {
                return false;
            }
        }
        return true;
    }
}
