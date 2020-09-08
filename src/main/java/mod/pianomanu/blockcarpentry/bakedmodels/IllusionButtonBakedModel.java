package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrassBlock;
import net.minecraft.block.WoodButtonBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
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
 * See {@link mod.pianomanu.blockcarpentry.util.ModelHelper} for more information
 * @author PianoManu
 * @version 1.0 09/08/20
 */
public class IllusionButtonBakedModel implements IDynamicBakedModel {
    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null && !(mimic.getBlock() instanceof FrameBlock)) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            if (location != null) {
                IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                if (model != null) {
                    return getIllusionQuads(state, side, rand, extraData, model);
                }
            }
        }
        return Collections.emptyList();
    }

    private List<BakedQuad> getIllusionQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData, IBakedModel model) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null && state != null) {
            List<TextureAtlasSprite> textureList = TextureHelper.getTextureListFromBlock(mimic.getBlock());
            TextureAtlasSprite texture;
            int tex = extraData.getData(FrameBlockTile.TEXTURE);
            if (textureList.size() > tex) {
                texture = textureList.get(tex);
            } else {
                texture = textureList.get(0);
            }
            int tintIndex = -1;
            if (mimic.getBlock() instanceof GrassBlock) {
                tintIndex = 1;
            }
            switch (state.get(WoodButtonBlock.FACE)) {
                case FLOOR:
                    switch (state.get(WoodButtonBlock.HORIZONTAL_FACING)) {
                        case EAST:
                        case WEST:
                            return new ArrayList<>(ModelHelper.createSixFaceCuboid(6 / 16f, 10 / 16f, 0f, 2 / 16f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex));
                        case NORTH:
                        case SOUTH:
                            return new ArrayList<>(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 0f, 2 / 16f, 6 / 16f, 10 / 16f, mimic, model, extraData, rand, tintIndex));
                    }
                case CEILING:
                    switch (state.get(WoodButtonBlock.HORIZONTAL_FACING)) {
                        case EAST:
                        case WEST:
                            return new ArrayList<>(ModelHelper.createSixFaceCuboid(6 / 16f, 10 / 16f, 14 / 16f, 1f, 5 / 16f, 11 / 16f, mimic, model, extraData, rand, tintIndex));
                        case NORTH:
                        case SOUTH:
                            return new ArrayList<>(ModelHelper.createSixFaceCuboid(5 / 16f, 11 / 16f, 14 / 16f, 1f, 6 / 16f, 10 / 16f, mimic, model, extraData, rand, tintIndex));
                    }
                case WALL:
                    switch (state.get(WoodButtonBlock.HORIZONTAL_FACING)) {
                        case EAST:
                            return new ArrayList<>(ModelHelper.createSixFaceCuboid(0f, 2/16f, 6/16f, 10/16f, 5/16f, 11/16f, mimic,model,extraData,rand, tintIndex));
                        case WEST:
                            return new ArrayList<>(ModelHelper.createSixFaceCuboid(14/16f, 1f, 6/16f, 10/16f, 5/16f, 11/16f, mimic,model,extraData,rand, tintIndex));
                        case NORTH:
                            return new ArrayList<>(ModelHelper.createSixFaceCuboid(5/16f, 11/16f, 6/16f, 10/16f, 14/16f, 1f, mimic,model,extraData,rand, tintIndex));
                        case SOUTH:
                            return new ArrayList<>(ModelHelper.createSixFaceCuboid(5/16f, 11/16f, 6/16f, 10/16f, 0f, 2/16f, mimic,model,extraData,rand, tintIndex));
                    }
            }

            return new ArrayList<>(ModelHelper.createCuboid(0f, 1f, 0f, 0.5f, 0f, 1f, texture, tintIndex));
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
    public TextureAtlasSprite getParticleTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("minecraft", "block/oak_planks"));
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