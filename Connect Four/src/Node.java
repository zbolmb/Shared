import java.util.ArrayList;
import java.util.List;


public class Node {

	protected Piece myPiece;
	protected ArrayList<Node> children;
	protected Integer move;
	protected Piece[][] bs;
	protected double score;
	
	// if min then score is min of children
	protected boolean min;

	
	public Node(int move, Piece myPiece) {
		this(move, new Piece[6][7], myPiece, true);
		for (int i = 0; i < bs.length; i++) {
			for (int j = 0; j < bs[0].length; j++) {
				bs[i][j] = Piece.EMPTY;
			}
		}
	}
	
	public Node(int move, Piece[][] bs, Piece myPiece, boolean min) {
		this.move = move;
		this.myPiece = myPiece;
		this.children = new ArrayList<Node>();
		this.bs = bs;
		this.score = 0;
		this.min = min;
	}

	public void setBS(int r, int c, Piece s) {
		bs[r][c] = s;
	}
	
	public String toString() {
		return score + "[" + move + "]\n";
	}
}
