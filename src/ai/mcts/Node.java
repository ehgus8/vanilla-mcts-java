package ai.mcts;

import games.BoardGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Node {
    public Node parent;
    public Point prevAction;
    public int currentPlayer;
    public int moveCount;
    public List<Node> children = new ArrayList<>();
    public int visit = 0;
    public double value = 0;

    public Node(Node parent, Point prevAction, int currentPlayer, int moveCount) {
        this.parent = parent;
        this.prevAction = prevAction;
        this.currentPlayer = currentPlayer;
        this.moveCount = moveCount;
    }

    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    public static double getUCB(Node node) {
        return node.value / node.visit + Math.sqrt(2 * Math.log(node.parent.visit) / node.visit);
    }

    public Node select() {
        double maxUCB = -1;
        Node selectedNode = null;
        for(Node child: this.children) {
            double curUCB = Node.getUCB(child);
            if(maxUCB < curUCB) {
                maxUCB = curUCB;
                selectedNode = child;
            }
        }

        return selectedNode;
    }

    public void expand(List<Point> validActions) {
        for(Point action: validActions) {
            this.children.add(new Node(this, action, 1 - this.currentPlayer,
                    this.moveCount + 1));
        }
    }

    public void backup(BoardGame game, List<Node> trace, double value) {
        for(int i = trace.size() - 1; i >= 0; i--) {
            Node node = trace.get(i);
            node.visit++;
            node.value += value;
            value *= -1;
            if(node.parent != null) {
                game.undoAction(node.prevAction);
            }
        }
    }
}
