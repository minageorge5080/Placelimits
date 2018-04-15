package example.george.mina.placelimits;

/**
 * Created by minageorge on 4/15/18.
 */

public class MyPoints {
    private String lat;
    private String lon;

    public MyPoints(String lat, String lon) {
        setLat(lat);
        setLon(lon);
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
