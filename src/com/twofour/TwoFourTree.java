package com.twofour;

public class TwoFourTree {
    private class TwoFourTreeItem {
        int values = 1;                             // Number of values in a TreeItem
        int value1 = 0;                             // always exists.
        int value2 = 0;                             // exists iff the node is a 3-node or 4-node.
        int value3 = 0;                             // exists iff the node is a 4-node.
        boolean isLeaf = true;
        
        TwoFourTreeItem parent = null;              // parent exists iff the node is not root.
        TwoFourTreeItem leftChild = null;           // left and right child exist iff the note is a non-leaf.
        TwoFourTreeItem rightChild = null;          
        TwoFourTreeItem centerChild = null;         // center child exists iff the node is a non-leaf 3-node.
        TwoFourTreeItem centerLeftChild = null;     // center-left and center-right children exist iff the node is a non-leaf 4-node.
        TwoFourTreeItem centerRightChild = null;

        // TwoNode has 2 children, 2 value
        public boolean isTwoNode() {
            if (this.values == 1)
                return true;
            return false;
        }

        // ThreeNode has 3 children, 2 values
        public boolean isThreeNode() {
            if (this.values == 2)
                return true;
            return false;
        }

        // FourNode has 4 children, 3 values
        public boolean isFourNode() {
            if (this.values == 3)
                return true;
            return false;
        }

        // Root is fatherless
        public boolean isRoot() {
            if (this.parent == null)
                return true;
            return false;
        }
        
        // Constructs a TwoNode (1 value, 2 children)
        public TwoFourTreeItem(int value1) {
            this.value1 = value1;
        }

        // Constructs a ThreeNode (2 values, 3 children)
        public TwoFourTreeItem(int value1, int value2) {
            this.value1 = value1;
            this.value2 = value2;
            // There are 2 values in this constructed node
            this.values = 2;
        }

        // Constructs a FourNode (3 values, 4 children)
        public TwoFourTreeItem(int value1, int value2, int value3) {
            this.value1 = value1;
            this.value2 = value2;
            this.value3 = value3;
            // There are 3 values in this constructed node
            this.values = 3;
        }

        private void printIndents(int indent) {
            for(int i = 0; i < indent; i++) System.out.printf("  ");
        }

