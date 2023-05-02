package com.example.aljabermall.repository

import android.util.Log
import com.example.aljabermall.api.ApiClient
import com.example.aljabermall.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Part
import java.io.File

object NetworkRepository {

    private val apiPassword =
        "ase1iXcLAxanvXLZcgh6tk".toRequestBody("multipart/form-data".toMediaTypeOrNull())

    suspend fun userLogin(
        language: String,
        email: String,
        password: String,
        deviceId: String,
    ): NetworkResults<LoginModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val emailBody = email.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userPassBody = password.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val deviceIdBody = deviceId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results = ApiClient.retrofitService.userLogin(
                    apiPassword,
                    languageBody,
                    emailBody,
                    userPassBody,
                    deviceIdBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun userSignUp(
        language: String,
        name: String,
        email: String,
        password: String,
        phone: String,


    ): NetworkResults<RegisterModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val emailBody = email.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userPassBody = password.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val nameBody = name.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.userSignUp(
                    nameBody,
                    emailBody,
                    userPassBody,
                    phoneBody,

                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun userSignUpProvider(
        language: String,
        email: String?,
        deviceId: String,
        name: String?,
        userImage: String,
        loginProvider: String,
        providerStatus: String
    ): NetworkResults<RegisterModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val emailBody = email?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val deviceIdBody = deviceId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val nameBody = name?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val loginProviderBody =
                loginProvider.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val providerStatusBody =
                providerStatus.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userImageBody = userImage.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.userSignUpProvider(
                    apiPassword,
                    languageBody,
                    emailBody,
                    nameBody,
                    userImageBody,
                    deviceIdBody,
                    loginProviderBody,
//                    providerStatusBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun resetPassword(language: String, email: String): NetworkResults<MessageModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val emailBody = email.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results = ApiClient.retrofitService.resetPassword(
                    apiPassword,
                    languageBody,
                    emailBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun sendOtp(phone:String ,type:String,otp:String,language: String): NetworkResults<MessageModel> {
        return withContext(Dispatchers.IO) {
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val typeBody = type.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            val otpBody = otp.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val langBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.checkOtpCode(
                    phoneBody,typeBody,otpBody,langBody

                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun reSendOtp(phone:String ,type:String,language: String): NetworkResults<MessageModel> {
        return withContext(Dispatchers.IO) {
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val typeBody = type.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            val langBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.resendOtpCode(
                    phoneBody,typeBody,langBody

                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun resetPassword(uid:String,curPassword:String,newPasswrod:String,language: String): NetworkResults<MessageModel> {
        val type = "2"
        return withContext(Dispatchers.IO) {
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val curPasswordBodu = curPassword.toRequestBody("multipart/form-data".toMediaTypeOrNull())



            val newPasswrodBody = newPasswrod.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//            val typeBody = type.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val langBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val typeBody = type.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            try {
                val results = ApiClient.retrofitService.editUserForgetPass(
                    langBody,newPasswrodBody,uidBody,typeBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }


    }
    suspend fun otpForForget(phone: String,type: String,language: String): NetworkResults<MessageModelForget> {
        return withContext(Dispatchers.IO) {
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val typeBody = type.toRequestBody("multipart/form-data".toMediaTypeOrNull())



            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())


            try {
                val results = ApiClient.retrofitService.resetPasswordOTp(
                    phoneBody,typeBody,languageBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }


    }
    suspend fun getUserInfo(language: String, userId: String): NetworkResults<ProfileModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results =
                    ApiClient.retrofitService.getUserInfo( languageBody, userIdBody)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getBranches(language: String): NetworkResults<BranchesModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results = ApiClient.retrofitService.getBranches(apiPassword, languageBody)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun updateUserBranches(
        language: String,
        userId: String,
        branchId: String,
        deviceId: String,
    ): NetworkResults<UpdateUserBranchModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val branchIdBody = branchId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val deviceIdBody = deviceId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results =


//                = if (userId == "0")
//                    ApiClient.guestRetrofitServices.updateGuestBranch(
//                        apiPassword,
//                        languageBody,
//                        userIdBody,
//                        branchIdBody,
//                        deviceIdBody
//                    )
//                else
                    ApiClient.retrofitService.updateUserBranch(
                    apiPassword,
                    languageBody,
                    userIdBody,
                    branchIdBody,
                    deviceIdBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getCategories(language: String, userId: String): NetworkResults<CategoriesModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results =

//                    if (userId == "0")
//                    ApiClient.guestRetrofitServices.getCategories(languageBody)
//                else
                    ApiClient.retrofitService.getCategories( languageBody)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.d("UN DONE " ,e.toString() )
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getProducts(
        language: String,
        userId: String,
        deviceId: String
    ): NetworkResults<ProductsModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val deviceIdBody = deviceId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results =

//                    if (userId == "0")
//                    ApiClient.guestRetrofitServices.getProducts(
//                        languageBody,
//                        userIdBody,
//
//                    )
//                else
                    ApiClient.retrofitService.getProducts(
                        languageBody,
                        userIdBody,
                    )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.d("NetworkResults",e.message.toString())
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getProductsDeals(
        language: String,
        userId: String,
        type: String
    ): NetworkResults<ProductsModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val type_str = type.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results =
//                    if (userId == "0")
//                    ApiClient.guestRetrofitServices.getProducts_deals(
//                        languageBody,
//                        userIdBody,
//                        type_str
//                        )
//                else
                    ApiClient.retrofitService.getProducts_deals(
                        languageBody,
                        userIdBody,
                        type_str
                    )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.d("NetworkResults",e.message.toString())
                NetworkResults.Error(e)
            }
        }
    }
    suspend fun getProductsOnSale(
        language: String,
        userId: String,
        deviceId: String
    ): NetworkResults<ProductsModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val deviceIdBody = deviceId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results =
//                    if (userId == "0")
//                    ApiClient.guestRetrofitServices.getProductOnSale(
//                        apiPassword,
//                        languageBody,
//                        userIdBody,
//                        deviceIdBody
//                    )
//                else
                    ApiClient.retrofitService.getProductOnSale(
                    apiPassword,
                    languageBody,
                    userIdBody,
                    deviceIdBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getRelatedProducts(
        language: String,
        userId: String,
        itemId: String,
    ): NetworkResults<RelatedProductModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val itemIds = itemId.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results =
//                    if (userId == "0")
//                    ApiClient.guestRetrofitServices.getProductsDetalis(
//                        languageBody,
//                        userIdBody,
//                        itemIds
//
//                    )
//                else
                    ApiClient.retrofitService.getProductsDetalis(
                    languageBody,
                    userIdBody,
                    itemIds
                )

                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.d("eException",e.message.toString())
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getProductsCategories(
        language: String,
        userId: String,
        deviceId: String
    ): NetworkResults<CategoryProductModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val deviceIdBody = deviceId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results =
//                    if (userId == "0")
//                    ApiClient.guestRetrofitServices.getProductsCategories(
//
//                        userIdBody,
//
//                    )
//                else
                    ApiClient.retrofitService.getProductsCategories(

                    userIdBody,

                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun addUserLocation(
        language: String,
        userId: String,
        locality: String?,
        address_line1: String?,
        given_name: String?,
        family_name: String?,
//        area: String,
//        street: String,
//        buildingNo: String,
//        apartmentNo: String
    ): NetworkResults<MessageModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val locationNameBody =
                locality?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val addressLineBody = address_line1?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val givenNameBody = given_name?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val familyNameBody = family_name?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//            val areaBody = area.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//            val streetBody = street.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//            val buildingNoBody = buildingNo.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//            val apartmentNoBody = apartmentNo.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results = ApiClient.retrofitService.addUserLocation(
                    languageBody,
                    userIdBody,
                    locationNameBody,
                    addressLineBody,
                    givenNameBody,
                    familyNameBody,
//                    areaBody,
//                    streetBody,
//                    buildingNoBody
//                    apartmentNoBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun updateUserLocation(

        uid: String,
        profile_id: String,
        locality: String,
        address_line1: String,
        given_name: String,
        family_name: String,
        language: String

    ): NetworkResults<MessageModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val profileIdBody =
                profile_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val localityBody = locality.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val addressLineBody = address_line1.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val givenNameBody = given_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val familyNameBody = family_name.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.updateLocation(
                    userIdBody,
                    profileIdBody,
                    localityBody,
                    addressLineBody,
                    givenNameBody,
                    familyNameBody,
                    languageBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getUserLocations(
        language: String,
        userId: String
    ): NetworkResults<UserLocationsModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results = ApiClient.retrofitService.getUserLocations(
                    languageBody,
                    userIdBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }




//    suspend fun getAddressDetails(
//        profile_id: String
//
//    ): NetworkResults<UserLocationsModel> {
//        return withContext(Dispatchers.IO) {
//            val profileIdBody = profile_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//            try {
//                val results = ApiClient.retrofitService.getAddressDetails(
//                    profileIdBody
//                )
//                NetworkResults.Success(results)
//            } catch (e: Exception) {
//                NetworkResults.Error(e)
//            }
//        }
//    }


    suspend fun deleteUserLocation(
        userid:String,
        flag: String,
        locationId: String,

    ): NetworkResults<MessageModel> {
        return withContext(Dispatchers.IO) {
            val flag = flag.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val locationIdBody = locationId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val uid = userid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.deleteLocation(
                    uid,
                    flag,
                    locationIdBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun addToCart(
        vid: String,
        uid: String,
        quantity: String,
        price: String,
        lang: String

    ): NetworkResults<Message> {
        return withContext(Dispatchers.IO) {
            val languageBody = lang.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val productIdBody = vid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val quantityBody = quantity.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val price = price.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results =
//                    if (uid == "0")
//                    ApiClient.guestRetrofitServices.addToCart(
//                        productIdBody,
//                        userIdBody,
//                        languageBody,
//                        price,
//                        quantityBody,
//
//
//                    )
//                else
                    ApiClient.retrofitService.addToCart(
                        productIdBody,
                        userIdBody,
                        quantityBody,
                        price,
                        languageBody

                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun updateCart(
        language: String,
        userId: String,
        productId: String,
        quantity: String,
        order_id:String
    ): NetworkResults<Message> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val productIdBody = productId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val quantityBody = quantity.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val order_idBody = order_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results =
//                    if (userId == "0")
//                    ApiClient.guestRetrofitServices.updateCart(
//                        languageBody,
//                        userIdBody,
//                        productIdBody,
//                        quantityBody,
//                        order_idBody
//                    )
//                else
                    ApiClient.retrofitService.updateCart(
                    languageBody,
                    userIdBody,
                    productIdBody,
                    quantityBody,
                    order_idBody
//                    @Part("lang") language: RequestBody,
//                    @Part("uid") userId: RequestBody,
//                    @Part("order_item_id") productId: RequestBody,
//                    @Part("quantity") quantity: RequestBody,
//                    @Part("order_id") order_id: RequestBody,


                )
                Log.d("DoNES",results.toString())
                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.d("UNNDoNES",e.toString())

                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getCart(
        language: String,
        userId: String,

    ): NetworkResults<UserCartModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results =
//                    if (userId == "0")
//                    ApiClient.guestRetrofitServices.getCart(
//                        languageBody,
//                        userIdBody,
//
//                    )
//                else
                    ApiClient.retrofitService.getCart(
                    languageBody,
                    userIdBody,

                )
                Log.d("DONE",results.toString())

                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.d("UnDONE",e.toString())

                NetworkResults.Error(e)
            }
        }
    }



    suspend fun getProductsSubCategories(
        language: String,
        userId: String,
        deviceId: String,
        cid: String
    ): NetworkResults<CategoryProductModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val cid_body = cid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results =
//                    if (userId == "0")
//                    ApiClient.guestRetrofitServices.getProductsSubCategories(
//
//                        languageBody,
//                        userIdBody,
//                        cid_body
//                    )
//                else
                    ApiClient.retrofitService.getProductsSubCategories(

                    languageBody,
                    userIdBody,
                    cid_body
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun deleteFromCart(
        language: String,
        userId: String,
        productId: String,

    ): NetworkResults<Message> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val productIdBody = productId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results =
//                    if (userId == "0")
//                    ApiClient.guestRetrofitServices.deleteFromCart(
//
//                        languageBody,
//                        userIdBody,
//                        productIdBody,
//
//                    )
//                else
                    ApiClient.retrofitService.deleteFromCart(

                    languageBody,
                    userIdBody,
                    productIdBody,

                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun checkPromoCode(
        language: String,
        order_id: String,
        userId: String,
        promoCode: String
    ): NetworkResults<PromoCodeModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val promoCodeBody = promoCode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val order_idBody = order_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.checkPromoCode(

                    languageBody,
                    order_idBody,
                    userIdBody,
                    promoCodeBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }
    suspend fun removecheckPromoCode(
        language: String,
        order_id: String,
        userId: String,
        promoCode: String
    ): NetworkResults<PromoCodeModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val promoCodeBody = promoCode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val order_idBody = order_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.removeCheckPromoCode(

                    languageBody,
                    order_idBody,
                    userIdBody,
                    promoCodeBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun cartCheckout(
        language: String,
        userId: String,

        order_id: String,

    ): NetworkResults<Message> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val orderIdBody = order_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.checkout(
                    languageBody,
                    userIdBody,
                    orderIdBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {

                Log.d("eemessagee",e.localizedMessage.toString())
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getInvoices(language: String, userId: String): NetworkResults<InvoicesModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results =
                    ApiClient.retrofitService.getInvoices( languageBody, userIdBody)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.d("UnDones",e.toString())

                NetworkResults.Error(e)
            }
        }
    }
    suspend fun getAboutUs(language: String): NetworkResults<AboutUsData> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results =
                    ApiClient.retrofitService.aboutUsRequest( languageBody)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.d("UnDones",e.toString())

                NetworkResults.Error(e)
            }
        }
    }

    suspend fun addToFavourite(
        language: String,
        userId: String,
        productId: String,
    ): NetworkResults<FavouriteMessageModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val productIdBody = productId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results = ApiClient.retrofitService.addToFavourite(

                    userIdBody,
                    productIdBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getFavourite(language: String, userId: String): NetworkResults<FavouriteModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results =
                    ApiClient.retrofitService.getFavourites( userIdBody)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun removeFavourite(
        language: String,
        userId: String,
        favoriteId: String
    ): NetworkResults<FavouriteMessageModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val favoriteIdBody = favoriteId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results = ApiClient.retrofitService.deleteFavourite(
                    userIdBody,
                    favoriteIdBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun editUseProfile(
        language: String,
        userId: String,
        password: String?,
        rePassword: String?,
        phone: String?,
        name: String?,
        birthDate: String?,
        profileImage: File?,
        gender: String?,
        email: String?
    ): NetworkResults<MessageModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val passwordBody = password?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val rePasswordBody = rePassword?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val phoneBody = phone?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val nameBody = name?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val birthDateBody = birthDate?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val genderBody = gender?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val emailBody = email?.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            var imagePart: MultipartBody.Part? = null
            profileImage?.let {
                imagePart = MultipartBody.Part.createFormData(
                    "user_picture",
                    it.name,
                    it.asRequestBody("image/*".toMediaTypeOrNull())
                )
            }

            val proName = profileImage?.name?.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = ApiClient.retrofitService.editUserProfile(

                    languageBody,
                    emailBody,
                    rePasswordBody,
                    passwordBody,
                    phoneBody,
                    nameBody,
                    userIdBody,
                    imagePart,
                    proName
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun addPhoneNumber(
        language: String,
        userId: String,
        phone: String,
    ): NetworkResults<MessageModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results = ApiClient.retrofitService.addPhoneNumber(
                    apiPassword,
                    userIdBody,
                    languageBody,
                    phoneBody,
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun productSearch(
        language: String,
        userId: String,
        searchTerm: String,
//        deviceId: String,
    ): NetworkResults<ProductSearchModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val searchTermBody = searchTerm.toRequestBody("multipart/form-data".toMediaTypeOrNull())
//            val deviceIdBody = deviceId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results =
//                    if (userId == "0")
//                    ApiClient.guestRetrofitServices.productSearchGuest(
//                        apiPassword,
//                        languageBody,
//                        userIdBody,
//                        searchTermBody,
//                        deviceIdBody,
//                    )
//                else
                    ApiClient.retrofitService.productSearch(
//                        apiPassword,
                        languageBody,
                        userIdBody,
                        searchTermBody,
                    )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun sendConactUs(
        language: String,
        userId: String,
        name:String,
        email: String,
    subject:String,
        message: String

        ): NetworkResults<Message> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val userIdBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            val nameBody = name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val emailBody = email.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val subjectBody = subject.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val messageBody = message.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results = if (userId == "0")
                    ApiClient.retrofitService.sendContactUs(
                        languageBody,
                        userIdBody,
                        nameBody
                        ,emailBody
                        ,subjectBody
                        ,messageBody



                        )
                else  ApiClient.retrofitService.sendContactUs(
                    languageBody,
                    userIdBody,
                    nameBody
                    ,emailBody
                    ,subjectBody
                    ,messageBody



                )
                Log.d("DONE",results.toString())

                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.d("UnDONE",e.message.toString())

                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getCity(language: String): NetworkResults<CityModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results =
                    ApiClient.retrofitService.getCity( languageBody)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getArea(tid:String,language: String): NetworkResults<CityModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val tidBody = tid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results =
                    ApiClient.retrofitService.getArea( tidBody,languageBody)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun updateuserInfo(userId: String,order_id: String,tid:String,fName:String,lName:String,phone: String,language: String): NetworkResults<Message> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val orderIDBody = order_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val uidBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val tidBody = tid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            val phoneBody = phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val lNameBody = lName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val fNameBody = fName.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results =
                    ApiClient.retrofitService.updateCartInfo(languageBody,uidBody,orderIDBody,fNameBody,lNameBody,phoneBody,tidBody)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }
    suspend fun updateuserInfoForTakeAway(userId: String,order_id: String,tid:String,language: String): NetworkResults<Message> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val orderIDBody = order_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val uidBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val tidBody = tid.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results =
                    ApiClient.retrofitService.updateCartForTakeAway(languageBody,uidBody,orderIDBody,tidBody)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun orderDetalis(userId: String,order_id: String,language: String): NetworkResults<DetailsInvoicesModel> {
        return withContext(Dispatchers.IO) {
            val languageBody = language.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val orderIDBody = order_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val uidBody = userId.toRequestBody("multipart/form-data".toMediaTypeOrNull())

            try {
                val results =
                    ApiClient.retrofitService.viewOrderDetalis(uidBody,orderIDBody)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                Log.d("EEEEE",e.toString())
                Log.d("EEEEE",order_id)

                NetworkResults.Error(e)
            }
        }
    }
}
