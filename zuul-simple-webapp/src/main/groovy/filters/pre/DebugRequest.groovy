import org.apache.logging.log4j.Logger;

/*
 * Copyright 2013 Netflix, Inc.
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */


import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.Debug
import com.netflix.zuul.context.RequestContext

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Mikey Cohen
 * Date: 3/12/12
 * Time: 1:51 PM
 */
class DebugRequest extends ZuulFilter {
	
	private static final Logger log = LogManager.getLogger(DebugRequest.class)
	
    @Override
    String filterType() {
        return 'pre'
    }

    @Override
    int filterOrder() {
        return 10000
    }

    @Override
    boolean shouldFilter() {
        return Debug.debugRequest()
    }

    @Override
    Object run() {
        HttpServletRequest req = RequestContext.currentContext.request as HttpServletRequest
					log.info("Request::");
        Debug.addRequestDebug("REQUEST:: " + req.getScheme() + " " + req.getRemoteAddr() + ":" + req.getRemotePort())
					      log.info("Host " + req.getScheme() + " " + req.getRemoteAddr() + ":" + req.getRemotePort() );
        Debug.addRequestDebug("REQUEST:: > " + req.getMethod() + " " + req.getRequestURI() + " " + req.getProtocol())
							log.info("Query "+ req.getMethod() + " " + req.getRequestURI() + " " + req.getProtocol());
        Iterator headerIt = req.getHeaderNames().iterator()
        while (headerIt.hasNext()) {
            String name = (String) headerIt.next()
            String value = req.getHeader(name)
            Debug.addRequestDebug("REQUEST:: > " + name + ":" + value)
							log.info("Header > " + name + ":" + value);
        }

        final RequestContext ctx = RequestContext.getCurrentContext()
        if (!ctx.isChunkedRequestBody()) {
            InputStream inp = ctx.request.getInputStream()
            String body = null
            if (inp != null) {
                body = inp.getText()
                Debug.addRequestDebug("REQUEST:: > " + body)
								  log.info("Body > " + body);
            }
        }
        return null;
    }

}
