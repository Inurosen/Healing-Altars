package inurosen.healaltar.common.core;

import inurosen.healaltar.common.gui.GuiSoulHearts;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy
{
    @Override
    public void registerRenderers() {
        MinecraftForge.EVENT_BUS.register(new GuiSoulHearts(Minecraft.getMinecraft()));
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return (ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
    }
}