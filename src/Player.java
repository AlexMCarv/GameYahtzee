import javafx.scene.layout.HBox;
import javafx.geometry.*;


public class Player extends HBox {

  private int numberOfDice;
  private ScoreBoard scoreBoard;
  private Die[] dice;
  private int totalScore;
  private int[] dieTypes;
  private boolean endedTurn;

  public Player(int numberOfDice, ScoreBoard scoreBoard){
    this.numberOfDice = numberOfDice;
    this.scoreBoard = scoreBoard;
    this.dice = new Die[numberOfDice];
    totalScore = 0;
    this.dieTypes = new int[6];
    endedTurn = false;
    init();
  }

  public void init(){
    setSpacing(10);
    setAlignment(Pos.CENTER);
    for (int i = 0; i < numberOfDice; i++){
      dice[i] = new Die(50);
      getChildren().add(dice[i]);
    }
  }

  public void roll(){
    for (Die die:dice){
      if (!die.isClicked()){
        die.roll();
      }
    }
    setDieTypes();
  }

  public ScoreBoard getScoreBoard(){
   return scoreBoard;
  }

  public void setTotalScore(){
    totalScore = scoreBoard.getTotal();
  }

  public int getTotalScore(){
   return totalScore;
  }

  // This method updates the player hand with the amount of dice of each type
  // he currently has.
  public void setDieTypes(){
    dieTypes = new int[6];

    for (Die die:dice){
      int dieValue = die.getFaceValue();
      switch (dieValue){
        case 1:
          dieTypes[0]++;
          break;
        case 2:
          dieTypes[1]++;
          break;
        case 3:
          dieTypes[2]++;
          break;
        case 4:
          dieTypes[3]++;
          break;
        case 5:
          dieTypes[4]++;
          break;
        case 6:
          dieTypes[5]++;
          break;
        }
      }
    }

    public int[] getDieTypes(){
      return dieTypes;
    }

    public boolean getEndedTurn(){
      return endedTurn;
    }

    public void endTurn(){
      endedTurn = true;
    }

    public void resetHand(){
        for (Die die:dice){
          die.reset();
        }
        endedTurn = false;
    }
}
