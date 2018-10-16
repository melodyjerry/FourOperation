package hadoop.FileOperator.sort;

import hadoop.tools.JobBuilder;
import hadoop.tools.NcdcRecordParser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class MaxTemperatureUsingSecondarySort extends Configured implements Tool {

    static class MaxTemperatureMapper extends Mapper<LongWritable, Text, IntPair, NullWritable> {
        private NcdcRecordParser ncdcRecordParser = new NcdcRecordParser();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            ncdcRecordParser.parse(value);
            if (ncdcRecordParser.isValidTemperature()) {
                context.write(new IntPair(ncdcRecordParser.getYearInt(),
                        ncdcRecordParser.getAirTemperature()), NullWritable.get());
            }
        }
    }

    static class MaxTemperatureReducer extends Reducer<IntPair, NullWritable, IntPair, NullWritable> {
        @Override
        protected void reduce(IntPair key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            context.write(key, NullWritable.get());
        }
    }

    public static class FirstPartitioner extends Partitioner<IntPair, NullWritable> {

        @Override
        public int getPartition(IntPair intPair, NullWritable nullWritable, int numPartitions) {
            // multiply by 127 to perform some mixing
            return Math.abs(intPair.getFirst() * 127) % numPartitions;
        }
    }

    public static class KeyComparator extends WritableComparator {

        protected KeyComparator() {
            super(IntPair.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            IntPair ip1 = (IntPair) a;
            IntPair ip2 = (IntPair) b;
            int cmp = IntPair.compare(ip1.getFirst(), ip2.getFirst());
            if (cmp != 0) {
                return cmp;
            }
            return -IntPair.compare(ip1.getSecond(), ip2.getSecond());
        }
    }

    public static class GroupComparator extends WritableComparator {
        protected GroupComparator() {
            super(IntPair.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            IntPair ip1 = (IntPair) a;
            IntPair ip2 = (IntPair) b;
            return IntPair.compare(ip1.getFirst(), ip2.getSecond());
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

        job.setMapperClass(MaxTemperatureMapper.class);
        job.setPartitionerClass(FirstPartitioner.class);
        job.setSortComparatorClass(KeyComparator.class);
        job.setGroupingComparatorClass(GroupComparator.class);
        job.setReducerClass(MaxTemperatureReducer.class);
        job.setOutputKeyClass(IntPair.class);
        job.setOutputValueClass(NullWritable.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args)throws Exception {
        int exitCode = ToolRunner.run(new MaxTemperatureUsingSecondarySort(), args);
        System.exit(exitCode);
    }
}
