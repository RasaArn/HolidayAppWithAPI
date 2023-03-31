import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the month number: ");
        int monthNumber = scanner.nextInt();

        try {
            URL url = new URL("https://openholidaysapi.org/PublicHolidays?countryIsoCode=LT&languageIsoCode=LT&validFrom=2023-01-01&validTo=2023-12-31&subdivisionCode=LT");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            //checking if connection is OK
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {  // streaming data from API, converting to string
                StringBuilder holidayInformationString = new StringBuilder();
                scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    holidayInformationString.append(scanner.nextLine());
                }
                scanner.close(); // closing scanner

                //System.out.println(holidayInformationString); - data from API to String. Print out to check what we get
                JSONParser parser = new JSONParser();
                JSONArray dataObject = (JSONArray) parser.parse(String.valueOf(holidayInformationString));

                // Loop through the holiday information objects in the JSONArray
                for (int i = 0; i < dataObject.size(); i++) {
                    JSONObject holidayInfo = (JSONObject) dataObject.get(i);

                    int holidayMonth = Integer.parseInt(((String) holidayInfo.get("startDate")).split("-")[1]);
                    if (holidayMonth == monthNumber) {
                        String holidayName = (String) ((JSONObject) ((JSONArray) holidayInfo.get("name")).get(0)).get("text");
                        String holidayDate = (String) holidayInfo.get("startDate");
                        System.out.println("Holiday name: " + holidayName + ", Date: " + holidayDate);
                        break;
                    }else{
                        System.out.println("Error: Response code " + responseCode);
                    }
                }
            }
        } catch (Exception exception ) {
            exception.printStackTrace();
        }
    }
}
