package comp1110.ass2;

/**
 * Authorship: Paul Won, Yuxuan Yang, Chensheng Zhang
 * This enumeration type represents the four orientations of the piece state
 * <p>
 * This class may get deleted in future depends on a usage.
 */

public enum Orientation {
    N(0), E(90), S(180), W(270);
    int degree;

    Orientation(int degree) {
        this.degree = degree;
    }


    /**
     * @return A string represents the abbreviation of orientation
     */
    @Override
    public String toString() {
        return name();
    }
}
