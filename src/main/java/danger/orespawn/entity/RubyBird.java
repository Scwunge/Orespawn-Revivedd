package danger.orespawn.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

/**
 * Gold {@code RubyBird} extends {@code Cockateil}. Size 0.5×0.5 (same as Cockateil).
 * Forces birdtype 5 (ruby / bird6 texture) and {@code setFlyUp()} on init.
 * Ambient: {@code orespawn:rubybird} daytime non-rain (SoundsHandler deferred).
 * Spawn: gold {@code getCanSpawnHere} always true. Drops inherit Cockateil (type 5 + player kill → 1/3 ruby).
 */
public class RubyBird extends Cockateil {
    public RubyBird(EntityType<? extends RubyBird> type, Level level) {
        super(type, level);
        // gold entityInit: birdtype = 5; setBirdType; setFlyUp
        this.setBirdType(5);
        this.setFlyUp();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Cockateil.createAttributes();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        // gold: daytime && !raining → "orespawn:rubybird"
        // Sound registration (SoundsHandler + sounds.json) is not wired yet (deferred).
        if (this.level().isDay() && !this.level().isRaining()) {
            return null; // TODO: register orespawn:rubybird → entity/birds/rubybird.ogg
        }
        return null;
    }

    /** Gold {@code getCanSpawnHere}: always true (overrides Cockateil day/y≥50). */
    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType spawnType) {
        return true;
    }
}
