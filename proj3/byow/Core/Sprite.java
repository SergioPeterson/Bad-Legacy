package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 * Sprite:
 * A sprite is any player, enemy or object that moves after the game has already been rendered.
 * <p>
 * Need to do:
 * Make more efficient, and get rid of undersea memory usage
 * ie. any variables that we don't really need or can be used more effectively.
 * <p>
 * Could do later:
 * Add an enemy Sprite
 * <p>
 * Bugs:
 * N/A
 */
public class Sprite {
    protected Pair<Integer, Integer> pos;
    protected TETile skin;
    protected TETile floor = Tileset.FLOOR;
    protected TETile bullet = Tileset.BULLET;
    protected String direction = null;

    //A sprite is made at position x and y with a given skin
    public Sprite(int x, int y, TETile skin) {
        this.skin = skin;
        this.pos = new Pair<>(x, y);
    }

    //place and draw the sprite at its current x and y positions
    public void place(TETile[][] world) {
        world[this.pos.a][this.pos.b] = this.skin;
    }

    //set the x and y value back to the floor
    public void clean(TETile[][] world, int x, int y) {
        world[x][y] = this.floor;
    }

    //moves the sprite one space up in the y direction
    public void moveUp(TETile[][] world, boolean overwrite) {
        if (world[pos.a][pos.b + 1] == Tileset.FLOOR) {
            clean(world, pos.a, pos.b);
            pos = new Pair<>(pos.a, pos.b + 1);
            place(world);
        } else if (overwrite) {
            clean(world, pos.a, pos.b);
            pos = new Pair<>(pos.a, pos.b + 1);
            place(world);
        }
        direction = "up";
    }

    //moves the sprite one space down in the y direction
    public void moveDown(TETile[][] world, boolean overwrite) {
        if (world[pos.a][pos.b - 1] == Tileset.FLOOR) {
            clean(world, pos.a, pos.b);
            pos = new Pair<>(pos.a, pos.b - 1);
            place(world);
        } else if (overwrite) {
            clean(world, pos.a, pos.b);
            pos = new Pair<>(pos.a, pos.b - 1);
            place(world);
        }
        direction = "down";
    }

    //moves the sprite one space right in the x direction
    public void moveRight(TETile[][] world, boolean overwrite) {
        if (world[pos.a + 1][pos.b] == Tileset.FLOOR) {
            clean(world, pos.a, pos.b);
            pos = new Pair<>(pos.a + 1, pos.b);
            place(world);
        } else if (overwrite) {
            clean(world, pos.a, pos.b);
            pos = new Pair<>(pos.a + 1, pos.b);
            place(world);
        }
        direction = "right";
    }

    //moves the sprite one space left in the x direction
    public void moveLeft(TETile[][] world, boolean overwrite) {
        if (world[pos.a - 1][pos.b] == Tileset.FLOOR) {
            clean(world, pos.a, pos.b);
            pos = new Pair<>(pos.a - 1, pos.b);
            place(world);
        } else if (overwrite) {
            clean(world, pos.a, pos.b);
            pos = new Pair<>(pos.a - 1, pos.b);
            place(world);
        }
        direction = "left";
    }
}
