package dev.yuluo.mc.living_unvanished.client.renderer.entity.thylacine;

import com.geckolib.renderer.GeoEntityRenderer;
import dev.yuluo.mc.living_unvanished.client.model.entity.thylacine.ThylacineModel;
import dev.yuluo.mc.living_unvanished.client.renderer.entity.ModDataTickets;
import dev.yuluo.mc.living_unvanished.entity.thylacine.Thylacine;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import org.jspecify.annotations.Nullable;

public class ThylacineRenderer extends GeoEntityRenderer<Thylacine, LivingEntityRenderState> {
    public ThylacineRenderer(EntityRendererProvider.Context context) {
        super(context, new ThylacineModel());
        this.shadowRadius = 0.45F;
        this.withRenderLayer(new ThylacineArmorLayer(this));
    }

    @Override
    public void addRenderData(Thylacine animatable, @Nullable Void relatedObject, LivingEntityRenderState renderState, float partialTick) {
        renderState.addGeckolibData(ModDataTickets.BODY_ARMOR_ITEM, animatable.getItemBySlot(EquipmentSlot.BODY).copy());
    }
}
