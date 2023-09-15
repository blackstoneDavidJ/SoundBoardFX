package Recorder;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Target;

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
    private long recordLength;
    private String filePath;
    private File wavFile;

    private final TargetDataLine targetDataLine;

    public void stopRecording()
    {
        targetDataLine.stop();
        targetDataLine.close();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getRecordLength() {
        return recordLength;
    }

    public void setRecordLength(long recordLength) {
        this.recordLength = recordLength;
    }

    public File RecordSound() throws LineUnavailableException
    {
        return record(targetDataLine);
    }
    private File record(TargetDataLine targetDataLine) throws LineUnavailableException {

        targetDataLine.open();
        targetDataLine.start();
        System.out.println("Recording...");

        Thread stopper = new Thread(() -> {
            AudioInputStream audioStream = new AudioInputStream(targetDataLine);
            wavFile = new File(filePath + "/" + name + ".wav");
            try {
                AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, wavFile);
            }

            catch (IOException e) {
                e.printStackTrace();
            }
        });

        stopper.start();
        try {

            targetDataLine.stop();
            targetDataLine.close();
            System.out.println("End");
        }

        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return wavFile;
    }
}