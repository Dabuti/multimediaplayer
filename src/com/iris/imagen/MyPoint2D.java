/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.imagen;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.geom.Ellipse2D;
import java.awt.RenderingHints;

/**
 * Describe class MyPoint2D here.
 *
 *
 * Created: Thu Apr  3 18:25:45 2014
 *
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>
 * @version 1.0
 */
public class MyPoint2D extends Ellipse2D.Double implements MyShapes{

   /**
    * Creates a new <code>MyPoint2D</code> instance.
    *
    */
   private Color fgcolor = Color.black;
   private Color bgcolor = Color.black;
   private BasicStroke stroke;;
   private boolean filled = false, selected = false;
   private int tiporelleno, dirrelleno, trazo, grosor;

   public MyPoint2D() {}

   public MyPoint2D(double x, double y) {
      super(x, y, 2, 2);
      stroke = new BasicStroke(1.0f);
      grosor = 2;
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
   
   public void update(Point p1, Point p2){}

   public boolean contains(Point2D p1){
      Point2D tmp = new Point2D.Double(this.getX(), this.getY());
      return (p1.distance(tmp) <= stroke.getLineWidth()+2);
   }

   public void move(Point p1, Point p2){
      double offset_x = p2.x - p1.x;
      double offset_y = p2.y - p1.y;

      setFrame(this.getX() + offset_x, this.getY() + offset_y,
               this.getWidth(), this.getHeight());

   }

   public void draw(Graphics2D g){
      g.setColor(fgcolor);
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                         RenderingHints.VALUE_ANTIALIAS_ON);

      int x_fix = (int) (this.getX() - grosor/2);
      int y_fix = (int) (this.getY() - grosor/2);

      g.fillOval(x_fix, y_fix, grosor, grosor);

      if (this.selected){
        stroke = new BasicStroke(1,             // Width
                    BasicStroke.CAP_ROUND,      // End cap
                    BasicStroke.JOIN_ROUND,     // Join style
                    10.0f,                      // Miter limit
                    new float[] {5.0f,10.0f},  // Dash pattern
                    0.0f);                      // Dash phase
        
        g.setStroke(stroke);
        g.setColor(Color.darkGray);
        g.drawOval(x_fix - 2, y_fix - 2, grosor+2, grosor+2);
      }
   }

   
}