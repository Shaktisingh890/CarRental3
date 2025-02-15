package com.example.myapplication.network;


import com.example.myapplication.models.request.BookingRequest;
import com.example.myapplication.models.request.ChangePasswordRequest;
import com.example.myapplication.models.response.AddCarResponse;
import com.example.myapplication.models.response.AllDriversResponse;
import com.example.myapplication.models.response.BookingDetailsResponse;
import com.example.myapplication.models.response.BookingResponse;
import com.example.myapplication.models.response.Car;
import com.example.myapplication.models.response.CarDetailsResponse;
import com.example.myapplication.models.response.CategoryResponse;
import com.example.myapplication.models.response.ChangePasswordResponse;
import com.example.myapplication.models.response.CustomerBookingResponse;
import com.example.myapplication.models.response.CustomerCarResponse;
import com.example.myapplication.models.response.DriverImageResponse;
import com.example.myapplication.models.response.DriverResponse1;
import com.example.myapplication.models.response.GetCarByUserResponse;
import com.example.myapplication.models.response.ImageResponse;
import com.example.myapplication.models.request.LoginRequest;
import com.example.myapplication.models.response.LoginResponse;
import com.example.myapplication.models.response.LogoutResponse;
import com.example.myapplication.models.request.PartnerRegisterRequest;
import com.example.myapplication.models.response.Notification;
import com.example.myapplication.models.response.NotificationResponse;
import com.example.myapplication.models.response.PartnerResponse;
import com.example.myapplication.models.request.RegisterRequest;
import com.example.myapplication.models.response.RegisterResponse;
import com.example.myapplication.models.response.SubCategoryResponse;
import com.example.myapplication.models.response.UploadIdResponse;
import com.example.myapplication.models.response.UserProfileResponse;


import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Multipart;


public interface ApiService {

    // Define the POST request for login
    @POST("/api/v1/users/login")  // The endpoint of the login API
    Call<LoginResponse> login(@Body LoginRequest loginRequest); // Send LoginRequest and receive LoginResponse.java

    // Define the POST request for registration
    @POST("/api/v1/customers/customerRegister")  // The endpoint of the register API
    Call<RegisterResponse> register(@Body RegisterRequest registerRequest); // Send RegisterRequest and receive RegisterResponse


    @GET("/api/v1/users/profile")
    Call<UserProfileResponse> getUserProfile();

//    @GET("/api/v1/users/profile")
//    Call<UserProfileResponse> deleteProfileImage();


    @Multipart
    @POST("/api/v1/users/uploadImage") // Replace with your server's endpoint URL
    Call<ImageResponse> uploadImage(@Part MultipartBody.Part file);

    @Multipart
    @POST("/api/v1/users/updateProfile/")
    Call<UserProfileResponse> updateUserProfile(
            @Part("fullName") RequestBody name,
            @Part("email") RequestBody email,
            @Part("phoneNumber") RequestBody phoneNumber,
            @Part("address") RequestBody address


    );

    @Multipart
    @POST("api/v1/users/updateProfile/") // Replace with your API endpoint
    Call<UserProfileResponse> updatePartnerProfile(
            @Part("fullName") RequestBody name,
            @Part("phoneNumber") RequestBody phone,
            @Part("email") RequestBody email,
            @Part("company") RequestBody company,
            @Part("companyAddress") RequestBody companyAddress,
            @Part("area") RequestBody area,
            @Part("account") RequestBody account,
            @Part("upi") RequestBody upi
    );

        @POST("/api/v1/users/logout")
        Call<LogoutResponse> logoutUser();
    @POST("/api/v1/users/deleteAccount")
    Call<LogoutResponse> deleteAccount();


    @Multipart
    @POST("uploadDriverLicensePhoto") // Endpoint for uploading driver license photos
    Call<DriverImageResponse> uploadDriverLicensePhoto(
            @Part MultipartBody.Part licenseFront,
            @Part MultipartBody.Part licenseBack
    );





    @Multipart
    @POST("/api/v1/drivers/registerDriver")
    Call<DriverResponse1> registerDriverWithPhotos(
            @Part MultipartBody.Part licenseFront,
            @Part MultipartBody.Part licenseBack,
            @Part("fullName") RequestBody fullName,
            @Part("email") RequestBody email,
            @Part("phoneNumber") RequestBody phoneNumber,
            @Part("password") RequestBody password,
            @Part("licenseNumber") RequestBody licenseNumber,
            @Part("licenseExpiryDate") RequestBody licenseExpiryDate
    );


    @POST("/api/v1/partners/registerPartner")  // Specify the endpoint
    Call<PartnerResponse> register(@Body PartnerRegisterRequest request);





    @Multipart
    @POST("/api/v1/cars/addCar")
    Call<AddCarResponse> addCarWithImages(
            @Part("carDetails") RequestBody carDetails,
            @Part List<MultipartBody.Part> imageFiles,
            @Part List<MultipartBody.Part> idImageFiles,
            @Part List<MultipartBody.Part> carDocumentFiles,
            @Part List<MultipartBody.Part> vehicleLicenseFiles,
            @Part List<MultipartBody.Part> bankpassbookFiles

    );

