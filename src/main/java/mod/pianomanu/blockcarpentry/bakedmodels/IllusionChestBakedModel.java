package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.BlockCarpentryMain;
import mod.pianomanu.blockcarpentry.tileentity.ChestFrameTileEntity;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Contains all information for the block model
 * See {@link ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.2 11/07/22
 */
public class IllusionChestBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        BlockState mimic = extraData.getData(ChestFrameTileEntity.MIMIC);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            return getMimicQuads(state, side, rand, extraData, model);
        }

        return Collections.emptyList();
    }

    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, IBakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.getData(ChestFrameTileEntity.MIMIC);
        if (mimic != null && state != null) {
            List<TextureAtlasSprite> designTextureList = new ArrayList<>(TextureHelper.getMetalTextures());
            designTextureList.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/shulker_box")));
            TextureAtlasSprite chestFront = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chest_front"));
            TextureAtlasSprite chestSide = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chest_side"));
            TextureAtlasSprite chestTop = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation(BlockCarpentryMain.MOD_ID, "block/chest_top"));
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            int rotation = extraData.getData(ChestFrameTileEntity.ROTATION);
            int design = extraData.getData(ChestFrameTileEntity.DESIGN);
            int desTex = extraData.getData(ChestFrameTileEntity.DESIGN_TEXTURE);
            TextureAtlasSprite designTexture = designTextureList.get(desTex);
            List<BakedQuad> quads = new ArrayList<>();
            if (design == 0) {
                return new ArrayList<>(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
            }
            if (design == 1 || design == 2) {
                quads.addAll(ModelHelper.createSixFaceCuboid(2 / 16f, 14 / 16f, 2 / 16f, 14 / 16f, 2 / 16f, 14 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
            }
            if (design == 3) {
                quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 2 / 16f, 14 / 16f, 2 / 16f, 14 / 16f, designTexture, -1));
            }
            if (design == 1 || design == 3 || design == 4) {
                //vertical
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 2 / 16f, 0f, 1f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 2 / 16f, 0f, 1f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(14 / 16f, 1f, 0f, 1f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(14 / 16f, 1f, 0f, 1f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                //horizontal down
                quads.addAll(ModelHelper.createSixFaceCuboid(2 / 16f, 14 / 16f, 0f, 2 / 16f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(2 / 16f, 14 / 16f, 0f, 2 / 16f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 2 / 16f, 0f, 2 / 16f, 2 / 16f, 14 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(14 / 16f, 1f, 0f, 2 / 16f, 2 / 16f, 14 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                //horizontal up
                quads.addAll(ModelHelper.createSixFaceCuboid(2 / 16f, 14 / 16f, 14 / 16f, 1f, 0f, 2 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(2 / 16f, 14 / 16f, 14 / 16f, 1f, 14 / 16f, 1f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(0f, 2 / 16f, 14 / 16f, 1f, 2 / 16f, 14 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
                quads.addAll(ModelHelper.createSixFaceCuboid(14 / 16f, 1f, 14 / 16f, 1f, 2 / 16f, 14 / 16f, mimic, model, extraData, rand, tintIndex, rotation));
            }
            if (design == 2) {
                //vertical
                quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 0f, 1f, 0f, 2 / 16f, designTexture, -1));
                quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 0f, 1f, 14 / 16f, 1f, designTexture, -1));
                quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 0f, 1f, 0f, 2 / 16f, designTexture, -1));
                quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 0f, 1f, 14 / 16f, 1f, designTexture, -1));
                //horizontal down
                quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 0f, 2 / 16f, 0f, 2 / 16f, designTexture, -1));
                quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 0f, 2 / 16f, 14 / 16f, 1f, designTexture, -1));
                quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 0f, 2 / 16f, 2 / 16f, 14 / 16f, designTexture, -1));
                quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 0f, 2 / 16f, 2 / 16f, 14 / 16f, designTexture, -1));
                //horizontal up
                quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 14 / 16f, 1f, 0f, 2 / 16f, designTexture, -1));
                quads.addAll(ModelHelper.createCuboid(2 / 16f, 14 / 16f, 14 / 16f, 1f, 14 / 16f, 1f, designTexture, -1));
                quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 14 / 16f, 1f, 2 / 16f, 14 / 16f, designTexture, -1));
                quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 14 / 16f, 1f, 2 / 16f, 14 / 16f, designTexture, -1));
            }
            if (design == 4) {
                quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 2 / 16f, 1 / 16f, 15 / 16f, chestTop, -1));
                quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 14 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestTop, -1));
                //has to be inverted because chest knob-thing is also inverted
                int[] ulow = {7, 7, 7, 9, 8, 9};
                int[] uhigh = {9, 9, 8, 7, 9, 7};
                int[] vlow = {4, 7, 4, 4, 4, 4};
                int[] vhigh = {5, 8, 8, 8, 8, 8};
                switch (state.get(BlockStateProperties.FACING)) {
                    case NORTH:
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 2 / 16f, chestFront, -1));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 14 / 16f, 15 / 16f, chestSide, -1));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 2 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestSide, -1));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestSide, -1));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 8 / 16f, 12 / 16f, 0 / 16f, 1 / 16f, chestFront, -1, ulow, uhigh, vlow, vhigh));
                        break;
                    case EAST:
                        ulow = new int[]{7, 8, 9, 8, 9, 7};
                        uhigh = new int[]{8, 9, 7, 9, 7, 8};
                        vlow = new int[]{6, 4, 4, 4, 4, 4};
                        vhigh = new int[]{4, 6, 8, 8, 8, 8};
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 2 / 16f, chestSide, -1));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 14 / 16f, 15 / 16f, chestSide, -1));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 2 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestSide, -1));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestFront, -1));
                        quads.addAll(ModelHelper.createCuboid(15 / 16f, 1f, 8 / 16f, 12 / 16f, 7 / 16f, 9 / 16f, chestFront, -1, ulow, uhigh, vlow, vhigh));
                        break;
                    case SOUTH:
                        ulow = new int[]{9, 9, 8, 9, 7, 9};
                        uhigh = new int[]{7, 7, 9, 7, 8, 7};
                        vlow = new int[]{4, 8, 4, 4, 4, 4};
                        vhigh = new int[]{5, 7, 8, 8, 8, 8};
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 2 / 16f, chestSide, -1));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 14 / 16f, 15 / 16f, chestFront, -1));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 2 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestSide, -1));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestSide, -1));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 8 / 16f, 12 / 16f, 15 / 16f, 1f, chestFront, -1, ulow, uhigh, vlow, vhigh));
                        break;
                    case WEST:
                        ulow = new int[]{7, 7, 9, 7, 9, 8};
                        uhigh = new int[]{8, 8, 7, 8, 7, 9};
                        vlow = new int[]{4, 6, 4, 4, 4, 4};
                        vhigh = new int[]{6, 4, 8, 8, 8, 8};
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 2 / 16f, chestSide, -1));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 14 / 16f, 15 / 16f, chestSide, -1));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 2 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestFront, -1));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, chestSide, -1));
                        quads.addAll(ModelHelper.createCuboid(0 / 16f, 1 / 16f, 8 / 16f, 12 / 16f, 7 / 16f, 9 / 16f, chestFront, -1, ulow, uhigh, vlow, vhigh));
                        break;
                }
            }
            return quads;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean func_230044_c_() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    @Nonnull
    public TextureAtlasSprite getParticleTexture() {
        return getTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }
}
//========SOLI DEO GLORIA========//