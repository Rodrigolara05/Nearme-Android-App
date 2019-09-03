package resasoftware.com.pe.nearme.models

import org.json.JSONObject
import java.io.Serializable

data class Comment(
    val id: Int,
    var enterpriseId: Enterprise,
    var userId: User,
    var comment: String
): Serializable {
    constructor() : this(
        0,
        Enterprise(),
        User(),
        ""
    )

    fun converToJson(): JSONObject {
        val jsonComment = JSONObject()
        var jsonUser = userId.converToJson()
        var jsonEnterprise = enterpriseId.converToJson()

        jsonComment.put("id", id)
        jsonComment.put("enterpriseId", jsonEnterprise)
        jsonComment.put("userId", jsonUser)
        jsonComment.put("comment", comment)

        return jsonComment
    }
}