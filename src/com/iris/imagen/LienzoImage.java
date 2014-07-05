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

/**
 *
 * @author dabuti
 */
public class LienzoImage extends Lienzo{
   private BufferedImage img, original;
   private int bright, rotation;
   
   
   /**
    * Creates a new <code>LienzoImage</code> instance.
    *
    */

   public LienzoImage(BufferedImage img) {
      super();
      this.img = img;
      this.original = img;
      this.bright = this.rotation = 0;
      this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
   }

   public void setImage(BufferedImage img){
      this.img = img;
   }

   public void setBright(int value){
      this.bright = value;
   }

  public int getBright(){
      return this.bright;
   }

  public void setRotation(int value){
      this.rotation = value;
   }

  public int getRotation(){
      return this.rotation;
   }

   public BufferedImage getImage(){
      return original;
   }

   public int getImageFormat(){
      System.out.println("Typo: " + img.getType());
      return img.getType();
   }

   public BufferedImage getFilteredImage(){
      Graphics2D big = (Graphics2D) img.createGraphics();

      for (MyShapes s : this.getShapes()){
         s.draw(big);
      }

      return img;
   }

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

      return source;
   }

   @Override
   public void paint(final Graphics g){
      super.paint(g, img);
   }

}
