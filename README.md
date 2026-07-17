# Orespawn Revived

NeoForge **1.21.1** mod (`mod_id`: `orespawn`).

**Owner:** Scwunge  
**License:** MIT — free for anyone to use, copy, modify, redistribute, and include in modpacks (including commercial), as long as the MIT notice is kept. See [LICENSE](LICENSE).

## Requirements

| Tool | Version |
|------|---------|
| JDK | **21** |
| Gradle | Wrapper included (no global install needed) |

## Build

```bat
gradlew build
```

Jar output: `build/libs/Orespawn-Revived-<version>.jar`

## Run (dev)

```bat
gradlew runClient
gradlew runServer
```

## Features (highlights)

- OreSpawn content for 1.21.1 (mobs, blocks, dimensions, items)
- Built-in **portal gun** system (same mod id `orespawn`)
  - Craft: Ender Pearl Dust → Ender Star → Portal Gun
  - Config: `run/config/orespawn-portal.toml`

## Project layout

| Path | Purpose |
|------|---------|
| `src/main/java` | Mod code |
| `src/main/resources` | Assets, data, sounds |
| `src/main/templates` | `neoforge.mods.toml` (expanded at build) |
| `LICENSE` | MIT |

## Notes

- Do not commit `build/`, `.gradle/`, or `run/` (gitignored).
- Use **JDK 21** for builds.
