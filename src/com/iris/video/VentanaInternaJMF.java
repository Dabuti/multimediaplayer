/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.video;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.CannotRealizeException;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 *
 * @author 
 */
public class VentanaInternaJMF extends JInternalFrame implements InternalFrameListener{
    private Player player = null;
    
    private VentanaInternaJMF(File f){
        try{
            MediaLocator ml = new MediaLocator(f.getAbsolutePath());
            player = Manager.createPlayer(ml);
        }catch(IOException ioe){
            System.err.println(ioe);
        } catch (NoPlayerException ex) {
            System.err.println(ex);
        }
    }
    
    public static VentanaInternaJMF getInstance(File f){
        VentanaInternaJMF nueva = new VentanaInternaJMF(f);
        return nueva;
    }


    public void play(){ player.start(); }
    public void stop(){ player.stop(); }
    
    @Override
    public void internalFrameOpened(InternalFrameEvent ife) {   }
    @Override
    public void internalFrameClosing(InternalFrameEvent ife) { player.close(); }
    @Override
    public void internalFrameClosed(InternalFrameEvent ife) { }
    @Override
    public void internalFrameIconified(InternalFrameEvent ife) { }
    @Override
    public void internalFrameDeiconified(InternalFrameEvent ife) {  }
    @Override
    public void internalFrameActivated(InternalFrameEvent ife) {  }
    @Override
    public void internalFrameDeactivated(InternalFrameEvent ife) { }
}
