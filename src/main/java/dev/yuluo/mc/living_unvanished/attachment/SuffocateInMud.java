package dev.yuluo.mc.living_unvanished.attachment;

import dev.yuluo.mc.living_unvanished.block.MudSlurryBlock;
import dev.yuluo.mc.living_unvanished.registry.ModAttachmentTypes;
import dev.yuluo.mc.living_unvanished.registry.ModDamageTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public final class SuffocateInMud {
    private static final int DAMAGE_THRESHOLD = -20;
    private static final float DAMAGE_AMOUNT = 2;

    public static int get(LivingEntity entity) {
        return entity.getData(ModAttachmentTypes.SUFFOCATE_IN_MUD.get());
    }

    private static void set(LivingEntity entity, int air) {
        entity.setData(ModAttachmentTypes.SUFFOCATE_IN_MUD.get(), air);
    }

    private static boolean isSuffocating(LivingEntity entity) {
        if (entity instanceof Player player && player.getAbilities().invulnerable) {
            return false;
        }

        return get(entity) <= DAMAGE_THRESHOLD;
    }

    public static void tick(LivingEntity entity) {
        var current = get(entity);
        var newAir = current;
        if (MudSlurryBlock.isEntityInside(entity)) {
            newAir = entity.decreaseAirSupply(current);
        } else {
            newAir = entity.increaseAirSupply(current);
        }

        if (isSuffocating(entity)) {
            newAir = 0;
            var level = entity.level();
            if (level instanceof ServerLevel serverLevel) {
                entity.hurtServer(serverLevel, entity.damageSources().source(ModDamageTypes.SUFFOCATE_IN_MUD), DAMAGE_AMOUNT);
            }
        }

        if (newAir != current) {
            set(entity, newAir);
        }
    }

    public static boolean shouldShowSuffocateAir(Player player) {
        var data = get(player);
        return data < player.getMaxAirSupply();
    }
}
