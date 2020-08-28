package kristi71111.bacontime.definitions;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kristi71111.bacontime.BaconTime;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Game;
import org.spongepowered.api.service.pagination.PaginationService;

import java.util.Arrays;
import java.util.List;

public class Helpers {
    public static final String[] formats = {"#w", "#d", "#h", "#m", "#s", ""};
    private static final int[] constants = {604800, 86400, 3600, 60, 1};
    //Permissions
    public static String BasePermission = "bacontime.command.base";
    public static String LeaderBoardPermission = "bacontime.command.leaderboard";
    public static String CheckPermission = "bacontime.command.check";
    public static String ReloadPermission = "bacontime.admin.reload";
    public static String ImportPermission = "bacontime.admin.import";
    public static String SetPermission = "bacontime.admin.set";
    public static String MilestoneCheck = "bacontime.milestones";
    final BaconTime pl = BaconTime.instance;
    final Game game = pl.game;

    public static String OutputTime(int time) {
        if (time > 0) {
            List<String> times = new ObjectArrayList<>();
            for (int i = 0; i < 5; i++) {
                int num = time / constants[i];
                if (num > 0 && !formats[i].isEmpty()) {
                    times.add(formats[i].replace("#", String.valueOf(num)).replace("<s>", num == 1 ? "" : "s"));
                    time -= num * constants[i];
                }
            }
            if (!times.isEmpty()) {
                return String.join(formats[5], times);
            }
        }
        return formats[4].contains("#") ? formats[4].replace("#", String.valueOf(time)).replace("<s>", "s") : time + "s";
    }

    public static List<String> getStringList(ConfigurationNode node, String type) {
        try {
            return node.getList(TypeToken.of(String.class), Lists::newArrayList);
        } catch (ObjectMappingException e) {
            return Arrays.asList("ERROR IMPORTING COMMANDS. THIS IS A PLACEHOLDER!");
        }
    }

    //Pagination service builder
    public PaginationService getPaginationService() {
        if (game.getServiceManager().provide(PaginationService.class).isPresent()) {
            return game.getServiceManager().provide(PaginationService.class).get();
        } else {
            return null;
        }
    }

}
