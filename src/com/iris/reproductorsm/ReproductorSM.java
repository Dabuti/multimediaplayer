/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.reproductorsm;

import com.iris.imagen.LienzoImage;
import com.iris.imagen.LienzoImageToolBar;
import com.iris.imagen.LienzoToolBar;
import com.iris.imagen.VentanaInternaImg;
import com.iris.sonido.LienzoSound;
import com.iris.sonido.LienzoSoundRecorder;
import com.iris.sonido.VentanaInternaSound;
import com.iris.video.VentanaInternaVlc;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import net.miginfocom.swing.MigLayout;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;


/**
 * Esta clase representa un reproductor multimedia, encargada de construir la interfaz
 * de la aplicación, así como controlar el comportamiento general del reproductor.
 *
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>
 * @version 1.0
 */

public class ReproductorSM {
   // Variables declaration
   private static JFrame frame;
   private JPanel mainPanel, footbar, leftPanel;
   private JDesktopPane desktop;
   private LienzoToolBar lienzoToolbar;
   private LienzoImageToolBar lienzoImageToolbar;
   private ShortcutToolBar shortcut;
   public JMenuItem itemGuardar, itemGuardarComo;
   public JLabel estadoLbl;
    
   /**
    * Constructor por defecto.
    */
   public ReproductorSM(){
    UIManager.put(
        "OptionPane.messageFont",
        new FontUIResource(new Font("Inconsolata", Font.BOLD, 20))
    );
   }
   
