package com.intershala.connectfour;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    private Controller controller;


    @Override
    public void init() throws Exception {
      //  System.out.println("init");
        super.stop();
    }
    @Override

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane rootGridPane = loader.load();
        controller = loader.getController();
        controller.PlayGround();
        MenuBar menuBar=createMenu();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        Pane menuPane = (Pane) rootGridPane.getChildren().get(0);
        menuPane.getChildren().add(menuBar);
        Scene scene = new Scene(rootGridPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect Four");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
        private MenuBar createMenu()
    {
            //File Menu
            Menu fileMenu=new Menu("File");
            MenuItem newGame=new MenuItem("New Game");
            newGame.setOnAction(event -> {
                resetGame();
            });
            MenuItem resetGame=new MenuItem("ResetGame");
            resetGame.setOnAction(event -> {
                resetGame();
            });
            SeparatorMenuItem separator=new SeparatorMenuItem();
            MenuItem quitGame=new MenuItem("Exit Game");
            quitGame.setOnAction(event -> exitGame());
            fileMenu.getItems().addAll(newGame,resetGame,separator,quitGame);
                //Help Menu
            Menu helpMenu=new Menu("Help");
            MenuItem aboutGame=new MenuItem("About Connect4");
            aboutGame.setOnAction(event -> aboutConnect4());
        SeparatorMenuItem sep=new SeparatorMenuItem();
            MenuItem aboutMe=new MenuItem("About Me");
aboutMe.setOnAction(event -> aboutMe());
        helpMenu.getItems().addAll(aboutGame,sep,aboutMe);
            //Menu Bar
            MenuBar menuBar=new MenuBar();
            menuBar.getMenus().addAll(fileMenu,helpMenu);
            return menuBar;
        }

    private void aboutMe() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About the Developer");
        alert.setHeaderText("Deep Jyoti");
        alert.setContentText("This is my first Game tha I learn in my trainig");
        alert.show();

    }

    private void aboutConnect4() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How To Play");
        alert.setHeaderText("How To Play");
        alert.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves.\n" +
                "\n");
        alert.show();
    }

    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }

    private void resetGame() {
        //TODO
    }

    @Override
    public void stop() throws Exception{
      //  System.out.println("Stop");
        super.stop();

    }


}
