/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package elemento;

import gui.Configurar;
import gui.Welcome;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.swing.JOptionPane;
import log.Log;
import net.contentobjects.jnotify.JNotify;
import org.apache.log4j.Logger;

/**
 *
 * @author Abe
 */
public class Elemento {

    public static Log logObject;
    public static Logger log;
    public static String noCertificado, logo, lugarExpedicion;
    public static String rfc, regimenFiscal, unidad;
    public static boolean produccion;
    public static final String HOLA = "El programa esta corriendo";
    public static final int PORT = 1334;
    private final static ConnectionFactory factory = new ConnectionFactory();
    public static String pathXml, pathPdf, pathXmlST, pathXmlMod, pathLayout, pathConfig, pathPlantillas, pathQR;
    public static String user, pass;
    private static String tipoConexion, baseDatos;
    public static String estructuraNombre;
    private static String tipoEnvioMail, pathOutlook;

    public Elemento() {
        new Thread() {
            @Override
            public void run() {
                listen();
            }
        }.start();

        Properties prop = new Properties();
        InputStream in;
        OutputStream out;
        File propFile = new File("propiedades.properties");
        try {
            if (propFile.exists()) {
                log.info("Se lee el archivo de propiedades");
                in = new FileInputStream(propFile);
                prop.load(in);
                tipoConexion = prop.getProperty("tipo_conexion");
                baseDatos = prop.getProperty("base_datos");
                estructuraNombre = prop.getProperty("estructura_nombre");
                String tipo = prop.getProperty("tipoEnvioMail");
                tipoEnvioMail = tipo == null ? "1" : tipo;
                if(tipo != null && tipo.equals("2")){
                    pathOutlook = prop.getProperty("pathOutlook");
                }
                in.close();
            } else {
                log.info("El archivo de propiedades no existe, se crea uno nuevo con los valores por default");
                out = new FileOutputStream(propFile);
                tipoConexion = "directo";
                baseDatos = unidad + ":\\Facturas\\config\\ElementoBD3.mdb";
                estructuraNombre = "serie_folio_rfce_rfcr_uuid";
                tipoEnvioMail = "1";
                
                prop.setProperty("tipo_conexion", tipoConexion);
                prop.setProperty("base_datos", baseDatos);
                prop.setProperty("estructura_nombre", estructuraNombre);
                prop.setProperty("tipoEnvioMail", tipoEnvioMail);
                prop.setProperty("pathOutlook", "C:\\Program Files (x86)\\Microsoft Office\\Root\\Office16\\OUTLOOK.EXE");
                
                prop.store(out, "En el tipo_conexion va \"archivo\" para cuando es un archivo .mdb o .accdb usando JDBC,\r\n"
                        + "\"odbc\" para cuando se configura un ODBC {Microsoft Access Driver (*.mdb)} en el panel de control de Windows\r\n"
                        + "y \"directo\" es igual que \"archivo\" pero con la diferencia que funciona con Java 8 utilizando UCanAccess.\r\n"
                        + "Cuando sea por archivo o directo debemos poner la ruta completa que tiene la base de datos.\r\n"
                        + "Cuando es odbc ponemos el nombre del ODBC creado.\r\n"
                        + "Para la estructura del nombre, el punto sirve para indicar que van juntos o pegados,\r\n"
                        + "Cualquier otro caracter sera un separador.\r\n"
                        + "Las palabras claves son:\r\n"
                        + "serie\r\nfolio\r\nrfce (RFC Emisor)\r\nrfcr (RFC Receptor)\r\nuuid\r\n"
                        + "Para el tipoEnvioMail serian 1 o 2, los cuales significan:\r\n"
                        + "\t1: Envio desde el Elemento por medio de JavaMail.\r\n"
                        + "\t2: Envio desde Outlook (previamente configurado).\r\n"
                        + "Cuando el tipo es 2, entonces debemos poner en pathOutlook la ruta del archivo exe de outlook, utilizando dobles diagonales (\\\\)\r\n"
                        + "en lugar de una diagonal (\\).");
                
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Excepcion al verificar el archivo de propiedades", e);
        }
        
        verificarBd();
    }

    private static void fileCopy(String sourceFile, String destinationFile) {
        try {
            File inFile = new File(sourceFile);
            File outFile = new File(destinationFile);

            FileInputStream in = new FileInputStream(inFile);
            FileOutputStream out = new FileOutputStream(outFile);

            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }

            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Hubo un error de entrada/salida!!!");
        }
    }

