/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetrisfx;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * Needs reworked: look up drawing with the scene node
 * using an AnimationTimer.....
 * @author ruaridhi
 */
public class GameCanvas {
    private int height;
    private int width;
    private int score;
    private int[][] grid; //game grid
    private FallingBlock fB; //all information about offsets, etc kept in Block
    private Canvas c;
    private GraphicsContext gC;
    private Timer t;
    private TT tt;
    private boolean busy;
    private boolean pause;
    //initial time between downward movements in ms
    private int DEFAULT_INTERVAL = 750;
    private char DOWN = 'd';
    private char UP = 'u';
    private char LEFT = 'l';
    private char RIGHT = 'r';
    protected int HEIGHT= 20;
    protected int WIDTH = 10;
    private final int STARTX = 6;
    private final int STARTY =  3;
    //numbers: red, yellow, green, blue, orange, grey
    private final int RED = 1;
    private final int YELLOW = 2;
    private final int GREEN = 3;
    private final int BLUE = 4;
    private final int ORANGE = 5;
    private final int GREY = 6;
    private boolean override;
    private Rectangle rect;
    //grid dimensions:10 by 20
    //what about saved games?
    private int sideLength;
    //needed height, width, score, FallingBlock, grid
    public GameCanvas(int heightIn, int widthIn) {
       score = 0;     
       this.height = heightIn;
       this.width = widthIn; 
       grid = new int[WIDTH][HEIGHT];
       sideLength = heightIn/20; //need to get square length right
       c = new Canvas(width, height);
       //initially white or black?
       gC = c.getGraphicsContext2D();       
       gC.setFill(Color.WHITE);
       //gC.setStrokeType(StrokeType.INSIDE); //needs to be applied to Shape.
       gC.fillRect(0, 0, height, width);
       updateScore();  
    }
    
    public void startGame() {
       for(int i=0; i < grid.length; i++)
       {
           for(int j = 0; j < grid[0].length; j++) 
           {
               grid[i][j] = 0;
           }
       }
        drawGrid();
        initBlock();
        initTimer();
        aT.start();
        pause = false;
        override = false;
        busy = false; //if doing tempo, will need to be reset here
    } //should perhaps have a stopGame as well? Add when doign hi-scores and stuff
    
    public void endGame() {
        stopTimer(); //guess I'm leaving stuff on the board as-was
        aT.stop();
        pause = true;
        override = true;
        busy = true;
    }
    
    private void initBlock() {
        fB = new FallingBlock(STARTX, STARTY);
        if(checkMove(0,0)) {
        //drawFB();
        }
        else {
            System.out.println("Game over man!");
        } //loss condition. To avoid bugginess, startY should be modified
    }
    
    
    public boolean move(int xOff, int yOff) {
        if (pause) return false;
        while(busy) {}
        override = true;
        busy = true;
        if (checkMove(xOff, yOff)) {
            //clearFB(); Should no longer be needed
            fB.move(xOff, yOff);
            //drawFB();
            busy = false;
            override = false;
            return true;
        } else { //no need to redraw
            System.out.println("checkMove false");
            if (yOff == 1) {
               // doPause();
                System.out.println("down");
                //do stick, check for scoring or loss condition and start next tetrimino
                //loss condition: highest Y in offsets higher than STARTY
                int[][] posn = fB.getPosn();
                int clr = fB.getColor();
                ArrayList<Integer> ys = new ArrayList<>();
                boolean lost = false;
                for (int[] i: posn) {
                    grid[i[0]][i[1]] = clr; //should this be after? 
                    if (i[1] <= 0) {
                        lost = true;
                        }
                    if(!ys.contains(i[1])) ys.add(i[1]);
                }
                if (lost) {
                    System.out.println("Game lost");
                    endGame();
                     //loss handling here
                } //ends
                //Might want to sort ys: bottom first?
                for(int y: ys)
                { //ys needs to be sorted
                    System.out.println(y);
                    int x = 0;
                    while(x < WIDTH)
                    {
                        System.out.println(x + " " + y);
                        if (grid[x][y] == 0) break;
                        x++;
                    }
                    //System.out.println(x);
                    if (x == WIDTH) {
                        //score handling and stuff
                        score++;
                        System.out.println("Score " + score);
                        doFall(y);
                    }
                }
                initBlock();
            }
            busy = false;
//            doPause();
            override = false;
            return false;
        } //what if there's a sound effect or something?   
    }
    private void doFall(int y) {
        System.out.println("doFall");
        boolean recurse = false;
       for(int i = 0; i < WIDTH; i++) {
           grid[i][y] = grid[i][y-1];
       }
       for(int i = 0; i < WIDTH; i++) {
           if(grid[i][y-1] != 0) {
               recurse = true;
               break;
           }
       }
       if (recurse) {
           doFall(y-1);
       }
    }
    
    
    
    
    private void drawGrid() {
    //System.out.println("drawGrid called");
    gC.clearRect(0,0, WIDTH, HEIGHT);
    gC.setFill(Color.WHITE);
    gC.fillRect(0, 0, WIDTH, HEIGHT);
    for(int i = 0; i < WIDTH; i++)
       { //numbers: red1, yellow2, green3, blue4, orange5, grey6
           for(int j = 0; j < HEIGHT; j++) {
               switch(grid[i][j]) {
                   case  1: drawSquare(i, j, Color.RED);
                            break;
                   case  2: drawSquare(i, j, Color.YELLOW);
                            break;
                   case  3: drawSquare(i, j, Color.GREEN);
                            break;
                   case  4: drawSquare(i, j, Color.BLUE);
                            break;
                   case  5: drawSquare(i, j, Color.ORANGE);
                            break;
                   case  6: drawSquare(i, j, Color.GREY);
                            break;
                   default: clear(i, j);
                            break;
               } //switch
           } //ends inner for
       } //ends outer for
    updateScore();
    }
    
    
    private void clear(int x, int y) {
        gC.setFill(Color.WHITE);
        //gC.setStroke(Color.WHITE);
       // gC.clearRect(x*sideLength, y*sideLength, sideLength, sideLength);
        gC.fillRect(x*sideLength, y*sideLength, sideLength, sideLength);
       // gC.strokeRect(x*sideLength, y*sideLength, sideLength, sideLength);
    }
    