    @Multipart
    @PUT("/api/v1/cars/updateCar/{carId}")
    Call<AddCarResponse> EditCarWithImages(
            @Path("carId") String carId,
            @Part("carDetails") RequestBody carDetails,
            @Part List<MultipartBody.Part> imageFiles,
            @Part List<MultipartBody.Part> idImageFiles,
            @Part List<MultipartBody.Part> carDocumentFiles,
            @Part List<MultipartBody.Part> vehicleLicenseFiles,
            @Part List<MultipartBody.Part> bankpassbookFiles

    );


    @GET("api/v1/cars/getCarByUserId")
    Call<CarDetailsResponse> getCarDetailsByUserId(@Query("userId") String userId);

    @GET("api/v1/cars/getCarByUserId")
    Call<GetCarByUserResponse> getCarsByUserId();

    @GET("api/v1/cars/getCarByCost")
    Call<List<Car>> getCarsByCost(@Query("filter") String costFilter);

    @GET("api/v1/cars/getFetchCategory")
    Call<CategoryResponse> getCarCategories();


    @GET("api/v1/cars/getCarBysubCategory")

    Call<List<CustomerCarResponse>> getCarsBySubCategory(
            @Query("category") String category,
            @Query("subCategory") String subCategory,
            @Query("filter") String costType
    );

    @DELETE("api/v1/cars/deleteCar/{carId}")
    Call<Void> partnerDeleteCarByCarId(@Path("carId") String carId);


    @PUT("partner/updateCar/{carId}")
    Call<Void> updateCarDetails(@Path("carId") String carId, @Body CarDetailsResponse.Car car);


    @GET("api/v1/cars/getFetchSubCategory/{category}")
    Call<SubCategoryResponse> getSubCategories(@Path("category") String category);

    @GET("api/v1/booking/getBookingByuserId")
    Call<ResponseBody> getBookingsByUserId();

    @GET("api/v1/booking/getBookingBypartner")
    Call<ResponseBody> getBookingsByPartnerId();



//    @Multipart
//    @PUT("api/v1/customers/upload-id")
//    Call<UploadIdResponse> uploadId(
//            @Part("type") RequestBody type,
//            @Part("id_number") RequestBody idNumber,
//            @Part MultipartBody.Part frontPhoto,
//            @Part MultipartBody.Part backPhoto
//    );



    @GET("api/v1/customers/identification")
    Call<UploadIdResponse> fetchCustomerDoc();

    @Multipart
    @PUT("api/v1/customers/upload-id")
    Call<UploadIdResponse> saveNewDocCustomer(
            @Part("type") RequestBody type,
            @Part("id_number") RequestBody idNumber,
            @Part MultipartBody.Part frontPhoto,
            @Part MultipartBody.Part backPhoto
    );

//    @Multipart
//    @POST("updateCustomerDoc")
//    Call<UploadIdResponse> updateCustomerDoc(
//            @Part("type") RequestBody type,
//            @Part("id_number") RequestBody idNumber,
//            @Part MultipartBody.Part frontPhoto,
//            @Part MultipartBody.Part backPhoto
//    );


    @POST("api/v1/booking/createBooking")
    Call<BookingResponse> createBooking(@Body BookingRequest bookingRequest);


    // Method to change password
    @PUT("api/v1/users/update_password")
    Call<ChangePasswordResponse> changePassword(@Body ChangePasswordRequest request);




    @GET("api/v1/booking/byId/{bookingId}")
    Call<BookingDetailsResponse> getBookingDetails1(@Path("bookingId") String bookingId);


    @GET("api/v1/drivers/getAllDriver")
    Call<AllDriversResponse> getAllDrivers(); // Fetch all drivers

    @POST("api/v1/notifications/new_notification")
    Call<Void> storeNotification(@Body Notification notification);

    @GET("api/v1/notifications/get_partner_booking_notification") // Replace with the actual endpoint for fetching notifications
    Call<NotificationResponse> getNotifications();





    @DELETE("api/v1/notifications/all_notification")
    Call<Void> deleteAllNotifications();

    @DELETE("api/v1/notifications/delete/{id}")
    Call<Void> deleteNotificationById(@Path("id") String notificationId);




    @FormUrlEncoded
    @POST("api/v1/booking/updatePartnerStatus")
    Call<Void> updateBookingStatus(
            @Field("bookingId") String bookingId,
            @Field("partnerStatus") String partnerStatus,
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("api/v1/booking/updateDriverStatus")
    Call<Void> updateDriverStatus(
            @Field("bookingId") String bookingId,
            @Field("driverStatus") String driverStatus,
            @Field("status") String status

    );

    @FormUrlEncoded
    @POST("api/v1/booking/assignDriver")
    Call<Void> assignDriver(
            @Field("bookingId") String bookingId,
            @Field("driverId") String driverId
    );

    @PUT("api/v1/drivers/updateStatus")
    Call<Void> updateAvailabilityStatus(@Body Map<String, Boolean> availabilityStatus);


    @GET("api/v1/drivers/getAvailability") // Replace with your API endpoint
    Call<Map<String, Boolean>> getAvailabilityStatus();



    @POST("update_driver_location")
    Call<Void> updateDriverLocation(@Body Map<String, Object> locationData);



}



