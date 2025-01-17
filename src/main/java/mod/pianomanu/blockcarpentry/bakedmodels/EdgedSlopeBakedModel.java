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
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
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
 * @version 1.2 11/07/22
 */
public class EdgedSlopeBakedModel implements IDynamicBakedModel {
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

    public List<BakedQuad> getMimicQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, ModelData extraData, BakedModel model) {
        if (side != null) {
            return Collections.emptyList();
        }
        BlockState mimic = extraData.get(FrameBlockTile.MIMIC);
        if (mimic != null && state != null) {
            List<TextureAtlasSprite> texture = TextureHelper.getTextureFromModel(model, extraData, rand);
            int index = extraData.get(FrameBlockTile.TEXTURE);
            if (index >= texture.size()) {
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
            Half half = state.getValue(StairBlock.HALF);
            Half lower = Half.BOTTOM;
            Half upper = Half.TOP;
            Direction direction = state.getValue(StairBlock.FACING);
            Direction north = Direction.NORTH;
            Direction east = Direction.EAST;
            Direction south = Direction.SOUTH;
            Direction west = Direction.WEST;
            StairsShape shape = state.getValue(StairBlock.SHAPE);
            StairsShape straight = StairsShape.STRAIGHT;
            StairsShape innerLeft = StairsShape.INNER_LEFT;
            StairsShape innerRight = StairsShape.INNER_RIGHT;
            StairsShape outerLeft = StairsShape.OUTER_LEFT;
            StairsShape outerRight = StairsShape.OUTER_RIGHT;

            //Don't even try to understand what's happening here
            //If any of it broke, it's easier to completely rewrite it than to find the problem
            //All the boolean expressions are used to determine whether a certain face of the voxel is visible or not
            //e.g.: "z == 0" for parameter "north" means:
            //      All voxels at z == 0 will have a visible north face
            //e.g.: "z == 15 - y" for parameter "up" means:
            //      All voxels with a z-value equal to "15 minus the current y-value" will have a visible top face
            if (half == lower) {
                //iterate through each of the 4096 voxels and check, which sides are visible, then add to quads
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            if (shape == straight) {
                                if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == 15 - y, x == 15 && z <= 15 - y, x == 0 && z <= 15 - y, z == 15 - y, y == 0));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x >= y, z == 15 && x >= y, x == 15, x == y, x == y, y == 0));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == y, z == 15, x == 15 && z >= y, x == 0 && z >= y, z == y, y == 0));
                                } else if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= 15 - y, z == 15 && x <= 15 - y, x == 15 - y, x == 0, x == 15 - y, y == 0));
                                }
                            }
                            if (shape == innerLeft) {
                                if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == 15 - y && z < x || z == 15 && x <= 15 - y, x == 15 && z <= 15 - y || x == 15 - y && x < z, x == 0, z == 15 - y && z <= x || x == 15 - y && x <= z, y == 0));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == 15 && x >= y || z == 15 - y && z < 15 - x, x == 15, x == y && x > 15 - z || x == 0 && z <= 15 - y, z == 15 - y && z <= 15 - x || x == y && x >= 15 - z, y == 0));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == y && z > x || z == 0 && x >= y, z == 15, x == 15, x == 0 && z >= y || x == y && x > z, z == y && z >= x || x == y && x >= z, y == 0));
                                } else if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= 15 - y || z == y && z > 15 - x, z == 15, x == 15 - y && x < 15 - z || x == 15 && z >= y, x == 0, x == 15 - y && x <= 15 - z || z == y && z > 15 - x, y == 0));
                                }
                            }
                            if (shape == innerRight) {
                                if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == 15 - y && z < x || z == 15 && x <= 15 - y, x == 15 && z <= 15 - y || x == 15 - y && x < z, x == 0, z == 15 - y && z <= x || x == 15 - y && x <= z, y == 0));
                                } else if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == 15 && x >= y || z == 15 - y && z < 15 - x, x == 15, x == y && x > 15 - z || x == 0 && z <= 15 - y, z == 15 - y && z <= 15 - x || x == y && x >= 15 - z, y == 0));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == y && z > x || z == 0 && x >= y, z == 15, x == 15, x == 0 && z >= y || x == y && x > z, z == y && z >= x || x == y && x >= z, y == 0));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= 15 - y || z == y && z > 15 - x, z == 15, x == 15 - y && x < 15 - z || x == 15 && z >= y, x == 0, x == 15 - y && x <= 15 - z || z == y && z > 15 - x, y == 0));
                                }
                            }
                            if (shape == outerLeft) {
                                if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= 15 - y, z == 15 - y && x <= 15 - y, x == 15 - y && z <= 15 - y, x == 0 && z <= 15 - y, z == 15 - y && z >= x || x == 15 - y && x >= z, y == 0));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x >= y, z == 15 - y && x >= y, x == 15 && z <= 15 - y, x == y && x <= 15 - z, x == y && x <= 15 - z || z == 15 - y && z >= 15 - x, y == 0));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == y && z <= x, z == 15 && x >= y, x == 15 && z >= y, x == y && z >= y, z == y && z <= x || x == y && x <= z, y == 0));
                                } else if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == y && x <= 15 - y, z == 15 && x <= 15 - y, x == 15 - y && x >= 15 - z, x == 0 && z >= y, x == 15 - y && x >= 15 - z || z == y && z < 15 - x, y == 0));
                                }
                            }
                            if (shape == outerRight) {
                                if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= 15 - y, z == 15 - y && x <= 15 - y, x == 15 - y && z <= 15 - y, x == 0 && z <= 15 - y, z == 15 - y && z >= x || x == 15 - y && x >= z, y == 0));
                                } else if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x >= y, z == 15 - y && x >= y, x == 15 && z <= 15 - y, x == y && x <= 15 - z, x == y && x <= 15 - z || z == 15 - y && z >= 15 - x, y == 0));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == y && z <= x, z == 15 && x >= y, x == 15 && z >= y, x == y && z >= y, z == y && z <= x || x == y && x <= z, y == 0));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == y && x <= 15 - y, z == 15 && x <= 15 - y, x == 15 - y && x >= 15 - z, x == 0 && z >= y, x == 15 - y && x >= 15 - z || z == y && z < 15 - x, y == 0));
                                }
                            }
                        }
                    }
                }
            }


            if (half == upper) {
                //iterate through each of the 4096 voxels and check, which sides are visible, then add to quads
                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            if (shape == straight) {
                                if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == y, x == 15 && z <= y, x == 0 && z <= y, y == 15, z == y));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x >= 15 - y, z == 15 && x >= 15 - y, x == 15, x == 15 - y, y == 15, x == 15 - y));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 15 - y, z == 15, x == 15 && z >= 15 - y, x == 0 && z >= 15 - y, y == 15, z == 15 - y));
                                } else if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= y, z == 15 && x <= y, x == y, x == 0, y == 15, x == y));
                                }
                            }
                            if (shape == innerLeft) {
                                if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == y && z < x || z == 15 && x <= y, x == 15 && z <= y || x == y && x < z, x == 0, y == 15, z == y && z <= x || x == y && x <= z));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == 15 && x >= 15 - y || z == y && z < 15 - x, x == 15, x == 15 - y && x > 15 - z || x == 0 && z <= y, y == 15, z == y && z <= 15 - x || x == 15 - y && x >= 15 - z));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 15 - y && z > x || z == 0 && x >= 15 - y, z == 15, x == 15, x == 0 && z >= 15 - y || x == 15 - y && x > z, y == 15, z == 15 - y && z >= x || x == 15 - y && x >= z));
                                } else if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= y || z == 15 - y && z > 15 - x, z == 15, x == y && x < 15 - z || x == 15 && z >= 15 - y, x == 0, y == 15, x == y && x <= 15 - z || z == 15 - y && z > 15 - x));
                                }
                            }
                            if (shape == innerRight) {
                                if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == y && z < x || z == 15 && x <= y, x == 15 && z <= y || x == y && x < z, x == 0, y == 15, z == y && z <= x || x == y && x <= z));
                                } else if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0, z == 15 && x >= 15 - y || z == y && z < 15 - x, x == 15, x == 15 - y && x > 15 - z || x == 0 && z <= y, y == 15, z == y && z <= 15 - x || x == 15 - y && x >= 15 - z));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 15 - y && z > x || z == 0 && x >= 15 - y, z == 15, x == 15, x == 0 && z >= 15 - y || x == 15 - y && x > z, y == 15, z == 15 - y && z >= x || x == 15 - y && x >= z));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= y || z == 15 - y && z > 15 - x, z == 15, x == y && x < 15 - z || x == 15 && z >= 15 - y, x == 0, y == 15, x == y && x <= 15 - z || z == 15 - y && z > 15 - x));
                                }
                            }
                            if (shape == outerLeft) {
                                if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= y, z == y && x <= y, x == y && z <= y, x == 0 && z <= y, y == 15, z == y && z >= x || x == y && x >= z));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x >= 15 - y, z == y && x >= 15 - y, x == 15 && z <= y, x == 15 - y && x <= 15 - z, y == 15, x == 15 - y && x <= 15 - z || z == y && z >= 15 - x));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 15 - y && z <= x, z == 15 && x >= 15 - y, x == 15 && z >= 15 - y, x == 15 - y && z >= 15 - y, y == 15, z == 15 - y && z <= x || x == 15 - y && x <= z));
                                } else if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 15 - y && x <= y, z == 15 && x <= y, x == y && x >= 15 - z, x == 0 && z >= 15 - y, y == 15, x == y && x >= 15 - z || z == 15 - y && z < 15 - x));
                                }
                            }
                            if (shape == outerRight) {
                                if (direction == west) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x <= y, z == y && x <= y, x == y && z <= y, x == 0 && z <= y, y == 15, z == y && z >= x || x == y && x >= z));
                                } else if (direction == north) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 0 && x >= 15 - y, z == y && x >= 15 - y, x == 15 && z <= y, x == 15 - y && x <= 15 - z, y == 15, x == 15 - y && x <= 15 - z || z == y && z >= 15 - x));
                                } else if (direction == east) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 15 - y && z <= x, z == 15 && x >= 15 - y, x == 15 && z >= 15 - y, x == 15 - y && z >= 15 - y, y == 15, z == 15 - y && z <= x || x == 15 - y && x <= z));
                                } else if (direction == south) {
                                    quads.addAll(ModelHelper.createVoxel(x, y, z, texture.get(index), tintIndex, z == 15 - y && x <= y, z == 15 && x <= y, x == y && x >= 15 - z, x == 0 && z >= 15 - y, y == 15, x == y && x >= 15 - z || z == 15 - y && z < 15 - x));
                                }
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