

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class APIConnection {
    public static void main(String[] args) {
      //  "https://openholidaysapi.org/PublicHolidays?countryIsoCode=LT&languageIsoCode=LT&validFrom=2023-01-01&validTo=2023-12-31&subdivisionCode=LT"

        try {
            URL url = new URL("https://openholidaysapi.org/PublicHolidays?countryIsoCode=LT&languageIsoCode=LT&validFrom=2023-01-01&validTo=2023-12-31&subdivisionCode=LT");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();


            //checking if connection is OK
            int responseCode = connection.getResponseCode();
            if(responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            }else{  // streaming data from API, converting to string
                StringBuilder holidayInformationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    holidayInformationString.append(scanner.nextLine());
                }
                    scanner.close(); // closing scanner


                    //System.out.println(holidayInformationString); - data from API to String. Print out to check what we get
                    JSONParser parser = new JSONParser();
                    JSONArray dataObject = (JSONArray) parser.parse(String.valueOf(holidayInformationString));


                   System.out.println(dataObject.get(3));

                   JSONObject holidayName = (JSONObject) dataObject.get(2);
                   System.out.println(holidayName.get("text"));


            }
        } catch (Exception exception){
            exception.printStackTrace();

        }
    }
}
