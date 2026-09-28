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

## Spawn rates

Spawn weights are data-driven, so a datapack can override any of them:

| File (under `data/orespawn/`) | Controls |
|------|---------|
| `worldgen/biome/<dim>.json` | Mob lists for each OreSpawn dimension (Mobzilla lives in `village_mania.json`) |
| `neoforge/biome_modifier/add_*_spawns.json` | Overworld / Nether additions (ants, termites, ocean monsters, …) |
| `neoforge/biome_modifier/add_cephadrome_altars.json` + `worldgen/placed_feature/cephadrome_altar.json` | Cephadrome Altar (rarity 1 in 150 chunks, then a flat-ground check) |

The King and Queen altars are generated in Utopia by `UtopiaDimEvents`.

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
