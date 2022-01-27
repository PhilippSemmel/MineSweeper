public class Tile {
    private boolean uncovered = false;
    private boolean flagged = false;
    private boolean mine = false;
    private int minesAround = 0;
    private boolean detonated = false;

    public boolean isUncovered() {
        return uncovered;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public int getMinesAround() {
        return minesAround;
    }

    public boolean isMine() {
        return mine;
    }

    public boolean isDetonated() {
        return detonated;
    }

    public void uncover() {
        uncovered = true;
    }

    public void alterFlagged() {
        flagged = !flagged;
    }

    public void makeMine() {
        mine = true;
    }

    public void setMinesAround(int nMines) {
        minesAround = nMines;
    }

    public void detonate() {
        detonated = true;
    }
}
