
package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Transport implements Serializable
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
    private final static long serialVersionUID = 6053351247278159943L;

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

    @Override
    public String toString() {
        return "Transport{" +
                "gpsId=" + gpsId +
                ", licensePlate='" + licensePlate + '\'' +
                ", driver=" + driver +
                '}';
    }
}
