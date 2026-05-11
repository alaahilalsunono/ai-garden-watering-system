package app;

import javax.sound.sampled.*;
import java.net.URL;

public class MusicPlayer {

    private static Clip clip;

    public static void playLoop(String path) {
        try {
            if (clip != null && clip.isRunning()) return;

            URL url = MusicPlayer.class.getResource(path);

            if (url == null) {
                System.out.println("Music not found!");
                return;
            }

            AudioInputStream audio = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audio);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}