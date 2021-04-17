package student_player;
public class MyTools {
    //Method for the selection phase
    public static Node selectPromisingNode(Node node){
        while (node.getChildren().size()!=0){
            node = node.getMaxChild();
        }
        return node;
    }

    //Method for the expansion phase
    public static void expandNode(Node node){
        node.getAllPossibleStates();
    }

    //Method for the simulation phase
    public static int stimulateGame(Node node, int numPlayer){
        //using random move default policy
        Node temp = new Node(node);
        //check if the node is a leaf node
        int status = temp.getState().getWinner();
        //checking if its a node where the opponent won and penalize accordingly
        if (status == (1-numPlayer)) {
            temp.getParent().setWinScore(-20);
            return status;
        }
        //if node is not a leaf node, expanding randomly though the tree until a leaf node is reached
        while (!temp.getState().gameOver()){
            temp.getAllPossibleStates();
            temp = temp.getRandomChild();
        }
        //returning who the winner was
        status = temp.getState().getWinner();
        return status;
    }

    //Method for the backpropagation phase
    public static void backPropagation(Node node , int playerwin, int playerNum){
        Node temp = node;
        while(temp != null){
            //incrementing the number of visits to the node by and if the player won, adding 10 'wins'
            temp.setVisits(temp.getVisits()+1);
            if(playerNum == playerwin){
                temp.setWinScore(temp.getWinScore()+10);
            }
            temp = temp.getParent();
        }
    }
}