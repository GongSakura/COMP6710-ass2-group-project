package comp1110.ass2;

import javafx.scene.paint.Color;


/**
 * Authorship: Paul Won, Yuxuan Yang, Chensheng Zhang
 * This enumeration type represents 20 possible types of pieces
 * depends on default orientation('N') and its color.
 * <p>
 * The shape of piece is regarded as a 4x2 matrix or 2x4 matrix
 * <p>
 * This may contain methods to be used in other classes.
 */
public enum PieceType {
    g(Color.GREEN, 3, 1, new int[][]{{1, 1, 1}, {0, 1, 0}}, new int[][]{{0, 1}, {1, 1}, {0, 1}}, new int[][]{{0, 1, 0}, {1, 1, 1}}, new int[][]{{1, 0}, {1, 1}, {1, 0}}),
    G(Color.GREEN, 3, 2, new int[][]{{1, 1, 1}, {1, 1, 0}}, new int[][]{{1, 1}, {1, 1}, {0, 1}}, new int[][]{{0, 1, 1}, {1, 1, 1}}, new int[][]{{1, 0}, {1, 1}, {1, 1}}),
    i(Color.INDIGO, 3, 1, new int[][]{{1, 1, 1}, {0, 0, 1}}, new int[][]{{0, 1}, {0, 1}, {1, 1}}, new int[][]{{1, 0, 0}, {1, 1, 1}}, new int[][]{{1, 1}, {1, 0}, {1, 0}}),
    I(Color.INDIGO, 3, 2, new int[][]{{1, 1, 1}, {0, 1, 1}}, new int[][]{{0, 1}, {1, 1}, {1, 1}}, new int[][]{{1, 1, 0}, {1, 1, 1}}, new int[][]{{1, 1}, {1, 1}, {1, 0}}),
    l(Color.LIMEGREEN, 3, 1, new int[][]{{1, 1, 1}, {1, 0, 0}}, new int[][]{{1, 1}, {0, 1}, {0, 1}}, new int[][]{{0, 0, 1}, {1, 1, 1}}, new int[][]{{1, 0}, {1, 0}, {1, 1}}),
    L(Color.LIMEGREEN, 3, 2, new int[][]{{1, 1, 1}, {1, 0, 1}}, new int[][]{{1, 1}, {0, 1}, {1, 1}}, new int[][]{{1, 0, 1}, {1, 1, 1}}, new int[][]{{1, 1}, {1, 0}, {1, 1}}),
    n(Color.NAVY, 3, 1, new int[][]{{1, 1, 1}, {0, 1, 0}}, new int[][]{{0, 1}, {1, 1}, {0, 1}}, new int[][]{{0, 1, 0}, {1, 1, 1}}, new int[][]{{1, 0}, {1, 1}, {1, 0}}),
    N(Color.NAVY, 3, 2, new int[][]{{1, 1, 1}, {1, 0, 1}}, new int[][]{{1, 1}, {0, 1}, {1, 1}}, new int[][]{{1, 0, 1}, {1, 1, 1}}, new int[][]{{1, 1}, {1, 0}, {1, 1}}),

