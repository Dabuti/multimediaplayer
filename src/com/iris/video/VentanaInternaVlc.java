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
import java.io.File;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.test.basic.PlayerControlsPanel;

/**
 * Clase que representa una ventana interna de tipo VLC, esta ventana será usada
 * para la reproducción de Videos y sonidos complejos.
 *
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>
 */
public class VentanaInternaVlc extends JInternalFrame implements InternalFrameListener{
    private final MediaPlayerFactory mediaPlayerFactory;
    private final EmbeddedMediaPlayer player;
    private final PlayerControlsPanel controls;
    private final Canvas videoSurface;
    private final File filename;    
    
    /**
     * Constructor común, que crea una instancia de <code>VentanaInternaVlc</code>.
     * También crea un objeto reproductor asociado a la ventana interna.
     * 
     * @param f <code>File</code> archivo de video o sonido.
     */
    public VentanaInternaVlc(File f) {
        mediaPlayerFactory = new MediaPlayerFactory();
        player = mediaPlayerFactory.newEmbeddedMediaPlayer();
        filename = f;
        
        videoSurface = new Canvas();
        videoSurface.setBackground(Color.black);
        videoSurface.setSize(800, 600); 
        player.setVideoSurface(mediaPlayerFactory.newVideoSurface(videoSurface));
        controls = new PlayerControlsPanel(this.player);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.black);
        panel.add(videoSurface, BorderLayout.CENTER);
        panel.add(controls, BorderLayout.SOUTH);
        
        this.setTitle(filename.getName());
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
     * Comienza la reproducción.
     */
    public void play(){ player.playMedia(this.filename.getAbsolutePath()); }
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
        return this.player.getSnapshot(videoSurface.getWidth(), videoSurface.getHeight());
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent ife) { }
    @Override
    public void internalFrameClosing(InternalFrameEvent ife) {
        if (this.player.isPlaying())
            this.player.stop();
    }
    @Override
    public void internalFrameClosed(InternalFrameEvent ife) { }
    
    @Override
    public void internalFrameIconified(InternalFrameEvent ife) { }
    @Override
    public void internalFrameDeiconified(InternalFrameEvent ife) {  
        // Parar y volver a iniciar el video al maximizar ventana
        // Motivo: Bug con librería libvlc.
        float pos = player.getPosition();
        player.stop();
        player.playMedia(this.filename.getAbsolutePath());
        player.setPosition(pos);
        player.start();
    }
    @Override
    public void internalFrameActivated(InternalFrameEvent ife) {  }
    @Override
    public void internalFrameDeactivated(InternalFrameEvent ife) {  }
}
