package net.mcreator.discordchatsound;

import net.mcreator.discordchatsound.event.PlayerEvents;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ClientModInitializer;

import java.util.stream.Stream;
import java.util.Locale;

import java.nio.file.Path;
import java.nio.file.Files;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class DiscordChatSoundModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		cleanInvalidDevelopmentResources();
		// Start of user code block mod constructor
		// End of user code block mod constructor
		// Dispatch client player tick once per client tick, without a Player mixin.
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player != null) {
				PlayerEvents.END_PLAYER_TICK.invoker().onEndTick(client.player);
			}
		});
		// Start of user code block mod init
		// End of user code block mod init
	}

	private static void cleanInvalidDevelopmentResources() {
		FabricLoader loader = FabricLoader.getInstance();
		if (!loader.isDevelopmentEnvironment())
			return;
		loader.getModContainer("discord_chat_sound").ifPresent(container -> {
			for (Path root : container.getRootPaths()) {
				try {
					if (!Files.isDirectory(root))
						continue;
					String rootName = root.toAbsolutePath().normalize().toString().replace('\\', '/').toLowerCase(Locale.ROOT);
					// Never modify source assets. Loom normally exposes build/resources/main
					// as the resource root in a Gradle development run.
					if (rootName.contains("/src/"))
						continue;
					try (Stream<Path> paths = Files.walk(root)) {
						paths.filter(Files::isRegularFile).forEach(path -> {
							String relative = root.relativize(path).toString().replace('\\', '/');
							String lower = relative.toLowerCase(Locale.ROOT);
							if (relative.contains(" ") || lower.endsWith(".lnk")) {
								try {
									if (Files.deleteIfExists(path))
										System.out.println("[MCreator/Fabric 1.21.11] Ignored invalid development resource: " + relative);
								} catch (IOException ignored) {
								}
							}
						});
					}
				} catch (IOException ignored) {
				}
			}
		});
	}
	// Start of user code block mod methods
	// End of user code block mod methods
}