    private void clearFB() {
        for(int[] posn: fB.getPosn()) {
                clear(posn[0], posn[1]);
            }
    }
        
    private void drawFB() {
         Color cIn; 
         switch(fB.getColor()) {
                   case  1:  cIn = Color.RED;
                            break;
                   case  2: cIn = Color.YELLOW;
                            break;
                   case  3: cIn = Color.GREEN;
                            break;
                   case  4: cIn = Color.BLUE;
                            break;
                   case  5: cIn = Color.ORANGE;
                            break;
                   default: cIn = Color.GREY;
                            break;
               }
        for(int[] p: fB.getPosn()) { drawSquare(p[0], p[1], cIn);}
    }
    
    private void drawSquare( int xIn, int yIn, Color c) {
        //gC.setStroke(Color.BLACK);
        int x = xIn*sideLength;
        int y = yIn* sideLength;
        gC.setFill(c);
        gC.fillRect(x, y, sideLength, sideLength);
    }
    
    //valid x-axis stuff failing
    private boolean checkMove(int xOff, int yOff) {
       int[][] offsets;
       if (yOff == -1) {
           //needs own handling 
           offsets = fB.prospectiveFlip();
           for(int[] vals: offsets) {
               if(vals[1] >= HEIGHT
                       || vals[0] >= WIDTH || vals[0] < 0
                       || vals[1] <0 || grid[vals[0]][vals[1]] != 0) return false;
           }
          return true;
       }//fB to vbe removed from grid; drawn separately
       else  { //problem here: need to ignore fB's own squares when checking
           offsets = fB.getProspective(xOff,yOff);
           for (int i = 0; i < offsets.length; i++) {
           int x  = offsets[i][0];
           int y =  offsets[i][1];
           if (x < 0 ||  x >= WIDTH || y >= HEIGHT || y < 0||
                   grid[x][y] != 0) return false;
            }
           return true;
       } //ends else
    }
    //for testing; should be removed later
    public void setGrid(int[][] newGrid) {
        this.grid = newGrid;
    }
      
    public void doPause() {
        if (!override) pause = !pause;
        if(pause) {
            aT.stop();
        }
        else {
            aT.start();
        }
    }
    
    private void updateScore() {
      // gC.setFill(Color.WHITE);
       //gC.fillRect(10, 10, 50, 10);
       gC.setFill(Color.BLACK);
       gC.fillText(String.valueOf(score), 10, 10);    
    }
     
    
    public Canvas getCanvas() {
      return this.c;
    }

private class TT extends TimerTask {
    @Override
    public void run() {
        if (!pause) move(0, 1); //TODO: remove
    }
    
}

private final AnimationTimer aT = new AnimationTimer() {
    
  @Override
  public void handle(long in) {
      System.out.println("handling");
      drawGrid();
      clearFB();
      drawFB();
  }
};
 
private void initTimer() {
    t = new Timer(); //i.e. define a default rect
    tt = new TT();
    t.schedule(tt, 0, DEFAULT_INTERVAL);
    }
private void stopTimer() {
    try {
        t.cancel();
    }
    catch (IllegalStateException e) {} //need to do anything here?
}

//needed? Should be done by gC itself...
   public void setFB(FallingBlock fIn) {
       this.fB = fIn;
   }

  public static void main(String[] args) {
      int[][] testGrid = new int[10][20];
      for (int i = 0; i < 10; i++) {
          for (int j = 0; j < 20; j++) {
              testGrid[i][j] = 0;
          }
      }
      testGrid[9][15] = 1;
      testGrid[9][16] = 1;
      testGrid[9][17] = 1;
      testGrid[9][19] = 1;
      testGrid[9][18] = 1;
      GameCanvas gC = new GameCanvas(100, 100);
      gC.setGrid(testGrid); //pick up from here
      FallingBlock test = new FallingBlock(7,15);
      gC.setFB(test);
      if (gC. move(1, 0)) {
          System.out.println("Moving into block to right accepted");
      }
      test.setX(8);
      test.setY(12);
      if (gC. move(1, 0)) {
          System.out.println("Moving off right accepted");
      }
      test.setX(1);
      test.setY(12);
       gC.setFB(test);
      if (gC. move(-1, 0)) {
          System.out.println("Moving off left accepted");
      }
      test.setX(3);
      test.setY(19);
      if (gC. move(0, 1)) {
          System.out.println("Moving off bottom accepted");
      }
      test.setX(8);
      test.setY(14);
      if (gC. move(0, 1)) {
          System.out.println("Moving into lower block accepted");
      }
  }
  
}
