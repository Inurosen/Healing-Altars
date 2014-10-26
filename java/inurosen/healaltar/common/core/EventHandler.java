package inurosen.healaltar.common.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import inurosen.healaltar.common.HealingAltar;
import inurosen.healaltar.common.entity.EggProjectile;
import inurosen.healaltar.common.entity.EntityWaterBucket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;

public class EventHandler
{
    @SubscribeEvent
    public void onItemToss(ItemTossEvent event)
    {
        if(Config.enableRainAltar)
        {
            EntityItem entityItem = event.entityItem;
            World world = event.player.worldObj;
            long now = world.getTotalWorldTime();
            long diff;
            if( now < 72000 && HealingAltar.lastRain == 0 )
            {
                diff = 72000;
            }
            else
            {
                diff = now - HealingAltar.lastRain;
            }

            if( !world.isRemote && diff >= 72000 && entityItem.getEntityItem().getItem().delegate.name().equals("minecraft:water_bucket") )
            {
                EntityWaterBucket bucket = new EntityWaterBucket( world, entityItem.posX, entityItem.posY, entityItem.posZ, new ItemStack(Items.water_bucket, 1) );
                bucket.delayBeforeCanPickup = 25;
                bucket.fireResistance = 100;
                bucket.setVelocity(entityItem.motionX, entityItem.motionY, entityItem.motionZ);
                world.spawnEntityInWorld(bucket);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void eggThrowToTheMoon(PlayerInteractEvent event)
    {
        if( event.entityPlayer.getHeldItem() == null) return;
        Item egg = event.entityPlayer.getHeldItem().getItem();
        World world = event.entityPlayer.worldObj;
        EntityPlayer player = event.entityPlayer;
        boolean validAltar = false;
        int tier = 1;
        long time = world.getWorldTime();
        long now = world.getTotalWorldTime();
        long diff;
        if( now < 10800 && HealingAltar.lastUse == 0)
        {
            diff = 10800;
        }
        else
        {
            diff = now - HealingAltar.lastUse;
        }
        Vec3 look = player.getLookVec();
        if( !world.isRemote && diff >= 10800 && time >= 17500 && time <= 18500 && egg == Items.egg && look.yCoord == 1)
        {
            int x = (int) Math.floor(player.posX);
            int y = (int) (player.posY - player.getYOffset()) - 1;
            int z = (int) Math.floor(player.posZ);

            if(Helper.getHighestBlockAt(world, x, y, z) == Blocks.lapis_block)
            {
                // Altar body
                if(world.getBlock(x - 1, y, z - 1) == Blocks.brick_block)
                {
                    if(world.getBlock(x, y, z - 1) == Blocks.brick_stairs)
                    {
                        if(world.getBlock(x + 1, y, z - 1) == Blocks.brick_block)
                        {
                            if(world.getBlock(x - 1, y, z) == Blocks.brick_stairs)
                            {
                                if(world.getBlock(x + 1, y, z) == Blocks.brick_stairs)
                                {
                                    if(world.getBlock(x - 1, y, z + 1) == Blocks.brick_block)
                                    {
                                        if(world.getBlock(x, y, z + 1) == Blocks.brick_stairs)
                                        {
                                            if(world.getBlock(x + 1, y, z + 1) == Blocks.brick_block)
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
                if(world.getTileEntity(x - 1, y + 1, z - 1) instanceof TileEntitySkull)
                {
                    if(world.getTileEntity(x + 1, y + 1, z - 1) instanceof TileEntitySkull)
                    {
                        if(world.getTileEntity(x - 1, y + 1, z + 1) instanceof TileEntitySkull)
                        {
                            if(world.getTileEntity(x + 1, y + 1, z + 1) instanceof TileEntitySkull)
                            {
                                tier = 2;
                            }
                        }
                    }
                }

                if(validAltar)
                {
                    HealingAltar.lastUse = now;
                    EggProjectile eggShot = new EggProjectile( world, player.posX, y + 4, player.posZ, new Integer[]{x, y, z}, tier);
                    world.spawnEntityInWorld(eggShot);
                    eggShot.setVelocity(0, 1.5, 0);
                    event.setCanceled(true);
                }
            }
        }
    }
}
