package com.ensa.videots;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioRecorder {

    AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

    DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

    TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(dataInfo);
    public static String pathOfSavedAudioFile = null;

    Thread audioRecorderThread;


    public AudioRecorder() throws LineUnavailableException {
    }

    public void begin() throws LineUnavailableException {
        if (audioRecorderThread != null) {
            end();
            audioRecorderThread.interrupt();
        }
        audioRecorderThread = new Thread(() -> {
            if (!audioRecorderThread.isInterrupted()) {

                AudioInputStream recordingStream = new AudioInputStream(targetLine);
                File outputFile = new File("record.wav");
                try {
                    AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
                    pathOfSavedAudioFile = outputFile.getAbsolutePath();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        targetLine.open();
        targetLine.start();
        audioRecorderThread.setDaemon(true);
        audioRecorderThread.start();
    }

    public void end() {
        targetLine.stop();
        targetLine.close();
    }
}

