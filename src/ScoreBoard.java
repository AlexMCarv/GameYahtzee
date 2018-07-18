import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.geometry.*;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;

public class ScoreBoard extends GridPane{
  // Score Board Fields
  private final int ONES = 0;
  private final int TWOS = 1;
  private final int THREES = 2;
  private final int FOURS = 3;
  private final int FIVES = 4;
  private final int SIXES = 5;
  private final int UPPERSUM = 6;
  private final int BONUS = 7;
  private final int THREEKIND = 8;
  private final int FOURKIND = 9;
  private final int FULLHOUSE = 10;
  private final int SMALLSTR = 11;
  private final int LARGESTR = 12;
  private final int CHANCE = 13;
  private final int YAHTZEE = 14;
  private final int TOTAL = 15;

  //
  private DiceRoll instance;
  private boolean gameOver;
  private int possibleMoves;
  private Score[] scoreDisplay = new Score[16];
  private int total = 0;

  private Label[] scoreHeader = {new Label("Ones"), new Label("Twos"), new Label("Threes"),
                        new Label("Fours"), new Label("Fives"), new Label("Sixes"),
                        new Label("Sum"), new Label("Bonus"), new Label("Three of a Kind"),
                        new Label("Four of a Kind"),new Label("Full House"),
                        new Label("Small Straight"), new Label("Large Straight"),
                        new Label("Chance"), new Label("Yahtzee!"), new Label("Total Score")};

  public ScoreBoard(DiceRoll instance){
    this.instance = instance;
    possibleMoves = 0;
    gameOver = false;
    init();
  }

  public void init(){
    setAlignment(Pos.CENTER);
    gridLinesVisibleProperty().set(false);
    setPadding(new Insets(10, 10, 10, 10));

    ColumnConstraints col1 = new ColumnConstraints();
    col1.setMinWidth(50);
    getColumnConstraints().addAll(col1);

    for(int i = 0; i < scoreDisplay.length; i++){
        scoreDisplay[i] = new Score();
        setConstraints(scoreDisplay[i], 0, i);
        scoreDisplay[i].init();
        scoreDisplay[i].setOnAction(instance::executeRound);
        scoreDisplay[i].setMinWidth(col1.getMinWidth());
        getChildren().addAll(scoreDisplay[i]);
    }
    scoreDisplay[UPPERSUM].setFixed();
    scoreDisplay[BONUS].setFixed();
    scoreDisplay[TOTAL].setFixed();
  }

  public void setPossibleMoves(int[] dieType){
    possibleMoves = 0;
    resetBoard();

    for (int i = 0; i < dieType.length; i++){
      if (dieType[i] >= 1 && !scoreDisplay[i].isFixed()){
        scoreDisplay[i].setText("" + (dieType[i]*(i+1)));
        scoreDisplay[i].setDisable(false);
        possibleMoves++;
      }
    }

    if (!scoreDisplay[THREEKIND].isFixed()){
      setThreeOfKind(dieType);
    }

    if (!scoreDisplay[FOURKIND].isFixed()){
      setFourOfKind(dieType);
    }

    if (!scoreDisplay[FULLHOUSE].isFixed()){
      setFullHouse(dieType);
    }

    if (!scoreDisplay[SMALLSTR].isFixed()){
      setSmallStaight(dieType);
    }

    if (!scoreDisplay[LARGESTR].isFixed()){
      setLargeStaight(dieType);
    }

    if (!scoreDisplay[CHANCE].isFixed()){
      setChance(dieType);
    }

    if (!scoreDisplay[YAHTZEE].isFixed()){
      setYahtzee(dieType);
    }

    if (possibleMoves == 0){
      throwAwayMove();
    }
  }

  // This method locks the selected move and clears the board for the next round
  public void chooseMove(ActionEvent event){
    Score selectedMove = (Score)(event.getSource());
    selectedMove.setFixed();
    resetBoard();

    // Gets the stored button value
    try {
      int i = Integer.parseInt(selectedMove.getText());
    } catch (Exception e){
      selectedMove.setText("0");
    }

    setSumScore();
    // If player completed the board, then it is game over
    for (int i = 0; i < scoreDisplay.length; i++){
      if (!scoreDisplay[i].isFixed()){
        return;
      }
    }
    gameOver = true;
}

  // This method re-enable all possible moves, except those previoulsly selected
  public void throwAwayMove(){
    for (int i = 0; i < scoreDisplay.length; i++){
      if (!scoreDisplay[i].isFixed()){
        scoreDisplay[i].setText("");
        scoreDisplay[i].setDisable(false);
      }
    }
  }

  // This method clears the temporary value in all of the score fields and deactivates
  // the buttons. Fields that were selected previoulsly are not affected.
  private void resetBoard(){
    for (int i = 0; i < scoreDisplay.length; i++){
      if (!scoreDisplay[i].isFixed()){
        scoreDisplay[i].setText("");
        scoreDisplay[i].setDisable(true);
      }
    }
    possibleMoves = 0;
  }

