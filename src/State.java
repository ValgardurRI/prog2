import java.util.ArrayList;

public class State {
	public ArrayList<Pawn> pawns;
	public boolean myTurn; //transposition table what? waldo?
	public int myPawns;
	public int opponentPawns;
	
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
	//returns the pawn that it can capture, otherwise null.
    public Pawn canCaptureLeft(Pawn p) {
    	
    	return null;
    }
	//returns the pawn that it can capture, otherwise null.
    public Pawn canCaptureRight(Pawn p) {
    	
    	return null;
    }
    //returns the new position, otherwise null.
    public Position canGoForward(Pawn p) {
    
    	return null; //might want to return bool isntead of null?  We'll seeeee
    }
    //returns the new position, otherwise null.
    public Position canGoBackwards(Pawn p) {
    	return null;
    }
    
}
