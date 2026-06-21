package dev.yuluo.mc.living_unvanished.client.model.entity.thylacine;

import com.geckolib.model.DefaultedEntityGeoModel;
import dev.yuluo.mc.living_unvanished.entity.thylacine.Thylacine;
import dev.yuluo.mc.living_unvanished.util.IdHelper;
import net.minecraft.resources.Identifier;

public class ThylacineModel extends DefaultedEntityGeoModel<Thylacine> {
    private static final Identifier ID = IdHelper.modLoc("thylacine");

    public ThylacineModel() {
        super(ID);
    }
}
