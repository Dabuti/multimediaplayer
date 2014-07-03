/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.imagen;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/**
 * Describe class MyRectangle2D here.
 *
 *
 * Created: 
 *
 * @author 
 * @version 1.0
 */
public class MyRectangle2D extends Rectangle2D.Double implements MyShapes{

   /**
    * Creates a new <code>MyRectangle2D</code> instance.
    *
    */
   private double x1, x2, y1, y2;
   private Color fgcolor = Color.black;
   private Color bgcolor = Color.black;
   private BasicStroke stroke;;
   private boolean selected = false, filled = false;
   private int tiporelleno, dirrelleno, trazo, grosor;
   private MyRectangle2D rectSeleccion = null;
   private int padding = 2;

   
   public MyRectangle2D() {}
   public MyRectangle2D(double x, double y, double w, double h) {
      super(x, y, w, h);
      stroke = new BasicStroke(1.0f);
   }

   // My interface
   @Override
   public void setfgColor(Color col){ this.fgcolor = col; }
   @Override
   public void setbgColor(Color col){ this.bgcolor = col; }
   @Override
   public void setFilled(boolean filled){ this.filled = filled; }
   @Override
   public void setGrosor(int grosor){ this.grosor = grosor; }
   @Override
   public void setFilledType(int type) { this.tiporelleno = type; }
   @Override
   public void setFilledDirection(int dir) { this.dirrelleno = dir; }
   @Override
   public void setTrazo(int trazo) { this.trazo = trazo; }
   @Override
   public void update(Point p1, Point p2){ setFrameFromDiagonal(p1, p2); }
   @Override
   public void setSelected(boolean selection) { this.selected = selection; }

   // Getters
   @Override
   public Color getfgColor() { return fgcolor; }
   @Override
   public Color getbgColor() { return bgcolor; }
   @Override
   public int getGrosor() { return grosor; }
   @Override
   public boolean getFilled() { return filled; }
   @Override
   public int getFilledType() { return tiporelleno; }
   @Override
   public int getFilledDirection() { return dirrelleno; }
   @Override
   public int getTrazo() { return trazo; }

   @Override
   public void move(Point p1, Point p2){
      double offset_x = p2.x - p1.x;
      double offset_y = p2.y - p1.y;

      setFrame(this.getX() + offset_x, this.getY() + offset_y,
               this.getWidth(), this.getHeight());
   }

   @Override
   public void draw(Graphics2D g){
      // Comprobar trazo
      if (trazo == LienzoToolBar.DISCONTINUA)
          stroke = new BasicStroke((float) grosor,  // Width
                        BasicStroke.CAP_SQUARE,     // End cap
                        BasicStroke.JOIN_MITER,     // Join style
                           10.0f,                     // Miter limit
                           new float[] {15.0f,15.0f}, // Dash pattern
                           0.0f);                     // Dash phase
      else
          stroke = new BasicStroke((float) grosor);
      
      g.setColor(fgcolor);
      g.setStroke(stroke);
      g.draw(this);

      // Relleno
      if (filled){
         // Liso
         if (tiporelleno == LienzoToolBar.LISO)
             g.setColor(bgcolor);
        // Degradado
         else if(tiporelleno == LienzoToolBar.DEGRADADO){
             GradientPaint gp = null;
             float x1 = (float) this.getMinX();
             float y1 = (float) this.getMinY();
             float x2 = (float) this.getMaxX();
             float y2 = (float) this.getMaxY();
                         
             if (dirrelleno == LienzoToolBar.HORIZONTAL)
                 gp = new GradientPaint(x1, y1, fgcolor, x2, y1, bgcolor);
             if (dirrelleno == LienzoToolBar.VERTICAL)
                 gp = new GradientPaint(x1, y1, fgcolor, x1, y2, bgcolor);
             
             if (gp != null)
                 g.setPaint(gp);
         }
         g.fill(this);
      }
      
      // Si está seleccionado pintar rectangulo discontinuo de selección.
      if (this.selected){
        double ini_x = this.getMinX() - padding - grosor/1.5;
        double ini_y = this.getMinY() - padding - grosor/1.5;
        double fin_x = this.getMaxX() - ini_x + padding + grosor/1.5;
        double fin_y = this.getMaxY() - ini_y + padding + grosor/1.5;

        rectSeleccion = new MyRectangle2D(ini_x, ini_y, fin_x, fin_y);
        stroke = new BasicStroke(1,  // Width
                        BasicStroke.CAP_SQUARE,     // End cap
                        BasicStroke.JOIN_MITER,     // Join style
                        10.0f,                     // Miter limit
                        new float[] {15.0f,15.0f}, // Dash pattern
                        0.0f);                     // Dash phase
        g.setStroke(stroke);
        g.setColor(Color.darkGray);
        g.draw(rectSeleccion);
      }

   }

}