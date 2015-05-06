/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetrisfx;

/**
 *
 * @author ruaridhi
 */
public class Vals {
    
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
  
    public class BlockShape {  
        // abstract class. Required info for each shape: sequence of offsets from center
        //which represent shape's various configurations
        //stored in form : { {brick one's posn in each config} {brick two's posn in each config}
        //{brick three} {brick four}        } 
       //current configuration of a given block
        protected int[][] xOff;
        protected int[][] yOff;
        protected int[][] getXOff() { return xOff;}
        protected int[][] getYOff() { return yOff;} 
    } //ends shape
      
    /*
      b1
      b2
      b3b4
     */
    private class L extends BlockShape {
        protected int[][] xOff = {{0, 1,0,-1}, {0,0,0,0}, {0, -1, 0, 1}, {1, -1, -1, 1}  };
        protected int[][] yOff = {{-1, 0,1,0}, {0,0,0,0}, {1, 0, -1, 0}, {1, 1, -1, -1}  };
           
        public L() {
            super();
        }
    } //ends L
    
    /*
        b1b2
        b3b4
    */
    private class Square extends BlockShape {
        private final int[][] SxOff = { {-1, -1, -1, -1}, {0,0,0,0 },
                                  {-1, -1, -1, -1}, {0,0,0,0 }};
        private final int[][] SyOff = { {-1,-1,-1,-1}, {0,0,0,0},
                               {-1,-1,-1,-1 }, {0,0,0,0 } };    
        public Square() {
            super();
            
        }
    } //ends Square
    // default: up and down, other side to side
    /*
      b1
      b2
      b3
      b4
    */
    private class Line extends BlockShape{
        private final int[][] xOff = { {0, -2, 0, -2}, {0,-1,0,-1 },
                                  {0, 0, 0, 0}, {0,1,0,1 }};
        private final int[][] yOff = { {-2,0,-2,-0}, {-1,0,-1,0},
                               {0,0,0,0 }, {1,0,1,0 } };  
        public Line() {
            super();
        }
    } //ends Line
    
    /*
      b1
      b2b3
      b4
    */
    private class T extends BlockShape{
       private final int[][] xOff =  { {0, 1, 0, -1}, {0,0,0,0 },
                                  {1, 0, -1, 0}, {0,-1,0,1}};   
       private final int[][] yOff =  { {-1, 0, 1, 0}, {0,0,0,0 },
                                  {1, 0, -1, 0}, {0,-1,0,1}};  
       public T() {
            super();
        }
    } //ends T
  
     //shift in position as a result of being flipped. Does not actually flip thing
   
  
    }

