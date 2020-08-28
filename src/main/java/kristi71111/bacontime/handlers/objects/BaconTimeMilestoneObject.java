package kristi71111.bacontime.handlers.objects;

import java.util.List;

public class BaconTimeMilestoneObject {
    private String milestoneName;
    private int requiredTime;
    private List<String> commands;
    private boolean repeatable;

    public BaconTimeMilestoneObject(String milestoneName, int requiredTime, List<String> commands, boolean repeatable) {
        this.milestoneName = milestoneName;
        this.requiredTime = requiredTime;
        this.commands = commands;
        this.repeatable = repeatable;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public String getMilestoneName() {
        return milestoneName;
    }

    public void setMilestoneName(String milestoneName) {
        this.milestoneName = milestoneName;
    }

    public int getRequiredTime() {
        return requiredTime;
    }

    public void setRequiredTime(int requiredTime) {
        this.requiredTime = requiredTime;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
}
