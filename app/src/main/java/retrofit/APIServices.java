package retrofit;

/**
 * Created by Iron_Man on 24/06/17.
 */

import com.google.gson.JsonObject;

import models.ContactCreateModel;
import models.ListVehicleLocations;
import models.LocationModel;
import models.QRModel;
import models.UpdateContactsModel;
import models.User;
import models.UserJourneyList;
import models.UserModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIServices {

    @POST("/api/location/create/")
    Call<LocationModel> savePost(@Body LocationModel locationModel);

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("/account/test/")
    Call<User> createNewUser(@Body User user);

    @GET("/api/transport/{gps_id}/")
    Call<QRModel> getDriverDetails(@Path("gps_id") String license_num);

    @Headers("Content-Type: application/json")
    @POST("/api-token-auth/")
    Call<JsonObject> getToken(@Body JsonObject loginModel);

    @GET("/api/accounts/users/{username}")
    Call<UserModel> getUserDetails(@Path("username") String username);

    @GET("/api/location/nearby/{username}/")
    Call<ListVehicleLocations> getNearByCoordinates(@Path("username") String username);

    @GET("/api/location/history/{username}")
    Call<UserJourneyList> getUserHistory(@Path("username") String username);

    @POST("/api/accounts/users/create/contact/")
    Call<ContactCreateModel> contactCreate(@Body ContactCreateModel contactCreateModel);

    @PUT("/api/accounts/users/contact/{username}/")
    Call<UpdateContactsModel> contactUpdate(@Path("username") String username, @Body UpdateContactsModel contactCreateModel);


}