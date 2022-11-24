package tiimae.webshop.iprwc.constants;

public class ApiConstant {

    public static final String apiPrefix = "/api/v1.0/";

    // authentication api calls
    public static final String register = apiPrefix + "auth/register";
    public static final String login = apiPrefix + "auth/login";

    // User API calls
    public static final String getOneUser = apiPrefix + "user/{userId}";
    public static final String getUsersWithRoles = apiPrefix + "user/roles";
}
