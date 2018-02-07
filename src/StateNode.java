public class StateNode {
	State state;
	StateNode parent;
	String actionTo;
	//Is path cost calculation necessary here? Who knows
	
	//some heuristic thing here probably?
	
	public StateNode(State s, StateNode p)
	{
		state = s;
		parent = p;
	}
}
