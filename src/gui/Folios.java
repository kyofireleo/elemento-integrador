/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Folios.java
 *
 * Created on 13/11/2011, 08:13:44 PM
 */
package gui;

import com.impresoresdigitales.verificar.cfdi.TestValidator;
import elemento.ConnectionFactory;
import elemento.Elemento;
import elemento.Exe;
import elemento.Listener;
import elemento.Stylezer;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import nominas.configuracion.NominaCorreos;
import utils.Utils;
import pagos.Documento;
import pagos.Pago;
import pagos.RecibosPagos;
import utils.cfdi.Comprobante;

/**
 *
 * @author Abe
 */
public class Folios extends javax.swing.JFrame {

    /**
     * Creates new form Folios
     */
    DefaultTableModel model = null;
    Connection con = null;
    Utils util = new Utils(Elemento.log);
    ConnectionFactory factory = new ConnectionFactory();
    String nombreXml;
    List<String> rfcEmisor = new ArrayList(), uuid = new ArrayList();
    List<Long> transId = new ArrayList();
    boolean activar;
    public List<String> cfdisAsoc;
    public List<Documento> docsPagar;
    public static Boolean isCancelado;
    //public static boolean finishWhile;
    private Factura_View fv;
    private int orderColumn = -1;
    private String tipoOrder = "ASC"; //variable para el tipo de ordenamiento de los folios
    
    RecibosPagos rp;
    Pago p;

