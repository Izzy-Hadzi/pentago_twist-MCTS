package student_player;

import boardgame.Move;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoPlayer;

/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260774610");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(PentagoBoardState boardState) {
        //see getTurn Number for boardState and first two moves are random (starting sequence)
        if (boardState.getTurnNumber() < 2) {
            Move myMove = boardState.getRandomMove();
            return myMove;
        }
        //finding out if we are player 0 or 1
        int playerNum = boardState.getTurnPlayer();
        Node root = new Node(boardState, null, playerNum);
        //setting time for method to run, I set it to 1.5s so the it would not cut off mid-iteration with the 2s time limit
        long start = System.currentTimeMillis();
        long end = start + 1500;
        //building the tree for the amount of time allowed
        while (System.currentTimeMillis() <end){
            //selection phase
            Node selectedNode = MyTools.selectPromisingNode(root);
            //expansion phase as long as the node is not a terminal node, if it was skip to simulation
            if(!selectedNode.getState().gameOver()) {
                MyTools.expandNode(selectedNode);
            }
            Node nodeToExpand = selectedNode;
            //picking random child for the stimulation phase
            if(selectedNode.getChildren().size()>0){
                nodeToExpand= selectedNode.getRandomChild();
            }
            int stimulationScore = MyTools.stimulateGame(nodeToExpand, playerNum);
            //backpropagation phase
            MyTools.backPropagation(nodeToExpand, stimulationScore, playerNum);
        }
        //getting the action/PentagoMove from the node with the best Upper Confidence Tree value
        Node bestMove = root.getMaxChild();
        Move myMove = bestMove.getAction();
        return myMove;
    }
}