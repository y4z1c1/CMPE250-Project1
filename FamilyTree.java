/**
 * FamilyTree.java - Manages a hierarchical family tree structure.
 * Allows insertion, deletion, and querying of members based on ranks and other criteria.
 * Supports complex operations like dividing the family and finding members with similar ranks.
 */

import java.util.ArrayList;
import java.util.List;

public class FamilyTree {
    private Member root; // Represents the root of the family.

    private List<String> output = new ArrayList<>(); // Stores the lines which will be written later.

    private Member replace = null; // Stores the member that replaces the removed member to give output.

    public FamilyTree() {
        this.root = null; // Creates an empty tree.
    }

    
    // Getter and setter methods
    public Member getRoot() {
        return root;
    }

    public List<String> getOutput() {
        return output;
    }

    // Returns the height of the member.
    private int height(Member N) {
        if (N == null)
            return 0;
        return N.height;
    }

    // Returns the balance factor of the member recursively.
    private int getBalance(Member N) {
        if (N == null)
            return 0;
        return height(N.left) - height(N.right);
    }

    // Updates all of the ranks in the given root's tree recursively.
    private Member updateRank(Member node, int currentRank) {
        if (node != null) {
            node.rank = currentRank;
            updateRank(node.left, currentRank + 1);
            updateRank(node.right, currentRank + 1);
        }
        return node;
    }


