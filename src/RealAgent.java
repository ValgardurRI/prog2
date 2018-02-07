import java.util.ArrayList;

public class RealAgent implements Agent{
	
	private String role; // the name of this agent's role (white or black)
	private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
	private boolean myTurn; // whether it is this agent's turn or not
	private int width, height; // dimensions of the board
	State playState; // The state of the game as it appears 
	
	public void init(String role, int width, int height, int playclock) {
		this.role = role;
		this.playclock = playclock;
		myTurn = !role.equals("white");
		this.width = width;
		this.height = height;
		playState = State.generateInitialState(width, height);
    }
	
	
	 public String nextAction(int[] lastMove) {
	    	if (lastMove != null) {
	    		int x1 = lastMove[0], y1 = lastMove[1], x2 = lastMove[2], y2 = lastMove[3];
	    		String roleOfLastPlayer;
	    		if (myTurn && role.equals("white") || !myTurn && role.equals("black")) {
	    			roleOfLastPlayer = "white";
	    		} else {
	    			roleOfLastPlayer = "black";
	    		}
	   			System.out.println(roleOfLastPlayer + " moved from " + x1 + "," + y1 + " to " + x2 + "," + y2);
	    		// TODO: 1. update your internal world model according to the action that was just executed
	   			
	   			
	    	}
			
	    	// update turn (above that line it myTurn is still for the previous state)
			myTurn = !myTurn;
			if (myTurn) {
				// TODO: 2. run alpha-beta search to determine the best move

				
				// Here we just construct a random move (that will most likely not even be possible),
				// this needs to be replaced with the actual best move.
				int x1,y1,x2,y2;
				x1 = width-1;
				x2 = x1 + 3;
				if (role.equals("white")) {
					y1 = height-1;
					y2 = y1 + 1;
				} else {
					y1 = height-1;
					y2 = y1 - 1;
				}
				return "(move " + x1 + " " + y1 + " " + x2 + " " + y2 + ")";
			} else {
				return "noop";
			}
		}
	
	@Override
	public void cleanup() {
	
	}
}
