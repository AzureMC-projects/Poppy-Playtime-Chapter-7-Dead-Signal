package dev.azuremc.deadsignal.game;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import dev.azuremc.deadsignal.DeadSignalGame;

import java.util.ArrayList;
import java.util.List;

public final class GameplayState extends BaseAppState {
    private static final String INTERACT = "Act1Interact";
    private static final String FLASHLIGHT = "Act1Flashlight";
    private static final String ESCAPE = "Act1Escape";

    private final DeadSignalGame game;
    private final List<Geometry> puzzleNodes = new ArrayList<>();

    private Node world;
    private SpotLight flashlight;
    private BitmapText objective;
    private BitmapText prompt;
    private BitmapText title;
    private Geometry securityDoor;
    private Geometry exitDoor;
    private Geometry morrow;
    private Geometry eyeLeft;
    private Geometry eyeRight;

    private int stage;
    private boolean flashlightOn = true;
    private boolean chaseActive;
    private boolean actComplete;
    private float messageTimer;
    private float chaseTimer;

    private final ActionListener inputListener = (name, pressed, tpf) -> {
        if (!pressed) return;
        switch (name) {
            case INTERACT -> interact();
            case FLASHLIGHT -> toggleFlashlight();
            case ESCAPE -> game.returnToMenu();
            default -> { }
        }
    };

    public GameplayState(DeadSignalGame game) {
        this.game = game;
    }

    @Override
    protected void initialize(Application app) {
        var input = app.getInputManager();
        input.setCursorVisible(false);
        input.addMapping(INTERACT, new KeyTrigger(KeyInput.KEY_E));
        input.addMapping(FLASHLIGHT, new KeyTrigger(KeyInput.KEY_F));
        input.addMapping(ESCAPE, new KeyTrigger(KeyInput.KEY_ESCAPE));
        input.addListener(inputListener, INTERACT, FLASHLIGHT, ESCAPE);

        world = new Node("Act1World");
        app.getRootNode().attachChild(world);

        buildEnvironment(app);
        setupPlayer((SimpleApplication) app);
        setupHud(app);

        showMessage("SECTOR 07  //  SIGNAL RECEIVED", 4.0f);
        objective.setText("OBJECTIVE  //  Follow the transmission");
    }

