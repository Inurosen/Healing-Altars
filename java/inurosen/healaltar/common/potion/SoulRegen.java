package inurosen.healaltar.common.potion;

import inurosen.healaltar.common.entity.ExtendedPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;

public class SoulRegen extends Potion {

    public SoulRegen(int par1, boolean par2, int par3)
    {
        super(par1, par2, par3);
    }

    public Potion setIconIndex(int par1, int par2)
    {
        super.setIconIndex(par1, par2);
        return this;
    }

    public void performEffect(EntityLivingBase entity, int par2)
    {
        if(!entity.worldObj.isRemote)
        {
            if (entity.getHealth() < entity.getMaxHealth())
            {
                entity.heal(1.0F);
            }
            else
            {
                if(entity instanceof EntityPlayer)
                {
                    float current = ExtendedPlayer.get((EntityPlayer) entity).getSoulHearts();
                    if(current < 20)
                    {
                        ExtendedPlayer.get((EntityPlayer) entity).setSoulHearts(current + 1);
                    }
                }
            }
        }
    }

    public boolean isReady(int p_76397_1_, int p_76397_2_)
    {
        int k;

        k = 50 >> p_76397_2_;
        return k > 0 ? p_76397_1_ % k == 0 : true;
    }

}