# Changelog

## 1.0.3 (Minecraft 1.21.1, NeoForge)

This update brings world generation and spawning back in line with the original OreSpawn 1.7.10.

### Ancient Dried Spawn Eggs
- **All 105 original egg ores are back.** Before this update only 32 existed. The new ones include the vanilla mobs (Spider, Skeleton, Enderman, Blaze, Wither, Ender Dragon, Villager and more), plus Girlfriend, Boyfriend, Kraken, Cephadrome, Leonopteryx, Water Dragon, Sea Monster and many others.
- **Boss egg parts are back.** Mine 9 Mobzilla, King or Queen Spawn Egg Parts and craft them into the whole Ancient Dried egg.
- **Rehydrate eggs like the original.** Craft an Ancient Dried Spawn Egg with a water bucket to get the spawn egg (the bucket comes back empty).
- **Original generation.** Each vein is a random egg ore, in small veins between y 50 and 128, about as often as in 1.7.10. Before this update every type had its own large veins, which made egg ores far more common.

### Ores
- **Red Ant Troll and Termite Troll ores now generate** underground (between y 5 and 50). They never appeared before.
- **Ruby works like the original.** It forms in the stone right under lava. Rubies also appear in the Nether.
- **Nether Lavafoam now generates.**
- **Extra underground treasure from the original:** extra diamond, emerald and gold veins, and rare buried blocks of diamond, emerald, gold and ruby.
- **Uranium, Titanium, Amethyst and Salt rates match the original.** They were several times more common before.
- The Mining Dimension gets the original's extra lapis and extra rubies.

### Ant nests
- **All five ant nests from the original:** Ant Nest, Red Ant Nest, Rainbow Ant Nest, Unstable Ant Nest and Termite Nest. Ants now come from nests, as in the original.
- **Crystal Termite Nests** generate on crystal grass in the Crystal Dimension, and Red Ant Nests generate on netherrack in the Nether.
- Nests release ants whenever it isn't raining, as in the original.

### Mob spawning
- **Overworld and Nether spawns now follow the original's spawn table.** About 55 mobs spawn in their original biomes with the original weights. Many were missing before, including Girlfriend, Boyfriend, Cockateil, Moths, Crickets, Chipmunks, Ender Knights and Reapers, Hydrolisc, Frogs, Lizards, Whales, Rubber Duckies and Rats.
- **Holidays are back.** Extra ghosts on Halloween, the Easter Bunny on April 20, and Girlfriend Valentine's Day mode on February 14.
- Birds and Cave Fishers no longer spawn naturally everywhere, and ants, termites and the Kraken no longer spawn on their own. None of these spawned naturally in the original.

### Structures
- **Small Overworld structures now generate:** Play Pool, Water Dragon Lair, Gold Fish Bowl, Girlfriend Island and Monster Island in oceans, plus Frog Pond, Bee and Mantis Hives, Haunted House, Leaf Monster tower, Spit Bug Lair, Igloo, Bouncy Castle and Rubber Ducky Pond. They use the original biomes and rarity.

### Weapon rendering fixed
- **Big Bertha, The Slice, Royal Guardian Sword, Attitude Adjuster, Battle Axe, Queen Scale Battle Axe, Chainsaw and SquidZooka** now render the way they did in the original, in first person, third person, on armor stands and in the off-hand. Before this update they appeared huge, sideways or blocking the screen.

## 1.0.2 (Minecraft 1.21.1, NeoForge)

### Fixes

- **Boyfriend textures fixed.** His arms and legs rendered broken or see-through because his skins (including the swim shorts and Frog Prince skins) use the old 64×32 skin layout but were drawn on the newer 64×64 player model. He now uses a model that matches his skins.

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
