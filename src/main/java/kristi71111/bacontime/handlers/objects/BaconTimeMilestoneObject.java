package kristi71111.bacontime.handlers.objects;

import java.util.List;

public class BaconTimeMilestoneObject implements Comparable<BaconTimeMilestoneObject> {
    private final String milestoneName;
    private final int requiredTime;
    private final List<String> commands;
    private final boolean repeatable;

    public BaconTimeMilestoneObject(String milestoneName, int requiredTime, List<String> commands, boolean repeatable) {
        this.milestoneName = milestoneName;
        this.requiredTime = requiredTime;
        this.commands = commands;
        this.repeatable = repeatable;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public int getRequiredTime() {
        return requiredTime;
    }

    public List<String> getCommands() {
        return commands;
    }

    @Override
    public int compareTo(BaconTimeMilestoneObject o) {
        return Integer.compare(this.requiredTime, o.requiredTime);
    }
}
