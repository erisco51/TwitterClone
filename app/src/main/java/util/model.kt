package util

data class User(
val email: String? ="",
val username: String? ="",
val imageurl: String? ="",
val followHashtags: ArrayList<String>? = arrayListOf(),
val followUsers:ArrayList<String>? = arrayListOf()
)