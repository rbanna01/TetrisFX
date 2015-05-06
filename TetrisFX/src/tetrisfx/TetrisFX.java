/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetrisfx;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;


/**
 * Tests:  
 * to do: gear shifts after ten minutes? Making drawing purtier
 * @author Ruaridhi Bannatyne (to8451@tom.com)
 */
public class TetrisFX extends Application {
    /*
    Whether a game is in progress.
    */
    private boolean playing;
    /*
    Whether it's paused.
    */
    private boolean paused;
    
    /*
    Quit and start buttons.
    */
    private Button qB;
    private Button sB;
    /*
    Text for them.
    */
    private String stopText = "Quit";
    private String goText = "Start game";
    private String playingGo = "Pause";
    private String playingStop = "End game";
    private String pauseGo = "Resume";
    /*
    GameCanvas holds game model and view.
    */
    private GameCanvas gC;
    /*
    Dimensions of the window.
    */
    private int HEIGHT = 530;
    private int WIDTH = 200;
    
    @Override
    public void start(Stage primaryStage) {
        playing = false;
        sB = new Button();
        sB.setText(goText);
        sB.setOnAction(mH);
        qB = new Button();
        qB.setOnAction(mH);
        qB.setText(stopText);
        FlowPane root = new FlowPane(javafx.geometry.Orientation.VERTICAL);
        root.setVgap(5.0);
        gC = new GameCanvas(WIDTH, HEIGHT-70);
        //root.setPrefWidth()
        Canvas c =  gC.getCanvas();
        root.getChildren().addAll(c, sB, qB);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        root.addEventHandler(KeyEvent.KEY_PRESSED, kH);
        primaryStage.setTitle("TetrisFX!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /*
    Handles all events passed to the qB and sB Buttons.
    */
    private EventHandler<ActionEvent> mH = new EventHandler<ActionEvent>() {
        
      @Override
      public void handle(ActionEvent aE) { //need  modification to stop when playing
           Object source = aE.getSource(); //and vice versa
           if(source == sB) {
               if(playing) {
                   doPause();
               }
               else {
                   playing = true;
                   sB.setText(playingGo);
                   qB.setText(playingStop);
                   gC.startGame();
               }
           }
           else if (source == qB) {
                   if (playing) {
                   if (!paused) gC.doPause();
                   Alert alert = new Alert(AlertType.CONFIRMATION);
                   alert.setTitle("End game?");
                   alert.setContentText("Click ok to confirm , or no to cancel");
                   Optional<ButtonType> result = alert.showAndWait();
                   if (result.get() == ButtonType.OK) {
                   gC.endGame();
                   playing = false;
                   paused = false;
                   sB.setText(goText);
                   qB.setText(stopText);
                   }
                   else {
                       gC.doPause();
                   }
                }
               else { 
                   if (!paused) gC.doPause();
                   Alert alert = new Alert(AlertType.CONFIRMATION);
                   alert.setTitle("Close TetrisFX?");
                   alert.setContentText("Click ok to confirm and exit, or cancel");
                   Optional<ButtonType> result = alert.showAndWait();
                   if (result.get() == ButtonType.OK) {
                   Platform.exit();
                   }
                }
           }
      }
   };
    
    /*
    Handles all events from the keyboard. Cursor keys and 'p'.
    */
    private EventHandler<KeyEvent> kH = new EventHandler<KeyEvent>() {
       @Override 
       public void handle(KeyEvent e) {
           KeyCode code = e.getCode();
           if (playing) {
              if (code == KeyCode.P) { 
                  doPause();
            }
            else {
                if ( code == KeyCode.LEFT || code == KeyCode.RIGHT ) {
                    gC.move(getOffset(code), 0);
                }
                else if (code == KeyCode.UP || code == KeyCode.DOWN) {
                    gC.move(0, getOffset(code));
                }
           } 
           }           
        }
    };
    
    /*
    Pauses or unpauses game (i.e. this and the GameCanvas)
    */
    private void doPause() {
        if (playing) {
        if (paused) {
                paused = false;
                sB.setText(playingGo);
                gC.doPause();
                }
        else {
                paused = true;
                sB.setText(pauseGo);
                gC.doPause();
                }
        }
        //else do nothing?
    }
    
    /* Converts a KeyCode representing a direction into input suited for GameCanvas.
      @param - KeyCode assumed to be one of: KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT,
                                                                      KeyCode.RIGHT
      @return - integer representing adjustment to be made to the falling block's
     x or y-coordinates.
    */
    private int getOffset(KeyCode direction) {
            if (direction == KeyCode.RIGHT || direction == KeyCode.DOWN) return 1;
            else return -1;
    }    

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
