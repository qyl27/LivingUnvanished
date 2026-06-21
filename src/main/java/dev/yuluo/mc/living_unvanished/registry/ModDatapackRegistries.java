package dev.yuluo.mc.living_unvanished.registry;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class ModDatapackRegistries {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.DAMAGE_TYPE, ModDamageTypes::bootstrap);
}
