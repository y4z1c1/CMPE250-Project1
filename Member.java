/**
 * Member.java - Represents a single member within a family tree.
 * Stores information such as name, rank, and connections to other family members.
 */

public class Member {
    // Member class variables.
    String name; // Represents the SURNAME_NAME value.
    double gms; // Represents the GMS value.
    Member left, right; // Represents two children of the member.
    int height; // Represents the height of the member.
    int rank; // Represent the rank number of the memeber.
    Member parent; // Represent the ancestor of the member.

    // Constructor for the Member class.
    public Member(String name, double gms) {
        this.name = name;
        this.gms = gms;
        this.height = 1; // Set the default height value as 1.
        this.rank = 0; // Set the default rank value as 0.
    }

    // Getter and setter methods for the Member class variables.
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGms() {
        return gms;
    }

    public void setGms(double gms) {
        this.gms = gms;
    }

    public Member getLeft() {
        return left;
    }

    public void setLeft(Member left) {
        this.left = left;
    }

    public Member getRight() {
        return right;
    }

    public void setRight(Member right) {
        this.right = right;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

}
