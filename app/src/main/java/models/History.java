package models;

import android.graphics.Bitmap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by sagar on 2/8/17.
 */

public class History {

    //String weekday;
    String date;
    String time;
    String vehicle_type;
    String vehicle_num;
    String source;
    String dest;
    ByteArrayOutputStream image;
    Driver driver;

    public History(String date, String time, String vehicle_type, String vehicle_num, String source, String dest,ByteArrayOutputStream image,Driver driver) {
        this.date = date;
        this.time = time;
        this.vehicle_type = vehicle_type;
        this.vehicle_num = vehicle_num;
        this.source = source;
        this.dest = dest;
        this.image=image;
        this.driver=driver;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getVehicle_num() {
        return vehicle_num;
    }

    public void setVehicle_num(String vehicle_num) {
        this.vehicle_num = vehicle_num;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDest() {
        return dest;
    }

    public ByteArrayOutputStream getImage() {
        return image;
    }

    public void setImage(ByteArrayOutputStream image) {
        this.image = image;
    }
    public void setDest(String dest) {
        this.dest = dest;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
