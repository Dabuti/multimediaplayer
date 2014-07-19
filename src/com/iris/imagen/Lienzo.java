/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.imagen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JPanel;


/**
 * Esta clase representa un lienzo de dibujo, que almacena 
 * formas de tipo MyShape y las dibuja sobrecargando el método
 * de la clase heradada <i>JPanel</i>. 
 * 
 * Contiene la implementación de los listener de Ratón.
 * 
 * @author Iris García
 */
public class Lienzo extends JPanel implements MouseListener, MouseMotionListener{
    private LienzoToolBar toolbar;
    private Point clickPoint, dragFin, lastDrag;
    private MyShapes selectedShape;
    private ArrayList<MyShapes> shapes;
    private boolean curve_drawing = false;
    
    /**
     * Constructor por defecto
     */
    public Lienzo(){
        selectedShape = null;
        shapes = new ArrayList();
        setBackground(Color.white);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }
    
    /**
     * Constructor común que asocia un toolbar al lienzo.
     * 
     * @param toolbar Objeto LienzoToolBar a asociar.
     */
    public Lienzo(LienzoToolBar toolbar){
        this();
        this.toolbar = toolbar;
    }
    
    /**
     * Asocia un toolbar recibido como argumento al lienzo.
     * 
     * @param toolbar Objeto LienzoToolBar a asociar.
     */
    public void setToolBar(LienzoToolBar toolbar){
        this.toolbar = toolbar;
    }
    
    /**
     * Devuelve el toolbar asociado al lienzo.
     * 
     * @return LienzoToolBar asociado al lienzo.
     */
    public LienzoToolBar getToolBar(){ return this.toolbar; }

    /**
     * Devuelve la forma MyShape que contiene al punto dónde se hizo click.
     * 
     * @return Forma MyShape que contiene el punto clickPoint, null si no
     * es contenido por ninguna forma.
     */
    public MyShapes getSelectedShape(){ 
        for (MyShapes shape : shapes)
            if (shape.contains(clickPoint))
                return shape;

      return null;
    }

    /**
     * Elimina todas las formas MyShape del lienzo.
     */
    public void removeShapes(){ shapes.clear(); }
    
    /**
     * Método que crea un objeto de tipo Color a partir de
     * un String recibido como argumento.
     * 
     * @param colorStr Nombre del color a crear.
     * @return Objeto Color creado.
     */
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

    /**
     * Método que devuelve las formas creadas en el lienzo.
     * 
     * @return Colección de formas MyShapes.
     */
    public ArrayList<MyShapes> getShapes(){
       return shapes;
    }

    /**
     * Método llamado para crear una forma nueva, la que esté seleccionada en la
     * barra toolbar.
     */
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
      case "Curva":
         if (clickPoint != null){
            MyCurve2D nuevo = new MyCurve2D((double) clickPoint.x, (double) clickPoint.y);
            setShapeProperties(nuevo); 
            shapes.add(nuevo);
            curve_drawing = true;
         }
         break;
      }
   }

    /**
     * Actualiza los atributos de una forma MyShape recibida como argumento,
     * con los seleccionados en la barra de herramientas toolbar.
     * 
     * @param shape Forma MyShape a actualizar atributos.
     */
    private void setShapeProperties(MyShapes shape){
        shape.setfgColor(toolbar.fgcolorSelected());
        shape.setbgColor(toolbar.bgcolorSelected());
        shape.setGrosor(toolbar.getGrosor());
        shape.setFilled(toolbar.relleno());
        shape.setFilledType(toolbar.getTipoRelleno());
        shape.setFilledDirection(toolbar.getDirRelleno());
        shape.setTrazo(toolbar.getTrazo());
    }
   
    /**
     * Método que actualiza la última forma añadida al lienzo,
     * con los puntos de click inicial y drag final.
     */
    public void updateShape(){
      MyShapes shape = shapes.get(shapes.size()-1);

      if (dragFin != null)
         shape.update(clickPoint, dragFin);
    }
  
    /**
     * Método de pintado del lienzo, que incluye el pintado 
     * de todas las formas del lienzo en cuestión.
     * 
     * @param g Objeto gráfico donde pintar.
     */
    @Override
    public void paint(final Graphics g){
      super.paint(g);
      Graphics2D g2d = (Graphics2D) g;

      for (MyShapes s : shapes){
         s.draw(g2d);
      }
    }

    /**
     * Método de pintado que pinta una imagen recibida como argumento,
     * y las formas creadas en el lienzo.
     * 
     * @param g Objeto gráfico donde pintar.
     * @param img Imagen a pintar.
     */
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
        // Si se estaba pintando una curva, este click es para el punto de control
        if (curve_drawing){
            MyCurve2D curva = (MyCurve2D) shapes.get(shapes.size()-1);
            curva.updateCtrl(clickPoint);
            curve_drawing = false;
            return;
        }
        
        // Poner todas las formas sin seleccionar.
        if (toolbar.getSelectedShape() != null)
            toolbar.getSelectedShape().setSelected(false);
        
        toolbar.setSelectedShape(null);
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
        if (!toolbar.editable() && !toolbar.seleccion() && !curve_drawing)
            updateShape();

        dragFin = null;
        lastDrag = null;
        selectedShape = null;
    }

    @Override
    public void mouseDragged(MouseEvent evt) {
        dragFin = evt.getPoint();
        if (toolbar.editable()){
           if (selectedShape != null)
              selectedShape.move(lastDrag, dragFin);
        }else if(!toolbar.seleccion()){
           updateShape();
        }
        
        lastDrag = dragFin;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent evt) {
    }

    @Override
    public void mouseExited(MouseEvent evt) {
    }   

    @Override
    public void mouseMoved(MouseEvent evt) {
        // Si se estaba pintando una curva bezier
        if (curve_drawing){
            // Obtener la curva y actualizar el punto de control.
            MyCurve2D curva = (MyCurve2D) shapes.get(shapes.size()-1);
            curva.updateCtrl(evt.getPoint());
            repaint();
        }
            
        return;
    }
}
