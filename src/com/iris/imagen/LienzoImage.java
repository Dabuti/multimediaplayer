/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.imagen;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Clase que hereda de Lienzo, su propósito es permitir la funcionalidad
 * de la clase Lienzo además de poder trabajar con una imagen.
 * 
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>
 */
public class LienzoImage extends Lienzo{
   private BufferedImage img, original;
   private int bright, rotation, umbral;
   private String last_trans = "";
   private VentanaInternaImg vi = null;
   private boolean nuevo;
   private File file;
   
   /**
    * Constructor por defecto <code>LienzoImage</code>.
    *
    * @param img <code>BufferedImage</code> imagen asociada al lienzo.
    */

   public LienzoImage(BufferedImage img) {
      super();
      this.img = img;
      this.original = img;
      this.bright = this.rotation = this.umbral = 0;
      this.nuevo = false;
      this.file = null;
      this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
      this.setMaximumSize(new Dimension(img.getWidth(), img.getHeight()));
      this.setMinimumSize(new Dimension(img.getWidth(), img.getHeight()));
      //updateDimension();
   }
   
   /**
    * Método que actualiza las dimensiones del <code>LienzoImage</code> acorde
    * con las dimensiones de la imagen asociada (esta imagen puede transformarse,
    * y por tanto cambiar sus dimensiones).
    */
   private void updateDimension(){
      this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
      this.setMaximumSize(new Dimension(img.getWidth(), img.getHeight()));
      this.setMinimumSize(new Dimension(img.getWidth(), img.getHeight()));
      
      if (vi != null){
          vi.updateSize();
          vi.validate();
          vi.repaint();
      }
   }
   
   // Setters
   /**
    * Asocia una <code>VentanaInternaImg</code> al <code>LienzoImage</code>
    * @param vi <code>VentanaInternaImg</code> a asociar.
    */
   public void setInternalFrame(VentanaInternaImg vi){ this.vi = vi; }
   /**
    * Ajusta la imagen del <code>LienzoImage</code> a la recibida como argumento.
    * @param img </code>BufferedImage</code> a asociar.
    */
   public void setImage(BufferedImage img){ this.img = img; updateDimension(); }
   /**
    * Actualiza la última transformación realizada al <code>LienzoImage</code>.
    * @param value <code>String</code> última transformación.
    */
   public void setUltimaTrans(String value){ this.last_trans = value; }
   /**
    * Determina si el <code>LienzoImage</code> es nuevo o no.
    * @param op <code>Boolean</code> <code>true</code> si es nuevo,
    * <code>false</code> en caso contrario.
    */
   public void setNuevo(boolean op){ this.nuevo = op; }
   /**
    * Asocia un objeto <code>File</code> al <code>LienzoImage</code>
    * @param f <code>File</code> a asociar.
    */
   public void setFile(File f){ this.file = f; }
   /**
    * Actualiza la imagen original del lienzo a la resultante después de pintar,
    * sus transformaciones y sus formas asociadas.
    * También elimina las formas asociadas.
    */
   public void updateOriginal(){ this.original = this.getFilteredImageRGB(); this.removeShapes(); this.img = this.original; }
   /**
    * Guarda el contenido del <code>LienzoImage</code> en un archivo recibido
    * como argumento.
    * @param f <code>File</code> dónde guardar el <code>LienzoImage</code>
    */
   public void saveToFile(File f){
      String ext = "";

      int i = f.getAbsolutePath().lastIndexOf('.');
      if (i > 0) {
         ext = f.getAbsolutePath().substring(i+1);
      }
      
      try{
        ImageIO.write(getFilteredImage(), ext, f);
      }catch(IOException e){
        System.err.println(e);
      }
   }
   
   // Getters
   /**
    * Devuelve la última transformación realizada al <code>LienzoImage</code>.
    * @return <code>String</code> última transformación.
    */
   public String getUltimaTrans(){ return this.last_trans; }
   /**
    * Devuelve el valor de brillo del <code>LienzoImage</code>
    * @return <code>Integer</code> valor de brillo.
    */
   public int getBright(){ return this.bright; }
   /**
    * Devuelve el valor de umbral del <code>LienzoImage</code>
    * @return <code>Integer</code> valor de umbral.
    */
   public int getUmbral(){ return this.umbral; }
   /**
    * Devuelve la imagen original del <code>LienzoImage</code>.
    * Útil para transformaciones que requieren de la imagen 
    * original de punto de partida.
    * @return <code>BufferedImage</code> original.
    */
   public BufferedImage getImage(){ return original;  }
   /**
    * Devuelve la imagen transformada.
    * @return <code>BufferedImage</code> transformada.
    */
   public BufferedImage getFilteredImage(){
      Graphics2D big = (Graphics2D) img.createGraphics();

      for (MyShapes s : this.getShapes()){
         s.draw(big);
      }

      this.removeShapes();
      return img;
   }
   /**
    * Devuelve la imagen transformada y convertida a RGB.
    * Necesario para poder aplicar algunas transformaciones.
    * 
    * @return <code>BufferedImage</code> transformada y convertida a RGB.
    */
   public BufferedImage getFilteredImageRGB(){
      int w = img.getWidth();
      int h = img.getHeight();

      BufferedImage source = new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB);
      Raster raster = img.getRaster().createChild(0, 0, w, h, 0, 0,
                                                  new int[]{0,1,2});
      source.setData(raster);

      Graphics2D big = (Graphics2D) source.createGraphics();

      for (MyShapes s : this.getShapes()){
         s.draw(big);
      }

      this.removeShapes();
      return source;
   }
   /**
    * Devuelve el <code>File</code> asociado al <code>LienzoImage</code>
    * @return <code>File</code> asociado.
    */
   public File getFile(){ return this.file; }
   /**
    * Devuelve si el <code>Lienzo</code> es nuevo o no.
    * @return <code>boolean</code> <code>true</code> si es nuevo,
    * <code>false</code> en caso contrario.
    */
   public boolean isNuevo(){ return this.nuevo; }
   // Setters
   /**
    * Ajusta el valor de brillo.
    * @param value <code>Integer</code> valor de brillo.
    */
   public void setBright(int value){ this.bright = value; }
   /**
    * Ajusta el valor de umbral.
    * @param value <code>Integer</code> valor de umbral.
    */
   public void setUmbral(int value){ this.umbral = value; }

   @Override
   public void paint(final Graphics g){
      super.paint(g, img);
   }
}
