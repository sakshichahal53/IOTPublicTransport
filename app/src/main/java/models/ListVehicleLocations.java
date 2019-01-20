package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sagar on 7/9/17.
 */

public class ListVehicleLocations implements Serializable {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("location")
    @Expose
    private List<VehicleLocationModel> location = null;
    private final static long serialVersionUID = -1699540220770273402L;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<VehicleLocationModel> getLocation() {
        return location;
    }

    public void setLocation(List<VehicleLocationModel> location) {
        this.location = location;
    }
}