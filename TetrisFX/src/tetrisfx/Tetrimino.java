/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetrisfx;

import java.util.Random;
/**
 *
 * @author ruaridhi
 */
public enum Tetrimino {
  /* L (Vals.lX, Vals.lY),
   LINE(Vals.lineX, Vals.lineY),
   T(Vals.tX, Vals.tY),
   SQUARE(Vals.squareX, Vals.squareY);
    //needs the parallelothingy
   

    //shift in position as a result of being flipped. Does not actually flip thing
    protected int[][] prospectiveFlip()
    {
        int[][] out = new int[4][2];
        int tempState = ++state;
        if(tempState == 4) tempState = 0; //end of flip cycle
        for(int i =0; i < out.length; i++)
        {
          out[i][0] = cX + xVals[i][tempState];
          out[i][1] = cY + yVals[i][tempState];
        }
        return out;
    }
    
    public void doFlip() {
      this.offsets = prospectiveFlip();   
    }
    
    
    protected int[][] getOffsets() { return offsets;  }
    
    private static class Vals {
   public static final int[][] lX = {{0, 1,0,-1}, {0,0,0,0}, 
                            {0, -1, 0, 1}, {1, -1, -1, 1}  };
   public static final int[][] lY = {{-1, 0,1,0}, {0,0,0,0},
                                     {1, 0, -1, 0}, {1, 1, -1, -1}  };
   public static final int[][] lineX = { {0, -2, 0, -2}, {0,-1,0,-1 },
                                  {0, 0, 0, 0}, {0,1,0,1 }};
   public static final int[][] lineY = { {-2,0,-2,-0}, {-1,0,-1,0},
                               {0,0,0,0 }, {1,0,1,0 } };  
   public static final int[][] tX =  { {0, 1, 0, -1}, {0,0,0,0 },
                                  {1, 0, -1, 0}, {0,-1,0,1}};   
   public static final int[][] tY =  { {-1, 0, 1, 0}, {0,0,0,0 },
                                  {1, 0, -1, 0}, {0,-1,0,1}};  
  public static final int[][] squareX = { {-1, -1, -1, -1}, {0,0,0,0 },
                                  {-1, -1, -1, -1}, {0,0,0,0 }};
  public static final int[][] squareY = { {-1,-1,-1,-1}, {0,0,0,0},
                               {-1,-1,-1,-1 }, {0,0,0,0 } };    
    }
    */
}     

