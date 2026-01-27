package net.exylia.exyliaSimpleCommands.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.exylia.exyliaSimpleCommands.ExyliaSimpleCommands;
import net.exylia.exyliaSimpleCommands.util.MessageUtil;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

public class DynamicCommandExecutor {

    private final ExyliaSimpleCommands plugin;
    private final List<String> registeredCommands;

    public DynamicCommandExecutor(ExyliaSimpleCommands plugin) {
        this.plugin = plugin;
        this.registeredCommands = new ArrayList<>();
    }

    public void registerAll() {
        for (var cmd : plugin.getConfigManager().getCommands()) {
            List<String> allNames = new ArrayList<>();
            allNames.add(cmd.name());
            allNames.addAll(cmd.aliases());

            SimpleCommand executor = new CommandExecutor(cmd);

            plugin.getServer().getCommandManager().register(
                    cmd.name(),
                    executor,
                    cmd.aliases().toArray(new String[0])
            );

            registeredCommands.add(cmd.name());
        }
    }

    public void unregisterAll() {
        for (String name : registeredCommands) {
            plugin.getServer().getCommandManager().unregister(name);
        }
        registeredCommands.clear();
    }

    private record CommandExecutor(net.exylia.exyliaSimpleCommands.model.SimpleCommand cmd) implements SimpleCommand {

        @Override
        public void execute(Invocation invocation) {
            CommandSource source = invocation.source();

            if (cmd.hasPermission() && !source.hasPermission(cmd.permission())) {
                source.sendMessage(MessageUtil.parse("<red>No tienes permiso para usar este comando."));
                return;
            }

            for (String line : cmd.messages()) {
                Component component = cmd.centered()
                        ? MessageUtil.center(line)
                        : MessageUtil.parse(line);
                source.sendMessage(component);
            }
        }

        @Override
        public boolean hasPermission(Invocation invocation) {
            return true;
        }
    }
}
