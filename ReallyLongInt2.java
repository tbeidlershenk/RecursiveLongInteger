// CS 0445 Spring 2022
// This is a partial implementation of the ReallyLongInt2 class.  You need to
// complete the implementations of the remaining methods.  Also, for this class
// to work, you must complete the implementation of the LinkedListPlus class.
// See additional comments below.

public class ReallyLongInt2 extends LinkedListPlus2<Integer> 
							implements Comparable<ReallyLongInt2>
{
	// Used to create a ReallyLongInt2 of value = 0
	private ReallyLongInt2 () {
		super();
		add(0);
	}

	// constructor (parses string argument)
	public ReallyLongInt2 (String s){
		super();
		recursiveStringParse(s, 0, -1);		
	}

	// recursive method for string constructor
	private void recursiveStringParse(String s, int count, int digit) {			
		char c = s.charAt(count);
		if (('0' <= c) && (c <= '9')) {
			digit = c - '0';
			if (!(digit == 0 && this.getLength() == 0)) 
				this.add(1, Integer.valueOf(digit));
			if (count != s.length()-1) {
				recursiveStringParse(s, count+=1, digit);
			} else {
				if (digit == 0 && this.numberOfEntries == 0)
					this.add(1, Integer.valueOf(digit));
			}
		} else 
			throw new NumberFormatException("Illegal digit " + c);	
	}

	// copy constructor (only calling super)
	public ReallyLongInt2 (ReallyLongInt2 rightOp) {
		super(rightOp);
	}

	// constructor (parses long argument)
	public ReallyLongInt2 (long X) {
		super();
		recursiveLongParse(X);
	}

	// recursive method for long constructor
	private void recursiveLongParse(long X) {
		if (X / 10 == 0) {
			add((int)X);
		} else {
			long temp = (X / 10) * 10;
			recursiveLongParse(temp / 10);
			add(1, (int)(X - temp));
		}
		
	}

	// overriden toString (stub method)
	public String toString() {
		return recursiveToString(this.firstNode.prev, "", 0);
	}

	// toString (recursive method)
	private String recursiveToString (Node curr, String s, int count) {
		if (count == this.numberOfEntries) {
			return s;
		} else {
			s += curr.data.toString();
			return recursiveToString(curr.prev, s, count+=1);
		}
	}

	// add (stub method) handles special cases
	public ReallyLongInt2 add(ReallyLongInt2 rightOp) {

		ReallyLongInt2 sum;
		Node curr1, curr2;
		int smaller;
		
		// Setting sum = the larger of the two ReallyLongInts
		if (this.numberOfEntries > rightOp.numberOfEntries) {
			sum = new ReallyLongInt2(this);
			curr2 = rightOp.firstNode;
			smaller = rightOp.numberOfEntries;
		} 
		else {
			sum = new ReallyLongInt2(rightOp);
			curr2 = this.firstNode;
			smaller = this.numberOfEntries;
		}
		curr1 = sum.firstNode;

		recursiveAdd(sum, curr1, curr2, smaller, 0, 0);

		return sum;

	}

	// add (recursive method)
	private void recursiveAdd (ReallyLongInt2 sum, Node curr1, Node curr2, int smaller, int count, int carry) {
		if (count == sum.numberOfEntries) {
			if (carry == 1)
				sum.add(1); 
		} else {
			int val = carry;
			if (count < smaller)
				val += curr2.data;
			if (val + curr1.data < 10) {
				curr1.data += val;
				recursiveAdd(sum, curr1.next, curr2.next, smaller, count+=1, 0);
			} else {
				curr1.data += val - 10;
				recursiveAdd(sum, curr1.next, curr2.next, smaller, count+=1, 1);
			}
		}
	}
	
	// subtract (stub method) removes excess zeroes after subtracting
	public ReallyLongInt2 subtract(ReallyLongInt2 rightOp) {

		if (compareTo(rightOp) == -1) throw new ArithmeticException();

		ReallyLongInt2 diff = new ReallyLongInt2(this);
		Node curr1 = diff.firstNode;
		Node curr2 = rightOp.firstNode;

		recursiveSubtract (curr1, curr2, rightOp.numberOfEntries, 0);
		removeZeroes(diff, diff.firstNode.prev);
		return diff;

	}

	// subtract (recursive method) calls recursive borrow when borrowing is needed
	private void recursiveSubtract (Node curr1, Node curr2, int smaller, int count) {
		if (count == smaller)
			return;
		else {
			if (curr1.data < curr2.data)
				recursiveBorrow(curr1, curr1, curr2, smaller, 0);
			curr1.data -= curr2.data;
			recursiveSubtract(curr1.next, curr2.next, smaller, count+=1);
		}
	}

	// recursive method to borrow from larger place terms
	private void recursiveBorrow (Node initial, Node curr1, Node curr2, int smaller, int count) {
		if ((count >= smaller && curr1.data != 0) || curr1.data > curr2.data) {
			curr1.data -= 1;
			initial.data += 10;
		} else {
			recursiveBorrow(initial, curr1.next, curr2.next, smaller, count+=1);
			if (!curr1.equals(initial))
				curr1.data += 9;
		}	
	}

	// removeZeroes (recursive method) used in several methods for removing excess zeroes at logical front
	private void removeZeroes (ReallyLongInt2 diff, Node curr) {
		if (curr.data != 0 || diff.numberOfEntries == 1) {
			diff.firstNode.prev = curr;
			curr.next = diff.firstNode;
		} else {
			diff.numberOfEntries--;
			removeZeroes(diff, curr.prev);
		}
	}

	// multiply (stub method) removing excess zeroes at the end
	public ReallyLongInt2 multiply(ReallyLongInt2 rightOp) {

		Node thisCurr = this.firstNode;
		Node otherCurr = rightOp.firstNode;
		int innerSize = rightOp.numberOfEntries;
		int power = 0;

		ReallyLongInt2 product = recursiveOuterMult(innerSize, power, 0, thisCurr, otherCurr);
		removeZeroes(product, product.firstNode.prev);
		return product;
	}

	// recursive method for multiply (recurses through this ReallyLongInt2)
	private ReallyLongInt2 recursiveOuterMult (int innerSize, int power, int count, Node thisCurr, Node otherCurr) {
		if (count == this.numberOfEntries)
			return new ReallyLongInt2();
		else {
			ReallyLongInt2 subProd = recursiveInnerMult(innerSize, power, 0, thisCurr, otherCurr);
			ReallyLongInt2 prod = recursiveOuterMult(innerSize, power+=1, count+=1, thisCurr.next, otherCurr);
			prod = prod.add(subProd);
			return prod;
		}
	}

	// recursive method for multiply (recurses through rightOp)
	private ReallyLongInt2 recursiveInnerMult (int innerSize, int power, int count, Node thisCurr, Node otherCurr) {
		if (count == innerSize)
			return new ReallyLongInt2();
		else {
			long val = thisCurr.data * otherCurr.data;
			ReallyLongInt2 temp = calcPow(power, 0, val);
			ReallyLongInt2 subProd = recursiveInnerMult(innerSize, power+=1, count+=1, thisCurr, otherCurr.next);
			subProd = subProd.add(temp);
			return subProd;
		}
	}

	// recursive method for multiply (adds 0s to sub product)
	private ReallyLongInt2 calcPow (int power, int count, long val) {
		if (count == power)
			return new ReallyLongInt2(val);
		else {
			ReallyLongInt2 temp = calcPow(power, count+=1, val);
			if (!temp.toString().equals("0"));
				temp.add(1,0);
			return temp;				
		}
	}

	// compareTo (stub method) covers special cases
	public int compareTo(ReallyLongInt2 rOp) {
		if (this.numberOfEntries > rOp.numberOfEntries) return 1;
		else if (this.numberOfEntries < rOp.numberOfEntries) return -1;
		Node thisCurr = this.firstNode.prev;
		Node otherCurr = rOp.firstNode.prev;
		return recursiveCompare(0, thisCurr, otherCurr);
	}

	// compareTo (recursive method)
	private int recursiveCompare (int count, Node thisCurr, Node otherCurr) {
		if (count == this.numberOfEntries)
			return 0;
		else if (thisCurr.data > otherCurr.data) 
			return 1;
		else if (otherCurr.data > thisCurr.data)
			return -1;
		else
			return recursiveCompare(count+=1, thisCurr.prev, otherCurr.prev);
	}

	// equals (stub method) covers special cases
	public boolean equals(Object rightOp) {
		ReallyLongInt2 copy = (ReallyLongInt2) rightOp;
		if (this.numberOfEntries != copy.numberOfEntries) return false;
		Node thisCurr = this.firstNode;
		Node otherCurr = copy.firstNode;;
		return recursiveEquals(0, thisCurr, otherCurr);
	}

	// equals (recursive method)
	private boolean recursiveEquals (int count, Node thisCurr, Node otherCurr) {
		if (count == this.numberOfEntries)
			return true;
		else if (thisCurr.data != otherCurr.data) 
			return false;
		else
			return recursiveEquals(count+=1, thisCurr.next, otherCurr.next);
	}
}
