package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.*;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    public static final int MAX_ROOM_WIDTH = 9;
    public static final int MAX_ROOM_HEIGHT = 9;
    public static final int MAX_HALLWAYS_WIDTH = 8;

    private static Random RANDOM;

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        int[] pos = new int[2];

        guidePage();

        String input = "";
        while (input.length() < 1) {
            if (StdDraw.hasNextKeyTyped()) {
                input += StdDraw.nextKeyTyped();
            }
        }
        switch (input) {
            case "N":
                startNewWorld(finalWorldFrame);
                pos = initPosition(finalWorldFrame, ter);
                break;
            case "L":
                finalWorldFrame = (TETile[][]) loadWorld("world");
                pos = (int[]) loadWorld("player");
                break;
            case "Q":
                StdDraw.clear(new Color(0, 0, 0));
                Font font = new Font("Monaco", Font.BOLD, 30);
                StdDraw.setFont(font);
                StdDraw.setPenColor(Color.white);
                StdDraw.text(WIDTH/2, HEIGHT/2, "You have quit the game!");
                StdDraw.show();
                break;
            default:

        }
        Font font1 = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(font1);
        ter.renderFrame(finalWorldFrame);

        char input1 = ' ';
        while (input1 != 'Q') {
            if (StdDraw.hasNextKeyTyped()) {
                input1 = StdDraw.nextKeyTyped();
                playerMovement1(pos, input1, finalWorldFrame, ter);
            }
        }

        saveWorld(finalWorldFrame, "world");
        saveWorld(pos, "player");

        StdDraw.clear(new Color(0, 0, 0));
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH/2, HEIGHT/2, "You have saved and quit the game!");
        StdDraw.show();
    }

    private void playerMovement1(int[] pos, char input, TETile[][] world, TERenderer ter) {
        switch (input) {
            case 'W':
                if (checkMove(world, pos[0], pos[1]+1)) {
                    world[pos[0]][pos[1]] = Tileset.FLOOR;
                    pos[1] += 1;
                    world[pos[0]][pos[1]] = Tileset.PLAYER;
                    ter.renderFrame(world);
                }
                break;
            case 'D':
                if (checkMove(world, pos[0]+1, pos[1])) {
                    world[pos[0]][pos[1]] = Tileset.FLOOR;
                    pos[0] += 1;
                    world[pos[0]][pos[1]] = Tileset.PLAYER;
                    ter.renderFrame(world);
                }
                break;
            case 'S':
                if (checkMove(world, pos[0], pos[1]-1)) {
                    world[pos[0]][pos[1]] = Tileset.FLOOR;
                    pos[1] -= 1;
                    world[pos[0]][pos[1]] = Tileset.PLAYER;
                    ter.renderFrame(world);
                }
                break;
            case 'A':
                if (checkMove(world, pos[0]-1, pos[1])) {
                    world[pos[0]][pos[1]] = Tileset.FLOOR;
                    pos[0] -= 1;
                    world[pos[0]][pos[1]] = Tileset.PLAYER;
                    ter.renderFrame(world);
                }
                break;
            default:
        }
    }

    private void startNewWorld(TETile[][] world) {
        StdDraw.clear(new Color(0, 0, 0));
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH/2, HEIGHT/2, "Please enter a seed, and end with 'S'");
        StdDraw.show();

        String input = "";
        while (!input.substring(Math.max(0, input.length() - 1)).equals("S")) {
            if (StdDraw.hasNextKeyTyped()) {
                input += StdDraw.nextKeyTyped();
                drawFrame(input);
            }
        }
        RANDOM = new Random(Integer.parseInt(input.substring(0, input.length()-1)));
        initWorld(world);

        //create first room includes locked door
        final int doorX = RANDOM.nextInt(WIDTH);
        final int doorY = RANDOM.nextInt(HEIGHT);

        int width = RANDOM.nextInt(MAX_ROOM_WIDTH-4)+4;
        int height = RANDOM.nextInt(MAX_ROOM_HEIGHT-4)+4;


        int[] loc = randomDoorLoc(doorX, doorY, width, height, RANDOM.nextInt(4));
        int[] reDir = {2, 3, 0, 1};
        int[][] doorLoc = new int[1][3];
        doorLoc[0][0] = doorX;
        doorLoc[0][1] = doorY;
        doorLoc[0][2] = reDir[loc[2]];

        addRoom(loc[0], loc[1], width, height, world, doorLoc);
        world[doorX][doorY] = Tileset.LOCKED_DOOR;
    }

    private void drawFrame(String input) {
        StdDraw.clear(Color.black);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH/2, HEIGHT/2, input);
        StdDraw.show();

    }

    private void guidePage() {
        int midWidth = WIDTH/2;
        int midHeight = HEIGHT/2;

        StdDraw.clear(Color.black);

        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, HEIGHT*(3/4.0), "CS61B: THE GAME");

        Font font1 = new Font("Monaco", Font.PLAIN, 20);
        StdDraw.setFont(font1);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight+2, "New Game (N)");
        StdDraw.text(midWidth, midHeight, "Load Game (L)");
        StdDraw.text(midWidth, midHeight-2, "Quit (Q)");

        StdDraw.show();
    }

