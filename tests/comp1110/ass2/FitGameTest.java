package comp1110.ass2;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FitGameTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(1000);

    Piece[] pieces = {
            new Piece("b30N"),
            new Piece("g03S"),
            new Piece("l00W"),
            new Piece("N12N"),
            new Piece("Y41S"),
    };

    int[][][] expect_matrixBoard = {
            new int[][]{{0, 0, 0, 1, 1, 1, 1, 0, 0, 0},
                    {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            },
            new int[][]{
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                    {1, 1, 1, 0, 0, 0, 0, 0, 0, 0}
            },
            new int[][]{
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            },
            new int[][]{
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 1, 1, 1, 0, 0, 0, 0, 0, 0},
                    {0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
            },
            new int[][]{
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
                    {0, 0, 0, 0, 1, 1, 1, 1, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            }
    };

    /**
     * Test placePiece()
     * @param shape                the shape of certain piece
     * @param expected_matrixBoard the expected result when the piece is placed
     * @param col                  the col which the piece places in
     * @param row                  the row which the piece places in
     */
    private void testPlacePiece(int[][] shape, int[][] expected_matrixBoard, int col, int row) {
////        int[][] result = new int[5][10];
////        FitGame.placePiece(shape, result, col, row);
//        Assert.assertArrayEquals("the piece " + shape + " does not place in ( " + col + " , " + row + " )", expected_matrixBoard, result);
    }

    @Test
    public void placePiece_Null() {
        testPlacePiece(null, new int[5][10], 0, 0);
    }

    @Test
    public void placePiece_empty() {
        testPlacePiece(new int[0][0], new int[5][10], 5, 5);
    }

    @Test
    public void placePiece_inBoard() {
        for (int i = 0; i < pieces.length; i++) {
            int[][] tmp = pieces[i].getMatrixShape();
            testPlacePiece(tmp, expect_matrixBoard[i], pieces[i].getCol(), pieces[i].getRow());
        }
    }

    // task 3:
    // test drop piece
    // test isDropViable

    /**
     * Test isDropViable()
     * @param shape                 the shape of certain piece
     * @param board                 the board which a piece will drop to
     * @param col                   the col which the piece places in
     * @param row                   the row which the piece places in
     * @param expected_viable       the expected boolean
     */
    private void testIsDropViable(int[][] shape, int[][] board, int col, int row, boolean expected_viable) {

        boolean out = FitGame.isDropViable(shape, board, col, row);
        assertTrue("the piece " + shape + " drop in ( " + col + " , " + row + " ) ,expected " + expected_viable + " but got " + out, out == expected_viable);
    }

    @Test
    public void isDropViable_Null(){
        testIsDropViable(null, new int[5][10], 0, 0,false);
    }

    @Test
    public void isDropViable_empty(){
        testIsDropViable(new int[0][0],new int[5][10],5,5,false);
    }

    @Test
    public void isDropViable_offRange(){
        testIsDropViable(pieces[1].getMatrixShape(), new int[5][10], 11, 0, false);
        testIsDropViable(pieces[1].getMatrixShape(), new int[5][10], -1, 0, false);
        testIsDropViable(pieces[1].getMatrixShape(), new int[5][10], 0, 11, false);
        testIsDropViable(pieces[1].getMatrixShape(), new int[5][10], 0, -1, false);
        testIsDropViable(pieces[1].getMatrixShape(), new int[5][10], 9, 0, false);
        testIsDropViable(pieces[1].getMatrixShape(), new int[5][10], 0, 5, false);

    }

    @Test
    public void isDropViable_overlap(){
        testIsDropViable(pieces[0].getMatrixShape(), expect_matrixBoard[0], 0, 0, false);
        testIsDropViable(pieces[1].getMatrixShape(), expect_matrixBoard[1], 0, 3, false);
        testIsDropViable(pieces[0].getMatrixShape(), expect_matrixBoard[0], 0, 2, true);

    }



}
