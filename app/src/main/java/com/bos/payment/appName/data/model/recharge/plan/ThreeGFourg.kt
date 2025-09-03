import com.google.gson.annotations.SerializedName

data class ThreeGFourg(
    @SerializedName("rs") var rs: String? = null,
    @SerializedName("desc") var desc: String? = null,
    @SerializedName("validity") var validity: String? = null,
    @SerializedName("last_update") var lastUpdate: String? = null
)