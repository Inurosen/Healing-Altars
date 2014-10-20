package inurosen.healaltar.common.core;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import java.util.Random;

public class Helper
{
    public static float randRange(float x, float y)
    {
        Random rand = new Random();
        Float f = rand.nextFloat();
        return f * (x - y) + y;
    }

    public static Block getHighestBlockAt(World world, int x, int y, int z)
    {
        Block b = Blocks.air;
        int h = world.getHeight();
        for(int i = y; i <= h; i++)
        {
            if(!world.isAirBlock(x, i, z))
            {
                b = world.getBlock(x, i, z);
            }
        }
        return b;
    }
}
