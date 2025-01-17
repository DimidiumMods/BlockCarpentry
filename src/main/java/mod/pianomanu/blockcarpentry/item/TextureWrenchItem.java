package mod.pianomanu.blockcarpentry.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * This class is used to add a tooltip to the texture wrench item
 *
 * @author PianoManu
 * @version 1.2 11/12/22
 */
public class TextureWrenchItem extends BCToolItem {

    /**
     * Standard constructor for Minecraft items
     *
     * @param properties the item's properties
     */
    public TextureWrenchItem(Properties properties) {
        super(properties);
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
            component.add(Component.translatable("tooltip.blockcarpentry.texture_wrench"));
        } else {
            component.add(Component.translatable("tooltip.blockcarpentry.shift"));
        }
    }
}
//========SOLI DEO GLORIA========//