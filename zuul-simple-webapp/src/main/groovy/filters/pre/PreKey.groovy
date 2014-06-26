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
import javax.servlet.http.HttpServletRequest
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Luke Grey | productOps, Inc.
 */
class PreKeyFilter extends ZuulFilter {
	
	private static final Logger log = LogManager.getLogger(PreKeyFilter.class)

    @Override
    int filterOrder() {
        return 4 
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
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest req = ctx.request
		String k;
		boolean stay=true;
		Iterator headerIt = req.getHeaderNames().iterator()
		while (headerIt.hasNext()&&stay) {
			String name=(String) headerIt.next()
			if(name=="key"){
				k = req.getHeader(name)
				ctx.setPermissionKey(k);
				stay=false;
			}
		}
		if(k==null){
			ctx.stopFiltering();
			ctx.setErrorCondition("lack of key");
			log.error("Request is without key header");
		}
		ctx.setHasKey(k!=null);
		
    }

}
