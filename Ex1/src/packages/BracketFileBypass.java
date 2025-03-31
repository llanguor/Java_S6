package packages;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;
import java.util.logging.Logger;

public final class BracketFileBypass
{
    public static int findWrongBracketPosition(String pathToFile, HashMap<Integer, Integer> bracketMap)
            throws IOException, IllegalArgumentException
    {
        if(bracketMap==null || bracketMap.isEmpty())
            throw new IllegalArgumentException("Incorrect brackets map");

        Stack<Integer> bracketStack = new Stack<>();

        try(FileReader fr = new FileReader(pathToFile))
        {
            int character, pos=0;
            while((character = fr.read()) != -1)
            {
                ++pos;

                //If bracket is in the Value-list (left-side bracket), then push it to stack
                //If bracket is in the Key-list (right-side bracket), then pop value from stack
                //      If popped value is left-side version from current bracket - it's ok
                //      If not - error. Return current position on text
                //      If stack is empty - error. Return current position on text

                if(bracketMap.containsValue(character))
                {
                    bracketStack.push(character);
                }
                else if(bracketMap.containsKey(character))
                {
                    if (bracketStack.isEmpty()) return pos;
                    int popValue = bracketStack.pop();
                    if(popValue != bracketMap.get(character)) return pos;
                }
            }
        }
        catch (IOException e)
        {
            throw new IOException("Exception from public static int findWrongBracketPosition(String pathToFile, HashMap<Integer, Integer> bracketMap): An error occurred while parsing JSON to Map\"");
        }

        return -1;
    }
}
