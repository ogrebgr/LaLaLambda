package ezlambda.simple

import ezlambda.misc.HttpMethod

interface SimpleRequestContext {
    /**
     * Gets a variable form GET parameters
     *
     * @param parameterName Parameter name
     * @return Parameter value
     */
    fun getFromGet(parameterName: String): String?

    /**
     * Alias to [.getFromGet]
     */
    fun getFromQuery(parameterName: String): String?

    /**
     * Gets a variable form POST parameters
     *
     * @param parameterName Parameter name
     * @return Parameter value
     */
    fun getFromPost(parameterName: String): String?

    /**
     * @return List containing path info parameters ordered from left to right
     */
    fun getPathInfoParameters(): List<String>

    /**
     * Alias of [.getPathInfoParameters]
     *
     * @return List containing path info parameters ordered from left to right
     */
    fun getPi(): List<String>

    /**
     * Returns the path of the matched route
     *
     * @return path of the matched route
     */
    fun getRoutePath(): String

    /**
     * Returns the Path info string, i.e. the path part after the route path
     * For example if we have a request URL `http://somedomain.com/route/path/path/info/string` and
     * route path '/route/path/path' the returned string will be  /path/info/string
     *
     * @return String containing the path info
     */
    fun getPathInfoString(): String?

    /**
     * Optionally gets from GET parameters if parameter is present
     *
     * @param parameterName Parameter name
     * @param defaultValue  Default value to be returned if parameter is not present
     * @return Parameter value or the default value if not present
     */
    fun optFromGet(parameterName: String, defaultValue: String): String?

    /**
     * Optionally gets from POST parameters if parameter is present
     *
     * @param parameterName Parameter name
     * @param defaultValue  Default value to be returned if parameter is not present
     * @return Parameter value or the default value if not present
     */
    fun optFromPost(parameterName: String, defaultValue: String): String?

    /**
     * Returns the value of the specified request header as a String. If the request did not include a header of the
     * specified name, this method returns null. If there are multiple headers with the same name, this method returns
     * the first head in the request. The header name is case insensitive. You can use this method with any request
     * header.
     *
     * @param headerName Header name
     * @return a String containing the value of the requested header, or null if the request does not have a header
     * of that name
     */
    fun getHeader(headerName: String): String?


    /**
     * Alias of [.getHeader]
     */
    fun getHeaderValue(headerName: String): String?

    /**
     * Returns all values for a given header (if it is present multiple times)
     *
     * @param headerName Header name
     * @return List of header values
     */
    fun getHeaderValues(headerName: String): List<String>

    /**
     * Alias of [.getHttpMethod]
     *
     * @return HTTP method
     */
    fun getMethod(): HttpMethod

    /**
     * Returns the HTTP method of the request, e.g. GET, POST, etc
     *
     * @return HTTP method
     */
    fun getHttpMethod(): HttpMethod?

    /**
     * Checks if the request HTTP method matched the specified as parameter
     *
     * @param method Specified method
     * @return true if the HTTP method matches the specified, false otherwise
     */
    fun isMethod(method: HttpMethod): Boolean
}

