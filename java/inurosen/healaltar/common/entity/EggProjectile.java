package inurosen.healaltar.common.entity;

import inurosen.healaltar.common.core.Config;
import inurosen.healaltar.common.core.Helper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.Random;

public class EggProjectile extends EntityEgg
{
    private Integer[] coreBlock;
    private Integer tier;

    public EggProjectile(World p_i1781_1_, double p_i1781_2_, double p_i1781_4_, double p_i1781_6_, Integer[] cb, int t)
    {
        super(p_i1781_1_, p_i1781_2_, p_i1781_4_, p_i1781_6_);
        coreBlock = cb;
        tier = t;
        this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.bow", 1.0F, 0.5F);
    }


    protected void onImpact(MovingObjectPosition obj)
    {
        World world = worldObj;
        int x = coreBlock[0];
        int y = coreBlock[1];
        int z = coreBlock[2];

        if (!world.isRemote)
        {
            EntityLightning lightning = new EntityLightning(world, posX, posY-1, posZ);
            world.addWeatherEffect(lightning);
            world.setBlock(x, y, z, Blocks.cobblestone);
            if( tier == 2 )
            {
                world.setBlockToAir(x - 1, y + 1, z - 1);
                world.setBlockToAir(x + 1, y + 1, z - 1);
                world.setBlockToAir(x - 1, y + 1, z + 1);
                world.setBlockToAir(x + 1, y + 1, z + 1);

                ItemStack[] drops = new ItemStack[]{new ItemStack(Items.rotten_flesh, 1, 0), new ItemStack(Items.dye, 1, 15)};
                EntityItem flesh1 = new EntityItem( world, x - 1, y + 2, z - 1, drops[new Random().nextInt(drops.length)] );
                flesh1.delayBeforeCanPickup = 25;
                flesh1.fireResistance = 100;
                flesh1.setVelocity(Helper.randRange(-.4F, .0F), .1F, Helper.randRange(-.4F, .0F));
                world.spawnEntityInWorld(flesh1);

                EntityItem flesh2 = new EntityItem( world, x + 1, y + 2, z - 1, drops[new Random().nextInt(drops.length)] );
                flesh2.delayBeforeCanPickup = 25;
                flesh2.fireResistance = 100;
                flesh2.setVelocity(Helper.randRange(.0F, .4F), .1F, Helper.randRange(-.4F, .0F));
                world.spawnEntityInWorld(flesh2);

                EntityItem flesh3 = new EntityItem( world, x - 1, y + 2, z + 1, drops[new Random().nextInt(drops.length)] );
                flesh3.delayBeforeCanPickup = 25;
                flesh3.fireResistance = 100;
                flesh3.setVelocity(Helper.randRange(-.4F, .0F), .1F, Helper.randRange(.0F, .4F));
                world.spawnEntityInWorld(flesh3);

                EntityItem flesh4 = new EntityItem( world, x + 1, y + 2, z + 1, drops[new Random().nextInt(drops.length)] );
                flesh4.delayBeforeCanPickup = 25;
                flesh4.fireResistance = 100;
                flesh4.setVelocity(Helper.randRange(.0F, .4F), .1F, Helper.randRange(.0F, .4F));
                world.spawnEntityInWorld(flesh4);

            }
            EntityPlayer player = world.getClosestPlayer(posX, posY-2, posZ, 2);
            if(player != null)
            {
                if( world.isThundering() && Config.thunderAmplify )
                {
                    player.addPotionEffect(new PotionEffect(Potion.regeneration.id, Config.healDuration + (Config.healDuration - Config.healDuration / tier), Config.healAmplifier + 1 ));
                }
                else
                {
                    player.addPotionEffect(new PotionEffect(Potion.regeneration.id, Config.healDuration + (Config.healDuration - Config.healDuration / tier), Config.healAmplifier ));
                }
            }
            for(int i = 0; i < 6; i++)
            {
                EntityItem lapis = new EntityItem( world, x + .5, y + 1.5, z + .5, new ItemStack(Items.dye, 1, 4) );
                lapis.delayBeforeCanPickup = 25;
                lapis.fireResistance = 100;
                world.spawnEntityInWorld(lapis);
                lapis.setVelocity(Helper.randRange(-.4F, .4F), .5, Helper.randRange(-.4F, .4F));
            }
            this.setDead();
        }

    }
}

