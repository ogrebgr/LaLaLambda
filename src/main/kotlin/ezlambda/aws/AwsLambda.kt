package ezlambda.aws

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2ProxyResponseEvent
import ezlambda.generic.GenericLambda

interface AwsLambda :
    GenericLambda<AwsRequestData, APIGatewayV2ProxyResponseEvent>

data class AwsRequestData(val event: APIGatewayV2ProxyRequestEvent, val awsContext: Context)