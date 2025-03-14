package test.tester;

import test.ai.mcts.*;
import test.games.BoardGame;
import test.games.Gomoku;

import java.awt.*;

public class AmongSimulatorTester {
    private static int competeBetweenSimulators(BoardGame game, int mctsIterations, MCTSSimulator[] simulators) {
        int currentPlayer = 1;
        int moveCount = 0;
        int winner = 0;

        while(true) {
            Node root = new Node(null, null, currentPlayer, moveCount);
            if(currentPlayer == 1) {
                MCTS.mcts(game, root, mctsIterations, simulators[0]);
            } else {
                MCTS.mcts(game, root, mctsIterations, simulators[1]);
            }
            Point action = root.getNextAction("maxVisit");
            currentPlayer = game.applyAction(currentPlayer, action);
            moveCount++;
            winner = game.checkWinner(currentPlayer * -1, action);

            if(winner != 0) {
                System.out.println("player " + currentPlayer * -1 + " win!");
                return currentPlayer * -1;
            }
            else if(moveCount == game.rows * game.cols) {
                System.out.println("Draw!");
                return 0;
            }
        }
    }



    public static int[][] testBetweenSimulators(BoardGame game, int gameIterations, int mctsIterations, MCTSSimulator[] simulators) {

        // results: [vanilla, fourStrategy], draw
        // row 1: when vanilla first player, row 2: when FourStrategy first player.
        int[][] results = new int[][] {
                {0, 0, 0},
                {0, 0, 0}
        };
        int result = 0;
        for(int i = 0; i < gameIterations/2; i++) {
            long startTime = System.currentTimeMillis();
            result = competeBetweenSimulators(new Gomoku(), 2000, simulators);
            long endTime = System.currentTimeMillis();
            System.out.printf("i: %d, execute time of mcts: %f sec \n", i+1, (endTime - startTime) / 1000.0);
            if(result == 1) {
                results[0][0]++;
            } else if(result == -1) {
                results[0][1]++;
            } else {
                results[0][2]++;
            }
        }
        System.out.printf("중간 결과: [%d, %d, %d]\n", results[0][0], results[0][1], results[0][2]);
        simulators[0] = new FourStrategySimulator();
        simulators[1] = new VanillaStrategySimulator();
        for(int i = 0; i < gameIterations/2; i++) {
            long startTime = System.currentTimeMillis();
            result = competeBetweenSimulators(new Gomoku(), 2000, simulators);
            long endTime = System.currentTimeMillis();
            System.out.printf("i: %d, execute time of mcts: %f sec \n", i+1, (endTime - startTime) / 1000.0);
            if(result == 1) {
                results[1][1]++;
            } else if(result == -1) {
                results[1][0]++;
            } else {
                results[1][2]++;
            }
        }
        return results;
    }
}
