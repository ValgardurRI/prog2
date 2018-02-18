import java.util.Comparator;

public class PawnComparator implements Comparator<Pawn>{
	@Override
	public int compare(Pawn arg0, Pawn arg1) {
		if(arg0.color != arg1.color) {
			return 0; //we do not really care about the ordering of nonmatching pairs of pawns.
		}
		else if(arg0.color == Pawn.Color.White) {
			return arg1.pos.y - arg0.pos.y;
		}
		else {
			return arg0.pos.y - arg1.pos.y;
		}
	}
}
