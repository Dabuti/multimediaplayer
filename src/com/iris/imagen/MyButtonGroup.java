/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.imagen;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;

/**
 * Clase que extiende a <code>ButtonGroup</code> impidiendo el borrado
 * total de la selección de un botón.
 * 
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>.
 */
public class MyButtonGroup extends ButtonGroup {
   @Override
   public void setSelected(ButtonModel model, boolean selected) {
      if (selected){
         super.setSelected(model, selected);
      }else{
         //clearSelection();
      }
   }
}
