package hadoop.FileOperator.sort;

import hadoop.tools.JobBuilder;
import hadoop.tools.NcdcRecordParser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 *  该MapReduce程序将天气数据转成SequenceFile格式
 */
public class SortDataPreprocessor extends Configured implements Tool {

    static class CleanerMapper extends Mapper<LongWritable, Text, IntWritable, Text> {
        private NcdcRecordParser ncdcRecordParser = new NcdcRecordParser();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            ncdcRecordParser.parse(value);
            if (ncdcRecordParser.isValidTemperature()) {
                context.write(new IntWritable(ncdcRecordParser.getAirTemperature()), value);
            }
        }
    }

    @Override
    public int run(String[] args) throws Exception {
        Configuration config = getConf();
        config.set("mapreduce.framework.name", "yarn");
        config.set("mapred.jar", "/root/IdeaProjects/Hadoop/target/Hadoop-1.0-SNAPSHOT.jar");
        Job job = JobBuilder.parseInputAndOutput(this, config, args);
        if (job == null){
            return -1;
        }

        job.setMapperClass(CleanerMapper.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);
        job.setNumReduceTasks(0);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setCompressOutput(job, true);
        SequenceFileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);
        SequenceFileOutputFormat.setOutputCompressionType(job, CompressionType.BLOCK);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args)throws Exception {
        int exitCode = ToolRunner.run(new SortDataPreprocessor(), args);
        System.exit(exitCode);
    }
}
