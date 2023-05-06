/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package elemento;

import gui.Configurar;
import gui.Welcome;
import java.awt.HeadlessException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import javax.swing.JOptionPane;
import log.Log;
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
    public static String pathLayoutWorking, pathLayoutDone, pathLayoutError;
    public static String user, pass;
    private static String tipoConexion, baseDatos;
    public static String estructuraNombre;
    private static String tipoEnvioMail, tipoMailAdjunto, pathOutlook;

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
                String adjunto = prop.getProperty("tipoMailAdjunto");
                tipoEnvioMail = tipo == null ? "1" : tipo;
                tipoMailAdjunto = adjunto == null ? "2" : adjunto;
                
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
                tipoMailAdjunto = "2";
                
                prop.setProperty("tipo_conexion", tipoConexion);
                prop.setProperty("base_datos", baseDatos);
                prop.setProperty("estructura_nombre", estructuraNombre);
                prop.setProperty("tipoEnvioMail", tipoEnvioMail);
                prop.setProperty("tipoMailAdjunto", tipoMailAdjunto);
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
                        + "en lugar de una diagonal (\\).\r\n\r\n"
                        + "Para el tipoMailAdjunto serian 1 o 2, los cuales significan:\r\n"
                        + "\t1: Envio de un solo archivo ZIP con todos los XMLs y PDFs\r\n"
                        + "\t2: Envio de dos archivos (XML y PDF) por comprobante\r\n");
                
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
        
        pathLayoutWorking = pathLayout + "working\\";
        pathLayoutDone = pathLayout + "done\\";
        pathLayoutError = pathLayout + "error\\";
        
        pathXmlST = unidad + ":\\Facturas\\C_Interpretados\\";
        pathXmlMod = unidad + ":\\Facturas\\XmlModificados\\";
        pathConfig = unidad + ":\\Facturas\\config\\";
        pathPlantillas = unidad + ":\\Facturas\\config\\plantillas\\";
        pathQR = unidad + ":\\Facturas\\qrs\\";


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
        
        File file12 = new File(pathLayoutWorking);
        File file13 = new File(pathLayoutDone);
        File file14 = new File(pathLayoutError);
        
        File file15 = new File(unidad + ":\\Facturas\\zips");

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
        
        file12.mkdir();
        file13.mkdir();
        file14.mkdir();
        
        file15.mkdir();
        
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-7"));
        LocalDateTime fechaHora = LocalDateTime.now();
        System.out.println(fechaHora.toString());
        System.out.println("TimeZone: " + TimeZone.getDefault().getDisplayName());

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
        } catch (HeadlessException ex) {
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
            /*
            user = "ZAG4";
            pass = "ZAG.2015";
            */
            user = "General";
            pass = "2dtTlwJy";
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
        utils.Utils u = new utils.Utils(log);
        String ruta = Elemento.pathLayout;
        
        File f = new File(pathLayout);
        log.info("Buscando layout creado...");
        while(true){    
            if(f.listFiles().length > 0){
                Listener l = new Listener();
                for(File a : f.listFiles()){
                    if(a.isFile()){
                        u.fileMove(a.getPath(), pathLayoutWorking + a.getName());
                        l.fileCreated(0, pathLayoutWorking, a.getName()); 
                    }
                }
            }
            
            Thread.sleep(10000);
        }
        
        /*
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
        */
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
                resp += rs.getString(1) + "|" + rs.getString(2) + "|" + rs.getBoolean(3) + "|" + tipoEnvioMail + "|" + pathOutlook + "|" + tipoMailAdjunto;
                
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
        
        MantenimientoDataBase mdb = new MantenimientoDataBase(baseDatos);
    }
}
