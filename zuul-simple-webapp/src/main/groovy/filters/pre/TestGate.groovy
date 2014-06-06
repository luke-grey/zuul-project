


import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.ZuulFilterResult
import com.netflix.zuul.context.RequestContext
import com.netflix.zuul.context.Debug


class TestGate extends ZuulFilter {

    @Override
    int filterOrder() {
        return 6
    }

    @Override
    String filterType() {
        return "pre"
    }

    @Override
    boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		
		if("/test"==ctx.request.getRequestURI())
			return true;
			
		return false;
    }

    @Override
    Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
		
        // sets origin
		ctx.setRouteHost(new URL("http://apache.org/"));

        // sets custom header to send to the origin
        ctx.addOriginResponseHeader("cache-control", "max-age=3600");
    }

}



