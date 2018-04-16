package example.george.mina.placelimits;

/**
 * Created by minageorge on 4/15/18.
 */

public class MyPoints {
    private double lat;
    private double lon;
    private double alti;

    public MyPoints(double lat, double lon ,double alti) {
        setLat(lat);
        setLon(lon);
        setAlti(alti);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getAlti() {
        return alti;
    }

    public void setAlti(double alti) {
        this.alti = alti;
    }
}
