import java.util.*;

public class Minefield {
    private final int tilesInXDirection;
    private final int tilesInYDirection;
    private final int numberOfTiles;
    private Tile[][] tileArray;
    private int[][] allTilePositions;
    private final float percentageOfMines;

    Minefield(int tilesInXDirection, int tilesInYDirection, float percentageOfMines) {
        this.tilesInXDirection = tilesInXDirection;
        this.tilesInYDirection = tilesInYDirection;
        numberOfTiles = tilesInXDirection * tilesInYDirection;
        this.percentageOfMines = percentageOfMines;
        constructAttrs();
    }

    /*
    constructing methods
     */
    private void constructAttrs() {
        allTilePositions = generateAllTilePositions();
        tileArray = newTilesArray();
    }

    private int[][] generateAllTilePositions() {
        int[][] positions = new int[numberOfTiles][2];
        int index = 0;
        for (int x = 0; x < tilesInXDirection; x++) {
            for (int y = 0; y < tilesInYDirection; y++) {
                positions[index] = new int[] {x, y};
                index++;
            }
        }
        return positions;
    }

    private Tile[][] newTilesArray() {
        Tile[][] array = new Tile[tilesInXDirection][tilesInYDirection];
        for (int[] pos: allTilePositions) {
           array[pos[0]][pos[1]] = new Tile();
        }
        return array;
    }

    /*
    initialization
     */
    public void init(int xPos, int yPos) {
        int[] pos = new int[] {xPos, yPos};
        int[][] minePositions = getMinePositions(pos);
        makeMines(minePositions);
        setMinesAroundTiles();
    }

    private int[][] getMinePositions(int[] pos) {
        int minesToCreate = (int) (numberOfTiles * percentageOfMines);
        int freeTiles =  numberOfTiles - minesToCreate;
        int[][] availableMinePositions = generateAllTilePositions();
        availableMinePositions = removePositionsAroundInitPos(availableMinePositions, pos, freeTiles - 1);
        availableMinePositions = removePosFromPosArray(availableMinePositions, pos);
        return generateMinePositions(availableMinePositions, minesToCreate);
    }

    private void makeMines(int[][] minePositions) {
        for (int [] pos : minePositions) {
            getTile(pos[0], pos[1]).makeMine();
        }
    }

    private void setMinesAroundTiles() {
        for (int[] pos : allTilePositions) {
            setMineAroundTile(pos);
        }
    }

    private int[][] removePositionsAroundInitPos(int[][] posArray, int[] pos, int freeTiles) {
        if (freeTiles < 8) {
            return removeRandomPositionsAroundTile(posArray, getPositionsAroundTile(pos), freeTiles);
        } else {
            return removeAllPositionsAroundInitPos(posArray, pos);
        }
    }

    private int[][] removeRandomPositionsAroundTile(int[][] posArray, int[][] positionsAroundInitPos, int freeTiles) {
        int minesToRemove = getMinesToRemove(positionsAroundInitPos.length, freeTiles);
        for (int i = 0; i < minesToRemove; i++) {
            int[] pos = getRandomPos(positionsAroundInitPos);
            posArray = removePosFromPosArray(posArray, pos);
            positionsAroundInitPos = removePosFromPosArray(positionsAroundInitPos, pos);
        }
        return posArray;
    }

    private int[][] removeAllPositionsAroundInitPos(int[][] posArray, int[] middlePos) {
        for (int[] pos : getPositionsAroundTile(middlePos)) {
            posArray = removePosFromPosArray(posArray, pos[0], pos[1]);
        }
        return posArray;
    }

    private int[][] generateMinePositions(int[][] availableMinePositions, int minesToCreate) {
        int[][] minePositions = new int[minesToCreate][2];
        for (int createdMines = 0; createdMines < minesToCreate; createdMines++) {
            int[] pos = getRandomPos(availableMinePositions);
            availableMinePositions = removePosFromPosArray(availableMinePositions, pos);
            minePositions[createdMines] = pos;
        }
        return minePositions;
    }

