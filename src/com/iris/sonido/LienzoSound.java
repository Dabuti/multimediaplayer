/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.sonido;


import java.awt.Dimension;
import java.io.File;
import javax.swing.JPanel;


/**
 *
 * @author 
 */
public class LienzoSound extends JPanel{
    private final File soundfile;
    private final AudioPlayer player;
    
    public LienzoSound(File sound){
        this.soundfile = sound;
        this.setPreferredSize(new Dimension(512,250));
        this.player = new AudioPlayer(soundfile);
        this.add(player.getWaveFormPanel());
        this.add(player.getPlayBtn());
        this.add(player.getPauseBtn());
        this.add(player.getStopBtn());
    }
}
