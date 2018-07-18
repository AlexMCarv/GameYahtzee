import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.geometry.*;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;

public class DiceRoll extends Application{

  Player player1;
  ScoreBoard p1ScoreBoard = new ScoreBoard(this);
  Player player2;
  ScoreBoard p2ScoreBoard = new ScoreBoard(this);
  Player activePlayer;

  Button roll1 = new Button("Roll");
  Button roll2 = new Button("Roll");
  GridPane scoreSheet;
  VBox status;

  Label lcurrentPlaying = new Label("");
  Label lscore = new Label("");
  Label lwinner = new Label("");
  final int REROLL = 2;
  int turn = 1;

  public void start(Stage stage){
    // Stage Setup
    stage.setTitle("Dice Roll");

    //Player Setup
    player1 = new Player(5, p1ScoreBoard);
    player2 = new Player(5, p2ScoreBoard);
    activePlayer = player1;

    //Game Screen Setup
    FlowPane gameScreen = new FlowPane(Orientation.VERTICAL);
    gameScreen.setAlignment(Pos.CENTER);

    roll1.setOnAction(this::roll);
    roll1.setDisable(true);
    roll2.setOnAction(this::roll);
    roll2.setDisable(true);

    Label lPlayer1 = new Label("Player 1");
    Label lPlayer2 = new Label("Player 2");
    status = new VBox();
    status.getChildren().addAll(lcurrentPlaying, lscore, lwinner);
    status.setAlignment(Pos.CENTER);
    status.setPadding(new Insets(50, 10, 50, 10));

    VBox diceLocation = new VBox(lPlayer1, player1, roll1, status, lPlayer2, player2, roll2);
    diceLocation.setAlignment(Pos.CENTER);
    diceLocation.setSpacing(10);
    gameScreen.getChildren().addAll(diceLocation);

    //Score Sheet Setup
    GridPane headerPane = new GridPane();
    Label[] header = p1ScoreBoard.getScoreHeader();
    for (int i = 0; i < header.length; i++){
      GridPane.setConstraints(header[i], 0, i);
      header[i].setPadding(new Insets(5.0, 5.0, 5.0, 5.0));
      headerPane.getChildren().addAll(header[i]);
    }
    scoreSheet = new GridPane();
    GridPane.setConstraints(headerPane, 0, 0);
    GridPane.setConstraints(p1ScoreBoard, 1, 0);
    GridPane.setConstraints(p2ScoreBoard, 2, 0);
    scoreSheet.getChildren().addAll(headerPane, p1ScoreBoard, p2ScoreBoard);

    //Game Window Setup
    BorderPane layout = new BorderPane();
    layout.setCenter(gameScreen);
    layout.setRight(scoreSheet);
    BorderPane.setAlignment(scoreSheet, Pos.CENTER_LEFT);
    layout.setPadding(new Insets(10, 10, 10, 10));
    enableAction();

    Scene scene = new Scene(layout, 600, 450);
    stage.setScene(scene);
    stage.show();
  }

  public void enableAction(){
    if (activePlayer.equals(player1)){
      roll1.setDisable(false);
    } else {
      roll2.setDisable(false);
    }
  }

  public void roll (ActionEvent event){
    Button selected = (Button)(event.getSource());
    if (selected.equals(roll1)) {
      player1.roll();
      p1ScoreBoard.setPossibleMoves(player1.getDieTypes());
      lcurrentPlaying.setText("Player 1 - You have " + ((REROLL + 1)-turn)  + " re-rolls left");
    } else {
      player2.roll();
      p2ScoreBoard.setPossibleMoves(player2.getDieTypes());
      lcurrentPlaying.setText("Player 2 - You have " + ((REROLL + 1)-turn)  + " re-rolls left");
    }

    turn++;
    if (turn > (REROLL + 1)){
      selected.setDisable(true);
    }
  }

  public void executeRound(ActionEvent event){
    if (activePlayer.equals(player1)){
      p1ScoreBoard.chooseMove(event);
      player1.resetHand();
      player1.setTotalScore();
    } else {
      p2ScoreBoard.chooseMove(event);
      player2.resetHand();
      player2.setTotalScore();
    }

    if (p1ScoreBoard.isGameOver() && p2ScoreBoard.isGameOver()){
      String winner = "";
      if (player1.getTotalScore() > player2.getTotalScore()){
        lwinner.setText("Player 1 Wins!");
      } else if (player1.getTotalScore() < player2.getTotalScore()){
        lwinner.setText("Player 2 Wins!");
      } else {
        lwinner.setText("Tie!");
      }
      lwinner.setFont(new Font("Arial", 30));
      lcurrentPlaying.setText("Game Over!");
      lscore.setText("Score Player 1: " + player1.getTotalScore() +
                        " - Player 2: " + player2.getTotalScore());
      roll1.setDisable(true);
      roll2.setDisable(true);

      return;
    }
    swapAndRestart();
  }

  public void swapAndRestart(){
    turn = 1;
    if (activePlayer.equals(player1)){
      activePlayer = player2;
      roll2.setDisable(false);
      roll1.setDisable(true);
      lcurrentPlaying.setText("Player 2 - You have " + ((REROLL + 1)-turn)  + " re-rolls left");
    } else {
      activePlayer = player1;
      roll1.setDisable(false);
      roll2.setDisable(true);
      lcurrentPlaying.setText("Player 1 - You have " + ((REROLL + 1)-turn)  + " re-rolls left");
    }
  }
  
	public static void main(String [] args)	{
		launch(args);
	}
}