    private void setMineAroundTile(int[] middlePos) {
        int nMines = 0;
        for (int[] pos : getPositionsAroundTile(middlePos)) {
            if (getTile(pos[0], pos[1]).isMine()) nMines++;
        }
        getTile(middlePos[0], middlePos[1]).setMinesAround(nMines);
    }

    private int[][] getPositionsAroundTile(int xPos, int yPos) {
        int[][] positions = new int[][] {};
        for (int x = xPos - 1; x < xPos + 2; x++) {
            for (int y = yPos - 1; y < yPos + 2; y++) {
                if (!isPositionsOnField(x, y)) continue;
                if (isPositionInMiddle(xPos, yPos, x, y)) continue;
                positions = addPosToPosArray(positions, new int[] {x, y});
            }
        }
        return positions;
    }

    private int[][] getPositionsAroundTile(int[] pos) {
        return getPositionsAroundTile(pos[0], pos[1]);
    }

    private int[][] removePosFromPosArray(int[][] posArray, int[] posToRemove) {
        int i = getIndexOfVal(posArray, posToRemove);
        posArray[i] = posArray[posArray.length - 1];
        return Arrays.copyOf(posArray, posArray.length - 1);
    }

    private int[][] removePosFromPosArray(int[][] posArray, int x, int y) {
        return removePosFromPosArray(posArray, new int[] {x, y});
    }

    private int[][] addPosToPosArray(int[][] posArray, int[] posToAdd) {
        posArray = Arrays.copyOf(posArray, posArray.length + 1);
        posArray[posArray.length - 1] = posToAdd;
        return posArray;
    }

    private int getIndexOfVal(int[][] array, int[] val) {
        int indexOfVal = -1;
        for (int i = 0; i < array.length; i++) {
            if (positionsEqual(array[i], val)) {
                indexOfVal = i;
                break;
            }
        }
        return indexOfVal;
    }

    private Tile getTile(int[] pos) {
        return getTile(pos[0], pos[1]);
    }

    private Tile getTile(int x, int y) {
        return tileArray[x][y];
    }

    private int getMinesToRemove(int positionsAround, int freeTiles) {
        return Math.min(positionsAround, freeTiles);
    }

    private int[] getRandomPos(int[][] posArray) {
        int i = getRandomIndex(posArray.length - 1);
        return posArray[i];
    }

    private int getRandomIndex(int maxVal) {
        return (int) Math.round((Math.random() * (maxVal + 1)) - 0.5F);
    }

    private boolean isPositionsOnField(int x, int y) {
        return (y >= 0) && (x >= 0) && (y <= tilesInYDirection - 1) && (x <= tilesInXDirection - 1);
    }

    private boolean isPositionInMiddle(int x1, int y1, int x2, int y2) {
        return x1 == x2 && y1 == y2;
    }

    private boolean positionsEqual(int[] pos1, int[] pos2) {
        return (pos1[0] == pos2[0]) && (pos1[1] == pos2[1]);
    }

    /*
    interaction methods
     */
    public void alterTileFlagged(int xPos, int yPos) {
        Tile tile = getTile(xPos, yPos);
        if (tile.isUncovered()) return;
        tile.alterFlagged();
    }

    public void uncoverTiles(int xPos, int yPos) {
        Tile tile = getTile(xPos, yPos);
        uncoverTile(xPos, yPos);
        if (tile.isDetonated() || tile.isFlagged()) return;
        for (int[] pos : getPositionsAroundTile(xPos, yPos)) {
            Tile t = getTile(pos);
            if (t.isUncovered() || t.isFlagged()) continue;
            uncoverTiles(pos);
        }
    }

    public void uncoverTiles(int[] pos) {
        uncoverTiles(pos[0], pos[1]);
    }

    private void uncoverTile(int x, int y) {
        Tile tile = getTile(x, y);
        if (tile.isFlagged()) return;
        if (tile.isMine()) tile.detonate();
        tile.uncover();
    }

    /*
    getter
     */
    public Tile[][] getTilesArray() {
        return tileArray;
    }

    public int getXSize() {
        return tileArray.length;
    }

    public int getYSize() {
        return tileArray[0].length;
    }
}

