import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

public class DataTest {

    @Test
    public void testData(){
        String data = "0029029070999991901010106004+64333+023450FM-12+000599999V0202701N015919999999N0000001N9-00781+99999102001ADDGF108991999999999999999999";
        String stationId = data.substring(4, 10) + "-" + data.substring(10, 15);
//        System.out.println(stationId);
        assertThat(stationId, is("029070-99999"));
    }
}
