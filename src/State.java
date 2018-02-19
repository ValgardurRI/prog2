import java.util.ArrayList;
import java.util.PriorityQueue;

public class State {
	public PriorityQueue<Pawn> pawns;
	public Pawn.Color myColor;
	public int myPawns;
	public int opponentPawns;
	public int width, height;
	
	//I'm thinking we could maybe copy/reconstruct the parameter arraylist in here, using = for now though
	public State(PriorityQueue<Pawn> p, int width, int height)
	{
		pawns = new PriorityQueue<Pawn>(new PawnComparator());
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
	static State generateInitialState(int width, int height, Pawn.Color color)
	{
		PriorityQueue<Pawn> initPawns = new PriorityQueue<Pawn>(new PawnComparator());
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
		initState.myColor = color;
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
    	int newY = p.pos.y + (p.color == Pawn.Color.White?1:-1);
    	Position forwardPos = new Position(p.pos.x, newY);
    	Pawn forwardPawn = new Pawn(p.color, forwardPos);
    	//is there a pawn already there?  Then we can't move there.
    	for(Pawn iterator : pawns)
    	{
    		if(iterator.pos.equals(forwardPos))
    		{
    			return null;
    		}
    	}

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
        for(int y = height; y>=1; y--)
        {
            for(int x = 1; x<=width; x++)
            {
                char nextChar = '-';
                for(Pawn p : pawns)
                {
                    if(p.pos.equals(new Position(x, y)))
                    {
                        if(p.color == Pawn.Color.White)
                        {
                            nextChar = 'W';
                        }
                        else
                        {
                            nextChar = 'B';
                        }
                    }
                }
                returnString += nextChar;
            }
            returnString += '\n';
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
    			return Integer.MAX_VALUE -1;
    		}
    		else {
    			return Integer.MIN_VALUE +1;
    		}
    	}
		return utility();
    }
    
    private int utility()
    {
    	return goalDistanceDelta()*2 + pawnDelta() + dyingIsBad()*10;
    }
    
    private int pawnDelta()
    {
    	return (myPawns - opponentPawns)*2;
    }
    
    private int goalDistanceDelta()
    {
    	int myClosest = 100;
    	int theirClosest = 100;
    	//How close is the closest pawn to the goal?
    	for(Pawn p : pawns)
    	{
    		if(p.color == myColor)
    		{
    			int distance = Math.abs((p.pos.y - (myColor == Pawn.Color.White ? height : 1)));
    			if(myClosest > distance)
    			{
    				myClosest = distance;
    			}
    		}
    		else
    		{
    			int distance = Math.abs((p.pos.y - (myColor == Pawn.Color.White ? 1 : height)));
    			if(theirClosest > distance)
    			{
    				theirClosest = distance;
    			}
    		}
    	}
    	return (int) Math.pow((theirClosest - myClosest), 3);
    }
    
    public int dyingIsBad()
    {
    	int val = 0;
    	for(Pawn p : pawns)
    	{
    		int protection = -1;
        	Position leftProtected;
        	int newXl = p.pos.x + (p.color == Pawn.Color.White?1:-1);
        	int newYl = p.pos.y + (p.color == Pawn.Color.White?1:-1);
        	leftProtected = new Position(newXl, newYl);
        	Pawn protectingl = new Pawn(p.color == Pawn.Color.White ? Pawn.Color.Black : Pawn.Color.White, leftProtected);
        	if(pawns.contains(protectingl) && isInBounds(leftProtected)) {
        		protection+=2;
        	}
        	Position rightProtected;
        	int newXr = p.pos.x + (p.color == Pawn.Color.White?1:-1);
        	int newYr = p.pos.y + (p.color == Pawn.Color.White?-1:1);
        	rightProtected = new Position(newXr, newYr);
        	Pawn protectingr = new Pawn(p.color == Pawn.Color.White ? Pawn.Color.Black : Pawn.Color.White, rightProtected);
        	if(pawns.contains(protectingr) && isInBounds(rightProtected)) {
        		protection+=2;
        	}
        	
        	protection *= canCaptureLeft(p) != null ? 2 : 1;
        	protection *= canCaptureRight(p) != null ? 2 : 1;

        	val = p.color == myColor ? val + protection : val - protection;
    	}
    	return val;
    }
}
