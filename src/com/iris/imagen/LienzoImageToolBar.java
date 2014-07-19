/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.imagen;

import com.iris.reproductorsm.ReproductorSM;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.awt.image.LookupTable;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.beans.PropertyVetoException;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import sm.image.KernelProducer;
import sm.image.LookupTableProducer;
import sm.image.ThresholdOp;


/**
 * Clase encargada de construir un <code>JPanel</code> con las herramientas necesarias
 * para la manipulación de imágenes. 
 * 
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>.
 */
public class LienzoImageToolBar extends JPanel {
    private final JPanel rotPanel, zoomPanel, contPanel, filtPanel, umbPanel, otrosPanel;
    private final JDesktopPane desktop;
    private String filtro, tipoUmbral;
    private Kernel kfiltro;
    private final ReproductorSM repSM;


    /**
     * Constructor común.
     * @param repSM <code>ReproductorSM</code> asociado.
     */
    public LienzoImageToolBar(ReproductorSM repSM) {
        this.repSM = repSM;
        this.desktop = repSM.getDesktop();
        GridBagConstraints c;
        kfiltro = KernelProducer.createKernel(KernelProducer.TYPE_MEDIA_3x3);
        tipoUmbral = "Grises";
        filtro = "Media";
        
        rotPanel = createRotPanel();
        zoomPanel = createZoomPanel();
        contPanel = createContPanel();
        filtPanel = createFiltPanel();
        umbPanel = createUmbPanel();
        otrosPanel = createOtrosPanel();
        
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(300, 450));
        this.setMinimumSize(new Dimension(300, 450));
        this.setMaximumSize(new Dimension(300, 450));
        this.setBorder(BorderFactory.createTitledBorder("Imagen:"));

        // Panel rotación
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weighty = 1;
        c.weightx = 1;
        this.add(rotPanel, c);

        // Panel zoom
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weighty = 0;
        c.weightx = 0;
        this.add(zoomPanel, c);

        // Panel contraste
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weighty = 1;
        c.weightx = 1;
        this.add(contPanel, c);

        // Panel filtro
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weighty = 1;
        c.weightx = 1;
        this.add(filtPanel, c);
        
        // Panel umbralización
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weighty = 1;
        c.weightx = 1;
        this.add(umbPanel, c);
        
