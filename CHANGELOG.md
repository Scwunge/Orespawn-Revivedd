# Changelog

## 1.0.1 (Minecraft 1.21.1, NeoForge)

### Spawning fixes

- **Ants are back.** Brown Ants, Rainbow Ants and Unstable Ants now spawn naturally across the Overworld, and Termites spawn in forests and jungles. Before this update they could only be spawned from eggs, so the Utopia, Village Mania, Islands and Crystal dimensions couldn't be reached in survival.
- **Mobzilla spawns properly.** Mobzilla could only spawn naturally once per game session. After the first one, no more would appear until the game was restarted. Now another one can spawn about a minute after the previous one dies or unloads. It still spawns rarely, at night, in the Village Mania dimension.
- **Ocean monsters spawn.** The Kraken, Sea Monster, Sea Viper and Attack Squid now spawn at the water's surface in ocean biomes. They never appeared naturally before.
- **Water mobs spawn.** Whales, Flounders, Frogs, Skates and Irukandji never spawned naturally, even in dimensions that list them. Now they do.
- **Mob caps fixed in the OreSpawn dimensions.** Several mobs, such as Cockateils, Chipmunks, Gold Fish, Peacocks and Bees, were filed under the wrong mob category. This let them skip the normal mob caps and pile up. They now count toward the correct cap.
- Fixed a server startup error about Coin and T-Shirt spawns.

### World generation

- **The Cephadrome Altar now generates in the Overworld.** It's a rare stepped stone platform with sea lanterns, Extreme Torches and a Cephadrome spawner on top, and it only appears on flat, dry ground.

### For modpack makers

- All of these spawn rates are data-driven JSON and can be changed with a datapack. See the "Spawn rates" section of the README for which file controls what.

### Note for existing worlds

- The Cephadrome Altar only appears in newly generated chunks. Mob spawn changes apply everywhere right away.
