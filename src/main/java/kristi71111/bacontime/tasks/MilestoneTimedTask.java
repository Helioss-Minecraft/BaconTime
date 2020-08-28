package kristi71111.bacontime.tasks;

import kristi71111.bacontime.BaconTime;
import kristi71111.bacontime.handlers.ConfigHandler;
import kristi71111.bacontime.handlers.DataHandler;
import kristi71111.bacontime.handlers.objects.BaconTimeMilestoneObject;
import kristi71111.bacontime.handlers.objects.BaconTimePlayerObject;
import kristi71111.bacontime.handlers.objects.BaconTimeReachedMilestoneObject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

import java.util.Collection;
import java.util.List;

import static kristi71111.bacontime.definitions.Helpers.MilestoneCheck;

public class MilestoneTimedTask implements Runnable {
    @Override
    public void run() {
        Collection<Player> players = Sponge.getServer().getOnlinePlayers();
        for (Player player : players) {
            BaconTimePlayerObject playerRecord = DataHandler.getPlayer(player.getUniqueId());
            if (playerRecord == null) {
                DataHandler.addPlayer(new BaconTimePlayerObject(0, 0, player.getName(), player.getUniqueId(), null));
                return;
            }
            List<BaconTimeReachedMilestoneObject> milestoneObjectList = playerRecord.getMilestones();
            Whoosh:
            for (BaconTimeMilestoneObject milestoneObject : ConfigHandler.AllMilestones.values()) {
                if (!player.hasPermission(MilestoneCheck + "." + milestoneObject.getMilestoneName())) {
                    continue;
                }
                if (milestoneObjectList != null) {
                    for (BaconTimeReachedMilestoneObject reachedMilestoneObject : milestoneObjectList) {
                        if (milestoneObject.getMilestoneName().equalsIgnoreCase(reachedMilestoneObject.getMilestoneName())) {
                            if (milestoneObject.isRepeatable()) {
                                if ((reachedMilestoneObject.getClaimedAt() + milestoneObject.getRequiredTime()) <= playerRecord.getActiveTime()) {
                                    for (String command : milestoneObject.getCommands()) {
                                        RunCommandsMain(command, player);
                                    }
                                    playerRecord.ReplaceMilestoneReached(new BaconTimeReachedMilestoneObject(reachedMilestoneObject.getMilestoneName(), playerRecord.getActiveTime(), true), reachedMilestoneObject.getMilestoneName());
                                }
                            }
                            continue Whoosh;
                        }
                    }
                }
                if (milestoneObject.isRepeatable() && (milestoneObject.getRequiredTime() <= playerRecord.getActiveTime())) {
                    for (String command : milestoneObject.getCommands()) {
                        RunCommandsMain(command, player);
                    }
                    playerRecord.addMilestoneReached(new BaconTimeReachedMilestoneObject(milestoneObject.getMilestoneName(), playerRecord.getActiveTime(), true));
                    continue;
                }
                if (milestoneObject.getRequiredTime() <= playerRecord.getActiveTime()) {
                    for (String command : milestoneObject.getCommands()) {
                        RunCommandsMain(command, player);
                    }
                    playerRecord.addMilestoneReached(new BaconTimeReachedMilestoneObject(milestoneObject.getMilestoneName(), playerRecord.getActiveTime(), false));
                }
            }
            DataHandler.players.replace(player.getUniqueId(), playerRecord);
            DataHandler.savePlayer(player.getUniqueId());
        }
    }

    public void RunCommandsMain(String command, Player player) {
        //We need to force this on the main thread or sponge goes whooooooooooooooooooooooooooooooooooooooooooooooooooooo
        Task.Builder taskBuilder = Task.builder();
        taskBuilder.execute(() -> Sponge.getCommandManager().process(Sponge.getServer().getConsole(), command.replace("<player>", player.getName()))).submit(BaconTime.getInstance());
    }
}
