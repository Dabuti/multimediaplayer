/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package reproductorsm;

import com.iris.imagen.Lienzo;
import com.iris.imagen.LienzoToolBar;

import javax.swing.*;
import javax.swing.UIManager.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.image.BufferedImage;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.RescaleOp;
import java.awt.image.ConvolveOp;
import java.awt.image.LookupOp;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;
import java.awt.image.Kernel;
import java.awt.image.LookupTable;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;


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
   //private LienzoImageToolBar lienzoImageToolbar;
    
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
      //lienzoImageToolbar = new LienzoImageToolBar();
      leftPanel.add(lienzoToolbar);
      
      mainPanel.setLayout(new BorderLayout());
      mainPanel.setPreferredSize(new Dimension(1366, 768));
      
      
      // Wrapper para hacer scroll en el panel de herramientas
      JScrollPane leftToolbar = new JScrollPane(leftPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      leftToolbar.setPreferredSize(new Dimension(300,0));
      
      //mainPanel.add(createToolBar(), BorderLayout.PAGE_START);
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

      // MenuBar
      menuBar.add(menuArchivo);
      menuBar.add(menuEdicion);
      menuBar.add(menuImagen);

      // Menu Archivo
      menuArchivo.add(itemNuevo);

      // Set menu items listeners.
      // Item Nuevo
      itemNuevo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                Lienzo li = new Lienzo(lienzoToolbar);
                // Crear internal frame
                JInternalFrame intf = new JInternalFrame("Lienzo Dibujo", 
                        true,  //resizable
                        true,  //closable
                        true,  //maximizable
                        true); //iconifiable
                
                intf.setContentPane(li);
                intf.setVisible(true);
                intf.setPreferredSize(new Dimension(400, 350));
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

   public static void main(String args[]) {
      EventQueue.invokeLater(new Runnable() {
            public void run() {
               createAndShowGUI();
            }
         });
   }
}
