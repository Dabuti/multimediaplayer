/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.imagen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;


/**
 *
 * @author dabuti
 */
public class Lienzo extends JPanel implements MouseListener, MouseMotionListener{
    private LienzoToolBar toolbar;
    private Point clickPoint, dragFin, lastDrag;
    private MyShapes selectedShape;
    private ArrayList<MyShapes> shapes;
    
    public Lienzo(){
        selectedShape = null;
        shapes = new ArrayList();
        setBackground(Color.white);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }
    
    public Lienzo(LienzoToolBar toolbar){
        this();
        this.toolbar = toolbar;
    }
    
    public void setToolBar(LienzoToolBar toolbar){
        this.toolbar = toolbar;
    }
    
    public LienzoToolBar getToolBar(){ return this.toolbar; }

    public MyShapes getSelectedShape(){ 
        for (MyShapes shape : shapes)
            if (shape.contains(clickPoint))
                return shape;

      return null;
    }

    public void removeShapes(){ shapes.clear(); }
    
    public static Color toColor(String colorStr){
      Color color = Color.black;
      switch(colorStr){
      case "black":
         color = Color.black;
         break;
      case "red":
         color = Color.red;
         break;
      case "yellow":
         color = Color.yellow;
         break;
      case "white":
         color = Color.white;
         break;
      case "blue":
         color = Color.blue;
         break;
      case "green":
         color = Color.green;
         break;
      }
      
      return color;
   }

    public ArrayList<MyShapes> getShapes(){
       return shapes;
    }

    public void createShape(){
      switch(toolbar.formaSelected()){
      case "Punto":
         if (clickPoint != null){
            MyPoint2D nuevo = new MyPoint2D(clickPoint.x, clickPoint.y);
            setShapeProperties(nuevo);
            shapes.add(nuevo);
         }
         break;
      case "Linea":
         if (clickPoint != null){
            MyLine2D nuevo = new MyLine2D(clickPoint, clickPoint);
            setShapeProperties(nuevo);
            shapes.add(nuevo);
         }
         break;
      case "Rectangulo":
         if (clickPoint != null){
            MyRectangle2D nuevo = new MyRectangle2D((double) clickPoint.x,
                                                    (double) clickPoint.y,
                                                    2.0, 2.0);
            setShapeProperties(nuevo);
            shapes.add(nuevo);
         }
         break;
      case "Elipse":
         if (clickPoint != null){
            MyOval2D nuevo = new MyOval2D((double) clickPoint.x,
                                          (double) clickPoint.y,
                                          2.0, 2.0);
            setShapeProperties(nuevo); 
            shapes.add(nuevo);
         }
         break;
      }
   }

   // Actualiza las propiedades de una forma con la selección actual.
   private void setShapeProperties(MyShapes shape){
        shape.setfgColor(toolbar.fgcolorSelected());
        shape.setbgColor(toolbar.bgcolorSelected());
        shape.setGrosor(toolbar.getGrosor());
        shape.setFilled(toolbar.relleno());
        shape.setFilledType(toolbar.getTipoRelleno());
        shape.setFilledDirection(toolbar.getDirRelleno());
        shape.setTrazo(toolbar.getTrazo());
   }
   
   public void updateShape(){
      MyShapes shape = shapes.get(shapes.size()-1);

      if (dragFin != null)
         shape.update(clickPoint, dragFin);
   }
  
   @Override
   public void paint(final Graphics g){
      super.paint(g);
      Graphics2D g2d = (Graphics2D) g;

      for (MyShapes s : shapes){
         s.draw(g2d);
      }
   }

   public void paint(final Graphics g, BufferedImage img){
      super.paint(g);
      Graphics2D g2d = (Graphics2D) g;

      // Pintar Imagen
      if (img != null) g2d.drawImage(img,0,0,this);
      //Pintar formas
      for (MyShapes s : shapes){
         s.draw(g2d);
      }
   }
   
    // Implementación de métodos abstractos de los listeners
    @Override
    public void mouseClicked(MouseEvent evt) {
        clickPoint = evt.getPoint();
        dragFin = null;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent evt) {
        clickPoint = evt.getPoint();
        lastDrag = clickPoint;
        
        // Poner todas las formas sin seleccionar.
        if (toolbar.selectedShape != null)
            toolbar.selectedShape.setSelected(false);
        
        toolbar.selectedShape = null;
        for (MyShapes s : shapes)
            s.setSelected(false);
        
        if (toolbar.editable()){
            selectedShape = getSelectedShape();
        }else if(toolbar.seleccion()){
            selectedShape = getSelectedShape();
            if (selectedShape != null){
                selectedShape.setSelected(true);
                toolbar.readSelectedShape(selectedShape);
            }
        }else{
            createShape();
        }
    }

    @Override
    public void mouseReleased(MouseEvent evt) {
        if (!toolbar.editable() && !toolbar.seleccion())
            updateShape();

        dragFin = null;
        lastDrag = null;
        selectedShape = null;
    }

    
    /**
     *
     * @param evt
     */
    @Override
    public void mouseDragged(MouseEvent evt) {
        dragFin = evt.getPoint();
        if (toolbar.editable()){
           if (selectedShape != null)
              selectedShape.move(lastDrag, dragFin);
        }else if(!toolbar.seleccion()){
           updateShape();;
        }
        
        lastDrag = dragFin;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent evt) {
        return;
    }

    @Override
    public void mouseExited(MouseEvent evt) {
        return;
    }   

    @Override
    public void mouseMoved(MouseEvent me) {
        return;
    }
}
