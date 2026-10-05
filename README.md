# Poppy Playtime: Chapter 7 — Dead Signal

> **A fan-made, non-commercial horror game inspired by the Poppy Playtime universe.**

**Status:** Pre-production / playable prototype  
**Engine:** jMonkeyEngine 3.9  
**Language:** Java 25  
**Distribution target:** itch.io

## About

**Dead Signal** is an original fan-game concept set in an abandoned Playtime Co. facility.

A mysterious transmission has brought one forgotten section of the factory back online. Follow the signal into **Sector 07 — Signal Processing**, solve environmental puzzles, uncover the story behind **Project: Echo**, and survive whatever has been listening.

This repository contains the game source and development builds.

## Current prototype

The first milestone focuses on a professional foundation:

- Java 25 Gradle project
- jMonkeyEngine 3.9
- Main menu and options entry point
- First-person prototype scene
- Basic movement and mouse-look
- Flashlight-ready architecture
- Clean separation between menu, gameplay, and systems
- Build configuration suitable for future itch.io releases

## How to download and play

### Recommended: itch.io

**The public release page will be added here when the first playable build is released.**

1. Download the latest release for your operating system from the itch.io page.
2. Extract the downloaded ZIP.
3. Run the game executable.
4. If your operating system asks for permission, allow the game to launch.

Do **not** download development source code if you only want to play the game.

### Developers

You need **JDK 25** installed.

Clone the repository and run:

```bash
./gradlew run
```

On Windows:

```powershell
gradlew.bat run
```

Build the project with:

```bash
./gradlew build
```

The generated files are placed in `build/libs/`.

> A Gradle wrapper will be committed before the first public developer release so contributors do not need a separate Gradle installation.

## Controls

| Action | Default |
|---|---|
| Move | W A S D |
| Look | Mouse |
| Jump | Space |
| Flashlight | F |
| Pause | Esc |

Controls are subject to change during development.

## Project structure

```text
src/main/java/dev/azuremc/deadsignal/
├── DeadSignalGame.java
├── game/
│   ├── GameState.java
│   └── GameplayState.java
└── menu/
    └── MainMenuState.java
```

## Development roadmap

### Milestone 0 — Foundation
- [x] Java 25 project
- [x] jMonkeyEngine integration
- [x] Main menu shell
- [x] Prototype gameplay state

### Milestone 1 — Vertical slice
- [ ] First factory room
- [ ] First-person controller
- [ ] Flashlight
- [ ] Interactable doors
- [ ] First GrabPack prototype
- [ ] First power puzzle
- [ ] Save/checkpoint system

### Milestone 2 — Horror systems
- [ ] Morrow AI
- [ ] Stalking behaviour
- [ ] Chase sequence
- [ ] Audio system
- [ ] Environmental events
- [ ] Cutscene framework

### Milestone 3 — Public demo
- [ ] Complete opening sequence
- [ ] Sector 07 environment
- [ ] First major encounter
- [ ] Optimisation
- [ ] Accessibility/options
- [ ] Windows/Linux builds
- [ ] itch.io release

## Fan project notice

This is an unofficial fan project and is not affiliated with or endorsed by Mob Entertainment or the official Poppy Playtime team.

The project will use original fan-made code, environments, characters, story elements, audio, and other assets where possible. Third-party assets will only be included when their licenses permit redistribution.

The final distribution page will clearly identify the project as an unofficial fan game.

## Credits

**AzureMC Projects** — Development

Built with **jMonkeyEngine** and Java.

jMonkeyEngine is licensed under its own open-source license. See the engine project for licensing information.

## Contributing

Issues and pull requests are welcome during development. Please keep contributions focused on the current milestone and avoid adding copyrighted assets without permission or a compatible redistribution license.
