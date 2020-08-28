package kristi71111.bacontime.tasks;

import io.github.nucleuspowered.nucleus.api.NucleusAPI;
import io.github.nucleuspowered.nucleus.api.service.NucleusAFKService;
import kristi71111.bacontime.handlers.ConfigHandler;
import kristi71111.bacontime.handlers.DataHandler;
import kristi71111.bacontime.handlers.objects.BaconTimePlayerObject;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

import java.time.Duration;
import java.util.Collection;

public class UpdatePlayTimesTask implements Runnable {
    @Override
    public void run() {
        final NucleusAFKService NUCLEUS_AFK_SERVICE = NucleusAPI.getAFKService().orElse(null);
        Collection<Player> players = Sponge.getServer().getOnlinePlayers();
        for (Player player : players) {
            BaconTimePlayerObject playerRecord = DataHandler.getPlayer(player.getUniqueId());
            if (playerRecord == null) {
                DataHandler.addPlayer(new BaconTimePlayerObject(0, 0, player.getName(), player.getUniqueId(), null));
                return;
            }
            if (NUCLEUS_AFK_SERVICE != null) {
                if (NUCLEUS_AFK_SERVICE.isAFK(player)) {
                    Duration afkTime = NUCLEUS_AFK_SERVICE.timeSinceLastActivity(player);
                    if (afkTime.getSeconds() < ConfigHandler.saveInterval) {
                        playerRecord.addAfkTime((int) afkTime.getSeconds());
                        playerRecord.addActiveTime((int) (ConfigHandler.saveInterval - afkTime.getSeconds()));
                    } else {
                        playerRecord.addAfkTime(ConfigHandler.saveInterval);
                    }
                } else {
                    playerRecord.addActiveTime(ConfigHandler.saveInterval);
                }
            } else {
                playerRecord.addActiveTime(ConfigHandler.saveInterval);
            }
            DataHandler.players.replace(player.getUniqueId(), playerRecord);
            DataHandler.savePlayer(player.getUniqueId());
        }
    }
}
