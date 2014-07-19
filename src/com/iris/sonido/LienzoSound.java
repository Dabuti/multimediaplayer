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
 * Clase que representa un lienzo de sonido simple, hereda de <code>JPanel</code>.
 * 
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>
 */
public class LienzoSound extends JPanel{
    private final File soundfile;
    private final AudioPlayer player;
    
    /**
     * Constructor común, que crea una instancia de <code>LienzoSound</code> a
     * partir de un archivo recibido como argumento.
     * 
     * @param sound <code>File</code> archivo sonido simple.
     */
    public LienzoSound(File sound){
        this.soundfile = sound;
        this.setPreferredSize(new Dimension(512,180));
        this.player = new AudioPlayer(soundfile);
        this.add(player.getWaveFormPanel());
        this.add(player.getPlayBtn());
        this.add(player.getPauseBtn());
        this.add(player.getStopBtn());
    }
}
