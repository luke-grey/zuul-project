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
package filters.post

import com.netflix.zuul.StartServer;
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.Debug;
import com.netflix.zuul.context.RequestContext
import com.netflix.util.Pair

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Mikey Cohen
 * Date: 2/3/12
 * Time: 2:48 PM
 */
class OutboundLogFilter extends ZuulFilter {
	
	private static final Logger log = LogManager.getLogger(OutboundLogFilter.class);
	
    @Override
    String filterType() {
        return "post"
    }

    @Override
    int filterOrder() {
        return 1050
    }

    @Override
    boolean shouldFilter() {
		return true
    }

    @Override
    Object run() {
		RequestContext ctx = RequestContext.getCurrentContext()
		ctx.endTimer()
		log.info("Ended Timer")
		
		log.info("Outbound::")
        ctx.getZuulResponseHeaders()?.each { Pair<String, String> it ->
			log.info("Headers<  " + it.first() + ":" + it.second());
        }
    }

}
