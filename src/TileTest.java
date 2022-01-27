//import static org.junit.jupiter.api.Assertions.*;
//import org.junit.jupiter.api.Test;
//
//class TileTest {
//    Tile tile = new Tile();
//
//    @Test
//    void canMakeTileAMine() {
//        tile.makeMine();
//        assertTrue(tile.isMine());
//    }
//
//    @Test
//    void canSetMinesAroundValueToAnInt() {
//        tile.setMinesAround(5);
//        assertEquals(5, tile.getMinesAround());
//    }
//
//    @Test
//    void canSetMinesAroundValueToAnyInt() {
//        tile.setMinesAround(15);
//        assertEquals(15, tile.getMinesAround());
//    }
//
//    @Test
//    void canGetAnyAmountOfMinesAround() {
//        tile.setMinesAround(25);
//        assertEquals(25, tile.getMinesAround());
//    }
//
//    @Test
//    void canFlagMine() {
//        tile.makeMine();
//        tile.alterFlagged();
//        assertTrue(tile.isFlagged());
//    }
//
//    @Test
//    void canUnflagFlaggedMine() {
//        tile.makeMine();
//        tile.alterFlagged();
//        tile.alterFlagged();
//        assertFalse(tile.isFlagged());
//    }
//}