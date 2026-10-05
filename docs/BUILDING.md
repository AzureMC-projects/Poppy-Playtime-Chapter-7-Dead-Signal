# Building Dead Signal

## Requirements

- JDK 25
- Internet access for the first dependency download
- A machine capable of running a jMonkeyEngine desktop application

The project uses Gradle and jMonkeyEngine 3.9.0-stable. jMonkeyEngine's 3.9 SDK is Java 25-ready, and Gradle is the recommended project workflow in the official documentation.

## Run from source

Linux/macOS:

```bash
./gradlew run
```

Windows:

```powershell
gradlew.bat run
```

If the Gradle wrapper is not present in your checkout, install a compatible Gradle release and run:

```bash
gradle run
```

## Build

```bash
gradle clean build
```

Build outputs are written to `build/libs/`.

## Release philosophy

Public player builds should be packaged separately from source. The intended distribution channel is itch.io, with platform-specific archives and a clear version number.

Do not package development-only files, source-only tooling, or copyrighted third-party assets that are not licensed for redistribution.
