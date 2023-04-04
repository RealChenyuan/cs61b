package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final long SEED = 1234856;
    private static final Random RANDOM = new Random(SEED);

    public static void addHexagon(int s, int x, int y, TETile[][] tiles, TETile type) {
        int height = tiles[0].length;
        int width = tiles.length;
        if (x-s+1 < 0 || x+s >= width || y<0 || y+2*s > height) {
            System.out.println("Out of bounds!");
            return;
        }
        for (int j=0; j<s; j++) {
            for (int i=0; i<s+2*j; i++) {
                tiles[x-j+i][y+j] = type;
                tiles[x-j+i][y+2*s-1-j] = type;
            }
        }
    }

    public static void tesselationHex(int s, TETile[][] tiles) {
        int height = tiles[0].length;
        int startX = s-1;
        int startY = height/2-s-1;
        drawHex(s, startX, startY, tiles);
    }

    private static void drawHex(int s, int startX, int startY, TETile[][] tiles) {
        addHexagon(s, startX, startY, tiles, randomTile());
        int[][] vertexes = getAllStartVertex(s, startX, startY);
        for (int i=0; i<6; i++) {
            if (isEmptySpace(s, vertexes[i][0], vertexes[i][1], tiles)) {
                drawHex(s, vertexes[i][0], vertexes[i][1], tiles);
            }
        }
    }

    private static boolean isEmptySpace(int s, int x, int y, TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        if (x-s+1 < 0 || x+s >= width || y<0 || y+2*s > height) {
            return false;
        }
        for (int j=0; j<s; j++) {
            for (int i=0; i<s+2*j; i++) {
                if (tiles[x-j+i][y+j] != Tileset.NOTHING || tiles[x-j+i][y+2*s-1-j] != Tileset.NOTHING) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int[][] getAllStartVertex(int s, int startX, int startY) {
        int[][] vertexs = new int[6][2];
        vertexs[0][0] = startX;
        vertexs[0][1] = startY-2*s;
        vertexs[1][0] = startX+2*s-1;
        vertexs[1][1] = startY-s;
        vertexs[2][0] = startX+2*s-1;
        vertexs[2][1] = startY+s;
        vertexs[3][0] = startX;
        vertexs[3][1] = startY+2*s;
        vertexs[4][0] = startX-2*s+1;
        vertexs[4][1] = startY+s;
        vertexs[5][0] = startX-2*s+1;
        vertexs[5][1] = startY-s;
        return vertexs;
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(6);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.WATER;
            case 4: return Tileset.TREE;
            case 5: return Tileset.MOUNTAIN;
            default: return Tileset.NOTHING;
        }
    }


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(60, 30);

        TETile[][] randomTiles = new TETile[60][30];
        for (int x = 0; x < 60; x += 1) {
            for (int y = 0; y < 30; y += 1) {
                randomTiles[x][y] = Tileset.NOTHING;
            }
        }

        tesselationHex(4, randomTiles);

        ter.renderFrame(randomTiles);
    }
}
