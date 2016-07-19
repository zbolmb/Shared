import java.util.ArrayList;
import java.util.Random;

public class AIPlayerMonteCarlo {

	private Node head;
	private int gamesRun = 1000;
	private int depth = 1;
	private static Piece[][] board;
	private Piece color;

	public AIPlayerMonteCarlo() {
		this(new Piece[6][7], Piece.RED);
	}

	public AIPlayerMonteCarlo(Piece color) {
		this(new Piece[6][7], color);
	}

	public AIPlayerMonteCarlo(Piece[][] board, Piece color) {
		this.board = board;
		this.color = color;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = Piece.EMPTY;
			}
		}
		head = new Node(-1, color);
	}

	/**
	 * gets the next best move
	 * @return int[2] array with row, col
	 */
	public int move() {
		int m = monteCarlo();
		//System.out.println(" : " + m + " : ");
		return m;
	}

	public int moveG() {
		int m = runGames(2000);
		return m;
	}

	/** 
	 * places a piece at col a
	 * @param a the column to drop the piece in
	 */
	public boolean move(int a, Piece p) {
		return move(a, p, board);
	}

	public boolean move(int a, Piece p, Piece[][] bs) {
		for (int i = bs.length - 1; i >= 0; i--) {
			if (bs[i][a] == Piece.EMPTY) {
				bs[i][a] = p;
				return true;
			}
		}
		return false;
	}

	public int monteCarlo() {
		generateTree();
		//t.print();
		int maxI = 0;
		Node n;
		for (int i = 0; i < head.children.size(); i++) {
			n = head.children.get(i);
			calcScore(n);
			if (n.score > head.children.get(maxI).score) maxI = i;
		}
		/**
		 * 
		 */
		for (int i = 0; i < head.children.size(); i++) {
			//	System.out.println(head.children.get(i).move + " : " + head.children.get(i).score * 100 + "%");
		}
		/**
		 * 
		 */
		//System.out.println(head.children.get(maxI).move);
		return head.children.get(maxI).move;

	}

	public void generateTree() {
		head = new Node(-1, color);
		generateTree(board, head, depth, color, true);
	}

	public void generateTree(Piece[][] bs, Node n
			, int depth, Piece p, boolean min) {
		if (depth == 0) {
			Piece cond;
			for (int i = 0; i < gamesRun; i++) {
				cond = runGame(copy(bs), p);
				if (cond == color) n.score += 1;
			}
			//t.print();
			n.score /= gamesRun;
			return;
		}
		ArrayList<Integer> moves = generateMoves(bs);
		for (Integer m : moves) {
			Node temp = new Node(m, copy(bs), p, min);
			move(m, p, temp.bs);
			n.children.add(temp);
			generateTree(temp.bs, temp, depth - 1, p, !min);
		}
	}


	public double calcScore(Node n) {
		if (n.children.size() == 0) return n.score;
		else {
			double s;
			double score;
			if (n.min) {
				s = Integer.MAX_VALUE;
				for (int i = 0; i < n.children.size(); i++) {
					score = calcScore(n.children.get(i)) ;
					if (score < s) 
						s = score;
				}
			} else {
				s = Integer.MIN_VALUE;
				for (int i = 0; i < n.children.size(); i++) {
					score = calcScore(n.children.get(i)) ;
					if (score > s) 
						s = score;
				}
			}
			//System.out.println(n.score);
			n.score = s;
			return s;
		}
	}

	//	public Piece runGame(Piece[][] bs, Piece turn){
	//		Piece[][] b = copy(bs);
	//		Random rand = new Random();
	//		ArrayList<Integer> moves = generateMoves(b);
	//		if (moves.size() == 0) return Piece.EMPTY;
	//		move(moves.get(rand.nextInt(moves.size())), turn, b);
	//		if (hasWon(turn, b)) return turn;
	//		return runGame(b, turn.opPiece());
	//	}

	public Piece runGame(Piece[][] bs, Piece turn) {
		Piece[][] b = copy(bs);
		Random rand = new Random();
		ArrayList<Integer> moves = generateMoves(b);
		if (moves.size() == 0) {
			if (hasWon(color, b)) return color;
			else if (hasWon(color.opPiece(), b)) return color.opPiece();
			else return Piece.EMPTY;
		}
		Piece current = turn;
		do {
			moves = generateMoves(b);
			if (moves.size() != 0) {
				move(moves.get(rand.nextInt(moves.size())), current, b);
				current = current.opPiece();			
			}
		} while (moves.size() != 0);
		if (hasWon(color, b)) {
			return color;
		}
		else if (hasWon(color.opPiece(), b)) {
			return color.opPiece();
		}
		else {
			return Piece.EMPTY;
		}
	}

	public int runGames(int numGames) {
		ArrayList<Integer> moves = generateMoves(board);
		int opMove = 0;
		int maxWins = 0;
		Piece[][] tmp;
		int count = 0;
		for (int m : moves) {
			tmp = copy(board);
			move(m, color, tmp);
			for (int i = 0; i < numGames; i++) {
				if (runGame(tmp, color.opPiece()) == color) count++;
			}
			if (count > maxWins) {
				maxWins = count;
				opMove = m;
			}
			//System.out.println(((double)count / numGames) * 100 + "%");
			count = 0;
		}
		return opMove;
	}

	public ArrayList<Integer> generateMoves() {
		return generateMoves(board);
	}

	public ArrayList<Integer> generateMoves(Piece[][] b) {
		ArrayList<Integer> moves = new ArrayList<>();
		if (hasWon(color, b) || hasWon(color.opPiece(), b)) return moves;
		for (int i = 0; i < 7; i++) {
			if (b[0][i] == Piece.EMPTY) moves.add(i);
		}
		return moves;
	}

	public static boolean hasWon(Piece p) {
		return hasWon(p, board);
	}

	public static boolean hasWon(Piece p, Piece[][] b) {
		for (int i = 0; i < b.length; i++) {
			for (int j = 0; j < b[0].length; j++) {
				if (check(i, j, p, b)) {
					//System.out.println(i + " : " + j + " " + check(i, j, p, b));
					return true;
				}
			}
		}
		return false;
	}

	public static boolean check(int a, int b, Piece p, Piece[][] bs) {
		int v = 1; // |
		int h = 1; // _
		int d1 = 1; // /
		int d2 = 1; // \

		if (bs[a][b] != p) return false;

		int i;
		int ii;

		// check |
		for (i = a + 1; i < bs.length && bs[i][b] == p; i++) v++;
		for (i = a - 1; i >= 0 && bs[i][b] == p; i--) v++;

		if (v >= 4) {
			//System.out.println("V");
			return true;
		}

		// check _
		for (i = b + 1; i < bs[0].length && bs[a][i] == p; i++) h++;
		for (i = b - 1; i >= 0 && bs[a][i] == p; i--) h++;

		if (h >= 4) {
			//System.out.println("H");
			return true;
		}

		// check /
		for (i = a - 1, ii = b + 1; i >= 0 && ii < bs[0].length && bs[i][ii] == p; i--, ii++) d1++;
		for (i = a + 1, ii = b - 1; i < bs.length && ii >= 0 && bs[i][ii] == p; i++, ii--) d1++;

		if (d1 >= 4) {
			//System.out.println("D1");
			return true;
		}

		// check \
		for (i = a - 1, ii = b - 1; i >= 0 && ii >= 0 && bs[i][ii] == p; i--, ii--) d2++;
		for (i = a + 1, ii = b + 1; i < bs.length && ii < bs[0].length && bs[i][ii] == p; i++, ii++) d2++;

		if (d2 >= 4) {
			//System.out.println("D2");
			return true;
		}

		return false;

	}

	public Piece[][] copy(Piece[][] c) {
		Piece[][] copy = new Piece[c.length][c[0].length];
		for (int i = 0; i < c.length; i++) {
			for (int j = 0; j < c[0].length; j++) {
				copy[i][j] = c[i][j];
			}
		}
		return copy;
	}

	public static void print(Piece [][] b) {
		for (int i = 0; i < b.length; i++) {
			System.out.print("[ ");
			for (int j = 0; j < b[0].length; j++) {
				System.out.print(b[i][j] + " ");
			}
			System.out.print("]\n");
		}
	}


	public Node getHead() {
		return head;
	}

	public void setHead(Node head) {
		this.head = head;
	}

	public int getGamesRun() {
		return gamesRun;
	}

	public void setGamesRun(int gamesRun) {
		this.gamesRun = gamesRun;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public Piece[][] getBoard() {
		return board;
	}

	public void setBoard(Piece[][] board) {
		this.board = board;
	}

	public Piece getColor() {
		return color;
	}

	public void setColor(Piece color) {
		this.color = color;
	}

	public static void main(String[] args) {
		Piece[][] board = new Piece[6][7];
		AIPlayerMonteCarlo a = new AIPlayerMonteCarlo(board, Piece.RED);
		AIPlayerMonteCarlo b = new AIPlayerMonteCarlo(board, Piece.YELLOW);

		for (int i = 0; i < 6; i++) {
			board[0][i] = Piece.RED;
		}
		System.out.println(a.generateMoves(board));
		print(a.board);

		//		a.board[5][3] = Piece.RED;
		//		a.board[5][2] = Piece.RED;
		//		a.board[4][3] = Piece.YELLOW;
		//		a.board[5][4] = Piece.YELLOW;
		//		a.board[5][1] = Piece.RED;
		//		print(a.board);
		//		System.out.println(a.runGame(board, a.color));
		////		//System.out.println(a.check(5, 3, Piece.YELLOW, a.board));
		////		//a.hasWon(Piece.YELLOW, a.board);
		////		//System.out.println(a.hasWon(Piece.YELLOW, a.board));
		//		print(a.board);
		//		b.move(b.move(), b.color);
		//		System.out.println();
		//		//a.runGame(a.board, a.color);
		//		print(a.board);
		//		a.move(a.move(), a.color);
		//		System.out.println();
		//		print(a.board);
		//		System.out.println(a.generateMoves().size());
		//		
		//		Scanner in = new Scanner(System.in);
		//		boolean turn = true;
		//		while (!a.hasWon(a.color, board)
		//				&& !b.hasWon(b.color, board)
		//				&& a.generateMoves(board).size() != 0) {
		//			if (turn) {
		//				a.move(a.move(), a.color);
		//				print(a.board);
		//				System.out.println();
		//				turn = !turn;
		//			} else {
		//				b.move(b.move(), b.color);
		//				print(b.board);
		//				System.out.println();
		//				turn = !turn;
		//			}
		//			//			System.out.println("Continue?");
		//			//			if (in.nextLine().equals("n")) break;
		//		}
		//		System.out.println(a.hasWon(a.color, board));
		//		System.out.println(b.hasWon(b.color, board));
		//		System.out.println(a.generateMoves(board).size() != 0);
	}

}
