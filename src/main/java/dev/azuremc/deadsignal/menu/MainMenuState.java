package dev.azuremc.deadsignal.menu;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import dev.azuremc.deadsignal.DeadSignalGame;

import java.util.ArrayList;
import java.util.List;

public final class MainMenuState extends BaseAppState {

    private static final String SELECT = "MenuSelect";

    private final DeadSignalGame game;
    private final List<MenuButton> buttons = new ArrayList<>();

    private Node guiNode;
    private BitmapText status;
    private int hovered = -1;

    private final ActionListener mouseListener = (name, pressed, tpf) -> {
        if (!SELECT.equals(name) || !pressed || hovered < 0 || hovered >= buttons.size()) {
            return;
        }
        buttons.get(hovered).action.run();
    };

    public MainMenuState(DeadSignalGame game) {
        this.game = game;
    }

    @Override
    protected void initialize(Application app) {
        guiNode = new Node("MainMenu");
        app.getGuiViewPort().attachScene(guiNode);

        var input = app.getInputManager();
        input.setCursorVisible(true);
        input.addMapping(SELECT, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        input.addListener(mouseListener, SELECT);

        var font = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");

        addBackground(app);
        addBranding(font);
        addButton(font, "START GAME", 370, game::startGame);
        addButton(font, "OPTIONS", 310, () -> setStatus("Options will be available in a future build."));
        addButton(font, "QUIT", 250, game::stop);

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

    private void addBackground(Application app) {
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
    }

    private void addBranding(BitmapFont font) {
        var title = text(font, "POPPY PLAYTIME", 42, ColorRGBA.White, 72, 610);
        guiNode.attachChild(title);

        var chapter = text(font, "CHAPTER 7", 28, new ColorRGBA(0.82f, 0.82f, 0.82f, 1f), 75, 565);
        guiNode.attachChild(chapter);

        var subtitle = text(font, "DEAD SIGNAL", 56, new ColorRGBA(0.86f, 0.16f, 0.10f, 1f), 72, 510);
        guiNode.attachChild(subtitle);
    }

    private BitmapText text(BitmapFont font, String value, float size, ColorRGBA color, float x, float y) {
        var result = new BitmapText(font, false);
        result.setText(value);
        result.setSize(size);
        result.setColor(color);
        result.setLocalTranslation(x, y, 0);
        return result;
    }

    private void addButton(BitmapFont font, String value, float y, Runnable action) {
        var button = text(font, value, 24, ColorRGBA.White, 76, y);
        guiNode.attachChild(button);
        buttons.add(new MenuButton(button, y, action));
    }

    private void setStatus(String message) {
        if (status != null) {
            status.setText(message);
        }
    }

    @Override
    public void update(float tpf) {
        var cursor = getApplication().getInputManager().getCursorPosition();
        var nextHovered = -1;

        for (int i = 0; i < buttons.size(); i++) {
            var button = buttons.get(i);
            float left = 68;
            float right = 360;
            float bottom = button.y - 28;
            float top = button.y + 6;

            if (cursor.x >= left && cursor.x <= right && cursor.y >= bottom && cursor.y <= top) {
                nextHovered = i;
                break;
            }
        }

        if (nextHovered != hovered) {
            hovered = nextHovered;
            for (int i = 0; i < buttons.size(); i++) {
                buttons.get(i).label.setColor(i == hovered
                        ? new ColorRGBA(1f, 0.28f, 0.18f, 1f)
                        : ColorRGBA.White);
            }
        }
    }

    @Override
    protected void cleanup(Application app) {
        app.getInputManager().deleteMapping(SELECT);
        app.getInputManager().removeListener(mouseListener);
        app.getInputManager().setCursorVisible(false);
        guiNode.removeFromParent();
        buttons.clear();
    }

    private record MenuButton(BitmapText label, float y, Runnable action) {
    }
}
