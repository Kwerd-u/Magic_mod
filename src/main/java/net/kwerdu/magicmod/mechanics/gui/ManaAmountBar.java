package net.kwerdu.magicmod.mechanics.gui;

import net.kwerdu.magicmod.mechanics.mana.ClientManaData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "magicmod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ManaAmountBar {

    private static final int width = 15; // Ширина прогресс-бара
    private static final int height = 130; // Высота прогресс-бара
    private static final int x = 2; // Отступ слева

    private static int animatedHeight = 0; // Текущая высота анимации
    private static int targetHeight = 0; // Целевая высота (итоговое значение Маны)
    private static long lastUpdateTime = 0; // Время последнего обновления
    private static final int sleepDuration = 20; // Задержка анимации (в миллисекундах)


    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        GuiGraphics guiGraphics = event.getGuiGraphics();
        Player player = minecraft.player;

        // Проверка на null
        if (player == null) {
            return;
        }

        // Получение текущего масштаба интерфейса
        int guiScale = minecraft.options.guiScale().get();
        int actualGuiScale = guiScale;

        // Если масштаб автоматический (0), вычисляем фактический масштаб
        if (guiScale == 0) {
            int windowWidth = minecraft.getWindow().getWidth();
            int windowHeight = minecraft.getWindow().getHeight();
            int scaleFactor = Math.max(1, Math.min(windowWidth / 320, windowHeight / 240));
            actualGuiScale = Math.min(4, scaleFactor); // Ограничение до 4
        }

        // Корректировка координаты y с учётом масштаба
        int currentY = 0;
        switch (actualGuiScale) {
            case 1:
                currentY = (int) (minecraft.getWindow().getHeight() - height * 1.05);
                break;
            case 2:
                currentY = (int) (minecraft.getWindow().getHeight() / 2 - height * 1.05);
                break;
            case 3:
                currentY = (int) (minecraft.getWindow().getHeight() * 0.33 - height * 1.05);
                break;
            case 4:
                currentY = (int) (minecraft.getWindow().getHeight() / 4 - height * 1.05);
        }

        // Получение текущего и максимального значения Маны
        int current = ClientManaData.getCurrentMana();
        int max = ClientManaData.getMaxMana();
        //player.sendSystemMessage(Component.literal(String.valueOf(ClientManaData.getMaxMana())));
        if (max != 0) {
            // Вычисление целевой высоты
            targetHeight = (int) ((float) current / max * height);

            // Плавное изменение высоты
            if (System.currentTimeMillis() - lastUpdateTime >= sleepDuration) {
                if (animatedHeight < targetHeight) {
                    animatedHeight++;
                } else if (animatedHeight > targetHeight) {
                    animatedHeight--;
                }
                lastUpdateTime = System.currentTimeMillis();
            }

            // Рендеринг фона (пустая текстура)
            guiGraphics.blit(
                    new ResourceLocation("magicmod", "textures/gui/empty_mana_bar.png"), // Текстура
                    x, currentY, // Координаты
                    0, 0, // Смещение текстуры (uOffset, vOffset)
                    width, height, // Ширина и высота
                    width, height // Ширина и высота текстуры
            );

            // Рендеринг заполненной части
            guiGraphics.blit(
                    new ResourceLocation("magicmod", "textures/gui/full_mana_bar.png"), // Текстура
                    x, currentY + (height - animatedHeight), // Координаты (смещение по Y)
                    0, height - animatedHeight, // Смещение текстуры (uOffset, vOffset)
                    width, animatedHeight, // Ширина и высота отрисовываемой части
                    width, height // Ширина и высота текстуры
            );

            // Рендеринг плейсхолдера (потраченная мана)
            if (animatedHeight != targetHeight) {
                int placeholderHeight = animatedHeight - targetHeight;
                int placeholderY = currentY + (height - Math.max(animatedHeight, targetHeight));

                guiGraphics.blit(
                        new ResourceLocation("magicmod", "textures/gui/placeholder_mana_bar.png"), // Текстура плейсхолдера
                        x, placeholderY, // Координаты
                        0, height - Math.max(targetHeight, animatedHeight), // Смещение текстуры (uOffset, vOffset)
                        width, Math.abs(placeholderHeight), // Ширина и высота отрисовываемой части
                        width, height // Ширина и высота текстуры
                );
            }
        }
    }
}
