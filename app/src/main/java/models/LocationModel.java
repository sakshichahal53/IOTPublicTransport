package models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationModel implements Serializable
{

    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("speed")
    @Expose
    private String speed;
    @SerializedName("loc_type")
    @Expose
    private String locType;
    @SerializedName("type_id")
    @Expose
    private String typeId;
    private final static long serialVersionUID = -9080877836218750667L;

    public LocationModel(String latitude, String longitude, String speed, String locType, String typeId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.locType = locType;
        this.typeId = typeId;
    }

    public LocationModel() {
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getLocType() {
        return locType;
    }

    public void setLocType(String locType) {
        this.locType = locType;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return "LocationModel{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", speed='" + speed + '\'' +
                ", locType='" + locType + '\'' +
                ", typeId=" + typeId +
                '}';
    }
}