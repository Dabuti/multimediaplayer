/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.imagen;

/**
 * La clase SepiaOp es una clase de transformación de imágenes.
 * Su filtro permite convertir una imagen en su equivalente en sepia.
 * 
 * @author Iris García.
 * @version 1.0
 * @see FilterOp
 */
public class SepiaOp extends FilterOp {
    /**
     * Constructor por defecto.
     */
    public SepiaOp(){ 
    }
    
   /**
    * Devuelve el valor RGB en forma de entero de un pixel recibido como argumento
    * después de aplicar la transformación en sepia.
    * 
    * @param pixel  El pixel original a transformar.
    * @return       El valor entero del pixel transformado.
    */
    @Override
    public int filterOp(int pixel) {
        int inputRed, inputGreen, inputBlue, outRed, outGreen, outBlue;
        inputRed = getRed(pixel);
        inputGreen = getGreen(pixel);
        inputBlue = getBlue(pixel);
        outRed = (int)((inputRed * .393) + (inputGreen * .769) + (inputBlue * .189));
        outGreen = (int)((inputRed * .349) + (inputGreen * .686) + (inputBlue * .168));
        outBlue = (int)((inputRed * .272) + (inputGreen *.534) + (inputBlue * .131));
         
        return makePixel(outRed, outGreen, outBlue);
    }
}