    b(Color.BLUE, 4, 1, new int[][]{{1, 1, 1, 1}, {1, 0, 0, 0}}, new int[][]{{1, 1}, {0, 1}, {0, 1}, {0, 1}}, new int[][]{{0, 0, 0, 1}, {1, 1, 1, 1}}, new int[][]{{1, 0}, {1, 0}, {1, 0}, {1, 1}}),
    B(Color.BLUE, 4, 2, new int[][]{{1, 1, 1, 1}, {0, 1, 0, 1}}, new int[][]{{0, 1}, {1, 1}, {0, 1}, {1, 1}}, new int[][]{{1, 0, 1, 0}, {1, 1, 1, 1}}, new int[][]{{1, 1}, {1, 0}, {1, 1}, {1, 0}}),
    o(Color.ORANGE, 4, 1, new int[][]{{1, 1, 1, 1}, {0, 1, 0, 0}}, new int[][]{{0, 1}, {1, 1}, {0, 1}, {0, 1}}, new int[][]{{0, 0, 1, 0}, {1, 1, 1, 1}}, new int[][]{{1, 0}, {1, 0}, {1, 1}, {1, 0}}),
    O(Color.ORANGE, 4, 2, new int[][]{{1, 1, 1, 1}, {1, 0, 1, 0}}, new int[][]{{1, 1}, {0, 1}, {1, 1}, {0, 1}}, new int[][]{{0, 1, 0, 1}, {1, 1, 1, 1}}, new int[][]{{1, 0}, {1, 1}, {1, 0}, {1, 1}}),
    p(Color.PINK, 4, 1, new int[][]{{1, 1, 1, 1}, {0, 0, 1, 0}}, new int[][]{{0, 1}, {0, 1}, {1, 1}, {0, 1}}, new int[][]{{0, 1, 0, 0}, {1, 1, 1, 1}}, new int[][]{{1, 0}, {1, 1}, {1, 0}, {1, 0}}),
    P(Color.PINK, 4, 2, new int[][]{{1, 1, 1, 1}, {1, 1, 0, 0}}, new int[][]{{1, 1}, {1, 1}, {0, 1}, {0, 1}}, new int[][]{{0, 0, 1, 1}, {1, 1, 1, 1}}, new int[][]{{1, 0}, {1, 0}, {1, 1}, {1, 1}}),
    r(Color.RED, 4, 1, new int[][]{{1, 1, 1, 1}, {1, 0, 0, 0}}, new int[][]{{1, 1}, {0, 1}, {0, 1}, {0, 1}}, new int[][]{{0, 0, 0, 1}, {1, 1, 1, 1}}, new int[][]{{1, 0}, {1, 0}, {1, 0}, {1, 1}}),
    R(Color.RED, 4, 2, new int[][]{{1, 1, 1, 1}, {1, 0, 0, 1}}, new int[][]{{1, 1}, {0, 1}, {0, 1}, {1, 1}}, new int[][]{{1, 0, 0, 1}, {1, 1, 1, 1}}, new int[][]{{1, 1}, {1, 0}, {1, 0}, {1, 1}}),
    s(Color.SKYBLUE, 4, 1, new int[][]{{1, 1, 1, 1}, {0, 1, 0, 0}}, new int[][]{{0, 1}, {1, 1}, {0, 1}, {0, 1}}, new int[][]{{0, 0, 1, 0}, {1, 1, 1, 1}}, new int[][]{{1, 0}, {1, 0}, {1, 1}, {1, 0}}),
    S(Color.SKYBLUE, 4, 2, new int[][]{{1, 1, 1, 1}, {0, 1, 1, 0}}, new int[][]{{0, 1}, {1, 1}, {1, 1}, {0, 1}}, new int[][]{{0, 1, 1, 0}, {1, 1, 1, 1}}, new int[][]{{1, 0}, {1, 1}, {1, 1}, {1, 0}}),
    y(Color.YELLOW, 4, 1, new int[][]{{1, 1, 1, 1}, {0, 0, 0, 1}}, new int[][]{{0, 1}, {0, 1}, {0, 1}, {1, 1}}, new int[][]{{1, 0, 0, 0}, {1, 1, 1, 1}}, new int[][]{{1, 1}, {1, 0}, {1, 0}, {1, 0}}),
    Y(Color.YELLOW, 4, 2, new int[][]{{1, 1, 1, 1}, {0, 0, 1, 1}}, new int[][]{{0, 1}, {0, 1}, {1, 1}, {1, 1}}, new int[][]{{1, 1, 0, 0}, {1, 1, 1, 1}}, new int[][]{{1, 1}, {1, 1}, {1, 0}, {1, 0}});

    private final int spine;
    private final int protrusion;
    private final Color color;
    private final int[][] matrixShape_N;
    private final int[][] matrixShape_E;
    private final int[][] matrixShape_S;
    private final int[][] matrixShape_W;

    /**
     * Constructor for PieceType
     * It has 3 or 4 spheres for spine
     * It has 3 protruding spheres
     * i.e. 2 in one plane (double protrusion on flat view)
     * and 1 in the other (single protrusion on flat view)
     * The representation of the piece is made in a rectangular matrix
     * where 0 in the matrix shows empty places and 1 shows a place of a sphere
     *
     * @param color         the color of piece
     * @param spine         the number of spines of piece
     * @param protrusion    the number of protrusion of piece
     * @param matrixShape_N the shape of piece represented by matrix
     *                      i.e. The Pink piece(P) for 2 protrusions and in 'N' orientation
     *                      looks like :⚪⚪⚪⚪  then,in matrix [1,1,1,1]
     *                                  ⚪⚪                   [1,1,0,0]
     */

    PieceType(Color color, int spine, int protrusion, int[][] matrixShape_N, int[][] matrixShape_E, int[][] matrixShape_S, int[][] matrixShape_W) {
        this.color = color;
        this.spine = spine;
        this.protrusion = protrusion;
        this.matrixShape_N = matrixShape_N;
        this.matrixShape_E = matrixShape_E;
        this.matrixShape_S = matrixShape_S;
        this.matrixShape_W = matrixShape_W;

    }

    /**
     * @return get the color of the piece
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return get the number of spines of the piece
     */
    public int getSpine() {
        return spine;
    }

    /**
     * @return get the number of protrusion of the piece
     */
    public int getProtrusion() {
        return protrusion;
    }

    /**
     * @return get the default matrix shape of the piece
     */
    public int[][] getMatrixShape() {
        return matrixShape_N;
    }

    /**
     * @return get the matrix shape(N orientation) of the piece
     */
    public int[][] getMatrixShape_N() {
        return matrixShape_N;
    }

    /**
     * @return get the matrix shape(E orientation) of the piece
     */
    public int[][] getMatrixShape_E() {
        return matrixShape_E;
    }

    /**
     * @return get the matrix shape(S orientation) of the piece
     */
    public int[][] getMatrixShape_S() {
        return matrixShape_S;
    }

    /**
     * @return get the matrix shape(W orientation) of the piece
     */
    public int[][] getMatrixShape_W() {
        return matrixShape_W;
    }

    /**
     * @return get the matrix shape via orientation
     */
    public int[][] getMatrixShapeViaOrientation(Orientation orientation) {
        switch (orientation) {
            case N:
                return matrixShape_N;
            case E:
                return matrixShape_E;
            case S:
                return matrixShape_S;
            default:
                return matrixShape_W;
        }
    }

    /**
     * @return get the name of the piece
     */
    @Override
    public String toString() {
        return name();
    }
}
