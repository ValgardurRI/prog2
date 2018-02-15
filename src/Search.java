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
		//states.add(initNode);
		this.width = width;
		this.height = height;
		
		
		agentColor = role.equals("white") ? Pawn.Color.White : Pawn.Color.Black;
		initNode = new StateNode(State.generateInitialState(width, height, agentColor), null, "noop"); 
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
		//generateLegalMoves(initNode);
		
		//3 cases:
		//there is an enemy, 1 up & 1 left
		//there is an enemy, 1 up & 1 right
		//move 1 (up if white, down if black) if it isn't at bounds and a friendly pawn isn't there
		//Ingibergur said that we're going to want to expand all legal moves for ALL pawns. 
		//This means that our branching factor becomes insane. (but it makes sense though)
	}
	//for the state in stateNode s, we want to generate all legal moves.
	//TODO: TEST
	private ArrayList<StateNode> generateLegalMoves(StateNode s, Pawn.Color color) {
		ArrayList<StateNode> states = new ArrayList<StateNode>();

		for(Pawn p: s.state.pawns) {
			if(p.color == color)
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
			ArrayList<StateNode> childNodes = generateLegalMoves(stateNode, playerColor); //TODO: change this function to no longer have this super weird global thing
			if(childNodes.isEmpty() || stateNode.state.isWinstate() != null) {
				//no possible moves, terminal state
				return stateNode;
			}
			
			for(StateNode childNode: childNodes) {
				   StateNode generatedChild = minTurn(playerColor == Pawn.Color.White ? Pawn.Color.Black : Pawn.Color.White, childNode);
				   if(bestChild == null) {
					   bestChild = generatedChild;
				   }
				   if(bestChild.state.value() < generatedChild.state.value()) {
					   bestChild = generatedChild;
				   }
			}
			return bestChild;
	}
	
	public StateNode minTurn(Pawn.Color playerColor, StateNode stateNode) {

		//code for minplayer
		StateNode bestChild = null;
		ArrayList<StateNode> childNodes = generateLegalMoves(stateNode, playerColor); //TODO: change this function to no longer have this super weird global thing
		if(childNodes.isEmpty() || stateNode.state.isWinstate() != null) {
			//no possible moves, terminal state
			return stateNode;
		}
		
		for(StateNode childNode: childNodes) {
			   StateNode generatedChild = maxTurn(playerColor == Pawn.Color.White ? Pawn.Color.Black : Pawn.Color.White, childNode);
			   if(bestChild == null) {
				   bestChild = generatedChild;
			   }
			   if(bestChild.state.value() > generatedChild.state.value()) {
				   bestChild = generatedChild;
			   }
		}
		return bestChild;
	}
	
	//should be deleted eventually, testing purposes
	
	public StateNode testMove()
	{
		/*
		ArrayList<StateNode> nodes = generateLegalMoves(initNode, Pawn.Color.White);
		StateNode testNode1 =  nodes.get(nodes.size()-1);
		System.out.println(testNode1.state);
		
		nodes = generateLegalMoves(testNode1, Pawn.Color.Black);
		StateNode testNode2 =  nodes.get(nodes.size()-1);
		System.out.println(testNode2.state);
		
		nodes = generateLegalMoves(testNode2, Pawn.Color.White);
		StateNode testNode3 =  nodes.get(nodes.size()-1);
		System.out.println(testNode3.state);
		
		nodes = generateLegalMoves(testNode3, Pawn.Color.Black);
		StateNode testNode4 =  nodes.get(nodes.size()-1);
		System.out.println(testNode4.state);
		
		return testNode4;
		*/
		
		
		StateNode winBoy = maxTurn(agentColor, initNode); //WHHHHY MINMOVE???? WHY???
		System.out.println("was it a winstate? " + winBoy.state.isWinstate());
		System.out.println("chosen path minimax value: " + winBoy.state.value());
		while(winBoy.parent != initNode) {
			System.out.println(winBoy.state);
			winBoy = winBoy.parent;
		}
		System.out.println(winBoy.state);
		return winBoy;
		
		
		
	}
	
	
}


