package ezlambda.generic

import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent
import ezlambda.misc.HttpMethod

interface LambdaDispatcher<T : GenericLambda<*, *>> :
    RequestHandler<APIGatewayV2ProxyRequestEvent, APIGatewayV2ProxyResponseEvent> {
    fun registerLambda(path: String, method: HttpMethod, lambda: T)
}

abstract class AbstractLambdaDispatcher<T : GenericLambda<*, *>> constructor(private val isPathInfoEnabled: Boolean) :
    LambdaDispatcher<T> {
    private val endpointsGet = mutableMapOf<String, T>()
    private val endpointsPost = mutableMapOf<String, T>()
    private val endpointsPut = mutableMapOf<String, T>()
    private val endpointsDelete = mutableMapOf<String, T>()

    private var maxPathSegments = 0

    @Throws(IllegalStateException::class)
    override fun registerLambda(path: String, method: HttpMethod, lambda: T) {
        when (method) {
            HttpMethod.GET -> register(endpointsGet, path, lambda)
            HttpMethod.POST -> register(endpointsPost, path, lambda)
            HttpMethod.DELETE -> register(endpointsDelete, path, lambda)
            HttpMethod.PUT -> register(endpointsPut, path, lambda)
        }
        maxPathSegments = Math.max(maxPathSegments, countSlashes(path))
    }

    private fun register(map: MutableMap<String, T>, path: String, lambda: T) {
        if (map.containsKey(path)) {
            throw IllegalStateException("Path already registered: $path")
        }
        map.put(path, lambda)
    }

    protected fun match(path: String, method: HttpMethod): MatchedLambda<T>? {
        return when (method) {
            HttpMethod.GET -> match(endpointsGet, path)
            HttpMethod.POST -> match(endpointsPost, path)
            HttpMethod.DELETE -> match(endpointsDelete, path)
            HttpMethod.PUT -> match(endpointsPut, path)
        }
    }


    private fun match(map: Map<String, T>, path: String): MatchedLambda<T>? {
        val pathCanon = if (path.startsWith("/")) {
            path.substring(1)
        } else {
            path
        }
        val tmp = map[pathCanon]
        return if (tmp != null) {
            MatchedLambda(pathCanon, tmp)
        } else {
            if (isPathInfoEnabled) {
                val count: Int = countSlashes(pathCanon)
                // maxPathSegments prevents DDOS attacks with intentionally maliciously composed urls that contain multiple slashes like
                // "/a/a/a/a/b/b/a/d/" in order to slow down the matching (because matching is rather expensive operation)
                if (count in 1..maxPathSegments) {
                    match(map, removeLastPathSegment(pathCanon))
                } else {
                    null
                }
            } else {
                return null
            }
        }
    }

    private fun countSlashes(str: String): Int {
        return str.filter { it == '/' }.count()
    }

    private fun removeLastPathSegment(path: String): String {
        return if (path.endsWith("/")) {
            path.substring(0, path.lastIndexOf('/'))
        } else {
            path.substring(0, path.lastIndexOf('/') + 1)
        }
    }
}

data class MatchedLambda<T : GenericLambda<*, *>>(val routePath: String, val lambda: T)