import packages.BracketFileBypass;
import packages.JsonToMapParser;
import java.io.IOException;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            var dir = "Ex1/src/resources/";
            var bracketMap = JsonToMapParser.parse(String.format("%s%s", dir, "config.json"));
            var result = BracketFileBypass.findWrongBracketPosition(
                    String.format("%s%s", dir, "text.txt"),
                    bracketMap);

            if (result == -1)
                System.out.print("Input text is correct");
            else
                System.out.print("Error! Incorrect symbol on position " + result);
        }
        catch (IOException e)
        {
            System.out.print(e.getMessage());
        }
    }
}