package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.item.BCToolItem;
import mod.pianomanu.blockcarpentry.item.BaseFrameItem;
import mod.pianomanu.blockcarpentry.item.BaseIllusionItem;
import mod.pianomanu.blockcarpentry.setup.Registration;
import mod.pianomanu.blockcarpentry.setup.config.BCModConfig;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.util.BCBlockStateProperties;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelper;
import mod.pianomanu.blockcarpentry.util.BlockAppearanceHelperItems;
import mod.pianomanu.blockcarpentry.util.BlockSavingHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Basic interface for frame blocks (WIP)
 * Everything here is just for test purposes and subject to change
 *
 * @author PianoManu
 * @version 1.0 11/12/22
 */
public interface IFrameBlock {
    BooleanProperty CONTAINS_BLOCK = BCBlockStateProperties.CONTAINS_BLOCK;
    BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    IntegerProperty LIGHT_LEVEL = BCBlockStateProperties.LIGHT_LEVEL;
    DirectionProperty FACING = BlockStateProperties.FACING;

    static int getLightEmission(BlockState state) {
        if (state.getValue(LIGHT_LEVEL) > 15) {
            return 15;
        }
        return state.getValue(LIGHT_LEVEL);
    }

    default boolean shouldCallFrameUse(BlockState state, ItemStack itemStack) {
        return !state.getValue(CONTAINS_BLOCK) || BlockAppearanceHelperItems.isAppearanceModifier(itemStack.getItem()) || (itemStack.getItem() instanceof BCToolItem);
    }

    default boolean removeBlock(Level level, BlockPos pos, BlockState state, ItemStack itemStack, Player player) {
        if (itemStack.getItem() == Registration.HAMMER.get() || (!BCModConfig.HAMMER_NEEDED.get() && player.isCrouching())) {
            if (!player.isCreative())
                this.dropContainedBlock(level, pos);
            else {
                this.clearTile(level, pos);
            }
            state = state.setValue(CONTAINS_BLOCK, Boolean.FALSE);
            level.setBlock(pos, state, 2);
            return true;
        }
        return false;
    }

    default void clearTile(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            BlockEntity tileentity = level.getBlockEntity(pos);
            if (tileentity instanceof FrameBlockTile frameBlockEntity) {
                BlockState blockState = frameBlockEntity.getMimic();
                if (!(blockState == null)) {
                    frameBlockEntity.clear();
                }
            }
        }
    }

    /**
     * Used to drop the contained block
     * We check the tile entity, get the block from the tile entity and drop it at the block pos plus some small random coords in the level
     *
     * @param level the level where we drop the block
     * @param pos   the block position where we drop the block
     */
    default void dropContainedBlock(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof FrameBlockTile frameBlockEntity) {
                BlockState blockState = frameBlockEntity.getMimic();
                if (!(blockState == null)) {
                    dropItemStackInWorld(level, pos, blockState);
                    frameBlockEntity.clear();
                }
            }
        }
    }

    default void dropItemStackInWorld(Level level, BlockPos pos, BlockState blockState) {
        level.levelEvent(1010, pos, 0);
        float f = 0.7F;
        double dx = (double) (level.random.nextFloat() * f) + (double) 0.15F;
        double dy = (double) (level.random.nextFloat() * f) + (double) f;
        double dz = (double) (level.random.nextFloat() * f) + (double) 0.15F;
        ItemStack itemStack = new ItemStack(blockState.getBlock());
        ItemEntity itemEntity = new ItemEntity(level, (double) pos.getX() + dx, (double) pos.getY() + dy, (double) pos.getZ() + dz, itemStack);
        itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);
    }

    /**
     * Used to place a block in a frame. Therefor we need the tile entity of the block and set its mimic to the given block state.
     * Lastly, we update the block state (useful for observers or something, idk)
     *
     * @param levelIn   the level where we drop the block
     * @param pos       the block position where we drop the block
     * @param state     the old block state
     * @param handBlock the block state of the held block - the block we want to insert into the frame
     */
    default void insertBlock(Level levelIn, BlockPos pos, BlockState state, BlockState handBlock) {
        BlockEntity blockEntity = levelIn.getBlockEntity(pos);
        if (isCorrectTileInstance(blockEntity)) {
            fillBlockEntity(levelIn, pos, state, handBlock, blockEntity);
        }
    }

    default void fillBlockEntity(Level levelIn, BlockPos pos, BlockState state, BlockState handBlock, BlockEntity blockEntity) {
        FrameBlockTile frameBlockEntity = (FrameBlockTile) blockEntity;
        frameBlockEntity.clear();
        frameBlockEntity.setMimic(handBlock);
        levelIn.setBlock(pos, state.setValue(CONTAINS_BLOCK, Boolean.TRUE), 2);
    }

    default InteractionResult frameUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitresult) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (removeBlock(level, pos, state, itemStack, player))
            return InteractionResult.SUCCESS;
        if (BlockAppearanceHelper.setLightLevel(itemStack, state, level, pos, player, hand) ||
                BlockAppearanceHelper.setTexture(itemStack, state, level, player, pos) ||
                BlockAppearanceHelper.setDesign(level, pos, player, itemStack) ||
                BlockAppearanceHelper.setDesignTexture(level, pos, player, itemStack) ||
                BlockAppearanceHelper.setColor(level, pos, player, hand) ||
                BlockAppearanceHelper.setOverlay(level, pos, player, itemStack) ||
                BlockAppearanceHelper.setRotation(level, pos, player, itemStack))
            return InteractionResult.SUCCESS;
        if (itemStack.getItem() instanceof BlockItem) {
            if (changeMimic(state, level, pos, player, itemStack))
                return InteractionResult.SUCCESS;
        }
        return itemStack.getItem() instanceof BlockItem ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    default boolean changeMimic(BlockState state, Level level, BlockPos pos, Player player, ItemStack itemStack) {
        if (state.getValue(BCBlockStateProperties.CONTAINS_BLOCK) || itemStack.getItem() instanceof BaseFrameItem || itemStack.getItem() instanceof BaseIllusionItem) {
            return false;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        int count = itemStack.getCount();
        Block heldBlock = ((BlockItem) itemStack.getItem()).getBlock();
        if (isCorrectTileInstance(blockEntity) && !itemStack.isEmpty() && BlockSavingHelper.isValidBlock(heldBlock) && !state.getValue(CONTAINS_BLOCK)) {
            BlockState handBlockState = ((BlockItem) itemStack.getItem()).getBlock().defaultBlockState();
            insertBlock(level, pos, state, handBlockState);
            if (!player.isCreative())
                itemStack.setCount(count - 1);
        }
        return true;
    }

    default boolean isCorrectTileInstance(BlockEntity blockEntity) {
        return blockEntity instanceof FrameBlockTile;
    }
}

//========SOLI DEO GLORIA========//