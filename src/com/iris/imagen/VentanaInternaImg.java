/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.imagen;

import com.iris.imagen.LienzoImage;
import com.iris.sonido.LienzoSound;
import java.awt.Dimension;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

/**
 *
 * @author
 */
public class VentanaInternaImg extends JInternalFrame {
    private LienzoImage li = null;
    private JScrollPane scroller;
    
    public VentanaInternaImg(LienzoImage li, String titulo){
        this.li = li;
        this.scroller = new JScrollPane(this.li);
        this.add(scroller);
        this.setPreferences();
        this.setTitle(titulo);
    }
    
    public VentanaInternaImg(LienzoImage li, String titulo, int w, int h){
        this.li = li;
        this.scroller = new JScrollPane(this.li);
        this.add(scroller);
        this.setPreferences();
        this.setTitle(titulo);
        this.setPreferredSize(new Dimension(w, h));
        this.pack();
    }
    
    private void setPreferences(){
        this.setMaximizable(true);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setResizable(true);
        this.setVisible(true);
        this.pack();
    }
    
    public LienzoImage getLienzoImage(){ return this.li; }
}
