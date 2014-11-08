package inurosen.healaltar.common.gui;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import inurosen.healaltar.common.entity.ExtendedPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class GuiSoulHearts extends GuiIngameForge
{

    private Minecraft mc;
    public final ResourceLocation soulHeart = new ResourceLocation("healingaltar", "textures/gui/soulheart.png");
    public static int updateCounter = 0;
    private Timer timer = new Timer(20.0F);


    public GuiSoulHearts(Minecraft mc)
    {
        super(mc);
        this.mc = mc;
    }

    @SubscribeEvent
    public void onRenderExperienceBar(RenderGameOverlayEvent event)
    {
        if(event.isCancelable() || event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE || mc.thePlayer.capabilities.isCreativeMode)
        {
            return;
        }
        int xs = event.resolution.getScaledWidth();
        int ys = event.resolution.getScaledHeight();
        float soulHealth = ExtendedPlayer.get(this.mc.thePlayer).getSoulHearts();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, .85F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        this.mc.getTextureManager().bindTexture(soulHeart);
        for (int i = MathHelper.ceiling_float_int(20 / 2.0F) - 1; i >= 0; --i)
        {
            int x = (xs / 2 - 91) + i % 10 * 8;
            int y = ys - 39;
            if (i * 2 + 1 < soulHealth)
            {
                this.drawTexturedModalRect(x, y, 0, 0, 9, 9);
            }
            else if (i * 2 + 1 == soulHealth)
            {
                this.drawTexturedModalRect(x, y, 9, 0, 9, 9);
            }
        }
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
