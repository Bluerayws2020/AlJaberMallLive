package com.example.aljabermall.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

sealed class NetworkResults<out R> {
    data class Success<out T>(val data: T) : NetworkResults<T>()
    data class Error(val exception: Exception) : NetworkResults<Nothing>()
}

data class LoginModel(

    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val user: UserModel,
//    @SerializedName("token_type") val token_type: String,
//    @SerializedName("expires_in") val expires_in: Int,
//    @SerializedName("error") val errorMessage: String?
)

data class UserModel
    (

    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("user_type") val user_type: String,
    @SerializedName("provider") val provider: String,
    @SerializedName("provider_status") val provider_status: String,
    @SerializedName("otp") val verified: String? = null,
    @SerializedName("phone") val phone: Int? = null

)

data class RegisterModel(

    @SerializedName("msg") val msg: MessageModel? = null,
    @SerializedName("status") val status: Int? = null,
    @SerializedName("message") val message: String?=null,
    @SerializedName("data") val user: User? = null,


)

/*data class SignUpUser (

    @SerializedName("name") val name : String,
    @SerializedName("device_id") val device_id : String,
    @SerializedName("phone") val phone : Int,
    @SerializedName("email") val email : String,
    @SerializedName("user_type") val user_type : String,
    @SerializedName("user_status") val user_status : String,
    @SerializedName("image") val image : String,
    @SerializedName("provider") val provider : Int,
    @SerializedName("provider_status") val provider_status : Int,
    @SerializedName("update_status") val update_status : Int,
    @SerializedName("gender") val gender : String,
    @SerializedName("birth_date") val birth_date : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("id") val id : Int
)*/

data class BranchesModel(

    @SerializedName("status") val status: Int,
    @SerializedName("errNum") val errNum: String,
    @SerializedName("msg") val msg: String,
    @SerializedName("branches") val branches: List<BranchItem>
)

data class BranchItem(

    @SerializedName("id") val id: Int,
    @SerializedName("name_ar") val name_ar: String,
    @SerializedName("name_en") val name_en: String,
    @SerializedName("location_url") val location_url: String,
    @SerializedName("employee_quantity") val employee_quantity: Int,
    @SerializedName("space") val space: String,
    @SerializedName("image") val image: String,
    @SerializedName("created_by") val created_by: Int,
    @SerializedName("deleted_at") val deleted_at: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String,
)

data class UpdateUserBranchModel(

    @SerializedName("status") val status: Int,
    @SerializedName("errNum") val errNum: String,
    @SerializedName("msg") val msg: String,
    @SerializedName("user") val user: User
)

data class User(

    @SerializedName("id") val id: Int,
    @SerializedName("role") val user_type: String,
    @SerializedName("user_name") val user_name: String,
    @SerializedName("social") val provider_status: Int,
    @SerializedName("user_picture") val image: String,
    @SerializedName("email") val email: String,
//    @SerializedName("phone") val phone: String,

)

data class CategoriesModel(

    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val categories: List<CategoryModel>
)

data class CategoryModel(

    @SerializedName("id") val id: Int,
    @SerializedName("created_by") val created_by: Int,
    @SerializedName("name") val category_name_ar: String,
    @SerializedName("image") val category_image: String,

)

data class ProductsModel(

    @SerializedName("msg") val status: MessageModel,




    @SerializedName("data") val items: List<ProductItems>,


)
data class CategoriesItem(

    @SerializedName("id") val category_id: Int,
    @SerializedName("name") val category_name_ar: String,
    @SerializedName("image") val category_image: String,
    @SerializedName("category_logo") val category_logo: String,
    @SerializedName("status") val status: Int,
    @SerializedName("products") val items: List<ProductItems>,
    var isSelected : Boolean = false
)


@Parcelize
data class ProductItems(

    @SerializedName("pid") val id: Int,
    @SerializedName("vid") val category_id: Int?,
    @SerializedName("title") val item_name_ar: String?,
    @SerializedName("price") val item_price: Double?,

    @SerializedName("images") val item_image: String?,
    @SerializedName("fav") var favorite_status: Int?,
    @SerializedName("always_in_stock") var always_in_stock: Int?,
    @SerializedName("stock") var stock: Double?,
    var click_add_to_cart: Boolean?,
    var isSelected: Boolean = false


) : Parcelable
data class RelatedProductModel(

    @SerializedName("msg") val status: MessageModel,




    @SerializedName("data") val items: RelatedProductItemss,


    )
