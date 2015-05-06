/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetrisfx;

import java.util.Random;


/** Descending block taking one of several shapes: L, square, line with one in center,
 * straight line, 
 * @author Ruaridhi Bannatyne
 */
public class FallingBlock {
    //current central x and y coordinates
    private int cX;
    private int cY;
    //Type of shape: 0 L, 1 square, 2 line with extra in center, 3 straight line, 4
    private int state; //which shape is the block at present?
    private int[][] xOffs; //rotation x and y offsets
    private int[][] yOffs;
    //x-offsets and y-offsets which defien the position of each block relative to cX and cY
    private int[][] posn;
    private int[][] prev; //last position
    private int color;
        //color?
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
        //good to go from here. What about color? Here or in GameComponent?
     protected void move(int xMod, int yMod) {
         this.posn = getProspective(xMod, yMod);
         cX += xMod;
         if(yMod >=0) cY += yMod;
         else {
             System.out.println(state);
             if (state == 3 ) state = 0;
             else state++;
         }
     }
    
   protected int[][] getProspective(int xMod, int yMod) {
       if(yMod == -1) {
           System.out.println("flip operation");
           return prospectiveFlip();
       } 
       int[][] out = new int[4][2];
        for(int i = 0; i < out.length; i++)
        {
            out[i][0] = posn[i][0] + xMod;
            out[i][1] = posn[i][1] + yMod;
        }
        return out;
    } 
    
    //shift in position as a result of being flipped
   
    protected int[][] getPosn() { return posn;  }
    
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
    
    protected int getColor() { return this.color;}
  
    
    public static void main(String[] args) {
       
        
    }
   private static final int[][] lX = {{0, 1,0,-1}, {0,0,0,0}, 
                            {0, -1, 0, 1}, {1, -1, -1, 1}  };
   private static final int[][] lY = {{-1, 0,1,0}, {0,0,0,0},
                                     {1, 0, -1, 0}, {1, 1, -1, -1}  };
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
   
   private static final int[][] tX =  { {-1, 0, 0, 0}, {1,-1,-1,0 },
                                  {0, 0, 0, 0}, {0,0,1,1}};   
   private static final int[][] tY =  { {0, -1, -1, -1}, {0, 0, 0, 1 },
                                  {0, 0, 0, 0}, {1, 1, 0, 0}};  
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
   private static final int[][] zX = {{-1,1, -1,1}, {0, 1, 0, 1},
                               {0, 0, 0, 0}, {1, 0, 1, 0}};
   private static final int[][] zY = { {-1, -1, -1, -1}, {-1, 0, -1, 0},
                               {0, 0, 0, 0}, {0, 1, 0, 1}};
   private static final int[][] izX = {{1, -1, 1,-1}, {0, -1, 0, -1},
                               {0, 0, 0, 0}, {-1, 0, -1, 0}};
   private static final int[][] izY = { {-1, -1, -1, -1}, {-1, 0, -1, 0},
                               {0, 0, 0, 0}, {0, 1, 0, 1}};
} //ends FallingBlock