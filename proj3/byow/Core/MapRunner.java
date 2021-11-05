package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;
import java.util.Objects;

/**
 * Map Tester:
 * Will just test the map so that we don't have to run it from the main class and specify which type we want to run
 * its just easier on us running it from a tester
 * <p>
 * Need to do:
 * N/A
 * <p>
 * Could do later:
 * N/A
 * <p>
 * Bugs:
 * N/A
 */

public class MapRunner {
    private final int WIDTH;
    private final int HEIGHT;
    private long Seed;
    private static final TETile[] avatars = {Tileset.AVATAR, Tileset.AVATAR2, Tileset.AVATAR3};
    private static final String numbers = "1234567890";
    private static final HashMap<Integer, String> codes = new HashMap<>();
    private final TERenderer ter = new TERenderer();
    public final File CWD = new File(System.getProperty("user.dir"));
    private static final FileFilter filter = pathname -> {
        return pathname.getName().endsWith(".txt");
    };

    public MapRunner(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        setCodes();
    }

    public TETile[][] run(String args) {
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        Map map = null;
        Player user = new Thrower(null);
        String difficulty = "n";
        long SEED;

        String command = args.substring(0, 1);
        String movement = args.substring(args.indexOf("s") + 1);

        if (command.equals("n")) {
            SEED = Long.parseLong(args.substring(1, args.indexOf("s")));
            map = new Map(WIDTH, HEIGHT, SEED, world);
            map.makeMap();
            map.setDifficulty(difficulty, user);
            user.setMap(map);
            user.place(world);
        } else if (command.equals("l")) {
            File save = Utils.join(CWD, "playerinfo.txt");
            String info = Utils.readContentsAsString(save);
            SEED = Long.parseLong(info.substring(1, info.indexOf("s")));
            map = new Map(WIDTH, HEIGHT, SEED, world);
            map.makeMap();
            String moves = info.substring(info.indexOf("s") + 1, info.indexOf("-"));
            TETile avatar = getTETile(info.substring(info.indexOf("-") + 1, info.indexOf("=")));
            difficulty = info.substring(info.indexOf("=") + 1);
            map.setDifficulty(difficulty, user);
            assert avatar != null;
            user = getPlayerNamed(avatar.description(), map);
            assert user != null;
            Engine.interactWithInputString(moves, user, map, world);
            movement = args.substring(args.indexOf("l") + 1);
        }
        for (Character move : movement.toCharArray()) {
            String input = String.valueOf(move);
            if (input.equals(":")) {
                assert map != null;
//                map.saveState(getName(movement.substring(movement.indexOf(":") + 1)), user);
                map.saveState(null, user);
                return world;
            }
            user.inputs(world, map, input);
        }
        return world;
    }

    public void run() {
        startUp();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        Map map = null;
        Player user = new Thrower(null);
        String val = null;
        String difficulty = "n";
        while (val == null) {
            val = menu();
            if (val == null) {
                //
            } else if (val.equals("n")) {
                String seed = getSeed();
                Seed = Long.parseLong(seed);
                map = new Map(WIDTH, HEIGHT, Seed, world);
                map.makeMap();
                map.setDifficulty(difficulty, user);
                user.setMap(map);
                user.place(world);
                descriptionMenu();

            } else if (val.equals("l")) {
                File save = Utils.join(CWD, "playerinfo.txt");
                String info = Utils.readContentsAsString(save);
                Seed = Long.parseLong(info.substring(1, info.indexOf("s")));
                map = new Map(WIDTH, HEIGHT, Seed, world);
                map.makeMap();
                String moves = info.substring(info.indexOf("s") + 1, info.indexOf("-"));
                TETile avatar = getTETile(info.substring(info.indexOf("-") + 1, info.indexOf("=")));
                difficulty = info.substring(info.indexOf("=") + 1);
                map.setDifficulty(difficulty, user);
                assert avatar != null;
                user = getPlayerNamed(avatar.description(), map);
                assert user != null;
                Engine.interactWithInputString(moves, user, map, world);
            } else if (val.equals("c")) {
                user = selectMenu(map);
                val = null;
            } else if (val.equals("d")) {
                difficulty = chooseDifficultyMenu();
                val = null;
            } else if (val.equals("q")) {
                System.exit(0);
                return;
            }
        }
        ter.renderFrame(world);
        boolean fullview = false;
        while (!won(map)) {
            HUD(world, user, map);

            if (StdDraw.hasNextKeyTyped()) {
                int holder = (int) StdDraw.nextKeyTyped();
                if (codes.containsKey(holder)) {
                    String key = codes.get(holder);
                    if (key.equals(":")) {
                        map.saveState(getName(), user);
                        endMenu("End of Game");
                        break;
                    }
                    if (key.equals("v")) {
                        fullview = !fullview;
                    } else {
                        if (user.inputs(world, map, key)) {
                            fullview = !fullview;
                        }
                    }
                }
            }
            ter.renderFrame(Map.filter(map, user.pos, fullview));
        }
    }