    public static void main(String[] args) {
        String path;
        String jVersion = "jre" + System.getProperty("java.version");
        String sistema = System.getProperty("os.name").trim();
        unidad = System.getenv("WINDIR").split(":")[0];
        System.out.println(sistema);
        pathXml = unidad + ":\\Facturas\\C_Procesados\\";
        pathPdf = unidad + ":\\Facturas\\D_Pdfs\\";
        pathLayout = unidad + ":\\Facturas\\B_Layout\\";
        pathXmlST = unidad + ":\\Facturas\\C_Interpretados\\";
        pathXmlMod = unidad + ":\\Facturas\\XmlModificados\\";
        pathConfig = unidad + ":\\Facturas\\config\\";
        pathPlantillas = unidad + ":\\Facturas\\config\\plantillas\\";
        pathQR = unidad + ":\\Facturas\\qrs\\";

        if (sistema.contains("Windows Vista") || sistema.contains("Windows 7") || sistema.contains("Windows 8") || sistema.contains("Windows 10")) {
            path = unidad + ":\\Program Files (x86)\\Java\\" + jVersion + "\\bin\\";
            if (!new File(path).exists()) {
                path = unidad + ":\\Program Files\\Java\\" + jVersion + "\\bin\\";
            }
        } else {
            path = unidad + ":\\Archivos de Programa\\Java\\" + jVersion + "\\bin\\";
        }
        path = System.getProperty("java.home");
        File dll = new File(unidad + ":\\Facturas\\jnotify.dll");
        File libso = new File(unidad + ":\\Facturas\\libjnotify.so");
        File programDll = new File(path + "\\bin\\jnotify.dll");
        File programLibso = new File(path + "\\bin\\libjnotify.so");

        File file1 = new File(unidad + ":\\Facturas");
        File file2 = new File(unidad + ":\\Facturas\\A_Archivos");
        File file3 = new File(pathLayout);
        File file4 = new File(pathXml);
        File file8 = new File(pathXmlST);
        File file5 = new File(unidad + ":\\Facturas\\logs");
        File file6 = new File(pathPdf);
        File file7 = new File(pathXmlMod);
        File file9 = new File(pathConfig);
        File file10 = new File(pathPlantillas);
        File file11 = new File(pathQR);

        file1.mkdir();
        file2.mkdirs();
        file3.mkdir();
        file4.mkdir();
        file5.mkdir();
        file6.mkdir();
        file7.mkdir();
        file8.mkdir();
        file9.mkdir();
        file10.mkdir();
        file11.mkdir();

        if (!programDll.exists()) {
            fileCopy(dll.getAbsolutePath(), programDll.getAbsolutePath());
            fileCopy(libso.getAbsolutePath(), programLibso.getAbsolutePath());
        } else {
            System.out.println("Ya existe la DLL");
        }

        try {
            logObject = new Log(unidad + ":/Facturas/");
            log = logObject.getLog();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Elemento Integrador iniciado correctamente");
        new DemoTray();
        new Elemento();
        Welcome.main(new String[2]);
    }

    public static Connection odbc() {
        Connection con = null;
        java.sql.Statement stmt = null;

        try {
            try {
                Properties props = new Properties();
                props.put ("charSet", "iso-8859-1");
                props.put ("user","");
                props.put ("password", "administradorID");
                
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver").newInstance();
                String dbURL;
                if (tipoConexion.equalsIgnoreCase("directo")) {
                    String dataSource = baseDatos.trim();
                    dbURL = "jdbc:ucanaccess://" + dataSource + ";showSchema=true";
                }else if(tipoConexion.equalsIgnoreCase("odbc")){
                    dbURL = "jdbc:odbc:"+baseDatos.trim();
                }else if(tipoConexion.equalsIgnoreCase("archivo")){
                    String dataSource = baseDatos.trim();
                    dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + dataSource;
                }else{
                    JOptionPane.showMessageDialog(null, "El tipo definido de conexion es invalido, se utilizara el tipo\narchivo por default.", "Error en conexion", JOptionPane.ERROR_MESSAGE);
                    String dataSource = baseDatos.trim();
                    dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" + dataSource;
                }
                
                con = DriverManager.getConnection(dbURL, props);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
                log.error("Se presento una excepcion crear la instancia JDBC-ODBC: " + e.getMessage(), e);
                e.printStackTrace();
            }
            //con = DriverManager.getConnection("jdbc:odbc:ElementoID","","administradorID");
        } catch (Exception ex) {
            log.error("Se presento una excepcion al crear la conexion con el ODBC: " + ex.getMessage(), ex);
            ex.printStackTrace();
        }
        return con;
    }

    public static void leerConfig(String rfce) {
        Configurar conf = new Configurar(rfce);
        rfc = conf.getRfc();
        noCertificado = conf.getNoCertificado();
        produccion = conf.isProduccion();
        logo = conf.getLogo();
        regimenFiscal = conf.getRegimen();
        lugarExpedicion = conf.getLugarExp();
        conf.dispose();

        if (produccion) {
            user = "ZAG4";
            pass = "ZAG.2015";
        } else {
            user = "DEMOGon";
            pass = "cfdi";
        }

    }

    public static boolean checarCreditos(String rfc) {
        Connection con = odbc();
        Statement stmt = factory.stmtLectura(con);
        ResultSet rs;
        boolean respuesta = false;

        try {
            rs = stmt.executeQuery("SELECT creditosRestantes,creditosUsados FROM Cuentas WHERE rfc like\'" + rfc + "\'");
            if (rs.next()) {
                int restantes = rs.getInt("creditosRestantes");
                if (restantes == 0) {
                    respuesta = false;
                } else {
                    respuesta = true;
                }
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Excepcion al checar si quedan creditos suficientes: " + ex.getMessage(), ex);
        }
        return respuesta;
    }

    private void sample() throws Exception {
        // path to watch
        log.info("Se ha ejecutado el elemento...");
        String ruta = Elemento.pathLayout;

        int mask = JNotify.FILE_CREATED;

        // watch subtree?
        boolean watchSubtree = true;

        // add actual watch
        int watchID = JNotify.addWatch(ruta, mask, watchSubtree, new Listener());

        // sleep a little, the application will exit
        //if you don't (watching is asynchronous), depending on your
        // application, this may not be required
        Thread.sleep(64800000);

        // to remove watch the watch
        boolean res = JNotify.removeWatch(watchID);
        if (!res) {
            // invalid watch ID specified.
            System.out.println("ID invalido");
        }
    }

    public void listen() {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(PORT);
        } catch (IOException ioe) {
            Socket s = null;
            try {
                s = new Socket("127.0.0.1", PORT);
                DataInputStream dis = new DataInputStream(s.getInputStream());
                String mensaje = dis.readUTF();
                if (HOLA.equals(mensaje)) {
                    print("La aplicacion ya se esta ejecutando");
                    System.exit(0);
                } else {
                    print("Ya hay algo ejecutandose... pero no parece ser nuestra aplicacion");
                }
            } catch (IOException ie) {
                log.error("Excepcion al crear el Socket y el ServerSocket: " + ioe.getMessage() + "\n" + ie.getMessage(), ie);
            } finally {
                try {
                    if (s != null) {
                        s.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    sample();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }.start();

        try {
            Socket s = null;
            while ((s = ss.accept()) != null) {
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                dos.writeUTF(HOLA);
                s.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Execpcion al escribir en el Socket: " + e.getLocalizedMessage(), e);
        }
    }

    public static void interpretarXML(String pathXml, String nameXml, String dato, String destinoXml) {
        File configuracion = new File(pathXml + nameXml + ".xml");
        BufferedReader entrada2;
        String con = "";
        try {
            entrada2 = new BufferedReader(new InputStreamReader(new FileInputStream(configuracion), "utf-8"));
            while (entrada2.ready()) {
                con += entrada2.readLine();
            }

            int posi = con.indexOf("<cfdi:Comprobante");
            int posf = con.indexOf(">", posi);
            String reemplazo = con.substring(0, posf) + " leyenda=\"" + dato + "\"" + con.substring(posf);

            escribir(reemplazo, nameXml, destinoXml);
        } catch (Exception e) {
            log.error("Excepcion al leer el XML: " + e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private static void escribir(String con, String nameXml, String destinoXml) {
        try {
            File lay = new File(destinoXml + nameXml + ".xml");
            FileOutputStream file = new FileOutputStream(lay);
            OutputStreamWriter fw = new OutputStreamWriter(file, "UTF8");
            Writer bw = new BufferedWriter(fw);
            bw.write(con);
            bw.close();
            fw.close();
            file.close();
        } catch (IOException ioex) {
            log.error("Excepcion al crear el XML interpretado: " + ioex.getMessage(), ioex);
            ioex.printStackTrace();
        }
    }

    public static void restarCredito(String rfc) {
        Connection con = Elemento.odbc();
        Statement stmt = factory.stmtEscritura(con);
        ResultSet rs;
        int creditosRestantes = 0;
        int creditosUsados = 0;
        try {
            rs = stmt.executeQuery("SELECT creditosRestantes,creditosUsados FROM Cuentas WHERE rfc like \'" + rfc + "\'");
            if (rs.next()) {
                creditosRestantes = rs.getInt("creditosRestantes");
                creditosUsados = rs.getInt("creditosUsados");
            }
            rs.close();

            creditosRestantes--;
            creditosUsados++;

            stmt.executeUpdate("UPDATE Cuentas SET creditosRestantes=" + creditosRestantes + ", creditosUsados=" + creditosUsados + " WHERE rfc like \'" + rfc + "\'");
            stmt.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static String getMailConfiguration(String email){
        String resp = "";
        
        String prov = (email.trim().isEmpty() ? "gmail" : email.split("@")[1].split("\\.")[0]);
        Connection con = odbc();
        Statement stmt;
        ResultSet rs;
        
        try{
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT TOP 1 smtp_server, port, tls FROM ConfiguracionMail WHERE proveedor = '" + prov + "'");
            
            if(rs.next()){
                resp += rs.getString(1) + "|" + rs.getString(2) + "|" + rs.getBoolean(3) + "|" + tipoEnvioMail + "|" + pathOutlook;
                
                rs.close();
                stmt.close();
                con.close();
            }
        }catch(SQLException ex){
            ex.printStackTrace();
            log.error("Excepcion al obtener la configuracion del correo: " + email, ex);
        }
        
        return resp;
    }

    private void print(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }
    
    private void verificarBd(){
        /******catalogo de bancos******/
        verificarCBancos();
        
        /******cambios en tabla Cuentas y Folios******/
        verificarCuentasFolios();
    }
    
    private void verificarCBancos(){
        Connection con = odbc();
        Statement stmt = null;
        ResultSet rs;
        try{
            stmt = factory.stmtEscritura(con);
            rs = stmt.executeQuery("select 1 from c_bancos");
            
            rs.close();
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
                log.error("Error al crear la tabla de c_bancos", ex);
                try{
                    con.rollback();
                    con.close();
                }catch(SQLException exx){
                    exx.printStackTrace();
                    log.error("Excepcion al cerrar la conexion con la base de datos o al hacer rollback", exx);
                }
            }
        }
    }
    
    private void verificarCuentasFolios(){
        Connection con = odbc();
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
                    String dbFileSpec = baseDatos.trim();
                    
                    // write a temporary VBScript file ...
                    File vbsFile = File.createTempFile("AlterTable", ".vbs");
                    vbsFile.deleteOnExit();
                    PrintWriter pw = new PrintWriter(vbsFile);
                    pw.println("Set conn = CreateObject(\"ADODB.Connection\")");
                    pw.println("conn.Open \"Driver={Microsoft Access Driver (*.mdb)};Dbq=" + dbFileSpec + "\"");
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
                log.error("Error al actualizar la estructura de la base de datos a las tablas Cuentas y/o Folios", e);
                try{
                    con.close();
                }catch(SQLException sex){
                    sex.printStackTrace();
                    log.error("Error al cerrar la conexion");
                }
                JOptionPane.showMessageDialog(null, "Error al actualizar la estructura de la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
