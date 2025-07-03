public enum ResourceType {
    STUDY_ROOM("Study Room"),
    CONFERENCE_ROOM("Conference Room"),
    COMPUTER_LAB("Computer Lab"),
    EQUIPMENT("Equipment"),
    PROJECTOR("Projector"),
    VEHICLE("Vehicle"),
    SPORTS_FACILITY("Sports Facility"),
    LIBRARY_SPACE("Library Space");
    
    private final String displayName;
    
    ResourceType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}