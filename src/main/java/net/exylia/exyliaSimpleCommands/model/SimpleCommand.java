package net.exylia.exyliaSimpleCommands.model;

import java.util.List;

public record SimpleCommand(
        String name,
        List<String> aliases,
        String permission,
        boolean centered,
        List<String> messages
) {
    public boolean hasPermission() {
        return permission != null && !permission.isEmpty();
    }
}
