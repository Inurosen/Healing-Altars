package inurosen.healaltar.common.core;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import inurosen.healaltar.common.HealingAltar;
import inurosen.healaltar.common.entity.EntityWaterBucket;
import inurosen.healaltar.common.entity.ExtendedPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class EventHandler
{
    @SubscribeEvent
    public void onItemToss(ItemTossEvent event)
    {
        if (Config.enableRainAltar)
        {
            EntityItem entityItem = event.entityItem;
            World world = event.player.worldObj;
            long now = world.getTotalWorldTime();
            long diff;
            if (now < 72000 && HealingAltar.lastRain == 0)
            {
                diff = 72000;
            }
            else
            {
                diff = now - HealingAltar.lastRain;
            }

            if (!world.isRemote && diff >= 72000 && entityItem.getEntityItem().getItem().delegate.name().equals("minecraft:water_bucket"))
            {
                EntityWaterBucket bucket = new EntityWaterBucket(world, entityItem.posX, entityItem.posY, entityItem.posZ, new ItemStack(Items.water_bucket, 1));
                bucket.delayBeforeCanPickup = 25;
                bucket.fireResistance = 100;
                bucket.setVelocity(entityItem.motionX, entityItem.motionY, entityItem.motionZ);
                world.spawnEntityInWorld(bucket);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            if (ExtendedPlayer.get((EntityPlayer) event.entity) == null)
                ExtendedPlayer.register((EntityPlayer) event.entity);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer)
        {
            ExtendedPlayer.loadProxyData((EntityPlayer) event.entity);
        }
    }

    @SubscribeEvent
    public void onLivingDeathEvent(LivingDeathEvent event)
    {
        if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer)
        {
            ExtendedPlayer.get((EntityPlayer) event.entity).setSoulHearts(0);
            ExtendedPlayer.saveProxyData((EntityPlayer) event.entity);
        }
    }

    @SubscribeEvent
    public void onLivingHurtEvent(LivingHurtEvent event)
    {
        if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer)
        {
            float damage = event.ammount;
            float soulHearts = ExtendedPlayer.get((EntityPlayer) event.entity).getSoulHearts();
            float soulDamage = damage * Config.soulDamageFactor;
            float damageDiff = soulHearts - soulDamage;
            if (damageDiff < 0)
            {
                ExtendedPlayer.get((EntityPlayer) event.entity).setSoulHearts(0);
                event.ammount = (-1 * damageDiff) / Config.soulDamageFactor;
            }
            else
            {
                ExtendedPlayer.get((EntityPlayer) event.entity).setSoulHearts(damageDiff);
                event.ammount = 0;
            }
        }
    }

}
