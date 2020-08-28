package kristi71111.bacontime.handlers.objects;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;
import java.util.UUID;

public class BaconTimePlayerObject {
    private int activeTime;
    private int afkTime;
    private String username;
    final UUID uuid;
    private List<BaconTimeReachedMilestoneObject> milestones;

    public BaconTimePlayerObject(int activeTime, int afkTime, String username, UUID uuid, List<BaconTimeReachedMilestoneObject> milestones) {
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
            this.milestones = new ObjectArrayList<>();
        }
        this.milestones.add(object);
    }

    public void ReplaceMilestoneReached(BaconTimeReachedMilestoneObject object, String name) {
        List<BaconTimeReachedMilestoneObject> milestoneObjects = this.milestones;
        for (BaconTimeReachedMilestoneObject object2 : milestoneObjects) {
            if (object2.getMilestoneName().equalsIgnoreCase(name)) {
                this.milestones.remove(object2);
            }
        }
        this.milestones.add(object);
    }

    public List<BaconTimeReachedMilestoneObject> getMilestones() {
        return milestones;
    }

    public void setMilestones(List<BaconTimeReachedMilestoneObject> milestones) {
        this.milestones = milestones;
    }
}
