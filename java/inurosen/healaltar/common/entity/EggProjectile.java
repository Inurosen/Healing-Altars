package inurosen.healaltar.common.entity;

import inurosen.healaltar.common.HealingAltar;
import inurosen.healaltar.common.core.Config;
import inurosen.healaltar.common.core.Helper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.Random;

public class EggProjectile extends EntityThrowable
{
    private Integer[] coreBlock = new Integer[]{0, 0, 0};
    private Integer tier = 1;


    public EggProjectile(World world, double p_i1781_2_, double p_i1781_4_, double p_i1781_6_, Integer[] cb, int t)
    {
        super(world, p_i1781_2_, p_i1781_4_, p_i1781_6_);
        coreBlock = cb;
        tier = t;
        this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "random.bow", 1.0F, 0.5F);
    }

    public EggProjectile(World world)
    {
        super(world);
    }

    public EggProjectile(World world, EntityLivingBase entityLiving)
    {
        super(world, entityLiving);
    }

    public void onUpdate()
    {
        super.onUpdate();
        this.worldObj.spawnParticle(
            "happyVillager",
            this.posX + rand.nextFloat() * this.width * 2.0F - this.width,
            this.posY + rand.nextFloat(),
            this.posZ + rand.nextFloat() * this.width * 2.0F - this.width,
            0,
            0,
            0);
    }

    @Override
    protected void onImpact(MovingObjectPosition obj)
    {
        if (!this.worldObj.isRemote && coreBlock != null)
        {
            int x = coreBlock[0];
            int y = coreBlock[1];
            int z = coreBlock[2];

            if(this.worldObj.getBlock(x, y, z) == Blocks.lapis_block)
            {
                EntityLightning lightning = new EntityLightning(this.worldObj, posX, posY - 1, posZ);
                this.worldObj.addWeatherEffect(lightning);

                this.worldObj.setBlock(x, y, z, Blocks.cobblestone);
                if (tier == 2)
                {
                    this.worldObj.setBlockToAir(x - 1, y + 1, z - 1);
                    this.worldObj.setBlockToAir(x + 1, y + 1, z - 1);
                    this.worldObj.setBlockToAir(x - 1, y + 1, z + 1);
                    this.worldObj.setBlockToAir(x + 1, y + 1, z + 1);

                    ItemStack[] drops = new ItemStack[]{new ItemStack(Items.rotten_flesh, 1, 0), new ItemStack(Items.dye, 1, 15)};
                    EntityItem flesh1 = new EntityItem(this.worldObj, x - 1, y + 2, z - 1, drops[new Random().nextInt(drops.length)]);
                    flesh1.delayBeforeCanPickup = 25;
                    flesh1.fireResistance = 100;
                    flesh1.setVelocity(Helper.randRange(-.4F, .0F), .1F, Helper.randRange(-.4F, .0F));
                    this.worldObj.spawnEntityInWorld(flesh1);

                    EntityItem flesh2 = new EntityItem(this.worldObj, x + 1, y + 2, z - 1, drops[new Random().nextInt(drops.length)]);
                    flesh2.delayBeforeCanPickup = 25;
                    flesh2.fireResistance = 100;
                    flesh2.setVelocity(Helper.randRange(.0F, .4F), .1F, Helper.randRange(-.4F, .0F));
                    this.worldObj.spawnEntityInWorld(flesh2);

                    EntityItem flesh3 = new EntityItem(this.worldObj, x - 1, y + 2, z + 1, drops[new Random().nextInt(drops.length)]);
                    flesh3.delayBeforeCanPickup = 25;
                    flesh3.fireResistance = 100;
                    flesh3.setVelocity(Helper.randRange(-.4F, .0F), .1F, Helper.randRange(.0F, .4F));
                    this.worldObj.spawnEntityInWorld(flesh3);

                    EntityItem flesh4 = new EntityItem(this.worldObj, x + 1, y + 2, z + 1, drops[new Random().nextInt(drops.length)]);
                    flesh4.delayBeforeCanPickup = 25;
                    flesh4.fireResistance = 100;
                    flesh4.setVelocity(Helper.randRange(.0F, .4F), .1F, Helper.randRange(.0F, .4F));
                    this.worldObj.spawnEntityInWorld(flesh4);

                }
                EntityPlayer player = this.worldObj.getClosestPlayer(posX, posY - 2, posZ, 2);
                if (player != null)
                {
                    if (this.worldObj.isThundering() && Config.thunderAmplify)
                    {
                        player.addPotionEffect(new PotionEffect(HealingAltar.soulRegen.id, Config.healDuration + (Config.healDuration - Config.healDuration / tier), Config.healAmplifier + 1));
                    }
                    else
                    {
                        player.addPotionEffect(new PotionEffect(HealingAltar.soulRegen.id, Config.healDuration + (Config.healDuration - Config.healDuration / tier), Config.healAmplifier));
                    }
                }
                for (int i = 0; i < 6; i++)
                {
                    EntityItem lapis = new EntityItem(this.worldObj, x + .5, y + 1.5, z + .5, new ItemStack(Items.dye, 1, 4));
                    lapis.delayBeforeCanPickup = 25;
                    lapis.fireResistance = 100;
                    this.worldObj.spawnEntityInWorld(lapis);
                    lapis.setVelocity(Helper.randRange(-.4F, .4F), .5, Helper.randRange(-.4F, .4F));
                }
            }
            else
            {
                EntityItem runicEgg = new EntityItem(this.worldObj, x + .5, y + 1.5, z + .5, new ItemStack(HealingAltar.runicEgg, 1));
                runicEgg.delayBeforeCanPickup = 25;
                this.worldObj.spawnEntityInWorld(runicEgg);
            }
            this.setDead();
        }

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound p_70014_1_)
    {
        super.writeEntityToNBT(p_70014_1_);
        p_70014_1_.setInteger("xCore", this.coreBlock[0]);
        p_70014_1_.setInteger("yCore", this.coreBlock[1]);
        p_70014_1_.setInteger("zCore", this.coreBlock[2]);
        p_70014_1_.setInteger("tier", this.tier);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound p_70037_1_)
    {
        super.readEntityFromNBT(p_70037_1_);
        this.coreBlock[0] = p_70037_1_.getInteger("xCore");
        this.coreBlock[1] = p_70037_1_.getInteger("yCore");
        this.coreBlock[2] = p_70037_1_.getInteger("zCore");
        this.tier = p_70037_1_.getInteger("tier");
    }

}

