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

//placeholder
/**
 * Tests:  
 * to do: gear shifts after ten minutes? Making drawing purtier
 * @author Ruaridhi Bannatyne (to8451@tom.com)
 */
public class TetrisFX extends Application {
    //needed: stop and go buttons, String for score, tetrisGame object
    //gameOn state var
    private boolean playing;
    private boolean paused;
    private Button qB;
    private Button sB;
    private String stopText = "Quit";
    private String goText = "Start game";
    private String playingGo = "Pause";
    private String playingStop = "End game";
    private String pauseGo = "Resume";
    private GameCanvas gC;
    //private Tetris game;
    //dimensions: 100 x 200?
    private int HEIGHT = 500;
    private int WIDTH = 175;
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
        gC = new GameCanvas(WIDTH, HEIGHT-100);
        Canvas c =  gC.getCanvas();
        root.getChildren().addAll(c, sB, qB);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        root.addEventHandler(KeyEvent.KEY_PRESSED, kH);
        primaryStage.setTitle("TetrisFX!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private EventHandler<ActionEvent> mH = new EventHandler<ActionEvent>() {
        
      @Override
      public void handle(ActionEvent aE) { //need  modification to stop when playing
           Object source = aE.getSource(); //and vice versa
           if(source == sB) {
               System.out.println("go");
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
               //Guess this should really be a halt button, with a prompt
               System.out.println("stop");
               if (playing) {
                   if (!paused) gC.doPause();
                   Alert alert = new Alert(AlertType.CONFIRMATION);
                   alert.setTitle("End game?");
                   alert.setContentText("Click ok to confirm , or no to cancel");
                   Optional<ButtonType> result = alert.showAndWait();
                   if(result.get() == ButtonType.OK) {
                   //Platform.exit();
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
               else { //prompt here?
                   if (!paused) gC.doPause();
                   Alert alert = new Alert(AlertType.CONFIRMATION);
                   alert.setTitle("Close TetrisFX?");
                   alert.setContentText("Click ok to confirm and exit, or cancel");
                   Optional<ButtonType> result = alert.showAndWait();
                   if (result.get() == ButtonType.OK) {
                   Platform.exit();
                   }
                   else {
                       
                       return;
                   }
               }
           }
      }
   };
    
    
    private EventHandler<KeyEvent> kH = new EventHandler<KeyEvent>() {
       @Override 
       public void handle(KeyEvent e) {
         //keys to use: up, down, left, right; p?
           KeyCode code = e.getCode();
           //System.out.println(e.toString());
           //provision for hitting enter on play or quit button?
           if (playing) {
              if (code == KeyCode.P) { 
                  doPause();
            }
              else  {
                   if ( code == KeyCode.LEFT || code == KeyCode.RIGHT ) {
                   //System.out.println(code.toString());
                   gC.move(getOffset(code), 0);
                   }
                   else gC.move(0, getOffset(code));
           } //soudn if it doesn't work and stuff
           }           
        }
    };
    
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
