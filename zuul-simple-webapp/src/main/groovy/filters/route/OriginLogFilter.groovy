/**
 * Author: Luke Grey | productOps, Inc.
 *
 *
 */
package filters.route

import com.netflix.config.DynamicBooleanProperty
import com.netflix.config.DynamicPropertyFactory
import com.netflix.config.DynamicStringProperty
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.constants.ZuulConstants
import com.netflix.zuul.context.RequestContext

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.netflix.util.Pair;
import java.util.Map;
import javax.servlet.http.HttpServletRequest

class OriginLogFilter extends ZuulFilter {

    private static final Logger log = LogManager.getLogger(OriginLogFilter.class);

    @Override
    String filterType() {
        return 'route'
    }

    @Override
    int filterOrder() {
        return 150
    }

    boolean shouldFilter() {
        return true;
    }

    Object run() {
        RequestContext ctx = RequestContext.getCurrentContext()
		ctx.stopRTTTimer();
		HttpServletRequest request = ctx.getRequest()
		Map<String,String> headers = ctx.getZuulRequestHeaders()
		String uri = RequestContext.currentContext.requestURI
		InputStream requestEntity = request.getInputStream()
		
		log.info("Zuul::");
		log.info("Host>${ctx.getRouteHost()}");
		headers.each { String k, String v ->
			log.info("Headers> ${k}  ${v}");
		}
		log.info("Query> ${request.getMethod().toUpperCase()}  ${uri}?${request.queryString} HTTP/1.1");
		if (requestEntity != null)
			log.info("Entity> ${requestEntity.getText()}");
		
			
		//these arent right, we want to have the origin response headers, not the zuul ones
		log.info("Origin Response::");
        ctx.getOriginResponseHeaders()?.each { Pair header ->
			log.info("Headers< ${header.first()}, ${header.second()}")
        }
    }
}