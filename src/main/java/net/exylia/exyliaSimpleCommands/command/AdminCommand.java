package net.exylia.exyliaSimpleCommands.command;

import com.velocitypowered.api.command.CommandSource;
import net.exylia.exyliaSimpleCommands.ExyliaSimpleCommands;
import net.exylia.exyliaSimpleCommands.util.MessageUtil;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.velocity.annotation.CommandPermission;

public class AdminCommand {

    private final ExyliaSimpleCommands plugin;

    public AdminCommand(ExyliaSimpleCommands plugin) {
        this.plugin = plugin;
    }

    @Command("simplecommands")
    @Subcommand("reload")
    @CommandPermission("simplecommands.admin")
    @Description("Reload configuration")
    public void reload(CommandSource sender) {
        plugin.reload();
        sender.sendMessage(MessageUtil.parse("<green>Configuration reloaded. " + plugin.getConfigManager().getCommands().size() + " commands loaded."));
    }

    @Command("simplecommands")
    @Subcommand("list")
    @CommandPermission("simplecommands.admin")
    @Description("List all commands")
    public void list(CommandSource sender) {
        var commands = plugin.getConfigManager().getCommands();
        sender.sendMessage(MessageUtil.parse("<gray>Registered commands (" + commands.size() + "):"));
        for (var cmd : commands) {
            String aliases = cmd.aliases().isEmpty() ? "" : " <dark_gray>(" + String.join(", ", cmd.aliases()) + ")";
            String perm = cmd.hasPermission() ? " <yellow>[" + cmd.permission() + "]" : "";
            sender.sendMessage(MessageUtil.parse("<white>/" + cmd.name() + aliases + perm));
        }
    }
}
