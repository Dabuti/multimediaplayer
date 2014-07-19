/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.video;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.test.basic.PlayerControlsPanel;

/**
 * Clase que representa una ventana interna de tipo Webcam, hereda de
 * <code>JInternalFrame</code> e implementa sus propios listeners.
 *
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>
 */
public class VentanaInternaCam extends JInternalFrame implements InternalFrameListener{
    private final MediaPlayerFactory mediaPlayerFactory;
    private final EmbeddedMediaPlayer player;
    private final PlayerControlsPanel controls;
    private final Canvas videoSurface;
    private String mrl = null;
    
    /**
     * Constructor por defecto.
     */
    public VentanaInternaCam() {
        mediaPlayerFactory = new MediaPlayerFactory();
        player = mediaPlayerFactory.newEmbeddedMediaPlayer();
        
        videoSurface = new Canvas();
        videoSurface.setBackground(Color.black);
        videoSurface.setSize(800, 600); 
        player.setVideoSurface(mediaPlayerFactory.newVideoSurface(videoSurface));
        controls = new PlayerControlsPanel(this.player);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.black);
        panel.add(videoSurface, BorderLayout.CENTER);
        //panel.add(controls, BorderLayout.SOUTH);
        
        this.setTitle("Webcam");
        this.setContentPane(panel);
        this.setVisible(true);
        this.setPreferredSize(new Dimension(800,600));
        this.setResizable(true);
        this.setMaximizable(true);
        this.setClosable(true);
        this.setIconifiable(true);
        this.pack();
        this.addInternalFrameListener(this);
    }
    
    /**
     * Constructor común.
     * El <i>mrl</i> comunmente será el siguiente:
     * <ul>
     * <li>Linux: v4l2:///dev/video0</li>
     * <li>Windows: dshow://</li>
     * </ul>
     * 
     * @param mrl <code>String</code> Dirección del recurso de webcam
     */
    public VentanaInternaCam(String mrl) {
        this();
        this.mrl = mrl;
    }
    
    /**
     * Comienza la reproducción.
     */
    public void play(){ player.playMedia(mrl); }
    /**
     * Para la reproducción.
     */
    public void stop(){ player.stop(); }
    /**
     * Crea una captura instantánea.
     * 
     * @return <code>BufferedImage</code> captura.
     */
    public BufferedImage snapshot(){
        BufferedImage captura = null;
        captura = this.player.getSnapshot(videoSurface.getWidth(), videoSurface.getHeight());
        return captura;
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent ife) { }
    @Override
    public void internalFrameClosing(InternalFrameEvent ife) {
        if (player.isPlaying())
            player.stop();
        
        // Se comenta el release, por algún motivo falla en windows por problema de permisos
        // con la librería vlc.
        //player.release();
        //mediaPlayerFactory.release();
    }
    @Override
    public void internalFrameClosed(InternalFrameEvent ife) { }
    
    @Override
    public void internalFrameIconified(InternalFrameEvent ife) { }
    @Override
    public void internalFrameDeiconified(InternalFrameEvent ife) {  }
    @Override
    public void internalFrameActivated(InternalFrameEvent ife) {  }
    @Override
    public void internalFrameDeactivated(InternalFrameEvent ife) {  }    
}
