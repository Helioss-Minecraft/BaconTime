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

import java.util.*;
import java.util.Set;

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
                    BaconTimePlayerObject playerRecord;
                    BaconTimePlayerObject playerRecordSource = DataHandler.getPlayer(player.getUniqueId());
                    if (playerRecordSource == null) {
                        DataHandler.addPlayer(new BaconTimePlayerObject(0, 0, player.getName(), player.getUniqueId(), null));
                        return;
                    }
                    Set<UUID> uuidSet = playerRecords.keySet();
                    ArrayList<BaconTimePlayerObject> rankings = new ArrayList<>();
                    for (UUID playerUUID : uuidSet) {
                        playerRecord = DataHandler.getPlayer(playerUUID);
                        rankings.add(playerRecord);
                    }
                    if (!args.getOne("afk/active").isPresent() || args.getOne("afk/active").isPresent() && args.getOne("afk/active").get().equals("active")) {
                        rankings.sort(new SortedByActiveTime());
                        Collections.reverse(rankings);

                        List<Text> contents = new ArrayList();
                        int counter = 1;
                        int ownRank = 0;
                        for (BaconTimePlayerObject r : rankings) {

                            if (counter == 1) {
                                contents.add(Text.builder(counter + "")
                                        .color(TextColors.GOLD)
                                        .append(new Text[]{Text.builder(" - ")
                                                .color(TextColors.WHITE)
                                                .build()})
                                        .append(Text.builder(r.getUsername() + " (Time: " + Helpers.OutputTime(r.getActiveTime()) + ")")
                                                .color(TextColors.YELLOW)
                                                .build())
                                        .onClick(TextActions.runCommand("/playtime check " + r.getUsername()))
                                        .onHover(TextActions.showText(Text.of("Click here to check " + r.getUsername() + "'s activity!")))
                                        .build());
                            } else if (counter == 2) {
                                contents.add(Text.builder(counter + "")
                                        .color(TextColors.GOLD)
                                        .append(new Text[]{Text.builder(" - ")
                                                .color(TextColors.WHITE)
                                                .build()})
                                        .append(Text.builder(r.getUsername() + " (Time: " + Helpers.OutputTime(r.getActiveTime()) + ")")
                                                .color(TextColors.GRAY)
                                                .build())
                                        .onClick(TextActions.runCommand("/playtime check " + r.getUsername()))
                                        .onHover(TextActions.showText(Text.of("Click here to check " + r.getUsername() + "'s activity!")))
                                        .build());
                            } else if (counter == 3) {
                                contents.add(Text.builder(counter + "")
                                        .color(TextColors.GOLD)
                                        .append(new Text[]{Text.builder(" - ")
                                                .color(TextColors.WHITE)
                                                .build()})
                                        .append(Text.builder(r.getUsername() + " (Time: " + Helpers.OutputTime(r.getActiveTime()) + ")")
                                                .color(TextColors.DARK_GRAY)
                                                .build())
                                        .onClick(TextActions.runCommand("/playtime check " + r.getUsername()))
                                        .onHover(TextActions.showText(Text.of("Click here to check " + r.getUsername() + "'s activity!")))
                                        .build());
                            } else {
                                contents.add(Text.builder(counter + "")
                                        .color(TextColors.GOLD)
                                        .append(new Text[]{Text.builder(" - ")
                                                .color(TextColors.WHITE)
                                                .build()})
                                        .append(Text.builder(r.getUsername() + " (Time: " + Helpers.OutputTime(r.getActiveTime()) + ")")
                                                .color(TextColors.GREEN)
                                                .build())
                                        .onClick(TextActions.runCommand("/playtime check " + r.getUsername()))
                                        .onHover(TextActions.showText(Text.of("Click here to check " + r.getUsername() + "'s activity!")))
                                        .build());
                            }
                            if (r.getUUID().equals(player.getUniqueId())) {
                                ownRank = counter;
                            }
                            counter++;
                        }
                        PaginationList paginationlist = new Helpers().getPaginationService().builder().footer(Text.of(TextColors.GOLD, "You are no. ", TextColors.AQUA, +ownRank, TextColors.GOLD, " with a time of ", TextColors.AQUA, Helpers.OutputTime(playerRecordSource.getActiveTime()) + ".")).padding(Text.of("-")).title(Text.builder().append(Text.builder()
                                .append(Text.of(TextColors.GOLD, TextStyles.BOLD, "{"))
                                .append(Text.of(TextColors.WHITE, TextStyles.BOLD, "BaconTime - Sorted by Active Time"))
                                .append(Text.of(TextColors.GOLD, TextStyles.BOLD, "}"))
                                .build())
                                .build()).contents(contents).build();
                        paginationlist.sendTo(player);
                    } else {
                        rankings.sort(new SortedByAfkTime());
                        Collections.reverse(rankings);

                        List<Text> contents = new ObjectArrayList<>();
                        int counter = 1;
                        int ownRank = 0;
                        for (BaconTimePlayerObject r : rankings) {

                            if (counter == 1) {
                                contents.add(Text.builder(counter + "")
                                        .color(TextColors.GOLD)
                                        .append(new Text[]{Text.builder(" - ")
                                                .color(TextColors.WHITE)
                                                .build()})
                                        .append(Text.builder(r.getUsername() + " (Time: " + Helpers.OutputTime(r.getAfkTime()) + ")")
                                                .color(TextColors.YELLOW)
                                                .build())
                                        .onClick(TextActions.runCommand("/playtime check " + r.getUsername()))
                                        .onHover(TextActions.showText(Text.of("Click here to check " + r.getUsername() + "'s activity!")))
                                        .build());
                            } else if (counter == 2) {
                                contents.add(Text.builder(counter + "")
                                        .color(TextColors.GOLD)
                                        .append(new Text[]{Text.builder(" - ")
                                                .color(TextColors.WHITE)
                                                .build()})
                                        .append(Text.builder(r.getUsername() + " (Time: " + Helpers.OutputTime(r.getAfkTime()) + ")")
                                                .color(TextColors.GRAY)
                                                .build())
                                        .onClick(TextActions.runCommand("/playtime check " + r.getUsername()))
                                        .onHover(TextActions.showText(Text.of("Click here to check " + r.getUsername() + "'s activity!")))
                                        .build());
                            } else if (counter == 3) {
                                contents.add(Text.builder(counter + "")
                                        .color(TextColors.GOLD)
                                        .append(new Text[]{Text.builder(" - ")
                                                .color(TextColors.WHITE)
                                                .build()})
                                        .append(Text.builder(r.getUsername() + " (Time: " + Helpers.OutputTime(r.getAfkTime()) + ")")
                                                .color(TextColors.DARK_GRAY)
                                                .build())
                                        .onClick(TextActions.runCommand("/playtime check " + r.getUsername()))
                                        .onHover(TextActions.showText(Text.of("Click here to check " + r.getUsername() + "'s activity!")))
                                        .build());
                            } else {
                                contents.add(Text.builder(counter + "")
                                        .color(TextColors.GOLD)
                                        .append(new Text[]{Text.builder(" - ")
                                                .color(TextColors.WHITE)
                                                .build()})
                                        .append(Text.builder(r.getUsername() + " (Time: " + Helpers.OutputTime(r.getAfkTime()) + ")")
                                                .color(TextColors.GREEN)
                                                .build())
                                        .onClick(TextActions.runCommand("/playtime check " + r.getUsername()))
                                        .onHover(TextActions.showText(Text.of("Click here to check " + r.getUsername() + "'s activity!")))
                                        .build());
                            }
                            if (r.getUUID().equals(player.getUniqueId())) {
                                ownRank = counter;
                            }
                            counter++;
                        }
                        PaginationList paginationlist = new Helpers().getPaginationService().builder().footer(Text.of(TextColors.GOLD, "You are no. ", TextColors.AQUA, +ownRank, TextColors.GOLD, " with a time of ", TextColors.AQUA, Helpers.OutputTime(playerRecordSource.getAfkTime()) + ".")).padding(Text.of("-")).title(Text.builder().append(Text.builder()
                                .append(Text.of(TextColors.GOLD, TextStyles.BOLD, "{"))
                                .append(Text.of(TextColors.WHITE, TextStyles.BOLD, "BaconTime - Sorted by AFK Time"))
                                .append(Text.of(TextColors.GOLD, TextStyles.BOLD, "}"))
                                .build())
                                .build()).contents(contents).build();
                        paginationlist.sendTo(player);
                    }
                }
        ).async().submit(BaconTime.getInstance());
        return CommandResult.success();
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