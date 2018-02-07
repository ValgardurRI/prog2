import java.util.ArrayList;

public class State {
	public ArrayList<Pawn> pawns;
	//maybe add whose turn it is (transposition table thought)
	
	//It might be a good idea to keep amount of each colored pawn as 2 INTs in here for heuristic purposes.
	
	//I'm thinking we could maybe copy/reconstruct the parameter arraylist in here, using = for now though
	public State(ArrayList<Pawn> p)
	{
		pawns = p;
	}
	
	public State() {}
	
	//Creates the initial pawns for the match
	static State generateInitialState(int width, int height)
	{
		ArrayList<Pawn> initPawns = new ArrayList<Pawn>();
		for(int y = 0; y<2; y++)
		{
			for(int x = 0; x<width; x++)
			{
				initPawns.add(new Pawn(Pawn.Color.Black, new Position(x,y)));
			}
		}
		
		for(int y = height-2; y<height; y++)
		{
			for(int x = 0; x<width; x++)
			{
				initPawns.add(new Pawn(Pawn.Color.White, new Position(x,y)));
			}
		}
		
		State initState = new State();
		initState.pawns = initPawns;
		return initState;
	}
}
