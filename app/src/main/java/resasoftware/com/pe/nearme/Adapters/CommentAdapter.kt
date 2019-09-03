package resasoftware.com.pe.nearme.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_commentary.view.*
import resasoftware.com.pe.nearme.R
import resasoftware.com.pe.nearme.models.Comment

class CommentAdapter (var comments: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageProfile = itemView.profileImageView
        val nameText = itemView.NameTextView
        val commentText = itemView.CommentaryTextView
        val image = itemView.profileImageView
        fun bindTo(comment: Comment) {
            nameText.text = comment.userId.username
            commentText.text = comment.comment
            if(comment.userId.gender == "Male" || comment.userId.gender == "Masculino"){
                image.setImageResource(R.drawable.profile_male)
            }else if(comment.userId.gender == "Female" || comment.userId.gender == "Femenino"){
                image.setImageResource(R.drawable.profile_female)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_commentary, parent, false))
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        holder.bindTo(comments[position])
    }

}