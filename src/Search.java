import java.util.ArrayList;

public class Search {
	int width;
	int height;
	int playclock;
	
	
	//just to create SOME sort of state space, I'm using an arraylist.
	ArrayList<StateNode> states;
	
	public Search()
	{
		states = new ArrayList<StateNode>();
	}
	
	//expands our statenode list by generating list of legal moves from node
	void expand()
	{
		for(StateNode s : states)
		{
			//4 cases:
			//there is an enemy, 1 up & 1 left
			//there is an enemy, 1 up & 1 right
			//move 1 up if it isn't at bounds and a friendly pawn isn't there
			//move 1 down ----||----
		}
		//Ingibergur said that we're going to want to expand all legal moves for ALL pawns. 
		//This means that our branching factor becomes insane. (but it makes sense though)
	}
}
