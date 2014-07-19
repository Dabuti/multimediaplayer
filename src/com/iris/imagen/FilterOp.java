/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.imagen;

import java.awt.image.BufferedImage;

/**
 * Clase abstracta para la transformación de imágenes.
 * 
 * @author Anónimo
 */

abstract class FilterOp {
    public static final int MAX_INTESITY = 255;

    /**
     * Método que transforma un pixel pasado como argumento.
     * 
     * @param pixel el pixel a convertir.
     * @return El valor del pixel convertido en entero.
     */
    public abstract int filterOp(int pixel);
    
    /**
     * Método general para todas las transformaciones, recorre cada pixel
     * de la imagen y le aplica la transformación implementada en <i>filterOp</i>.
     * 
     * @param src Imagen para la transformación.
     * @return Imagen resultante de la transformación.
     */
    public BufferedImage filter(BufferedImage src){
        BufferedImage result = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        for(int x = 0; x < src.getWidth(); x++)
            for(int y = 0; y < src.getHeight(); y++)
                result.setRGB(x, y, filterOp(src.getRGB(x, y)));
        return result;
    }
    
    /**
     * Extracts the red component of a pixel.
     * @param pixel an integer pixel
     * @return  the red component [0-255] of the pixel.
     */
    public static int getRed(int pixel) {
        return pixel >> 16 & 0xff;
    }

    /**
     * Extracts the green component of a pixel.
     * @param pixel an integer pixel
     * @return  the green component [0-255] of the pixel.
     */
    public static int getGreen(int pixel) {
        return pixel >> 8 & 0xff;
    }

    /**
     * Extracts the blue component of a pixel.
     * @param pixel an integer pixel
     * @return  the blue component [0-255] of the pixel.
     */
    public static int getBlue(int pixel) {
        return pixel & 0xff;
    }
    
    /**
     * Extracts all components of a pixel.
     * @param pixel an integer pixel
     * @return  an array with r, g, b values. r is at index 0
     * g is at index 1, and b is at index 2.
     */    
    public static int[] getRGB(int pixel) {
        return new int[]{getRed(pixel), getGreen(pixel), getBlue(pixel)};
    }
    
    /**
     * Constructs a pixel from RGB components.
     * @param red   the red component [0-255]
     * @param green the green component [0-255]
     * @param blue  the blue component [0-255]
     * @return  the packed integer pixel.
     */
    public static int makePixel(int red, int green, int blue) {
        red = Math.min(Math.max(red, 0), MAX_INTESITY);
        blue = Math.min(Math.max(blue, 0), MAX_INTESITY);
        green = Math.min(Math.max(green, 0), MAX_INTESITY);
        return (red & 0xff) << 16 | (green & 0xff) << 8 | (blue & 0xff);
    }
} // end of FilterOp class
