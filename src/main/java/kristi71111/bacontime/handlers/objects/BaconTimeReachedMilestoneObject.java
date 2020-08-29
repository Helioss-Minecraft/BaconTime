package kristi71111.bacontime.handlers.objects;

public class BaconTimeReachedMilestoneObject {
    private final String milestoneName;
    private final int claimedAt;
    private final boolean repeats;

    public BaconTimeReachedMilestoneObject(String milestoneName, int claimedAt, boolean repeats) {
        this.milestoneName = milestoneName;
        this.claimedAt = claimedAt;
        this.repeats = repeats;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public int getClaimedAt() {
        return claimedAt;
    }

    public boolean isRepeats() {
        return repeats;
    }
}
