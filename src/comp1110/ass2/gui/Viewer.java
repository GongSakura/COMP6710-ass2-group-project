package comp1110.ass2.gui;

import comp1110.ass2.FitGame;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 * A very simple viewer for piece placements in the IQ-Fit game.
 * <p>
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 */
public class Viewer extends Application {

    /* board layout */
    private static final int SQUARE_SIZE = 60;
    private static final int VIEWER_WIDTH = 720;
    private static final int VIEWER_HEIGHT = 480;
    private static final String URI_BASE = "comp1110/ass2/gui/assets/";
    private final Group root = new Group();
    private final Group controls = new Group();
    private TextField textField;
    private final Group pieceGroup = new Group();
    private final Group matrixBoard = new Group();
    private final Group lineBoard = new Group();

    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement A valid placement string
     */
    void makePlacement(String placement) {
        // remove any previously drawn one
        pieceGroup.getChildren().clear();

        String[] pieces = placement.split("(?<=\\G.{4})");
        if (FitGame.isPlacementWellFormed(placement)) {
            for (String piece : pieces) {
                char pieceID = piece.charAt(0);
                // currently positions exaggerated
                int col = Integer.parseInt(piece.substring(1, 2));
                int row = Integer.parseInt(piece.substring(2, 3));
                String orientation = piece.charAt(3) + "";
                int letter;
                if (pieceID == Character.toUpperCase(pieceID)) {
                    letter = 2;
                } else {
                    letter = 1;
                }

                //choose a piece and set the size of image
                ImageView imageView = new ImageView(new Image(URI_BASE + pieceID + letter + ".png"));
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(120);

                //set the location
                int x = col * 60;
                int y = row * 60;
                imageView.setLayoutX(x);
                imageView.setLayoutY(y);

                //get the bound of imageView
                Bounds bounds = imageView.getBoundsInParent();
                double w = bounds.getWidth();
                double h = bounds.getHeight();
//                System.out.println(bounds);

                //rotate the piece to correct orientation
                switch (orientation) {
                    case "N":
                        break;
                    case "E":
                        imageView.setRotate(90);
                        imageView.setTranslateX(h / 2 - w / 2);
                        imageView.setTranslateY(w / 2 - h / 2);
                        break;
                    case "S":
                        imageView.setRotate(180);
                        break;
                    case "W":
                        imageView.setRotate(270);
                        imageView.setTranslateX(h / 2 - w / 2);
                        imageView.setTranslateY(w / 2 - h / 2);
                        break;
                }

                pieceGroup.getChildren().add(imageView);
            }
        }
    }

    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                makePlacement(textField.getText());
                textField.clear();
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    /**
     * Load the board image
     */
    private void drawBoard() {
        ImageView boardView = new ImageView(new Image(URI_BASE+"board.png"));
        boardView.setFitWidth(700);
        boardView.setFitHeight(380);
        matrixBoard.getChildren().add(boardView);

    }

    public void drawLine(){
        //draw column
        for (int i = 0; i < 10; i++) {
            Line l = new Line(60 * (i + 1), 0, 60 * (i + 1), 300);
            lineBoard.getChildren().addAll(l);
        }

        //draw rows
        for (int i = 0; i < 5; i++) {
            Line l = new Line(0, 60 * (i + 1), 600, 60 * (i + 1));
            lineBoard.getChildren().addAll(l);
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("FitGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);

        root.getChildren().add(matrixBoard);
        root.getChildren().add(controls);
        root.getChildren().add(pieceGroup);
        root.getChildren().add(lineBoard);

        pieceGroup.setLayoutX(60);
        pieceGroup.setLayoutY(60);
        lineBoard.setLayoutX(60);
        lineBoard.setLayoutY(60);
        matrixBoard.setLayoutX(10);
        matrixBoard.setLayoutY(30);

        drawBoard();
        makeControls();
//        makePlacement("B03SG70Si52SL00Nn01Eo63Sp20Er41WS40Ny62N");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
