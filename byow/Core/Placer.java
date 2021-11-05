package byow.Core;


import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Placer extends Player {

    private final TETile light = Tileset.PLACER_LIGHT;

    public Placer(Map map) {
        super(map, Tileset.AVATAR3);
        numOfUses = 5;
        ability = Tileset.PLACER_LIGHT;
    }

    private void place(TETile[][] world, int x, int y) {
        if (world[x][y] == Tileset.FLOOR) {
            world[x][y] = light;
            lights.add(new Pair<>(x, y));
        } else {
            numOfUses++;
        }
    }


    @Override
    protected boolean power(TETile[][] world) {
        switch (direction) {
            case "up" -> {
                place(world, pos.a, pos.b + 1);
                numOfUses--;
            }
            case "down" -> {
                place(world, pos.a, pos.b - 1);
                numOfUses--;
            }
            case "right" -> {
                place(world, pos.a + 1, pos.b);
                numOfUses--;
            }
            case "left" -> {
                place(world, pos.a - 1, pos.b);
                numOfUses--;
            }
            default -> {
                return false;
            }
        }
        return false;
    }
}
