/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.sonido;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 * Clase que representa una ventana interna de tipo sonido simple o grabación de sonido.
 * hereda de <code>JInternalFrame</code> e implementa sus propios listeners.
 *
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>
 */
public class VentanaInternaSound extends JInternalFrame implements InternalFrameListener{
    private LienzoSound ls = null;
    private LienzoSoundRecorder lsr = null;

    /**
     * Constructor común, que crea una instancia <code>VentanaInternaSound</code>
     * para un lienzo de sonido simple.
     * 
     * @param ls <code>LienzoSound</code> lienzo sonido simple.
     * @param title <code>String</code> título ventana interna.
     */
    public VentanaInternaSound(LienzoSound ls, String title) {
        this.ls = ls;
        this.setContentPane(ls);
        this.setTitle(title);
        this.setMaximizable(false);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setResizable(false);
        this.setVisible(true);
        this.pack();
    }
    
    /**
     * Constructor común, que crea una instancia <code>VentanaInternaSound</code>
     * para un lienzo de grabación de sonido.
     * 
     * @param lsr <code>LienzoSoundRecorder</code> lienzo grabación sonido.
     * @param title <code>String</code> título ventana interna.
     */
    public VentanaInternaSound(LienzoSoundRecorder lsr, String title){
        this.lsr = lsr;
        this.setContentPane(lsr);
        this.setTitle(title);
        this.setMaximizable(false);
        this.setClosable(true);
        this.setIconifiable(true);
        this.setResizable(false);
        this.setVisible(true);
        this.pack();
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent ife) {
        if (ls != null)
            this.setPreferredSize(this.ls.getSize());
        else
            this.setPreferredSize(this.lsr.getSize());
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent ife) {
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent ife) {
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent ife) {
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent ife) {
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent ife) {
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent ife) {
    }
}
