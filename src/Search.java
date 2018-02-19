import java.util.HashSet;


public class Search {
	int width;
	int height;
	int playclock;
	Pawn.Color agentColor;
	StateNode initNode;
	int playClock;
	StateNode bestValueNode;
	int totalExpansions; 
	
	long timeStart;
	public Search(int width, int height, String role, int clock)
	{
		//states = new ArrayList<StateNode>();
		//states.add(initNode);
		this.width = width;
		this.height = height;
		
		
		agentColor = role.equals("white") ? Pawn.Color.White : Pawn.Color.Black;
		initNode = new StateNode(State.generateInitialState(width, height, agentColor), null, "noop"); 
		
		timeStart = System.currentTimeMillis();
		
		playClock = clock;
	}
	
	private double ElapsedTime()
	{
		long timeNow = System.currentTimeMillis();
		return (timeNow - timeStart)/1000.0;
	}
	
	public Search(State startState, String role, int clock)
	{
		initNode = new StateNode(startState, null, "noop"); 
		this.width = startState.width;
		this.height = startState.height;
		
		agentColor = role.equals("white") ? Pawn.Color.White : Pawn.Color.Black;
		
		playClock = clock;
		timeStart = System.currentTimeMillis();
		
		totalExpansions = 0;
	}
	

	//for the state in stateNode s, we want to generate all legal moves.
	private HashSet<StateNode> generateLegalMoves(StateNode s, Pawn.Color color) {
		HashSet<StateNode> states = new HashSet<StateNode>();

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
					
					newState.opponentPawns = s.state.opponentPawns;
					newState.myPawns = s.state.myPawns;
					
					if(color == agentColor)
						newState.opponentPawns--;
					else
						newState.myPawns--;
					
					String action = "(move " + p.pos.x + " " + p.pos.y + " " + movedPawn.pos.x + " " + movedPawn.pos.y + " )";
					StateNode newStateNode = new StateNode(newState, s, action);
					
					states.add(newStateNode);
					
					totalExpansions++;
					
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
					newState.opponentPawns = s.state.opponentPawns;
					
					if(color == agentColor)
						newState.opponentPawns--;
					else
						newState.myPawns--;
					
					newState.myPawns = s.state.myPawns;
					String action = "(move " + p.pos.x + " " + p.pos.y + " " + movedPawn.pos.x + " " + movedPawn.pos.y + " )";
					StateNode newStateNode = new StateNode(newState, s, action);
					
					states.add(newStateNode);
					
					totalExpansions++;
					
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
					
					totalExpansions++;
					
				}
			}
		}
		return states;
	}

	
	public StateNode maxTurn(Pawn.Color playerColor, StateNode stateNode, int alpha, int beta, int limit) { 
	
			StateNode bestChild = null;
			HashSet<StateNode> childNodes = generateLegalMoves(stateNode, playerColor); 
			
			//terminal test
			if(childNodes.isEmpty() || stateNode.state.isWinstate() != null || limit == 0) {
				stateNode.valueCheck(0, 0);
				if (childNodes.isEmpty()) {
					stateNode.value = 0;
				}
				return stateNode;
			}
			
			stateNode.value = Integer.MIN_VALUE;
			for(StateNode childNode: childNodes) {
				   StateNode generatedChild = minTurn(playerColor == Pawn.Color.White ? Pawn.Color.Black : Pawn.Color.White, childNode, alpha, beta, limit - 1);
				   generatedChild.valueCheck(0, Integer.MIN_VALUE);

				   if(bestChild == null) {
					   //The first child we look at, let's instantiate some stuff!
					   bestChild = generatedChild;
					   stateNode.value = Integer.max(stateNode.value, bestChild.value);
				   }
				   //watching the clock
				    if(ElapsedTime() > (playClock - 0.15))
					{
						return bestChild;
					}
				    
				   if(bestChild.value < generatedChild.value) {
					   //Is the child we're looking at better than any previous child?
					   stateNode.value = Integer.max(stateNode.value, bestChild.value);
					   bestChild = generatedChild;
				   }
				   if(stateNode.value >= beta) {
					 //prune the other kids as this node will not be selected!
					   stateNode.valueCheck(limit-1,  Integer.MIN_VALUE);
					   return stateNode;
				   }
				   alpha = Integer.max(stateNode.value, alpha);
			}
			return bestChild;
	}
	
	public StateNode minTurn(Pawn.Color playerColor, StateNode stateNode, int alpha, int beta, int limit) {
		
		StateNode bestChild = stateNode; //was null, lets see if this fixes anything.
		HashSet<StateNode> childNodes = generateLegalMoves(stateNode, playerColor); 

		if(childNodes.isEmpty() || stateNode.state.isWinstate() != null || limit == 0) {
			stateNode.valueCheck(0, 10000);
			if (childNodes.isEmpty()) {
				stateNode.value = 0;
			}
			return stateNode;
		}
		
		stateNode.value = Integer.MAX_VALUE;
		for(StateNode childNode: childNodes) {
			   StateNode generatedChild = maxTurn(playerColor == Pawn.Color.White ? Pawn.Color.Black : Pawn.Color.White, childNode, alpha, beta, limit-1);
			   generatedChild.valueCheck(0, Integer.MAX_VALUE);
			   if(bestChild == null) {
				   //The first child we look at, let's instantiate some stuff
				   bestChild = generatedChild;
				   stateNode.value = Integer.min(stateNode.value, bestChild.value);
			   }
			   //We have run out of time for our search, and must return whatever the best option is so far to be compared with the previous best.
			    if(ElapsedTime() > (playClock -0.15))
				{
					return bestChild;
				}
			    
			   if(bestChild.value > generatedChild.value) {
				   //Is the child we're looking at better than any previous child?
				   stateNode.value = Integer.min(stateNode.value, bestChild.value);
				   bestChild = generatedChild;
			   }
			   if(stateNode.value <= alpha) {
				 //prune the other kids as this node will not be selected!
				   return stateNode; 
			   }
			   beta = Integer.max(stateNode.value, beta);
			   

		}
		return bestChild;
	}
		
	public StateNode findMove()
	{
		
		int startAlpha = Integer.MIN_VALUE;
		int startBeta = Integer.MAX_VALUE;
		
		initNode.value = Integer.MIN_VALUE;
		
		StateNode calculatedNode = initNode;
		StateNode chosenMoveNode = initNode;
		int i = 2;
		while (ElapsedTime() < (playClock -0.15))
		{
			initNode.value = Integer.MIN_VALUE;
			calculatedNode = maxTurn(agentColor, initNode, startAlpha, startBeta, i);
			if(ElapsedTime() < (playClock-0.15)) {
				//the last search could have been aborted, so we need to check that the calculatedNode is from a complete search.
				chosenMoveNode = calculatedNode;
			}
			
			if(chosenMoveNode.state.isWinstate() == agentColor) {
				//if we have found a win, we do not need to keep searching
				break;
			}

			totalExpansions = 0;
			//we want to increment i by 2, so we make sure that the bottom layer is one of our own moves.
			i += 2;
		}

		if(chosenMoveNode == initNode) {
			System.out.println("ERROR");
		}
		else {
			while(chosenMoveNode.parent != initNode) {
				//the actual node we return is a terminal state or from the bottom of the search, we need to trace our way back up.
				chosenMoveNode = chosenMoveNode.parent;
			}
		}
		return chosenMoveNode;

	}
	
	
}


