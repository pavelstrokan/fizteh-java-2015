package TwitterTests;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static ru.fizteh.fivt.students.StrokanPavel.TwitterStream.GeoCoder.*;
import twitter4j.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavel on 06.12.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class GeoCoderTest extends TestCase {

    @Test
    public void getCoordinatesTest() throws InterruptedException, JSONException, IOException {
        List<String> cities = new ArrayList<>();
        cities.add("Краснодар");
        cities.add("Сочи");
        cities.add("Barcelona");
        cities.add("Deerfield_Beach");
        List<Double[]> coordinates = new ArrayList<>();
        coordinates.add(new Double[]{45.039268493652344, 38.987220764160156});
        coordinates.add(new Double[]{43.602806091308594, 39.734153747558594});
        coordinates.add(new Double[]{41.38506317138672, 2.17340350151062});
        coordinates.add(new Double[]{26.31841278076172, -80.09976196289062});
        List<GeoLocation> locations = new ArrayList<>();
        locations.add(new GeoLocation(coordinates.get(0)[0], coordinates.get(0)[1]));
        locations.add(new GeoLocation(coordinates.get(1)[0], coordinates.get(1)[1]));
        locations.add(new GeoLocation(coordinates.get(2)[0], coordinates.get(2)[1]));
        locations.add(new GeoLocation(coordinates.get(3)[0], coordinates.get(3)[1]));
        assertEquals(locations.get(0), getCoordinates(cities.get(0)));
        assertEquals(locations.get(1), getCoordinates(cities.get(1)));
        assertEquals(locations.get(2), getCoordinates(cities.get(2)));
        assertEquals(locations.get(3), getCoordinates(cities.get(3)));
    }

    @Test
    public void sqrTest() {
        List<Double> dataProvided = new ArrayList<>();
        for(int i = 0; i < 10000D; ++i) {
            dataProvided.add((double) i * i);
            assertEquals(dataProvided.get(i), sqr(i));
        }

    }

    @Test
    public void nearTest() {
        List<String> cities = new ArrayList<>();
        cities.add("Краснодар");
        cities.add("Сочи");
        cities.add("Долгопрудный");
        cities.add("Химки");
        cities.add("Москва");
        List<Double[]> coordinates = new ArrayList<>();
        coordinates.add(new Double[]{45.03926740000001, 38.987221});
        coordinates.add(new Double[]{43.60280789999999, 39.7341543});
        coordinates.add(new Double[]{55.947064, 37.4992755});
        coordinates.add(new Double[]{55.8940553, 37.4439487});
        coordinates.add(new Double[]{55.755826, 37.6173});

        List<GeoLocation> locations = new ArrayList<>();
        locations.add(new GeoLocation(coordinates.get(0)[0], coordinates.get(0)[1]));
        locations.add(new GeoLocation(coordinates.get(1)[0], coordinates.get(1)[1]));
        locations.add(new GeoLocation(coordinates.get(2)[0], coordinates.get(2)[1]));
        locations.add(new GeoLocation(coordinates.get(3)[0], coordinates.get(3)[1]));
        locations.add(new GeoLocation(coordinates.get(4)[0], coordinates.get(4)[1]));

        assertEquals(false, near(locations.get(0), locations.get(1), 170));
        assertEquals(true, near(locations.get(0), locations.get(1), 171));
        assertEquals(true, near(locations.get(2), locations.get(3), 10));
    }
}
