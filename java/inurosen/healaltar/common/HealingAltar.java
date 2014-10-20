package inurosen.healaltar.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import inurosen.healaltar.common.core.Config;

@Mod(modid = HealingAltar.MODID, version = HealingAltar.VERSION, name = HealingAltar.NAME)

public class HealingAltar
{
    public static final String NAME = "Healing Altar";
    public static final String MODID = "healaltar";
    public static final String VERSION = "1.0.1";
    public static long lastUse = 0;

    @EventHandler
    public static void preInit ( FMLPreInitializationEvent event ) {
        Config.init(event.getSuggestedConfigurationFile());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new inurosen.healaltar.common.core.EventHandler());
    }

}

