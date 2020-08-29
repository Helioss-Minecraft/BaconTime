package kristi71111.bacontime.commands;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kristi71111.bacontime.BaconTime;
import kristi71111.bacontime.definitions.Helpers;
import kristi71111.bacontime.handlers.DataHandler;
import kristi71111.bacontime.handlers.objects.BaconTimePlayerObject;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Set;
import java.util.*;

public class Leaderboard implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            throw new CommandException(Text.of(TextColors.RED, "You need to be a player to run this command!"));
        }
        Task.Builder taskBuilder = Task.builder();
        taskBuilder.execute(() -> {
                    Player player = (Player) src;
                    Object2ObjectOpenHashMap<UUID, BaconTimePlayerObject> playerRecords = DataHandler.getPlayers();
                    BaconTimePlayerObject playerRecordSource = DataHandler.getPlayer(player.getUniqueId());
                    if (playerRecordSource == null) {
                        DataHandler.addPlayer(new BaconTimePlayerObject(0, 0, player.getName(), player.getUniqueId(), null));
                        return;
                    }
                    Set<UUID> uuidSet = playerRecords.keySet();
                    ObjectArrayList<BaconTimePlayerObject> rankings = new ObjectArrayList<>();
                    for (UUID playerUUID : uuidSet) {
                        rankings.add(DataHandler.getPlayer(playerUUID));
                    }
                    if (!args.getOne("afk/active").isPresent() || args.getOne("afk/active").isPresent() && args.getOne("afk/active").get().equals("active")) {
                        rankings.sort(new SortedByActiveTime());
                        Collections.reverse(rankings);
                        SendMessage(player, rankings, "Active", playerRecordSource);
                    } else {
                        rankings.sort(new SortedByAfkTime());
                        Collections.reverse(rankings);
                        SendMessage(player, rankings, "AFK", playerRecordSource);
                    }
                }
        ).async().submit(BaconTime.getInstance());
        return CommandResult.success();
    }

    public void SendMessage(Player player, ObjectArrayList<BaconTimePlayerObject> rankings, String AFKorActive, BaconTimePlayerObject playerRecordSource) {
        List<Text> contents = new ObjectArrayList<>();
        int counter = 1;
        int ownRank = 0;
        int time = 0;
        int timePlayer = 0;
        for (BaconTimePlayerObject r : rankings) {
            if (AFKorActive.equalsIgnoreCase("AFK")) {
                time = r.getAfkTime();
            } else {
                time = r.getActiveTime();
            }
            if (counter == 1) {
                contents.add(Text.builder(counter + "")
                        .color(TextColors.GOLD)
                        .append(Text.of(TextColors.WHITE, " - "))
                        .append(Text.of(TextColors.RED, r.getUsername() + " (Time: ["))
                        .append(Text.of(TextColors.WHITE, Helpers.convertSecondsToHoursOrMinutes(time)))
                        .append(Text.of(TextColors.RED, "] "))
                        .append(Text.of(TextColors.WHITE, Helpers.OutputTime(time)))
                        .append(Text.of(TextColors.RED, ")"))
                        .onClick(TextActions.runCommand("/playtime check " + r.getUsername()))
                        .onHover(TextActions.showText(Text.of("Click here to check " + r.getUsername() + "'s activity!")))
                        .build());
            } else if (counter == 2) {
                contents.add(Text.builder(counter + "")
                        .color(TextColors.GOLD)
                        .append(Text.of(TextColors.WHITE, " - "))
                        .append(Text.of(TextColors.GOLD, r.getUsername() + " (Time: ["))
                        .append(Text.of(TextColors.WHITE, Helpers.convertSecondsToHoursOrMinutes(time)))
                        .append(Text.of(TextColors.GOLD, "] "))
                        .append(Text.of(TextColors.WHITE, Helpers.OutputTime(time)))
                        .append(Text.of(TextColors.GOLD, ")"))
                        .onClick(TextActions.runCommand("/playtime check " + r.getUsername()))
                        .onHover(TextActions.showText(Text.of("Click here to check " + r.getUsername() + "'s activity!")))
                        .build());

            } else if (counter == 3) {
                contents.add(Text.builder(counter + "")
                        .color(TextColors.GOLD)
                        .append(Text.of(TextColors.WHITE, " - "))
                        .append(Text.of(TextColors.YELLOW, r.getUsername() + " (Time: ["))
                        .append(Text.of(TextColors.WHITE, Helpers.convertSecondsToHoursOrMinutes(time)))
                        .append(Text.of(TextColors.YELLOW, "] "))
                        .append(Text.of(TextColors.WHITE, Helpers.OutputTime(time)))
                        .append(Text.of(TextColors.YELLOW, ")"))
                        .onClick(TextActions.runCommand("/playtime check " + r.getUsername()))
                        .onHover(TextActions.showText(Text.of("Click here to check " + r.getUsername() + "'s activity!")))
                        .build());

            } else {
                contents.add(Text.builder(counter + "")
                        .color(TextColors.GOLD)
                        .append(Text.of(TextColors.WHITE, " - "))
                        .append(Text.of(TextColors.GRAY, r.getUsername() + " (Time: ["))
                        .append(Text.of(TextColors.WHITE, Helpers.convertSecondsToHoursOrMinutes(time)))
                        .append(Text.of(TextColors.GRAY, "] "))
                        .append(Text.of(TextColors.WHITE, Helpers.OutputTime(time)))
                        .append(Text.of(TextColors.GRAY, ")"))
                        .onClick(TextActions.runCommand("/playtime check " + r.getUsername()))
                        .onHover(TextActions.showText(Text.of("Click here to check " + r.getUsername() + "'s activity!")))
                        .build());
            }
            if (r.getUUID().equals(player.getUniqueId())) {
                ownRank = counter;
            }
            counter++;
        }
        if (AFKorActive.equalsIgnoreCase("AFK")) {
            timePlayer = playerRecordSource.getAfkTime();
        } else {
            timePlayer = playerRecordSource.getActiveTime();
        }
        PaginationList paginationlist = new Helpers().getPaginationService().builder().footer(Text.of(TextColors.GOLD, "You are no. ", TextColors.AQUA, +ownRank, TextColors.GOLD, " with a time of ", TextColors.AQUA,
                Helpers.OutputTime(timePlayer) + ".")).padding(Text.of("-")).title(Text.builder().append(Text.builder()
                .append(Text.of(TextColors.GOLD, TextStyles.BOLD, "{"))
                .append(Text.of(TextColors.WHITE, TextStyles.BOLD, "BaconTime - Sorted by " + AFKorActive + " Time"))
                .append(Text.of(TextColors.GOLD, TextStyles.BOLD, "}"))
                .build())
                .build()).contents(contents).build();
        paginationlist.sendTo(player);
    }
}

class SortedByActiveTime implements Comparator<BaconTimePlayerObject> {
    @Override
    public int compare(BaconTimePlayerObject o1, BaconTimePlayerObject o2) {
        return Integer.compare(o1.getActiveTime(), o2.getActiveTime());
    }
}

class SortedByAfkTime implements Comparator<BaconTimePlayerObject> {
    @Override
    public int compare(BaconTimePlayerObject o1, BaconTimePlayerObject o2) {
        return Integer.compare(o1.getAfkTime(), o2.getAfkTime());
    }
}