import javafx.scene.paint.Color;


public enum Piece {
	RED, YELLOW, EMPTY;
	
	public Piece opPiece() {
		if (this == RED) return YELLOW;
		if (this == YELLOW) return RED;
		return EMPTY;
	}
	public Color getColor() {
		if (this == RED) return Color.RED;
		if (this == YELLOW) return Color.YELLOW;
		return null;
	}
	
	public String toString() {
		if (this == RED) return"R";
		if (this == YELLOW) return "Y";
		return "O";
	}
}
