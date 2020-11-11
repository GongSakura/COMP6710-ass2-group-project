package comp1110.ass2;

import org.junit.Assert;
import org.junit.Test;

/**
 * Yuxuan Yang(Ryan)'s junit test
 *
 * This test is aim at testing when drop/drag a piece, if it return order placement
 */
public class UpdatePlacementTest {
    //  0: b43S
    //  1: G01E
    //  2: I30E
    //  3: l82W
    //  4: n61W
    //  5: O50N
    //  6: p00N
    //  7: R22S
    //  8: s80E
    //  9: y03S
    Games testGame1 = new Games(23, "b43SG01EI30El82Wn61WO50Np00NR22Ss80Ey03S", "b43SG01EI30El82Wn61WO50Np00NR22Ss80Ey03S");
    Games testGame2 = new Games(23, "", "b43SG01EI30El82Wn61WO50Np00NR22Ss80Ey03S");

    @Test
    public void testUpdateDragPiece() {
//        pieceState = "";
        FitGame fitGame = new FitGame(testGame1);
        for (Piece piece : fitGame.getPieces()) {
            piece.setPrePlaced(false);
        }
        String pieceState = fitGame.getPieces().get(3).getPieceState();
        fitGame.dragPiece(fitGame.getPieces().get(3), fitGame.getMatrixBoard());
        Assert.assertEquals("To drag: " + pieceState + ". The expected: " + "b43SG01EI30En61WO50Np00NR22Ss80Ey03S" + ", but got: " + fitGame.getPlacement(), "b43SG01EI30En61WO50Np00NR22Ss80Ey03S", fitGame.getPlacement());

        pieceState = fitGame.getPieces().get(3).getPieceState();
        fitGame.dragPiece(fitGame.getPieces().get(3), fitGame.getMatrixBoard());
        Assert.assertEquals("To drag: " + pieceState + ".The expected: " + "b43SG01EI30En61WO50Np00NR22Ss80Ey03S" + ", but got: " + fitGame.getPlacement(), "b43SG01EI30En61WO50Np00NR22Ss80Ey03S", fitGame.getPlacement());

        pieceState = fitGame.getPieces().get(1).getPieceState();
        fitGame.dragPiece(fitGame.getPieces().get(1), fitGame.getMatrixBoard());
        Assert.assertEquals("To drag: " + pieceState + ".The expected: " + "b43SI30En61WO50Np00NR22Ss80Ey03S" + ", but got: " + fitGame.getPlacement(), "b43SI30En61WO50Np00NR22Ss80Ey03S", fitGame.getPlacement());

        pieceState = fitGame.getPieces().get(7).getPieceState();
        fitGame.dragPiece(fitGame.getPieces().get(7), fitGame.getMatrixBoard());
        Assert.assertEquals("To drag: " + pieceState + ".The expected: " + "b43SI30En61WO50Np00Ns80Ey03S" + ", but got: " + fitGame.getPlacement(), "b43SI30En61WO50Np00Ns80Ey03S", fitGame.getPlacement());

        pieceState = fitGame.getPieces().get(5).getPieceState();
        fitGame.dragPiece(fitGame.getPieces().get(5), fitGame.getMatrixBoard());
        Assert.assertEquals("To drag: " + pieceState + ".The expected: " + "b43SI30En61Wp00Ns80Ey03S" + ", but got: " + fitGame.getPlacement(), "b43SI30En61Wp00Ns80Ey03S", fitGame.getPlacement());

    }

    @Test
    public void testUpdateDropPiece() {

        FitGame fitGame = new FitGame(testGame2);
        fitGame.getPieces().get(1).flip();
        fitGame.getPieces().get(1).setOrientation(Orientation.E);
        fitGame.dropPiece(fitGame.getPieces().get(1), fitGame.getMatrixBoard(), 0, 1);
        Assert.assertEquals("The expected: " + "G01E" + ", but got: " + fitGame.getPlacement(), "G01E", fitGame.getPlacement());

        fitGame.getPieces().get(0).setOrientation(Orientation.S);
        fitGame.dropPiece(fitGame.getPieces().get(0), fitGame.getMatrixBoard(), 4, 3);
        Assert.assertEquals("The expected: " + "b43SG01E" + ", but got: " + fitGame.getPlacement(), "b43SG01E", fitGame.getPlacement());


        fitGame.dropPiece(fitGame.getPieces().get(6), fitGame.getMatrixBoard(), 0, 0);
        Assert.assertEquals("The expected: " + "b43SG01Ep00N" + ", but got: " + fitGame.getPlacement(), "b43SG01Ep00N", fitGame.getPlacement());

        fitGame.getPieces().get(4).setOrientation(Orientation.W);
        fitGame.dropPiece(fitGame.getPieces().get(4), fitGame.getMatrixBoard(), 6, 1);
        Assert.assertEquals("The expected: " + "b43SG01En61Wp00N" + ", but got: " + fitGame.getPlacement(), "b43SG01En61Wp00N", fitGame.getPlacement());
    }
}
