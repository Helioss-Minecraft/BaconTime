package kristi71111.bacontime.commands;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kristi71111.bacontime.BaconTime;
import kristi71111.bacontime.definitions.Helpers;
import kristi71111.bacontime.handlers.ConfigHandler;
import kristi71111.bacontime.handlers.DataHandler;
import kristi71111.bacontime.handlers.objects.BaconTimeMilestoneObject;
import kristi71111.bacontime.handlers.objects.BaconTimePlayerObject;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Collections;
import java.util.List;

import static kristi71111.bacontime.definitions.Helpers.MilestoneCheck;

public class Check implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!args.hasAny("user") && !(src instanceof User)) {
            throw new CommandException(Text.of(TextColors.RED, "You need to be a player to run this command without the user argument!"));
        }
        Task.Builder taskBuilder = Task.builder();
        taskBuilder.execute(() -> {
                    User user;
                    if (args.<User>getOne("user").isPresent()) {
                        user = args.<User>getOne("user").get();
                    } else {
                        user = (User) src;
                    }
                    BaconTimePlayerObject playerRecord = DataHandler.getPlayer(user.getUniqueId());
                    if (playerRecord == null) {
                        DataHandler.addPlayer(new BaconTimePlayerObject(0, 0, user.getName(), user.getUniqueId(), null));
                        playerRecord = new BaconTimePlayerObject(0, 0, user.getName(), user.getUniqueId(), null);
                    }
                    List<Text> texts = new ObjectArrayList<>();
                    texts.add(Text.builder()
                            .append(Text.builder()
                                    .append(Text.of(TextColors.GOLD, "Active time: ["))
                                    .append(Text.of(Helpers.convertSecondsToHoursOrMinutes(playerRecord.getActiveTime())))
                                    .append(Text.of(TextColors.GOLD, "] "))
                                    .append(Text.of(TextColors.AQUA, Helpers.OutputTime(playerRecord.getActiveTime())))
                                    .append(Text.NEW_LINE)
                                    .append(Text.of(TextColors.GOLD, "AFK time: ["))
                                    .append(Text.of(Helpers.convertSecondsToHoursOrMinutes(playerRecord.getAfkTime())))
                                    .append(Text.of(TextColors.GOLD, "] "))
                                    .append(Text.of(TextColors.AQUA, Helpers.OutputTime(playerRecord.getAfkTime())))
                                    .append(Text.NEW_LINE)
                                    .append(Text.of(TextColors.GOLD, "Active percentage: "))
                                    .append(Text.of(TextColors.AQUA, Helpers.getActivePercentage(playerRecord)))
                                    .build())
                            .build());
                    if (!ConfigHandler.AllMilestones.isEmpty()) {
                        texts.add(Text.builder()
                                .append(Text.builder()
                                        .append(Text.of(TextColors.GOLD, "Time needed for milestones: "))
                                        .build())
                                .build());
                        List<BaconTimeMilestoneObject> milestonesSorted = new ObjectArrayList<>();
                        milestonesSorted.addAll(ConfigHandler.AllMilestones.values());
                        Collections.sort(milestonesSorted);
                        int check = 0;
                        for (BaconTimeMilestoneObject object : milestonesSorted) {
                            if (!user.hasPermission(MilestoneCheck + "." + object.getMilestoneName()) || object.isRepeatable()) {
                                continue;
                            }
                            if (playerRecord.getMilestones() != null && !playerRecord.getMilestones().isEmpty() && playerRecord.getMilestones().containsKey(object.getMilestoneName())) {
                                continue;
                            }
                            check++;
                            texts.add(Text.builder()
                                    .append(Text.builder()
                                            .append(Text.of(TextColors.WHITE, " - ", TextColors.GOLD, object.getMilestoneName() + ": "))
                                            .append(Text.of(TextColors.AQUA, Helpers.convertSecondsToHoursOrMinutes(object.getRequiredTime() - playerRecord.getActiveTime())))
                                            .build())
                                    .build());
                        }
                        if(check == 0){
                            texts.add(Text.builder()
                                    .append(Text.builder()
                                            .append(Text.of(TextColors.WHITE, " - ", "You already have all of the milestones!"))
                                            .build())
                                    .build());
                        }
                    }
                    PaginationList paginationlist = new Helpers().getPaginationService().builder().footer(Text.of(TextColors.GOLD, TextStyles.BOLD, "Made by kristi71111")).padding(Text.of("-")).title(Text.builder()
                            .append(Text.builder()
                                    .append(Text.of(TextColors.GOLD, TextStyles.BOLD, "{"))
                                    .append(Text.of(TextStyles.BOLD, user.getName() + "'s Activity"))
                                    .append(Text.of(TextColors.GOLD, TextStyles.BOLD, "}"))
                                    .build())
                            .build()).contents(texts).build();
                    paginationlist.sendTo(src);
                }
        ).async().submit(BaconTime.getInstance());
        return CommandResult.success();
    }
}