    private void buildEnvironment(Application app) {
        var ambient = new AmbientLight();
        ambient.setColor(new ColorRGBA(0.12f, 0.13f, 0.15f, 1f));
        world.addLight(ambient);

        addBox(app, "Floor", new Vector3f(0, -0.15f, 0), 27, 0.15f, 8, new ColorRGBA(0.10f, 0.11f, 0.12f, 1f));
        addBox(app, "NorthWall", new Vector3f(0, 2.5f, 7.8f), 27, 5, 0.2f, new ColorRGBA(0.15f, 0.16f, 0.17f, 1f));
        addBox(app, "SouthWall", new Vector3f(0, 2.5f, -7.8f), 27, 5, 0.2f, new ColorRGBA(0.15f, 0.16f, 0.17f, 1f));
        addBox(app, "WestWall", new Vector3f(-27, 2.5f, 0), 0.2f, 5, 8, new ColorRGBA(0.15f, 0.16f, 0.17f, 1f));
        addBox(app, "EastWall", new Vector3f(27, 2.5f, 0), 0.2f, 5, 8, new ColorRGBA(0.15f, 0.16f, 0.17f, 1f));

        for (int x = -24; x <= 24; x += 8) {
            addBox(app, "CeilingBeam", new Vector3f(x, 7.1f, 0), 0.35f, 0.35f, 8, new ColorRGBA(0.07f, 0.08f, 0.09f, 1f));
        }

        addSign(app, "SECTOR 07  //  SIGNAL PROCESSING", -21, 4.5f, -7.55f);
        addLight(new Vector3f(-18, 5.5f, 0), new ColorRGBA(0.35f, 0.55f, 0.65f, 1f), 8);
        addLight(new Vector3f(-8, 5.5f, 0), new ColorRGBA(0.42f, 0.20f, 0.16f, 1f), 7);

        addMachine(app, "GrabPackStation", new Vector3f(-13, 1.4f, -5.8f), 1.4f, 2.4f, 0.7f);
        addPanel(app, "GrabPackConsole", new Vector3f(-13, 2.1f, -5.35f), new ColorRGBA(0.18f, 0.48f, 0.52f, 1f));

        for (int i = 0; i < 3; i++) {
            float x = -7 + i * 4;
            puzzleNodes.add(addPanel(app, "PowerNode" + (i + 1), new Vector3f(x, 1.7f, -5.8f),
                    new ColorRGBA(0.55f, 0.08f, 0.06f, 1f)));
            addLight(new Vector3f(x, 3.0f, -5.4f), new ColorRGBA(0.65f, 0.10f, 0.06f, 1f), 3.5f);
        }

        securityDoor = addDoor(app, "SecurityDoor", new Vector3f(2, 2.5f, 0), 3.8f, 5.5f);
        addSign(app, "SIGNAL ROUTING  //  AUTHORIZED PERSONNEL", 0, 5.8f, -7.55f);

        addMachine(app, "EchoTerminal", new Vector3f(9, 1.6f, -5.8f), 1.6f, 2.5f, 0.8f);
        addPanel(app, "EchoScreen", new Vector3f(9, 2.4f, -5.2f), new ColorRGBA(0.05f, 0.32f, 0.30f, 1f));

        addGlassWall(app, 16, 0, 0);
        morrow = addBox(app, "Morrow", new Vector3f(18, 2.2f, 0), 0.9f, 2f, 0.7f, new ColorRGBA(0.035f, 0.025f, 0.03f, 1f));
        eyeLeft = addBox(app, "MorrowEyeL", new Vector3f(18.35f, 2.45f, -0.55f), 0.09f, 0.08f, 0.04f, new ColorRGBA(0.85f, 0.10f, 0.06f, 1f));
        eyeRight = addBox(app, "MorrowEyeR", new Vector3f(18.35f, 2.45f, -0.15f), 0.09f, 0.08f, 0.04f, new ColorRGBA(0.85f, 0.10f, 0.06f, 1f));
        morrow.setCullHint(com.jme3.scene.Spatial.CullHint.Always);
        eyeLeft.setCullHint(com.jme3.scene.Spatial.CullHint.Always);
        eyeRight.setCullHint(com.jme3.scene.Spatial.CullHint.Always);

        exitDoor = addDoor(app, "ExitDoor", new Vector3f(25, 2.5f, 0), 3.8f, 5.5f);
        addSign(app, "MAINTENANCE ACCESS", 23, 5.8f, -7.55f);
    }

    private void setupPlayer(SimpleApplication app) {
        var cam = app.getCamera();
        cam.setLocation(new Vector3f(-22, 2.2f, 0));
        cam.lookAtDirection(Vector3f.UNIT_X, Vector3f.UNIT_Y);

        flashlight = new SpotLight();
        flashlight.setColor(new ColorRGBA(1f, 0.94f, 0.82f, 1f));
        flashlight.setSpotRange(22);
        flashlight.setSpotInnerAngle(0.12f);
        flashlight.setSpotOuterAngle(0.32f);
        flashlight.setPosition(cam.getLocation());
        flashlight.setDirection(cam.getDirection());
        world.addLight(flashlight);

        app.getFlyByCamera().setMoveSpeed(5f);
        app.getFlyByCamera().setEnabled(true);
    }

    private void setupHud(Application app) {
        var gui = app.getGuiViewPort().getGuiRoot();
        var font = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");

        title = hudText(font, "ACT 1  //  THE SIGNAL", 18, new ColorRGBA(0.75f, 0.78f, 0.78f, 0.9f), 30, 690);
        objective = hudText(font, "", 18, ColorRGBA.White, 30, 660);
        prompt = hudText(font, "", 16, new ColorRGBA(1f, 0.55f, 0.42f, 1f), 30, 55);
        var crosshair = hudText(font, "+", 22, ColorRGBA.White, 637, 361);

        gui.attachChild(title);
        gui.attachChild(objective);
        gui.attachChild(prompt);
        gui.attachChild(crosshair);
    }

    private BitmapText hudText(BitmapFont font, String value, float size, ColorRGBA color, float x, float y) {
        var text = new BitmapText(font, false);
        text.setText(value);
        text.setSize(size);
        text.setColor(color);
        text.setLocalTranslation(x, y, 0);
        return text;
    }