    public String chooseDifficultyMenu() {
        StdDraw.clear();
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.white);
        StdDraw.text((int) (WIDTH / 2), ((int) (HEIGHT / 2)) + 10, "Choose your difficutly");
        StdDraw.text((int) (WIDTH / 2), ((int) (HEIGHT / 2)) + 5, "(E) Easy");
        StdDraw.text((int) (WIDTH / 2), ((int) (HEIGHT / 2)), "(N) Normal");
        StdDraw.text((int) (WIDTH / 2), ((int) (HEIGHT / 2)) - 5, "(H) Hard");
        StdDraw.text(4, 1, "Unimpossible Inc.");
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                String val = codes.get((int) StdDraw.nextKeyTyped());
                if (val.equals("e") || val.equals("n") || val.equals("h")) {
                    return val;
                }
            }
        }
    }

    private static void setCodes() {
        codes.put(10, "Enter");
        codes.put(8, "Backspace");
        codes.put(32, "Space");
        codes.put(49, "1");
        codes.put(50, "2");
        codes.put(51, "3");
        codes.put(52, "4");
        codes.put(53, "5");
        codes.put(54, "6");
        codes.put(55, "7");
        codes.put(56, "8");
        codes.put(57, "9");
        codes.put(48, "0");
        codes.put(97, "a");
        codes.put(98, "b");
        codes.put(99, "c");
        codes.put(100, "d");
        codes.put(101, "e");
        codes.put(102, "f");
        codes.put(103, "g");
        codes.put(104, "h");
        codes.put(105, "i");
        codes.put(106, "j");
        codes.put(107, "k");
        codes.put(108, "l");
        codes.put(109, "m");
        codes.put(110, "n");
        codes.put(78, "n");
        codes.put(111, "o");
        codes.put(112, "p");
        codes.put(113, "q");
        codes.put(114, "r");
        codes.put(115, "s");
        codes.put(83, "s");
        codes.put(116, "t");
        codes.put(117, "u");
        codes.put(118, "v");
        codes.put(119, "w");
        codes.put(120, "x");
        codes.put(121, "y");
        codes.put(122, "z");
        codes.put(58, ":");
        codes.put(0, ";");
        codes.put(81, "q");
    }

    private void startUp() {
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
    }


    private String menu() {
        StdDraw.clear();
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, (HEIGHT / 2) + 15, "CS61B : The Game");
        StdDraw.text(WIDTH / 2, (HEIGHT / 2) + 10, "(N) New Game");
        StdDraw.text(WIDTH / 2, (HEIGHT / 2) + 5, "(L) Load Game");
        StdDraw.text(WIDTH / 2, (HEIGHT / 2), "(C) Change Player");
        StdDraw.text(WIDTH / 2, (HEIGHT / 2) - 5, "(D) Change Difficulty");
        StdDraw.text(WIDTH / 2, (HEIGHT / 2) - 10, "(Q) Exit Game");
        StdDraw.text(4, 1, "Unimpossible Inc.");
        StdDraw.show();
        if (StdDraw.hasNextKeyTyped()) {
//            System.out.println((int) StdDraw.nextKeyTyped());
            String val = codes.get((int) StdDraw.nextKeyTyped());
            if (val.equals("q") || val.equals("n") || val.equals("l") || val.equals("c") || val.equals("d")) {
                return val;
            }
        }
        return null;
    }

    private void selectTemplet(TETile avatar, String name) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "Choose Avatar (S):");
        StdDraw.text(WIDTH / 2 - 5, HEIGHT / 2, "<- ( a ) ");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, String.valueOf(avatar.character()));
        StdDraw.text(WIDTH / 2 + 5, HEIGHT / 2, " ( d ) ->");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, name);
        switch (name) {
            case "Placer":
                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 8, "Carries lanterns that you can drop throughout the map to keep areas visible");
                break;
            case "Thrower":
                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 8, "Can throw a stream of fire to illuminate your path");
                break;
            case "Viewer":
                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 8, "Can use it's power to see the entire map, but wears out after you take a step");
                break;
        }
        StdDraw.show();
    }

    private Player getPlayer(Map map, String args) {
        String val = args.substring(0, 1);
        int index = 0;
        while (!args.equals("")) {
            if (val.equals("d") || index != avatars.length - 1) {
                index++;
            } else if (val.equals("a") || index != 0) {
                index--;
            } else if (val.equals("s")) {
                return getPlayerNamed(avatars[index].description(), map);
            }
            args = args.substring(1);
            val = args.substring(0, 1);
        }
        return null;
    }

    private Player selectMenu(Map map) {
        int index = 0;
        selectTemplet(avatars[index], avatars[index].description());
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                String val = codes.get((int) StdDraw.nextKeyTyped());
                if (val.equals("s")) {
                    return getPlayerNamed(avatars[index].description(), map);
                }
                if (val.equals("a") && index != 0) {
                    index--;
                }
                if (val.equals("d") && index != avatars.length - 1) {
                    index++;
                }
                selectTemplet(avatars[index], avatars[index].description());
            }
        }
    }

    private static Player getPlayerNamed(String name, Map map) {
        switch (name) {
            case "Placer" -> {
                return new Placer(map);
            }
            case "Thrower" -> {
                return new Thrower(map);
            }
            case "Viewer" -> {
                return new Viewer(map);
            }
        }
        return null;
    }

    private void printMenu(String seed, String message) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, (HEIGHT / 2) + 10, message);
        StdDraw.text(WIDTH / 2, (HEIGHT / 2), seed);
        StdDraw.show();
    }

    private String getSeed() {
        String seed = "";
        printMenu(seed, "Enter Seed and hit (S) to begin");
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                String key = codes.get((int) StdDraw.nextKeyTyped());
                if (key.equals("s")) {
                    return seed;
                } else {
                    if (!numbers.contains(key)) {
                        continue;
                    }
                    seed += key;
                }
                printMenu(seed, "Enter Seed and hit (S) to begin");

            }
        }
    }

    private String getName(String name) {
        if (name.equals("q")) {
            return "Game " + Objects.requireNonNull(CWD.listFiles(filter)).length + 1;
        } else {
            return name;
        }
    }

    private String getName() {
        String name = "";
        printMenu(name, "Enter Save Name and press (S) or press (Q) to quick save");
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                String key = codes.get((int) StdDraw.nextKeyTyped());
                if (key.equals("q")) {
                    return "Game " + CWD.listFiles(filter).length + 1;
                }
                if (key.equals("s") || name.length() > 0) {
                    return name;
                } else {
                    name += key;
                }
                printMenu(name, "Enter Save Name and press (S) or press (Q) to quick save");

            }
        }
    }

    public void endMenu(String message) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, (HEIGHT / 2), message);
        StdDraw.text(WIDTH / 2, (HEIGHT / 2) - 5, "Try again? Y/N");
        StdDraw.text(4, 1, "Unimpossible Inc.");
        StdDraw.show();
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                String key = codes.get((int) StdDraw.nextKeyTyped());
                if (key.equals("y")) {
                    run();
                } else if (key.equals("n")) {
                    System.exit(0);
                }
            }
        }
    }


    private void descriptionMenu() {
        StdDraw.clear(Color.black);
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 3, "Pick Up all the keys ( Ψ ) to open the door ( Ξ )");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Use the teleporters (  ҉  ) to your advantage");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 6, "Good Luck");
        StdDraw.show();
        StdDraw.pause(5000);
    }

    //make pretty
    private String getSaved() {
        File[] files = CWD.listFiles(filter);
//        if (files == null || files.length == 0) {
//            endMenu("No Games Saved");
//        }
//        if (files.length == 1) {
//            return files[0];
//        }
//        StdDraw.clear(Color.BLACK);
//        StdDraw.setPenColor(Color.white);
//        StdDraw.text(WIDTH / 2, HEIGHT - 4, "Choose Saved Game");
//        int scale = HEIGHT / (files.length + 1) - 4;
//        int count = 1;
//        for (File file : files) {
//            StdDraw.text(WIDTH / 2, scale * count, count + " : " + file.getName());
//            count++;
//        }
//        StdDraw.show();
//        while (true) {
//            if (StdDraw.hasNextKeyTyped()) {
//                String val = codes.get((int) StdDraw.nextKeyTyped());
//                if (numbers.contains(String.valueOf(val)) && Integer.parseInt(val) <= files.length) {
//                    return files[Integer.parseInt(val) - 1];
//                }
//            }
//        }
        return null;
    }

    private void HUD(TETile[][] world, Player user, Map map) {
        StdDraw.setPenColor(Color.WHITE);
        if ((int) StdDraw.mouseX() < WIDTH && (int) StdDraw.mouseY() < HEIGHT) {
            StdDraw.text(WIDTH / 2, HEIGHT - 1, world[(int) StdDraw.mouseX()][(int) StdDraw.mouseY()].description());
        }

        StdDraw.text(WIDTH / 4, HEIGHT - 1, "Powers Left : " + user.getNumOfUses() + " (L) ");
        if (map.getNumOfKeys() > 0) {
            StdDraw.text(WIDTH - 5, HEIGHT - 1, "Keys Left : " + map.getNumOfKeys());
        } else {
            StdDraw.text(WIDTH - 5, HEIGHT - 1, "GO TO THE DOOR");
        }
        StdDraw.text(WIDTH - 10, 2, "Seed : " + Seed);
        StdDraw.text(10, 2, "Press (:) to open save menu");
        StdDraw.text(3, HEIGHT - 1, "Difficulty : " + map.getDifficulty());
        StdDraw.show();
    }

    private static TETile getTETile(String name) {
        for (TETile avatar : avatars) {
            if (avatar.description().equals(name)) {
                return avatar;
            }
        }
        return null;
    }

    private boolean won(Map map) {
        if (map.getNumOfKeys() == 0 && !map.hasDoor()) {
            endMenu("Congratulations!");
            return true;
        }
        return false;
    }
}
