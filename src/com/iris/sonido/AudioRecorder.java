/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.sonido;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import sm.sound.SMSoundRecorder;

/**
 *
 * @author
 */
public class AudioRecorder implements ActionListener{
    private JButton recordBtn, stopBtn;
    private File temp;
    private SMSoundRecorder smrec;
    private ImageIcon recGris, recRojo, stopGris, stopRojo;
    private JPanel panel;

    public AudioRecorder(JPanel panel){
        this.panel = panel;
        recordBtn = new JButton();
        stopBtn = new JButton();
        
        recGris = new ImageIcon(AudioRecorder.class.getResource("/com/iris/iconos/RecordDisabled_48x48.png"));
        recRojo = new ImageIcon(AudioRecorder.class.getResource("/com/iris/iconos/RecordPressed_48x48.png"));
        stopGris = new ImageIcon(AudioRecorder.class.getResource("/com/iris/iconos/StopDisabled_48x48.png"));
        stopRojo = new ImageIcon(AudioRecorder.class.getResource("/com/iris/iconos/StopNormalRed_48x48.png"));

        recordBtn.setDisabledIcon(recRojo);
        recordBtn.setIcon(recGris);
        recordBtn.setPreferredSize(new Dimension(48,48));
        
        stopBtn.setDisabledIcon(stopGris);
        stopBtn.setIcon(stopGris);
        stopBtn.setPreferredSize(new Dimension(48,48));
        recordBtn.addActionListener(this);
        stopBtn.addActionListener(this);
    }
    
    public JButton getRecordBtn(){
        return recordBtn;
    }
    public JButton getStopBtn(){
        return stopBtn;
    }

    public void record(){
        try {
            temp = File.createTempFile("tempfile", ".wav");
            smrec = new SMSoundRecorder(temp);
            smrec.record();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    public void stop(){
        smrec.stop();
        JFileChooser chooser = new JFileChooser();
        
        int resp = chooser.showSaveDialog(panel);

        if (resp == JFileChooser.APPROVE_OPTION){
            String path = chooser.getSelectedFile().getAbsolutePath() + ".wav";
            File seleccion = new File(path);
            try {
                Files.copy(temp.toPath(), seleccion.toPath());
            } catch (IOException ex) {
                System.err.println(ex);
            }
            temp = null;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        
        if (source == recordBtn){
            this.record();
            recordBtn.setEnabled(false);
            stopBtn.setEnabled(true);
            stopBtn.setIcon(stopRojo);
        }else if (source == stopBtn){
            this.stop();
            stopBtn.setEnabled(false);
            recordBtn.setEnabled(true);
            recordBtn.setIcon(recGris);
        }
    }
}
