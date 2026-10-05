package dev.azuremc.deadsignal.menu;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.ui.Picture;
import dev.azuremc.deadsignal.DeadSignalGame;

public final class MainMenuState extends BaseAppState {

    private final DeadSignalGame game;
    private Node guiNode;
    private BitmapText status;

    public MainMenuState(DeadSignalGame game) {
        this.game = game;
    }

    @Override
    protected void initialize(Application app) {
        guiNode = new Node("MainMenu");
        app.getGuiViewPort().attachScene(guiNode);

        var font = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");

        var background = new Geometry("MenuBackground", new Quad(1280, 720));
        var backgroundMaterial = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        backgroundMaterial.setColor("Color", new ColorRGBA(0.012f, 0.014f, 0.018f, 1f));
        background.setMaterial(backgroundMaterial);
        background.setLocalTranslation(0, 0, -10);
        guiNode.attachChild(background);

        var accent = new Geometry("SignalBar", new Quad(5, 720));
        var accentMaterial = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        accentMaterial.setColor("Color", new ColorRGBA(0.78f, 0.13f, 0.08f, 1f));
        accent.setMaterial(accentMaterial);
        accent.setLocalTranslation(0, 0, -5);
        guiNode.attachChild(accent);

        var title = new BitmapText(font, false);
        title.setText("POPPY PLAYTIME");
        title.setSize(42);
        title.setColor(ColorRGBA.White);
        title.setLocalTranslation(72, 610, 0);
        guiNode.attachChild(title);

        var chapter = new BitmapText(font, false);
        chapter.setText("CHAPTER 7");
        chapter.setSize(28);
        chapter.setColor(new ColorRGBA(0.82f, 0.82f, 0.82f, 1f));
        chapter.setLocalTranslation(75, 565, 0);
        guiNode.attachChild(chapter);

        var subtitle = new BitmapText(font, false);
        subtitle.setText("DEAD SIGNAL");
        subtitle.setSize(56);
        subtitle.setColor(new ColorRGBA(0.86f, 0.16f, 0.10f, 1f));
        subtitle.setLocalTranslation(72, 510, 0);
        guiNode.attachChild(subtitle);

        addButton(font, "START GAME", 76, 370, () -> game.startGame());
        addButton(font, "OPTIONS", 76, 310, () -> setStatus("Options will be available in a future build."));
        addButton(font, "QUIT", 76, 250, () -> game.stop());

        status = new BitmapText(font, false);
        status.setSize(16);
        status.setColor(new ColorRGBA(0.65f, 0.65f, 0.65f, 1f));
        status.setLocalTranslation(76, 130, 0);
        guiNode.attachChild(status);

        var version = new BitmapText(font, false);
        version.setText("PRE-ALPHA 0.1.0  •  JAVA 25  •  jME 3.9");
        version.setSize(14);
        version.setColor(new ColorRGBA(0.42f, 0.42f, 0.42f, 1f));
        version.setLocalTranslation(76, 55, 0);
        guiNode.attachChild(version);
    }

    private void addButton(BitmapFont font, String text, float x, float y, Runnable action) {
        var button = new BitmapText(font, false);
        button.setText(text);
        button.setSize(24);
        button.setColor(ColorRGBA.White);
        button.setLocalTranslation(x, y, 0);
        button.setUserData("action", action);
        guiNode.attachChild(button);
    }

    private void setStatus(String message) {
        if (status != null) {
            status.setText(message);
        }
    }

    @Override
    public void update(float tpf) {
        var input = getApplication().getInputManager();
        if (input.isCursorVisible()) {
            return;
        }
    }

    @Override
    protected void cleanup(Application app) {
        guiNode.removeFromParent();
    }
}
