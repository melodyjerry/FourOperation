package hadoop.Tempreture;

import hadoop.tools.NcdcRecordParser;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SequenceMaxTemperature extends Mapper<Text, Text, Text, IntWritable> {

    public enum Temperature{
        MALFORMED
    }

    private NcdcRecordParser ncdcRecordParser = new NcdcRecordParser();

    @Override
    public void map(Text key, Text value, Context context) throws IOException, InterruptedException {
        ncdcRecordParser.parse(value);
        if (ncdcRecordParser.isValidTemperature()) {
            int airTemperature = ncdcRecordParser.getAirTemperature();
//            if (airTemperature > 1000){
//                System.err.println("Temperature over 100 degrees for input: " + value);
//                context.setStatus("Detected possibly corrupt record: see logs.");
//                context.getCounter(Temperature.MALFORMED).increment(1);
//            }
            context.write(new Text(ncdcRecordParser.getYear()),
                    new IntWritable(airTemperature));
        }else if (ncdcRecordParser.isMalformedTemperature()){
            System.err.println("Ignoring possibly corrupt input: " + value);
            context.getCounter(MaxTemperatureMapper.Temperature.MALFORMED).increment(1);
        }
    }
}
