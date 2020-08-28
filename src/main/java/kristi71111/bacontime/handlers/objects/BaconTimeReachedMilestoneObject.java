package kristi71111.bacontime.handlers.objects;

public class BaconTimeReachedMilestoneObject {
    private String milestoneName;
    private int claimedAt;
    private boolean repeats;

    public BaconTimeReachedMilestoneObject(String milestoneName, int claimedAt, boolean repeats) {
        this.milestoneName = milestoneName;
        this.claimedAt = claimedAt;
        this.repeats = repeats;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public int getClaimedAt() {
        return claimedAt;
    }

    public void setClaimedAt(int claimedAt) {
        this.claimedAt = claimedAt;
    }

    public boolean isRepeats() {
        return repeats;
    }

    public void setRepeats(boolean repeats) {
        this.repeats = repeats;
    }
}
