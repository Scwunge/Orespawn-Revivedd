# Orespawn Revived

NeoForge **1.21.1** port of OreSpawn (`mod_id`: `orespawn`, package `danger.orespawn`).

[![Build](https://github.com/OWNER/REPO/actions/workflows/build.yml/badge.svg)](https://github.com/OWNER/REPO/actions/workflows/build.yml)

> After you create the GitHub repo, replace `OWNER/REPO` in the badge above with your real path (or delete the badge).

## License / freedom

**MIT** — free for anyone. **No DRM**, no premium checks, no paid unlock.

You may use, copy, modify, redistribute, and include this mod in modpacks
(including commercial ones), as long as you keep the MIT license notice
(see [`LICENSE`](LICENSE)).

Original OreSpawn by **TheyCallMeDanger** / **TheBulbacraft** and contributors.
This repository is a community **1.21.1** NeoForge port.

## Requirements

| Tool | Version |
|------|---------|
| JDK | **21** (Minecraft 1.21.1) |
| Gradle | Wrapper included (**9.2.1**) — no global install needed |

## Build

From the repo root:

```bash
# Linux / macOS
./gradlew build

# Windows (PowerShell / cmd)
gradlew.bat build
```

Output jar:

```text
build/libs/Orespawn-Revived-<version>.jar
```

Version comes from `mod_version` in [`gradle.properties`](gradle.properties).

## Run (dev)

```bash
./gradlew runClient    # or: gradlew.bat runClient
./gradlew runServer    # dedicated server, no GUI
```

First run downloads Minecraft/NeoForge assets and can take several minutes.

## Project layout

| Path | Purpose |
|------|---------|
| `src/main/java` | Mod code |
| `src/main/resources` | Assets, data packs, sounds |
| `src/main/templates` | `neoforge.mods.toml` template (expanded at build) |
| `build.gradle` / `gradle.properties` | NeoForge 21.1 + Parchment setup |
| `gradlew` / `gradlew.bat` | Gradle wrapper |
| `.github/workflows/build.yml` | CI: compile + upload jar artifact |
| `LICENSE` | MIT |

## GitHub setup (once)

```bash
# if not already a git repo
git init
git add .
git commit -m "Initial commit: Orespawn Revived (NeoForge 1.21.1)"

# create empty repo on GitHub, then:
git branch -M main
git remote add origin https://github.com/OWNER/REPO.git
git push -u origin main
```

Or with GitHub CLI:

```bash
gh repo create OWNER/REPO --public --source=. --remote=origin --push
```

## Notes

- Do **not** commit `build/`, `.gradle/`, or `run/` — they are gitignored.
- CI uses **JDK 21** (same as the mod toolchain). Building with only JDK 25 may work via toolchains, but **21 is recommended**.
- NeoForge MDK template files retain NeoForged’s template license notice in `TEMPLATE_LICENSE.txt`.
