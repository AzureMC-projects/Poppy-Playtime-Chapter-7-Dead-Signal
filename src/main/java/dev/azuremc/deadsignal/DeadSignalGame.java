package dev.azuremc.deadsignal;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import dev.azuremc.deadsignal.game.GameplayState;
import dev.azuremc.deadsignal.menu.MainMenuState;

/**
 * Application entry point for Poppy Playtime: Chapter 7 — Dead Signal.
 */
public final class DeadSignalGame extends SimpleApplication {

    public static final String GAME_TITLE = "Poppy Playtime: Chapter 7 — Dead Signal";

    public static void main(String[] args) {
        var settings = new AppSettings(true);
        settings.setTitle(GAME_TITLE);
        settings.setResolution(1280, 720);
        settings.setVSync(true);
        settings.setSamples(4);
        settings.setGammaCorrection(true);

        var game = new DeadSignalGame();
        game.setSettings(settings);
        game.setShowSettings(false);
        game.start();
    }

    private DeadSignalGame() {
        super();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
        stateManager.attach(new MainMenuState(this));
    }

    public void startGame() {
        stateManager.detach(stateManager.getState(MainMenuState.class));
        stateManager.attach(new GameplayState(this));
    }

    public void returnToMenu() {
        var gameplay = stateManager.getState(GameplayState.class);
        if (gameplay != null) {
            stateManager.detach(gameplay);
        }
        stateManager.attach(new MainMenuState(this));
    }
}
