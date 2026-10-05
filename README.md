# Poppy Playtime: Chapter 7 — Dead Signal

> **An original, non-commercial fan-made horror game inspired by the Poppy Playtime universe.**

<p align="center">
  <strong>DEAD SIGNAL</strong><br>
  <em>Sector 07 is listening.</em>
</p>

## Project status

| Area | Status |
|---|---|
| Engine | jMonkeyEngine 3.9.0-stable |
| Runtime | Java 25 |
| Build | Gradle |
| Current phase | Pre-production / playable prototype |
| Target release | itch.io |
| Platform target | Windows + Linux |

jMonkeyEngine 3.9.0-stable is the selected engine baseline for this project, with Java 25 as the project runtime.

## The game

A forgotten transmission has brought **Sector 07 — Signal Processing** back online.

You enter the abandoned facility looking for the source. Instead, you find evidence of **Project: Echo**, a Playtime Co. experiment built around preserving memories and personality data.

Something else is receiving the signal.

**Morrow** was designed as a child's nighttime companion. Now corrupted, it has developed one simple belief:

> Nobody should be alone.

Explore. Restore power. Follow the signal. Do not let it find you.

## Design goals

Dead Signal is being built around:

- First-person exploration
- Physical environmental puzzles
- GrabPack-style interaction mechanics with original implementations
- Industrial machinery and power systems
- Slow-burn psychological horror
- Stalking and chase encounters
- Environmental storytelling
- Strong audio and lighting direction
- Checkpoints and reliable save progression
- A polished, itch.io-ready player experience

The project takes inspiration from the pacing, puzzle language, presentation, and menu conventions of modern mascot-horror games while using original code, environments, characters, story beats, audio, and other assets.

## Act 1 — The Signal

Act 1 is the first complete gameplay target.

**Planned sequence:**

1. Arrival at Sector 07.
2. Establish the abandoned signal-processing facility.
3. Introduce movement, camera, and flashlight.
4. Discover the first interactive power system.
5. Introduce the GrabPack prototype.
6. Solve the first power-routing puzzle.
7. Discover evidence connected to Project: Echo.
8. Trigger the first unmistakable Morrow encounter.
9. Escape into the deeper facility.
10. End on a strong story cliffhanger.

Act 1 is not considered complete until these pieces work together as one continuous playable sequence.

See [docs/GAME_DESIGN.md](docs/GAME_DESIGN.md) for the design target.

## Play the game

### Public releases

The first public itch.io build will be linked here when it is ready.

**Recommended player workflow:**

1. Download the latest platform build.
2. Extract the ZIP.
3. Launch the included game executable.
4. Play using the controls shown in-game/readme.
5. Report bugs through GitHub or the itch.io community page.

**Do not download the source repository if you only want to play the game.**

### Development build

Requires **JDK 25**.

Linux/macOS:

```bash
./gradlew run
```

Windows:

```powershell
gradlew.bat run
```

If the Gradle wrapper is not present in your checkout yet, install a compatible Gradle release and run:

```bash
gradle run
```

For the full build guide, see [docs/BUILDING.md](docs/BUILDING.md).

## Controls

| Action | Default |
|---|---|
| Move | W A S D |
| Look | Mouse |
| Jump | Space |
| Flashlight | F |
| Pause | Esc |

Controls will be finalized during Act 1 development.

## Repository layout

```text
.
├── .github/
│   ├── ISSUE_TEMPLATE/
│   ├── pull_request_template.md
│   └── workflows/
├── docs/
│   ├── BUILDING.md
│   └── GAME_DESIGN.md
├── src/main/java/dev/azuremc/deadsignal/
│   ├── DeadSignalGame.java
│   ├── game/
│   └── menu/
├── build.gradle
├── settings.gradle
├── CONTRIBUTING.md
├── CHANGELOG.md
├── LICENSE
└── README.md
```

## Roadmap

### Milestone 0 — Foundation
- [x] Java 25 project
- [x] jMonkeyEngine integration
- [x] Professional menu shell
- [x] Prototype gameplay state
- [x] CI build workflow
- [x] Contribution and issue templates
- [x] Build/design documentation

### Milestone 1 — Act 1 / Vertical Slice
- [ ] First factory environment
- [ ] First-person controller
- [ ] Flashlight
- [ ] Doors and physical interactions
- [ ] GrabPack prototype
- [ ] Power-routing puzzle
- [ ] Project: Echo story sequence
- [ ] Morrow introduction
- [ ] First chase
- [ ] Checkpoint/save flow
- [ ] Act 1 playable from start to finish

### Milestone 2 — Production Systems
- [ ] Expanded Morrow AI
- [ ] Advanced stalking behaviour
- [ ] Chase framework
- [ ] Audio manager and positional ambience
- [ ] Environmental events
- [ ] Cutscene framework
- [ ] Settings/accessibility
- [ ] Performance profiling

### Milestone 3 — Public Demo
- [ ] Content polish
- [ ] QA pass
- [ ] Windows build
- [ ] Linux build
- [ ] Release notes
- [ ] itch.io store page
- [ ] First public demo

## Development workflow

The repository is intended to stay buildable throughout development.

Pull requests should:

- Keep changes focused.
- Use Java 25.
- Avoid committing generated build output.
- Include testing notes.
- Avoid unlicensed or ripped assets.
- Update documentation when player-facing behaviour changes.

See [CONTRIBUTING.md](CONTRIBUTING.md).

## Fan-project notice

**Dead Signal is unofficial.** It is not affiliated with or endorsed by Mob Entertainment or the official Poppy Playtime team.

The repository is for an original fan-made project. Official trademarks, characters, artwork, audio, models, textures, story material, and other copyrighted content remain the property of their respective owners.

Any third-party asset included in a future release must have a license that permits the intended distribution, or explicit permission must be obtained.

The final itch.io page will clearly identify the game as an unofficial fan project.

## Credits

**AzureMC Projects** — Development

Built with **Java** and **jMonkeyEngine**.

jMonkeyEngine is an independent open-source project with its own licensing terms.

---

**Development status:** early prototype. Expect incomplete systems, placeholder visuals, changing controls, and breaking changes while Act 1 is being built.
