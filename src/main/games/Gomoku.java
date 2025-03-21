package main.games;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Gomoku extends BoardGame {
    int[][] directions = { {0, 1}, {1, 0}, {1, 1}, {1, -1} };
    public Gomoku() {
        super(15, 15);
    }

    @Override
    public void displayBoard() {
        System.out.print("    ");
        for(int j = 0; j < this.cols; j++) {
            System.out.printf("'%d' ",j % 10);
        }
        System.out.println();
        for(int i = 0; i < this.rows; i++) {
            System.out.printf("'%d' ",i % 10);
            for(int j = 0; j < this.cols; j++) {
                if(this.board[i][j] == 0) {
                    System.out.print("' ' ");
                } else if (this.board[i][j] == 1) {
                    System.out.print("'O' ");
                } else {
                    System.out.print("'X' ");
                }
            }
            System.out.println();
        }
    }

    @Override
    public List<Point> getValidActions() {
        List<Point> validActions = new ArrayList<>();
        for(int i = 0; i < this.rows; i++) {
            for(int j = 0; j < this.cols; j++) {
                if(board[i][j] != 0) {
                    for(int[] dir: directions) {
                        int nr = i + dir[0];
                        int nc = j + dir[1];
                        if(isInBounds(nr, nc) && board[nr][nc] == 0)
                            validActions.add(new Point(nc, nr));
                        nr = i - dir[0];
                        nc = j - dir[1];
                        if(isInBounds(nr, nc) && board[nr][nc] == 0)
                            validActions.add(new Point(nc, nr));
                    }
                }
            }
        }
        if(validActions.isEmpty()) {
            validActions.add(new Point(this.cols/2, this.rows/2));
        }
        return validActions;
    }

    @Override
    public int applyAction(int currentPlayer, Point action) {
        int y = action.y;
        int x = action.x;
        if(board[y][x] == 0) {
            board[y][x] = currentPlayer;
            return currentPlayer * -1;
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

    private boolean dfsForCheckWinner(int player, Point start, int dr, int dc) {
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

            if (seqCount >= 5) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int checkWinner(int player, Point action) {
        for (int[] dir : directions) {
            int dr = dir[0];
            int dc = dir[1];
            if (dfsForCheckWinner(player, action, dr, dc)) {
                return player;
            }
        }
        return 0; // no winner
    }


    private boolean dfsForConsecutiveStrategy(int player, Point start,
                                            int dr, int dc, int inarow, int maxMoveCount) {
        Deque<Point> stack = new ArrayDeque<>();
        Set<Point> visited = new HashSet<>();

        stack.push(start);
        int seqCount = 1;
        int moveCount = 0;

        while(!stack.isEmpty() && moveCount < maxMoveCount) {
            Point current = stack.pop();
            visited.add(current);
            moveCount++;

            int nr = current.y + dr;
            int nc = current.x + dc;
            Point nextPoint = new Point(nc, nr);
            if (isInBounds(nr, nc) &&
                    !visited.contains(nextPoint) &&
                    board[nr][nc] == player) {
                stack.push(nextPoint);
                seqCount++;
            }

        }
        if (seqCount == inarow + 1) {
            return true;
        }
        return false;
    }

    private boolean dfsForOpenThreeStrategy(int player, Point start,
                                              int dr, int dc, int inarow, int maxMoveCount) {
        Deque<Point> stack = new ArrayDeque<>();
        Set<Point> visited = new HashSet<>();

        stack.push(start);
        int seqCount = 1;
        int moveCount = 0;
        int[] template = new int[] {0, player, player, player, 0};
        while(!stack.isEmpty() && moveCount < maxMoveCount) {
            Point current = stack.pop();
            visited.add(current);
            if(template[moveCount] != board[current.y][current.x]) {
                break;
            }
            moveCount++;

            int nr = current.y + dr;
            int nc = current.x + dc;
            Point nextPoint = new Point(nc, nr);
            if (isInBounds(nr, nc) &&
                    !visited.contains(nextPoint) &&
                    board[nr][nc] == player) {
                stack.push(nextPoint);
            }

        }
        if (seqCount == inarow + 1 && moveCount == maxMoveCount) {
            return true;
        }
        return false;
    }
    /*
     * inarow = 3
     * maxMoveCount = 5
     */
    public Boolean checkOpenThreeStrategy(Point action, int player) {
        for (int[] dir: directions) {
            if(dfsForOpenThreeStrategy(player, action, dir[0], dir[1], 3, 5)) {
                return true;
            }
            if(dfsForOpenThreeStrategy(player, action, -dir[0], -dir[1], 3, 5)) {
                return true;
            }
        }
        return false;
    }

    /*
     * inarow = 4
     * maxMoveCount = 5
     */
    public Boolean checkFourStrategy(Point action, int player) {
        for (int[] dir: directions) {
            if(dfsForConsecutiveStrategy(player, action, dir[0], dir[1], 4, 5)) {
                return true;
            }
            if(dfsForConsecutiveStrategy(player, action, -dir[0], -dir[1], 4, 5)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Point getActionByStrategies(List<Point> validActions, int currentPlayer) {
        for (Point action: validActions) {
            // enemy's four, do not need to check player's four because it is processed in mcts in advance.
            if (checkFourStrategy(action, -currentPlayer)) {
                return action;
            }
            // player's open three
            if (checkOpenThreeStrategy(action, currentPlayer)) {
                return action;
            }
            // enemy's open three
            if (checkOpenThreeStrategy(action, -currentPlayer)) {
                return action;
            }
        }
        return null;
    }

    @Override
    public Point getUserAction() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Input (row and col): ");
        String row = sc.next();
        String col = sc.next();

        return new Point(Integer.parseInt(col), Integer.parseInt(row));
    }
}
