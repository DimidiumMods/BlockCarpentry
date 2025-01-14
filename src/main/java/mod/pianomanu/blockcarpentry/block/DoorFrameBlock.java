package mod.pianomanu.blockcarpentry.block;

import mod.pianomanu.blockcarpentry.item.BaseFrameItem;
import mod.pianomanu.blockcarpentry.item.BaseIllusionItem;
import mod.pianomanu.blockcarpentry.tileentity.FrameBlockTile;
import mod.pianomanu.blockcarpentry.tileentity.LockableFrameTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Main class for frame doors - all important block info can be found here
 * Visit {@link FrameBlock} for a better documentation
 *
 * @author PianoManu
 * @version 1.5 09/19/23
 */
public class DoorFrameBlock extends DoorBlock implements EntityBlock, IFrameBlock {

    public DoorFrameBlock(Properties builder) {
        super(builder, BlockSetType.OAK);
        this.registerDefaultState(this.stateDefinition.any().setValue(CONTAINS_BLOCK, Boolean.FALSE).setValue(DoorBlock.FACING, Direction.NORTH).setValue(OPEN, Boolean.valueOf(false)).setValue(HINGE, DoorHingeSide.LEFT).setValue(POWERED, Boolean.valueOf(false)).setValue(HALF, DoubleBlockHalf.LOWER).setValue(LIGHT_LEVEL, 0));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONTAINS_BLOCK, LIGHT_LEVEL);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LockableFrameTile(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitresult) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            convertOutdatedTile(state, level, pos, player);
            if (shouldCallFrameUse(state, itemStack))
                return frameUse(state, level, pos, player, hand, hitresult);
            if (lockRedstoneSignal(state, level, pos, player, itemStack) || lockOpenClose(state, level, pos, player, itemStack))
                return InteractionResult.CONSUME;
            if (!(itemStack.getItem() instanceof BaseFrameItem || itemStack.getItem() instanceof BaseIllusionItem)) {
                BlockEntity tileEntity = level.getBlockEntity(pos);
                if (tileEntity instanceof LockableFrameTile doorTileEntity) {
                    if (doorTileEntity.canBeOpenedByPlayers()) {
                        super.use(state, level, pos, player, hand, hitresult);
                        level.levelEvent(null, state.getValue(OPEN) ? 1012 : 1006, pos, 0);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return itemStack.getItem() instanceof BlockItem ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }

    private void convertOutdatedTile(BlockState state, Level level, BlockPos pos, Player player) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (!(tileEntity instanceof LockableFrameTile) && (tileEntity instanceof FrameBlockTile)) {
            LockableFrameTile newTile = (LockableFrameTile) newBlockEntity(pos, state);
            if (newTile != null) {
                newTile.addFromOutdatedTileEntity((FrameBlockTile) tileEntity);
                level.setBlockEntity(newTile);
                player.displayClientMessage(Component.translatable("message.blockcarpentry.converting_outdated_block"), true);
            }
        }
    }

    private boolean lockRedstoneSignal(BlockState state, Level level, BlockPos pos, Player player, ItemStack itemStack) {
        if (itemStack.getItem() == Items.REDSTONE) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof LockableFrameTile doorTileEntity) {
                LockableFrameTile secondTile = (LockableFrameTile) (state.getValue(HALF) == DoubleBlockHalf.LOWER ? level.getBlockEntity(pos.above()) : level.getBlockEntity(pos.below()));
                if (doorTileEntity.canBeOpenedByRedstoneSignal()) {
                    player.displayClientMessage(Component.translatable("message.blockcarpentry.redstone_off"), true);
                } else {
                    player.displayClientMessage(Component.translatable("message.blockcarpentry.redstone_on"), true);
                }
                doorTileEntity.setCanBeOpenedByRedstoneSignal(!doorTileEntity.canBeOpenedByRedstoneSignal());
                if (secondTile != null) {
                    secondTile.setCanBeOpenedByRedstoneSignal(!secondTile.canBeOpenedByRedstoneSignal());
                }
            } else {
                convertOutdatedTile(state, level, pos, player);
            }
            return true;
        }
        return false;
    }

    private boolean lockOpenClose(BlockState state, Level level, BlockPos pos, Player player, ItemStack itemStack) {
        if (itemStack.getItem() == Items.IRON_INGOT) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof LockableFrameTile doorTileEntity) {
                LockableFrameTile secondTile = (LockableFrameTile) (state.getValue(HALF) == DoubleBlockHalf.LOWER ? level.getBlockEntity(pos.above()) : level.getBlockEntity(pos.below()));
                if (doorTileEntity.canBeOpenedByPlayers()) {
                    player.displayClientMessage(Component.translatable("message.blockcarpentry.lock"), true);
                } else {
                    player.displayClientMessage(Component.translatable("message.blockcarpentry.unlock"), true);
                }
                doorTileEntity.setCanBeOpenedByPlayers(!doorTileEntity.canBeOpenedByPlayers());
                if (secondTile != null) {
                    secondTile.setCanBeOpenedByPlayers(!secondTile.canBeOpenedByPlayers());
                }
            } else {
                convertOutdatedTile(state, level, pos, player);
            }
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level levelIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            dropContainedBlock(levelIn, pos);

            super.onRemove(state, levelIn, pos, newState, isMoving);
        }
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return IFrameBlock.getLightEmission(state);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean update) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof LockableFrameTile doorTileEntity && doorTileEntity.canBeOpenedByRedstoneSignal()) {
            super.neighborChanged(state, level, pos, block, pos2, update);
        }
    }

    @Override
    public boolean isCorrectTileInstance(BlockEntity blockEntity) {
        return blockEntity instanceof LockableFrameTile;
    }

    public void fillBlockEntity(Level levelIn, BlockPos pos, BlockState state, BlockState handBlock, BlockEntity blockEntity) {
        LockableFrameTile frameBlockEntity = (LockableFrameTile) blockEntity;
        frameBlockEntity.clear();
        frameBlockEntity.setMimic(handBlock);
        levelIn.setBlock(pos, state.setValue(CONTAINS_BLOCK, Boolean.TRUE), 2);
    }
}
//========SOLI DEO GLORIA========//