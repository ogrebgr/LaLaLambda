package lalalambda.simple

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent
import lalalambda.misc.HttpMethod
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

class SimpleRequestContextAws(private val awsReq: APIGatewayV2ProxyRequestEvent, private val routePath: String) :
    SimpleRequestContext {
    companion object {
        private const val HEADER_CONTENT_TYPE = "Content-Type"
        private const val CONTENT_TYPE_FORM_ENCODED = "application/x-www-form-urlencoded"
    }

    private val getParams = awsReq.queryStringParameters
    private var postParams = mapOf<String, String>()
    private var pathInfoParams = mutableListOf<String>()

    private val headers = awsReq.headers
    private val mvHeaders = awsReq.multiValueHeaders
    private var arePostParametersExtracted = false
    private var arePiParametersExtracted = false

    override fun getFromGet(parameterName: String): String? {
        return getParams[parameterName]
    }

    override fun getFromQuery(parameterName: String): String? {
        return getFromGet(parameterName)
    }

    override fun getFromPost(parameterName: String): String? {
        if (!arePostParametersExtracted) {
            try {
                extractPostParameters()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }

        return postParams[parameterName]
    }

    override fun getPathInfoParameters(): List<String> {
        if (!arePiParametersExtracted) {
            extractPiParameters()
        }
        return pathInfoParams
    }

    override fun getPi(): List<String> {
        return getPathInfoParameters()
    }

    override fun getRoutePath(): String {
        return routePath
    }

    override fun getPathInfoString(): String? {
        return awsReq.path.substring(routePath.length)
    }

    override fun optFromGet(parameterName: String, defaultValue: String): String? {
        var ret = getFromGet(parameterName)
        if (ret == null) {
            ret = defaultValue
        }

        return ret
    }

    override fun optFromPost(parameterName: String, defaultValue: String): String? {
        var ret = getFromPost(parameterName)
        if (ret == null) {
            ret = defaultValue
        }

        return ret
    }

    override fun getHeader(headerName: String): String? {
        return headers[headerName]
    }

    override fun getHeaderValue(headerName: String): String? {
        return getHeader(headerName)
    }

    override fun getHeaderValues(headerName: String): List<String> {
        return mvHeaders.get(headerName) ?: emptyList()
    }

    @Throws(IllegalArgumentException::class)
    override fun getMethod(): HttpMethod {
        return HttpMethod.fromString(awsReq.httpMethod)
    }

    override fun getHttpMethod(): HttpMethod {
        return getMethod()
    }

    override fun isMethod(method: HttpMethod): Boolean {
        return getMethod() == method
    }

    @Throws(IOException::class)
    private fun extractPostParameters() {
        val body: String = awsReq.body
        if (getMethod() == HttpMethod.POST) {
            val contentType = getHeader(HEADER_CONTENT_TYPE)
            if (contentType != null) {
                if (contentType.toLowerCase().contains(CONTENT_TYPE_FORM_ENCODED.toLowerCase())) {
                    postParams = extractParameters(body)
                }
            }
        }
        arePostParametersExtracted = true
    }


    private fun extractParameters(queryString: String): Map<String, String> {
        val ret = mutableMapOf<String, String>()

        if (queryString.isNotEmpty()) {
            try {
                val split = queryString.split("&").toTypedArray()
                for (aSplit in split) {
                    val keyValue = aSplit.split("=").toTypedArray()
                    if (keyValue.size == 1) {
                        ret[URLDecoder.decode(keyValue[0], "UTF-8")] = ""
                    } else {
                        ret[URLDecoder.decode(keyValue[0], "UTF-8")] = URLDecoder.decode(keyValue[1], "UTF-8")
                    }
                }
            } catch (e: UnsupportedEncodingException) {
                throw RuntimeException(e)
            }
        }

        return ret
    }


    private fun extractPiParameters() {
        //protection against directory traversal. Jetty never sends '..' here but other containers may do so...
        check(!pathInfoParams.contains("..")) { "Path info contains '..'" }
        val piRaw: Array<String> = awsReq.path.split("/").toTypedArray()
        for (s in piRaw) {
            if (s.trim { it <= ' ' }.length > 0) {
                pathInfoParams.add(s)
            }
        }
        arePiParametersExtracted = true
    }
}