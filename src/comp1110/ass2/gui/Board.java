package comp1110.ass2.gui;

import comp1110.ass2.*;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.*;


public class Board extends Application {

    private static final int BOARD_WIDTH = 933;                 // the width of the stage
    private static final int BOARD_HEIGHT = 700;                // the height of the stage
    private static final double BOARD_X = 0;            // the x of top left corner of board
    private static final double BOARD_Y = 0;           // the y of top left corner of board
    private static final double BOARD_MARGIN_X = 37;            // the x of top left corner of grid
    private static final double BOARD_MARGIN_Y = 20;           // the y of top left corner of grid
    private static final double BOARD_GRID_SIZE = 45.5;          // the side length of each grid on board
    private static final double IMAGE_BOARD_WIDTH = 533;        // the width of the board
    private static final double IMAGE_PIECE_HEIGHT = 91;        // the height of the board
    private static final double PLACEMENT_AREA_X = 15;          // the x of top left corner of the placement area
    private static final double PLACEMENT_AREA_Y = 270;         // the y of top left corner of the placement area
    private static final double PLACEMENT_AREA_WIDTH = 500;     // the width of placement area
    private static final double PLACEMENT_AREA_HEIGHT = 280;    // the height of placement area
    private static final double PLACEMENT_AREA_SCALE = 0.8;
    private static final double GAME_AREA_WIDTH = 633;
    private static final double GAME_AREA_HEIGHT = 550;
    private static final double MARGINAL_ERROR = 10;
    private static final double TOP_BAR_HEIGHT = 30;
    private static final String URI_BASE = "comp1110/ass2/gui/assets/";
    private static String URI_RECORD = "data/";
    private static final String[] IMAGE_NAME = {"B", "G", "I", "L", "N", "O", "P", "R", "S", "Y"};
    private static String currentPreview = "";
    private boolean hold_on;
    private boolean revealHints;
    private MediaPlayer mediaPlayer;
    private MediaPlayer successMP;
    private MediaPlayer selectMP;
    private boolean musicPaused=false;
    private static Rectangle progressBar = new Rectangle(0, 0, 0, 30);
    private static long BEGIN_TIME;
    private static TextField textField = new TextField();
    /* game mode */
    private boolean isGenerated = false;
    private boolean isRandom = false;

    /* Archives information*/
    private static ArrayList<String> RECORD_PLAYERS = new ArrayList<>();
    private static String CURRENT_PLAYER = "null";
    private static int CURRENT_STARS = -1;
    private static int CURRENT_LEVEL = -1;
    private static Text stars_selectPage = new Text();
    private static Text stars_playPage = new Text();
    private static int[] RECORD_STARTER = new int[24];
    private static int[] RECORD_JUNIOR = new int[24];
    private static int[] RECORD_EXPERT = new int[24];
    private static int[] RECORD_MASTER = new int[24];
    private static int[] RECORD_WIZARD = new int[24];

    /* pieces are pre-placed, record its lowercase name */
    private static final HashSet<String> used = new HashSet<>();

