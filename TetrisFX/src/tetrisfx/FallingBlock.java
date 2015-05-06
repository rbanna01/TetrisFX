
package tetrisfx;

import java.util.Random;


/** Descending block taking one of several shapes: L, square, stunted t,
 * straight line, 
 * @author Ruaridhi Bannatyne
 */
public class FallingBlock {
    
    /*
    * Central x and y co-ordinates.
    */
    private int cX;
    private int cY;
    
    /*
    * Current configuration
    */
    private int state; 
    /*
    * x-offsets of each block in each configuration
    */
    private int[][] xOffs; //rotation x and y offsets
    /*
    * y-offsets of each block in each configuration
    */
    private int[][] yOffs;
    
    /*
    * Position of each block in GameCanvas' grid.
    */
    private int[][] posn;
    /*
    * This block's color.
    */
    private int color;
        //color?
    
    /*
    * Constructor: creates a new FB with central x and y coordinates
    startX and startY 
    @param startX - cX to be used.
    @param startY - cY to be used.
    */
    
    public FallingBlock(int startX, int startY) {
        this.cX = startX;
        this.cY = startY;
        Random r = new Random(); //needed for type, state and color
        switch(r.nextInt(6)) { //should be r.nextInt(4); 0 L, 1 Square, 2 T, 3 Line
            case 0: xOffs = lX;
                    yOffs = lY;
                    break;
            case 1: xOffs = squareX;
                    yOffs = squareY;
                    break;
            case 2: xOffs = tX;
                    yOffs = tY;
                    break;
            case 3: xOffs = zX;
                    yOffs = zY;
                    break;
            case 4: xOffs = izX;
                    yOffs = izY;
                    break;
            default: xOffs = lineX;
                     yOffs = lineY;
                     break;
        } //testing purposes: horizontal line
        state = r.nextInt(4);
        posn = new int[4][2];
        color = r.nextInt(5)+1;
        //initial offsets as taken from type and state
        
        for(int i = 0; i < posn.length; i++)
        {
            posn[i][0] = xOffs[i][state] +startX;
            posn[i][1] = yOffs[i][state] + startY;
        }
        }
    
    /*
    *Moves this FB by the x and y values provided.
    NB yMod of -1 indicates a flip operation,
    @param xMod - offset to add to cX
    @param yMod - offset to add to cY
    */
    protected void move(int xMod, int yMod) {
         this.posn = getProspective(xMod, yMod);
         cX += xMod;
         if (yMod >=0) cY += yMod;
         else {
             System.out.println(state);
             if (state == 3 ) state = 0;
             else state++;
         }
     }
    
    /*
    *  Gets position of this object after the input modifications.
    Used for checking legality of moves
    @param xMod - offset to add to cX
    @param yMod - offset to add to cY
    @return a 2d integer array containing the FB's coordinates after proposed move.
    */
    protected int[][] getProspective(int xMod, int yMod) {
       if (yMod == -1) { //i.e. flip operation
           return prospectiveFlip();
       } 
       int[][] out = new int[4][2];
        for (int i = 0; i < out.length; i++)
        {
            out[i][0] = posn[i][0] + xMod;
            out[i][1] = posn[i][1] + yMod;
        }
        return out;
    } 
    
    
    /* The FB's current position in grid coordinates.
    * @param a 2d integer array containing the FB's coordinates.
    */
    protected int[][] getPosn() { return posn;  }
    
    /* The FB's position after a flip operation.
    * @param a 2d integer array containing the FB's coordinates after a flip.
    */
    protected int[][] prospectiveFlip()
    {
        int[][] out = new int[4][2];
        int tempState = state+1;
        if (tempState > 3) tempState = 0; //end of flip cycle
        for (int i =0; i < out.length; i++)
        {
          out[i][0] = cX + xOffs[i][tempState];
          out[i][1] = cY + yOffs[i][tempState];
        }
        return out;
    }
    
    /*
    * Returns this FB's color
    @return this FB's color.
    */
    protected int getColor() { return this.color;}
  
   /*
    * X and y-offsets for each configuration of an L-shaped block
    */ 
   private static final int[][] lX = {{0, 1,0,-1}, {0,0,0,0}, 
                            {0, -1, 0, 1}, {1, -1, -1, 1}  };
   private static final int[][] lY = {{-1, 0,1,0}, {0,0,0,0},
                                     {1, 0, -1, 0}, {1, 1, -1, -1}  };
   /*
    * X and y-offsets for each configuration of a line-shaped block.
    */ 
   private static final int[][] lineX = { {-2, 0, -2, 0}, {-1,0,-1, 0 },
                                  {0, 0, 0, 0}, {1,0, 1, 0 }};
   private static final int[][] lineY = { {0, -2, 0, -2}, {0,-1, 0,-1},
                               {0,0,0,0 }, {0, 1, 0, 1} };  
   /*
    1 3 2
      4
   
      1
     23
      4
      
      1
     234
      
      1
      34
      2
   */
   /*
    * X and y-offsets for each configuration of a T-shaped block.
    */ 
   private static final int[][] tX =  { {-1, 0, 0, 0}, {1,-1,-1,0 },
                                  {0, 0, 0, 0}, {0,0,1,1}};   
   private static final int[][] tY =  { {0, -1, -1, -1}, {0, 0, 0, 1 },
                                  {0, 0, 0, 0}, {1, 1, 0, 0}};  
   /*
    * X and y-offsets for each configuration of a square block.
    */ 
   private static final int[][] squareX = { {-1, -1, -1, -1}, {0,0,0,0 },
                                  {-1, -1, -1, -1}, {0,0,0,0 }};
   private static final int[][] squareY = { {-1,-1,-1,-1}, {-1, -1, -1, -1},
                               {0, 0, 0, 0 }, {0,0,0,0 } };  
    //todo: z-shape
   /* z:
      1 2
        3 4
         1
        32
        4
   Inverted z:
        21
       43
       1
       23
        4
   */
   /*
    * X and y-offsets for each configuration of a Z-shaped block.
    */ 
   private static final int[][] zX = {{-1,1, -1,1}, {0, 1, 0, 1},
                               {0, 0, 0, 0}, {1, 0, 1, 0}};
   private static final int[][] zY = { {-1, -1, -1, -1}, {-1, 0, -1, 0},
                               {0, 0, 0, 0}, {0, 1, 0, 1}};
   /*
    * X and y-offsets for each configuration of a reversed Z-shaped block
    */ 
   private static final int[][] izX = {{1, -1, 1,-1}, {0, -1, 0, -1},
                               {0, 0, 0, 0}, {-1, 0, -1, 0}};
   private static final int[][] izY = { {-1, -1, -1, -1}, {-1, 0, -1, 0},
                               {0, 0, 0, 0}, {0, 1, 0, 1}};
    /*
    //testing method
    public void setX(int newX) {
        cX = newX;
        for(int i = 0; i < posn.length; i++) {
            posn[i][0] = cX + xOffs[i][state];
        }
       
    
    }
    //also tecting
    public void setY(int newY) {
        cY = newY;
       for (int i = 0; i < posn.length; i++) {
           posn[i][1] = yOffs[i][state] + cY;
       }
    }
    
    public int getX() { return cX;}
    public int getY() {return cY;}
    
   
    */
} //ends FallingBlock