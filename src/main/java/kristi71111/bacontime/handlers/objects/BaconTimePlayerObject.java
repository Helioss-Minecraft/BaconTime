package kristi71111.bacontime.handlers.objects;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.UUID;

public class BaconTimePlayerObject {
    private final UUID uuid;
    private int activeTime;
    private int afkTime;
    private String username;
    private Object2ObjectOpenHashMap<String, BaconTimeReachedMilestoneObject> milestones;

    public BaconTimePlayerObject(int activeTime, int afkTime, String username, UUID uuid, Object2ObjectOpenHashMap<String, BaconTimeReachedMilestoneObject> milestones) {
        this.activeTime = activeTime;
        this.afkTime = afkTime;
        this.username = username;
        this.uuid = uuid;
        this.milestones = milestones;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(int activeTime) {
        this.activeTime = activeTime;
    }

    public int getAfkTime() {
        return afkTime;
    }

    public void setAfkTime(int afkTime) {
        this.afkTime = afkTime;
    }

    public void addAfkTime(int add) {
        this.afkTime += add;
    }

    public void addActiveTime(int add) {
        this.activeTime += add;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addMilestoneReached(BaconTimeReachedMilestoneObject object) {
        if (this.milestones == null) {
            this.milestones = new Object2ObjectOpenHashMap<String, BaconTimeReachedMilestoneObject>();
        }
        this.milestones.put(object.getMilestoneName(), object);
    }

    public void ReplaceMilestoneReached(BaconTimeReachedMilestoneObject object) {
        this.milestones.replace(object.getMilestoneName(), object);
    }

    public Object2ObjectOpenHashMap<String, BaconTimeReachedMilestoneObject> getMilestones() {
        return milestones;
    }
}
