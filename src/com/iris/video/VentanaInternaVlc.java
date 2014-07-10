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
 *
 * @author 
 */
public class VentanaInternaVlc extends JInternalFrame implements InternalFrameListener{
    private final MediaPlayerFactory mediaPlayerFactory;
    private final EmbeddedMediaPlayer player;
    private final PlayerControlsPanel controls;
    private final Canvas videoSurface;
    private File filename;    
    
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
    
    public void play(){ player.playMedia(this.filename.getAbsolutePath()); }
    public void stop(){ player.stop(); }
    public BufferedImage snapshot(){
        return this.player.getSnapshot(videoSurface.getWidth(), videoSurface.getHeight());
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent ife) { System.out.println("Abierto"); }
    @Override
    public void internalFrameClosing(InternalFrameEvent ife) {
        if (this.player.isPlaying())
            this.player.stop();
        
        this.player.release();
        this.mediaPlayerFactory.release();
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