        public void printInOrder(int indent) {
            if(!isLeaf) leftChild.printInOrder(indent + 1);
            printIndents(indent);
            System.out.printf("%d\n", value1);
            if(isThreeNode()) {
                if(!isLeaf) centerChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
            } else if(isFourNode()) {
                if(!isLeaf) centerLeftChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value2);
                if(!isLeaf) centerRightChild.printInOrder(indent + 1);
                printIndents(indent);
                System.out.printf("%d\n", value3);
            }
            if(!isLeaf) rightChild.printInOrder(indent + 1);
        }
    }
    //End of Private Subclass

    //Attribute of Main Tree Class
    TwoFourTreeItem root = null;

    // What needs to be worked on

    // Returns true if the value is already there, if not, it returns false and adds the parameter into the Tree
    public boolean addValue(int value) {
        // Checks if the Tree is empty. Creates a new Tree if so
        if (root == null) {
            root = new TwoFourTreeItem(value);
            return false;
        }

        // Root is the last node in the Tree
        if (root.isLeaf) {
            // Handle the root insertion separately
            if (root.isTwoNode()) {
                // Insert value into the 2-node root
                if (value < root.value1) {
                    root.value2 = root.value1;
                    root.value1 = value;
                } else {
                    root.value2 = value;
                }
                root.values++;
            } else if (root.isThreeNode()) {
                // Insert value into the 3-node root
                if (value < root.value1) {
                    root.value3 = root.value2;
                    root.value2 = root.value1;
                    root.value1 = value;
                } else if (value < root.value2) {
                    root.value3 = root.value2;
                    root.value2 = value;
                } else {
                    root.value3 = value;
                }
                root.values++;
                // Split the root since it becomes a 4-node
                splitTree(root);
            }
            return false;
        }

        // Tree is not empty and root is not a leaf, so we need to traverse
        TwoFourTreeItem tmp = root;

        while (!tmp.isLeaf) {
            // Split if we encounter a 4-node during traversal
            if (tmp.isFourNode()) {
                splitTree(tmp);
                tmp = tmp.parent; // After splitting, move back to parent
            }

            // Normal traversal based on value comparison
            if (tmp.isTwoNode()) {
                if (value < tmp.value1) tmp = tmp.leftChild;
                else tmp = tmp.rightChild;
            } else if (tmp.isThreeNode()) {
                if (value < tmp.value1) tmp = tmp.leftChild;
                else if (value < tmp.value2) tmp = tmp.centerChild;
                else tmp = tmp.rightChild;
            }
        }

        // We've reached a leaf, now insert the value
        if (tmp.isTwoNode()) {
            if (value < tmp.value1) {
                tmp.value2 = tmp.value1;
                tmp.value1 = value;
            } else {
                tmp.value2 = value;
            }
            tmp.values++;
        } else if (tmp.isThreeNode()) {
            if (value < tmp.value1) {
                tmp.value3 = tmp.value2;
                tmp.value2 = tmp.value1;
                tmp.value1 = value;
            } else if (value < tmp.value2) {
                tmp.value3 = tmp.value2;
                tmp.value2 = value;
            } else {
                tmp.value3 = value;
            }
            tmp.values++;
            // If it becomes a 4-node, split the leaf node
            splitTree(tmp);
        }

        return false; // added the value
    }


    // Returns true if the parameter 'value' is found within the Tree, if not, returns false
    public boolean hasValue(int value) {
        // Tree is empty
        if (root == null)
            return false;
        // There is only one node in the tree
        if (root.isLeaf) {
            // Checks said single node
            if (value == root.value1 || value == root.value2 || value == root.value3)
                // value found
                return true;
            else
                // value not found
                return false;
        }

        // Tree is not empty
        // Used to traverse Tree
        TwoFourTreeItem tmp = root;

        // Traverses until you get to a leaf
        while (!tmp.isLeaf) {
            if (tmp.isTwoNode()) {
                if (value < tmp.value1)
                    tmp = tmp.leftChild;
                else
                    tmp = tmp.rightChild;
            } else if (tmp.isThreeNode()) {
                if (value < tmp.value1)
                    tmp = tmp.leftChild;
                else if (value < tmp.value2)
                    tmp = tmp.centerChild;
                else // if tmp > tmp.value2
                    tmp = tmp.rightChild;
            } else { // if tmp.isFourNode()
                if (value < tmp.value1)
                    tmp = tmp.leftChild;
                else if (value < tmp.value2)
                    tmp = tmp.centerLeftChild;
                else if (value < tmp.value3)
                    tmp = tmp.centerRightChild;
                else // value > tmp.value3
                    tmp = tmp.rightChild;
            }
        }

        // You are now in a leaf node
        // Checks said leaf node
        if (value == tmp.value1 || value == tmp.value2 || value == tmp.value3)
            return true;

        // Value could not be found anywhere in the entire tree
        return false;
    }

    // Returns true if the value is not in the Tree, else, returns false and deletes the parameter 'value'
    public boolean deleteValue(int value) {
        return false;
    }

    public void printInOrder() {
        if(root != null) root.printInOrder(0);
    }


    // Used to split a FourNode. Returns false if not a FourNode.
    private boolean splitTree(TwoFourTreeItem node) {
        if (!node.isFourNode())
            return false;

        if (node.isRoot()) {
            TwoFourTreeItem newRoot = new TwoFourTreeItem(node.value2); // Create new root
            newRoot.leftChild = new TwoFourTreeItem(node.value1);       // Left child gets smallest value
            newRoot.rightChild = new TwoFourTreeItem(node.value3);      // Right child gets largest value
            
            // Set children of new nodes
            newRoot.leftChild.leftChild = node.leftChild;
            newRoot.leftChild.rightChild = node.centerLeftChild;
            newRoot.rightChild.leftChild = node.centerRightChild;
            newRoot.rightChild.rightChild = node.rightChild;
            
            // Update root reference
            root = newRoot;
            return true;
        }

        TwoFourTreeItem parent = node.parent;

        // Create new left and right children from the current 4-node
        TwoFourTreeItem leftChild = new TwoFourTreeItem(node.value1);  // Node with the smallest value
        TwoFourTreeItem rightChild = new TwoFourTreeItem(node.value3); // Node with the largest value

        // Reassign children from the 4-node to the new left and right children
        leftChild.leftChild = node.leftChild;
        leftChild.rightChild = node.centerLeftChild;
        rightChild.leftChild = node.centerRightChild;
        rightChild.rightChild = node.rightChild;

        // Update children's parent pointers
        if (leftChild.leftChild != null) leftChild.leftChild.parent = leftChild;
        if (leftChild.rightChild != null) leftChild.rightChild.parent = leftChild;
        if (rightChild.leftChild != null) rightChild.leftChild.parent = rightChild;
        if (rightChild.rightChild != null) rightChild.rightChild.parent = rightChild;

        // Push the middle value (node.value2) up to the parent and adjust child pointers

        // Case 1: Node is the leftChild of its parent
        if (parent.leftChild == node) {
            if (parent.isTwoNode()) {
                parent.value2 = parent.value1;
                parent.value1 = node.value2;
                parent.centerChild = rightChild;
            } else { // Parent is a 3-node, and will become a 4-node after this
                parent.value3 = parent.value2;
                parent.value2 = parent.value1;
                parent.value1 = node.value2;
                parent.centerRightChild = parent.centerChild;
                parent.centerChild = rightChild;
            }
            parent.leftChild = leftChild;

        // Case 2: Node is the centerChild of its parent
        } else if (parent.centerChild == node) {
            parent.value3 = parent.value2;
            parent.value2 = node.value2;
            parent.centerChild = leftChild;
            parent.rightChild = rightChild;

        // Case 3: Node is the rightChild of its parent
        } else if (parent.rightChild == node) {
            if (parent.isTwoNode()) {
                parent.value2 = node.value2;
                parent.centerChild = leftChild;
                parent.rightChild = rightChild;
            } else { // Parent is a 3-node, and will become a 4-node
                parent.value3 = node.value2;
                parent.centerRightChild = leftChild;
                parent.rightChild = rightChild;
            }
        }

        // Ensure the new left and right children point to the correct parent
        leftChild.parent = parent;
        rightChild.parent = parent;

        // If the parent becomes a 4-node, it may need to be split as well
        if (parent.isFourNode()) {
            splitTree(parent); // Recursively split the parent if needed
        }

        return true; // Split successfully
    }

    
    //Constructor
    public TwoFourTree() {
        
    }
}
