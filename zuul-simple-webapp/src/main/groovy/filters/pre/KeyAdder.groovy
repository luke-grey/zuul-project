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
 * Author: Luke Grey | productOps, Inc.
 */

class KeyAdderFilter extends ZuulFilter {

    @Override
    int filterOrder() {
        return 2
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
		ctx.addZuulRequestHeader("key","1234567890");
		//Debug.addRequestDebug("GREY::"+ctx.request.getRequestURI())
    }

}
