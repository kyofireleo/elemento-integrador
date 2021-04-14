package gui;

import elemento.Elemento;
import static elemento.Elemento.pass;
import static elemento.Elemento.user;
import elemento.TextPrompt;
import java.awt.Font;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import utils.ConnectionFactory;
import utils.Utils;

/**
 *
 * @author Abe
 */
public class Configurar extends javax.swing.JFrame {

    String rfce, noCertificado, logo, regimenFiscal, lugarExp;
    boolean prod;
    private final String ODBC = "Elemento", PASS = "administradorID";
    private String rfcActual, certActual, logoActual, regimenActual, lugarActual;
    private String rfcVendedor;
    private List<Boolean> sonSucursales;
    private List<Integer> idSucursales;
    private int idSucursal;
    private Integer cuentaIdActual;
    private Sucursales suc;
    private Map<String,Integer> mapemi;
    private TextPrompt holder;
    private boolean isUpdateFolios = false;

    ConnectionFactory fact = new ConnectionFactory(Elemento.log);
    Utils util = new Utils(Elemento.log);
    private List<String> nombres;

    public Configurar(String rfce) {
        this.rfce = rfce;
        mapemi = new HashMap();
        initComponents();
        setLocationRelativeTo(null);
        llenarFormulario(consultarProd().booleanValue());
        if (!produccion.isSelected()) {
            actualizar.setEnabled(false);
        } else {
            prod = true;
        }
        
        setHolders();
        setFoliosTablaColumnWidths();
        
        datos.getColumnModel().getColumn(6).setMinWidth(0);
        datos.getColumnModel().getColumn(6).setMaxWidth(0);
        datos.getColumnModel().getColumn(6).setWidth(0);
    }

    public Configurar() {
        rfce = "";
        mapemi = new HashMap();
        initComponents();
        setLocationRelativeTo(null);
        llenarFormulario(consultarProd().booleanValue());
        if (!produccion.isSelected()) {
            actualizar.setEnabled(false);
        } else {
            prod = true;
        }
        
        setHolders();
        setFoliosTablaColumnWidths();
        
        datos.getColumnModel().getColumn(6).setMinWidth(0);
        datos.getColumnModel().getColumn(6).setMaxWidth(0);
        datos.getColumnModel().getColumn(6).setWidth(0);
    }
    
    private void setHolders(){
        holder = new TextPrompt("Serie", this.serie);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
        
        holder = new TextPrompt("Folio Inicial", this.folio);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
        
        holder = new TextPrompt("Plantilla", this.plantilla);
        holder.changeAlpha(0.75f);
        holder.changeStyle(Font.ITALIC);
    }
    
    public void setFoliosTablaColumnWidths() {
        TableColumnModel columnModel = foliosTabla.getColumnModel();
        int widths[] = {150,75,75,350};
        for (int i = 0; i < widths.length; i++) {
            if (i < columnModel.getColumnCount()) {
                columnModel.getColumn(i).setMaxWidth(widths[i]);
            }
            else break;
        }
    }

    private void llenarFormulario(boolean consulta) {
        Connection con;
        Statement stmt, stmt2, stmt3, stmt4;
        ResultSet rs, rs2, rs3, rs4;
        DefaultTableModel model = (DefaultTableModel) datos.getModel();
        model.setRowCount(0);
        DefaultTableModel modelFolios = (DefaultTableModel) foliosTabla.getModel();
        modelFolios.setRowCount(0);

        DefaultComboBoxModel combo = (DefaultComboBoxModel) rfc.getModel();
        combo.removeAllElements();
        combo.addElement("Seleccione un RFC");
        
        DefaultComboBoxModel comboTipos = (DefaultComboBoxModel) tipoComprobante.getModel();
        comboTipos.removeAllElements();
        comboTipos.addElement("Tipos de Comprobante");
        
        try {
            rfc.setSelectedIndex(0);
            certificado.setText("");
            logoPath.setText("");
            regimenText.setText("");
            lugarExpedicion.setText("");
            esSucursalCheck.setSelected(false);

            con = Elemento.odbc();
            stmt = fact.stmtLectura(con);
            stmt2 = fact.stmtLectura(con);
            stmt3 = fact.stmtLectura(con);
            stmt4 = fact.stmtLectura(con);

            rs = consultar(stmt);
            
            rs3 = stmt3.executeQuery("SELECT Id,rfc,nombre FROM Emisores");
            rs4 = stmt4.executeQuery("SELECT c_tiposcomprobante_id, tiposcomprobante, descripcion FROM c_tiposcomprobante");

            String rfct, cert, activados, regimen, restantes, usados, cuenta_id;
            boolean esSucursal;
            int idSucursal;
            
            sonSucursales = new ArrayList();
            idSucursales = new ArrayList();

            while (rs.next()) {
                rfct = rs.getString("rfc").trim();
                cert = rs.getString("nocertificado").trim();
                activados = rs.getString("creditosActivados").trim();
                regimen = rs.getString("regimenFiscal").trim();
                restantes = rs.getString("creditosRestantes").trim();
                usados = rs.getString("creditosUsados").trim();
                esSucursal = rs.getBoolean("esSucursal");
                cuenta_id = rs.getString("cuenta_id").trim();

                if (esSucursal) {
                    idSucursal = rs.getInt("idSucursal");
                } else {
                    idSucursal = 0;
                }

                //consultarInfoFolios();

                if (consulta) {
                    produccion.setSelected(true);
                    if (rfct.equalsIgnoreCase("AAA010101AAA")) {
                        continue;
                    }
                } else {
                    produccion.setSelected(false);
                    if (!rfct.equalsIgnoreCase("AAA010101AAA")) {
                        continue;
                    }
                }

                String[] tmp = {rfct, cert, regimen, activados, restantes, usados, cuenta_id};
                model.addRow(tmp);
                sonSucursales.add(esSucursal);
                idSucursales.add(idSucursal);
            }
            
            if(!rfce.trim().isEmpty()){
                rs2 = consultar(stmt2, rfce);
                if (rs2.next()) {
                    rfc.setSelectedItem(rs2.getString("rfc").trim());
                    certificado.setText(rs2.getString("nocertificado").trim());
                    logoPath.setText(rs2.getString("logo").trim());
                    regimenText.setText(rs2.getString("regimenFiscal").trim());
                    lugarExpedicion.setText(rs2.getString("lugarExpedicion").trim());
                    esSucursalCheck.setSelected(rs2.getBoolean("esSucursal"));

                }
                rs2.close();
            }
            nombres = new ArrayList();
            while (rs3.next()) {
                String rr = rs3.getString("rfc");
                mapemi.put(rr,rs3.getInt("id"));
                combo.addElement(rr);
                nombres.add(rs3.getString("nombre"));
            }
            
            while(rs4.next()){
                String tipo = rs4.getString("tiposcomprobante");
                String descripcion = tipo + ", " + rs4.getString("descripcion");
                String nom = "";
                switch(tipo){
                    case "I":
                        nom = " (Factura)";
                    break;
                    
                    case "E":
                        nom = " (Nota de Crédito)";
                    break;
                }
                comboTipos.addElement(descripcion + nom);
            }

            //consultarInfoFolios();
            rs.close();
            rs3.close();
            rs4.close();
            stmt.close();
            stmt2.close();
            stmt3.close();
            stmt4.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al consultar la tabla Cuentas: " + e.getMessage(), e);
        }
    }

