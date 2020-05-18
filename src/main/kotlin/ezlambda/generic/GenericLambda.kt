package ezlambda.generic

interface GenericLambda<REQ, RESP> {
    fun handleRequest(input: REQ): RESP
}