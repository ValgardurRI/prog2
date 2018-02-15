import java.util.ArrayList;

public class State {
	public ArrayList<Pawn> pawns;
	public Pawn.Color myColor;
	public int myPawns;
	public int opponentPawns;
	public int width, height;
	
	//I'm thinking we could maybe copy/reconstruct the parameter arraylist in here, using = for now though
	public State(ArrayList<Pawn> p, int width, int height)
	{
		pawns = new ArrayList<Pawn>();
		for(Pawn pawn : p)
		{
			pawns.add(pawn);
		}
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
				initPawns.add(new Pawn(Pawn.Color.White, new Position(x+1,y+1)));
			}
		}
		
		for(int y = height-2; y<height; y++)
		{
			for(int x = 0; x<width; x++)
			{
				initPawns.add(new Pawn(Pawn.Color.Black, new Position(x+1,y+1)));
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
    	//Pawn blockingPawn = new Pawn(p.color == Pawn.Color.White ? Pawn.Color.Black : Pawn.Color.White, forwardPos);
    	for(Pawn iterator : pawns)
    	{
    		if(iterator.pos.equals(forwardPos))
    		{
    			return null;
    		}
    	}
    	//we do not need to check for bounds, because if a pawn is facing the edge on the opponents side
    	//we have reached a terminal state and will not expand that state.
    	//if(!pawns.contains(blockingPawn)) {
    	//	return forwardPawn;
    	//}
    	return forwardPawn;    	   
    }
    
    
    private boolean isInBounds(Position p) {
    	if (p.x > width || p.y > height || p.x <= 0 || p.y <= 0) {
    		return false;
    	}
    	return true;
    }
    
    @Override
	public String toString() {
    	String returnString = "";
    	for(Pawn p : pawns)
    	{
    		returnString += (p + "\n");
    	}
		return returnString;
	}
    
    public Pawn.Color isWinstate() {
    	//has someone won?
    	
    	for(Pawn p:pawns) {
    		if(p.pos.y == 1 && p.color == Pawn.Color.Black) {
    			return Pawn.Color.Black;
    		}
    		if(p.pos.y == height && p.color == Pawn.Color.White) {
    			return Pawn.Color.White;
    		}
    	}
    	
    	return null;
    }
    
    public int value() {
    	Pawn.Color winStatus = isWinstate();
    	
    	if(winStatus!= null) {
    		if(winStatus == myColor) {
    			return 100;
    		}
    		else {
    			return -100;
    		}
    	}
		return myPawns-opponentPawns;
    }
}
