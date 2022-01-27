import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class MinefieldTest {
    // interface?
    private static class MinefieldTestInterface {
        public final int xSize;
        public final int ySize;
        public final float percentageOfMines;
        public final Minefield field;
        
        MinefieldTestInterface(int xSize, int ySize, float percentageOfMines) {
            this.xSize = xSize;
            this.ySize = ySize;
            this.percentageOfMines = percentageOfMines;
            this.field = new Minefield(xSize, ySize, percentageOfMines);
        }

        MinefieldTestInterface(int size, float percentageOfMines) {
            this.xSize = size;
            this.ySize = size;
            this.percentageOfMines = percentageOfMines;
            this.field = new Minefield(xSize, ySize, percentageOfMines);
        }

        /*
        interface to minefield
         */
        public Tile[][] getTilesArray() {
            return field.getTilesArray();
        }

        public int getXSize() {
            return field.getXSize();
        }

        public int getYSize() {
            return field.getYSize();
        }

        public void alterTileFlagged(int xPos, int yPos) {
            field.alterTileFlagged(xPos, yPos);
        }

        public void uncoverTiles(int xPos, int yPos) {
            field.uncoverTiles(xPos, yPos);
        }

        public void init(int xPos, int yPos) {
            field.init(xPos, yPos);
        }

        /*
        for debugging
         */
        public String minesAround() {
            StringBuilder positions = new StringBuilder();
            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    positions.append(tileAtPos(x, y).getMinesAround()).append(" ");
                }
                positions.append("\n");
            }
            return positions.toString();
        }

        public String uncovered() {
            StringBuilder positions = new StringBuilder();
            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    if (tileAtPos(x, y).isUncovered()) {
                        positions.append("1 ");
                    } else {
                        positions.append("0 ");
                    }
                }
                positions.append("\n");
            }
            return positions.toString();
        }

        public String flagged() {
            StringBuilder positions = new StringBuilder();
            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    if (tileAtPos(x, y).isFlagged()) {
                        positions.append("1 ");
                    } else {
                        positions.append("0 ");
                    }
                }
                positions.append("\n");
            }
            return positions.toString();
        }

        public String mines() {
            StringBuilder positions = new StringBuilder();
            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    if (tileAtPos(x, y).isMine()) {
                        positions.append("1 ");
                    } else {
                        positions.append("0 ");
                    }
                }
                positions.append("\n");
            }
            return positions.toString();
        }

        /*
        support methods
         */
        public Tile tileAtPos(int[] pos) {
            return tileAtPos(pos[0], pos[1]);
        }

        public Tile tileAtPos(int x, int y) {
            return getTilesArray()[x][y];
        }

        private int[][] getAllPositions() {
            int[][] positions = new int[xSize * ySize][2];
            int index = 0;
            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    positions[index] = new int[] {x, y};
                    index++;
                }
            }
            return positions;
        }

        private boolean noTilesUncovered() {
            for (int[] pos : getAllPositions()) {
                if (tileAtPos(pos).isUncovered()) return false;
            }
            return true;
        }

        private boolean noTilesFlagged() {
            for (int[] pos : getAllPositions()) {
                if (tileAtPos(pos).isFlagged()) return false;
            }
            return true;
        }

        private boolean noMineAroundTile() {
            for (int[] pos : getAllPositions()) {
                if (tileAtPos(pos).getMinesAround() != 0) return false;
            }
            return true;
        }

        private boolean noMines() {
            for (int[] pos : getAllPositions()) {
                if (tileAtPos(pos).isMine()) return false;
            }
            return true;
        }

        private boolean noDetonatedMine() {
            for (int[] pos : getAllPositions()) {
                if (tileAtPos(pos).isDetonated()) return false;
            }
            return true;
        }

        private boolean isTileFlagged() {
            return tileAtPos(0, 0).isFlagged();
        }

        private boolean isTileUncovered(int x, int y) {
            return tileAtPos(x, y).isUncovered();
        }

        private int getExpectedNumMines() {
            return (int) (xSize * ySize * percentageOfMines);
        }

        private int getActualNumMines() {
            int numMines = 0;
            for (int[] pos : getAllPositions()) {
                if (tileAtPos(pos).isMine()) numMines++;
            }
            return numMines;
        }

        private int[][] getPositionsAround(int xPos, int yPos) {
            int[][] positions = new int[][]{};
            for (int x = xPos - 1; x < xPos + 2; x++) {
                for (int y = yPos - 1; y < yPos + 2; y++) {
                    if (isMiddlePos(x, xPos, y, yPos)) continue;
                    if (posNotOnMinefield(x , y)) continue;
                    positions = addPosToPosArray(positions, x, y);
                }
            }
            return positions;
        }

        private boolean isMiddlePos(int x1, int x2, int y1, int y2) {
            return x1 == x2 && y1 == y2;
        }

        private int[][] addPosToPosArray(int[][] posArray, int x, int y) {
            posArray = Arrays.copyOf(posArray, posArray.length + 1);
            posArray[posArray.length - 1] = new int[]{x, y};
            return posArray;
        }

        private boolean posNotOnMinefield(int x, int y) {
            return (x < 0) || (y < 0) || (x >= xSize) || (y >= ySize);
        }

        private int getMinesAround(int x, int y) {
            int minesAround = 0;
            for (int[] pos : getPositionsAround(x, y)) {
                if (tileAtPos(pos[0], pos[1]).isMine()) minesAround++;
            }
            return minesAround;
        }

        private boolean allTilesAroundUncovered() {
            for (int[] pos : getPositionsAround(1, 1)) {
                if (!tileAtPos(pos).isUncovered()) return false;
            }
            return true;
        }

        private boolean noTilesAroundUncovered() {
            for (int[] pos : getPositionsAround(1, 1)) {
                if (tileAtPos(pos).isUncovered()) return false;
            }
            return true;
        }

        private boolean noMinesAroundAndAtPos(int x, int y) {
            for (int[] pos : getPositionsAround(x, y)) {
                if (tileAtPos(pos).isMine()) return false;
            }
            return !tileAtPos(x, y).isMine();
        }

        private boolean minePositionAreEqual(MinefieldTestInterface otherField) {
            for (int[] pos : getAllPositions()) {
                if (tileAtPos(pos) != otherField.tileAtPos(pos)) return false;
            }
            return true;
        }
    }

    private final MinefieldTestInterface minefield1 = new MinefieldTestInterface(10, 0.5F);
    private final MinefieldTestInterface minefield2 = new MinefieldTestInterface(8, 0.25F);
    private final MinefieldTestInterface minefield091 = new MinefieldTestInterface(10, 0.91F);
    private final MinefieldTestInterface minefield092 = new MinefieldTestInterface(10, 0.92F);
    private final MinefieldTestInterface minefield095 = new MinefieldTestInterface(10, 0.95F);
    private final MinefieldTestInterface minefield097 = new MinefieldTestInterface(10, 0.97F);
    private final MinefieldTestInterface minefield099 = new MinefieldTestInterface(10, 0.99F);

    @Test
    public void canGetMinefieldXSize() {
        assertEquals(minefield1.xSize, minefield1.getXSize());
    }

    @Test
    public void canGetAnyMinefieldXSize() {
        assertEquals(minefield2.xSize, minefield2.getXSize());
    }

    @Test
    public void canGetMinefieldYSize() {
        assertEquals(minefield1.ySize, minefield1.getYSize());
    }

    @Test
    public void canGetAnyMinefieldYSize() {
        assertEquals(minefield2.ySize, minefield2.getYSize());
    }

    @Test
    public void noTileOnNewMinefieldIsUncovered() {
        assertTrue(minefield1.noTilesUncovered());
    }

    @Test
    public void noTilesOnNewMinefieldIsFlagged() {
        assertTrue(minefield1.noTilesFlagged());
    }

    @Test
    public void noMinesAroundAnyTileOnNewMinefield() {
        assertTrue(minefield1.noMineAroundTile());
    }

    @Test
    public void noMinesOnNewMinefield() {
        assertTrue(minefield1.noMines());
    }

    @Test
    public void noPositionOfDetonatedMineOnNewMinefield() {
        assertTrue(minefield1.noDetonatedMine());
    }

    @Test
    public void sizeOfTilesArrayIsGivenValue() {
        Tile[][] tileArray = minefield1.getTilesArray();
        assertEquals(minefield1.xSize, tileArray.length);
        assertEquals(minefield1.ySize, tileArray[0].length);
    }

    @Test
    public void sizeOfTilesArrayIsAnyGivenValue() {
        Tile[][] tileArray = minefield2.getTilesArray();
        assertEquals(minefield2.xSize, tileArray.length);
        assertEquals(minefield2.ySize, tileArray[0].length);
    }

    @Test
    public void xSizeOfMinefieldCanBeBiggerThanYSize() {
        int xSize = 3;
        int ySize = 6;
        MinefieldTestInterface field = new MinefieldTestInterface(xSize, ySize, 0.5F);
        Tile[][] tileArray = field.getTilesArray();
        assertEquals(xSize, tileArray.length);
        assertEquals(ySize, tileArray[0].length);
    }

    @Test
    public void ySizeOfMinefieldCanBeBiggerThanXSize() {
        int xSize = 6;
        int ySize = 3;
        MinefieldTestInterface field = new MinefieldTestInterface(xSize, ySize, 0.5F);
        Tile[][] tileArray = field.getTilesArray();
        assertEquals(xSize, tileArray.length);
        assertEquals(ySize, tileArray[0].length);
    }

    @Test
    public void tileXFlaggedAfterAlteringFlaggedStatusOfTileX() {
        minefield1.alterTileFlagged(0, 0);
        assertTrue(minefield1.isTileFlagged());
    }

    @Test
    public void tileXIsNotFlaggedAfterAlteringFlaggedStatusOfTileY() {
        minefield1.alterTileFlagged(1, 1);
        assertFalse(minefield1.isTileFlagged());
    }

    @Test
    public void tileNotFlaggedAfterAlteringFlaggedStatusTwice() {
        minefield1.alterTileFlagged(0, 0);
        minefield1.alterTileFlagged(0, 0);
        assertFalse(minefield1.isTileFlagged());
    }

    @Test
    public void tileXIsUncoveredAfterUncoveringTileX() {
        minefield1.uncoverTiles(0, 0);
        assertTrue(minefield1.isTileUncovered(0, 0));
    }

    @Test
    public void tileXIsNotUncoveredAfterUncoveringTileY() {
        minefield099.init(1, 1);
        minefield099.uncoverTiles(1, 1);
        assertFalse(minefield099.isTileUncovered(minefield099.xSize - 1, minefield099.xSize - 1));
    }

    @Test
    public void cannotUncoverAFlaggedTile() {
        minefield1.alterTileFlagged(0, 0);
        minefield1.uncoverTiles(0, 0);
        assertFalse(minefield1.isTileUncovered(0, 0));
    }

    @Test
    public void cannotFlagAUncoveredTile() {
        minefield1.uncoverTiles(0, 0);
        minefield1.alterTileFlagged(0, 0);
        assertFalse(minefield1.isTileFlagged());
    }

    @Test
    public void minesOnMinefieldAfterInitialization() {
        minefield1.init(1, 1);
        assertFalse(minefield1.noMines());
    }

    @Test
    public void givenPercentageOfTilesAreMines() {
        minefield1.init(1, 1);
        int expectedNumMines = minefield1.getExpectedNumMines();
        int actualNumMines = minefield1.getActualNumMines();
        assertEquals(expectedNumMines, actualNumMines);
    }

    @Test
    public void anyGivenPercentageOfTilesAreMines() {
        minefield2.init(1, 1);
        int expectedNumMines = minefield2.getExpectedNumMines();
        int actualNumMines = minefield2.getActualNumMines();
        assertEquals(expectedNumMines, actualNumMines);
    }

    /**
     * <p>By an extremely low change, this test can fail without the code being wrong.</p>
     */
    @Test
    public void minePositionsAreGeneratedRandomly() {
        int testSize = 10;
        MinefieldTestInterface[] testFields = newMinefieldArray(testSize);
        initAllMinefieldsInMinefieldArray(testSize, testFields);
        assertTrue(minePositionsOnEveryMinefieldDiffer(testFields));
    }

    /**
     * <p><b>NOTE</b>: This test <u>cannot</u> fail.</p>
     * <p>However, it can take very long for execution, if a slow algorithms is used to position generation.</p>
     * <p>This test is supposed to indicate the possible slowness of the algorithm.</p>
     */
    @Test
    public void generatesLargerMinefieldWithHighMinePercentageEasily() {
        minefield099.init(1, 1);
    }

    @Test
    public void tileFromWhereTheMinefieldIsInitializedAndTilesAroundItAreNotMines() {
        minefield091.init(1, 1);
        assertTrue(minefield091.noMinesAroundAndAtPos(1, 1));
    }

    @Test
    public void anyTileFromWhereTheMinefieldIsInitializedAndTilesAroundItAreNotMines() {
        minefield091.init(minefield091.xSize - 2, minefield091.ySize - 2);
        assertTrue(minefield091.noMinesAroundAndAtPos(minefield091.xSize - 2,minefield091.ySize - 2));
    }

    @Test
    public void minefieldCanBeInitializedFromTileOnTheUpperFieldsBorder() {
        minefield091.init(1, 0);
        assertTrue(minefield091.noMinesAroundAndAtPos(1, 0));
    }

    @Test
    public void minefieldCanBeInitializedFromTileOnTheLeftFieldsBorder() {
        minefield091.init(0, 1);
        assertTrue(minefield091.noMinesAroundAndAtPos(0, 1));
    }

    @Test
    public void minefieldCanBeInitializedFromTileOnTheBottomFieldsBorder() {
        minefield091.init(1, minefield091.ySize - 1);
        assertTrue(minefield091.noMinesAroundAndAtPos(1, minefield091.ySize - 1));
    }

    @Test
    public void minefieldCanBeInitializedFromTileOnTheRightFieldsBorder() {
        minefield091.init(minefield091.xSize - 1, 1);
        assertTrue(minefield091.noMinesAroundAndAtPos(minefield091.xSize - 1, 1));
    }

    @Test
    public void minefieldCanBeInitializedOnFlaggedTiles() {
        minefield091.alterTileFlagged(1, 1);
        minefield091.init(1, 1);
        assertTrue(minefield091.noMinesAroundAndAtPos(1, 1));
    }

    @Test
    public void tileIsStillFlaggedAfterMinefieldHasBeenInitializedFromIt() {
        minefield091.alterTileFlagged(1, 1);
        minefield091.init(1, 1);
        assertFalse(minefield091.isTileUncovered(1, 1));
    }

    @Test
    public void canInitializeMinefieldWithOnlyEightNonMineTiles() {
        minefield092.init(1, 1);
        int minesAround = minefield092.getMinesAround(1, 1);
        assertEquals(1, minesAround);
    }

    @Test
    public void canInitializeMinefieldWithOnlyOneNonMineTiles() {
        minefield099.init(1, 1);
        int minesAround = minefield099.getMinesAround(1, 1);
        assertEquals(8, minesAround);
    }

    @Test
    public void canInitializeMinefieldWithOnlyFiveNonMineTilesOnLeftBorderOfTheField() {
        minefield095.init(0, 1);
        int minesAround = minefield095.getMinesAround(0, 1);
        assertEquals(1, minesAround);
    }

    @Test
    public void canInitializeMinefieldWithOnlyThreeNonMineTilesInRightBottomCorner() {
        minefield097.init(minefield097.xSize - 1, minefield097.ySize - 1);
        int minesAround = minefield097.getMinesAround(minefield097.xSize - 1, minefield097.ySize - 1);
        assertEquals(1, minesAround);
    }

    @Test
    public void zeroMinesAroundTileIfZeroMinesAround() {
        minefield091.init(1, 1);
        int minesAround = minefield091.tileAtPos(1, 1).getMinesAround();
        assertEquals(0, minesAround);
    }

    @Test
    public void oneMineAroundTileIfOneMineAroundTile() {
        minefield092.init(1, 1);
        int minesAround = minefield092.tileAtPos(1, 1).getMinesAround();
        assertEquals(1, minesAround);
    }

    @Test
    public void fiveMinesAroundTilesIfFiveMinesAroundTile() {
        minefield095.init(1, 1);
        int minesAround = minefield095.tileAtPos(1, 1).getMinesAround();
        assertEquals(4, minesAround);
    }

    @Test
    public void fiveMinesAroundAnyTilesIfFiveMinesAroundTile() {
        minefield095.init(2, 2);
        int minesAround = minefield095.tileAtPos(2, 2).getMinesAround();
        assertEquals(4, minesAround);
    }

    @Test
    public void canSetMinesAroundInTheCorner() {
        minefield097.init(0, 0);
        int minesAround = minefield097.tileAtPos(0, 0).getMinesAround();
        assertEquals(1, minesAround);
    }

    @Test
    public void mineAroundIsSetForAllTiles() {
        MinefieldTestInterface f = new MinefieldTestInterface(3, 5.0F/9.0F);
        f.init(0, 0);
        int[][] compareValues = new int[][] {
            new int[] {0, 2, 1},
            new int[] {2, 5, 3},
            new int[] {1, 3, 2}
        };
        Tile[][] tileArray = f.getTilesArray();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                assertEquals(compareValues[x][y], tileArray[x][y].getMinesAround());
            }
        }
    }

    @Test
    public void mineDetonatesWhenMineIsUncovered() {
        minefield099.init(0, 0);
        minefield099.uncoverTiles(1, 1);
        assertTrue(minefield099.tileAtPos(1, 1).isDetonated());
    }

    @Test
    public void tileXIsNotDetonatedIfYIsDetonated() {
        minefield099.init(0, 0);
        minefield099.uncoverTiles(1, 1);
        assertFalse(minefield099.tileAtPos(0, 1).isDetonated());
    }

    @Test
    public void ifNoMinesAroundTileUncoversAllTilesAround() {
        minefield091.init(1, 1);
        minefield091.uncoverTiles(1, 1);
        assertTrue(minefield091.allTilesAroundUncovered());
    }

    @Test
    public void uncoversTileAroundRecursively() {
        MinefieldTestInterface f = new MinefieldTestInterface(10, 0F);
        f.init(1, 1);
        f.uncoverTiles(1, 1);
        for (int[] pos : f.getAllPositions()) {
            assertTrue(f.tileAtPos(pos).isUncovered());
        }
    }

    @Test
    public void uncoversNoFlaggedTilesAround() {
        minefield091.init(1, 1);
        minefield091.alterTileFlagged(0, 0);
        minefield091.uncoverTiles(1, 1);
        assertFalse(minefield091.tileAtPos(0, 0).isUncovered());
    }

    @Test
    public void tilesAroundFlaggedTileWillNoBeUncovered() {
        minefield091.alterTileFlagged(1, 1);
        minefield091.init(1, 1);
        minefield091.uncoverTiles(1, 1);
        assertTrue(minefield091.noTilesAroundUncovered());
    }

    private MinefieldTestInterface[] newMinefieldArray(int nFields) {
        MinefieldTestInterface[] fields = new MinefieldTestInterface[nFields];
        for (int i = 0; i < nFields; i++) {
            fields[i] = new MinefieldTestInterface(10, 0.5F);
        }
        return fields;
    }

    private void initAllMinefieldsInMinefieldArray(int testSize, MinefieldTestInterface[] testField) {
        for (int i = 0; i < testSize; i++) {
            testField[i].init(1, 1);
        }
    }

    private boolean minePositionsOnEveryMinefieldDiffer(MinefieldTestInterface[] fields) {
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields.length; j++) {
                if (i == j) continue;
                if (fields[i].minePositionAreEqual(fields[j])) return false;
            }
        }
        return true;
    }
}