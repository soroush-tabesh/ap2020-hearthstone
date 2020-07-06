package ir.soroushtabesh.hearthstone.controllers;

import javafx.beans.property.DoubleProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URISyntaxException;

public class AudioManager {
    private static AudioManager instance;

    private MediaPlayer mediaPlayer_bg;
    private Media hit_bg;

    private MediaPlayer mediaPlayer_alarm;
    private Media hit_alarm;

    private AudioManager() {
        try {
            hit_bg = new Media(getClass().getClassLoader()
                    .getResource("sound/pull-up-a-chair-lq.mp3").toURI().toString());
            hit_alarm = new Media(getClass().getClassLoader()
                    .getResource("sound/alarm.mp3").toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        mediaPlayer_bg = new MediaPlayer(hit_bg);
        mediaPlayer_bg.setCycleCount(-1);
        mediaPlayer_bg.setOnEndOfMedia(() -> mediaPlayer_bg.seek(new Duration(9000)));

        mediaPlayer_alarm = new MediaPlayer(hit_alarm);
    }

    public static AudioManager getInstance() {
        if (instance == null)
            instance = new AudioManager();
        return instance;
    }

    public void startBackgroundMusic() {
        mediaPlayer_bg.play();
    }

    public void playAlarm() {
        mediaPlayer_alarm.play();
    }

    public void stopAlarm() {
        mediaPlayer_alarm.stop();
    }

    public DoubleProperty bgMusicVolumeProperty() {
        return mediaPlayer_bg.volumeProperty();
    }

    public void stopBackgroundMusic() {
        if (mediaPlayer_bg != null)
            mediaPlayer_bg.stop();
    }

}
