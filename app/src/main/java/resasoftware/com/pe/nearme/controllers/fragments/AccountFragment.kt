package resasoftware.com.pe.nearme.controllers.fragments


import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_account.*
import resasoftware.com.pe.nearme.R
import resasoftware.com.pe.nearme.controllers.activities.EditAccountActivity
import resasoftware.com.pe.nearme.models.Preferences
import resasoftware.com.pe.nearme.models.User


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AccountFragment : Fragment() {
    var image: Int = 1
    var user: User = User()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onResume() {
        super.onResume()
        buttonEdit.setOnClickListener {
            val intent = Intent(this.activity, EditAccountActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }

        activity?.intent?.extras?.apply {
            user = getSerializable("user") as User
            text_fullname.text = user.fullname
            text_email.text = user.email
            text_password.text = user.password
            text_gender.text = user.gender
            text_f_username.text = user.username
        }
        image_visibilitypassword.setOnClickListener {
            image*=-1

            if(image == -1){
                image_visibilitypassword.setImageResource(R.drawable.ic_visibility_off_black_24dp)
                text_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            }else if(image == 1){
                image_visibilitypassword.setImageResource(R.drawable.ic_visibility_black_24dp)
                text_password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
    }
}
