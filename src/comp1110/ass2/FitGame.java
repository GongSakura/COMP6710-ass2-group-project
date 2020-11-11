package comp1110.ass2;

import java.util.*;


/**
 * This class provides the text interface for the IQ Fit Game
 * <p>
 * The game is based directly on Smart Games' IQ-Fit game
 * (https://www.smartgames.eu/uk/one-player-games/iq-fit)
 */
public class FitGame {

    private final int[][] matrixBoard;     // the 10x5 matrix board represent 10 columns and 5 rows
    private final ArrayList<Piece> pieces;   // all pieces
    public final Games challenge;         // it represents the challenge we need to overcome
    private String placement;        // current placement that record the state of all pieces on board, it has specific order
    static final char[] PIECE_TYPE = {'b', 'B', 'g', 'G', 'i', 'I', 'l', 'L', 'n', 'N',
            'o', 'O', 'p', 'P', 'r', 'R', 's', 'S', 'y', 'Y'};
    static final String[] PIECE_NAME_LOWERCASE = {"b", "g", "i", "l", "n", "o", "p", "r", "s", "y"};
    static final Set<String> EXIST_GAMES_SOLUTIONS = new HashSet<>();


    /**
     * Authorship: Yuxuan Yang
     * Construct a game with a specific objective
     *
     * @param games one of the challenges of this game.
     */
    public FitGame(Games games) {
        this.challenge = games;
        this.pieces = new ArrayList<>();
        this.matrixBoard = initialize(challenge.objective);
        this.placement = challenge.objective;
    }

    /**
     * Authorship: Yuxuan Yang
     * Construct a game for a given level of difficulty.
     * This chooses a new game and creates a new instance of
     * the Games at the given level of difficulty.
     *
     * @param difficulty The difficulty of the game.
     */
    public FitGame(int difficulty) {
        this.challenge = Games.newGame(difficulty);
        this.pieces = new ArrayList<>();
        this.matrixBoard = initialize(challenge.objective);
        this.placement = challenge.objective;
    }

    /**
     * Authorship: Yuxuan Yang, Paul Won (Matrix shape discussion partner)
     * It will initialise a a board comprised of 50 locations arranged
     * in a 5X10 matrix and it will pre-place some pieces.
     * create piece instances:
     * - pre-place pieces
     * - the rest pieces
     * place the pre-placed piece
     *
     * @param placement the piece placement
     * @return the matrix board
     */
    public int[][] initialize(String placement) {
        int[][] temp_board = new int[5][10];
        HashSet<String> used = new HashSet<>();
        // test null and empty
        if (placement == null || placement.isEmpty()) {
            // create the rest piece with default orientation N
            for (int i = 0; i < 10; i++) {
                String name = "";
                for (String s : PIECE_NAME_LOWERCASE) {
                    if (!used.contains(s)) {
                        name = s;
                        break;
                    }
                }
                Piece temp_piece = new Piece(PieceType.valueOf(name), 9, 9, Orientation.N);
                pieces.add(temp_piece);
                used.add(name);
            }
            return temp_board;
        }

        int n = placement.length() / 4;

        // test length
        if (placement == null || placement.isEmpty()) {
            return temp_board;
        }

        // create pieces
        // create the pre-place pieces
        for (int i = 0; i < n; i++) {
            Piece temp_piece = new Piece(placement.substring(i * 4, i * 4 + 4));
            temp_piece.setPrePlaced(true);
            pieces.add(temp_piece);
            used.add(placement.substring(i * 4, i * 4 + 1).toLowerCase());

            // place the piece
            dropPiece(temp_piece, temp_board, temp_piece.getCol(), temp_piece.getRow());
        }

        // create the rest piece with default orientation N
        for (int i = 0; i < 10 - n; i++) {
            String name = "";
            for (String s : PIECE_NAME_LOWERCASE) {
                if (!used.contains(s)) {
                    name = s;
                    break;
                }
            }

            Piece temp_piece = new Piece(PieceType.valueOf(name), 9, 9, Orientation.N);
            pieces.add(temp_piece);
            used.add(name);

        }
        return temp_board;
    }