    private void consultarInfoFolios(String rfcE) {
        Connection con = Elemento.odbc();
        Statement stmt = fact.stmtLectura(con);
        ResultSet rs;
        DefaultTableModel model = (DefaultTableModel)foliosTabla.getModel();
        model.setRowCount(0);
        
        this.tipoComprobante.setSelectedIndex(0);
        this.serie.setText("");
        this.folio.setText("");
        this.plantilla.setText("");
        
        try {
            rs = stmt.executeQuery("SELECT emiteNominas FROM Emisores WHERE rfc = \'"+rfcE+"\'");
            if(rs.next()){
                DefaultComboBoxModel cmbTipo = (DefaultComboBoxModel)tipoComprobante.getModel();
                if(!rs.getBoolean("emiteNominas")){
                    for (int i = 0; i < cmbTipo.getSize(); i++) {
                        String x = cmbTipo.getElementAt(i).toString();
                        if(x.contains("N,")){
                            cmbTipo.removeElementAt(i);
                        }
                    }
                }else{
                    boolean estaNomina = false;
                    for (int i = 0; i < cmbTipo.getSize(); i++) {
                        String x = cmbTipo.getElementAt(i).toString();
                        if(x.contains("N,")){
                            estaNomina = true;
                            break;
                        }
                    }
                    if(!estaNomina)
                        cmbTipo.insertElementAt("N, Nómina", 3);
                }
            }
            rs.close();
            
            rs = stmt.executeQuery("SELECT f.serie, f.ultimo_folio, f.plantilla, tc.tiposcomprobante, tc.descripcion, tc.c_tiposcomprobante_id "
                    + "FROM Folios f INNER JOIN c_tiposcomprobante tc ON f.idComprobante = tc.c_tiposcomprobante_id "
                    + "WHERE f.cuenta_id = " + cuentaIdActual + " "
                    + "ORDER BY f.idComprobante ASC");
            while (rs.next()) {
                String tipo = rs.getString("tiposcomprobante");
                String serieT = rs.getString("serie");
                String folioT = rs.getString("ultimo_folio");
                String plantillaT = rs.getString("plantilla");
                String idTipo = rs.getString("c_tiposcomprobante_id");
                
                String nom = "";
                switch(tipo){
                    case "I":
                        nom = " (Factura)";
                    break;
                    
                    case "E":
                        nom = " (Nota de Crédito)";
                    break;
                }
                
                String tipoComprobanteT = idTipo + ", " + rs.getString("descripcion") + nom;
                
                Object row [] = {tipoComprobanteT, serieT, folioT, plantillaT};
                model.addRow(row);
            }
            rs.close();
            
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al consultar la informacion de los folios y series: " + ex.getMessage(), ex);
        }
    }

    private ResultSet consultar(Statement stmt, String valor) throws Exception {
        ResultSet rs;
        if(valor.trim().equalsIgnoreCase(rfce.trim()))
            rs = stmt.executeQuery("SELECT TOP 1 * FROM Cuentas WHERE rfc = \'" + valor + "\'");
        else
            rs = stmt.executeQuery("SELECT * FROM Cuentas WHERE cuenta_id = " + valor);
        return rs;
    }

    private ResultSet consultar(Statement stmt) throws Exception {
        ResultSet rs;
        if (true) {
            rs = stmt.executeQuery("SELECT * FROM Cuentas");
        }
        return rs;
    }

    private Boolean consultarProd() {
        try {
            Connection con;
            Statement stmt;
            ResultSet rs;

            con = Elemento.odbc();
            stmt = fact.stmtLectura(con);

            rs = stmt.executeQuery("SELECT * FROM Produccion");
            rs.next();
            Boolean produ = rs.getBoolean("produccion");
            stmt.close();
            con.close();
            produccion.setSelected(produ);
            return produ;
        } catch (Exception ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al consultar la tabla Produccion: " + ex.getMessage(), ex);
            return false;
        }
    }

    public String getNoCertificado() {
        return certificado.getText().trim();
    }

    public String getRfc() {
        if (rfce != null || !rfce.equals("")) {
            return rfc.getSelectedItem().toString();
        } else {
            return rfce;
        }
    }

