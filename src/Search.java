import java.util.ArrayList;


public class Search {
	int width;
	int height;
	int playclock;
	Pawn.Color agentColor;
	StateNode initNode;
		
	public Search(int width, int height, String role)
	{
		//states = new ArrayList<StateNode>();
		initNode = new StateNode(State.generateInitialState(width, height), null, "noop"); 
		//states.add(initNode);
		this.width = width;
		this.height = height;
		
		
		agentColor = role.equals("white") ? Pawn.Color.White : Pawn.Color.Black;
	}
	
	public Search(State startState, String role)
	{
		initNode = new StateNode(startState, null, "noop"); 
		this.width = startState.width;
		this.height = startState.height;
		
		agentColor = role.equals("white") ? Pawn.Color.White : Pawn.Color.Black;
	}
	
	//expands our statenode list by generating list of legal moves from node
	void expand()
	{
		generateLegalMoves(initNode);
		
		//3 cases:
		//there is an enemy, 1 up & 1 left
		//there is an enemy, 1 up & 1 right
		//move 1 (up if white, down if black) if it isn't at bounds and a friendly pawn isn't there
		//Ingibergur said that we're going to want to expand all legal moves for ALL pawns. 
		//This means that our branching factor becomes insane. (but it makes sense though)
	}
	//for the state in stateNode s, we want to generate all legal moves.
	//TODO: TEST
	private ArrayList<StateNode> generateLegalMoves(StateNode s) {
		ArrayList<StateNode> states = new ArrayList<StateNode>();

		for(Pawn p: s.state.pawns) {
			if(p.color == agentColor)
			{
				Pawn leftCapture = s.state.canCaptureLeft(p);
				if(leftCapture != null) {
					
					State newState = new State(s.state.pawns, width, height);
					
					Pawn movedPawn = leftCapture;
					newState.pawns.remove(leftCapture);
					newState.pawns.remove(p);
					movedPawn.color = p.color;
					newState.pawns.add(movedPawn);
					
					newState.myColor = s.state.myColor;
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
					
					newState.myColor = s.state.myColor;
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
					
					newState.myColor = s.state.myColor;
					newState.opponentPawns = s.state.opponentPawns;
					newState.myPawns = s.state.myPawns;
					String action = "(move " + p.pos.x + " " + p.pos.y + " " + forwardPawn.pos.x + " " + forwardPawn.pos.y + " )";
					StateNode newStateNode = new StateNode(newState, s, action);
					
					states.add(newStateNode);
				}
			}
		}
		return states;
	}
	
	public void getMove(State currentState) {
		//expand and alpha beta prune from currentState and return the best move!
		//TODO: implement : D
	}
	
	public StateNode maxTurn(Pawn.Color playerColor, StateNode stateNode) {
			//code for maxplayer
			StateNode bestChild = null;
			ArrayList<StateNode> childNodes = generateLegalMoves(stateNode); //TODO: change this function to no longer have this super weird global thing
			if(childNodes.isEmpty()) {
				//no possible moves, terminal state
				return stateNode;
			}
			
			for(StateNode childNode: childNodes) {
				   if( childNode.state.isWinstate() != null) {
					   return childNode;
				   }
				   StateNode generatedChild = minTurn(playerColor == Pawn.Color.White ? Pawn.Color.Black : Pawn.Color.White, childNode);
				   if(bestChild == null) {
					   bestChild = generatedChild;
				   }
				   else if(bestChild.state.value() < generatedChild.state.value()) {
					   bestChild = generatedChild;
				   }
			}
			return bestChild;
	}
	
	public StateNode minTurn(Pawn.Color playerColor, StateNode stateNode) {

		//code for minplayer
		StateNode bestChild = null;
		ArrayList<StateNode> childNodes = generateLegalMoves(stateNode); //TODO: change this function to no longer have this super weird global thing
		if(childNodes.isEmpty()) {
			//no possible moves, terminal state
			return stateNode;
		}
		
		for(StateNode childNode: childNodes) {
			   if( childNode.state.isWinstate() != null) {
				   return childNode;
			   }
			   StateNode generatedChild = maxTurn(playerColor == Pawn.Color.White ? Pawn.Color.Black : Pawn.Color.White, childNode);
			   if(bestChild == null) {
				   bestChild = generatedChild;
			   }
			   else if(bestChild.state.value() > generatedChild.state.value()) {
				   bestChild = generatedChild;
			   }
		}
		return bestChild;
	}
	
	//should be deleted eventually, testing purposes
	
	public StateNode testMove()
	{
		//ArrayList<StateNode> nodes = generateLegalMoves(initNode);
		//return nodes.get(nodes.size()-1).actionTo;
		StateNode winBoy = minTurn(agentColor, initNode);
		
		while(winBoy.parent != initNode) {
			winBoy = winBoy.parent;
		}
		System.out.println(winBoy.state);
		return winBoy;
	}
	
	
}


