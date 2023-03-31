import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        System.out.println("please enter the number of the month for which you would like to check holiday: ");


            try {

                Scanner scanner = new Scanner(System.in);
                int monthNumber = scanner.nextInt();


                URL url = new URL("https://openholidaysapi.org/PublicHolidays?countryIsoCode=LT&languageIsoCode=LT&validFrom=2023-01-01&validTo=2023-12-31&subdivisionCode=LT");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();


                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    throw new RuntimeException("HttpResponseCode: " + responseCode);
                } else {  // streaming data from API, converting to string
                    StringBuilder holidayInformationString = new StringBuilder();
                    scanner = new Scanner(url.openStream());

                    while (scanner.hasNext()) {
                        holidayInformationString.append(scanner.nextLine());
                    }


                    //System.out.println(holidayInformationString); - data from API array to String. Print out to check what we get
                    try{
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
                            }
                        }
                    } catch (ParseException e){
                        e.printStackTrace();
                    }



                }
            } catch ( Exception exception ) {
                exception.printStackTrace();
            }
    }
}

