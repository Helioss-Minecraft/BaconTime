package kristi71111.bacontime.commands;

import kristi71111.bacontime.definitions.Helpers;
import kristi71111.bacontime.handlers.DataHandler;
import kristi71111.bacontime.handlers.objects.BaconTimePlayerObject;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class Set implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<Integer> Amount = args.getOne(Text.of("amount"));
        if (!Amount.isPresent()) {
            throw new CommandException(Text.of(TextColors.RED, "Usage /playtime set amount player"));
        }
        User user;
        if (args.<User>getOne("user").isPresent()) {
            user = args.<User>getOne("user").get();
        } else {
            user = (User) src;
        }
        int amount = Amount.get();
        if (DataHandler.getPlayer(user.getUniqueId()) == null) {
            DataHandler.addPlayer(new BaconTimePlayerObject(0, 0, user.getName(), user.getUniqueId(), null));
            throw new CommandException(Text.of(TextColors.RED, "User was somehow null. Try again."));
        }

        if (!args.getOne("afk/active").isPresent() || args.getOne("afk/active").isPresent() && args.getOne("afk/active").get().equals("active")) {
            DataHandler.getPlayer(user.getUniqueId()).setActiveTime(amount);
            src.sendMessage(Text.of(user.getName() + "s active time is now: " + Helpers.OutputTime(amount)));
        } else {
            DataHandler.getPlayer(user.getUniqueId()).setAfkTime(amount);
            src.sendMessage(Text.of(user.getName() + "s afk time is now: " + Helpers.OutputTime(amount)));
        }
        DataHandler.savePlayer(user.getUniqueId());
        return CommandResult.success();
    }
}
