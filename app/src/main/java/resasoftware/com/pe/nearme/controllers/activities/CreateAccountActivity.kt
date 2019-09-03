package resasoftware.com.pe.nearme.controllers.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_create_account.*
import resasoftware.com.pe.nearme.R
import android.widget.RadioGroup
import android.widget.Toast
import resasoftware.com.pe.nearme.models.User
import resasoftware.com.pe.nearme.network.NearmeApi
import resasoftware.com.pe.nearme.network.Notifications


class CreateAccountActivity : AppCompatActivity() {
    private var email: String = ""
    private var password: String = ""
    private var name: String = ""
    private var username: String = ""

    private val pattern = Patterns.EMAIL_ADDRESS
    private var passwordError:String = ""
    private var emailError:String = ""
    private var nameError:String = ""
    private var usernameError:String = ""

    private var nameErrorSize:String = ""
    private var usernameErrorSize:String = ""
    private var passwordErrorSize:String = ""

    private var validEmail: Boolean = false
    private var validPassword: Boolean = false
    private var validName: Boolean = false
    private var validUserName: Boolean = false

    private var image: Int = 1
    private var user: User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        passwordError = getString(R.string.error_label_password)
        emailError = getString(R.string.error_label_email)
        nameError = getString(R.string.error_label_full_name)
        usernameError = getString(R.string.error_label_username)
        passwordErrorSize = getString(R.string.error_label_password_size)
        nameErrorSize = getString(R.string.error_label_full_name_size)
        usernameErrorSize = getString(R.string.error_label_user_name_size)
        radioButton_male.isChecked = true
        user.gender = getString(R.string.edit_account_label_male)

        image_create_visibilitypassword.setOnClickListener {
            image*=-1

            if(image == -1){
                image_create_visibilitypassword.setImageResource(R.drawable.ic_visibility_off_black_24dp)
                input_create_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            }else if(image == 1){
                image_create_visibilitypassword.setImageResource(R.drawable.ic_visibility_black_24dp)
                input_create_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        buttonSave.setOnClickListener {
            email = input_create_email.text.toString()
            password = input_create_password.text.toString()
            name = input_create_fullname.text.toString()
            username = input_create_username.text.toString()

            email.apply {
                if (!validateEmail(this)){
                    text_create_error_email.text = emailError
                    validEmail = false
                }else{
                    text_create_error_email.text = ""
                    validEmail = true
                }
            }

            password.apply {
                if(this.isBlank()) {
                    text_create_error_password.text = passwordError
                    validPassword = false
                }else{
                    if(password.length < 7){
                        text_create_error_password.text = passwordErrorSize
                        validPassword = false
                    }else{
                        text_create_error_password.text = ""
                        validPassword = true
                    }
                }
            }

            name.apply {
                if(this.isBlank()) {
                    text_create_error_fullname.text = nameError
                    validName = false
                }else{
                    if(name.length < 4){
                        text_create_error_fullname.text = nameErrorSize
                        validName = false
                    }else {
                        text_create_error_fullname.text = ""
                        validName = true
                    }
                }
            }

            username.apply {
                if(this.isBlank()) {
                    text_create_error_username.text = usernameError
                    validUserName = false
                }else{
                    if(username.length < 4){
                        text_create_error_username.text = usernameErrorSize
                        validUserName = false
                    }else {
                        text_create_error_username.text = ""
                        validUserName = true
                    }
                }
            }

            if(validEmail and validPassword and validName and validUserName){
                create()
            }
        }

        select_create_gender.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton_male -> user.gender = getString(R.string.edit_account_label_male)
                R.id.radioButton_female -> user.gender = getString(R.string.edit_account_label_female)
            }
        })
    }

    private fun validateEmail(value: String): Boolean{
        return pattern.matcher(value).matches()
    }

    private fun create(){
        user.id=0
        user.email= email
        user.fullname = name
        user.password = password
        user.username = username

        NearmeApi.getUserType(
            {
                it?.apply {
                    for (item in  it){
                        if(item.name == "Traveler"){
                            user.type_user_id = item
                            break
                        }
                    }
                }
                    if(user.type_user_id.id != 0){
                        Log.d("gender", user.gender)
                        NearmeApi.postUser(
                            user,
                            {
                                Notifications.toastNotifications(getString(R.string.notifications_success), application, Toast.LENGTH_SHORT, Gravity.BOTTOM )
                            }, {
                                Notifications.toastNotifications(getString(R.string.notifications_fail), this, Toast.LENGTH_SHORT, Gravity.BOTTOM )
                            },getString(R.string.nearme_api_key)
                        )
                    }else{
                        Notifications.toastNotifications(getString(R.string.notifications_fail), this, Toast.LENGTH_SHORT, Gravity.BOTTOM )
                    }
                        Log.d("valor de type", user.type_user_id.toString())
            }, {
                Log.d("NewsApi", "${it.errorBody} ${it.localizedMessage}")
                Notifications.toastNotifications(getString(R.string.notifications_fail), this, Toast.LENGTH_SHORT, Gravity.BOTTOM )
            },getString(R.string.nearme_api_key)
            )
    }
}
