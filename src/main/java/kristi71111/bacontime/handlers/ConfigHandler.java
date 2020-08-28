package kristi71111.bacontime.handlers;

import com.google.common.reflect.TypeToken;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import kristi71111.bacontime.BaconTime;
import kristi71111.bacontime.handlers.objects.BaconTimeMilestoneObject;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ConfigHandler {
    //Definitions
    public static Object2ObjectOpenHashMap<String, BaconTimeMilestoneObject> AllMilestones = new Object2ObjectOpenHashMap<String, BaconTimeMilestoneObject>();
    public static int saveInterval = 0;
    public static int milestoneCheckInterval = 0;
    public static File configFile;
    public static ConfigurationLoader<CommentedConfigurationNode> configManager;
    public static CommentedConfigurationNode config;

    public static void init(File rootDir) {
        configFile = new File(rootDir, "config.conf");
        configManager = HoconConfigurationLoader.builder().setPath(configFile.toPath()).build();
    }

    public static void load() {
        load(null);
    }

    public static void load(CommandSource src) {
        // load file
        try {
            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
                config = configManager.load();
                makeConfig();
                configManager.save(config);

            }
            config = configManager.load();
        } catch (IOException e) {
            BaconTime.getLogger().error("Failed to load or create config file");
            e.printStackTrace();
            if (src != null) {
                src.sendMessage(Text.of(TextColors.RED, "Failed to load or create config file"));
            }
        }

    }

    public static void makeConfig() {
        //Aliases go here:
        Utils.ensureList(config.getNode("Aliases"), Arrays.asList("playtime", "bacontime", "bt", "ptime"), "The aliases the plugin uses for commands. The first value is always displayed when running the base command!" + "\n" + "Requires server restart to change!");
        //Intervals go here:
        Utils.ensureNumber(config.getNode("Intervals", "milestone"), 180, "The interval in seconds between milestone checks, will be disabled if there are no milestones.");
        Utils.ensureNumber(config.getNode("Intervals", "save"), 120, "The interval in seconds between saving the playtime data.");
        //MileStoneExample
        config.getNode(new Object[]{"milestones"}).setValue(Arrays.asList("example_milestone"));
        config.getNode(new Object[]{"milestones", "example_milestone", "requiredTime"}).setValue(3600);
        config.getNode(new Object[]{"milestones", "example_milestone", "commands"}).setValue(Arrays.asList("give <player> minecraft:apple 10", "msg <player> Apple time!"));
        config.getNode(new Object[]{"milestones", "example_milestone", "repeatable"}).setValue(true);
        save();
    }

    public static void loadValues() {
        try {
            AllMilestones.clear();
            milestoneCheckInterval = getNode("Intervals", "milestone").getInt();
            saveInterval = getNode("Intervals", "save").getInt();
            ConfigurationNode milestones = getNode("milestones");
            for (ConfigurationNode milestonex : milestones.getChildrenMap().values()) {
                try {
                    BaconTimeMilestoneObject milestone = new BaconTimeMilestoneObject((String) milestonex.getKey(), milestonex.getNode("requiredTime").getInt(), milestonex.getNode("commands").getList(TypeToken.of(String.class)), milestonex.getNode("repeatable").getBoolean());
                    AllMilestones.put((String) milestonex.getKey(), milestone);
                } catch (ObjectMappingException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            BaconTime.getLogger().error("Failed to load milestone values to variables!");
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            configManager.save(config);
        } catch (IOException e) {
            BaconTime.getLogger().error("Could not save or load config file !");
        }
    }

    public static CommentedConfigurationNode getNode(Object... path) {
        return config.getNode(path);
    }

    public static class Utils {
        public static void ensureString(CommentedConfigurationNode node, String def, String comment) {
            if (node.getString() == null) {
                node.setValue(def);
            }
            if (!node.getComment().isPresent() && comment != null) {
                node.setComment(comment);
            }
        }

        public static void ensureList(CommentedConfigurationNode node, List<String> def) {
            if (!(node.getValue() instanceof List)) {
                node.setValue(def);
            }
        }

        public static void ensureList(CommentedConfigurationNode node, List<String> def, String comment) {
            if (!(node.getValue() instanceof List)) {
                node.setValue(def);
            }
            if (!node.getComment().isPresent() && comment != null) {
                node.setComment(comment);
            }
        }

        public static void ensureNumber(CommentedConfigurationNode node, Number def, String comment) {
            if (!(node.getValue() instanceof Number)) {
                node.setValue(def);
            }
            if (!node.getComment().isPresent()) {
                node.setComment(comment);
            }
        }
    }
}

