package dev.yuluo.mc.living_unvanished.data;

import dev.yuluo.mc.living_unvanished.LivingUnvanished;
import dev.yuluo.mc.living_unvanished.registry.ModBlocks;
import dev.yuluo.mc.living_unvanished.registry.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public final class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, LivingUnvanished.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(ModItems.MUD_SLURRY_BUCKET.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(ModItems.STRANGE_SKULL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.AVIAN_SKULL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BEAST_SKULL.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(ModItems.STRANGE_FEATHER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BLUE_CONTOUR_FEATHER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.ORANGE_CONTOUR_FEATHER.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(ModItems.STRANGE_BONES.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.KEEL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.RIBS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.FEMUR.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(ModItems.STRANGE_LEATHER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.STRIPED_LEATHER.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(ModItems.IDENTIFICATION_MANUAL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.LEFTOVER_PAGE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.MEMOIR.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BLUE_PIGEON_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);


        blockModels.createBrushableBlock(ModBlocks.SUSPICIOUS_MUD.get());
        blockModels.createBrushableBlock(ModBlocks.SUSPICIOUS_RED_SAND.get());
        createBrushableColumnBlock(blockModels, ModBlocks.SUSPICIOUS_ROTTEN_WOOD.get());

        blockModels.createNonTemplateModelBlock(ModBlocks.MUD_SLURRY.get());

        blockModels.createNonTemplateModelBlock(ModBlocks.BLUE_PIGEON_EGG.get());
        itemModels.generateFlatItem(ModItems.BLUE_PIGEON_EGG.get(), ModelTemplates.FLAT_ITEM);

        TextureMapping mapping = new TextureMapping()
            .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(ModBlocks.RESTORATION_TABLE.get(), "_bottom"))
            .put(TextureSlot.DOWN, TextureMapping.getBlockTexture(ModBlocks.RESTORATION_TABLE.get(), "_bottom"))
            .put(TextureSlot.UP, TextureMapping.getBlockTexture(ModBlocks.RESTORATION_TABLE.get(), "_top"))
            .put(TextureSlot.NORTH, TextureMapping.getBlockTexture(ModBlocks.RESTORATION_TABLE.get(), "_bottom"))
            .put(TextureSlot.EAST, TextureMapping.getBlockTexture(ModBlocks.RESTORATION_TABLE.get(), "_side"))
            .put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(ModBlocks.RESTORATION_TABLE.get(), "_front"))
            .put(TextureSlot.WEST, TextureMapping.getBlockTexture(ModBlocks.RESTORATION_TABLE.get(), "_side"));
        blockModels.blockStateOutput.accept(
            BlockModelGenerators.createSimpleBlock(
                ModBlocks.RESTORATION_TABLE.get(),
                BlockModelGenerators.plainVariant(
                    ModelTemplates.CUBE.create(
                        ModBlocks.RESTORATION_TABLE.get(),
                        mapping,
                        blockModels.modelOutput)
                )
            )
        );
    }

    private static void createBrushableColumnBlock(BlockModelGenerators blockModels, Block block) {
        blockModels.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(block)
                .with(
                    PropertyDispatch.initial(BlockStateProperties.DUSTED)
                        .generate(dusted -> {
                            var suffix = "_" + dusted;
                            var textures = new TextureMapping()
                                .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, suffix))
                                .put(TextureSlot.END, TextureMapping.getBlockTexture(block, "_top"));
                            var model = ModelTemplates.CUBE_COLUMN.createWithSuffix(block, suffix, textures, blockModels.modelOutput);
                            return BlockModelGenerators.plainVariant(model);
                        })
                )
                .with(BlockModelGenerators.createRotatedPillar())
        );
        blockModels.registerSimpleItemModel(block, ModelLocationUtils.getModelLocation(block, "_0"));
    }
}
