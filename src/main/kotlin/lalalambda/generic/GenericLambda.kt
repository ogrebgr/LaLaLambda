package lalalambda.generic

interface GenericLambda<REQ, RESP> {
    fun handleRequest(input: REQ): RESP
}