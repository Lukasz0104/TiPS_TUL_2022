package pl.lodz.p.it.tips;

import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Recorder {

    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;

    class CaptureThread extends Thread {

        public void run() {
            AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
            File audioFile = new File("record.wav");

            try {
                // Opens the line with the specified format, causing the line to acquire
                // any required system resources and become operational.
                targetDataLine.open(audioFormat);

                targetDataLine.start(); // Allows a line to engage in data I/O

                // Writes a stream of bytes representing an audio file
                // of the specified file type to the external file provided
                AudioSystem.write(new AudioInputStream(targetDataLine), fileType, audioFile);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void click() {
        try {
            int _a = System.in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void captureAudio() {
        try {
            audioFormat = new AudioFormat(8000.0F, 16, 1, true, false);

            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

            CaptureThread captureThread = new CaptureThread(); // create a thread
            captureThread.start(); // start the thread
        } catch (NullPointerException e) {
            System.out.println("NullPointerException");
        } catch (IllegalArgumentException | LineUnavailableException e) {
            System.out.println("IllegalArgumentException | LineUnavailableException");
            System.exit(0);
        }
    }

    public void stopAudio() {
        targetDataLine.stop(); // A stopped line should cease I/O activity
        targetDataLine.close(); // Closes the line
    }

    public void playRecord() {
        try {
            Clip clip = AudioSystem.getClip();
            File record = new File("record.wav");
            clip.open(AudioSystem.getAudioInputStream(record));
            clip.start();
            Thread.sleep(clip.getMicrosecondLength() / 1000);
        } catch (Exception exc) {
            System.out.println("error");
        }
    }
}
