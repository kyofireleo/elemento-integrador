/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * Factura_View.java
 *
 * Created on 14/10/2011, 09:10:00 PM
 */
package gui;

import com.mxrck.autocompleter.AutoCompleterCallback;
import com.mxrck.autocompleter.TextAutoCompleter;
import elemento.ConnectionFactory;
import elemento.Donataria;
import elemento.Elemento;
import elemento.Emisor;
import elemento.Exe;
import elemento.ExpedidoEn;
import elemento.Factura;
import elemento.Factura.ConceptoTraslado;
import elemento.Factura.ConceptoRetencion;
import elemento.Layout;
import java.awt.AWTException;
import java.awt.KeyboardFocusManager;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import nominas.Empleado;
import pagos.RecibosPagos;
import reportes.Receptor;

/**
 *
 * @author Abe
 */
public class Factura_View extends javax.swing.JFrame {

    DefaultTableModel model;
    DefaultComboBoxModel combo;
    DefaultComboBoxModel comboE;
    String folio = null;
    String serie = null;
    String leyenda = "";
    String mensaje = "";
    static String plantilla = null;
    public BigDecimal porcentaje = new BigDecimal("0.16");
    public boolean modificar;
    static utils.Utils util = new utils.Utils(Elemento.log);
    ConnectionFactory factory = new ConnectionFactory();
    Emisor emisor;
    Receptor receptor;
    String rfcE, rfc, nombre, calle, numExt, numInt, colonia, municipio, localidad, estado, pais, cp, regimenFiscalReceptor, tipoPersona;
    int contadorComprobantes = 0;
    int creditosRestantes = 0;
    int idEmpleado;
    BigDecimal porcentajeIeps, importeIeps, porcentajeIsr, importeIsr, porcentajeIvaRet, importeIvaRet;
    List<BigDecimal> tasaIva = new ArrayList();
    List<BigDecimal> tasaIeps = new ArrayList();
    List<BigDecimal> tasaIsr = new ArrayList();
    List<BigDecimal> tasaIvaRet = new ArrayList();
    Empleado emp = null;
    List<Boolean> bancarizado;
    List<String> cfdisAsoc;
    String uuids = null;
    RecibosPagos rp = null;
    boolean entroEvento = false;
    List<String> formasPagoLista;
    List<String> arrayTipoRel;
    List<String> listaClientes;
    public boolean fromFolios = false;
    public boolean isNotaCredito = false;
    private TextAutoCompleter texter;
    public boolean esperaCliente = true;

    /**
     * Creates new form Factura_View
     */
    public Factura_View() {
//        model = new DefaultTableModel();
//        model.addColumn("Cantidad");
//        model.addColumn("Unidad");
//        model.addColumn("No Identificacion");
//        model.addColumn("Descripcion");
//        model.addColumn("Precio");
//        model.addColumn("Importe");
//        model.addColumn("Aplica IVA");
        initComponents();
        setLocationRelativeTo(null);
        iniciar();
    }

    public Factura_View(String nada) {

    }

    public Factura_View(int idCliente) {
        initComponents();
        setLocationRelativeTo(null);
        iniciar();
        this.setVisible(true);
        setCliente(idCliente);

    }

    //Constructor para crear Nota de Credito
    public Factura_View(int idEmisor, int idReceptor, String fact_uuid, double descuento, boolean tieneIva) {
        initComponents();
        setLocationRelativeTo(null);
        this.fromFolios = true;
        this.isNotaCredito = true;
        iniciar();
        this.setVisible(true);
        
        setNotaDeCredito(idEmisor, idReceptor, fact_uuid, descuento, tieneIva);
    }

    private void iniciar() {
        comboE = (DefaultComboBoxModel) listaEmisores.getModel();
        listaClientes = new ArrayList();
        listaEmisores.setModel(comboE);
        model = (DefaultTableModel) conceptos.getModel();
        bancarizado = new ArrayList();
        formasPagoLista = new ArrayList();
        arrayTipoRel = new ArrayList();
        consultar("Combo", "SELECT * FROM Clientes");
        consultar("ComboM", "SELECT metodopago, descripcion FROM c_metodopago");
        consultar("ComboF", "SELECT formapago, descripcion, bancarizado FROM c_formapago");
        //consultar("ComboTC", "SELECT tiposcomprobante, descripcion FROM c_tiposcomprobante");
        if(!this.fromFolios)
            consultar("ComboU", "SELECT usocfdi, descripcion FROM c_usocfdi");
        consultar("ComboE", "SELECT * FROM Emisores");
        consultar("ComboT", "SELECT tiporelacion, descripcion FROM c_tiporelacion");
        listaEmisores.setSelectedIndex(0);
        
        texter = new TextAutoCompleter(txtCliente, new AutoCompleterCallback() {
            @Override
            public void callback(Object selectedItem) {
                System.out.println("El usuario seleccionó: " + selectedItem);
                consultar("Campos", "SELECT * FROM Clientes WHERE id = " + selectedItem.toString().split(",")[0]);
            }
        });

        texter.setCaseSensitive(false);
        texter.setItems(listaClientes.toArray());
        texter.setMode(0);
        
        //consultar("Folio", "SELECT * FROM Folios WHERE idComprobante = " + getIdComprobante(tipocfd.getSelectedItem().toString().split(",")[0]) + " AND rfc like \'" + rfcE + "\'");
    }

    public int getIdComprobante(String tipo) {
        int idComprobante;
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;

        try {
            rs = stmt.executeQuery("SELECT c_tiposcomprobante_id FROM c_tiposcomprobante WHERE tiposcomprobante = '" + tipo + "'");
            if (rs.next()) {
                idComprobante = rs.getInt(1);
            } else {
                idComprobante = 1;
                Elemento.log.error("No se encontro el tipo de comprobante: " + tipo);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            idComprobante = 0;
            Elemento.log.error("No se pudo obtener el ID del tipo de comprobante: ", ex);
        }
        return idComprobante;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cantidad = new javax.swing.JTextField();
        precio = new javax.swing.JTextField();
        emitir = new javax.swing.JButton();
        cancelar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        conceptos = new javax.swing.JTable();
        agregarConcepto = new javax.swing.JButton();
        quitar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        subtotal = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        total = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        iva = new javax.swing.JTextField();
        preFactura = new javax.swing.JButton();
        metodoCombo = new javax.swing.JComboBox();
        aplicaIva = new javax.swing.JCheckBox();
        tipocfd = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        listaEmisores = new javax.swing.JComboBox();
        jLabel25 = new javax.swing.JLabel();
        noIdentificacion = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        folioText = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        ivaRetenido = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        serieText = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        descripcion = new javax.swing.JTextArea();
        editarConcepto = new javax.swing.JButton();
        jLabel28 = new javax.swing.JLabel();
        moneda = new javax.swing.JComboBox();
        tipoCambio = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        isrRetenido = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        descuentoTotal = new javax.swing.JTextField();
        agregarLeyenda = new javax.swing.JButton();
        lbl_Restantes = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        verCte = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        ieps = new javax.swing.JTextField();
        aplicaIeps = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        condicionPago = new javax.swing.JTextField();
        formasPagoCombo = new javax.swing.JComboBox<>();
        unidad = new javax.swing.JTextField();
        claveProdSat = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        usocfdi = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        claveUnidad = new javax.swing.JTextField();
        aplicaIsr = new javax.swing.JCheckBox();
        aplicaIvaRet = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        descuento = new javax.swing.JTextField();
        asociarCfdi = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        cfdisAsociados = new javax.swing.JTextPane();
        tipoRelacion = new javax.swing.JComboBox<>();
        txtCliente = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Crear CFDI");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Cantidad");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Descripcion");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Precio");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Unidad");

