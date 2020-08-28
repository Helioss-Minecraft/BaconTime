package kristi71111.bacontime.commands;

import kristi71111.bacontime.definitions.ImportFromActiveTime;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

public class Import implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (args.getOne("plugin").isPresent()) {
            String plugin = (String) args.getOne("plugin").get();
            if (plugin.equalsIgnoreCase("activetime")) {
                ImportFromActiveTime.importFromActiveTime(src);
            }
        }
        return CommandResult.success();

    }
}
