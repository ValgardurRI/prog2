import java.util.ArrayList;

public class RealAgent implements Agent{
	
	private String role; // the name of this agent's role (white or black)
	private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
	private boolean myTurn; // whether it is this agent's turn or not
	private int width, height; // dimensions of the board
	State playState; // The state of the game as it appears 
	Search adversarySearch;
	
	public void init(String role, int width, int height, int playclock) {
		adversarySearch = new Search(width, height, role);
		
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
	    		if(!roleOfLastPlayer.equals(role)) {
		   			System.out.println(roleOfLastPlayer + " moved from " + x1 + "," + y1 + " to " + x2 + "," + y2);
		   			Position oldPos = new Position(x1, y1);
		   			Position newPos = new Position(x2, y2);
		   			Pawn wasCaptured = new Pawn(role.equals("white")?Pawn.Color.White:Pawn.Color.Black, newPos); //if there was a captured pawn, we want to remove it.
		   			Pawn movedPawn = new Pawn(role.equals("white")?Pawn.Color.Black:Pawn.Color.White, oldPos);
		   			if(playState.pawns.contains(wasCaptured)) {
		   				System.out.println("captured was removed? " + playState.pawns.remove(wasCaptured));
		   				playState.myPawns--;
		   			}
		   			
		   			System.out.println("white pawn removed? " + playState.pawns.remove(movedPawn)); //delete old version of pawn
		   			
		   			movedPawn.pos = newPos;
		   			playState.pawns.add(movedPawn); //re-add the pawn	   			
	   			}
	   			

	    	}
			
	    	// update turn (above that line it myTurn is still for the previous state)
			myTurn = !myTurn;
			if (myTurn) {
				// TODO: 2. run alpha-beta search to determine the best move

				
				// Here we just construct a random move (that will most likely not even be possible),
				// this needs to be replaced with the actual best move.
				adversarySearch = new Search(playState, role);
				
				StateNode newPlayStateNode = adversarySearch.testMove();
				playState = newPlayStateNode.state;
				return newPlayStateNode.actionTo;
			} else {
				return "noop";
			}
		}
	
	@Override
	public void cleanup() {
	
	}
}