  //This method checks if the player have three dice with the same value, and
  //calculates the score for the Three of a Kind move
  private boolean setThreeOfKind(int[] dieType){
    for (int i = 0; i < dieType.length; i++){
      if (dieType[i] >= 3){
        scoreDisplay[THREEKIND].setText("" + (dieType[0] * 1 + dieType[1] * 2 +
                                    dieType[2] * 3 + dieType[3] * 4 +
                                    dieType[4] * 5 + dieType[5] * 6));
        scoreDisplay[THREEKIND].setDisable(false);
        possibleMoves++;
        return true;
      }
    }
    return false;
  }

  //This method checks if the player have four dice with the same value, and
  //calculates the score for the Four of a Kind move
  private boolean setFourOfKind(int[] dieType){
    for (int i = 0; i < dieType.length; i++){
      if (dieType[i] >= 4){
        scoreDisplay[FOURKIND].setText("" + (dieType[0] * 1 + dieType[1] * 2 +
                                    dieType[2] * 3 + dieType[3] * 4 +
                                    dieType[4] * 5 + dieType[5] * 6));
        scoreDisplay[FOURKIND].setDisable(false);
        possibleMoves++;
        return true;
      }
    }
    return false;
  }

  //This method checks if the player have a combination of three dice, and two
  // with the same value. Calculates the score for the Full House move
  private boolean setFullHouse(int[] dieType){
    boolean hasTwoDice = false;
    boolean hasThreeDice = false;

    for (int i = 0; i < dieType.length; i++){
      if (dieType[i] == 2){
        hasTwoDice = true;
      }

      if (dieType[i] == 3){
        hasThreeDice = true;
      }
    }

    if(hasTwoDice && hasThreeDice){
      scoreDisplay[FULLHOUSE].setText("25");
      scoreDisplay[FULLHOUSE].setDisable(false);
      possibleMoves++;
      return true;
    }
    return false;
  }

  //This method checks if the player have a four dice sequence, and
  //sets the score for the Small Straight move
  private boolean setSmallStaight(int[] dieType){
    if ((dieType[0] >= 1 && dieType[1] >= 1 && dieType[2] >= 1 && dieType[3] >= 1) ||
        (dieType[1] >= 1 && dieType[2] >= 1 && dieType[3] >= 1 && dieType[4] >= 1) ||
        (dieType[2] >= 1 && dieType[3] >= 1 && dieType[4] >= 1 && dieType[5] >= 1)){
          scoreDisplay[SMALLSTR].setText("30");
          scoreDisplay[SMALLSTR].setDisable(false);
          possibleMoves++;
          return true;
      }
    return false;
  }

  //This method checks if the player have a full sequence, and
  //sets the score for the Large Straight move
  private boolean setLargeStaight(int[] dieType){
    if ((dieType[0] == 1 && dieType[1]== 1 && dieType[2]== 1 && dieType[3]== 1 && dieType[4]== 1) ||
        (dieType[1] == 1 && dieType[2]== 1 && dieType[3]== 1 && dieType[4]== 1 && dieType[5]== 1)){
          scoreDisplay[LARGESTR].setText("40");
          scoreDisplay[LARGESTR].setDisable(false);
          possibleMoves++;
          return true;
      }
    return false;
  }

  //This method sums the dice value sets the score for the Chance move
  private boolean setChance(int[] dieType){
    int sum = 0;
    for (int i = 0; i < dieType.length; i++){
      sum += (dieType[i] * (i+1));
    }
    scoreDisplay[CHANCE].setText("" + sum);
    scoreDisplay[CHANCE].setDisable(false);
    possibleMoves++;
    return true;
  }

  //This method checks if the player have five dice with same value, and
  //sets the score for the Yahtzee move
  private boolean setYahtzee(int[] dieType){
    for (int i = 0; i < dieType.length; i++){
      if (dieType[i] == 5){
        scoreDisplay[YAHTZEE].setText("50");
        scoreDisplay[YAHTZEE].setDisable(false);
        possibleMoves++;
        return true;
      }
    }
    return false;
  }

  //This method sets and updates the sum, bonus and total score fields
  private void setSumScore(){
    //Upper Section Sum Setup
    int sum = 0;
    for (int i = 0; i < UPPERSUM; i++){
        if (scoreDisplay[i].isFixed()){
          sum += Integer.parseInt(scoreDisplay[i].getText());
        }
    }

    scoreDisplay[UPPERSUM].setText("" + sum);
    scoreDisplay[UPPERSUM].setDisable(true);
    scoreDisplay[BONUS].setDisable(true);

    //Bonus Field Setup
    int bonus = 0;
    if (sum > 63){
      bonus = 35;
      scoreDisplay[BONUS].setText("" + bonus);
    }

    //Total Score Sum Setup
    total = 0;
    for (int i = UPPERSUM; i < (scoreDisplay.length-1); i++){
        if (scoreDisplay[i].isFixed()){
          int add;
          try{
            add = Integer.parseInt(scoreDisplay[i].getText());
          } catch (NumberFormatException e){
            add = 0;
          }
          total += add;
        }
    }
    scoreDisplay[TOTAL].setText("" + total);
    scoreDisplay[TOTAL].setDisable(true);
  }

  public boolean isGameOver(){
    return gameOver;
  }

  public Label[] getScoreHeader(){
    return scoreHeader;
  }

  public int getTotal(){
    return total;
  }
}
