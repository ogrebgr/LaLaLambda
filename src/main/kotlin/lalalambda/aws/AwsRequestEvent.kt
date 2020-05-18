package lalalambda.aws

data class AwsRequestEvent(
    val resource: String,
    val path: String,
    val httpMethod: String,
    val headers: Map<String, String>,
    val multiValueHeaders: Map<String, List<String>>,
    val queryStringParameters: Map<String, String>,
    val multiValueQueryStringParameters: Map<String, List<String>>,
    val pathParameters: Map<String, String>,
    val stageVariables: Map<String, String>,
    val requestContext: AwsRequestContext,
    val body: String,
    val isBase64Encoded: Boolean = false
)


data class AwsRequestIdentity(
    val cognitoIdentityPoolId: String,
    val accountId: String,
    val cognitoIdentityId: String,
    val caller: String,
    val apiKey: String,
    val sourceIp: String,
    val cognitoAuthenticationType: String,
    val cognitoAuthenticationProvider: String,
    val userArn: String,
    val userAgent: String,
    val user: String,
    val accessKey: String
)

data class AwsRequestContext(
    val accountId: String,
    val resourceId: String,
    val stage: String,
    val requestId: String,
    val identity: AwsRequestIdentity,
    val resourcePath: String,
    val authorizer: Map<String, Object>,
    val httpMethod: String,
    val apiId: String,
    val connectedAt: Long,
    val connectionId: String,
    val domainName: String,
    val error: String,
    val eventType: String,
    val extendedRequestId: String,
    val integrationLatency: String,
    val messageDirection: String,
    val messageId: String,
    val requestTime: String,
    val requestTimeEpoch: Long,
    val routeKey: String,
    val status: String
)