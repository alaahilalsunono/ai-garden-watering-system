package app;

public class Main {
    public static void main(String[] args) {
        AppTheme.applyGlobalTheme();
        MusicPlayer.playLoop("/app/bgmusic.wav");
        new StartScreen();
    }
}