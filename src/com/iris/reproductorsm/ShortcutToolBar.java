/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.reproductorsm;

import com.iris.imagen.LienzoImage;
import com.iris.imagen.LienzoToolBar;
import com.iris.imagen.VentanaInternaImg;
import com.iris.video.VentanaInternaCam;
import com.iris.video.VentanaInternaVlc;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import net.miginfocom.swing.MigLayout;

/**
 * Clase que describe una barra de herramientas general.
 * 
 * 
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>
 * @version 1.0
 */
public class ShortcutToolBar extends JToolBar{
    private JDesktopPane desktop;
    private LienzoToolBar litoolbar;
    private ReproductorSM repSM;
   
    /**
     * Constructor común, que crea una instancia de <code>ShortcutToolBar</code>
     * y asocia el <code>ReproductorSM</code> pasado como argumento.
     * 
     * @param repSM <code>ReproductorSM</code> reproductor multimedia.
     */
    public ShortcutToolBar(ReproductorSM repSM){
        this.repSM = repSM;
        this.desktop = repSM.getDesktop();
        this.litoolbar = repSM.getLienzoToolBar();
        this.add(getBtnNuevo());
        this.add(getBtnAbrir());
        this.add(getBtnGuardar());
        this.add(getBtnGuardarComo());
        this.add(getBtnWebcam());
        this.add(getBtnSnapshot());
        this.setVisible(true);
    }
    
    /**
     * Crea el botón Nuevo e implementa su listener.
     * 
     * @return <code>JButton</code> nuevo
     */
    private JButton getBtnNuevo(){
        JButton boton = new JButton();
        
        ImageIcon icono = new ImageIcon(ShortcutToolBar.class.getResource("/com/iris/iconos/NuevoBoceto.GIF"));
        boton.setIcon(icono);
        boton.setToolTipText("Nuevo lienzo");
        
        // Listener
        boton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
               repSM.nuevoLienzo();
            }
        });
        
        return boton;
    }
    /**
     * Crea el botón Abrir e implementa su listener.
     * 
     * @return <code>JButton</code> abrir
     */
    private JButton getBtnAbrir(){
        JButton boton = new JButton();
        
        ImageIcon icono = new ImageIcon(ShortcutToolBar.class.getResource("/com/iris/iconos/AbrirMedio.png"));
        boton.setIcon(icono);
        boton.setToolTipText("Abrir");
        
        // Listener
        boton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
               repSM.abrirArchivo();
            }
        });
        
        return boton;
    }
    /**
     * Crea el botón Webcam e implementa su listener.
     * 
     * @return <code>JButton</code> webcam
     */
    private JButton getBtnWebcam(){
        JButton boton = new JButton();
        
        ImageIcon icono = new ImageIcon(ShortcutToolBar.class.getResource("/com/iris/iconos/Camara.png"));
        boton.setIcon(icono);
        boton.setToolTipText("Webcam");
        
        // Listener
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JTextField mrl = new JTextField(30);

                JPanel popup = new JPanel(new MigLayout("wrap 1"));
                popup.add(new JLabel("MRL:"));
                popup.add(mrl);
                popup.add(new JLabel("Linux: v4l2:///dev/video0"));
                popup.add(new JLabel("Windows: dshow://"));
                
                mrl.setText("dshow://");

                int result = JOptionPane.showConfirmDialog(null, popup, 
                         "Media Resource Locator", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String sel = mrl.getText();
                    VentanaInternaCam vic = new VentanaInternaCam(sel);
                    desktop.add(vic);
                    vic.play();
                }
            }
        });
        return boton;
    }
    /**
     * Crea el botón Captura e implementa su listener.
     * 
     * @return <code>JButton</code> captura pantalla
     */
    private JButton getBtnSnapshot(){
        JButton boton = new JButton();
        
        ImageIcon icono = new ImageIcon(ShortcutToolBar.class.getResource("/com/iris/iconos/Capturar.png"));
        boton.setIcon(icono);
        boton.setToolTipText("Snapshot");
        
        // Listener
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JInternalFrame ifr = desktop.getSelectedFrame();
                if (ifr != null && ifr instanceof VentanaInternaVlc ){
                    VentanaInternaVlc vivlc = (VentanaInternaVlc) ifr;
                    BufferedImage snapshot = vivlc.snapshot();
                    LienzoImage li = new LienzoImage(snapshot);
                    VentanaInternaImg viimg = new VentanaInternaImg(li, "Nueva Captura", repSM);
                    
                    li.setToolBar(litoolbar);
                    desktop.add(viimg);
                }else{
                    if (ifr != null && ifr instanceof VentanaInternaCam){
                        VentanaInternaCam vic = (VentanaInternaCam) ifr;
                        BufferedImage snapshot = vic.snapshot();
                        LienzoImage li = new LienzoImage(snapshot);
                        VentanaInternaImg viimg = new VentanaInternaImg(li, "Nueva Captura", repSM);

                        li.setToolBar(litoolbar);
                        desktop.add(viimg);
                    }
                }
            }
        });
        return boton;
    }
   /**
    * Crea el botón Guardar e implementa su listener.
    * 
    * @return <code>JButton</code> guardar
    */
    private JButton getBtnGuardar(){
        JButton boton = new JButton();
        ImageIcon icono = new ImageIcon(ReproductorSM.class.getResource("/com/iris/iconos/Guardar.png"));

        boton.setIcon(icono);
        boton.setToolTipText("Guardar");
        
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                repSM.guardarLienzo();
            }
        });
        
        return boton;
    }
   /**
    * Crea el botón Guardar como e implementa su listener.
    * 
    * @return <code>JButton</code> guardar como
    */
    private JButton getBtnGuardarComo(){
        JButton boton = new JButton();
        ImageIcon icono = new ImageIcon(ReproductorSM.class.getResource("/com/iris/iconos/GuardarComo.png"));
        boton.setIcon(icono);
        boton.setToolTipText("Guardar como");
        
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                repSM.guardarComoLienzo();
            }
        });
        
        return boton;
    }
}
