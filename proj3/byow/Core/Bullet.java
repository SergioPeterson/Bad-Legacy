package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Bullet extends Sprite {

    public Bullet(int x, int y) {
        super(x, y, Tileset.BULLET);
    }

    public void shootUp(TETile[][] world) {

        while (world[pos.a][pos.b + 1] == floor) {
            pos = new Pair<>(pos.a, pos.b + 1);
            place(world);
        }
    }

    public void shootDown(TETile[][] world) {
        while (world[pos.a][pos.b - 1] == floor) {
            pos = new Pair<>(pos.a, pos.b - 1);
            place(world);
        }

    }

    public void shootLeft(TETile[][] world) {
        while (world[pos.a - 1][pos.b] == floor) {
            pos = new Pair<>(pos.a - 1, pos.b);
            place(world);
        }

    }

    public void shootRight(TETile[][] world) {
        while (world[pos.a + 1][pos.b] == floor) {
            pos = new Pair<>(pos.a + 1, pos.b);
            place(world);
        }

    }
}
