package main;

import main.ai.mcts.MCTS;
import main.ai.mcts.Node;
import main.games.BoardGame;
import main.games.Gomoku;
import main.games.TicTacToe;

import java.awt.*;
import java.util.Scanner;

public class Main {

    public static void playAgainstMCTS(BoardGame game, int mctsIterations) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Select your turn.");
        System.out.println("1. first\t 2. second");

        String turn = sc.next();
        int userTurn;
        if(turn.equals("1")) {
            userTurn = 1;
        } else if (turn.equals("2")) {
            userTurn = -1;
        } else {
            System.out.println("Invalid input.");
            return;
        }

        int currentPlayer = 1;
        int moveCount = 0;
        int winner = 0;
        while(true) {
            game.displayBoard();
            if(currentPlayer == userTurn) {
                Point action = game.getUserAction();
                currentPlayer = game.applyAction(currentPlayer, action);
                moveCount++;
                winner = game.checkWinner(currentPlayer * -1, action);
            } else {
                Node root = new Node(null, null, currentPlayer, moveCount);
                long startTime = System.currentTimeMillis();
                MCTS.mcts(game, root, mctsIterations);
                long endTime = System.currentTimeMillis();
                Point action = root.getNextAction("maxVisit");
                currentPlayer = game.applyAction(currentPlayer, action);
                moveCount++;
                winner = game.checkWinner(currentPlayer * -1, action);

                for(Node child: root.children) {
                    System.out.printf("Action:(%d, %d), Visit: %d UCB: %f\n",child.prevAction.y, child.prevAction.x, child.visit, Node.getUCB(child));
                }
                System.out.printf("execute time of mcts: %f sec \n", (endTime - startTime) / 1000.0);
            }

            if(winner != 0) {
                game.displayBoard();
                if(winner == userTurn) {
                    System.out.println("User win!");
                } else {
                    System.out.println("Computer win!");
                }
                return;
            }
            else if(moveCount == game.rows * game.cols) {
                game.displayBoard();
                System.out.println("Draw!");
                return;
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        BoardGame game;
        while(true) {
            System.out.println("1. TicTacToe");
            System.out.println("2. Gomoku(Omok)");
            System.out.println("3. Exit");
            String sel = sc.next();
            if (sel.length() > 1) {
                System.out.println("Invalid input.");
            } else if (sel.equals("1")) {
                game = new TicTacToe();
                playAgainstMCTS(game, 500);
            } else if (sel.equals("2")) {
                game = new Gomoku();
                playAgainstMCTS(game, 800);
            } else if (sel.equals("3")) {
                return;
            } else {
                System.out.println("Invalid input.");
            }
        }
    }
}
