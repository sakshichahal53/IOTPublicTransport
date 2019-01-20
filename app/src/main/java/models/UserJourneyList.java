package models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserJourneyList implements Serializable
{

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("transport")
    @Expose
    private List<List<Transport>> transport = null;
    @SerializedName("journey")
    @Expose
    private List<Journey> journey = null;
    private final static long serialVersionUID = -4462001644332020067L;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<List<Transport>> getTransport() {
        return transport;
    }

    public void setTransport(List<List<Transport>> transport) {
        this.transport = transport;
    }

    public List<Journey> getJourney() {
        return journey;
    }

    public void setJourney(List<Journey> journey) {
        this.journey = journey;
    }

    @Override
    public String toString() {
        return "UserJourneyList{" +
                "username='" + username + '\'' +
                ", transport=" + transport +
                ", journey=" + journey +
                '}';
    }
}