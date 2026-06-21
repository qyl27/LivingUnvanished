package dev.yuluo.mc.living_unvanished.client.renderer.entity.blue_pigeon;

import com.geckolib.model.GeoModel;
import com.geckolib.renderer.GeoEntityRenderer;
import dev.yuluo.mc.living_unvanished.client.model.entity.DispatchedEntityModel;
import dev.yuluo.mc.living_unvanished.client.model.entity.blue_pigeon.BabyBluePigeonModel;
import dev.yuluo.mc.living_unvanished.client.model.entity.blue_pigeon.BluePigeonModel;
import dev.yuluo.mc.living_unvanished.client.renderer.entity.ModDataTickets;
import dev.yuluo.mc.living_unvanished.entity.blue_pigeon.BluePigeon;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.jspecify.annotations.Nullable;

public class BluePigeonRenderer extends GeoEntityRenderer<BluePigeon, LivingEntityRenderState> {
    private static final GeoModel<BluePigeon> ADULT_MODEL = new BluePigeonModel();
    private static final GeoModel<BluePigeon> BABY_MODEL = new BabyBluePigeonModel();

    private static final GeoModel<BluePigeon> MODEL = new DispatchedEntityModel<>(
        state -> Boolean.TRUE.equals(state.getGeckolibData(ModDataTickets.IS_BABY)) ? BABY_MODEL : ADULT_MODEL,
        instance -> instance.isBaby() ? BABY_MODEL : ADULT_MODEL);

    public BluePigeonRenderer(EntityRendererProvider.Context context) {
        super(context, MODEL);
        this.shadowRadius = 0.25F;
    }

    @Override
    public void addRenderData(BluePigeon animatable, @Nullable Void relatedObject, LivingEntityRenderState renderState, float partialTick) {
        renderState.addGeckolibData(ModDataTickets.IS_BABY, animatable.isBaby());
        renderState.addGeckolibData(ModDataTickets.HELD_ITEM, animatable.getHeldItem());
    }
}
