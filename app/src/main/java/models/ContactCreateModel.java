package models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContactCreateModel implements Serializable
{

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("emergency_contact")
    @Expose
    private String emergencyContact;
    private final static long serialVersionUID = -2914198308949785181L;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

}