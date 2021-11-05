package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Thrower extends Player {
    public Thrower(Map map) {
        super(map, Tileset.AVATAR);
        numOfUses = 50;
        ability = Tileset.THROWER_LIGHT;
    }


    @Override
    protected boolean power(TETile[][] world) {
        switch (direction) {
            case "up" -> lightUp(world, pos.a, pos.b);
            case "down" -> lightDown(world, pos.a, pos.b);
            case "right" -> lightRight(world, pos.a, pos.b);
            case "left" -> lightLeft(world, pos.a, pos.b);
            default -> {
                return false;
            }
        }
        return false;
    }

    private void lightLeft(TETile[][] world, int a, int b) {
        a--;
        while (world[a][b] == Tileset.FLOOR && numOfUses > 0) {
            world[a][b] = Tileset.THROWER_LIGHT;
            lights.add(new Pair<>(a, b));
            a--;
            numOfUses--;
        }
    }

    private void lightRight(TETile[][] world, int a, int b) {
        a++;
        while (world[a][b] == Tileset.FLOOR && numOfUses > 0) {
            world[a][b] = Tileset.THROWER_LIGHT;
            lights.add(new Pair<>(a, b));
            a++;
            numOfUses--;
        }
    }

    private void lightDown(TETile[][] world, int a, int b) {
        b--;
        while (world[a][b] == Tileset.FLOOR && numOfUses > 0) {
            world[a][b] = Tileset.THROWER_LIGHT;
            lights.add(new Pair<>(a, b));
            b--;
            numOfUses--;
        }
    }

    private void lightUp(TETile[][] world, int a, int b) {
        b++;
        while (world[a][b] == Tileset.FLOOR && numOfUses > 0) {
            world[a][b] = Tileset.THROWER_LIGHT;
            lights.add(new Pair<>(a, b));
            b++;
            numOfUses--;
        }
    }
}
