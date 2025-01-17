package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.PaneFrameBlock;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.ModelHelper;
import mod.pianomanu.blockcarpentry.util.TextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains all information for the block model
 * See {@link ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.2 11/07/22
 */
public class PaneBakedModel implements IDynamicBakedModel {
    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, RenderType renderType) {

        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
        Integer design = extraData.get(FrameBlockTile.DESIGN);
        if (side != null) {
            return Collections.emptyList();
        }
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            if (state != null) {
                BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                List<TextureAtlasSprite> textureList = TextureHelper.getTextureFromModel(model, extraData, rand);
                TextureAtlasSprite texture;
                Integer tex = extraData.get(FrameBlockTile.TEXTURE);
                if (textureList.size() <= tex) {
                    extraData.derive().with(FrameBlockTile.TEXTURE, 0);
                    tex = 0;
                }
                if (textureList.size() == 0) {
                    if (Minecraft.getInstance().player != null) {
                        Minecraft.getInstance().player.displayClientMessage(Component.translatable("message.blockcarpentry.block_not_available"), true);
                    }
                    for (int i = 0; i < 6; i++) {
                        textureList.add(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("missing")));
                    }
                    //return Collections.emptyList();
                }
                texture = textureList.get(tex);
                TextureAtlasSprite glass = TextureHelper.getGlassTextures().get(extraData.get(FrameBlockTile.GLASS_COLOR));
                boolean north = state.getValue(PaneFrameBlock.NORTH);
                boolean east = state.getValue(PaneFrameBlock.EAST);
                boolean south = state.getValue(PaneFrameBlock.SOUTH);
                boolean west = state.getValue(PaneFrameBlock.WEST);
                boolean renderNorth = extraData.get(FrameBlockTile.NORTH_VISIBLE);
                boolean renderEast = extraData.get(FrameBlockTile.EAST_VISIBLE);
                boolean renderSouth = extraData.get(FrameBlockTile.SOUTH_VISIBLE);
                boolean renderWest = extraData.get(FrameBlockTile.WEST_VISIBLE);
                boolean renderUp = extraData.get(FrameBlockTile.UP_VISIBLE);
                boolean renderDown = extraData.get(FrameBlockTile.DOWN_VISIBLE);
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

                int overlayIndex = extraData.get(FrameBlockTile.OVERLAY);
                if (overlayIndex != 0) {
                    //TODO fix overlay for transparent blocks - then also use transparent overlay
                    quads.addAll(ModelHelper.createOverlay(7 / 16f, 9 / 16f, 0f, 1f, 7 / 16f, 9 / 16f, overlayIndex, !north, !south, !east, !west, renderUp, renderDown, true));
                    if (north)
                        quads.addAll(ModelHelper.createOverlay(7 / 16f, 9 / 16f, 0f, 1f, 0f, 7 / 16f, overlayIndex, renderNorth, false, true, true, renderUp, renderDown, true));
                    if (east)
                        quads.addAll(ModelHelper.createOverlay(9 / 16f, 1f, 0f, 1f, 7 / 16f, 9 / 16f, overlayIndex, true, true, renderEast, false, renderUp, renderDown, true));
                    if (south)
                        quads.addAll(ModelHelper.createOverlay(7 / 16f, 9 / 16f, 0f, 1f, 9 / 16f, 1f, overlayIndex, false, renderSouth, true, true, renderUp, renderDown, true));
                    if (west)
                        quads.addAll(ModelHelper.createOverlay(0f, 7 / 16f, 0f, 1f, 7 / 16f, 9 / 16f, overlayIndex, true, true, false, renderWest, renderUp, renderDown, true));
                }
                return quads;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    @Nonnull
    public TextureAtlasSprite getParticleIcon() {
        return getTexture();
    }

    @Override
    @Nonnull
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    @NotNull
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return ChunkRenderTypeSet.of(RenderType.translucent());
    }
}
//========SOLI DEO GLORIA========//