package hadoop.Tempreture;

import hadoop.tools.JobBuilder;
import hadoop.tools.NcdcRecordParser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 *  统计最高气温的作业,包括统计气温值缺失的记录,
 *  不规范的字段和质量代码
 */
public class MaxTemperatureWithCounters extends Configured implements Tool {

    enum Temperature{
        MISSING,
        MALFORMED
    }

    static class MaxTemperatureMapperWithCounters
            extends Mapper<LongWritable, Text, Text, IntWritable>{
        private NcdcRecordParser ncdcRecordParser = new NcdcRecordParser();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            ncdcRecordParser.parse(value);
            if (ncdcRecordParser.isValidTemperature()) {
                int airTemperature = ncdcRecordParser.getAirTemperature();
                context.write(new Text(ncdcRecordParser.getYear()), new IntWritable(airTemperature));
            }else if (ncdcRecordParser.isMalformedTemperature()){
                System.err.println("Ignoring possibly corrupt input: " + value);
                context.getCounter(Temperature.MALFORMED).increment(1);
            }else if (ncdcRecordParser.isMissingTemperature()){
                context.getCounter(Temperature.MISSING).increment(1);
            }

            context.getCounter("TemperatureQuality", ncdcRecordParser.getQuality()).increment(1);
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration configuration = getConf();
        configuration.set("mapreduce.framework.name", "yarn");
        configuration.set("mapred.jar", "/root/IdeaProjects/Hadoop/target/Hadoop-1.0-SNAPSHOT.jar");
        Job job = JobBuilder.parseInputAndOutput(this, configuration, args);
        if (job == null)
            return -1;

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(MaxTemperatureMapperWithCounters.class);
        job.setCombinerClass(MaxTemperatureReducer.class);
        job.setReducerClass(MaxTemperatureReducer.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception{
        int exitCode = ToolRunner.run(new MaxTemperatureWithCounters(), args);
        System.exit(exitCode);
    }
}
