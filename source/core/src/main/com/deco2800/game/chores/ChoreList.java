package com.deco2800.game.chores;

/**
 * A standard set of object names for the chore system
 */
public enum ChoreList {
    TV,
    DRINK,
    PUDDLE;
    // Keep adding more

    /**
     * Get the chore description for the given object
     * @return The description
     */
    public String getDescription() {
        switch (this) {
            case TV:
                return "Turn off TV";

            case DRINK:
                return "Have a little sippy";

            case PUDDLE:
                return "Clean up a puddle";

            default:
                return null;
        }
    }

    /**
     * Get the plural chore description for an amount of objects
     * @param amount The number of objects of this type
     * @return The description
     */
    public String getDescription(int amount) {
        switch (this) {
            case TV:
                return "Turn off " + amount + " TVs";

            case DRINK:
                return "Drink " + amount + " cans of Dountain Mew";

            case PUDDLE:
                return "Clean up " + amount + " puddles";

            default:
                return null;
        }
    }
}