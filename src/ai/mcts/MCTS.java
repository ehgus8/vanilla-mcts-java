package ai.mcts;

import games.BoardGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MCTS {

    public MCTS() {
    }

    public static void mcts(BoardGame game, Node root, int mctsIterations) {

        for(int i = 0; i < mctsIterations; i++) {
            // Initializing
            Node currentNode = root;
            List<Node> trace = new ArrayList<>();
            trace.add(currentNode);

            // 1. Select
            while(currentNode.hasChildren()) {
                currentNode = currentNode.select();
                trace.add(currentNode);

                game.applyAction(currentNode.parent.currentPlayer, currentNode.prevAction);
            }
            if(currentNode.parent != null) {
                int winner = game.checkWinner(currentNode.parent.currentPlayer, currentNode.prevAction);
                if(winner != -1) {
                    double value = currentNode.parent.currentPlayer == winner ? 1 : -1;
                    currentNode.backup(game, trace, value);
                    continue;
                } else if (currentNode.moveCount == game.rows * game.cols) {
                    currentNode.backup(game, trace, 0);
                    continue;
                }
            }

            // 2. expand
            currentNode.expand(game.getValidActions());
            double value = MCTS.simulate(game, currentNode);
            currentNode.backup(game, trace, value);
        }
    }

    public static double simulate(BoardGame game, Node node) {

        int currentPlayer = node.currentPlayer;
        int moveCount = node.moveCount;

        int stateDim = game.rows * game.cols;
        while(moveCount < stateDim) {
            List<Point> validActions = game.getValidActions();
            int actionIdx = (int)(Math.random() * validActions.size());
            Point action = validActions.get(actionIdx);

            currentPlayer = game.applyAction(currentPlayer, action);
            moveCount++;

            int winner = game.checkWinner(1 - currentPlayer, action);
            if(winner != -1) {
                return winner == (1 - node.currentPlayer) ? 1 : -1;
            }
        }
        return 0; // draw
    }
}