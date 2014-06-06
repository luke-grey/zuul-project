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

import com.netflix.zuul.Config
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.ZuulFilterResult
import com.netflix.zuul.context.RequestContext
import com.netflix.zuul.context.Debug

/**
 * @author mhawthorne
 */
class PreDecorationFilter extends ZuulFilter {

    @Override
    int filterOrder() {
        return 5
    }

    @Override
    String filterType() {
        return "pre"
    }

    @Override
    boolean shouldFilter() {
        return true;
    }

    @Override
    Object run() {
		/*
        RequestContext ctx = RequestContext.getCurrentContext();
		
		Config config = new Config("config.json");
		Debug.addRequestDebug("GREY::URI::"+ctx.request.getRequestURI())
		
		if( config.getMap().containsKey(ctx.request.getRequestURI()) ){
			Debug.addRequestDebug("GREY::HOST::"+config.get(ctx.request.getRequestURI()))
			ctx.setRouteHost(new URL(config.get(ctx.request.getRequestURI())))
		}
		else{ 
			Debug.addRequestDebug("GREY::Throwing Result!")
			ZuulFilterResult z =new ZuulFilterResult();
			z.setException(new Throwable("Failed to find URI on gateway."));
			throw z.getException();
		}
		
        // sets origin
		

        // sets custom header to send to the origin
        ctx.addOriginResponseHeader("cache-control", "max-age=3600");
        */
    }

}