    /* page container */
    private final AnchorPane root = new AnchorPane();
    private final Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT);

    /* all pages in the scene */
    private final Group homePage = new Group();
    private final Group playerPage = new Group();
    private final Group createPage = new Group();
    private final Group playPage = new Group();
    private final Group settingsPage = new Group();
    private final Group selectPage = new Group();
    private final Group waitingPage = new Group();
    private final Group winPage = new Group();

    /* Layouts */
    private final VBox vbSetting = new VBox();
    private final VBox vbHome = new VBox();
    private final VBox vbPlayers = new VBox();
    private final VBox vbSelect = new VBox();
    private final VBox vbWait = new VBox();
    private final VBox vbWin = new VBox();
    private final GridPane gpSTARTER = new GridPane();
    private final GridPane gpJUNIOR = new GridPane();
    private final GridPane gpEXPERT = new GridPane();
    private final GridPane gpMASTER = new GridPane();
    private final GridPane gpWIZARD = new GridPane();
    private final Group gpEXPLORE = new Group();
    private Group gpLEVEL = new Group();
    private final AnchorPane apLeft = new AnchorPane();
    private final AnchorPane apRight = new AnchorPane();

    // for task 10, create a new ap
    private final AnchorPane apHint = new AnchorPane();

    /* Nodes */
    private final AnchorPane guiBoard = new AnchorPane();
    private final Group guiPieces = new Group();
    private final Group topBar = new Group();
    private final Group view = new Group();
    private final Text title = new Text(0, 0, "IQ-Fit");
    private final Group showHint = new Group();
    private final Polygon[] stars = new Polygon[3];
    private final Text TIME_COST = new Text();


    /* UI effect */
    private final Font font = new Font("Times new Roman", 30.0);
    private final Color color_background_scene = Color.valueOf("#252525");
    private final Color color_background_topBar = Color.valueOf("#202020");
    private final Color color_button_default = Color.valueOf("#354A5F");
    private final Color color_button_highlight = Color.valueOf("#cd863b");
    private final Color color_text_default = Color.valueOf("#808080");
    private final Color color_text_highlight = Color.valueOf("#e1e1e1");
    private final Color color_background_transparent = Color.valueOf("#ffffff00");
    private final ScaleTransition progress_bar_scale_trans = new ScaleTransition();
    private final ScaleTransition[] star_effect = new ScaleTransition[3];
    private final DropShadow pieceShadow_E = new DropShadow();
    private final DropShadow pieceShadow_N = new DropShadow();
    private final DropShadow pieceShadow_S = new DropShadow();
    private final DropShadow pieceShadow_W = new DropShadow();
    private final DropShadow boardShadow = new DropShadow();

    /* set the drop shadow effect for piece and board */ {
        boardShadow.setColor(Color.BLACK);
        boardShadow.setOffsetY(5);
        boardShadow.setBlurType(BlurType.GAUSSIAN);
        boardShadow.setRadius(10);
        pieceShadow_N.setColor(color_background_topBar);
        pieceShadow_N.setOffsetY(5);
        pieceShadow_N.setBlurType(BlurType.GAUSSIAN);
        pieceShadow_E.setColor(color_background_topBar);
        pieceShadow_E.setOffsetX(5);
        pieceShadow_E.setBlurType(BlurType.GAUSSIAN);
        pieceShadow_S.setColor(color_background_topBar);
        pieceShadow_S.setOffsetY(-5);
        pieceShadow_S.setBlurType(BlurType.GAUSSIAN);
        pieceShadow_W.setColor(color_background_topBar);
        pieceShadow_W.setOffsetX(-5);
        pieceShadow_W.setBlurType(BlurType.GAUSSIAN);
    }


    /* the difficulty of the game */
    private int difficulty = 0;

    /* The underlying game */
    public static FitGame fitGame;
    public static Games interestingGame = null;

    /* temporary variable */
    double previousX = 0;
    double previousY = 0;

    /**
     * Authorship: Paul Won
     * Add Background Music
     */
    public void playMusic() {
        mediaPlayer = new MediaPlayer(new Media(Board.class.getResource("assets/dream.mp3").toExternalForm()));

        // add an indefinite loop
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }


    /**
     * Authorship: Paul Won
     * get the piece type --task10
     */
    private PieceType getPieceType(String pieceHead) {
        if (pieceHead.equals(PieceType.B.name()))
            return PieceType.B;
        else if (pieceHead.equals(PieceType.b.name()))
            return PieceType.b;
        else if (pieceHead.equals(PieceType.o.name()))
            return PieceType.o;
        else if (pieceHead.equals(PieceType.O.name()))
            return PieceType.O;
        else if (pieceHead.equals(PieceType.p.name()))
            return PieceType.p;
        else if (pieceHead.equals(PieceType.P.name()))
            return PieceType.P;
        else if (pieceHead.equals(PieceType.r.name()))
            return PieceType.r;
        else if (pieceHead.equals(PieceType.R.name()))
            return PieceType.R;
        else if (pieceHead.equals(PieceType.s.name()))
            return PieceType.s;
        else if (pieceHead.equals(PieceType.S.name()))
            return PieceType.S;
        else if (pieceHead.equals(PieceType.y.name()))
            return PieceType.y;
        else if (pieceHead.equals(PieceType.Y.name()))
            return PieceType.Y;
        else if (pieceHead.equals(PieceType.g.name()))
            return PieceType.g;
        else if (pieceHead.equals(PieceType.G.name()))
            return PieceType.G;
        else if (pieceHead.equals(PieceType.i.name()))
            return PieceType.i;
        else if (pieceHead.equals(PieceType.I.name()))
            return PieceType.I;
        else if (pieceHead.equals(PieceType.l.name()))
            return PieceType.l;
        else if (pieceHead.equals(PieceType.L.name()))
            return PieceType.L;
        else if (pieceHead.equals(PieceType.n.name()))
            return PieceType.n;
        else if (pieceHead.equals(PieceType.N.name()))
            return PieceType.N;
        else
            return null;
    }

    /**
     * Authorship: Paul Won
     * get the orientation --task10
     */
    private Orientation getOrientation(String piece) {
        String orientation = "" + piece.charAt(3);
        switch (orientation) {
            case ("N"):
                return Orientation.N;
            case ("S"):
                return Orientation.S;
            case ("E"):
                return Orientation.E;
            case ("W"):
                return Orientation.W;
            default:
                return null;
        }
    }

    /**
     * Authorship: Paul Won
     * get hints --task10
     */
    private HashSet<PieceType> implementHint() {
        String placement = fitGame.getPlacement();
        String hintPieces = FitGame.getSolution(placement);
        HashSet<PieceType> pieceTypes = new HashSet<>();
        for (int i = 0; i < hintPieces.length(); i++) {
            if (i % 4 == 0) {
                String target = Character.toString(hintPieces.charAt(i));
                if (target.equals(PieceType.B.name()) || target.equals(PieceType.b.name())) {
                    pieceTypes.add(PieceType.B);
                    pieceTypes.add(PieceType.b);
                } else if (target.equals(PieceType.o.name()) || target.equals(PieceType.O.name())) {
                    pieceTypes.add(PieceType.o);
                    pieceTypes.add(PieceType.O);
                } else if (target.equals(PieceType.p.name()) || target.equals(PieceType.P.name())) {
                    pieceTypes.add(PieceType.p);
                    pieceTypes.add(PieceType.P);
                } else if (target.equals(PieceType.r.name()) || target.equals(PieceType.R.name())) {
                    pieceTypes.add(PieceType.r);
                    pieceTypes.add(PieceType.R);
                } else if (target.equals(PieceType.s.name()) || target.equals(PieceType.S.name())) {
                    pieceTypes.add(PieceType.s);
                    pieceTypes.add(PieceType.S);
                } else if (target.equals(PieceType.y.name()) || target.equals(PieceType.Y.name())) {
                    pieceTypes.add(PieceType.y);
                    pieceTypes.add(PieceType.Y);
                } else if (target.equals(PieceType.g.name()) || target.equals(PieceType.G.name())) {
                    pieceTypes.add(PieceType.g);
                    pieceTypes.add(PieceType.G);
                } else if (target.equals(PieceType.i.name()) || target.equals(PieceType.I.name())) {
                    pieceTypes.add(PieceType.i);
                    pieceTypes.add(PieceType.I);
                } else if (target.equals(PieceType.l.name()) || target.equals(PieceType.L.name())) {
                    pieceTypes.add(PieceType.l);
                    pieceTypes.add(PieceType.L);
                } else if (target.equals(PieceType.n.name()) || target.equals(PieceType.N.name())) {
                    pieceTypes.add(PieceType.n);
                    pieceTypes.add(PieceType.N);
                }
            }
        }
        return pieceTypes;
    }

    /**
     * Authorship:Paul Won, Yuxuan Yang, Chensheng Zhang
     * In this method, it will load all pages for later usage, and launch the GUI.
     */
    @Override
    public void start(Stage stage) throws FileNotFoundException, MalformedURLException {

        // initialise the uri
        String  temp = Board.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        int index = temp.indexOf("COMP6710-ass2-group-project");
        URI_RECORD = temp.substring(0,index)+"COMP6710-ass2-group-project/data/";

        // initialise music
        successMP = new MediaPlayer(new Media(Board.class.getResource("assets/success3.mp3").toExternalForm()));
        selectMP = new MediaPlayer(new Media(Board.class.getResource("assets/decision.mp3").toExternalForm()));
        selectMP.setCycleCount(MediaPlayer.INDEFINITE);


        // set scene
        scene.setFill(color_background_scene);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            switch (event.getCode()) {
                case Q:
                    if (scene.getFocusOwner() == null || scene.getFocusOwner() == playPage) {
                        Platform.exit();
                    }
                    break;
                case M:
                    if (scene.getFocusOwner() == null || scene.getFocusOwner() == playPage) {
                        if (musicPaused) {
                                mediaPlayer.play();
                            musicPaused = false;
                        } else {
                                mediaPlayer.pause();
                            musicPaused = true;
                        }
                    }
                    break;
                default:
                    break;
            }
        });
        setTopBar(stage);
        root.setStyle("-fx-background-color:#252525");

        // load all the pages
        readPlayers();
        loadHomePage();
        LoadPlayerPage();
        loadSelectPage();
        loadCreatePage();
        loadWaitingPage();
        loadPlayPage();
        loadSettingsPage();
        loadWinPage();

        // show first page
        showHomePage();

        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("Smart Games");
        stage.show();
    }

    /**
     * Authorship: Yuxuan Yang
     * The top bar has close button and it controls the location of the stage on computer screen
     * @param stage
     */
    private void setTopBar(Stage stage) {

        // the background of the top bar
        Rectangle bg = new Rectangle(0, 0, BOARD_WIDTH, TOP_BAR_HEIGHT);
        bg.setFill(color_background_topBar);

        //Close window button
        Group close_stage = new Group();
        Rectangle container = new Rectangle(0, 0, 50, TOP_BAR_HEIGHT);
        container.setFill(color_background_transparent);
        Text content = new Text("×");
        content.setFill(color_text_default);
        content.setFont(font);
        content.setTextAlignment(TextAlignment.CENTER);
        content.setTextOrigin(VPos.TOP);
        content.setWrappingWidth(50);
        Bounds bounds = content.getBoundsInParent();
        double contentWidth = bounds.getWidth();
        double contentHeight = bounds.getHeight();
        content.setLayoutX((50 - contentWidth) / 2);
        content.setLayoutY((TOP_BAR_HEIGHT - contentHeight) / 2);
        close_stage.setLayoutX(BOARD_WIDTH - 50);
        close_stage.setOnMouseEntered(mouseEvent -> {
            close_stage.setCursor(Cursor.HAND);
            FadeTransition ft = new FadeTransition();
            ft.setNode(container);
            ft.setFromValue(0);
            ft.setFromValue(1);
            ft.setDuration(Duration.millis(50));
            ft.setOnFinished(actionEvent -> {
                container.setFill(color_button_default);
                content.setFill(color_text_highlight);
            });
            ft.play();

        });
        close_stage.setOnMouseExited(mouseEvent -> {
            container.setFill(color_background_transparent);
            content.setFill(color_text_default);
        });
        close_stage.setOnMouseReleased(mouseEvent -> {
            try {
                if (!CURRENT_PLAYER.equals("null")) {
                    saveArchives(CURRENT_PLAYER);
                }
                if (RECORD_PLAYERS.size() != 0) {
                    savePlayers();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Platform.exit();
        });

        // through top bar to drag the stage
        topBar.setOnMousePressed(mouseEvent -> {
            topBar.requestFocus();
            previousX = mouseEvent.getScreenX();
            previousY = mouseEvent.getScreenY();
        });

        topBar.setOnMouseDragged(mouseEvent ->
        {
            double moveX = mouseEvent.getScreenX() - previousX;
            double moveY = mouseEvent.getScreenY() - previousY;
            stage.setX(stage.getX() + moveX);
            stage.setY(stage.getY() + moveY);
            previousX = mouseEvent.getScreenX();
            previousY = mouseEvent.getScreenY();
        });
        close_stage.getChildren().addAll(container, content);
        topBar.getChildren().addAll(bg, close_stage);
        root.getChildren().add(topBar);
    }

    /**
     * Authorship: Yuxuan Yang, Paul Won (media player)
     * The HomePage is the first page in the GUI
     * - Once select "challenge" and press "start" button it will show the challenge selection button,
     * - Once press "exit" button, it will exit the GUI
     * Including load/show/remove this page
     */
    private void loadHomePage() throws FileNotFoundException {
        // start game music
        playMusic();

        // set  background & title
        Group bg_title = new Group();
        ImageView bg = new ImageView(new Image(URI_BASE + "bg.png"));
        bg.setSmooth(true);
        bg.setFitWidth(533);
        bg.setPreserveRatio(true);

        TranslateTransition tt = new TranslateTransition();
        tt.setNode(bg);
        tt.setFromY(0);
        tt.setToY(100);
        tt.setDuration(Duration.millis(3000));
        tt.setCycleCount(-1);
        tt.setAutoReverse(true);
        tt.play();

        bg_title.setLayoutX(200);
        bg_title.setLayoutY(100);
        bg_title.prefWidth(533);
        bg_title.prefHeight(500);
        bg_title.setClip(title);

        title.setFill(color_text_highlight);
        title.setFont(new Font("Arial Black", 150));
        title.setLayoutX(0);
        title.setLayoutY(200);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setWrappingWidth(533);
        bg_title.getChildren().addAll(bg);

        // set function buttons
        FunctionButton new_game = new FunctionButton("NEW GAME");
        FunctionButton old_game = new FunctionButton("LOAD");
        FunctionButton exit = new FunctionButton("EXIT");

        new_game.setOnMouseClicked(mouseEvent -> {
            Animation a = fadeEffectForNode(homePage, 300);
            a.setOnFinished(actionEvent -> {
                removeHomePage();
                showCreatePage();
            });

        });
        old_game.setOnMouseClicked(mouseEvent -> {
            Animation a = fadeEffectForNode(homePage, 500);
            a.setOnFinished(actionEvent -> {
                removeHomePage();
                try {
                    showPlayerPage();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        });
        exit.setOnMouseClicked(mouseEvent -> {
            try {
//                if (!CURRENT_PLAYER.equals("null")) {
//                    saveArchives(CURRENT_PLAYER);
//                }
                if (RECORD_PLAYERS.size() != 0) {
                    savePlayers();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                System.out.println("exit");
                Platform.exit();
            }

        });


        // the layout of vbHome
        vbHome.setAlignment(Pos.CENTER);
        vbHome.setSpacing(20);
        vbHome.getChildren().addAll(new_game, old_game, exit);
        vbHome.setPrefHeight(BOARD_HEIGHT - TOP_BAR_HEIGHT);
        vbHome.setPrefWidth(233);
        vbHome.setLayoutX(350);
        vbHome.setLayoutY(130);

        homePage.getChildren().addAll(vbHome, bg_title);
    }

    private void showHomePage() {
        homePage.setOpacity(1);
        root.getChildren().add(homePage);
    }

    private void removeHomePage() {
        root.getChildren().remove(homePage);
    }

    /**
     * Authorship: Yuxuan Yang
     * This page allows players to read their own archives.
     * @throws FileNotFoundException
     */
    private void LoadPlayerPage() {
        // set info board
        Rectangle r = new Rectangle(0, 0, 533, 400);
        r.setFill(color_background_topBar);
        r.setOpacity(0.8);
        r.setLayoutY(150);
        r.setLayoutX((933 - r.getBoundsInParent().getWidth()) / 2);
        playerPage.getChildren().add(r);

        // set title
        Text title = new Text("SELECT SAVE GAME");
        title.setWrappingWidth(333);
        title.setFont(new Font("Arial black", 24));
        title.setTextAlignment(TextAlignment.CENTER);
        title.setTextOrigin(VPos.TOP);
        title.setLayoutX((BOARD_WIDTH - title.getBoundsInParent().getWidth()) / 2);
        title.setLayoutY(190);
        title.setFill(color_text_highlight);


        //play && delete button
        HBox hBox = new HBox();
        hBox.maxWidth(383);
        hBox.minWidth(383);
        FunctionButton play = new FunctionButton("PLAY");
        FunctionButton delete = new FunctionButton("DELETE");
        FunctionButton back = new FunctionButton("BACK");
        delete.setLayoutX((BOARD_WIDTH - play.getBoundsInParent().getWidth()) / 2);
        hBox.getChildren().addAll(play, back, delete);
        hBox.setScaleX(0.8);
        hBox.setScaleY(0.8);
        hBox.setLayoutY(470);
        hBox.setLayoutX(160);
        hBox.setSpacing(10);
        delete.setOnMouseClicked(mouseEvent -> {
            RECORD_PLAYERS.remove(CURRENT_PLAYER + " " + CURRENT_STARS);

            // remove record from players.txt and delete "CURRENT_PLAYER".txt
            File f = new File(URI_RECORD + CURRENT_PLAYER + ".txt");
            f.delete();
            System.out.println("deleted " +CURRENT_PLAYER+".text");
            CURRENT_PLAYER="null";
            try {
                savePlayers();
                removePlayerPage();
                showPlayerPage();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        play.setOnMouseClicked(mouseEvent -> {
            Animation a = fadeEffectForNode(playerPage, 300);
            a.setOnFinished(actionEvent -> {
                removePlayerPage();

                // if can read an archive
                if (!CURRENT_PLAYER.equals("null")) {
                    try {
                        readArchives(CURRENT_PLAYER);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    showSelectPage();
                } else {
                    showCreatePage();
                }
            });
        });
        back.setOnMouseClicked(mouseEvent -> {
            removeCreatePage();
            removePlayerPage();
            showHomePage();
        });
        // the layout of vbHome
        vbPlayers.setAlignment(Pos.CENTER);
        vbPlayers.setMaxSize(343, BOARD_HEIGHT - TOP_BAR_HEIGHT);
        vbPlayers.setMinSize(343, BOARD_HEIGHT - TOP_BAR_HEIGHT);
        vbPlayers.setLayoutX(305);
        vbPlayers.setLayoutY(10);
        playerPage.getChildren().addAll(title, vbPlayers, hBox);
    }

    private void showPlayerPage() throws FileNotFoundException {
        readPlayers();
        vbPlayers.getChildren().clear();
        for (String s : RECORD_PLAYERS) {
            String[] temp = s.split("\\s");
            String name ="";
            for (int i=0; i<temp.length-1; i++){
                if (i==temp.length-2){
                    name += temp[i];
                }else {
                    name += temp[i]+" ";
                }
            }
            PlayerInfo p = new PlayerInfo(name, Integer.parseInt(temp[temp.length-1]));
            vbPlayers.getChildren().addAll(p);
        }

        for (int i = 0; i < 3 - RECORD_PLAYERS.size(); i++) {
            Group p = new Group();

            Rectangle rr = new Rectangle(0, 0, 333, 50);
            rr.setFill(color_button_default);
            rr.setOpacity(0.5);
            rr.setArcWidth(10);
            rr.setArcHeight(10);
            rr.setEffect(pieceShadow_N);
            Text tt = new Text("+");
            tt.setFill(color_text_default);
            tt.setFont(new Font("Arial black", 30));
            tt.setWrappingWidth(333);
            tt.setTextOrigin(VPos.TOP);
            tt.setTextAlignment(TextAlignment.CENTER);
            tt.setLayoutY((50 - tt.getBoundsInParent().getHeight()) / 2);


            p.getChildren().addAll(rr, tt);
            p.setEffect(pieceShadow_N);
            p.setTranslateX(-8);
            p.setOnMouseEntered(mouseEvent1 -> {
                p.setCursor(Cursor.HAND);
                rr.setFill(Color.valueOf("#3f4c59"));
                tt.setFill(color_text_highlight);
                tt.setScaleX(1.1);
                tt.setScaleY(1.1);
            });
            p.setOnMouseExited(mouseEvent1 -> {
                rr.setFill(color_button_default);
                tt.setFill(color_text_default);
                tt.setScaleX(1);
                tt.setScaleY(1);
            });
            p.setOnMouseClicked(mouseEvent1 -> {
                Animation a = fadeEffectForNode(playerPage, 300);
                a.setOnFinished(actionEvent -> {
                    removePlayerPage();
                    showCreatePage();
                });
            });
            vbPlayers.getChildren().add(p);
        }

        playerPage.setOpacity(1);
        root.getChildren().add(playerPage);
    }

    private void removePlayerPage() {
        root.getChildren().remove(playerPage);
    }

    /**
     * Authorship: Yuxuan Yang
     * This page is designed for create a player
     * Including load/show/remove this page
     */
    private void loadCreatePage() {
        Rectangle r = new Rectangle(0, TOP_BAR_HEIGHT, BOARD_WIDTH, BOARD_HEIGHT - TOP_BAR_HEIGHT);
        r.setFill(color_background_scene);
        Text t = new Text("Hi, wise man! Please enter your name.");
        t.setWrappingWidth(333);
        t.setFont(new Font("Arial black", 15));
        t.setTextAlignment(TextAlignment.CENTER);
        t.setTextOrigin(VPos.TOP);
        t.setLayoutX(300);
        t.setLayoutY(280);
        t.setFill(color_text_highlight);

        textField.setFocusTraversable(true);
        textField.setMaxWidth(333);
        textField.setMinWidth(333);
        textField.setFont(new Font("Arial Black", 20));
        Background bg = new Background(new BackgroundFill(color_button_highlight, new CornerRadii(10), new Insets(0)));
        textField.setBackground(bg);
        textField.setStyle("-fx-text-inner-color: #e1e1e1");

        textField.setLayoutY(330);
        textField.setLayoutX(300);

        HBox button = new HBox();
        FunctionButton play = new FunctionButton("PLAY");
        FunctionButton back = new FunctionButton("BACK");
        play.setOnMouseClicked(mouseEvent -> {
            CURRENT_PLAYER = textField.getText();
            boolean exist = false;
            for (String recordPlayer : RECORD_PLAYERS) {
                if (recordPlayer.contains(CURRENT_PLAYER)) {
                    exist = true;
                    CURRENT_STARS = Integer.parseInt(recordPlayer.substring(recordPlayer.indexOf(" ") + 1));
                    System.out.println(CURRENT_STARS);
                    break;
                }
            }

            if (exist) {
                try {
                    readArchives(CURRENT_PLAYER);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                stars_selectPage.setText("" + CURRENT_STARS);
                removeCreatePage();
                showSelectPage();
            } else {
                CURRENT_STARS = 0;
                stars_selectPage.setText("" + CURRENT_STARS);
                RECORD_MASTER = new int[24];
                RECORD_STARTER = new int[24];
                RECORD_EXPERT = new int[24];
                RECORD_WIZARD = new int[24];
                RECORD_JUNIOR = new int[24];
                RECORD_STARTER[0] = 9;
                System.out.println(CURRENT_PLAYER);
                if (RECORD_PLAYERS.size() == 3) {
                    RECORD_PLAYERS.set(2, CURRENT_PLAYER + " " + CURRENT_STARS);
                } else {
                    RECORD_PLAYERS.add(CURRENT_PLAYER + " " + CURRENT_STARS);
                }
                try {
                    savePlayers();
                    saveArchives(CURRENT_PLAYER);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Animation a = fadeEffectForNode(createPage, 300);
                a.setOnFinished(actionEvent -> {
                    removeCreatePage();
                    showSelectPage();
                });
            }
        });
        back.setOnMouseClicked(mouseEvent -> {
            removeCreatePage();
            removePlayerPage();
            showHomePage();
        });

        button.getChildren().addAll(play, back);
        button.setScaleX(0.8);
        button.setScaleY(0.8);
        button.setSpacing(10);
        button.setLayoutX(260);
        button.setLayoutY(400);
        createPage.getChildren().addAll(r, t, textField, button);
        createPage.setOnMouseClicked(mouseEvent -> {
            createPage.requestFocus();
        });
    }

    private void showCreatePage() {
        textField.clear();
        createPage.setOpacity(1);
        root.getChildren().addAll(createPage);
    }

    private void removeCreatePage() {
        root.getChildren().remove(createPage);
    }


    /**
     * Authorship: Yuxuan Yang
     * The selecting Page is the second page in the GUI, and it show five challenges selections as well as a selection of generating an interesting game
     * In this page, there are 6 buttons
     * Including load/show/remove this page
     */
    private void loadSelectPage() {
        Rectangle select_bg = new Rectangle(0, 0, 533, 400);
        select_bg.setLayoutX(250);
        select_bg.setLayoutY(150);
        select_bg.setFill(color_background_topBar);
        select_bg.setEffect(boardShadow);


        // challenge container‘s title
        Text t = new Text("LEVEL: STARTER");
        t.setWrappingWidth(433);
        t.setFont(new Font("Arial black", 25));
        t.setTextAlignment(TextAlignment.CENTER);
        t.setTextOrigin(VPos.TOP);
        t.setLayoutX(350);
        t.setLayoutY(170);
        t.setFill(color_text_highlight);


        SelectionButton starter = new SelectionButton("STARTER");
        SelectionButton junior = new SelectionButton("JUNIOR");
        SelectionButton expert = new SelectionButton("EXPERT");
        SelectionButton master = new SelectionButton("MASTER");
        SelectionButton wizard = new SelectionButton("WIZARD");
        SelectionButton generate = new SelectionButton("EXPLORE");

        // generate interesting game
        Group confirm = new Group();
        Text notice = new Text("Bravely wise man，are you ready to challenge the unknowns?" +
                "\nThis will give you extra reward! ");
        notice.setTextAlignment(TextAlignment.CENTER);
        notice.setTextOrigin(VPos.TOP);
        notice.setWrappingWidth(433);
        notice.setFill(color_text_highlight);
        notice.setFont(new Font("Arial black", 18));
        SelectionButton ok = new SelectionButton("OK");
        notice.setLayoutX(350);
        notice.setLayoutY(300);
        confirm.getChildren().addAll(ok);
        confirm.setScaleY(0.6);
        confirm.setScaleX(0.6);
        confirm.setLayoutX(450);
        confirm.setLayoutY(380);
        confirm.setOnMouseEntered(mouseEvent -> {
            for (Node child : confirm.getChildren()) {
                System.out.println("hi");
                if (child instanceof SelectionButton) {
                    ((SelectionButton) child).r.setFill(color_button_highlight);
                }
            }
        });
        confirm.setOnMouseExited(mouseEvent -> {
            for (Node child : confirm.getChildren()) {
                ;
                if (child instanceof SelectionButton) {
                    ((SelectionButton) child).r.setFill(color_button_default);
                }
            }
        });
        confirm.setOnMouseClicked(mouseEvent -> {
            CURRENT_LEVEL = -1;
            difficulty = -1;
            isGenerated = true;
            // begin generating interesting game
            service_generateGame.restart();
            Animation a = fadeEffectForNode(selectPage, 300);
            a.setOnFinished(actionEvent -> {
                removeSelectPage();
                showWaitingPage();
            });
        });

        gpEXPLORE.getChildren().addAll(notice, confirm);

        generate.setOnMouseClicked(mouseEvent -> {
            for (Node child : vbSelect.getChildren()) {
                if (child instanceof SelectionButton) {
                    SelectionButton p = (SelectionButton) child;
                    p.st.stop();
                    p.r.setFill(color_button_default);
                }
            }
            generate.r.setFill(color_button_highlight);
            generate.st.play();

            difficulty = -1;
            selectPage.getChildren().remove(t);
            gpLEVEL.getChildren().clear();
            gpLEVEL.getChildren().add(gpEXPLORE);
            mouseEvent.consume();
        });

        // the event of "select" button
        starter.setOnMouseClicked(mouseEvent -> {
            if (!selectPage.getChildren().contains(t)) {
                selectPage.getChildren().add(t);
            }
            selectPage.getChildren().removeAll(notice, confirm);
            for (Node child : vbSelect.getChildren()) {
                if (child instanceof SelectionButton) {
                    SelectionButton p = (SelectionButton) child;
                    p.st.stop();
                    p.r.setFill(color_button_default);
                }
            }
            starter.r.setFill(color_button_highlight);
            starter.st.play();
            t.setText("LEVEL: STARTER");
            difficulty = 0;
            isGenerated = false;
            gpLEVEL.getChildren().clear();
            gpLEVEL.getChildren().add(gpSTARTER);
            mouseEvent.consume();
        });

        junior.setOnMouseClicked(mouseEvent -> {
            if (!selectPage.getChildren().contains(t)) {
                selectPage.getChildren().add(t);
            }
            selectPage.getChildren().removeAll(notice, confirm);
            for (Node child : vbSelect.getChildren()) {
                if (child instanceof SelectionButton) {
                    SelectionButton p = (SelectionButton) child;
                    p.st.stop();
                    p.r.setFill(color_button_default);
                }
            }
            junior.r.setFill(color_button_highlight);
            junior.st.play();
            t.setText("LEVEL: JUNIOR");
            difficulty = 1;
            isGenerated = false;
            gpLEVEL.getChildren().clear();
            gpLEVEL.getChildren().add(gpJUNIOR);
            mouseEvent.consume();
        });

        expert.setOnMouseClicked(mouseEvent -> {
            if (!selectPage.getChildren().contains(t)) {
                selectPage.getChildren().add(t);
            }
            selectPage.getChildren().removeAll(notice, confirm);
            for (Node child : vbSelect.getChildren()) {
                if (child instanceof SelectionButton) {
                    SelectionButton p = (SelectionButton) child;
                    p.st.stop();
                    p.r.setFill(color_button_default);
                }
            }
            expert.r.setFill(color_button_highlight);
            expert.st.play();

            t.setText("LEVEL: EXPERT");
            difficulty = 2;
            isGenerated = false;
            gpLEVEL.getChildren().clear();
            gpLEVEL.getChildren().add(gpEXPERT);
            mouseEvent.consume();
        });

        master.setOnMouseClicked(mouseEvent -> {
            if (!selectPage.getChildren().contains(t)) {
                selectPage.getChildren().add(t);
            }
            selectPage.getChildren().removeAll(notice, confirm);
            for (Node child : vbSelect.getChildren()) {
                if (child instanceof SelectionButton) {
                    SelectionButton p = (SelectionButton) child;
                    p.st.stop();
                    p.r.setFill(color_button_default);
                }
            }
            master.r.setFill(color_button_highlight);
            master.st.play();
            t.setText("LEVEL: MASTER");
            difficulty = 3;
            isGenerated = false;
            gpLEVEL.getChildren().clear();
            gpLEVEL.getChildren().add(gpMASTER);
            mouseEvent.consume();
        });

        wizard.setOnMouseClicked(mouseEvent -> {
            if (!selectPage.getChildren().contains(t)) {
                selectPage.getChildren().add(t);
            }
            selectPage.getChildren().removeAll(notice, confirm);
            for (Node child : vbSelect.getChildren()) {
                if (child instanceof SelectionButton) {
                    SelectionButton p = (SelectionButton) child;
                    p.st.stop();
                    p.r.setFill(color_button_default);
                }
            }
            wizard.r.setFill(color_button_highlight);
            wizard.st.play();
            t.setText("LEVEL: WIZARD");
            difficulty = 4;
            isGenerated = false;
            gpLEVEL.getChildren().clear();
            gpLEVEL.getChildren().add(gpWIZARD);
            mouseEvent.consume();
        });


        // the layout of vbSelect
        vbSelect.setAlignment(Pos.CENTER);
        vbSelect.setSpacing(20);
        vbSelect.setPrefHeight(BOARD_HEIGHT - TOP_BAR_HEIGHT);
        vbSelect.setPrefWidth(233);
        vbSelect.setLayoutX(145);
        vbSelect.setLayoutY(20);
        vbSelect.setScaleX(0.8);
        vbSelect.setScaleY(0.8);
        vbSelect.getChildren().addAll(starter, junior, expert, master, wizard, generate);
        selectPage.getChildren().addAll(select_bg, t, vbSelect, gpLEVEL, settingsButton(), showStars());
    }

    private void showSelectPage() {
        // if mediaplayer is playing, stop it
        mediaPlayer.stop();

        // play select music
        selectMP.play();

        gpLEVEL.getChildren().clear();
        if (RECORD_STARTER != null) {
            gpSTARTER.getChildren().clear();
            for (int i = 0; i < RECORD_STARTER.length; i++) {
                Levels l = new Levels("" + (1 + i), i, RECORD_STARTER[i]);
                gpSTARTER.add(l, i % 6, i / 6);
                gpSTARTER.setLayoutX(370);
                gpSTARTER.setLayoutY(225);
                gpSTARTER.setHgap(5);
                gpSTARTER.setVgap(5);
            }
        }
        if (RECORD_JUNIOR != null) {
            gpJUNIOR.getChildren().clear();
            for (int i = 0; i < RECORD_JUNIOR.length; i++) {
                Levels l = new Levels("" + (1 + i + 24), i, RECORD_JUNIOR[i]);
                gpJUNIOR.add(l, i % 6, i / 6);
                gpJUNIOR.setLayoutX(370);
                gpJUNIOR.setLayoutY(225);
                gpJUNIOR.setHgap(5);
                gpJUNIOR.setVgap(5);
            }
        }
        if (RECORD_MASTER != null) {
            gpMASTER.getChildren().clear();
            for (int i = 0; i < RECORD_MASTER.length; i++) {
                Levels l = new Levels("" + (1 + i + 2 * 24), i, RECORD_MASTER[i]);
                gpMASTER.add(l, i % 6, i / 6);
                gpMASTER.setLayoutX(370);
                gpMASTER.setLayoutY(225);
                gpMASTER.setHgap(5);
                gpMASTER.setVgap(5);
            }
        }
        if (RECORD_EXPERT != null) {
            gpEXPERT.getChildren().clear();
            for (int i = 0; i < RECORD_EXPERT.length; i++) {
                Levels l = new Levels("" + (1 + i + 3 * 24), i, RECORD_EXPERT[i]);
                gpEXPERT.add(l, i % 6, i / 6);
                gpEXPERT.setLayoutX(370);
                gpEXPERT.setLayoutY(225);
                gpEXPERT.setHgap(5);
                gpEXPERT.setVgap(5);
            }
        }
        if (RECORD_WIZARD != null) {
            gpWIZARD.getChildren().clear();
            for (int i = 0; i < RECORD_WIZARD.length; i++) {
                Levels l = new Levels("" + (1 + i + 4 * 24), i, RECORD_WIZARD[i]);
                gpWIZARD.add(l, i % 6, i / 6);
                gpWIZARD.setLayoutX(370);
                gpWIZARD.setLayoutY(225);
                gpWIZARD.setHgap(5);
                gpWIZARD.setVgap(5);
            }
        }

        switch (difficulty) {
            case 1:
                gpLEVEL.getChildren().add(gpJUNIOR);
                break;
            case 2:
                gpLEVEL.getChildren().add(gpEXPERT);
                break;
            case 3:
                gpLEVEL.getChildren().add(gpMASTER);
                break;
            case 4:
                gpLEVEL.getChildren().add(gpWIZARD);
                break;
            case -1:
                System.out.println("hi");
                gpLEVEL.getChildren().add(gpEXPLORE);
                break;
            case 0:
                gpLEVEL.getChildren().add(gpSTARTER);
                break;
        }

        selectPage.setOpacity(1);
        root.getChildren().add(selectPage);
    }

    private void removeSelectPage() {

        root.getChildren().remove(selectPage);
    }

    /**
     * Authorship: Yuxuan Yang
     * The waiting page is design for keeping player to wait for the success of loading the play page
     * Including load/show/remove this page
     */
    private void loadWaitingPage() {
        Group bar = new Group();
        DropShadow ds = new DropShadow();
        ds.setBlurType(BlurType.GAUSSIAN);
        ds.setOffsetY(5);
        ds.setColor(color_background_topBar);
        progressBar.setArcHeight(30);
        progressBar.setArcWidth(30);
        progressBar.setFill(color_button_highlight);
        progressBar.setOpacity(0.95);

        progress_bar_scale_trans.setNode(bar);
        progress_bar_scale_trans.setFromX(1);
        progress_bar_scale_trans.setToX(1.05);
        progress_bar_scale_trans.setCycleCount(Animation.INDEFINITE);
        progress_bar_scale_trans.setDuration(Duration.millis(300));
        progress_bar_scale_trans.setAutoReverse(true);

        bar.getChildren().addAll(progressBar);
        Text t = new Text("LOADING");
        t.setFont(new Font("Arial black", 30));
        t.setWrappingWidth(933);
        t.setLayoutY(280);
        t.setTextAlignment(TextAlignment.CENTER);
        t.setTextOrigin(VPos.TOP);
        t.setFill(color_text_highlight);

        vbWait.getChildren().addAll(bar);
        vbWait.setAlignment(Pos.CENTER);
        vbWait.setLayoutX(315);
        vbWait.setLayoutY(30);
        vbWait.setSpacing(10);
        vbWait.setPrefHeight(BOARD_HEIGHT - TOP_BAR_HEIGHT);
        waitingPage.getChildren().addAll(t, vbWait);
    }

    private void showWaitingPage() {
        waitingPage.setOpacity(1);
        root.getChildren().add(waitingPage);
        timer_loadGame.start();
    }

    private void removeWaitingPage() {
        root.getChildren().remove(waitingPage);
    }

    /**
     * Authorship: Paul Won, Yuxuan Yang, Chensheng Zhang
     * The playPage is the third page in the GUI and it's the body of the game
     * In this page, there is a "setting button"
     * Including load/show/remove this page
     */
    private void loadPlayPage() {
        // set game area
        setLeftPane();

        // set right pane
        setRightPane();

        // a new settings button
        playPage.getChildren().addAll(apRight, apLeft, settingsButton());
    }

    private void showPlayPage() {
        // stop selection music
        selectMP.stop();

        playMusic();

        // set opacity ==1 means no showing effect
        playPage.setOpacity(0);

        // starts initialize a game
        System.out.println(isGenerated);
        System.out.println("CURRENT_LEVEL: " + (CURRENT_LEVEL));
        fitGame = isGenerated ? new FitGame(interestingGame) : (isRandom ? new FitGame(difficulty) : new FitGame(Games.SOLUTIONS[difficulty * 24 + CURRENT_LEVEL]));

        playPage.getChildren().add(showStars_playPage());

        initBoard();
        initPiece();


        // the layout get focus in case of focus on piece
        playPage.setOnMouseClicked(mouseEvent -> {
            playPage.requestFocus();
        });
        playPage.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.SPACE)) {
                tidyPieces();

            }
        });

    }
    private void removePlayPage() {
        //once remove playPage
        currentPreview = "";
        interestingGame = null;
        apLeft.getChildren().removeAll(guiBoard, guiPieces);
        root.getChildren().remove(playPage);
    }

    // the left pane is designed for the body of the game
    private void setLeftPane() {
        // Left pane setting
        apLeft.setMaxSize(GAME_AREA_WIDTH, GAME_AREA_HEIGHT);
        apLeft.setMinSize(GAME_AREA_WIDTH, GAME_AREA_HEIGHT);

        // to set the board background image
        ImageView iv = new ImageView(new Image(URI_BASE + "bg.png"));
        iv.setPreserveRatio(true);
        iv.setFitWidth(533);
        iv.setSmooth(true);
        iv.setOpacity(0.3);
        iv.setLayoutX(PLACEMENT_AREA_X);
        iv.setLayoutY(PLACEMENT_AREA_Y);

        // to set the board background color
        Rectangle left_background = new Rectangle(0, 0, 533, 550);
        left_background.setFill(Color.valueOf("#2d3135"));
        left_background.setOpacity(1);
        left_background.setArcWidth(30);
        left_background.setArcHeight(30);
        DropShadow dark = new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 20, 0, 0, 5);
        left_background.setEffect(dark);

        // current is choose color background
        apLeft.getChildren().addAll(left_background);
        apLeft.setLayoutX(60);
        apLeft.setLayoutY(80);

    }

    private void initBoard() {
        guiBoard.getChildren().clear();
        // board setting
        ImageView board_img = new ImageView(URI_BASE + "board.png");
        AnchorPane.setTopAnchor(board_img, BOARD_Y);
        AnchorPane.setLeftAnchor(board_img, BOARD_X);
        board_img.setPreserveRatio(true);
        board_img.setFitWidth(IMAGE_BOARD_WIDTH);
        board_img.setEffect(boardShadow);
        guiBoard.getChildren().add(board_img);

        // pre-Placed pieces setting
        for (Piece piece : fitGame.getPieces()) {
            String name = piece.pieceType.name().toUpperCase();
            if (piece.isPrePlaced()) {

                // record the pre-placed piece
                used.add(name.toLowerCase());

                // check if flip to select the corresponding image
                if (piece.isFlipped()) {
                    name += "2.png";
                } else {
                    name += "1.png";
                }
                ImageView tmp = new ImageView(URI_BASE + name);

                //  size setting
                if (piece.pieceType.getSpine() == 4) {
                    tmp.setFitWidth(IMAGE_PIECE_HEIGHT * 2);
                } else {
                    tmp.setFitWidth((IMAGE_PIECE_HEIGHT / 2) * 3);
                }
                tmp.setFitHeight(IMAGE_PIECE_HEIGHT);

                //get the bound of imageView
                Bounds bounds = tmp.getBoundsInParent();
                double w = bounds.getWidth();
                double h = bounds.getHeight();

                //rotate the piece to correct orientation
                switch (piece.getOrientation()) {
                    case N:
                        break;
                    case E:
                        tmp.setRotate(90);
                        tmp.setTranslateX(h / 2 - w / 2);
                        tmp.setTranslateY(w / 2 - h / 2);
                        break;
                    case S:
                        tmp.setRotate(180);
                        break;
                    case W:
                        tmp.setRotate(270);
                        tmp.setTranslateX(h / 2 - w / 2);
                        tmp.setTranslateY(w / 2 - h / 2);
                        break;
                }

                // location setting
                double x = BOARD_X + BOARD_MARGIN_X + piece.getCol() * IMAGE_PIECE_HEIGHT / 2;
                double y = BOARD_Y + BOARD_MARGIN_Y + piece.getRow() * IMAGE_PIECE_HEIGHT / 2;
                AnchorPane.setTopAnchor(tmp, y);
                AnchorPane.setLeftAnchor(tmp, x);
                guiBoard.getChildren().add(tmp);
            }
        }

        apLeft.getChildren().add(guiBoard);
    }

    private void initPiece() {
        guiPieces.getChildren().clear();
        // loading pieces
        int count = 0;
        double interval = PLACEMENT_AREA_SCALE * IMAGE_PIECE_HEIGHT * 2;
        int n = (int) Math.round(PLACEMENT_AREA_WIDTH / interval);
        for (String s : IMAGE_NAME) {
            String tmp = s.toLowerCase();
            if (!used.contains(tmp)) {
                // col = row = 9 means the piece is not on the board
                GUIPiece tmpPiece = new GUIPiece(new Piece(PieceType.valueOf(tmp), 9, 9, Orientation.N), IMAGE_PIECE_HEIGHT);
                double w = PLACEMENT_AREA_SCALE * tmpPiece.getFitWidth();

                // set the scale of piece
                tmpPiece.setScaleX(PLACEMENT_AREA_SCALE);
                tmpPiece.setScaleY(PLACEMENT_AREA_SCALE);

                // set the piece in the middle of each "grid" in placement area
                tmpPiece.setLayoutX(PLACEMENT_AREA_X + MARGINAL_ERROR * 1.5 + (count % n) * interval + (interval - w) / 2);
                tmpPiece.setLayoutY(PLACEMENT_AREA_Y + MARGINAL_ERROR * 2 + (count / n) * interval / 1.8);

                // set effect
                tmpPiece.setEffect(pieceShadow_N);
                guiPieces.getChildren().add(tmpPiece);

                count++;
                if (count == 1) {
                    showPreview(s.toUpperCase());
                }
            }
        }
        used.clear();
        apLeft.getChildren().add(guiPieces);
    }

    // the right pane is designed for functional buttons and description
    private void setRightPane() {
        // right pane setting
        apRight.setMaxSize(250, GAME_AREA_HEIGHT);
        apRight.setMinSize(250, GAME_AREA_HEIGHT);
        Rectangle right_background = new Rectangle(0, 0, 250, 550);
        right_background.setFill(Color.valueOf("#2d3135"));
        right_background.setOpacity(0.6);
        right_background.setArcWidth(5);
        right_background.setArcHeight(5);

        Text instruction = new Text("INSTRUCTION");
        instruction.setFont(new Font("Arial black", 26));
        instruction.setFill(Color.WHITE);
        instruction.setTextAlignment(TextAlignment.CENTER);
        instruction.setTextOrigin(VPos.TOP);
        instruction.setWrappingWidth(250);
        instruction.setLayoutX(0);
        instruction.setLayoutY(30);

        Text description = new Text("               - How to win -\nFill out the board by the rest pieces\n \n          " +
                "    - How to play -\n1. Click to drag and drop a piece\n2. Press \"R\" key to rotate 90 degrees\n3. " +
                "Press \"F\" key to flip to different protrusion\n4. Press \"/\" to get hints, it will cost 3 stars each time"+
                "\n5. Press \"SPACE\" to tidy pieces up");


        description.setWrappingWidth(200);
        description.setFont(new Font("Arial black", 11));
        description.setTextAlignment(TextAlignment.JUSTIFY);
        description.setTextOrigin(VPos.TOP);
        description.setFill(Color.WHITE);
        description.setX(22.5);
        description.setY(80);
        description.setLineSpacing(2);

        Text preview = new Text("- Example Preview -");
        Text rotate = new Text("[Rotated]");
        Text flip = new Text("[Flipped]");
        preview.setWrappingWidth(200);
        rotate.setWrappingWidth(100);
        flip.setWrappingWidth(100);
        preview.setFont(new Font("Arial black", 16));
        rotate.setFont(new Font("Arial black", 14));
        flip.setFont(new Font("Arial black", 14));
        preview.setTextAlignment(TextAlignment.CENTER);
        rotate.setTextAlignment(TextAlignment.CENTER);
        flip.setTextAlignment(TextAlignment.CENTER);
        preview.setTextOrigin(VPos.TOP);
        rotate.setTextOrigin(VPos.TOP);
        flip.setTextOrigin(VPos.TOP);
        preview.setFill(Color.WHITE);
        rotate.setFill(Color.WHITE);
        flip.setFill(Color.WHITE);
        preview.setX(22.5);
        rotate.setX(22.5);
        flip.setX(122.5);
        preview.setY(375);
        rotate.setY(400);
        flip.setY(400);
        preview.setLineSpacing(2);
        rotate.setLineSpacing(2);
        flip.setLineSpacing(2);

        view.setLayoutX(0);
        view.setLayoutY(400);

        apRight.getChildren().addAll(right_background, instruction, description, preview, rotate, flip, view);
        apRight.setLayoutY(80);
        apRight.setLayoutX(623);
    }

    private void showPreview(String name) {

        if (!currentPreview.equals(name)) {
            view.getChildren().clear();
            currentPreview = name;
            // two image
            ImageView iv1 = new ImageView(new Image(URI_BASE + name + "1.png"));
            ImageView iv2 = new ImageView(new Image(URI_BASE + name + "2.png"));

            iv1.setRotate(90);
            iv1.setSmooth(true);
            iv2.setSmooth(true);
            iv1.setPreserveRatio(true);
            iv2.setPreserveRatio(true);
            iv1.setFitHeight(50);
            iv2.setFitHeight(50);
            Bounds bounds = iv1.getBoundsInParent();
            double h = bounds.getHeight();

            iv1.setLayoutX(17 + (103 - h) / 2);
            iv1.setLayoutY(50);
            iv2.setLayoutX(172.5 - (h / 2));
            iv2.setLayoutY(50);
            view.getChildren().addAll(iv1, iv2);
            view.setOpacity(1);

        }
    }

    // the hint pane is designed for task 10, which can show all the piece hints

    /**
     * Authorship: Chensheng Zhang, Paul Won
     * The Hint page contains single solution image of the current piece in apHint,
     * When press "Slash", the aphint page will overwrite the previous page
     * When release "Slash", it the hint will disappear and player can continue playing
     */
    private void setApHint(Piece CurrentPiece) {
        apHint.getChildren().clear();

        apHint.setMaxSize(GAME_AREA_WIDTH, GAME_AREA_HEIGHT);
        apHint.setMinSize(GAME_AREA_WIDTH, GAME_AREA_HEIGHT);

        String placement = fitGame.getPlacement();
        String hintPieces = FitGame.getSolution(placement);
        String[] pieceArray = hintPieces.split("(?<=\\G.{4})");


        for (String piece : pieceArray) {
            if (CurrentPiece.getPieceType() == getPieceType(piece.substring(0, 1).toLowerCase()) || CurrentPiece.getPieceType() == getPieceType(piece.substring(0, 1).toUpperCase())) {
                Piece tmpP = new Piece(getPieceType(piece.substring(0, 1)), Integer.parseInt(piece.substring(1, 2)), Integer.parseInt(piece.substring(2, 3)), getOrientation(piece));

                String name = Character.toString(piece.charAt(0)).toUpperCase();

                // check if flip to select the corresponding image
                if (Character.isLowerCase(piece.charAt(0))) {
                    name += "1.png";
                } else {
                    name += "2.png";
                }
                ImageView tmpImg = new ImageView(URI_BASE + name);

                //  size setting
                if (tmpP.pieceType.getSpine() == 4) {
                    tmpImg.setFitWidth(IMAGE_PIECE_HEIGHT * 2);
                } else {
                    tmpImg.setFitWidth((IMAGE_PIECE_HEIGHT / 2) * 3);
                }
                tmpImg.setFitHeight(IMAGE_PIECE_HEIGHT);

                //get the bound of imageView
                Bounds bounds = tmpImg.getBoundsInParent();
                double w = bounds.getWidth();
                double h = bounds.getHeight();

                //rotate the piece to correct orientation
                switch (tmpP.getOrientation()) {
                    case N:
                        break;
                    case E:
                        tmpImg.setRotate(90);
                        tmpImg.setTranslateX(h / 2 - w / 2);
                        tmpImg.setTranslateY(w / 2 - h / 2);
                        break;
                    case S:
                        tmpImg.setRotate(180);
                        break;
                    case W:
                        tmpImg.setRotate(270);
                        tmpImg.setTranslateX(h / 2 - w / 2);
                        tmpImg.setTranslateY(w / 2 - h / 2);
                        break;
                }


                // location setting
                double x = BOARD_X + BOARD_MARGIN_X + tmpP.getCol() * IMAGE_PIECE_HEIGHT / 2;
                double y = BOARD_Y + BOARD_MARGIN_Y + tmpP.getRow() * IMAGE_PIECE_HEIGHT / 2;
                AnchorPane.setTopAnchor(tmpImg, y);
                AnchorPane.setLeftAnchor(tmpImg, x);
                tmpImg.setOpacity(0.5);
                apHint.getChildren().add(tmpImg);
                apHint.setLayoutX(60);
                apHint.setLayoutY(80);

            }
        }
    }


    /**
     * Authorship: Yuxuan Yang
     * To tidy the pieces are at placement area
     */
    private void tidyPieces() {
        double interval = PLACEMENT_AREA_SCALE * IMAGE_PIECE_HEIGHT * 2;
        int n = (int) Math.round(PLACEMENT_AREA_WIDTH / interval);
        int count = 0;

        if (guiPieces.getChildren().size() != 0) {
            for (Object o : guiPieces.getChildren().toArray()) {
                GUIPiece p = (GUIPiece) o;
                double delta_w = PLACEMENT_AREA_SCALE * p.getFitWidth();
                p.setRotate(0);
                p.guiPiece.rotateByOrientation(Orientation.N);
                p.setLayoutX(PLACEMENT_AREA_X + MARGINAL_ERROR * 1.5 + (count % n) * interval + (interval - delta_w) / 2);
                p.setLayoutY(PLACEMENT_AREA_Y + MARGINAL_ERROR * 2 + (count / n) * interval / 1.8);
                count++;
            }
        }
    }


    /**
     * Authorship: Paul Won
     * The settings Page is the fourth page in the GUI
     * In this page, there are "continue", "pause music", "home" and "exit" buttons.
     * Including load/show/remove this page
     */
    private void loadSettingsPage() {
        // set page background color/image
        Rectangle setting_bg = new Rectangle(0, TOP_BAR_HEIGHT, BOARD_WIDTH, BOARD_HEIGHT - TOP_BAR_HEIGHT);
        setting_bg.setFill(color_background_scene);
        setting_bg.setOpacity(1);

        FunctionButton conPlay = new FunctionButton("CONTINUE");
        FunctionButton exit = new FunctionButton("EXIT GAME");
        FunctionButton mute = new FunctionButton("PAUSE SOUND");
        FunctionButton selDiff = new FunctionButton("RETURN");

        // button functions
        conPlay.setOnMouseClicked(mouseEvent -> {
            removeSettingsPage();
        });

        exit.setOnMouseClicked(mouseEvent -> {
            try {
                if (!CURRENT_PLAYER.equals("null")) {
                    saveArchives(CURRENT_PLAYER);
                }
                if (RECORD_PLAYERS.size() != 0) {
                    savePlayers();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Platform.exit();
        });

        mute.setOnMouseClicked(mouseEvent -> {
            if (musicPaused) {
                mediaPlayer.play();
                musicPaused = false;
            } else {
                mediaPlayer.pause();
                musicPaused = true;
            }

        });

        selDiff.setOnMouseClicked(mouseEvent -> {
            Animation a = fadeEffectForNode(settingsPage, 500);
            a.setOnFinished(actionEvent -> {
                removeSettingsPage();
                if (root.getChildren().contains(playPage)) {
                    removePlayPage();
                    showSelectPage();
                } else if (root.getChildren().contains(selectPage)) {
                    selectMP.pause();

                    try {
                        saveArchives(CURRENT_PLAYER);
                        savePlayers();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    removeSelectPage();
                    removePlayerPage();
                    removeCreatePage();
                    showHomePage();
                    mediaPlayer.play();
                }
            });
        });

        vbSetting.setAlignment(Pos.CENTER);
        vbSetting.setSpacing(20);
        vbSetting.getChildren().addAll(conPlay, mute, selDiff, exit);
        vbSetting.setPrefHeight(BOARD_HEIGHT - TOP_BAR_HEIGHT);
        vbSetting.setPrefWidth(233);
        vbSetting.setLayoutX(350);
        settingsPage.getChildren().addAll(setting_bg, vbSetting);
    }

    private void showSettingsPage() {
        settingsPage.setOpacity(1);
        root.getChildren().add(settingsPage);
    }

    private void removeSettingsPage() {
        root.getChildren().remove(settingsPage);
    }

    /**
     * Authorship: Yuxuan Yang, Paul Won
     * The win page is the fifth page in the GUI
     * Including load/show/remove this page
     */
    private void loadWinPage() {

        // the background of win page
        Rectangle win_bg = new Rectangle(0, TOP_BAR_HEIGHT, BOARD_WIDTH, BOARD_HEIGHT - TOP_BAR_HEIGHT);
        win_bg.setFill(color_background_scene);
        win_bg.setOpacity(0.98);
        win_bg.setEffect(new GaussianBlur(50));

        // the notice board of win page
        Rectangle bg_notice = new Rectangle(250, 150, 433, 400);
        bg_notice.setFill(Color.valueOf("#2d3135"));
        bg_notice.setArcWidth(50);
        bg_notice.setArcHeight(50);
        bg_notice.setOpacity(0.95);
        bg_notice.setEffect(pieceShadow_N);

        // draw star
        HBox stars_stroke = new HBox();
        stars_stroke.minWidth(433);
        stars_stroke.maxWidth(433);
        stars_stroke.setSpacing(20);
        stars_stroke.setScaleX(1.1);
        stars_stroke.setScaleY(1.1);
        stars_stroke.setAlignment(Pos.CENTER);
        Double[] points = new Double[]{
                24.86, 0.41, 32.41, 15.7, 49.28, 18.16, 37.07, 30.06, 39.95, 46.87, 24.86, 38.94, 9.76, 46.87, 12.64, 30.06, 0.43, 18.16, 17.31, 15.7
        };
        for (int i = 0; i < 3; i++) {
            Polygon star_outline = new Polygon();
            Polygon star = new Polygon();


            // draw the outline of a star
            star_outline.getPoints().addAll(points);
            star_outline.setFill(color_background_transparent);
            star_outline.setStroke(color_background_topBar);
            star_outline.setStrokeWidth(2);
            stars_stroke.getChildren().add(star_outline);

            // draw the body of a star
            star.getPoints().addAll(points);
            star.setFill(Color.GOLD);
            star.setEffect(boardShadow);
            Bounds bounds = star.getBoundsInParent();
            star.setLayoutX(360 + (bounds.getWidth() + 13) * i);
            star.setLayoutY(253);
            stars[i] = star;
            stars[i].setScaleX(0);
            stars[i].setScaleY(0);


            // add effect
            star_effect[i] = new ScaleTransition(Duration.millis(500));
            star_effect[i].setNode(stars[i]);
            star_effect[i].setToX(1);
            star_effect[i].setToY(1);
            star_effect[i].setDelay(Duration.millis(500 * i));
            int n = i;
        }

        HBox hb_buttons = new HBox();
        hb_buttons.setAlignment(Pos.CENTER);
        hb_buttons.setSpacing(20);
        hb_buttons.maxWidth(433);
        hb_buttons.minWidth(433);
        FunctionButton next = new FunctionButton("NEXT GAME");
        FunctionButton select = new FunctionButton("RETURN");

        next.setOnMouseClicked(mouseEvent -> {
            Animation a = fadeEffectForNode(winPage, 500);
            a.setOnFinished(actionEvent -> {
                removeWinPage();
                removePlayPage();
                successMP.stop();
                selectMP.play();
                playMusic();
                if (CURRENT_LEVEL == 23 || CURRENT_LEVEL == -1) {
                    showSelectPage();
                } else {
                    CURRENT_LEVEL++;
                    switch (difficulty) {
                        case 0:
                            RECORD_STARTER[CURRENT_LEVEL] = RECORD_STARTER[CURRENT_LEVEL] == 0 ? 9 : RECORD_STARTER[CURRENT_LEVEL];
                            break;
                        case 1:
                            RECORD_JUNIOR[CURRENT_LEVEL] = RECORD_JUNIOR[CURRENT_LEVEL] == 0 ? 9 : RECORD_JUNIOR[CURRENT_LEVEL];
                            break;
                        case 2:
                            RECORD_EXPERT[CURRENT_LEVEL] = RECORD_EXPERT[CURRENT_LEVEL] == 0 ? 9 : RECORD_EXPERT[CURRENT_LEVEL];
                            break;
                        case 3:
                            RECORD_MASTER[CURRENT_LEVEL] = RECORD_MASTER[CURRENT_LEVEL] == 0 ? 9 : RECORD_MASTER[CURRENT_LEVEL];
                            break;
                        case 4:
                            RECORD_WIZARD[CURRENT_LEVEL] = RECORD_WIZARD[CURRENT_LEVEL] == 0 ? 9 : RECORD_WIZARD[CURRENT_LEVEL];
                            break;
                    }
                    showWaitingPage();
                    showPlayPage();

                }
            });
        });
        select.setOnMouseClicked(mouseEvent -> {
            Animation a = fadeEffectForNode(winPage, 500);
            a.setOnFinished(actionEvent -> {
                if (CURRENT_LEVEL != 23) {
                    switch (difficulty) {
                        case 0:
                            if (RECORD_STARTER[CURRENT_LEVEL + 1] == 0)
                                RECORD_STARTER[CURRENT_LEVEL + 1] = 9;
                            break;
                        case 1:
                            if (RECORD_JUNIOR[CURRENT_LEVEL + 1] == 0)
                                RECORD_JUNIOR[CURRENT_LEVEL + 1] = 9;
                            break;
                        case 2:
                            if (RECORD_EXPERT[CURRENT_LEVEL + 1] == 0)
                                RECORD_EXPERT[CURRENT_LEVEL + 1] = 9;
                            break;
                        case 3:
                            if (RECORD_MASTER[CURRENT_LEVEL + 1] == 0)
                                RECORD_MASTER[CURRENT_LEVEL + 1] = 9;
                            break;
                        case 4:
                            if (RECORD_WIZARD[CURRENT_LEVEL + 1] == 0)
                                RECORD_WIZARD[CURRENT_LEVEL + 1] = 9;
                            break;
                    }
                }
                removeWinPage();
                removePlayPage();
                showSelectPage();
                successMP.stop();
            });
        });

        hb_buttons.getChildren().addAll(select, next);
        hb_buttons.setScaleX(0.8);
        hb_buttons.setScaleY(0.8);
        Text text = new Text("CONGRATULATIONS!");
        text.setFont(new Font("Arial black", 25));
        text.setFill(color_text_highlight);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setWrappingWidth(433);
        text.setTextOrigin(VPos.TOP);

        Text text2 = new Text("- TIME COST -");
        text2.setFont(new Font("Arial black", 20));
        text2.setFill(color_text_highlight);
        text2.setTextAlignment(TextAlignment.CENTER);
        text2.setWrappingWidth(433);
        text2.setTextOrigin(VPos.TOP);

        Text text3 = new Text("");
        text3.setFont(new Font("Arial black", 30));
        text3.setFill(color_text_highlight);
        text3.setTextAlignment(TextAlignment.CENTER);
        text3.setWrappingWidth(433);
        text3.setTextOrigin(VPos.TOP);

        TIME_COST.setText("00 : 00");
        TIME_COST.setFont(new Font("Arial black", 40));
        TIME_COST.setWrappingWidth(433);
        TIME_COST.setTextAlignment(TextAlignment.CENTER);
        TIME_COST.setTextOrigin(VPos.TOP);
        TIME_COST.setFill(color_text_highlight);
        TIME_COST.setLayoutX(250);
        TIME_COST.setLayoutY(370);
        TIME_COST.setScaleY(1.1);

        vbWin.setMaxSize(433, 400);
        vbWin.setMinSize(433, 400);
        vbWin.setAlignment(Pos.CENTER);
        vbWin.setSpacing(30);
        vbWin.setLayoutX(250);
        vbWin.setLayoutY(150);

        vbWin.getChildren().addAll(text, stars_stroke, text2, text3, hb_buttons);
        winPage.getChildren().addAll(win_bg, bg_notice, vbWin, TIME_COST, stars[0], stars[1], stars[2]);
    }

    private void showWinPage() {
        winPage.setOpacity(1);
        root.getChildren().add(winPage);
    }

    private void removeWinPage() {
        for (Polygon s : stars) {
            s.setScaleX(0);
            s.setScaleY(0);
        }
        root.getChildren().remove(winPage);
    }


    /**
     * Authorship: Yuxuan Yang
     * To read the how many players have been recorded
     */
    private void readPlayers() throws FileNotFoundException {
        File players = new File(URI_RECORD + "players.txt");
        Scanner sc = new Scanner(players);
        RECORD_PLAYERS.clear();
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            String[] temp = s.split("\\s");

            // check if the player has a archive, if not, then delete the player.
            String name ="";
            // here to solve if a name contains space
            for (int i=0; i<temp.length-1; i++){
                if (i==temp.length-2){
                    name += temp[i];
                }else {
                name += temp[i]+" ";
                }
            }
            File file = new File(URI_RECORD + name + ".txt");
            if (file.exists()) {
                if (!name.equals("")) {
                    RECORD_PLAYERS.add(s);
                }
            }
        }
        sc.close();


    }

    /**
     * Authorship: Yuxuan Yang
     * To read certain player's information
     */
    private void readArchives(String player) throws FileNotFoundException {
        File file = new File(URI_RECORD + player + ".txt");

        Scanner sc = new Scanner(file);
        int count = 0;
        String last = "";
        while (sc.hasNextLine()) {
            String temp = sc.nextLine();
            switch (temp) {
                case "STARTER":
                case "JUNIOR":
                case "EXPERT":
                case "MASTER":
                case "WIZARD":
                    last = temp;
                    count = 0;
                    continue;
                default:
                    break;
            }

            if (last.equals("STARTER")) {
                for (int i = 0; i < temp.length(); i++) {
                    RECORD_STARTER[count] = Integer.parseInt(temp.substring(i, i + 1));
                    count++;
                }
            } else if (last.equals("JUNIOR")) {
                for (int i = 0; i < temp.length(); i++) {
                    RECORD_JUNIOR[count] = Integer.parseInt(temp.substring(i, i + 1));
                    count++;
                }
            } else if (last.equals("EXPERT")) {
                for (int i = 0; i < temp.length(); i++) {
                    RECORD_EXPERT[count] = Integer.parseInt(temp.substring(i, i + 1));
                    count++;
                }
            } else if (last.equals("MASTER")) {
                for (int i = 0; i < temp.length(); i++) {
                    RECORD_MASTER[count] = Integer.parseInt(temp.substring(i, i + 1));
                    count++;
                }
            } else if (last.equals("WIZARD")) {
                for (int i = 0; i < temp.length(); i++) {
                    RECORD_WIZARD[count] = Integer.parseInt(temp.substring(i, i + 1));
                    count++;
                }
            }

        }
        sc.close();
    }

    /**
     * Authorship: Yuxuan Yang
     * To save the how many players have been recorded
     */
    private void savePlayers() throws FileNotFoundException {
        PrintWriter out = new PrintWriter(URI_RECORD + "players.txt");

        for (String recordPlayer : RECORD_PLAYERS) {

            String s = recordPlayer;
            if (s.contains(CURRENT_PLAYER)) {
                s = CURRENT_PLAYER + " " + CURRENT_STARS;
            }
            out.println(s);
        }

        System.out.println("saved players!");
        out.close();
    }

    /**
     * Authorship: Yuxuan Yang
     * To save the certain player's information
     */
    private void saveArchives(String player) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(URI_RECORD + player + ".txt");
        out.println("STARTER");
        for (int i = 0; i < 4; i++) {
            String s = "";
            for (int j = 0; j < 6; j++) {
                s += RECORD_STARTER[i * 6 + j];
            }
            out.println(s);
        }
        out.println("JUNIOR");
        for (int i = 0; i < 4; i++) {
            String s = "";
            for (int j = 0; j < 6; j++) {
                s += RECORD_JUNIOR[i * 6 + j];
            }
            out.println(s);
        }
        out.println("EXPERT");
        for (int i = 0; i < 4; i++) {
            String s = "";
            for (int j = 0; j < 6; j++) {
                s += RECORD_EXPERT[i * 6 + j];
            }
            out.println(s);
        }
        out.println("MASTER");
        for (int i = 0; i < 4; i++) {
            String s = "";
            for (int j = 0; j < 6; j++) {
                s += RECORD_MASTER[i * 6 + j];
            }
            out.println(s);
        }
        out.println("WIZARD");
        for (int i = 0; i < 4; i++) {
            String s = "";
            for (int j = 0; j < 6; j++) {
                s += RECORD_WIZARD[i * 6 + j];
            }
            out.println(s);
        }
        out.close();
        System.out.println("saved archive!");

    }

    /**
     * Authorship: Yuxuan Yang
     * Create the setting button icon
     */
    private Group settingsButton() {
        Group main = new Group();
        main.prefHeight(20);
        main.prefWidth(25);
        Rectangle r0 = new Rectangle(0, 0, 25, 20);
        Rectangle r1 = new Rectangle(0, 0, 25, 3);
        Rectangle r2 = new Rectangle(0, 8, 25, 3);
        Rectangle r3 = new Rectangle(0, 16, 25, 3);
        r1.setArcHeight(3);
        r2.setArcHeight(3);
        r3.setArcHeight(3);
        r1.setArcWidth(3);
        r2.setArcWidth(3);
        r3.setArcWidth(3);
        r0.setFill(color_background_scene);
        r1.setFill(color_text_default);
        r2.setFill(color_text_default);
        r3.setFill(color_text_default);

        main.setLayoutX(18);
        main.setLayoutY(660);
        main.getChildren().addAll(r0, r1, r2, r3);

        main.setOnMouseEntered(mouseEvent -> {
            main.setCursor(Cursor.HAND);
            main.setOpacity(0.8);
            r1.setFill(color_text_highlight);
            r2.setFill(color_text_highlight);
            r3.setFill(color_text_highlight);
        });
        main.setOnMouseExited(mouseEvent -> {
            main.setOpacity(1);
            r1.setFill(color_text_default);
            r2.setFill(color_text_default);
            r3.setFill(color_text_default);
        });
        main.setOnMouseClicked(mouseEvent -> {
            showSettingsPage();
        });
        return main;
    }

    /**
     * Authorship: Yuxuan Yang
     * To show the star icon and the number of stars on selectPage
     */
    private Group showStars() {
        Group stars_count = new Group();
        stars_selectPage.setText(CURRENT_STARS + "");
        stars_selectPage.setFill(color_text_highlight);
        stars_selectPage.setEffect(boardShadow);
        stars_selectPage.setFont(new Font("Arial black", 20));
        stars_selectPage.setTextAlignment(TextAlignment.CENTER);
        stars_selectPage.setTextOrigin(VPos.TOP);
        stars_selectPage.setLayoutY(40);
        stars_selectPage.setWrappingWidth(50);


        Double[] points = new Double[]{
                24.86, 0.41, 32.41, 15.7, 49.28, 18.16, 37.07, 30.06, 39.95, 46.87, 24.86, 38.94, 9.76, 46.87, 12.64, 30.06, 0.43, 18.16, 17.31, 15.7
        };

        Polygon p = new Polygon();
        p.getPoints().addAll(points);
        p.setScaleX(0.7);
        p.setScaleY(0.7);
        p.setFill(Color.GOLD);
        p.setEffect(pieceShadow_N);

        stars_count.getChildren().addAll(p, stars_selectPage);
        stars_count.setLayoutX(20);
        stars_count.setLayoutY(40);
        return stars_count;
    }

    /**
     * Authorship: Yuxuan Yang
     * To show the star icon and the number of stars on playPage
     */
    private Group showStars_playPage() {
        Group stars_count = new Group();
        stars_playPage.setText(CURRENT_STARS + "");
        stars_playPage.setFill(color_text_highlight);
        stars_playPage.setEffect(boardShadow);
        stars_playPage.setFont(new Font("Arial black", 25));
        stars_playPage.setTextAlignment(TextAlignment.CENTER);
        stars_playPage.setTextOrigin(VPos.TOP);
        stars_playPage.setLayoutX(40);
        stars_playPage.setLayoutY(6);
        stars_playPage.setWrappingWidth(80);


        Double[] points = new Double[]{
                24.86, 0.41, 32.41, 15.7, 49.28, 18.16, 37.07, 30.06, 39.95, 46.87, 24.86, 38.94, 9.76, 46.87, 12.64, 30.06, 0.43, 18.16, 17.31, 15.7
        };

        Polygon p = new Polygon();
        p.getPoints().addAll(points);
        p.setScaleX(0.6);
        p.setScaleY(0.6);
        p.setFill(Color.GOLD);
        p.setEffect(boardShadow);

        stars_count.getChildren().addAll(p, stars_playPage);
        stars_count.setLayoutX(300);
        stars_count.setLayoutY(35);
        return stars_count;
    }

    /**
     * Authorship: Yuxuan Yang
     * To show a query that ask player to unlock the locked challenge
     */
    private Group askUnlock(int curLevel) {
        Group main = new Group();
        Rectangle bg = new Rectangle(0, 30, BOARD_WIDTH, BOARD_HEIGHT - TOP_BAR_HEIGHT);
        bg.setFill(color_background_scene);
        bg.setOpacity(0.9);
        bg.setEffect(new GaussianBlur(10));
        main.maxWidth(333);
        main.minWidth(333);
        Rectangle r = new Rectangle(250, 250, 433, 200);
        r.setArcHeight(20);
        r.setArcWidth(20);
        r.setEffect(pieceShadow_N);
        r.setFill(Color.valueOf("#8B7D6B"));

        Text t = new Text("Would you like to consume 3 stars to unlock this challenge?");
        t.setWrappingWidth(433);
        t.setFont(new Font("Arial black", 20));
        t.setLayoutX(250);
        t.setLayoutY(300);
        t.setTextAlignment(TextAlignment.CENTER);
        t.setTextOrigin(VPos.TOP);
        t.setFill(color_text_highlight);

        HBox hb = new HBox();
        FunctionButton yes = new FunctionButton("YES");
        FunctionButton no = new FunctionButton("NO");
        FunctionButton ok = new FunctionButton("OK");
        yes.setOnMouseClicked(mouseEvent -> {
            if (CURRENT_STARS - 3 >= 0) {

                // update stars
                CURRENT_STARS -= 3;
                stars_selectPage.setText("" + CURRENT_STARS);

                CURRENT_LEVEL = curLevel;
                switch (difficulty) {
                    case 0:
                        RECORD_STARTER[CURRENT_LEVEL] = 9;
                        break;
                    case 1:
                        RECORD_JUNIOR[CURRENT_LEVEL] = 9;
                        break;
                    case 2:
                        RECORD_EXPERT[CURRENT_LEVEL] = 9;
                        break;
                    case 3:
                        RECORD_MASTER[CURRENT_LEVEL] = 9;
                        break;
                    case 4:
                        RECORD_WIZARD[CURRENT_LEVEL] = 9;
                        break;
                }
                selectPage.getChildren().remove(main);
                removeSelectPage();
                showSelectPage();
            } else {
                hb.getChildren().clear();
                t.setText("Sorry, you don't have enough stars!");
                hb.getChildren().add(ok);
                hb.setLayoutX(360);

            }
        });

        no.setOnMouseClicked(mouseEvent -> {
            selectPage.getChildren().remove(main);
        });

        ok.setOnMouseClicked(mouseEvent -> {
            selectPage.getChildren().remove(main);
            t.setText("Would you like to consume 3 stars to unlock this challenge?");
            hb.getChildren().clear();
            hb.getChildren().addAll(yes, no);
            hb.setLayoutX(270);
        });
        hb.getChildren().addAll(yes, no);
        hb.setLayoutX(270);
        hb.setLayoutY(380);
        hb.setScaleY(0.8);
        hb.setScaleX(0.8);
        hb.setSpacing(20);
        main.getChildren().addAll(bg, r, t, hb);

        return main;
    }

    /**
     * Authorship: Yuxuan Yang
     * To create function buttons.
     */
    class FunctionButton extends Group {
        double buttonHeight = 50;
        double buttonWidth = 200;
        double buttonArc = 50;
        double textHeight;
        double textWidth;
        Color buttonColor = color_button_default;
        Text text = new Text();
        Rectangle r = new Rectangle(buttonWidth, buttonHeight);


        FunctionButton(String str) {
            super();
            initButton(str);
            setEffect();
        }

        private void initButton(String str) {
            text.setFont(Font.font("Arial Black", 22));
            text.setFill(Color.WHITE);
            text.setText(str);
            text.setWrappingWidth(buttonWidth);
            text.setTextAlignment(TextAlignment.CENTER);
            text.setTextOrigin(VPos.TOP);
            text.setLineSpacing(300);
            Bounds bounds = text.getBoundsInParent();
            textWidth = bounds.getWidth();
            textHeight = bounds.getHeight();
            text.setLayoutX((buttonWidth - textWidth) / 2);
            text.setLayoutY((buttonHeight - textHeight) / 2);

            r.setArcHeight(buttonArc);
            r.setArcWidth(buttonArc);
            r.setFill(buttonColor);
            r.setLayoutX(0);
            r.setLayoutY(0);

            this.getChildren().addAll(r, text);
            DropShadow ds = new DropShadow();
            ds.setColor(color_background_topBar);
            ds.setOffsetX(5);
            ds.setOffsetY(8);
            setEffect(ds);

        }

        private void setEffect() {

            ScaleTransition st = new ScaleTransition();
            st.setNode(this);
            st.setFromX(0.95);
            st.setFromY(0.95);
            st.setToX(1.05);
            st.setToY(1.05);
            st.setDuration(Duration.millis(800));
            st.setCycleCount(Animation.INDEFINITE);
            st.setAutoReverse(true);

            // hover
            this.setOnMouseEntered(mouseEvent -> {
                setCursor(Cursor.HAND);
                r.setFill(color_button_highlight);
                st.play();

            });

            this.setOnMouseExited(mouseEvent -> {
                st.stop();
                this.setScaleX(1);
                this.setScaleY(1);
                r.setFill(color_button_default);
            });
        }
    }

    /**
     * Authorship: Yuxuan Yang
     * To create selection buttons in select page
     */
    class SelectionButton extends Group {
        double buttonHeight = 50;
        double buttonWidth = 200;
        double buttonArc = 50;
        double textHeight;
        double textWidth;
        Color buttonColor = color_button_default;
        Text text = new Text();
        Rectangle r = new Rectangle(buttonWidth, buttonHeight);
        ScaleTransition st = new ScaleTransition();

        SelectionButton(String str) {
            super();
            initButton(str);
            setEffect();
        }

        private void initButton(String str) {
            text.setFont(Font.font("Arial Black", 22));
            text.setFill(Color.WHITE);
            text.setText(str);
            text.setWrappingWidth(buttonWidth);
            text.setTextAlignment(TextAlignment.CENTER);
            text.setTextOrigin(VPos.TOP);
            text.setLineSpacing(300);
            Bounds bounds = text.getBoundsInParent();
            textWidth = bounds.getWidth();
            textHeight = bounds.getHeight();
            text.setLayoutX((buttonWidth - textWidth) / 2);
            text.setLayoutY((buttonHeight - textHeight) / 2);

            r.setArcHeight(buttonArc);
            r.setArcWidth(buttonArc);
            r.setFill(buttonColor);
            r.setLayoutX(0);
            r.setLayoutY(0);

            this.getChildren().addAll(r, text);
            DropShadow ds = new DropShadow();
            ds.setColor(color_background_topBar);
            ds.setOffsetX(5);
            ds.setOffsetY(8);
            setEffect(ds);

        }

        private void setEffect() {


            st.setNode(this);
            st.setFromX(0.95);
            st.setFromY(0.95);
            st.setToX(1.05);
            st.setToY(1.05);
            st.setDuration(Duration.millis(800));
            st.setCycleCount(Animation.INDEFINITE);
            st.setAutoReverse(true);

            // hover
            this.setOnMouseEntered(mouseEvent -> {
                setCursor(Cursor.HAND);
            });

            this.setOnMouseClicked(mouseEvent -> {

            });

        }

    }

    /**
     * Authorship: Paul Won, Yuxuan Yang, Chensheng Zhang
     * To show a query that ask player to unlock the locked challenge
     */
    class GUIPiece extends ImageView {
        Piece guiPiece;
        Piece guiPiece_old;
        Image guiImage;
        Image guiImage_old;
        double rotate_old = 0;
        boolean isRotatedOrFlipped = false;
        boolean isHolding = false;
        double mouseX = 0;
        double mouseY = 0;
        int oldCol = 0;
        int oldRow = 0;
        double oldX = 0;  // old layoutX
        double oldY = 0;  // old layoutY

        GUIPiece(Piece p, double height) {
            guiPiece = p;
            if (p.isFlipped()) {
            }
            setImg();
            setSize(height);
            setFunction();
        }

        /**
         * Set the image for the ImageView
         */
        private void setImg() {
            String name = URI_BASE;
            if (guiPiece.isFlipped()) {
                name += guiPiece.pieceType.name().toUpperCase() + "2.png";
            } else {
                name += guiPiece.pieceType.name().toUpperCase() + "1.png";
            }
            this.guiImage = new Image(name);
            super.setImage(guiImage);
        }

        /**
         * set the image size according to h
         *
         * @param h the height of the image
         */
        private void setSize(double h) {
            super.setFitHeight(h);
            if (guiPiece.pieceType.getSpine() == 4) {
                super.setFitWidth(h * 2);
            } else {
                super.setFitWidth((h * 3) / 2);
            }
        }

        /**
         * A function container that contains all piece behaviors.
         */
        private void setFunction() {
            drag_drop();
            rotate_flip();
            pressSlashShowHint(); //task 10
        }

        /**
         * the function of drag and drop
         * - If the piece is dragged from "placement area" to "the board", and it cannot drop, then it will set back to the previous location.
         * - If the piece is dragged from "board" to "other place on the board",and it cannot drop, then it will set back to the previous location.
         * - If the piece is dragged from "board" to "other place on the board", and it cannot drop as well as it has been rotated/flipped,
         * then it will not only set back to previous location, but also the piece will turn back to the previous state which can drop to the board.
         */
        private void drag_drop() {


            // hover the piece
            setOnMouseEntered(mouseEvent -> {
//                MediaPlayer hoverMP;
//                hoverMP = new MediaPlayer(new Media(Paths.get(URI_SOUND+"hover.mp3").toUri().toString()));
//                hoverMP.play();
                requestFocus(); // more logical to have here
                setCursor(Cursor.HAND);
                showPreview(this.guiPiece.pieceType.name().toUpperCase());
                setApHint(this.guiPiece);
            });

            // press the piece
            setOnMousePressed(mouseEvent -> {
                isHolding = true;
                setEffect(null);

                // click sound effect
                MediaPlayer clickMP;
                clickMP = new MediaPlayer(new Media(Board.class.getResource("assets/click.mp3").toExternalForm()));
                clickMP.play();

                // the effect of putting current piece to the top layer
                apLeft.getChildren().remove(this);
                apLeft.getChildren().add(this);

                // record the current mouse(X,Y)
                mouseX = mouseEvent.getSceneX();
                mouseY = mouseEvent.getSceneY();

                // record the current layout(X,Y)
                oldX = getLayoutX();
                oldY = getLayoutY();

                // the effect of scale
                setScaleX(1.05);
                setScaleY(1.05);

                // check if the piece had been dropped on the board,and now drag it out
                if (guiPiece.isPlaced()) {
                    // record the current piece state, location and image for later recovery
                    rotate_old = getRotate();
                    guiImage_old = guiImage;
                    guiPiece_old = new Piece(guiPiece.getPieceState());
                    oldCol = guiPiece.getCol();
                    oldRow = guiPiece.getRow();

                    // to drag the piece out
                    fitGame.dragPiece(guiPiece, fitGame.getMatrixBoard());
                }

            });

            // drag and hold the piece
            setOnMouseDragged(mouseEvent -> {

                setOpacity(0.85);
                double movementX = mouseEvent.getSceneX() - mouseX;
                double movementY = mouseEvent.getSceneY() - mouseY;

                setLayoutX(getLayoutX() + movementX);
                setLayoutY(getLayoutY() + movementY);

                mouseX = mouseEvent.getSceneX();
                mouseY = mouseEvent.getSceneY();

                mouseEvent.consume();
            });

            // drop the piece
            setOnMouseReleased(mouseEvent -> {

                // drop sound effect
                MediaPlayer dropMP;
                dropMP = new MediaPlayer(new Media(Board.class.getResource("assets/drop.mp3").toExternalForm()));
                dropMP.play();

                setOpacity(1);
                setScaleX(1);
                setScaleY(1);
                Bounds bounds = getBoundsInParent();
                double w = bounds.getWidth();
                double h = bounds.getHeight();
                double newX = bounds.getMinX();
                double newY = bounds.getMinY();
                double maxX = bounds.getMaxX();
                double maxY = bounds.getMaxY();

                // when the piece is holding and outside the placement area
                if (newX < PLACEMENT_AREA_X ||
                        maxX > PLACEMENT_AREA_WIDTH + PLACEMENT_AREA_X ||
                        maxY > PLACEMENT_AREA_HEIGHT + PLACEMENT_AREA_Y ||
                        newY < PLACEMENT_AREA_Y) {

                    // calculate the col and row of current location on the board
                    int x_col = (int) Math.round((newX - BOARD_MARGIN_X) / BOARD_GRID_SIZE);
                    int y_row = (int) Math.round((newY - BOARD_MARGIN_Y) / BOARD_GRID_SIZE);
                    System.out.println(x_col + " - " + y_row);

                    // if the piece can drop in the new place
                    if (FitGame.isDropViable(guiPiece.getMatrixShape(), fitGame.getMatrixBoard(), x_col, y_row)) {

                        /* the code is designed for tidyPieces()*/
                        if (!apLeft.getChildren().contains(this)) {
                            guiPieces.getChildren().remove(this);
                            apLeft.getChildren().add(this);
                        }


                        fitGame.dropPiece(guiPiece, fitGame.getMatrixBoard(), x_col, y_row);
                        setLayoutX(BOARD_MARGIN_X + x_col * BOARD_GRID_SIZE);
                        setLayoutY(BOARD_MARGIN_Y + y_row * BOARD_GRID_SIZE);
                        switch (guiPiece.getOrientation()) {
                            case E:
                            case W:
                                setLayoutX(getLayoutX() - h / 2 + w / 2);
                                setLayoutY(getLayoutY() - w / 2 + h / 2);
                                break;
                        }

                    }
                    // if the piece cannot drop in the new place
                    else {
                        if (oldY < PLACEMENT_AREA_Y) {

                            if (!isRotatedOrFlipped) { // if dragged out and didn't rotate/flip, then only set back to original place
                                fitGame.dropPiece(guiPiece, fitGame.getMatrixBoard(), oldCol, oldRow);
                                setLayoutX(BOARD_MARGIN_X + oldCol * BOARD_GRID_SIZE);
                                setLayoutY(BOARD_MARGIN_Y + oldRow * BOARD_GRID_SIZE);
                                switch (guiPiece.getOrientation()) {
                                    case E:
                                    case W:
                                        setLayoutX(getLayoutX() - h / 2 + w / 2);
                                        setLayoutY(getLayoutY() - w / 2 + h / 2);
                                        break;
                                }
                            } else { // if dragged out and rotated/flipped, then set back to original place and set the piece back to original state
                                setLayoutX(oldX);
                                setLayoutY(oldY);
                                setRotate(rotate_old);
                                this.guiPiece = this.guiPiece_old;
                                this.guiImage = guiImage_old;
                                setImage(guiImage);
                                fitGame.dropPiece(guiPiece, fitGame.getMatrixBoard(), oldCol, oldRow);
                                setEffect(null);
                            }

                        } else {

                            /* the code is designed for tidyPieces()*/
                            if (apLeft.getChildren().contains(this)) {
                                apLeft.getChildren().remove(this);
                                guiPieces.getChildren().add(this);
                            }

                            setLayoutX(oldX);
                            setLayoutY(oldY);
                            setScaleX(PLACEMENT_AREA_SCALE);
                            setScaleY(PLACEMENT_AREA_SCALE);
                        }
                    }
                }

                // when the piece is holding and on the above the placement area
                else {

                    /* the code is designed for tidyPieces()*/
                    if (apLeft.getChildren().contains(this)) {
                        apLeft.getChildren().remove(this);
                        guiPieces.getChildren().add(this);
                    }

                    // the piece comes from placement area
                    setScaleX(PLACEMENT_AREA_SCALE);
                    setScaleY(PLACEMENT_AREA_SCALE);
                    switch (guiPiece.getOrientation()) {
                        case N:
                            setEffect(pieceShadow_N);
                            break;
                        case E:
                            setEffect(pieceShadow_E);
                            break;
                        case S:
                            setEffect(pieceShadow_S);
                            break;
                        default:
                            setEffect(pieceShadow_W);
                            break;
                    }

                }

                // print the matrix board and the matrix shape of the piece
                int[][] current_board = fitGame.getMatrixBoard();
                for (int i = 0; i < current_board.length; i++) {
                    for (int j = 0; j < current_board[i].length; j++) {
                        System.out.print(current_board[i][j] + " ");
                    }
                    System.out.println();
                }
                for (int i = 0; i < guiPiece.getMatrixShape().length; i++) {
                    for (int j = 0; j < guiPiece.getMatrixShape()[i].length; j++) {
                        System.out.print(guiPiece.getMatrixShape()[i][j] + " ");
                    }
                    System.out.println();
                }
                System.out.println(this.guiPiece.getPieceState());
                isHolding = false;
                isRotatedOrFlipped = false;
                mouseEvent.consume();
            });
        }

        /**
         * the function of rotate and flip
         */
        private void rotate_flip() {

            // get focus firstly
            setOnMouseClicked(mouseEvent -> {
                requestFocus();
                mouseEvent.consume();
            });

            setOnKeyReleased(keyEvent -> {
                // check if the piece is placed on the board, otherwise it couldn't be flipped and rotated
                if (!guiPiece.isPlaced()) {
                    // press "R" to rotate the piece and update the piece
                    if (keyEvent.getCode().equals(KeyCode.R)) {
                        isRotatedOrFlipped = true;
                        switch (guiPiece.getOrientation()) {
                            case N:
                                setRotate(90);
                                guiPiece.rotate();
                                break;
                            case E:
                                setRotate(180);
                                guiPiece.rotate();
                                break;
                            case S:
                                setRotate(270);
                                guiPiece.rotate();
                                break;
                            default:
                                setRotate(0);
                                guiPiece.rotate();
                                break;
                        }
                        if (!isHolding) {
                            switch (guiPiece.getOrientation()) {
                                case N:
                                    setEffect(pieceShadow_N);
                                    break;
                                case E:
                                    setEffect(pieceShadow_E);
                                    break;
                                case S:
                                    setEffect(pieceShadow_S);
                                    break;
                                default:
                                    setEffect(pieceShadow_W);
                                    break;
                            }
                        }
                    }

                    // press "F" to flip the piece, and update the guiImage as well as the piece
                    if (keyEvent.getCode().equals(KeyCode.F)) {
                        isRotatedOrFlipped = true;
                        String name = guiPiece.pieceType.name().toUpperCase();
                        if (guiPiece.isFlipped()) {
                            this.guiImage = new Image(URI_BASE + name + "1.png");
                        } else {
                            this.guiImage = new Image(URI_BASE + name + "2.png");
                        }
                        setImage(guiImage);
                        guiPiece.flip();
                    }
                    if (keyEvent.getCode().equals(KeyCode.SLASH)) {
                        if (CURRENT_STARS >= 3) {
                            System.out.println("show a hint");
                            stars_playPage.setScaleX(1);
                            stars_playPage.setScaleY(1);

                            // update the stars
                            CURRENT_STARS -= 3;
                            stars_playPage.setText("" + CURRENT_STARS);
                            stars_selectPage.setText(""+CURRENT_STARS);
                        }
                        playPage.getChildren().remove(apHint);

                    }


                }
            });
        }

        /**
         * the function is to test for task 10, when holding on the key "SPACE" and the Piece will continue flip
         */
        private void test_hold_down() {
            if (!guiPiece.isPlaced()) {
                scene.setOnKeyPressed(event -> {
                    switch (event.getCode()) {
                        case SPACE:
                            hold_on = true;
                            break;
                    }
                });

                scene.setOnKeyReleased(event -> {
                    switch (event.getCode()) {
                        case SPACE:
                            hold_on = false;
                            break;
                    }
                });

                AnimationTimer timer = new AnimationTimer() {


                    @Override
                    public void handle(long now) {

                        if (hold_on) {
                            isRotatedOrFlipped = true;
                            String name = guiPiece.pieceType.name();
                            if (guiPiece.isFlipped()) {
                                guiImage = new Image(URI_BASE + name + "1.png");
                            } else {
                                guiImage = new Image(URI_BASE + name + "2.png");
                            }
                            setImage(guiImage);
                            guiPiece.flip();
                            System.out.println("PieceState: " + guiPiece.getPieceState());

                        }

                    }
                };
                timer.start();
            }

        }

        /**
         * Authorship: Paul Won, Chensheng Zhang
         * Task 10 continued from implementHint()
         */
        private void showHints() {
            String placement = fitGame.getPlacement();
            String hintPieces = FitGame.getSolution(placement);
            String[] pieceArray = hintPieces.split("(?<=\\G.{4})");
            HashSet<Piece> hintSet = new HashSet<>();
            for (String s : pieceArray) {
                PieceType tmpPType = getPieceType(Character.toString(s.charAt(0)));
                Orientation tmpO = getOrientation(s);
                hintSet.add(new Piece(tmpPType, s.charAt(1), s.charAt(2), tmpO));
            }
            if (!guiPiece.isPlaced()) {
                scene.setOnKeyPressed(event -> {
                    if (event.getCode().equals(KeyCode.SLASH)) {
                        revealHints = true;
                        System.out.println("pressed");
                    }
                });

                scene.setOnKeyReleased(event -> {
                    if (event.getCode().equals(KeyCode.SLASH)) {
                        revealHints = false;
                        System.out.println("released");
                    }
                });

                AnimationTimer timer = new AnimationTimer() {


                    @Override
                    public void handle(long now) {
                        showHint.getChildren().clear();
                        if (revealHints) {
                            HashSet<PieceType> hints = implementHint();
                            if (!guiPiece.isPlaced()) {
                                if (hints.contains(guiPiece.pieceType)) {
                                    System.out.println("contains:   " + guiPiece.pieceType);

                                    setOpacity(0.5);
                                    for (String piece : pieceArray) {
                                        Piece tmpP = new Piece(getPieceType(piece.substring(0, 1)), Integer.parseInt(piece.substring(1, 2)), Integer.parseInt(piece.substring(2, 3)), getOrientation(piece));
                                        String name = Character.toString(piece.charAt(0)).toUpperCase();

                                        // check if flip to select the corresponding image
                                        if (Character.isLowerCase(piece.charAt(0))) {
                                            name += "1.png";
                                        } else {
                                            name += "2.png";
                                        }
                                        ImageView tmpImg = new ImageView(URI_BASE + name);

                                        //  size setting
                                        if (tmpP.pieceType.getSpine() == 4) {
                                            tmpImg.setFitWidth(IMAGE_PIECE_HEIGHT * 2);
                                        } else {
                                            tmpImg.setFitWidth((IMAGE_PIECE_HEIGHT / 2) * 3);
                                        }
                                        tmpImg.setFitHeight(IMAGE_PIECE_HEIGHT);
                                        tmpImg.setOpacity(0.5);

                                        //get the bound of imageView
                                        Bounds bounds = tmpImg.getBoundsInParent();
                                        double w = bounds.getWidth();
                                        double h = bounds.getHeight();

                                        //rotate the piece to correct orientation
                                        switch (tmpP.getOrientation()) {
                                            case N:
                                                break;
                                            case E:
                                                tmpImg.setRotate(90);
                                                tmpImg.setTranslateX(h / 2 - w / 2);
                                                tmpImg.setTranslateY(w / 2 - h / 2);
                                                break;
                                            case S:
                                                tmpImg.setRotate(180);
                                                break;
                                            case W:
                                                tmpImg.setRotate(270);
                                                tmpImg.setTranslateX(h / 2 - w / 2);
                                                tmpImg.setTranslateY(w / 2 - h / 2);
                                                break;
                                        }


                                        // location setting
                                        double x = BOARD_X + BOARD_MARGIN_X + tmpP.getCol() * IMAGE_PIECE_HEIGHT / 2;
                                        double y = BOARD_Y + BOARD_MARGIN_Y + tmpP.getRow() * IMAGE_PIECE_HEIGHT / 2;
                                        AnchorPane.setTopAnchor(tmpImg, y);
                                        AnchorPane.setLeftAnchor(tmpImg, x);
                                        apLeft.getChildren().add(tmpImg);
                                    }
                                }
                            }
                        } else {
                            setOpacity(1);
                        }

                    }
                };
                timer.start();
            }
        }

        /**
         * Authorship: Chensheng Zhang, Paul Won
         * Write for task 10, when press Slash the hint page will occur
         * In case clash with the release function in rotate_flip, the release Slash
         * Key function code is included in rotate_flip()
         */

        private void pressSlashShowHint() {
            setOnKeyPressed(keyEvent -> {
                if (!guiPiece.isPlaced()) {
                    if (keyEvent.getCode().equals(KeyCode.SLASH)) {
                        if (CURRENT_STARS >= 3) {
                            stars_playPage.setScaleX(1.2);
                            stars_playPage.setScaleY(1.2);
                            if (!playPage.getChildren().contains(apHint)) {
                                playPage.getChildren().add(apHint);
                            }

                        }
                    }
                }

            });
        }

    }

    /**
     * Authorship: Yuxuan Yang
     * To show the player information: the name and the number of stars
     */
    class PlayerInfo extends Group {
        String name;
        int stars;
        Rectangle r = new Rectangle(0, 0, 333, 50);
        ScaleTransition st = new ScaleTransition();

        PlayerInfo(String name, int stars) {
            super();
            super.maxWidth(333);
            super.minWidth(333);
            this.name = name;
            this.stars = stars;
            init();
            setEffect();
        }

        void init() {
            // set background
            r.setFill(color_button_default);
            r.setOpacity(0.9);
            r.setArcWidth(10);
            r.setArcHeight(10);
            r.setEffect(pieceShadow_N);


            // set player name
            Text t_name = new Text(name);
            t_name.setFill(color_text_highlight);
            t_name.setFont(new Font("Arial Black", 20));
            t_name.setWrappingWidth(120);
            t_name.setTextAlignment(TextAlignment.CENTER);
            t_name.setTextOrigin(VPos.TOP);
            t_name.setLayoutY((50 - t_name.getBoundsInParent().getHeight()) / 2);
            t_name.setEffect(pieceShadow_N);

            // set the star icon
            Double[] points = new Double[]{
                    24.86, 0.41, 32.41, 15.7, 49.28, 18.16, 37.07, 30.06, 39.95, 46.87, 24.86, 38.94, 9.76, 46.87, 12.64, 30.06, 0.43, 18.16, 17.31, 15.7
            };
            if (!name.equals("+")) {
                Polygon p_star = new Polygon();
                p_star.getPoints().addAll(points);
                p_star.setScaleX(0.5);
                p_star.setScaleY(0.5);
                p_star.setTranslateX(-12);
                p_star.setTranslateY(-12);
                p_star.setLayoutX(220);
                p_star.setLayoutY(12);
                p_star.setFill(Color.GOLD);
                p_star.setEffect(pieceShadow_N);

                // set the total  number of stars
                Text t_stars = new Text("" + stars);
                t_stars.setFill(color_text_highlight);
                t_stars.setFont(new Font("Arial Black", 20));
                t_stars.setWrappingWidth(150);
                t_stars.setTextAlignment(TextAlignment.CENTER);
                t_stars.setTextOrigin(VPos.TOP);
                t_stars.setLayoutX(200);
                t_stars.setLayoutY((50 - t_stars.getBoundsInParent().getHeight()) / 2);
                t_stars.setEffect(pieceShadow_N);
                this.getChildren().addAll(r, t_name, p_star, t_stars);
            } else {
                this.getChildren().addAll(r, t_name);
            }
        }

        void setEffect() {
            st.setNode(this);
            st.setFromX(0.98);
            st.setFromY(0.98);
            st.setToX(1.02);
            st.setToY(1.02);
            st.setDuration(Duration.millis(800));
            st.setCycleCount(Animation.INDEFINITE);
            st.setAutoReverse(true);

            // hover
            this.setOnMouseEntered(mouseEvent -> {
                setCursor(Cursor.HAND);
            });

            this.setOnMouseClicked(mouseEvent -> {

                // achieve the effect of getting/losing focus
                for (Node child : vbPlayers.getChildren()) {
                    if (child instanceof PlayerInfo) {
                        PlayerInfo p = (PlayerInfo) child;
                        p.st.stop();
                        p.r.setFill(color_button_default);
                    }
                }
                r.setFill(color_button_highlight);
                st.play();
                CURRENT_PLAYER = name;

                // update stars
                CURRENT_STARS = stars;
                stars_selectPage.setText("" + CURRENT_STARS);
                System.out.println(CURRENT_PLAYER +" "+CURRENT_STARS);
            });

        }
    }

    /**
     * Authorship: Yuxuan Yang
     * To show the player information: the name and the number of stars
     */
    class Levels extends Group {
        private Rectangle r = new Rectangle(0, 0, 60, 60);
        private Text t = new Text();
        private int n;
        public int level;
        public String id;

        Levels(String id, int level, int n) {
            super();
            super.prefHeight(60);
            super.prefWidth(60);
            this.id = id;
            this.n = n;
            this.level = level;
            init();
            setEvent();
        }

        private void init() {
            DropShadow ds = new DropShadow();
            ds.setBlurType(BlurType.GAUSSIAN);
            ds.setColor(color_background_topBar);
            ds.setOffsetY(3);

            r.setArcHeight(20);
            r.setArcWidth(20);
            r.setFill(color_button_default);
            r.setOpacity(0.85);


            if (n > 0) {
                t.setText(id);
                t.setFont(new Font("Arial black", 20));
                t.setFill(color_text_highlight);
            } else {
                t.setText("?");
                t.setFont(new Font("Arial black", 30));
                t.setFill(color_text_default);
            }
            t.setLayoutY((60 - t.getBoundsInParent().getHeight()) / 2);
            t.setTextAlignment(TextAlignment.CENTER);
            t.setWrappingWidth(60);
            t.setTextOrigin(VPos.TOP);


            Group rating = new Group();
            rating.minWidth(60);
            rating.maxWidth(60);
            Double[] points = new Double[]{
                    24.86, 0.41, 32.41, 15.7, 49.28, 18.16, 37.07, 30.06, 39.95, 46.87, 24.86, 38.94, 9.76, 46.87, 12.64, 30.06, 0.43, 18.16, 17.31, 15.7
            };
            if (n <= 3) {
                for (int i = 0; i < n; i++) {
                    Polygon star = new Polygon();
                    star.getPoints().addAll(points);
                    star.setScaleX(0.3);
                    star.setScaleY(0.3);
                    star.setTranslateX(-15);
                    star.setTranslateY(-15);
                    star.setFill(Color.GOLD);
                    star.setEffect(ds);
                    star.setLayoutX(6 * i);
                    rating.getChildren().add(star);
                }
            }
            super.getChildren().addAll(r, t, rating);
        }

        private void setEvent() {
            if (n > 0) {
                ScaleTransition st = new ScaleTransition();
                st.setNode(this);
                st.setFromX(0.95);
                st.setFromY(0.95);
                st.setToX(1.02);
                st.setToY(1.02);
                st.setDuration(Duration.millis(800));
                st.setCycleCount(Animation.INDEFINITE);
                st.setAutoReverse(true);

                this.setOnMouseEntered(mouseEvent -> {
                    requestFocus();
                    setCursor(Cursor.HAND);
                    r.setFill(color_button_highlight);
                    st.play();
                });

                this.setOnMouseExited(mouseEvent -> {
                    st.stop();
                    this.setScaleX(1);
                    this.setScaleY(1);
                    r.setFill(color_button_default);
                });

                this.setOnMouseClicked(mouseEvent -> {
                    CURRENT_LEVEL = this.level;
                    Animation a = fadeEffectForNode(selectPage, 300);
                    a.setOnFinished(actionEvent -> {
                        removeSelectPage();
                        showWaitingPage();
                        showPlayPage();
                    });
                });
            } else {
                this.setOnMouseEntered(mouseEvent -> {
                    setCursor(Cursor.HAND);
                    t.setFont(new Font("Arial black", 35));
                    t.setLayoutY((60 - t.getBoundsInParent().getHeight()) / 2);
                    t.setFill(color_text_highlight);

                });

                this.setOnMouseExited(mouseEvent -> {
                    t.setFont(new Font("Arial black", 30));
                    t.setLayoutY((60 - t.getBoundsInParent().getHeight()) / 2);
                    t.setFill(color_text_default);

                });

                this.setOnMouseClicked(mouseEvent -> {
                    selectPage.getChildren().add(askUnlock(this.level));
                });
            }
        }
    }


    /* set effect */

    /**
     * Authorship: Yuxuan Yang
     * To show the fade transition when each page is removed.
     */
    private Animation fadeEffectForNode(Node node, double duration) {
        FadeTransition ft = new FadeTransition();
        ft.setNode(node);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setDuration(Duration.millis(duration));
        ft.setInterpolator(Interpolator.EASE_IN);
        ft.play();
        return ft;
    }


    /**
     * Authorship: Yuxuan Yang
     * backend job1: generate a new game --task11
     */
    ScheduledService<Games> service_generateGame = new ScheduledService<Games>() {

        @Override
        protected Task<Games> createTask() {
            Task<Games> task = new Task<Games>() {

                // to execute the code in backend.
                @Override
                protected Games call() throws Exception {
                    String iPlacement = FitGame.generateGames();
                    String[] pieces = iPlacement.split("(?<=\\G.{4})");
                    String iObjective = "";
                    Random random = new Random();
                    int count = random.nextInt(7) + 2;
                    for (int i = 0; i < count; i++) {
                        iObjective += pieces[i];
                    }
                    System.out.println(iObjective + " - " + iPlacement);
                    return new Games(0, iObjective, iPlacement);
                }

                // once finished, update the GUI
                @Override
                protected void updateValue(Games games) {
                    interestingGame = games;
                    showPlayPage();
                    service_generateGame.cancel();
                }

            };
            return task;
        }

    };

    {
        service_generateGame.setDelay(Duration.millis(0));
    }

    /**
     * Authorship: Yuxuan Yang
     * backend job2: load initial GUI board and GUI pieces and show the animation on waiting page
     */
    AnimationTimer timer_loadGame = new AnimationTimer() {
        double i = 0;

        @Override
        public void handle(long l) {
            i++;

            // update the progress bar
            if (i < 90) {
                progressBar.setWidth(i * 3.3);
            } else {
                progress_bar_scale_trans.play();
            }

            // if the play page is ready, then show it.
            if (apLeft.getChildren().contains(guiPieces) && i > 90) {
                i = 0;
                timer_loadGame.stop();
                progress_bar_scale_trans.stop();
                Animation a = fadeEffectForNode(waitingPage, 500);
                a.setOnFinished(actionEvent -> {
                    removeWaitingPage();
                    progressBar.setWidth(0);
                    root.getChildren().add(playPage);
                    playPage.setOpacity(1);
                    timer_cost.start();
                    BEGIN_TIME = System.currentTimeMillis();
                });
            }
        }
    };

    /**
     * Authorship: Yuxuan Yang
     * backend job3: load initial player information
     */
    AnimationTimer timer_loadPlayer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if (selectPage.getChildren().contains(vbSelect)) {
                showSelectPage();
            }
        }
    };

    /**
     * Authorship: Yuxuan Yang, Paul Won
     * backend job4: count the time consuming and give the rating, if the player finished, then play "win" music, pause BGM;
     */
    AnimationTimer timer_cost = new AnimationTimer() {

        // to count how many stars the player will get.
        int n = 0;

        @Override
        public void handle(long l) {
            if (fitGame.checkWin(fitGame.getMatrixBoard())) {

                // stop the BGM
                mediaPlayer.stop();

                // play success music
                successMP.play();

                // stop the timer
                timer_cost.stop();

                // calculate the time cost
                long end = System.currentTimeMillis();
                Duration duration = new Duration(end - BEGIN_TIME);
                String res = "";
                int sec = (int) duration.toSeconds();

                if (sec / 60 >= 10) {
                    res += (sec / 60) + " : ";
                } else {
                    res += "0" + (sec / 60) + " : ";
                }
                if (sec % 60 >= 10) {
                    res += sec % 60;
                } else {
                    res += "0" + sec % 60;
                }

                // show the time cost
                TIME_COST.setText(res);

                // to show the win page
                showWinPage();

                // to give the player a rating.
                if (duration.toSeconds() < 120) {
                    n = 3;
                } else if (duration.toSeconds() < 300) {
                    n = 2;
                } else {
                    n = 1;
                }

                // show the rating effect according to "n" stars
                for (int i = 0; i < n; i++) {
                    star_effect[i].play();
                }

                // reward stars
                switch (difficulty) {
                    case 0:
                        if (RECORD_STARTER[CURRENT_LEVEL] != 9) {
                            if (RECORD_STARTER[CURRENT_LEVEL] < n) {
                                CURRENT_STARS += (n - RECORD_STARTER[CURRENT_LEVEL]);
                                RECORD_STARTER[CURRENT_LEVEL] = n;
                            }
                        } else {
                            CURRENT_STARS += n;
                            RECORD_STARTER[CURRENT_LEVEL] = n;
                        }
                        break;
                    case 1:

                        if (RECORD_JUNIOR[CURRENT_LEVEL] != 9) {

                            if (RECORD_JUNIOR[CURRENT_LEVEL] < n) {
                                CURRENT_STARS += (n - RECORD_JUNIOR[CURRENT_LEVEL]);
                                RECORD_JUNIOR[CURRENT_LEVEL] = n;
                            }
                        } else {
                            CURRENT_STARS += n;
                            RECORD_JUNIOR[CURRENT_LEVEL] = n;
                        }
                        break;
                    case 2:
                        if (RECORD_EXPERT[CURRENT_LEVEL] != 9) {
                            if (RECORD_EXPERT[CURRENT_LEVEL] < n) {
                                CURRENT_STARS += (n - RECORD_EXPERT[CURRENT_LEVEL]);
                                RECORD_EXPERT[CURRENT_LEVEL] = n;
                            }
                        } else {
                            CURRENT_STARS += n;
                            RECORD_EXPERT[CURRENT_LEVEL] = n;
                        }
                        break;
                    case 3:
                        if (RECORD_MASTER[CURRENT_LEVEL] != 9) {
                            if (RECORD_MASTER[CURRENT_LEVEL] < n) {
                                CURRENT_STARS += (n - RECORD_MASTER[CURRENT_LEVEL]);
                                RECORD_MASTER[CURRENT_LEVEL] = n;
                            }
                        } else {
                            CURRENT_STARS += n;
                            RECORD_MASTER[CURRENT_LEVEL] = n;
                        }
                        break;
                    case 4:
                        if (RECORD_WIZARD[CURRENT_LEVEL] != 9) {
                            if (RECORD_WIZARD[CURRENT_LEVEL] < n) {
                                CURRENT_STARS += (n - RECORD_WIZARD[CURRENT_LEVEL]);
                                RECORD_WIZARD[CURRENT_LEVEL] = n;
                            }
                        } else {
                            CURRENT_STARS += n;
                            RECORD_WIZARD[CURRENT_LEVEL] = n;
                        }
                        break;
                    default:
                        CURRENT_STARS += n;
                        break;
                }
                // update stars
                stars_playPage.setText("" + CURRENT_STARS);
                stars_selectPage.setText("" + CURRENT_STARS);
            }
        }
    };
}