@Parcelize
data class RelatedProductItemss(
    @SerializedName("body") val body: String?,
    @SerializedName("pid") val id: Int,
    @SerializedName("vid") val category_id: Int?,
    @SerializedName("title") val item_name_ar: String?,
    @SerializedName("price") val item_price: Double?,
    @SerializedName("images") val images : List<ProudectImageModel>,
    @SerializedName("related") val related: List<ProductItems>,
    @SerializedName("fav") var favorite_status: Int?,
    @SerializedName("always_in_stock") var always_in_stock: Int?,
    @SerializedName("stock") var stock: Double?,



    ):Parcelable
@Parcelize
data class ProudectImageModel(
    @SerializedName("url") val proudectdetails_image: String?,

    ):Parcelable

data class CategoryProductModel(

    @SerializedName("msg") val status: MessageModel,

    @SerializedName("data") val categories_with_items: List<CategoriesItems?>
)

data class CategoriesItems(

    @SerializedName("id") val category_id: Int,
    @SerializedName("name") val category_name_ar: String,
    @SerializedName("image") val category_image: String,
    @SerializedName("category_logo") val category_logo: String,
    @SerializedName("status") val status: Int,
    @SerializedName("products") val items: List<ProductItems>,
    var isSelected:Boolean = false
)

data class Message(
    @SerializedName("msg") val status: MessageModel,

)
data class AboutUsData(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val data: AboutUsModel,

    )
data class AboutUsModel(
    @SerializedName("body") val body: String,


    )
data class MessageModel(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val msg: String,
)
data class MessageModelForget(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val msg: String,
    @SerializedName("data") val data: ForgetUid? = null,

    )

data class ForgetUid(
    @SerializedName("uid") val uid: Int,

    )



data class FavouriteMessageModel(
    @SerializedName("msg") val status: MessageModel,
    @SerializedName("errNum") val errNum: String,
//    @SerializedName("msg") val msg: String,
)

data class UserCartModel(

    @SerializedName("msg") val status: MessageModel,

    @SerializedName("data") val user_cart:  UserCartData
)


data class  UserCartData(
    @SerializedName("order_id") val order_id: String,
    @SerializedName("total_order_price") val total_order_price: String,
    @SerializedName("items") val cart_items: List<CartItemModel>,
    @SerializedName("coupons") val coupons: List<Coupons>,


    @SerializedName("shiping_fees") val shiping_fees: String



)
@Parcelize
data class Coupons(
    @SerializedName("coupon_id") val coupon_id: String,
    @SerializedName("code") val code: String,

):Parcelable
@Parcelize
data class CartItemModel(



    @SerializedName("order_id") val item_id: String,
    @SerializedName("title") val item_name_ar: String,
    @SerializedName("image") val item_image: String,
    @SerializedName("unit_price") val unit_price: String,
    @SerializedName("quantity") val cart_quantity: Int,
    @SerializedName("total_unit_price") val total_price: String,
    @SerializedName("order_item_id") val order_item_id: String,

) : Parcelable

data class UserLocationsModel(

    @SerializedName("msg") val status: MessageModel,

    @SerializedName("data") val user_locations: List<LocationModel>
)


data class LocationModel(

    @SerializedName("profile_id") val user_id: Int,
    @SerializedName("is_default") val is_default: Int,
    @SerializedName("FullAddress  ") val fullAddress: FullAddress

)


data class FullAddress(
    @SerializedName("given_name") val given_name: String,
    @SerializedName("family_name") val family_name: String,
    @SerializedName("locality") val locality: String,
    @SerializedName("address_line1") val address_line1: String,
    @SerializedName("country_code") val country_code: String


)
{
    override fun toString(): String {
        return locality
    }
}
data class InvoicesModel(

    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val invoices: List<Invoices>,

    )



data class Invoices(

    @SerializedName("order_id") val order_id: Int,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("item_count") val item_count: Int,
    @SerializedName("total_order_price") val total_price: Double,
    @SerializedName("state") val state: String,
    @SerializedName("date") val inVodate: String,
    @SerializedName("city") val city: String,
    @SerializedName("area") val area: String,
    @SerializedName("street") val street: String,
    @SerializedName("building_number") val building_number: String,
    @SerializedName("apartment_number") val apartment_number: String,
    @SerializedName("items") val sale_operations: List<SalesOperations>,
//    @SerializedName("billing_information") val billing_information: BillingInformation
)


data class DetailsInvoicesModel(

    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val invoices:DetailsInvoicesCartModel,

    )