        cantidad.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cantidad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cantidadFocusGained(evt);
            }
        });
        cantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cantidadActionPerformed(evt);
            }
        });

        precio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        precio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                precioActionPerformed(evt);
            }
        });

        emitir.setText("Emitir CFDi");
        emitir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emitirActionPerformed(evt);
            }
        });

        cancelar.setText("Cancelar");
        cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });

        conceptos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Clave Sat", "Cant.", "Cve. Uni.", "Unidad", "Descripcion", "Precio", "Dcto.", "Importe", "IVA", "IEPS", "ISR", "IVA Ret."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, false, false, false, false, false, true, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(conceptos);
        if (conceptos.getColumnModel().getColumnCount() > 0) {
            conceptos.getColumnModel().getColumn(0).setPreferredWidth(70);
            conceptos.getColumnModel().getColumn(1).setPreferredWidth(70);
            conceptos.getColumnModel().getColumn(2).setPreferredWidth(50);
            conceptos.getColumnModel().getColumn(3).setPreferredWidth(70);
            conceptos.getColumnModel().getColumn(4).setPreferredWidth(100);
            conceptos.getColumnModel().getColumn(5).setPreferredWidth(300);
            conceptos.getColumnModel().getColumn(6).setPreferredWidth(100);
            conceptos.getColumnModel().getColumn(7).setPreferredWidth(100);
            conceptos.getColumnModel().getColumn(8).setPreferredWidth(100);
            conceptos.getColumnModel().getColumn(9).setPreferredWidth(50);
            conceptos.getColumnModel().getColumn(10).setPreferredWidth(50);
            conceptos.getColumnModel().getColumn(11).setPreferredWidth(50);
            conceptos.getColumnModel().getColumn(12).setPreferredWidth(60);
        }

        agregarConcepto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/add.png"))); // NOI18N
        agregarConcepto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarConceptoActionPerformed(evt);
            }
        });

        quitar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/DeleteRed.png"))); // NOI18N
        quitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitarActionPerformed(evt);
            }
        });

        jLabel6.setText("SubTotal");

        subtotal.setEditable(false);
        subtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        subtotal.setText("0.00");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("Total");

        total.setEditable(false);
        total.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        total.setText("0.00");

        jLabel9.setText("IVA");

        iva.setEditable(false);
        iva.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        iva.setText("0.00");

        preFactura.setText("Pre Facturar");
        preFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                preFacturaActionPerformed(evt);
            }
        });

        metodoCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione un Método de Pago" }));
        metodoCombo.setMaximumSize(new java.awt.Dimension(171, 20));
        metodoCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                metodoComboActionPerformed(evt);
            }
        });

        aplicaIva.setSelected(true);
        aplicaIva.setText("IVA");

        tipocfd.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccione un Tipo de Comprobante" }));
        tipocfd.setMaximumSize(new java.awt.Dimension(196, 20));
        tipocfd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipocfdActionPerformed(evt);
            }
        });

        jLabel24.setText("Emisor");

        listaEmisores.setMaximumSize(new java.awt.Dimension(28, 20));
        listaEmisores.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                listaEmisoresItemStateChanged(evt);
            }
        });
        listaEmisores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listaEmisoresMouseClicked(evt);
            }
        });

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("No Identificacion");

        noIdentificacion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                noIdentificacionKeyPressed(evt);
            }
        });

        jLabel26.setText("Folio");

        jLabel23.setText("IVA Retenido");

        ivaRetenido.setEditable(false);
        ivaRetenido.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ivaRetenido.setText("0.00");
        ivaRetenido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ivaRetenidoMouseClicked(evt);
            }
        });
        ivaRetenido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ivaRetenidoActionPerformed(evt);
            }
        });
        ivaRetenido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ivaRetenidoKeyPressed(evt);
            }
        });

        jLabel27.setText("Serie");

        descripcion.setColumns(20);
        descripcion.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        descripcion.setLineWrap(true);
        descripcion.setRows(5);
        descripcion.setWrapStyleWord(true);
        descripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                descripcionKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(descripcion);

        editarConcepto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/edit-29.png"))); // NOI18N
        editarConcepto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarConceptoActionPerformed(evt);
            }
        });

        jLabel28.setText("Moneda");

        moneda.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MXN", "USD", "EUR" }));
        moneda.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                monedaItemStateChanged(evt);
            }
        });

        tipoCambio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tipoCambio.setText("1.0");
        tipoCambio.setEnabled(false);

        jLabel29.setText("ISR Retenido");

        isrRetenido.setEditable(false);
        isrRetenido.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        isrRetenido.setText("0.00");
        isrRetenido.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                isrRetenidoMouseClicked(evt);
            }
        });
        isrRetenido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isrRetenidoActionPerformed(evt);
            }
        });
        isrRetenido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                isrRetenidoKeyPressed(evt);
            }
        });

        jLabel30.setText("Descuento");

        descuentoTotal.setEditable(false);
        descuentoTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        descuentoTotal.setText("0.00");
        descuentoTotal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                descuentoTotalMouseClicked(evt);
            }
        });
        descuentoTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                descuentoTotalKeyPressed(evt);
            }
        });

        agregarLeyenda.setText("Leyenda");
        agregarLeyenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarLeyendaActionPerformed(evt);
            }
        });

        lbl_Restantes.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbl_Restantes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel2.setText("Creditos Restantes");

        verCte.setText("Ver");
        verCte.setEnabled(false);
        verCte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verCteActionPerformed(evt);
            }
        });

        jLabel10.setText("IEPS");

        ieps.setEditable(false);
        ieps.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        ieps.setText("0.00");

        aplicaIeps.setText("IEPS");

        jLabel12.setText("Condición de Pago");

        formasPagoCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione una Forma de Pago" }));
        formasPagoCombo.setMaximumSize(new java.awt.Dimension(171, 20));
        formasPagoCombo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                formasPagoComboItemStateChanged(evt);
            }
        });
        formasPagoCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formasPagoComboActionPerformed(evt);
            }
        });

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Clave Prod. SAT");

        usocfdi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione un Uso de CFDi" }));
        usocfdi.setMaximumSize(new java.awt.Dimension(151, 20));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Clave Unidad");

        aplicaIsr.setText("ISR");

        aplicaIvaRet.setText("IVA Ret.");

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Descuento %");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        descuento.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        descuento.setText("0");
        descuento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                descuentoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                descuentoFocusLost(evt);
            }
        });
        descuento.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                descuentoInputMethodTextChanged(evt);
            }
        });

        asociarCfdi.setText("Asociar CFDIs");
        asociarCfdi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                asociarCfdiActionPerformed(evt);
            }
        });

        jLabel14.setText("CFDIS Asociados:");

        cfdisAsociados.setEditable(false);
        cfdisAsociados.setEnabled(false);
        jScrollPane3.setViewportView(cfdisAsociados);

        tipoRelacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecciona el tipo de relación" }));
        tipoRelacion.setEnabled(false);
        tipoRelacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoRelacionActionPerformed(evt);
            }
        });

        jLabel15.setText("Cliente");

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
                                .addComponent(emitir)
                                .addGap(35, 35, 35)
                                .addComponent(preFactura)
                                .addGap(31, 31, 31)
                                .addComponent(agregarLeyenda)
                                .addGap(31, 31, 31)
                                .addComponent(cancelar))
                            .addComponent(tipoRelacion, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel30)
                                        .addGap(18, 18, 18))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel10)
                                            .addComponent(jLabel29))
                                        .addGap(18, 18, 18)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(isrRetenido)
                                    .addComponent(descuentoTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                                    .addComponent(ieps)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(18, 18, 18)
                                .addComponent(iva, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addGap(18, 18, 18)
                                .addComponent(ivaRetenido, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(noIdentificacion)
                                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(claveProdSat)
                                        .addComponent(jLabel13))
                                    .addGap(14, 14, 14)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(13, 13, 13)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(claveUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(unidad, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane1)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(precio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(descuento)
                                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(aplicaIva)
                                                .addComponent(aplicaIeps))
                                            .addGap(54, 54, 54)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(aplicaIsr)
                                                .addComponent(aplicaIvaRet))))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(editarConcepto, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                        .addComponent(agregarConcepto, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(quitar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabel24)
                                    .addGap(18, 18, 18)
                                    .addComponent(listaEmisores, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(44, 44, 44)
                                    .addComponent(jLabel15)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtCliente)
                                    .addGap(18, 18, 18)
                                    .addComponent(verCte)
                                    .addGap(27, 27, 27)
                                    .addComponent(jLabel27)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(serieText, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel26)
                                    .addGap(12, 12, 12)
                                    .addComponent(folioText, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(metodoCombo, 0, 236, Short.MAX_VALUE)
                                        .addComponent(formasPagoCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel12)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(condicionPago, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(tipocfd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(asociarCfdi)))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel28)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(moneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(tipoCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(usocfdi, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(lbl_Restantes, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(listaEmisores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(formasPagoCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12)
                            .addComponent(condicionPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel28)
                            .addComponent(moneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tipoCambio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(folioText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel15))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel27)
                                .addComponent(serieText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(verCte)))
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(metodoCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tipocfd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(asociarCfdi)
                                .addComponent(usocfdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_Restantes, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 3, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(noIdentificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(claveProdSat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel13)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(unidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(claveUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(agregarConcepto)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(editarConcepto)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(quitar))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(precio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(descuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(aplicaIva)
                                .addComponent(aplicaIsr))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(aplicaIeps)
                                .addComponent(aplicaIvaRet)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tipoRelacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(descuentoTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel30))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(isrRetenido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(ieps, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(iva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(ivaRetenido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(emitir)
                        .addComponent(preFactura)
                        .addComponent(cancelar)
                        .addComponent(agregarLeyenda)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cantidadActionPerformed

    private void precioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_precioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_precioActionPerformed

    private void emitirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emitirActionPerformed
        if (validar()) {
            crearLayout("");
        } else {
            JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_emitirActionPerformed

    private void setNotaDeCredito(int idEmisor, int idReceptor, String fact_uuid, double descuento, boolean tieneIva){
        //Seteamos Emisor
        for (int i = 1; i < listaEmisores.getItemCount(); i++) {
            int id = Integer.parseInt(listaEmisores.getItemAt(i).toString().split(",")[0].trim());

            if (idEmisor == id) {
                listaEmisores.setSelectedIndex(i);
                break;
            }
        }
        
        //Seteamos Receptor
        setCliente(idReceptor);
             
        //Setamos datos de CFD
        metodoCombo.setSelectedItem("PUE,Pago en una sola exhibición");
        tipocfd.setSelectedItem("E,Egreso");
        formasPagoCombo.setSelectedItem("99,Por definir");
        tipoRelacion.setSelectedItem("01,Nota de crédito de los documentos relacionados");

        //Seteamos concepto
        if (tieneIva) {
            descuento = util.redondear(descuento / 1.16);
        }
        noIdentificacion.setText("00001");
        claveProdSat.setText("84111506");
        cantidad.setText("1");
        claveUnidad.setText("ACT");
        unidad.setText("Actividad");
        descripcion.setText("Servicios de Facturación");
        precio.setText("" + descuento);
        aplicaIva.setSelected(tieneIva);

        agregarConceptoActionPerformed(null);

        //Seteamos factura asociada
        cfdisAsoc = new ArrayList();
        cfdisAsoc.add(fact_uuid);
        setUuids(null);
    }
    
    private boolean validar() {
        if (txtCliente.getText().trim().isEmpty()) {
            mensaje = "No se ha seleccionado ningun cliente, seleccione e intentelo de nuevo.";
            return false;
        }

        if (tipocfd.getSelectedIndex() == 0) {
            mensaje = "No se ha seleccionado ningun tipo de comprobante, seleccione e intentelo de nuevo.";
            return false;
        }

        if (usocfdi.getSelectedIndex() == 0) {
            mensaje = "No se ha seleccionado ningun uso de cfdi, seleccione e intentelo de nuevo.";
            return false;
        }

        if (metodoCombo.getSelectedIndex() == 0) {
            mensaje = "No se ha seleccionado ningun metodo de pago, seleccione e intentelo de nuevo.";
            return false;
        }

        if (formasPagoCombo.getSelectedIndex() == 0) {
            mensaje = "No se ha seleccionado ninguna forma de pago, seleccione e intentelo de nuevo.";
            return false;
        }

        if (model.getRowCount() == 0) {
            mensaje = "Necesita almenos un concepto o articulo para poder continuar";
            return false;
        }

        if (creditosRestantes == 0) {
            mensaje = "El Emisor con RFC: " + rfcE + " no cuenta con\ncreditos. Llame al (667)2-80-29-66 \no al 2-80-44-44 para aquirir mas";
            return false;
        }

        if (uuids != null && !uuids.trim().isEmpty()) {
            if (tipoRelacion.getSelectedIndex() == 0) {
                mensaje = "Debe seleccionar un tipo de relación para los CFDIs";
                return false;
            }
        }

        return true;
    }

    private Empleado getEmpleado(int idEmpleado) throws SQLException {
        Empleado empleado;
        Statement stmtE = factory.stmtLectura(Elemento.odbc());
        ResultSet rsE = stmtE.executeQuery("SELECT * FROM Empleados WHERE id = " + idEmpleado);
        if (rsE.next()) {
            empleado = new Empleado();
            empleado.setBanco(rsE.getString("banco"));
            empleado.setClabe(rsE.getString("clabe"));
            empleado.setCurp(rsE.getString("curp"));
            empleado.setDepartamento(rsE.getString("departamento"));
            empleado.setFechaInicialRelLaboral(rsE.getDate("fechaInicialRelLaboral"));
            empleado.setIdEmpleado(rsE.getInt("idEmpleado"));
            empleado.setNss(rsE.getString("nss"));
            empleado.setNumEmpleado(rsE.getString("numEmpleado"));
            empleado.setPeriodicidadPago(rsE.getString("periodicidadPago").split(",")[0]);
            empleado.setPuesto(rsE.getString("puesto"));
            empleado.setRiesgoPuesto(rsE.getString("riesgoPuesto"));
            empleado.setSalarioBaseCotApor(rsE.getBigDecimal("salarioBaseCotApor"));
            empleado.setSalarioDiarioInt(rsE.getBigDecimal("salarioDiarioInt"));
            empleado.setTipoContrato(rsE.getString("tipoContrato"));
            empleado.setTipoJornada(rsE.getString("tipoJornada"));
            empleado.setTipoRegimen(rsE.getString("tipoRegimen"));
            rsE.close();
            stmtE.close();
            return empleado;
        } else {
            return null;
        }
    }
    
    public void resetCliente(int idCliente){
        if(idCliente > 0){
            listaClientes.clear();
            texter.removeAllItems();
            consultar("Combo", "SELECT * FROM Clientes");
            texter.setItems(listaClientes.toArray());
            
            for (int i = 0; i < listaClientes.size(); i++) {
                int id = Integer.parseInt(listaClientes.get(i).split(",")[0].trim());

                if (idCliente == id) {
                    txtCliente.transferFocus();
                    String nom = listaClientes.get(i);
                    txtCliente.setText(nom.substring(0, nom.length() -2));
                    
                    KeyEvent ke = new KeyEvent(this.txtCliente, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '\n');
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().redispatchEvent(this.txtCliente, ke);
                    
                    break;
                }
            }
        }else{
            txtCliente.setText("");
            verCte.setEnabled(false);
        }
    }
    
    public void setCliente(int idCliente){
        for (int i = 0; i < listaClientes.size(); i++) {
            int id = Integer.parseInt(listaClientes.get(i).split(",")[0].trim());

            if (idCliente == id) {
                txtCliente.transferFocus();
                String nom = listaClientes.get(i);
                txtCliente.setText(nom.substring(0, nom.length() -2));

                KeyEvent ke = new KeyEvent(this.txtCliente, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '\n');
                KeyboardFocusManager.getCurrentKeyboardFocusManager().redispatchEvent(this.txtCliente, ke);

                break;
            }
        }
    }

    private void crearLayout(String condicion) {
        Elemento.leerConfig(emisor.getRfc());
        Layout layout = null;
        Factura fact = setearFactura(condicion);

        int folioNext = Integer.parseInt(folio) + 1;

        if (!modificar) {
            layout = new Layout(fact);
        } else {
            layout = new Layout(fact, folio);
        }

        if (condicion.equals("")) {
            resetCliente(0);
            rfc = "";
            this.formasPagoCombo.setSelectedIndex(0);
            this.usocfdi.setSelectedIndex(0);
            this.metodoCombo.setSelectedIndex(0);
            claveProdSat.setText("");
            cantidad.setText("");
            unidad.setText("");
            descripcion.setText("");
            precio.setText("");
            subtotal.setText("");
            descuentoTotal.setText("0.00");
            iva.setText("0.00");
            ivaRetenido.setText("0.00");
            isrRetenido.setText("0.00");
            total.setText("0.00");
            folioText.setText("" + folioNext);
            ieps.setText("0.00");
            //layout.conectar();
            this.aplicaIva.setSelected(true);
            this.aplicaIeps.setSelected(false);
            this.aplicaIsr.setSelected(false);
            this.aplicaIvaRet.setSelected(false);
            this.tipoRelacion.setSelectedIndex(0);
            this.cfdisAsociados.setText("");
            model.setRowCount(0);
            iniciar();
        }

    }

//    private String consultarSerie(String tipoCfd){
//        Connection con = Elemento.odbc();
//        Statement stmt = factory.stmtLectura(con);
//        ResultSet rs;
//        String ser = "F";
//        try{
//            rs = stmt.executeQuery("SELECT serie FROM Folios WHERE idComprobante = "+tipoCfd);
//            if(rs.next()){
//                ser = rs.getString("serie");
//            }
//        }catch(Exception ex){
//            ex.printStackTrace();
//            Elemento.log.error("Excepcion al consultar la Serie: " + ex.getMessage());
//        }
//        return ser;
//    }
    private Factura setearFactura(String condicion) {
        List<String> conc = new ArrayList();
        String aux = "";
        String rfce = rfc;
        Factura fact = new Factura();
        String sub = subtotal.getText().trim(),
                iv = iva.getText().trim(),
                tot = total.getText().trim();
        boolean continuar = true;
        String idEmisor = listaEmisores.getSelectedItem().toString().split(",")[0];
        Donataria donat = obtenerDonataria(idEmisor);

        if (emisor == null) {
            consultar("Emisores", "SELECT * FROM Emisores WHERE id=" + idEmisor);
        }

        Elemento.leerConfig(emisor.getRfc());

        while (!validarRfc(rfce)) {
            int op = JOptionPane.showConfirmDialog(null, "El RFC " + rfce + " es invalido, desea cambiarlo?");
            switch (op) {
                case 0:
                    rfce = JOptionPane.showInputDialog("Cambie el RFC por uno válido:");
                    break;
                case 1:
                    JOptionPane.showMessageDialog(null, "Se utilizara el RFC Generico: XAXX010101000");
                    rfce = "XAXX010101000";
                    break;
                case 2:
                    continuar = false;
                    break;
            }
            rfc = rfce;
            if (!continuar) {
                break;
            } else if (op == 1) {
                break;
            }
        }
        if (continuar) {
            List<ConceptoTraslado> tr = new ArrayList();
            List<ConceptoRetencion> re = new ArrayList();
            ConceptoTraslado ct;
            ConceptoRetencion cr;

            //ClaveProdServ@ClaveUnidad@Unidad@NoIdentificacion@Cantidad@Descripcion@ValorUnitario@Importe
            for (int i = 0; i < model.getRowCount(); i++) {
                aux = "C" + (i + 1) + ": ";
                aux += model.getValueAt(i, 1).toString() + "@";
                aux += model.getValueAt(i, 3).toString() + "@";
                aux += model.getValueAt(i, 4).toString() + "@";
                aux += model.getValueAt(i, 0).toString() + "@";
                aux += model.getValueAt(i, 2).toString() + "@";
                aux += model.getValueAt(i, 5).toString() + "@";
                aux += model.getValueAt(i, 6).toString() + "@";
                aux += model.getValueAt(i, 7).toString() + "@";
                aux += model.getValueAt(i, 8).toString();

                BigDecimal base = new BigDecimal(model.getValueAt(i, 8).toString()).subtract(new BigDecimal(model.getValueAt(i, 7).toString())).setScale(2, RoundingMode.HALF_UP);

                ct = fact.new ConceptoTraslado();
                ct.setBase(base);
                ct.setImporte(base.multiply(tasaIva.get(i)));
                ct.setImpuesto("002");
                ct.setNumConcepto(i + 1);
                ct.setTasa(redondear(tasaIva.get(i), 6));
                ct.setTipoFactor("Tasa");

                tr.add(ct);

                if ((Boolean) model.getValueAt(i, 10)) {
                    ct = fact.new ConceptoTraslado();
                    ct.setBase(base);
                    ct.setImporte(base.multiply(tasaIeps.get(i)));
                    ct.setImpuesto("003");
                    ct.setNumConcepto(i + 1);
                    ct.setTasa(redondear(tasaIeps.get(i), 6));
                    ct.setTipoFactor("Tasa");

                    tr.add(ct);
                }

                if ((Boolean) model.getValueAt(i, 11)) {
                    cr = fact.new ConceptoRetencion();
                    cr.setBase(base);
                    cr.setImporte(redondear(base.multiply(tasaIsr.get(i)), 2));
                    cr.setImpuesto("001");
                    cr.setNumConcepto(i + 1);
                    cr.setTasa(redondear(tasaIsr.get(i), 6));
                    cr.setTipoFactor("Tasa");

                    re.add(cr);
                }

                if ((Boolean) model.getValueAt(i, 12)) {
                    BigDecimal tas = redondear(tasaIvaRet.get(i), 6);
                    BigDecimal imp = redondear(base.multiply(tas), 2);
                    cr = fact.new ConceptoRetencion();
                    cr.setBase(base);
                    cr.setImporte(imp);
                    cr.setImpuesto("002");
                    cr.setNumConcepto(i + 1);
                    cr.setTasa(tas);
                    cr.setTipoFactor("Tasa");

                    re.add(cr);
                }

                conc.add(aux);
            }

            fact.traslados = tr;
            fact.retenciones = re;
            System.out.println(aux);

            folio = folioText.getText().trim();
            serie = serieText.getText().trim();

            BigDecimal ivR = new BigDecimal(ivaRetenido.getText().trim());
            BigDecimal isr = new BigDecimal(isrRetenido.getText().trim());
            BigDecimal ivaT = new BigDecimal(iv);
            BigDecimal iepsT = new BigDecimal(ieps.getText().trim());
            BigDecimal desTot = new BigDecimal(descuentoTotal.getText().trim());

            //Asginamos valores de la factura
            fact.emisor = emisor;
            fact.folio = folio;
            fact.serie = serie;
            fact.nombre = nombre;
            fact.rfc = rfce;
            fact.cp = cp;
            fact.regimenFiscalReceptor = regimenFiscalReceptor;
            fact.numRegIdTrib = "";
            /*fact.calle = calle;
            fact.colonia = colonia;
            fact.numExt = numExt;
            fact.numInt = numInt;
            fact.localidad = localidad;
            fact.cp = cp;
            fact.municipio = municipio;
            fact.estado = estado;*/
            fact.pais = pais;
            fact.conceptos = conc;
            fact.subtotal = new BigDecimal(sub);
            fact.descuento = desTot;
            fact.motivoDescuento = "_";
            fact.iva = ivaT;
            fact.ivaRetenido = ivR;
            fact.total = new BigDecimal(tot);
            fact.usoCfdi = usocfdi.getSelectedItem().toString().split(",")[0];
            fact.formaPago = formasPagoCombo.getSelectedItem().toString().split(",")[0];
            fact.tipoCfd = tipocfd.getSelectedItem().toString().split(",")[0];
            fact.metodoPago = metodoCombo.getSelectedItem().toString().split(",")[0];
            fact.lugarExpedicion = Elemento.lugarExpedicion;
            fact.condicionPago = condicionPago.getText().trim();
            fact.moneda = moneda.getSelectedItem().toString().trim();
            fact.tipoCambio = tipoCambio.getText().trim().trim();
            fact.isrRetenido = isr;
            fact.idEmpleado = idEmpleado;
            fact.cfdisAsociados = uuids;
            fact.tipoRelacion = uuids != null ? tipoRelacion.getSelectedItem().toString().split(",")[0].trim() : null;
            fact.setDonataria(donat);

            if (tasaIeps.size() > 0) {
                fact.porIeps = redondear(tasaIeps.get(0).multiply(new BigDecimal("100")), 6);
                fact.totalIeps = iepsT;
            }

            fact.totalRetenidos = redondear(ivR.add(isr), 2);
            fact.totalTraslados = redondear(ivaT.add(iepsT), 2);
            fact.leyenda = leyenda;
            fact.prefactura = condicion;

            return fact;
        }
        return null;
    }

    public boolean validarRfc(String rfc) {
        rfc = rfc.toUpperCase().trim();
        if (rfc.contains(" ")) {
            return false;
        } else if (rfc.contains("&")) {
            return true;
        } else {
            return rfc.toUpperCase().matches("[A-Z]{4}[0-9]{6}[A-Z0-9]{3}") || rfc.toUpperCase().matches("[A-Z]{3}[0-9]{6}[A-Z0-9]{3}");
        }
    }

    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed
        Elemento.log.info("Se ha cerrado el programa");
        this.setVisible(false);
    }//GEN-LAST:event_cancelarActionPerformed

    private void agregarConceptoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarConceptoActionPerformed
        Object[] concept = new Object[13];
        if (cantidad.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe introducir una cantidad", "Mensaje", JOptionPane.WARNING_MESSAGE);
        } else {
            BigDecimal cant = new BigDecimal(cantidad.getText().trim());
            BigDecimal prec = new BigDecimal(precio.getText().trim()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal desc = new BigDecimal(descuento.getText().trim()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal impDesc;
            if (desc.doubleValue() > 100 || desc.doubleValue() < 0) {
                JOptionPane.showMessageDialog(null, "El descuento no puede ser mayor a 100 ni menor a 0", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                desc = desc.divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal impor = cant.multiply(prec).setScale(2, RoundingMode.HALF_UP);
                impDesc = impor.multiply(desc).setScale(2, RoundingMode.HALF_UP);

                Boolean aplica = aplicaIva.isSelected();
                Boolean aplIeps = aplicaIeps.isSelected();
                Boolean aplIsr = aplicaIsr.isSelected();
                Boolean aplIvaRet = aplicaIvaRet.isSelected();

                concept[0] = noIdentificacion.getText();
                concept[1] = claveProdSat.getText();
                concept[2] = cant.toString();
                concept[3] = claveUnidad.getText();
                concept[4] = unidad.getText();
                concept[5] = descripcion.getText();
                concept[6] = prec.toString();
                concept[7] = impDesc.toString();
                concept[8] = impor.toString();
                concept[9] = aplica;
                concept[10] = aplIeps;
                concept[11] = aplIsr;
                concept[12] = aplIvaRet;

                model.addRow(concept);

                if (!aplica) {
                    int resp;
                    if (this.fromFolios) {
                        resp = JOptionPane.YES_OPTION;
                    } else {
                        resp = JOptionPane.showConfirmDialog(null, "El producto está exento de IVA?", "IVA exento o IVA 0%", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    }

                    if (resp == JOptionPane.YES_OPTION) {
                        porcentaje = new BigDecimal("-1");
                    } else {
                        porcentaje = BigDecimal.ZERO;
                    }
                    tasaIva.add(porcentaje);
                } else {
                    porcentaje = new BigDecimal("0.16");
                    tasaIva.add(porcentaje);
                }

                if (aplicaIeps.isSelected()) {
                    porcentajeIeps = new BigDecimal(JOptionPane.showInputDialog(null, "Ingresa el porcentaje de IEPS a aplicar:", 8.0));

                    if (porcentajeIeps.doubleValue() < 1) {
                        tasaIeps.add(porcentajeIeps);
                    } else {
                        tasaIeps.add(redondear(porcentajeIeps.divide(new BigDecimal(100)), 6));
                    }
                }

                if (aplIsr) {
                    porcentajeIsr = new BigDecimal(JOptionPane.showInputDialog(null, "Ingresa el porcentaje de ISR a aplicar:", 0.0));

                    if (porcentajeIsr.doubleValue() < 1) {
                        tasaIsr.add(porcentajeIsr);
                    } else {
                        tasaIsr.add(redondear(porcentajeIsr.divide(new BigDecimal(100)), 6));
                    }
                }

                if (aplIvaRet) {
                    porcentajeIvaRet = new BigDecimal(JOptionPane.showInputDialog(null, "Ingresa el porcentaje de IVA a retener:", 4.0));

                    if (porcentajeIvaRet.doubleValue() < 1) {
                        tasaIvaRet.add(porcentajeIvaRet);
                    } else {
                        tasaIvaRet.add(redondear(porcentajeIvaRet.divide(new BigDecimal(100)), 6));
                    }
                }

                claveProdSat.setText("");
                cantidad.setText("");
                unidad.setText("");
                claveUnidad.setText("");
                noIdentificacion.setText("");
                descripcion.setText("");
                precio.setText("");
                descuento.setText("0");
                aplicaIva.setSelected(true);
                aplicaIeps.setSelected(false);
                aplicaIsr.setSelected(false);
                aplicaIvaRet.setSelected(false);
                noIdentificacion.requestFocus();

                calcular();
                //agregarProducto();
            }
        }
    }//GEN-LAST:event_agregarConceptoActionPerformed

//    private void agregarProducto(){
//        Connection con = Elemento.odbc();
//        Statement stmt = factory.stmtEscritura(con);
//        String query = "INSERT INTO Productos VALUES ("
//                    + "\'"+noIdentificacion.getText().trim()+"\',\'"+descripcion.getText().trim()+"\', \'"+unidad.getSelectedItem().toString().trim()+"\',\'"
//                    +precio.getText().trim()+"\',";
//        try{
//            if(aplicaIva.isSelected()){
//                query += Boolean.TRUE + ")";
//            }else{
//                query += Boolean.FALSE + ")";
//            }
//            stmt.executeUpdate(query);
//            stmt.close();
//            con.close();
//            Elemento.log.info("El producto " + descripcion.getText().trim() + " fue agregado correctamente...");
//        }catch(Exception e){
//            e.printStackTrace();
//            System.out.println(e.getCause().getMessage());
//            Elemento.log.error("Excepcion al agregar un producto desde CrearCFDi: " + e.getMessage());
//        }
//    }

    private void quitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitarActionPerformed
        int row = conceptos.getSelectedRow();
        if ((Boolean) model.getValueAt(row, 9)) {
            tasaIva.remove(row);
        }

        if ((Boolean) model.getValueAt(row, 10)) {
            tasaIeps.remove(row);
        }

        if ((Boolean) model.getValueAt(row, 11)) {
            tasaIsr.remove(row);
        }

        if ((Boolean) model.getValueAt(row, 12)) {
            tasaIvaRet.remove(row);
        }

        model.removeRow(row);
        calcular();
    }//GEN-LAST:event_quitarActionPerformed

    private void preFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_preFacturaActionPerformed
        crearLayout("PREFACTURA");
    }//GEN-LAST:event_preFacturaActionPerformed

    public final void consultar(String x, String query) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);

        try {
            ResultSet rs = stmt.executeQuery(query);
            if (x.equalsIgnoreCase("ComboTC")) {
                tipocfd.addItem("Seleccione un Tipo de Comprobante");
            }
            if (rs.next()) {
                rs.previous();
                while (rs.next()) {
                    switch (x) {
                        case "Folio":
                            folio = "" + rs.getInt("ultimo_folio");
                            serie = rs.getString("serie").trim();
                            plantilla = rs.getString("plantilla").trim();
                            if (folioText != null) {
                                folioText.setText(folio);
                                serieText.setText(serie);
                            }
                            break;
                        case "Combo":
                            listaClientes.add(rs.getInt("id") + "," + rs.getString("nombre"));
                            break;
                        case "ComboM":
                            metodoCombo.addItem(rs.getString("metodopago") + "," + rs.getString("descripcion"));
                            break;
                        case "ComboF":
                            String valor = rs.getString("formapago") + "," + rs.getString("descripcion");
                            formasPagoCombo.addItem(valor);
                            formasPagoLista.add(valor);
                            bancarizado.add(rs.getBoolean("bancarizado"));
                            break;
                        case "ComboTC":
                            tipocfd.addItem(rs.getString("tiposcomprobante") + "," + rs.getString("descripcion"));
                            break;
                        case "ComboU":
                            usocfdi.addItem(rs.getString("usocfdi") + "," + rs.getString("descripcion"));
                            break;
                        case "ComboT":
                            String rel = rs.getInt("tiporelacion") > 9 ? rs.getString("tiporelacion") : ("0" + rs.getString("tiporelacion")) + "," + rs.getString("descripcion");
                            arrayTipoRel.add(rel);
                            tipoRelacion.addItem(rel);
                            break;
                        case "Campos":
                            int idCliente = rs.getInt("id");

                            rfc = rs.getString("rfc");
                            nombre = rs.getString("nombre");
                            calle = rs.getString("calle");
                            numExt = rs.getString("noExterior");
                            numInt = rs.getString("noInterior");
                            colonia = rs.getString("colonia");
                            localidad = rs.getString("localidad");
                            municipio = rs.getString("municipio");
                            estado = rs.getString("estado");
                            pais = rs.getString("pais");
                            cp = rs.getString("cp");
                            tipoPersona = rs.getString("tipoPersona");
                            regimenFiscalReceptor = rs.getString("regimenfiscal");

                            receptor = new Receptor();
                            receptor.setId(idCliente);
                            receptor.setNombre(nombre);
                            receptor.setRfc(rfc);
                            receptor.setPais(pais);
                            receptor.setCp(cp);
                            receptor.setRegimenFiscal(regimenFiscalReceptor);
                            
                            verCte.setEnabled(true);
                            
                            String campo = tipoPersona.equals("F") ? "aplicaFisica" : "aplicaMoral";
                            String consulta = "SELECT * FROM c_usocfdi WHERE " + campo + " = " + Boolean.TRUE + (regimenFiscalReceptor.trim().isEmpty() ? "" : " AND (regimenFiscalReceptor LIKE '*"+ regimenFiscalReceptor +"*' OR regimenFiscalReceptor = '" + regimenFiscalReceptor + "')");
                            
                            this.usocfdi.removeAllItems();
                            this.usocfdi.addItem("Seleccione un Uso de CFDi");
                            consultar("ComboU", consulta);
                            
                            if(this.fromFolios && this.isNotaCredito){
                                usocfdi.setSelectedItem("G02,Devoluciones, descuentos o bonificaciones.");
                            }
                            break;
                        case "ComboE":
                            listaEmisores.addItem(rs.getInt("id") + ", " + rs.getString("nombre"));
                            break;
                        case "Emisores":
                            emisor = setearEmisor(rs);
                            break;
                    }
                }
            } else {
                borrarTodo();
            }
            if (x.equalsIgnoreCase("ComboE")) {
                checarComprobantes();
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception ex) {
            Elemento.log.error("Excepcion: No se pudo consultar los Clientes: " + ex.getLocalizedMessage(), ex);
            ex.printStackTrace();
        }
    }

    private void checarComprobantes() {
        tipocfd.removeAllItems();
        String tmp = comboE.getSelectedItem().toString();
        String idEmi = tmp.split(",")[0].trim();

        Connection con = Elemento.odbc();
        Statement stmtC = factory.stmtLectura(con);
        ResultSet rsC;
        String tiposco = "";
        int restantes = 0;
        try {
            consultar("Emisores", "SELECT * FROM Emisores WHERE id=" + idEmi);
            rfcE = emisor.getRfc();

            rsC = stmtC.executeQuery("SELECT * FROM Cuentas WHERE rfc = \'" + rfcE + "\'");
            if (rsC.next()) {
                restantes = rsC.getInt("creditosRestantes");

                creditosRestantes = restantes;

                if (tiposco.length() > 0) {
                    tiposco += ",5";
                } else {
                    tiposco += "5";
                }

                consultar("ComboTC", "SELECT tc.tiposcomprobante, tc.descripcion "
                        + "FROM Folios f INNER JOIN c_tiposcomprobante tc ON f.idComprobante = tc.c_tiposcomprobante_id "
                        + "WHERE f.rfc = \'" + rfcE + "\' "
                        + "AND f.idComprobante <> 4 "
                        + "ORDER BY tc.c_tiposcomprobante_id ASC");

            }
            rsC.close();
            con.close();
            prepararFolio(rfcE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al checaar los tipos de comprobante admitidos para el cliente: " + tmp + " : " + ex.getMessage(), ex);
        } finally {
            try {
                stmtC.close();
                con.close();
            } catch (SQLException sex) {
                sex.printStackTrace();
            }
        }
        if (restantes == 0) {
            if (contadorComprobantes == 0) {
                JOptionPane.showMessageDialog(null, "El Emisor con RFC: " + rfcE + " no cuenta con\ncreditos. Llame al (667)2-80-29-66 \no al 2-80-44-44 para aquirir mas");
                contadorComprobantes = 1;
            } else if (contadorComprobantes == 1) {
                contadorComprobantes = 0;
            }
//            int num = listaEmisores.getItemCount();
//            int index = listaEmisores.getSelectedIndex();
//            if(num > 1){
//                if(index > 0){
//                    listaEmisores.setSelectedIndex(0);
//                }else{
//                    listaEmisores.setSelectedIndex(1);
//                }
//            }
        }
    }

    private void borrarTodo() {
        rfc = "";
        calle = "";
        numExt = "";
        numInt = "";
        colonia = "";
        localidad = "";
        municipio = "";
        estado = "";
        pais = "";
        cp = "";
        resetCliente(0);

        if (model != null) {
            model.setRowCount(0);
        }
    }

    private void metodoComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_metodoComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_metodoComboActionPerformed

    private void ivaRetenidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ivaRetenidoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
//            if(!(iva.getText().trim().equalsIgnoreCase("0.0") || iva.getText().trim().equalsIgnoreCase("0.00"))){
//                double iv = Double.parseDouble(iva.getText().trim());
//                double ivr = Double.parseDouble(ivaRetenido.getText().trim());
//                double des = Double.parseDouble(descuento.getText().trim());
//                double sb = Double.parseDouble(subtotal.getText().trim());
//                double tot = (sb-des)+(iv-ivr);
//
//                total.setText(""+redondear(tot));
//            }else{
//                ivaRetenido.setText("0.00");
//            }
            calcular();
        }
    }//GEN-LAST:event_ivaRetenidoKeyPressed

    private void ivaRetenidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ivaRetenidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ivaRetenidoActionPerformed

    private void noIdentificacionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_noIdentificacionKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            this.consultarProducto("noIdentificacion", noIdentificacion.getText().trim());
        }
    }//GEN-LAST:event_noIdentificacionKeyPressed


    private void listaEmisoresItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_listaEmisoresItemStateChanged
        // TODO add your handling code here:
        checarComprobantes();
        lbl_Restantes.setText("" + creditosRestantes);
    }//GEN-LAST:event_listaEmisoresItemStateChanged


    private void editarConceptoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarConceptoActionPerformed
        // TODO add your handling code here:
        int pos = conceptos.getSelectedRow();
        noIdentificacion.setText(model.getValueAt(pos, 0).toString());
        claveProdSat.setText(model.getValueAt(pos, 1).toString());
        cantidad.setText(model.getValueAt(pos, 2).toString());
        claveUnidad.setText(model.getValueAt(pos, 3).toString());
        unidad.setText(model.getValueAt(pos, 4).toString().trim());
        descripcion.setText(model.getValueAt(pos, 5).toString());
        precio.setText(model.getValueAt(pos, 6).toString());
        descuento.setText(model.getValueAt(pos, 7).toString());
        aplicaIva.setSelected((Boolean) model.getValueAt(pos, 9));
        aplicaIeps.setSelected((Boolean) model.getValueAt(pos, 10));
        aplicaIsr.setSelected((Boolean) model.getValueAt(pos, 11));
        aplicaIvaRet.setSelected((Boolean) model.getValueAt(pos, 12));

        this.quitarActionPerformed(evt);
    }//GEN-LAST:event_editarConceptoActionPerformed

    private void monedaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_monedaItemStateChanged
        // TODO add your handling code here:
        if (!moneda.getSelectedItem().toString().equalsIgnoreCase("MXN")) {
            tipoCambio.setEnabled(true);
            moneda.transferFocus();
            tipoCambio.setText("");
        } else {
            tipoCambio.setEnabled(false);
            tipoCambio.setText("1.0");
        }
    }//GEN-LAST:event_monedaItemStateChanged

    private void isrRetenidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isrRetenidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_isrRetenidoActionPerformed

    private void isrRetenidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_isrRetenidoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            calcular();
        }
    }//GEN-LAST:event_isrRetenidoKeyPressed

    private void descuentoTotalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descuentoTotalKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            calcular();
        }
    }//GEN-LAST:event_descuentoTotalKeyPressed

    private void descripcionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descripcionKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            evt.consume();
            this.consultarProducto("descripcion", descripcion.getText().trim());
        }
        if (evt.getKeyCode() == KeyEvent.VK_TAB) {
            evt.consume();
            descripcion.transferFocus();
        }
    }//GEN-LAST:event_descripcionKeyPressed

    private void agregarLeyendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarLeyendaActionPerformed
        // TODO add your handling code here:
        leyenda = JOptionPane.showInputDialog("Ingrese la leyenda que desea mostrar en el comprobante:                     ", leyenda);
        if (leyenda == null) {
            leyenda = "";
        } else {
            leyenda = leyenda.trim();
        }
    }//GEN-LAST:event_agregarLeyendaActionPerformed

    private void listaEmisoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listaEmisoresMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_listaEmisoresMouseClicked

    private void descuentoTotalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_descuentoTotalMouseClicked
        // TODO add your handling code here:
        descuentoTotal.selectAll();
    }//GEN-LAST:event_descuentoTotalMouseClicked

    private void isrRetenidoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_isrRetenidoMouseClicked
        // TODO add your handling code here:
        isrRetenido.selectAll();
    }//GEN-LAST:event_isrRetenidoMouseClicked

    private void ivaRetenidoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ivaRetenidoMouseClicked
        // TODO add your handling code here:
        ivaRetenido.selectAll();
    }//GEN-LAST:event_ivaRetenidoMouseClicked

    private void verCteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verCteActionPerformed
        // TODO add your handling code here:
        if(txtCliente.getText().trim().isEmpty()){
            util.printError("No hay ningún cliente seleccionado");
        }else{
            CteUpdate cte = new CteUpdate(""+receptor.getId(), this);
            cte.setVisible(true);
            cte.crearComprobante.setEnabled(false);
        }
    }//GEN-LAST:event_verCteActionPerformed

    private void cantidadFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cantidadFocusGained
        if (!noIdentificacion.getText().trim().isEmpty() && descripcion.getText().trim().isEmpty()) {
            this.consultarProducto("noIdentificacion", noIdentificacion.getText().trim());
        }
    }//GEN-LAST:event_cantidadFocusGained

    private void formasPagoComboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_formasPagoComboItemStateChanged
        // TODO add your handling code here:

    }//GEN-LAST:event_formasPagoComboItemStateChanged

    private void formasPagoComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formasPagoComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_formasPagoComboActionPerformed

    private void descuentoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descuentoFocusGained
        // TODO add your handling code here:
        descuento.selectAll();
    }//GEN-LAST:event_descuentoFocusGained

    private void descuentoInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_descuentoInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_descuentoInputMethodTextChanged

    private void descuentoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_descuentoFocusLost
        // TODO add your handling code here:
        if (descuento.getText().trim().isEmpty()) {
            descuento.setText("0");
        }
    }//GEN-LAST:event_descuentoFocusLost

    private void asociarCfdiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asociarCfdiActionPerformed
        if (rfc != null && !rfc.trim().isEmpty()) {
            Folios fol = new Folios(emisor.getRfc(), rfc, this, null);
            fol.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Debe seleccionar a un cliente", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_asociarCfdiActionPerformed

    public void setUuids(Folios fol) {
        if (!this.fromFolios) {
            cfdisAsoc = fol.cfdisAsoc;
            fol.dispose();
        }

        uuids = "";

        for (int i = 0; i < cfdisAsoc.size(); i++) {
            String x = cfdisAsoc.get(i);
            uuids += x;
            if (i < cfdisAsoc.size() - 1) {
                uuids += ",";
            }
        }
        cfdisAsociados.setText(uuids);
        tipoRelacion.setEnabled(true);
    }

    private void tipoRelacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoRelacionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tipoRelacionActionPerformed

    private void tipocfdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipocfdActionPerformed
        String tipo = tipocfd.getSelectedItem() != null ? tipocfd.getSelectedItem().toString().split(",")[0] : "";

        if (tipocfd.getSelectedIndex() > 0) {
            if (!txtCliente.getText().trim().isEmpty()) {
                prepararFolio(rfcE);
                String id = listaEmisores.getSelectedItem().toString().split(",")[0];

                switch (tipo) {
                    case "E":
                        asociarCfdi.setEnabled(true);
                        break;
                    case "P":
                        rp = new RecibosPagos();
                        asociarCfdi.setEnabled(true);
                        if (!rp.isVisible()) {
                            if (emisor == null) {
                                consultar("Emisores", "SELECT * FROM Emisores WHERE id = " + id);
                            }
                            rp.setEmisor(emisor);
                            rp.setReceptor(receptor);
                            rp.setFormasPago(formasPagoCombo.getModel());
                            rp.setBancarizado(bancarizado);
                            rp.setFolio(this.folioText.getText());
                            rp.setArrayTipoRel(arrayTipoRel);
                            rp.setVisible(true);
                            this.tipocfd.setSelectedIndex(0);
                        }
                        break;
                    case "I":
                        asociarCfdi.setEnabled(true);
                        break;
                }

                /*if (tipocfd.getItemCount() > 0) {
                    listaClientes.removeAllItems();
                    if (tipocfd.getSelectedItem().toString().equals("Recibo de Nomina")) {
                        listaClientes.addItem("Seleccione un empleado");
                        consultar("Combo", "SELECT * FROM EmpleadosRec WHERE idEmisor = " + id);
                    } else {
                        listaClientes.addItem("Seleccione un cliente");
                        consultar("Combo", "SELECT * FROM Clientes");
                    }
                }*/
            } else {
                util.print("Debe seleccionar un cliente");
                tipocfd.setSelectedIndex(0);
            }
        }
    }//GEN-LAST:event_tipocfdActionPerformed

    private void prepararFolio(String rfcE) {
        if (tipocfd.getItemCount() > 0) {
            int tipo = getIdComprobante(tipocfd.getSelectedItem().toString().split(",")[0]);
            consultar("Folio", "SELECT * FROM Folios WHERE rfc = \'" + rfcE + "\' AND idComprobante = " + tipo);
        }
    }

    public static void visualizar(String ruta, String name, String jsonDomicilios) throws Exception {
        String xml;
//        if(name.contains("_PREFACTURA")){
//            xml = ruta+name.replace("_PREFACTURA", " ").trim()+".xml";
//        }else{
//            xml = ruta+name+".xml";
//        }
        xml = ruta + name + ".xml";
        String pdf = Elemento.pathPdf;
        if (plantilla == null || plantilla.equalsIgnoreCase("")) {
            plantilla = Elemento.pathPlantillas + "FacturaE_V33.jasper";
        }
        String logo = Elemento.logo;
        if (jsonDomicilios != null) {
            util.setJsonDomicilios(jsonDomicilios);
        }
        if (Folios.isCancelado != null) {
            if (Folios.isCancelado) {
                util.crearPdfCancelado(xml, pdf, name, logo);
            } else {
                util.generarPdf(pdf, xml, name, logo, plantilla, true);
            }
        } else {
            util.generarPdf(pdf, xml, name, logo, plantilla, true);
        }
    }

    //Se agrega nuevo parametro para el envio de los domicilios
    public static void visualizar(String ruta, String name, String email, String jsonDomicilios) {
        util.setJsonDomicilios(jsonDomicilios);
        String xml = ruta + name + ".xml";
        String pdf = Elemento.pathPdf;
        if (plantilla == null || plantilla.equalsIgnoreCase("")) {
            plantilla = Elemento.pathPlantillas + "FacturaE_V33.jasper";
        }
        String logo = Elemento.logo;
        try {
            util.generarPdf(pdf, xml, name, logo, plantilla, false);
            util.enviarEmail("", "", email, name + " : SERVICIO DE REPOSITORIO", xml, pdf + name + ".pdf", name + ".xml", name + ".pdf", "");
            Exe.exeSinTiempo(pdf + name + ".pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Se agrega nuevo parametro para el envio de los domicilios
    public static void visualizarInterpretado(String ruta, String rutaModificado, String name, String email, String jsonDomicilios) {
        util.setJsonDomicilios(jsonDomicilios);
        String xml = ruta + name + ".xml";
        String xmlModificado = rutaModificado + name + ".xml";
        String pdf = Elemento.pathPdf;
        if (plantilla == null || plantilla.equalsIgnoreCase("")) {
            plantilla = Elemento.pathPlantillas + "FacturaE_V33.jasper";
        }
        String logo = Elemento.logo;
        try {
            util.generarPdf(pdf, xmlModificado, name, logo, plantilla, false);
            util.enviarEmail("", "", email, name + " : SERVICIO DE REPOSITORIO", xml, pdf + name + ".pdf", name + ".xml", name + ".pdf", "");
            Exe.exeSinTiempo(pdf + name + ".pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calcular() {
        BigDecimal iv  = BigDecimal.ZERO;
        BigDecimal ie  = BigDecimal.ZERO;
        BigDecimal is  = BigDecimal.ZERO;
        BigDecimal ir  = BigDecimal.ZERO;

        BigDecimal subt, dcto, tota, ivaT;

        BigDecimal aux, auxB;
        Boolean aplica, aplIeps, aplIsr, aplIvaRet;
        List<BigDecimal> importesIva = new ArrayList();
        List<BigDecimal> importesIeps = new ArrayList();
        List<BigDecimal> importesIsr = new ArrayList();
        List<BigDecimal> importesIvaRet = new ArrayList();
        subt = BigDecimal.ZERO;
        dcto = BigDecimal.ZERO;

        for (int i = 0; i < model.getRowCount(); i++) {
            aplica = (Boolean) model.getValueAt(i, 9);
            aplIeps = (Boolean) model.getValueAt(i, 10);
            aplIsr = (Boolean) model.getValueAt(i, 11);
            aplIvaRet = (Boolean) model.getValueAt(i, 12);

            dcto = dcto.add(new BigDecimal(model.getValueAt(i, 7).toString()));

            aux = new BigDecimal(model.getValueAt(i, 8).toString());
            auxB = aux.subtract(new BigDecimal(model.getValueAt(i, 7).toString()));

            if (aplica || tasaIva.get(i).compareTo(BigDecimal.ZERO) == 0) {
                importesIva.add(auxB);
            } else {
                importesIva.add(null);
            }

            if (aplIeps) {
                importesIeps.add(auxB);
            }

            if (aplIsr) {
                importesIsr.add(auxB);
            }

            if (aplIvaRet) {
                importesIvaRet.add(auxB);
            }

            subt = subt.add(aux);
        }

        for (int i = 0; i < importesIva.size(); i++) {
            iv = iv.add(importesIva.get(i) != null ? redondear(importesIva.get(i).multiply(tasaIva.get(i)), 2) : BigDecimal.ZERO);
            
            if(i == (importesIva.size() - 1)){
                iv = redondear(iv, 2);
            }
        }

        for (int i = 0; i < importesIeps.size(); i++) {
            ie = ie.add(redondear(importesIeps.get(i).multiply(tasaIeps.get(i)), 2));
            
            if(i == (importesIeps.size() - 1)){
                ie = redondear(ie, 2);
            }
        }

        for (int i = 0; i < importesIsr.size(); i++) {
            is = is.add(redondear(importesIsr.get(i).multiply(tasaIsr.get(i)), 2));
            
            if(i == (importesIsr.size() - 1)){
                is = redondear(is, 2);
            }
        }

        for (int i = 0; i < importesIvaRet.size(); i++) {
            ir = ir.add(redondear(importesIvaRet.get(i).multiply(tasaIvaRet.get(i)), 2));
            
            if(i == (importesIvaRet.size() - 1)){
                ir = redondear(ir, 2);
            }
        }

        ivaT = iv;
        tota = redondear(subt.subtract(dcto).add(ivaT).add(ie).subtract(is).subtract(ir), 2);
        descuentoTotal.setText(dcto.toString());
        ieps.setText(ie.toString());
        isrRetenido.setText(is.toString());
        ivaRetenido.setText(ir.toString());
        subtotal.setText(redondear(subt, 2).toString());
        iva.setText(ivaT.toString());
        total.setText(tota.toString());
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Factura_View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new Factura_View().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregarConcepto;
    private javax.swing.JButton agregarLeyenda;
    private javax.swing.JCheckBox aplicaIeps;
    private javax.swing.JCheckBox aplicaIsr;
    private javax.swing.JCheckBox aplicaIva;
    private javax.swing.JCheckBox aplicaIvaRet;
    private javax.swing.JButton asociarCfdi;
    private javax.swing.JButton cancelar;
    private javax.swing.JTextField cantidad;
    private javax.swing.JTextPane cfdisAsociados;
    private javax.swing.JTextField claveProdSat;
    private javax.swing.JTextField claveUnidad;
    private javax.swing.JTable conceptos;
    private javax.swing.JTextField condicionPago;
    private javax.swing.JTextArea descripcion;
    private javax.swing.JTextField descuento;
    private javax.swing.JTextField descuentoTotal;
    private javax.swing.JButton editarConcepto;
    private javax.swing.JButton emitir;
    private javax.swing.JTextField folioText;
    private javax.swing.JComboBox<String> formasPagoCombo;
    private javax.swing.JTextField ieps;
    private javax.swing.JTextField isrRetenido;
    private javax.swing.JTextField iva;
    private javax.swing.JTextField ivaRetenido;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbl_Restantes;
    private javax.swing.JComboBox listaEmisores;
    private javax.swing.JComboBox metodoCombo;
    private javax.swing.JComboBox moneda;
    private javax.swing.JTextField noIdentificacion;
    private javax.swing.JButton preFactura;
    private javax.swing.JTextField precio;
    private javax.swing.JButton quitar;
    private javax.swing.JTextField serieText;
    private javax.swing.JTextField subtotal;
    private javax.swing.JTextField tipoCambio;
    private javax.swing.JComboBox<String> tipoRelacion;
    private javax.swing.JComboBox tipocfd;
    private javax.swing.JTextField total;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField unidad;
    private javax.swing.JComboBox<String> usocfdi;
    private javax.swing.JButton verCte;
    // End of variables declaration//GEN-END:variables

    private double redondear(double d) {
        return Math.rint(d * 100) / 100;
    }

    private BigDecimal redondear(BigDecimal number, int digits) {
        return number.setScale(digits, RoundingMode.HALF_UP);
    }

    public void agregarFactura(String serie, String folio, String rfcEmi, String rfc, String nombre, String fecha, BigDecimal total, String datos, String layout, String xml, Boolean timbre, String fechaTimbrado, String uuid, Long transId, String tipoCfd) {
        Connection con;
        PreparedStatement stmt;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try {
            con = Elemento.odbc();
            //Fecha Expedicion
            Date date = new Date(format.parse(fecha).getTime());
            java.sql.Timestamp dateSql = new java.sql.Timestamp(date.getTime());

            //Fecha Timbrado
            java.sql.Timestamp dateSqlTim = new java.sql.Timestamp(format.parse(fechaTimbrado).getTime());
            String status;
            if (timbre) {
                status = "VIGENTE";
            } else if (layout.contains("PREFACTURA")) {
                status = "PREFACTURA";
                layout = layout.replace("PREFACTURA", "").trim();
            } else {
                status = "NO TIMBRADA";
            }

            if (!modificar) {
                stmt = con.prepareStatement("INSERT INTO Facturas "
                        + "(serie, folio, rfcEmisor, rfc, nombre, fecha, total, layout, xml, timbrado, fecha_timbrado, uuid, transId, status, idComprobante) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                stmt.setString(1, serie.trim());
                stmt.setString(2, folio.trim());
                stmt.setString(3, rfcEmi.trim());
                stmt.setString(4, rfc.trim());
                stmt.setString(5, nombre.trim());
                stmt.setTimestamp(6, dateSql);
                stmt.setBigDecimal(7, total);
                stmt.setString(8, layout.trim());
                stmt.setString(9, xml.trim());
                stmt.setBoolean(10, timbre);
                stmt.setTimestamp(11, dateSqlTim);
                stmt.setString(12, uuid.trim());
                stmt.setLong(13, transId);
                stmt.setString(14, status);
                stmt.setInt(15, getIdComprobante(tipoCfd));
            } else {
                stmt = con.prepareStatement("UPDATE Facturas SET "
                        + "rfc=?, nombre=?, fecha=?, total=?, layout=? , xml=? , timbrado=?, status=?, fecha_timbrado=?, uuid=?, transId=? "
                        + "WHERE folio = ? AND serie = ? AND rfcEmisor = ?");
                stmt.setString(1, rfc.trim());
                stmt.setString(2, nombre.trim());
                stmt.setTimestamp(3, dateSql);
                stmt.setBigDecimal(4, total);
                stmt.setString(5, layout.trim());
                stmt.setString(6, xml.trim());
                stmt.setBoolean(7, timbre);
                stmt.setString(8, status);
                stmt.setTimestamp(9, dateSqlTim);
                stmt.setString(10, uuid.trim());
                stmt.setLong(11, transId);
                stmt.setString(12, folio.trim());
                stmt.setString(13, serie.trim());
                stmt.setString(14, rfcEmi.trim());
            }
            stmt.execute();
            stmt.close();
            con.close();
        } catch (ParseException | SQLException e) {
            Elemento.log.error("Se presento un error al agregar una factura ala base de datos: " + e.getLocalizedMessage(), e);
            e.printStackTrace();
        }
    }

    public static String leerXML(String ruta) {
        String texto = null;
        StringBuilder sb = new StringBuilder();
        File file = new File(ruta);
        BufferedReader entrada;

        try {
            String ren = "";
            entrada = new BufferedReader((new InputStreamReader(new FileInputStream(file), "utf-8")));
            while (entrada.ready()) {
                ren = entrada.readLine();
                boolean bo = ren.matches("");
                if (ren.contains("ñ") || ren.contains("Ñ") || ren.contains("Ã‘")) {
                    if (ren.contains("Ã‘")) {
                        ren = ren.replace("Ã‘", "Ñ");
                    }
                    ren = ren.toUpperCase();
                    ren = ren.replace('Ñ', '\u00d1');
                }
                if (!bo) {
                    sb.append(ren + "\r\n");
                }
            }
            texto = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Elemento.log.error("Se presento una excepcion al leer el XML: " + e.getMessage(), e);
        }
        return texto;
    }

    public void vistaModificar(String nombre, String rfc, String folio, String[][] conceptos) {

        for (int i = 1; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).contains(nombre)) {
                resetCliente(new Integer(listaClientes.get(i).split(",")[0]));
                break;
            }
        }

        this.rfc = rfc;
        this.folio = folio;
        String[] cols = {"Cantidad", "Unidad", "Descripcion", "Precio", "Importe", "Aplica IVA"};
        model.setDataVector(conceptos, cols);
        modificar = true;
        calcular();
    }

    private Emisor setearEmisor(ResultSet rs) {
        try {
            Emisor emi = new Emisor();
            Connection con;
            Statement stmt;
            ResultSet res;
            int idEmisor = rs.getInt("id");
            emi.setIdEmisor(idEmisor);
            emi.setNombre(rs.getString("nombre").trim());
            emi.setRfc(rs.getString("rfc").trim());
            /*emi.setCalle(rs.getString("calle").trim());
            emi.setNoExterior(rs.getString("noExterior").trim());
            emi.setNoInterior(rs.getString("noInterior").trim());
            emi.setColonia(rs.getString("colonia").trim());
            emi.setLocalidad(rs.getString("localidad").trim());
            emi.setMunicipio(rs.getString("municipio").trim());
            emi.setEstado(rs.getString("estado").trim());
            emi.setPais(rs.getString("pais").trim());
            emi.setCp(rs.getString("cp").trim());*/
            emi.setEmitirNominas(rs.getBoolean("emiteNominas"));
            emi.setRegistroPatronal(rs.getString("registroPatronal"));
            emi.setCurp(rs.getString("curp"));

            emi.setExpedidoEn(obtenerExpedidoEn(idEmisor));

            con = Elemento.odbc();
            stmt = factory.stmtLectura(con);
            res = stmt.executeQuery("SELECT * FROM Cuentas WHERE rfc = \'" + emi.getRfc() + "\'");
            if (res.next()) {
                emi.setRegimenFiscal(res.getString("regimenFiscal").trim());
            }
            stmt.close();
            con.close();
            return emi;
        } catch (Exception ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al setear el Emisor: " + ex.getMessage(), ex);
            Elemento.log.error("Causa: " + ex.getCause().getMessage(), ex);
            return null;
        }
    }

    private void consultarProducto(String campo, String valor) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        try {
            String query = "SELECT p.noIdentificacion, cu.claveunidad, cu.nombre as unidad, "
                    + "p.descripcion, "
                    + "p.precio, p.aplicaIva, cc.claveprodserv "
                    + "FROM (Productos p "
                    + "INNER JOIN c_claveprodserv cc ON p.c_claveprodserv_id = cc.c_claveprodserv_id) "
                    + "INNER JOIN c_claveunidad cu ON p.c_claveunidad_id = cu.c_claveunidad_id";

            if (campo.equalsIgnoreCase("descripcion")) {
                query += " WHERE " + campo + " like \'%" + valor + "%\'";
            } else {
                query += " WHERE " + campo + " = \'" + valor + "\'";
            }
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                claveUnidad.setText(rs.getString("claveunidad").trim());
                unidad.setText(rs.getString("unidad").trim());
                noIdentificacion.setText(rs.getString("noIdentificacion"));
                descripcion.setText(rs.getString("descripcion"));
                precio.setText("" + rs.getDouble("precio"));
                aplicaIva.setSelected(rs.getBoolean("aplicaIva"));
                claveProdSat.setText(rs.getString("claveprodserv"));
                claveProdSat.transferFocus();
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al consultar un producto: " + ex.getMessage(), ex);
        }
    }

    private ExpedidoEn obtenerExpedidoEn(int idEmisor) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        ExpedidoEn xx = null;

        try {
            rs = stmt.executeQuery("SELECT * FROM Sucursales WHERE idEmisor = " + idEmisor);
            if (rs.next()) {
                xx = new ExpedidoEn();
                xx.setCalle(rs.getString("calle"));
                xx.setCodigoPostal(rs.getString("cp"));
                xx.setColonia(rs.getString("colonia"));
                xx.setEstado(rs.getString("estado"));
                xx.setLocalidad(rs.getString("localidad"));
                xx.setMunicipio(rs.getString("municipio"));
                xx.setNoExterior(rs.getString("noExterior"));
                xx.setNoInterior(rs.getString("noInterior"));
                xx.setPais(rs.getString("pais"));
            }
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xx;
    }

    private Donataria obtenerDonataria(String idEmisor) {
        Donataria donat = null;
        try {
            Connection con = Elemento.odbc();
            Statement stmt = factory.stmtLectura(con);
            ResultSet rs = stmt.executeQuery("SELECT * FROM Donatarias WHERE idEmisor = " + idEmisor);
            if (rs.next()) {
                donat = new Donataria();
                donat.setFechaAutorizacion(rs.getDate("fechaAuto"));
                donat.setNoAutorizacion(rs.getString("noAuto"));
                donat.setIdEmisor(new Integer(idEmisor));
            }
            rs.close();
            stmt.close();
            con.close();
            return donat;
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al obtener los datos de donatarias ", e);
            return null;
        }
    }
}