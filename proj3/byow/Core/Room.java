package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/**
 * Room Class:
 * Will hold a room and all the things a room has inside it
 * and all the things a room can do
 * <p>
 * Need to do:
 * Make more efficient, and get rid of undersea memory usage
 * ie. any variables that we don't really need or can be used more effectively
 * <p>
 * Could do later:
 * Make room class be a superclass of smaller subclasses of specific rooms
 * like a rectangular room that extends room or a triangular room that extends room
 * so that we can have more types of rooms not just rectangular rooms.
 * We could even make specific rooms do specific shit like give the player
 * the ability to walk faster or slower.
 * <p>
 * Bugs:
 * N/A
 */

public class Room {
    private final int width;
    private final int height;
    private int xCenter;
    private int yCenter;
    private Pair<Integer, Integer> topLeft;
    private Pair<Integer, Integer> topRight;
    private Pair<Integer, Integer> bottomLeft;
    private Pair<Integer, Integer> bottomRight;
    private final TETile floor = Tileset.FLOOR;
    private final TETile wall = Tileset.WALL;
    private final TETile lock = Tileset.LOCKED_DOOR;
    private final TETile key = Tileset.KEY;
    private boolean hasLock = false;
    private boolean hasKey = false;
    private int valueHolder = 0;
    private boolean hasTelep = false;
    private Pair<Integer, Integer> teleporter;

    //Room with width and height
    public Room(int width, int height) {
        this.height = height;
        this.width = width;
    }

    public void makeRoom(TETile[][] world, int xPos, int yPos) {
        Pair<Integer, Integer> holder = null;

        this.xCenter = width / 2 + xPos;
        this.yCenter = height / 2 + yPos;

        for (int x = xPos; x < width + xPos; x++) {
            for (int y = yPos; y < height + yPos; y++) {
                //if wall
                if (x == xPos) {
                    world[x][y] = wall;
                    if (y == yPos) {
                        this.bottomLeft = new Pair<>(x, y);
                    } else if (y == height + yPos - 1) {
                        this.topLeft = new Pair<>(x, y);
                    }
                } else if (x == width + xPos - 1) {
                    world[x][y] = wall;
                    if (y == yPos) {
                        this.bottomRight = new Pair<>(x, y);
                    } else if (y == height + yPos - 1) {
                        this.topRight = new Pair<>(x, y);
                    }
                } else if (y == yPos) {
                    world[x][y] = wall;
                } else if (y == height + yPos - 1) {
                    world[x][y] = wall;
                } else {
                    if (y == yPos + 2 || x == xPos + 2
                            || y == height + yPos - 2 || x == width + xPos - 2) {
                        world[x][y] = floor;
                    } else {
                        if (hasKey && valueHolder == 0) {
                            world[x][y] = key;
                            hasKey = false;
                        } else if (hasLock && valueHolder == 0) {
                            world[x][y] = lock;
                            hasLock = false;
                        } else if (hasTelep && valueHolder == 0) {
                            world[x][y] = Tileset.TELEPORTER;
                            hasTelep = false;
                            holder = new Pair<>(x, y);
                        } else if ((hasKey || hasTelep || hasLock) && valueHolder > 0) {
                            world[x][y] = floor;
                            valueHolder--;
                        } else {
                            world[x][y] = floor;
                        }
                    }
                }
            }
        }
        this.teleporter = holder;
    }

    //Connects this room to the other room using the make path function;
    public void connectTo(TETile[][] world, Room other) {
        makePath(world, this.xCenter, this.yCenter, other.xCenter, other.yCenter);
    }

    private void makePath(TETile[][] world, int x1, int y1, int x2, int y2) {

        makeCorner(world, x2, y1);

        for (int x = Math.min(x1, x2); x <= Math.max(x2, x1); x++) {
            if (world[x][y1] == wall || world[x][y1] == Tileset.WATER) {
                if (world[x][y1 + 1] != floor) {
                    world[x][y1 + 1] = wall;
                }
                world[x][y1] = floor;
                if (world[x][y1 - 1] != floor) {
                    world[x][y1 - 1] = wall;
                }
            }
        }

        for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
            if (world[x2][y] == wall || world[x2][y] == Tileset.WATER) {
                if (world[x2 + 1][y] != floor) {
                    world[x2 + 1][y] = wall;
                }
                world[x2][y] = floor;
                if (world[x2 - 1][y - 1] != floor) {
                    world[x2 - 1][y - 1] = wall;
                }
            }
        }
    }

    //checks if the room fits at a specified point
    public boolean fits(TETile[][] world, int xPos, int yPos) {

        for (int x = xPos - 1; x < width + xPos + 1; x++) {
            for (int y = yPos - 1; y < height + yPos + 1; y++) {
                if (x >= world.length || y >= world[0].length) {
                    return false;
                } else if ((world[x][y] == Tileset.WALL || world[x][y] == Tileset.FLOOR
                        || world[x][y] == Tileset.MOUNTAIN)) {
                    return false;
                }
            }
        }
        return true;
    }

    //makes a corner at a cord
    private void makeCorner(TETile[][] world, int x, int y) {
        if (world[x][y] == Tileset.WATER) {
            world[x][y] = wall;
        }
        if (world[x][y + 1] == Tileset.WATER) {
            world[x][y + 1] = wall;
        }
        if (world[x][y - 1] == Tileset.WATER) {
            world[x][y - 1] = wall;
        }
        if (world[x + 1][y] == Tileset.WATER) {
            world[x + 1][y] = wall;
        }
        if (world[x + 1][y + 1] == Tileset.WATER) {
            world[x + 1][y + 1] = wall;
        }
        if (world[x + 1][y - 1] == Tileset.WATER) {
            world[x + 1][y - 1] = wall;
        }
        if (world[x - 1][y] == Tileset.WATER) {
            world[x - 1][y] = wall;
        }
        if (world[x - 1][y + 1] == Tileset.WATER) {
            world[x - 1][y + 1] = wall;
        }
        if (world[x - 1][y - 1] == Tileset.WATER) {
            world[x - 1][y - 1] = wall;
        }
    }

    public Pair<Integer, Integer> postion(int count) {
        for (int x = bottomLeft.a + 1; x < bottomRight.a; x++) {
            for (int y = bottomLeft.b + 1; y < topLeft.b; y++) {
                if (count == 0) {
                    return new Pair<>(x, y);
                }
                count--;
            }
        }
        return null;
    }

    public void makeLand(TETile[][] world, int xPos, int yPos) {
        for (int x = xPos; x < width + xPos; x++) {
            for (int y = yPos; y < height + yPos; y++) {
                world[x][y] = Tileset.MOUNTAIN;
            }
        }
    }


    //Get statements, remove the unnecessary ones
    public int getxCenter() {
        return xCenter;
    }

    public void placeLock(int value) {
        hasLock = true;
        this.valueHolder = value;
    }

    public void placeKey(int value) {
        hasKey = true;
        this.valueHolder = value;
    }

    public void placeTelep(int value) {
        hasTelep = true;
        this.valueHolder = value;
    }

    public Pair<Integer, Integer> getTeleporter() {
        return this.teleporter;
    }

    public int getyCenter() {
        return yCenter;
    }


    public Pair<Integer, Integer> getBottomRight() {
        return bottomRight;
    }

    public Pair<Integer, Integer> getTopLeft() {
        return topLeft;
    }

    public Pair<Integer, Integer> getTopRight() {
        return topRight;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

}
