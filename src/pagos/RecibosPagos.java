/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagos;

import elemento.Elemento;
import elemento.Emisor;
import elemento.Factura;
import elemento.Layout;
import elemento.TextPrompt;
import gui.Folios;
import java.awt.Font;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import reportes.Receptor;

/**
 *
 * @author Abe
 */
public class RecibosPagos extends javax.swing.JFrame {

    /**
     * Creates new form RecibosPagos
     */
    private TextPrompt holder;
    private Emisor emi;
    private Receptor rec;
    private List<Boolean> bancarizado;
    private Pagos pagos;
    private List<Pago> listaPagos;
    private Pago ultimoPago;
    private final int MSG_ERROR = JOptionPane.ERROR_MESSAGE;
    private final int MSG_WARN = JOptionPane.WARNING_MESSAGE;
    private final int MSG_INFO = JOptionPane.INFORMATION_MESSAGE;
    private List<Documento> docs;
    private List<String> arrayTipoRel;
    private List<String> cfdisAsoc;
    private String uuids;
    private List<Banco> bancosBene, bancosOrde;
    private String[] bene, orde;
    private utils.Utils util;
    
    public BigDecimal pagoRetencionesIVA = null;
    public BigDecimal pagoRetensionesISR = null;
    public BigDecimal pagoRetensionesIEPS = null; 
    public BigDecimal pagoTrasladosBaseIVA16 = null; 
    public BigDecimal pagoTrasladosImpuestoIVA16 = null; 
    public BigDecimal pagoTrasladosBaseIVA8 = null;
    public BigDecimal pagoTrasladosImpuestoIVA8 = null; 
    public BigDecimal pagoTrasladosBaseIVA0 = null;
    public BigDecimal pagoTrasladosImpuestoIVA0 = null; 
    public BigDecimal pagoTrasladosBaseIVAExento = null;  
    
    public BigDecimal totalMontoPagos = BigDecimal.ZERO;
    public BigDecimal totalRetencionesIVA = null;
    public BigDecimal totalRetensionesISR = null;
    public BigDecimal totalRetensionesIEPS = null; 
    public BigDecimal totalTrasladosBaseIVA16 = null; 
    public BigDecimal totalTrasladosImpuestoIVA16 = null; 
    public BigDecimal totalTrasladosBaseIVA8 = null;
    public BigDecimal totalTrasladosImpuestoIVA8 = null; 
    public BigDecimal totalTrasladosBaseIVA0 = null;
    public BigDecimal totalTrasladosImpuestoIVA0 = null; 
    public BigDecimal totalTrasladosBaseIVAExento = null; 

    public List<String> getArrayTipoRel() {
        return arrayTipoRel;
    }

    public void setArrayTipoRel(List<String> arrayTipoRel) {
        this.arrayTipoRel = arrayTipoRel;
        llenarTipoRelacion();
    }
    
    public RecibosPagos(){
        initComponents();
        this.util = new utils.Utils(Elemento.log);
        this.getBancos();
        this.setLocationRelativeTo(null);
        pagos = new Pagos();
        listaPagos = new ArrayList();
        setHolders();
        setCellListener();
    }
    
    public RecibosPagos(Emisor emi, Receptor rec) {
        this.emi = emi;
        this.rec = rec;
        initComponents();
        this.util = new utils.Utils(Elemento.log);
        this.getBancos();
        this.setLocationRelativeTo(null);
        pagos = new Pagos();
        listaPagos = new ArrayList();
        setHolders();
        setCellListener();
    }
    
    private void getBancos(){
        bancosBene = new ArrayList();
        bancosOrde = new ArrayList();
        List<String> nombresBancosBene = new ArrayList();
        List<String> nombresBancosOrde = new ArrayList();
        Connection con = Elemento.odbc();
        Statement stmt = null;
        ResultSet rsBene = null;
        ResultSet rsOrde = null;
        
        try {
            stmt = con.createStatement();
            if(emi != null)
                rsBene = stmt.executeQuery("SELECT b.id_banco, b.nombre, b.rfc FROM c_bancos b INNER JOIN EmisoresBancos eb ON b.id_banco = eb.id_banco AND eb.id_emisor = " + emi.getIdEmisor());
            else
                rsBene = stmt.executeQuery("SELECT id_banco, nombre, rfc FROM c_bancos b");
            
            rsOrde = stmt.executeQuery("SELECT id_banco, nombre, rfc FROM c_bancos b");
            nombresBancosBene.add("Seleccione Banco Beneficiario");
            nombresBancosOrde.add("Seleccione Banco Ordenante");
            bancosBene.add(null);
            bancosOrde.add(null);
            
            if(!rsBene.next()){
                rsBene.close();
                rsBene = stmt.executeQuery("SELECT id_banco, nombre, rfc FROM c_bancos b");
                rsBene.next();
            }
            
            do{
                Banco b = new Banco(rsBene.getString("nombre"), rsBene.getString("rfc"), rsBene.getInt("id_banco"));
                bancosBene.add(b);
                
                nombresBancosBene.add(b.getId() + " - " + b.getBanco());
            } while(rsBene.next());
            
            while(rsOrde.next()){
                Banco b = new Banco(rsOrde.getString("nombre"), rsOrde.getString("rfc"), rsOrde.getInt("id_banco"));
                bancosOrde.add(b);
                
                nombresBancosOrde.add(b.getId() + " - " + b.getBanco());
            }
            
            bancoBeneficiario.setModel(new DefaultComboBoxModel(nombresBancosBene.toArray(new String[nombresBancosBene.size()])));
            bancoOrdenante.setModel(new DefaultComboBoxModel(nombresBancosOrde.toArray(new String[nombresBancosOrde.size()])));
            
            rsBene.close();
            rsOrde.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al obtener los bancos", e);
            
            try {
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Elemento.log.error("Excepcion al cerrar la conexion a la base de datos", ex);
            }
        }
    }
    
