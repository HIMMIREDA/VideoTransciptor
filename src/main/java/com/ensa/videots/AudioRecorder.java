package com.ensa.videots;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioRecorder {

        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);

        DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

        TargetDataLine targetLine = (TargetDataLine)AudioSystem.getLine(dataInfo);


        Thread audioRecorderThread = new Thread(() -> {
            AudioInputStream recordingStream = new AudioInputStream(targetLine);
            File outputFile = new File("record.wav");
            try{
                AudioSystem.write(recordingStream,AudioFileFormat.Type.WAVE,outputFile);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        });


    public AudioRecorder() throws LineUnavailableException {
    }

    public void begin() throws LineUnavailableException {
        targetLine.open();
        targetLine.start();
        audioRecorderThread.setDaemon(true);
        audioRecorderThread.start();
    }

    public void end(){
        targetLine.stop();
        targetLine.close();
    }



}

