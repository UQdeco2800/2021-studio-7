package com.deco2800.game.chores;

/**
 * A standard set of object names for the chore system
 */
public enum ChoreList {
    BED,
    TV,
    DRINK;
    // Keep adding more

    /**
     * Get the chore description for the given object
     * @return The description
     */
    public String getDescription() {
        switch (this) {
            case BED:
                return "Get to bed";

            case TV:
                return "Turn off TV";

            case DRINK:
                return "Have a little sippy";

            default:
                return null;
        }
    }
}
