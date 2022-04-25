/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dvrakas
 */
public class State 
{
    int [] getGoal()
    {
        int res[] = new int [2];
        for (int x=0;x<rows;x++)
            for (int y=0;y<cols;y++)
                if (layout[x][y]=='g')
                {
                    res[0]=x;
                    res[1]=y;
                    return res;
                }        
        return res;
    }
    
    public static int rows,cols;

    public static int getRows() {
        return rows;
    }

    public static void setRows(int rows) {
        State.rows = rows;
    }

    public static int getCols() {
        return cols;
    }

    public static void setCols(int cols) {
        State.cols = cols;
    }

   
    private char layout[][];
    public void set(int x, int y, char k)
    {
        layout[x][y]=k;
    }
    char get(int x, int y){
        return layout[x][y];
    }    
    boolean isEmpty(int x, int y){
        return layout[x][y]=='x';
    }
    public int getRobotX(){
        for (int x=0;x<rows;x++)
            for (int y=0;y<cols;y++)
                if (layout[x][y]=='r')
                    return x;
        return 0;
    } 
    public int getRobotY(){
        for (int x=0;x<rows;x++)
            for (int y=0;y<cols;y++)
                if (layout[x][y]=='r')
                    return y;
        return 0;
    } 
    
    State(int rows, int cols){
        layout = new char [rows][cols]; 
        this.rows = rows;
        this.cols= cols;
    }
    
    
}
