package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.FenceFrameBlock;
import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.GrassBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
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
 * See {@link ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.2 09/28/20
 */
public class WallBakedModel implements IDynamicBakedModel {

    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        if (mimic != null && !(mimic.getBlock() instanceof FrameBlock)) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            if (location != null && state != null) {
                IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                if (model != null) {
                    return getMimicQuads(state, side, rand, extraData, model);
                }
            }
        }
        return Collections.emptyList();
    }

    @Nonnull
    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, IModelData extraData, IBakedModel model) {
        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        Integer design = extraData.getData(FrameBlockTile.DESIGN);
        if (side != null) {
            return Collections.emptyList();
        }
        if (mimic != null && state != null) {
            int index = extraData.getData(FrameBlockTile.TEXTURE);
            List<TextureAtlasSprite> texture = TextureHelper.getTextureFromModel(model, extraData, rand);
            if (texture.size() <= index) {
                extraData.setData(FrameBlockTile.TEXTURE, 0);
                index = 0;
            }
            if (texture.size() == 0) {
                return Collections.emptyList();
            }
            int tintIndex = -1;
            if (mimic.getBlock() instanceof GrassBlock) {
                tintIndex = 1;
            }
            List<BakedQuad> quads = new ArrayList<>(ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 1f, 4 / 16f, 12 / 16f, texture.get(index), tintIndex));

            if (design == 0) {
                if (state.get(FenceFrameBlock.NORTH)) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 4 / 16f, 0f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, 14 / 16f, 0f, 4 / 16f, texture.get(index), tintIndex));
                }
                if (state.get(FenceFrameBlock.EAST)) {
                    quads.addAll(ModelHelper.createCuboid(10 / 16f, 1f, 0f, 4 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(10 / 16f, 1f, 10 / 16f, 14 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
                if (state.get(FenceFrameBlock.SOUTH)) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 4 / 16f, 10 / 16f, 1f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, 14 / 16f, 10 / 16f, 1f, texture.get(index), tintIndex));
                }
                if (state.get(FenceFrameBlock.WEST)) {
                    quads.addAll(ModelHelper.createCuboid(0f, 4 / 16f, 0f, 4 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 4 / 16f, 10 / 16f, 14 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
            }
            if (design == 1) {
                if (state.get(FenceFrameBlock.NORTH)) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 3 / 16f, 7 / 16f, 0f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, 14 / 16f, 0f, 4 / 16f, texture.get(index), tintIndex));
                }
                if (state.get(FenceFrameBlock.EAST)) {
                    quads.addAll(ModelHelper.createCuboid(10 / 16f, 1f, 3 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(10 / 16f, 1f, 10 / 16f, 14 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
                if (state.get(FenceFrameBlock.SOUTH)) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 3 / 16f, 7 / 16f, 10 / 16f, 1f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, 14 / 16f, 10 / 16f, 1f, texture.get(index), tintIndex));
                }
                if (state.get(FenceFrameBlock.WEST)) {
                    quads.addAll(ModelHelper.createCuboid(0f, 4 / 16f, 3 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 4 / 16f, 10 / 16f, 14 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
            }
            if (design == 2) {
                if (state.get(FenceFrameBlock.NORTH)) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 14 / 16f, 0f, 4 / 16f, texture.get(index), tintIndex));
                    //quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 9 / 16f, 12 / 16f, 0f, 4 / 16f, texture.get(index), tintIndex));
                }
                if (state.get(FenceFrameBlock.EAST)) {
                    quads.addAll(ModelHelper.createCuboid(10 / 16f, 1f, 0f, 14 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    //quads.addAll(ModelHelper.createCuboid(10 / 16f, 1f, 9 / 16f, 12 / 16f, 7 / 16f, 9 / 16f, texture.get(index), tintIndex));
                }
                if (state.get(FenceFrameBlock.SOUTH)) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 14 / 16f, 10 / 16f, 1f, texture.get(index), tintIndex));
                    //quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 9 / 16f, 12 / 16f, 10 / 16f, 1f, texture.get(index), tintIndex));
                }
                if (state.get(FenceFrameBlock.WEST)) {
                    quads.addAll(ModelHelper.createCuboid(0f, 4 / 16f, 0f, 14 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    //quads.addAll(ModelHelper.createCuboid(0f, 4 / 16f, 9 / 16f, 12 / 16f, 7 / 16f, 9 / 16f, texture.get(index), tintIndex));
                }
            }
            if (design == 3) {
                if (state.get(FenceFrameBlock.NORTH)) {
                    //Heart form
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 12 / 16f, 14 / 16f, 0f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 11 / 16f, 12 / 16f, 3 / 16f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 11 / 16f, 12 / 16f, 0f, 1 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 8 / 16f, 9 / 16f, 3 / 16f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 7 / 16f, 8 / 16f, 2 / 16f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 6 / 16f, 7 / 16f, 1 / 16f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 2 / 16f, 6 / 16f, 0f, 4 / 16f, texture.get(index), tintIndex));

                }
                if (state.get(FenceFrameBlock.EAST)) {
                    //Heart form
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 1f, 12 / 16f, 14 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(15 / 16f, 1f, 11 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 13 / 16f, 11 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 13 / 16f, 8 / 16f, 9 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 14 / 16f, 7 / 16f, 8 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 15 / 16f, 6 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 1f, 2 / 16f, 6 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
                if (state.get(FenceFrameBlock.SOUTH)) {
                    //Heart form
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 12 / 16f, 14 / 16f, 12 / 16f, 1f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 11 / 16f, 12 / 16f, 15 / 16f, 1f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 11 / 16f, 12 / 16f, 12 / 16f, 13 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 8 / 16f, 9 / 16f, 12 / 16f, 13 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 7 / 16f, 8 / 16f, 12 / 16f, 14 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 6 / 16f, 7 / 16f, 12 / 16f, 15 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 2 / 16f, 6 / 16f, 12 / 16f, 1f, texture.get(index), tintIndex));
                }
                if (state.get(FenceFrameBlock.WEST)) {
                    //Heart form
                    quads.addAll(ModelHelper.createCuboid(0f, 4 / 16f, 12 / 16f, 14 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 4 / 16f, 11 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 1 / 16f, 11 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 4 / 16f, 8 / 16f, 9 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(2 / 16f, 4 / 16f, 7 / 16f, 8 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(1 / 16f, 4 / 16f, 6 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 4 / 16f, 2 / 16f, 6 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
            }
            if (design == 4) {
                if (state.get(FenceFrameBlock.NORTH)) {
                    //Cross form
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 12 / 16f, 14 / 16f, 0f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, 12 / 16f, 1 / 16f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 8 / 16f, 10 / 16f, 3 / 16f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 2 / 16f, 8 / 16f, 1 / 16f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 2 / 16f, 0f, 4 / 16f, texture.get(index), tintIndex));
                }
                if (state.get(FenceFrameBlock.EAST)) {
                    //Cross form
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 1f, 12 / 16f, 14 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 15 / 16f, 10 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 13 / 16f, 8 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 15 / 16f, 2 / 16f, 8 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 1f, 0f, 2 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
                if (state.get(FenceFrameBlock.SOUTH)) {
                    //Cross form
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 12 / 16f, 14 / 16f, 12 / 16f, 1f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, 12 / 16f, 12 / 16f, 15 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 8 / 16f, 10 / 16f, 12 / 16f, 13 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 2 / 16f, 8 / 16f, 12 / 16f, 15 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 2 / 16f, 12 / 16f, 1f, texture.get(index), tintIndex));
                }
                if (state.get(FenceFrameBlock.WEST)) {
                    //Cross form
                    quads.addAll(ModelHelper.createCuboid(0f, 4 / 16f, 12 / 16f, 14 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(1 / 16f, 4 / 16f, 10 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 4 / 16f, 8 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(1 / 16f, 4 / 16f, 2 / 16f, 8 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 4 / 16f, 0f, 2 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
            }
            int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                quads.addAll(ModelHelper.createOverlay(4 / 16f, 12 / 16f, 0f, 1f, 4 / 16f, 12 / 16f, overlayIndex));
            }
            return quads;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
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
        return getTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }
}
//========SOLI DEO GLORIA========//