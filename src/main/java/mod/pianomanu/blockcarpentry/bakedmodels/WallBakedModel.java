package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.WallFrameBlock;
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
import net.minecraft.world.level.block.state.properties.WallSide;
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
 * @version 1.2 09/18/23
 */
public class WallBakedModel implements IDynamicBakedModel {

    public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "block/oak_planks");

    private TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(TEXTURE);
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, RenderType renderType) {
        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            if (state != null) {
                BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
                return getMimicQuads(state, side, rand, extraData, model);
            }
        }
        return Collections.emptyList();
    }

    @Nonnull
    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, ModelData extraData, BakedModel model) {
        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
        Integer design = extraData.get(FrameBlockTile.DESIGN);
        if (side != null) {
            return Collections.emptyList();
        }
        if (mimic != null && state != null) {
            int index = extraData.get(FrameBlockTile.TEXTURE);
            List<TextureAtlasSprite> texture = TextureHelper.getTextureFromModel(model, extraData, rand);
            if (texture.size() <= index) {
                extraData.derive().with(FrameBlockTile.TEXTURE, 0);
                index = 0;
            }
            if (texture.size() == 0) {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.displayClientMessage(Component.translatable("message.blockcarpentry.block_not_available"), true);
                }
                return Collections.emptyList();
            }
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            List<BakedQuad> quads = new ArrayList<>();

            WallSide north = state.getValue(WallFrameBlock.NORTH_WALL);
            WallSide east = state.getValue(WallFrameBlock.EAST_WALL);
            WallSide south = state.getValue(WallFrameBlock.SOUTH_WALL);
            WallSide west = state.getValue(WallFrameBlock.WEST_WALL);

            boolean isThickPost = state.getValue(WallFrameBlock.UP);
            //determine wall height - if block above this block is also a wall with connections, the wall height of this block right here needs to be a full block - otherwise just 14/16
            float height_north = 1f;
            float height_east = 1f;
            float height_south = 1f;
            float height_west = 1f;
            float height_post = 1f;
            if (north != WallSide.TALL)
                height_north = 14 / 16f;
            if (east != WallSide.TALL)
                height_east = 14 / 16f;
            if (south != WallSide.TALL)
                height_south = 14 / 16f;
            if (west != WallSide.TALL)
                height_west = 14 / 16f;

            boolean lowNorth = height_north == 14 / 16f;
            boolean lowEast = height_east == 14 / 16f;
            boolean lowSouth = height_south == 14 / 16f;
            boolean lowWest = height_west == 14 / 16f;

            boolean tallNorth = height_north == 1;
            boolean tallEast = height_east == 1;
            boolean tallSouth = height_south == 1;
            boolean tallWest = height_west == 1;
            boolean somethingOnTop = tallNorth || tallEast || tallSouth || tallWest;

            boolean lowMiddle = lowNorth && lowEast && lowSouth && lowWest;
            if (lowMiddle)
                height_post = 14 / 16f;

            boolean renderNorth = north == WallSide.NONE;
            boolean renderEast = east == WallSide.NONE;
            boolean renderSouth = south == WallSide.NONE;
            boolean renderWest = west == WallSide.NONE;

            //Create thin middle post
            if (!isThickPost) {
                quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, height_post, 5 / 16f, 11 / 16f, texture.get(index), tintIndex, renderNorth, renderSouth, renderEast, renderWest, !somethingOnTop, true));
            }
            // Create thick middle post
            else {
                quads.addAll(ModelHelper.createCuboid(4 / 16f, 12 / 16f, 0f, 1, 4 / 16f, 12 / 16f, texture.get(index), tintIndex, true, true, true, true, true, true));
            }

            //classic wall design
            if (design == 0) {
                if (north == WallSide.LOW || north == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, height_north, 0f, 5 / 16f, texture.get(index), tintIndex, true, false, true, true, lowNorth, true));
                }
                if (east == WallSide.LOW || east == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 1f, 0f, height_east, 5 / 16f, 11 / 16f, texture.get(index), tintIndex, true, true, true, false, lowEast, true));
                }
                if (south == WallSide.LOW || south == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, height_south, 11 / 16f, 1f, texture.get(index), tintIndex, false, true, true, true, lowSouth, true));
                }
                if (west == WallSide.LOW || west == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(0f, 5 / 16f, 0f, height_west, 5 / 16f, 11 / 16f, texture.get(index), tintIndex, true, true, false, true, lowWest, true));
                }
            }
            //wall with hole
            if (design == 1) {
                if (north == WallSide.LOW || north == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 4 / 16f, 0f, 5 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, height_north, 0f, 5 / 16f, texture.get(index), tintIndex));
                }
                if (east == WallSide.LOW || east == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 1f, 0f, 4 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 1f, 10 / 16f, height_east, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
                if (south == WallSide.LOW || south == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 4 / 16f, 11 / 16f, 1f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, height_south, 11 / 16f, 1f, texture.get(index), tintIndex));
                }
                if (west == WallSide.LOW || west == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(0f, 5 / 16f, 0f, 4 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 5 / 16f, 10 / 16f, height_west, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
            }
            //fence-like design
            if (design == 2) {
                if (north == WallSide.LOW || north == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 3 / 16f, 7 / 16f, 0f, 5 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, height_north, 0f, 5 / 16f, texture.get(index), tintIndex));
                }
                if (east == WallSide.LOW || east == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 1f, 3 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 1f, 10 / 16f, height_east, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
                if (south == WallSide.LOW || south == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 3 / 16f, 7 / 16f, 11 / 16f, 1f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, height_south, 11 / 16f, 1f, texture.get(index), tintIndex));
                }
                if (west == WallSide.LOW || west == WallSide.TALL) {
                    quads.addAll(ModelHelper.createCuboid(0f, 5 / 16f, 3 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 5 / 16f, 10 / 16f, height_west, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
            }
            //heart shaped holes in wall
            if (design == 3) {
                if (north == WallSide.LOW || north == WallSide.TALL) {
                    //Heart form
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 12 / 16f, height_north, 0f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 11 / 16f, 12 / 16f, 3 / 16f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 11 / 16f, 12 / 16f, 0f, 1 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 8 / 16f, 9 / 16f, 3 / 16f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 7 / 16f, 8 / 16f, 2 / 16f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 6 / 16f, 7 / 16f, 1 / 16f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 6 / 16f, 0f, 4 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, height_north, 4 / 16f, 5 / 16f, texture.get(index), tintIndex));

                }
                if (east == WallSide.LOW || east == WallSide.TALL) {
                    //Heart form
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 1f, 12 / 16f, height_east, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(15 / 16f, 1f, 11 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 13 / 16f, 11 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 13 / 16f, 8 / 16f, 9 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 14 / 16f, 7 / 16f, 8 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 15 / 16f, 6 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(12 / 16f, 1f, 0f, 6 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 12 / 16f, 0f, height_east, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
                if (south == WallSide.LOW || south == WallSide.TALL) {
                    //Heart form
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 12 / 16f, height_south, 12 / 16f, 1f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 11 / 16f, 12 / 16f, 15 / 16f, 1f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 11 / 16f, 12 / 16f, 12 / 16f, 13 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 8 / 16f, 9 / 16f, 12 / 16f, 13 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 7 / 16f, 8 / 16f, 12 / 16f, 14 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 6 / 16f, 7 / 16f, 12 / 16f, 15 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 6 / 16f, 12 / 16f, 1f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, height_south, 11 / 16f, 12 / 16f, texture.get(index), tintIndex));
                }
                if (west == WallSide.LOW || west == WallSide.TALL) {
                    //Heart form
                    quads.addAll(ModelHelper.createCuboid(0f, 4 / 16f, 12 / 16f, height_west, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 4 / 16f, 11 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 1 / 16f, 11 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 4 / 16f, 8 / 16f, 9 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(2 / 16f, 4 / 16f, 7 / 16f, 8 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(1 / 16f, 4 / 16f, 6 / 16f, 7 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 4 / 16f, 0f, 6 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(4 / 16f, 5 / 16f, 0f, height_west, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
            }
            //cross shaped holes in wall
            if (design == 4) {
                if (north == WallSide.LOW || north == WallSide.TALL) {
                    //Cross form
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 12 / 16f, height_north, 0f, 5 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, 12 / 16f, 1 / 16f, 5 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 8 / 16f, 10 / 16f, 3 / 16f, 5 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 2 / 16f, 8 / 16f, 1 / 16f, 5 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 2 / 16f, 0f, 5 / 16f, texture.get(index), tintIndex));
                }
                if (east == WallSide.LOW || east == WallSide.TALL) {
                    //Cross form
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 1f, 12 / 16f, height_east, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 15 / 16f, 10 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 13 / 16f, 8 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 15 / 16f, 2 / 16f, 8 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(11 / 16f, 1f, 0f, 2 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
                if (south == WallSide.LOW || south == WallSide.TALL) {
                    //Cross form
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 12 / 16f, height_south, 11 / 16f, 1f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 10 / 16f, 12 / 16f, 11 / 16f, 15 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 8 / 16f, 10 / 16f, 11 / 16f, 13 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 2 / 16f, 8 / 16f, 11 / 16f, 15 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(5 / 16f, 11 / 16f, 0f, 2 / 16f, 11 / 16f, 1f, texture.get(index), tintIndex));
                }
                if (west == WallSide.LOW || west == WallSide.TALL) {
                    //Cross form
                    quads.addAll(ModelHelper.createCuboid(0f, 5 / 16f, 12 / 16f, height_west, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(1 / 16f, 5 / 16f, 10 / 16f, 12 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(3 / 16f, 5 / 16f, 8 / 16f, 10 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(1 / 16f, 5 / 16f, 2 / 16f, 8 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                    quads.addAll(ModelHelper.createCuboid(0f, 5 / 16f, 0f, 2 / 16f, 5 / 16f, 11 / 16f, texture.get(index), tintIndex));
                }
            }
            int overlayIndex = extraData.get(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                //Create thin middle post
                if (!isThickPost) {
                    quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, 0f, height_post, 5 / 16f, 11 / 16f, overlayIndex, renderNorth, renderSouth, renderEast, renderWest, !somethingOnTop, true, true));
                }
                // Create thick middle post
                else {
                    quads.addAll(ModelHelper.createOverlay(4 / 16f, 12 / 16f, 0f, 1, 4 / 16f, 12 / 16f, overlayIndex, true, true, true, true, true, true, true));
                }

                if (north == WallSide.LOW || north == WallSide.TALL) {
                    quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, 0f, height_north, 0f, 5 / 16f, overlayIndex, true, false, true, true, lowNorth, true, true));
                }
                if (east == WallSide.LOW || east == WallSide.TALL) {
                    quads.addAll(ModelHelper.createOverlay(11 / 16f, 1f, 0f, height_east, 5 / 16f, 11 / 16f, overlayIndex, true, true, true, false, lowEast, true, true));
                }
                if (south == WallSide.LOW || south == WallSide.TALL) {
                    quads.addAll(ModelHelper.createOverlay(5 / 16f, 11 / 16f, 0f, height_south, 11 / 16f, 1f, overlayIndex, false, true, true, true, lowSouth, true, true));
                }
                if (west == WallSide.LOW || west == WallSide.TALL) {
                    quads.addAll(ModelHelper.createOverlay(0f, 5 / 16f, 0f, height_west, 5 / 16f, 11 / 16f, overlayIndex, true, true, false, true, lowWest, true, true));
                }
            }
            return quads;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
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