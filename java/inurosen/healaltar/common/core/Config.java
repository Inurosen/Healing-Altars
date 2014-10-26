package inurosen.healaltar.common.core;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

public class Config
{
    public static Integer healDuration = 150;
    public static Integer healAmplifier = 0;
    public static Boolean thunderAmplify = true;
    public static Boolean enableRainAltar = false;
    public static Integer rainDuration = 3600;
    public static Integer thunderChance = 10;

    public static void init(File file)
    {
        Configuration config = new Configuration( file );
        config.load();
        healDuration = config.getInt("Regen buff duration", "Buff", 150, 100, 1000, "Regen buff duration in ticks");
        healAmplifier = config.getInt("Regen buff amplifier", "Buff", 0, 0, 2, "Regen buff amplifier");
        thunderAmplify = config.getBoolean("Stronger regen during thunder", "Buff", true, "Regen buff is stronger during thunder");
        enableRainAltar = config.getBoolean("Enable altar of rain", "Rain Altar", false, "Enables summoning rain");
        rainDuration = config.getInt("Rain duration", "Rain Altar", 3600, 100, 24000, "Rain duration in ticks");
        thunderChance = config.getInt("Thunder chance", "Rain Altar", 10, 1, 100, "Chance to summon thunder instead of rain");
        config.save();
    }
}
