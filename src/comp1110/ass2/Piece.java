package comp1110.ass2;

import java.awt.*;

/**
 * Authorship: Paul Won, Yuxuan Yang, Chensheng Zhang
 * This class can represent a general plastic piece
 * The property of a plastic piece is:
 * - piece type
 * - orientation
 * - piece state (Describe the piece in text)
 * - piece location(upper left corner)
 * - can be fixed, dragged, dropped, rotated, and flipped
 */
public class Piece {

    public PieceType pieceType;       // it represent the shape and color of a piece
    public Orientation orientation;   // the piece orientation
    private int col;                   // the column(0-9) location of the upper left corner of the piece
    private int row;                   // the row(0-4) location of the upper left corner of the piece
    private boolean prePlaced = false;          // true: the piece cannot be moved, it is fixed on board
    private boolean placed = false;            // true: the piece place in the board,then it couldn't rotate and flip
    private boolean flip;              // true: double protrusions side, false: single protrusion side
    private String pieceState;         // Four characters consisted of 'pieceType','X','Y','orientation' like "b04N"
    private int[][] matrixShape;       // the shape of piece formed by matrix


    /**
     * Construct a plastic piece by specified the piece type and upper left location
     *
     * @param pieceType   the type of piece which consisted of color and shape
     * @param col         the col which the piece is in
     * @param row         the row which the piece is in
     * @param orientation the piece orientation
     */
    public Piece(PieceType pieceType, int col, int row, Orientation orientation) {
        this.pieceType = pieceType;
        this.col = col;
        this.row = row;
        this.orientation = orientation;
        setFlipped(pieceType.toString());
        this.pieceState = getPieceState();
        switch (orientation) {
            case N:
                this.matrixShape = pieceType.getMatrixShape_N();
                break;
            case E:
                this.matrixShape = pieceType.getMatrixShape_E();
                break;
            case S:
                this.matrixShape = pieceType.getMatrixShape_S();
                break;
            case W:
                this.matrixShape = pieceType.getMatrixShape_W();
                break;
        }
    }

    /**
     * Another constructor that uses four characters string to construct a plastic piece
     *
     * @param piecePlacement It contains the piece type, x, y, orientation
     */
    public Piece(String piecePlacement) {
        this.pieceType = PieceType.valueOf(piecePlacement.substring(0, 1));
        this.col = Integer.parseInt(piecePlacement.substring(1, 2));
        this.row = Integer.parseInt(piecePlacement.substring(2, 3));
        this.orientation = Orientation.valueOf(piecePlacement.substring(3, 4));
        setFlipped(piecePlacement.substring(0, 1));
        this.pieceState = piecePlacement;
        switch (orientation) {
            case N:
                this.matrixShape = pieceType.getMatrixShape_N();
                break;
            case E:
                this.matrixShape = pieceType.getMatrixShape_E();
                break;
            case S:
                this.matrixShape = pieceType.getMatrixShape_S();
                break;
            case W:
                this.matrixShape = pieceType.getMatrixShape_W();
                break;
        }
    }


    /**
     * To flip over the piece
     * When flip the piece, the "pieceType","flip","pieceState",and "matrixShape" will be updated
     */
    public void flip() {
        // if the type is true, change the piece string to lowercase
        if (this.flip == true) {
            this.pieceType = PieceType.valueOf(this.pieceType.toString().toLowerCase());

            this.flip = false;
        } else {
            this.pieceType = PieceType.valueOf(this.pieceType.toString().toUpperCase());
            this.flip = true;
        }

        switch (this.orientation) {
            case N:
                this.setMatrixShape(this.pieceType.getMatrixShape_N());
                break;
            case E:
                this.setMatrixShape(this.pieceType.getMatrixShape_E());
                break;
            case S:
                this.setMatrixShape(this.pieceType.getMatrixShape_S());
                break;
            case W:
                this.setMatrixShape(this.pieceType.getMatrixShape_W());
                break;

        }
        updatePieceState();
    }

    /**
     * To rotate the piece 90 degree clockwise
     * "pieceState","matrixShape",and "orientation"will be updated
     */
    public void rotate() {
        switch (orientation) {
            case N:
                this.orientation = Orientation.E;
                this.setMatrixShape(this.pieceType.getMatrixShape_E());
                break;
            case E:
                this.orientation = Orientation.S;
                this.setMatrixShape(this.pieceType.getMatrixShape_S());
                break;
            case S:
                this.orientation = Orientation.W;
                this.setMatrixShape(this.pieceType.getMatrixShape_W());
                break;
            case W:
                this.orientation = Orientation.N;
                this.setMatrixShape(this.pieceType.getMatrixShape_N());
                break;
        }
        updatePieceState();
    }

    public void rotateByOrientation(Orientation o) {
        this.orientation = o;
        this.setMatrixShape(this.pieceType.getMatrixShapeViaOrientation(o));
        updatePieceState();
    }

    /**
     * If the piece has been rotated, flipped, it will update the piece state
     */
    public void updatePieceState() {
        String s = "";
        s += pieceType.toString();
        s += col;
        s += row;
        s += orientation.toString();
        pieceState = s;
    }

    /**
     * @return Get the piece state
     */
    public String getPieceState() {
        if (pieceState == null) {
            updatePieceState();
        }
        return pieceState;
    }

    /**
     * @return get the column of location of the piece
     */
    public int getCol() {
        return col;
    }

    /**
     * Set the column of location of the piece
     *
     * @param n the column which piece is set in
     */
    public void setCol(int n) {
        this.col = n;
        updatePieceState();
    }

    /**
     * @return get the row of location of the piece
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Set the row of location of the piece
     *
     * @param n the row which piece is set in
     */
    public void setRow(int n) {
        this.row = n;
        updatePieceState();
    }

    /**
     * @return the matrix shape of the piece
     */
    public int[][] getMatrixShape() {
        return this.matrixShape;
    }

    /**
     * set the matrix shape of the piece
     */
    public void setMatrixShape(int[][] matrix) {
        this.matrixShape = matrix;
    }


    /**
     * If it returns true,it means the piece cannot
     * be dragged, dropped, rotated, and flipped.
     *
     * @return true
     */
    public boolean isPrePlaced() {
        return this.prePlaced;
    }

    /**
     * set the piece to be pre-placed
     *
     * @param value true: pre-place the piece
     */
    public void setPrePlaced(boolean value) {
        this.prePlaced = value;
    }


    /**
     * True(upper case) = double protrusion, False(lower case) = single protrusion
     *
     * @param typeName the name of piece type
     * @return the flip state
     */
    public void setFlipped(String typeName) {
        this.flip = !typeName.equals(typeName.toLowerCase());
    }

    public boolean isFlipped() {
        return this.flip;
    }


    /***
     * set the value of placed, if true, then the piece has been placed
     * @param value
     */
    public void setPlaced(boolean value) {
        this.placed = value;
    }

    /***
     *@return if true, then the piece has been placed and cannot be rotated and flipped
     */
    public boolean isPlaced() {
        return this.placed;
    }


    /**
     * Set the orientation, and update the matrix shape
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
        this.matrixShape = this.pieceType.getMatrixShapeViaOrientation(orientation);
        updatePieceState();
    }

    /**
     * Get the orientation
     *
     * @return
     */
    public Orientation getOrientation() {
        return this.orientation;
    }

    // Get the PieceType


    public PieceType getPieceType() {
        return pieceType;
    }
}