    private void setHolders(){
        holder = new TextPrompt("Tipo de Cambio", tipoCambio);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
        
        holder = new TextPrompt("Cuenta Beneficiario", cuentaBeneficiario);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
        
//        holder = new TextPrompt("RFC Cta Beneficiario", rfcCtaBen);
//        holder.changeAlpha(0.75f);
//        holder.changeStyle(Font.ITALIC);
        
        holder = new TextPrompt("Cuenta Ordenante", cuentaOrdenante);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
        
//        holder = new TextPrompt("RFC Cta Ordenante", rfcCtaOrd);
//        holder.changeAlpha(0.75f);
//        holder.changeStyle(Font.ITALIC);
        
        holder = new TextPrompt("Monto", montoPago);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
        
        holder = new TextPrompt("Numero de Operación", txtNumOperacion);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
    }
    
    private void setCellListener(){
        tablaDocs.getModel().addTableModelListener(new TableModelListener(){
            @Override
            public void tableChanged(TableModelEvent e){
                if(e.getColumn() >= 0){
                    DefaultTableModel docus = (DefaultTableModel)tablaDocs.getModel();

                    int pos = tablaPagos.getSelectedRow();
                    int posD = e.getFirstRow();
                    if(pos >= 0){
                        Pago p = listaPagos.get(pos);
                        Documento d = p.getDocumentos().get(posD);
                        
                        BigDecimal saldoAnt = (BigDecimal)docus.getValueAt(posD, 2);
                        BigDecimal importe = (BigDecimal)docus.getValueAt(posD, 3);
                        BigDecimal saldoInso = (BigDecimal)docus.getValueAt(posD, 4);
                        BigDecimal newSaldoInso = saldoAnt.subtract(importe);
                        
                        if(saldoInso.compareTo(newSaldoInso) != 0){
                            docus.setValueAt(newSaldoInso, posD, 4);
                        }else{
                            d.setImpSaldoAnterior(saldoAnt);
                            d.setImpPagado(importe);
                            d.setImpSaldoInsoluto(saldoInso);
                            d.setNumParcialidad((Integer)docus.getValueAt(posD, 5));
                            
                            listaPagos.set(pos, p);
                            validarDatosDocumento((DefaultTableModel)tablaDocs.getModel());
                        }
                    }
                }
            }
        });
    }
    
    public void setEmisor(Emisor emi){
        this.emi = emi;
        this.labelEmisor.setText(emi.getNombre());
        this.getBancos();
    }
    
    public void setReceptor(Receptor rec){
        this.rec = rec;
        labelReceptor.setText(rec.getNombre());
    }
    
    public void setFolio(String folio){
        folioText.setText(folio);
    }
    
    public void setFormasPago(ComboBoxModel formasPago){
        comboFormaPago.setModel(formasPago);
    }
    
