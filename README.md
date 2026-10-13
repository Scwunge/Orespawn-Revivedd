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

Spawns and world generation follow the original OreSpawn 1.7.10 tables, and all of them are data-driven, so a datapack can override any of them:

| File (under `data/orespawn/`) | Controls |
|------|---------|
| `neoforge/biome_modifier/gold_spawns_<biome>.json` | Overworld / Nether mob spawns (the original's spawn table, by biome) |
| `worldgen/biome/<dim>.json` | Mob lists for each OreSpawn dimension (Mobzilla lives in `village_mania.json`) |
| `worldgen/placed_feature/spawn_egg_ores.json` | Ancient Dried Spawn Egg ore veins (count / height) |
| `neoforge/biome_modifier/add_overworld_ores.json` | Which Overworld ores generate (uranium, titanium, ruby, trolls, buried blocks, ...) |
| `worldgen/placed_feature/ant_hill*.json` | Ant nest rarity per dimension |

Halloween ghosts and the Easter Bunny are date-based (`SeasonalSpawnEvents`). The King and Queen altars are generated in Utopia by `UtopiaDimEvents`.

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
