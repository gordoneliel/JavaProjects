import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

class MazeSolver
{
    protected char array[][];
    protected boolean visited[][];
    protected int initialX, initialY, goalX, goalY;
    protected Random r;
    protected int indexPermute[][];

    protected int counter = 0;
    protected int step = 100;


    public MazeSolver()
    {
        r = new Random();
        indexPermute = new int[4][2];
    }



    boolean validLocation(int x, int y)
    {
        return ((x > 0) && (y > 0) &&
                (x < array.length- 1) &&
                (y < array.length - 1) &&
                (array[x][y] != 'X') && 
                !visited[x][y]);
    }

    void randomizeIndices()
    {
        for (int i = 0; i < 4; i++)
        {
            int swapindex = r.nextInt(4-i) + i;
            int[] tmp = indexPermute[swapindex];
            indexPermute[swapindex] = indexPermute[i];
            indexPermute[i] = tmp;
        }

    }
    void buildMaze(int x, int y)
    {
        visited[x][y] = true;
        randomizeIndices();
        for (int i = 0; i < 4; i++)
        {
            if (validLocation(x + 2 * indexPermute[i][0],y + 2 *indexPermute[i][1]))
            {
                array[x + indexPermute[i][0]][y + indexPermute[i][1]] = ' ';
                buildMaze(x + 2 * indexPermute[i][0], y + 2 * indexPermute[i][1]);
            }
        }

    }


    public void generateRandom(int size)
    {   
        array = new char[2*size + 1][2*size+1];
        for (int i = 0; i < 2*size+1; i++)
            for (int j = 0; j < 2 * size + 1; j++)
            {
                array[i][j] = 'X';
            }
        for (int i = 1; i < 2*size; i+=2)
        {
            for (int j = 1; j < 2*size; j += 2)
                array[i][j] = ' ';
        }

        visited = null;
        clearVisited();

        indexPermute[0][0] = -1;
        indexPermute[0][1] = 0;
        indexPermute[1][0] = +1;
        indexPermute[1][1] = 0;
        indexPermute[2][0] = 0;
        indexPermute[2][1] = -1;
        indexPermute[3][0] = 0;
        indexPermute[3][1] = +1;


        buildMaze(1 + 2 * (size/2),1 + 2 * (size / 2));
        initialX = 1;
        initialY = 1;
        goalX = 2*size - 1;
        goalY = 2*size - 1;
    }


    public void readMaze(String filename)
    {
        try
        {
            Scanner s = new Scanner(new File(filename));
            int size = s.nextInt();
            s.nextLine();
            array = new char[size][size];
            for (int i = 0; i < size; i++)
            {
                String line = s.nextLine();
                for (int j = 0; j < size; j++)
                {
                    if (line.charAt(j) == 'S')
                    {
                        initialX = i;
                        initialY = j;
                    }
                    else if (line.charAt(j) == 'E')
                    {
                        goalX = i;
                        goalY= j;
                    }
                    else
                    {
                        array[i][j] = line.charAt(j);
                    }
                }
            }
            for (int i = 0; i < array.length; i++)
            {
                for (int j = 0; j < array.length; j++)
                {
                    if (i == initialX && j == initialY)
                        System.out.print('S');
                    else if (i == goalX && j == goalY)
                        System.out.print('E');
                    else
                        System.out.print(array[i][j]);
                }
                System.out.println();
            }
        }
        catch (Exception e) 
        {
            System.out.print(e);
            array = new char[10][10];
            for (int i = 0; i < 10; i++)
                for (int j = 0; j < 10; j++)
                    array[i][j] = '.';
            initialX = 4;
            initialY = 4;
            goalX = 8;
            goalY = 8;
        }        
    }


    public String toString()
    {
        String result = "";
        result = Integer.toString(array.length) +  "\n";
        for (int i = 0; i < array.length; i++)
        {
            for (int j = 0; j < array.length; j++)
            {
                if (i == initialX && j == initialY)
                    result = result + "S";
                else if (i == goalX && j == goalY)
                    result = result + "E";
                else
                    result = result + array[i][j];
            }
            result = result + "\n";
        }
        return result;
        
    }
    
    void printMaze(PrintStream s)
    {
        for (int i = 0; i < array.length; i++)
        {
            for (int j = 0; j < array.length; j++)
            {
                if (i == initialX && j == initialY)
                    s.print('S');
                else if (i == goalX && j == goalY)
                    s.print('E');
                else
                    s.print(array[i][j]);
            }
            s.println();
        }
    }

    public void clearVisited()
    {
        if (visited == null)
        {
            visited = new boolean[array.length][array.length];
        }
        for (int i = 0; i < visited.length; i++)
            for (int j = 0; j < visited.length; j++)
                visited[i][j] = false;        
    }

    public boolean findPath(int startX, int startY, int endX, int endY)
    {        
    	boolean validate = false;
    	visited[startX][startY] = true;

        // Base Case
//    	if(validLocation(startX, startY))
//    	{
//    		return true;
//    	}
    	
    	if(startX == endX && startY == endY)
    	{
    		validate = true;
    		return validate;
    	}
    	
    	
    	if(validLocation(startX - 1,  startY) && !validate)
    	{
    		validate = findPath(startX - 1 , startY, endX, endY);
    	}
    	
    	if(validLocation(startX + 1,  startY) && !validate)
    	{
    		validate = findPath(startX + 1 , startY, endX, endY);
    	}
    	
    	if(validLocation(startX,  startY + 1)  && !validate)
    	{
    		validate = findPath(startX, startY + 1, endX, endY);
    	}
    	
    	if(validLocation(startX,  startY - 1)  && !validate)
    	{
    		validate = findPath(startX , startY - 1, endX, endY);
    	}
    	
    	//if any are true return true, if any are false return false
        // Fill me in!
        //    Do recursive backtracking search to solve maze
        //    starting from (startX, startY) and ending at (endX, endY)
        // 
        //    Return true if there is a path, and false otherwise
        //    Fill in path in maze with '*' symbols.
    	if(validate)
    	{
    		array[startX][startY] = '#';
    	}
    	
        return validate;
    }


    public boolean solve()
    {
        clearVisited();
        return findPath(initialX, initialY, goalX, goalY);
    }







    public static void main(String args[])
    {
        MazeSolver m = new MazeSolver();
        m.generateRandom(50);
        m.solve();
        System.out.println(m);
        try
        {
            PrintStream p = new  PrintStream(new FileOutputStream("Maze201"));
            p.println(m);
        }
        catch (Exception e)
        {
            
        }
       
    }
    
    


}