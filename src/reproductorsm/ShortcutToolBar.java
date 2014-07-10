/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reproductorsm;

import com.iris.imagen.LienzoImage;
import com.iris.imagen.LienzoToolBar;
import com.iris.imagen.VentanaInternaImg;
import com.iris.video.VentanaInternaVlc;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JToolBar;

/**
 *
 * @author dabuti
 */
public class ShortcutToolBar extends JToolBar{
    private JDesktopPane desktop;
    private LienzoToolBar litoolbar;
    private ReproductorSM repSM;
    
    public ShortcutToolBar(ReproductorSM repSM){
        this.repSM = repSM;
        this.desktop = repSM.getDesktop();
        this.litoolbar = repSM.getLienzoToolBar();
        this.add(getBtnAbrir());
        this.add(getBtnWebcam());
        this.add(getBtnSnapshot());
        this.setVisible(true);
    }
    
    private JButton getBtnAbrir(){
        JButton boton = new JButton();
        
        ImageIcon icono = new ImageIcon(ShortcutToolBar.class.getResource("/com/iris/iconos/AbrirMedio.png"));
        boton.setIcon(icono);
        boton.setToolTipText("Abrir");
        
        return boton;
    }
    private JButton getBtnWebcam(){
        JButton boton = new JButton();
        
        ImageIcon icono = new ImageIcon(ShortcutToolBar.class.getResource("/com/iris/iconos/Camara.png"));
        boton.setIcon(icono);
        boton.setToolTipText("Webcam");
        
        return boton;
    }
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
                    VentanaInternaImg viimg = new VentanaInternaImg(li, "Nueva Captura", 
                            snapshot.getWidth(), snapshot.getHeight());
                    
                    li.setToolBar(litoolbar);
                    desktop.add(viimg);
                }
            }
        });
        return boton;
    }
}
