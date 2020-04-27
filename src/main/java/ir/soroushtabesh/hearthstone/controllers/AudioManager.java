package ir.soroushtabesh.hearthstone.controllers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URISyntaxException;

public class AudioManager {
    private static AudioManager instance;

    MediaPlayer mediaPlayer_bg;
    Media hit_bg;
    Thread thread_bg;

    private AudioManager() {
    }

    public static AudioManager getInstance() {
        if (instance == null)
            instance = new AudioManager();
        return instance;
    }

    public void startBackgroundMusic() {
        if (mediaPlayer_bg != null) {
            mediaPlayer_bg.play();
            return;
        }
        if (hit_bg == null) {
            try {
                hit_bg = new Media(getClass().getClassLoader().getResource("sound/pull-up-a-chair-lq.mp3").toURI().toString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return;
            }
        }
        mediaPlayer_bg = new MediaPlayer(hit_bg);
        mediaPlayer_bg.setCycleCount(-1);
        mediaPlayer_bg.setOnEndOfMedia(() -> mediaPlayer_bg.seek(new Duration(9000)));
        mediaPlayer_bg.play();
    }

    public void stopBackgroundMusic() {
        if (mediaPlayer_bg != null)
            mediaPlayer_bg.stop();
    }

}
