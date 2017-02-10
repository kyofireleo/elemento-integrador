
package gui;

import elemento.Elemento;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import utils.ConnectionFactory;
import utils.Utils;

/**
 *
 * @author Abe
 */
public class Configurar1 extends javax.swing.JFrame {
    
    /*String rfce,noCertificado,logo, regimenFiscal, lugarExp;
    boolean prod;
    private final String ODBC="ElementoID", PASS="administradorID";
    private String rfcActual,certActual,logoActual, regimenActual, lugarActual;
    private String rfcVendedor;
    private List<Boolean> sonSucursales;
    private List<Integer> idSucursales;
    private int idSucursal;
    private Sucursales suc;
    
    ConnectionFactory fact = new ConnectionFactory(Elemento.log);
    Utils util = new Utils(Elemento.log);
    private List<String> nombres;

    public Configurar1(String rfce) {
        this.rfce = rfce;
        initComponents();
        setLocationRelativeTo(null);
        llenarFormulario(consultarProd().booleanValue());
        if(!produccion.isSelected()){
            actualizar.setEnabled(false);
        }else{
            prod = true;
        }
    }
    
    public Configurar1 (){
        rfce = "";
        initComponents();
        setLocationRelativeTo(null);
        llenarFormulario(consultarProd().booleanValue());
        if(!produccion.isSelected()){
            actualizar.setEnabled(false);
        }else{
            prod = true;
        }
    }
    
    private void llenarFormulario(boolean consulta){
        Connection con;
        Statement stmt,stmt2, stmt3;
        ResultSet rs, rs2, rs3;
        DefaultTableModel model = (DefaultTableModel)datos.getModel();
        model.setRowCount(0);
        
        DefaultComboBoxModel combo = (DefaultComboBoxModel)rfc.getModel();
        combo.removeAllElements();
        combo.addElement("Seleccione un RFC");
        
        try{
            rfc.setSelectedIndex(0);
            certificado.setText("");
            logoPath.setText("");
            regimenText.setText("");
            lugarExpedicion.setText("");
            esSucursalCheck.setSelected(false);
            facturas.setSelected(true);
            notasCredito.setSelected(false);
            donativos.setSelected(false);
            
            con = Elemento.odbc();
            stmt = fact.stmtLectura(con);
            stmt2 = fact.stmtLectura(con);
            stmt3 = fact.stmtLectura(con);
            
            rs = consultar(stmt);
            rs2 = consultar(stmt2,"rfc",rfce);
            rs3 = stmt3.executeQuery("SELECT rfc,nombre FROM Emisores");
            
            String rfct, cert, activados, regimen, restantes,usados;
            boolean esSucursal;
            int idSucursal;
            Boolean facts,notas,dona;
            sonSucursales = new ArrayList();
            idSucursales = new ArrayList();
            
            while(rs.next()){
                rfct = rs.getString("rfc").trim();
                cert = rs.getString("nocertificado").trim();
                activados = rs.getString("creditosActivados").trim();
                regimen = rs.getString("regimenFiscal").trim();
                restantes = rs.getString("creditosRestantes").trim();
                usados = rs.getString("creditosUsados").trim();
                esSucursal = rs.getBoolean("esSucursal");
                
                if(esSucursal){
                    idSucursal = rs.getInt("idSucursal");
                }else{
                    idSucursal = 0;
                }
                
                bloquearInfoFolios();
                //consultarInfoFolios();
                
                if(consulta){
                    produccion.setSelected(true);
                   if(rfct.equalsIgnoreCase("AAA010101AAA")){
                        continue;
                   } 
                }else{
                    produccion.setSelected(false);
                    if(!rfct.equalsIgnoreCase("AAA010101AAA")){
                        continue;
                    }
                }
                
                String [] tmp = {rfct,cert,regimen,activados,restantes,usados};
                model.addRow(tmp);
                sonSucursales.add(esSucursal);
                idSucursales.add(idSucursal);
            }
            
            if(rs2.next()){
                rfc.setSelectedItem(rs2.getString("rfc").trim());
                certificado.setText(rs2.getString("nocertificado").trim());
                logoPath.setText(rs2.getString("logo").trim());
                regimenText.setText(rs2.getString("regimenFiscal").trim());
                lugarExpedicion.setText(rs2.getString("lugarExpedicion").trim());
                esSucursalCheck.setSelected(rs2.getBoolean("esSucursal"));
                
                facts = rs2.getBoolean("facturas");
                notas = rs2.getBoolean("notasCredito");
                dona = rs2.getBoolean("recibosDonativos");
                facturas.setSelected(facts);
                notasCredito.setSelected(notas);
                donativos.setSelected(dona);
            }
            nombres = new ArrayList();
            while(rs3.next()){
                combo.addElement(rs3.getString("rfc"));
                nombres.add(rs3.getString("nombre"));
            }
            
            bloquearInfoFolios();
            //consultarInfoFolios();
            rs.close();
            rs2.close();
            rs3.close();
            stmt.close();
            stmt2.close();
            con.close();
        }catch (Exception e){
            e.printStackTrace();
            Elemento.log.error("Excepcion al consultar la tabla Cuentas: " + e.getMessage(),e);
        }
    }
    
    private void bloquearInfoFolios(){
        boolean enable;
        
        if(!facturas.isSelected()){
            enable = false;
            serie.setEnabled(enable);
            folio.setEnabled(enable);
            plantilla.setEnabled(enable);
            exaFact.setEnabled(enable);
        }else{
            enable = true;
            serie.setEnabled(enable);
            folio.setEnabled(enable);
            plantilla.setEnabled(enable);
            exaFact.setEnabled(enable);
        }
        
        if(!notasCredito.isSelected()){
            enable = false;
            serieNC.setEnabled(enable);
            folioNC.setEnabled(enable);
            plantillaNC.setEnabled(enable);
            exaNC.setEnabled(enable);
        }else{
            enable = true;
            serieNC.setEnabled(enable);
            folioNC.setEnabled(enable);
            plantillaNC.setEnabled(enable);
            exaNC.setEnabled(enable);
        }
        
        if(!donativos.isSelected()){
            enable = false;
            serieRD.setEnabled(enable);
            folioRD.setEnabled(enable);
            plantillaRD.setEnabled(enable);
            exaRD.setEnabled(enable);
        }else{
            enable = true;
            serieRD.setEnabled(enable);
            folioRD.setEnabled(enable);
            plantillaRD.setEnabled(enable);
            exaRD.setEnabled(enable);
        }
        
    }
    
    private void consultarInfoFolios(String rfcE){
        Connection con = Elemento.odbc();
        Statement stmt = fact.stmtLectura(con);
        ResultSet rs;
        serie.setText("");
        serieNC.setText("");
        serieRD.setText("");
        folio.setText("");
        folioNC.setText("");
        folioRD.setText("");
        plantilla.setText("");
        plantillaNC.setText("");
        plantillaRD.setText("");
        
        try{
            rs = stmt.executeQuery("SELECT * FROM Folios f, Comprobantes tc WHERE rfc like \'"+rfcE+"\' AND f.idComprobante = tc.idComprobante");
            while(rs.next()){
                String nom = rs.getString("nombre");
                if(nom.equalsIgnoreCase("Factura")){
                    serie.setText(rs.getString("serie"));
                    folio.setText(rs.getString("ultimo_folio"));
                    plantilla.setText(rs.getString("plantilla"));
                }
                if(nom.equalsIgnoreCase("Nota de Credito")){
                    serieNC.setText(rs.getString("serie"));
                    folioNC.setText(rs.getString("ultimo_folio"));
                    plantillaNC.setText(rs.getString("plantilla"));
                }
                if(nom.equalsIgnoreCase("Recibo de Donativos")){
                    serieRD.setText(rs.getString("serie"));
                    folioRD.setText(rs.getString("ultimo_folio"));
                    plantillaRD.setText(rs.getString("plantilla"));
                }
            }
            rs.close();
        }catch(SQLException ex){
            ex.printStackTrace();
            Elemento.log.error("Excepcion al consultar la informacion de los folios y series: " + ex.getMessage(),ex);
        }
    }
    
    private ResultSet consultar(Statement stmt, String dato, String valor) throws Exception{
        ResultSet rs;
        rs = stmt.executeQuery("SELECT * FROM Cuentas WHERE " + dato + " like \'" + valor+"\'");
        return rs;
    }
    
    private ResultSet consultar(Statement stmt) throws Exception{
        ResultSet rs;
        if(true)
        rs = stmt.executeQuery("SELECT * FROM Cuentas");
        return rs;
    }
    
    private Boolean consultarProd(){
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
            Elemento.log.error("Excepcion al consultar la tabla Produccion: " + ex.getMessage(),ex);
            return false;
        }
    }
    
    public String getNoCertificado(){
        return certificado.getText().trim();
    }
    
    public String getRfc(){
        if(rfce != null || !rfce.equals("")){
            return rfc.getSelectedItem().toString();
        }else{
            return rfce;
        }
    }
    
    public String getRegimen(){
        return regimenText.getText().trim();
    }
    
    public String getLugarExp(){
        return lugarExpedicion.getText().trim();
    }
    
    public boolean isProduccion(){
        return prod;
    }
    
    public String getLogo(){
        return logoPath.getText().trim();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    /*
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
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        serie = new javax.swing.JTextField();
        folio = new javax.swing.JTextField();
        plantilla = new javax.swing.JTextField();
        exaFact = new javax.swing.JButton();
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
        tipoComprobante = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        agregarComprobante = new javax.swing.JButton();

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

        actualizar.setText("Actualizar Informacion");
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
                "RFC", "No. Certificado", "Regimen Fiscal", "Creditos Activados", "Creditos Restantes", "Creditos Usados"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        datos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                datosMouseClicked(evt);
            }
        });
        datos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                datosFocusLost(evt);
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

        jLabel8.setText("Tipos de Comprobantes a Emitir:");

        jLabel9.setText("Serie:");

        jLabel10.setText("Folio Actual:");

        jLabel11.setText("Plantilla:");

        serie.setNextFocusableComponent(folio);

        folio.setNextFocusableComponent(plantilla);

        exaFact.setText("...");
        exaFact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exaFactActionPerformed(evt);
            }
        });

        jLabel7.setText("Lugar Exp.");

        lugarExpedicion.setNextFocusableComponent(serie);

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

        tipoComprobante.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tipos de comprobantes", "Factura", "Nota de Credito", "Recibo de Donativo", "Recibo de Notarios" }));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable1);

        agregarComprobante.setText("Agregar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(actualizar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(agregarCreditos)
                            .addGap(18, 18, 18)
                            .addComponent(infoSucursal)
                            .addGap(18, 18, 18)
                            .addComponent(infoEmisor)
                            .addGap(18, 18, 18)
                            .addComponent(borrar))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 997, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
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
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(logoPath, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(examinar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                        .addComponent(esSucursalCheck))
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(tipoComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(19, 19, 19)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel9)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(serie, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel10)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(folio, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel11)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(plantilla, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(exaFact, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(agregarComprobante)
                                        .addGap(30, 30, 30))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 537, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8))))
                        .addGap(6, 6, 6))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(produccion)
                            .addComponent(agregarEmisor))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(serie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10)
                                    .addComponent(folio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(plantilla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(exaFact)
                                    .addComponent(agregarComprobante))
                                .addGap(103, 103, 103))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel4)
                                    .addComponent(rfc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tipoComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(certificado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)
                                    .addComponent(esSucursalCheck))
                                .addGap(9, 9, 9)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(regimenText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2))
                                .addGap(8, 8, 8)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(logoPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1)
                                    .addComponent(examinar))
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(lugarExpedicion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(agregar))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(actualizar)
                    .addComponent(infoEmisor)
                    .addComponent(borrar)
                    .addComponent(agregarCreditos)
                    .addComponent(infoSucursal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void examinarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_examinarActionPerformed
        logoPath.setText(seleccionarArchivo("Imagen de Mapa de Bits .JPG","jpg"));
    }//GEN-LAST:event_examinarActionPerformed
    
    private String seleccionarArchivo(String nombre,String tipo){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileNameExtensionFilter ext = new FileNameExtensionFilter (nombre,tipo);
        chooser.setFileFilter(ext);
        if(nombre.contains("Plantilla"))
            chooser.setCurrentDirectory(new File(Elemento.pathPlantillas));
        else
            chooser.setCurrentDirectory(new File(Elemento.unidad+":\\"));
        chooser.setVisible(true);
        int opc = chooser.showOpenDialog(null);
        String path = "";

        switch (opc){
            case JFileChooser.APPROVE_OPTION:
            // path to watch
            path = chooser.getSelectedFile().getAbsolutePath();
            chooser.setVisible(false);
            break;
            case JFileChooser.ERROR_OPTION:
            JOptionPane.showMessageDialog(null,"Ocurrio un error, intentelo de nuevo");
            chooser.setVisible(false);
            case JFileChooser.CANCEL_OPTION:
            chooser.setVisible(true);
        }
        
        return path;
    }
    
    private void habilitarBotones(boolean var){
        actualizar.setEnabled(var);
        borrar.setEnabled(var);
        infoEmisor.setEnabled(var);
        infoSucursal.setEnabled(var);
        agregarCreditos.setEnabled(var);
    }
    
    private void datosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_datosMouseClicked
        TableModel model = datos.getModel();
        String rfct = model.getValueAt(datos.getSelectedRow(),0).toString().trim();
        String cert,logot,regi,ser;
        boolean esSuc;
        habilitarBotones(true);
        
        try {
            Connection con = Elemento.odbc();
            Statement stmt = fact.stmtLectura(con);

            ResultSet rs = this.consultar(stmt,"rfc",rfct);
            
            if(rs.next()){
                rfc.setSelectedItem(rfct);
                cert = rs.getString("nocertificado");
                logot = rs.getString("logo");
                regi = rs.getString("regimenFiscal");
                ser = rs.getString("lugarExpedicion");
                esSuc = rs.getBoolean("esSucursal");
                
                certificado.setText(cert);
                logoPath.setText(logot);
                regimenText.setText(regi);
                lugarExpedicion.setText(ser);
                esSucursalCheck.setSelected(esSuc);
                facturas.setSelected(rs.getBoolean("facturas"));
                notasCredito.setSelected(rs.getBoolean("notasCredito"));
                donativos.setSelected(rs.getBoolean("recibosDonativos"));
                
                bloquearInfoFolios();
                consultarInfoFolios(rfct);
                
                rfcActual = rfct;
                certActual = cert;
                logoActual = logot;
                regimenActual = regi;
                lugarActual = ser;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al consultar la credencial del RFC: " + rfct,ex);
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
            Elemento.log.error("No se pudo desencriptar el token:",e);
        } 
        return devuelve; 
    } 
    
    private int verificarToken(){
        Calendar cal = GregorianCalendar.getInstance();
        Date x = cal.getTime();
        rfcVendedor = JOptionPane.showInputDialog("Ingrese su RFC:").toUpperCase();
        
        if(rfcVendedor.equals("32288722466")){
            rfcVendedor = "Administrador";
            return Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad de creditos a activar: "));
        }
        
        String token = JOptionPane.showInputDialog(null,"Ingresa el token para activar creditos:");
        String dato = decrypt(token);
        
        if(!dato.isEmpty()){
            try {
                String [] tok = dato.split(",");
                //RFCVendedor,RFCEmisor,Creditos,Fecha
                String rfcV = tok[0].trim();
                String rfcEmisor = tok[1].trim();
                String rfcR = rfc.getSelectedItem().toString().trim();
                int creditos = Integer.parseInt(tok[2].trim());
                SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
                Date y = formato.parse(tok[3].trim());
                
                if(rfcVendedor.equals(rfcV) && rfcEmisor.equals(rfcR)){
                    if(x.getDate() == y.getDate() && x.getMonth() == y.getMonth() && x.getYear() == y.getYear()){
                        return creditos;
                    }else{
                        Elemento.log.warn("El token ha caducado, pongase en contacto con el administrador");
                        JOptionPane.showMessageDialog(null, "El token ha caducado, pongase en contacto con el administrador");
                        return 0;
                    }
                }else{
                    Elemento.log.warn("El token no es valido, contacte con el administrador");
                    JOptionPane.showMessageDialog(null, "El token no es valido, contacte con el administrador");
                    return 0;
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                Elemento.log.error("Excepcion al parsear la fecha del token",ex);
                return 0;
            }
        }else{
            return 0;
        }
    }
    
    private void agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarActionPerformed
        final int creditos = verificarToken();
        if(creditos > 0){
            try {
                // TODO add your handling code here:
                String sf = serie.getText().trim();
                String ff = folio.getText().trim();
                String pf = plantilla.getText().trim();

                String sn = serieNC.getText().trim();
                String fn = folioNC.getText().trim();
                String pn = plantillaNC.getText().trim();

                String sr = serieRD.getText().trim();
                String fr = folioRD.getText().trim();
                String pr = plantillaRD.getText().trim();

                Connection con = Elemento.odbc();
                Statement stmt = fact.stmtEscritura(con);
                final String cert = certificado.getText().trim();
                final String rfcE = rfc.getSelectedItem().toString().trim();
                final String nombre = nombres.get(rfc.getSelectedIndex()-1);
                if(suc != null)
                    idSucursal = suc.getIdSucursal();
                else
                    idSucursal = 0;
                
                ResultSet rs = stmt.executeQuery("SELECT rfc,nocertificado FROM Cuentas WHERE rfc like \'"+rfcE+"\'");
                if(!rs.next()){
                    if((sf.isEmpty() || ff.isEmpty() || pf.isEmpty()) && (sn.isEmpty() || fn.isEmpty() || pn.isEmpty()) && (sr.isEmpty() || fr.isEmpty() || pr.isEmpty())){
                        JOptionPane.showMessageDialog(null, "Falta agregar la informacion de los folios y series");
                    }else{ 
                        stmt.executeUpdate("INSERT INTO Cuentas (rfc,nocertificado,logo,regimenFiscal,lugarExpedicion,facturas,notasCredito,recibosDonativos,cantDecimales,creditosActivados,creditosRestantes,creditosUsados,esSucursal,idSucursal)"
                                + "VALUES (\'"+rfcE+"\', \'"+certificado.getText().trim()+"\', \'"+logoPath.getText()+"\', \'" 
                                + regimenText.getText().trim()+"\', \'"+lugarExpedicion.getText()+"\',"+facturas.isSelected()+","+notasCredito.isSelected()+","+donativos.isSelected()+","
                                + 2+"," + creditos+ "," + creditos + "," + 0 + ","+esSucursalCheck.isSelected()+","+idSucursal+")");

                        agregarInfoFolios(con);
                        stmt.close();
                        con.close();
                        llenarFormulario(produccion.isSelected());
                        final Emisores emis = new Emisores(true,rfcE);
                        new Thread(){
                            @Override
                            public void run(){
                                util.enviarEmail("esquerodriguez@gmail.com,activacion@feimpresoresdigitales.com,gorenajc2.3@gmail.com", 
                                        "SE AGREGO LA SIGUIENTE CUENTA\r\n"
                                        + "*Vendedor: "+rfcVendedor.toUpperCase()+"\r\n"
                                        + "*Certificado: "+cert+"\r\n\r\n"
                                        + "***DATOS DEL EMISOR***\r\n\r\n"
                                        + "NOMBRE2: "+nombre+"\r\n"
                                        + "RFC2: "+rfcE+"\r\n"
                                        + "CALLE2: "+emis.calle.getText().trim()+"\r\n"
                                        + "NOEXTERIOR2: "+emis.noExterior.getText().trim()+"\r\n"
                                        + "NOINTERIOR2: "+emis.noInterior.getText().trim()+"\r\n"
                                        + "COLONIA2: "+emis.colonia.getText().trim()+"\r\n"
                                        + "LOCALIDAD2: "+emis.localidad.getText().trim()+"\r\n"
                                        + "MUNICIPIO2: "+emis.municipio.getText().trim()+"\r\n"
                                        + "ESTADO2: "+emis.estado.getSelectedItem().toString()+"\r\n"
                                        + "PAIS2: "+emis.pais.getSelectedItem().toString()+"\r\n"
                                        + "CP2: "+emis.cp.getText().trim()+"\r\n\r\n"
                                        + "*Folios Activados: "+creditos+"\r\n");
                            }
                        }.start();
                    }
                }else{
                    util.print("No se puede agregar el RFC: " + rfcE + " porque ya existe.");
                }
            }catch (Exception ex) {
                ex.printStackTrace();
                Elemento.log.error("Excepcion al insertar cuenta en la tabla Cuentas: " + ex.getMessage(),ex);
            }
        }else{
            //JOptionPane.showMessageDialog(null, "El token no es valido, verificalo con el administrador");
        }
    }//GEN-LAST:event_agregarActionPerformed

    private void agregarInfoFolios(Connection con) throws SQLException{
        Statement stmt = fact.stmtEscritura(con);
        String rf = this.rfc.getSelectedItem().toString().trim();
        
        if(facturas.isSelected()){
            stmt.executeUpdate(getQueryFoliosInsert(1,rf,folio.getText().trim(),serie.getText().trim(),plantilla.getText().trim()));
        }
        if(notasCredito.isSelected()){
            stmt.executeUpdate(getQueryFoliosInsert(2,rf,this.folioNC.getText().trim(),serieNC.getText().trim(),plantillaNC.getText().trim()));
        }
        if(donativos.isSelected()){
            stmt.executeUpdate(getQueryFoliosInsert(3,rf,this.folioRD.getText().trim(),serieRD.getText().trim(),plantillaRD.getText().trim()));
        }
        stmt.close();
        con.close();
    }
    
    private String getQueryFoliosInsert(int idComprobante,String rfc,String ultimoFolio,String serie,String plantilla){
        return "INSERT INTO Folios (idComprobante,rfc,ultimo_folio,serie,plantilla) "
                    + "VALUES ("+ idComprobante + ",\'" + rfc + "\'," + ultimoFolio +",\'"
                    + serie + "\',\'" + plantilla +"\')";
    }
    
    private void actualizarInfoFolios(Connection conX) throws SQLException{
        Connection con = conX;
        
        Statement stmtFact = fact.stmtEscritura(con);
        Statement stmtNC = fact.stmtEscritura(con);
        Statement stmtRD = fact.stmtEscritura(con);
        
        if(facturas.isSelected()){
        stmtFact.executeUpdate("UPDATE Folios SET ultimo_folio=\'"+folio.getText().trim()+"\', serie=\'"+serie.getText().trim()+"\', "
                + "plantilla=\'"+plantilla.getText().trim()+"\' WHERE idComprobante = 1 AND rfc like \'"+rfc.getSelectedItem().toString().trim()+"\'");
        }
        if(notasCredito.isSelected()){
        stmtNC.executeUpdate("UPDATE Folios SET ultimo_folio=\'"+folioNC.getText().trim()+"\', serie=\'"+serieNC.getText().trim()+"\', "
                + "plantilla=\'"+plantillaNC.getText().trim()+"\' WHERE idComprobante = 2  AND rfc like \'"+rfc.getSelectedItem().toString().trim()+"\'");
        }
        if(donativos.isSelected()){
        stmtRD.executeUpdate("UPDATE Folios SET ultimo_folio=\'"+folioRD.getText().trim()+"\', serie=\'"+serieRD.getText().trim()+"\', "
                + "plantilla=\'"+plantillaRD.getText().trim()+"\' WHERE idComprobante = 3  AND rfc like \'"+rfc.getSelectedItem().toString().trim()+"\'");
        }
        stmtFact.close();
        stmtNC.close();
        stmtRD.close();
        con.close();
    }
    
    private void actualizarComboEmisores(final Emisores emis){
        emis.setVisible(true);
        
        Thread th = new Thread(){
            @Override
            public void run(){
                while(true){
                    if(!emis.isDisplayable()){
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
        Boolean facts,notas,dona;
        try{
            con = Elemento.odbc();
            stmt = fact.stmtEscritura(con);

            noCertificado = getNoCertificado();
            rfce = getRfc();
            logo = getLogo();
            regimenFiscal = getRegimen();
            lugarExp = getLugarExp();
            
            facts = facturas.isSelected();
            notas = notasCredito.isSelected();
            dona = donativos.isSelected();
            
            stmt.executeUpdate("UPDATE Cuentas SET rfc=\'"+rfce+"\', nocertificado=\'"+noCertificado+"\', logo=\'"+logo+"\', regimenFiscal=\'"+regimenFiscal+"\', lugarExpedicion=\'"+lugarExp+"\', "
                    + "facturas="+facts+", notasCredito="+notas+", recibosDonativos="+dona
                    + " WHERE rfc like \'" + rfcActual + "\' AND nocertificado like \'" + certActual + "\'");
            
            actualizarInfoFolios(con);

            util.print("Cuenta actualizada correctamente!!");
            llenarFormulario(true);
            stmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
            Elemento.log.info("Excepcion al actualizar la cuenta productiva: " + e.getMessage());
        }
    }//GEN-LAST:event_actualizarActionPerformed

    private void produccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_produccionActionPerformed
        // TODO add your handling code here:
        Connection con;
        Statement stmt;
        try{
            con = Elemento.odbc();
            stmt = fact.stmtEscritura(con);
            boolean produ = produccion.isSelected();
            stmt.executeUpdate("UPDATE Produccion SET produccion = "+produ);

            stmt.close();
            con.close();

            llenarFormulario(produ);
        }catch(Exception e){
            e.printStackTrace();
            Elemento.log.error("Excepcion al leer la tabla Produccion: " + e.getMessage(),e);
        }
    }//GEN-LAST:event_produccionActionPerformed

    private void agregarEmisorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarEmisorActionPerformed
        // TODO add your handling code here:
        Emisores emis = new Emisores();
        actualizarComboEmisores(emis);
    }//GEN-LAST:event_agregarEmisorActionPerformed
    
    private void exaFactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exaFactActionPerformed
        // TODO add your handling code here:
        plantilla.setText(seleccionarArchivo("Plantilla de Factura .jasper","jasper"));
    }//GEN-LAST:event_exaFactActionPerformed

    private void borrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarActionPerformed
        // TODO add your handling code here:
        if(JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar esta cuenta?", "Borrar",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            Connection con = Elemento.odbc();
            Statement stmt = fact.stmtEscritura(con);
            DefaultTableModel model = (DefaultTableModel)datos.getModel();
            String rfcx = model.getValueAt(datos.getSelectedRow(), 0).toString().trim();
            try {
                stmt.executeUpdate("DELETE FROM Cuentas WHERE rfc like \'"+rfcx+"\'");
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                Elemento.log.error("Excepcion al eliminar una cuenta: " + ex.getMessage(),ex);
            }
            llenarFormulario(true);
        }
    }//GEN-LAST:event_borrarActionPerformed

    private void infoEmisorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoEmisorActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel)datos.getModel();
        String rfct = model.getValueAt(datos.getSelectedRow(), 0).toString().trim();
        Emisores emis = new Emisores(true,rfct);
        actualizarComboEmisores(emis);
    }//GEN-LAST:event_infoEmisorActionPerformed

    private void agregarCreditosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarCreditosActionPerformed
        // TODO add your handling code here:
        int creditos = verificarToken();
        
        if(creditos > 0){
            DefaultTableModel model = (DefaultTableModel)datos.getModel();
            String rfcE = model.getValueAt(datos.getSelectedRow(), 0).toString().trim();
            String cert = model.getValueAt(datos.getSelectedRow(), 1).toString().trim();

//            String cr = JOptionPane.showInputDialog(null, "Ingrese la cantidad de creditos a agregar:", "Agregar Creditos",JOptionPane.INFORMATION_MESSAGE);
            
              this.agregarCreditos(creditos, rfcE, cert);

            this.llenarFormulario(true);
        }else{
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
        if(sonSucursales.get(datos.getSelectedRow())){
            new Sucursales(true,idSucursales.get(datos.getSelectedRow()),null).setVisible(true);
        }
    }//GEN-LAST:event_infoSucursalActionPerformed

    private void esSucursalCheckMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_esSucursalCheckMouseClicked
        // TODO add your handling code here:
        if(esSucursalCheck.isSelected()){
            int idEmisor = obtenerIDEmisor(rfc.getSelectedItem().toString().trim(),Elemento.odbc());
            suc = new Sucursales(false,null,idEmisor);
            suc.setVisible(true);
        }
    }//GEN-LAST:event_esSucursalCheckMouseClicked

    private void agregarCreditos(final int creditos,final String rfcE,final String cert){
        try {
            Connection con = Elemento.odbc();
            Statement stmt = fact.stmtEscritura(con);
            Statement stmt2 = fact.stmtLectura(con);
            final String nombre = nombres.get(rfc.getSelectedIndex()-1);
            ResultSet rs = stmt2.executeQuery("SELECT creditosActivados,creditosRestantes FROM Cuentas WHERE rfc like \'"+rfcE+"\' AND nocertificado like \'"+cert+"\'");
            
            if(rs.next()){
                int rest = creditos + rs.getInt("creditosRestantes"); 
                stmt.executeUpdate("UPDATE Cuentas SET creditosActivados = \'"+creditos+"\', creditosRestantes = \'"+rest+"\' WHERE rfc like \'"+rfcE+"\' AND nocertificado like \'"+cert+"\'");
                Elemento.log.info("Se agregaron " + creditos + " creditos a la cuenta " + rfcE);
                final Emisores emis = new Emisores(true,rfcE);
                new Thread(){
                    @Override
                    public void run(){
                        util.enviarEmail("esquerodriguez@gmail.com,activacion@feimpresoresdigitales.com,gorenajc2.3@gmail.com", 
                            "SE AGREGARON CREDITOS A LA SIGUIENTE CUENTA\r\n"
                            + "*Vendedor: "+rfcVendedor.toUpperCase()+"\r\n"
                            + "*Certificado: "+cert+"\r\n\r\n"
                            + "***DATOS DEL EMISOR***\r\n\r\n"
                            + "NOMBRE2: "+nombre+"\r\n"
                            + "RFC2: "+rfcE+"\r\n"
                            + "CALLE2: "+emis.calle.getText().trim()+"\r\n"
                            + "NOEXTERIOR2: "+emis.noExterior.getText().trim()+"\r\n"
                            + "NOINTERIOR2: "+emis.noInterior.getText().trim()+"\r\n"
                            + "COLONIA2: "+emis.colonia.getText().trim()+"\r\n"
                            + "LOCALIDAD2: "+emis.localidad.getText().trim()+"\r\n"
                            + "MUNICIPIO2: "+emis.municipio.getText().trim()+"\r\n"
                            + "ESTADO2: "+emis.estado.getSelectedItem().toString()+"\r\n"
                            + "PAIS2: "+emis.pais.getSelectedItem().toString()+"\r\n"
                            + "CP2: "+emis.cp.getText().trim()+"\r\n\r\n"
                            + "*Folios Activados: "+creditos+"\r\n");
                }
                    }.start();
            }
            
            rs.close();
            stmt2.close();
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al agregar creditos: " + ex.getMessage(),ex);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    /*
    public static void main(String args[]) {
        /* Set the Windows look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Windows (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
    /*
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Configurar1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        /*
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new Configurar1().setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Elemento.log.error("Excepcion al cargar la clase Configurar: " + ex.getMessage(),ex);
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
    private javax.swing.JTextField certificado;
    private javax.swing.JTable datos;
    private javax.swing.JCheckBox esSucursalCheck;
    private javax.swing.JButton exaFact;
    private javax.swing.JButton examinar;
    private javax.swing.JTextField folio;
    private javax.swing.JButton infoEmisor;
    private javax.swing.JButton infoSucursal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JTable jTable1;
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
            rs = stmt.executeQuery("SELECT id FROM Emisores WHERE rfc like \'"+rfcE+"\'");
            if(rs.next()){
                idEmisor = rs.getInt("id");
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idEmisor;
    }
    */
}
