/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.sonido;

import java.awt.Dimension;
import javax.swing.JPanel;
import com.iris.reproductorsm.ReproductorSM;

/**
 * Clase que representa un lienzo de grabación de sonido, hereda de
 * <code>JPanel</code>
 *
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>
 */
public class LienzoSoundRecorder extends JPanel {
    private final AudioRecorder recorder;
    private final ReproductorSM repsm;
    
    /**
     * Constructor común, que crea una instancia de <code>LienzoSoundRecorder</code> y
     * lo asocia a un <code>ReproductorSM</code>
     * 
     * @param repsm <code>ReproductorSM</code> reproductor multimedia.
     */
    public LienzoSoundRecorder(ReproductorSM repsm) {
        this.setPreferredSize(new Dimension(220,110));
        this.recorder = new AudioRecorder(this);
        this.add(recorder.getRecordBtn());
        this.add(recorder.getStopBtn());
        this.repsm = repsm;
    }
    
    /**
     * Devuelve el reproductor multimedia asociado.
     * 
     * @return <code>ReproductorSM</code> asociado.
     */
    public ReproductorSM getReproductorSM(){ return this.repsm; }
}
