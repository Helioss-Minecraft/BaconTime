package kristi71111.bacontime.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import kristi71111.bacontime.handlers.objects.BaconTimePlayerObject;
import kristi71111.bacontime.handlers.serializers.BaconTimePlayerRecordDeserializer;
import kristi71111.bacontime.handlers.serializers.BaconTimePlayerRecordSerializer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

public class DataHandler {
    public static Object2ObjectOpenHashMap<UUID, BaconTimePlayerObject> players;
    private static File PlayersDir;
    private static Gson gson;

    public static void init(File rootDir) {
        PlayersDir = new File(rootDir, "storage");
        gson = new GsonBuilder().registerTypeAdapter(BaconTimePlayerObject.class, new BaconTimePlayerRecordSerializer()).registerTypeAdapter(BaconTimePlayerObject.class, new BaconTimePlayerRecordDeserializer()).setPrettyPrinting().create();
    }

    public static void load() {
        PlayersDir.mkdirs();
        players = new Object2ObjectOpenHashMap<>();
        for (File f : PlayersDir.listFiles()) {
            if ((f.isFile()) && (f.getName().matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\.json"))) {
                try {
                    String json = new String(Files.readAllBytes(f.toPath()));
                    BaconTimePlayerObject playerRecord = gson.fromJson(json, BaconTimePlayerObject.class);
                    players.put(playerRecord.getUUID(), playerRecord);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static BaconTimePlayerObject getPlayer(UUID uuid) {
        return players.getOrDefault(uuid, null);
    }

    public static void addPlayer(BaconTimePlayerObject playerRecord) {
        players.put(playerRecord.getUUID(), playerRecord);
        savePlayer(playerRecord.getUUID());
    }

    public static void save() {
        for (UUID uuid : players.keySet()) {
            savePlayer(uuid);
        }
    }

    public static void savePlayer(UUID uuid) {
        BaconTimePlayerObject playerRecord = getPlayer(uuid);
        if (playerRecord == null) {
            return;
        }
        File file = new File(PlayersDir, uuid.toString() + ".json");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            String json = gson.toJson(playerRecord, BaconTimePlayerObject.class);
            Files.write(file.toPath(), json.getBytes());
            if (players.containsKey(uuid)) {
                players.replace(uuid, playerRecord);
            } else {
                players.put(uuid, playerRecord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object2ObjectOpenHashMap<UUID, BaconTimePlayerObject> getPlayers() {
        return players;
    }

    public static void setPlayers(Object2ObjectOpenHashMap<UUID, BaconTimePlayerObject> playersSet) {
        players = playersSet;
    }
}
