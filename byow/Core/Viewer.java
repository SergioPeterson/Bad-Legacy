package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Viewer extends Player {

    public Viewer(Map map) {
        super(map, Tileset.AVATAR2);
        numOfUses = 3;
    }

    @Override
    protected boolean power(TETile[][] world) {
        numOfUses--;
        return true;
    }
}
