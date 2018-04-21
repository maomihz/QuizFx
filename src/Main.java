import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;


/**
 *
 * @author Anandi
 */
public class Main extends Application implements EventHandler<ActionEvent> {
  Button btn, btn2;
  Button tbtn, bbtn, lbtn, rbtn;
  Text ctext;

  public Main() {
    btn = new Button("Say 'Hello World'");
    btn2 = new Button("Are You OK?");
    tbtn = new Button("hello, top");
    bbtn = new Button("hello, bottom");
    lbtn = new Button("hello, left");
    rbtn = new Button("hello, right");
    ctext = new Text("JavaFx");
  }

  @Override
  public void handle(ActionEvent event) {
    if (event.getSource() == btn) {
      System.out.println("Hello World!");
    } else if (event.getSource() == btn2) {
      System.out.println("Are YOU OK???");
    } else if (event.getSource() == tbtn) {
      System.out.println("Hello from the top!");
    } else if (event.getSource() == bbtn) {
      System.out.println("Hello from the bottom!");
    } else if (event.getSource() == lbtn) {
      System.out.println("Hello from the left!");
    } else if (event.getSource() == rbtn) {
      System.out.println("Hello from the right!");
    }
  }

  @Override
  public void start(Stage primaryStage) {
    btn.setOnAction(this);
    btn.setRotate(60);

    btn2.setOnAction(this);

    GridPane primaryPane = new GridPane();
    primaryPane.setAlignment(Pos.CENTER);
    primaryPane.setPadding(new Insets(10,11,12,13));
    primaryPane.setVgap(14);

    primaryPane.add(btn, 0, 0);
    primaryPane.add(btn2, 1, 0);
    primaryPane.add(new TextField(), 0, 1);
    primaryPane.add(new TextField(), 1, 1);

    Scene scene1 = new Scene(primaryPane, 400, 300);

    primaryStage.setTitle("Hello World!");
    primaryStage.setScene(scene1);
    primaryStage.show();


    Stage newStage = new Stage();

    tbtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    bbtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    lbtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    rbtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    tbtn.setOnAction(this);
    bbtn.setOnAction(this);
    lbtn.setOnAction(this);
    rbtn.setOnAction(this);

    ctext.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 50));
    ctext.setUnderline(true);
    ctext.setFill(Color.RED);

    BorderPane bp = new BorderPane();
    bp.setTop(tbtn);
    bp.setBottom(bbtn);
    bp.setLeft(lbtn);
    bp.setRight(rbtn);
    bp.setCenter(ctext);

    Scene newScene = new Scene(bp, 400, 300);

    newStage.setTitle("Stage 2");
    newStage.setScene(newScene);
    newStage.show();
  }

    /**
     * @param args the command line arguments
     */
  public static void main(String[] args) {
    launch(args);
  }

}
