package test.tester;

import test.ai.mcts.MCTS;
import test.ai.mcts.MCTSSimulator;
import test.ai.mcts.Node;
import test.ai.mcts.VanillaStrategySimulator;
import test.games.BoardGame;
import test.games.Gomoku;

import java.awt.*;

public class AmongMCTSTester {
    public static int testAmongMCTS(BoardGame game, int mctsIterations, MCTSSimulator simulator, int treeSavePlayer) {
        int currentPlayer = 1;
        int moveCount = 0;
        int winner = 0;
        Node savedRoot = null;
//        Point prevAction = null;
        while(true) {
            Node root;
            if(currentPlayer == treeSavePlayer && savedRoot != null) {
                root = savedRoot;
                root.parent = null;
                root.prevAction = null;
            } else {
                root = new Node(null, null, currentPlayer, moveCount);
            }

            MCTS.mcts(game, root, mctsIterations, simulator);
//            System.out.println(currentPlayer);
            Node nextNode = root.getNextNode("maxVisit");
            Point action = nextNode.prevAction;

            // for treeSavePlayer
            if(currentPlayer == treeSavePlayer) {
                savedRoot = nextNode;
            }
            else if(savedRoot != null) {
                for(Node child: savedRoot.children){
                    if((child.prevAction.y == nextNode.prevAction.y)
                            && (child.prevAction.x == nextNode.prevAction.x)){
                        savedRoot = child;

                        break;
                    }
                }
            }
//            prevAction = action;

//            for(Node child: root.children) {
//                System.out.printf("currentPlayer: %d Action:(%d, %d), Visit: %d UCB: %f\n", currentPlayer,
//                        child.prevAction.y, child.prevAction.x, child.visit, Node.getUCB(child));
//            }
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
    private static int[][] testBetweenNonSaveAndSave(int mctsIterations, int gameIterations) {

        // results: [save, nonSave], draw
        // row 1: when save first player, row 2: when nonSave first player.
        MCTSSimulator simulator = new VanillaStrategySimulator();
        int[][] results = new int[][] {
                {0, 0, 0},
                {0, 0, 0}
        };
        int result = 0;
        for(int i = 0; i < gameIterations; i++) {
            long startTime = System.currentTimeMillis();
            result = testAmongMCTS(new Gomoku(), mctsIterations, simulator, 1);
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
        for(int i = 0; i < gameIterations; i++) {
            long startTime = System.currentTimeMillis();
            result = testAmongMCTS(new Gomoku(), mctsIterations, simulator, -1);
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
