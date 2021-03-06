/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.sonido;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import sm.sound.SMSoundRecorder;

/**
 * Clase que representa un grabador de audio. 
 * También implementa su propio <code>ActionListener</code>
 *
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>
 */
public class AudioRecorder implements ActionListener{
    private JButton recordBtn, stopBtn;
    private File temp;
    private SMSoundRecorder smrec;
    private final ImageIcon recGris, recRojo, stopGris, stopRojo, stopAzul;
    private final LienzoSoundRecorder lirec;

    /**
     * Constructor común, que crea una instancia de <code>AudioRecorder</code>
     * y le asocia un <code>LienzoSoundRecorder</code>.
     * 
     * @param lirec <code>LienzoSoundRecorder</code> asociado.
     */
    public AudioRecorder(LienzoSoundRecorder lirec){
        this.lirec = lirec;
        recordBtn = new JButton();
        stopBtn = new JButton();
        
        recGris = new ImageIcon(AudioRecorder.class.getResource("/com/iris/iconos/RecordDisabled_48x48.png"));
        recRojo = new ImageIcon(AudioRecorder.class.getResource("/com/iris/iconos/RecordPressed_48x48.png"));
        stopGris = new ImageIcon(AudioRecorder.class.getResource("/com/iris/iconos/StopDisabled_48x48.png"));
        stopRojo = new ImageIcon(AudioRecorder.class.getResource("/com/iris/iconos/StopNormalRed_48x48.png"));
        stopAzul = new ImageIcon(AudioRecorder.class.getResource("/com/iris/iconos/StopPressedBlue_48x48.png"));

        recordBtn.setDisabledIcon(recRojo);
        recordBtn.setIcon(recGris);
        recordBtn.setPreferredSize(new Dimension(90,90));
        
        stopBtn.setDisabledIcon(stopGris);
        stopBtn.setIcon(stopGris);
        stopBtn.setPreferredSize(new Dimension(90,90));
        stopBtn.setEnabled(false);
        recordBtn.addActionListener(this);
        stopBtn.addActionListener(this);
        
        recordBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                if (recordBtn.isEnabled()){
                    recordBtn.setIcon(recRojo);
                }
            }
            @Override
            public void mouseExited(MouseEvent me) {
                if (recordBtn.isEnabled()){
                    recordBtn.setIcon(recGris);
                }
            }
        });
        stopBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                if (stopBtn.isEnabled()){
                    stopBtn.setIcon(stopAzul);
                }
            }
            @Override
            public void mouseExited(MouseEvent me) {
                if (stopBtn.isEnabled()){
                    stopBtn.setIcon(stopRojo);
                }
            }
        });

    }
    
    /**
     * Devuelve el botón de comienzo de grabación.
     * @return <code>JButton</code> comienzo grabación.
     */
    public JButton getRecordBtn(){
        return recordBtn;
    }
    /**
     * Devuelve el botón de fin de grabación.
     * @return <code>JButton</code> fin grabación.
     */
    public JButton getStopBtn(){
        return stopBtn;
    }

    /**
     * Método que comienza la grabación de sonido.
     */
    public void record(){
        try {
            temp = File.createTempFile("tempfile", ".wav");
            smrec = new SMSoundRecorder(temp);
            smrec.record();
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }
    
    /**
     * Método que para la grabación de sonido.
     * Una vez parada, lanza un dialog al usuario para seleccionar el
     * archivo donde guardar el nuevo sonido.
     */
    public void stop(){
        smrec.stop();
        JFileChooser chooser = new JFileChooser();
        
        int resp = chooser.showSaveDialog(lirec);

        if (resp == JFileChooser.APPROVE_OPTION){
            String path = chooser.getSelectedFile().getAbsolutePath() + ".wav";
            File seleccion = new File(path);
            try {
                Files.copy(temp.toPath(), seleccion.toPath());
                lirec.getReproductorSM().cargarArchivo(seleccion);
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
        temp = null;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        
        if (source == recordBtn && recordBtn.isEnabled()){
            this.record();
            recordBtn.setEnabled(false);
            stopBtn.setEnabled(true);
            stopBtn.setIcon(stopRojo);
        }else if (source == stopBtn && stopBtn.isEnabled()){
            this.stop();
            stopBtn.setEnabled(false);
            recordBtn.setEnabled(true);
            recordBtn.setIcon(recGris);
        }
    }
}
