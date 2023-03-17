package tiimae.webshop.iprwc.constants;

public class ApiConstant {

    public static final String apiPrefix = "/api/v1.0/";

    // authentication api calls
    public static final String register = apiPrefix + "auth/register";
    public static final String login = apiPrefix + "auth/login";
    public static final String profile = apiPrefix + "auth/profile";

    public static final String verifyEmail = apiPrefix + "auth/verify-email";
    public static final String sendVerifyEmail = apiPrefix + "auth/send-verify-email";

    public static final String setNewPassword = apiPrefix + "auth/set-new-password";
    public static final String forgotPassword = apiPrefix + "auth/forgot-password";


    // User API calls
    public static final String getOneUser = apiPrefix + "user/{userId}";
    public static final String getOneUserAdmin = apiPrefix + "user/{userId}/admin";
    public static final String getAllUsers = apiPrefix + "user";
    public static final String getOneUserWithRole = apiPrefix + "user/{userId}/roles";
    public static final String getOneUserHasRole = apiPrefix + "user/{userId}/has-role";
    public static final String getUsersWithRoles = apiPrefix + "user/roles";

    // Role API calls
    public static final String getAllRoles = apiPrefix + "role";

    //Category Api Calls
    public static final String getOneCategories = apiPrefix + "category/{categoryId}";
    public static final String getAllCategories = apiPrefix + "category";

    //Brand Api Calls
    public static final String getOneBrand = apiPrefix + "brand/{brandId}";
    public static final String getAllBrands = apiPrefix + "brand";

    public static final String getOneSupplier = apiPrefix + "supplier/{supplierId}";
    public static final String getAllSupplier = apiPrefix + "supplier";

    public static final String getOneProduct = apiPrefix + "product/{productId}";
    public static final String restoreOneProduct = apiPrefix + "product/{productId}/restore";
    public static final String getAllProducts = apiPrefix + "product";

    public static final String getOneUserAddress = apiPrefix + "user-address/{userAddressId}";
    public static final String getAllUserAddressesByUser = apiPrefix + "user-address/user/{userId}";
    public static final String getAllUserAddresses = apiPrefix + "user-address";

    public static final String getAllOrders = apiPrefix + "order";
    public static final String getOrderByUserId = apiPrefix + "order/{userId}";
    public static final String getAllReview = apiPrefix + "review";

    public static final String getCSRF = apiPrefix + "csrf";

    public static final String setNewCartItem = apiPrefix + "cart";
    public static final String getCartById = apiPrefix + "cart/{userId}";
    public static final String removeCartById = apiPrefix + "cart/{userId}/{productId}";
}