    public Folios() {
        initComponents();
        setLocationRelativeTo(null);
        model = (DefaultTableModel) folios.getModel();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String order = getOrdenamiento();
            con = conexion();
            if (con != null) {
                Statement stmt = factory.stmtLectura(con);
                ResultSet rs = stmt.executeQuery("SELECT * FROM Facturas" + order);
                Object[] fila = new Object[7];
                rfcEmisor.clear();
                uuid.clear();
                transId.clear();
                
                activar = true;
                
                while (rs.next()) {
                    String rfcE = rs.getString("rfcEmisor");
                    fila[0] = ((String) (rs.getString("serie") + "_" + rs.getString("folio")));
                    fila[1] = ((String) rfcE);
                    fila[2] = ((String) rs.getString("rfc"));
                    fila[3] = ((String) rs.getString("status"));
                    fila[4] = ((String) sdf.format(new Date(rs.getTimestamp("fecha").getTime())));
                    fila[5] = ((String) rs.getString("total"));
                    fila[6] = ((Boolean) rs.getBoolean("timbrado"));

                    rfcEmisor.add(rs.getString("rfcEmisor"));
                    transId.add(rs.getLong("transId"));
                    uuid.add(rs.getString("UUID"));

                    model.addRow(fila);
                    fila = new Object[7];
                }

                rs.close();
                stmt.close();
                con.close();
            }
            setTableHeaderListener();
        } catch (Exception e) {
            Elemento.log.error("Excepcion al consultar los Folios Registrados: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public Folios(String nada) {
        con = conexion();
        setTableHeaderListener();
    }
    
    public Folios(String rfcEmi, String rfcReceptor, Factura_View fv, RecibosPagos rp){
        initComponents();
        setLocationRelativeTo(null);
        model = (DefaultTableModel) folios.getModel();
        //finishWhile = true;
        if(fv != null){
            this.fv = fv;
        }
        if(rp != null){
            this.rp = rp;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String order = getOrdenamiento();
            con = conexion();
            if (con != null) {
                Statement stmt = factory.stmtLectura(con);
                ResultSet rs = stmt.executeQuery("SELECT * FROM Facturas WHERE rfcEmisor = '"+rfcEmi+"' AND rfc = '"+rfcReceptor+"' AND idComprobante <> 4 AND timbrado = True" + order);
                Object[] fila = new Object[7];
                rfcEmisor.clear();
                uuid.clear();
                transId.clear();

                //Deshabilitar los controles
                activar = false;
                consultar.setEnabled(false);
                //verificar.setEnabled(false);
                reporte.setEnabled(false);
                folioTxt.setEnabled(false);
                noTimbrados.setEnabled(false);
                porEmisores.setEnabled(false);

                verificar.setText("Asociar");

                while (rs.next()) {
                    String rfcE = rs.getString("rfcEmisor");
                    fila[0] = ((String) (rs.getString("serie") + "_" + rs.getString("folio")));
                    fila[1] = ((String) rfcE);
                    fila[2] = ((String) rs.getString("rfc"));
                    fila[3] = ((String) rs.getString("status"));
                    fila[4] = ((String) sdf.format(new Date(rs.getTimestamp("fecha").getTime())));
                    fila[5] = ((String) rs.getString("total"));
                    fila[6] = ((Boolean) rs.getBoolean("timbrado"));

                    rfcEmisor.add(rs.getString("rfcEmisor"));
                    transId.add(rs.getLong("transId"));
                    uuid.add(rs.getString("UUID"));

                    model.addRow(fila);
                    fila = new Object[7];
                }

                rs.close();
                stmt.close();
                con.close();
            }
            setTableHeaderListener();
        } catch (SQLException e) {
            Elemento.log.error("Excepcion al consultar los Folios Registrados: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }
    
    public Folios(String rfcEmi, String rfcReceptor, RecibosPagos rp, Pago p){
        this.rp = rp;
        this.p = p;
        initComponents();
        setLocationRelativeTo(null);
        model = (DefaultTableModel) folios.getModel();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            con = conexion();
            String order = getOrdenamiento();
            if (con != null) {
                Statement stmt = factory.stmtLectura(con);
                ResultSet rs = stmt.executeQuery("SELECT * FROM Facturas WHERE rfcEmisor = '"+rfcEmi+"' AND rfc = '"+rfcReceptor+"' AND idComprobante <> 4 AND timbrado = True" + order);
                Object[] fila = new Object[7];
                rfcEmisor.clear();
                uuid.clear();
                transId.clear();

                //Deshabilitar los controles
                activar = false;
                consultar.setEnabled(false);
                //verificar.setEnabled(false);
                reporte.setEnabled(false);
                folioTxt.setEnabled(false);
                noTimbrados.setEnabled(false);
                porEmisores.setEnabled(false);

                verificar.setText("Asociar");

                while (rs.next()) {
                    String rfcE = rs.getString("rfcEmisor");
                    fila[0] = ((String) (rs.getString("serie") + "_" + rs.getString("folio")));
                    fila[1] = ((String) rfcE);
                    fila[2] = ((String) rs.getString("rfc"));
                    fila[3] = ((String) rs.getString("status"));
                    fila[4] = ((String) sdf.format(new Date(rs.getTimestamp("fecha").getTime())));
                    fila[5] = ((String) rs.getString("total"));
                    fila[6] = ((Boolean) rs.getBoolean("timbrado"));

                    rfcEmisor.add(rs.getString("rfcEmisor"));
                    transId.add(rs.getLong("transId"));
                    uuid.add(rs.getString("UUID"));

                    model.addRow(fila);
                    fila = new Object[7];
                }

                rs.close();
                stmt.close();
                con.close();
            }
            setTableHeaderListener();
            
        } catch (SQLException e) {
            Elemento.log.error("Excepcion al consultar los Folios Registrados: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }
    
    private void setTableHeaderListener(){
        if(folios != null){
            folios.getTableHeader().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(orderColumn == folios.columnAtPoint(e.getPoint())){
                        tipoOrder = tipoOrder.equals("ASC") ? "DESC" : "ASC";
                    }else{
                        tipoOrder = "ASC";
                    }
                    orderColumn = folios.columnAtPoint(e.getPoint());
                    consultarFacturas();
                }
            });
        }
    }
    
    private String getOrdenamiento(){
        String query;
        switch(this.orderColumn){
            case 0:
                query = " ORDER BY serie " + this.tipoOrder + ", folio DESC, rfcEmisor ASC";
            break;
            
            case 1:
                query = " ORDER BY rfcEmisor " + this.tipoOrder + ", serie ASC, folio DESC";
            break;
            
            case 2:
                query = " ORDER BY rfc " + this.tipoOrder + ", rfcEmisor ASC, serie ASC, folio DESC";
            break;
            
            case 3:
                query = " ORDER BY status " + this.tipoOrder + ", rfcEmisor ASC, serie ASC, folio DESC";
            break;
            
            case 4:
                query = " ORDER BY fecha " + this.tipoOrder + ", rfcEmisor ASC, serie ASC, folio DESC";
            break;
            
            case 5:
                query = " ORDER BY total " + this.tipoOrder + ", rfcEmisor ASC, serie ASC, folio DESC";
            break;
            
            case 6:
                query = " ORDER BY timbrado " + this.tipoOrder + ", rfcEmisor ASC, serie ASC, folio DESC";
            break;
            
            default:
                query = " ORDER BY fecha DESC, rfcEmisor ASC, serie ASC, folio DESC";
            break;
        }
        
        return query;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        folios = new javax.swing.JTable();
        consultar = new javax.swing.JButton();
        noTimbrados = new javax.swing.JCheckBox();
        verPdf = new javax.swing.JButton();
        enviar = new javax.swing.JButton();
        cancelar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        folioTxt = new javax.swing.JTextField();
        reporte = new javax.swing.JButton();
        timbrar = new javax.swing.JButton();
        verificar = new javax.swing.JButton();
        porEmisores = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Folios Registrados");

        folios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Folio", "RFC Emisor", "RFC Receptor", "Estatus", "Fecha de Registro", "Total", "Timbrado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
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
        folios.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        folios.setColumnSelectionAllowed(true);
        folios.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        folios.getTableHeader().setReorderingAllowed(false);
        folios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                foliosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(folios);
        folios.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        consultar.setText("Consultar");
        consultar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                consultarActionPerformed(evt);
            }
        });

        noTimbrados.setText("No Timbrados");

        verPdf.setText("Ver PDF");
        verPdf.setEnabled(false);
        verPdf.setName("verPdf"); // NOI18N
        verPdf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verPdfActionPerformed(evt);
            }
        });

        enviar.setText("Enviar");
        enviar.setEnabled(false);
        enviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enviarActionPerformed(evt);
            }
        });

        cancelar.setText("Cancelar CFDi");
        cancelar.setEnabled(false);
        cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarActionPerformed(evt);
            }
        });

        jLabel2.setText("Buscar por RFC o Folio");

        folioTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                folioTxtKeyPressed(evt);
            }
        });

        reporte.setText("Reporte");
        reporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reporteActionPerformed(evt);
            }
        });

        timbrar.setText("Timbrar");
        timbrar.setEnabled(false);
        timbrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timbrarActionPerformed(evt);
            }
        });

        verificar.setText("Verificar");
        verificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verificarActionPerformed(evt);
            }
        });

        porEmisores.setText("Por Emisores");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(folioTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(noTimbrados)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(porEmisores)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(consultar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(verificar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(reporte)
                        .addGap(18, 18, 18)
                        .addComponent(timbrar)
                        .addGap(18, 18, 18)
                        .addComponent(enviar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(verPdf)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancelar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(consultar)
                    .addComponent(noTimbrados)
                    .addComponent(verPdf)
                    .addComponent(enviar)
                    .addComponent(cancelar)
                    .addComponent(jLabel2)
                    .addComponent(folioTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reporte)
                    .addComponent(timbrar)
                    .addComponent(porEmisores)
                    .addComponent(verificar))
                .addGap(11, 11, 11)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void consultarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_consultarActionPerformed
        this.orderColumn = -1;
        consultarFacturas();
    }//GEN-LAST:event_consultarActionPerformed

    private boolean isNumeric(String x) {
        try {
            Double d = Double.parseDouble(x);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private void consultarFacturas() {
        // TODO add your handling code here:
        String tim;
        model.setRowCount(0);
        String folio = folioTxt.getText().trim();
        Boolean timbra = (Boolean) noTimbrados.isSelected();
        String rfcDato;

        if (porEmisores.isSelected()) {
            rfcDato = "rfcEmisor";
        } else {
            rfcDato = "rfc";
        }

        if (!folio.isEmpty()) {
            if (isNumeric(folio)) {
                tim = "WHERE folio = " + folio;
            } else {
                tim = "WHERE " + rfcDato + " like \'%" + folio + "%\'";
            }
        } else {
            tim = "";
        }

        if (timbra) {
            if (tim.isEmpty()) {
                tim = "WHERE timbrado=" + !timbra;
            } else {
                tim += " AND timbrado=" + !timbra;
            }
        }

//        if(timbra || !(folio.isEmpty())) {
//            if(isNumeric(folio)){
//                tim = " WHERE folio = " + folio + " AND timbrado="+!timbra;
//            }else{
//                tim = " WHERE rfc like \'%"+folio+"%\' AND timbrado="+!timbra;
//            }
//        }else{
//            if(isNumeric(folio)){
//                tim = " WHERE folio = " + folio;
//            }else{
//                tim = " WHERE rfc like \'%"+folio+"%\'";
//            }
//        }
        Object[] fila = new Object[7];
        String order = getOrdenamiento();
        String query = "SELECT * FROM Facturas " + tim + order;
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            con = conexion();
            Statement stmt;
            ResultSet rs;
            if (con != null) {
                stmt = factory.stmtLectura(con);
                rs = stmt.executeQuery(query);
                rfcEmisor.clear();
                uuid.clear();
                transId.clear();

                while (rs.next()) {
                    String rfcE = rs.getString("rfcEmisor");
                    fila[0] = ((String) (rs.getString("serie") + "_" + rs.getString("folio")));
                    fila[1] = ((String) rfcE);
                    fila[2] = ((String) rs.getString("rfc"));
                    fila[3] = ((String) rs.getString("status"));
                    fila[4] = ((String) sdf.format(rs.getDate("fecha")));
                    fila[5] = ((String) rs.getString("total"));
                    fila[6] = ((Boolean) rs.getBoolean("timbrado"));

                    rfcEmisor.add(rfcE);
                    transId.add(0l);
                    uuid.add(rs.getString("UUID"));

                    model.addRow(fila);
                    fila = new Object[7];
                }

                rs.close();
                stmt.close();
                con.close();
            }
        } catch (SQLException e) {
            Elemento.log.error("Excepcion al usar el ODBC: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void foliosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_foliosMouseClicked
        int row = folios.getSelectedRow();
        Boolean tim = (Boolean) model.getValueAt(row, 6);
        if(activar){
            if (!tim) {
                enviar.setEnabled(false);
                verPdf.setEnabled(false);
                cancelar.setEnabled(false);
                timbrar.setEnabled(true);
            } else if (model.getValueAt(row, 3).toString().equalsIgnoreCase("PREFACTURA")) {
                enviar.setEnabled(false);
                verPdf.setEnabled(true);
                cancelar.setEnabled(false);
                timbrar.setEnabled(true);
            } else {
                enviar.setEnabled(true);
                verPdf.setEnabled(true);
                cancelar.setEnabled(true);
                timbrar.setEnabled(false);
            }
        }
        System.out.println("RFC: " + rfcEmisor.get(row) + " Row: " + row + " y UUID: " + uuid.get(row));
    }//GEN-LAST:event_foliosMouseClicked

    private String obtenerNombreComprobante(int row) {
        String rfcEmi = model.getValueAt(row, 1).toString().trim();
        String rfcRec = model.getValueAt(row, 2).toString().trim();
        String folioSerie = model.getValueAt(row, 0).toString().trim();
        String folio = folioSerie.split("_")[1];
        String serie = folioSerie.split("_")[0];
        String uid = uuid.get(row);
        Elemento.leerConfig(rfcEmi);

        String name = folioSerie + "_" + rfcEmi + "_" + rfcRec + "_" + uid;
        String pathXml = Elemento.pathXml;
        String path = "";

        if (!new File(pathXml + name + ".xml").exists()) {
            name = folioSerie + "_" + rfcEmi + "_" + rfcRec;
            if (!new File(pathXml + name + ".xml").exists()) {
                name = name + "_" + uid;
                try {
                    util.escribirArchivo(getTextXml(serie, folio, rfcEmi, rfcRec), pathXml, name + ".xml");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Elemento.log.error("Ocurrio un error al regenerar el XML:",ex);
                }
                return name;
            } else {
                return name;
            }
        } else {
            return name;
        }

    }

    private void verPdfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verPdfActionPerformed
        try {
            // TODO add your handling code here:
            int row = folios.getSelectedRow();

            String name = obtenerNombreComprobante(row);
            String pathXml = Elemento.pathXml;
            String pathPdf = Elemento.pathPdf;
            File pdf = new File(pathPdf + name + ".pdf");
            File pdfCancel = new File(pathPdf + name + "_CANCELADA" + ".pdf");
            String path;
            isCancelado = model.getValueAt(row, 3).toString().equalsIgnoreCase("CANCELADA");
            
            if (pdf.exists()) {
                path = pdf.getAbsolutePath();
                if (pdfCancel.exists()) {
                    path = pdfCancel.getAbsolutePath();
                }
                try {
                    Exe.exeSinTiempo(path);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    Elemento.log.error("Excepcion al tratar de visualizar un PDF ya existente: " + ex.getMessage(), ex);
                }
            } else {
                Factura_View.visualizar(pathXml, name, null);
            }
        } catch (Exception ex) {
            Logger.getLogger(Folios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_verPdfActionPerformed

    private void enviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enviarActionPerformed
        // TODO add your handling code here:
        int selec[] = folios.getSelectedRows();
        int row;

        String correo = "";
        String emailO = "";
        String passO = "";
        String rfcEmi, rfcRec, uid;
        List<String> emis = new ArrayList();
        List<String> recs = new ArrayList();
        List<String> series = new ArrayList();
        List<String> correos = new ArrayList();

        StringBuilder xmls, pdfs, nameX, nameP;
        String serie = "";
        String nombre = "";
        char coma = ',';

        revisando:
        if (selec.length > 1) {
            xmls = new StringBuilder();
            pdfs = new StringBuilder();
            nameX = new StringBuilder();
            nameP = new StringBuilder();

            for (int i = 0; i < selec.length; i++) {
                row = selec[i];
                rfcEmi = model.getValueAt(row, 1).toString().trim();
                rfcRec = model.getValueAt(row, 2).toString().trim();
                Boolean tim = (Boolean) model.getValueAt(row, 6);
                serie = model.getValueAt(row, 0).toString().trim().split("_")[0];
                series.add(serie);
                emis.add(rfcEmi);
                recs.add(rfcRec);
                
                nombre = this.obtenerNombreComprobante(row);
                
                if(i == 0){
                    try {
                        emailO = this.getEmail("Emisores", rfcEmi);
                        passO = this.getPass(rfcEmi);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        Elemento.log.error("Excepcion al obtener el nombre del XML: ", ex);
                    }
                }

                if (!tim) {
                    JOptionPane.showMessageDialog(null, "Solo puede enviar comprobantes timbrados");
                    break revisando;
                }

                xmls.append(Elemento.pathXml).append(nombre).append(".xml").append((i == (selec.length -1) ? "" : coma));
                pdfs.append(Elemento.pathPdf).append(nombre).append(".pdf").append((i == (selec.length -1) ? "" : coma));
                nameX.append(nombre).append(".xml").append((i == (selec.length -1) ? "" : coma));
                nameP.append(nombre).append(".pdf").append((i == (selec.length -1) ? "" : coma));
            }

            try {
                boolean continuar = true;
                String ent = emis.get(0);
                
                for(String x : emis){
                    if(!x.equals(ent)){
                        continuar = false;
                    }
                }
                
                if(continuar){
                    boolean enviarNomi = true;
                    String nant = series.get(0);
                    
                    for(String x: series){
                        if(!x.equals(nant)){
                            enviarNomi = false;
                        }
                    }
                    
                    if(enviarNomi){
                        String pathXmls[] = xmls.toString().split(",");
                        String pathPdfs[] = pdfs.toString().split(",");
                        String nameXmls[] = nameX.toString().split(",");
                        String namePdfs[] = nameP.toString().split(",");
                        
                        NominaCorreos nc = new NominaCorreos(emailO, passO, recs, pathXmls, pathPdfs, nameXmls, namePdfs);
                        nc.setVisible(true);
                        
                    }else{
                        boolean si = true;
                        String ant = recs.get(0);

                        for (String x : recs) {
                            if (!x.equals(ant)) {
                                si = false;
                            } 
                        }
                        if (si) {
                            if (!serie.equalsIgnoreCase("NOM")) {
                                correo = getEmail("Clientes", ant);
                            } else {
                                correo = getEmail("EmpleadosRec", ant);
                            }
                        }
                        
                        String args[] = new String[8];
                        args[0] = xmls.toString();
                        args[1] = pdfs.toString();
                        args[2] = nameX.toString();
                        args[3] = nameP.toString();
                        args[4] = correo;
                        args[5] = emailO;
                        args[6] = passO;
                        args[7] = Elemento.getMailConfiguration(emailO);

                        SendMail.main(args);
                    }
                }else{
                    util.printError("Solo puede enviar comprobantes del mismo emisor");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Elemento.log.error("Excepcion al obtener el email del receptor con RFC: " + recs.get(0) + " || SQLException: " + ex.getMessage(), ex);
            }

        } else {
            row = selec[0];
            rfcEmi = model.getValueAt(row, 1).toString().trim();
            rfcRec = model.getValueAt(row, 2).toString().trim();
            
            nombre = this.obtenerNombreComprobante(row);

            try {
                correo = getEmail("Clientes", rfcRec);
                emailO = getEmail("Emisores", rfcEmi);
                passO = getPass(rfcEmi);
            } catch (SQLException ex) {
                ex.printStackTrace();
                Elemento.log.error("Excepcion al obtener el email del cliente con RFC: " + rfcRec + " || SQLException: " + ex.getMessage(), ex);
            }

            String args[] = new String[8];
            args[0] = Elemento.pathXml + nombre + ".xml";
            args[1] = Elemento.pathPdf + nombre + ".pdf";
            args[2] = nombre + ".xml";
            args[3] = nombre + ".pdf";
            args[4] = correo;
            args[5] = emailO;
            args[6] = passO;
            args[7] = Elemento.getMailConfiguration(emailO);

            SendMail.main(args);
        }


    }//GEN-LAST:event_enviarActionPerformed

    private void cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarActionPerformed
        String texto = "";
        String xml = "";
        String logo = "";

        int pos = folios.getSelectedRow();
        String rfcE = rfcEmisor.get(pos);
        String rfcR = model.getValueAt(pos, 2).toString().trim();
        String tot = model.getValueAt(pos, 5).toString().trim();

        Elemento.leerConfig(rfcE);

        String txt = model.getValueAt(pos, 0).toString().trim();

        String folio = txt.split("_")[1];
        String serie = txt.split("_")[0];
        try {
            xml = this.obtenerNombreComprobante(pos);
        } catch (Exception ex) {
            ex.printStackTrace();
            Elemento.log.error("Excepcion al obtener el nombre del XML: ", ex);
        }
        logo = Elemento.logo;
        String uu = uuid.get(pos);
        Long trans = transId.get(pos);
        Elemento.log.info("Se cancelara la factura del RFC Emisor: " + rfcE + " y con RFC Receptor: " + rfcR + " con UUID: " + uu);
        //String idIntegrador = "59548258-e908-48d3-9b07-6e9d3ff9c64f";
        String args[] = {folio, xml, logo, uu, rfcE, rfcR, "" + trans, tot, serie};
        CancelarView.main(args);
    }//GEN-LAST:event_cancelarActionPerformed

    private void reporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reporteActionPerformed
        // TODO add your handling code here:
        ReporteadorView re = new ReporteadorView();
        re.setVisible(true);
    }//GEN-LAST:event_reporteActionPerformed

    private void folioTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_folioTxtKeyPressed
        if (evt.getKeyCode() == 10) {
            consultarFacturas();
        }
    }//GEN-LAST:event_folioTxtKeyPressed

    private void timbrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timbrarActionPerformed
        int row = folios.getSelectedRow();
        String rfcEmi = model.getValueAt(row, 1).toString().trim();
        String rfcRec = model.getValueAt(row, 2).toString().trim();
        String folioSerie = model.getValueAt(row, 0).toString().trim();
        String[] fs = folioSerie.split("_");
        String serie = fs[0];
        String folio = fs[1];
        String name = folioSerie + "_" + rfcEmi + "_" + rfcRec + ".txt";
        File layout = new File(Elemento.pathLayout + name);
        Elemento.leerConfig(rfcEmi);
        if (layout.exists()) {
            try {
                new Listener().procesar(Elemento.pathLayout, name);
            } catch (Exception ex) {
                ex.printStackTrace();
                Elemento.log.error("Excepcion: Ocurrio un problema al timbrar: ", ex);
            }
        } else {
            crearLayout(serie, folio, rfcEmi, rfcRec, name);
        }
    }//GEN-LAST:event_timbrarActionPerformed

    private void verificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verificarActionPerformed
        // TODO add your handling code here:
        if(activar){
            TestValidator validar = new TestValidator();
            int row = folios.getSelectedRow();
            String re = model.getValueAt(row, 1).toString();
            String rr = model.getValueAt(row, 2).toString();
            String tt = model.getValueAt(row, 5).toString();
            String uid = uuid.get(row);
            try {
                obtenerCadenaOriginal(new File(Elemento.pathXml + obtenerNombreComprobante(row) + ".xml"));
            } catch (Exception ex) {
                ex.printStackTrace();
                Elemento.log.error("Excepcion al obtener la cadena original del comprobante: " + uuid, ex);
            }

            util.print(validar.consultar(validar.formarXml(re, rr, tt, uid)));
        }else{
            cfdisAsoc = new ArrayList();
            docsPagar = new ArrayList();
            Documento doc;
            Comprobante comp;
            int select[] = folios.getSelectedRows();
            if(select.length > 0){
                for (int i = 0; i < select.length; i++) {
                    try {
                        int pos = select[i];
                        doc = new Documento();
                        cfdisAsoc.add(uuid.get(pos));
                        String nameXml = folios.getModel().getValueAt(pos,0).toString() + "_" + folios.getModel().getValueAt(pos,1).toString() + "_" + folios.getModel().getValueAt(pos,2).toString();
                        File xml = new File(Elemento.pathXml + nameXml + ".xml");
                        
                        if(!xml.exists()){
                            nameXml += "_" + uuid.get(pos) + ".xml";
                        }
                        
                        comp = util.analizarXml(Elemento.pathXml+nameXml);
                        
                        doc.setFolio(comp.getFolio());
                        doc.setSerie(comp.getSerie());
                        doc.setImpPagado(new BigDecimal(comp.getTotal()));
                        doc.setImpSaldoAnterior(new BigDecimal(comp.getTotal()));
                        doc.setImpSaldoInsoluto(BigDecimal.ZERO);
                        doc.setMoneda(comp.getMoneda());
                        doc.setNumParcialidad(1);
                        doc.setTipoCambio(comp.getTipoCambio() == null || comp.getTipoCambio().isEmpty() ? null : new BigDecimal(comp.getTipoCambio()));
                        doc.setMetodoPago(comp.getMetodoPago());
                        doc.setUuid(uuid.get(pos));
                        docsPagar.add(doc);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        String msg = "Error al obtener los datos de las documentos";
                        Elemento.log.error(msg, ex);
                        util.printError(msg + " - " + ex.getMessage());
                    }
                }
                if(p != null){
                    rp.addDocumentos(this, p);
                }else{
                    if(this.fv != null){
                        this.fv.setUuids(this);
                    }else if(this.rp != null){
                        this.rp.setUuids(this);
                    }
                }
                this.setVisible(false);
            }else{
                JOptionPane.showMessageDialog(null,"Debe seleccionar almenos un documento", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_verificarActionPerformed
    
    private String obtenerCadenaOriginal(File xml) throws Exception {
        File stylesheet = new File("/Facturas/config/cadenaoriginal_3_3.xslt");
        Stylezer st = new Stylezer();
        st.obtener(xml, stylesheet);
        return "";
    }

    public String getEmail(String tabla, String rfcr) throws SQLException { //Obteniene el email de la base de datos si es que se encuentra
        con = conexion();
        Statement stmt;
        ResultSet rs;
        if (con != null) {
            stmt = factory.stmtLectura(con);
            rs = stmt.executeQuery("SELECT email FROM " + tabla + " WHERE rfc like \'" + rfcr + "\'");
            if (rs.next()) {
                String tmp = rs.getString("email");
                if (tmp == null) {
                    tmp = "";
                }
                rs.close();
                stmt.close();
                con.close();
                return tmp.trim();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public String getPass(String rfcr) throws SQLException { //Obteniene el email de la base de datos si es que se encuentra
        con = conexion();
        Statement stmt;
        ResultSet rs;
        if (con != null) {
            stmt = factory.stmtLectura(con);
            rs = stmt.executeQuery("SELECT pass FROM Emisores WHERE rfc = \'" + rfcr + "\'");
            if (rs.next()) {
                String tmp = rs.getString("pass");
                rs.close();
                stmt.close();
                con.close();
                return tmp != null ? tmp.trim() : tmp;
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    private Connection conexion() {
        return Elemento.odbc();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String args[]) {
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Folios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Folios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Folios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Folios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Folios().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelar;
    private javax.swing.JButton consultar;
    private javax.swing.JButton enviar;
    private javax.swing.JTextField folioTxt;
    private javax.swing.JTable folios;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox noTimbrados;
    private javax.swing.JCheckBox porEmisores;
    private javax.swing.JButton reporte;
    private javax.swing.JButton timbrar;
    private javax.swing.JButton verPdf;
    private javax.swing.JButton verificar;
    // End of variables declaration//GEN-END:variables

    private void crearLayout(String serie, String folio, String rfcEmi, String rfcRec, String name) {
        Connection conn = Elemento.odbc();
        Statement stmt = factory.stmtLectura(conn);
        ResultSet rs;

        try {
            rs = stmt.executeQuery("SELECT layout FROM Facturas WHERE serie like \'" + serie + "\' AND folio = " + folio + " AND rfcEmisor like \'" + rfcEmi + "\' AND rfc like \'" + rfcRec + "\'");
            if (rs.next()) {
                String lay = rs.getString("layout");
                util.escribirLayout(lay, name);
            } else {
                String msg = "No se encuentra registrado este comprobante: " + name;
                Elemento.log.warn(msg);
                JOptionPane.showMessageDialog(null, msg);
            }
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al tratar de timbrar un comprobante registrado como \"NO TIMBRADO\": " + name, e);
        }
    }

    private String getTextXml(String serie, String folio, String rfcEmi, String rfcRec) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        String xml = null;
        try {
            rs = stmt.executeQuery("SELECT xml FROM Facturas WHERE serie like \'" + serie + "\' AND folio = " + folio + " AND rfcEmisor like \'" + rfcEmi + "\' AND rfc like \'" + rfcRec + "\'");
            if (rs.next()) {
                xml = rs.getString("xml");
            }
            stmt.close();
            con.close();
            return xml;
        } catch (Exception e) {
            e.printStackTrace();
            Elemento.log.error("Excepcion al obtener el xml de la base de datos: ", e);
            return null;
        }
    }
}
