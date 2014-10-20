package inurosen.healaltar.common.core;

import net.minecraftforge.common.config.Configuration;
import java.io.File;

public class Config
{
    public static Integer healDuration = 100;
    public static Integer healAmplifier = 0;
    public static Boolean thunderAmplify = true;

    public static void init(File file)
    {
        Configuration config = new Configuration( file );
        config.load();
        healDuration = config.getInt("Regen buff duration", "Buff", 100, 100, 1000, "Regen buff duration in ticks");
        healAmplifier = config.getInt("Regen buff amplifier", "Buff", 0, 0, 2, "Regen buff amplifier");
        thunderAmplify = config.getBoolean("Stronger regen during thunder", "Buff", true, "Regen buff is stronger during thunder");
        config.save();
    }
}
