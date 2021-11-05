package byow.lab12;

import byow.TileEngine.TETile;

public class Hexagon {
    private TETile[][] world;

    public Hexagon(TETile[][] world){
        this.world = world;
    }


    public void addHexagon(int size, int xTraslation, int yTraslation, TETile texture){
        int height = size * 2;
        int width = (size * 2) + (size - 2);
        int midPoint = height / 2;
        int count = 1;

        for (int x = 0; x < width; x++) {
            int value = count;

            boolean skip = x > size - 2 && x <= width - size;
            for (int y = midPoint - 1+yTraslation; y >= yTraslation; y--) {
                if (skip) {
                    world[x+xTraslation][y] = texture;
                    count = width / 2 - (size / 2);
                } else if (value > 0) {
                    world[x+xTraslation][y] = texture;
                    value--;
                }
            }

            value = count;
            for (int y = midPoint+yTraslation; y < height+yTraslation; y++) {
                if (skip) {
                    world[x+xTraslation][y] = texture;
                } else if (value > 0) {
                    world[x+xTraslation][y] = texture;
                    value--;
                }

            }
            if (x < size - 1) {
                count++;
            } else if (x > width - size) {
                count--;
            }
        }
    }

}
