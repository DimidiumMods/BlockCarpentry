package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.FrameBlock;
import mod.pianomanu.blockcarpentry.block.PaneFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
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
 * @version 1.0 08/27/21
 */
public class PaneBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    @SuppressWarnings("deprecation")
    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {

        BlockState mimic = extraData.getData(FrameBlockTile.MIMIC);
        Integer design = extraData.getData(FrameBlockTile.DESIGN);
        Integer desTex = extraData.getData(FrameBlockTile.DESIGN_TEXTURE);
        if (side != null) {
            return Collections.emptyList();
        }
        if (mimic != null && !(mimic.getBlock() instanceof FrameBlock)) {
            ModelResourceLocation location = BlockModelShapes.getModelLocation(mimic);
            if (state != null) {
                IBakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                List<TextureAtlasSprite> textureList = TextureHelper.getTextureFromModel(model, extraData, rand);
                TextureAtlasSprite texture;
                Integer tex = extraData.getData(FrameBlockTile.TEXTURE);
                if (textureList.size() <= tex) {
                    extraData.setData(FrameBlockTile.TEXTURE, 0);
                    tex = 0;
                }
                if (textureList.size() == 0) {
                    if (Minecraft.getInstance().player != null) {
                        Minecraft.getInstance().player.sendStatusMessage(new TranslationTextComponent("message.blockcarpentry.block_not_available"), true);
                    }
                    for (int i = 0; i < 6; i++) {
                        textureList.add(Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(new ResourceLocation("missing")));
                    }
                    //return Collections.emptyList();
                }
                texture = textureList.get(tex);
                TextureAtlasSprite glass = TextureHelper.getGlassTextures().get(extraData.getData(FrameBlockTile.GLASS_COLOR));
                boolean north = state.get(PaneFrameBlock.NORTH);
                boolean east = state.get(PaneFrameBlock.EAST);
                boolean south = state.get(PaneFrameBlock.SOUTH);
                boolean west = state.get(PaneFrameBlock.WEST);
                boolean renderNorth = extraData.getData(FrameBlockTile.NORTH_VISIBLE);
                boolean renderEast = extraData.getData(FrameBlockTile.EAST_VISIBLE);
                boolean renderSouth = extraData.getData(FrameBlockTile.SOUTH_VISIBLE);
                boolean renderWest = extraData.getData(FrameBlockTile.WEST_VISIBLE);
                boolean renderUp = extraData.getData(FrameBlockTile.UP_VISIBLE);
                boolean renderDown = extraData.getData(FrameBlockTile.DOWN_VISIBLE);
                int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
                List<BakedQuad> quads = new ArrayList<>();
                if (design == 0) {
                    quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, !north, !south, !east, !west, renderUp, renderDown));
                    if (north)
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 1f, 0f, 7 / 16f, texture, tintIndex, renderNorth, false, true, true, true, true)); //TODO rendering top and bottom
                    if (east)
                        quads.addAll(ModelHelper.createCuboid(9 / 16f, 1f, 0f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, renderEast, false, true, true));
                    if (south)
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 1f, 9 / 16f, 1f, texture, tintIndex, false, renderSouth, true, true, true, true)); //TODO rendering top and bottom
                    if (west)
                        quads.addAll(ModelHelper.createCuboid(0f, 7 / 16f, 0f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, renderWest, true, true));
                } else if (design == 1) {
                    //Middle Post
                    quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, !north, !south, !east, !west, false, renderDown));
                    quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 2 / 16f, 14 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, true, true, false, false));
                    quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, !north, !south, !east, !west, renderUp, false));
                    if (north) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 2 / 16f, 7 / 16f, texture, tintIndex, false, false, true, true, true, true)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 2 / 16f, 7 / 16f, texture, tintIndex, false, false, true, true, true, true)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 0f, 2 / 16f, texture, tintIndex, renderNorth, false, true, true, false, renderDown));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 2 / 16f, 14 / 16f, 0f, 2 / 16f, texture, tintIndex, renderNorth, true, true, true, false, false));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 0f, 2 / 16f, texture, tintIndex, renderNorth, false, true, true, renderUp, false));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 2 / 16f, 14 / 16f, 2 / 16f, 7 / 16f, glass, -1, false, false, true, true, false, false));
                    }
                    if (east) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createCuboid(9 / 16f, 14 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, false, true, true));
                        quads.addAll(ModelHelper.createCuboid(9 / 16f, 14 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, false, true, true));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, renderEast, false, false, renderDown));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 2 / 16f, 14 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, renderEast, true, false, false));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, renderEast, false, renderUp, false));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(9 / 16f, 14 / 16f, 2 / 16f, 14 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, true, true, false, false, false, false));
                    }
                    if (south) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 9 / 16f, 14 / 16f, texture, tintIndex, false, false, true, true, true, true)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 9 / 16f, 14 / 16f, texture, tintIndex, false, false, true, true, true, true)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 14 / 16f, 1f, texture, tintIndex, false, renderSouth, true, true, false, renderDown));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 2 / 16f, 14 / 16f, 14 / 16f, 1f, texture, tintIndex, false, renderSouth, true, true, false, false));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 14 / 16f, 1f, texture, tintIndex, false, renderSouth, true, true, renderUp, false));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 2 / 16f, 14 / 16f, 9 / 16f, 14 / 16f, glass, -1, false, false, true, true, false, false));
                    }
                    if (west) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createCuboid(2 / 16f, 7 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, false, true, true));
                        quads.addAll(ModelHelper.createCuboid(2 / 16f, 7 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, false, true, true));
                        quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, renderWest, false, renderDown));
                        quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 2 / 16f, 14 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, true, renderWest, false, false));
                        quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, renderWest, renderUp, false));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(2 / 16f, 7 / 16f, 2 / 16f, 14 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, true, true, false, false, false, false));
                    }
                } else if (design == 2) {
                    //Middle Post
                    quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, !north, !south, !east, !west, true, renderDown));
                    quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 2 / 16f, 14 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, !north, !south, !east, !west, false, false));
                    quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, !north, !south, !east, !west, renderUp, true));
                    if (north) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 0f, 7 / 16f, texture, tintIndex, renderNorth, false, true, true, true, true)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 0f, 7 / 16f, texture, tintIndex, renderNorth, false, true, true, true, true)); //TODO rendering top and bottom
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 2 / 16f, 14 / 16f, 0f, 7.5f / 16f, glass, -1, renderNorth, false, true, true, false, false));
                    }
                    if (east) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createCuboid(9 / 16f, 1f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, renderEast, false, true, true));
                        quads.addAll(ModelHelper.createCuboid(9 / 16f, 1f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, renderEast, false, true, true));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(8.5f / 16f, 1f, 2 / 16f, 14 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, true, true, renderEast, false, false, false));
                    }
                    if (south) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 9 / 16f, 1f, texture, tintIndex, false, renderSouth, true, true, true, true)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 9 / 16f, 1f, texture, tintIndex, false, renderSouth, true, true, true, true)); //TODO rendering top and bottom
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 2 / 16f, 14 / 16f, 8.5f / 16f, 1f, glass, -1, false, renderSouth, true, true, false, false));
                    }
                    if (west) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createCuboid(0f, 7 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, renderWest, true, true));
                        quads.addAll(ModelHelper.createCuboid(0f, 7 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, renderWest, true, true));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(0f, 7.5f / 16f, 2 / 16f, 14 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, true, true, false, false, false, false));
                    }
                } else if (design == 3) {
                    //Middle Post
                    quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, !north, !south, !east, !west, true, renderDown));
                    quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 2 / 16f, 14 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, !north, !south, !east, !west, false, false));
                    quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, !north, !south, !east, !west, renderUp, true));
                    if (north) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 2 / 16f, 7 / 16f, texture, tintIndex, false, false, true, true, true, true)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 2 / 16f, 7 / 16f, texture, tintIndex, false, false, true, true, true, true)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 0f, 2 / 16f, texture, tintIndex, renderNorth, false, true, true, false, renderDown));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 2 / 16f, 14 / 16f, 0f, 2 / 16f, texture, tintIndex, renderNorth, true, true, true, false, false));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 0f, 2 / 16f, texture, tintIndex, renderNorth, false, true, true, renderUp, false));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 2 / 16f, 14 / 16f, 2 / 16f, 7.5f / 16f, glass, -1, false, false, true, true, false, false));
                    }
                    if (east) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createCuboid(9 / 16f, 14 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, false, true, true));
                        quads.addAll(ModelHelper.createCuboid(9 / 16f, 14 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, false, true, true));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, renderEast, false, false, renderDown));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 2 / 16f, 14 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, renderEast, true, false, false));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, renderEast, false, renderUp, false));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(8.5f / 16f, 14 / 16f, 2 / 16f, 14 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, true, true, false, false, false, false));
                    }
                    if (south) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 9 / 16f, 14 / 16f, texture, tintIndex, false, false, true, true, true, true)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 9 / 16f, 14 / 16f, texture, tintIndex, false, false, true, true, true, true)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 2 / 16f, 14 / 16f, 1f, texture, tintIndex, false, renderSouth, true, true, false, renderDown));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 2 / 16f, 14 / 16f, 14 / 16f, 1f, texture, tintIndex, false, renderSouth, true, true, false, false));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 14 / 16f, 1f, 14 / 16f, 1f, texture, tintIndex, false, renderSouth, true, true, renderUp, false));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 2 / 16f, 14 / 16f, 8.5f / 16f, 14 / 16f, glass, -1, false, false, true, true, false, false));
                    }
                    if (west) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createCuboid(2 / 16f, 7 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, false, true, true));
                        quads.addAll(ModelHelper.createCuboid(2 / 16f, 7 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, false, true, true));
                        quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 0f, 2 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, renderWest, false, renderDown));
                        quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 2 / 16f, 14 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, true, renderWest, false, false));
                        quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 14 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, renderWest, renderUp, false));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(2 / 16f, 7.5f / 16f, 2 / 16f, 14 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, true, true, false, false, false, false));
                    }
                } else if (design == 4) {
                    //Middle Post
                    quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 1 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, !north, !south, !east, !west, true, renderDown));
                    quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 1 / 16f, 15 / 16f, 7f / 16f, 9f / 16f, texture, tintIndex, true, true, true, true, false, false));
                    quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 15 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, !north, !south, !east, !west, renderUp, true));
                    if (north) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 1 / 16f, 1 / 16f, 7 / 16f, texture, tintIndex, false, false, true, true, true, true)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 15 / 16f, 1f, 1 / 16f, 7 / 16f, texture, tintIndex, false, false, true, true, true, true)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 1 / 16f, 0f, 1 / 16f, texture, tintIndex, renderNorth, false, true, true, false, renderDown));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 1 / 16f, 15 / 16f, 0f, 1 / 16f, texture, tintIndex, renderNorth, true, true, true, false, false));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 15 / 16f, 1f, 0f, 1 / 16f, texture, tintIndex, renderNorth, false, true, true, renderUp, false));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, 1 / 16f, 7 / 16f, texture, tintIndex, false, false, true, true, true, true));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 1 / 16f, 15 / 16f, 1 / 16f, 7 / 16f, glass, -1, false, false, true, true, false, false));
                    }
                    if (east) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createCuboid(9 / 16f, 15 / 16f, 0f, 1 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, false, true, true));
                        quads.addAll(ModelHelper.createCuboid(9 / 16f, 15 / 16f, 15 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, false, true, true));
                        quads.addAll(ModelHelper.createCuboid(15 / 16f, 1f, 0f, 1 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, renderEast, false, false, renderDown));
                        quads.addAll(ModelHelper.createCuboid(15 / 16f, 1f, 1 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, renderEast, true, false, false));
                        quads.addAll(ModelHelper.createCuboid(15 / 16f, 1f, 15 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, renderEast, false, renderUp, false));
                        quads.addAll(ModelHelper.createCuboid(9 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, false, true, true));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(9 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, true, true, false, false, false, false));
                    }
                    if (south) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 1 / 16f, 9 / 16f, 15 / 16f, texture, tintIndex, false, false, true, true, true, true)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 15 / 16f, 1f, 9 / 16f, 15 / 16f, texture, tintIndex, false, false, true, true, true, true)); //TODO rendering top and bottom
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 0f, 1 / 16f, 15 / 16f, 1f, texture, tintIndex, false, renderSouth, true, true, false, renderDown));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 1 / 16f, 15 / 16f, 15 / 16f, 1f, texture, tintIndex, false, renderSouth, true, true, false, false));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 15 / 16f, 1f, 15 / 16f, 1f, texture, tintIndex, false, renderSouth, true, true, renderUp, false));
                        quads.addAll(ModelHelper.createCuboid(7 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, 9 / 16f, 15 / 16f, texture, tintIndex, false, false, true, true, true, true));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(7.5f / 16f, 8.5f / 16f, 1 / 16f, 15 / 16f, 9 / 16f, 15 / 16f, glass, -1, false, false, true, true, false, false));
                    }
                    if (west) {
                        //Inner Pane Frame
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 7 / 16f, 0f, 1 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, false, true, true));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 7 / 16f, 15 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, false, true, true));
                        quads.addAll(ModelHelper.createCuboid(0f, 1 / 16f, 0f, 1 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, renderWest, false, renderDown));
                        quads.addAll(ModelHelper.createCuboid(0f, 1 / 16f, 1 / 16f, 15 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, true, renderWest, false, false));
                        quads.addAll(ModelHelper.createCuboid(0f, 1 / 16f, 15 / 16f, 1f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, renderWest, renderUp, false));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 7 / 16f, 7 / 16f, 9 / 16f, 7 / 16f, 9 / 16f, texture, tintIndex, true, true, false, false, true, true));
                        //Inner Pane
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 7 / 16f, 1 / 16f, 15 / 16f, 7.5f / 16f, 8.5f / 16f, glass, -1, true, true, false, false, false, false));
                    }
                }

                int overlayIndex = extraData.getData(FrameBlockTile.OVERLAY);
                if (overlayIndex != 0) {
                    //TODO fix overlay for transparent blocks - then also use transparent overlay
                    quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, 1f, overlayIndex, true, true, renderEast, renderWest, renderUp, renderDown, true));
                }
                return quads;
            }
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
    @Nonnull
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }
}
//========SOLI DEO GLORIA========//