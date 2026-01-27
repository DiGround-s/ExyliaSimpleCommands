package net.exylia.exyliaSimpleCommands.config;

import lombok.Getter;
import net.exylia.exyliaSimpleCommands.ExyliaSimpleCommands;
import net.exylia.exyliaSimpleCommands.model.SimpleCommand;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ConfigManager {

    private final ExyliaSimpleCommands plugin;
    private final Path configPath;
    private List<SimpleCommand> commands;

    public ConfigManager(ExyliaSimpleCommands plugin) {
        this.plugin = plugin;
        this.configPath = plugin.getDataDirectory().resolve("commands.yml");
        this.commands = new ArrayList<>();
    }

    public void load() {
        try {
            Files.createDirectories(plugin.getDataDirectory());
            saveDefaultConfig();
            loadCommands();
        } catch (IOException e) {
            plugin.getLogger().error("Failed to load configuration", e);
        }
    }

    private void saveDefaultConfig() throws IOException {
        if (Files.exists(configPath)) return;
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("commands.yml")) {
            if (in != null) {
                Files.copy(in, configPath);
            }
        }
    }

    private void loadCommands() throws IOException {
        commands.clear();
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(configPath).build();
        ConfigurationNode root = loader.load();

        for (var entry : root.node("commands").childrenMap().entrySet()) {
            String name = entry.getKey().toString();
            ConfigurationNode node = entry.getValue();

            List<String> aliases = node.node("aliases").getList(String.class, List.of());
            ConfigurationNode permNode = node.node("permission");
            String permission = permNode.virtual() ? null : permNode.getString("");
            if (permission != null && permission.isEmpty()) permission = null;
            boolean centered = node.node("centered").getBoolean(false);
            List<String> messages = node.node("messages").getList(String.class, List.of());

            commands.add(new SimpleCommand(name, aliases, permission, centered, messages));
        }
    }
}
