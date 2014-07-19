/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.imagen;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import com.iris.reproductorsm.ReproductorSM;

/**
 * Clase que representa una ventana interna de imagen, hereda de <code>JInternalFrame</code>
 * e implementa su propio listener.
 * 
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>
 * @version 1.0
 */
public class VentanaInternaImg extends JInternalFrame implements InternalFrameListener {
    private LienzoImage li = null;
    private JScrollPane scroller;
    private JPanel background;
    private int offset = 1;
    public ReproductorSM repSM;
    
    /**
     * Constructor común, que crea una instancia de <code>VentanaInternaImg</code> a partir
     * de los parametros recibidos como argumento.
     * 
     * @param li <code>LienzoImage</code> lienzo.
     * @param titulo <code>String</code> título de la ventana.
     * @param repSM <code>ReproductorSM</code> asociado.
     */
    public VentanaInternaImg(LienzoImage li, String titulo, ReproductorSM repSM){
        this.repSM = repSM;
        this.li = li;
        this.background = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;

        background.add(li, c);
        background.setBackground(Color.gray);
        background.setPreferredSize(new Dimension(li.getFilteredImage().getWidth()+10, li.getFilteredImage().getHeight()+10));
        scroller = new JScrollPane(background);
        this.add(scroller);
        this.setTitle(titulo);
        this.setPreferredSize(new Dimension(li.getFilteredImage().getWidth()+50, li.getFilteredImage().getHeight()+50));
        this.setMinimumSize(new Dimension(100, 20));
        this.setPreferences();
        this.addInternalFrameListener(this);
        
        if ("Nuevo".equals(titulo))
            li.setNuevo(true);
    }
    
    /**
     * Ajusta el comportamiento de la ventana interna.
     */
    private void setPreferences(){
        this.setMaximizable(true);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setResizable(true);
        this.setVisible(true);
        this.pack();
    }
    
    /**
     * Actualiza las dimensiones de la ventana interna para ajustarse a las
     * del <code>LienzoImage</code>. Ya que puede variar dinámicamente.
     */
    public void updateSize(){
        background.setPreferredSize(new Dimension(li.getFilteredImage().getWidth()+10, li.getFilteredImage().getHeight()+10));
        scroller.setPreferredSize(new Dimension(background.getWidth(), background.getHeight()));
        synchronized(getTreeLock()) {
            validateTree();
        }
        this.setSize(new Dimension(this.getWidth()+1, this.getHeight()+1));
        this.setSize(new Dimension(this.getWidth()-1, this.getHeight()-1));
        this.repaint();

    }
    
    /**
     * Devuelve el <code>LienzoImage</code> asociado a la ventana.
     * 
     * @return <code>LienzoImage</code> asociado.
     */
    public LienzoImage getLienzoImage(){ return this.li; }
    
    @Override
    public void internalFrameOpened(InternalFrameEvent ife) { }
    @Override
    public void internalFrameClosing(InternalFrameEvent ife) {
    }
    @Override
    public void internalFrameClosed(InternalFrameEvent ife) { }
    
    @Override
    public void internalFrameIconified(InternalFrameEvent ife) { 
    }
    @Override
    public void internalFrameDeiconified(InternalFrameEvent ife) {  
    }
    /**
     * Activa los botones de guardar y guardar como, cuando esté activa
     * la ventana interna.
     * 
     * @param ife evento de activación de ventana.
     */
    @Override
    public void internalFrameActivated(InternalFrameEvent ife) { 
        repSM.itemGuardar.setEnabled(true);
        repSM.itemGuardarComo.setEnabled(true);
    }
    /**
     * Desactiva los botones de guardar y guardar como, cuando esté desactivada
     * la ventana interna.
     * 
     * @param ife evento de desactivación de ventana.
     */
    @Override
    public void internalFrameDeactivated(InternalFrameEvent ife) {  
        repSM.itemGuardar.setEnabled(false);
        repSM.itemGuardarComo.setEnabled(false);
    }
}
