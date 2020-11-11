package comp1110.ass2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import java.util.Arrays;

import static org.junit.Assert.*;

public class PieceTest {
    // Practicing Rule
    @Rule
    public Timeout globalTimeout = Timeout.millis(500); // shouldn't take too long

    // Create all piece types
    Piece[] pieces = {
            new Piece("b00N"),
            new Piece("B55S"),
            new Piece("g70W"),
            new Piece("G33E"),
            new Piece("i55S"),
            new Piece("I70W"),
            new Piece("l33E"),
            new Piece("L55S"),
            new Piece("n70W"),
            new Piece("N33E"),
            new Piece("s55S"),
            new Piece("S70W"),
            new Piece("o33E"),
            new Piece("O55S"),
            new Piece("p70W"),
            new Piece("P33E"),
            new Piece("r55S"),
            new Piece("R70W"),
            new Piece("y33E"),
            new Piece("Y33E")
    };

    // Test updatePieceState() which reassigns changed value to the piece
    @Test
    public void testUpdatePieceState() {
        for (Piece piece: pieces) {
            // initial state which will not change if updatePieceState() don't work
            String initialState = piece.getPieceState();
            // incurring multiple updatePieceState()
            piece.pieceType = PieceType.Y;
            piece.setCol(1);
            piece.setRow(1);
            piece.setOrientation(Orientation.N);
            String stateAfter = piece.getPieceState();
            assertTrue("State not changed for " + initialState + " in assert True when expected piece state is Y11N but got " + stateAfter, piece.getPieceState().equals("Y11N"));
            assertFalse("This is assert False for " + initialState + " when didn't expected to get Y11N but got " + stateAfter, !piece.getPieceState().equals("Y11N"));
        }
    }

    // Test Flip()
    @Test
    public void testFlip() {
        for (Piece piece: pieces) {
            // Initial setups
            char initialPieceType = piece.getPieceState().charAt(0);
            char newPieceType;
            boolean initialFlipState = piece.isFlipped();
            int[][] matrixBefore = piece.getMatrixShape();
            int[][] expectedMatrix;
            String pieceState = piece.getPieceState();
            Orientation orientation = piece.getOrientation();

            // Create expected piece type in regards to the initial piece type when flipped
            if(Character.isLowerCase(initialPieceType))
                newPieceType = Character.toUpperCase(initialPieceType);
            else
                newPieceType = Character.toLowerCase(initialPieceType);
            PieceType expectedChange = PieceType.valueOf(Character.toString(newPieceType));

            // Initiate flip()
            piece.flip();

            // Load expected matrix shaped when flipped
            switch (orientation) {
                case S:
                    expectedMatrix = expectedChange.getMatrixShape_S();
                    break;
                case N:
                    expectedMatrix = expectedChange.getMatrixShape_N();
                    break;
                case E:
                    expectedMatrix = expectedChange.getMatrixShape_E();
                    break;
                case W:
                    expectedMatrix = expectedChange.getMatrixShape_W();
                    break;
                default:
                    // catch and throw error if test piece contained orientation value outside N or S or E or W
                    throw new IllegalStateException("Unexpected value: " + orientation);
            }

            // Test if it has been flipped
            testBoolean(pieceState, piece,!initialFlipState, false);

            // Test if the flipped matrix shape is the same as what is expected
            testState(piece, matrixToString(matrixBefore), matrixToString(expectedMatrix));
        }
    }

    // Test rotate
    @Test
    public void testRotate() {
        for (Piece piece: pieces) {
            // Initialize values
            int[][] matrixBefore = piece.getMatrixShape();
            int[][] expectedMatrix;
            Orientation orientationBefore = piece.getOrientation();
            Orientation expectedOtn;
            String newPiece;

            // Initiate rotate()
            piece.rotate();

            // Load expected changes when rotated
            switch (orientationBefore) {
                case S:
                    expectedOtn = Orientation.W;
                    newPiece = "" + piece.pieceType + piece.getCol() + piece.getRow() + expectedOtn;
                    expectedMatrix = piece.pieceType.getMatrixShape_W();
                    break;
                case N:
                    expectedOtn = Orientation.E;
                    newPiece = "" + piece.pieceType + piece.getCol() + piece.getRow() + expectedOtn;
                    expectedMatrix = piece.pieceType.getMatrixShape_E();
                    break;
                case E:
                    expectedOtn = Orientation.S;
                    newPiece = "" + piece.pieceType + piece.getCol() + piece.getRow() + expectedOtn;
                    expectedMatrix = piece.pieceType.getMatrixShape_S();
                    break;
                case W:
                    expectedOtn = Orientation.N;
                    newPiece = "" + piece.pieceType + piece.getCol() + piece.getRow() + expectedOtn;
                    expectedMatrix = piece.pieceType.getMatrixShape_N();
                    break;
                default:
                    // catch and throw error if test piece contained orientation value outside N or S or E or W
                    throw new IllegalStateException("Unexpected value: " + orientationBefore);
            }

            // Test if orientation has been changed.
            // Expected boolean compares to manually set orientation, i.e. expectedOtn.
            testBoolean(newPiece, piece, piece.getOrientation().equals(expectedOtn), true);

            // Test if rotated matrix is correct.
            testState(piece, matrixToString(matrixBefore), matrixToString(expectedMatrix));
        }
    }

    // Method to test boolean functions.
    private void testBoolean(String in, Piece piece, boolean expected, boolean rotate) {
        // It is false if flipped from Uppercase or true if flipped from lowercase.
        boolean out = piece.isFlipped();
        // If the function is testing rotations, it creates a new piece with given string to compare expected change.
        if (rotate)
            out = piece.getOrientation().equals(new Piece(in).getOrientation());

        assertEquals("Input was '" + in + "', expected " + expected + " but got " + out, expected, out);
    }

    // Method to test piece states.
    private void testState(Piece piece, String in, String expected) {
        // Matrix shape of the current piece state
        String out = matrixToString(piece.getMatrixShape());

        assertEquals("Input was '" + in + "', expected " + expected + " but got " + out, expected, out);
    }

    // Additional method to convert int[][] to custom string format.
    public static String matrixToString(int[][] matrix) {
        String s = "";
        for (int i = 0; i < matrix.length; i++) {
            s += Arrays.toString(matrix[i]);
        }
        return s;
    }
}
