package hadoop.avro;

import hadoop.tools.JobBuilder;
import hadoop.tools.NcdcRecordParser;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroValue;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.avro.Schema;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 *  该MapReduce找出最高气温, 输出的是Avro文件
 */
public class AvroGenericMaxTemperature extends Configured implements Tool {

    private static final Schema SCHEMA = new Schema.Parser().parse(
            "{" +
                    "  \"type\": \"record\"," +
                    "  \"name\": \"WeatherRecord\"," +
                    "  \"doc\": \"A weather reading.\"," +
                    "  \"fields\": [" +
                    "    {\"name\": \"year\", \"type\": \"int\"}," +
                    "    {\"name\": \"temperature\", \"type\": \"int\"}," +
                    "    {\"name\": \"stationId\", \"type\": \"string\"}" +
                    "  ]" +
                    "}"
    );

    public static class MaxTemperatureMapper
            extends Mapper<LongWritable, Text, AvroKey<Integer>,
            AvroValue<GenericRecord>> {
        private NcdcRecordParser parser = new NcdcRecordParser();
        private GenericRecord record = new GenericData.Record(SCHEMA);

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            parser.parse(value.toString());
            if (parser.isValidTemperature()) {
                record.put("year", parser.getYearInt());
                record.put("temperature", parser.getAirTemperature());
                record.put("stationId", parser.getStationId());
                context.write(new AvroKey<Integer>(parser.getYearInt()),
                        new AvroValue<GenericRecord>(record));
            }
        }
    }

    public static class MaxTemperatureReducer
            extends Reducer<AvroKey<Integer>, AvroValue<GenericRecord>,
                  AvroKey<GenericRecord>, NullWritable> {
        @Override
        protected void reduce(AvroKey<Integer> key, Iterable<AvroValue<GenericRecord>> values, Context context) throws IOException, InterruptedException {
            GenericRecord max = null;
            for (AvroValue<GenericRecord> value : values) {
                GenericRecord record = value.datum();
                if (max == null ||
                        (Integer) record.get("temperature") > (Integer) max.get("temperature")) {
                    max = newWeatherRecord(record);
                }
            }
        }

        private GenericRecord newWeatherRecord(GenericRecord value) {
            GenericRecord record = new GenericData.Record(SCHEMA);
            record.put("year", value.get("year"));
            record.put("temperature", value.get("temperature"));
            record.put("stationId", value.get("stationId"));
            return record;
        }
    }


    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.printf("Usage: %s [generic options] <input> <output>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        Configuration config = getConf();
        config.set("mapreduce.framework.name", "yarn");
        config.set("mapred.jar", "/root/IdeaProjects/Hadoop/target/Hadoop-1.0-SNAPSHOT.jar");
        Job job = JobBuilder.parseInputAndOutput(this, config, args);
        if (job == null){
            return -1;
        }

        job.setJobName("Max temperature");
        job.setJarByClass(getClass());

        job.getConfiguration().setBoolean(
                Job.MAPREDUCE_JOB_USER_CLASSPATH_FIRST, true);

        AvroJob.setMapOutputKeySchema(job, Schema.create(Schema.Type.INT));
        AvroJob.setMapOutputValueSchema(job, SCHEMA);
        AvroJob.setOutputKeySchema(job, SCHEMA);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(AvroKeyOutputFormat.class);

        job.setMapperClass(MaxTemperatureMapper.class);
        job.setReducerClass(MaxTemperatureReducer.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new AvroGenericMaxTemperature(), args);
        System.exit(exitCode);
    }
}