    public void setBancarizado(List<Boolean> bancarizado){
        this.bancarizado = bancarizado;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        comboFormaPago = new javax.swing.JComboBox<>();
        btnEmitir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaPagos = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaDocs = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        tipoCambio = new javax.swing.JTextField();
        comboMoneda = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        labelEmisor = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        labelReceptor = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        folioText = new javax.swing.JTextField();
        montoPago = new javax.swing.JTextField();
        fechaPago = new com.toedter.calendar.JDateChooser();
        cuentaOrdenante = new javax.swing.JTextField();
        cuentaBeneficiario = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        cfdisAsociados = new javax.swing.JTextPane();
        tipoRelacion = new javax.swing.JComboBox<>();
        asociarCfdi = new javax.swing.JButton();
        bancoBeneficiario = new javax.swing.JComboBox<>();
        bancoOrdenante = new javax.swing.JComboBox<>();
        chkOrdenante = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        lblTotalSaldoAnterior = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblTotalPagado = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblTotalSaldoInsoluto = new javax.swing.JLabel();
        chkInfoSpei = new javax.swing.JCheckBox();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtCertPago = new javax.swing.JTextArea();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtCadPago = new javax.swing.JTextArea();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtSelloPago = new javax.swing.JTextArea();
        txtNumOperacion = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Recibos de Pago");
        setResizable(false);

        jLabel1.setText("Pagos");

        comboFormaPago.setName(""); // NOI18N
        comboFormaPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboFormaPagoActionPerformed(evt);
            }
        });

        btnEmitir.setText("Emitir");
        btnEmitir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmitirActionPerformed(evt);
            }
        });

        tablaPagos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NumPago","Forma de Pago","Cta Beneficiario", "Cta Ordenante", "Monto", "Moneda", "Tipo Cambio", "Fecha Pago"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, BigDecimal.class, java.lang.String.class, BigDecimal.class, java.lang.String.class, Date.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tablaPagos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaPagosMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tablaPagosMouseEntered(evt);
            }
        });
        jScrollPane1.setViewportView(tablaPagos);

        tablaDocs.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Folio", "Serie", "Imp. Saldo Anterior", "Imp. Pagado", "Imp. Saldo Insoluto", "Num. Parcialidad", "Metodo de Pago", "Moneda", "Tipo Cambio", "UUID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, BigDecimal.class, BigDecimal.class, BigDecimal.class, Integer.class, java.lang.String.class, java.lang.String.class, BigDecimal.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tablaDocs);

        jLabel3.setText("Documentos Relacionados");

        tipoCambio.setEditable(false);
        tipoCambio.setToolTipText("");
        tipoCambio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoCambioActionPerformed(evt);
            }
        });
        tipoCambio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tipoCambioKeyTyped(evt);
            }
        });

        comboMoneda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Moneda", "MXN", "USD", "EUR" }));
        comboMoneda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboMonedaActionPerformed(evt);
            }
        });

        jLabel4.setText("Emisor:");

        jLabel5.setText("Receptor:");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Folio");

        folioText.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        montoPago.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                montoPagoKeyTyped(evt);
            }
        });

        fechaPago.setDate(new Date());
        fechaPago.setDateFormatString("dd/MM/yyyy");

        cuentaOrdenante.setEditable(false);

        cuentaBeneficiario.setEditable(false);

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/add.png"))); // NOI18N
        btnAdd.setToolTipText("Agregar");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/edit-29.png"))); // NOI18N
        btnEdit.setToolTipText("Editar");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/DeleteRed.png"))); // NOI18N
        btnDel.setToolTipText("Eliminar");
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelActionPerformed(evt);
            }
        });

        jLabel6.setText("CFDIs Relacionados:");

        cfdisAsociados.setEditable(false);
        cfdisAsociados.setEnabled(false);
        jScrollPane3.setViewportView(cfdisAsociados);

        tipoRelacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecciona el tipo de relación" }));
        tipoRelacion.setEnabled(false);

        asociarCfdi.setText("Asociar CFDIs");
        asociarCfdi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                asociarCfdiActionPerformed(evt);
            }
        });

        bancoBeneficiario.setEnabled(false);
        bancoBeneficiario.setName("bancoBeneficiario"); // NOI18N

        bancoOrdenante.setEnabled(false);
        bancoOrdenante.setName("bancoOrdenante"); // NOI18N

        chkOrdenante.setSelected(true);
        chkOrdenante.setText("Enviar informacion de cuenta ordenante");
        chkOrdenante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkOrdenanteActionPerformed(evt);
            }
        });

        jLabel8.setText("Asociacion de Recibos Cancelados");

        jLabel9.setText("Total Saldo Anterior: ");

        jLabel10.setText("Total Pagado: ");

        jLabel11.setText("Total Saldo Insoluto: ");

        chkInfoSpei.setText("Enviar informacion SPEI");
        chkInfoSpei.setEnabled(false);
        chkInfoSpei.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkInfoSpeiActionPerformed(evt);
            }
        });

        txtCertPago.setColumns(20);
        txtCertPago.setRows(5);
        txtCertPago.setWrapStyleWord(true);
        txtCertPago.setEnabled(false);
        jScrollPane4.setViewportView(txtCertPago);

        txtCadPago.setColumns(20);
        txtCadPago.setRows(5);
        txtCadPago.setWrapStyleWord(true);
        txtCadPago.setEnabled(false);
        jScrollPane5.setViewportView(txtCadPago);

        txtSelloPago.setColumns(20);
        txtSelloPago.setRows(5);
        txtSelloPago.setWrapStyleWord(true);
        txtSelloPago.setEnabled(false);
        jScrollPane6.setViewportView(txtSelloPago);

        jLabel12.setText("Certificado de Pago");

        jLabel13.setText("Cadena de Pago");

        jLabel14.setText("Sello de Pago");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnEmitir, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(tipoRelacion, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(asociarCfdi)
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane3)))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(comboFormaPago, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(montoPago, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(comboMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(tipoCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(fechaPago, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 908, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jSeparator1))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel4)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(labelEmisor, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(cuentaBeneficiario, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(bancoBeneficiario, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(36, 36, 36)
                                            .addComponent(jLabel5)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(labelReceptor, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(folioText, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(10, 10, 10)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGap(174, 174, 174)
                                                    .addComponent(txtNumOperacion, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(chkOrdenante))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(cuentaOrdenante, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(bancoOrdenante, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(btnDel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel14)
                                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jSeparator2))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1090, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lblTotalSaldoAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel10)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lblTotalPagado, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel11)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(lblTotalSaldoInsoluto, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1090, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(chkInfoSpei)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12))
                                .addGap(44, 44, 44)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelEmisor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(labelReceptor, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(folioText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 2, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboFormaPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(montoPago, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tipoCambio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fechaPago, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(chkOrdenante)
                            .addComponent(txtNumOperacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cuentaBeneficiario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bancoBeneficiario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cuentaOrdenante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bancoOrdenante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnEdit)
                        .addComponent(btnDel, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(btnAdd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkInfoSpei)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(lblTotalSaldoAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(lblTotalPagado, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(lblTotalSaldoInsoluto, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tipoRelacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(asociarCfdi))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addComponent(btnEmitir)
                .addContainerGap())
        );

        tipoCambio.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tipoCambioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoCambioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tipoCambioActionPerformed

    private void comboFormaPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboFormaPagoActionPerformed
        if(comboFormaPago.getSelectedIndex() > 0){
            if(bancarizado.get(comboFormaPago.getSelectedIndex()-1)){
                cuentaBeneficiario.setEditable(true);
                bancoBeneficiario.setEnabled(true);
                cuentaOrdenante.setEditable(true);
                bancoOrdenante.setEnabled(true);
                chkOrdenante.setEnabled(true);
                chkOrdenante.setSelected(true);
            }else{
                cuentaBeneficiario.setText("");
                bancoBeneficiario.setSelectedIndex(0);
                cuentaOrdenante.setText("");
                bancoOrdenante.setSelectedIndex(0);
                cuentaBeneficiario.setEditable(false);
                bancoBeneficiario.setEnabled(false);
                cuentaOrdenante.setEditable(false);
                bancoOrdenante.setEnabled(false);
                chkOrdenante.setEnabled(false);
                chkOrdenante.setSelected(false);
            }
            
            if(comboFormaPago.getSelectedItem().toString().split(",")[0].trim().equals("03")){
                chkInfoSpei.setEnabled(true);
                chkInfoSpei.setSelected(false);
            }else{
                chkInfoSpei.setEnabled(false);
                chkInfoSpei.setSelected(false);
            }
        }else{
            cuentaBeneficiario.setText("");
            bancoBeneficiario.setSelectedIndex(0);
            cuentaOrdenante.setText("");
            bancoOrdenante.setSelectedIndex(0);
            cuentaBeneficiario.setEditable(false);
            bancoBeneficiario.setEnabled(false);
            cuentaOrdenante.setEditable(false);
            bancoOrdenante.setEnabled(false);
            chkOrdenante.setEnabled(false);
            chkOrdenante.setSelected(false);
        }
    }//GEN-LAST:event_comboFormaPagoActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
        if(validarPago()){
            DefaultTableModel model = (DefaultTableModel)tablaPagos.getModel();
            DefaultTableModel model2 = (DefaultTableModel)tablaDocs.getModel();
            model2.setRowCount(0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            Pago p = new Pago();
            p.setFormaPago(comboFormaPago.getSelectedItem().toString().split(",")[0]);
            p.setCuentaBeneficiario(cuentaBeneficiario.getText().trim());
            p.setRfcCtaBen(getRfcBanco(bancoBeneficiario));
            p.setCuentaOrdenante(cuentaOrdenante.getText().trim());
            p.setRfcCtaOrd(getRfcBanco(bancoOrdenante));
            p.setMonto(new BigDecimal(montoPago.getText()));
            p.setMoneda(comboMoneda.getSelectedItem().toString());
            p.setTipoCambio(tipoCambio.getText().trim().isEmpty() ? BigDecimal.ONE : new BigDecimal(tipoCambio.getText()));
            p.setFechaPago(fechaPago.getDate());
            p.setNumOperacion(txtNumOperacion.getText());
            
            if(p.getRfcCtaOrd().equals("XEXX010101000")){
                String nomBanco = JOptionPane.showInputDialog(null, "Ingrese el nombre del banco extranjero", "Nombre Banco Extranjero", JOptionPane.INFORMATION_MESSAGE);
                
                while(nomBanco == null || nomBanco.trim().isEmpty()){
                    nomBanco = JOptionPane.showInputDialog(null, "Es necesario ingresar el nombre del banco extranjero", "Nombre Banco Extranjero", JOptionPane.WARNING_MESSAGE);
                }
                
                p.setNomBancoOrdExt(nomBanco);
            }
            
            if(p.getFormaPago().equals("03") && chkInfoSpei.isSelected()){
                p.setTipoCadPago("01");
                p.setCertPago(txtCertPago.getText().trim());
                p.setCadPago(txtCadPago.getText().trim());
                p.setSelloPago(txtSelloPago.getText().trim());
            }
            
            int numPago = model.getRowCount() + 1;
            
            Object[] row = new Object[8];
            row[0] = (numPago < 10 ? "0" : "") + numPago;
            row[1] = p.getFormaPago();
            row[2] = p.getCuentaBeneficiario();
            row[3] = p.getCuentaOrdenante();
            row[4] = p.getMonto();
            row[5] = p.getMoneda();
            row[6] = p.getTipoCambio();
            row[7] = sdf.format(p.getFechaPago());
            
            model.addRow(row);
            
            totalMontoPagos = totalMontoPagos.add(p.getMonto());
            
            limpiarCampos(false);
            tablaPagos.setRowSelectionInterval(model.getRowCount()-1, model.getRowCount()-1);
            buscarDocumentos(p);
        }
    }//GEN-LAST:event_btnAddActionPerformed
    
    private String getRfcBanco(javax.swing.JComboBox banco){
        if(banco.getName().equalsIgnoreCase("bancoBeneficiario"))
            return this.bancosBene.get(banco.getSelectedIndex()) != null ? this.bancosBene.get(banco.getSelectedIndex()).getRfc() : "";
        
        if(banco.getName().equalsIgnoreCase("bancoOrdenante"))
            return this.bancosOrde.get(banco.getSelectedIndex()) != null ? this.bancosOrde.get(banco.getSelectedIndex()).getRfc() : "";
        
        return null;
    }
    
    private int getIndexBanco(String rfc, char tipo){
        if(tipo == 'B')
            for (int i = 1; i < bancosBene.size(); i++) {
                String r = bancosBene.get(i).getRfc();
            if(rfc.equals(r)){
                return i;
            }
        }
        else
            for (int i = 1; i < bancosOrde.size(); i++) {
                String r = bancosOrde.get(i).getRfc();
                if(rfc.equals(r)){
                    return i;
                }
            }
        
        return 0;
    }
    
    private void montoPagoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_montoPagoKeyTyped
        // TODO add your handling code here:
        if(!Character.isDigit(evt.getKeyChar()) && !(evt.getKeyChar() == '.' && montoPago.getText().length() > 0 && !montoPago.getText().contains("."))){
            evt.consume();
        }
        
        if(montoPago.getText().contains(".")){
            if((montoPago.getText()+evt.getKeyChar()).split("\\.")[1].length() > 6){
                evt.consume();
            }
        }
    }//GEN-LAST:event_montoPagoKeyTyped
    
    private void tablaPagosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPagosMouseClicked
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)tablaPagos.getModel();
        DefaultTableModel model2 = (DefaultTableModel)tablaDocs.getModel();
        int pos = tablaPagos.getSelectedRow();
        model2.setRowCount(0);
        Object[] row;
        if(pos >= 0){
            Pago p = listaPagos.get(pos);
            ultimoPago = p;
            for(Documento d : p.getDocumentos()){
                row = new Object[10];
                row[0] = d.getFolio();
                row[1] = d.getSerie();
                row[2] = d.getImpSaldoAnterior();
                row[3] = d.getImpPagado();
                row[4] = d.getImpSaldoInsoluto();
                row[5] = d.getNumParcialidad();
                row[6] = d.getMetodoPago();
                row[7] = d.getMoneda();
                row[8] = d.getTipoCambio();
                row[9] = d.getUuid();
                
                model2.addRow(row);
            }
            
            validarDatosDocumento(model2);
        }
    }//GEN-LAST:event_tablaPagosMouseClicked

    private void comboMonedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboMonedaActionPerformed
        // TODO add your handling code here:
        tipoCambio.setText("");
        if(comboMoneda.getSelectedIndex() > 1){
            tipoCambio.setEditable(true);
            tipoCambio.requestFocus();
        }else{
            tipoCambio.setEditable(false);
        }
    }//GEN-LAST:event_comboMonedaActionPerformed

    private void tipoCambioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tipoCambioKeyTyped
        // TODO add your handling code here:
        if(!Character.isDigit(evt.getKeyChar()) && !(evt.getKeyChar() == '.' && tipoCambio.getText().length() > 0 && !tipoCambio.getText().contains("."))){
            evt.consume();
        }
        
        if(tipoCambio.getText().contains(".")){
            if(tipoCambio.getText().split(".")[1].length() > 6){
                evt.consume();
            }
        }
    }//GEN-LAST:event_tipoCambioKeyTyped

    private void btnEmitirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmitirActionPerformed
        // TODO add your handling code here:        
        this.validarDatosDocumento((DefaultTableModel)tablaDocs.getModel());
        Elemento.leerConfig(emi.getRfc());
        Factura fact = new Factura();
        fact.emisor = emi;
        fact.nombre = rec.getNombre();
        fact.rfc = rec.getRfc();
        fact.regimenFiscalReceptor = rec.getRegimenFiscal();
        fact.cp = rec.getCp();
        fact.pais = rec.getPais();
        fact.folio = folioText.getText();
        fact.serie = "P";
        fact.prefactura = "";
        fact.usoCfdi = "CP01";
        fact.moneda = "XXX";
        fact.tipoCambio = "";
        fact.lugarExpedicion = Elemento.lugarExpedicion;
        fact.leyenda = "";
        fact.cfdisAsociados = uuids;
        fact.tipoRelacion = uuids != null ? tipoRelacion.getSelectedItem().toString().split(",")[0].trim() : null;
        
        pagos.setPagos(listaPagos);
        pagos.setTotalMontoPagos(totalMontoPagos);
        pagos.setTotalRetencionesIVA(totalRetencionesIVA);
        pagos.setTotalRetensionesIEPS(totalRetensionesIEPS);
        pagos.setTotalRetensionesISR(totalRetensionesISR);
        pagos.setTotalTrasladosBaseIVA0(totalTrasladosBaseIVA0);
        pagos.setTotalTrasladosBaseIVA16(totalTrasladosBaseIVA16);
        pagos.setTotalTrasladosBaseIVA8(totalTrasladosBaseIVA8);
        pagos.setTotalTrasladosBaseIVAExento(totalTrasladosBaseIVAExento);
        pagos.setTotalTrasladosImpuestoIVA0(totalTrasladosImpuestoIVA0);
        pagos.setTotalTrasladosImpuestoIVA16(totalTrasladosImpuestoIVA16);
        pagos.setTotalTrasladosImpuestoIVA8(totalTrasladosImpuestoIVA8);
        
        Layout l = new Layout(fact, pagos);
        
        int folioNext = Integer.parseInt(folioText.getText()) + 1;
        this.folioText.setText("" + folioNext);
        
        this.limpiarTodo();
        this.dispose();
    }//GEN-LAST:event_btnEmitirActionPerformed

    private void btnDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)tablaPagos.getModel();
        DefaultTableModel model2 = (DefaultTableModel)tablaDocs.getModel();
        int pos = tablaPagos.getSelectedRow();
        if(pos >= 0){
            if(JOptionPane.showConfirmDialog(null, "Esta seguro que desea eliminar este pago?", "Eliminar Pago", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
                model2.setRowCount(0);
                model.removeRow(pos);
                
                Pago p = listaPagos.get(pos);
                totalMontoPagos = totalMontoPagos.subtract(p.getMonto());
                
                listaPagos.remove(pos);
            }
        }
    }//GEN-LAST:event_btnDelActionPerformed

    private void asociarCfdiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_asociarCfdiActionPerformed
        String rfc = rec.getRfc();
        
        if(rfc != null && !rfc.trim().isEmpty()){
            final Folios fol = new Folios(emi.getRfc(), rfc, null, this);
            fol.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(null,"Debe seleccionar a un cliente","Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_asociarCfdiActionPerformed
    
    public void setUuids(Folios fol){
        cfdisAsoc = fol.cfdisAsoc;
        uuids = "";
        for (int i = 0; i < cfdisAsoc.size(); i++) {
            String x = cfdisAsoc.get(i);
            uuids += x;
            if(i < cfdisAsoc.size() - 1){
                uuids += ",";
            }
        }
        cfdisAsociados.setText(uuids);
        tipoRelacion.setEnabled(true);
        fol.dispose();
    }
    
    private void tablaPagosMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPagosMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaPagosMouseEntered

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)tablaPagos.getModel();
        DefaultTableModel model2 = (DefaultTableModel)tablaDocs.getModel();
        int pos = tablaPagos.getSelectedRow();
        if(pos >= 0){
            Pago p = listaPagos.get(pos);
            comboFormaPago.setSelectedIndex(getIndexFormaPago(p.getFormaPago()));
            cuentaBeneficiario.setText(p.getCuentaBeneficiario());
            bancoBeneficiario.setSelectedIndex(getIndexBanco(p.getRfcCtaBen(), 'B'));
            cuentaOrdenante.setText(p.getCuentaOrdenante());
            bancoOrdenante.setSelectedIndex(getIndexBanco(p.getRfcCtaOrd(), 'O'));
            montoPago.setText(p.getMonto().toString());
            comboMoneda.setSelectedItem(p.getMoneda());
            tipoCambio.setText(p.getTipoCambio() != null ? p.getTipoCambio().toString() : "");
            fechaPago.setDate(p.getFechaPago());
            
            boolean spei = p.getTipoCadPago() != null && p.getTipoCadPago().equals("01");
            chkInfoSpei.setEnabled(spei);
            txtCertPago.setEnabled(spei);
            txtCadPago.setEnabled(spei);
            txtSelloPago.setEnabled(spei);
            
            if(spei){
                txtCertPago.setText(p.getCertPago());
                txtCadPago.setText(p.getCadPago());
                txtSelloPago.setText(p.getSelloPago());
            }
            
            totalMontoPagos = totalMontoPagos.subtract(p.getMonto());
            
            model2.setRowCount(0);
            model.removeRow(pos);
            listaPagos.remove(pos);
        }
        
        
    }//GEN-LAST:event_btnEditActionPerformed

    private void chkOrdenanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkOrdenanteActionPerformed
        this.bancoOrdenante.setEnabled(this.chkOrdenante.isSelected());
        this.cuentaOrdenante.setEditable(this.chkOrdenante.isSelected());
    }//GEN-LAST:event_chkOrdenanteActionPerformed

    private void chkInfoSpeiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkInfoSpeiActionPerformed
        this.txtCertPago.setEnabled(this.chkInfoSpei.isSelected());
        this.txtCadPago.setEnabled(this.chkInfoSpei.isSelected());
        this.txtSelloPago.setEnabled(this.chkInfoSpei.isSelected());
    }//GEN-LAST:event_chkInfoSpeiActionPerformed
    
    private int getIndexFormaPago(String fp){
        int index = 0;
        for (int i = 0; i < comboFormaPago.getItemCount(); i++) {
            if(comboFormaPago.getItemAt(i).split(",")[0].equals(fp)){
                index = i;
                break;
            }
        }
        return index;
    }
    
    private boolean validarPago() {
        if(comboFormaPago.getSelectedIndex() <= 0){
            mensaje("Debe seleccionar una forma de pago", MSG_WARN);
            return false;
        }
        
        int lengthCB = cuentaBeneficiario.getText().trim().length();
        int lengthCO = cuentaOrdenante.getText().trim().length();
        //int lengthRB = rfcCtaBen.getText().trim().length();
        //int lengthRO = rfcCtaOrd.getText().trim().length();
        if(cuentaBeneficiario.isEditable() && (lengthCB < 10 || lengthCB > 50)){
            mensaje("La cuenta del beneficiario es requerida y debe de contener entre 10 y 50 catacteres", MSG_WARN);
            return false;
        }
        
        if(bancoBeneficiario.isEnabled() && bancoBeneficiario.getSelectedIndex() == 0){
            mensaje("Debe seleccionar el banco del beneficiario", MSG_WARN);
            return false;
        }
        
        if(cuentaOrdenante.isEditable() && (lengthCO < 10 || lengthCO > 50)){
            mensaje("La cuenta del Ordenante es requeridaa y debe de contener entre 10 y 50 catacteres", MSG_WARN);
            return false;
        }
        
        if(bancoOrdenante.isEnabled() && bancoOrdenante.getSelectedIndex() == 0){
            mensaje("Debe seleccionar el banco ordenante", MSG_WARN);
            return false;
        }
        
        if(montoPago.getText().trim().isEmpty()){
            mensaje("El campo \"Monto\" es obligatorio", MSG_WARN);
            return false;
        }
        
        if(comboMoneda.getSelectedIndex() <= 0){
            mensaje("Debe seleccionar una moneda", MSG_WARN);
            return false;
        }
        
        if(tipoCambio.isEditable() && tipoCambio.getText().isEmpty()){
            mensaje("El campo \"Tipo de Cambio\" es obligatorio", MSG_WARN);
            return false;
        }
        
        if(fechaPago.getDate() == null){
            mensaje("El campo \"Fecha de Pago\" es obligatorio", MSG_WARN);
            return false;
        }
        
        if(txtNumOperacion.getText().trim().isEmpty()){
            mensaje("El campo \"Numero de Operacion\" es obligatorio", MSG_WARN);
            return false;
        }
        
        if(chkInfoSpei.isSelected()){
            if(txtCertPago.getText().trim().isEmpty()){
                mensaje("Si la opcion \"Envio informacion SPEI\" esta activa,\n entonces el campo \"Certificado de Pago\" es obligatorio", MSG_WARN);
                return false;
            }
            
            if(txtCadPago.getText().trim().isEmpty()){
                mensaje("Si la opcion \"Envio informacion SPEI\" esta activa,\n entonces el campo \"Cadena de Pago\" es obligatorio", MSG_WARN);
                return false;
            }
            
            if(txtSelloPago.getText().trim().isEmpty()){
                mensaje("Si la opcion \"Envio informacion SPEI\" esta activa,\n entonces el campo \"Sello de Pago\" es obligatorio", MSG_WARN);
                return false;
            }
        }
        
        return true;
    }
    
    private void validarDatosDocumento(DefaultTableModel model){
        Pago p = ultimoPago;
        BigDecimal totalSaldoAnterior = BigDecimal.ZERO;
        BigDecimal totalPagado = BigDecimal.ZERO;
        BigDecimal totalSaldoInsoluto = BigDecimal.ZERO;
        
        for (int i = 0; i < p.getDocumentos().size(); i++) {
            Documento d = p.getDocumentos().get(i);
            d.setImpSaldoInsoluto(d.getImpSaldoAnterior() == (BigDecimal)model.getValueAt(i, 4) && !((BigDecimal)model.getValueAt(i, 4)).equals(BigDecimal.ZERO) ? d.getImpSaldoAnterior() : (BigDecimal)model.getValueAt(i, 4));
            d.setImpPagado(d.getImpPagado() == (BigDecimal)model.getValueAt(i, 3) ? d.getImpPagado() : (BigDecimal)model.getValueAt(i, 3));
            d.setImpSaldoAnterior(d.getImpSaldoAnterior() == (BigDecimal)model.getValueAt(i, 2) ? d.getImpSaldoAnterior() : (BigDecimal)model.getValueAt(i, 2));
            d.setNumParcialidad(d.getNumParcialidad() == (Integer)model.getValueAt(i,5) ? d.getNumParcialidad() : (Integer)model.getValueAt(i, 5));
            
            totalSaldoAnterior = totalSaldoAnterior.add(d.getImpSaldoAnterior());
            totalPagado = totalPagado.add(d.getImpPagado());
            totalSaldoInsoluto = totalSaldoInsoluto.add(d.getImpSaldoInsoluto());
        }
        
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        lblTotalSaldoAnterior.setText(formatter.format(totalSaldoAnterior));
        lblTotalPagado.setText(formatter.format(totalPagado));
        lblTotalSaldoInsoluto.setText(formatter.format(totalSaldoInsoluto));
    }
    
    private void limpiarCampos(boolean todo){
        if(todo){
            comboFormaPago.setSelectedIndex(0);
            chkInfoSpei.setSelected(false);
            txtCertPago.setEnabled(false);
            txtCadPago.setEnabled(false);
            txtSelloPago.setEnabled(false);
        }
        
        montoPago.setText("");
        comboMoneda.setSelectedIndex(0);
        fechaPago.setDate(new Date());
        
        txtCertPago.setText("");
        txtCadPago.setText("");
        txtSelloPago.setText("");
    }
    
    private void limpiarTodo(){
        DefaultTableModel modelPay = (DefaultTableModel)tablaPagos.getModel();
        DefaultTableModel modelDoc = (DefaultTableModel)tablaDocs.getModel();
        
        this.listaPagos.clear();
        //this.docs.clear();
        modelPay.setRowCount(0);
        modelDoc.setRowCount(0);
        
        comboFormaPago.setSelectedIndex(0);
        montoPago.setText("");
        comboMoneda.setSelectedIndex(0);
        fechaPago.setDate(new Date());
        cfdisAsociados.setText("");
        tipoRelacion.setSelectedIndex(0);
    }
    
    private void buscarDocumentos(Pago p){
        Folios fol = new Folios(emi.getRfc(), rec.getRfc(), this, p);
        fol.setVisible(true);
    }
    
    public void addDocumentos (Folios fol, Pago p){
        final DefaultTableModel model2 = (DefaultTableModel)tablaDocs.getModel();
        p.setDocumentos(fol.docsPagar);
        Object[] row;
        
        p.setPagoRetencionesIVA(pagoRetencionesIVA);
        p.setPagoRetensionesIEPS(pagoRetensionesIEPS);
        p.setPagoRetensionesISR(pagoRetensionesISR);
        p.setPagoTrasladosBaseIVA0(pagoTrasladosBaseIVA0);
        p.setPagoTrasladosBaseIVA16(pagoTrasladosBaseIVA16);
        p.setPagoTrasladosBaseIVA8(pagoTrasladosBaseIVA8);
        p.setPagoTrasladosBaseIVAExento(pagoTrasladosBaseIVAExento);
        p.setPagoTrasladosImpuestoIVA0(pagoTrasladosImpuestoIVA0);
        p.setPagoTrasladosImpuestoIVA16(pagoTrasladosImpuestoIVA16);
        p.setPagoTrasladosImpuestoIVA8(pagoTrasladosImpuestoIVA8);
        
        listaPagos.add(p);
        ultimoPago = p;
        
        this.pagoRetencionesIVA = null;
	this.pagoRetensionesISR = null;
	this.pagoRetensionesIEPS = null; 
	this.pagoTrasladosBaseIVA16 = null; 
	this.pagoTrasladosImpuestoIVA16 = null; 
	this.pagoTrasladosBaseIVA8 = null;
	this.pagoTrasladosImpuestoIVA8 = null; 
	this.pagoTrasladosBaseIVA0 = null;
	this.pagoTrasladosImpuestoIVA0 = null; 
	this.pagoTrasladosBaseIVAExento = null; 
        
        if(!p.getDocumentos().isEmpty()){
            
            for(Documento d : p.getDocumentos()){
                row = new Object[10];
                row[0] = d.getFolio();
                row[1] = d.getSerie();
                row[2] = d.getImpSaldoAnterior();
                row[3] = d.getImpPagado();
                row[4] = d.getImpSaldoInsoluto();
                row[5] = d.getNumParcialidad();
                row[6] = d.getMetodoPago();
                row[7] = d.getMoneda();
                row[8] = d.getTipoCambio();
                row[9] = d.getUuid();

                model2.addRow(row);
            }
            
            this.validarDatosDocumento(model2);
        }

        fol.dispose();
    }
    
    private void mensaje(String msg, int tipo){
        JOptionPane.showMessageDialog(null, msg, "Mensaje", tipo);
    }
    
    private void llenarTipoRelacion(){
        for(String x : arrayTipoRel){
            this.tipoRelacion.addItem(x);
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
            java.util.logging.Logger.getLogger(RecibosPagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RecibosPagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RecibosPagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RecibosPagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RecibosPagos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton asociarCfdi;
    private javax.swing.JComboBox<String> bancoBeneficiario;
    private javax.swing.JComboBox<String> bancoOrdenante;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnEmitir;
    private javax.swing.JTextPane cfdisAsociados;
    private javax.swing.JCheckBox chkInfoSpei;
    private javax.swing.JCheckBox chkOrdenante;
    private javax.swing.JComboBox<String> comboFormaPago;
    private javax.swing.JComboBox<String> comboMoneda;
    private javax.swing.JTextField cuentaBeneficiario;
    private javax.swing.JTextField cuentaOrdenante;
    private com.toedter.calendar.JDateChooser fechaPago;
    private javax.swing.JTextField folioText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel labelEmisor;
    private javax.swing.JLabel labelReceptor;
    private javax.swing.JLabel lblTotalPagado;
    private javax.swing.JLabel lblTotalSaldoAnterior;
    private javax.swing.JLabel lblTotalSaldoInsoluto;
    private javax.swing.JTextField montoPago;
    private javax.swing.JTable tablaDocs;
    private javax.swing.JTable tablaPagos;
    private javax.swing.JTextField tipoCambio;
    private javax.swing.JComboBox<String> tipoRelacion;
    private javax.swing.JTextArea txtCadPago;
    private javax.swing.JTextArea txtCertPago;
    private javax.swing.JTextField txtNumOperacion;
    private javax.swing.JTextArea txtSelloPago;
    // End of variables declaration//GEN-END:variables
}

class Banco{
    private String nombre;
    private String rfc;
    private int id;
    
    public Banco(String nombre, String rfc, int id){
        this.nombre = nombre;
        this.rfc = rfc;
        this.id = id;
    }
    
    public String getBanco(){
        return this.nombre;
    }
    
    public String getRfc(){
        return this.rfc;
    }
    
    public int getId(){
        return this.id;
    }
}