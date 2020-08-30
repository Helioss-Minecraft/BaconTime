package kristi71111.bacontime.commands;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kristi71111.bacontime.definitions.Helpers;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.List;

public class Base implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        List<Text> texts = new ObjectArrayList<>();
        int i = 0;
        if (src.hasPermission(Helpers.CheckPermission)) {
            i++;
            texts.add(Text.builder()
                    .append(Text.builder()
                            .color(TextColors.CYAN)
                            .append(Text.of(" - /playtime check <username>"))
                            .onClick(TextActions.runCommand("/playtime check"))
                            .onHover(TextActions.showText(Text.of("Click here to check your playtime!")))
                            .build())
                    .build());
        }
        if (src.hasPermission(Helpers.LeaderBoardPermission)) {
            i++;
            texts.add(Text.builder()
                    .append(Text.builder()
                            .color(TextColors.GOLD)
                            .style(TextStyles.BOLD)
                            .append(Text.of(" - /playtime leaderboard <afk/active>"))
                            .onClick(TextActions.runCommand("/playtime leaderboard"))
                            .onHover(TextActions.showText(Text.of("Click here to view the leaderboard.")))
                            .build())
                    .build());
        }
        if (src.hasPermission(Helpers.ReloadPermission)) {
            i++;
            texts.add(Text.builder()
                    .append(Text.builder()
                            .color(TextColors.GOLD)
                            .style(TextStyles.BOLD)
                            .append(Text.of(" - /playtime reload"))
                            .onClick(TextActions.runCommand("/playtime reload"))
                            .onHover(TextActions.showText(Text.of("Click here to reload the config file!")))
                            .build())
                    .build());
        }
        if (src.hasPermission(Helpers.ImportPermission)) {
            i++;
            texts.add(Text.builder()
                    .append(Text.builder()
                            .color(TextColors.GOLD)
                            .style(TextStyles.BOLD)
                            .append(Text.of(" - /playtime import <plugin-name>"))
                            .onClick(TextActions.suggestCommand("/playtime import"))
                            .onHover(TextActions.showText(Text.of("Click here to import the data from another plugin into this one!")))
                            .build())
                    .build());
        }
        if (src.hasPermission(Helpers.SetPermission)) {
            i++;
            texts.add(Text.builder()
                    .append(Text.builder()
                            .color(TextColors.GOLD)
                            .style(TextStyles.BOLD)
                            .append(Text.of(" - /playtime set <afk/active> <amount> <user>"))
                            .onClick(TextActions.suggestCommand("/playtime set"))
                            .onHover(TextActions.showText(Text.of("Click here to set someones active or afk time!")))
                            .build())
                    .build());
        }
        if (i == 0) {
            texts.add(Text.builder()
                    .append(Text.builder()
                            .color(TextColors.GOLD)
                            .style(TextStyles.BOLD)
                            .append(Text.of(" You seem to only have the base permission and nothing else!"))
                            .build())
                    .build());
        }
        PaginationList paginationlist = new Helpers().getPaginationService().builder().footer(Text.of(TextColors.GOLD, TextStyles.BOLD, "Made by kristi71111")).padding(Text.of("-")).title(Text.builder()
                .append(Text.builder()
                        .append(Text.of(TextColors.GOLD, TextStyles.BOLD, "{"))
                        .append(Text.of(TextColors.WHITE, TextStyles.BOLD, "BaconTime"))
                        .append(Text.of(TextColors.GOLD, TextStyles.BOLD, "}"))
                        .build())
                .build()).contents(texts).build();
        paginationlist.sendTo(src);
        return CommandResult.success();
    }
}

