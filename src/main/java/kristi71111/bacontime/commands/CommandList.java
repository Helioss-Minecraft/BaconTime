package kristi71111.bacontime.commands;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import kristi71111.bacontime.BaconTime;
import kristi71111.bacontime.definitions.Helpers;
import kristi71111.bacontime.handlers.ConfigHandler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;

public class CommandList {
    public static CommandSpec Check = CommandSpec.builder()
            .permission(Helpers.CheckPermission)
            .arguments(GenericArguments.optional(GenericArguments.user(Text.of("user"))))
            .executor(new Check())
            .build();

    public static CommandSpec Leaderboard = CommandSpec.builder()
            .permission(Helpers.LeaderBoardPermission)
            .arguments(
                    GenericArguments.optional(GenericArguments.choices(Text.of("afk/active"),
                            ImmutableMap.<String, String>builder()
                                    .put("active", "active")
                                    .put("afk", "afk")
                                    .build())))
            .executor(new Leaderboard())
            .build();

    public static CommandSpec Import = CommandSpec.builder()
            .permission(Helpers.ImportPermission)
            .arguments(
                    GenericArguments.choices(Text.of("plugin"),
                            ImmutableMap.<String, String>builder()
                                    .put("activetime", "activetime")
                                    .build()))
            .executor(new Import())
            .build();

    public static CommandSpec Set = CommandSpec.builder()
            .permission(Helpers.SetPermission)
            .arguments(
                    GenericArguments.choices(Text.of("afk/active"),
                            ImmutableMap.<String, String>builder()
                                    .put("active", "active")
                                    .put("afk", "afk")
                                    .build()),
                    GenericArguments.integer(Text.of("amount")),
                    GenericArguments.optional(GenericArguments.user(Text.of("user")))
            )
            .executor(new Set())
            .build();

    public static CommandSpec Reload = CommandSpec.builder()
            .permission(Helpers.ReloadPermission)
            .executor(new Reload())
            .build();

    public static CommandSpec Base = CommandSpec.builder()
            .permission(Helpers.BasePermission)
            .child(Check, "check")
            .child(Leaderboard, "leaderboard", "top")
            .child(Import, "import")
            .child(Reload, "reload")
            .child(Set, "set")
            .executor(new Base())
            .build();

    public static void RegisterCommands() {
        final BaconTime pl = BaconTime.instance;
        try {
            List<String> aliases = ConfigHandler.getNode("Aliases").getList(TypeToken.of(String.class));
            if (aliases.isEmpty()) {
                aliases.addAll(Arrays.asList("playtime", "bacontime", "bt", "ptime"));
                BaconTime.getLogger().error("Error setting the aliases. The list is somehow null?!? Check your config file! Added default as placeholder!");
            }
            Sponge.getCommandManager().register(pl, Base, aliases);
        } catch (Exception e) {
            List<String> aliases = Arrays.asList("playtime", "bacontime", "bt", "ptime");
            Sponge.getCommandManager().register(pl, Base, aliases);
        }
    }
}
