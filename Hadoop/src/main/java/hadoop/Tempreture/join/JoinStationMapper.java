package hadoop.Tempreture.join;

import hadoop.tools.NcdcRecordParser;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class JoinStationMapper extends Mapper<LongWritable, Text, TextPair, Text> {
    private NcdcRecordParser ncdcRecordParser = new NcdcRecordParser();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        ncdcRecordParser.parse(value);
        if (ncdcRecordParser.isValidTemperature()) {
            context.write(new TextPair(ncdcRecordParser.getStationId() , "0"),
                    new Text());
        }
    }
}
