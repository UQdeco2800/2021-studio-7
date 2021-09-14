package com.deco2800.game.areas.rooms;

import java.util.Arrays;

public class RoomGrid {
    int height;
    int width;

    String[][] grid;

    public RoomGrid(int width, int height) {
        this.width = width;
        this.height = height;
        // TODO Check width/height order
        this.grid = new String[height][width];
    }

    public RoomGrid(int width, int height, String[][] grid) {
        this.width = width;
        this.height = height;
        this.grid = grid;
    }

    private static String gridToString(String[][] grid) {
        StringBuilder sb = new StringBuilder();

        System.out.println(grid[0].length);

        for (String[] line : grid) {
            for (String texture : line) {
                sb.append(texture + ' ');
            }

            // Finish the row
            sb.append('\n');
        }

        return sb.toString();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String[][] getGrid() {
        return grid;
    }

    public String getGridCell(int width, int height) {
        return this.grid[height][width];
    }

    public void setGrid(String[][] grid) {
        this.grid = grid;
    }

    public void setGridCell(int width, int height, String value) {
        this.grid[height][width] = value;
    }

    @Override
    public String toString() {
        return "RoomGrid{" +
                "height=" + height +
                ", width=" + width +
                ", grid=\n" + gridToString(this.grid) +
                '}';
    }


}
