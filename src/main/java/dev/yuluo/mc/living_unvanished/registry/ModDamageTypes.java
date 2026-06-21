package dev.yuluo.mc.living_unvanished.registry;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import dev.yuluo.mc.living_unvanished.util.IdHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public final class ModDamageTypes {
    public static final ResourceKey<DamageType> SUFFOCATE_IN_MUD = ResourceKey.create(
        Registries.DAMAGE_TYPE,
        IdHelper.modLoc("suffocate_in_mud")
    );

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(SUFFOCATE_IN_MUD, new DamageType(LivingUnvanished.MODID + ".suffocate_in_mud", 0));
    }
}
