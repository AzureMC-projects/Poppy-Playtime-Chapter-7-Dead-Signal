package dev.azuremc.deadsignal.game;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.light.PointLight;
import dev.azuremc.deadsignal.DeadSignalGame;

public final class GameplayState extends BaseAppState {

    private final DeadSignalGame game;
    private Node root;

    public GameplayState(DeadSignalGame game) {
        this.game = game;
    }

    @Override
    protected void initialize(Application app) {
        var simple = (SimpleApplication) app;
        root = new Node("Sector07Prototype");
        simple.getRootNode().attachChild(root);

        var ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.16f, 0.17f, 0.20f, 1f));
        root.addLight(ambient);

        var light = new PointLight();
        light.setColor(new ColorRGBA(1f, 0.28f, 0.18f, 1f));
        light.setRadius(18f);
        light.setPosition(new Vector3f(0, 4, -4));
        root.addLight(light);

        var floor = box(app, "FactoryFloor", new Vector3f(0, -0.25f, 0), new Vector3f(12, 0.25f, 12),
                new ColorRGBA(0.10f, 0.10f, 0.11f, 1f));
        root.attachChild(floor);

        root.attachChild(box(app, "BackWall", new Vector3f(0, 4, -12), new Vector3f(12, 4, 0.25f),
                new ColorRGBA(0.08f, 0.07f, 0.08f, 1f)));
        root.attachChild(box(app, "LeftWall", new Vector3f(-12, 4, 0), new Vector3f(0.25f, 4, 12),
                new ColorRGBA(0.08f, 0.07f, 0.08f, 1f)));
        root.attachChild(box(app, "RightWall", new Vector3f(12, 4, 0), new Vector3f(0.25f, 4, 12),
                new ColorRGBA(0.08f, 0.07f, 0.08f, 1f)));

        simple.getCamera().setLocation(new Vector3f(0, 2.1f, 8));
        simple.getCamera().lookAt(new Vector3f(0, 2, 0), Vector3f.UNIT_Y);
        simple.getFlyByCamera().setMoveSpeed(6f);
        simple.getFlyByCamera().setEnabled(true);

        simple.getInputManager().addMapping("DeadSignalPause", new KeyTrigger(KeyInput.KEY_ESCAPE));
        simple.getInputManager().addListener((com.jme3.input.controls.ActionListener) (name, pressed, tpf) -> {
            if (name.equals("DeadSignalPause") && pressed) {
                game.returnToMenu();
            }
        }, "DeadSignalPause");
    }

    private Geometry box(Application app, String name, Vector3f position, Vector3f extent, ColorRGBA color) {
        var geometry = new Geometry(name, new Box(extent.x, extent.y, extent.z));
        var material = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        material.setBoolean("UseMaterialColors", true);
        material.setColor("Diffuse", color);
        material.setColor("Ambient", color.mult(0.45f));
        geometry.setMaterial(material);
        geometry.setLocalTranslation(position);
        return geometry;
    }

    @Override
    protected void cleanup(Application app) {
        root.removeFromParent();
        var input = app.getInputManager();
        input.deleteMapping("DeadSignalPause");
    }
}
