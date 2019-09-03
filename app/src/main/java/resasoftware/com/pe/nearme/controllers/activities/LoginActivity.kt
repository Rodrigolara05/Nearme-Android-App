package resasoftware.com.pe.nearme.controllers.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_login.*
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.widget.Toast
import resasoftware.com.pe.nearme.R
import resasoftware.com.pe.nearme.network.NearmeApi
import resasoftware.com.pe.nearme.network.NearmeApi.Companion.getUsers
import resasoftware.com.pe.nearme.network.Notifications


class LoginActivity : AppCompatActivity() {
    private var username: String = ""
    private var password: String = ""

    private var passwordError: String = ""
    private var usernameError: String = ""
    private var emailError: String = ""
    private var generalError: String = ""

    private var usernamevalid: Boolean = false
    private var passwordvalid: Boolean = false

    private val pattern = Patterns.EMAIL_ADDRESS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        image_background.apply {
            setImageUrl(getString(R.string.url_loginbackground))
            Log.d("url", R.string.url_loginbackground.toString())
        }
        forgotpassword.visibility = View.INVISIBLE
        loadSession()
        passwordError = getString(R.string.error_label_password)
        usernameError = getString(R.string.error_label_username)
        emailError = getString(R.string.error_label_email)
        generalError = getString(R.string.error_label_general)
        check_rememberme.isChecked = true
    }


    override fun onResume() {
        super.onResume()

        button_signin.setOnClickListener {
            username = input_username.text.toString()
            password = input_password.text.toString()



            username.apply {
                if(this.isBlank()) {
                    text_username_error.text = usernameError
                    usernamevalid = false
                }else{
                    text_username_error.text = ""
                    usernamevalid = true
                }
            }

            password.apply {
                if(this.isBlank()) {
                    text_passworderror.text = passwordError
                    passwordvalid = false
                }else{
                    text_passworderror.text = ""
                    passwordvalid = true
                }
            }

            if(passwordvalid and usernamevalid){
                authorization(username, password)
            }
        }

        text_forgotyourpassword.setOnClickListener {
            forgotpassword.visibility = View.VISIBLE
        }

        forgot_image_close.setOnClickListener {
            forgotpassword.visibility = View.INVISIBLE
        }

        forgot_image_send.setOnClickListener {
            val e: String = forgot_input_email.text.toString()

            e.apply {
                if (!validateEmail(this)){
                    forgot_text_emailerror.text = emailError
                }else{
                    forgot_text_emailerror.text = ""
                    sendEmail(e)
                }
            }
        }

        create_account.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateEmail(value: String): Boolean{
        return pattern.matcher(value).matches()
    }

    private fun authorization(username: String, password: String){
        val intent = Intent(this, MainActivity::class.java)
        NearmeApi.loginUser(
            username,
            password,
            {
                it?.apply {
                    text_generalerror.text = ""
                    if(check_rememberme.isChecked ){
                        saveSession()
                    }
                    finish()
                    intent.putExtra("user", it)
                    startActivity(intent)
                }
            }, {
                if(it.errorCode == 404){
                    text_generalerror.text = generalError
                }else {
                    Log.d("NEARMETESTNewsApi", "${it.errorBody} ${it.localizedMessage}")
                    Notifications.toastNotifications(
                        getString(R.string.notifications_fail),
                        this,
                        Toast.LENGTH_SHORT,
                        Gravity.BOTTOM
                    )
                }
            },getString(R.string.nearme_api_key)
            )
    }

    private fun sendEmail(email: String){
        //la funcionalidad de recuperar contraseña
    }

    private fun loadSession(){
        val preference: SharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)

        val email: String = preference.getString("username","")
        val password: String = preference.getString("password","")

        input_username.setText(email)
        input_password.setText(password)
    }

    private fun saveSession(){
        val preference: SharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)

        val username: String = input_username.text.toString()
        val password: String = input_password.text.toString()

        val editor: SharedPreferences.Editor = preference.edit()

        editor.putString("username",username)
        editor.putString("password",password)

        editor.commit()
    }

}
