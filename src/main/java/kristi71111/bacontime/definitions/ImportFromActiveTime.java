package kristi71111.bacontime.definitions;

import com.mcsimonflash.sponge.activetime.ActiveTime;
import com.mcsimonflash.sponge.activetime.objects.ConfigHolder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import kristi71111.bacontime.BaconTime;
import kristi71111.bacontime.handlers.ConfigHandler;
import kristi71111.bacontime.handlers.DataHandler;
import kristi71111.bacontime.handlers.objects.BaconTimePlayerObject;
import kristi71111.bacontime.handlers.objects.BaconTimeReachedMilestoneObject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.nio.file.Path;
import java.util.UUID;

public class ImportFromActiveTime {
    public static void importFromActiveTime(CommandSource src) {
        Task.Builder taskBuilder = Task.builder();
        taskBuilder.execute(() -> {
                    try {
                        Path storDir = ActiveTime.getInstance().getDirectory().resolve("configuration");
                        ConfigHolder milestoness = new ConfigHolder(storDir.resolve("milestones.conf"), true);
                        for (CommentedConfigurationNode m : milestoness.getNode().getChildrenMap().values()) {
                            String name = (String) m.getKey();
                            src.sendMessage(Text.of("Now importing milestone: " + name));
                            ConfigHandler.config.getNode("milestones", name);
                            ConfigHandler.config.getNode(new Object[]{"milestones", name, "requiredTime"}).setValue(m.getNode("activetime").getInt());
                            ConfigHandler.config.getNode(new Object[]{"milestones", name, "commands"}).setValue(Helpers.getStringList(m.getNode("commands"), "milestone commands"));
                            ConfigHandler.config.getNode(new Object[]{"milestones", name, "repeatable"}).setValue(m.getNode("repeatable").getBoolean(false));
                        }
                        ConfigHandler.save();
                        ConfigHandler.loadValues();
                        src.sendMessage(Text.of(TextColors.GREEN, "ActiveTime import of milestones complete!"));
                        //Players
                        storDir = ActiveTime.getInstance().getDirectory().resolve("storage");
                        ConfigHolder players = new ConfigHolder(storDir.resolve("players.stor"), false);
                        DataHandler.players.clear();
                        for (CommentedConfigurationNode node : players.getNode().getChildrenMap().values()) {
                            Object2ObjectOpenHashMap<String, BaconTimeReachedMilestoneObject> milestones = new Object2ObjectOpenHashMap<String, BaconTimeReachedMilestoneObject>();
                            for (CommentedConfigurationNode milestone : node.getNode("milestones").getChildrenMap().values()) {
                                int claimedAt = 0;
                                if (milestone.getValue() != null) {
                                    claimedAt = (int) milestone.getValue();
                                }
                                if (ConfigHandler.AllMilestones.containsKey(milestone.getKey())) {
                                    milestones.put((String) milestone.getKey(), new BaconTimeReachedMilestoneObject((String) milestone.getKey(), claimedAt, ConfigHandler.AllMilestones.get(milestone.getKey()).isRepeatable()));
                                } else {
                                    milestones.put((String) milestone.getKey(), new BaconTimeReachedMilestoneObject((String) milestone.getKey(), claimedAt, false));
                                }
                            }
                            DataHandler.addPlayer(new BaconTimePlayerObject(node.getNode("activetime").getInt(), node.getNode("afktime").getInt(), node.getNode("username").getString(), UUID.fromString(node.getKey().toString()), milestones));
                        }
                        src.sendMessage(Text.of(TextColors.GREEN, "ActiveTime import of players complete!"));
                    } catch (Exception e) {
                        src.sendMessage(Text.of("Error check console"));
                        e.printStackTrace();
                    }
                }
        ).async().submit(BaconTime.getInstance());
    }
}
