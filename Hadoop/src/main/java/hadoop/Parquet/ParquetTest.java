package hadoop.Parquet;

import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;
import org.apache.avro.Schema;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.avro.AvroReadSupport;

import static javolution.testing.TestContext.assertNull;


public class ParquetTest extends Configured implements Tool {


    @Override
    public int run(String[] args) throws Exception {

        return 0;
    }
}
