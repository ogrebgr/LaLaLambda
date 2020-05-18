package ezlambda.simple


data class SimpleResponse(val statusCode: Int, val headers: Map<String, String>, val body: String?)

class SimpleResponseBuilder constructor(private val statusCode: Int) {
    private var _body: String? = null
    private val _headers: MutableMap<String, String> = mutableMapOf()


    fun build(): SimpleResponse {
        return SimpleResponse(statusCode, _headers, _body)
    }

    fun addHeader(header: String, value: String): SimpleResponseBuilder {
        if (_headers.containsKey(header)) {
            throw IllegalStateException("Header $header already added")
        }

        _headers.put(header, value)

        return this
    }

    fun body(body: String): SimpleResponseBuilder {
        _body = body

        return this
    }
}