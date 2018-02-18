public class StateNode {
	State state;
	StateNode parent;
	String actionTo;
	int value;
		
	public StateNode(State s, StateNode p, String a)
	{
		state = s;
		parent = p;
		actionTo = a;
		value = 0;
	}
	
	public void valueCheck(int depthRemaining, int extremeValue) {
		
		//TODO; does this properly account for draw states?
		if(depthRemaining == 0 || state.isWinstate() != null) {
			//only if we are at a terminal state or have reached the max depth for this iteration do we want the node to have any value to propagate back up.
			value = state.value();
		}
		else {
			value = extremeValue;
		}
	}
	
}
