package resasoftware.com.pe.nearme.models

import org.json.JSONObject
import java.io.Serializable

data class Enterprise(
    val id: Int,
    var categoryId: Category,
    val description: String,
    val image: String,
    val location: String,
    val name: String,
    val longitude: String,
    val latitude: String,
	val star: String
): Serializable {
    constructor() : this(
        0,
        Category(),
        "",
        "",
		"",
        "",
        "",
        "",
		""
    )

    fun converToJson(): JSONObject {
        val jsonEnterprise = JSONObject()
        var jsonType = categoryId.converToJson()

        jsonEnterprise.put("id", id)
        jsonEnterprise.put("categoryId", jsonType)
        jsonEnterprise.put("description", description)
        jsonEnterprise.put("image", image)
        jsonEnterprise.put("location", location)
        jsonEnterprise.put("longitude", longitude)
        jsonEnterprise.put("name", name)
        jsonEnterprise.put("latitude", latitude)
        jsonEnterprise.put("star", star)

        return jsonEnterprise
    }
}