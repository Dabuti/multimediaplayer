/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.iris.sonido;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;

/**
 * Clase que representa un reproductor de sonidos simples,
 * Permitiendo la reproducción, pausa y parado de los mismos.
 * Además como innovación se ha añadido una clase interna para 
 * dibujar la amplitud de onda que se escucha en cada momento de
 * la reproducción.
 * 
 * @author Iris García <a href="mailto:irisgarcia@correo.ugr.es"></a>
 */
public class AudioPlayer implements ActionListener {
    public static final int DEF_BUFFER_SAMPLE_SZ = 1024;
    public static final Color LIGHT_BLUE = new Color(128, 192, 255);
    public static final Color DARK_BLUE = new Color(0, 0, 127);

    public enum PlayStat {
        NO_FILE, PLAYING, PAUSED, STOPPED
    }

    private final WaveFormPanel waveformpanel = new WaveFormPanel();
    private AudioFormat audioFormat;
    private File audioFile;
    private PlayStat playStat;
    private final Object statLock = new Object();
    private final AudioButton bPlay = new AudioButton("Play");
    private final AudioButton bPause = new AudioButton("Pause");
    private final AudioButton bStop = new AudioButton("Stop");
    
    /**
     * Constructor común, que crea una instancia de <code>AudioPlayer</code> a partir
     * de un fichero recibido como argumento.
     * 
     * @param sound <code>File</code> sonido simple
     */
    public AudioPlayer(File sound){
        try {
            AudioFileFormat fmt = AudioSystem.getAudioFileFormat(sound);
            audioFile = sound;
            audioFormat = fmt.getFormat();
            playStat = PlayStat.STOPPED;

        } catch(IOException ioe) {
            System.err.println(ioe);
        } catch(UnsupportedAudioFileException uafe) {
            System.err.println(uafe);
        }
        
        
        bPlay.addActionListener(this);
        bPause.addActionListener(this);
        bStop.addActionListener(this);
    }

    /**
     * Devuelve el botón Play del reproductor de sonido.
     * 
     * @return <code>AudioButton</code> play
     */
    public AudioButton getPlayBtn(){ return bPlay; } 
    /**
     * Devuelve el botón Pause del reproductor de sonido.
     * 
     * @return <code>AudioButton</code> pause
     */
    public AudioButton getPauseBtn(){ return bPause; } 
    /**
     * Devuelve el botón Stop del reproductor de sonido.
     * 
     * @return <code>AudioButton</code> stop
     */
    public AudioButton getStopBtn(){ return bStop; } 
    /**
     * Devuelve el <code>JPanel</code> donde se dibuja la amplitud
     * de la onda de sonido.
     * 
     * @return <code>JPanel</code> waveformpanel
     */
    public JPanel getWaveFormPanel(){ return this.waveformpanel; }
    /**
     * Devuelve el objeto utilizado para la sincronización.
     * 
     * @return <code>Object</code> statLock
     */
    public Object getLock() { return statLock; }
    /**
     * Devuelve el estado actual del reproductor:
     * <ul>
     * <li>Parado</li>
     * <li>Pausado</li>
     * <li>Reproduciendo</li>
     * </ul>
     * @return <code>PlayStat</code> estado.
     */
    public PlayStat getStat() { return playStat; }
    /**
     * Devuelve el fichero de audio.
     * 
     * @return <code>File</code> audio file.
     */
    public File getFile() {return audioFile; }   
    
