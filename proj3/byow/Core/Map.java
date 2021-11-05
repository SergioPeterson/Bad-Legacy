package byow.Core;


import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


import java.io.File;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Random;


/**
 * Map Generator:
 * Will generate a map and all the things a starter map will have like the doors and keys
 * Does not generate the player the player will be generated after the map is made.
 * <p>
 * Need to do:
 * Make more efficient, and get rid of undersea memory usage
 * ie. any variables that we don't really need or can be used more effectively.
 * And any loops that are not needed or can be made faster
 * <p>
 * Could do later:
 * N/A
 * <p>
 * Bugs:
 * At times when you place the key and the locked door
 * the key is placed in the room with the locked door making it impossible
 * to open the door
 * ie. Seed : 1000  and 1500
 */
public class Map implements Serializable {
    private final int WIDTH;
    private final int HEIGHT;
    private Random RANDOM;
    private final long SEED;
    private final LinkedList<Room> rooms = new LinkedList<>();
    private TETile[][] world;
    private Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> teleporter;
    public static final File CWD = new File(System.getProperty("user.dir"));
    private int numOfKeys = 3;
    private boolean hadDoor = true;
    private int playerView = 4;
    private int torchesView = 4;
    private int fireView = 2;
    private String difficulty = "n";

    public Map(int width, int height, long seed, TETile[][] world) {
        this.HEIGHT = height;
        this.WIDTH = width;
        this.SEED = seed;
        this.RANDOM = new Random(SEED);
        this.world = world;
    }

    public void makeMap() {
        makeEmpty();
        makeRooms();
        makeConnections();
        addTexture();
        refine();
    }

