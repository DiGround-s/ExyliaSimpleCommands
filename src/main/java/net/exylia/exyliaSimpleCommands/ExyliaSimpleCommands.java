package net.exylia.exyliaSimpleCommands;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.exylia.exyliaSimpleCommands.command.AdminCommand;
import net.exylia.exyliaSimpleCommands.command.DynamicCommandExecutor;
import net.exylia.exyliaSimpleCommands.config.ConfigManager;
import org.slf4j.Logger;
import revxrsal.commands.velocity.VelocityCommandHandler;

import java.nio.file.Path;

@Plugin(
        id = "exyliasimplecommands",
        name = "ExyliaSimpleCommands",
        version = "1.0.0",
        authors = {"Exylia"}
)
@Getter
public class ExyliaSimpleCommands {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;

    private ConfigManager configManager;
    private VelocityCommandHandler commandHandler;
    private DynamicCommandExecutor dynamicExecutor;

    @Inject
    public ExyliaSimpleCommands(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        configManager = new ConfigManager(this);
        configManager.load();

        commandHandler = VelocityCommandHandler.create(this, server);
        commandHandler.register(new AdminCommand(this));

        dynamicExecutor = new DynamicCommandExecutor(this);
        dynamicExecutor.registerAll();

        logger.info("ExyliaSimpleCommands enabled - {} commands loaded", configManager.getCommands().size());
    }

    public void reload() {
        dynamicExecutor.unregisterAll();
        configManager.load();
        dynamicExecutor.registerAll();
        logger.info("ExyliaSimpleCommands reloaded - {} commands loaded", configManager.getCommands().size());
    }
}
