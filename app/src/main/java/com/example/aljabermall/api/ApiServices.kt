package com.example.aljabermall.api

import com.example.aljabermall.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServices {

    @Multipart
    @POST("app/login-user")
    suspend fun userLogin(
        @Part("api_password") apiPassword: RequestBody,
        @Part("lang") language: RequestBody,
        @Part("user") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("device_id") deviceId: RequestBody,
    ): LoginModel

    @Multipart
    @POST("app/add-user")
    suspend fun userSignUp(
        @Part("name") fullName: RequestBody,
        @Part("mail") email: RequestBody,
        @Part("pass") password: RequestBody,
        @Part("phone") phone: RequestBody,

    ): RegisterModel

    @Multipart
    @POST("users/register")
    suspend fun userSignUpProvider(
        @Part("api_password") apiPassword: RequestBody,
        @Part("lang") language: RequestBody,
        @Part("email") email: RequestBody?,
        @Part("name") fullName: RequestBody?,
//        @Part("image") userImage: RequestBody,
        @Part("device_id") deviceId: RequestBody,
        @Part("provider") loginProvider: RequestBody,
        @Part("provider_status") loginProviderStatus: RequestBody,
    ): RegisterModel

    @Multipart
    @POST("users/password")
    suspend fun resetPassword(
        @Part("api_password") apiPassword: RequestBody,
        @Part("lang") language: RequestBody,
        @Part("email") email: RequestBody
    ): MessageModel

    @Multipart
    @POST("branches/index")
    suspend fun getBranches(
        @Part("api_password") apiPassword: RequestBody,
        @Part("lang") language: RequestBody,
    ): BranchesModel

    @Multipart
    @POST("users/updateUserBranch")
    suspend fun updateUserBranch(
        @Part("api_password") apiPassword: RequestBody,
        @Part("lang") language: RequestBody,
        @Part("user_id") userId: RequestBody,
        @Part("branch_id") branchId: RequestBody,
        @Part("device_id") deviceId: RequestBody,
    ): UpdateUserBranchModel

//    @Multipart
//    @POST("users/updateBranchGuest")
//    suspend fun updateGuestBranch(
//        @Part("api_password") apiPassword: RequestBody,
//        @Part("lang") language: RequestBody,
//        @Part("user_id") userId: RequestBody,
//        @Part("branch_id") branchId: RequestBody,
//        @Part("device_id") deviceId: RequestBody,
//    ): UpdateUserBranchModel


    @Multipart
    @POST("app/get-main-department")
    suspend fun getCategories(

        @Part("lang") language: RequestBody,
    ): CategoriesModel

    @Multipart
    @POST("app/get-sub-department")
    suspend fun getProducts(
        @Part("lang") language: RequestBody,
        @Part("uid") userId: RequestBody,

    ): ProductsModel
    @Multipart
    @POST("app/get-products-deals")
    suspend fun getProducts_deals(
        @Part("lang") language: RequestBody,
        @Part("uid") userId: RequestBody,
        @Part("type") type: RequestBody,

        ): ProductsModel

    @Multipart
    @POST("app/get-product")
    suspend fun getProductsDetalis(
        @Part("lang") language: RequestBody,
        @Part("uid") userId: RequestBody,
        @Part("pid") pid: RequestBody,

        ): RelatedProductModel

    @Multipart
    @POST("items/getOnSales")
    suspend fun getProductOnSale(
        @Part("api_password") apiPassword: RequestBody,
        @Part("lang") language: RequestBody,
        @Part("user_id") userId: RequestBody,
        @Part("device_id") deviceId: RequestBody
    ): ProductsModel

    @Multipart
    @POST("items/getRelatedItems")
    suspend fun getRelatedProducts(
        @Part("api_password") apiPassword: RequestBody,
        @Part("lang") language: RequestBody,
        @Part("user_id") userId: RequestBody,
        @Part("item_id") productId: RequestBody,
        @Part("device_id") deviceId: RequestBody,
    ): ProductsModel

    @Multipart
    @POST("app/get-sub-department")
    suspend fun getProductsCategories(

        @Part("uid") userId: RequestBody,


    ): CategoryProductModel

    @Multipart
    @POST("app/add-address")
    suspend fun addUserLocation(
        @Part("lang") language: RequestBody,
        @Part("uid") userId: RequestBody,
        @Part("locality") locationName: RequestBody?,
        @Part("address_line1") longitude: RequestBody?,
        @Part("given_name") latitude: RequestBody?,
        @Part("family_name") city: RequestBody?,
//        @Part("area") area: RequestBody,
//        @Part("streetNum") street: RequestBody,
//        @Part("buildNum") buildingNumber: RequestBody,
//        @Part("depNum") apartmentNumber: RequestBody
    ): MessageModel

    @Multipart
    @POST("app/get-my-address")
    suspend fun getUserLocations(

        @Part("lang") language: RequestBody,
        @Part("uid") userId: RequestBody,
    ): UserLocationsModel

    @Multipart
    @POST("app/edit-profile")
    suspend fun updateLocation(
        @Part("uid") uid: RequestBody,
        @Part("profile_id") profile_id: RequestBody,
        @Part("locality") locality: RequestBody,
        @Part("address_line1") address_line1: RequestBody,
        @Part("given_name") given_name: RequestBody,
        @Part("family_name") family_name: RequestBody,
        @Part("lang") lang: RequestBody
//        @Part("latitude") latitude: RequestBody?,
//        @Part("city") city: RequestBody,
//        @Part("area") area: RequestBody,
//        @Part("street") street: RequestBody,
//        @Part("building_number") buildingNumber: RequestBody,
//        @Part("apartment_number") apartmentNumber: RequestBody
    ): MessageModel

    @Multipart
    @POST("app/profile-action")
    suspend fun deleteLocation(
        @Part("uid") uid: RequestBody,
        @Part("flag") flag: RequestBody,
        @Part("profile_id") locationId: RequestBody,
    ): MessageModel

    @Multipart
    @POST("app/add-to-cart")
    suspend fun addToCart(
        @Part("vid") apiPassword: RequestBody,
        @Part("uid") userId: RequestBody,
        @Part("quantity") quantity: RequestBody,
        @Part("price") productId: RequestBody,
        @Part("lang") language: RequestBody

        ): Message

    @Multipart
    @POST("app/update-cart")
    suspend fun updateCart(
        @Part("lang") language: RequestBody,
        @Part("uid") userId: RequestBody,
        @Part("order_item_id") productId: RequestBody,
        @Part("quantity") quantity: RequestBody,
        @Part("order_id") order_id: RequestBody,
    ): Message

    @Multipart
    @POST("app/view-cart")
    suspend fun getCart(
        @Part("lang") language: RequestBody,
        @Part("uid") userId: RequestBody,
    ): UserCartModel


    @Multipart
    @POST("app/get-sub-department")
    suspend fun getProductsSubCategories(


        @Part("lang") language: RequestBody,
        @Part("uid") uid:RequestBody,
        @Part("parent") cid: RequestBody,

        ): CategoryProductModel


    @Multipart
    @POST("app/remove-cart")
    suspend fun deleteFromCart(
        @Part("lang") language: RequestBody,
        @Part("uid") userId: RequestBody,
        @Part("order_item_id") productId: RequestBody,
    ): Message

    @Multipart
    @POST("app/applyCoupon")
    suspend fun checkPromoCode(
        @Part("lang") language: RequestBody,
        @Part("order_id") order_id: RequestBody,
        @Part("uid") userId: RequestBody,
        @Part("code") code: RequestBody,
    ): PromoCodeModel

    @Multipart
    @POST("app/removeCoupon")
    suspend fun removeCheckPromoCode(
        @Part("lang") language: RequestBody,
        @Part("order_id") order_id: RequestBody,
        @Part("uid") userId: RequestBody,
        @Part("coupon_id") code: RequestBody,
    ): PromoCodeModel


    @Multipart
    @POST("app/checkout")
    suspend fun checkout(
        @Part("lang") language: RequestBody,
        @Part("uid") userId: RequestBody,
        @Part("order_id") order_id: RequestBody,

        ): Message

    @Multipart
    @POST("app/view-all-order")
    suspend fun getInvoices(
        @Part("lang") language: RequestBody,
        @Part("uid") userId: RequestBody,
    ): InvoicesModel

    @Multipart
    @POST("app/add-favorite")
    suspend fun addToFavourite(

        @Part("uid") userId: RequestBody,
        @Part("product_id") productId: RequestBody,
    ): FavouriteMessageModel

    @Multipart
    @POST("app/get-fav-products")
    suspend fun getFavourites(
        @Part("uid") userId: RequestBody,
    ): FavouriteModel

    @Multipart
    @POST("app/add-favorite")
    suspend fun deleteFavourite(
        @Part("uid") userId: RequestBody,
        @Part("product_id") productId: RequestBody,
    ): FavouriteMessageModel

    @Multipart
    @POST("app/view-user-profile")
    suspend fun getUserInfo(
        @Part("lang") language: RequestBody,
        @Part("uid") userId: RequestBody,
    ): ProfileModel

    @Multipart
    @POST("app/update-user")
    suspend fun editUserProfile(
        @Part("lang") language: RequestBody,
        @Part("mail") email: RequestBody?,
        @Part("current_pass") passwordConfirmation: RequestBody?,
        @Part("pass") password: RequestBody?,
        @Part("phone") phone: RequestBody?,
        @Part("name") name: RequestBody?,
        @Part("uid") userId: RequestBody,
        @Part profileImage: MultipartBody.Part?,
        @Part("imageName") imageName: RequestBody?

    ): MessageModel

    @Multipart
    @POST("users/addPhoneNumber")
    suspend fun addPhoneNumber(
        @Part("api_password") apiPassword: RequestBody,
        @Part("user_id") userId: RequestBody,
        @Part("lang") language: RequestBody,
        @Part("phone_number") phone: RequestBody,
    ): MessageModel

    @Multipart
    @POST("app/get-products")
    suspend fun productSearch(
//        @Part("api_password") apiPassword: RequestBody,
        @Part("lang") language: RequestBody,
        @Part("uid") userId: RequestBody,
        @Part("key") searchTerm: RequestBody,
    ): ProductSearchModel

    @Multipart
    @POST("items/searchGuest")
    suspend fun productSearchGuest(
        @Part("api_password") apiPassword: RequestBody,
        @Part("lang") language: RequestBody,
        @Part("user_id") userId: RequestBody,
        @Part("search") searchTerm: RequestBody,
        @Part("device_id") deviceId: RequestBody,
    ): ProductSearchModel



    @Multipart
    @POST("app/contact-us")
    suspend fun sendContactUs(
        @Part("lang") language: RequestBody,
        @Part("uid") userId: RequestBody,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("subject") subject: RequestBody,
        @Part("message") message: RequestBody,


        ): Message


    @Multipart
    @POST("app/aboutUs")
    suspend fun aboutUsRequest(
        @Part("lang") language: RequestBody


        ): AboutUsData




    @Multipart
    @POST("app/get-address-details")
    fun getAddressDetails(
        @Part("profile_id") profile_id: RequestBody


    ): Call<UserLocationsModel>



    @Multipart
    @POST("app/view-all-order")
    fun getAddressInOrders(
        @Part("lang") language: RequestBody,
        @Part("uid") userId: RequestBody,
    ): Call<InvoicesModel>


    @Multipart
    @POST("app/getCity")
    suspend fun getCity(
//        @Part("api_password") apiPassword: RequestBody,
        @Part("lang") language: RequestBody,

    ): CityModel


    @Multipart
    @POST("app/getProvince")
    suspend fun getArea(
        @Part("tid") tid: RequestBody,
        @Part("lang") language: RequestBody,

        ): CityModel
    @Multipart
    @POST("app/update-cart")
    suspend fun updateCartInfo(
        @Part("lang") language: RequestBody,
        @Part("uid") userId: RequestBody,
        @Part("order_id") order_id: RequestBody,
        @Part("given_name") given_name: RequestBody,
        @Part("family_name") family_name: RequestBody,
        @Part("phone_number") phone_number: RequestBody,
        @Part("tid") tid: RequestBody,

    ): Message
    @Multipart
    @POST("app/update-cart")
    suspend fun updateCartForTakeAway(
        @Part("lang") language: RequestBody,
        @Part("uid") userId: RequestBody,
        @Part("order_id") order_id: RequestBody,
        @Part("tid") tid: RequestBody,

        ): Message


    @Multipart
    @POST("app/view-order-details")
    suspend fun viewOrderDetalis(
        @Part("uid") userId: RequestBody,
        @Part("order_id") order_id: RequestBody,

        ): DetailsInvoicesModel



    @Multipart
    @POST("app/checkOtpCode")
    suspend fun checkOtpCode(
        @Part("phone") phone: RequestBody,
        @Part("type") type: RequestBody,
        @Part("otp") otp: RequestBody,
        @Part("lang") lang: RequestBody

    ): MessageModel


    @Multipart
    @POST("app/resendOtpCode")
    suspend fun resendOtpCode(
        @Part("phone") phone: RequestBody,
        @Part("type") type: RequestBody,
        @Part("lang") lang: RequestBody

    ): MessageModel
    @Multipart
    @POST("app/forgetPassWord")
    suspend fun resetPasswordOTp(
        @Part("phone") phone: RequestBody,
        @Part("type") type: RequestBody,
        @Part("lang") lang: RequestBody

    ): MessageModelForget
    @Multipart
    @POST("app/resetPassWord")
    suspend fun editUserForgetPass(
        @Part("lang") language: RequestBody,
        @Part("new_pass") password: RequestBody?,
        @Part("uid") userId: RequestBody,
        @Part("type") typeBody: RequestBody,


    ): MessageModel
}