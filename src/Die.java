import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.geometry.*;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.DropShadow;

public class Die extends Button{

  private Rectangle rect;
  private int size = 50;
  private int value;
  private String valueDisplay;
  private final Image face1 = new Image("die_face_1.png", size, size, false, false);
  private final Image face2 = new Image("die_face_2.png", size, size, false, false);
  private final Image face3 = new Image("die_face_3.png", size, size, false, false);
  private final Image face4 = new Image("die_face_4.png", size, size, false, false);
  private final Image face5 = new Image("die_face_5.png", size, size, false, false);
  private final Image face6 = new Image("die_face_6.png", size, size, false, false);
  private ImageView faceDisplay;
  private boolean clicked;
  private DropShadow shadow = new DropShadow();


  public Die(int size){
    this.size = size;
    setMinSize(size,size);
    setPadding(Insets.EMPTY);
    setMaxSize(size,size);
    value = 0;
    clicked = false;
    valueDisplay = String.valueOf(value);
    init();
  }

  public void init(){
    setDisable(true);
    faceDisplay = new ImageView();
    this.setOnAction(this::fixDie);
  }

  public int getFaceValue(){
    return value;
  }

  public boolean isClicked(){
    return clicked;
  }

  public void roll(){
    Random generator = new Random();
    value = generator.nextInt(6) + 1;
    valueDisplay = String.valueOf(value);
    setDisable(false);

    switch (value){
      case 1:
        faceDisplay.setImage(face1);
        break;
      case 2:
        faceDisplay.setImage(face2);
        break;
      case 3:
        faceDisplay.setImage(face3);
        break;
      case 4:
        faceDisplay.setImage(face4);
        break;
      case 5:
        faceDisplay.setImage(face5);
        break;
      case 6:
        faceDisplay.setImage(face6);
        break;
      default:
        faceDisplay.setImage(face1);
    }
    setGraphic(faceDisplay);
  }

  public void fixDie(ActionEvent event){
      if (clicked == false) {
        clicked = true;
        setEffect(shadow);
      } else {
        clicked = false;
        setEffect(null);
      }
    }

    public void reset(){
      clicked = false;
      setEffect(null);
      faceDisplay.setImage(null);
      setDisable(true);
      value = 0;
    }
}
