/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.imagen;

import java.awt.geom.Line2D;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;
import java.awt.BasicStroke;

/**
 * Describe class MyLine2D here.
 *
 *
 * Created: Thu Apr  3 18:25:45 2014
 *
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>
 * @version 1.0
 */
public class MyLine2D extends Line2D implements MyShapes{

   /**
    * Creates a new <code>MyLine2D</code> instance.
    *
    */
   private double x1, x2, y1, y2;
   private Color fgcolor = Color.black;
   private Color bgcolor = Color.black;
   private BasicStroke stroke;;
   private boolean filled = false, selected = false;
   private int tiporelleno, dirrelleno, trazo, grosor = 2, padding = 4;
   private MyRectangle2D rectSeleccion = null;

   public MyLine2D() {}

   public MyLine2D(Point p1, Point p2) {
      this.x1 = (double) p1.x;
      this.y1 = (double) p1.y;
      this.x2 = (double) p2.x;
      this.y2 = (double) p2.y;
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
   public void setFilledType(int type) { this.tiporelleno = type; }
   @Override
   public void setFilledDirection(int dir) { this.dirrelleno = dir; }
   @Override
   public void setTrazo(int trazo) { this.trazo = trazo; }
   @Override
   public void setGrosor(int grosor){ if (grosor > 2) this.grosor = grosor;}
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
   public void setLine(double x1, double y1, double x2, double y2){
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;
   }

   @Override
   public Point2D getP1(){ return new Point2D.Double(x1, y1); }
   @Override
   public Point2D getP2(){ return new Point2D.Double(x2, y2); }
   @Override
   public double getX1(){ return x1; }
   @Override
   public double getX2(){ return x2; }
   @Override
   public double getY1(){ return y1; }
   @Override
   public double getY2(){ return y2; }
   @Override
   public Rectangle2D getBounds2D(){
      return new Rectangle2D.Double(x1, y1, x2, y2);
   }

   @Override
   public void update(Point p1, Point p2){
      setLine(p1.x, p1.y, p2.x, p2.y);
   }

   @Override
   public boolean contains(Point2D p1){
      return (ptLineDist(p1) <= stroke.getLineWidth());
   }

   @Override
   public void move(Point p1, Point p2){
      double offset_x = p2.x - p1.x;
      double offset_y = p2.y - p1.y;

      setLine(x1 + offset_x, y1 + offset_y,
              x2 + offset_x, y2 + offset_y);
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
      
      if (this.selected){
        Point p1 = new Point((int) this.getP1().getX(), (int) this.getP1().getY());
        Point p2 = new Point((int) this.getP2().getX(), (int) this.getP2().getY());
        
        rectSeleccion = new MyRectangle2D(0,0,0,0); 
        rectSeleccion.update(p1, p2);
        
        stroke = new BasicStroke(1,  // Width
                        BasicStroke.CAP_ROUND,     // End cap
                        BasicStroke.JOIN_ROUND,     // Join style
                        5.0f,                     // Miter limit
                        new float[] {10.0f,15.0f}, // Dash pattern
                        0.0f);                     // Dash phase
        g.setStroke(stroke);
        g.setColor(Color.darkGray);
        g.draw(rectSeleccion);
      }
   }  
}
