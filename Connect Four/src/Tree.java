import java.util.LinkedList;
import java.util.Queue;


public class Tree {

	protected Node head;
	
	public Tree(Piece myPiece) {
		this.head = new Node(-1, myPiece);
	}
	
	public void print() {
		Queue<Node> q = new LinkedList<>();
		q.add(head);
		Node node;
		while (!q.isEmpty()) {
			node = q.remove();
			System.out.print(node);
			for (Node n : node.children) {
				q.add(n);
			}
		}
	}
}
