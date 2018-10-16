package hadoop.avro;

import lancoo.ecbdc.pre.User;
import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.File;


public class test extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new test(), args);
        System.exit(exitCode);
    }

    @Override
    public int run(String[] args) throws Exception {
//        Parser  parser = new Parser();
//        Schema schema = parser.parse(getClass()
//                .getResourceAsStream("/root/IdeaProjects/Hadoop" +
//                        "/src/main/resources/avro/StringPair.avsc"));

        User user1 = new User();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);

        User user2 = new User("Ben", 7, "red");

        User user3 = User.newBuilder()
                .setName("Charlie")
                .setFavoriteColor("blue")
                .setFavoriteNumber(null)
                .build();


//        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<>(User.class);
//        DataFileWriter<User> dataFileWriter = new DataFileWriter<>(userDatumWriter);
//        dataFileWriter.create(user1.getSchema(), new File("/home/luengmingbiao/user.avro"));
//        dataFileWriter.append(user1);
//        dataFileWriter.append(user2);
//        dataFileWriter.append(user3);
//        dataFileWriter.close();

        DatumReader<GenericRecord> reader = new GenericDatumReader<>();
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(
                new File("/home/luengmingbiao/user.avro"), reader
        );

        System.out.println();
        return 1;
    }

}
