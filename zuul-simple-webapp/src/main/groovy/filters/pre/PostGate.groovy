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
package filters.pre;
import com.netflix.zuul.Config
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.ZuulFilterResult
import com.netflix.zuul.context.RequestContext
import com.netflix.zuul.context.Debug

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Luke Grey | productOps, Inc.
 */
class PostGateFilter extends ZuulFilter {

	private static final Logger log = LogManager.getLogger(PostGateFilter.class)

	@Override
	int filterOrder() {
		return 200
	}

	@Override
	String filterType() {
		return "pre"
	}

	@Override
	boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		if(ctx.getContinueFiltering()){
			return ctx.getRouteHost()==null;
		}
		return false
	}

	@Override
	Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		if(ctx.getContinueFiltering()){
			if(ctx.getHasKey()){
				ctx.stopFiltering();
				ctx.setErrorCondition("lack of acceptable gateway");
				log.error("Bad Gateway");
			}
		}
	}
}