        // Panel Otros
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weighty = 1;
        c.weightx = 1;
        this.add(otrosPanel, c);
    }

    /**
     * Crea un <code>JPanel</code> con funciones de transformaciones:
     * Duplicar y Sepia
     * 
     * @return <code>JPanel</code> con Otras transformaciones.
     */
    private JPanel createOtrosPanel(){
        JPanel panel = new JPanel();
        JButton grises = new JButton("Grises");
        JButton duplicar = new JButton("Duplicar");
        JButton propia = new JButton("Sepia");
        
        grises.setToolTipText("Escala de grises");
        duplicar.setToolTipText("Duplicar imagen");
        propia.setToolTipText("Transformar a Sepia");
        
        panel.setBorder(BorderFactory.createTitledBorder("Otros:"));
        panel.setPreferredSize(new Dimension(120,120));
        
        grises.setPreferredSize(new Dimension(70,40));
        duplicar.setPreferredSize(new Dimension(70,40));
        propia.setPreferredSize(new Dimension(70,40));
        grises.setMargin(new Insets(0,0,0,0));
        duplicar.setMargin(new Insets(0,0,0,0));
        propia.setMargin(new Insets(0,0,0,0));
        
        panel.setLayout(new GridLayout(3, 1));
        panel.add(grises);
        panel.add(duplicar);
        panel.add(propia);
        
        // Listeners
        duplicar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                LienzoImage li = null;
                if (desktop.getSelectedFrame() != null && desktop.getSelectedFrame() instanceof VentanaInternaImg){
                    li = ((VentanaInternaImg) desktop.getSelectedFrame()).getLienzoImage();
                    
                    if (li != null){
                     // Comprobar última transformación
                     if (!"Duplicar".equals(li.getUltimaTrans())){
                        li.updateOriginal();
                        li.setUltimaTrans("Duplicar");
                     }
                     BufferedImage orig = li.getFilteredImage();


                     // Obtener la imagen original y crear una copia
                     ColorModel cm = orig.getColorModel();
                     boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
                     WritableRaster raster = orig.copyData(null);
                     BufferedImage copia = new BufferedImage(cm, raster, isAlphaPremultiplied, null);

                     LienzoImage licopia = new LienzoImage(copia);
                     licopia.setNuevo(true);
                     licopia.setToolBar(repSM.getLienzoToolBar());
                     VentanaInternaImg viimg = new VentanaInternaImg(licopia, 
                             "Copia " + desktop.getSelectedFrame().getTitle(), repSM);
                     desktop.add(viimg);
                     
                     Dimension d1 = viimg.getSize();
                     Dimension d2 = desktop.getSize();
   
                     try {
                        viimg.setSelected(true);
                        if (d1.width > d2.width || d1.height > d2.height)
                            viimg.setMaximum(true); 
                      } catch (PropertyVetoException ex) {
                        System.err.println(ex);
                      }
                    }
                }
   
            }
        });        
        propia.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                LienzoImage li = null;
                if (desktop.getSelectedFrame() != null && desktop.getSelectedFrame() instanceof VentanaInternaImg){
                    li = ((VentanaInternaImg) desktop.getSelectedFrame()).getLienzoImage();
                    
                    if (li != null){
                     // Comprobar última transformación
                     if (!"Propia".equals(li.getUltimaTrans())){
                        li.updateOriginal();
                        li.setUltimaTrans("Propia");
                     }
                     try{
                        BufferedImage orig = li.getFilteredImage();
                        SepiaOp sop = new SepiaOp();
                        BufferedImage imgdest = sop.filter(orig);
                        li.setImage(imgdest);
                        li.repaint();
                     }catch(IllegalArgumentException e){
                        System.err.println(e);
                     }
                    }
                }
            }
        });

        grises.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                LienzoImage li = null;
                if (desktop.getSelectedFrame() != null && desktop.getSelectedFrame() instanceof VentanaInternaImg){
                    li = ((VentanaInternaImg) desktop.getSelectedFrame()).getLienzoImage();
                    
                    if (li != null){
                     // Comprobar última transformación
                     if (!"Grises".equals(li.getUltimaTrans())){
                        li.updateOriginal();
                        li.setUltimaTrans("Grises");
                     }
                     try{
                        BufferedImage orig = li.getImage();
                        ICC_Profile ip = ICC_Profile.getInstance(ColorSpace.CS_GRAY);
                        ColorSpace cs = new ICC_ColorSpace(ip);
                        ColorConvertOp ccop = new ColorConvertOp(cs, null);
                        BufferedImage imgdest = ccop.filter(orig, null);
                        li.setImage(imgdest);
                        li.repaint();
                     }catch(IllegalArgumentException e){
                        System.err.println(e);
                     }
                    }
                }
            }
        });
        
        return panel;
    }
    
    /**
     * Crea un <code>JPanel</code> con transformaciones de rotación.
     * Botones y slider.
     * @return <code>JPanel</code> con transformaciones de rotación.
     */
    private JPanel createRotPanel() {
        JPanel panel = new JPanel();
        GridBagConstraints c;
        JButton rot90 = new JButton();
        JButton rot180 = new JButton();
        JButton rot270 = new JButton();
        MyButtonGroup rotationBtns = new MyButtonGroup();
        JSlider rotationSli = new JSlider(JSlider.HORIZONTAL, 0, 360, 1);

        ImageIcon icon90 = new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/rotacion90.png"));
        ImageIcon icon180 = new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/rotacion180.png"));
        ImageIcon icon270 = new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/rotacion270.png"));

        // Atributos panel rotación
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Rotación:"));
        panel.setPreferredSize(new Dimension(150,100));
        
        rotationSli.setPreferredSize(new Dimension(100,25));
        rotationSli.setToolTipText("Rotación libre");
        
        rot90.setIcon(icon90);
        rot180.setIcon(icon180);
        rot270.setIcon(icon270);
        rot90.setToolTipText("Rotar 90º");
        rot180.setToolTipText("Rotar 180º");
        rot270.setToolTipText("Rotar 270º");
        rot90.setName("rot90");
        rot180.setName("rot180");
        rot270.setName("rot270");
        rotationBtns.add(rot90);
        rotationBtns.add(rot180);
        rotationBtns.add(rot270);

        // Botón rot90
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(rot90, c);
        
        // Botón rot180
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(rot180, c);
        
        // Botón rot90
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(rot270, c);
        
        // Slider rotación
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.gridwidth = 3;
        panel.add(rotationSli, c);      
        
        // Listeners
        // Botones de rotación
        for (Enumeration<AbstractButton> buttons = rotationBtns.getElements();
           buttons.hasMoreElements();){
            final AbstractButton button = buttons.nextElement();

            button.addMouseListener(new MouseAdapter(){
               public void mouseClicked(MouseEvent evt){
                  String botonStr = button.getName();
                  double r = 0;

                  switch(botonStr){
                  case "rot90":
                     r = Math.toRadians(90);
                     break;
                  case "rot180":
                     r = Math.toRadians(180);
                     break;
                  case "rot270":
                     r = Math.toRadians(270);
                     break;
                  }
                  LienzoImage li = null;
                  if (desktop.getSelectedFrame() != null && desktop.getSelectedFrame() instanceof VentanaInternaImg){
                     li = ((VentanaInternaImg) desktop.getSelectedFrame()).getLienzoImage();   
                  }

                  if (li != null){
                     BufferedImage imgSource = li.getFilteredImageRGB();
                     try{
                        Point p = new Point(imgSource.getWidth()/2, imgSource.getHeight()/2);
                        AffineTransform at = AffineTransform.getRotateInstance(r, p.x, p.y);
                        AffineTransformOp atop;
                        atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                        BufferedImage imgdest = atop.filter(imgSource, null);
                        li.setImage(imgdest);
                        li.removeShapes();
                        li.repaint();
                     }catch(IllegalArgumentException e){
                        System.err.println(e.getLocalizedMessage());
                     }
                  }
               }
            });
        }
        
        // Slider rotación
        rotationSli.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent evt){
               int value = ((JSlider) evt.getSource()).getValue();
               if (desktop.getSelectedFrame() != null && desktop.getSelectedFrame() instanceof VentanaInternaImg){
                 LienzoImage li = ((VentanaInternaImg) desktop.getSelectedFrame()).getLienzoImage();

                 if (li != null){
                   // Comprobar última transformación
                   if (!"Rotacion".equals(li.getUltimaTrans())){
                       li.updateOriginal();
                       li.setUltimaTrans("Rotacion");
                   }
                   BufferedImage imgSource = li.getImage();
                   try{
                      Point p = new Point(imgSource.getWidth()/2, imgSource.getHeight()/2);
                      AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(value),
                                                                             p.x, p.y);
                      AffineTransformOp atop;
                      atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                      BufferedImage imgdest = atop.filter(imgSource, null);
                      li.setImage(imgdest);
                      li.repaint();
                   }catch(IllegalArgumentException e){
                      System.err.println(e.getLocalizedMessage());
                   }
                }
             }
            }
         });
        
        
        return panel;
    }    

    /**
     * Crea un <code>JPanel</code> con transformaciones para aumentar
     * y diminuir la escala de la imagen.
     * 
     * @return <code>JPanel</code> con transformaciones de escala.
     */
    private JPanel createZoomPanel() {
        JPanel panel = new JPanel();
        JButton zoomIn = new JButton();
        JButton zoomOut = new JButton();
        
        ImageIcon iconIn = new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/aumentar.png"));
        ImageIcon iconOut = new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/disminuir.png"));

        zoomIn.setIcon(iconIn);
        zoomOut.setIcon(iconOut);
        zoomIn.setPreferredSize(new Dimension(32, 32));
        zoomOut.setPreferredSize(new Dimension(32, 32));
        zoomIn.setToolTipText("Aumentar escala");
        zoomOut.setToolTipText("Disminuir escala");
        
        // Atributos panel Zoom
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Esacala:"));
        panel.setPreferredSize(new Dimension(110,80));

        panel.add(zoomIn);
        panel.add(zoomOut);
        
        // Listeners
        zoomIn.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent evt){
               if (desktop.getSelectedFrame() != null && desktop.getSelectedFrame() instanceof VentanaInternaImg){
                 LienzoImage li = ((VentanaInternaImg) desktop.getSelectedFrame()).getLienzoImage();

                 if (li != null){
                   BufferedImage imgSource = li.getFilteredImageRGB();
                   if (!"zoomIn".equals(li.getUltimaTrans())){
                      li.updateOriginal();
                      li.setUltimaTrans("zoomIn");
                   }
                   try{
                      Point p = new Point(imgSource.getWidth()/2, imgSource.getHeight()/2);
                      AffineTransform at = AffineTransform.getScaleInstance(1.25,1.25);

                      AffineTransformOp atop;
                      atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                      BufferedImage imgdest = atop.filter(imgSource, null);
                      li.setImage(imgdest);
                      //li.repaint();
                   }catch(IllegalArgumentException e){
                      System.err.println(e.getLocalizedMessage());
                   }
                }
               }
            }
         });

        zoomOut.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent evt){
               if (desktop.getSelectedFrame() != null && desktop.getSelectedFrame() instanceof VentanaInternaImg){
                  LienzoImage li = ((VentanaInternaImg) desktop.getSelectedFrame()).getLienzoImage();

                   if (li != null){
                      BufferedImage imgSource = li.getFilteredImageRGB();
                      if (!"zoomOut".equals(li.getUltimaTrans())){
                        li.updateOriginal();
                        li.setUltimaTrans("zoomOut");
                      }
                      try{
                         Point p = new Point(imgSource.getWidth()/2, imgSource.getHeight()/2);
                         AffineTransform at = AffineTransform.getScaleInstance(0.75, 0.75);

                         AffineTransformOp atop;
                         atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                         BufferedImage imgdest = atop.filter(imgSource, null);
                         li.setImage(imgdest);
                         li.repaint();
                      }catch(IllegalArgumentException e){
                         System.err.println(e.getLocalizedMessage());
                      }
                   }
               }
            }
         });

        return panel;
    }

    /**
     * Crea un <code>JPanel</code> con transformaciones para iluminar,
     * oscurecer y normalizar el contraste de la imagen.
     * También un slider para ajustar el brillo.
     * 
     * @return <code>JPanel</code> con transformaciones de contraste y brillo.
     */
    private JPanel createContPanel() {
        JPanel panel = new JPanel();
        GridBagConstraints c;
        JButton normal = new JButton();
        JButton iluminar = new JButton();
        JButton oscurecer = new JButton();
        MyButtonGroup contrasteBtns = new MyButtonGroup();
        JSlider brilloSli = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);

        ImageIcon icon1 = new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/contraste.png"));
        ImageIcon icon2 = new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/iluminar.png"));
        ImageIcon icon3 = new ImageIcon(Lienzo.class.getResource("/com/iris/iconos/oscurecer.png"));

        // Atributos panel rotación
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Contraste:"));
        panel.setPreferredSize(new Dimension(150,100));
        
        brilloSli.setPreferredSize(new Dimension(100,20));
        brilloSli.setValue(50);
        normal.setIcon(icon1);
        iluminar.setIcon(icon2);
        oscurecer.setIcon(icon3);
        normal.setName("normal");
        iluminar.setName("iluminar");
        oscurecer.setName("oscurecer");
        contrasteBtns.add(normal);
        contrasteBtns.add(iluminar);
        contrasteBtns.add(oscurecer);
        normal.setToolTipText("Normalizar");
        iluminar.setToolTipText("Iluminar");
        oscurecer.setToolTipText("Oscurecer");
        brilloSli.setToolTipText("Ajustar Brillo");
        
        // Botón normal
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(normal, c);
        
        // Botón iluminar
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(iluminar, c);
        
        // Botón oscurecer
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(oscurecer, c);
        
        // Slider brillo
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.gridwidth = 3;
        panel.add(brilloSli, c);      
        
        // Listeners
        // Botones de contraste
        for (Enumeration<AbstractButton> buttons = contrasteBtns.getElements();
           buttons.hasMoreElements();){
         final AbstractButton button = buttons.nextElement();

         button.addMouseListener(new MouseAdapter(){
               public void mouseClicked(MouseEvent evt){
                  String botonStr = button.getName();
                  LookupTable lt = null;

                  switch(botonStr){
                  case "normal":
                     lt = LookupTableProducer.createLookupTable(LookupTableProducer.TYPE_SFUNCION);
                     break;
                  case "iluminar":
                     lt = LookupTableProducer.createLookupTable(LookupTableProducer.TYPE_LOGARITHM);

                     break;
                  case "oscurecer":
                     lt = LookupTableProducer.createLookupTable(LookupTableProducer.TYPE_POWER);
                     break;
                  }

                  LienzoImage li = null;
                  if (desktop.getSelectedFrame() != null && desktop.getSelectedFrame() instanceof VentanaInternaImg)
                     li = ((VentanaInternaImg) desktop.getSelectedFrame()).getLienzoImage();

                  if (li != null){
                     if (!"Contraste".equals(li.getUltimaTrans())){
                       li.updateOriginal();
                       li.setUltimaTrans("Contraste");
                     }
                     BufferedImage imgSource = li.getFilteredImageRGB();
                     try{
                        LookupOp lop = new LookupOp(lt, null);
                        BufferedImage imgdest = lop.filter(imgSource, null);
                        li.setImage(imgdest);
                        li.repaint();
                     }catch(IllegalArgumentException e){
                        System.err.println(e.getLocalizedMessage());
                     }
                  }
               }
            });
        }
        
        // Slider
        brilloSli.addChangeListener(new ChangeListener(){
              public void stateChanged(ChangeEvent evt){
                 int value = ((JSlider) evt.getSource()).getValue();
                 if (desktop.getSelectedFrame() != null && desktop.getSelectedFrame() instanceof VentanaInternaImg){
                     LienzoImage li = ((VentanaInternaImg) desktop.getSelectedFrame()).getLienzoImage();

                     if (li != null){
                        // Comprobar última transformación
                        if (!"Brillo".equals(li.getUltimaTrans())){
                            li.updateOriginal();
                            li.setUltimaTrans("Brillo");
                        }
                        BufferedImage imgSource = li.getFilteredImageRGB();
                        try{
                           value -= li.getBright();
                           RescaleOp rop = new RescaleOp(1.0f, (float) value, null);
                           BufferedImage imgdest = rop.filter(imgSource, null);
                           li.setImage(imgdest);
                           li.setBright(((JSlider) evt.getSource()).getValue());
                           li.repaint();
                        }catch(IllegalArgumentException e){
                           System.err.println(e.getLocalizedMessage());
                        }
                     }
                  }
              }
           });
        
        return panel;
    }

    /**
     * Crea un <code>JPanel</code> que permite aplicar distintos tipos de filtros a la imagen.
     * <ul>
     * <li>Media</li>
     * <li>Binomial</li>
     * <li>Enfoque</li>
     * <li>Relieve</li>
     * <li>Laplaciano</li>
     * <li>Sobel</li>
     * <li>Negativo</li>
     * </ul>
     * @return <code>JPanel</code> de filtros.
     */
    private JPanel createFiltPanel() {
        JPanel panel = new JPanel();
        String [] filters = {"Media", "Binomial", "Enfoque", "Relieve", "Laplaciano", "Sobel", "Negativo"};
        JComboBox tipoFiltro = new JComboBox(filters);
        JButton aplicar = new JButton("Aplicar");

        // Atributos panel Filtros
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Filtros:"));
        panel.setPreferredSize(new Dimension(110,100));
        
        tipoFiltro.setPreferredSize(new Dimension(80, 25));
        aplicar.setPreferredSize(new Dimension(70, 25));
        
        panel.add(tipoFiltro);
        panel.add(aplicar);
        
        // Listeners
        tipoFiltro.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){
               JComboBox cb = (JComboBox) evt.getSource();
               String opcion = (String) cb.getSelectedItem();
               filtro = opcion;
               switch(opcion){
               case "Media":
                  kfiltro = MyKernel.createKernel(MyKernel.TYPE_MEDIA_3x3);
                  break;
               case "Binomial":
                  kfiltro = MyKernel.createKernel(MyKernel.TYPE_BINOMIAL_3x3);
                  break;
               case "Enfoque":
                  kfiltro = MyKernel.createKernel(MyKernel.TYPE_ENFOQUE_3x3);
                  break;
               case "Relieve":
                  kfiltro = MyKernel.createKernel(MyKernel.TYPE_RELIEVE_3x3);
                  break;
               case "Laplaciano":
                  kfiltro = MyKernel.createKernel(MyKernel.TYPE_LAPLACIANA_3x3);
                  break;
               }
            }
        });
        
        aplicar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt){
                 if (desktop.getSelectedFrame() != null && desktop.getSelectedFrame() instanceof VentanaInternaImg){
                   LienzoImage li = ((VentanaInternaImg) desktop.getSelectedFrame()).getLienzoImage();
                   
                   if (li != null){
                    // Comprobar última transformación
                    if (!filtro.equals(li.getUltimaTrans())){
                        li.updateOriginal();
                        li.setUltimaTrans(filtro);
                    }
                    
                    BufferedImage imgSource = li.getFilteredImage();
                    if ("Sobel".equals(filtro)){
                        try{
                            SobelOp sop = new SobelOp();
                            BufferedImage imgdest = sop.filter(imgSource, null);
                            li.setImage(imgdest);
                            li.repaint();
                        }catch(IllegalArgumentException e){
                            System.err.println(e);
                        }
                    }else if("Negativo".equals(filtro)){
                        try{
                            LookupTable lt = LookupTableProducer.negativeFuction();
                            LookupOp lop = new LookupOp(lt, null);
                            BufferedImage imgdest = lop.filter(imgSource, null);
                            li.setImage(imgdest);
                            li.repaint();
                        }catch(IllegalArgumentException e){
                            System.err.println(e);
                        }
                    }else{
                        try{
                           ConvolveOp cop = new ConvolveOp(kfiltro);
                           BufferedImage imgdest = cop.filter(imgSource, null);
                           li.setImage(imgdest);
                           li.repaint();
                        }catch(IllegalArgumentException e){
                           System.err.println(e.getLocalizedMessage());
                        }
                    }
                 }
               }
            }
        });
        
        return panel;
    }

    /**
     * Crea un <code>JPanel</code> que permite umbralizar la imagen, tanto en
     * niveles de grises, como en el espacio de color.
     * 
     * @return <code>JPanel</code> de umbralización.
     */
    private JPanel createUmbPanel() {
        JPanel panel = new JPanel();
        GridBagConstraints c;
        String [] filters = {"Grises", "Colores"};
        JComboBox tipoFiltro = new JComboBox(filters);
        JSlider umbralSli = new JSlider(JSlider.HORIZONTAL, 0, 255, 10);
        final JButton color = new JButton();
        color.setBackground(Color.red);
        
        // Atributos panel Umbralizar
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Umbralizar:"));
        panel.setPreferredSize(new Dimension(150,100));

        color.setPreferredSize(new Dimension(24,24));
        color.setEnabled(false);
        color.setToolTipText("Color a umbralizar");
        umbralSli.setPreferredSize(new Dimension(100,20));
        umbralSli.setToolTipText("Ajustar Umbral");
        
        // Menu tipo de filtro: Color/Grises
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(tipoFiltro, c);
        
        // Botón color umbral
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(color, c);
        
        // Slider umbralización
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.gridwidth = 2;
        panel.add(umbralSli, c);
        
        
        // Listeners:
        // En el combobox activar/desactivar boton color si es color o grises.
        tipoFiltro.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){
               JComboBox cb = (JComboBox) evt.getSource();
               tipoUmbral = (String) cb.getSelectedItem();
               if ("Grises".equals(tipoUmbral))
                   color.setEnabled(false);
               else
                   color.setEnabled(true);
            }
        });
        
        // Botón de selección de color
        color.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){
                Color seleccion = null;
                seleccion = JColorChooser.showDialog(getRootPane(),
                   "Seleccione color de fondo", Color.red);

                if (seleccion != null){
                    ((JButton) evt.getSource()).setBackground(seleccion);
                }
            }
        });
        
        // Slider de umbral
        umbralSli.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent evt){
                int value = ((JSlider) evt.getSource()).getValue();
                if (desktop.getSelectedFrame() != null && desktop.getSelectedFrame() instanceof VentanaInternaImg){
                 LienzoImage li = ((VentanaInternaImg) desktop.getSelectedFrame()).getLienzoImage();

                 if (li != null){
                    // Comprobar última transformación
                    if (!"Umbral".equals(li.getUltimaTrans())){
                        li.updateOriginal();
                        li.setUltimaTrans("Umbral");
                    }
                    
                    BufferedImage imgSource = li.getImage();
                    ThresholdOp top = null;
                    if ("Grises".equals(tipoUmbral)){
                       top = new ThresholdOp(value);
                       top.setType(ThresholdOp.TYPE_GREY_LEVEL);
                    }else{
                       top = new ThresholdOp(color.getBackground(), value);
                       top.setType(ThresholdOp.TYPE_COLOR);
                    }
                    try{
                       BufferedImage imgdest = top.filter(imgSource, null);
                       li.setImage(imgdest);
                       li.repaint();
                    }catch(IllegalArgumentException e){
                       System.err.println(e.getLocalizedMessage());
                    }
                 }
                }
            }
        });
        
        return panel;
    }
}
