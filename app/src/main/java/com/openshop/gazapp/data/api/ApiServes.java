package com.openshop.gazapp.data.api;

import android.util.Log;

import com.openshop.gazapp.data.model.DeliveryCost;
import com.openshop.gazapp.data.model.addresses.Addresses;
import com.openshop.gazapp.data.model.advertisement.Advertisement;
import com.openshop.gazapp.data.model.category.Category;
import com.openshop.gazapp.data.model.clientorder.ClientOrder;
import com.openshop.gazapp.data.model.delivery.Delivery;
import com.openshop.gazapp.data.model.login.Login;
import com.openshop.gazapp.data.model.notification.Notification;
import com.openshop.gazapp.data.model.orders.Orders;
import com.openshop.gazapp.data.model.products.Products;
import com.openshop.gazapp.data.model.profile.Profile;
import com.openshop.gazapp.data.model.setting.Setting;


import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiServes {

    //TODO client login
    @Headers({"Accept: application/json"})

    @POST("client/login")
    @FormUrlEncoded
    Call<Login> getClientLogin(@Field("emailOrPhone") String emailOrPhone,
                               @Field("password") String password);
//    //TODO categories
    @Headers({"Accept: application/json"})
    @POST("categories")
    @FormUrlEncoded
    Call<Category> getCategories(@Field("api_token") String api_token ,
                                 @Field("page") int page);
    //TODO products
    @Headers({"Accept: application/json"})
    @POST("products")
    @FormUrlEncoded
    Call<Products> getProducts(@Field("api_token") String api_token ,
                               @Field("category_id") int category_id ,
                               @Field("page") int page);
    //TODO reset password
    @Headers({"Accept: application/json"})
    @POST("client/reset-password")
    @FormUrlEncoded
    Call<Login> resetPassword(@Field("emailOrPhone") String emailOrPhone );
    //TODO new password
    @Headers({"Accept: application/json"})
    @POST("client/new-password")
    @FormUrlEncoded
    Call<Login> newPassword(@Field("password") String password ,
                               @Field("password_confirmation") String password_confirmation ,
                               @Field("pin_code") String pin_code);
    //TODO setting
    @GET("setting")
    Call<Setting> getAppSetting();

    //TODO contact us
    @Headers({"Accept: application/json"})
    @POST("send-message")
    @FormUrlEncoded
    Call<Login> sendMessage(@Field("name") String name ,
                            @Field("phone") String phone ,
                            @Field("content") String pin_code);
    //TODO confirm order
    @Headers({"Accept: application/json"})
    @POST("order/request")
    @FormUrlEncoded
    Call<ClientOrder> confirmOrder(@FieldMap Map<String, String> parms
                                );
    //TODO search
    @Headers({"Accept: application/json"})
    @POST("products")
    @FormUrlEncoded
    Call<Products> search(@Field("api_token") String api_token ,
                               @Field("keyword") String keyword ,
                               @Field("page") int page);
    //TODO get address list
    @Headers({"Accept: application/json"})
    @POST("address/list")
    @FormUrlEncoded
    Call<Addresses> getAddresses(@Field("api_token") String api_token );

    //TODO add address
    @Headers({"Accept: application/json"})
    @POST("address/add")
    @FormUrlEncoded
    Call<Addresses> addAddress(@Field("api_token") String api_token ,
                          @Field("latitude") String latitude ,
                               @Field("longitude") String longitude ,
                               @Field("address") String address);
    //TODO register
    @Headers({"Accept: application/json"})
    @POST("client/register")
    @FormUrlEncoded
    Call<Login> register(@FieldMap Map<String, String> parms
    );
    //TODO get address list
    @Headers({"Accept: application/json"})
    @POST("order/current")
    @FormUrlEncoded
    Call<Orders> getCurrentOrfders(@Field("api_token") String api_token );

    @Headers({"Accept: application/json"})
    @POST("order/previous")
    @FormUrlEncoded
    Call<Orders> getLastOrfders(@Field("api_token") String api_token );
    //TODO edit prof
    @Headers({"Accept: application/json"})
    @POST("client/profile")
    @Multipart
    Call<Profile> editProfile(
                                   @Part("api_token") RequestBody api_token,
                                   @Part("name") RequestBody name,
                                   @Part("phone") RequestBody phone,
                                   @Part("home_phone") RequestBody home_phone,

                                   @Part MultipartBody.Part file);

    //TODO change password
    @Headers({"Accept: application/json"})
    @POST("client/change-password")
    @FormUrlEncoded
    Call<Login> changePassword(@Field("old_password") String old_password ,
                               @Field("new_password") String new_password ,
                               @Field("new_password_confirmation") String new_password_confirmation ,
                               @Field("api_token") String api_token);

    //TODO register token
    @Headers({"Accept: application/json"})
    @GET("client/register-token")
    Call<Setting> registerToken(
                               @Query("token") String token ,
                               @Query("type") String type ,
                               @Query("client_id") int client_id);
    //TODO get ads
    @Headers({"Accept: application/json"})
    @POST("advertisements")
    @FormUrlEncoded
    Call<Advertisement> getAds(
                               @Field("api_token") String api_token);
    //TODO delivery

    @Headers({"Accept: application/json"})
    @GET("show-deliver-price")
     Call<Delivery> getDeliveryCost();
    //TODO notifications
    @Headers({"Accept: application/json"})
    @GET("client/notification/list")
    Call<Notification> getNotificationList(@Query("api_token") String api_token);
    //TODO remove token
    @Headers({"Accept: application/json"})
    @GET("client/remove/token")
    Call<Setting> removeToken(
            @Query("api_token") String api_token ,
            @Query("token") String token );







//
//
//    @POST("seller/login")
//    @FormUrlEncoded
//    Call<Sellerlogin> getDelarLogin(@Field("email") String email,
//                                    @Field("password") String password);
//
//
//    @POST("client/register")
//    @FormUrlEncoded
//    Call<ClientLogin> getClientRegister(@Field("name") String name,
//                                        @Field("email") String email,
//                                        @Field("password") String password,
//                                        @Field("password_confirmation") String password_confirmation,
//                                        @Field("phone") String phone,
//                                        @Field("age") String age,
//                                        @Field("gender") String gender,
//                                        @Field("address") String address,
//                                        @Field("longitude") double longitude,
//                                        @Field("latitude") double latitude);
//
//
//
//    @POST("seller/register")
//    @FormUrlEncoded
//    Call<SellerRegister> getSellerRegister(@Field("name") String name,
//                                           @Field("email") String email,
//                                           @Field("password") String password,
//                                           @Field("password_confirmation") String password_confirmation,
//                                           @Field("phone") String phone,
////                                        @Field("image") String image,
//                                           @Field("address") String address,
//                                           @Field("longitude") double longitude,
//                                           @Field("latitude") double latitude
//
//    );
//
//    //TODO edit profile user
//    @Headers({"Accept: application/json"})
//    @POST("client/profile/update")
//    @FormUrlEncoded
//    Call<ClientProfileUpdate> editUserProfile(@Header("Authorization") String Token,
//                                              @Field("name") String name,
//                                              @Field("email") String email,
//                                              @Field("phone") String phone,
//                                              @Field("age") int age,
//                                              @Field("gender") String gender,
//                                              @Field("address") String address,
//                                              @Field("longitude") double longitude,
//                                              @Field("latitude") double latitude);
//
//
//    //TODO Edit profile dealer
//    @Headers({"Accept: application/json"})
//    @POST("seller/profile/update")
//    @FormUrlEncoded
//    Call<SellerProfileUpdate> editDealefProfile(@Header("Authorization") String Token,
//                                                @Field("name") String name,
//                                                @Field("email") String email,
//                                                @Field("phone") String phone,
//                                                @Field("address") String address,
//                                                @Field("longitude") double longitude,
//                                                @Field("latitude") double latitude);
//
//
//    //TODO update password
//    @Headers({"Accept: application/json"})
//    @POST("client/password/update")
//    @FormUrlEncoded
//    Call<ClientPasswordUpdate> updateClientPassword(@Header("Authorization") String Token,
//                                                    @Field("current-password") String current_password,
//                                                    @Field("password") String password,
//                                                    @Field("password_confirmation") String password_confirmation);
//
//
//    @GET("general/category")
//    Call<Categories> getCategories();
//
//
//    //TODO get products
//    @Headers({"Accept: application/json"})
//    @GET("general/products")
//    Call<com.woow.woowsouq.data.model.products.Products> getAllProducts();
//
//
//    //TODO get products top
//    @Headers({"Accept: application/json"})
//    @GET("general/products/top")
//    Call<TopProducts> getATopProducts();
//
//
//    //TODO get products details
//    @Headers({"Accept: application/json"})
//    @GET("general/product")
//    Call<com.woow.woowsouq.data.model.productDetail.ProductDetail> getAProductDetails(@Query("product_id") int id);
//
//
//    //TODO category products
//    @Headers({"Accept: application/json"})
//    @GET("general/category/product")
//    Call<ProductDetail> getcategoryProduct(@Query("category_id") int category_id);
//
//
//    //TODO ads
//    @Headers({"Accept: application/json"})
//    @GET("general/ads")
//    Call<SlideAds> getAds();
//
//
//    //TODO get seller products
//    @Headers({"Accept: application/json"})
//    @GET("seller/products")
//    Call<ProductDetail> getDealerProducts(@Header("Authorization") String Token);
//
//
//    //TODO get seller products of adepartment
//    @Headers({"Accept: application/json"})
//    @GET("seller/category/products")
//    Call<ProductDetail> getDealerCategoyProducts(@Header("Authorization") String Token
//            , @Query("category_id") int category_id);
//
//
//
//    //TODO Reset password User
//    @Headers({"Accept: application/json"})
//    @POST("client/reset-password")
//    @FormUrlEncoded
//    Call<ClientResetPassword> getResetPasswordUser(@Header("Authorization") String Token,
//                                                   @Field("email") String email);
//
//
//
//    //TODO Reset password Dealer
//    @Headers({"Accept: application/json"})
//    @POST("seller/reset-password")
//    @FormUrlEncoded
//    Call<SellerResetPassword> getResetPasswordDealer(@Header("Authorization") String Token,
//                                                     @Field("email") String email);
//
//
//    //TODO New password User
//    @Headers({"Accept: application/json"})
//    @POST("client/new-password")
//    @FormUrlEncoded
//    Call<ClientNewPassword> getNewPasswordUser(@Header("Authorization") String Token,
//                                               @Field("pin_code") String pin_code,
//                                               @Field("email") String email,
//                                               @Field("password") String password,
//                                               @Field("password_confirmation") String password_confirmation);
//
//
//
//
//
//    //TODO New password Dealer
//    @Headers({"Accept: application/json"})
//    @POST("seller/new-password")
//    @FormUrlEncoded
//    Call<SellerNewPassword> getNewPasswordDealer(@Header("Authorization") String Token,
//                                                 @Field("pin_code") String pin_code,
//                                                 @Field("email") String email,
//                                                 @Field("password") String password,
//                                                 @Field("password_confirmation") String password_confirmation);
//
//
//    //TODO confirm user order
//    @Headers({"Accept: application/json"})
//    @POST("client/create-order")
//    @FormUrlEncoded
//    Call<NewOrder> confirmOrder(@Header("Authorization") String Token,
//                                @Field("products[0][product_id]") List<Integer> ids,
//                                @Field("products[0][qty]") List<Integer> quantities,
//                                @Field("note") String note,
//                                @Field("address") String address,
//                                @Field("payment_method_id") int payment_method_id,
//                                @Field("phone") String phone,
//                                @Field("name") String name);
//
//    //TODO add product
//    @Headers({"Accept: application/json"})
//    @POST("seller/create-product")
//    @Multipart
//    Call<CreateProduct> addProduct(@Header("Authorization") String Token,
//                                   @Part("name") RequestBody name,
//                                   @Part("description") RequestBody description,
//                                   @Part MultipartBody.Part image,
//                                   @Part("price") RequestBody price,
//                                   @Part("category_id") RequestBody category_id,
//                                   @Part("number_product") RequestBody number_product,
//                                   @Part List<MultipartBody.Part> files);
//
//
//
//
//    //TODO get Client Order
//    @Headers({"Accept: application/json"})
//    @GET("client/order")
//    Call<UserOrder> getClientOrderUser(@Header("Authorization") String Token,
//                                       @Query("status") String status);
//
//
//
//    //TODO get Client Cancel Order
//    @Headers({"Accept: application/json"})
//    @POST("client/declined-order")
//    @FormUrlEncoded
//    Call<ClientDeclinedOrder> getClientCancelOrderUser(@Header("Authorization") String Token,
//                                                       @Field("order_id") int order_id);






}
