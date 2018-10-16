import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.avro.AvroReadSupport;
import org.junit.Test;

import java.io.IOException;

import static javolution.testing.TestContext.assertNull;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ParquetTest {

    @Test
    public void ParquetAvroTest() throws IOException {
        Schema.Parser parser = new Schema.Parser();
        Schema projectionSchema = parser.parse(getClass()
                .getResourceAsStream("hadoop/Parquet/ProjectedStringPair.avsc"));
        Configuration conf = new Configuration();
        AvroReadSupport.setRequestedProjection(conf, projectionSchema);

        Path path = new Path("hadoop/Parquet/ProjectedStringPair.avsc");
        AvroParquetReader<GenericRecord> reader = new AvroParquetReader<GenericRecord>(conf, path);
        GenericRecord result = reader.read();
        assertNull(result.get("left"));
        assertThat(result.get("right").toString(), is("R"));
    }
}
