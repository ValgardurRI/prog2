import java.util.ArrayList;

public class Search {
	int width;
	int height;
	int playclock;
	
	
	//just to create SOME sort of state space, I'm using an arraylist.
	ArrayList<StateNode> states;
	
	public Search(int width, int height)
	{
		states = new ArrayList<StateNode>();
		this.width = width;
		this.height = height;
	}
	
	//expands our statenode list by generating list of legal moves from node
	void expand()
	{
		for(StateNode s : states)
		{
			generateLegalMoves(s);
			
			
			//3 cases:
			//there is an enemy, 1 up & 1 left
			//there is an enemy, 1 up & 1 right
			//move 1 up if it isn't at bounds and a friendly pawn isn't there
		}
		//Ingibergur said that we're going to want to expand all legal moves for ALL pawns. 
		//This means that our branching factor becomes insane. (but it makes sense though)
	}
	//for the state in stateNode s, we want to generate all legal moves.
	//TODO: TEST
	private void generateLegalMoves(StateNode s) {
		
		for(Pawn p: s.state.pawns) {
			Pawn leftCapture = s.state.canCaptureLeft(p);
			if(leftCapture != null) {
				
				State newState = new State(width, height);
				newState.pawns = s.state.pawns;
				Pawn movedPawn = leftCapture;
				newState.pawns.remove(leftCapture);
				newState.pawns.remove(p);
				movedPawn.color = p.color;
				newState.pawns.add(movedPawn);
				
				newState.myTurn = !s.state.myTurn;
				newState.opponentPawns = s.state.opponentPawns -1;
				newState.myPawns = s.state.myPawns;
				String action = "move(" + p.pos.x + " " + p.pos.y + " " + movedPawn.pos.x + " " + movedPawn.pos.y + " ";
				StateNode newStateNode = new StateNode(newState, s, action);
				
				states.add(newStateNode);
				
			}
			Pawn rightCapture = s.state.canCaptureLeft(p);
			if(s.state.canCaptureLeft(p) != null) {
				State newState = new State(width, height);
				newState.pawns = s.state.pawns;
				Pawn movedPawn = rightCapture;
				newState.pawns.remove(rightCapture);
				newState.pawns.remove(p);
				movedPawn.color = p.color;
				newState.pawns.add(movedPawn);
				
				newState.myTurn = !s.state.myTurn;
				newState.opponentPawns = s.state.opponentPawns -1;
				newState.myPawns = s.state.myPawns;
				String action = "move(" + p.pos.x + " " + p.pos.y + " " + movedPawn.pos.x + " " + movedPawn.pos.y + " ";
				StateNode newStateNode = new StateNode(newState, s, action);
				
				states.add(newStateNode);
				}
			Pawn forwardPawn = s.state.canGoForward(p);
			if(forwardPawn != null) {
				State newState = new State(width, height);
				newState.pawns = s.state.pawns;
				newState.pawns.remove(p);
				newState.pawns.add(forwardPawn);
				
				newState.myTurn = !s.state.myTurn;
				newState.opponentPawns = s.state.opponentPawns;
				newState.myPawns = s.state.myPawns;
				String action = "move(" + p.pos.x + " " + p.pos.y + " " + forwardPawn.pos.x + " " + forwardPawn.pos.y + " ";
				StateNode newStateNode = new StateNode(newState, s, action);
				
				states.add(newStateNode);
			}
		}
	}
	
	public void getMove(State currentState) {
		//expand and alpha beta prune from currentState and return the best move!
		//TODO: implement : D
	}
	
}