    // Performs a right rotation in the given member.
    private Member rightRotate(Member y) {
        Member x = y.left;
        Member T2 = x.right;

        x.right = y;
        y.left = T2;

        x.parent = y.parent;
        y.parent = x;

        if (T2 != null)
            T2.parent = y;

        // Update heights
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    // Performs a left rotation in the given member.
    private Member leftRotate(Member x) {
        Member y = x.right;
        Member T2 = y.left;

        y.left = x;
        x.right = T2;

        y.parent = x.parent;
        x.parent = y;
        if (T2 != null)
            T2.parent = x;

        // Update heights
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    // Reorganize members according to their balance factors.
    private Member rebalance(Member node) {
        if (node == null) {
            return null;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rightRotate(node);
        }

        // Left Right Case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && getBalance(node.right) <= 0) {
            return leftRotate(node);
        }

        // Right Left Case
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node; 
    }

    // Insert a new member to the tree.
    public void insert(String name, double gms) {
        // Update the root after insertion.
        root = insertMember(root, name, gms);
    }

    // Insert new member recursively, and return the updated root.
    private Member insertMember(Member node, String name, double gms) {

        if (node == null) { // Base case
            Member newMember = new Member(name, gms);
            return newMember;
        }

        // Find the place where will new member inserted.
        if (gms < node.gms) { 
            output.add(node.name + " welcomed " + name);
            node.left = insertMember(node.left, name, gms);
            if (node.left != null) {  
                node.left.parent = node;
            }

        } else if (gms > node.gms) {
            output.add(node.name + " welcomed " + name);
            node.right = insertMember(node.right, name, gms);
            if (node.right != null) {  
                node.right.parent = node;
            }
        } else {
            return node;
        }

        // Update and reorganize the tree.
        node.height = 1 + Math.max(height(node.left), height(node.right));

        node = rebalance(node);

        return node;

    }

    // Returns the member when SURNAME_NAME value is given.
    public Member findMember(String name) {
        return findMember(this.root, name);
    }

    public Member findMember(Member node, String name) {
        if (node == null || node.name.equals(name)) {
            return node;
        }
        Member leftSearch = findMember(node.left, name);
        if (leftSearch != null) {
            return leftSearch;
        }

        return findMember(node.right, name);
    }

   
    // Returns an arraylist of members that includes a path from specified member to the boss.
    public List<Member> getPathToBoss(Member node) {
        List<Member> path = new ArrayList<>();
        Member current = node;

        while (current != null) {
            path.add(current);
            current = current.parent;
        }

        return path;
    } 

    // Removes the specified member from the tree and updates the root.
    public void remove(String name, double gms) {
        replace = null; // Replace variable represents the replacing member of the deleted member.
        root = deleteMember(root, name, gms); // Update the root after removing the member.

        if (replace == null) {
            output.add(name + " left the family, replaced by nobody");
        } else {
            output.add(name + " left the family, replaced by " + replace.name);
        }

    }

    // Removes the specified member from the tree recursively. 
    private Member deleteMember(Member node, String name, double gms) {

        if (node == null) { // Base case.
            return null;
        }

        // Find the member that will be deleted.
        if (gms < node.gms) {
            node.left = deleteMember(node.left, name, gms);
        } else if (gms > node.gms) {
            node.right = deleteMember(node.right, name, gms);
        } else { 

            // Case 1: Node with only one child or no child.
            if ((node.left == null) || (node.right == null)) {
                Member temp = (node.left != null) ? node.left : node.right;

                if (temp == null) { // No child case.
                    temp = node;
                    node = null;
                    replace = null;
                } else { // One child case.
                    node = temp; // Copy the contents of the non-empty child.
                    replace = temp;
                }
            } else {
                // Case 2: Node with two children.
                // Find successor with the lowest GMS greater than the current member's GMS.
                Member successor = getLowestGMSGreaterThan(node.right, node.gms);

                // Copy the successor's data to this node and delete the successor.
                node.name = successor.name;
                node.gms = successor.gms;
                node.right = deleteMember(node.right, successor.name, successor.gms);
                replace = successor;
            }
        }

        if (node == null) {
            return null;
        }

        // Update height and rebalance the tree
        node.height = Math.max(height(node.left), height(node.right)) + 1;
        node = rebalance(node);
        return node;

    }

    // Returns the successor when deleted member has two children.
    // Sets the successor as the member that has the lowest GMS among the members that have higher gms value than deleted member.
    private Member getLowestGMSGreaterThan(Member root, double gms) {
        Member current = root;
        Member successor = null;

        while (current != null) {
            if (current.gms > gms) {
                if (successor == null || current.gms < successor.gms) {
                    successor = current;
                }
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return successor;
    }

    // Returns the member that will be targeted.
    // Takes the names of the two members as parameters.
    public void intelTarget(String name1, String name2) {
        Member target = findTarget(findMember(name1), findMember(name2));
        output.add("Target Analysis Result: " + target.name + " " + String.format("%.3f", target.gms));
    }

    // Finds the target member using their paths to the boss.
    private Member findTarget(Member m1, Member m2) {
        List<Member> path1 = getPathToBoss(m1);
        List<Member> path2 = getPathToBoss(m2);

        // Returns the first member that is superior to the both of the members and the lowest ranking member.
        for (Member member : path2) {
            if (path1.contains(member)) {
                return member;
            }
        }

        return root;

    }

    // Gets all of the members that have same rank value with the specified member and adds them to the output.
    public void getSameRanks(String name) {
        updateRank(root, 0);

        Member baseMember = findMember(name);
        if (baseMember != null) {
            StringBuilder rankResult = new StringBuilder("Rank Analysis Result:"); // Stores all of the members that have the same rank.
            buildListOfSameRanks(root, baseMember.rank, rankResult);
            output.add(rankResult.toString()); // Adds the final rankResult variable to the output.
        }
    }

    // Finds all the members that have the same rank value and adds them to StringBuilder recursively.
    private void buildListOfSameRanks(Member node, int rank, StringBuilder rankResult) {
        if (node == null) { // Base case.
            return;
        }

        // Traverse the left subtree first if rank is larger or equal.
        if (node.rank <= rank) {
            buildListOfSameRanks(node.left, rank, rankResult);
        }

        // Process the current node.
        if (node.rank == rank) {
            rankResult.append(" ").append(node.name).append(" ").append(String.format("%.3f", node.gms));
        }

        // Traverse the right subtree.
        if (node.rank <= rank) {
            buildListOfSameRanks(node.right, rank, rankResult);
        }
    }

    // Finds the maximum numbers of the independent members in the tree.
    public void divideFamily() {
        int maxIndependentMembers = findMaxIndependentMembers(root);
        output.add("Division Analysis Result: " + maxIndependentMembers);
    }

    // Returns the maximum numbers of the independent members.
    private int findMaxIndependentMembers(Member root) {
        if (root == null)
            return 0;

        // Each element in the pair will hold counts for including or excluding the current node.
        int[] counts = findMaxIndependentMembersRecursive(root);

        // Returns the max count from including or excluding the current node.
        return Math.max(counts[0], counts[1]);
    }

    // Returns an integer list of the maximum independent members with including the current node and excluding it recursively. 
    private int[] findMaxIndependentMembersRecursive(Member node) {
        if (node == null) { // Base case.
            return new int[] { 0, 0 };
        }

        int[] leftCounts = findMaxIndependentMembersRecursive(node.left);
        int[] rightCounts = findMaxIndependentMembersRecursive(node.right);

        int include = 1 + leftCounts[1] + rightCounts[1];
        int exclude = Math.max(leftCounts[0], leftCounts[1]) + Math.max(rightCounts[0], rightCounts[1]);

        return new int[] { include, exclude };
    }

}
