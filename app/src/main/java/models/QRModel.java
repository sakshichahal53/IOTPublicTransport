package models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QRModel implements Serializable
{

    @SerializedName("gps_id")
    @Expose
    private int gpsId;
    @SerializedName("license_plate")
    @Expose
    private String licensePlate;
    @SerializedName("driver")
    @Expose
    private Driver driver;
    private final static long serialVersionUID = 730909875711130337L;

    public int getGpsId() {
        return gpsId;
    }

    public void setGpsId(int gpsId) {
        this.gpsId = gpsId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }


}