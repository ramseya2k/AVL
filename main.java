import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.FileOutputStream;

public class Main
{
    public static void main(String[] args) throws FileNotFoundException
    {
        Scanner input = new Scanner(new File("input.txt"));
        Tree AVL = new Tree();
        while(input.hasNextLine())
        {
            int num = Integer.parseInt(input.nextLine());
            AVL.insert(num);
        }
        input.close();
        PrintStream out = new PrintStream(new FileOutputStream("output.txt", true), true); // writes to output file
        System.setOut(out);
        System.out.println("====================");
        System.out.println();
        System.out.println("BREADTH LEVEL TRAVERSAL");
        AVL.breadthLevelOrder();
        System.out.println("\n");
        System.out.println("====================");
        out.close();
    }
}

class Tree {
    private Node root;
    private int balance;

    public int height(Node leaf) // gets the height of the leaf
    {
        return leaf == null ? -1 : leaf.getHeight();
    }

    public int max(int a, int b) // gets the max number between a & b
    {
        return a > b ? a : b;
    }

    public Node insert(int key) // recursion method to insert a key
    {
        root = insert(root, key);
        return root;
    }

    public Node insert(Node leaf, int key) {
        if (leaf == null) // if leaf is empty, create a new leaf
        {
            leaf = new Node(key); // create the leaf
            leaf.setHeight(0); // set its height to 0
            return leaf; // return it
        } else if (key < leaf.getItem()) // if the key is less than the items key, then traverse to the left
        {
            leaf.setLeft(insert(leaf.getLeft(), key));
        } else // otherwise traverse to the right
        {
            leaf.setRight(insert(leaf.getRight(), key));
        }

        leaf = rebalance(leaf); // rebalance the tree after insertion
        return leaf;
    }

    public Node leftRotate(Node x) {
        Node y = x.getRight(); // y now becomes the top node
        Node z = y.getLeft(); // z is the bottom node

        y.setLeft(x); // connect y to x
        x.setRight(z); // connect x to y

        x.setHeight(max(height(x.getLeft()), height(x.getLeft())) + 1); // reconfigure x height before y
        y.setHeight(max(height(y.getLeft()), height(y.getLeft())) + 1); // once x height is configured, reconfigure y's height

        return y;
    }

    public Node rightRotate(Node x) {
        Node y = x.getLeft(); // y now becomes the top node
        Node z = y.getRight(); // z is the bottom node

        y.setRight(x); // connect y to x
        x.setLeft(z); // connect x to y

        x.setHeight(max(height(x.getLeft()), height(x.getLeft())) + 1); // reconfigure x height before y
        y.setHeight(max(height(y.getLeft()), height(y.getLeft())) + 1); // once x height is configured, reconfigure y's height

        return y;
    }

    public Node rebalance(Node leaf) {
        leaf.setHeight(1 + max(height(leaf.getLeft()), height(leaf.getRight()))); // gets the max number of the height between the left and the right child
        int balance = height(leaf.getLeft()) - height(leaf.getRight());

        if (balance == 2) // left leaning
        {
            if (getBalance(leaf.getLeft()) > 0) // if the balance of the left child is positive do a single rotation
            {
                leaf = rightRotate(leaf);
                return leaf;
            } else // otherwise do a double rotation on the leaf
            {
                leaf.setLeft(leftRotate(leaf.getLeft())); // do a left rotate on the left child on leaf
                leaf = rightRotate(leaf); // then do a right rotate on the entire leaf and return it
                return leaf;
            }
        } else if (balance == -2) // right leaning
        {
            if (getBalance(leaf.getRight()) < 0) // if the balance of the right child is negative do a single rotation
            {
                leaf = leftRotate(leaf);
                return leaf;
            } else // otherwise do a double rotation if its positive
            {
                leaf.setRight(rightRotate(leaf.getRight()));
                leaf = leftRotate(leaf);
                return leaf;
            }
        } else // return the leaf if theres no change
        {
            return leaf;
        }
    }

    public int getBalance(Node leaf) // gets balance between the 2 nodes
    {
        return height(leaf.getLeft()) - height(leaf.getRight());
    }

    public int heightTree(Node leaf) // gets max height of the tree
    {
        if (leaf == null) {
            return 0;
        } else {
            int left = heightTree(leaf.getLeft());
            int right = heightTree(leaf.getRight());

            if (right > left) {
                return right + 1;
            } else {
                return left + 1;
            }
        }
    }

    public void breadthLevel(Node leaf, int level) // breadth level order
    {
        if (leaf == null) {
            return;
        } else if (level == 1) {
            System.out.print(leaf.getItem() + " (" + leaf.getHeight() + ", " + getBalance(leaf) + ")," + " ");
        } else {
            breadthLevel(leaf.getLeft(), level - 1);
            breadthLevel(leaf.getRight(), level - 1);
        }
    }

    public void breadthLevelOrder() // displas
    {
        int height = heightTree(root);

        for (int i = 0; i <= height; i++) {
            breadthLevel(root, i);
        }
    }
}

class Node
{
    private Node left;
    private Node right;
    private int item;
    private int height;

    public Node(int item)
    {
        this.item = item;
    }

    public int getItem()
    {
        return this.item;
    }

    public Node getLeft()
    {
        return this.left;
    }

    public Node getRight()
    {
        return this.right;
    }

    public void setLeft(Node left)
    {
        this.left = left;
    }

    public void setRight(Node right)
    {
        this.right = right;
    }

    public int getHeight()
    {
        return this.height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }
}
