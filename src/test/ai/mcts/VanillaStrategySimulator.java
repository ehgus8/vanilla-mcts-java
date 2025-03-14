package test.ai.mcts;

import test.games.BoardGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VanillaStrategySimulator implements MCTSSimulator{

    @Override
    public double simulate(BoardGame game, Node node) {

        int currentPlayer = node.currentPlayer;
        int moveCount = node.moveCount;
        int winner = 0;
        int stateDim = game.rows * game.cols;
        java.util.List<Point> actionHistory = new ArrayList<>();
        while(moveCount < stateDim && winner == 0) {
            List<Point> validActions = game.getValidActions();

            int actionIdx = (int)(Math.random() * validActions.size());
            Point action = validActions.get(actionIdx);

            currentPlayer = game.applyAction(currentPlayer, action);
            actionHistory.add(action);
            moveCount++;

            winner = game.checkWinner(currentPlayer * -1, action);

        }
        for(Point a: actionHistory) {
            game.undoAction(a);
        }
        if(winner != 0) {
            return winner == (node.currentPlayer * -1) ? 1 : -1;
        }
        return 0; // draw
    }
}
