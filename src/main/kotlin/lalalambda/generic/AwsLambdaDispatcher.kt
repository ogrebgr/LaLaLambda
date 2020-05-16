package lalalambda.generic

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent
import lalalambda.misc.HttpMethod


open class AwsLambdaDispatcher constructor(isPathInfoEnabled: Boolean) :
    AbstractLambdaDispatcher<AwsLambda>(isPathInfoEnabled) {

    override fun handleRequest(
        input: APIGatewayV2ProxyRequestEvent,
        context: Context
    ): APIGatewayV2ProxyResponseEvent {

        val lambda = match(input.path, HttpMethod.fromString(input.httpMethod))
        return if (lambda != null) {
            try {
                lambda.handleRequest(AwsRequestData(input, context))
            } catch (e: Exception) {
                val ret = APIGatewayV2ProxyResponseEvent()
                ret.statusCode = 500
                ret
            }
        } else {
            val ret = APIGatewayV2ProxyResponseEvent()
            ret.statusCode = 404
            ret
        }
    }
}