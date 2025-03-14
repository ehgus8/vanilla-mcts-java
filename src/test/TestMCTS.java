package test;

import test.ai.mcts.*;
import test.games.BoardGame;
import test.games.Gomoku;
import test.tester.AmongMCTSTester;
import test.tester.AmongSimulatorTester;

import java.awt.*;

public class TestMCTS {

    public static void main(String[] args) {

        MCTSSimulator[] simulators = new MCTSSimulator[] {new VanillaStrategySimulator(), new VanillaStrategySimulator()};
        int[][] results = AmongSimulatorTester.testBetweenSimulators(new Gomoku(), 100,1000, simulators);
        System.out.printf("결과: [%d, %d, %d] save 선공, nonSave 후공\n", results[0][0], results[0][1], results[0][2]);
        System.out.printf("결과: [%d, %d, %d] save 후공, nonSave 선공\n", results[1][0], results[1][1], results[1][2]);
        System.out.printf("합산 결과: [%d, %d, %d]\n",
                results[0][0] + results[1][0],
                results[0][1] + results[1][1],
                results[0][2] + results[1][2]);
    }



}
