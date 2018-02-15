public class StateNode {
	State state;
	StateNode parent;
	String actionTo;
		
	public StateNode(State s, StateNode p, String a)
	{
		state = s;
		parent = p;
		actionTo = a;
	}
	
}
