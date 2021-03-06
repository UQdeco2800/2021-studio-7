package com.deco2800.game.chores;

/**
 * A standard set of object names for the chore system
 */
public enum ChoreList {
    TV,
    DRINK,
    PUDDLE,
    DISHWASHER,
    PLANT,
    SHRUB,
    BOOKS,
    TRASH;
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
                return "Drink a can of Dountain Mew (TM)";

            case PUDDLE:
                return "Clean up a puddle";
            case DISHWASHER:
                return "Wash the dishes";
            case PLANT:
                return "Water the plants";
            case SHRUB:
                return "Trim the shrubs";
            case BOOKS:
                return "Pick up books";
            case TRASH:
                return "Pick up trash";
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
            case DISHWASHER:
                return "Wash the dishes";
            case PLANT:
                return "Water the plants";
            case SHRUB:
                return "Trim the shrubs";
            case BOOKS:
                return "Pick up books";
            case TRASH:
                return "Pick up trash";
            default:
                return null;
        }
    }

    /*public String getComplete() {
        switch(this) {
            case TV:
                return "Turning the TV off was hard... but now theres no evidence";
            case DRINK:
                return "Ah... now the temptation of energy drink is finally vanquished";
            case DISHWASHER:
        }
    }*/
}
