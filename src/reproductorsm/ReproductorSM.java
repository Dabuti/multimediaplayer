/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reproductorsm;

import com.iris.imagen.LienzoImage;
import com.iris.imagen.LienzoImageToolBar;
import com.iris.imagen.LienzoToolBar;
import com.iris.imagen.VentanaInternaImg;
import com.iris.sonido.LienzoSound;
import com.iris.sonido.LienzoSoundRecorder;
import com.iris.video.VentanaInternaVlc;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;


/**
 * This class describe a basic Frame with the features of
 * drawing dots or lines.
 *
 * Created: Tue Mar 20 17:36:46 2014
 *
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>
 * @version 1.0
 */

public class ReproductorSM {

   // Variables declaration
   private static JFrame frame;
   private JPanel mainPanel;
   private JDesktopPane desktop;
   private LienzoToolBar lienzoToolbar;
   private LienzoImageToolBar lienzoImageToolbar;
   private ShortcutToolBar shortcut;
    
   public ReproductorSM(){
   }
   
   // Construcción del panel principal.
   public JPanel createContentPane(){
      mainPanel = new JPanel();
      desktop = new JDesktopPane();
      JPanel leftPanel = new JPanel();
      leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
      
      // Añadir barras de herramientas al panel izquierdo
      lienzoToolbar = new LienzoToolBar();
      lienzoImageToolbar = new LienzoImageToolBar(desktop);
      
      leftPanel.add(lienzoToolbar);
      leftPanel.add(lienzoImageToolbar);
      
      mainPanel.setLayout(new BorderLayout());
      mainPanel.setPreferredSize(new Dimension(1366, 768));
      
      
      // Wrapper para hacer scroll en el panel de herramientas
      JScrollPane leftToolbar = new JScrollPane(leftPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      leftToolbar.setPreferredSize(new Dimension(300,0));
      
      // Barra de acceso rápido
      shortcut = new ShortcutToolBar(this);
      
      mainPanel.add(shortcut, BorderLayout.PAGE_START);
      mainPanel.add(desktop, BorderLayout.CENTER);
      mainPanel.add(leftToolbar, BorderLayout.LINE_START);
      //mainPanel.add(createFootBar(), BorderLayout.PAGE_END);

      return mainPanel;
   }

   // Barra de menú
   public JMenuBar createMenuBar(){
      JMenuBar menuBar = new JMenuBar();
      JMenu menuArchivo = new JMenu("Archivo");
      JMenu menuEdicion = new JMenu("Edición");
      JMenu menuImagen = new JMenu("Imagen");

      // Items
      JMenuItem itemNuevo = new JMenuItem("Nuevo");
      JMenuItem itemAbrir = new JMenuItem("Abrir imagen");
      JMenuItem itemSonido = new JMenuItem("Abrir Sonido");
      JMenuItem itemVideo = new JMenuItem("Abrir Video");
      JMenuItem itemRec = new JMenuItem("Grabar Sonido");

      // MenuBar
      menuBar.add(menuArchivo);
      menuBar.add(menuEdicion);
      menuBar.add(menuImagen);

      // Menu Archivo
      menuArchivo.add(itemNuevo);
      menuArchivo.add(itemAbrir);
      menuArchivo.add(itemSonido);
      menuArchivo.add(itemVideo);
      menuArchivo.add(itemRec);

      // Set menu items listeners.
      // Item Nuevo
      itemNuevo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                JTextField ancho = new JTextField(5);
                JTextField alto = new JTextField(5);
                int w = 0;
                int h = 0;

                JPanel dimen = new JPanel();
                dimen.add(new JLabel("Ancho:"));
                dimen.add(ancho);
                dimen.add(Box.createHorizontalStrut(15)); // a spacer
                dimen.add(new JLabel("Alto:"));
                dimen.add(alto);

                int result = JOptionPane.showConfirmDialog(null, dimen, 
                         "Introduzca las dimensiones del lienzo", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    w = Integer.parseInt(ancho.getText());
                    h = Integer.parseInt(alto.getText());
                }
                
                if (w < 1 || h < 1){ w=400; h=350; }
                
                BufferedImage nueva = new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = nueva.createGraphics();
                graphics.setPaint(Color.white);
                graphics.fillRect(0, 0, nueva.getWidth(), nueva.getHeight());
                
                LienzoImage li = new LienzoImage(nueva);
                li.setToolBar(lienzoToolbar);
                
                // Crear internal frame
                VentanaInternaImg intf = new VentanaInternaImg(li, "Nueva", w, h);
                
                desktop.add(intf);
            }
      });
      
      // Item Prueba imagenes
      itemAbrir.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
               JFileChooser chooser = new JFileChooser();
               FileNameExtensionFilter filter;
               filter = new FileNameExtensionFilter("JPG/GIF/BMP/PNG Images",
                                                    "jpg", "gif", "bmp", "png"
                                                    );

               chooser.setFileFilter(filter);
               int resp = chooser.showOpenDialog(frame);
               File imgfile = null;

               if (resp == JFileChooser.APPROVE_OPTION){
                  imgfile = chooser.getSelectedFile();
               }

               try{
                  BufferedImage img = ImageIO.read(imgfile);
                  LienzoImage li = new LienzoImage(img);
                  li.setToolBar(lienzoToolbar);
                  
                  // Crear internal frame 
                 VentanaInternaImg intf = new VentanaInternaImg(li, imgfile.getName(), img.getWidth(), img.getHeight());
                 desktop.add(intf);
                 
               }catch(IOException e){
                  System.out.println(e.getMessage());
               }
            }
      });
      
      itemSonido.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
               JFileChooser chooser = new JFileChooser();
               FileNameExtensionFilter filter;
               filter = new FileNameExtensionFilter("WAV/MP3 Files",
                                                    "wav", "mp3");

               chooser.setFileFilter(filter);
               int resp = chooser.showOpenDialog(frame);
               File soundfile = null;

               if (resp == JFileChooser.APPROVE_OPTION){
                  soundfile = chooser.getSelectedFile();
               }

               LienzoSound ls = new LienzoSound(soundfile);
                  
               // Crear internal frame 
               JInternalFrame intf = new JInternalFrame("Nombre Sonido", 
                    true,  //resizable
                    true,  //closable
                    true,  //maximizable
                    true); //iconifiable

                intf.setContentPane(ls);
                intf.setVisible(true);
                intf.setPreferredSize(new Dimension(512, 200));
                intf.pack();
                desktop.add(intf);                
            }
      });
      
      itemVideo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
               JFileChooser chooser = new JFileChooser();
               FileNameExtensionFilter filter;
               filter = new FileNameExtensionFilter("Videos",
                                                    "mp4", "avi");

               //chooser.setFileFilter(filter);
               int resp = chooser.showOpenDialog(frame);
               File videofile = null;

               if (resp == JFileChooser.APPROVE_OPTION){
                  videofile = chooser.getSelectedFile();
               }
               
               VentanaInternaVlc vivlc = new VentanaInternaVlc(videofile);
               desktop.add(vivlc);
               vivlc.play();
            }
      });

      itemRec.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
               LienzoSoundRecorder lsr = new LienzoSoundRecorder();
                  
               // Crear internal frame 
               JInternalFrame intf = new JInternalFrame("Grabación Sonido", 
                    true,  //resizable
                    true,  //closable
                    true,  //maximizable
                    true); //iconifiable

                intf.setContentPane(lsr);
                intf.setVisible(true);
                intf.setPreferredSize(new Dimension(250, 200));
                intf.pack();
                desktop.add(intf);                 
            }
      
      });
      
      return menuBar;
   }

   private static void createAndShowGUI() {
      // Set Nimbus look and feel.
      try {
         for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()){
            if ("Nimbus".equals(info.getName())) {
               javax.swing.UIManager.setLookAndFeel(info.getClassName());
               break;
            }
         }
      } catch (ClassNotFoundException ex) {
         System.out.println("Error");
      } catch (InstantiationException ex) {
         System.out.println("Error");
      } catch (IllegalAccessException ex) {
         System.out.println("Error");
      } catch (javax.swing.UnsupportedLookAndFeelException ex) {
         System.out.println("Error");
      }

      frame = new JFrame("Reproductor Multimedia");

      //Create and set up the content pane.
      ReproductorSM repSM = new ReproductorSM();
      frame.setContentPane(repSM.createContentPane());

      // Create and set up the MenuBar.
      frame.setJMenuBar(repSM.createMenuBar());

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      // Display the window.
      frame.pack();
      frame.setVisible(true);
   }

   public JDesktopPane getDesktop(){ return this.desktop; }
   public LienzoToolBar getLienzoToolBar(){ return this.lienzoToolbar; }
   
   public static void main(String args[]) {
      EventQueue.invokeLater(new Runnable() {
            public void run() {
            NativeLibrary.addSearchPath(
                RuntimeUtil.getLibVlcLibraryName(), "c:/Program Files/VideoLAN/VLC/");
            Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
               createAndShowGUI();
            }
         });
   }
}
