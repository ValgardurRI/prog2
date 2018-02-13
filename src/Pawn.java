import java.util.Objects;

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
	
	//overriding equals for contains()
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		if (!State.class.isAssignableFrom(o.getClass())) {
		    return false;
		}
		Pawn pawn = (Pawn)o;
		
		return pawn.pos == pos && pawn.color == color;
	}
	//overriding hashCode because of our coding standards.
	@Override
	public int hashCode() {
		return Objects.hash(pos.x, pos.y, color);
	}
}
