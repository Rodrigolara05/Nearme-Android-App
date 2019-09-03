package resasoftware.com.pe.nearme.models

import org.json.JSONObject
import java.io.Serializable

data class Category(
    val id: Int,
    val name: String,
    val description: String
): Serializable {
    constructor() : this(
        0,
        "",
        ""
    )

    fun converToJson(): JSONObject {
        val jsonCategory = JSONObject()

        jsonCategory.put("id", id)
        jsonCategory.put("name", name)
        jsonCategory.put("description", description)

        return jsonCategory
    }

}