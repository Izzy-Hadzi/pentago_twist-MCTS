package student_player;

import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;

import java.util.*;
//represents a node in the MCTS tree
public class Node {
    PentagoBoardState state;
    Node parent;
    ArrayList<Node> children;
    int playerNum;
    int visits;
    double numWins;
    PentagoMove action;

    //constructors for node
    public Node(Node otherNode){
        this.state = (PentagoBoardState) otherNode.getState().clone();
        this.parent = otherNode.getParent();
        this.children = otherNode.getChildren();
        this.playerNum = otherNode.getPlayerNum();
        this.visits = otherNode.getVisits();
        this.numWins = otherNode.getWinScore();
        this.action = otherNode.getAction();
    }
    public Node(PentagoBoardState state, PentagoMove newaction, int newplayerNum){
        this.state = state;
        this.parent = null;
        this.visits =1;
        this.numWins =0;
        this.action = newaction;
        this.playerNum= newplayerNum;
        this.children = new ArrayList<Node>();
    }

    //get/set methods
    public ArrayList<Node> getChildren(){
        return children;
    }

    public Node getParent(){
        return this.parent;
    }

    public void setParent(Node newparent){
        this.parent= newparent;
    }

    public int getVisits(){
        return visits;
    }

    public void setVisits(int newvisits){
        this.visits = newvisits;
    }

    public double getWinScore(){
        return numWins;
    }

    public void setWinScore(double newWinScore){
        this.numWins = newWinScore;
    }

    public PentagoBoardState getState(){
        return this.state;
    }

    public int getPlayerNum(){
        return this.playerNum;
    }

    public void setPlayerNum(int newPlayerNum){
        this.playerNum = newPlayerNum;
    }

    public PentagoMove getAction(){
        return this.action;
    }

    //adding new child method instead of a set method
    public void addChild(Node newchild){
        children.add(newchild);
    }

    //method to get random child of this node
    public Node getRandomChild(){
        int indexOfRandom = (int) (Math.random() * (children.size()));
        return this.children.get(indexOfRandom);
    }

    //method to get the child of this node with the maximum UCT value
    public Node getMaxChild(){
        return Collections.max(children, Comparator.comparing(c-> c.UpperConfidenceValue()));
    }

    //method to expand all the possible children of this node (according to which moves are legal in context
    public void getAllPossibleStates(){
        if (this.children.size() >0){
            return;
        }
        ArrayList<PentagoMove> possibleMoves= this.state.getAllLegalMoves();
        //converting valid moves into new states and Nodes and adding this to children
        for(PentagoMove m: possibleMoves){
            PentagoBoardState newstate = (PentagoBoardState) this.state.clone();
            newstate.processMove(m);
            Node newChild = new Node(newstate, m, 1-this.getPlayerNum());
            newChild.setParent(this);
            addChild(newChild);
        }
    }

    //method for calculating UCT value for this node
    public double UpperConfidenceValue(){
        int s = this.parent.getVisits();
        double nodeScore = this.numWins/ (double) this.visits;
        double c = Math.sqrt(2);
        // formula for Q(s,a) = Q-(s,a) + c sqrt(log(n(s)/(n(s,a)))
        return nodeScore + c*Math.sqrt(Math.log(s)/(double)this.visits);
    }
}
