
import hadoop.Tempreture.MaxTemperatureMapper;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Test;
import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.is;
import java.io.IOException;

public class MaxTemperatureMapperTest {

    @Test
    public void processesValidRecord() throws IOException{
        Text value = new Text("0029029070999991901010706004+64333+023450FM-12+0005" +
                "99999V0209991C000019999999N0000001N9-00331+99999103341ADDGF100991999999999999999999");
        new MapDriver<LongWritable, Text, Text, IntWritable>()
                .withMapper(new MaxTemperatureMapper())
                .withInput(new LongWritable(0), value)
                .withOutput(new Text("1901"), new IntWritable(-33))
                .runTest();
    }

    @Test
    public void ignoresMissingTemperatureRecord() throws IOException{
        Text value = new Text("0043011990999991950051518004+68750+023550FM-12+0382" +
                "99999V0203201N00261220001CN9999999N9+99991+99999999999");
        new MapDriver<LongWritable, Text, Text, IntWritable>()
                .withMapper(new MaxTemperatureMapper())
                .withInput(new LongWritable(0), value)
                .runTest();
    }

    @Test
    public void parsesMalformedTemperature() throws IOException{
        Text value = new Text("0335999999433181957042302005+37950+139117SAO  +0004" +
                                            // Year ^^^^
                "RJSN V02011359003150070356999999433201957010100005+353");
                                      // Temperature ^^^^^
        Counters counters = new Counters();
        new MapDriver<LongWritable, Text, Text, IntWritable>()
                .withMapper(new MaxTemperatureMapper())
                .withInput(new LongWritable(0), value)
                .withCounters(counters)
                .runTest();
        Counter counter = counters.findCounter(MaxTemperatureMapper.Temperature.MALFORMED);
        assertThat(counter.getValue(), is(1L));
    }
}