    private void makeEmpty() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Tileset.WATER;
            }
        }
    }

    private void makeRooms() {
        int trys = 100;
        int keys = numOfKeys;
        boolean placedLock = false;
        int telepsPlaced = 0;
        Room telep1 = null;
        Room telep2 = null;
        while (trys > 0) {
            int height = RANDOM.nextInt(6) + 6;
            int width = RANDOM.nextInt(6) + 6;

            Room newRoom = new Room(width, height);

            int x = RANDOM.nextInt(WIDTH - width - 2) + 3;
            int y = RANDOM.nextInt(HEIGHT - height - 2) + 3;


            if (newRoom.fits(world, x, y)) {

                if (keys > 0) {
                    newRoom.placeKey(RANDOM.nextInt((width - 5) * (height - 5)));
                    keys--;
                } else if (!placedLock) {
                    newRoom.placeLock(RANDOM.nextInt((width - 5) * (height - 5)));
                    placedLock = true;
                } else if (telepsPlaced == 0) {
                    newRoom.placeTelep(RANDOM.nextInt((width - 5) * (height - 5)));
                    telepsPlaced++;
                    telep1 = newRoom;
                } else if (telepsPlaced == 1) {
                    newRoom.placeTelep(RANDOM.nextInt((width - 5) * (height - 5)));
                    telepsPlaced++;
                    telep2 = newRoom;
                }
                newRoom.makeRoom(world, x, y);
                rooms.add(newRoom);
            } else {
                trys--;
            }
        }
        teleporter = new Pair<>(telep1.getTeleporter(), telep2.getTeleporter());
    }

    private void makeConnections() {
        while (rooms.size() > 1) {
            Room r1 = rooms.get(0);
            Room r2 = closestRoom(r1);
            r1.connectTo(world, r2);
            rooms.remove(0);
        }
    }

    private double distance(Room a, Room b) {
        return Math.sqrt(Math.pow((b.getxCenter() - a.getxCenter()), 2)
                + Math.pow((b.getyCenter() - a.getyCenter()), 2));
    }

    private Room closestRoom(Room room) {
        int place = 0;
        double distance = HEIGHT + WIDTH;
        for (int index = 0; index < rooms.size(); index++) {
            if (!(room.getxCenter() == rooms.get(index).getxCenter()
                    && room.getyCenter() == rooms.get(index).getyCenter())) {
                if (distance(room, rooms.get(index)) < distance) {
                    distance = distance(room, rooms.get(index));
                    place = index;
                }
            }
        }
        return rooms.get(place);
    }

    public Pair<Integer, Integer> playerInit() {
        Room room = rooms.get(RANDOM.nextInt(rooms.size()));
        return room.postion(RANDOM.nextInt((room.getHeight() - 3) * (room.getWidth() - 3) + 2));
    }

    private void refine() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (world[x][y] == Tileset.WALL && wallOnOposite(x, y)) {
                    world[x][y] = Tileset.FLOOR;
                }
                if (world[x][y] == Tileset.WALL
                        && numOfSurandedBy(Tileset.FLOOR, x, y) >= 3) {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
    }

    private boolean wallOnOposite(int x, int y) {
        return (world[x + 1][y] == Tileset.FLOOR && world[x - 1][y] == Tileset.FLOOR)
                || (world[x][y + 1] == Tileset.FLOOR && world[x][y - 1] == Tileset.FLOOR);
    }

    private void addTexture() {

        int trys = 200;
        while (trys > 0) {
            int height = RANDOM.nextInt(4) + 2;
            int width = RANDOM.nextInt(4) + 2;

            Room land = new Room(width, height);

            int x = RANDOM.nextInt(WIDTH - width - 2) + 2;
            int y = RANDOM.nextInt(HEIGHT - height - 2) + 2;

            if (land.fits(world, x, y)) {

                land.makeLand(world, x, y);

            } else {
                trys--;
            }
        }

    }

    private int numOfSurandedBy(TETile item, int x, int y) {
        int num = 0;
        if (x > 0 && x < world.length - 1) {
            if (world[x + 1][y] == item) {
                num++;
            }
            if (world[x - 1][y] == item) {
                num++;
            }
        }
        if (y > 0 && y < world[0].length - 1) {
            if (world[x][y + 1] == item) {
                num++;
            }
            if (world[x][y - 1] == item) {
                num++;
            }
        }
        return num;
    }

    //only for one save rn
    public void saveState(String name, Player user) {

        File info = Utils.join(CWD, "playerinfo.txt");
        info.delete();
        String movementOutput = "n" + SEED + "s" + user.getMoves();
        Utils.writeContents(info, movementOutput, "-",
                user.skin.description(), "=", this.difficulty);

    }

    public void returnWorld(Player user) {
        RANDOM = new Random(SEED);
        makeMap();
        user = new Player(this, user.skin);
        user.place(world);

    }


    public boolean isTeleporter(int x, int y) {

        return world[x][y] == Tileset.TELEPORTER;
    }

    public Pair<Integer, Integer> getOtherPortal(int x, int y) {
        if (teleporter.a.a == x && teleporter.a.b == y) {
            return new Pair<>(teleporter.b.a, teleporter.b.b);
        }
        return new Pair<>(teleporter.a.a, teleporter.a.b);
    }

    public void setRenderDistance(int x) {
        this.playerView = x;
    }

    public void setFireView(int x) {
        this.fireView = x;
    }

    public void setTorchesView(int x) {
        this.torchesView = x;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    private boolean inXofItem(int dis, TETile item, int x, int y) {
//        x = Math.max(x, 5);
//        y = Math.max(y, 5);
        for (int indexX = Math.max(x - dis, 1);
             ((x + dis < WIDTH) && (indexX < x + dis)); indexX++) {
            for (int indexY = Math.max(y - dis, 1);
                 ((y + dis < HEIGHT) && (indexY < y + dis)); indexY++) {
                if (world[indexX][indexY] == item) {
                    return true;
                }
            }
        }
        return false;
    }

    public static TETile[][] filter(Map map, Pair<Integer, Integer> pos, boolean full) {
        TETile[][] filterWorld = new TETile[map.WIDTH][map.HEIGHT];
        if (full) {
            for (int x = 0; x < map.WIDTH; x++) {
                for (int y = 0; y < map.HEIGHT; y++) {
                    filterWorld[x][y] = map.world[x][y];
                }
            }
        } else {
            for (int x = 0; x < map.WIDTH; x++) {
                for (int y = 0; y < map.HEIGHT; y++) {
                    if (pos.a - map.playerView <= x && x <= pos.a + map.playerView
                            && pos.b - map.playerView <= y && y <= pos.b + map.playerView) {
                        filterWorld[x][y] = map.world[x][y];
                    } else if (map.inXofItem(map.torchesView, Tileset.PLACER_LIGHT, x, y)) {
                        filterWorld[x][y] = map.world[x][y];
                    } else if (map.inXofItem(map.fireView, Tileset.THROWER_LIGHT, x, y)) {
                        filterWorld[x][y] = map.world[x][y];
                    } else {
                        filterWorld[x][y] = Tileset.NOTHING;
                    }
                }
            }
        }
        return filterWorld;
    }

    public int getNumOfKeys() {
        return numOfKeys;
    }

    public void removeKey() {
        numOfKeys--;
    }

    public boolean hasDoor() {
        return hadDoor;
    }

    public void removeDoor() {
        hadDoor = false;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String d, Player user) {
        if (d.equals("n")) {
            this.difficulty = "n";
            this.playerView = 5;
            this.fireView = 3;
            this.torchesView = 5;
        } else if (d.equals("h")) {
            this.difficulty = "h";
            this.playerView = 3;
            this.fireView = 1;
            this.torchesView = 3;
            user.setHard();
        } else if (d.equals("e")) {
            this.difficulty = "e";
            this.playerView = 8;
            this.fireView = 6;
            this.torchesView = 8;
            user.setEasy();
        }
    }
}
