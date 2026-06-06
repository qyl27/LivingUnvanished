package dev.yuluo.mc.living_unvanished.client.model.entity;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

import java.util.function.Function;

public class DispatchedEntityModel<T extends Entity & GeoAnimatable> extends GeoModel<T> {
    private final Function<GeoRenderState, GeoModel<T>> stateDispatcher;
    private final Function<T, GeoModel<T>> instanceDispatcher;

    public DispatchedEntityModel(Function<GeoRenderState, GeoModel<T>> stateDispatcher, Function<T, GeoModel<T>> instanceDispatcher) {
        this.stateDispatcher = stateDispatcher;
        this.instanceDispatcher = instanceDispatcher;
    }

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        return stateDispatcher.apply(renderState).getModelResource(renderState);
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return stateDispatcher.apply(renderState).getTextureResource(renderState);
    }

    @Override
    public Identifier getAnimationResource(T animatable) {
        return instanceDispatcher.apply(animatable).getAnimationResource(animatable);
    }
}
