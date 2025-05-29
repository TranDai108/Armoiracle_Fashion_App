import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    val iduser: String,
    val name: String,
    val username: String,
    val email: String,
    val phone: String,
    val gender: Int,
    val avatar: String,
    val birthday: String,
    val zodiac: Int,
    val weight: Int,
    val height: Int,
    val theme: Int
): Parcelable