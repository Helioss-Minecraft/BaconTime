package kristi71111.bacontime.definitions;

import com.mcsimonflash.sponge.activetime.ActiveTime;
import com.mcsimonflash.sponge.activetime.objects.ConfigHolder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kristi71111.bacontime.BaconTime;
import kristi71111.bacontime.handlers.ConfigHandler;
import kristi71111.bacontime.handlers.DataHandler;
import kristi71111.bacontime.handlers.objects.BaconTimePlayerObject;
import kristi71111.bacontime.handlers.objects.BaconTimeReachedMilestoneObject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ImportFromActiveTime {
    public static void importFromActiveTime(CommandSource src) {
        Task.Builder taskBuilder = Task.builder();
        Optional<PluginContainer> pluginContainer = Sponge.getPluginManager().getPlugin("activetime");
        taskBuilder.execute(() -> {
                    if (pluginContainer.isPresent()) {
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
                                List<BaconTimeReachedMilestoneObject> milestones = new ObjectArrayList<>();
                                for (CommentedConfigurationNode milestone : node.getNode("milestones").getChildrenMap().values()) {
                                    if (ConfigHandler.AllMilestones.containsKey(milestone.getKey())) {
                                        milestones.add(new BaconTimeReachedMilestoneObject((String) milestone.getKey(), (int) milestone.getValue(), ConfigHandler.AllMilestones.get(milestone.getKey()).isRepeatable()));
                                    } else {
                                        milestones.add(new BaconTimeReachedMilestoneObject((String) milestone.getKey(), (int) milestone.getValue(), false));
                                    }
                                }
                                DataHandler.addPlayer(new BaconTimePlayerObject(node.getNode("activetime").getInt(), node.getNode("afktime").getInt(), node.getNode("username").getString(), UUID.fromString(node.getKey().toString()), milestones));
                            }
                            src.sendMessage(Text.of(TextColors.GREEN, "ActiveTime import of players complete!"));
                        } catch (Exception e) {
                            src.sendMessage(Text.of("Error check console"));
                            e.printStackTrace();
                        }
                    } else {
                        src.sendMessage(Text.of("ActiveTime is not present on the server!"));
                    }
                }
        ).async().submit(BaconTime.getInstance());
    }
}
