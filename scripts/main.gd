extends Node3D

const RED := Color("#c11212")
const DARK := Color("#080808")
const WHITE := Color("#e5e5e5")
const MUTED := Color("#777777")

var menu_layer: CanvasLayer
var game_root: Node3D
var player: CharacterBody3D
var flashlight: SpotLight3D
var objective: Label
var prompt: Label
var progress := 0
var chasing := false
var morrow: Node3D
var speed := 4.5

func _ready() -> void:
    _show_menu()

func _show_menu() -> void:
    _clear()
    menu_layer = CanvasLayer.new()
    add_child(menu_layer)

    var bg := ColorRect.new()
    bg.color = DARK
    bg.set_anchors_and_offsets_preset(Control.PRESET_FULL_RECT)
    menu_layer.add_child(bg)

    var title := Label.new()
    title.text = "POPPY PLAYTIME"
    title.position = Vector2(74, 88)
    title.add_theme_font_size_override("font_size", 42)
    title.modulate = WHITE
    menu_layer.add_child(title)

    var chapter := Label.new()
    chapter.text = "CHAPTER 7"
    chapter.position = Vector2(78, 142)
    chapter.add_theme_font_size_override("font_size", 24)
    chapter.modulate = RED
    menu_layer.add_child(chapter)

    var game_title := Label.new()
    game_title.text = "DEAD SIGNAL"
    game_title.position = Vector2(74, 176)
    game_title.add_theme_font_size_override("font_size", 68)
    menu_layer.add_child(game_title)

    var subtitle := Label.new()
    subtitle.text = "SECTOR 07 // SIGNAL PROCESSING"
    subtitle.position = Vector2(80, 258)
    subtitle.modulate = MUTED
    menu_layer.add_child(subtitle)

    var start := Button.new()
    start.text = "START GAME"
    start.position = Vector2(80, 340)
    start.size = Vector2(300, 54)
    start.add_theme_font_size_override("font_size", 22)
    start.pressed.connect(_start_game)
    menu_layer.add_child(start)

    var quit := Button.new()
    quit.text = "QUIT"
    quit.position = Vector2(80, 410)
    quit.size = Vector2(300, 48)
    quit.pressed.connect(get_tree().quit)
    menu_layer.add_child(quit)

    var version := Label.new()
    version.text = "GODOT 4.7.2 • PRE-ALPHA • 0.2.0"
    version.position = Vector2(80, 650)
    version.modulate = MUTED
    menu_layer.add_child(version)

func _start_game() -> void:
    _clear()
    _build_level()

func _build_level() -> void:
    game_root = Node3D.new()
    game_root.name = "Sector07"
    add_child(game_root)

    var env := WorldEnvironment.new()
    var environment := Environment.new()
    environment.background_mode = Environment.BG_COLOR
    environment.background_color = Color("#030303")
    environment.ambient_light_source = Environment.AMBIENT_SOURCE_COLOR
    environment.ambient_light_color = Color("#273040")
    environment.ambient_light_energy = 0.35
    env.environment = environment
    game_root.add_child(env)

    var light := DirectionalLight3D.new()
    light.rotation_degrees = Vector3(-65, -20, 0)
    light.light_energy = 0.25
    game_root.add_child(light)

    _box("Floor", Vector3(0,-0.5,0), Vector3(34,1,12), Color("#202020"))
    _box("LeftWall", Vector3(0,3,-6), Vector3(34,7,1), Color("#151515"))
    _box("RightWall", Vector3(0,3,6), Vector3(34,7,1), Color("#151515"))
    _box("BackWall", Vector3(-17,3,0), Vector3(1,7,12), Color("#181818"))
    _box("Ceiling", Vector3(0,7,0), Vector3(34,1,12), Color("#101010"))

    for z in [-4.0, 0.0, 4.0]:
        var lamp := OmniLight3D.new()
        lamp.position = Vector3(-8,5.5,z)
        lamp.light_color = Color("#d8e8ff")
        lamp.light_energy = 2.0
        lamp.omni_range = 9.0
        game_root.add_child(lamp)

    _label_3d("SECTOR 07 // SIGNAL PROCESSING", Vector3(-15,5, -5.35), 1.0)
    _label_3d("AUTHORIZED PERSONNEL ONLY", Vector3(5,5, -5.35), 0.8)

    _box("GrabPackStation", Vector3(-7,1.5,0), Vector3(2,3,2), RED)
    for i in range(3):
        _box("PowerNode%d" % i, Vector3(-2 + i*3.0, 0.5, 0), Vector3(1.2,1.0,1.2), Color("#303030"))

    var terminal := _box("EchoTerminal", Vector3(9,1.4,0), Vector3(2,2.8,1.5), Color("#26313b"))
    terminal.set_meta("echo_terminal", true)

    _box("SecurityDoor", Vector3(12.5,3,0), Vector3(0.8,6,5), Color("#303030"))
    _box("ExitDoor", Vector3(16,3,0), Vector3(0.8,6,5), Color("#101010"))

    _spawn_player()
    _build_hud()
    _spawn_morrow()
    objective.text = "OBJECTIVE // Follow the transmission."

