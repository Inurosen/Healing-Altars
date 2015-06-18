package inurosen.healaltar.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import inurosen.healaltar.common.core.Helper;
import inurosen.healaltar.common.entity.EggProjectile;
import inurosen.healaltar.common.entity.ExtendedPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class RunicEgg extends Item
{
    protected IIcon[] iconArray;

    public RunicEgg()
    {
        super();
        setMaxStackSize(64);
        setUnlocalizedName("runicEgg");
        setTextureName("healingaltar:egg_dull");
        setCreativeTab(CreativeTabs.tabMisc);
        setHasSubtypes(false);
    }

   @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int p_77617_1_)
    {
        if (isItTime())
        {
            return iconArray[1];
        }
        return iconArray[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister register)
    {
        iconArray = new IIcon[2];
        iconArray[0] = register.registerIcon("healingaltar:egg_dull");
        iconArray[1] = register.registerIcon("healingaltar:egg_active");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote && !player.capabilities.isCreativeMode)
        {
            boolean validAltar = false;
            int tier = 1;

            long now = world.getTotalWorldTime();
            long diff;
            if (now < 10800 && ExtendedPlayer.get(player).lastRitual == 0)
            {
                diff = 10800;
            }
            else
            {
                diff = now - ExtendedPlayer.get(player).lastRitual;
            }
            Vec3 look = player.getLookVec();
            if (diff >= 10800 && look.yCoord == 1 && isItTime())
            {
                int x = (int) Math.floor(player.posX);
                int y = (int) (player.posY - player.getYOffset()) - 1;
                int z = (int) Math.floor(player.posZ);

                if (Helper.getHighestBlockAt(world, x, y, z) == Blocks.lapis_block)
                {
                    // Altar body
                    if (world.getBlock(x - 1, y, z - 1) == Blocks.brick_block)
                    {
                        if (world.getBlock(x, y, z - 1) == Blocks.brick_stairs)
                        {
                            if (world.getBlock(x + 1, y, z - 1) == Blocks.brick_block)
                            {
                                if (world.getBlock(x - 1, y, z) == Blocks.brick_stairs)
                                {
                                    if (world.getBlock(x + 1, y, z) == Blocks.brick_stairs)
                                    {
                                        if (world.getBlock(x - 1, y, z + 1) == Blocks.brick_block)
                                        {
                                            if (world.getBlock(x, y, z + 1) == Blocks.brick_stairs)
                                            {
                                                if (world.getBlock(x + 1, y, z + 1) == Blocks.brick_block)
                                                {
                                                    validAltar = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Altar heads
                    if (world.getTileEntity(x - 1, y + 1, z - 1) instanceof TileEntitySkull)
                    {
                        if (world.getTileEntity(x + 1, y + 1, z - 1) instanceof TileEntitySkull)
                        {
                            if (world.getTileEntity(x - 1, y + 1, z + 1) instanceof TileEntitySkull)
                            {
                                if (world.getTileEntity(x + 1, y + 1, z + 1) instanceof TileEntitySkull)
                                {
                                    tier = 2;
                                }
                            }
                        }
                    }

                    if (validAltar)
                    {
                        ExtendedPlayer.get(player).lastRitual = now;
                        EggProjectile eggShot = new EggProjectile(world, player.posX, y + 4, player.posZ, new Integer[]{x, y, z}, tier);
                        world.spawnEntityInWorld(eggShot);
                        eggShot.setVelocity(0, 1.5, 0);
                        stack.stackSize--;
                    }
                }
            }
        }
        return stack;
    }

    protected boolean isItTime()
    {
        long time = Minecraft.getMinecraft().theWorld.getWorldTime();
        double fraction = ((double) time / 24000) % 1;

        return (fraction >= 0.743d && fraction <= 0.756d);
    }

}