    /**
     * Método que limpia el panel de onda de audio y pone
     * el estado del reproductor a Parado.
     */
    public void playEnded() {
        synchronized(statLock) {
            playStat = PlayStat.STOPPED;
        }
        
        waveformpanel.reset();
        waveformpanel.repaint();
    }
    /**
     * Pinta en el <code>JPanel</code> de amplitud de onda
     * los valores, de la reproducción actual.
     * 
     * @param samples <code>float[]</code> muestras
     * @param svalid <code>int</code> límite de frames
     */
    public void drawDisplay(float[] samples, int svalid){
        waveformpanel.makePath(samples, svalid);
        waveformpanel.repaint();
    }
  
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        
        if(source == bPlay
                && audioFile != null
                && playStat != PlayStat.PLAYING) {
            
            synchronized(statLock) {
                switch(playStat) {
                    
                    case STOPPED: {
                        playStat = PlayStat.PLAYING;
                        new PlayLoop(this).execute();
                        break;
                    }
                        
                    case PAUSED: {
                        playStat = PlayStat.PLAYING;
                        statLock.notifyAll();
                        break;
                    }
                }
            }
            
        } else if(source == bPause
                && playStat == PlayStat.PLAYING) {
            
            synchronized(statLock) {
                playStat = PlayStat.PAUSED;
            }
            
        } else if(source == bStop
                && (playStat == PlayStat.PLAYING || playStat == PlayStat.PAUSED)) {
            
            synchronized(statLock) {
                switch(playStat) {
                    
                    case PAUSED: {
                        playStat = PlayStat.STOPPED;
                        statLock.notifyAll();
                        break;
                    }
                        
                    case PLAYING: {
                        playStat = PlayStat.STOPPED;
                        break;
                    }
                }
            }
        }
    }

    // Clases internas
    /**
     * Clase que representa un <code>JPanel</code> para dibujar en él
     * la amplitud de onde del sonido que se está reproduciendo.
     */
    public class WaveFormPanel extends JPanel{
        private final BufferedImage image;
        
        private final Path2D.Float[] paths = {
            new Path2D.Float(), new Path2D.Float(), new Path2D.Float()
        };
        
        private final Object pathLock = new Object();
        {
            Dimension pref = getPreferredSize();
            
            image = (
                GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration()
                .createCompatibleImage(
                    pref.width, pref.height, Transparency.OPAQUE
                )
            );
        }
        
        /**
         * Constructor por defecto.
         */
        public WaveFormPanel() {
            setOpaque(false);
        }
        
        /**
         * Resetea el panel completamente, con fondo negro.
         */
        public void reset() {
            Graphics2D g2d = image.createGraphics();
            g2d.setBackground(Color.BLACK);
            g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
            g2d.dispose();
        }
        
        /**
         * Compone el camino de las ondas a partir de las muestras 
         * recibidas como argumento.
         * 
         * @param samples <code>float[]</code> muestras
         * @param svalid <code>int</code> frames
         */
        public void makePath(float[] samples, int svalid) {
            if(audioFormat == null) {
                return;
            }
            
            Path2D.Float current = paths[2];
            paths[2] = paths[1];
            paths[1] = paths[0];
            
            float avg = 0f;
            float hd2 = getHeight() / 2f;
            final int channels = audioFormat.getChannels();
            
            int i = 0;
            while(i < channels && i < svalid) {
                avg += samples[i++];
            }
            
            avg /= channels;
            
            current.reset();
            current.moveTo(0, hd2 - avg * hd2);
            
            int fvalid = svalid / channels;
            for(int ch, frame = 0; i < svalid; frame++) {
                avg = 0f;
                
                for(ch = 0; ch < channels; ch++) {
                    avg += samples[i++];
                }
                
                avg /= channels;
                
                current.lineTo(
                    (float)frame / fvalid * image.getWidth(), hd2 - avg * hd2
                );
            }
            
            paths[0] = current;
            Graphics2D g2d = image.createGraphics();
            
            synchronized(pathLock) {
                g2d.setBackground(Color.BLACK);
                g2d.clearRect(0, 0, image.getWidth(), image.getHeight());
                
                g2d.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2d.setRenderingHint(
                    RenderingHints.KEY_STROKE_CONTROL,
                    RenderingHints.VALUE_STROKE_PURE
                );
                
                g2d.setPaint(DARK_BLUE);
                g2d.draw(paths[2]);
                
                g2d.setPaint(LIGHT_BLUE);
                g2d.draw(paths[1]);
                
                g2d.setPaint(Color.WHITE);
                g2d.draw(paths[0]);
            }
            
            g2d.dispose();
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            synchronized(pathLock) {
                g.drawImage(image, 0, 0, null);
            }
        }
        
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(DEF_BUFFER_SAMPLE_SZ / 2, 128);
        }
        
        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }
        
        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }
    }

    /**
     * Clase encargada de la reproducción del sonido, hereda de <code>SwingWorker</code>
     * para poder lanzar una nueva hebra con cada reproducción.
     * 
     */
    public class PlayLoop extends SwingWorker<Void, Void>{
        private final AudioPlayer audioPlayer;

        /**
         * Constructor común.
         * 
         * @param ap <code>AudioPlayer</code> reproductor de audio.
         */
        public PlayLoop(AudioPlayer ap) {
            this.audioPlayer = ap;
        }
        
        /**
         * Sobrecarga del método que lanza una nueva hebra en cada llamada,
         * para reproducir el sonido.
         * 
         * @return 
         */
        @Override
        public Void doInBackground() {
            try {
                AudioInputStream in = null;
                SourceDataLine out = null;
                
                try {
                    try {
                        final AudioFormat audioFormat = (
                            AudioSystem.getAudioFileFormat(audioPlayer.getFile()).getFormat()
                        );
                        
                        in = AudioSystem.getAudioInputStream(audioPlayer.getFile());
                        out = AudioSystem.getSourceDataLine(audioFormat);
                        
                        final int normalBytes = normalBytesFromBits(audioFormat.getSampleSizeInBits());
                        
                        float[] samples = new float[DEF_BUFFER_SAMPLE_SZ * audioFormat.getChannels()];
                        long[] transfer = new long[samples.length];
                        byte[] bytes = new byte[samples.length * normalBytes];
                        
                        out.open(audioFormat, bytes.length);
                        out.start();
                        
                        for(int feed = 0; feed < 6; feed++) {
                            out.write(bytes, 0, bytes.length);
                        }
                        
                        int bread;
                        
                        play_loop: do {
                            while(audioPlayer.getStat() == PlayStat.PLAYING) {
                                
                                if((bread = in.read(bytes)) == -1) {
                                    
                                    break play_loop; // eof
                                }
                                
                                samples = unpack(bytes, transfer, samples, bread, audioFormat);
                                samples = window(samples, bread / normalBytes, audioFormat);
                                
                                audioPlayer.drawDisplay(samples, bread / normalBytes);
                                
                                out.write(bytes, 0, bread);
                            }
                            
                            if(audioPlayer.getStat() == PlayStat.PAUSED) {
                                out.flush();
                                try {
                                    synchronized(audioPlayer.getLock()) {
                                        audioPlayer.getLock().wait(1000L);
                                    }
                                } catch(InterruptedException ie) {}
                                continue;
                            } else {
                                break;
                            }
                        } while(true);
                        
                    } catch(UnsupportedAudioFileException uafe) {
                        System.err.println(uafe);
                    } catch(LineUnavailableException lue) {
                        System.err.println(lue);
                    }
                } finally {
                    if(in != null) {
                        in.close();
                    }
                    if(out != null) {
                        out.flush();
                        out.close();
                    }
                }
            } catch(IOException ioe) {
                System.err.println(ioe);
            }
            
            return (Void) null;
        }
        
        /**
         * Método que finaliza la reproducción de sonido.
         */
        @Override
        public void done() {
            audioPlayer.playEnded();
            
            try {
                get();
            } catch(InterruptedException io) {
            } catch(CancellationException ce) {
            } catch(ExecutionException ee) {
                System.err.println(ee.getCause());
            }
        }
    }

    /**
     * Clase que representa un botón del reproductor de sonido simple.
     */
    public static class AudioButton extends JButton {
        public AudioButton(String text) {
            super(text);
            
            setOpaque(true);
            setBorderPainted(true);
            setBackground(Color.BLACK);
            setForeground(Color.WHITE);
            
            setBorder(new LineBorder(Color.GRAY) {
                @Override
                public Insets getBorderInsets(Component c) {
                    return new Insets(1, 4, 1, 4);
                }
                @Override
                public Insets getBorderInsets(Component c, Insets i) {
                    return getBorderInsets(c);
                }
            });
            
            Font font = getFont();
            setFont(font.deriveFont(font.getSize() - 1f));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent me) {
                    if(me.getButton() == MouseEvent.BUTTON1) {
                        setForeground(LIGHT_BLUE);
                    }
                }
                @Override
                public void mouseReleased(MouseEvent me) {
                    if(me.getButton() == MouseEvent.BUTTON1) {
                        setForeground(Color.WHITE);
                    }
                }
            });
        }
    }
    
    public static float[] unpack(
        byte[] bytes,
        long[] transfer,
        float[] samples,
        int bvalid,
        AudioFormat fmt
    ) {
        if(fmt.getEncoding() != AudioFormat.Encoding.PCM_SIGNED
                && fmt.getEncoding() != AudioFormat.Encoding.PCM_UNSIGNED) {
            
            return samples;
        }
        
        final int bitsPerSample = fmt.getSampleSizeInBits();
        final int bytesPerSample = bitsPerSample / 8;
        final int normalBytes = normalBytesFromBits(bitsPerSample);
        
        
        if(fmt.isBigEndian()) {
            for(int i = 0, k = 0, b; i < bvalid; i += normalBytes, k++) {
                transfer[k] = 0L;
                
                int least = i + normalBytes - 1;
                for(b = 0; b < normalBytes; b++) {
                    transfer[k] |= (bytes[least - b] & 0xffL) << (8 * b);
                }
            }
        } else {
            for(int i = 0, k = 0, b; i < bvalid; i += normalBytes, k++) {
                transfer[k] = 0L;
                
                for(b = 0; b < normalBytes; b++) {
                    transfer[k] |= (bytes[i + b] & 0xffL) << (8 * b);
                }
            }
        }
        
        final long fullScale = (long)Math.pow(2.0, bitsPerSample - 1);
        
        if(fmt.getEncoding() == AudioFormat.Encoding.PCM_SIGNED) {
            final long signShift = 64L - bitsPerSample;
            
            for(int i = 0; i < transfer.length; i++) {
                transfer[i] = (
                    (transfer[i] << signShift) >> signShift
                );
            }
        }else{
            for(int i = 0; i < transfer.length; i++) {
                transfer[i] -= fullScale;
            }
        }
        
        for(int i = 0; i < transfer.length; i++) {
            samples[i] = (float)transfer[i] / (float)fullScale;
        }
        
        return samples;
    }
    
    public static float[] window(float[] samples,int svalid, AudioFormat fmt) {       
        int channels = fmt.getChannels();
        int slen = svalid / channels;
        
        for(int ch = 0, k, i; ch < channels; ch++) {
            for(i = ch, k = 0; i < svalid; i += channels) {
                samples[i] *= Math.sin(Math.PI * k++ / (slen - 1));
            }
        }
        
        return samples;
    }
    
    public static int normalBytesFromBits(int bitsPerSample) {
        return bitsPerSample + 7 >> 3;
    }
}
