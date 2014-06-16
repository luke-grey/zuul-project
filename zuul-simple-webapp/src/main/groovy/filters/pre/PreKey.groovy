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

/**
 * @author Luke Grey | productOps, Inc.
 */
class PreKeyFilter extends ZuulFilter {

    @Override
    int filterOrder() {
        return 3 
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
		String k = ctx.getZuulRequestHeaders().get("key");
		if(k==null)
			ctx.stopFiltering();
		else{
			ctx.setPermissionKey(k);
		}
		ctx.setHasKey(k!=null);
		
    }

}
