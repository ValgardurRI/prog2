import java.util.Objects;

public class Position {
	public int x;
	public int y;
	
	public Position() {}
	
	public Position(int xVal, int yVal) {
		x = xVal;
		y = yVal;
	}
	
	
	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}

	//overriding equals so that it doesn't just check if object addresses are the same
	@Override
	public boolean equals(Object o) {
		if(o == null) {
			return false;
		}
		if (!Position.class.isAssignableFrom(o.getClass())) {
		    return false;
		}
		Position pos = (Position)o;
		return (this.x == pos.x) && (this.y == pos.y);
	}
	//also overriding hashCode because of coding standards
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
}
