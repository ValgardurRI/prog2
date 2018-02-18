import java.util.HashSet;


public class Search {
	int width;
	int height;
	int playclock;
	Pawn.Color agentColor;
	StateNode initNode;
	int playClock;
	StateNode bestValueNode;
	private int totalExpansions; 
	
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
				stateNode.valueCheck(0, Integer.MIN_VALUE);
				if (childNodes.isEmpty()) {
					stateNode.value = 0;
				}
				return stateNode;
			}
			
			stateNode.value = Integer.MIN_VALUE;
			for(StateNode childNode: childNodes) {
				   StateNode generatedChild = minTurn(playerColor == Pawn.Color.White ? Pawn.Color.Black : Pawn.Color.White, childNode, alpha, beta, limit - 1);
				   generatedChild.valueCheck(limit, stateNode.value);

				   if(bestChild == null) {
					   //The first child we look at, let's instantiate some stuff!
					   bestChild = generatedChild;
					   stateNode.value = Integer.max(stateNode.value, bestChild.value);
				   }
				   //watching the clock
				    if(ElapsedTime() > (playClock -1))
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
			stateNode.valueCheck(0, Integer.MAX_VALUE);
			if (childNodes.isEmpty()) {
				stateNode.value = 0;
			}
			return stateNode;
		}
		
		stateNode.value = Integer.MAX_VALUE;
		for(StateNode childNode: childNodes) {
			   StateNode generatedChild = maxTurn(playerColor == Pawn.Color.White ? Pawn.Color.Black : Pawn.Color.White, childNode, alpha, beta, limit-1);
			   generatedChild.valueCheck(limit, stateNode.value);
			   if(bestChild == null) {
				   //The first child we look at, let's instantiate some stuff
				   bestChild = generatedChild;
				   stateNode.value = Integer.min(stateNode.value, bestChild.value);
			   }
			   //We have run out of time for our search, and must return whatever the best option is so far to be compared with the previous best.
			    if(ElapsedTime() > (playClock -1))
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
	
	//should be deleted eventually, testing purposes
	
	public StateNode testMove()
	{
		
		int startAlpha = Integer.MIN_VALUE;
		int startBeta = Integer.MAX_VALUE;
		
		//TEMP
		initNode.value = Integer.MIN_VALUE;
		
		StateNode runningBoy = initNode;
		StateNode winBoy = initNode;
		int i = 2;
		while (ElapsedTime() < (playClock -1))
		{
			initNode.value = Integer.MIN_VALUE;
			runningBoy = maxTurn(agentColor, initNode, startAlpha, startBeta, i);
			if(ElapsedTime() > (playClock-1)) {
				System.out.println("Timeout at layer " + i);
			}
			else {
				System.out.print(".");
				winBoy = runningBoy;
			}
			if(winBoy.state.isWinstate() == agentColor) 
				break; //This will stop him from going much too far.
			
			System.out.println("State expansions for a search of depth " + i + ": " + totalExpansions);
			totalExpansions = 0;
			i += 2;
		}
		
		System.out.println("chosen value: " + winBoy.value);
		if(winBoy == initNode) {
			System.out.println("god shit howdy fuck my lads");
		}
		else {
			while(winBoy.parent != initNode) {
				System.out.println(winBoy.state);
				winBoy = winBoy.parent;
			}
		}
		
		
		System.out.println(winBoy.state);
		System.out.println("time to compute: " + ElapsedTime());
    	System.out.println("dying is bad: " + winBoy.state.dyingIsBad());
		return winBoy;

	}
	
	
}


