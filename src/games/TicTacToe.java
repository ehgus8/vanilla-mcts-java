package games;

import java.awt.*;
import java.util.*;
import java.util.List;

public class TicTacToe extends BoardGame {
    public TicTacToe() {
        super(3, 3);
    }

    @Override
    public void displayBoard() {
        System.out.println(board.toString());
    }

    @Override
    public List<Point> getValidActions() {
        return List.of();
    }

    @Override
    public int applyAction(int currentPlayer, Point action) {
        int y = action.y;
        int x = action.x;
        if(board[y][x] == 0) {
            board[y][x] = currentPlayer;
            return 1 - currentPlayer;
        }
        System.out.println("Invalid action. Try again.");
        return currentPlayer;
    }

    @Override
    public void undoAction(Point action) {
        board[action.y][action.x] = 0;
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
    private boolean dfs(int player, Point start,int dr, int dc) {
        Deque<Point> stack = new ArrayDeque<>();
        Set<Point> visited = new HashSet<>();

        stack.push(start);
        int seqCount = 1;

        while(!stack.isEmpty()) {
            Point current = stack.pop();
            visited.add(current);

            int nr = current.y + dr;
            int nc = current.x + dc;
            Point nextPoint = new Point(nc, nr);
            if (isInBounds(nr, nc) &&
                    !visited.contains(nextPoint) &&
                    board[nr][nc] == player) {
                stack.push(nextPoint);
                seqCount++;
            }

            nr = current.y - dr;
            nc = current.x - dc;
            nextPoint = new Point(nc, nr);
            if (isInBounds(nr, nc) &&
                    !visited.contains(nextPoint) &&
                    board[nr][nc] == player) {
                stack.push(nextPoint);
                seqCount++;
            }

            if (seqCount >= 3) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int checkWinner(int player, Point action) {
        int[][] directions = { {0, 1}, {1, 0}, {1, 1}, {1, -1} };

        for (int[] dir : directions) {
            int dr = dir[0];
            int dc = dir[1];
            if (dfs(player, action, dr, dc)) {
                return player;
            }
        }
        return -1; // no winner
    }

    @Override
    public void playAgainstMCTS(int mctsIterations) {

    }
}
