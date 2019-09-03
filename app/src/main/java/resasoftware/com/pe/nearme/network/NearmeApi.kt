package resasoftware.com.pe.nearme.network

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONObject
import resasoftware.com.pe.nearme.models.*


class NearmeApi {
    companion object {
        private val BASE_URL = "https://nearme-api-rest.herokuapp.com"
        private val categoryURL = "$BASE_URL/categories"
        private val commentURL = "$BASE_URL/comments"
        private val enterpriseURL = "$BASE_URL/enterprises"
        private val typeUserURL = "$BASE_URL/type_users"
        private val userURL = "$BASE_URL/users"
        private val loginURL = "$userURL/search"
        private val TAG = "NearmeApi"

        fun getEnterprises(responseHandler: (ArrayList<Enterprise>?) -> Unit,
                     errorHandler: (ANError) -> Unit, key: String) {
            get(enterpriseURL,responseHandler,errorHandler, key)
        }

        fun getEnterprise(id: Int, responseHandler: (Enterprise?) -> Unit,
                           errorHandler: (ANError) -> Unit, key: String) {
            val extra: String = id.toString()
            var URL = "$enterpriseURL/$extra"
            getWithID(URL,responseHandler,errorHandler, key)
        }

        fun getComments(ID: Int, responseHandler: (ArrayList<Comment>?) -> Unit,
                     errorHandler: (ANError) -> Unit, key: String) {
            var id = ID.toString()
            var url = "$commentURL/search/$id"
            get(url,responseHandler,errorHandler, key)
        }

        fun getCategories(responseHandler: (ArrayList<Category>?) -> Unit,
                        errorHandler: (ANError) -> Unit, key: String) {
            get(categoryURL,responseHandler,errorHandler, key)
        }

        fun getUsers(responseHandler: (ArrayList<User>?) -> Unit,
        errorHandler: (ANError) -> Unit, key: String) {
            get(userURL,responseHandler,errorHandler, key)
        }

        fun getUserType(responseHandler: (ArrayList<Type_User>?) -> Unit,
                           errorHandler: (ANError) -> Unit, key: String) {
            get(typeUserURL, responseHandler, errorHandler, key)
        }

        fun loginUser(username:String, password: String, responseHandler: (User?) -> Unit,
                        errorHandler: (ANError) -> Unit, key: String) {
            val url = "$loginURL/$username/$password"
            login(url, responseHandler, errorHandler, key)
        }

        fun postUser(user: User, responseHandler: (JSONObject?) -> Unit,
                     errorHandler: (ANError?) -> Unit, key: String) {
            val json: JSONObject = user.converToJson()
            post(json, userURL, responseHandler, errorHandler, key)

        }

        fun putUser(user: User, responseHandler: (JSONObject?) -> Unit,
                     errorHandler: (ANError?) -> Unit, key: String) {
            val json: JSONObject = user.converToJson()
            put(userURL, json, responseHandler, errorHandler, key)

        }

        fun postComment(comment: Comment, responseHandler: (JSONObject?) -> Unit,
                     errorHandler: (ANError?) -> Unit, key: String) {
            val json: JSONObject = comment.converToJson()
            post(json, commentURL, responseHandler, errorHandler, key)

        }

        private inline fun login(url: String, crossinline responseHandler: (User?) -> Unit,
                                           crossinline errorHandler: (ANError) -> Unit, key: String)
        {
            AndroidNetworking.get(url)
                .addHeaders("Authorization", "Bearer $key")
                .setTag(TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(User::class.java,
                    object : ParsedRequestListener<User> {
                        override fun onResponse(response: User?) {
                            response?.apply {
                                responseHandler(response)
                            }

                        }

                        override fun onError(anError: ANError?) {
                            anError?.apply {
                                Log.d(TAG, "Error $errorCode: $errorBody $localizedMessage")
                                errorHandler(this)
                            }
                        }
                    }
                )
        }

        // GET - All
        private inline fun <reified T> get(url: String, crossinline responseHandler: (ArrayList<T>?) -> Unit,
            crossinline errorHandler: (ANError) -> Unit, key: String)
        {
            AndroidNetworking.get(url)
                .addHeaders("Authorization", "Bearer $key")
                .setTag(TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(T::class.java,
                    object : ParsedRequestListener<ArrayList<T>> {
                        override fun onResponse(response: ArrayList<T>?) {
                            response?.apply {
                                responseHandler(response)
                            }

                        }

                        override fun onError(anError: ANError?) {
                            anError?.apply {
                                Log.d(TAG, "Error $errorCode: $errorBody $localizedMessage")
                                errorHandler(this)
                            }
                        }

                    }
                )
        }

        // GET with ID
        private inline fun <reified T> getWithID(url: String, crossinline responseHandler: (T?) -> Unit,
                                           crossinline errorHandler: (ANError) -> Unit, key: String)
        {
            AndroidNetworking.get(url)
                .addHeaders("Authorization", "Bearer $key")
                .setTag(TAG)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(T::class.java,
                    object : ParsedRequestListener<T> {
                        override fun onResponse(response: T?) {
                            response?.apply {
                                responseHandler(response)
                            }

                        }

                        override fun onError(anError: ANError?) {
                            anError?.apply {
                                Log.d(TAG, "Error $errorCode: $errorBody $localizedMessage")
                                errorHandler(this)
                            }
                        }

                    }
                )
        }

        // POST - All
        private inline fun  post(obj: JSONObject, url: String, crossinline responseHandler: (JSONObject?) -> Unit,
                                 crossinline errorHandler: (ANError?) -> Unit, key: String) {
            AndroidNetworking.post(url)
                .addHeaders("Authorization", "Bearer $key")
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(obj)
                .setTag(TAG)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        responseHandler(response)
                    }

                    override fun onError(anError: ANError?) {
                        errorHandler(anError)
                    }
                })
        }

        // PUT - All
        private inline fun put(url: String, obj: JSONObject, crossinline responseHandler: (JSONObject?) -> Unit,
                                           crossinline errorHandler: (ANError?) -> Unit, key: String)
        {
            AndroidNetworking.put(url)
                .addHeaders("Authorization", "Bearer $key")
                .addHeaders("Content-Type", "application/json")
                .addJSONObjectBody(obj)
                .setTag(TAG)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        responseHandler(response)
                    }

                    override fun onError(anError: ANError?) {
                        errorHandler(anError)
                    }
                })
        }
    }
}


