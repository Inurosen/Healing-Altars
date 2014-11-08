package inurosen.healaltar.common.entity;

import inurosen.healaltar.common.core.CommonProxy;
import inurosen.healaltar.common.network.PacketDispatcher;
import inurosen.healaltar.common.network.packets.SyncSoulHearts;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedPlayer implements IExtendedEntityProperties
{
    public final static String EXT_PROP_NAME = "HealingAltar";

    private final EntityPlayer player;

    public static final int HEARTS_WATCHER = 20;
    public long lastRitual = 0;

    public ExtendedPlayer(EntityPlayer player) {
        this.player = player;
        this.player.getDataWatcher().addObject(HEARTS_WATCHER, 0F);
    }

    public static final void register(EntityPlayer player) {
        player.registerExtendedProperties(ExtendedPlayer.EXT_PROP_NAME, new ExtendedPlayer(player));
    }

    public static final ExtendedPlayer get(EntityPlayer player) {
        return (ExtendedPlayer) player.getExtendedProperties(EXT_PROP_NAME);
    }

    @Override
    public final void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound properties = new NBTTagCompound();

        properties.setFloat("soulHearts", player.getDataWatcher().getWatchableObjectFloat(HEARTS_WATCHER));
        properties.setFloat("lastRitual", lastRitual);

        compound.setTag(EXT_PROP_NAME, properties);
    }

    @Override
    public final void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound properties = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
        player.getDataWatcher().updateObject(HEARTS_WATCHER, properties.getFloat("soulHearts"));
        lastRitual = properties.getLong("lastRitual");
    }

    @Override
    public void init(Entity entity, World world) {}

    public final float getSoulHearts() {
        return player.getDataWatcher().getWatchableObjectFloat(HEARTS_WATCHER);
    }

    public final void setSoulHearts(float amount) {
        player.getDataWatcher().updateObject(HEARTS_WATCHER, amount);
    }

    private static final String getSaveKey(EntityPlayer player) {
        return player.getCommandSenderName() + ":" + EXT_PROP_NAME;
    }

    public static final void saveProxyData(EntityPlayer player) {
        NBTTagCompound savedData = new NBTTagCompound();
        ExtendedPlayer.get(player).saveNBTData(savedData);
        CommonProxy.storeEntityData(getSaveKey(player), savedData);
    }

    public static final void loadProxyData(EntityPlayer player) {
        ExtendedPlayer playerData = ExtendedPlayer.get(player);
        NBTTagCompound savedData = CommonProxy.getEntityData(getSaveKey(player));
        if (savedData != null) { playerData.loadNBTData(savedData); }
        PacketDispatcher.sendTo(new SyncSoulHearts(player), (EntityPlayerMP) player);
    }
}