/**
 * Author: Luke Grey | productOps, Inc.
 *
 *
 */
package filters.pre

import com.netflix.config.DynamicBooleanProperty
import com.netflix.config.DynamicPropertyFactory
import com.netflix.config.DynamicStringProperty
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.constants.ZuulConstants
import com.netflix.zuul.context.RequestContext
import javax.servlet.http.HttpServletRequest

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class RequestLogFilter extends ZuulFilter {

    private static final Logger log = LogManager.getLogger(RequestLogFilter.class);

    @Override
    String filterType() {
        return 'pre'
    }

    @Override
    int filterOrder() {
        return 1
    }

    boolean shouldFilter() {
        return true;
    }

    Object run() {
		HttpServletRequest req = RequestContext.currentContext.request
        log.info("Request::");
		log.info("Host " + req.getScheme() + " " + req.getRemoteAddr() + ":" + req.getRemotePort() );
		log.info("Query "+ req.getMethod() + " " + req.getRequestURI() + " " + req.getProtocol());
		Iterator headerIt = req.getHeaderNames().iterator()
		while (headerIt.hasNext()) {
            String name = (String) headerIt.next()
            String value = req.getHeader(name)
			log.info("Header > " + name + ":" + value)
        }
		
		final RequestContext ctx = RequestContext.getCurrentContext()
        if (!ctx.isChunkedRequestBody()) {
            InputStream inp = ctx.request.getInputStream()
            String body = null
            if (inp != null) {
                body = inp.getText()
				log.info("Body > " + body);
            }
        }
		
		ctx.beginTimer();
		log.info("Began timer");
    }


}