func _spawn_player() -> void:
    player = CharacterBody3D.new()
    player.name = "Player"
    player.position = Vector3(-13,0.7,0)
    game_root.add_child(player)

    var collision := CollisionShape3D.new()
    var shape := CapsuleShape3D.new()
    shape.height = 1.8
    shape.radius = 0.35
    collision.shape = shape
    collision.position.y = 0.9
    player.add_child(collision)

    var camera := Camera3D.new()
    camera.position = Vector3(0,1.55,0)
    camera.current = true
    player.add_child(camera)

    flashlight = SpotLight3D.new()
    flashlight.position = Vector3(0,1.5,-0.2)
    flashlight.rotation_degrees = Vector3(-2,0,0)
    flashlight.light_energy = 5.0
    flashlight.spot_range = 18.0
    flashlight.spot_angle = 32.0
    player.add_child(flashlight)

func _build_hud() -> void:
    menu_layer = CanvasLayer.new()
    add_child(menu_layer)

    objective = Label.new()
    objective.position = Vector2(28,28)
    objective.add_theme_font_size_override("font_size",20)
    menu_layer.add_child(objective)

    prompt = Label.new()
    prompt.position = Vector2(28,650)
    prompt.add_theme_font_size_override("font_size",18)
    prompt.modulate = RED
    menu_layer.add_child(prompt)

    var cross := Label.new()
    cross.text = "+"
    cross.position = Vector2(638,348)
    cross.add_theme_font_size_override("font_size",20)
    menu_layer.add_child(cross)

func _spawn_morrow() -> void:
    morrow = Node3D.new()
    morrow.name = "Morrow"
    morrow.position = Vector3(13,0,0)
    game_root.add_child(morrow)
    var body := _box("MorrowBody", Vector3.ZERO, Vector3(1.4,3.8,1.2), Color("#090909"))
    body.reparent(morrow)
    body.position.y = 1.9
    for x in [-0.28,0.28]:
        var eye := OmniLight3D.new()
        eye.position = Vector3(x,2.7,-0.65)
        eye.light_color = RED
        eye.light_energy = 3.0
        eye.omni_range = 2.5
        morrow.add_child(eye)
    morrow.visible = false

func _process(delta: float) -> void:
    if player == null:
        return
    if Input.is_action_just_pressed("flashlight"):
        flashlight.visible = not flashlight.visible
    if Input.is_action_just_pressed("pause_menu"):
        _show_menu()
        return

    var input_vec := Input.get_vector("ui_left", "ui_right", "ui_up", "ui_down")
    var movement := Vector3(input_vec.x, 0, input_vec.y)
    if Input.is_key_pressed(KEY_W): movement.z -= 1
    if Input.is_key_pressed(KEY_S): movement.z += 1
    if Input.is_key_pressed(KEY_A): movement.x -= 1
    if Input.is_key_pressed(KEY_D): movement.x += 1
    player.velocity = movement.normalized() * speed
    player.move_and_slide()
    player.position.x = clamp(player.position.x, -15.5, 15.5)
    player.position.z = clamp(player.position.z, -4.8, 4.8)

    if progress < 3 and player.position.x > -1.0:
        progress = 3
        objective.text = "OBJECTIVE // Restore power to Sector 07."
    if progress < 4 and player.position.x > 7.0:
        progress = 4
        objective.text = "OBJECTIVE // Access Project: Echo."
    if progress < 5 and player.position.x > 10.5:
        progress = 5
        objective.text = "OBJECTIVE // Something is watching."
        morrow.visible = true
        prompt.text = "I HEARD YOU."
    if progress < 6 and player.position.x > 13.5:
        progress = 6
        chasing = true
        objective.text = "RUN // GET TO THE MAINTENANCE DOOR."
        prompt.text = "MORROW IS COMING."
    if chasing:
        morrow.position.x = move_toward(morrow.position.x, player.position.x, delta * 3.8)
        if morrow.position.distance_to(player.position) < 1.3:
            player.position = Vector3(-13,0.7,0)
            morrow.position.x = 13
            prompt.text = "SIGNAL LOST // TRY AGAIN."
    elif progress == 6 and player.position.x > 15:
        prompt.text = "ACT 1 COMPLETE // THE SIGNAL GOES DEEPER"

func _box(name: String, pos: Vector3, size: Vector3, color: Color) -> MeshInstance3D:
    var mesh := MeshInstance3D.new()
    mesh.name = name
    var box := BoxMesh.new()
    box.size = size
    mesh.mesh = box
    var mat := StandardMaterial3D.new()
    mat.albedo_color = color
    mesh.material_override = mat
    mesh.position = pos
    game_root.add_child(mesh)
    var body := StaticBody3D.new()
    body.position = pos
    var shape := CollisionShape3D.new()
    var box_shape := BoxShape3D.new()
    box_shape.size = size
    shape.shape = box_shape
    body.add_child(shape)
    game_root.add_child(body)
    return mesh

func _label_3d(text_value: String, pos: Vector3, scale_value: float) -> void:
    var label := Label3D.new()
    label.text = text_value
    label.position = pos
    label.font_size = 32
    label.modulate = RED
    label.pixel_size = 0.004 * scale_value
    game_root.add_child(label)

func _clear() -> void:
    if menu_layer:
        menu_layer.queue_free()
        menu_layer = null
    if game_root:
        game_root.queue_free()
        game_root = null
    player = null
    flashlight = null
    morrow = null
    progress = 0
    chasing = false
    prompt = null
    objective = null
