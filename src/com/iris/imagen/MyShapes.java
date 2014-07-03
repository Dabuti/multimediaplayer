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
   public void setfgColor(Color col);
   public void setbgColor(Color col);
   public void setGrosor(int grosor);
   public void setFilled(boolean filled);
   public void setFilledType(int type);
   public void setFilledDirection(int dir);
   public void setTrazo(int trazo);
   
   public Color getfgColor();
   public Color getbgColor();
   public int getGrosor();
   public boolean getFilled();
   public int getFilledType();
   public int getFilledDirection();
   public int getTrazo();
   public void setSelected(boolean selection);
   public void update(Point p1, Point p2);
   public void move(Point p1, Point p2);
   public boolean contains(Point2D p1);
   public void draw(Graphics2D g2d);
}