   /**
    * Crea el <code>JPanel</code> principal que se añade al frame.
    * 
    * @return <code>JPanel</code> principal.
    */
   public JPanel createContentPane(){
      mainPanel = new JPanel();
      desktop = new JDesktopPane();
      leftPanel = new JPanel();
      footbar = createFootBar();
      leftPanel.setLayout(new MigLayout("wrap 1"));
      leftPanel.setPreferredSize(new Dimension(330,690));
      
      // Añadir barras de herramientas al panel izquierdo
      lienzoToolbar = new LienzoToolBar(this);
      lienzoImageToolbar = new LienzoImageToolBar(this);
   
      leftPanel.add(lienzoToolbar, "wrap, align left");
      leftPanel.add(lienzoImageToolbar, "wrap, align left");
      
      mainPanel.setLayout(new BorderLayout());
      mainPanel.setPreferredSize(new Dimension(1366, 768));
      
      
      // Wrapper para hacer scroll en el panel de herramientas
      JScrollPane leftToolbar = new JScrollPane(leftPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
              ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      leftToolbar.setPreferredSize(new Dimension(330,0));
      
      // Barra de acceso rápido
      shortcut = new ShortcutToolBar(this);
      
      mainPanel.add(shortcut, BorderLayout.PAGE_START);
      mainPanel.add(desktop, BorderLayout.CENTER);
      mainPanel.add(leftToolbar, BorderLayout.LINE_START);
      mainPanel.add(footbar, BorderLayout.PAGE_END);

      return mainPanel;
   }

   /**
    * Crea un <code>JPanel</code> para la barra de estado.
    * 
    * @return <code>JPanel</code> footbar
    */
   private JPanel createFootBar(){
      JPanel footbar = new JPanel();
      estadoLbl = new JLabel("Barra de estado");
      estadoLbl.setBorder(BorderFactory.createTitledBorder(""));
      footbar.setLayout(new BorderLayout());
      footbar.setPreferredSize(new Dimension(400,30));
      footbar.add(estadoLbl, BorderLayout.LINE_START);

      return footbar;
   }
   
   /**
    * Crea un <code>JMenuBar</code> con opciones básicas para trabajar con el
    * reproductor.
    * 
    * @return <code>JMenuBar</code> menu
    */
   private JMenuBar createMenuBar(){
      JMenuBar menuBar = new JMenuBar();
      JMenu menuArchivo = new JMenu("Archivo");
      JMenu menuVer = new JMenu("Ver");
      JMenu menuAyuda = new JMenu("Ayuda");
      JMenu menuNuevo = new JMenu("Nuevo");

      // Iconos
      ImageIcon iconLienzo = new ImageIcon(ReproductorSM.class.getResource("/com/iris/iconos/NuevoBoceto.GIF"));
      ImageIcon iconGuardar = new ImageIcon(ReproductorSM.class.getResource("/com/iris/iconos/Guardar.png"));
      ImageIcon iconGuardarComo = new ImageIcon(ReproductorSM.class.getResource("/com/iris/iconos/GuardarComo.png"));
      ImageIcon iconRec = new ImageIcon(ReproductorSM.class.getResource("/com/iris/iconos/Microfono.png"));
      ImageIcon iconAbrir = new ImageIcon(ReproductorSM.class.getResource("/com/iris/iconos/Open.png"));
      ImageIcon iconCerrar = new ImageIcon(ReproductorSM.class.getResource("/com/iris/iconos/Cerrar.png"));
      
      // Items
      JMenuItem itemAbrir = new JMenuItem("Abrir");
      itemGuardar = new JMenuItem("Guardar");
      itemGuardarComo = new JMenuItem("Guardar como");
      JMenuItem itemRec = new JMenuItem("Grabación Sonido");
      JMenuItem itemLienzo = new JMenuItem("Lienzo");
      JMenuItem itemAcerca = new JMenuItem("Acerca de");
      JMenuItem itemCerrar = new JMenuItem("Cerrar");
      
      itemGuardar.setEnabled(false);
      itemGuardarComo.setEnabled(false);
      
      // Items de mostrar/ocultar
      JCheckBoxMenuItem itemBarraGeneral = new JCheckBoxMenuItem("Barra General");
      JCheckBoxMenuItem itemBarraEstado = new JCheckBoxMenuItem("Barra de Estado");
      JCheckBoxMenuItem itemBarraDibujo = new JCheckBoxMenuItem("Barra de Dibujo");
      JCheckBoxMenuItem itemBarraImagen = new JCheckBoxMenuItem("Barra de Imagen");
      itemBarraGeneral.setSelected(true);
      itemBarraEstado.setSelected(true);
      itemBarraDibujo.setSelected(true);
      itemBarraImagen.setSelected(true);
      
      // Asignar iconos
      itemAbrir.setIcon(iconAbrir);
      itemRec.setIcon(iconRec);
      itemGuardar.setIcon(iconGuardar);
      itemGuardarComo.setIcon(iconGuardarComo);
      itemLienzo.setIcon(iconLienzo);
      itemCerrar.setIcon(iconCerrar);
      
      // MenuBar
      menuBar.add(menuArchivo);
      menuBar.add(menuVer);
      menuBar.add(menuAyuda);

      // Menu Nuevo
      menuNuevo.add(itemLienzo);
      menuNuevo.add(itemRec);
      
      // Menu Archivo
      menuArchivo.add(menuNuevo);
      menuArchivo.add(itemAbrir);
      menuArchivo.add(itemGuardar);
      menuArchivo.add(itemGuardarComo);
      menuArchivo.add(itemCerrar);

      // Menu Ver
      menuVer.add(itemBarraGeneral);
      menuVer.add(itemBarraEstado);
      menuVer.add(itemBarraDibujo);
      menuVer.add(itemBarraImagen);
      
      // Menu Ayuda
      menuAyuda.add(itemAcerca);
      
      // Set menu items listeners.
      // Item Nuevo
      itemLienzo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                nuevoLienzo();
            }
      });
      
      // Item Abrir
      itemAbrir.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                abrirArchivo();
            }
      });
      
      itemRec.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                nuevoRecSonido();
            }
      
      });
      
      itemGuardar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                guardarLienzo();
            }
      });

      itemGuardarComo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                guardarComoLienzo();
            }
      });

      itemAcerca.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){
                showAcercade();
            }
      });
      
      itemCerrar.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent ae) {
              cerrarReproductor();
          }
      });
      
      itemBarraGeneral.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent ae) {
              JCheckBoxMenuItem item = (JCheckBoxMenuItem) ae.getSource();
              if (item.isSelected()){
                  shortcut.setVisible(true);
              }else{
                  shortcut.setVisible(false);
              }
          }
      });
      
      itemBarraEstado.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent ae) {
              JCheckBoxMenuItem item = (JCheckBoxMenuItem) ae.getSource();
              if (item.isSelected()){
                  footbar.setVisible(true);
              }else{
                  footbar.setVisible(false);
              }
          }
      });
      
      itemBarraDibujo.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent ae) {
              JCheckBoxMenuItem item = (JCheckBoxMenuItem) ae.getSource();
              if (item.isSelected()){
                  leftPanel.add(lienzoToolbar);
                  leftPanel.validate();
                  leftPanel.repaint();
              }else{
                  leftPanel.remove(lienzoToolbar);
                  leftPanel.validate();
                  leftPanel.repaint();
              }
          }
      });

      itemBarraImagen.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent ae) {
              JCheckBoxMenuItem item = (JCheckBoxMenuItem) ae.getSource();
              if (item.isSelected()){
                  leftPanel.add(lienzoImageToolbar);
                  leftPanel.validate();
                  leftPanel.repaint();
              }else{
                  leftPanel.remove(lienzoImageToolbar);
                  leftPanel.validate();
                  leftPanel.repaint();
              }
          }
      });

      
      return menuBar;
   }

   /**
    * Lanza un <code>JOptionPane</code> con la información Acerca de, del reproductor.
    */
   private void showAcercade(){
       ImageIcon foto = new ImageIcon(ReproductorSM.class.getResource("/com/iris/iconos/iris.png"));
       
       JOptionPane.showMessageDialog(frame,
        "Aplicación: Reproductor multimedia\n" + 
        "Versión:    1.0 \n" + 
        "Autor:      Iris García de Sebastián\n" +
        "Profesor:   Jesús Chamorro Martínez\n" +
        "Fecha:      15/07/2014",
        "Acerca de",
        JOptionPane.INFORMATION_MESSAGE,
        foto);
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

      // Display the window.
      frame.pack();
      frame.setVisible(true);
      frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

      frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e){
            cerrarReproductor();
        }
      });
   }
   
   /**
    * Ajusta el texto de la barra de estado.
    * 
    * @param estado <code>String</code> estado.
    */
   public void setEstado(String estado){
       if ("".equals(estado))
           estadoLbl.setText("Barra de estado");
       else
           estadoLbl.setText(estado);
   }
   
   /**
    * Devuelve el <code>JDesktopPane</code> del reproductor.
    * 
    * @return <code>JDesktopPane</code> desktop.
    */
   public JDesktopPane getDesktop(){ return this.desktop; }
   /**
    * Devuelve el <code>LienzoToolBar</code> del reproductor.
    * 
    * @return <code>LienzoToolBar</code> lienzo toolbar.
    */
   public LienzoToolBar getLienzoToolBar(){ return this.lienzoToolbar; }
   /**
    * Método que pregunta al usuario si realmente desea cerrar la aplicación.
    */
   public static void cerrarReproductor(){
        int result = JOptionPane.showConfirmDialog(
            frame,
            "¿Está seguro de que desea cerrar la aplicación?",
            "Cerrar",
            JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION){
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.dispose();
        }
   }
   /**
    * Crea una nueva ventana interna <code>VentanaInternaSound</code> para 
    * la grabación de sonido.
    */
   public void nuevoRecSonido(){
        LienzoSoundRecorder lsr = new LienzoSoundRecorder(this);
        VentanaInternaSound vis = new VentanaInternaSound(lsr, "Grabación sonido");
        desktop.add(vis);
        try {
           vis.setSelected(true);
        } catch (PropertyVetoException ex) {
           System.err.println(ex);
        }
   }
   
   /**
    * Crea una nueva ventana interna <code>VentanaInternaImg</code> y un
    * <code>LienzoImage</code> con las dimensiones introducidas por el usuario
    * en un dialog.
    */
   public void nuevoLienzo(){
        JTextField ancho = new JTextField(5);
        JTextField alto = new JTextField(5);
        ancho.setText("400");
        alto.setText("400");

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

            if (w < 1 || h < 1){ 
                w = 400; 
                h = 350; 
            }

            BufferedImage nueva = new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = nueva.createGraphics();
            graphics.setPaint(Color.white);
            graphics.fillRect(0, 0, nueva.getWidth(), nueva.getHeight());

            LienzoImage li = new LienzoImage(nueva);
            li.setToolBar(lienzoToolbar);

            // Crear internal frame
            VentanaInternaImg intf = new VentanaInternaImg(li, "Nuevo", this);
            li.setInternalFrame(intf);

            desktop.add(intf);
            
            try {
                intf.setSelected(true);
            } catch (PropertyVetoException ex) {
                System.err.println(ex);
            }
        }
   }
   /**
    * Guarda el <code>LienzoImage</code> seleccionado.
    */
   public void guardarLienzo(){
        LienzoImage li = null;
        VentanaInternaImg viimg = null;
        if (desktop.getSelectedFrame() != null && desktop.getSelectedFrame() instanceof VentanaInternaImg){
            viimg = (VentanaInternaImg) desktop.getSelectedFrame();
            li = viimg.getLienzoImage();
            if (li != null){
               // Comprobar si es nuevo
               if (li.isNuevo()){
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter1 = new FileNameExtensionFilter("PNG Image", "png");
                FileNameExtensionFilter filter2 = new FileNameExtensionFilter("JPEG Image", "jpg");
                FileNameExtensionFilter filter3 = new FileNameExtensionFilter("BMP Image", "bmp");

                chooser.setFileFilter(filter3);
                chooser.setFileFilter(filter2);
                chooser.setFileFilter(filter1);

                int resp = chooser.showSaveDialog(frame);

                if (resp == JFileChooser.APPROVE_OPTION){
                   String path=chooser.getSelectedFile().getAbsolutePath();
                   String ext = "";
                   FileFilter sel_filter = chooser.getFileFilter();

                   // Comprobar extensión
                   if (sel_filter.equals(filter1))
                     ext = "png";
                   if (sel_filter.equals(filter2))
                     ext = "jpg";
                   if (sel_filter.equals(filter3))
                     ext = "bmp";

                   path += "." + ext;
                   File savefile = new File(path);
                   
                   li.saveToFile(savefile);
                   desktop.remove(viimg);
                   cargarArchivo(savefile);
                   desktop.repaint();
                }
               // No es nuevo, sobreescribir fichero
               }else{
                   li.saveToFile(li.getFile());
               }
            }
        }
   }
   /**
    * Guarda el <code>LienzoImage</code> seleccionado en un fichero
    * que también ha de seleccionar el usuario.
    */
   public void guardarComoLienzo(){
        LienzoImage li = null;
        VentanaInternaImg viimg = null;
        if (desktop.getSelectedFrame() != null && desktop.getSelectedFrame() instanceof VentanaInternaImg){
            viimg = (VentanaInternaImg) desktop.getSelectedFrame();
            li = viimg.getLienzoImage();
            if (li != null){
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter1 = new FileNameExtensionFilter("PNG Image", "png");
                FileNameExtensionFilter filter2 = new FileNameExtensionFilter("JPEG Image", "jpg");
                FileNameExtensionFilter filter3 = new FileNameExtensionFilter("BMP Image", "bmp");

                chooser.setFileFilter(filter3);
                chooser.setFileFilter(filter2);
                chooser.setFileFilter(filter1);

                int resp = chooser.showSaveDialog(frame);

                if (resp == JFileChooser.APPROVE_OPTION){
                   String path=chooser.getSelectedFile().getAbsolutePath();
                   String ext = "";
                   FileFilter sel_filter = chooser.getFileFilter();

                   // Comprobar extensión
                   if (sel_filter.equals(filter1))
                     ext = "png";
                   if (sel_filter.equals(filter2))
                     ext = "jpg";
                   if (sel_filter.equals(filter3))
                     ext = "bmp";

                   path += "." + ext;
                   File savefile = new File(path);
                   
                   li.saveToFile(savefile);
                   desktop.remove(viimg);
                   cargarArchivo(savefile);
                   desktop.repaint();
                }
            }
        }
   }
   
   /**
    * Lanza un <code>JFileChooser</code> para seleccionar un fichero.
    * Una vez seleccionado, se carga.
    * 
    * @see cargarArchivo
    */
   public void abrirArchivo(){
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter1, filter2, filter3, filter4;
        filter1 = new FileNameExtensionFilter("Imágenes", "jpg", "gif", "bmp", "png");
        filter2 = new FileNameExtensionFilter("Sonido simple", "wav");
        filter3 = new FileNameExtensionFilter("Sonido complejo", "mp3", "wma");
        filter4 = new FileNameExtensionFilter("Video", "avi", "mkv", "mp4", "ogg", "wmv");

        chooser.setFileFilter(filter4);
        chooser.setFileFilter(filter3);
        chooser.setFileFilter(filter2);
        chooser.setFileFilter(filter1);

        int resp = chooser.showOpenDialog(frame);

        if (resp == JFileChooser.APPROVE_OPTION){  
            File selected = chooser.getSelectedFile();
            cargarArchivo(selected);
        }
   }
   
   /**
    * Método que recibe un archivo como argumento, comprueba su extensión
    * y crea una ventana interna adecuada para cada tipo de fichero.
    * 
    * @param f <code>File</code> archivo.
    */
   public void cargarArchivo(File f){
        String ext = "";

        int i = f.getAbsolutePath().lastIndexOf('.');
        if (i > 0) {
            ext = f.getAbsolutePath().substring(i+1);
        }

        // Archivo de imagen
        if (ext.equals("bmp") || ext.equals("jpg") || ext.equals("png") || ext.equals("gif")){
         try{
            BufferedImage img = ImageIO.read(f);
            LienzoImage li = new LienzoImage(img);
            li.setToolBar(lienzoToolbar);

            // Crear internal frame 
            VentanaInternaImg intf = new VentanaInternaImg(li, f.getName(), this);
            li.setInternalFrame(intf);
            li.setFile(f);
            desktop.add(intf);
            intf.setSelected(true);
            
            Dimension d1 = intf.getSize();
            Dimension d2 = desktop.getSize();
            if (d1.width > d2.width || d1.height > d2.height)
                intf.setMaximum(true);
            
         }catch(IOException e){
            System.out.println(e.getMessage());
         }catch (PropertyVetoException ex) {
             System.err.println(ex);
         }
        }

        // Archivo sonido simple
        if (ext.equals("wav") || ext.equals("wav")){
            LienzoSound ls = new LienzoSound(f);
            VentanaInternaSound vis = new VentanaInternaSound(ls, f.getName());
            desktop.add(vis);
            try {
                vis.setSelected(true);
            } catch (PropertyVetoException ex) {
                System.err.println(ex);
            }
        }
        // Archivo sonido complejo
        if (ext.equals("mp3") || ext.equals("mp3")){
             VentanaInternaVlc vivlc = new VentanaInternaVlc(f);
             desktop.add(vivlc);
             try {
                 vivlc.setSelected(true);
             } catch (PropertyVetoException ex) {
                 System.err.println(ex);
             }
             vivlc.play();
        }

        // Archivo video
        if (ext.equals("avi") || ext.equals("mkv") || ext.equals("mp4") ||
                ext.equals("wma")){
             VentanaInternaVlc vivlc = new VentanaInternaVlc(f);
             desktop.add(vivlc);
            try {
                vivlc.setSelected(true);
            } catch (PropertyVetoException ex) {
                System.err.println(ex);
            }
             vivlc.play();
        }
   }
   
   /**
    * Método principal de la aplicación encargado de crear una instancia
    * del reproductor multimedia.
    * @param args 
    */
   public static void main(String args[]) {
      EventQueue.invokeLater(new Runnable() {
            public void run() {
            NativeLibrary.addSearchPath(
                RuntimeUtil.getLibVlcLibraryName(), "c:/Program Files/VideoLAN/VLC/");
            NativeLibrary.addSearchPath(
                RuntimeUtil.getLibVlcLibraryName(), "c:/Program Files (x86)/VideoLAN/VLC/");
            Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
               createAndShowGUI();
            }
         });
   }
}
