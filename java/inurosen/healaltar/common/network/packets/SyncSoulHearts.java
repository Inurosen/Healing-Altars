package inurosen.healaltar.common.network.packets;

import inurosen.healaltar.common.entity.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class SyncSoulHearts implements IMessage
{
    private NBTTagCompound data;

    public SyncSoulHearts() {}

    public SyncSoulHearts(EntityPlayer player)
    {
        data = new NBTTagCompound();
        ExtendedPlayer.get(player).saveNBTData(data);
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        data = ByteBufUtils.readTag(buffer);
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        ByteBufUtils.writeTag(buffer, data);
    }

    public static class Handler extends AbstractClientMessageHandler<SyncSoulHearts>
    {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage handleClientMessage(EntityPlayer player, SyncSoulHearts message, MessageContext ctx)
        {
            ExtendedPlayer.get(player).loadNBTData(message.data);
            return null;
        }
    }
}