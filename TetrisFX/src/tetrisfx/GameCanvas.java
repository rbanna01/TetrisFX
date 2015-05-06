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
    /*
    *Height and width of game grid in squares.
    */
    protected int HEIGHT= 20;
    protected int WIDTH = 10;
    /*
    * Dimensions of image representing grid to be drawn.
    */
    private int height;
    private int width;
    /*
    * Player's current score (number of complete rows made.)
    */
    private int score;
    /*
    * The game grid.
    */
    private int[][] grid; 
    /*
    * The tetrimino currently descending
    */
    private FallingBlock fB; 
    /*
    * Used to draw the current state of the game.
    */
    private Canvas c;
    private GraphicsContext gC;
    /*
    * t is used to make fB descend
    * tt is the TimerTask t runs. 
    */
    private Timer t;
    private TT tt;
    
    /*
    * busy ensures non-concurrent modification of fB's position
    * Pause indicates whether the game is currently paused.
    */
    private boolean busy;
    private boolean pause;
    
    /*
    * Time between downward movements in ms
    */
    private int DEFAULT_INTERVAL = 750;
    /*
    * fB's starting coordinates
    */
    private final int STARTX = 6;
    private final int STARTY =  3;
    
    /*
    * Integers on grid which correspond to colors on the Canvas.
    */
    private final int RED = 1;
    private final int YELLOW = 2;
    private final int GREEN = 3;
    private final int BLUE = 4;
    private final int ORANGE = 5;
    private final int GREY = 6;
    //used to lock user or timer out when needed.
    private boolean override;
    /*
    * Length of a square's side in pixels. 
    * Depends on dimensions of Canvas.
    */
    private int sideLength;
    
    
    public GameCanvas(int widthIn, int heightIn) {
       score = 0;     
       this.height = heightIn;
       grid = new int[WIDTH][HEIGHT];
       sideLength = heightIn/20;  //Since grid is 20 squares high
       width = sideLength*10;
       c = new Canvas(width, height);
       gC = c.getGraphicsContext2D();       
       gC.setFill(Color.WHITE);
       gC.fillRect(0, 0, height, width);
       updateScore();  
    }
    /*
    * Begins a new game: clears board, resets score, and starts timers.
    */
    public void startGame() {
       for (int i=0; i < grid.length; i++)
       {
           for (int j = 0; j < grid[0].length; j++) 
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
    } 
    
    /*
    * Ends game: stops timers and resets all state variables.
    */
    public void endGame() {
        stopTimer(); //guess I'm leaving stuff on the board as-was
        aT.stop();
        pause = true;
        override = true;
        busy = true;
    }
    
    /*
    *Places fB at the statrting position: if it can't be placed there, game is over.
    */
    private void initBlock() {
        fB = new FallingBlock(STARTX, STARTY);
        if(!checkMove(0,0)) {
        endGame();
        }
    }
    
    /* Moves fB by the amounts input
    *@param xOff, yOff - the offsets to be applied to fB's coordinates.
    */
    
    public boolean move(int xOff, int yOff) {
        if (pause) return false;
        while (busy) {}
        override = true;
        busy = true;
        if (checkMove(xOff, yOff)) {
            fB.move(xOff, yOff);
            busy = false;
            override = false;
            return true;
        } else { 
            //System.out.println("checkMove false");
            if (yOff == 1) {
               
              //  System.out.println("down");
                //do stick, check for scoring or loss condition and start next tetrimino
                //loss condition: highest Y in offsets higher than STARTY
                int[][] posn = fB.getPosn();
                int clr = fB.getColor();
                ArrayList<Integer> ys = new ArrayList<>();
                boolean lost = false;
                for (int[] i: posn) {
                    grid[i[0]][i[1]] = clr; 
                    if (i[1] <= 0) {
                        lost = true;
                        }
                    if (!ys.contains(i[1])) ys.add(i[1]);
                }
                if (lost) {
                    endGame();
                } //ends
                for(int y: ys)
                { //   System.out.println(y);
                    int x = 0; //check whether row is fully occupied.
                    while(x < WIDTH)
                    {
                        if (grid[x][y] == 0) break;
                        x++;
                    }
                    if (x == WIDTH) {
                        score++;
                        doFall(y);
                    }
                }
                initBlock();
            }
            busy = false;
            override = false;
            return false;
        } //what if there's a sound effect or something?   
    }
    
    /*
    * For use when a row is removed. Contents of the row above are moved into
    * this row.
    */
    private void doFall(int y) {
       boolean recurse = false; //true only if there's something in this row
       for(int i = 0; i < WIDTH; i++) {
           grid[i][y] = grid[i][y-1];
       }
       for(int i = 0; i < WIDTH; i++) {
           if(grid[i][y-1] != 0) {
               recurse = true;
               break;
           }
       }
       if (recurse) { //Since there could be something above
           doFall(y-1);
       }
    }
       
    /*
    * Called by AnimationTimer; draws the grid, fB and the score.
    */
    private void drawGrid() {
    gC.clearRect(0,0, WIDTH, HEIGHT);
    gC.setFill(Color.WHITE);
    gC.fillRect(0, 0, WIDTH, HEIGHT);
    for (int i = 0; i < WIDTH; i++)
       {  
           for (int j = 0; j < HEIGHT; j++) {
               switch (grid[i][j]) {
                   case RED: drawSquare(i, j, Color.RED);
                            break;
                   case YELLOW: drawSquare(i, j, Color.YELLOW);
                            break;
                   case GREEN: drawSquare(i, j, Color.GREEN);
                            break;
                   case BLUE: drawSquare(i, j, Color.BLUE);
                            break;
                   case ORANGE: drawSquare(i, j, Color.ORANGE);
                            break;
                   case GREY: drawSquare(i, j, Color.GREY);
                            break;
                   default: clear(i, j);
                            break;
               } //switch
           } //ends inner for
       } //ends outer for
    updateScore();
    }
    
    /*
    * used to remove image of fB in previous positions.
    */
    private void clear(int x, int y) {
        gC.setFill(Color.WHITE);
        gC.fillRect(x*sideLength, y*sideLength, sideLength, sideLength);
    }
    
    /*
    *  Removes fB from the canvas. Done when moving.
    */
    private void clearFB() {
        for(int[] posn: fB.getPosn()) {
                clear(posn[0], posn[1]);
            }
    }
    /*
    * Draws fB at its present location.
    */    
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
    
    /*
    * Draw a square on the canvas at (xIn, Yin) using color c
    *@param - xIn x-coordinate on canvas
    @param - yIn y-coordinate on canvas
    @param c the color to be used in drawing th e square.
    */
    private void drawSquare( int xIn, int yIn, Color c) {
       int x = xIn*sideLength;
        int y = yIn* sideLength;
        gC.setStroke(Color.BLACK);
        gC.setFill(c);
        gC.strokeRect(x, y, sideLength-1, sideLength-1);
        gC.fillRect(x, y, sideLength, sideLength);
    }
    
    /*
    * Check whether adjusting position of fB to (x+ xOff, y + yOff) is legal.
    *@param - xIn x-coordinate on grid
    @param - yIn y-coordinate on grid
    */
    private boolean checkMove(int xOff, int yOff) {
       int[][] offsets;
       if (yOff == -1) { //indicates that the up cursor has been pressed: 
           offsets = fB.prospectiveFlip(); //fB is to be flipped.
           for(int[] vals: offsets) {
               if(vals[1] >= HEIGHT
                       || vals[0] >= WIDTH || vals[0] < 0
                       || vals[1] <0 || grid[vals[0]][vals[1]] != 0) return false;
           }
          return true;
       }
       else  { 
           offsets = fB.getProspective(xOff,yOff);
           for (int i = 0; i < offsets.length; i++) {
           int x  = offsets[i][0];
           int y =  offsets[i][1]; 
           //move illegal if: goes off the grid, or square occupied
           if (x < 0 ||  x >= WIDTH || y >= HEIGHT || y < 0||
                   grid[x][y] != 0) return false;
            }
           return true;
       } //ends else
    }
    
   /*
    * Pauses or unpauses game.
    */
    
    public void doPause() {
        //ensures user cannot restart game after its ended.
        if (!override) pause = !pause; 
        if(pause) {
            aT.stop();
        }
        else {
            aT.start();
        }
    }
    /*
    * Updates score to latest value on Canvas.
    */ 
    private void updateScore() {
       gC.setFill(Color.BLACK);
       gC.fillText(String.valueOf(score), 10, 10);    
    }
     
    /*
    * gets the Canvas used by this GameCanvas
    @output - this GameCanvas' Canvas.
    */
    public Canvas getCanvas() {
      return this.c;
    }

    /*
    * Moves fB down by one square.
    */
    private class TT extends TimerTask {
    @Override
    public void run() {
        if (!pause) move(0, 1); //TODO: remove
    }
    
    }

    /*
    * Draws state of grid and fB at time called.
    */
    private final AnimationTimer aT = new AnimationTimer() {
    
      @Override
      public void handle(long in) {
          drawGrid();
          clearFB();
          drawFB();
       }
    };
 
    /*
    * Initializes t.
    */
    private void initTimer() {
        t = new Timer(); //i.e. define a default rect
        tt = new TT();
        t.schedule(tt, 0, DEFAULT_INTERVAL);
        }
    
    /*
    * Stops t
    */
    private void stopTimer() {
        try {
            t.cancel();
        }
        catch (IllegalStateException e) {
         //t already cancelled; no action necessary.
        } 
    }

   /*
    //test methods and tests in main()
   public void setFB(FallingBlock fIn) {
       this.fB = fIn;
   }
   
   
    public void setGrid(int[][] newGrid) {
        this.grid = newGrid;
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
  */ 
} //ends GameCanvas
