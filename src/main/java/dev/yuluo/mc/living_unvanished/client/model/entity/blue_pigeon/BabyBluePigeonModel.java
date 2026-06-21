package dev.yuluo.mc.living_unvanished.client.model.entity.blue_pigeon;

import com.geckolib.model.DefaultedEntityGeoModel;
import dev.yuluo.mc.living_unvanished.entity.blue_pigeon.BluePigeon;
import dev.yuluo.mc.living_unvanished.util.IdHelper;
import net.minecraft.resources.Identifier;

public class BabyBluePigeonModel extends DefaultedEntityGeoModel<BluePigeon> {
    private static final Identifier ID = IdHelper.modLoc("mauritius_blue_pigeon_baby");

    public BabyBluePigeonModel() {
        super(ID);
    }
}
