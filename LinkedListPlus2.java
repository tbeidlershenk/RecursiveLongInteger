// CS 0445 Spring 2022
// LinkedListPlus<T> class partial implementation

// See the commented methods below.  You must complete this class by
// filling in the method bodies for the remaining methods.  Note that you
// may NOT add any new instance variables, but you may use method variables
// as needed.

public class LinkedListPlus2<T> extends A2LList<T>
{
	private int loc;
	// Default constructor simply calls super()
	public LinkedListPlus2()
	{
		super();
	}
	
	// copy constructor, calls both copyList and setPrev recursive methods
	public LinkedListPlus2(LinkedListPlus2<T> oldList)
	{
		super();
		if (oldList.getLength() > 0) {
			this.numberOfEntries = oldList.getLength();
			this.firstNode = new Node(oldList.firstNode.data);
			copyList(this.firstNode, oldList.firstNode.next, 0);
			setPrev(this.firstNode, this.firstNode.next, 0);
		}		
	}

	// Recursive method for copy constructor (creates nodes, sets nexts)
	private void copyList (Node newCurr, Node oldCurr, int count) {
		if (count == this.numberOfEntries-1) {
			newCurr.next = this.firstNode;
		} else {
			newCurr.next = new Node(oldCurr.data);
			copyList(newCurr.next, oldCurr.next, count+=1);
		}
	}

	// Recursive method for copy constructor (sets prevs)
	private void setPrev (Node curr, Node next, int count) {
		if (count == this.numberOfEntries-1) {
			firstNode.prev = curr;
		} else {
			next.prev = curr;
			setPrev(curr.next, next.next, count+=1);
		}
	}
	
	// toString (stub method)
	public String toString() {
		return recursiveToString(this.firstNode, "", 0);
	}
	
	// toString (recursive method)
	private String recursiveToString (Node curr, String s, int count) {
		if (count == this.numberOfEntries) {
			return s;
		} else {
			s += curr.data.toString() + " ";
			return recursiveToString(curr.next, s, count+=1);
		}
	}
	
	// leftShift (stub method)
	public void leftShift(int num) {
		if (num >= this.numberOfEntries) 
			clear();
		else if (num > 0) 
			recursiveLeftShift(0, num, this.firstNode);		
	}

	// leftShift (recursive method)
	private void recursiveLeftShift (int count, int num, Node curr) {
		if (count == this.numberOfEntries-1) {
			this.numberOfEntries -= num;
			curr.next = this.firstNode;
			this.firstNode.prev = curr;
		} else if (count <= num) {
			this.firstNode = curr;
			recursiveLeftShift(count+=1, num, curr.next);
		} else {
			recursiveLeftShift(count+=1, num, curr.next);
		}
	}

	// rightShift (stub method)
	public void rightShift(int num) {
		if (num >= this.numberOfEntries) 
			clear();
		else if (num > 0) 
			recursiveRightShift(0, num, this.firstNode);	
	}

	// rightShift (recursive method)
	private void recursiveRightShift (int count, int num, Node curr) {
		if (count == num+1) {
			this.numberOfEntries -= num;
			curr.next = this.firstNode;
			this.firstNode.prev = curr;
		} else if (count <= num) {
			recursiveRightShift(count+=1, num, curr.prev);
		}
	}

	// leftRotate (stub method)
	public void leftRotate(int num) {
		int rot = Math.abs(num % this.numberOfEntries);
		if (num > 0)
			recursiveRotate(0, rot, this.firstNode, true);
		else
			recursiveRotate(0, rot, this.firstNode, false);
	}

	// rightRotate (stub method)
	public void rightRotate(int num) {
		int rot = Math.abs(num % this.numberOfEntries);
		if (num > 0)
			recursiveRotate(0, rot, this.firstNode, false);
		else
			recursiveRotate(0, rot, this.firstNode, true);

	}

	// rotate (recursive method for both left and right rotate)
	private void recursiveRotate (int count, int rot, Node curr, boolean isLeft) {
		if (count == rot)
			this.firstNode = curr;
		else if (isLeft)
			recursiveRotate(count+=1, rot, curr.next, isLeft);
		else 
			recursiveRotate(count+=1, rot, curr.prev, isLeft);
	}
}
