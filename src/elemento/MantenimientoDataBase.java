/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package elemento;

import static elemento.Elemento.log;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class MantenimientoDataBase {
    private String dataBase;
    private String password;
    private ConnectionFactory factory;
    private utils.Utils util;
    
    public MantenimientoDataBase(String dataBase){
        this.dataBase = dataBase;
        this.password = "administradorID";
        this.factory = new ConnectionFactory();
        this.util = new utils.Utils(Elemento.log);
        
        /******catalogo de bancos******/
        verificarCBancos();
        
        /******cambios en tabla Cuentas y Folios******/
        verificarCuentasFolios();
        
        /******cambios en tabla de Regimen Fiscal******/
        verificarRegimenFiscal();
        
        /******cambios en tabla de Uso CFDi******/
        verificarUsoCfdi();
        
        /******cambios en tabla de Clientes******/
        verificarClientes();
        
        /******cambios en tabla de EmisoresBancos******/
        verificarEmisoresBancos();
    }
    
    private void verificarCBancos(){
        Connection con = Elemento.odbc();
        Statement stmt = null;
        ResultSet rs;
        try{
            stmt = factory.stmtEscritura(con);
            rs = stmt.executeQuery("select top 1 1 from c_bancos");
            
            rs.close();
            stmt.close();
            con.close();
        }catch(SQLException e){
            log.warn("No existe la tabla c_bancos, se creara.");
            try{
                con.setAutoCommit(false);
                if(stmt != null)
                    stmt.executeUpdate(
                        "CREATE TABLE c_bancos (id_banco AUTOINCREMENT, nombre varchar(1000) not null, rfc varchar(13) not null, CONSTRAINT cboancos_PK PRIMARY KEY(id_banco))"
                    );

                log.info("Se creo la tabla, se procede a ingresar el catalogo");
                
                java.sql.PreparedStatement ps = con.prepareStatement("INSERT INTO c_bancos (nombre, rfc) values (?,?)");
                ps.setString(1,"AFORE AFIRME BAJIO, SA DE CV"); ps.setString(2,"AAB050415BJ9"); ps.addBatch();
                ps.setString(1,"AMERICAN EXPRESS BANK MEXICO, SA"); ps.setString(2,"AEB960223JP7"); ps.addBatch();
                ps.setString(1,"BANCA AFIRME, SA"); ps.setString(2,"BAF950102JP5"); ps.addBatch();
                ps.setString(1,"BANCA MIFEL SA INSTITUCION DE BANCA MULTIPLE GRUPO FINANCIERO MIFEL"); ps.setString(2,"BMI9312038R3"); ps.addBatch();
                ps.setString(1,"BANCO ACTINVER, S.A., INSTITUCION DE BANCA MULTIPLE, GRUPO FINANCIERO ACTINVER"); ps.setString(2,"PBI061115SC6"); ps.addBatch();
                ps.setString(1,"BANCO AHORRO FAMSA S.A. INSTITUCION DE BANCA MULTIPLE"); ps.setString(2,"BAF060524EV6"); ps.addBatch();
                ps.setString(1,"BANCO AUTOFIN MEXICO, SA"); ps.setString(2,"BAM0511076B3"); ps.addBatch();
                ps.setString(1,"BANCO AZTECA SA INSTITUCION DE BANCA MULTIPLE"); ps.setString(2,"BAI0205236Y8"); ps.addBatch();
                ps.setString(1,"BANCO BANCREA"); ps.setString(2,"BBA130722BR7"); ps.addBatch();
                ps.setString(1,"BANCO COMPARTAMOS SA INSTITUCION DE BANCA MULTIPLE"); ps.setString(2,"BCI001030ECA"); ps.addBatch();
                ps.setString(1,"BANCO DEL AHORRO NACIONAL Y SERVICIOS FINANCIEROS, SOCIEDAD NACIONAL DE CREDITO INSTITUCION DE BANCA DE DESARROLLO"); ps.setString(2,"BAN500901167"); ps.addBatch();
                ps.setString(1,"BANCO DEL BAJIO, SA"); ps.setString(2,"BBA940707IE1"); ps.addBatch();
                ps.setString(1,"BANCO INBURSA SA INSTITUCION DE BANCA MULTIPLE GRUPO FINANCIERO INBUR, SA"); ps.setString(2,"BII931004P61"); ps.addBatch();
                ps.setString(1,"BANCO INMOBILIARIO MEXICANO SA INSTITUCI"); ps.setString(2,"HCM010608EG1"); ps.addBatch();
                ps.setString(1,"BANCO INTERACCIONES SA INSTITUCION DE BANCA MULTIPLE GPO FINANC INTERACCIONES"); ps.setString(2,"BIN931011519"); ps.addBatch();
                ps.setString(1,"BANCO INVEX SA INSTITUCION DE BANCA MULTIPLE"); ps.setString(2,"BIN940223KE0"); ps.addBatch();
                ps.setString(1,"BANCO J P MORGAN SA INSTITUCION DE BANCA MULTIPLE J P MORGAN GRUPO FINANCIERO"); ps.setString(2,"BJP950104LJ5"); ps.addBatch();
                ps.setString(1,"BANCO MERCANTIL DEL NORTE SA INSTITUCION DE BANCA MULTIPLE GRUPO FINANCIERO BANORTE"); ps.setString(2,"BMN930209927"); ps.addBatch();
                ps.setString(1,"BANCO MONEX S.A. INSTITUCION DE BANCA MULTIPLE, MONEX GRUPO FINANCIERO"); ps.setString(2,"BMI9704113PA"); ps.addBatch();
                ps.setString(1,"BANCO MULTIVA SOCIEDAD ANONIMA INSTITUCION DE BANCA MULTIPLE GRUPO FINANCIERO MULTIVA"); ps.setString(2,"BMI061005NY5"); ps.addBatch();
                ps.setString(1,"BANCO NACIONAL DE EJERCITO FUERZA AEREA Y ARMADA, SNC"); ps.setString(2,"BNE820901682"); ps.addBatch();
                ps.setString(1,"BANCO NACIONAL DE MEXICO, SA"); ps.setString(2,"BNM840515VB1"); ps.addBatch();
                ps.setString(1,"BANCO PAGATODO, S.A."); ps.setString(2,"BPS121217FS6"); ps.addBatch();
                ps.setString(1,"BANCO REGIONAL DE MONTERREY SA INSTITUCION DE BANCA MULTIPLE BANREGIO GRUPO FINANCIERO"); ps.setString(2,"BRM940216EQ6"); ps.addBatch();
                ps.setString(1,"BANCO SANTANDER (MEXICO) S.A., INSTITUCION DE BANCA MULTIPLE, GRUPO FINANCIERO SANTANDER"); ps.setString(2,"BSM970519DU8"); ps.addBatch();
                ps.setString(1,"BANCO VE POR MAS, S.A., INSTITUCION DE BANCA MULTIPLE, GRUPO FINANCIERO VE POR MAS"); ps.setString(2,"BVM951002LX0"); ps.addBatch();
                ps.setString(1,"BANCO WAL MART DE MEXICO ADELANTE S A INSTITUCION DE BANCA MULTIPLE"); ps.setString(2,"BWM0611132A9"); ps.addBatch();
                ps.setString(1,"BANCOPPEL, S.A. INSTITUCION DE BANCA MULTIPLE"); ps.setString(2,"BSI061110963"); ps.addBatch();
                ps.setString(1,"BANK OF TOKYO-MITSUBISHI UFJ (MEXICO) SOCIEDAD ANONIMA, INSTITUCION DE BANCA MULTIPLE FILIAL"); ps.setString(2,"BTM960401DV7"); ps.addBatch();
                ps.setString(1,"BANORTE-IXE TARJETAS"); ps.setString(2,"ITA081127UZ1"); ps.addBatch();
                ps.setString(1,"BANRENACES ABASOLO, SC DE AP DE RL DE CV"); ps.setString(2,"BAB1011126Y1"); ps.addBatch();
                ps.setString(1,"BANRENACES ACAPULCO, SC DE AP DE RL DE CV"); ps.setString(2,"BAC091210GDA"); ps.addBatch();
                ps.setString(1,"BANRENACES AGUASCALIENTES, SC DE AP DE RL DE CV"); ps.setString(2,"BAG110513DW4"); ps.addBatch();
                ps.setString(1,"BANRENACES AHUACUOTZINGO, SC DE AP DE RL DE CV"); ps.setString(2,"BAH091210AA6"); ps.addBatch();
                ps.setString(1,"BANRENACES AR NGU BOJA, SC DE AP DE RL DE CV"); ps.setString(2,"BAN100831N37"); ps.addBatch();
                ps.setString(1,"BANRENACES ASCENSION SOCIEDAD COOPERATIVA DE AHORRO Y PRESTAMO, SC DE AP DE RL DE CV"); ps.setString(2,"BAA100902SS6"); ps.addBatch();
                ps.setString(1,"BANRENACES ATOYAC SOCIEDAD COOPERATIVA DE AHORRO Y PRESTAMO, SC DE AP DE RL DE CV"); ps.setString(2,"BAA100904CT3"); ps.addBatch();
                ps.setString(1,"BANRENACES BOCHIL CON VISION SOCIEDAD COOPERATIVA DE AHORRO Y PRESTAMO, SC DE AP DE RL DE CV"); ps.setString(2,"BBV100422DY7"); ps.addBatch();
                ps.setString(1,"BANRENACES CACAHOATAN, SC DE AP DE RL DE CV"); ps.setString(2,"BCA100329EG9"); ps.addBatch();
                ps.setString(1,"BANRENACES CALVILLO SOCIEDAD COOPERATIVA DE AHORRO Y PRESTAMO, SC DE AP DE RL DE CV"); ps.setString(2,"BCA101124KTA"); ps.addBatch();
                ps.setString(1,"BANRENACES CAPULTITLAN\"\", SC DE AP DE RL DE CV"); ps.setString(2,"BCA100326A77"); ps.addBatch();
                ps.setString(1,"BANRENACES CIENEGA GRANDE, SC DE AP DE RL DE CV"); ps.setString(2,"BCG101014DE3"); ps.addBatch();
                ps.setString(1,"BANRENACES CIUDAD DE MEXICO, SC DE AP DE RL DE CV"); ps.setString(2,"BCM100428KZ5"); ps.addBatch();
                ps.setString(1,"BANRENACES COLIMA, SC DE AP DE RL DE CV"); ps.setString(2,"BCO100214CL7"); ps.addBatch();
                ps.setString(1,"BANRENACES CONSTANCIA MOCHICAHUI, SOCIEDAD COOPERATIVA DE AHORRO Y PRESTAMO, SC DE AP DE RL DE CV"); ps.setString(2,"BCM110204KS5"); ps.addBatch();
                ps.setString(1,"BANRENACES CORITA DE ACAPONETA, SC DE AP DE RL DE CV"); ps.setString(2,"BCA1011166V1"); ps.addBatch();
                ps.setString(1,"BANRENACES CORONEO, SC DE AP DE RL DE CV"); ps.setString(2,"BCO1010011T0"); ps.addBatch();
                ps.setString(1,"BANRENACES CORTAZAR, SC DE AP DE RL DE CV"); ps.setString(2,"BCO1009088C6"); ps.addBatch();
                ps.setString(1,"BANRENACES COSOLAPA, SC DE AP DE RL DE CV"); ps.setString(2,"BCO100513K51"); ps.addBatch();
                ps.setString(1,"BANRENACES CUERNAVACA, SC DE AP DE RL DE CV"); ps.setString(2,"BCU110506SJ3"); ps.addBatch();
                ps.setString(1,"BANRENACES CUICATLAN, SC DE AP DE RL DE CV"); ps.setString(2,"BCU110107GJ7"); ps.addBatch();
                ps.setString(1,"BANRENACES CUNA DEL EQUIPAL SOCIEDAD COOPERATIVA DE AHORRO Y PRESTAMO, SC DE AP DE RL DE CV"); ps.setString(2,"BCE110903QM7"); ps.addBatch();
                ps.setString(1,"BANRENACES DE DURANGO, SC DE AP DE RL DE CV"); ps.setString(2,"BDU100530UH7"); ps.addBatch();
                ps.setString(1,"BANRENACES DE TOCOY DE SAN ANTONIO, SC DE AP DE RL DE CV"); ps.setString(2,"BTS110610ILA"); ps.addBatch();
                ps.setString(1,"BANRENACES DEL NAYAR SOCIEDAD COOPERATIVA DE AHORRO Y PRESTAMO, SC DE AP DE RL DE CV"); ps.setString(2,"BNA101215K85"); ps.addBatch();
                ps.setString(1,"BANRENACES DEL SUR, SC DE AP DE RL DE CV"); ps.setString(2,"BSU100524C34"); ps.addBatch();
                ps.setString(1,"BANRENACES DOLORES HIDALGO CUNA DE LA INDEPENDENCIA GUANAJUATO, SC DE AP DE RL DE CV"); ps.setString(2,"BDH100114E91"); ps.addBatch();
                ps.setString(1,"BANRENACES EJUTLA, SC DE AP DE RL DE CV"); ps.setString(2,"BEJ1009076H0"); ps.addBatch();
                ps.setString(1,"BANRENACES GENERAL BRAVO, SC DE AP DE RL DE CV"); ps.setString(2,"BGB100417FJ8"); ps.addBatch();
                ps.setString(1,"BANRENACES HEROICO PUERTO DE SAN BLAS, SC DE AP DE RL DE CV"); ps.setString(2,"BHP101108AS8"); ps.addBatch();
                ps.setString(1,"BANRENACES HIDALGO, SC DE AP DE RL DE CV"); ps.setString(2,"BHI100202H31"); ps.addBatch();
                ps.setString(1,"BANRENACES HORMIGUERO, SC DE AP DE RL DE CV"); ps.setString(2,"BHO1008164Z1"); ps.addBatch();
                ps.setString(1,"BANRENACES HUIMILPAN, SC DE AP DE RL DE CV"); ps.setString(2,"BHU120504C55"); ps.addBatch();
                ps.setString(1,"BANRENACES HUIXQUILUCAN, SC DE AP DE RL DE CV"); ps.setString(2,"BHU100915D49"); ps.addBatch();
                ps.setString(1,"BANRENACES IGUALA, SC DE AP DE RL DE CV"); ps.setString(2,"BIG100910AR8"); ps.addBatch();
                ps.setString(1,"BANRENACES JAHUARA SOCIEDAD COOPERATIVA DE AHORRO Y PRESTAMO, SC DE AP DE RL DE CV"); ps.setString(2,"BJA110204DF9"); ps.addBatch();
                ps.setString(1,"BANRENACES JIREH EN ZAPOPAN, SC DE AP DE RL DE CV"); ps.setString(2,"BJE111006878"); ps.addBatch();
                ps.setString(1,"BANRENACES JOCOTITLAN, SC DE AP DE RL DE CV"); ps.setString(2,"BJO1004274N9"); ps.addBatch();
                ps.setString(1,"BANRENACES LA VILLA, SC DE AP DE RL DE CV"); ps.setString(2,"BVI100518BHA"); ps.addBatch();
                ps.setString(1,"BANRENACES LEON, SC DE AP DE RL DE CV"); ps.setString(2,"BLE1012293T6"); ps.addBatch();
                ps.setString(1,"BANRENACES MANZANILLO, SC DE AP DE RL DE CV"); ps.setString(2,"BMA1010089R3"); ps.addBatch();
                ps.setString(1,"BANRENACES MARIPOSAS NUEVO LEON, SC DE AP DE RL DE CV"); ps.setString(2,"BMN101126GBA"); ps.addBatch();
                ps.setString(1,"BANRENACES MONTESCLAROS SOCIEDAD COOPERATIVA DE AHORRO Y PRESTAMO, SC DE AP DE RL DE CV"); ps.setString(2,"BMA110310L68"); ps.addBatch();
                ps.setString(1,"BANRENACES MORELIA, SC DE AP DE RL DE CV"); ps.setString(2,"BMO101208NT4"); ps.addBatch();
                ps.setString(1,"BANRENACES MORELOS SOCIEDAD COOPERATIVA DE AHORRO Y PRESTAMO, SC DE AP DE RL DE CV"); ps.setString(2,"BMA100930TG0"); ps.addBatch();
                ps.setString(1,"BANRENACES NAUCALPAN, SC DE AP DE RL DE CV"); ps.setString(2,"BNA100413EW9"); ps.addBatch();
                ps.setString(1,"BANRENACES NOPALA, SC DE AP DE RL DE CV"); ps.setString(2,"BNO100210AL7"); ps.addBatch();
                ps.setString(1,"BANRENACES OAXACA, SC DE AP DE RL DE CV"); ps.setString(2,"BOA1008014A8"); ps.addBatch();
                ps.setString(1,"BANRENACES OCTOPAN, SC DE AP DE RL DE CV"); ps.setString(2,"BOC101027JI0"); ps.addBatch();
                ps.setString(1,"BANRENACES PESQUERIA, SC DE AP DE RL DE CV"); ps.setString(2,"BPE110721ER0"); ps.addBatch();
                ps.setString(1,"BANRENACES POR BOCA DEL RIO, SC DE AP DE RL DE CV"); ps.setString(2,"BBR100805RG3"); ps.addBatch();
                ps.setString(1,"BANRENACES POR JALCOMULCO, SC DE AP DE RL DE CV"); ps.setString(2,"BJA111028JX9"); ps.addBatch();
                ps.setString(1,"BANRENACES POR KUALI, SC DE AP DE RL DE CV"); ps.setString(2,"BKU100705S24"); ps.addBatch();
                ps.setString(1,"BANRENACES POR MEDELLIN DE BRAVO, SC DE RL DE CV"); ps.setString(2,"BMD101026J91"); ps.addBatch();
                ps.setString(1,"BANRENACES POR TANTOYUCA, SC DE AP DE RL DE CV"); ps.setString(2,"BTA1006262U9"); ps.addBatch();
                ps.setString(1,"BANRENACES PUERTO ESCONDIDO, SC DE AP DE RL DE CV"); ps.setString(2,"BPE110919HQ9"); ps.addBatch();
                ps.setString(1,"BANRENACES QUERETARO, SC DE AP DE RL DE CV"); ps.setString(2,"BQU110418IF4"); ps.addBatch();
                ps.setString(1,"BANRENACES QUINTANA ROO, SC DE AP DE RL DE CV"); ps.setString(2,"BQR110511TKA"); ps.addBatch();
                ps.setString(1,"BANRENACES RAMONES, SC DE AP DE RL DE CV"); ps.setString(2,"BRA091222MJ9"); ps.addBatch();
                ps.setString(1,"BANRENACES RINCON DE MORELOS SOCIEDAD COOPERATIVA DE AHORRO Y PRESTAMO, SC DE AP DE RL DE CV"); ps.setString(2,"BRM100816FR3"); ps.addBatch();
                ps.setString(1,"BANRENACES SAHUAYO, SC DE AP DE RL DE CV"); ps.setString(2,"BSA101123NK3"); ps.addBatch();
                ps.setString(1,"BANRENACES SALAMANCA, SC DE AP DE RL DE CV"); ps.setString(2,"BSA1010213Y3"); ps.addBatch();
                ps.setString(1,"BANRENACES SAN FRANCISCO TUTLA, SC DE AP DE RL DE CV"); ps.setString(2,"BSF1005172M6"); ps.addBatch();
                ps.setString(1,"BANRENACES SAN LUIS POTOSI, SC DE AP DE RL DE CV"); ps.setString(2,"BSL100202UJ7"); ps.addBatch();
                ps.setString(1,"BANRENACES SANTA MARIA DEL RIO, SC DE AP DE RL DE CV"); ps.setString(2,"BSM100423UP1"); ps.addBatch();
                ps.setString(1,"BANRENACES SANTO NIÑO, SC DE AP DE RL DE CV"); ps.setString(2,"BSN1009101S7"); ps.addBatch();
                ps.setString(1,"BANRENACES SOLEDAD SALINAS, SC DE AP DE RL DE CV"); ps.setString(2,"BSS1103013C4"); ps.addBatch();
                ps.setString(1,"BANRENACES SONORA, SC DE AP DE RL DE CV"); ps.setString(2,"BSO100521KW3"); ps.addBatch();
                ps.setString(1,"BANRENACES TEMASCALCINGO SOCIEDAD COOPERATIVA DE AHORRO Y PRESTAMO, SC DE AP DE RL DE CV"); ps.setString(2,"BTA100622781"); ps.addBatch();
                ps.setString(1,"BANRENACES TEOTITLAN DEL VALLE, SC DE AP DE RL DE CV"); ps.setString(2,"BTV091227VC6"); ps.addBatch();
                ps.setString(1,"BANRENACES TEPECOACUILCO, SC DE AP DE RL DE CV"); ps.setString(2,"BTE110118F78"); ps.addBatch();
                ps.setString(1,"BANRENACES TEQUISISTLAN, SC DE AP DE RL DE CV"); ps.setString(2,"BTE100802SY4"); ps.addBatch();
                ps.setString(1,"BANRENACES TIERRA COLORADA, SC DE AP DE RL DE CV"); ps.setString(2,"BTC100920377"); ps.addBatch();
                ps.setString(1,"BANRENACES TLACOLULA, SC DE AP DE RL DE CV"); ps.setString(2,"BTL110919I60"); ps.addBatch();
                ps.setString(1,"BANRENACES UNIDAD Y CONFIANZA SOCIEDAD COOPERATIVA DE AHORRO Y PRESTAMO, SC DE AP DE RL DE CV"); ps.setString(2,"BUC1009229W6"); ps.addBatch();
                ps.setString(1,"BANRENACES UNIENDO ESFUERZOS Y REALIZANDO SUEÑOS, SC DE AP DE RL DE CV"); ps.setString(2,"BUE110823GDA"); ps.addBatch();
                ps.setString(1,"BANRENACES VANEGAS, SC DE AP DE RL DE CV"); ps.setString(2,"BVA1103109CA"); ps.addBatch();
                ps.setString(1,"BANRENACES VILLACORZO, SC DE AP DE RL DE CV"); ps.setString(2,"BVI100421RHA"); ps.addBatch();
                ps.setString(1,"BANRENACES XALTIANGUIS, SC DE AP DE RL DE CV"); ps.setString(2,"BXA091210I49"); ps.addBatch();
                ps.setString(1,"BANRENACES XOCOTEPEC, SC DE AP DE RL DE CV"); ps.setString(2,"BXO1003277F0"); ps.addBatch();
                ps.setString(1,"BANRENACES ZARAGOZA, SC DE AP DE RL DE CV"); ps.setString(2,"BZA100423AN3"); ps.addBatch();
                ps.setString(1,"BANSI, SA"); ps.setString(2,"BAN950525MD6"); ps.addBatch();
                ps.setString(1,"BBVA BANCOMER SA INSTITUCION DE BANCA MULTIPLE, GRUPO FINANCIERO BBVA BANCOMER"); ps.setString(2,"BBA830831LJ2"); ps.addBatch();
                ps.setString(1,"CAJA DE AHORRO DE LOS TRABAJADORES CUICACALLI, AC"); ps.setString(2,"CAT080804JG2"); ps.addBatch();
                ps.setString(1,"CAJA DE PRESTAMOS PARA TRABAJADORES DEL SISTEMA VALLADOLID, AC"); ps.setString(2,"CPT090930CE3"); ps.addBatch();
                ps.setString(1,"CAJA POPULAR CRISTOBAL COLON, SC DE AP DE RL DE CV"); ps.setString(2,"CPC780925B36"); ps.addBatch();
                ps.setString(1,"CAJA POPULAR SANTIAGO TINGAMBATO, SC DE AP DE RL DE CV"); ps.setString(2,"CPS9607177X2"); ps.addBatch();
                ps.setString(1,"CAJA SOLIDARIA TOMATLAN, SC DE AP DE RL DE CV"); ps.setString(2,"CST010410RBA"); ps.addBatch();
                ps.setString(1,"CASA DE BOLSA VE POR MAS SA DE CV GRUPO FINANCIERO VE POR MAS"); ps.setString(2,"CBA8701315H8"); ps.addBatch();
                ps.setString(1,"CIBANCO, SA"); ps.setString(2,"CIB850918BN8"); ps.addBatch();
                ps.setString(1,"DEUTSCHE BANK MEXICO SA INSTITUCION DE BANCA MULTIPLE"); ps.setString(2,"DBM000228J35"); ps.addBatch();
                ps.setString(1,"FONDO NACIONAL DE PENSIONES DE LOS TRABAJADORES AL SERVICIO DEL ESTADO"); ps.setString(2,"FNP070401RN9"); ps.addBatch();
                ps.setString(1,"HSBC MEXICO SA INSTITUCION DE BANCA MULTIPLE GRUPO FINANCIERO HSBC"); ps.setString(2,"HMI950125KG8"); ps.addBatch();
                ps.setString(1,"HSBC GLOBAL ASSET MANAGEMENT (MEXICO), SA DE CV, GRUPO FINANCIERO HSBC"); ps.setString(2,"HGA860129IW7"); ps.addBatch();
                ps.setString(1,"ING BANK MEXICO SA INSTITUCION DE BANCA MULTIPLE ING GRUPO FINANCIERO"); ps.setString(2,"IBM951129P29"); ps.addBatch();
                ps.setString(1,"INTER BANCO SA INSTITUCION DE BANCA MULTIPLE"); ps.setString(2,"IBI061030GD4"); ps.addBatch();
                ps.setString(1,"IXE BANCO SA INSTITUCION DE BANCA MULTIPLE GRUPO FINANCIERO BANORTE"); ps.setString(2,"IBA950503GTA"); ps.addBatch();
                ps.setString(1,"SCOTIABANK INVERLAT, SA"); ps.setString(2,"SIN9412025I4"); ps.addBatch();
                ps.setString(1,"SOLIDEZ DE MONTEALBAN, SC DE AP DE RL DE CV"); ps.setString(2,"SDM100205U72"); ps.addBatch();
                ps.setString(1,"TRABAJADORES ELECTRICISTAS RIO COLORADO, SC DE RL DE CV"); ps.setString(2,"TER0902249W7"); ps.addBatch();
                ps.setString(1,"UNION DE CREDITO GANADERO DE TABASCO, SA DE CV"); ps.setString(2,"UCG470616988"); ps.addBatch();
                ps.setString(1,"UNION DE CREDITO INDUSTRIAL Y AGROPECUARIA DE TABASCO, SA DE CV"); ps.setString(2,"UCI921211QH3"); ps.addBatch();
                
                ps.executeBatch();
                
                log.info("Catalogo insertado correctamente");
                
                if(stmt != null)
                    stmt.close();
                con.commit();
                con.close();
            }catch(SQLException ex){
                ex.printStackTrace();
                String error = "Error al crear la tabla de c_bancos: " + ex.getMessage();
                log.error(error, ex);
                try{
                    con.rollback();
                    con.close();
                }catch(SQLException exx){
                    exx.printStackTrace();
                    log.error("Excepcion al cerrar la conexion con la base de datos o al hacer rollback despues de intentar crear la tabla c_bancos: ", exx);
                }
                util.printError(error);
            }
        }
    }
    
    private void verificarCuentasFolios(){
        Connection con = Elemento.odbc();
        Statement stmt = null;
        ResultSet rs;
        
        try{
            stmt = con.createStatement();
            rs = stmt.executeQuery("Select top 1 cuenta_id from Cuentas");
            rs.close();
            
            rs = stmt.executeQuery("Select top 1 cuenta_id from Folios");
            rs.close();
            
            stmt.close();
            con.close();
        }catch(SQLException ex){
            log.warn("No existen los campos, se crearan...");
            try{
                if(ex.getMessage().toUpperCase().contains("CUENTA_ID")){
                    
                    // write a temporary VBScript file ...
                    File vbsFile = File.createTempFile("AlterTable", ".vbs");
                    vbsFile.deleteOnExit();
                    PrintWriter pw = new PrintWriter(vbsFile);
                    pw.println("Set conn = CreateObject(\"ADODB.Connection\")");
                    pw.println("conn.Open \"Driver={Microsoft Access Driver (*.mdb)};Dbq=" + this.dataBase + "; Pwd=" + this.password + ";\"");
                    pw.println("conn.Execute \"ALTER TABLE Cuentas drop column facturas\"");
                    pw.println("conn.Execute \"ALTER TABLE Cuentas drop column notasCredito\"");
                    pw.println("conn.Execute \"ALTER TABLE Cuentas drop column recibosDonativos\"");
                    pw.println("conn.Execute \"ALTER TABLE Cuentas DROP CONSTRAINT PrimaryKey\"");
                    pw.println("conn.Execute \"ALTER TABLE Cuentas ADD COLUMN cuenta_id AUTOINCREMENT PRIMARY KEY\"");
                    pw.println("conn.Execute \"ALTER TABLE Folios ADD COLUMN cuenta_id INT\"");
                    pw.println("conn.Execute \"ALTER TABLE Folios ADD CONSTRAINT FK_folios_cuenta_id FOREIGN KEY (cuenta_id) REFERENCES Cuentas (cuenta_id)\"");
                    pw.println("conn.Execute \"UPDATE Folios f INNER JOIN Cuentas c ON c.rfc = f.rfc set f.cuenta_id = c.cuenta_id;\"");
                    pw.println("conn.Close");
                    pw.println("Set conn = Nothing");
                    pw.close();
                    
                    // ... and execute it
                    Process p = Runtime.getRuntime().exec("CSCRIPT.EXE \"" + vbsFile.getAbsolutePath() + "\"");
                    p.waitFor();
                    BufferedReader rdr = 
                            new BufferedReader(new InputStreamReader(p.getErrorStream()));
                    int errorLines = 0;
                    String line = rdr.readLine();
                    while (line != null) {
                        errorLines++;
                        System.out.println(line);  // display error line(s), if any
                        line = rdr.readLine();
                    }
                    if (errorLines == 0) {
                        System.out.println("La estructura de la base de datos se cambio exitosamente");
                        log.info("La estructura de la base de datos se cambio exitosamente");
                    }else{
                        System.out.println(line);
                        log.error("Error al reestructurar la base de datos: \r\n" + line);
                    }

                    stmt.close();
                }
                
                con.close();
            }catch(Exception e){
                e.printStackTrace();
                String error = "Error al actualizar la estructura de la base de datos a las tablas Cuentas y/o Folios";
                log.error(error, e);
                try{
                    con.close();
                }catch(SQLException sex){
                    sex.printStackTrace();
                    log.error("Error al cerrar la conexion despues de intentar actualizar estructura de las tablas Cuentas y/o Folios: ", sex);
                }
                util.printError(error);
            }
        }
    }
    
    private void verificarRegimenFiscal(){
        Connection con = Elemento.odbc();
        Statement stmt = null;
        ResultSet rs;
        boolean crearTabla = false;
        boolean insertarDatos = false;
        
        try{
            stmt = factory.stmtEscritura(con);
            if(stmt != null){
                rs = stmt.executeQuery("Select top 1 cfdiv4 from c_regimenfiscal");
                insertarDatos = !rs.next();
                rs.close();
                stmt.close();
            }
            
            con.close();
        }catch(SQLException ex){
            try{
                crearTabla = true;
                if(ex.getMessage().toUpperCase().contains("CFDIV4")){
                    if(stmt != null){
                        stmt.executeUpdate("drop table c_regimenfiscal");
                    }
                }else if(!ex.getMessage().toUpperCase().contains("C_REGIMENFISCAL")){
                    ex.printStackTrace();
                }
            }catch(SQLException e){
                e.printStackTrace();
                crearTabla = false;
                String error = "Error al actualizar la estructura de la tabla c_regimenfiscal: " + e.getMessage();
                Elemento.log.error(error, e);
                try{
                    con.close();
                }catch(SQLException sex){
                    sex.printStackTrace();
                    Elemento.log.error("Error al cerrar la conexion despues de crear la tabla c_regimenfiscal: ", sex);
                }
                util.printError(error);
            }
        }finally{
            try {
                if(con == null || con.isClosed()){
                    con = Elemento.odbc();
                }

                if(crearTabla){
                    stmt = stmt != null && !stmt.isClosed() ? stmt : factory.stmtEscritura(con);
                    con.setAutoCommit(false);
                    log.warn("No existe la tabla c_regimenfiscal, se creara...");
                    
                    stmt.executeUpdate(
                        "CREATE TABLE c_regimenfiscal "
                        + "("
                            + "c_regimenfiscal_id AUTOINCREMENT, "
                            + "regimenfiscal varchar(10) not null, "
                            + "descripcion varchar(255) not null, "
                            + "fisica yesno not null, "
                            + "moral yesno not null, "
                            + "cfdiv4 bit default null, "
                            + "CONSTRAINT cregimenfiscalid_PK PRIMARY KEY(c_regimenfiscal_id)"
                        + ")"
                    );

                    stmt.close();
                }
                    
                if(crearTabla || insertarDatos){
                    log.info("Se procede a ingresar el catalogo");
                    
                    java.sql.PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO c_regimenfiscal "
                        + "("
                            + "regimenfiscal, "
                            + "descripcion, "
                            + "fisica, "
                            + "moral"
                        + ") "
                        + "values (?,?,?,?)"
                    );

                    ps.setString(1, "601"); ps.setString(2, "General de Ley Personas Morales"); ps.setBoolean(3, Boolean.FALSE); ps.setBoolean(4, Boolean.TRUE); ps.addBatch();
                    ps.setString(1, "603"); ps.setString(2, "Personas Morales con Fines no Lucrativos"); ps.setBoolean(3, Boolean.FALSE); ps.setBoolean(4, Boolean.TRUE); ps.addBatch();
                    ps.setString(1, "605"); ps.setString(2, "Sueldos y Salarios e Ingresos Asimilados a Salarios"); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.addBatch();
                    ps.setString(1, "606"); ps.setString(2, "Arrendamiento"); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.addBatch();
                    ps.setString(1, "607"); ps.setString(2, "Régimen de Enajenación o Adquisición de Bienes"); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.addBatch();
                    ps.setString(1, "608"); ps.setString(2, "Demás ingresos"); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.addBatch();
                    ps.setString(1, "610"); ps.setString(2, "Residentes en el Extranjero sin Establecimiento Permanente en México"); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.TRUE); ps.addBatch();
                    ps.setString(1, "611"); ps.setString(2, "Ingresos por Dividendos (socios y accionistas)"); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.addBatch();
                    ps.setString(1, "612"); ps.setString(2, "Personas Físicas con Actividades Empresariales y Profesionales"); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.addBatch();
                    ps.setString(1, "614"); ps.setString(2, "Ingresos por intereses"); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.addBatch();
                    ps.setString(1, "615"); ps.setString(2, "Régimen de los ingresos por obtención de premios"); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.addBatch();
                    ps.setString(1, "616"); ps.setString(2, "Sin obligaciones fiscales"); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.addBatch();
                    ps.setString(1, "620"); ps.setString(2, "Sociedades Cooperativas de Producción que optan por diferir sus ingresos"); ps.setBoolean(3, Boolean.FALSE); ps.setBoolean(4, Boolean.TRUE); ps.addBatch();
                    ps.setString(1, "621"); ps.setString(2, "Incorporación Fiscal"); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.addBatch();
                    ps.setString(1, "622"); ps.setString(2, "Actividades Agrícolas, Ganaderas, Silvícolas y Pesqueras"); ps.setBoolean(3, Boolean.FALSE); ps.setBoolean(4, Boolean.TRUE); ps.addBatch();
                    ps.setString(1, "623"); ps.setString(2, "Opcional para Grupos de Sociedades"); ps.setBoolean(3, Boolean.FALSE); ps.setBoolean(4, Boolean.TRUE); ps.addBatch();
                    ps.setString(1, "624"); ps.setString(2, "Coordinados"); ps.setBoolean(3, Boolean.FALSE); ps.setBoolean(4, Boolean.TRUE); ps.addBatch();
                    ps.setString(1, "625"); ps.setString(2, "Régimen de las Actividades Empresariales con ingresos a través de Plataformas Tecnológicas"); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.addBatch();
                    ps.setString(1, "626"); ps.setString(2, "Régimen Simplificado de Confianza"); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.TRUE); ps.addBatch();

                    ps.executeBatch();

                    ps.close();
                }
                con.commit();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                
                try{
                    if(con != null && !con.isClosed()){
                        con.rollback();
                        con.close();
                    }
                }catch(SQLException ex){
                    ex.printStackTrace();
                    Elemento.log.error("Error al cerrar la base de datos despues de error al actualizar la tabla c_regimenfiscal: " + ex.getMessage(), ex);
                }
                
                String error = "Error al actualizar la tabla c_regimenfiscal: " + e.getMessage();
                Elemento.log.error(error, e);
                util.printError(error);
            }
        }
    }
    
    private void verificarUsoCfdi(){
        Connection con = Elemento.odbc();
        Statement stmt = null;
        ResultSet rs;
        boolean crearTabla = false;
        boolean insertarDatos = false;
        
        try{
            stmt = factory.stmtEscritura(con);
            if(stmt != null){
                rs = stmt.executeQuery("Select top 1 regimenFiscalReceptor from c_usocfdi");
                insertarDatos = !rs.next();
                rs.close();
                stmt.close();
            }
            
            con.close();
        }catch(SQLException ex){
            try{
                crearTabla = true;
                if(ex.getMessage().toUpperCase().contains("REGIMENFISCALRECEPTOR")){
                    if(stmt != null){
                        stmt.executeUpdate("drop table c_usocfdi");
                    }
                }else if(!ex.getMessage().toUpperCase().contains("C_USOCFDI")){
                    ex.printStackTrace();
                }
                
            }catch(SQLException e){
                e.printStackTrace();
                crearTabla = false;
                String error = "Error al actualizar la estructura de la tabla c_usocfdi: " + e.getMessage();
                Elemento.log.error(error, e);
                try{
                    con.close();
                }catch(SQLException sex){
                    sex.printStackTrace();
                    Elemento.log.error("Error al cerrar la conexion despues de crear la tabla c_usocfdi: ", sex);
                }
                util.printError(error);
            }
        }finally{
            try {
                if(con == null || con.isClosed()){
                    con = Elemento.odbc();
                }
                
                con.setAutoCommit(false);
                if(crearTabla){
                    log.warn("No existe la tabla c_usocfdi, se creara...");

                        if(stmt != null){
                            stmt.executeUpdate(
                                "CREATE TABLE c_usocfdi "
                                + "("
                                    + "c_usocfdi_id AUTOINCREMENT, "
                                    + "usocfdi varchar(10) not null, "
                                    + "descripcion varchar(255) not null, "
                                    + "aplicaFisica yesno not null, "
                                    + "aplicaMoral yesno not null, "
                                    + "regimenFiscalReceptor varchar(500) not null, "
                                    + "CONSTRAINT cusocfdiid_PK PRIMARY KEY(c_usocfdi_id)"
                                + ")"
                            );

                            stmt.close();
                        }
                }
                
                if(crearTabla || insertarDatos){
                    log.info("Se procede a ingresar el catalogo");

                    java.sql.PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO c_usocfdi "
                        + "("
                            + "usocfdi, "
                            + "descripcion, "
                            + "aplicaFisica, "
                            + "aplicaMoral, "
                            + "regimenFiscalReceptor"
                        + ") "
                        + "values (?,?,?,?,?)"
                    );

                    ps.setString(1, "G01"); ps.setString(2, "Adquisición de mercancías."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.TRUE); ps.setString(5, "601,603,606,612,620,621,622,623,624,625,626"); ps.addBatch();
                    ps.setString(1, "G02"); ps.setString(2, "Devoluciones, descuentos o bonificaciones."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.TRUE); ps.setString(5, "601,603,606,612,620,621,622,623,624,625,626"); ps.addBatch();
                    ps.setString(1, "G03"); ps.setString(2, "Gastos en general."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.TRUE); ps.setString(5, "601,603,606,612,620,621,622,623,624,625,626"); ps.addBatch();
                    ps.setString(1, "I01"); ps.setString(2, "Construcciones."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.TRUE); ps.setString(5, "601,603,606,612,620,621,622,623,624,625,626"); ps.addBatch();
                    ps.setString(1, "I02"); ps.setString(2, "Mobiliario y equipo de oficina por inversiones."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.TRUE); ps.setString(5, "601,603,606,612,620,621,622,623,624,625,626"); ps.addBatch();
                    ps.setString(1, "I03"); ps.setString(2, "Equipo de transporte."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.TRUE); ps.setString(5, "601,603,606,612,620,621,622,623,624,625,626"); ps.addBatch();
                    ps.setString(1, "I04"); ps.setString(2, "Equipo de computo y accesorios."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.TRUE); ps.setString(5, "601,603,606,612,620,621,622,623,624,625,626"); ps.addBatch();
                    ps.setString(1, "I05"); ps.setString(2, "Dados, troqueles, moldes, matrices y herramental."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.TRUE); ps.setString(5, "601,603,606,612,620,621,622,623,624,625,626"); ps.addBatch();
                    ps.setString(1, "I06"); ps.setString(2, "Comunicaciones telefónicas."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.TRUE); ps.setString(5, "601,603,606,612,620,621,622,623,624,625,626"); ps.addBatch();
                    ps.setString(1, "I07"); ps.setString(2, "Comunicaciones satelitales."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.TRUE); ps.setString(5, "601,603,606,612,620,621,622,623,624,625,626"); ps.addBatch();
                    ps.setString(1, "I08"); ps.setString(2, "Otra maquinaria y equipo."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.TRUE); ps.setString(5, "601,603,606,612,620,621,622,623,624,625,626"); ps.addBatch();
                    ps.setString(1, "D01"); ps.setString(2, "Honorarios médicos, dentales y gastos hospitalarios."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.setString(5, "605,606,608,611,612,614,607,615,625"); ps.addBatch();
                    ps.setString(1, "D02"); ps.setString(2, "Gastos médicos por incapacidad o discapacidad."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.setString(5, "605,606,608,611,612,614,607,615,625"); ps.addBatch();
                    ps.setString(1, "D03"); ps.setString(2, "Gastos funerales."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.setString(5, "605,606,608,611,612,614,607,615,625"); ps.addBatch();
                    ps.setString(1, "D04"); ps.setString(2, "Donativos."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.setString(5, "605,606,608,611,612,614,607,615,625"); ps.addBatch();
                    ps.setString(1, "D05"); ps.setString(2, "Intereses reales efectivamente pagados por créditos hipotecarios (casa habitación)."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.setString(5, "605,606,608,611,612,614,607,615,625"); ps.addBatch();
                    ps.setString(1, "D06"); ps.setString(2, "Aportaciones voluntarias al SAR."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.setString(5, "605,606,608,611,612,614,607,615,625"); ps.addBatch();
                    ps.setString(1, "D07"); ps.setString(2, "Primas por seguros de gastos médicos."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.setString(5, "605,606,608,611,612,614,607,615,625"); ps.addBatch();
                    ps.setString(1, "D08"); ps.setString(2, "Gastos de transportación escolar obligatoria."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.setString(5, "605,606,608,611,612,614,607,615,625"); ps.addBatch();
                    ps.setString(1, "D09"); ps.setString(2, "Depósitos en cuentas para el ahorro, primas que tengan como base planes de pensiones."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.setString(5, "605,606,608,611,612,614,607,615,625"); ps.addBatch();
                    ps.setString(1, "D10"); ps.setString(2, "Pagos por servicios educativos (colegiaturas)."); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.setString(5, "605,606,608,611,612,614,607,615,625"); ps.addBatch();
                    ps.setString(1, "S01"); ps.setString(2, "Sin efectos fiscales.  "); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.TRUE); ps.setString(5, "601,603,605,606,608,610,611,612,614,616,620,621,622,623,624,607,615,625,626"); ps.addBatch();
                    ps.setString(1, "CP01"); ps.setString(2, "Pagos"); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.TRUE); ps.setString(5, "601,603,605,606,608,610,611,612,614,616,620,621,622,623,624,607,615,625,626"); ps.addBatch();
                    ps.setString(1, "CN01"); ps.setString(2, "Nómina"); ps.setBoolean(3, Boolean.TRUE); ps.setBoolean(4, Boolean.FALSE); ps.setString(5, "605"); ps.addBatch();

                    ps.executeBatch();
                    
                    ps.close();
                }
                con.commit();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                
                try{
                    if(con != null && !con.isClosed()){
                        con.rollback();
                        con.close();
                    }
                }catch(SQLException ex){
                    ex.printStackTrace();
                    Elemento.log.error("Error al cerrar la base de datos despues de error al actualizar la tabla c_usocfdi: " + ex.getMessage(), ex);
                }
                
                String error = "Error al actualizar la tabla c_usocfdi: " + e.getMessage();
                Elemento.log.error(error, e);
                util.printError(error);
            }
        }
    }
    
    private void verificarClientes(){
        Connection con = Elemento.odbc();
        Statement stmt = null;
        ResultSet rs;
        
        try{
            stmt = con.createStatement();
            rs = stmt.executeQuery("Select top 1 regimenfiscal, tipoPersona from Clientes");
            rs.close();
            
            stmt.close();
            con.close();
        }catch(SQLException ex){
            log.warn("No existen los campos, se crearan...");
            try{
                if(ex.getMessage().toUpperCase().contains("REGIMENFISCAL") || ex.getMessage().toUpperCase().contains("TIPOPERSONA")){
                    
                    // write a temporary VBScript file ...
                    File vbsFile = File.createTempFile("AlterTable", ".vbs");
                    vbsFile.deleteOnExit();
                    PrintWriter pw = new PrintWriter(vbsFile);
                    pw.println("Set conn = CreateObject(\"ADODB.Connection\")");
                    pw.println("conn.Open \"Driver={Microsoft Access Driver (*.mdb)};Dbq=" + this.dataBase + "; Pwd=" + this.password + ";\"");
                    pw.println("conn.Execute \"ALTER TABLE Clientes ADD COLUMN regimenfiscal VARCHAR(10)\"");
                    pw.println("conn.Execute \"ALTER TABLE Clientes ADD COLUMN tipoPersona VARCHAR(1)\"");
                    pw.println("conn.Execute \"UPDATE Clientes SET regimenfiscal = '', tipoPersona = IIF(LEN(TRIM(rfc)) = 12, 'M', 'F')\"");
                    pw.println("conn.Close");
                    pw.println("Set conn = Nothing");
                    pw.close();
                    
                    // ... and execute it
                    Process p = Runtime.getRuntime().exec("CSCRIPT.EXE \"" + vbsFile.getAbsolutePath() + "\"");
                    p.waitFor();
                    BufferedReader rdr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                    int errorLines = 0;
                    String line = rdr.readLine();
                    while (line != null) {
                        errorLines++;
                        System.out.println(line);  // display error line(s), if any
                        line = rdr.readLine();
                    }
                    if (errorLines == 0) {
                        System.out.println("La estructura de la base de datos se cambio exitosamente");
                        log.info("La estructura de la base de datos se cambio exitosamente");
                    }else{
                        System.out.println(line);
                        log.error("Error al reestructurar la base de datos: \r\n" + line);
                    }

                    stmt.close();
                }
                
                con.close();
            }catch(Exception e){
                e.printStackTrace();
                String error = "Error al actualizar la estructura de la base de datos a la tabla Clientes";
                log.error(error, e);
                try{
                    con.close();
                }catch(SQLException sex){
                    sex.printStackTrace();
                    log.error("Error al cerrar la conexion despues de intentar actualizar estructura de la tabla Clientes: ", sex);
                }
                util.printError(error);
            }
        }
    }
    
    private void verificarEmisoresBancos(){
        Connection con = Elemento.odbc();
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT TOP 1 1 FROM EmisoresBancos");
            
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            log.info("No existe la tabla EmisoresBancos, se creara...");
            System.out.println("No existe la tabla EmisoresBancos, se creara...");
            
            try {
                if(stmt == null)
                    stmt = con.createStatement();
                
                stmt.executeUpdate("CREATE TABLE EmisoresBancos"
                        + "("
                            + "id_emisorBanco AUTOINCREMENT, "
                            + "id_emisor int not null, "
                            + "id_banco int not null, "
                            + "CONSTRAINT emisorBanco_PK PRIMARY KEY(id_emisorBanco),"
                            + "CONSTRAINT emisor_FK FOREIGN KEY(id_emisor) REFERENCES Emisores(id),"
                            + "CONSTRAINT banco_FK FOREIGN KEY(id_banco) REFERENCES c_bancos(id_banco)"
                        + ")");
                
            } catch (SQLException ex) {
                ex.printStackTrace();
                log.error("Error al crear la tabla EmisoresBancos: ", ex);
            }
        } finally{
            try{
                if(rs != null && !rs.isClosed())
                    rs.close();
                if(stmt != null && !stmt.isClosed())
                    stmt.close();
                if(con != null && !con.isClosed())
                    con.close();
            }catch(SQLException e){
                e.printStackTrace();
                Elemento.log.error("Error al cerrar la conexion para crear la tabla EmisoresBancos: ", e);
            }
        }
    }
}