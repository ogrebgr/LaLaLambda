package ezlambda.misc

enum class HttpMethod(val literal: String) {
    GET("get"),
    POST("post"),
    DELETE("delete"),
    PUT("put");

    companion object {
        fun fromString(str: String): HttpMethod {
            when (str.toLowerCase()) {
                GET.literal -> return GET
                POST.literal -> return POST
                DELETE.literal -> return DELETE
                PUT.literal -> return PUT
                else -> throw IllegalArgumentException("Invalid argument: $str")
            }
        }
    }
}