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

class RTTStarterFilter extends ZuulFilter {

    private static final Logger log = LogManager.getLogger(RTTStarterFilter.class);

    @Override
    String filterType() {
        return 'route'
    }

    @Override
    int filterOrder() {
        return 50
    }

    boolean shouldFilter() {
        return true;
    }

    Object run() {
		log.info("Starting timer");
        RequestContext.getCurrentContext().startRTTTimer();
    }


}