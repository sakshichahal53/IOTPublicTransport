package retrofit;

/**
 * Created by Iron_Man on 24/06/17.
 */

public class APIUtil {

    private APIUtil() {}

    public static APIServices getAPIService() {

        return ServiceGenerator.createService(APIServices.class,"btp3","sharma@007");
    }
}
