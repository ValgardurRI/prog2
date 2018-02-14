import java.util.ArrayList;

public class State {
	public ArrayList<Pawn> pawns;
	public boolean myTurn; //transposition table what? waldo?
	public int myPawns;
	public int opponentPawns;
	public int width, height;
	
	//I'm thinking we could maybe copy/reconstruct the parameter arraylist in here, using = for now though
	public State(ArrayList<Pawn> p, int width, int height)
	{
		pawns = p;
		this.width = width;
		this.height = height;
	}
	
	public State(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	
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
		
		State initState = new State(width, height);
		initState.pawns = initPawns;
		return initState;
	}
	
	
	//returns the pawn that it can capture, otherwise null.
    public Pawn canCaptureLeft(Pawn p) {
    	Position leftCapture;
    	int newX = p.pos.x + (p.color == Pawn.Color.White?-1:1);
    	int newY = p.pos.y + (p.color == Pawn.Color.White?1:-1);
    	leftCapture = new Position(newX, newY);
    	Pawn captured = new Pawn(p.color == Pawn.Color.White?Pawn.Color.Black:Pawn.Color.White, leftCapture);
    	if(pawns.contains(captured) && isInBounds(leftCapture)) {
    		return captured;
    	}
    	return null;
    }
    
    
	//returns the pawn that it can capture, otherwise null.
    public Pawn canCaptureRight(Pawn p) {
    	
    	Position rightCapture;
    	int newX = p.pos.x + (p.color == Pawn.Color.White?1:-1);
    	int newY = p.pos.y + (p.color == Pawn.Color.White?1:-1);
    	rightCapture = new Position(newX, newY);
    	Pawn captured = new Pawn(p.color == Pawn.Color.White?Pawn.Color.Black:Pawn.Color.White, rightCapture);
    	if(pawns.contains(captured) && isInBounds(rightCapture)) {
    		return captured;
    	}
    	return null;
    }
    
    
    //returns the new position, otherwise null.
    public Pawn canGoForward(Pawn p) {
    	Position forward;
    	int newY = p.pos.y + (p.color == Pawn.Color.White?1:-1);
    	Position forwardPos = new Position(p.pos.x, newY);
    	Pawn forwardPawn = new Pawn(p.color, forwardPos);
    	//is there a pawn already there?  Then we can't move there.
    	Pawn blockingPawn = new Pawn(p.color == Pawn.Color.White?Pawn.Color.Black:Pawn.Color.White, forwardPos);
    	//we do not need to check for bounds, because if a pawn is facing the edge on the opponents side
    	//we have reached a terminal state and will not expand that state.
    	if(!pawns.contains(blockingPawn)) {
    		return forwardPawn;
    	}
    	return null;    	   
    }
    
    
    private boolean isInBounds(Position p) {
    	if (p.x > width || p.y > height) {
    		return false;
    	}
    	return true;
    }
    
}
