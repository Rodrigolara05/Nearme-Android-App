package resasoftware.com.pe.nearme.controllers.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_edit_account.*
import resasoftware.com.pe.nearme.R
import resasoftware.com.pe.nearme.models.Category
import resasoftware.com.pe.nearme.models.Preferences
import resasoftware.com.pe.nearme.models.User
import resasoftware.com.pe.nearme.network.NearmeApi
import resasoftware.com.pe.nearme.network.Notifications

class EditAccountActivity : AppCompatActivity() {

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

    var user: User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account)

        radioButton_male.isChecked = true
        passwordError = getString(R.string.error_label_password)
        emailError = getString(R.string.error_label_email)
        nameError = getString(R.string.error_label_full_name)
        usernameError = getString(R.string.error_label_username)

        passwordErrorSize = getString(R.string.error_label_password_size)
        nameErrorSize = getString(R.string.error_label_full_name_size)
        usernameErrorSize = getString(R.string.error_label_user_name_size)

        intent.extras?.apply {
            user = getSerializable("user") as User
            text_edit_fullname.setText(user.fullname)
            text_edit_email.setText(user.email)
            text_edit_password.setText(user.password)
            text_edit_username.setText(user.username)

            when (user.gender) {
                "Male" -> radioButton_male.isChecked = true
                "Female" -> radioButton_female.isChecked = true
                "Masculino" -> radioButton_male.isChecked = true
                "Femenino" -> radioButton_female.isChecked = true
            }

            select_edit_gender.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    R.id.radioButton_male -> user.gender = getString(R.string.edit_account_label_male)
                    R.id.radioButton_female -> user.gender = getString(R.string.edit_account_label_female)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()

        buttonSave.setOnClickListener {
            if(ValidateInputs()){
                user.email= email
                user.fullname = name
                user.password = password
                user.username = username


                NearmeApi.putUser(
                    user,
                    {
                        Notifications.toastNotifications(getString(R.string.notifications_success), application, Toast.LENGTH_SHORT, Gravity.BOTTOM )
                    }, {
                        Notifications.toastNotifications(getString(R.string.notifications_fail), this, Toast.LENGTH_SHORT, Gravity.BOTTOM )
                    },getString(R.string.nearme_api_key)
                )
            }
        }

        image_edit_visibilitypassword.setOnClickListener {
            image*=-1

            if(image == -1){
                image_edit_visibilitypassword.setImageResource(R.drawable.ic_visibility_off_black_24dp)
                text_edit_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            }else if(image == 1){
                image_edit_visibilitypassword.setImageResource(R.drawable.ic_visibility_black_24dp)
                text_edit_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }

    private fun ValidateInputs(): Boolean{
        email = text_edit_email.text.toString()
        password = text_edit_password.text.toString()
        name = text_edit_fullname.text.toString()
        username = text_edit_username.text.toString()

        email.apply {
            if (!validateEmail(this)){
                text_edit_error_email.text = emailError
                validEmail = false
            }else{
                text_edit_error_email.text = ""
                validEmail = true
            }
        }

        password.apply {
            if(this.isBlank()) {
                text_edit_error_password.text = passwordError
                validPassword = false
            }else{
                if(password.length < 7){
                    text_edit_error_password.text = passwordErrorSize
                    validPassword = false
                }else{
                    text_edit_error_password.text = ""
                    validPassword = true
                }
            }
        }

        name.apply {
            if(this.isBlank()) {
                text_edit_error_fullname.text = nameError
                validName = false
            }else{
                if(name.length < 4){
                    text_edit_error_fullname.text = nameErrorSize
                    validName = false
                }else {
                    text_edit_error_fullname.text = ""
                    validName = true
                }
            }
        }

        username.apply {
            if(this.isBlank()) {
                text_edit_error_username.text = usernameError
                validUserName = false
            }else{
                if(username.length < 4){
                    text_edit_error_username.text = usernameErrorSize
                    validUserName = false
                }else {
                    text_edit_error_username.text = ""
                    validUserName = true
                }
            }
        }

        if(validEmail and validPassword and validName and validUserName){
            return true
        }
        return false
    }

    private fun validateEmail(value: String): Boolean{
        return pattern.matcher(value).matches()
    }
}
