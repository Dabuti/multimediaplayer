/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.imagen;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;

/**
 * Clase que representa una curva de Bezier, extiende la funcionalidad de
 * <code>QuadCurve2D.Double</code> e implementa la interface <MyShapes>.
 * 
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>.
 */
public class MyCurve2D extends QuadCurve2D.Double implements MyShapes {
   private double x1, y1, ctrlx, ctrly, x2, y2;
   private Color fgcolor = Color.black;
   private Color bgcolor = Color.black;
   private BasicStroke stroke;;
   private boolean filled = false, selected = false;
   private int tiporelleno, dirrelleno, trazo, grosor = 2, padding = 4;
   private MyRectangle2D rectSeleccion = null;
   private Point2D p1, p2, ctrlp;

   /**
    * Constructor por defecto.
    */
   public MyCurve2D() {}
   /**
    * Constructor común. Crea una instancia a partir de un 
    * punto.
    * 
    * @param x <code>double</code> coordenada x de un punto.
    * @param y <code>double</code> coordenada y de un punto.
    */
   public MyCurve2D(double x, double y) {
      super(x, y, x, y, x, y);
      p1 = new Point2D.Double(x, y);
      ctrlp = new Point2D.Double(x, y);
      p2 = new Point2D.Double(x, y);
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
   public void setSelected(boolean selection) { this.selected = selection; }
   
   // Para la curva significa el punto final de la curva, x2 e y2
   @Override
   public void update(Point p1, Point p2){
       double xctrl, yctrl;
       this.p1.setLocation(p1.x, p1.y);
       this.p2.setLocation(p2.x, p2.y);
       
       // Punto medio
       xctrl = (p1.x + p2.x) / 2;
       yctrl = (p1.y + p2.y) / 2;
       
       this.ctrlp.setLocation(xctrl, yctrl);
       
       setCurve(this.p1, this.ctrlp, this.p2);
   }
   
   // Método para actualizar punto de control
   /**
    * Actualiza el punto de control de la curva.
    * 
    * @param p <code>Point</code> de control.
    */
   public void updateCtrl(Point p){
       this.ctrlp.setLocation(p.x, p.y);
       setCurve(this.p1, this.ctrlp, this.p2);
   }
   
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
   public void move(Point p1, Point p2) {
      double offset_x = p2.x - p1.x;
      double offset_y = p2.y - p1.y;
      
      this.p1.setLocation(this.p1.getX() + offset_x, this.p1.getY() + offset_y);
      this.p2.setLocation(this.p2.getX() + offset_x, this.p2.getY() + offset_y);
      this.ctrlp.setLocation(this.ctrlp.getX() + offset_x, this.ctrlp.getY() + offset_y);  
      setCurve(this.p1, this.ctrlp, this.p2);
   }
   @Override
   public void draw(Graphics2D g) {
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
        Rectangle2D rec = this.getBounds2D();
        
        stroke = new BasicStroke(1,  // Width
                        BasicStroke.CAP_ROUND,     // End cap
                        BasicStroke.JOIN_ROUND,     // Join style
                        5.0f,                     // Miter limit
                        new float[] {10.0f,15.0f}, // Dash pattern
                        0.0f);                     // Dash phase
        g.setStroke(stroke);
        g.setColor(Color.darkGray);
        g.draw(rec);
      }
   }
}
