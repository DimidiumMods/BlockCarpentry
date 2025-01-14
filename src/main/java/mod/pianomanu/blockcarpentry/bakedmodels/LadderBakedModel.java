package mod.pianomanu.blockcarpentry.bakedmodels;

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
import net.minecraft.world.level.block.LadderBlock;
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
public class LadderBakedModel implements IDynamicBakedModel {
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
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            return getMimicQuads(state, side, rand, extraData, model);
        }

        return Collections.emptyList();
    }

    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, BakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
        int tex = extraData.get(FrameBlockTile.TEXTURE);
        if (mimic != null && state != null) {
            List<TextureAtlasSprite> textureList = TextureHelper.getTextureFromModel(model, extraData, rand);
            List<TextureAtlasSprite> designTextureList = new ArrayList<>();
            if (textureList.size() == 0) {
                if (Minecraft.getInstance().player != null) {
                    Minecraft.getInstance().player.displayClientMessage(Component.translatable("message.blockcarpentry.block_not_available"), true);
                }
                return Collections.emptyList();
            }
            designTextureList.add(textureList.get(0));
            designTextureList.addAll(TextureHelper.getMetalTextures());
            TextureAtlasSprite texture;
            if (textureList.size() <= tex) {
                extraData.derive().with(FrameBlockTile.TEXTURE, 0);
                tex = 0;
            }
            texture = textureList.get(tex);
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            int design = extraData.get(FrameBlockTile.DESIGN);
            int desTex = extraData.get(FrameBlockTile.DESIGN_TEXTURE);
            TextureAtlasSprite designTexture = designTextureList.get(desTex);
            List<BakedQuad> quads = new ArrayList<>();
            if (design == 5) { //do we use that? I don't really like it
                switch (state.getValue(LadderBlock.FACING)) {
                    case WEST:
                        return new ArrayList<>(ModelHelper.createCuboid(13 / 16f, 1f, 0f, 1f, 0f, 1f, texture, tintIndex));
                    case SOUTH:
                        return new ArrayList<>(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 0f, 3 / 16f, texture, tintIndex));
                    case NORTH:
                        return new ArrayList<>(ModelHelper.createCuboid(0f, 1f, 0f, 1f, 13 / 16f, 1f, texture, tintIndex));
                    case EAST:
                        return new ArrayList<>(ModelHelper.createCuboid(0f, 3 / 16f, 0f, 1f, 0f, 1f, texture, tintIndex));
                }
            }
            if (design == 0 || design == 1 || design == 2) {
                switch (state.getValue(LadderBlock.FACING)) {
                    case WEST -> {
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 0f, 1f, 0f, 1 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 0f, 1f, 15 / 16f, 1f, texture, tintIndex));
                    }
                    case SOUTH -> {
                        quads.addAll(ModelHelper.createCuboid(0f, 1 / 16f, 0f, 1f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(15 / 16f, 1f, 0f, 1f, 0f, 3 / 16f, texture, tintIndex));
                    }
                    case NORTH -> {
                        quads.addAll(ModelHelper.createCuboid(0f, 1 / 16f, 0f, 1f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(15 / 16f, 1f, 0f, 1f, 13 / 16f, 1f, texture, tintIndex));
                    }
                    case EAST -> {
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 0f, 1f, 0f, 1 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 0f, 1f, 15 / 16f, 1f, texture, tintIndex));
                    }
                }
            }
            if (design == 0 || design == 1) {
                switch (state.getValue(LadderBlock.FACING)) {
                    case WEST -> {
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 2 / 16f, 3 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 6 / 16f, 7 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 10 / 16f, 11 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 14 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                    }
                    case SOUTH -> {
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 2 / 16f, 3 / 16f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 6 / 16f, 7 / 16f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 10 / 16f, 11 / 16f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 14 / 16f, 15 / 16f, 0f, 3 / 16f, texture, tintIndex));
                    }
                    case NORTH -> {
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 2 / 16f, 3 / 16f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 6 / 16f, 7 / 16f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 10 / 16f, 11 / 16f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 14 / 16f, 15 / 16f, 13 / 16f, 1f, texture, tintIndex));
                    }
                    case EAST -> {
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 2 / 16f, 3 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 6 / 16f, 7 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 10 / 16f, 11 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 14 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                    }
                }
            }
            if (design == 1) {
                switch (state.getValue(LadderBlock.FACING)) {
                    case WEST -> {
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 0 / 16f, 1 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 4 / 16f, 5 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 8 / 16f, 9 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 12 / 16f, 13 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                    }
                    case SOUTH -> {
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 0 / 16f, 1 / 16f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 4 / 16f, 5 / 16f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 8 / 16f, 9 / 16f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 12 / 16f, 13 / 16f, 0f, 3 / 16f, texture, tintIndex));
                    }
                    case NORTH -> {
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 0 / 16f, 1 / 16f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 4 / 16f, 5 / 16f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 8 / 16f, 9 / 16f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 12 / 16f, 13 / 16f, 13 / 16f, 1f, texture, tintIndex));
                    }
                    case EAST -> {
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 0 / 16f, 1 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 4 / 16f, 5 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 8 / 16f, 9 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 12 / 16f, 13 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                    }
                }
            }
            if (design == 2) {
                switch (state.getValue(LadderBlock.FACING)) {
                    case WEST -> {
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 1 / 16f, 3 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 5 / 16f, 7 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 9 / 16f, 11 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 13 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                    }
                    case SOUTH -> {
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 3 / 16f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 5 / 16f, 7 / 16f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 9 / 16f, 11 / 16f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 13 / 16f, 15 / 16f, 0f, 3 / 16f, texture, tintIndex));
                    }
                    case NORTH -> {
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 1 / 16f, 3 / 16f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 5 / 16f, 7 / 16f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 9 / 16f, 11 / 16f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(1 / 16f, 15 / 16f, 13 / 16f, 15 / 16f, 13 / 16f, 1f, texture, tintIndex));
                    }
                    case EAST -> {
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 1 / 16f, 3 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 5 / 16f, 7 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 9 / 16f, 11 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 13 / 16f, 15 / 16f, 1 / 16f, 15 / 16f, texture, tintIndex));
                    }
                }
            }
            if (design == 3) {
                switch (state.getValue(LadderBlock.FACING)) {
                    case WEST -> {
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 1 / 16f, 3 / 16f, 0f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 5 / 16f, 7 / 16f, 0f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 9 / 16f, 11 / 16f, 0f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 1f, 13 / 16f, 15 / 16f, 0f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 0f, 1f, 2 / 16f, 4 / 16f, designTexture, -1));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 0f, 1f, 12 / 16f, 14 / 16f, designTexture, -1));
                    }
                    case SOUTH -> {
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 1 / 16f, 3 / 16f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 7 / 16f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 9 / 16f, 11 / 16f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 13 / 16f, 15 / 16f, 0f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(2 / 16f, 4 / 16f, 0f, 1f, 0f, 2 / 16f, designTexture, -1));
                        quads.addAll(ModelHelper.createCuboid(12 / 16f, 14 / 16f, 0f, 1f, 0f, 2 / 16f, designTexture, -1));
                    }
                    case NORTH -> {
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 1 / 16f, 3 / 16f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 7 / 16f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 9 / 16f, 11 / 16f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 13 / 16f, 15 / 16f, 13 / 16f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(2 / 16f, 4 / 16f, 0f, 1f, 14 / 16f, 1f, designTexture, -1));
                        quads.addAll(ModelHelper.createCuboid(12 / 16f, 14 / 16f, 0f, 1f, 14 / 16f, 1f, designTexture, -1));
                    }
                    case EAST -> {
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 1 / 16f, 3 / 16f, 0f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 5 / 16f, 7 / 16f, 0f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 9 / 16f, 11 / 16f, 0f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 3 / 16f, 13 / 16f, 15 / 16f, 0f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 0f, 1f, 2 / 16f, 4 / 16f, designTexture, -1));
                        quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 0f, 1f, 12 / 16f, 14 / 16f, designTexture, -1));
                    }
                }
            }
            if (design == 4) {
                switch (state.getValue(LadderBlock.FACING)) {
                    case WEST -> {
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 14 / 16f, 1 / 16f, 3 / 16f, 0f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 14 / 16f, 5 / 16f, 7 / 16f, 0f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 14 / 16f, 9 / 16f, 11 / 16f, 0f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(13 / 16f, 14 / 16f, 13 / 16f, 15 / 16f, 0f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 0f, 1f, 2 / 16f, 4 / 16f, designTexture, -1));
                        quads.addAll(ModelHelper.createCuboid(14 / 16f, 1f, 0f, 1f, 12 / 16f, 14 / 16f, designTexture, -1));
                    }
                    case SOUTH -> {
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 1 / 16f, 3 / 16f, 2 / 16f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 7 / 16f, 2 / 16f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 9 / 16f, 11 / 16f, 2 / 16f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 13 / 16f, 15 / 16f, 2 / 16f, 3 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(2 / 16f, 4 / 16f, 0f, 1f, 0f, 2 / 16f, designTexture, -1));
                        quads.addAll(ModelHelper.createCuboid(12 / 16f, 14 / 16f, 0f, 1f, 0f, 2 / 16f, designTexture, -1));
                    }
                    case NORTH -> {
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 1 / 16f, 3 / 16f, 13 / 16f, 14 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 5 / 16f, 7 / 16f, 13 / 16f, 14 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 9 / 16f, 11 / 16f, 13 / 16f, 14 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 1f, 13 / 16f, 15 / 16f, 13 / 16f, 14 / 16f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(2 / 16f, 4 / 16f, 0f, 1f, 14 / 16f, 1f, designTexture, -1));
                        quads.addAll(ModelHelper.createCuboid(12 / 16f, 14 / 16f, 0f, 1f, 14 / 16f, 1f, designTexture, -1));
                    }
                    case EAST -> {
                        quads.addAll(ModelHelper.createCuboid(2 / 16f, 3 / 16f, 1 / 16f, 3 / 16f, 0f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(2 / 16f, 3 / 16f, 5 / 16f, 7 / 16f, 0f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(2 / 16f, 3 / 16f, 9 / 16f, 11 / 16f, 0f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(2 / 16f, 3 / 16f, 13 / 16f, 15 / 16f, 0f, 1f, texture, tintIndex));
                        quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 0f, 1f, 2 / 16f, 4 / 16f, designTexture, -1));
                        quads.addAll(ModelHelper.createCuboid(0f, 2 / 16f, 0f, 1f, 12 / 16f, 14 / 16f, designTexture, -1));
                    }
                }
            }
            int overlayIndex = extraData.get(FrameBlockTile.OVERLAY);
            if (overlayIndex != 0) {
                switch (state.getValue(LadderBlock.FACING)) {
                    case NORTH -> quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 13 / 16f, 1f, overlayIndex));
                    case WEST -> quads.addAll(ModelHelper.createOverlay(13 / 16f, 1f, 0f, 1f, 0f, 1f, overlayIndex));
                    case EAST -> quads.addAll(ModelHelper.createOverlay(0f, 3 / 16f, 0f, 1f, 0f, 1f, overlayIndex));
                    case SOUTH -> quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, 3 / 16f, overlayIndex));
                }
            }
            return quads;
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