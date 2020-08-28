package kristi71111.bacontime;

import com.google.inject.Inject;
import kristi71111.bacontime.commands.CommandList;
import kristi71111.bacontime.handlers.ConfigHandler;
import kristi71111.bacontime.handlers.DataHandler;
import kristi71111.bacontime.tasks.MilestoneTimedTask;
import kristi71111.bacontime.tasks.UpdatePlayTimesTask;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Plugin(
        id = "bacontime",
        name = "BaconTime",
        description = "A playtime tracking plugin",
        authors = {
                "kristi71111"
        },
        dependencies = @Dependency(id = "nucleus"),
        version = "@VERSION@"
)
public class BaconTime {
    public static BaconTime instance;
    public static Task taskSave;
    public static Task milestoneCheck;

    @Inject
    public Game game;

    @Inject
    @ConfigDir(sharedRoot = true)
    private File defaultConfigDir;

    @Inject
    private Logger logger;

    public static Logger getLogger() {
        return getInstance().logger;
    }

    public static BaconTime getInstance() {
        return instance;
    }

    @Listener
    public void onInit(GameInitializationEvent event) {
        instance = this;
        File rootDir = new File(defaultConfigDir, "bacontime");
        ConfigHandler.init(rootDir);
        DataHandler.init(rootDir);
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("Started BaconTime successfully! Running v@VERSION@!");
        ConfigHandler.load();
        ConfigHandler.loadValues();
        DataHandler.load();
        CommandList.RegisterCommands();
        taskSave = Sponge.getScheduler()
                .createTaskBuilder()
                .async()
                .execute(new UpdatePlayTimesTask())
                .interval(ConfigHandler.saveInterval, TimeUnit.SECONDS)
                .delay(ConfigHandler.saveInterval, TimeUnit.SECONDS)
                .submit(this);
        milestoneCheck = Sponge.getScheduler()
                .createTaskBuilder()
                .async()
                .execute(new MilestoneTimedTask())
                .interval(ConfigHandler.milestoneCheckInterval, TimeUnit.SECONDS)
                .delay(ConfigHandler.milestoneCheckInterval, TimeUnit.SECONDS)
                .submit(this);
    }

    @Listener
    public void onServerStopping(GameStoppingServerEvent event) {
        logger.info("Stopped BaconTime successfully!");
    }

}
