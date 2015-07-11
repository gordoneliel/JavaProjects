import java.lang.reflect.Array;
import java.util.ArrayList;

class BinarySearchTree<T extends Comparable<T>>
{

	private class BSTNode
	{
		public T data;
		public BSTNode left;
		public BSTNode right;

		BSTNode(T newdata)
		{
			data = newdata;
		}
	}

	private BSTNode root;
	int total = 0;
	
	public void Insert(T elem)
	{
		root = Insert(root, elem);
	}

	public boolean Find(T elem)
	{
		return Find(root, elem);
	}

	public void Delete(T elem)
	{
		root = Delete(root, elem);
	}

	public T ElementAt(int i)
	{
		T temp = ElementAt(root, i);
		
		return temp;
	}
	
	public T ElementAt(BSTNode tree, int i)
	{ 
//        int treeSize = Size(tree);
//   
//		if (tree == null) return null;
		
		// Base case, if the size of the left tree is same as i, that is the element we are looking for
		if(Size(tree.left) == i )
		{
			return tree.data;
		}
		if(Size(tree.left) >= i)
		{
			return ElementAt(tree.left, i);
		}
		else
		{
		    return ElementAt(tree.right, i - Size(tree.left) - 1);
		}
		//return null;
		
	}
	public int NumLarger(T elem)
	{
		// If its bigger than left or right
		int totalLarger = 0;
		// Find the element and return it
		//BSTNode tempTree = getElem(root, elem);
		
		totalLarger = NumLarger(root, elem, totalLarger);
		return totalLarger;
	}

	public int NumLarger(BSTNode tree, T elem , int counter)
	{
		if(tree.data.compareTo(elem) == 0)
		{
			return Size(tree.right) + counter;
		}
		else if(tree.data.compareTo(elem) > 0)
		{
			counter += Size(tree.right) + 1;
			System.out.println(counter);
			return NumLarger(tree.left, elem,counter);
		}
		else // If smaller
		{
			return NumLarger(tree.right, elem,counter);
		}
		
		
	}
	
	/**
	 * Finds an element<T> and returns it
	 * @param tree
	 * @param elem
	 * @return
	 */
	private BSTNode getElem(BSTNode tree, T elem)
	{
		if (tree == null)
		{
			return tree;
		}
		if (elem.compareTo(tree.data) == 0)
		{
			return tree;
		}
		if (elem.compareTo(tree.data) < 0)
		{
			return getElem(tree.left, elem);
		} else
		{
			return getElem(tree.right, elem);
		}
	}
	
	public void Balance()
	{
		ArrayList<T> treeData = new ArrayList<T>();
		printToArray(treeData, root, 0);
		System.out.println("Tree: " + treeData);

		root = 	Balance(treeData, 0, treeData.size() -1);
	}
	
	/**
	 * 
	 * @param treeData
	 * @param f
	 * @param l
	 * @return
	 */
	public BSTNode Balance(ArrayList<T> treeData, int f, int l)
	{
		if (f > l)
		{
			return null;
		}
		int middle = (f + l) / 2;
		
		BSTNode balancedTreeRoot = new BSTNode(treeData.get(middle));
		balancedTreeRoot.left = Balance(treeData, f, middle - 1);
		balancedTreeRoot.right = Balance(treeData, middle + 1, l);
		
		return balancedTreeRoot;
		
	}
	
	/**
	 * Helper function to print to array
	 * @param treeArray
	 * @param tree
	 * @param i
	 * @return
	 */
	private ArrayList<T> printToArray(ArrayList<T> treeArray, BSTNode tree, int i)
	{
		
		if (tree != null)
		{
			printToArray(treeArray, tree.left, i++);
			treeArray.add(tree.data);
			printToArray(treeArray, tree.right, i++);
		}
		return treeArray;
	}
	
	public void Print()
	{
		Print(root);
	}

	public int Height()
	{
		return Height(root);
	}

	private int Height(BSTNode tree)
	{
		if (tree == null)
		{
			return 0;
		}
		return 1 + Math.max(Height(tree.left), Height(tree.right));
	}

	private boolean Find(BSTNode tree, T elem)
	{
		if (tree == null)
		{
			return false;
		}
		if (elem.compareTo(tree.data) == 0)
		{
			return true;
		}
		if (elem.compareTo(tree.data) < 0)
		{
			return Find(tree.left, elem);
		} else
		{
			return Find(tree.right, elem);
		}
	}

	private T Minimum(BSTNode tree)
	{
		if (tree == null)
		{
			return null;
		}
		if (tree.left == null)
		{
			return tree.data;
		} else
		{
			return Minimum(tree.left);
		}
	}

	private void Print(BSTNode tree)
	{
		if (tree != null)
		{
			Print(tree.left);
			System.out.println(tree.data);
			Print(tree.right);
		}
	}

	private BSTNode Insert(BSTNode tree, T elem)
	{
		if (tree == null)
		{
			return new BSTNode(elem);
		}
		if (elem.compareTo(tree.data) < 0)
		{
			tree.left = Insert(tree.left, elem);
			return tree;
		}
		else
		{
			tree.right = Insert(tree.right, elem);
			return tree;
		}
	}

	private BSTNode Delete(BSTNode tree, T elem)
	{
		if (tree == null)
		{
			return null;
		}
		if (tree.data.compareTo(elem) == 0)
		{
			if (tree.left == null)
			{
				return tree.right;
			} else if (tree.right == null)
			{
				return tree.left;
			} else
			{
				if (tree.right.left == null)
				{
					tree.data = tree.right.data;
					tree.right = tree.right.right;
					return tree;
				} else
				{
					tree.data = RemoveSmallest(tree.right);
					return tree;
				}
			}
		} else if (elem.compareTo(tree.data) < 0)
		{
			tree.left = Delete(tree.left, elem);
			return tree;
		} else
		{
			tree.right = Delete(tree.right, elem);
			return tree;
		}
	}

	int Size(BSTNode tree)
	{
		if (tree == null)
		{
			return 0;
		}
		return 1 + Size(tree.left) + Size(tree.right);
	}

	T RemoveSmallest(BSTNode tree)
	{
		if (tree.left.left == null)
		{
			T smallest = tree.left.data;
			tree.left = tree.left.right;
			return smallest;
		} else
		{
			return RemoveSmallest(tree.left);
		}
	}

	public static void main(String args[])

	{
		BinarySearchTree<Integer> t = new BinarySearchTree<Integer>();
		for (int x = 0; x < 31; x++)
			t.Insert(new Integer(x));
		//t.Print();
		System.out.println("---------------------");
 //    	System.out.println(t.ElementAt(new Integer(5)));
		System.out.println(t.NumLarger(new Integer(8)));
//		System.out.println(t.Height());
		t.Balance();
//		System.out.println("Tree left: " + t.root.right.right.data);
//		t.Print();
		System.out.println(t.ElementAt(new Integer(5)));
		System.out.println(t.NumLarger(new Integer(8)));
//		System.out.println(t.Height());
	}

}
