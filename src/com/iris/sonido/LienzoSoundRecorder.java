/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.sonido;

import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author 
 */
public class LienzoSoundRecorder extends JPanel {
    private AudioRecorder recorder;
    
    public LienzoSoundRecorder() {
        this.setPreferredSize(new Dimension(250,250));
        this.recorder = new AudioRecorder(this);
        this.add(recorder.getRecordBtn());
        this.add(recorder.getStopBtn());
    }
    
}
