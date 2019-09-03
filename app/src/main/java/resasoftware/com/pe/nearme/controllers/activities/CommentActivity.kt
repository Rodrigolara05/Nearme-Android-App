package resasoftware.com.pe.nearme.controllers.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_comment.*
import resasoftware.com.pe.nearme.R
import resasoftware.com.pe.nearme.models.Comment
import resasoftware.com.pe.nearme.models.Enterprise
import resasoftware.com.pe.nearme.models.User
import resasoftware.com.pe.nearme.network.NearmeApi
import resasoftware.com.pe.nearme.network.Notifications

class CommentActivity : AppCompatActivity() {
    var comment: Comment = Comment()
    var user: User =User()
    var enterprise: Enterprise = Enterprise()
    var errorComent: String = ""
    var errorComentSize: String = ""
    var empty: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
    }

    override fun onResume() {
        super.onResume()
        errorComent = getString(R.string.error_label_comment)
        errorComentSize = getString(R.string.error_label_comment_size)

        intent.extras?.apply {
            enterprise = getSerializable("enterprise") as Enterprise
            user = getSerializable("user") as User
        }
        enterButton.setOnClickListener {
            val _comment: String = textComment.text.toString()

            _comment.apply {
                if(this.isBlank()){
                    errorComment.text = errorComent

                }else{
                    if(this.length < 10){
                        errorComment.text = errorComentSize
                    }else{
                        errorComment.text = empty

                        comment.comment = _comment
                        comment.userId = user
                        comment.enterpriseId = enterprise

                        NearmeApi.postComment(
                            comment,
                            {
                                Notifications.toastNotifications(getString(R.string.notifications_success), this@CommentActivity, Toast.LENGTH_SHORT, Gravity.BOTTOM )
                            },{
                                Notifications.toastNotifications(getString(R.string.notifications_fail), this@CommentActivity, Toast.LENGTH_SHORT, Gravity.BOTTOM )
                            },
                            getString(R.string.nearme_api_key)
                        )
                    }
                }
            }

        }
    }
}
