package resasoftware.com.pe.nearme.models

import org.json.JSONObject
import java.io.Serializable

data class Type_User(
    var id: Int,
    var description: String,
    var name: String
): Serializable {
    constructor() : this(
        0,
		"",
		""
    )
    fun converToJson(): JSONObject {
        val jsonType = JSONObject()

        jsonType.put("id", id)
        jsonType.put("description", description)
        jsonType.put("name", name)

        return jsonType
    }
}