    @Override
    public void update(float tpf) {
        var cam = getApplication().getCamera();
        flashlight.setPosition(cam.getLocation());
        flashlight.setDirection(cam.getDirection());

        var p = cam.getLocation();
        p.x = FastMath.clamp(p.x, -25.5f, 25.5f);
        p.z = FastMath.clamp(p.z, -6.7f, 6.7f);
        p.y = 2.2f;
        cam.setLocation(p);

        updatePrompt(cam);
        if (messageTimer > 0) {
            messageTimer -= tpf;
            if (messageTimer <= 0 && !actComplete) prompt.setText("");
        }
        if (chaseActive) updateChase(tpf, cam);
    }

    private void updatePrompt(Camera cam) {
        if (chaseActive || actComplete) return;
        if (stage == 0 && near(cam, -13, -5.8f, 2.8f)) prompt.setText("[E]  Connect the GrabPack");
        else if (stage == 1 && near(cam, -7, -5.8f, 2.8f)) prompt.setText("[E]  Route power  //  NODE 1");
        else if (stage == 2 && near(cam, -3, -5.8f, 2.8f)) prompt.setText("[E]  Route power  //  NODE 2");
        else if (stage == 3 && near(cam, 1, -5.8f, 2.8f)) prompt.setText("[E]  Route power  //  NODE 3");
        else if (stage == 4 && near(cam, 9, -5.8f, 3f)) prompt.setText("[E]  Inspect Project: Echo terminal");
        else if (stage == 5 && near(cam, 24.2f, 0, 3f)) prompt.setText("[E]  Open maintenance access");
        else prompt.setText("");
    }

    private void interact() {
        if (actComplete || chaseActive) return;
        var cam = getApplication().getCamera();

        if (stage == 0 && near(cam, -13, -5.8f, 2.8f)) {
            stage = 1;
            showMessage("GRABPACK LINK ESTABLISHED", 2.5f);
            objective.setText("OBJECTIVE  //  Restore power to the security door");
        } else if (stage == 1 && near(cam, -7, -5.8f, 2.8f)) {
            activateNode(0);
            stage = 2;
            objective.setText("OBJECTIVE  //  Route power  [2/3]");
        } else if (stage == 2 && near(cam, -3, -5.8f, 2.8f)) {
            activateNode(1);
            stage = 3;
            objective.setText("OBJECTIVE  //  Route power  [3/3]");
        } else if (stage == 3 && near(cam, 1, -5.8f, 2.8f)) {
            activateNode(2);
            stage = 4;
            securityDoor.setLocalTranslation(2, 8.5f, 0);
            objective.setText("OBJECTIVE  //  Investigate the terminal beyond the security door");
            showMessage("SECURITY DOOR UNLOCKED", 2.5f);
        } else if (stage == 4 && near(cam, 9, -5.8f, 3f)) {
            stage = 5;
            showMorrow();
            objective.setText("OBJECTIVE  //  Reach maintenance access");
            showMessage("PROJECT: ECHO  //  BACKUP INSTANCE DETECTED", 4f);
        } else if (stage == 5 && near(cam, 24.2f, 0, 3f)) {
            startChase();
        }
    }

