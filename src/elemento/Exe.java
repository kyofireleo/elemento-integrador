/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package elemento;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Abe
 */
public class Exe {
    private static Process p1;
    private static Process p2;
    
    public static void exe (String cmd) throws IOException{
        p1 = Runtime.getRuntime().exec(cmd);
        BufferedReader input = new BufferedReader (new InputStreamReader(p1.getInputStream()));
//        try{
//            Thread.sleep(8000);
//        }catch(Exception e){
//            Elemento.log.error("Excepcion en el Thread.sleep de la clase Exe: " + e.getMessage(),e);
//        }
        try {
            while(p1.waitFor() != 0){
                System.out.println(p1.waitFor());
            }
            input.close();
            p1.destroy();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void exeSinTiempo (String cmd) throws IOException{
        p2 = Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL "+cmd);
        BufferedReader input = new BufferedReader (new InputStreamReader(p2.getInputStream()));
        input.close();
    }
}