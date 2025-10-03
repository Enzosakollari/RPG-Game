package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {

    Clip clip;
    URL soundUrl[]=new URL[30];
    public Sound(){
        try {
            soundUrl[0]=getClass().getResource("/sound/mainaudio.wav.mkv");
            soundUrl[1]=getClass().getResource("/sound/coin.wav");
            soundUrl[2]=getClass().getResource("/sound/Powerup.wav");
            soundUrl[3]=getClass().getResource("/sound/unlock.wav");
            soundUrl[4]=getClass().getResource("/sound/fanfare.wav");
            soundUrl[5]=getClass().getResource("/sound/knifesharpener1.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setFile(int i){
        try{
            if (soundUrl[i] == null) {
                System.err.println("Sound file not found for index: " + i);
                return;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundUrl[i]);
            clip=AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void play(){
        if (clip != null) {
            clip.start();
        }
    }
    public void loop(){
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    public void stop(){
        if (clip != null) {
            clip.stop();
        }
    }
}
