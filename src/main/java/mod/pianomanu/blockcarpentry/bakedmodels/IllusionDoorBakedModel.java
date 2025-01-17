package mod.pianomanu.blockcarpentry.bakedmodels;

import mod.pianomanu.blockcarpentry.block.DoorFrameBlock;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
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
 * See {@link mod.pianomanu.blockcarpentry.util.ModelHelper} for more information
 *
 * @author PianoManu
 * @version 1.5 11/17/22
 */
public class IllusionDoorBakedModel implements IDynamicBakedModel {
    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, RenderType renderType) {
        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
        if (mimic != null) {
            ModelResourceLocation location = BlockModelShaper.stateToModelLocation(mimic);
            BakedModel model = Minecraft.getInstance().getModelManager().getModel(location);
            return getIllusionQuads(state, side, rand, extraData, model);
        }
        return Collections.emptyList();
    }

    private List<BakedQuad> getIllusionQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull ModelData extraData, BakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
        if (mimic != null && state != null) {
            List<TextureAtlasSprite> glassBlockList = TextureHelper.getGlassTextures();
            TextureAtlasSprite glass = glassBlockList.get(extraData.get(FrameBlockTile.GLASS_COLOR));
            List<BakedQuad> quads = new ArrayList<>();
            Direction dir = state.getValue(DoorBlock.FACING);
            boolean open = state.getValue(DoorFrameBlock.OPEN);
            DoorHingeSide hinge = state.getValue(DoorFrameBlock.HINGE);
            Direction west = Direction.WEST;
            Direction east = Direction.EAST;
            Direction north = Direction.NORTH;
            Direction south = Direction.SOUTH;
            DoorHingeSide left = DoorHingeSide.LEFT;
            DoorHingeSide right = DoorHingeSide.RIGHT;
            int design = extraData.get(FrameBlockTile.DESIGN);//int design = state.getValue(DoorFrameBlock.DESIGN);
            DoubleBlockHalf half = state.getValue(DoorBlock.HALF);
            DoubleBlockHalf lower = DoubleBlockHalf.LOWER;
            DoubleBlockHalf upper = DoubleBlockHalf.UPPER;
            int tintIndex = BlockAppearanceHelper.setTintIndex(mimic);
            int rotation = extraData.get(FrameBlockTile.ROTATION);
            boolean upVisible, downVisible, nVisible, eVisible, sVisible, wVisible;
            boolean xStripe, yStripe, zStripe;

            int overlayIndex = extraData.get(FrameBlockTile.OVERLAY);
            boolean northSide = (dir == north && !open) || (dir == east && open && hinge == right) || (dir == west && open && hinge == left);
            boolean westSide = (dir == west && !open) || (dir == north && open && hinge == right) || (dir == south && open && hinge == left);
            boolean eastSide = (dir == south && open && hinge == right) || (dir == east && !open) || (dir == north && open && hinge == left);
            boolean southSide = (dir == east && open && hinge == left) || (dir == west && open && hinge == right) || (dir == south && !open);
            int xOffset = eastSide ? 0 : 13;
            int zOffset = southSide ? 0 : 13;
            if (design == 0 || design == 1) {
                if (northSide || southSide) {
                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 3; z++) {
                                xStripe = half == lower ? y < 4 : y > 11;
                                yStripe = x < 4 || x > 11;
                                upVisible = half == lower ? (y == 3 && !yStripe) : y == 15;
                                downVisible = half == upper ? (y == 12 && !yStripe) : y == 0;
                                wVisible = x == 0 || (x == 12 && !xStripe);
                                eVisible = x == 15 || (x == 3 && !xStripe);
                                nVisible = z == 0;
                                sVisible = z == 2;
                                if (xStripe || yStripe)
                                    quads.addAll(ModelHelper.createSixFaceVoxel(x, y, z + zOffset, mimic, model, extraData, rand, tintIndex, nVisible, sVisible, eVisible, wVisible, upVisible, downVisible, rotation));
                                if ((xStripe || yStripe) && overlayIndex > 0)
                                    quads.addAll(ModelHelper.createOverlayVoxel(x, y, z + zOffset, overlayIndex, nVisible, sVisible, eVisible, wVisible, upVisible && y == 15, downVisible, false));
                                if (!xStripe && !yStripe && z == 1)
                                    if (design == 0)
                                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createVoxel(x, y, z + zOffset, glass, tintIndex, true, true, false, false, false, false));
                                    else
                                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createSixFaceVoxel(x, y, z + zOffset, mimic, model, extraData, rand, tintIndex, true, true, false, false, false, false, rotation));
                                if (!xStripe && !yStripe && z == 1 && overlayIndex > 0)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlayVoxel(x, y, z + zOffset, overlayIndex, true, true, false, false, false, false, false));
                            }
                        }
                    }
                } else if (westSide || eastSide) {
                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 16; z++) {
                                zStripe = half == lower ? y < 4 : y > 11;
                                yStripe = z < 4 || z > 11;
                                upVisible = half == lower ? (y == 3 && !yStripe) : y == 15;
                                downVisible = half == upper ? (y == 12 && !yStripe) : y == 0;
                                nVisible = z == 0 || (z == 12 && !zStripe);
                                sVisible = z == 15 || (z == 3 && !zStripe);
                                wVisible = x == 0;
                                eVisible = x == 2;
                                if (zStripe || yStripe)
                                    quads.addAll(ModelHelper.createSixFaceVoxel(x + xOffset, y, z, mimic, model, extraData, rand, tintIndex, nVisible, sVisible, eVisible, wVisible, upVisible, downVisible, rotation));
                                if ((zStripe || yStripe) && overlayIndex > 0)
                                    quads.addAll(ModelHelper.createOverlayVoxel(x + xOffset, y, z, overlayIndex, nVisible, sVisible, eVisible, wVisible, upVisible && y == 15, downVisible, false));
                                if (!zStripe && !yStripe && x == 1)
                                    if (design == 0)
                                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createVoxel(x + xOffset, y, z, glass, tintIndex, false, false, true, true, false, false));
                                    else
                                        quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createSixFaceVoxel(x + xOffset, y, z, mimic, model, extraData, rand, tintIndex, false, false, true, true, false, false, rotation));
                                if (!zStripe && !yStripe && x == 1 && overlayIndex > 0)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlayVoxel(x + xOffset, y, z, overlayIndex, false, false, true, true, false, false, false));
                            }
                        }
                    }
                }
            }
            if (design == 2) {
                if (northSide) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 13 / 16f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, true, half == upper, half == lower, rotation));
                    if (overlayIndex > 0)
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 13 / 16f, 1f, overlayIndex, true, true, true, true, half == upper, half == lower, false));
                } else if (westSide) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(13 / 16f, 1f, 0f, 1f, 0f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, true, half == upper, half == lower, rotation));
                    if (overlayIndex > 0)
                        quads.addAll(ModelHelper.createOverlay(13 / 16f, 1f, 0f, 1f, 0f, 1f, overlayIndex, true, true, true, true, half == upper, half == lower, false));
                } else if (eastSide) {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 3 / 16f, 0f, 1f, 0f, 1f, mimic, model, extraData, rand, tintIndex, true, true, true, true, half == upper, half == lower, rotation));
                    if (overlayIndex > 0)
                        quads.addAll(ModelHelper.createOverlay(0f, 3 / 16f, 0f, 1f, 0f, 1f, overlayIndex, true, true, true, true, half == upper, half == lower, false));
                } else {
                    quads.addAll(ModelHelper.createSixFaceCuboid(0f, 1f, 0f, 1f, 0f, 3 / 16f, mimic, model, extraData, rand, tintIndex, true, true, true, true, half == upper, half == lower, rotation));
                    if (overlayIndex > 0)
                        quads.addAll(ModelHelper.createOverlay(0f, 1f, 0f, 1f, 0f, 3 / 16f, overlayIndex, true, true, true, true, half == upper, half == lower, false));
                }
            }
            if (design == 3) {
                if (northSide || southSide) {
                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 3; z++) {
                                xStripe = y < 4 || y > 11;
                                yStripe = x < 4 || x > 11;
                                upVisible = half == lower ? (y == 3 && !yStripe) : y == 15 || (y == 3 && !yStripe);
                                downVisible = half == upper ? (y == 12 && !yStripe) : y == 0 || (y == 12 && !yStripe);
                                wVisible = x == 0 || (x == 12 && !xStripe);
                                eVisible = x == 15 || (x == 3 && !xStripe);
                                nVisible = z == 0;
                                sVisible = z == 2;
                                if (xStripe || yStripe)
                                    quads.addAll(ModelHelper.createSixFaceVoxel(x, y, z + zOffset, mimic, model, extraData, rand, tintIndex, nVisible, sVisible, eVisible, wVisible, upVisible, downVisible, rotation));
                                if ((xStripe || yStripe) && overlayIndex > 0)
                                    quads.addAll(ModelHelper.createOverlayVoxel(x, y, z + zOffset, overlayIndex, nVisible, sVisible, eVisible, wVisible, upVisible && y == 15, downVisible, false));
                                if (!xStripe && !yStripe && z == 1)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createVoxel(x, y, z + zOffset, glass, tintIndex, true, true, false, false, false, false));
                                if (!xStripe && !yStripe && z == 1 && overlayIndex > 0)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlayVoxel(x, y, z + zOffset, overlayIndex, true, true, false, false, false, false, false));
                            }
                        }
                    }
                } else if (westSide || eastSide) {
                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 16; z++) {
                                zStripe = y < 4 || y > 11;
                                yStripe = z < 4 || z > 11;
                                upVisible = half == lower ? (y == 3 && !yStripe) : y == 15 || (y == 3 && !yStripe);
                                downVisible = half == upper ? (y == 12 && !yStripe) : y == 0 || (y == 12 && !yStripe);
                                nVisible = z == 0 || (z == 12 && !zStripe);
                                sVisible = z == 15 || (z == 3 && !zStripe);
                                wVisible = x == 0;
                                eVisible = x == 2;
                                if (zStripe || yStripe)
                                    quads.addAll(ModelHelper.createSixFaceVoxel(x + xOffset, y, z, mimic, model, extraData, rand, tintIndex, nVisible, sVisible, eVisible, wVisible, upVisible, downVisible, rotation));
                                if ((zStripe || yStripe) && overlayIndex > 0)
                                    quads.addAll(ModelHelper.createOverlayVoxel(x + xOffset, y, z, overlayIndex, nVisible, sVisible, eVisible, wVisible, upVisible && y == 15, downVisible, false));
                                if (!zStripe && !yStripe && x == 1)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createVoxel(x + xOffset, y, z, glass, tintIndex, false, false, true, true, false, false));
                                if (!zStripe && !yStripe && x == 1 && overlayIndex > 0)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlayVoxel(x + xOffset, y, z, overlayIndex, false, false, true, true, false, false, false));
                            }
                        }
                    }
                }
            }
            if (design == 4) {
                if (northSide || southSide) {
                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 3; z++) {
                                xStripe = y < 3 || y > 12 || y == 7 || y == 8;
                                yStripe = x < 3 || x > 12 || x == 7 || x == 8;
                                upVisible = half == lower ? ((y == 2 || y == 8) && !yStripe) : y == 15 || ((y == 2 || y == 8) && !yStripe);
                                downVisible = half == upper ? ((y == 7 || y == 13) && !yStripe) : y == 0 || ((y == 7 || y == 13) && !yStripe);
                                wVisible = x == 0 || ((x == 7 || x == 13) && !xStripe);
                                eVisible = x == 15 || ((x == 2 || x == 8) && !xStripe);
                                nVisible = z == 0;
                                sVisible = z == 2;
                                if (xStripe || yStripe)
                                    quads.addAll(ModelHelper.createSixFaceVoxel(x, y, z + zOffset, mimic, model, extraData, rand, tintIndex, nVisible, sVisible, eVisible, wVisible, upVisible, downVisible, rotation));
                                if ((xStripe || yStripe) && overlayIndex > 0)
                                    quads.addAll(ModelHelper.createOverlayVoxel(x, y, z + zOffset, overlayIndex, nVisible, sVisible, eVisible, wVisible, upVisible && y == 15, downVisible, false));
                                if (!xStripe && !yStripe && z == 1)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createVoxel(x, y, z + zOffset, glass, tintIndex, true, true, false, false, false, false));
                                if (!xStripe && !yStripe && z == 1 && overlayIndex > 0)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlayVoxel(x, y, z + zOffset, overlayIndex, true, true, false, false, false, false, false));
                            }
                        }
                    }
                }
                if (westSide || eastSide) {
                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 16; y++) {
                            for (int z = 0; z < 16; z++) {
                                zStripe = y < 3 || y > 12 || y == 7 || y == 8;
                                yStripe = z < 3 || z > 12 || z == 7 || z == 8;
                                upVisible = half == lower ? ((y == 2 || y == 8) && !yStripe) : y == 15 || ((y == 2 || y == 8) && !yStripe);
                                downVisible = half == upper ? ((y == 7 || y == 13) && !yStripe) : y == 0 || ((y == 7 || y == 13) && !yStripe);
                                nVisible = z == 0 || ((z == 7 || z == 13) && !zStripe);
                                sVisible = z == 15 || ((z == 2 || z == 8) && !zStripe);
                                wVisible = x == 0;
                                eVisible = x == 2;
                                if (zStripe || yStripe)
                                    quads.addAll(ModelHelper.createSixFaceVoxel(x + xOffset, y, z, mimic, model, extraData, rand, tintIndex, nVisible, sVisible, eVisible, wVisible, upVisible, downVisible, rotation));
                                if ((zStripe || yStripe) && overlayIndex > 0)
                                    quads.addAll(ModelHelper.createOverlayVoxel(x + xOffset, y, z, overlayIndex, nVisible, sVisible, eVisible, wVisible, upVisible && y == 15, downVisible, false));
                                if (!zStripe && !yStripe && x == 1)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createVoxel(x + xOffset, y, z, glass, tintIndex, false, false, true, true, false, false));
                                if (!zStripe && !yStripe && x == 1 && overlayIndex > 0)
                                    quads.addAll(mod.pianomanu.blockcarpentry.util.ModelHelper.createOverlayVoxel(x + xOffset, y, z, overlayIndex, false, false, true, true, false, false, false));
                            }
                        }
                    }
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
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation("minecraft", "block/oak_planks"));
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