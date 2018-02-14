import java.util.ArrayList;

public class Search {
	int width;
	int height;
	int playclock;
	Pawn.Color playerColor;
	
	
	//just to create SOME sort of state space, I'm using an arraylist.
	ArrayList<StateNode> states;
	
	public Search(int width, int height, String role)
	{
		states = new ArrayList<StateNode>();
		StateNode initNode = new StateNode(State.generateInitialState(width, height), null, "noop"); 
		states.add(initNode);
		this.width = width;
		this.height = height;
		
		playerColor = role.equals("white") ? Pawn.Color.White : Pawn.Color.Black;
	}
	
	public Search(State startState, String role)
	{
		states = new ArrayList<StateNode>();
		StateNode initNode = new StateNode(startState, null, "noop"); 
		states.add(initNode);
		this.width = startState.width;
		this.height = startState.height;
		
		playerColor = role.equals("white") ? Pawn.Color.White : Pawn.Color.Black;
	}
	
	//expands our statenode list by generating list of legal moves from node
	void expand()
	{
		generateLegalMoves(states.remove(0));
		
		//3 cases:
		//there is an enemy, 1 up & 1 left
		//there is an enemy, 1 up & 1 right
		//move 1 (up if white, down if black) if it isn't at bounds and a friendly pawn isn't there
		//Ingibergur said that we're going to want to expand all legal moves for ALL pawns. 
		//This means that our branching factor becomes insane. (but it makes sense though)
	}
	//for the state in stateNode s, we want to generate all legal moves.
	//TODO: TEST
	private void generateLegalMoves(StateNode s) {
		ArrayList<Pawn> pawnCopy = s.state.pawns;
		for(Pawn p: s.state.pawns) {
			if(p.color == playerColor)
			{
				Pawn leftCapture = s.state.canCaptureLeft(p);
				if(leftCapture != null) {
					
					State newState = new State(s.state.pawns, width, height);
					
					Pawn movedPawn = leftCapture;
					newState.pawns.remove(leftCapture);
					newState.pawns.remove(p);
					movedPawn.color = p.color;
					newState.pawns.add(movedPawn);
					
					newState.myTurn = !s.state.myTurn;
					newState.opponentPawns = s.state.opponentPawns -1;
					newState.myPawns = s.state.myPawns;
					String action = "(move " + p.pos.x + " " + p.pos.y + " " + movedPawn.pos.x + " " + movedPawn.pos.y + " )";
					StateNode newStateNode = new StateNode(newState, s, action);
					
					states.add(newStateNode);
					
				}
				Pawn rightCapture = s.state.canCaptureRight(p);
				if(s.state.canCaptureRight(p) != null) {
					State newState = new State(s.state.pawns, width, height);
					
					Pawn movedPawn = rightCapture;
					newState.pawns.remove(rightCapture);
					newState.pawns.remove(p);
					movedPawn.color = p.color;
					newState.pawns.add(movedPawn);
					
					newState.myTurn = !s.state.myTurn;
					newState.opponentPawns = s.state.opponentPawns -1;
					newState.myPawns = s.state.myPawns;
					String action = "(move " + p.pos.x + " " + p.pos.y + " " + movedPawn.pos.x + " " + movedPawn.pos.y + " )";
					StateNode newStateNode = new StateNode(newState, s, action);
					
					states.add(newStateNode);
					}
				Pawn forwardPawn = s.state.canGoForward(p);
				if(forwardPawn != null) {
					State newState = new State(s.state.pawns, width, height);
					
					newState.pawns.remove(p);
					newState.pawns.add(forwardPawn);
					
					newState.myTurn = !s.state.myTurn;
					newState.opponentPawns = s.state.opponentPawns;
					newState.myPawns = s.state.myPawns;
					String action = "(move " + p.pos.x + " " + p.pos.y + " " + forwardPawn.pos.x + " " + forwardPawn.pos.y + " )";
					StateNode newStateNode = new StateNode(newState, s, action);
					
					states.add(newStateNode);
				}
			}
		}
	}
	
	public void getMove(State currentState) {
		//expand and alpha beta prune from currentState and return the best move!
		//TODO: implement : D
	}
	
	//should be deleted eventually, testing purposes
	public String testMove()
	{
		expand();

		for(StateNode snoke : states)
		{
			System.out.println("legal move: " + snoke.actionTo);
		}
		StateNode stateNode = states.remove(states.size()-1);
		System.out.println(stateNode.state);
		
		return stateNode.actionTo;
	}
	
}


