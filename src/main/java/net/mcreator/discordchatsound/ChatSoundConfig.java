package net.mcreator.discordchatsound;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ChatSoundConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static ChatSoundConfig INSTANCE = new ChatSoundConfig();

    public boolean enabled = true;

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("chatsound.json");
    }

    public static void load() {
        if (Files.exists(path())) {
            try { INSTANCE = GSON.fromJson(Files.readString(path()), ChatSoundConfig.class); }
            catch (IOException ignored) {}
        }
        save();
    }

    public static void save() {
        try {
            Files.createDirectories(path().getParent());
            Files.writeString(path(), GSON.toJson(INSTANCE));
        } catch (IOException ignored) {}
    }

    public float volume = 1.0F;
}
