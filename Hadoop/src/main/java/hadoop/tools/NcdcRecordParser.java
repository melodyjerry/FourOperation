package hadoop.tools;

import org.apache.hadoop.io.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NcdcRecordParser {
    private static final int MISSING_TEMPERATURE = 9999;


    private static final DateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyyMMddHHmm");

    private String stationId;
    private String observationDateString;
    private String year;
    private int airTemperature;
    private boolean airTemperatureMalformed;
    private String quality;

    public void parse(Text record){
        parse(record.toString());
    }

    public void parse(String record){
        stationId = record.substring(4, 10) + "-" + record.substring(10, 15);
        observationDateString = record.substring(15, 27);
        year = record.substring(15, 19);
        airTemperatureMalformed = false;
        if (record.charAt(87) == '+'){
            airTemperature = Integer.parseInt(record.substring(88, 92));
        }else if (record.charAt(87) == '-') {
            airTemperature = Integer.parseInt(record.substring(87, 92));
        }else{
            airTemperatureMalformed = true;
        }
        quality = record.substring(92, 93);
    }

    public boolean isValidTemperature() {
        return !airTemperatureMalformed && airTemperature != MISSING_TEMPERATURE
                && quality.matches("[01459]");
    }

    public boolean isMalformedTemperature() {
        return airTemperatureMalformed;
    }

    public boolean isMissingTemperature(){
        return airTemperature == MISSING_TEMPERATURE;
    }

    public String getYear(){
        return year;
    }

    public int getYearInt(){
        return Integer.parseInt(year);
    }

    public int getAirTemperature(){
        return airTemperature;
    }

    public String getStationId() {
        return stationId;
    }

    public Date getObservationDate() {
        try {
            System.out.println(observationDateString);
            return DATE_FORMAT.parse(observationDateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String getQuality(){
        return quality;
    }

}
