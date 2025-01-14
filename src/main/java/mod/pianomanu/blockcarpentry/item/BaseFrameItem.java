package mod.pianomanu.blockcarpentry.item;

import mod.pianomanu.blockcarpentry.setup.Registration;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This class is used to add a tooltip to all frame blocks that can be seen when hovering over the associated item
 *
 * @author PianoManu
 * @version 1.1 06/11/22
 */
public class BaseFrameItem extends BlockItem {
    /**
     * Standard constructor for Minecraft block items
     *
     * @param block      the block associated to this item
     * @param properties the item's properties
     */
    public BaseFrameItem(Block block, Properties properties) {
        super(block, properties);
    }

    /**
     * This method adds a tooltip to the item shown when hovering over the item in an inventory
     *
     * @param itemStack the itemStack where the text shall be appended
     * @param level     the current level
     * @param component list of text components; for this item, we append either the item description or the reference to press shift
     * @param flag      I don't really know what this does, I did not need it
     */
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> component, TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            component.add(Component.translatable("tooltip.blockcarpentry.base_frame"));
            if (!this.getBlock().equals(Registration.FRAMEBLOCK.get()) && !this.getBlock().equals(Registration.LAYERED_FRAMEBLOCK.get()) && !this.getBlock().equals(Registration.SLOPE_FRAMEBLOCK.get()) && !this.getBlock().equals(Registration.EDGED_SLOPE_FRAMEBLOCK.get())) {
                component.add(Component.translatable("tooltip.blockcarpentry.vanilla_like"));
            }
            if (this.getBlock().equals(Registration.SLAB_FRAMEBLOCK.get())) {
                component.add(Component.translatable("tooltip.blockcarpentry.rotatable"));
            }
            if (this.getBlock().equals(Registration.LAYERED_FRAMEBLOCK.get())) {
                component.add(Component.translatable("tooltip.blockcarpentry.layered"));
            }
            if (this.getBlock().equals(Registration.SLOPE_FRAMEBLOCK.get()) || this.getBlock().equals(Registration.EDGED_SLOPE_FRAMEBLOCK.get())) {
                component.add(Component.translatable("tooltip.blockcarpentry.slope"));
            }
            if (this.getBlock().equals(Registration.DOOR_FRAMEBLOCK.get()) || this.getBlock().equals(Registration.TRAPDOOR_FRAMEBLOCK.get()) || this.getBlock().equals(Registration.FENCE_GATE_FRAMEBLOCK.get())) {
                component.add(Component.translatable("tooltip.blockcarpentry.locking"));
            }
        } else {
            component.add(Component.translatable("tooltip.blockcarpentry.shift"));
        }
    }
}
//========SOLI DEO GLORIA========//