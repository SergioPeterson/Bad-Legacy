package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 * <p>
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 * <p>
 * Ex:
 * world[x][y] = Tileset.FLOOR;
 * <p>
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {


    public static final TETile WALL = new TETile('¤', new Color(216, 128, 128),
            new Color(43, 11, 118), "Wall");
    public static final TETile FLOOR = new TETile('.', Color.darkGray, Color.black,
            "Floor");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "Nothing");
    public static final TETile GRASS = new TETile('"', Color.green, Color.black, "Grass");
    public static final TETile WATER = new TETile('~', new Color(70, 120, 245),
            new Color(44, 47, 163), "Water");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "Flower");
    public static final TETile LOCKED_DOOR = new TETile('Ξ', Color.orange, Color.black,
            "Locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('Π', Color.orange, Color.black,
            "Unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "Sand");
    public static final TETile MOUNTAIN = new TETile('▲', new Color(88, 107, 137),
            new Color(44, 47, 163), "Mountain");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "Tree");

    public static final TETile KEY = new TETile('Ψ', Color.yellow, Color.black, "Key");
    public static final TETile BULLET = new TETile('*', Color.blue, Color.black, "Bullet");
    public static final TETile ENEMY = new TETile('E', Color.yellow, Color.black, "Enemy");

    public static final TETile AVATAR = new TETile('Ж', Color.white, Color.black, "Thrower");
    public static final TETile AVATAR2 = new TETile('ʘ', Color.white, Color.black, "Viewer");
    public static final TETile AVATAR3 = new TETile('Ω', Color.white, Color.black, "Placer");
    public static final TETile TELEPORTER = new TETile('҉', Color.yellow, Color.black,
            "Teleporter");

    public static final TETile PLACER_LIGHT = new TETile('ȱ', Color.yellow, Color.black, "Torch");
    public static final TETile THROWER_LIGHT = new TETile('ƛ', Color.yellow, Color.black, "Fire");
}
