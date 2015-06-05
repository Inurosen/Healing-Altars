package inurosen.healaltar.common.items;

import inurosen.healaltar.common.entity.ExtendedPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;

public class SoulHeart extends Item
{
    public SoulHeart()
    {
        super();
        setMaxStackSize(64);
        setUnlocalizedName("soulHeart");
        setTextureName("healingaltar:soulheart");
        setCreativeTab(CreativeTabs.tabMisc);
        setHasSubtypes(false);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote && !player.capabilities.isCreativeMode)
        {
            float current = ExtendedPlayer.get(player).getSoulHearts();
            if (current == 20)
            {
                return stack;
            }
            else if (current >= 20)
            {
                ExtendedPlayer.get(player).setSoulHearts(20);
                return stack;
            }
            world.playSoundEffect(player.posX, player.posY, player.posZ, "random.orb", 1.0F, 0.5F);
            ExtendedPlayer.get(player).setSoulHearts(current + 2);
            stack.stackSize--;
        }
        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean p4)
    {
        list.add(EnumChatFormatting.ITALIC + "Gives 1 soul heart");
    }
}