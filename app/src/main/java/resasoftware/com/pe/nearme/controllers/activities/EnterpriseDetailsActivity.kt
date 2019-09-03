package resasoftware.com.pe.nearme.controllers.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_enterprise_details.*
import resasoftware.com.pe.nearme.Adapters.CommentAdapter
import resasoftware.com.pe.nearme.R
import resasoftware.com.pe.nearme.models.Comment
import resasoftware.com.pe.nearme.models.Enterprise
import resasoftware.com.pe.nearme.models.User
import resasoftware.com.pe.nearme.network.NearmeApi

class EnterpriseDetailsActivity : AppCompatActivity() {

    var enterprise: Enterprise = Enterprise()
    var user: User = User()
    var rating: ArrayList<String> = ArrayList<String>()
    var sumStars: Int = 0
    var comments: List<Comment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enterprise_details)
    }

    override fun onResume() {
        super.onResume()

        var commentsAdapter = CommentAdapter(comments)

        intent.extras?.apply{
            var id: Int = getSerializable("enterpriceID") as Int
            user = getSerializable("user") as User

            NearmeApi.getEnterprise(
                id,
                {
                    enterprise = it as Enterprise

                    textEnterpriseName.text = enterprise.name
                    textEnterpriseDirection.text = enterprise.location
                    imageEnterpriseImage.setImageUrl(enterprise.image)
                    ratingFields()

                    commentButton.setOnClickListener {
                        val intent = Intent(this@EnterpriseDetailsActivity, CommentActivity::class.java)
                        intent.putExtra("enterprise", enterprise)
                        intent.putExtra("user", user)
                        startActivity(intent)
                    }

                    NearmeApi.getComments(
                        enterprise.id,
                        {
                            it?.apply {
                                commentsAdapter.comments = this
                                commentsAdapter.notifyDataSetChanged()
                            }
                        },{
                            commentsAdapter.comments = comments
                            commentsAdapter.notifyDataSetChanged()
                        },
                        getString(R.string.nearme_api_key)
                    )

                }, {
                    Log.d("Nearme Api Error", it.message)
                },
                getString(R.string.nearme_api_key)
            )

            commentRecyclerView.apply {
                adapter = commentsAdapter
                layoutManager = LinearLayoutManager(this.context)
            }
        }
    }

    private fun ratingFields(){
        rating = enterprise.star.split("/") as ArrayList<String>

        var a = rating[0].toInt()
        var b = rating[1].toInt()
        var c = rating[2].toInt()
        var d = rating[3].toInt()
        var e = rating[4].toInt()
        sumStars = a + b + c + d + e
        if(sumStars==0){sumStars=1}
        var pa = (a*100)/sumStars
        var pb = (b*100)/sumStars
        var pc = (c*100)/sumStars
        var pd = (d*100)/sumStars
        var pe = (e*100)/sumStars
        progressBar1Stars.progress = pa
        progressBar2Stars.progress = pb
        progressBar3Stars.progress = pc
        progressBar4Stars.progress = pd
        progressBar5Stars.progress = pe

        textAverageStarsEnterprise.text = (((a*1) + (b*2) + (c*3) + (d*4) + (e*5))/sumStars).toString()
    }
}
