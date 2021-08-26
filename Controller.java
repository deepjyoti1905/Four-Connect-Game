package com.intershala.connectfour;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {
    private static final int COLUMNS=7;
    private static final int ROWS=6;
    private static final int CIRCLE_DIAMETER=80;
    private static final String discColor1="#24303E";
    private static final String discColor2="#4CAA88";




    //Player Names

   private static String input1;
    private static String input2;

    private boolean isPlayerOneTurn=true;
    private Disc[][] insertedDiscsArray=new Disc[ROWS][COLUMNS];

    @FXML
    public GridPane rootGridPane;
    @FXML
    public Pane insertedDiscPane;
    @FXML
    public TextField playerOne;
    @FXML
    public TextField playerTwo;
        @FXML
        public Button setNames;
    @FXML
    public Label playerName;
    @FXML
    public Label turn;


    public void PlayGround() {
        Platform.runLater(() -> setNames.requestFocus());
Shape rectangleWithHoles=createGameStruturalGrid();
     //Adding in GridPane
     rootGridPane.add(rectangleWithHoles,0,1);

     List <Rectangle> rectangleList=clickableColumns();
        for (Rectangle rectangle:rectangleList) {
            rootGridPane.add(rectangle,0,1);
            setNames.setOnAction(event -> {
                input1 = playerOne.getText();
                input2 = playerTwo.getText();
                playerName.setText(isPlayerOneTurn? input1 : input2);
            });
        }

    }
    private Shape createGameStruturalGrid()
    {
        Shape rectangleWithHoles = new Rectangle((COLUMNS+1) * CIRCLE_DIAMETER, (ROWS+1) * CIRCLE_DIAMETER);
        Circle circle = new Circle();
        for (int row = 0; row < ROWS; row++) {
            for(int col=0;col<COLUMNS;col++)
            {
                circle.setRadius(CIRCLE_DIAMETER / 2);
                circle.setCenterX(CIRCLE_DIAMETER / 2);
                circle.setCenterY(CIRCLE_DIAMETER / 2);
                circle.setTranslateX(col*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
                circle.setTranslateY(row*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
                rectangleWithHoles= Shape.subtract(rectangleWithHoles,circle);

            }
        }

        rectangleWithHoles.setFill(Color.WHITE);
        return rectangleWithHoles;
    }
    private List<Rectangle> clickableColumns()
    {
        List<Rectangle> rectangleList=new ArrayList<>();
        for(int col=0;col<COLUMNS;col++) {
            Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);

rectangleList.add(rectangle);
rectangle.setOnMouseEntered(event ->rectangle.setFill(Color.valueOf("#eeeeee26")) );
rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));
            final int column=col;
            rectangle.setOnMouseClicked(event ->
{

    insertDisc(new Disc(isPlayerOneTurn),column);
});
        }
        return rectangleList;
    }

    private void insertDisc(Disc disc,int column) {
        int rows=ROWS-1;
        while(rows>=0)
        {
            if(getDiscIfPresent(rows,column)==null)
                break;
            rows--;
        }
        if (rows<0)
            return;
        //For structural Changes
        insertedDiscsArray[rows][column]=disc;
        insertedDiscPane.getChildren().add(disc);
        disc.setTranslateX(column*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),disc);
        translateTransition.setToY(rows*(CIRCLE_DIAMETER+5)+CIRCLE_DIAMETER/4);
        int currRow=rows;
        translateTransition.setOnFinished(event -> {
            if(gameEnded(currRow,column))
            {
gameOver();
return;
            }
            isPlayerOneTurn=!isPlayerOneTurn;
            playerName.setText(isPlayerOneTurn?input1:input2);
        });
        translateTransition.play();
    }
    private boolean gameEnded(int rows,int column)
    {
        //vertical lines
        List<Point2D> verticalPoints = IntStream.rangeClosed(rows - 3, rows + 3).mapToObj(r -> new Point2D(r, column)).collect(Collectors.toList());
        List<Point2D> horizontalPoints = IntStream.rangeClosed(column - 3, column + 3).mapToObj(col -> new Point2D(rows, col)).collect(Collectors.toList());
        Point2D startpoint1=new Point2D(rows-3,column+3);
        List<Point2D> diagonalPoints=IntStream.rangeClosed(0,6).mapToObj(i->startpoint1.add(i,-i)).collect(Collectors.toList());
        Point2D startpoint2=new Point2D(rows-3,column-3);
        List<Point2D> diagonal2Points=IntStream.rangeClosed(0,6).mapToObj(i->startpoint2.add(i,i)).collect(Collectors.toList());
        boolean isEnded=checkCombinations(verticalPoints) || checkCombinations(horizontalPoints)||checkCombinations(diagonalPoints)||checkCombinations(diagonal2Points);
       return isEnded;
    }

    private boolean checkCombinations(List<Point2D> points) {
        int chain=0;
        for(Point2D point:points)
        {
            int rowIndexForArray= (int) point.getX();
            int columnIndexForArray= (int) point.getY();
            Disc disc=getDiscIfPresent(rowIndexForArray,columnIndexForArray);
            if (disc!=null && disc.isPlayerOneMove==isPlayerOneTurn)
            {
                chain++;
               if (chain==4)
               { return true;}
            }
            else
            {
                chain=0;

            }
         }
        return false;

    }
    private Disc getDiscIfPresent(int rows,int column)
    {
if(rows>=ROWS||rows<0||column>=COLUMNS||column<0)
    return null;
return insertedDiscsArray[rows][column];
    }


    private void gameOver() {
        String winner = isPlayerOneTurn ? input1 :input2;
        System.out.println("Winner is " + winner);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect Four");
        alert.setHeaderText("The winner is " + winner);
        alert.setContentText("Want to play again?");
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);
        Platform.runLater(() ->
        {
            Optional<ButtonType> btnclicked = alert.showAndWait();
            if (btnclicked.isPresent() && btnclicked.get() == yesButton) {
                resetgame();
            } else {
                Platform.exit();
                System.exit(0);
            }

        });

    }
    private void resetgame() {
        insertedDiscPane.getChildren().clear();
        for (int row = 0; row <insertedDiscsArray.length ; row++) {
            for (int col = 0; col < insertedDiscsArray[row].length; col++) {
                insertedDiscsArray[row][col]=null;
            }
        }
        isPlayerOneTurn=true;
        playerName.setText(input1);
        PlayGround();
    }

    private static class Disc<isPlayerOneMove> extends Circle
{
    private final boolean isPlayerOneMove;
    public Disc(boolean isPlayerOneMove)
    {
this.isPlayerOneMove=isPlayerOneMove;
setRadius(CIRCLE_DIAMETER/2);
setCenterX(CIRCLE_DIAMETER/2);
setCenterY(CIRCLE_DIAMETER/2);
setFill(isPlayerOneMove?Color.valueOf(discColor1):Color.valueOf(discColor2));
    }
}
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }



}