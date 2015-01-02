package inurosen.healaltar.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import inurosen.healaltar.common.core.CommonProxy;
import inurosen.healaltar.common.items.SoulHeart;
import inurosen.healaltar.common.network.PacketDispatcher;
import inurosen.healaltar.common.potion.SoulRegen;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.MinecraftForge;
import inurosen.healaltar.common.core.Config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Mod(modid = HealingAltar.MODID, version = HealingAltar.VERSION, name = HealingAltar.NAME)

public class HealingAltar
{
    public static final String NAME = "Healing Altar";
    public static final String MODID = "HealingAltar";
    public static final String VERSION = "1.1.3";
    public static long lastUse = 0;
    public static long lastRain = 0;
    public static Potion soulRegen;

    @SidedProxy(clientSide = "inurosen.healaltar.common.core.ClientProxy", serverSide = "inurosen.healaltar.common.core.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public static void preInit ( FMLPreInitializationEvent event ) {
        Config.init(event.getSuggestedConfigurationFile());
        Potion[] potionTypes = null;

        for (Field f : Potion.class.getDeclaredFields()) {
            f.setAccessible(true);
            try {
                if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a")) {
                    Field modfield = Field.class.getDeclaredField("modifiers");
                    modfield.setAccessible(true);
                    modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);

                    potionTypes = (Potion[])f.get(null);
                    final Potion[] newPotionTypes = new Potion[256];
                    System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
                    f.set(null, newPotionTypes);
                }
            }
            catch (Exception e) {
                System.err.println("Severe error, please report this to the mod author:");
                System.err.println(e);
            }
        }
        PacketDispatcher.registerPackets();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new inurosen.healaltar.common.core.EventHandler());
        GameRegistry.registerItem(new SoulHeart(), "soulHeart");
        soulRegen = (new SoulRegen(32, false, 0)).setIconIndex(7, 0).setPotionName("healingAltar.soulRegen");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.registerRenderers();
    }

}

