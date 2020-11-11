package comp1110.ass2;

import org.junit.Assert;
import org.junit.Test;

/**
 * individual Junit Test
 */

public class DragPieceTest {
    Games testGame = new Games(49, "b41Wg81WY01W", "b41Wg81WI13SL70Nn21EO63Sp00Nr40Es60WY01W");

    Piece[] piecesIsNotOnBoard = {
            new Piece("I13E"),
            new Piece("L70N"),
            new Piece("n21S"),
            new Piece("O63N"),
    };

    Piece[] piecesTobePlaced = {
            new Piece("r40E"),
            new Piece("s60W"),
            new Piece("p00N"),
    };
    Piece[] piecesIsPrePlaced = {
            new Piece("b41W"),
            new Piece("g81W"),
            new Piece("Y01W"),
    };

    int[][][] expected_matrixBoard = {
            new int[][]{
                    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                    {1, 1, 0, 0, 1, 0, 0, 0, 1, 0},
                    {1, 1, 0, 0, 1, 0, 0, 0, 1, 1},
                    {1, 0, 0, 0, 1, 0, 0, 0, 1, 0},
                    {1, 0, 0, 0, 1, 1, 0, 0, 0, 0}
            },
            new int[][]{
                    {0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
                    {1, 1, 0, 0, 1, 1, 0, 0, 1, 0},
                    {1, 1, 0, 0, 1, 1, 0, 0, 1, 1},
                    {1, 0, 0, 0, 1, 1, 0, 0, 1, 0},
                    {1, 0, 0, 0, 1, 1, 0, 0, 0, 0}
            },
            new int[][]{
                    {0, 0, 0, 0, 1, 1, 1, 0, 0, 0},
                    {1, 1, 0, 0, 1, 1, 1, 0, 1, 0},
                    {1, 1, 0, 0, 1, 1, 1, 1, 1, 1},
                    {1, 0, 0, 0, 1, 1, 1, 0, 1, 0},
                    {1, 0, 0, 0, 1, 1, 0, 0, 0, 0},
            },
    };

    private void nullPieceTest(Piece piece) {
        FitGame fitGame = new FitGame(testGame);
        int[][] tmp = new int[5][10];
        for (int i = 0; i < fitGame.getMatrixBoard().length; i++) {
            tmp[i] = fitGame.getMatrixBoard()[i].clone();
        }
        fitGame.dragPiece(piece, fitGame.getMatrixBoard());
        Assert.assertTrue(piece==null);
        Assert.assertArrayEquals("Couldn't drag the piece that is null", tmp, fitGame.getMatrixBoard());
    }
    private void prePlacedTest(Piece piece){
        FitGame fitGame = new FitGame(testGame);
        int[][] tmp = new int[5][10];
        for (int i = 0; i < fitGame.getMatrixBoard().length; i++) {
            tmp[i] = fitGame.getMatrixBoard()[i].clone();
        }
        fitGame.dragPiece(piece,fitGame.getMatrixBoard());
        Assert.assertArrayEquals("Couldn't drag the pre-placed piece: " + piece.getPieceState(), tmp,fitGame.getMatrixBoard());
    }

    private void isNotOnBoardTest(Piece piece){
        FitGame fitGame = new FitGame(testGame);
        int[][] tmp = new int[5][10];
        for (int i = 0; i < fitGame.getMatrixBoard().length; i++) {
            tmp[i] = fitGame.getMatrixBoard()[i].clone();
        }
        fitGame.dragPiece(piece,fitGame.getMatrixBoard());
        Assert.assertArrayEquals("Couldn't drag the piece: " + piece.getPieceState() + "that is not on the board", tmp,fitGame.getMatrixBoard());
    }

    private void isOnBoardTest(Piece piece,FitGame fitGame, int[][] expect){
        // testDragPieceOnBoard()
        fitGame.dragPiece(piece,fitGame.getMatrixBoard());
        Assert.assertArrayEquals("didn't drag the piece:"+piece.getPieceState()+"successfully", expect,fitGame.getMatrixBoard());
    }

    @Test
    public void testDragNullPiece() {
      nullPieceTest(null);

    }

    @Test
    public void testDragPieceIsPrePlaced() {
        for (Piece piece : piecesIsPrePlaced) {
            piece.setPrePlaced(true);
            piece.setPlaced(true);
            prePlacedTest(piece);
        }
    }

    @Test
    public void testDragPieceNotOnBoard() {
        for (Piece piece : piecesIsNotOnBoard) {
            isNotOnBoardTest(piece);
        }
    }

    @Test
    public void testDragPieceOnBoard() {
        FitGame fitGame = new FitGame(testGame);
        for (Piece piece : piecesTobePlaced) {
            fitGame.dropPiece(piece,fitGame.getMatrixBoard(),piece.getCol(),piece.getRow());
        }
        for (int i = piecesTobePlaced.length-1; i >0; i--) {
            isOnBoardTest(piecesTobePlaced[i],fitGame,expected_matrixBoard[i]);
        }
    }
}
