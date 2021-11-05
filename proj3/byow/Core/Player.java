package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.HashSet;

/**
 * Player:
 * A player is a sprite that is moved with the keyboard and can hold things
 * <p>
 * Need to do:
 * Make more efficient, and get rid of undersea memory usage
 * ie. any variables that we don't really need or can be used more effectively.
 * <p>
 * When running the player movement seems a little slow and lagging behind. Not sure why
 * but it could be something to do with player movement.
 * <p>
 * Could do later:
 * Give the player the ability to shoot.
 * Add a multiply function where there are two players with the other moving with the keys i j k l
 * <p>
 * Bugs:
 * N/A
 */
public class Player extends Sprite {
    private String moves = "";
    private boolean lightsOn = false;
    protected int numOfUses = 10;
    protected TETile ability = null;
    protected HashSet<Pair<Integer, Integer>> lights = new HashSet<>();
    private Map map;

    //makes a player in the given map at the maps player initializing position
    public Player(Map map, TETile avatar) {
        super(0, 0, avatar);
        this.map = map;
        boolean hasKey = false;

    }


    //checks if a keyboard key has ben pressed and if so it moves it according to the key pressed
    public boolean inputs(TETile[][] world, Map map, String key) {

        //key typed
        switch (key) {
            case "w" -> {
                //if the pixel above is a key make this player have a key and move it up
                if (world[pos.a][pos.b + 1] == Tileset.KEY) {
                    map.removeKey();
                    moveUp(world, true);
                } else if (world[pos.a][pos.b + 1] == ability) {
                    numOfUses++;
                    moveUp(world, true);
                } else if (world[pos.a][pos.b + 1] == Tileset.LOCKED_DOOR && map.getNumOfKeys() == 0) {
                    // if the pixel above is a locked door but it has a key
                    // it moved it up even if the pixel is not something you couldn't
                    //  normally move up to
                    map.removeDoor();
                    moveUp(world, true);
                } else if (map.isTeleporter(pos.a, pos.b + 1) && validMove(world, map.getOtherPortal(pos.a, pos.b + 1), 0, 1)) {
                    world[pos.a][pos.b] = Tileset.FLOOR;
                    pos = map.getOtherPortal(pos.a, pos.b + 1);
                    pos = new Pair<>(pos.a, pos.b + 1);
                    place(world);
                } else {
                    //move it up like normal
                    moveUp(world, false);
                }
                if (lightsOn) {
                    lightsOn = false;
                    return true;
                }
                moves += "w";

            }
            case "s" -> {
                //if the pixel below is a key make this player have a key and move it down
                if (world[pos.a][pos.b - 1] == Tileset.KEY) {
                    map.removeKey();
                    moveDown(world, true);
                } else if (world[pos.a][pos.b - 1] == ability) {
                    numOfUses++;
                    moveDown(world, true);
                } else if (world[pos.a][pos.b - 1] == Tileset.LOCKED_DOOR && map.getNumOfKeys() == 0) {
                    // if the pixel above is a locked door but it has a key
                    // it moved it up even if the pixel is not something you couldn't
                    //  normally move up to
                    map.removeDoor();
                    moveDown(world, true);
                } else if (map.isTeleporter(pos.a, pos.b - 1) && validMove(world, map.getOtherPortal(pos.a, pos.b - 1), 0, -1)) {
                    world[pos.a][pos.b] = Tileset.FLOOR;
                    pos = map.getOtherPortal(pos.a, pos.b - 1);
                    pos = new Pair<>(pos.a, pos.b - 1);
                    place(world);
                } else {
                    //move it down like normal
                    moveDown(world, false);
                }
                if (lightsOn) {
                    lightsOn = false;
                    return true;
                }
                moves += "s";
            }
            case "a" -> {
                //if the pixel to the left is a key make this player have a key and move it left
                if (world[pos.a - 1][pos.b] == Tileset.KEY) {
                    map.removeKey();
                    moveLeft(world, true);
                } else if (world[pos.a - 1][pos.b] == ability) {
                    numOfUses++;
                    moveLeft(world, true);
                } else if (world[pos.a - 1][pos.b] == Tileset.LOCKED_DOOR && map.getNumOfKeys() == 0) {
                    // if the pixel above is a locked door but it has a key
                    // it moved it up even if the pixel is not something you couldn't
                    //  normally move up to
                    map.removeDoor();
                    moveLeft(world, true);
                } else if (map.isTeleporter(pos.a - 1, pos.b) && validMove(world, map.getOtherPortal(pos.a - 1, pos.b), -1, 0)) {
                    world[pos.a][pos.b] = Tileset.FLOOR;
                    pos = map.getOtherPortal(pos.a - 1, pos.b);
                    pos = new Pair<>(pos.a - 1, pos.b);
                    place(world);
                } else {
                    //move it left like normal
                    moveLeft(world, false);
                }
                if (lightsOn) {
                    lightsOn = false;
                    return true;
                }
                moves += "a";
            }
            case "d" -> {
                //if the pixel below is a key make this player have a key and move it down
                if (world[pos.a + 1][pos.b] == Tileset.KEY) {
                    map.removeKey();
                    moveRight(world, true);
                } else if (world[pos.a + 1][pos.b] == ability) {
                    numOfUses++;
                    moveRight(world, true);
                } else if (world[pos.a + 1][pos.b] == Tileset.LOCKED_DOOR && map.getNumOfKeys() == 0) {
                    // if the pixel above is a locked door but it has a key
                    // it moved it up even if the pixel is not something you couldn't
                    //  normally move up to
                    map.removeDoor();
                    moveRight(world, true);
                } else if (map.isTeleporter(pos.a + 1, pos.b) && validMove(world, map.getOtherPortal(pos.a + 1, pos.b), 1, 0)) {
                    world[pos.a][pos.b] = Tileset.FLOOR;
                    pos = map.getOtherPortal(pos.a + 1, pos.b);
                    pos = new Pair<>(pos.a + 1, pos.b);
                    place(world);
                } else {
                    //move it right like normal
                    moveRight(world, false);
                }
                if (lightsOn) {
                    lightsOn = false;
                    return true;
                }
                moves += "d";
            }
            case "l" -> {
                if (numOfUses > 0) {
                    if (power(world)) {
                        lightsOn = true;
                        return true;
                    }
                }
                moves += "l";
            }
        }
        return false;
    }

    protected boolean power(TETile[][] world) {
        Bullet bullet = new Bullet(pos.a, pos.b);
        switch (direction) {
            case "up" -> {
                bullet.shootUp(world);
                numOfUses--;
            }
            case "down" -> {
                bullet.shootDown(world);
                numOfUses--;
            }
            case "right" -> {
                bullet.shootRight(world);
                numOfUses--;
            }
            case "left" -> {
                bullet.shootLeft(world);
                numOfUses--;
            }
            default -> {
                return false;
            }
        }
        return false;
    }

    private boolean validMove(TETile[][] world, Pair<Integer, Integer> telep, int x, int y) {
        if (world[telep.a + x][telep.b + y] == Tileset.FLOOR) {
            return true;
        }
        return false;
    }

    public String getMoves() {
        return moves;
    }

    public int getNumOfUses() {
        return numOfUses;
    }

    public void addUse() {
        numOfUses++;
    }

    public HashSet<Pair<Integer, Integer>> getLights() {
        return lights;
    }

    public void setMap(Map map) {
        this.map = map;
        this.pos = map.playerInit();
    }

    public void setHard() {
        this.numOfUses = this.numOfUses / 2;
    }

    public void setEasy() {
        this.numOfUses = this.numOfUses * 2;
    }

}
