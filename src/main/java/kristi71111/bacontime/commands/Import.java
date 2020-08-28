package kristi71111.bacontime.commands;

import kristi71111.bacontime.definitions.ImportFromActiveTime;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class Import implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (args.getOne("plugin").isPresent()) {
            String plugin = (String) args.getOne("plugin").get();
            if (plugin.equalsIgnoreCase("activetime")) {
                Optional<PluginContainer> pluginContainer = Sponge.getPluginManager().getPlugin("activetime");
                if (pluginContainer.isPresent()) {
                    ImportFromActiveTime.importFromActiveTime(src);
                } else {
                    throw new CommandException(Text.of(TextColors.RED, "ActiveTime is not present!"));
                }
            }
        }
        return CommandResult.success();
    }
}
