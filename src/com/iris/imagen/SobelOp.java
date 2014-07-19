/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.imagen;

import java.awt.image.BufferedImage;
import java.awt.image.Kernel;
import java.awt.image.ConvolveOp;
import java.awt.image.WritableRaster;
import sm.image.BufferedImageOpAdapter;
import sm.image.BufferedImagePixelIterator;

/**
 * Clase que representa la transformación Sobel, esta implementación
 * no trunca los valores, sino que los proporciona en relación al 
 * gradiente máximo.
 * 
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>
 * @version 1.0
 */
public class SobelOp extends BufferedImageOpAdapter {
    /**
     * Constructor por defecto.
     */
   public SobelOp(){}

   /**
    * Sobrecarga del método filter de <code>BufferedImageOpAdapter</code>
    * que aplica el filtro Sobel sobre una imagen <code>BufferedImage</code>
    * recibida como argumento.
    * 
    * @param src <code>BufferedImage</code> original.
    * @param dest <code>BufferedImage</code> transformada.
    * @return <code>BufferedImage</code> transformada.
    */
   @Override
   public BufferedImage filter(BufferedImage src, BufferedImage dest){
      /**
         1. Dos nuevas imágenes aplicando convolveOp para grandiente x e y.
         2. Guardar en maxValue la magnitud (raiz cuadrada de la suma de gradientes en cada pixel) máxima.
         3. Ajustar cada pixel en proporción.
       **/
      if (dest == null){
         dest = createCompatibleDestImage(src, null);
      }

      Kernel kx = MyKernel.createKernel(MyKernel.TYPE_GRADIENTE_X);
      Kernel ky = MyKernel.createKernel(MyKernel.TYPE_GRADIENTE_Y);

      ConvolveOp copx = new ConvolveOp(kx);
      ConvolveOp copy = new ConvolveOp(ky);

      BufferedImage gradx = copx.filter(src, null);
      BufferedImage grady = copy.filter(src, null);

      int maxValue = getMaxGradient(gradx, grady);

      // Una vez sabemos el máximo...
      BufferedImagePixelIterator itx = new BufferedImagePixelIterator(gradx);
      BufferedImagePixelIterator ity = new BufferedImagePixelIterator(grady);
      BufferedImagePixelIterator.PixelData pixelx, pixely;
      WritableRaster destRaster = dest.getRaster();

      while (itx.hasNext()){
         pixelx = itx.next();
         pixely = ity.next();

         int sx = 0;
         int sy = 0;

         for (int i = 0; i < pixelx.sample.length; i++){
            sx += pixelx.sample[i];
            sy += pixely.sample[i];
         }

         int mag = (int) Math.hypot((double)sx,(double)sy);

         mag = (int) (mag*255/maxValue);

         // Poner el mismo valor de magnitud a RGB del pixel
         for (int i = 0; i < pixelx.sample.length; i++){
            pixelx.sample[i] = mag;
         }

         destRaster.setPixel(pixelx.col, pixelx.row, pixelx.sample);
      }

      return dest;
   }

   // Devuelve el mayor valor de magnitud de gradientes
   /**
    * Devuelve el mayor valor de magnitud de gradientes recibidos
    * como argumento.
    * 
    * @param gradx <code>BufferedImage</code> gradiente x.
    * @param grady <code>BufferedImage</code> gradiente y.
    * @return <code>int</code> máximo
    */
   private int getMaxGradient(BufferedImage gradx, BufferedImage grady){
      int max = 0;
      BufferedImagePixelIterator itx = new BufferedImagePixelIterator(gradx);
      BufferedImagePixelIterator ity = new BufferedImagePixelIterator(grady);
      BufferedImagePixelIterator.PixelData pixelx, pixely;

      while (itx.hasNext()){
         pixelx = itx.next();
         pixely = ity.next();

         int sx = 0;
         int sy = 0;

         for (int i = 0; i < pixelx.sample.length; i++){
            sx += pixelx.sample[i];
            sy += pixely.sample[i];
         }

         int mag = (int) Math.hypot((double)sx,(double)sy);

         if (mag > max) max = mag;
      }

      return max;
   }
}
