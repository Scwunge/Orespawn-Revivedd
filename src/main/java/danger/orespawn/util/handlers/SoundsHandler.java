package danger.orespawn.util.handlers;

import danger.orespawn.util.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Gold SoundsHandler — DeferredRegister for 1.21.1.
 * Asset keys match assets/orespawn/sounds.json + ogg files.
 */
public final class SoundsHandler {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, Reference.MOD_ID);

    /** Gold keys; sounds.json maps to slime hit as stand-in if no ogg. */
    public static final DeferredHolder<SoundEvent, SoundEvent> LITTLE_SPLAT = reg("entity.misc.little_splat");
    public static final DeferredHolder<SoundEvent, SoundEvent> BIG_SPLAT = reg("entity.misc.big_splat");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_ALOSAURUS_LIVING = reg("entity.alosaurus.living");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_ALOSAURUS_HURT = reg("entity.alosaurus.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_ALOSAURUS_DEATH = reg("entity.alosaurus.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TREX_AMBIENT = reg("entity.trex.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_TREX_DEATH = reg("entity.trex.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DUCK_HURT = reg("entity.duck.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_CRYO_HURT = reg("entity.cryo.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_CRYO_DEATH = reg("entity.cryo.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_CRYO_LIVING = reg("entity.cryo.living");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD1 = reg("entity.birds.birds1");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD2 = reg("entity.birds.birds2");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD3 = reg("entity.birds.birds3");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD4 = reg("entity.birds.birds4");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD5 = reg("entity.birds.birds5");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD6 = reg("entity.birds.birds6");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD7 = reg("entity.birds.birds7");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD8 = reg("entity.birds.birds8");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD9 = reg("entity.birds.birds9");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD10 = reg("entity.birds.birds10");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD11 = reg("entity.birds.birds11");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD12 = reg("entity.birds.birds12");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD13 = reg("entity.birds.birds13");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD14 = reg("entity.birds.birds14");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD15 = reg("entity.birds.birds15");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD16 = reg("entity.birds.birds16");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD17 = reg("entity.birds.birds17");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD18 = reg("entity.birds.birds18");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD19 = reg("entity.birds.birds19");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD20 = reg("entity.birds.birds20");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD21 = reg("entity.birds.birds21");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD22 = reg("entity.birds.birds22");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BIRD_BIRD23 = reg("entity.birds.birds23");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GAMMAMETROID_LIVING = reg("entity.gammametroid.living");
    /** ogg present (entity/gammametroid/hurt.ogg); gold entity used DUCK_HURT stand-in */
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_GAMMAMETROID_HURT = reg("entity.gammametroid.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DRAGONFLY_LIVING = reg("entity.dragonfly.living");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DRAGONFLY_HURT = reg("entity.dragonfly.hurt");
    /** ogg present (entity/dragonfly/dragonfly_death.ogg); gold had no death holder */
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_DRAGONFLY_DEATH = reg("entity.dragonfly.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_MOSQUITO_LIVING = reg("entity.mosquito.living");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_ALIEN_LIVING = reg("entity.alien.living");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_ALIEN_HURT = reg("entity.alien.hurt");
    /** sounds.json + alien/death.ogg present; gold Alien left default death */
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_ALIEN_DEATH = reg("entity.alien.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_MOTHRA_WINGS = reg("entity.mothra.wings");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_KYUUBI_LIVING = reg("entity.kyuubi.living");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_STINKBUG_FART1 = reg("entity.stinkbug.fart1");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_STINKBUG_FART2 = reg("entity.stinkbug.fart2");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_STINKBUG_FART3 = reg("entity.stinkbug.fart3");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_STINKBUG_FART4 = reg("entity.stinkbug.fart4");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_STINKBUG_FART5 = reg("entity.stinkbug.fart5");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_STINKBUG_FART6 = reg("entity.stinkbug.fart6");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_STINKBUG_FART7 = reg("entity.stinkbug.fart7");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_STINKBUG_FART8 = reg("entity.stinkbug.fart8");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_STINKBUG_FART9 = reg("entity.stinkbug.fart9");
    /** Gold orespawn:ratlive / rathit / ratdead (ogg present). */
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_RAT_LIVING = reg("entity.rat.living");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_RAT_HURT = reg("entity.rat.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_RAT_DEATH = reg("entity.rat.death");
    /** Gold orespawn:cricket */
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_CRICKET_AMBIENT = reg("entity.cricket.ambient");
    /** Gold orespawn:scorpion_hit (Chipmunk hurt) */
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SCORPION_HIT = reg("entity.scorpion.hit");
    /** Gold orespawn:Beebuzz (beebuzz.ogg) */
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BEE_BUZZ = reg("entity.bee.buzz");
    /** Gold orespawn:basilisk_living (basilisk_living.ogg) */
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_BASILISK_LIVING = reg("entity.basilisk.living");
    /** Gold orespawn:emperorscorpion_death — Basilisk death (and EmperorScorpion) */
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_EMPERORSCORPION_DEATH = reg("entity.emperorscorpion.death");
    /** Gold orespawn:squid_hurt (squid_hurt1-4.ogg) — AttackSquid */
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SQUID_HURT = reg("entity.squid.hurt");
    /** Gold orespawn:squid_death (squid_death1-2.ogg) — AttackSquid */
    public static final DeferredHolder<SoundEvent, SoundEvent> ENTITY_SQUID_DEATH = reg("entity.squid.death");

    private static DeferredHolder<SoundEvent, SoundEvent> reg(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(
                ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, name)));
    }

    private SoundsHandler() {}
}
