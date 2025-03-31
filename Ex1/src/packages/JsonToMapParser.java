package packages;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public final class JsonToMapParser
{
    public static HashMap<Integer, Integer> parse(String pathToFile)
            throws IOException
    {
        //Json parse using com.googlecode.json-simple

        HashMap<Integer, Integer> bracketMap = new HashMap<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(pathToFile))
        {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray bracketArray = (JSONArray) jsonObject.get("bracket");

            for (Object bracket : bracketArray)
            {
                JSONObject bracketObject = (JSONObject) bracket;
                int leftBracket = bracketObject.get("left").toString().charAt(0);
                int rightBracket = bracketObject.get("right").toString().charAt(0);
                bracketMap.put(rightBracket, leftBracket);
            }

            return bracketMap;
        }
        catch (IOException | ParseException e)
        {
            throw new IOException("Exception from public static HashMap<Integer, Integer> parse(String pathToFile): An error occurred while parsing JSON to Map");
        }
    }
}
