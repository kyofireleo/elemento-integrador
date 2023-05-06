/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nominas.configuracion;

import nominas.Empleado;
import elemento.ConnectionFactory;
import elemento.Elemento;
import java.awt.HeadlessException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import nominas.Deducciones;
import nominas.OtrosPagos;
import nominas.Percepciones;

/**
 *
 * @author Abe
 */
public class Empleados extends javax.swing.JFrame {

    ConnectionFactory factory = new ConnectionFactory();
    int idEmpleado;
    String nombre = "";
    boolean modificar = false;
    OtrosPagos otr;
    Percepciones per;
    Deducciones dec;
    private boolean guardo;
    ConfigNominas nomi;
    
    public Empleados() {
        initComponents();
        this.setLocationRelativeTo(null);
        otr = new OtrosPagos();
        per = new Percepciones();
        dec = new Deducciones();
        guardo = false;
    }
    
    public Empleados(int idEmpleado, String nombre){
        initComponents();
        this.setLocationRelativeTo(null);
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.setTitle("Empleado: "+nombre);
        otr = new OtrosPagos(idEmpleado);
        per = new Percepciones(idEmpleado);
        dec = new Deducciones(idEmpleado);
        guardo = false;
    }
    
    public Empleados(int idEmpleado, String nombre, ConfigNominas nomi){
        initComponents();
        this.setLocationRelativeTo(null);
        this.nombre = nombre;
        this.idEmpleado = idEmpleado;
        this.setTitle("Empleado: "+nombre);
        otr = new OtrosPagos();
        per = new Percepciones();
        dec = new Deducciones();
        guardo = false;
        this.nomi = nomi;
    }
    
    public Empleados(int id, int idEmpleado, String nombre){
        initComponents();
        this.setLocationRelativeTo(null);
        this.idEmpleado = idEmpleado;
        this.setTitle("EMPLEADO: "+nombre);
        consultarEmpleado();
        modificar = true;
        otr = new OtrosPagos(idEmpleado,false);
        per = new Percepciones(idEmpleado,false);
        dec = new Deducciones(idEmpleado,false);
        guardo = true;
    }

