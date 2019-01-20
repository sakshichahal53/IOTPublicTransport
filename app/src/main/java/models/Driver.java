
package models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Driver implements Serializable
{

    @SerializedName("dname")
    @Expose
    private String dname;
    @SerializedName("dcontact")
    @Expose
    private String dcontact;
    @SerializedName("daddress")
    @Expose
    private String daddress;
    @SerializedName("dlicense")
    @Expose
    private String dlicense;
    @SerializedName("daadhar")
    @Expose
    private String daadhar;
    @SerializedName("dphoto")
    @Expose
    private String dphoto;
    @SerializedName("dscan")
    @Expose
    private String dscan;
    private final static long serialVersionUID = 8496600087387334629L;

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDcontact() {
        return dcontact;
    }

    public void setDcontact(String dcontact) {
        this.dcontact = dcontact;
    }

    public String getDaddress() {
        return daddress;
    }

    public void setDaddress(String daddress) {
        this.daddress = daddress;
    }

    public String getDlicense() {
        return dlicense;
    }

    public void setDlicense(String dlicense) {
        this.dlicense = dlicense;
    }

    public String getDaadhar() {
        return daadhar;
    }

    public void setDaadhar(String daadhar) {
        this.daadhar = daadhar;
    }

    public String getDphoto() {
        return dphoto;
    }

    public void setDphoto(String dphoto) {
        this.dphoto = dphoto;
    }

    public String getDscan() {
        return dscan;
    }

    public void setDscan(String dscan) {
        this.dscan = dscan;
    }


}
