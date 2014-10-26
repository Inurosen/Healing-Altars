package inurosen.healaltar.common.entity;

import inurosen.healaltar.common.HealingAltar;
import inurosen.healaltar.common.core.Config;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class EntityWaterBucket extends EntityItem
{
    private Integer phase = 0;
    public EntityWaterBucket(World p_i1709_1_, double p_i1709_2_, double p_i1709_4_, double p_i1709_6_)
    {
        super(p_i1709_1_);
        this.hoverStart = (float)(Math.random() * Math.PI * 2.0D);
        this.setSize(0.25F, 0.25F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(p_i1709_2_, p_i1709_4_, p_i1709_6_);
        this.rotationYaw = (float)(Math.random() * 360.0D);
        this.motionX = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
        this.motionY = 0.20000000298023224D;
        this.motionZ = (double)((float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D));
        this.isAirBorne = true;
    }

    public EntityWaterBucket(World p_i1710_1_, double p_i1710_2_, double p_i1710_4_, double p_i1710_6_, ItemStack p_i1710_8_)
    {
        this(p_i1710_1_, p_i1710_2_, p_i1710_4_, p_i1710_6_);
        this.setEntityItemStack(p_i1710_8_);
        this.lifespan = (p_i1710_8_.getItem() == null ? 6000 : p_i1710_8_.getItem().getEntityLifespan(p_i1710_8_, p_i1710_1_));
    }

    public boolean handleWaterMovement()
    {
        int x = (int) Math.floor(this.posX);
        int y = (int) (this.posY);
        int z = (int) Math.floor(this.posZ);
        if(this.phase == 0 && super.handleWaterMovement())
        {
            if(worldObj.getBlock(x, y, z) == Blocks.water)
            {
                for(Integer i = x - 1; i <= x + 1; i++)
                {
                    for(Integer j = z - 1; j <= z + 1; j++)
                    {
                        if(i != x || j != z)
                        {
                            this.phase = 1;
                        }
                    }
                }
            }
        }
        else if( this.phase == 1 )
        {
            EntityLightning lightning = new EntityLightning(worldObj, x, y-1, z);
            worldObj.addWeatherEffect(lightning);
            this.setDead();
            Random rand = new Random();
            if(rand.nextInt(101) <= Config.thunderChance)
            {
                worldObj.getWorldInfo().setRaining(true);
                worldObj.getWorldInfo().setThundering(true);
                worldObj.getWorldInfo().setThunderTime(Config.rainDuration);
                worldObj.getWorldInfo().setRainTime(Config.rainDuration);
            }
            else
            {
                worldObj.getWorldInfo().setThundering(false);
                worldObj.getWorldInfo().setRaining(true);
                worldObj.getWorldInfo().setRainTime(Config.rainDuration);
            }
            HealingAltar.lastRain = worldObj.getTotalWorldTime();
        }
        return this.inWater;
    }

}