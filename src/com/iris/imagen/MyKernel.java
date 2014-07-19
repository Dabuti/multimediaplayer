/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.imagen;

import java.awt.image.Kernel;

/**
 *
 * @author Jesus Chamorro
 */

public class MyKernel {

    //Tipos de máscaras
    final static int TYPE_MEDIA_3x3 = 0;
    final static int TYPE_BINOMIAL_3x3 = 1;
    final static int TYPE_ENFOQUE_3x3 = 2;
    final static int TYPE_RELIEVE_3x3 = 3;
    final static int TYPE_LAPLACIANA_3x3 = 4;
    final static int TYPE_GRADIENTE_X = 5;
    final static int TYPE_GRADIENTE_Y = 6;
    final static int TYPE_SEPIA = 7;

    /**
     * Crea objetos Kernel a partir de máscaras predefinidas
     */
    static Kernel createKernel(int type){
        switch(type){
            case TYPE_MEDIA_3x3:
                 return new Kernel(3,3,MASCARA_MEDIA_3x3);
            case TYPE_BINOMIAL_3x3:
                 return new Kernel(3,3,MASCARA_BINOMIAL_3x3);
            case TYPE_ENFOQUE_3x3:
                 return new Kernel(3,3,MASCARA_ENFOQUE_3x3);
            case TYPE_RELIEVE_3x3:
                 return new Kernel(3,3,MASCARA_RELIEVE_3x3);
            case TYPE_LAPLACIANA_3x3:
                 return new Kernel(3,3,MASCARA_LAPLACIANA_3x3);
            case TYPE_GRADIENTE_X:
                 return new Kernel(3,3,MASCARA_GRADIENTE_X);
            case TYPE_GRADIENTE_Y:
                 return new Kernel(3,3,MASCARA_GRADIENTE_Y);
            case TYPE_SEPIA:
                 return new Kernel(3,3,MASCARA_SEPIA);
            default:
                 return null;
        }
    }

    /**
     * Constructor privado
     */
    private MyKernel(){}

   // Máscaras de convolución
    static float MASCARA_MEDIA_3x3[] = {
      0.1f, 0.1f, 0.1f,
      0.1f, 0.2f, 0.1f,
      0.1f, 0.1f, 0.1f
    };
    static float MASCARA_BINOMIAL_3x3[] = {
      0.0625f, 0.125f, 0.0625f,
      0.125f, 0.25f, 0.125f,
      0.0625f, 0.125f, 0.0625f
    };
    static float MASCARA_ENFOQUE_3x3[] = {
      0.0f, -1.0f, 0.0f,
      -1.0f, 5.0f, -1.0f,
      0.0f, -1.0f, 0.0f
    };
    static float MASCARA_LAPLACIANA_3x3[] = {
      1.0f, 1.0f, 1.0f,
      1.0f, -8.0f, 1.0f,
      1.0f, 1.0f, 1.0f
    };
    static float MASCARA_RELIEVE_3x3[] = {
      -2.0f, -1.0f, 0.0f,
      -1.0f, 1.0f, 1.0f,
      0.0f, 1.0f, 2.0f
    };
    static float MASCARA_GRADIENTE_X[] = {
      -1.0f, -2.0f, -10.0f,
      0.0f, 0.0f, 0.0f,
      1.0f, 2.0f, 1.0f
    };
   static float MASCARA_GRADIENTE_Y[] = {
      1.0f, 0.0f, -1.0f,
      2.0f, 0.0f, -2.0f,
      1.0f, 0.0f, -1.0f
    };
   static float MASCARA_SEPIA[] = {
      0.393f, 0.769f, 0.189f,
      0.349f, 0.686f, 0.168f,
      0.272f, 0.534f, 0.131f
   };
    // Fin máscaras de convolución
}