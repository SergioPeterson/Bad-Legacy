package byow.lab12;

import org.junit.Test;

import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int WIDTH = 80;
    private static final int HEIGHT = 50;
    private static final Random ran = new Random(12345);

    public static void setEmpty(TETile[][] world) {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }


    private static TETile randomTile() {
        int tileNum = ran.nextInt(5);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.FLOOR;
            case 4: return Tileset.SAND;
            default: return Tileset.NOTHING;
        }
    }

    private static void makeTesselation(TETile[][] world, int size){
        Hexagon hex = new Hexagon(world);
        hex.addHexagon(size, getWidth(size) + size ,0, randomTile());
        hex.addHexagon(size, getWidth(size) - size ,getHeight(size), randomTile());
        hex.addHexagon(size, getWidth(size) - size+1 ,size, randomTile());
        hex.addHexagon(size, getWidth(size) - size+1 ,size+getHeight(size), randomTile());
        hex.addHexagon(size, getWidth(size) - size+1 ,size+(getHeight(size)*2), randomTile());
        hex.addHexagon(size, getWidth(size) - size+1 ,size+(getHeight(size)*3), randomTile());
        hex.addHexagon(size, getWidth(size) - size+1 + getWidth(size) + size,size, randomTile());
        hex.addHexagon(size, getWidth(size) - size+1 + getWidth(size) + size,size+getHeight(size), randomTile());
        hex.addHexagon(size, getWidth(size) - size+1 + getWidth(size) + size,size+(getHeight(size)*2), randomTile());
        hex.addHexagon(size, getWidth(size) - size+1 + getWidth(size) + size,size+(getHeight(size)*3), randomTile());
        hex.addHexagon(size, 0 ,getHeight(size), randomTile());
        hex.addHexagon(size, 0 ,getHeight(size)*2, randomTile());
        hex.addHexagon(size, 0 ,getHeight(size)*3, randomTile());
        hex.addHexagon(size, getWidth(size) + size ,getHeight(size), randomTile());
        hex.addHexagon(size, getWidth(size) + size ,getHeight(size)*2, randomTile());
        hex.addHexagon(size, getWidth(size) + size ,getHeight(size)*3, randomTile());
        hex.addHexagon(size, getWidth(size) + size ,getHeight(size)*4, randomTile());
        hex.addHexagon(size, (getWidth(size) + size) * 2 ,getHeight(size), randomTile());
        hex.addHexagon(size, (getWidth(size) + size) * 2 ,getHeight(size)*2, randomTile());
        hex.addHexagon(size, (getWidth(size) + size) * 2 ,getHeight(size)*3, randomTile());


    }

    private static int getWidth(int size){
        return (size * 2) + (size - 2);
    }
    private static int getHeight(int size){
        return  size * 2;
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        setEmpty(world);

        makeTesselation(world, 4);

        ter.renderFrame(world);

    }
}
