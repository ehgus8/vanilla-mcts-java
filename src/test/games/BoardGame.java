package test.games;

import java.awt.*;
import java.util.List;

public abstract class BoardGame {
    public int[][] board;
    public int rows, cols;

    public BoardGame(int rows, int cols) {
        this.board = new int[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

    public abstract void displayBoard();

    public abstract List<Point> getValidActions();

    public abstract int applyAction(int currentPlayer, Point action);

    public abstract void undoAction(Point action);

    public abstract int checkWinner(int player, Point action);

    public abstract Point getActionByStrategies(List<Point> validActions, int currentPlayer);

    public abstract Point getUserAction();

}
