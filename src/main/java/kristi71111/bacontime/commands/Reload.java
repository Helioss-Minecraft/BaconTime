package kristi71111.bacontime.commands;

import kristi71111.bacontime.BaconTime;
import kristi71111.bacontime.handlers.ConfigHandler;
import kristi71111.bacontime.tasks.MilestoneTimedTask;
import kristi71111.bacontime.tasks.UpdatePlayTimesTask;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.concurrent.TimeUnit;

public class Reload implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        ConfigHandler.load();
        ConfigHandler.loadValues();
        BaconTime.taskSave.cancel();
        BaconTime.taskSave = Sponge.getScheduler()
                .createTaskBuilder()
                .async()
                .execute(new UpdatePlayTimesTask())
                .interval(ConfigHandler.saveInterval, TimeUnit.SECONDS)
                .delay(ConfigHandler.saveInterval, TimeUnit.SECONDS)
                .submit(BaconTime.getInstance());
        src.sendMessage(Text.of(TextColors.GOLD, TextStyles.BOLD, "BaconTime config reloaded!"));
        BaconTime.milestoneCheck.cancel();
        if (!ConfigHandler.AllMilestones.isEmpty()) {
        BaconTime.milestoneCheck = Sponge.getScheduler()
                .createTaskBuilder()
                .async()
                .execute(new MilestoneTimedTask())
                .interval(ConfigHandler.milestoneCheckInterval, TimeUnit.SECONDS)
                .delay(ConfigHandler.milestoneCheckInterval, TimeUnit.SECONDS)
                .submit(BaconTime.getInstance());
        }
        return CommandResult.success();
    }
}