    private void consultarEmpleado(){
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        
        try{
            rs = stmt.executeQuery("SELECT * FROM Empleados WHERE idEmpleado = " + idEmpleado);
            if(rs.next()){
                numEmpleado.setText(rs.getString("numEmpleado"));
                curp.setText(rs.getString("curp"));
                tipoRegimen.setSelectedIndex(rs.getInt("tipoRegimen")-1);
                nss.setText(rs.getString("nss"));
                departamento.setText(rs.getString("departamento"));
                clabe.setText(rs.getString("clabe"));
                
                String ban = rs.getString("banco");
                for (int i = 0; i < banco.getItemCount(); i++) {
                    if(ban.equals(banco.getItemAt(i).toString().split(",")[0])){
                        banco.setSelectedIndex(i);
                    }
                }
                
                fechaInicialRelLaboral.setDate(rs.getDate("fechaInicialRelLaboral"));
                seleccionarTipoRegimen(rs.getString("tipoRegimen"));
                seleccionarTipoContrato(rs.getString("tipoContrato"));
                seleccionarTipoJornada(rs.getString("tipoJornada"));
                seleccionarPeriodicidadPago(rs.getString("periodicidadPago"));
                salarioBaseCotApor.setText(rs.getString("salarioBaseCotApor"));
                riesgoPuesto.setSelectedIndex(rs.getInt("riesgoPuesto")-1);
                salarioDiario.setText(rs.getString("salarioDiarioInt"));
                puesto.setText(rs.getString("puesto"));
            }
            rs.close();
            stmt.close();
            con.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    private void seleccionarTipoRegimen(String reg){
        for (int i = 0; i < tipoRegimen.getItemCount(); i++) {
            if(tipoRegimen.getItemAt(i).toString().contains(reg)){
                tipoRegimen.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private void seleccionarTipoContrato(String reg){
        for (int i = 0; i < tipoContrato.getItemCount(); i++) {
            if(tipoContrato.getItemAt(i).toString().contains(reg)){
                tipoContrato.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private void seleccionarTipoJornada(String reg){
        for (int i = 0; i < tipoJornada.getItemCount(); i++) {
            if(tipoJornada.getItemAt(i).toString().contains(reg)){
                tipoJornada.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private void seleccionarPeriodicidadPago(String reg){
        for (int i = 0; i < periodicidadPago.getItemCount(); i++) {
            if(periodicidadPago.getItemAt(i).toString().contains(reg)){
                periodicidadPago.setSelectedIndex(i);
                break;
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        numEmpleado = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        curp = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        tipoRegimen = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        nss = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        departamento = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        clabe = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        banco = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        fechaInicialRelLaboral = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        tipoContrato = new javax.swing.JComboBox();
        jLabel17 = new javax.swing.JLabel();
        tipoJornada = new javax.swing.JComboBox();
        jLabel18 = new javax.swing.JLabel();
        periodicidadPago = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        salarioBaseCotApor = new javax.swing.JTextField();
        salarioDiario = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        riesgoPuesto = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        puesto = new javax.swing.JTextField();
        guardarBtn = new javax.swing.JButton();
        cancelarBtn = new javax.swing.JButton();
        verPercepciones = new javax.swing.JButton();
        verDeducciones = new javax.swing.JButton();
        verOtrosPagos = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Empleado: ");

        jLabel2.setText("Numero de Empleado");

        jLabel3.setText("CURP");

        jLabel4.setText("Tipo de Regimen");

        tipoRegimen.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "02, Sueldos", "03, Jubilados", "04, Pensionados", "05, Asimilados Miembros Sociedades Cooperativas Produccion", "06, Asimilados Integrantes Sociedades Asociaciones Civiles", "07, Asimilados Miembros consejos", "08, Asimilados comisionistas", "09, Asimilados Honorarios", "10, Asimilados acciones", "11, Asimilados otros", "99, Otro Regimen" }));

        jLabel5.setText("Numero de Seguro Social");

        jLabel10.setText("Departamento");

        jLabel11.setText("CLABE");

        jLabel12.setText("Banco");

        banco.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "002,BANAMEX", "006,BANCOMEXT", "009,BANOBRAS", "012,BBVA BANCOMER", "014,SANTANDER", "019,BANJERCITO", "021,HSBC", "030,BAJIO", "032,IXE", "036,INBURSA", "037,INTERACCIONES", "042,MIFEL", "044,SCOTIABANK", "058,BANREGIO", "059,INVEX", "060,BANSI", "062,AFIRME", "072,BANORTE", "102,THE ROYAL BANK", "103,AMERICAN EXPRESS", "106,BAMSA", "108,TOKYO", "110,JP MORGAN", "112,BMONEX", "113,VE POR MAS", "116,ING", "124,DEUTSCHE", "126,CREDIT SUISSE", "127,AZTECA", "128,AUTOFIN", "129,BARCLAYS", "130,COMPARTAMOS", "131,BANCO FAMSA", "132,BMULTIVA", "133,ACTINVER", "134,WAL-MART", "135,NAFIN", "136,INTERBANCO", "137,BANCOPPEL", "138,ABC CAPITAL", "139,UBS BANK", "140,CONSUBANCO", "141,VOLKSWAGEN", "143,CIBANCO", "145,BBASE", "166,BANSEFI", "168,HIPOTECARIA FEDERAL", "600,MONEXCB", "601,GBM", "602,MASARI", "605,VALUE", "606,ESTRUCTURADORES", "607,TIBER", "608,VECTOR", "610,B&B", "614,ACCIVAL", "615,MERRILL LYNCH", "616,FINAMEX", "617,VALMEX", "618,UNICA", "619,MAPFRE", "620,PROFUTURO", "621,CB ACTINVER", "622,OACTIN", "623,SKANDIA", "626,CBDEUTSCHE", "627,ZURICH", "628,ZURICHVI", "629,SU CASITA", "630,CB INTERCAM", "631,CI BOLSA", "632,BULLTICK CB", "633,STERLING", "634,FINCOMUN", "636,HDI SEGUROS", "637,ORDER", "638,AKALA", "640,CB JPMORGAN", "642,REFORMA", "646,STP", "647,TELECOMM", "648,EVERCORE", "649,SKANDIA", "651,SEGMTY", "652,ASEA", "653,KUSPIT", "655,SOFIEXPRESS", "656,UNAGRA", "659,OPCIONES EMPRESARIALES DEL NOROESTE", "901,CLS", "902,INDEVAL", "670,LIBERTAD" }));

        jLabel13.setText("Fecha Inicial de Relacion Laboral");

        fechaInicialRelLaboral.setDateFormatString("yyyy-MM-dd");

        jLabel16.setText("Tipo de Contrato");

        tipoContrato.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01, Contrato de trabajo por tiempo indeterminado", "02, Contrato de trabajo para obra determinada", "03, Contrato de trabajo por tiempo determinado", "04, Contrato de trabajo por temporada", "05, Contrato de trabajo sujeto a prueba", "06, Contrato de trabajo con capacitación inicial", "07, Modalidad de contratación por pago de hora laborada", "08, Modalidad de trabajo por comisión laboral", "09, Modalidades de contratación donde no existe relación de trabajo", "10, Jubilación, pensión, retiro.", "99, Otro contrato" }));

        jLabel17.setText("Tipo de Jornada");

        tipoJornada.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01, Diurna", "02, Nocturna", "03, Mixta", "04, Por hora", "05, Reducida", "06, Continuada", "07, Partida", "08, Por turnos", "99, Otra Jornada" }));

        jLabel18.setText("Periodicidad de Pago");

        periodicidadPago.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "01, Diario", "02, Semanal", "03, Catorcenal", "04, Quincenal", "05, Mensual", "06, Bimestral", "07, Unidad de obra", "08, Comision", "09, Precio Alzado", "10, Decenal", "99, Otra Periodicidad" }));

        jLabel19.setText("Salario Base Cot. Apor.");

        jLabel21.setText("Sueldo Diario");

        salarioBaseCotApor.setText("0.0");
        salarioBaseCotApor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                salarioBaseCotAporFocusGained(evt);
            }
        });

        salarioDiario.setText("0.0");
        salarioDiario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                salarioDiarioFocusGained(evt);
            }
        });

        jLabel20.setText("Riesgo del Puesto");

        riesgoPuesto.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Clase I", "Clase II", "Clase III", "Clase IV", "Clase V" }));

        jLabel15.setText("Puesto");

        guardarBtn.setText("Guardar");
        guardarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarBtnActionPerformed(evt);
            }
        });

        cancelarBtn.setText("Cancelar");
        cancelarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarBtnActionPerformed(evt);
            }
        });

        verPercepciones.setText("Percepciones");
        verPercepciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verPercepcionesActionPerformed(evt);
            }
        });

        verDeducciones.setText("Deducciones");
        verDeducciones.setMaximumSize(new java.awt.Dimension(95, 23));
        verDeducciones.setMinimumSize(new java.awt.Dimension(95, 23));
        verDeducciones.setPreferredSize(new java.awt.Dimension(95, 23));
        verDeducciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verDeduccionesActionPerformed(evt);
            }
        });

        verOtrosPagos.setText("Otros Pagos");
        verOtrosPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                verOtrosPagosActionPerformed(evt);
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel12)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(departamento)
                                .addComponent(clabe)
                                .addComponent(banco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(fechaInicialRelLaboral, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2)))
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(numEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(curp, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                            .addComponent(nss, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                            .addComponent(tipoRegimen, 0, 1, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(guardarBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(verPercepciones, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(verDeducciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(verOtrosPagos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancelarBtn)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(66, 66, 66)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(83, 83, 83)
                                .addComponent(jLabel16))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel20)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(riesgoPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tipoJornada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(periodicidadPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 169, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tipoContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel21)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(salarioDiario, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel19)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(salarioBaseCotApor, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(79, 79, 79)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(puesto, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(44, 44, 44))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(numEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(curp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tipoRegimen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(nss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(departamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(clabe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(banco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel13)
                    .addComponent(fechaInicialRelLaboral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(tipoContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(tipoJornada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(periodicidadPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(riesgoPuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(salarioBaseCotApor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(salarioDiario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(puesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(guardarBtn)
                    .addComponent(cancelarBtn)
                    .addComponent(verPercepciones)
                    .addComponent(verDeducciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(verOtrosPagos))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void cancelarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarBtnActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_cancelarBtnActionPerformed

    private void guardarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarBtnActionPerformed
        // TODO add your handling code here:
        guardo = true;
        if(modificar)
            actualizarEmpleado(crearEmpleado());
        else
            agregarEmpleado(crearEmpleado());
        guardarBtn.setEnabled(false);
        
        if(nomi!=null)
            nomi.consultarEmpleados();
    }//GEN-LAST:event_guardarBtnActionPerformed

    private void verPercepcionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verPercepcionesActionPerformed
        // TODO add your handling code here:
        if(guardo){
            per.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(null, "Primero debe guardar el empleado");
        }
    }//GEN-LAST:event_verPercepcionesActionPerformed

    private void verDeduccionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verDeduccionesActionPerformed
        // TODO add your handling code here:
        if(guardo)
            dec.setVisible(true);
        else
            JOptionPane.showMessageDialog(null, "Primero debe guardar el empleado");
    }//GEN-LAST:event_verDeduccionesActionPerformed

    private void salarioBaseCotAporFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salarioBaseCotAporFocusGained
        // TODO add your handling code here:
        salarioBaseCotApor.selectAll();
    }//GEN-LAST:event_salarioBaseCotAporFocusGained

    private void salarioDiarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_salarioDiarioFocusGained
        // TODO add your handling code here:
        salarioDiario.selectAll();
    }//GEN-LAST:event_salarioDiarioFocusGained

    private void verOtrosPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verOtrosPagosActionPerformed
        if(guardo){
            otr.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(null, "Primero debe guardar el empleado");
        }
    }//GEN-LAST:event_verOtrosPagosActionPerformed

    private Empleado crearEmpleado(){
        Empleado emp = new Empleado();
        
        emp.setIdEmpleado(idEmpleado);
        emp.setBanco(banco.getSelectedItem().toString().split(",")[0]);
        emp.setClabe(clabe.getText());
        emp.setCurp(curp.getText());
        emp.setDepartamento(departamento.getText());
        emp.setFechaInicialRelLaboral(fechaInicialRelLaboral.getDate());
        emp.setNss(nss.getText());
        emp.setNumEmpleado(numEmpleado.getText());
        emp.setPeriodicidadPago(periodicidadPago.getSelectedItem().toString().split(",")[0]);
        emp.setPuesto(puesto.getText());
        emp.setRiesgoPuesto(""+(riesgoPuesto.getSelectedIndex()+1));
        emp.setTipoContrato(tipoContrato.getSelectedItem().toString().split(",")[0]);
        emp.setTipoJornada(tipoJornada.getSelectedItem().toString().split(",")[0]);
        emp.setTipoRegimen(tipoRegimen.getSelectedItem().toString().split(",")[0]);
        emp.setSalarioDiarioInt(new BigDecimal(salarioDiario.getText()));
        emp.setSalarioBaseCotApor(new BigDecimal(salarioBaseCotApor.getText()));
        return emp;
    }
    
    private void agregarEmpleado(Empleado emp){
        Connection con = Elemento.odbc();
        PreparedStatement stmt;
        java.sql.Date valorFecha;
        ResultSet rs;
        
        if(emp.getFechaInicialRelLaboral() != null){
            valorFecha = new java.sql.Date(emp.getFechaInicialRelLaboral().getTime());
        }else{
            valorFecha = null;
        }
        
        try{
            stmt = con.prepareStatement("INSERT INTO Empleados (idEmpleado,numEmpleado,curp,tipoRegimen,nss,departamento,clabe,banco,fechaInicialRelLaboral,puesto,tipoContrato,tipoJornada,periodicidadPago,riesgoPuesto,salarioDiarioInt,salarioBaseCotApor) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            stmt.setInt(1, emp.getIdEmpleado());
            stmt.setString(2, emp.getNumEmpleado());
            stmt.setString(3, emp.getCurp());
            stmt.setString(4, emp.getTipoRegimen());
            stmt.setString(5, emp.getNss());
            stmt.setString(6, emp.getDepartamento());
            stmt.setString(7, emp.getClabe());
            stmt.setString(8, emp.getBanco());
            stmt.setDate(9, valorFecha);
            stmt.setString(10, emp.getPuesto());
            stmt.setString(11, emp.getTipoContrato());
            stmt.setString(12, emp.getTipoJornada());
            stmt.setString(13, emp.getPeriodicidadPago());
            stmt.setString(14, emp.getRiesgoPuesto());
            stmt.setBigDecimal(15, emp.getSalarioDiarioInt());
            stmt.setBigDecimal(16, emp.getSalarioBaseCotApor());
            
            stmt.addBatch();
            stmt.executeBatch();
            
            stmt.close();
            con.close();
            JOptionPane.showMessageDialog(null, "Se ha guardado la informacion del empleado correctamente");
            Elemento.log.info("Se ha guardado la informacion del empleado " + nombre + " correctamente");
        }catch(SQLException | HeadlessException ex){
            ex.printStackTrace();
            Elemento.log.error("Excepcion al guardar el Empleado con clave: " + emp.getNumEmpleado(),ex);
        }
    }
    
    private void actualizarEmpleado(Empleado emp){
        Connection con = Elemento.odbc();
        PreparedStatement stmt = null;
        java.sql.Date valorFecha;
        
        if(emp.getFechaInicialRelLaboral() != null){
            valorFecha = new java.sql.Date(emp.getFechaInicialRelLaboral().getTime());
        }else{
            valorFecha = null;
        }
        
        try{
            con.setAutoCommit(false);
            stmt = con.prepareStatement("UPDATE Empleados SET  "
                    + "numEmpleado=?,curp=?,tipoRegimen=?,nss=?,departamento=?,"
                    + "clabe=?,banco=?,fechaInicialRelLaboral=?,puesto=?,tipoContrato=?,tipoJornada=?,"
                    + "periodicidadPago=?,riesgoPuesto=?,salarioDiarioInt=?,salarioBaseCotApor=?"
                    + " WHERE idEmpleado=?");
            stmt.setString(1, emp.getNumEmpleado());
            stmt.setString(2, emp.getCurp());
            stmt.setString(3, emp.getTipoRegimen());
            stmt.setString(4, emp.getNss());
            stmt.setString(5, emp.getDepartamento());
            stmt.setString(6, emp.getClabe());
            stmt.setString(7, emp.getBanco());
            stmt.setDate(8, valorFecha);
            stmt.setString(9, emp.getPuesto());
            stmt.setString(10, emp.getTipoContrato());
            stmt.setString(11, emp.getTipoJornada());
            stmt.setString(12, emp.getPeriodicidadPago());
            stmt.setString(13, emp.getRiesgoPuesto());
            stmt.setBigDecimal(14, emp.getSalarioDiarioInt());
            stmt.setBigDecimal(15, emp.getSalarioBaseCotApor());
            stmt.setInt(16, emp.getIdEmpleado());
            
            stmt.executeUpdate();
            con.commit();
            stmt.close();
            con.close();
            JOptionPane.showMessageDialog(null, "Se ha actualizado la informacion del empleado correctamente");
            Elemento.log.info("Se ha actualizado la informacion del empleado " + nombre + " correctamente");
            this.dispose();
        }catch(SQLException | HeadlessException ex){
            ex.printStackTrace();
            Elemento.log.error("Excepcion al guardar el Empleado con clave: " + emp.getNumEmpleado(),ex);
            try{
                if(stmt != null){
                    stmt.close();
                }
                con.rollback();
                con.close();
            }catch(SQLException e){
                e.printStackTrace();
                Elemento.log.error("Excepcion al hacer rollback: ", ex);
            }
            
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Empleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Empleados().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox banco;
    private javax.swing.JButton cancelarBtn;
    private javax.swing.JTextField clabe;
    private javax.swing.JTextField curp;
    private javax.swing.JTextField departamento;
    private com.toedter.calendar.JDateChooser fechaInicialRelLaboral;
    private javax.swing.JButton guardarBtn;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField nss;
    private javax.swing.JTextField numEmpleado;
    private javax.swing.JComboBox periodicidadPago;
    private javax.swing.JTextField puesto;
    private javax.swing.JComboBox riesgoPuesto;
    private javax.swing.JTextField salarioBaseCotApor;
    private javax.swing.JTextField salarioDiario;
    private javax.swing.JComboBox tipoContrato;
    private javax.swing.JComboBox tipoJornada;
    private javax.swing.JComboBox tipoRegimen;
    private javax.swing.JButton verDeducciones;
    private javax.swing.JButton verOtrosPagos;
    private javax.swing.JButton verPercepciones;
    // End of variables declaration//GEN-END:variables
}
