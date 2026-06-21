package dev.yuluo.mc.living_unvanished.util;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import net.minecraft.resources.Identifier;

public class IdHelper {
    public static Identifier modLoc(String path) {
        return Identifier.fromNamespaceAndPath(LivingUnvanished.MODID, path);
    }
}
