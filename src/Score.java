import javafx.scene.control.Button;

public class Score extends Button{

  private boolean fixed = false;

  public void init(){
    setDisable(true);
  }

  public void setFixed(){
    fixed = true;
    setDisable(true);
  }

  public boolean isFixed(){
    return fixed;
  }
}