    private void activateNode(int index) {
        var material = new Material(getApplication().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", new ColorRGBA(0.10f, 0.55f, 0.20f, 1f));
        puzzleNodes.get(index).setMaterial(material);
    }

    private void showMorrow() {
        morrow.setCullHint(com.jme3.scene.Spatial.CullHint.Inherit);
        eyeLeft.setCullHint(com.jme3.scene.Spatial.CullHint.Inherit);
        eyeRight.setCullHint(com.jme3.scene.Spatial.CullHint.Inherit);
        showMessage("MORROW:  \"I HEARD YOU.\"", 3f);
    }

    private void startChase() {
        chaseActive = true;
        chaseTimer = 0;
        morrow.setLocalTranslation(18, 2.2f, 0);
        objective.setText("RUN  //  Reach maintenance access");
        showMessage("RUN.", 1.5f);
    }

    private void updateChase(float tpf, Camera cam) {
        chaseTimer += tpf;
        var current = morrow.getLocalTranslation().clone();
        var delta = cam.getLocation().subtract(current);
        delta.y = 0;

        if (delta.lengthSquared() > 0.1f) {
            delta.normalizeLocal().multLocal(tpf * (2.4f + Math.min(chaseTimer * 0.08f, 1.8f)));
            current.addLocal(delta);
            current.y = 2.2f;
            morrow.setLocalTranslation(current);
            eyeLeft.setLocalTranslation(current.clone().addLocal(0.35f, 0.25f, -0.55f));
            eyeRight.setLocalTranslation(current.clone().addLocal(0.35f, 0.25f, -0.15f));
        }

        if (current.distance(cam.getLocation()) < 1.55f) {
            chaseActive = false;
            morrow.setLocalTranslation(18, 2.2f, 0);
            showMessage("MORROW FOUND YOU  //  KEEP MOVING", 2.5f);
            objective.setText("OBJECTIVE  //  Reach maintenance access");
        }

        if (cam.getLocation().x > 23.5f) completeAct();
    }

    private void completeAct() {
        chaseActive = false;
        actComplete = true;
        objective.setText("ACT 1 COMPLETE  //  THE SIGNAL GOES DEEPER");
        showMessage("END OF ACT 1  //  DEAD SIGNAL", 999f);
        morrow.setCullHint(com.jme3.scene.Spatial.CullHint.Always);
        eyeLeft.setCullHint(com.jme3.scene.Spatial.CullHint.Always);
        eyeRight.setCullHint(com.jme3.scene.Spatial.CullHint.Always);
        exitDoor.setLocalTranslation(25, 8.5f, 0);
    }

    private void toggleFlashlight() {
        flashlightOn = !flashlightOn;
        flashlight.setEnabled(flashlightOn);
        showMessage(flashlightOn ? "FLASHLIGHT ON" : "FLASHLIGHT OFF", 1f);
    }

    private boolean near(Camera cam, float x, float z, float radius) {
        var p = cam.getLocation();
        float dx = p.x - x;
        float dz = p.z - z;
        return dx * dx + dz * dz <= radius * radius;
    }

    private void showMessage(String message, float duration) {
        prompt.setText(message);
        messageTimer = duration;
    }

    private Geometry addMachine(Application app, String name, Vector3f location, float width, float height, float depth) {
        return addBox(app, name, location, width / 2, height / 2, depth / 2, new ColorRGBA(0.20f, 0.22f, 0.23f, 1f));
    }

    private Geometry addPanel(Application app, String name, Vector3f location, ColorRGBA color) {
        return addBox(app, name, location, 0.65f, 0.55f, 0.18f, color);
    }

    private Geometry addDoor(Application app, String name, Vector3f location, float width, float height) {
        return addBox(app, name, location, width / 2, height / 2, 0.35f, new ColorRGBA(0.22f, 0.08f, 0.08f, 1f));
    }

    private void addGlassWall(Application app, float x, float y, float z) {
        var material = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", new ColorRGBA(0.12f, 0.24f, 0.27f, 0.35f));
        material.setTransparent(true);
        var glass = new Geometry("ObservationGlass", new Box(0.12f, 4f, 7f));
        glass.setMaterial(material);
        glass.setLocalTranslation(x, y, z);
        world.attachChild(glass);
    }

    private void addSign(Application app, String value, float x, float y, float z) {
        var font = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        var text = new BitmapText(font, false);
        text.setText(value);
        text.setSize(16);
        text.setColor(new ColorRGBA(0.72f, 0.78f, 0.78f, 1f));
        text.setLocalTranslation(x, y, z);
        text.rotate(0, FastMath.PI, 0);
        world.attachChild(text);
    }

    private void addLight(Vector3f position, ColorRGBA color, float radius) {
        var light = new PointLight();
        light.setPosition(position);
        light.setColor(color);
        light.setRadius(radius);
        world.addLight(light);
    }

    private Geometry addBox(Application app, String name, Vector3f location, float x, float y, float z, ColorRGBA color) {
        var geometry = new Geometry(name, new Box(x, y, z));
        var material = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", color);
        geometry.setMaterial(material);
        geometry.setLocalTranslation(location);
        world.attachChild(geometry);
        return geometry;
    }

    @Override
    protected void cleanup(Application app) {
        app.getInputManager().deleteMapping(INTERACT);
        app.getInputManager().deleteMapping(FLASHLIGHT);
        app.getInputManager().deleteMapping(ESCAPE);
        app.getInputManager().removeListener(inputListener);
        app.getFlyByCamera().setEnabled(false);

        if (world != null) world.removeFromParent();

        var gui = app.getGuiViewPort().getGuiRoot();
        if (objective != null) objective.removeFromParent();
        if (prompt != null) prompt.removeFromParent();
        if (title != null) title.removeFromParent();
        gui.removeChild(objective);
        gui.removeChild(prompt);
        gui.removeChild(title);
    }
}
