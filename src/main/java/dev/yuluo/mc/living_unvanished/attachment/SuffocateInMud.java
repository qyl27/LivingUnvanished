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
        if (air >= entity.getMaxAirSupply()) {
            entity.removeData(ModAttachmentTypes.SUFFOCATE_IN_MUD.get());
        } else {
            entity.setData(ModAttachmentTypes.SUFFOCATE_IN_MUD.get(), air);
        }
    }

    private static void decrease(LivingEntity entity) {
        int airSupply = entity.decreaseAirSupply(get(entity));
        set(entity, airSupply);
    }

    private static void increase(LivingEntity entity) {
        int airSupply = entity.increaseAirSupply(get(entity));
        set(entity, airSupply);
    }

    private static boolean isSuffocating(LivingEntity entity) {
        if (entity instanceof Player player && player.getAbilities().invulnerable) {
            return false;
        }

        return get(entity) <= DAMAGE_THRESHOLD;
    }

    public static void tick(LivingEntity entity) {
        if (MudSlurryBlock.isEntityInside(entity)) {
            decrease(entity);
        } else {
            increase(entity);
        }

        if (isSuffocating(entity)) {
            set(entity, 0);
            var level = entity.level();
            if (level instanceof ServerLevel serverLevel) {
                entity.hurtServer(serverLevel, entity.damageSources().source(ModDamageTypes.SUFFOCATE_IN_MUD), DAMAGE_AMOUNT);
            }
        }
    }

    public static boolean shouldShowSuffocateAir(Player player) {
        var data = get(player);
        return data < player.getMaxAirSupply();
    }
}
