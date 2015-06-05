package inurosen.healaltar.common.core;

import cpw.mods.fml.client.registry.RenderingRegistry;
import inurosen.healaltar.common.HealingAltar;
import inurosen.healaltar.common.entity.EggProjectile;
import inurosen.healaltar.common.gui.GuiSoulHearts;
import inurosen.healaltar.common.renderer.RunicEggRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers() {
        MinecraftForge.EVENT_BUS.register(new GuiSoulHearts(Minecraft.getMinecraft()));
        RenderingRegistry.registerEntityRenderingHandler(EggProjectile.class, new RunicEggRenderer(HealingAltar.runicEgg));
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
    }
}