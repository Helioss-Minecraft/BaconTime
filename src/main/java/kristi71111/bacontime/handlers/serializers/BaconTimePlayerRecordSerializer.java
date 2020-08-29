package kristi71111.bacontime.handlers.serializers;

import com.google.gson.*;
import kristi71111.bacontime.handlers.objects.BaconTimePlayerObject;
import kristi71111.bacontime.handlers.objects.BaconTimeReachedMilestoneObject;

import java.lang.reflect.Type;

public class BaconTimePlayerRecordSerializer implements JsonSerializer<BaconTimePlayerObject> {
    public JsonElement serialize(BaconTimePlayerObject playerRecord, Type type, JsonSerializationContext ctx) {
        JsonObject json = new JsonObject();
        json.add("activeTime", new JsonPrimitive(playerRecord.getActiveTime()));
        json.add("afkTime", new JsonPrimitive(playerRecord.getAfkTime()));
        json.add("username", new JsonPrimitive(playerRecord.getUsername()));
        json.add("uuid", new JsonPrimitive(playerRecord.getUUID().toString()));
        if (playerRecord.getMilestones() != null && !playerRecord.getMilestones().isEmpty()) {
            JsonArray milestonesArray = new JsonArray();
            for (BaconTimeReachedMilestoneObject milestone : playerRecord.getMilestones().values()) {
                JsonObject milestoneJson = new JsonObject();
                milestoneJson.add("milestoneName", new JsonPrimitive(milestone.getMilestoneName()));
                milestoneJson.add("reachedAt", new JsonPrimitive(milestone.getClaimedAt()));
                milestoneJson.add("repeats", new JsonPrimitive(milestone.isRepeats()));
                milestonesArray.add(milestoneJson);
            }
            json.add("milestones", milestonesArray);
        }
        return json;
    }
}
