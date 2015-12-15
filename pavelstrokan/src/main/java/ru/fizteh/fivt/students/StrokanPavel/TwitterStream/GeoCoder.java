package ru.fizteh.fivt.students.StrokanPavel.TwitterStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import twitter4j.GeoLocation;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.*;
import java.util.List;
/**
 * Created by pavel on 05.12.15.
 */
public class GeoCoder {
    public static String webSource() throws IOException, JSONException {
        URL newUrl = new URL("http://ip-api.com/json");
        URLConnection urlConnector = newUrl.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                urlConnector.getInputStream(), "UTF-8"));
        JSONObject givenSource = new JSONObject(in.readLine());
        in.close();
        return givenSource.getString("city");
    }
    public static GeoLocation getCoordinates(String place) {
        if ("nearby".equals(place)) {
            try {
                place = webSource();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            Geocoder geocoder = new Geocoder();
            GeocoderRequest geocoderRequest = new GeocoderRequestBuilder()
                    .setAddress(place).getGeocoderRequest();
            GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
            List<GeocoderResult> geocoderResult = geocoderResponse.getResults();
            if (geocoderResult.size() == 0) {
                String msg = "Google doesn't know where are you ;(";
                throw new IOException(msg);
            }
            double latitude = geocoderResult.get(0).getGeometry().getLocation().getLat().floatValue();
            double longitude = geocoderResult.get(0).getGeometry().getLocation().getLng().floatValue();
            return new GeoLocation(latitude, longitude);
        } catch (Exception ge) {
            System.err.println("Error in Geocoder: " + ge.getMessage());
        }
        return null;
    }
    public static double sqr(double number) {
        return number * number;
    }
    static final double EARTH_RADIUS = 6371;
    public static boolean near(GeoLocation first, GeoLocation second, double radius) {
        double firstLatitude = Math.toRadians(first.getLatitude());
        double firstLongitude = Math.toRadians(first.getLongitude());
        double secondLatitude = Math.toRadians(second.getLatitude());
        double secondLongitude = Math.toRadians(second.getLongitude());
        double deltaPhi = secondLatitude - firstLatitude;
        double deltaLambda = secondLongitude - firstLongitude;

        double distance = 2 * Math.asin(Math.sqrt(sqr(Math.sin(deltaPhi / 2))
                + Math.cos(firstLatitude) * Math.cos(secondLatitude) * sqr(Math.sin(deltaLambda / 2))))
                * EARTH_RADIUS;
        return distance < radius;
    }
}


