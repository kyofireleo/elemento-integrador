/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package elemento;

import gui.Configurar;
import gui.Factura_View;
import gui.Folios;
import gui.Login;
import gui.Welcome;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DemoTray {
    public DemoTray(){
        final TrayIcon icono;
        Image imagen = null;
        final JFrame ventana = new JFrame("Principal");
        
        if(SystemTray.isSupported()){
            SystemTray tray = SystemTray.getSystemTray();
            imagen = Toolkit.getDefaultToolkit().
            getImage("tray.png");
            PopupMenu popup = new PopupMenu();
//            MenuItem optComenzar = new MenuItem("Comenzar...");
            MenuItem optSalir = new MenuItem("Salir");
            MenuItem optFolios = new MenuItem("Folios Registrados");
            MenuItem optFactura = new MenuItem("Crear CFD");
            MenuItem optWelcome = new MenuItem("Principal");
            MenuItem optConfigurar = new MenuItem("Configurar");
            
            optSalir.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    Elemento.log.info("Se ha cerrado el programa");
                    System.exit(0);
                }
            });
            
            optFolios.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    String [] args = new String [2];
                    Folios.main(args);
                }
            });
            
            optFactura.addActionListener( new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    Factura_View.main(new String[2]);
                }
            });
            
            optWelcome.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent arg0){
                    Welcome.main(new String[2]);
                }
            });
            
            optConfigurar.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent arg0){
                    Configurar.main(new String[2]);
                }
            });
            
//            optComenzar.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    ventana.setSize(300,300);
//                    ventana.setVisible(true);
//                    Sumimaz sumi = new Sumimaz();
//                }
//            });
   
//            popup.add(optComenzar);
            popup.add(optWelcome);
            popup.add(optFactura);
            popup.add(optFolios);
            popup.add(optConfigurar);
            popup.add(optSalir);
            icono = new TrayIcon(imagen, "Elemento Integrador", popup);
            icono.setImageAutoSize(true);
            
            icono.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e){
                    if(e.getButton() == 1){
//                        ventana.setSize(300,300);
//                        ventana.setVisible(true);
                    }
                }
            });
            
            try {
                tray.add(icono);
            }catch (AWTException e1) {
                JOptionPane.showMessageDialog(null,e1.getStackTrace());
            }
        }
    }
}