data class DetailsInvoicesCartModel(
    @SerializedName("order_id") val order_id: String,
    @SerializedName("total_order_price") val total_order_price: String,
    @SerializedName("state") val state:String,
    @SerializedName("first_name") val first_name:String,
    @SerializedName("family_name") val family_name:String,
    @SerializedName("location") val location:String? = "-",
    @SerializedName("phone_number") val phone_number:String? = "-",

    @SerializedName("items") val items: List<SalesOperations>,

    @SerializedName("date") val date: String,
    @SerializedName("shipping_fees") val shipping_fees: Shipping_Fees? = null


)
data class Shipping_Fees(
    @SerializedName("amount") val amount:String,

    )
data class BillingInformation(
    @SerializedName("profile_id") val profile_id: String,
    @SerializedName("is_default") val is_default: String,
    @SerializedName("city") val city: String,
    @SerializedName("area") val area: String,
    @SerializedName("streetnum") val streetnum: String,
    @SerializedName("buildNum") val buildNum: String,
    @SerializedName("depnum") val depnum: String,
    @SerializedName("lang") val lang: String,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("given_name") val given_name: String,
    @SerializedName("family_name") val family_name: String,
    @SerializedName("locality") val locality: String,
    @SerializedName("address_line1") val address_line1: String,
    @SerializedName("country_code") val country_code: String


    )

@Parcelize
data class SalesOperations(

    @SerializedName("order_item_id") val order_item_id: String,
    @SerializedName("quantity") val item_quantity: Int,
    @SerializedName("image") val item_image: String,
    @SerializedName("unit_price") val unit_price: Double,
    @SerializedName("total_unit_price") val total_unit_price: Double,
    @SerializedName("title") val title: String

) : Parcelable

data class FavouriteModel(

    @SerializedName("msg") val status: MessageModel,
    @SerializedName("data") val user_favorites: List<ProductItems>
)

/*data class UserFavorites(

    @SerializedName("id") val id: Int,
    @SerializedName("created_by") val created_by: Int,
    @SerializedName("category_id") val category_id: Int,
    @SerializedName("item_name_ar") val item_name_ar: String,
    @SerializedName("item_name_en") val item_name_en: String,
    @SerializedName("item_on_sale_price") val item_on_sale_price: Double,
    @SerializedName("item_price") val item_price: Double,
    @SerializedName("on_sale") val on_sale: Int,
    @SerializedName("unit_type") val unit_type: Int,
    @SerializedName("item_image") val item_image: String,
    @SerializedName("virtual_image") val virtual_image: String,
    @SerializedName("tax") val tax: Int,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("description_ar") val description_ar: String,
    @SerializedName("description_en") val description_en: String,
    @SerializedName("deleted_at") val deleted_at: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("favorite_id") val favorite_id: Int
)*/

data class UserResponse(

    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("link") val link: String,
    @SerializedName("email") val email: String,
    @SerializedName("birthday") val birthday: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("picture") val picture: FacebookPicture
)

data class FacebookPicture(

    @SerializedName("data") val data: FacebookImageData
)

data class FacebookImageData(

    @SerializedName("height") val height: Int,
    @SerializedName("is_silhouette") val is_silhouette: Boolean,
    @SerializedName("url") val url: String,
    @SerializedName("width") val width: Int
)

data class ProfileModel(

    @SerializedName("msg") val status: MessageModel,

    @SerializedName("data") val userProfile: UserProfile
)
data class CityModel(

    @SerializedName("msg") val status: MessageModel,

    @SerializedName("data") val cityItem: List<CityItem>
)

data class CityItem(
    @SerializedName("name") val name: String,
    @SerializedName("tid") val tid: String
)
data class UserProfile(

    @SerializedName("id") val id: Int,
    @SerializedName("user_name") val user_name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("role") val user_type: String?,
    @SerializedName("user_picture") val image: String?,

)



data class PromoCodeModel(

    @SerializedName("msg") val msg: MessageModel

)

data class PromoDataModel(

    @SerializedName("total_price") val total_price: Double,
    @SerializedName("tax") val tax: Int,
    @SerializedName("promo_amount") val promo_amount: Double,
    @SerializedName("total_price_tax") val total_price_tax: Double,
    @SerializedName("promo_type") val promo_type: String
)

data class ProductSearchModel(
    @SerializedName("msg") val status: MessageModel,
//    @SerializedName("errNum") val errNum: String,
//    @SerializedName("sub_total") val sub_total: String,
//    @SerializedName("msg") val msg: String,
//    @SerializedName("delivery_fees") val delivery_fees: String,
    @SerializedName("data") val search_items: List<ProductItems>
)