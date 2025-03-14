package test.ai.mcts;

import test.games.BoardGame;

public interface MCTSSimulator {
    public double simulate(BoardGame game, Node node);
}
