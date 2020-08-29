package kristi71111.bacontime.handlers.serializers;

import com.google.gson.*;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import kristi71111.bacontime.handlers.objects.BaconTimePlayerObject;
import kristi71111.bacontime.handlers.objects.BaconTimeReachedMilestoneObject;

import java.lang.reflect.Type;
import java.util.UUID;

public class BaconTimePlayerRecordDeserializer implements JsonDeserializer<BaconTimePlayerObject> {
    public BaconTimePlayerObject deserialize(JsonElement json, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        int activeTime = obj.get("activeTime").getAsInt();
        int afkTime = obj.get("afkTime").getAsInt();
        String username = obj.get("username").getAsString();
        UUID uuid = UUID.fromString(obj.get("uuid").getAsString());
        if (obj.get("milestones") != null) {
            Object2ObjectOpenHashMap<String, BaconTimeReachedMilestoneObject> playersMilestones = new Object2ObjectOpenHashMap<String, BaconTimeReachedMilestoneObject>();
            for (JsonElement e : obj.get("milestones").getAsJsonArray()) {
                JsonObject milestoneObj = e.getAsJsonObject();
                BaconTimeReachedMilestoneObject milestone = new BaconTimeReachedMilestoneObject(milestoneObj.get("milestoneName").getAsString(), milestoneObj.get("reachedAt").getAsInt(), milestoneObj.get("repeats").getAsBoolean());
                playersMilestones.put(milestoneObj.get("milestoneName").getAsString(), milestone);
            }
            return new BaconTimePlayerObject(activeTime, afkTime, username, uuid, playersMilestones);
        } else {
            return new BaconTimePlayerObject(activeTime, afkTime, username, uuid, null);
        }

    }
}