//    public static void main(String[] args) {
//        Game game = new Game();
//        // origin code: N11133SDDDDDDDDDDDDSSS:S
//        //game.playWithInputString("N11133S");
//        game.playWithKeyboard();
//    }



    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] finalWorldFrame = new TETile[WIDTH][HEIGHT];
        int[] pos;

        String start = input.substring(0, 1);

        if (start.equals("N")) {
            int seed = Integer.parseInt(input.split("N")[1].split("S", 2)[0]);
            RANDOM = new Random(seed);

            initWorld(finalWorldFrame);

            //create first room includes locked door
            final int doorX = RANDOM.nextInt(WIDTH);
            final int doorY = RANDOM.nextInt(HEIGHT);

            int width = RANDOM.nextInt(MAX_ROOM_WIDTH-4)+4;
            int height = RANDOM.nextInt(MAX_ROOM_HEIGHT-4)+4;

            int[] loc = randomDoorLoc(doorX, doorY, width, height, RANDOM.nextInt(4));
            int[] reDir = {2, 3, 0, 1};
            int[][] doorLoc = new int[1][3];
            doorLoc[0][0] = doorX;
            doorLoc[0][1] = doorY;
            doorLoc[0][2] = reDir[loc[2]];

            addRoom(loc[0], loc[1], width, height, finalWorldFrame, doorLoc);
            finalWorldFrame[doorX][doorY] = Tileset.LOCKED_DOOR;

            pos = initPosition(finalWorldFrame, ter);

            ter.renderFrame(finalWorldFrame);

            playerMovement(pos, input.split("S", 2)[1], finalWorldFrame, ter);

        } else if (start.equals("L")){
            finalWorldFrame = (TETile[][]) loadWorld("world");
            pos = (int[]) loadWorld("player");

            ter.renderFrame(finalWorldFrame);

            playerMovement(pos, input.substring(1), finalWorldFrame, ter);

        }


        if (input.split(":").length == 1) {
            int mouseX = (int) StdDraw.mouseX();
            int mouseY = (int) StdDraw.mouseY();
            while (StdDraw.mouseX() < WIDTH-1 && StdDraw.mouseY() < HEIGHT-1 ){
                int currentX = (int) StdDraw.mouseX();
                int currentY = (int) StdDraw.mouseY();
                if (currentY != mouseY || currentX != mouseX) {
                    createHUD(finalWorldFrame, ter);
                    mouseY = currentY;
                    mouseX = currentX;
                }
            }
        }


        return finalWorldFrame;
    }

    private static Object loadWorld(String name) {
        File f = new File("./" + name + ".ser");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                Object loadWorld = os.readObject();
                os.close();
                return loadWorld;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }

        /* In the case no World has been saved yet, we return a new one. */
        return null;
    }

    private void createHUD(TETile[][] world, TERenderer ter) {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        StdDraw.clear(new Color(0, 0, 0));
        ter.renderFrame(world);
        StdDraw.setPenColor(Color.white);
        StdDraw.textLeft(1, HEIGHT-1, world[x][y].description());
        StdDraw.show();
    }

    private void playerMovement(int[] pos, String input, TETile[][] world, TERenderer ter) {
        String quitChoice = "";
        if (input.split(":").length > 1) {
            quitChoice = input.split(":")[1].substring(0, 1);
        }
        char[] movement = input.split(":")[0].toCharArray();
        for (int i=0; i<movement.length; i++) {
            switch (movement[i]) {
                case 'W':
                    if (checkMove(world, pos[0], pos[1]+1)) {
                        world[pos[0]][pos[1]] = Tileset.FLOOR;
                        pos[1] += 1;
                        world[pos[0]][pos[1]] = Tileset.PLAYER;
                        StdDraw.pause(100);
                        ter.renderFrame(world);
                    }
                   break;
                case 'D':
                    if (checkMove(world, pos[0]+1, pos[1])) {
                        world[pos[0]][pos[1]] = Tileset.FLOOR;
                        pos[0] += 1;
                        world[pos[0]][pos[1]] = Tileset.PLAYER;
                        StdDraw.pause(100);
                        ter.renderFrame(world);
                    }
                    break;
                case 'S':
                    if (checkMove(world, pos[0], pos[1]-1)) {
                        world[pos[0]][pos[1]] = Tileset.FLOOR;
                        pos[1] -= 1;
                        world[pos[0]][pos[1]] = Tileset.PLAYER;
                        StdDraw.pause(100);
                        ter.renderFrame(world);
                    }
                    break;
                case 'A':
                    if (checkMove(world, pos[0]-1, pos[1])) {
                        world[pos[0]][pos[1]] = Tileset.FLOOR;
                        pos[0] -= 1;
                        world[pos[0]][pos[1]] = Tileset.PLAYER;
                        StdDraw.pause(100);
                        ter.renderFrame(world);
                    }
                    break;
                default:
            }
        }
        System.out.println(quitChoice);
        if (quitChoice.equals("Q")) {
            StdDraw.clear(new Color(0, 0, 0));
            Font font = new Font("Monaco", Font.BOLD, 30);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH/2, HEIGHT/2, "You have quit the game!");
            StdDraw.show();
        } else if (quitChoice.equals("S")) {
            saveWorld(world, "world");
            saveWorld(pos, "player");

            StdDraw.clear(new Color(0, 0, 0));
            Font font = new Font("Monaco", Font.BOLD, 30);
            StdDraw.setFont(font);
            StdDraw.setPenColor(Color.white);
            StdDraw.text(WIDTH/2, HEIGHT/2, "You have saved and quit the game!");
            StdDraw.show();
        }
    }

    private static void saveWorld(Object w, String name) {
        File f = new File("./" + name + ".ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(w);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private boolean checkMove(TETile[][] world, int x, int y) {
        switch (world[x][y].character()) {
            case '·':
                return true;
            case '#':
                StdDraw.setPenColor(Color.white);
                StdDraw.textLeft(1, HEIGHT-1, "Oops! You hit the wall!");
                StdDraw.pause(1500);
                StdDraw.show();
                return false;
            case '█':
                StdDraw.clear(new Color(0, 0, 0));
                Font font = new Font("Monaco", Font.BOLD, 30);
                StdDraw.setFont(font);
                StdDraw.setPenColor(Color.white);
                StdDraw.text(WIDTH/2, HEIGHT/2, "Congratulation! You successfully escape the room!");
                StdDraw.show();
                return false;
            default:
                return false;
        }
    }

    private int[] initPosition(TETile[][] world, TERenderer ter) {
        Map<Integer, Integer> floorLoc = new HashMap<>();
        for (int i=0; i<WIDTH; i++) {
            for (int j=0; j<HEIGHT; j++) {
                if (world[i][j] == Tileset.FLOOR){
                    floorLoc.put(i, j);
                }
            }
        }
        int[] xy = new int[2];
        if (floorLoc.size() > 0) {
            int n = RANDOM.nextInt(floorLoc.size());
            xy[0] = (int) floorLoc.keySet().toArray()[n];
            xy[1] = floorLoc.get(xy[0]);
            world[xy[0]][xy[1]] = Tileset.PLAYER;
        }

        return xy;
    }

    private void addRoom(int x, int y, int w, int h, TETile[][] world, int[][] loc) {
        if (x < 0 || x+w >= WIDTH || y < 0 || y+h >= HEIGHT) {
            return;
        }
        if (!isEmpty(x,y,w,h,world)) {
            return;
        }
        addRoad(x, y, w, h, world);
        if (loc.length == 3) {
            world[loc[0][0]][loc[0][1]] = Tileset.FLOOR;
            world[loc[1][0]][loc[1][1]] = Tileset.FLOOR;
        }
        ArrayList<Integer> dirs = randomHallways(loc);
        for (int i: dirs) {
            int[] doorLoc = randomDoorLoc(x+w-1, y+h-1, w, h, i);
            if (doorLoc[2] != i) return;
            addHallways(doorLoc[0], doorLoc[1], world, i);
        }
    }

    private void addHallways(int x, int y, TETile[][] world, int i) {
        int w = RANDOM.nextInt(MAX_HALLWAYS_WIDTH-3) + 3;
        switch (i) {
            case 0:
                if (y+w >= HEIGHT) return;
                if (!isEmpty(x-1, y+1, 3, w, world)) return;
                addRoad(x-1, y+1, 3, w, world);
                world[x][y] = Tileset.FLOOR;
                world[x][y+1] = Tileset.FLOOR;
                addRandomHOR(x-1, y+1, 3, w, world, i);
                break;
            case 1:
                if (x+w >= WIDTH) return;
                if (!isEmpty(x+1, y-1, w, 3, world)) return;
                addRoad(x+1, y-1, w, 3, world);
                world[x][y] = Tileset.FLOOR;
                world[x+1][y] = Tileset.FLOOR;
                addRandomHOR(x+1, y-1, w, 3, world, i);
                break;
            case 2:
                if (y-w < 0) return;
                if (!isEmpty(x-1, y-w, 3, w, world)) return;
                addRoad(x-1, y-w, 3, w, world);
                world[x][y] = Tileset.FLOOR;
                world[x][y-1] = Tileset.FLOOR;
                addRandomHOR(x-1, y-w, 3, w, world, i);
                break;
            case 3:
                if (x-w < 0) return;
                if (!isEmpty(x-w, y-1, w, 3, world)) return;
                addRoad(x-w, y-1, w, 3, world);
                world[x][y] = Tileset.FLOOR;
                world[x-1][y] = Tileset.FLOOR;
                addRandomHOR(x-w, y-1, w, 3, world, i);
                break;
            default:
        }
    }

    private void addRandomHOR(int x, int y, int w, int h, TETile[][] world, int type) {
        int isRoom = RANDOM.nextInt(5);
        if (isRoom == 0) {
            if (type == 0|| type == 2) {
                int dir = RANDOM.nextInt(2)*2+1;
                int[] loc = randomDoorLoc(x+w-1, y+h-1, w, h, dir);
                addHallways(loc[0], loc[1], world, dir);
            } else {
                int dir = RANDOM.nextInt(2)*2;
                int[] loc = randomDoorLoc(x+w-1, y+h-1, w, h, dir);
                addHallways(loc[0], loc[1], world, dir);
            }
        } else {
            int[][] loc = new int[3][2];
            int[] roomLoc = new int[2];

            int width = RANDOM.nextInt(MAX_ROOM_WIDTH-4)+4;
            int height = RANDOM.nextInt(MAX_ROOM_HEIGHT-4)+4;
            switch (type) {
                case 0:
                    loc[0][0] = x+1;
                    loc[0][1] = y+h-1;
                    loc[1][0] = x+1;
                    loc[1][1] = y+h;
                    loc[2][0] = 2;
                    roomLoc = randomDoorLoc(x+1, y+h, width, height, 0);
                    addRoom(roomLoc[0], roomLoc[1], width, height, world, loc);
                    break;
                case 1:
                    loc[0][0] = x+w-1;
                    loc[0][1] = y+1;
                    loc[1][0] = x+w;
                    loc[1][1] = y+1;
                    loc[2][0] = 3;
                    roomLoc = randomDoorLoc(x+w, y+1, width, height, 1);
                    addRoom(roomLoc[0], roomLoc[1], width, height, world, loc);
                    break;
                case 2:
                    loc[0][0] = x+1;
                    loc[0][1] = y;
                    loc[1][0] = x+1;
                    loc[1][1] = y-1;
                    loc[2][0] = 0;
                    roomLoc = randomDoorLoc(x+1, y-1, width, height, 2);
                    if (roomLoc[2] != 2) return;
                    addRoom(roomLoc[0], roomLoc[1], width, height, world, loc);
                    break;
                case 3:
                    loc[0][0] = x;
                    loc[0][1] = y+1;
                    loc[1][0] = x-1;
                    loc[1][1] = y+1;
                    loc[2][0] = 1;
                    roomLoc = randomDoorLoc(x-1, y+1, width, height, 3);
                    if (roomLoc[2] != 3) return;
                    addRoom(roomLoc[0], roomLoc[1], width, height, world, loc);
                    break;
                default:
            }
        }
    }

    private ArrayList<Integer> randomHallways(int[][] loc) {
        int num = RANDOM.nextInt(4) + 1;
        ArrayList<Integer> dirs = new ArrayList<>();
        if (loc.length == 1) {
            dirs.add(loc[0][2]);
            num = (num==1) ? num+1:num;
        } else {
            dirs.add(loc[2][0]);
            num = (num==1) ? num+1:num;
        }
        while(dirs.size() < num) {
            int dir = RANDOM.nextInt(4);
            if (!dirs.contains(dir)) dirs.add(dir);
        }
        if (loc.length == 1) {
            dirs.remove((Object) loc[0][2]);
        } else {
            dirs.remove((Object) loc[2][0]);
        }
        return dirs;
    }

    private ArrayList<Integer> advancedHallways(int x, int y, int w, int h, TETile[][] world, int[][] loc) {
        int num = RANDOM.nextInt(4) + 1;
        int[] advancedHallways = new int[4];
        ArrayList<Integer> dirs = new ArrayList<>();
        if (loc.length == 1) {
            dirs.add(loc[0][2]);
            num = (num==1) ? num+1:num;
        } else {
            dirs.add(loc[2][0]);
            num = (num==1) ? num+1:num;
        }
        advancedHallways[0] = (x>=(WIDTH-(x+w))) ? 3:1;
        advancedHallways[1] = (y>=(HEIGHT-(y+h))) ? 2:0;
        advancedHallways[2] = (x>=(WIDTH-(x+w))) ? 1:3;
        advancedHallways[3] = (y>=(HEIGHT-(y+h))) ? 0:2;

        int i=0;
        while(dirs.size() < num) {
            if (!dirs.contains(advancedHallways[i])) dirs.add(advancedHallways[i]);
            i++;
        }

        if (loc.length == 1) dirs.remove((Object) loc[0][2]);
        return dirs;
    }

    private void addRoad(int x, int y, int w, int h, TETile[][] world) {
        world[x][y] = Tileset.WALL;
        world[x][y+h-1] = Tileset.WALL;
        world[x+w-1][y] = Tileset.WALL;
        world[x+w-1][y+h-1] = Tileset.WALL;
        for (int i=1; i<w-1; i++) {
            for (int j=1; j<h-1; j++) {
                world[x+i][y+j] = Tileset.FLOOR;
                world[x][y+j] = Tileset.WALL;
                world[x+w-1][y+j] = Tileset.WALL;
                world[x+i][y] = Tileset.WALL;
                world[x+i][y+h-1] = Tileset.WALL;
            }
        }
    }


    private boolean isEmpty(int x, int y, int w, int h, TETile[][] world) {
        for (int i=0; i<w; i++) {
            for (int j=0; j<h; j++) {
                if(world[x+i][y+j] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }

    private int[] randomDoorLoc(int doorX, int doorY, int width, int height, int type) {
        int[] loc = new int[3];
        switch (type) {
            case 0:
                loc[0] = Math.max(0, doorX - RANDOM.nextInt(width-2) - 1);
                loc[1] = doorY;
                loc[2] = type;
                return loc;
            case 1:
                loc[0] = doorX;
                loc[1] = Math.max(0, doorY - RANDOM.nextInt(height-2) - 1);
                loc[2] = type;
                return loc;
            case 2:
                loc[0] = Math.max(0, doorX - RANDOM.nextInt(width-2) - 1);
                loc[1] = (doorY - height + 1 < 0) ? doorY : doorY - height + 1;
                loc[2] = (loc[1] == doorY) ? 0:2;
                return loc;
            case 3:
                loc[0] = (doorX - width + 1 < 0) ? doorX : doorX - width + 1;
                loc[1] = Math.max(0, doorY - RANDOM.nextInt(height-2) - 1);
                loc[2] = (loc[0] == doorX) ? 1:3;
                return loc;
            default:
                return loc;
        }
    }

    private void initWorld(TETile[][] world) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

}
