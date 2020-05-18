package lalalambda.simple

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent
import lalalambda.generic.AbstractLambdaDispatcher
import lalalambda.misc.HttpMethod

open class SimpleLambdaDispatcher constructor(isPathInfoEnabled: Boolean) :
    AbstractLambdaDispatcher<SimpleLambda>(isPathInfoEnabled) {

    override fun handleRequest(
        input: APIGatewayV2ProxyRequestEvent,
        context: Context?
    ): APIGatewayV2ProxyResponseEvent {

        val match = match(input.path, HttpMethod.fromString(input.httpMethod))
        return if (match != null) {
            try {
                val resp = match.lambda.handleRequest(SimpleRequestContextAws(input, match.routePath))
                val ret = APIGatewayV2ProxyResponseEvent()
                ret.statusCode = resp.statusCode
                ret.headers = resp.headers
                ret.body = resp.body
                ret
            } catch (e: Exception) {
                val ret = APIGatewayV2ProxyResponseEvent()
                ret.statusCode = 500
                ret.body = "500 Internal server error"
                ret
            }
        } else {
            val ret = APIGatewayV2ProxyResponseEvent()
            ret.statusCode = 404
            ret.body = "404 Not found"
            ret
        }
    }

}