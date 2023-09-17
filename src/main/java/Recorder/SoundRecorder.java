package Recorder;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Target;

import javafx.application.Platform;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class SoundRecorder {

    private static final int SAMPLE_RATE = 48000;
    private static final int SAMPLE_SIZE_IN_BITS = 16;
    private static final int CHANNELS = 2;
    private static final boolean SIGNED = true;
    private static final boolean BIG_EDIAN = true;
    private String name;
    private final String filePath;
    private static File wavFile;
    private Thread stopper;

    private final TargetDataLine targetDataLine;

    public File stopRecording() throws InterruptedException {
        targetDataLine.stop();
        targetDataLine.close();
        stopper.join();
        System.out.println("Recording Stopped");
        return wavFile;
    }
    public SoundRecorder(String filePath) throws LineUnavailableException {
        this.filePath = filePath;
        AudioFormat format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, SIGNED, BIG_EDIAN);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line not supported!");
        }

        this.targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
    }
    public void record(String name)
    {
        try {
            targetDataLine.open();
            targetDataLine.start();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Recording...");

        stopper = new Thread(() -> {
            String tmpFilePath = filePath + "/" + name;
            AudioInputStream audioStream = new AudioInputStream(targetDataLine);
            try {
                new File(filePath + "/" + name).mkdirs();
                wavFile = new File(tmpFilePath + "/" + name + ".wav");
                AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, wavFile);
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        });

        stopper.start();
    }
}