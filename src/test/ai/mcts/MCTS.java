package test.ai.mcts;

import test.games.BoardGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MCTS {

    public MCTS() {
    }

    public static void mcts(BoardGame game, Node root, int mctsIterations, MCTSSimulator simulator) {

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
                if(winner != 0) {
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
            double value = simulator.simulate(game, currentNode);
            currentNode.backup(game, trace, value);
        }
    }
}