package net.kwerdu.magicmod.mechanics.spell;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.List;

@Mod.EventBusSubscriber(modid = "magicmod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpellMagicCircleRenderer {
    private static long activateTime;
    private static final int castTime = 2000;
    private static Vec3 renderPosition;
    private static ResourceLocation CIRCLE_TEXTURE = new ResourceLocation("magicmod", "textures/spell/magic_circle_base.png");
    private static List<ResourceLocation> runeSequenceTexture;
    private static float centerCircleRadius = 1.5f;
    private static float runeSize = centerCircleRadius/3;
    private static float runeStep = runeSize/5;
    private static float timeBias;

    public static void activate(Vec3 position, List<ResourceLocation> runeTextures) {
        runeSequenceTexture = runeTextures;
        activateTime = System.currentTimeMillis();
        renderPosition = position;
        timeBias = 0;
    }

    private static boolean ShoudRender(){
        return System.currentTimeMillis() - activateTime <= castTime;
    }


    @SubscribeEvent
    public static void onRenderWorld(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS || !ShoudRender()) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();

        // Получаем позицию камеры
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        // Рассчитываем смещение относительно камеры
        float dx = (float)(renderPosition.x - cameraPos.x);
        float dy = (float)(renderPosition.y - cameraPos.y);
        float dz = (float)(renderPosition.z - cameraPos.z);

        poseStack.pushPose();
        poseStack.translate(dx, dy, dz);



        int packedLightmapUV = 0xF000F0;
        int colorRGBA = 0xFFFFFFFF;
        short overlayUV = 0;

        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(CIRCLE_TEXTURE));
        Matrix4f poseMatrix = poseStack.last().pose();
        Matrix3f normalMatrix = poseStack.last().normal();
        renderQuad(consumer, poseMatrix, normalMatrix, 2*centerCircleRadius, packedLightmapUV, colorRGBA, overlayUV);

        int runeCount = runeSequenceTexture.size(); // Общее количество рун
        double angleStep = 2 * Math.PI / runeCount; // Шаг угла между рунами

        for (int i = 0; i < runeCount; i++) {
            double angle = angleStep * i + Math.PI / 2 + timeBias; // +π/2 для старта сверху
            consumer = buffer.getBuffer(RenderType.entityCutout(runeSequenceTexture.get(i)));
            poseStack.pushPose();
            poseStack.translate((centerCircleRadius + runeSize/2 + runeStep) * Math.cos(angle), 0, (centerCircleRadius + runeSize/2 + runeStep) * Math.sin(angle));
            poseMatrix = poseStack.last().pose();
            normalMatrix = poseStack.last().normal();
            renderQuad(consumer, poseMatrix, normalMatrix, runeSize, packedLightmapUV, colorRGBA, overlayUV);
            poseStack.popPose();
        }


        timeBias += 17.5*(Math.PI*2)/castTime;

        poseStack.popPose();
        buffer.endBatch();
    }

    private static void renderQuad(VertexConsumer consumer, Matrix4f poseMatrix, Matrix3f normalMatrix, float size, int packedLightmapUV, int colorRGBA, short overlayUV){
        consumer.vertex(poseMatrix, -size/2, 0.01f, -size/2) // Левый-нижний
                .color(colorRGBA)
                .uv(0, 1)
                .overlayCoords(overlayUV)
                .uv2(packedLightmapUV)
                .normal(normalMatrix, 0, 1, 0)
                .endVertex();


        consumer.vertex(poseMatrix, -size/2, 0.01f, size/2)   // Левый-верхний
                .color(colorRGBA)
                .uv(0, 0)
                .overlayCoords(overlayUV)
                .uv2(packedLightmapUV)
                .normal(normalMatrix, 0, 1, 0)
                .endVertex();

        consumer.vertex(poseMatrix, size/2, 0.01f, size/2)    // Правый-верхний
                .color(colorRGBA)
                .uv(1, 0)
                .overlayCoords(overlayUV)
                .uv2(packedLightmapUV)
                .normal(normalMatrix, 0, 1, 0)
                .endVertex();

        consumer.vertex(poseMatrix, size/2, 0.01f, -size/2)  // Правый-нижний
                .color(colorRGBA)
                .uv(1, 1)
                .overlayCoords(overlayUV)
                .uv2(packedLightmapUV)
                .normal(normalMatrix, 0, 1, 0)
                .endVertex();
    }
}