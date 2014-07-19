/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.imagen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;


/**
 * Describe class MyShapes here.
 *
 *
 * Created: Thu Apr  3 18:34:09 2014
 *
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>
 * @version 1.0
 */
public interface MyShapes {
    
   // Setters
   public void setfgColor(Color col);
   public void setbgColor(Color col);
   public void setGrosor(int grosor);
   public void setFilled(boolean filled);
   public void setFilledType(int type);
   public void setFilledDirection(int dir);
   public void setTrazo(int trazo);
   public void setSelected(boolean selection);
   
   // Getters
   public Color getfgColor();
   public Color getbgColor();
   public int getGrosor();
   public boolean getFilled();
   public int getFilledType();
   public int getFilledDirection();
   public int getTrazo();
   
   
   /**
    * Actualiza el <code>MyShapes</code> con dos puntos recibidos
    * como argumento.
    * 
    * @param p1 <code>Point</code> p1.
    * @param p2 <code>Point</code> p2.
    */
   public void update(Point p1, Point p2);
   /**
    * Mueve el <code>MyShapes</code> en proporción a la
    * distancia entre p1 y p2.
    * 
    * @param p1 <code>Point</code> p1.
    * @param p2 <code>Point</code> p2.
    */
   public void move(Point p1, Point p2);
   /**
    * Método que determina si un <code>Point</code> recibido
    * como argumento pertenece o no al <code>MyShapes</code>
    * 
    * @param p1 <code>Point</code> punto.
    * @return <code>boolean</code> <code>true</code> si el punto
    * es contenido, <code>false</code> en caso contrario.
    */
   public boolean contains(Point2D p1);
   /**
    * Método que pinta el <code>MyShapes</code> en el objeto gráfico
    * recibido como argumento.
    * 
    * @param g2d <code>Graphics2D</code>.
    */
   public void draw(Graphics2D g2d);
}