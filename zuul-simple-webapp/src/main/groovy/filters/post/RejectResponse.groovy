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

import java.io.InputStream;

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import com.netflix.zuul.context.Debug

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author Luke Grey
 * Date: 6/16/14
 * Time: 9:18 AM
 */
class RejectResponse extends ZuulFilter {
    @Override
    String filterType() {
        return "post"
    }

    @Override
    int filterOrder() {
        return 1500
    }

    @Override
    boolean shouldFilter() {
		RequestContext ctx = RequestContext.currentContext;
		//if(!ctx.getContinueFiltering())
        	return true
		return false;
    }

    @Override
    Object run() {
		RequestContext ctx = RequestContext.currentContext;
		//build a response and add a body to it, a 404 or something in html. something from resources
		InputStream inputStream=ctx.responseBody;
		String entity = inputStream.getText()
		Debug.addRequestDebug("GREY::Ran reject filter")
		Debug.addRequestDebug("GREY::>${entity}")
    }

}
