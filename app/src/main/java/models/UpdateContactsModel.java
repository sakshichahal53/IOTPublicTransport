package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sagar on 15/9/17.
 */

public class UpdateContactsModel {

    @SerializedName("emergency_contact")
    @Expose
    private String emergencyContact;

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
}