    /**
     * Authorship: Yuxuan Yang
     * To drag the piece from the board
     * - if the piece is pre-placed, not placed,and null,then it couldn't be dragged from the board
     * - each off board piece will set its col and row to "9";
     *
     * @param piece the piece which to drag
     * @param board the matrix board, when drag a piece the board will update
     */
    public void dragPiece(Piece piece, int[][] board) {

        if (piece == null || piece.isPrePlaced()) {
            return;
        }
        if (piece.isPlaced()) {
            int[][] tmp = piece.getMatrixShape();
            int col = piece.getCol();
            int row = piece.getRow();

            // row=col=9 means the piece is not on the board
            piece.setRow(9);
            piece.setCol(9);
            piece.setPlaced(false);

            for (int i = 0; i < tmp.length; i++) {
                for (int j = 0; j < tmp[i].length; j++) {
                    if (tmp[i][j] == 1) {
                        board[row + i][col + j] = 0;
                    }
                }
            }

            updatePlacement();
        }

    }

    /**
     * Authorship: Yuxuan Yang
     * to drop the piece
     *
     * @param piece the piece which to drag
     * @param board the matrix board, when drop a piece the board will update
     * @param col   the column of location which drop in
     * @param row   the row of location which drop in
     */
    public void dropPiece(Piece piece, int[][] board, int col, int row) {
        int[][] tmp = piece.getMatrixShape();
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {
                board[row + i][col + j] += tmp[i][j];
            }
        }
        piece.setCol(col);
        piece.setRow(row);
        piece.setPlaced(true);
        updatePlacement();
    }


    /**
     * Authorship: Yuxuan Yang
     * Check if the location is viable for dropping the piece
     *
     * @param shape the matrix shape of the piece
     * @param board the board which a piece will drop to
     * @param col   the column of the location that a piece will drop in
     * @param row   the row of the location that a piece will drop in
     * @return True: a piece can drop in that location; False: a piece cannot drop in that location
     */
    public static boolean isDropViable(int[][] shape, int[][] board, int col, int row) {

        // check whether shape is null
        if (shape == null) {
            return false;
        }

        // check whether shape is empty
        if (shape.length == 0) {
            return false;
        }

        // check if it's off the board
        if (row + shape.length - 1 > 4 || col + shape[0].length - 1 > 9 || row < 0 || row > 4 || col < 0 || col > 9) {
            return false;
        }

        // place the piece shape to the board, if overlap, then return false
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                int tmp = board[row + i][col + j];
                tmp += shape[i][j];
                if (tmp > 1) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * Authorship: Yuxuan Yang
     * @return current placement
     */
    public String getPlacement() {
        return this.placement;
    }

    /**
     * Authorship: Yuxuan Yang
     * When there pieces are dropped on the board and dragged from the board, the placement will be updated
     */
    public void updatePlacement() {
        String res = "";
        Collections.sort(pieces, (Piece a, Piece b) -> {
            return a.pieceType.name().toLowerCase().compareTo(b.pieceType.name().toLowerCase());
        });
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).isPlaced()) {
                res += pieces.get(i).getPieceState();
            }
        }
        this.placement = res;
    }

    /**
     * Authorship: Yuxuan Yang
     * @return current matrix board of the game
     */
    public int[][] getMatrixBoard() {
        return this.matrixBoard;
    }

    /**
     * Authorship: Yuxuan Yang
     * @return a set of current pieces
     */
    public ArrayList<Piece> getPieces() {
        return this.pieces;
    }

    /**
     * Authorship: Yuxuan Yang
     * Determine whether a piece placement is well-formed according to the
     * following criteria:
     * - it consists of exactly four characters
     * - the first character is a valid piece descriptor character (b, B, g, G, ... y, Y)
     * - the second character is in the range 0 .. 9 (column)
     * - the third character is in the range 0 .. 4 (row)
     * - the fourth character is in valid orientation N, S, E, W
     *
     * @param piecePlacement A string describing a piece placement
     * @return True if the piece placement is well-formed
     */
    public static boolean isPiecePlacementWellFormed(String piecePlacement) {

        //verify the length
        if (piecePlacement.length() != 4) {
            return false;
        }

        char first = piecePlacement.charAt(0);
        char second = piecePlacement.charAt(1);
        char third = piecePlacement.charAt(2);
        char fourth = piecePlacement.charAt(3);

        //verify the first char
        int count = 0;
        for (char c : PIECE_TYPE) {
            if (first == c) {
                count++;
                break;
            }
        }
        if (count == 0) {
            return false;
        }

        //verify the second char
        if (second > '9' || second < '0') {
            return false;
        }

        //verify the third char
        if (third > '4' || third < '0') {
            return false;
        }

        //verify the fourth char
        switch (fourth) {
            case 'N':
            case 'S':
            case 'E':
            case 'W':
                break;
            default:
                return false;
        }

        //if satisfy all conditions then return true;
        return true;
    }

    /**
     * Authorship: Paul Won
     * Determine whether a placement string is well-formed:
     * - it consists of exactly N four-character piece placements (where N = 1 .. 10);
     * - each piece placement is well-formed
     * - no shape appears more than once in the placement
     * - the pieces are ordered correctly within the string
     *
     * @param placement A string describing a placement of one or more pieces
     * @return True if the placement is well-formed
     */
    public static boolean isPlacementWellFormed(String placement) {
        // setup for local variables
        List<Character> unordered = new ArrayList<>();
        List<Character> ordered = new ArrayList<>();
        String[] piece = placement.split("(?<=\\G.{4})");

        // check if exactly N four-char piece placements
        if (placement.length() / 4 > 10 || placement.length() % 4 != 0 || placement.equals(""))
            return false;

        // check no shape appears more than once in the placement
        for (int i = 0; i < placement.length(); i++) {
            if (i % 4 == 0) {
                // adding pieceID for future order check
                unordered.add(Character.toLowerCase(placement.charAt(i)));
                ordered.add(Character.toLowerCase(placement.charAt(i)));
                //checking duplicates
                for (int j = 0; j < placement.length(); j++) {
                    if (j % 4 == 0) {
                        if (j != i && placement.charAt(i) == placement.charAt(j))
                            return false;
                    }
                }
            }
        }

        // check each piece placement is well-formed
        for (String s : piece) {
            if (!isPiecePlacementWellFormed(s))
                return false;
        }

        // check if pieces are in correct order
        Collections.sort(ordered);
        return ordered.equals(unordered);

        // when everything passes then return true
    }

    /**
     * Authorship: Chensheng Zhang
     * @param matrix
     * @return
     */
    public static int[][] rotate_90_Clockwise(int[][] matrix) {
        int[][] temp = new int[matrix[0].length][matrix.length];
        int k = matrix.length - 1;
        for (int i = 0; i < matrix.length; i++, k--) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp[j][k] = matrix[i][j];
            }
        }
        return temp;
    }

    /**
     * Authorship: Chensheng Zhang
     * Determine whether a placement string is valid.
     * <p>
     * To be valid, the placement string must be:
     * - well-formed, and
     * - each piece placement must be a valid placement according to the
     * rules of the game:
     * - pieces must be entirely on the board
     * - pieces must not overlap each other
     *
     * @param placement A placement string
     * @return True if the placement sequence is valid
     */
    public static boolean isPlacementValid(String placement) {
        if ((isPlacementWellFormed(placement)) == false)
            return false;
        String[] piece = placement.split("(?<=\\G.{4})");
        int[][] blank_board = new int[10][5];

        for (String s : piece) {
            char first_char = s.charAt(0); // The first char stands for Type
            char fourth_char = s.charAt(3); // The fourth char stands for orientation

            int x = s.charAt(1) - 48;
            int y = s.charAt(2) - 48;    //upper left point position

            PieceType pieceType = PieceType.valueOf(Character.toString(first_char));

            int[][] temp = pieceType.getMatrixShape();
            switch (fourth_char) {
                case 'N':
                    break;
                case 'E':
                    temp = rotate_90_Clockwise(temp);
                    break;
                case 'S':
                    temp = rotate_90_Clockwise(rotate_90_Clockwise(temp));
                    break;
                case 'W':
                    temp = rotate_90_Clockwise(rotate_90_Clockwise(rotate_90_Clockwise(temp)));
                    break;

            }// Rotate the matrix shape

            int x_range = x + temp[0].length - 1; // the max value of (upper left corner x position + width of piece shape)
            int y_range = y + temp.length - 1; // the max value of (upper left corner u position + height of piece shape)

            //- pieces must be entirely on the board
            if (x_range < 0 || x_range >= 10)
                return false;
            if (y_range < 0 || y_range >= 5)
                return false;


            //- pieces must not overlap each other
            for (int i = x; i <= x_range; i++) {
                for (int j = y; j <= y_range; j++) {
                    blank_board[i][j] += temp[j - y][i - x];
                    if (blank_board[i][j] < 0 || blank_board[i][j] > 1)
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * Authorship: Paul Won
     * Given a string describing a placement of pieces, and a location
     * that must be covered by the next move, return a set of all
     * possible next viable piece placements which cover the location.
     * <p>
     * For a piece placement to be viable it must:
     * - be a well formed piece placement
     * - be a piece that is not already placed
     * - not overlap a piece that is already placed
     * - cover the location
     *
     * @param placement A starting placement string
     * @param col       The location's column.
     * @param row       The location's row.
     * @return A set of all viable piece placements, or null if there are none.
     */
    static Set<String> getViablePiecePlacements(String placement, int col, int row) {
        // split the placement string into each pieces
        String[] givenPieces = placement.split("(?<=\\G.{4})");
        // result set to return
        HashSet<String> result = new HashSet<>();
        // String HashSet to store used pieces
        HashSet<String> used = new HashSet<>();
        // create temp board for the method
        int[][] tempBoard = new int[5][10];

        // initialize the matrix board and place given pieces
        if (placement.length() != 0) {
            for (String piece : givenPieces) {
                String pieceType = "" + piece.charAt(0);
                int[][] pieceMatrix = PieceType.valueOf(pieceType).getMatrixShapeViaOrientation(Orientation.valueOf(piece.substring(3)));
                // place them on the tempBoard
                for (int i = 0; i < pieceMatrix.length; i++) {
                    for (int j = 0; j < pieceMatrix[i].length; j++) {
                        int newRow = i + Character.getNumericValue(piece.charAt(2));
                        int newCol = j + Character.getNumericValue(piece.charAt(1));
                        tempBoard[newRow][newCol] += pieceMatrix[i][j];
                    }
                }
                // add to used
                used.add(pieceType.toLowerCase());
            }
        }

        // search through all available pieces
        for (String piece: PIECE_NAME_LOWERCASE) {
            // if the piece was not used
            if (!used.contains(piece)) {
                int loop = 1;
                while (loop <= 2) {
                    // change to uppercase pieces in the second loop
                    if (loop == 2)
                        piece = piece.toUpperCase();
                    // check different orientation
                    for (Orientation orientation : Orientation.values()) {
                        int[][] pieceMatrix = PieceType.valueOf(piece).getMatrixShapeViaOrientation(orientation);
                        // i for col
                        for (int i = 0; i < pieceMatrix.length; i++) {
                            // j for row
                            for (int j = 0; j < pieceMatrix[i].length; j++) {
                                // if the sphere is present
                                if (pieceMatrix[i][j] == 1) {
                                    // update the top left location
                                    int newRow = row - i;
                                    int newCol = col - j;
                                    // check if it can be fitted then add to result if true
                                    if (isDropViable(pieceMatrix, tempBoard, newCol, newRow)) {
                                        String viable = "" + piece + newCol + newRow + orientation.toString();
                                        result.add(viable);
                                    }
                                }
                            }
                        }
                    }
                    loop++;
                }
            }
        }

        if (result.isEmpty()) {
            return null;
        }

        return result;
    }

    /**
     * Authorship: Paul Won
     * @param placement
     * @param testPiece
     * @param col
     * @param row
     * @param pieces
     * @param orientation
     * @param pieceHead
     * @param horizontal
     */
    private static void scanPieceToAdd(String placement, Piece testPiece, int col, int row, HashSet<String> pieces, String orientation, char pieceHead, boolean horizontal) {
        PieceType pieceType = testPiece.pieceType;
        int[][] pieceMatrix = testPiece.getMatrixShape();
        int spine = pieceType.getSpine();

        for (int g = 0; g < spine; g++) { // scanning spine spheres
            for (int h = 0; h < 2; h++) { // scanning protrusion spheres
                // for horizontal (E, W), update and scan col and row accordingly to position of spheres then add to pieces set if the placement is viable
                if (horizontal && pieceMatrix[g][h] == 1 && checkViablePlacement(placement + pieceType + (col - h) + (row - g) + orientation)) {
                    pieces.add("" + pieceHead + (col - h) + (row - g) + orientation);
                    // for vertical (N, S), update and scan col and row accordingly to position of spheres then add to pieces set if the placement is viable
                } else if (!horizontal && pieceMatrix[h][g] == 1 && checkViablePlacement(placement + pieceType + (col - g) + (row - h) + orientation)) {
                    pieces.add("" + pieceHead + (col - g) + (row - h) + orientation);
                }
            }
        }
    }

    /**
     * Authorship: Paul Won
     * additional method for task 6 to check if the placement is viable
     * @param newPlacement
     * @return
     */

    public static boolean checkViablePlacement(String newPlacement) {
        // split into individual pieces
        String[] placementUnordered = newPlacement.split("(?<=\\G.{4})");
        // custom sort to put in alphabetical order from A to Z ignoring case (it compares in lower cases)
        List<String> placementOrdered = new ArrayList<>(Arrays.asList(placementUnordered));
        placementOrdered.sort(String::compareToIgnoreCase);
        // above was simplified from
        /**
         * Collections.sort(placementOrdered, (String s1, String s2) -> {
         *             return s1.compareToIgnoreCase(s2);
         *         });
         */
        // ordered pieces are not put into one string to be tested
        String toTest = String.join("", placementOrdered);
        // if the ordered placement is valid, i.e. can be placed on the board, then it returns true
        return isPlacementValid(toTest);
        // otherwise false
    }


    /**
     * Authorship: Yuxuan Yang
     * Return the solution to a particular challenge.
     * @param challenge A challenge string.
     * @return A placement string describing the encoding of the solution to
     * the challenge.
     */

    static String finalSolution = "";
    public static String getSolution(String challenge) {

        HashSet<String> used = new HashSet<>();

        // implement matrix board
        int[][] matrix_board = new int[5][10];
        initializeBoard(challenge, matrix_board, used);

        search(challenge, used, matrix_board, 0);
        String ans = finalSolution;
        finalSolution = "";
        return sortString(ans);
    }

    /**
     * Authorship: Yuxuan Yang
     * place given piece beforehand --task9
     * @param placement
     * @param matrix_board
     * @param used
     */
    public static void initializeBoard(String placement, int[][] matrix_board, Set<String> used) {
        // split the placement string into each pieces
        String[] piece = placement.split("(?<=\\G.{4})");

        // initialize the matrix board and place given pieces
        for (String s : piece) {
            used.add(s.substring(0, 1).toLowerCase());
            int[][] shape = PieceType.valueOf(s.substring(0, 1)).getMatrixShapeViaOrientation(Orientation.valueOf(s.substring(3, 4)));
            dropPiece2(shape, matrix_board, Integer.parseInt(s.substring(1, 2)), Integer.parseInt(s.substring(2, 3)));
        }
    }

    /**
     * Authorship: Yuxuan Yang
     * drop a piece from the board --task9
     * @param shape
     * @param matrix_board
     * @param col
     * @param row
     */
    public static void dropPiece2(int[][] shape, int[][] matrix_board, int col, int row) {
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                matrix_board[row + i][col + j] += shape[i][j];
            }
        }
    }

    /**
     * Authorship: Yuxuan Yang
     * drag a piece from the board --task9
     * @param shape
     * @param matrix_board
     * @param col
     * @param row
     */
    public static void dragPiece2(int[][] shape, int[][] matrix_board, int col, int row) {
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] == 1) {
                    matrix_board[row + i][col + j] -= shape[i][j];
                }
            }
        }
    }


    /**
     * Authorship: Yuxuan Yang
     * get all viable pieces according to given col and row --task9
     * @param used
     * @param matrix_board
     * @param col
     * @param row
     * @return
     */
    public static Set<String> getViablePiecePlacements2(Set<String> used, int[][] matrix_board, int col, int row) {

        // record all viable pieces
        HashSet<String> result = new HashSet<>();

        //check the rest of pieces
        for (String s : PIECE_NAME_LOWERCASE) {
            if (!used.contains(s)) {
                // check lower case and upper case
                for (int k = 0; k < 2; k++) {
                    String name = s;
                    if (k == 1) {
                        name = s.toUpperCase();
                    }

                    // check different orientation
                    for (Orientation value : Orientation.values()) {
                        int[][] temp_shape = PieceType.valueOf(name).getMatrixShapeViaOrientation(value);

                        //  check all possible top-left corner location
                        for (int i = 0; i < temp_shape.length; i++) {
                            for (int j = 0; j < temp_shape[i].length; j++) {
                                if (temp_shape[i][j] != 0) {
                                    // get the top-left corner location
                                    int x = col - j;
                                    int y = row - i;

                                    // check if it can drop
                                    if (isDropViable(temp_shape, matrix_board, x, y)) {
                                        String p = name + x + "" + y + value.name();
                                        result.add(p);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (result.isEmpty()) {
            return null;
        }

        return result;
    }

    /**
     * Authorship: Yuxuan Yang
     * the main method for search a solution --task9
     * @param prePlacement
     * @param used
     * @param matrix_board
     * @param count
     */
    public static void search(String prePlacement, Set<String> used, int[][] matrix_board, int count) {

        while (matrix_board[count / 10][count % 10] == 1) {
            count++;
        }

        // get all viable pieces
        Set<String> viablePieces = getViablePiecePlacements2(used, matrix_board, count % 10, count / 10);

        if (viablePieces != null) {
            // check all viable pieces
            for (String pieceStr : viablePieces) {
                String curPlacement = prePlacement + pieceStr;

                String first = pieceStr.substring(0, 1);
                int x = Integer.parseInt(pieceStr.substring(1, 2));
                int y = Integer.parseInt(pieceStr.substring(2, 3));
                String orientation = pieceStr.substring(3, 4);

                String first_lowerCase = first.toLowerCase();
                used.add(first_lowerCase);

                int[][] shape = PieceType.valueOf(first).getMatrixShapeViaOrientation(Orientation.valueOf(orientation));

                // drop a piece to the board
                dropPiece2(shape, matrix_board, x, y);

                // check if it fill out all the indents
                if (checkWin(matrix_board)) {
                    finalSolution = curPlacement;
                    return;
                } else {
                    search(curPlacement, used, matrix_board, count + 1);
                    if (!finalSolution.isEmpty()) {
                        return;
                    }
                    dragPiece2(shape, matrix_board, x, y);
                    used.remove(first_lowerCase);
                }

            }

        } else {
            return;
        }

    }

    /**
     * Authorship: Yuxuan Yang
     * to check if find the solution -- task9
     * @param matrix_board
     * @return
     */
    public static boolean checkWin(int[][] matrix_board) {
        int count = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                count += matrix_board[i][j];
            }
        }
        return count == 50;
    }

    /**
     * Authorship: Yuxuan Yang
     * to sort the solution --task9
     * @param placement
     * @return
     */
    public static String sortString(String placement) {

        String[] piece = placement.split("(?<=\\G.{4})");
        ArrayList<String> tmp = new ArrayList<>();
        tmp.addAll(Arrays.asList(piece));

        Collections.sort(tmp, (String str1, String str2) -> {
            return str1.substring(0, 1).toLowerCase().compareTo(str2.substring(0, 1).toLowerCase());
        });

        String result = "";
        for (String s : tmp) {
            result += s;
        }
        return result;
    }

    /**
     * Authorship: Yuxuan Yang
     * This method is trying to generate interesting games Task11
     * @return
     */
    static int generateTimes = 0;
    static String new_game_solution = "";
    static int solution_count = 0;
    public static String generateGames() {
//        loadAllPieceOptions();
        loadExistSolution();
        Random random = new Random();
        generateTimes = 0;
        boolean isExisted = false;
        String solution = "";

        // new a empty board
        int[][] matrix_board = new int[5][10];
        // record the piece has been placed on board
        Set<String> used = new HashSet<>();


        int count = 0;
        String piece_placement = "";

        // check if find a solution and the solution is the only one and doesn't exist in games[]
        while (solution.isEmpty() || solution_count > 1 || isExisted) {
            isExisted = false;
            piece_placement = "";
            solution_count = 0;
            while (count < 3) {
                String piece_name = "";
                int[][] shape = null;
                int x = 0;
                int y = 0;
                Orientation orientation = null;

                // choose its piece type
                int piece_id = random.nextInt(10);
                piece_name = PIECE_NAME_LOWERCASE[piece_id];
                while (used.contains(piece_name)) {
                    piece_id = random.nextInt(10);
                    piece_name = PIECE_NAME_LOWERCASE[piece_id];
                }
                while (true) {
                    // choose its shape
                    int isFlipped = random.nextInt(2);
                    if (isFlipped == 1) {
                        piece_name = piece_name.toUpperCase();
                    }
                    int itsOrientation = random.nextInt(4);

                    switch (itsOrientation) {
                        case 1:
                            orientation = Orientation.E;
                            break;
                        case 2:
                            orientation = Orientation.S;
                            break;
                        case 3:
                            orientation = Orientation.W;
                            break;
                        default:
                            orientation = Orientation.N;
                    }
                    shape = PieceType.valueOf(piece_name).getMatrixShapeViaOrientation(orientation);

                    // choose its location/ top-left corner

                    x = random.nextInt(9);
                    y = random.nextInt(4);
                    boolean canDrop = isDropViable(shape, matrix_board, x, y);
                    int count_try = 0;

                    while (!canDrop && count_try < 30) {
                        x = random.nextInt(9);
                        y = random.nextInt(4);
                        canDrop = isDropViable(shape, matrix_board, x, y);
                        count_try++;
                    }

                    if (canDrop == true) {
                        break;
                    }
                }

                used.add(piece_name.toLowerCase());
                piece_name += x + "" + y + orientation.name();

                // place the piece
                dropPiece2(shape, matrix_board, x, y);
                count++;
                piece_placement += piece_name;
            }

//             place three pieces on board
//            while (count < 3) {
//
//                int no = random.nextInt(2880);
//                String piece = PIECES_OPTIONS[no];
//                if (used.contains(piece)) {
//                    continue;
//                }
//
//                int col = Integer.parseInt(piece.substring(1, 2));
//                int row = Integer.parseInt(piece.substring(2, 3));
//                int[][] shape = PieceType.valueOf(piece.substring(0, 1)).getMatrixShapeViaOrientation(Orientation.valueOf(piece.substring(3, 4)));
//                boolean canDrop = isDropViable(shape, matrix_board, col, row);
////                System.out.println(canDrop);
//
//                if (canDrop) {
//                    dropPiece2(shape, matrix_board, col, row);
//                    count++;
//                    piece_placement += piece;
//                    used.add(piece);
//                }
//
//            }

            generateTimes++;
            solution = getSolution2(piece_placement);

            if (EXIST_GAMES_SOLUTIONS.contains(solution)) {
                System.out.println(EXIST_GAMES_SOLUTIONS.size());
                isExisted = true;
            }

            matrix_board = new int[5][10];
            count = 0;
            used.clear();
        }
        System.out.println("game: " + piece_placement);
        System.out.println("solution: " + solution);
        return solution;
    }


    /**
     * Authorship: Yuxuan Yang
     * search a new game --task11
     * @param prePlacement
     * @param used
     * @param matrix_board
     * @param count
     */
    public static void search2(String prePlacement, Set<String> used, int[][] matrix_board, int count) {

        while (matrix_board[count / 10][count % 10] == 1) {
            count++;
        }

        // get all viable pieces
        Set<String> viablePieces = getViablePiecePlacements2(used, matrix_board, count % 10, count / 10);

        if (viablePieces != null) {

            // check all viable pieces
            for (String pieceStr : viablePieces) {
                String curPlacement = prePlacement + pieceStr;

                String first = pieceStr.substring(0, 1);
                int x = Integer.parseInt(pieceStr.substring(1, 2));
                int y = Integer.parseInt(pieceStr.substring(2, 3));
                String orientation = pieceStr.substring(3, 4);

                String first_lowerCase = first.toLowerCase();
                used.add(first_lowerCase);

                int[][] shape = PieceType.valueOf(first).getMatrixShapeViaOrientation(Orientation.valueOf(orientation));

                // drop a piece to the board
                dropPiece2(shape, matrix_board, x, y);

                // check if it fill out all the indents
                if (checkWin(matrix_board)) {
                    new_game_solution = curPlacement;
                    solution_count++;
                    return;
                } else {
                    search2(curPlacement, used, matrix_board, count + 1);
                    dragPiece2(shape, matrix_board, x, y);
                    used.remove(first_lowerCase);
                    if (solution_count > 1) {
                        return;
                    }
                }
            }

        } else {
            return;
        }
    }


    /**
     * Authorship: Yuxuan Yang
     * get solutions --task11
     * @param challenge
     * @return
     */
    public static String getSolution2(String challenge) {
        new_game_solution = "";
        solution_count = 0;

        HashSet<String> used = new HashSet<>();
        // implement matrix board
        int[][] matrix_board = new int[5][10];
        initializeBoard(challenge, matrix_board, used);
        search2(challenge, used, matrix_board, 0);

        return sortString(new_game_solution);
    }

    /**
     * Authorship: Yuxuan Yang
     * get all existed solutions from games --task11
     */
    public static void loadExistSolution() {
        for (Games g : Games.SOLUTIONS) {
            EXIST_GAMES_SOLUTIONS.add(g.placement);
        }
    }

    public static void main(String[] args) {
        long s = System.currentTimeMillis();
        for (int i = 0; i < 50; i++) {
            generateGames();
        }
        long e = System.currentTimeMillis();
        System.out.println(e-s);
    }
}
