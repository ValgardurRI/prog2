
public class Pawn {
	public enum Color
	{
		Black,
		White
	};
	
	
	public Color color;
	public Position pos;
	public Pawn(Color c, Position p)
	{
		color = c;
		pos = p;
	}
}
