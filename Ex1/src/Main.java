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
            var bracketMap = JsonToMapParser.parse(String.format("%sconfig.json", dir));
            var result = BracketFileBypass.findWrongBracketPosition(
                    String.format("%stext.txt", dir),
                    bracketMap);

            if (result == -1)
                System.out.print("Input text is correct");
            else
                System.out.printf("Error! Incorrect symbol on position %s", result);
        }
        catch (IOException e)
        {
            System.out.print(e.getMessage());
        }
    }
}