    public String getRegimen() {
        return regimenText.getText().trim();
    }

    public String getLugarExp() {
        return lugarExpedicion.getText().trim();
    }

    public boolean isProduccion() {
        return prod;
    }

    public String getLogo() {
        return logoPath.getText().trim();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        produccion = new javax.swing.JCheckBox();
        actualizar = new javax.swing.JButton();
        agregar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        datos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        logoPath = new javax.swing.JTextField();
        examinar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        regimenText = new javax.swing.JTextField();
        agregarEmisor = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lugarExpedicion = new javax.swing.JTextField();
        borrar = new javax.swing.JButton();
        infoEmisor = new javax.swing.JButton();
        agregarCreditos = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        certificado = new javax.swing.JTextField();
        rfc = new javax.swing.JComboBox();
        infoSucursal = new javax.swing.JButton();
        esSucursalCheck = new javax.swing.JCheckBox();
        btnDonat = new javax.swing.JButton();
        lblFechaCert = new javax.swing.JLabel();
        plantilla = new javax.swing.JTextField();
        exaFact = new javax.swing.JButton();
        tipoComprobante = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        foliosTabla = new javax.swing.JTable();
        agregarComprobante = new javax.swing.JButton();
        serie = new javax.swing.JTextField();
        folio = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        borrarComprobante = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Configuracion");
        setResizable(false);

        jLabel4.setText("RFC");

        produccion.setText("Produccion");
        produccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                produccionActionPerformed(evt);
            }
        });

        actualizar.setText("Guardar Cuenta");
        actualizar.setEnabled(false);
        actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualizarActionPerformed(evt);
            }
        });

        agregar.setText("Agregar Cuenta");
        agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarActionPerformed(evt);
            }
        });

        datos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "RFC", "No. Certificado", "Regimen Fiscal", "Creditos Activados", "Creditos Restantes", "Creditos Usados", "Cuenta ID"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
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
        datos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                datosFocusLost(evt);
            }
        });
        datos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(datos);

        jLabel1.setText("Logo");

        logoPath.setNextFocusableComponent(examinar);

        examinar.setText("Examinar");
        examinar.setNextFocusableComponent(lugarExpedicion);
        examinar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                examinarActionPerformed(evt);
            }
        });

        jLabel2.setText("Regimen Fiscal");

        regimenText.setNextFocusableComponent(logoPath);

        agregarEmisor.setText("Agregar Emisor");
        agregarEmisor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarEmisorActionPerformed(evt);
            }
        });

        jLabel5.setText("Emisor");

        jLabel6.setText("Certificados y Logo");

        jLabel8.setText("Tipos de Comprobantes a Emitir");

        jLabel7.setText("Lugar Exp.");

        borrar.setText("Borrar Cuenta");
        borrar.setEnabled(false);
        borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarActionPerformed(evt);
            }
        });

        infoEmisor.setText("Info Emisor");
        infoEmisor.setEnabled(false);
        infoEmisor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoEmisorActionPerformed(evt);
            }
        });

        agregarCreditos.setText("Agregar Creditos");
        agregarCreditos.setEnabled(false);
        agregarCreditos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarCreditosActionPerformed(evt);
            }
        });

        jLabel3.setText("No. Certificado");

        certificado.setNextFocusableComponent(regimenText);

        rfc.setNextFocusableComponent(certificado);
        rfc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rfcActionPerformed(evt);
            }
        });

        infoSucursal.setText("Info Sucursal");
        infoSucursal.setEnabled(false);
        infoSucursal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoSucursalActionPerformed(evt);
            }
        });

        esSucursalCheck.setText("Es Sucursal");
        esSucursalCheck.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                esSucursalCheckMouseClicked(evt);
            }
        });

        btnDonat.setText("Info Donataria");
        btnDonat.setEnabled(false);
        btnDonat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDonatActionPerformed(evt);
            }
        });

        plantilla.setName("plantilla"); // NOI18N

        exaFact.setText("...");
        exaFact.setName("examinarPlantilla"); // NOI18N
        exaFact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exaFactActionPerformed(evt);
            }
        });

        tipoComprobante.setName("tipoComprobante"); // NOI18N

        foliosTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tipo de Comprobante", "Serie", "Folio Actual", "Plantilla"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
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
        foliosTabla.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                foliosTablaFocusLost(evt);
            }
        });
        foliosTabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                foliosTablaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(foliosTabla);

        agregarComprobante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/add.png"))); // NOI18N
        agregarComprobante.setName("agregarComprobante"); // NOI18N
        agregarComprobante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarComprobanteActionPerformed(evt);
            }
        });

        serie.setName("serie"); // NOI18N

        folio.setName("folio"); // NOI18N

        borrarComprobante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/DeleteRed.png"))); // NOI18N
        borrarComprobante.setName("borrarComprobante"); // NOI18N
        borrarComprobante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarComprobanteActionPerformed(evt);
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
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(agregarEmisor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(produccion))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel7))
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(logoPath, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(examinar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lugarExpedicion, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(agregar))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(26, 26, 26)
                                .addComponent(regimenText, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(50, 50, 50)
                                        .addComponent(jLabel4))
                                    .addComponent(jLabel3))
                                .addGap(24, 24, 24)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(certificado, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                                    .addComponent(rfc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblFechaCert, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(esSucursalCheck))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(tipoComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(serie, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(folio, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(plantilla, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(exaFact, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(agregarComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(borrarComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(agregarCreditos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(borrar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(actualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDonat, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(infoSucursal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(infoEmisor)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(produccion)
                    .addComponent(agregarEmisor))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel6)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(rfc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(esSucursalCheck))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFechaCert, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(certificado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)))
                        .addGap(11, 11, 11)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(regimenText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(logoPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(examinar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(lugarExpedicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(agregar)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tipoComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(serie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(folio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(plantilla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(exaFact)
                                .addComponent(agregarComprobante))
                            .addComponent(borrarComprobante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(11, 11, 11)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(infoEmisor)
                        .addComponent(infoSucursal)
                        .addComponent(btnDonat))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(agregarCreditos)
                        .addComponent(borrar)
                        .addComponent(actualizar)))
                .addGap(11, 11, 11)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void examinarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_examinarActionPerformed
        logoPath.setText(seleccionarArchivo("Imagen de Mapa de Bits .JPG", "jpg"));
    }//GEN-LAST:event_examinarActionPerformed

    private String seleccionarArchivo(String nombre, String tipo) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileNameExtensionFilter ext = new FileNameExtensionFilter(nombre, tipo);
        chooser.setFileFilter(ext);
        if (nombre.contains("Plantilla")) {
            chooser.setCurrentDirectory(new File(Elemento.pathPlantillas));
        } else {
            chooser.setCurrentDirectory(new File(Elemento.unidad + ":\\"));
        }
        chooser.setVisible(true);
        int opc = chooser.showOpenDialog(null);
        String path = "";

        switch (opc) {
            case JFileChooser.APPROVE_OPTION:
                // path to watch
                path = chooser.getSelectedFile().getAbsolutePath();
                chooser.setVisible(false);
                break;
            case JFileChooser.ERROR_OPTION:
                JOptionPane.showMessageDialog(null, "Ocurrio un error, intentelo de nuevo");
                chooser.setVisible(false);
            case JFileChooser.CANCEL_OPTION:
                chooser.setVisible(true);
        }

        return path;
    }

    private void habilitarBotones(boolean var) {
        actualizar.setEnabled(var);
        borrar.setEnabled(var);
        infoEmisor.setEnabled(var);
        infoSucursal.setEnabled(var);
        agregarCreditos.setEnabled(var);
    }

    private void datosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datosMouseClicked
        TableModel model = datos.getModel();
        String rfct = model.getValueAt(datos.getSelectedRow(), 0).toString().trim();
        String cuenta_id = model.getValueAt(datos.getSelectedRow(), 6).toString().trim();
        String cert, logot, regi, ser;
        int cuent;
        boolean esSuc;
        habilitarBotones(true);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");

        try {
            File certFile = new File(Elemento.pathConfig+rfct+".cer");
            if(certFile.exists()){
                CertificateFactory fac = CertificateFactory.getInstance("X509");
                FileInputStream is = new FileInputStream(certFile);
                X509Certificate certi = (X509Certificate) fac.generateCertificate(is);
                //System.out.println("From: " + certi.getNotBefore());
                lblFechaCert.setText("Caduca el " + sdf.format(certi.getNotAfter()));
                byte[] byteArray = certi.getSerialNumber().toByteArray();
                String noSerie = new String(byteArray);
                Elemento.log.info("Se selecciono cuenta con certificado: " + noSerie);
            }else{
                lblFechaCert.setText("");
            }
            
            Connection con = Elemento.odbc();
            Statement stmt = fact.stmtLectura(con);

            ResultSet rs = this.consultar(stmt, cuenta_id);

            if (rs.next()) {
                rfc.setSelectedItem(rfct);
                cert = rs.getString("nocertificado");
                logot = rs.getString("logo");
                regi = rs.getString("regimenFiscal");
                ser = rs.getString("lugarExpedicion");
                esSuc = rs.getBoolean("esSucursal");
                cuent = rs.getInt("cuenta_id");

                certificado.setText(cert);
                logoPath.setText(logot);
                regimenText.setText(regi);
                lugarExpedicion.setText(ser);
                esSucursalCheck.setSelected(esSuc);

                rfcActual = rfct;
                certActual = cert;
                logoActual = logot;
                regimenActual = regi;
                lugarActual = ser;
                cuentaIdActual = cuent;
                
                consultarInfoFolios(rfct);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al consultar la credencial del RFC: " + rfct, ex);
        }
    }//GEN-LAST:event_datosMouseClicked

    private String decrypt(String cadena) {
        StandardPBEStringEncryptor s = new StandardPBEStringEncryptor();
        s.setPassword("adminID");
        String devuelve = "";
        try {
            devuelve = s.decrypt(cadena);
        } catch (Exception e) {
            e.printStackTrace();
            Elemento.log.error("No se pudo desencriptar el token:", e);
        }
        return devuelve;
    }

    private int verificarToken() {
        Calendar cal = GregorianCalendar.getInstance();
        Date x = cal.getTime();
        rfcVendedor = JOptionPane.showInputDialog("Ingrese el RFC del vendedor:").toUpperCase();

        if (rfcVendedor.equals("32288722466")) {
            rfcVendedor = "Administrador";
            return Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad de creditos a activar: "));
        }

        String token = JOptionPane.showInputDialog(null, "Ingresa el token para activar creditos:");
        String dato = decrypt(token);

        if (!dato.isEmpty()) {
            try {
                String[] tok = dato.split(",");
                //RFCVendedor,RFCEmisor,Creditos,Fecha
                String rfcV = tok[0].trim();
                String rfcEmisor = tok[1].trim();
                String rfcR = rfc.getSelectedItem().toString().trim();
                int creditos = Integer.parseInt(tok[2].trim());
                SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
                Date y = formato.parse(tok[3].trim());

                if (rfcVendedor.equals(rfcV) && rfcEmisor.equals(rfcR)) {
                    if (x.getDate() == y.getDate() && x.getMonth() == y.getMonth() && x.getYear() == y.getYear()) {
                        return creditos;
                    } else {
                        Elemento.log.warn("El token ha caducado, pongase en contacto con el administrador");
                        JOptionPane.showMessageDialog(null, "El token ha caducado, pongase en contacto con el administrador");
                        return 0;
                    }
                } else {
                    Elemento.log.warn("El token no es valido, contacte con el administrador");
                    JOptionPane.showMessageDialog(null, "El token no es valido, contacte con el administrador");
                    return 0;
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                Elemento.log.error("Excepcion al parsear la fecha del token", ex);
                return 0;
            }
        } else {
            return 0;
        }
    }

    private void agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarActionPerformed
        final int creditos = verificarToken();
        if (creditos > 0) {
            try {
                // TODO add your handling code here:

                Connection con = Elemento.odbc();
                con.setAutoCommit(false);
                Statement stmt = fact.stmtEscritura(con);
                final String cert = certificado.getText().trim();
                final String rfcE = rfc.getSelectedItem().toString().trim();
                final String nombre = nombres.get(rfc.getSelectedIndex() - 1);

                if (suc != null) {
                    idSucursal = suc.getIdSucursal();
                } else {
                    idSucursal = 0;
                }
                /******Se cambiara despues de analizarlo a mas detalle*******/
                /*ResultSet rs = stmt.executeQuery("SELECT 1 FROM Cuentas WHERE rfc = \'" + rfcE + "\' AND nocertificado = \'"+cert+"\'");*/
                /************************************************************/
                
                ResultSet rs = stmt.executeQuery("SELECT 1 FROM Cuentas WHERE rfc = \'" + rfcE + "\'");
                if (!rs.next()) {
                    if (foliosTabla.getRowCount() < 1) {
                        util.printError("Falta agregar la informacion de series y folios");
                    } else {
                        Savepoint sp = con.setSavepoint("Cuentas");
                        stmt.executeUpdate("INSERT INTO Cuentas (rfc,nocertificado,logo,regimenFiscal,lugarExpedicion,cantDecimales,creditosActivados,creditosRestantes,creditosUsados,esSucursal,idSucursal)"
                                + "VALUES (\'" + rfcE + "\', \'" + certificado.getText().trim() + "\', \'" + logoPath.getText() + "\', \'"
                                + regimenText.getText().trim() + "\', \'" + lugarExpedicion.getText() + "\',"
                                + 2 + "," + creditos + "," + creditos + "," + 0 + "," + esSucursalCheck.isSelected() + "," + idSucursal + ")");
                        
                        rs = stmt.executeQuery("SELECT cuenta_id FROM Cuentas WHERE rfc = \'"+rfcE+"\'");
                        rs.next();
                        int cuenta_id = rs.getInt("cuenta_id");
                        boolean resultado = guardarTiposComprobante(cuenta_id, rfcE, con);

                        stmt.close();
                        
                        if(resultado){
                            con.commit();
                            
                            final Emisores emis = new Emisores(true, rfcE);
                            new Thread() {
                                @Override
                                public void run() {
                                    util.enviarEmail("esquerodriguez@gmail.com,activacion@feimpresoresdigitales.com,gorenajc2.3@gmail.com",
                                            "SE AGREGO LA SIGUIENTE CUENTA\r\n"
                                            + "*Vendedor: " + rfcVendedor.toUpperCase() + "\r\n"
                                            + "*Certificado: " + cert + "\r\n\r\n"
                                            + "***DATOS DEL EMISOR***\r\n\r\n"
                                            + "NOMBRE2: " + nombre + "\r\n"
                                            + "RFC2: " + rfcE + "\r\n"
                                            + "CALLE2: " + emis.calle.getText().trim() + "\r\n"
                                            + "NOEXTERIOR2: " + emis.noExterior.getText().trim() + "\r\n"
                                            + "NOINTERIOR2: " + emis.noInterior.getText().trim() + "\r\n"
                                            + "COLONIA2: " + emis.colonia.getText().trim() + "\r\n"
                                            + "LOCALIDAD2: " + emis.localidad.getText().trim() + "\r\n"
                                            + "MUNICIPIO2: " + emis.municipio.getText().trim() + "\r\n"
                                            + "ESTADO2: " + emis.estado.getSelectedItem().toString() + "\r\n"
                                            + "PAIS2: " + emis.pais.getSelectedItem().toString() + "\r\n"
                                            + "CP2: " + emis.cp.getText().trim() + "\r\n\r\n"
                                            + "*Folios Activados: " + creditos + "\r\n");
                                }
                            }.start();
                            
                            util.print("Cuenta agregada exitosamente");
                        }else{
                            con.rollback(sp);
                            util.printError("Ocurrio un problema al guardar los tipos de comprobante");
                        }
                        
                        con.close();
                        llenarFormulario(produccion.isSelected());
                    }
                } else {
                    /******Se cambiara despues de analizarlo a mas detalle*******/
                    /*util.printError("No se puede agregar la cuenta con RFC: " + rfcE + " y con certificado: " + cert + " porque ya existe.");*/
                    /************************************************************/
                    
                    util.printError("No se puede agregar la cuenta con RFC: " + rfcE + " porque ya existe una.");
                }
            } catch (HeadlessException | SQLException ex) {
                ex.printStackTrace();
                Elemento.log.error("Excepcion al insertar cuenta en la tabla Cuentas: " + ex.getMessage(), ex);
                util.printError("Excepcion al insertar cuenta en la tabla Cuentas: " + ex.getMessage());
            }
        } else {
            util.printError("No se puede agregar la cuenta ya que no se han ingresado creditos");
        }
    }//GEN-LAST:event_agregarActionPerformed
    
    private boolean guardarTiposComprobante(int cuenta_id, String rfcE, Connection con) throws SQLException{
        DefaultTableModel model = (DefaultTableModel)foliosTabla.getModel();
        PreparedStatement pstmt = con.prepareStatement("INSERT INTO Folios (idComprobante,rfc,ultimo_folio,serie,plantilla,cuenta_id) VALUES(?,?,?,?,?,?)");
        
        for(int i=0; i < foliosTabla.getRowCount(); i++){
            int idComp = new Integer(model.getValueAt(i, 0).toString().split(",")[0]);
            int ultimoFolio = new Integer(model.getValueAt(i, 2).toString());
            String ser = model.getValueAt(i, 1).toString();
            String plant = model.getValueAt(i, 3).toString();
            
            pstmt.setInt(1, idComp);
            pstmt.setString(2, rfcE);
            pstmt.setInt(3, ultimoFolio);
            pstmt.setString(4, ser);
            pstmt.setString(5, plant);
            pstmt.setInt(6, cuenta_id);
            
            pstmt.addBatch();
        }
        
        int[] res = pstmt.executeBatch();
        pstmt.close();
        
        int posError = Arrays.binarySearch(res, PreparedStatement.EXECUTE_FAILED);
        
        boolean resultado = !(res.length != foliosTabla.getRowCount() || posError != -1);
        if(resultado){
            Elemento.log.info("Tipos de comprobante y folios agregados correctamente");
        }else{
            Elemento.log.error("Error al guardar los tipos de comprobante: {Length: " + res.length + ", PosicionError:" + posError + "}");
        }
        
        return resultado;
    }
    
    private String getQueryFoliosInsert(int idComprobante, String rfc, String ultimoFolio, String serie, String plantilla) {
        return "INSERT INTO Folios (idComprobante,rfc,ultimo_folio,serie,plantilla) "
                + "VALUES (" + idComprobante + ",\'" + rfc + "\'," + ultimoFolio + ",\'"
                + serie + "\',\'" + plantilla + "\')";
    }

    private void actualizarComboEmisores(final Emisores emis) {
        emis.setVisible(true);

        Thread th = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (!emis.isDisplayable()) {
                        llenarFormulario(true);
                        break;
                    }
                }
            }
        };
        th.start();
    }

    private void actualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizarActionPerformed
        // TODO add your handling code here:
        Connection con;
        Statement stmt;
        Boolean facts, notas, dona;
        try {
            con = Elemento.odbc();
            stmt = fact.stmtEscritura(con);

            noCertificado = getNoCertificado();
            rfce = getRfc();
            logo = getLogo();
            regimenFiscal = getRegimen();
            lugarExp = getLugarExp();

            stmt.executeUpdate("UPDATE Cuentas SET rfc=\'" + rfce + "\', nocertificado=\'" + noCertificado + "\',"
                    + " logo=\'" + logo + "\', regimenFiscal=\'" + regimenFiscal + "\', lugarExpedicion=\'" + lugarExp + "\'"
                    + " WHERE rfc = \'" + rfcActual + "\' AND nocertificado = \'" + certActual + "\'");


            util.print("Cuenta actualizada correctamente!!");
            llenarFormulario(true);
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            Elemento.log.info("Excepcion al actualizar la cuenta productiva: " + e.getMessage());
        }
    }//GEN-LAST:event_actualizarActionPerformed

    private void produccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_produccionActionPerformed
        // TODO add your handling code here:
        Connection con;
        Statement stmt;
        try {
            con = Elemento.odbc();
            stmt = fact.stmtEscritura(con);
            boolean produ = produccion.isSelected();
            stmt.executeUpdate("UPDATE Produccion SET produccion = " + produ);

            stmt.close();
            con.close();
            
            if (produ) {
                user = "SIGI7408036N9_71";
                pass = "66514482243426681793344";
            } else {
                user = "SIGI7408036N9_6";
                pass = "1505794126573071453720";
            }

            llenarFormulario(produ);
        } catch (Exception e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al leer la tabla Produccion: " + e.getMessage(), e);
        }
    }//GEN-LAST:event_produccionActionPerformed

    private void agregarEmisorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarEmisorActionPerformed
        // TODO add your handling code here:
        Emisores emis = new Emisores();
        actualizarComboEmisores(emis);
    }//GEN-LAST:event_agregarEmisorActionPerformed

    private void borrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarActionPerformed
        // TODO add your handling code here:
        if (JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar esta cuenta?", "Borrar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Connection con = Elemento.odbc();
            Statement stmt = fact.stmtEscritura(con);
            DefaultTableModel model = (DefaultTableModel) datos.getModel();
            String rfcx = model.getValueAt(datos.getSelectedRow(), 0).toString().trim();
            try {
                stmt.executeUpdate("DELETE FROM Cuentas WHERE rfc like \'" + rfcx + "\'");
                stmt.close();
                con.close();
                
                this.cuentaIdActual = null;
                this.rfcActual = null;
                this.logoActual = null;
                this.certActual = null;
                this.regimenActual = null;
                this.lugarActual = null;
                
            } catch (SQLException ex) {
                ex.printStackTrace();
                Elemento.log.error("Excepcion al eliminar una cuenta: " + ex.getMessage(), ex);
            }
            llenarFormulario(true);
        }
    }//GEN-LAST:event_borrarActionPerformed

    private void infoEmisorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoEmisorActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) datos.getModel();
        String rfct = model.getValueAt(datos.getSelectedRow(), 0).toString().trim();
        Emisores emis = new Emisores(true, rfct);
        actualizarComboEmisores(emis);
    }//GEN-LAST:event_infoEmisorActionPerformed

    private void agregarCreditosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarCreditosActionPerformed
        // TODO add your handling code here:
        int creditos = verificarToken();

        if (creditos > 0) {
//            String cr = JOptionPane.showInputDialog(null, "Ingrese la cantidad de creditos a agregar:", "Agregar Creditos",JOptionPane.INFORMATION_MESSAGE);
            this.agregarCreditos(creditos, rfcActual, certActual);

            this.llenarFormulario(true);
        } else {
            //JOptionPane.showMessageDialog(null, "El token no es valido, verificado con el administrador");
        }
    }//GEN-LAST:event_agregarCreditosActionPerformed

    private void datosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_datosFocusLost

    }//GEN-LAST:event_datosFocusLost

    private void rfcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rfcActionPerformed
        // TODO add your handling code here:
        rfc.transferFocus();
    }//GEN-LAST:event_rfcActionPerformed

    private void infoSucursalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoSucursalActionPerformed
        // TODO add your handling code here:
        if (sonSucursales.get(datos.getSelectedRow())) {
            new Sucursales(true, idSucursales.get(datos.getSelectedRow()), null).setVisible(true);
        }
    }//GEN-LAST:event_infoSucursalActionPerformed

    private void esSucursalCheckMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_esSucursalCheckMouseClicked
        // TODO add your handling code here:
        if (esSucursalCheck.isSelected()) {
            int idEmisor = obtenerIDEmisor(rfc.getSelectedItem().toString().trim(), Elemento.odbc());
            suc = new Sucursales(false, null, idEmisor);
            suc.setVisible(true);
        }
    }//GEN-LAST:event_esSucursalCheckMouseClicked

    private void btnDonatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDonatActionPerformed
        // TODO add your handling code here:
        String rr = rfc.getSelectedItem().toString();
        int idEmisor = mapemi.get(rr);
        String argu[] = {""+idEmisor};
        Donatarias.main(argu);
    }//GEN-LAST:event_btnDonatActionPerformed

    private void exaFactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exaFactActionPerformed
        // TODO add your handling code here:
        plantilla.setText(seleccionarArchivo("Plantilla .jasper","jasper"));
    }//GEN-LAST:event_exaFactActionPerformed

    private void agregarComprobanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarComprobanteActionPerformed
        // TODO add your handling code here:
        if(this.tipoComprobante.getSelectedIndex() > 0){
            String serieT = this.serie.getText();
            String plantillaT = this.plantilla.getText();
            int tipoComprobanteT = this.tipoComprobante.getSelectedIndex();
            int folioT = new Integer(this.folio.getText());
            DefaultTableModel model = (DefaultTableModel)foliosTabla.getModel();
            if(this.certActual == null || this.certActual.trim().isEmpty()){
                Object[] row = {tipoComprobanteT, serieT, folioT, plantillaT};
                if(this.isUpdateFolios){
                    model.removeRow(this.foliosTabla.getSelectedRow());
                }
                model.addRow(row);
            }else{
                try{
                    Connection con = Elemento.odbc();
                    PreparedStatement stmt;
                    if(this.isUpdateFolios){
                        stmt = con.prepareStatement("UPDATE Folios SET ultimo_folio = ?, serie = ?, plantilla = ? WHERE cuenta_id = ? AND idComprobante = ?");
                        stmt.setInt(1, folioT);
                        stmt.setString(2, serieT);
                        stmt.setString(3, plantillaT);
                        stmt.setInt(4, this.cuentaIdActual);
                        stmt.setInt(5, tipoComprobanteT);
                    }else{
                        stmt = con.prepareStatement("INSERT INTO Folios (rfc, idComprobante, ultimo_folio, serie, plantilla, cuenta_id) VALUES (?,?,?,?,?,?)");
                        stmt.setString(1, rfcActual);
                        stmt.setInt(2, tipoComprobanteT);
                        stmt.setInt(3, folioT);
                        stmt.setString(4, serieT);
                        stmt.setString(5, plantillaT);
                        stmt.setInt(6, this.cuentaIdActual);
                    }

                    stmt.executeUpdate();
                    stmt.close();
                    con.close();

                    this.isUpdateFolios = false;
                    this.cambiarBotonTiposComprobante();
                    this.consultarInfoFolios(rfcActual);
                }catch(SQLException e){
                    e.printStackTrace();
                    util.printError("Excepcion al agregar un tipo de comprobante a la cuenta: " + e.getMessage());
                    Elemento.log.error("Excepcion al agregar un tipo de comprobante a la cuenta: " + e.getMessage(), e);
                }
            }
            
        }else{
            util.printError("Debe seleccionar un tipo de comprobante");
        }
        
    }//GEN-LAST:event_agregarComprobanteActionPerformed
    
    private void cambiarBotonTiposComprobante(){
        if(isUpdateFolios){
            this.agregarComprobante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/save.png")));
            this.tipoComprobante.setEnabled(false);
        }else{
            this.agregarComprobante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/gui/add.png")));
            this.tipoComprobante.setEnabled(true);
        }
    }
    
    private void foliosTablaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_foliosTablaMouseClicked
        // TODO add your handling code here:
        TableModel model = foliosTabla.getModel();
        int row = foliosTabla.getSelectedRow();
        
        int indiceTipo = new Integer(model.getValueAt(row, 0).toString().split(",")[0]);
        this.tipoComprobante.setSelectedIndex(indiceTipo);
        this.serie.setText(model.getValueAt(row, 1).toString());
        this.folio.setText(model.getValueAt(row, 2).toString());
        this.plantilla.setText(model.getValueAt(row, 3).toString());
        
        this.isUpdateFolios = true;
        this.cambiarBotonTiposComprobante();
    }//GEN-LAST:event_foliosTablaMouseClicked

    private void foliosTablaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_foliosTablaFocusLost
        // TODO add your handling code here:
        String nameOb = evt.getOppositeComponent().getName();
        String names [] = {"serie", "folio", "plantilla", "agregarComprobante", "tipoComprobante", "exaFact", "borrarComprobante"};
        List<String> nombresTipos = new ArrayList(Arrays.asList(names));
        
        if(!nombresTipos.contains(nameOb)){
            this.tipoComprobante.setSelectedIndex(0);
            this.serie.setText("");
            this.folio.setText("");
            this.plantilla.setText("");

            this.isUpdateFolios = false;
            this.cambiarBotonTiposComprobante();
            this.foliosTabla.clearSelection();
        }
    }//GEN-LAST:event_foliosTablaFocusLost

    private void borrarComprobanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarComprobanteActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)foliosTabla.getModel();
        if(this.certActual == null || this.certActual.trim().isEmpty()){
            model.removeRow(foliosTabla.getSelectedRow());
        }else{
            removerComprobante(foliosTabla.getSelectedRow());
        }
    }//GEN-LAST:event_borrarComprobanteActionPerformed

    private void agregarCreditos(final int creditos, final String rfcE, final String cert) {
        try {
            Connection con = Elemento.odbc();
            Statement stmt = fact.stmtEscritura(con);
            Statement stmt2 = fact.stmtLectura(con);
            final String nombre = nombres.get(rfc.getSelectedIndex() - 1);
            ResultSet rs = stmt2.executeQuery("SELECT creditosActivados,creditosRestantes FROM Cuentas WHERE rfc = \'" + rfcE + "\' AND nocertificado = \'" + cert + "\'");

            if (rs.next()) {
                int rest = creditos + rs.getInt("creditosRestantes");
                stmt.executeUpdate("UPDATE Cuentas SET creditosActivados = \'" + creditos + "\', creditosRestantes = \'" + rest + "\' WHERE rfc = \'" + rfcE + "\' AND nocertificado = \'" + cert + "\'");
                Elemento.log.info("Se agregaron " + creditos + " creditos a la cuenta " + rfcE);
                final Emisores emis = new Emisores(true, rfcE);
                new Thread() {
                    @Override
                    public void run() {
                        util.enviarEmail("esquerodriguez@gmail.com,activacion@feimpresoresdigitales.com,gorenajc2.3@gmail.com",
                                "SE AGREGARON CREDITOS A LA SIGUIENTE CUENTA\r\n"
                                + "*Vendedor: " + rfcVendedor.toUpperCase() + "\r\n"
                                + "*Certificado: " + cert + "\r\n\r\n"
                                + "***DATOS DEL EMISOR***\r\n\r\n"
                                + "NOMBRE2: " + nombre + "\r\n"
                                + "RFC2: " + rfcE + "\r\n"
                                + "CALLE2: " + emis.calle.getText().trim() + "\r\n"
                                + "NOEXTERIOR2: " + emis.noExterior.getText().trim() + "\r\n"
                                + "NOINTERIOR2: " + emis.noInterior.getText().trim() + "\r\n"
                                + "COLONIA2: " + emis.colonia.getText().trim() + "\r\n"
                                + "LOCALIDAD2: " + emis.localidad.getText().trim() + "\r\n"
                                + "MUNICIPIO2: " + emis.municipio.getText().trim() + "\r\n"
                                + "ESTADO2: " + emis.estado.getSelectedItem().toString() + "\r\n"
                                + "PAIS2: " + emis.pais.getSelectedItem().toString() + "\r\n"
                                + "CP2: " + emis.cp.getText().trim() + "\r\n\r\n"
                                + "*Folios Activados: " + creditos + "\r\n");
                    }
                }.start();
            }

            rs.close();
            stmt2.close();
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al agregar creditos: " + ex.getMessage(), ex);
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Configurar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new Configurar().setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Elemento.log.error("Excepcion al cargar la clase Configurar: " + ex.getMessage(), ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton actualizar;
    private javax.swing.JButton agregar;
    private javax.swing.JButton agregarComprobante;
    private javax.swing.JButton agregarCreditos;
    private javax.swing.JButton agregarEmisor;
    private javax.swing.JButton borrar;
    private javax.swing.JButton borrarComprobante;
    private javax.swing.JButton btnDonat;
    private javax.swing.JTextField certificado;
    private javax.swing.JTable datos;
    private javax.swing.JCheckBox esSucursalCheck;
    private javax.swing.JButton exaFact;
    private javax.swing.JButton examinar;
    private javax.swing.JTextField folio;
    private javax.swing.JTable foliosTabla;
    private javax.swing.JButton infoEmisor;
    private javax.swing.JButton infoSucursal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblFechaCert;
    private javax.swing.JTextField logoPath;
    private javax.swing.JTextField lugarExpedicion;
    private javax.swing.JTextField plantilla;
    private javax.swing.JCheckBox produccion;
    private javax.swing.JTextField regimenText;
    private javax.swing.JComboBox rfc;
    private javax.swing.JTextField serie;
    private javax.swing.JComboBox tipoComprobante;
    // End of variables declaration//GEN-END:variables

    private int obtenerIDEmisor(String rfcE, Connection con) {
        Statement stmt = this.fact.stmtLectura(con);
        ResultSet rs;
        int idEmisor = 0;
        try {
            rs = stmt.executeQuery("SELECT id FROM Emisores WHERE rfc = \'" + rfcE + "\'");
            if (rs.next()) {
                idEmisor = rs.getInt("id");
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idEmisor;
    }

    private void removerComprobante(int selectedRow) {
        Connection con = Elemento.odbc();
        Statement stmt;
        DefaultTableModel model = (DefaultTableModel)foliosTabla.getModel();
        String tipoComp = model.getValueAt(selectedRow, 0).toString().split(",")[0];
        
        try{
            stmt = this.fact.stmtEscritura(con);
            stmt.executeUpdate("DELETE FROM Folios WHERE cuenta_id = " + this.cuentaIdActual + " and idComprobante = " + tipoComp);
            stmt.close();
            con.close();
            
            this.consultarInfoFolios(this.rfcActual);
        }catch(SQLException ex){
            ex.printStackTrace();
            Elemento.log.error("Error al eliminar un comprobante en la cuenta con certificado: " + this.certActual, ex);
        }
    }
}
