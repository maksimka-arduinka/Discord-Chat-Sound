package net.mcreator.discordchatsound;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ChatSoundConfigScreen extends Screen {
    private final Screen parent;

    public ChatSoundConfigScreen(Screen parent) {
        super(Component.literal("Chat Sound"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        addRenderableWidget(Button.builder(
                Component.literal(ChatSoundConfig.INSTANCE.enabled ? "Звук: Вкл" : "Звук: Выкл"),
                b -> {
                    ChatSoundConfig.INSTANCE.enabled = !ChatSoundConfig.INSTANCE.enabled;
                    ChatSoundConfig.save();
                    b.setMessage(Component.literal(ChatSoundConfig.INSTANCE.enabled ? "Звук: Вкл" : "Звук: Выкл"));
                })
            .bounds(this.width / 2 - 100, this.height / 2 - 50, 200, 20)
            .build());

        AbstractSliderButton volumeSlider = new AbstractSliderButton(
                this.width / 2 - 100, this.height / 2 - 20, 200, 20,
                Component.empty(), ChatSoundConfig.INSTANCE.volume) {
            @Override
            protected void updateMessage() {
                setMessage(Component.literal("Громкость: " + Math.round(value * 100) + "%"));
            }

            @Override
            protected void applyValue() {
                ChatSoundConfig.INSTANCE.volume = (float) value;
                ChatSoundConfig.save();
            }
        };
        volumeSlider.setMessage(Component.literal("Громкость: " + Math.round(ChatSoundConfig.INSTANCE.volume * 100) + "%"));

        addRenderableWidget(volumeSlider);

        addRenderableWidget(Button.builder(Component.literal("Готово"), b -> onClose())
            .bounds(this.width / 2 - 100, this.height / 2 + 10, 200, 20)
            .build());
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 80, 0xFFFFFF);
    }
}
