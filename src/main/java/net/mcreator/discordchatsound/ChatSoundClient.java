package net.mcreator.discordchatsound;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class ChatSoundClient implements ClientModInitializer {
    public static final SoundEvent CHAT_ALERT =
        SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("discordchatsound", "chat_alert"));

    @Override
    public void onInitializeClient() {
        ChatSoundConfig.load();

        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, timestamp) -> {
            if (!ChatSoundConfig.INSTANCE.enabled) return;
            Minecraft mc = Minecraft.getInstance();
            // свои сообщения пропускаем
            if (mc.player != null && sender != null && sender.id().equals(mc.player.getUUID())) return;
            play();
        });

        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (ChatSoundConfig.INSTANCE.enabled) play();
        });
    }

    private static void play() {
    Minecraft.getInstance().getSoundManager()
        .play(SimpleSoundInstance.forUI(CHAT_ALERT, 1.0F, ChatSoundConfig.INSTANCE.volume));
    }
}
