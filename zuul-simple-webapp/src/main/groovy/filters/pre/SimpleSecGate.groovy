package filters.pre;
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.ZuulFilterResult
import com.netflix.zuul.context.RequestContext
import com.netflix.zuul.context.Debug
import java.util.Map.Entry

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class SimpleSecGateV1 extends ZuulFilter {

	private static final Logger log = LogManager.getLogger(SimpleSecGateV1.class)
	private static int quota = 5;
	
    @Override
    int filterOrder() {
        return 22
    }

    @Override
    String filterType() {
        return "pre"
    }

    @Override
    boolean shouldFilter() {
		//if nothing in the chain has caused it to fail, and the request has permissions, 
		//and one of those permissions are this filters endpoint,  and there are still calls left in the quota
		//then this request should be processed by this filter
		//however if this call doesnt have permission to use this, stop filtering
		//and if there is nothing left in the quota, stop filtering too
		
		RequestContext ctx = RequestContext.getCurrentContext();
		if(ctx.getContinueFiltering()){
			if(!ctx.getRequestPermissions().isEmpty()){
				if(ctx.request.getRequestURI().startsWith("/simple/v1")){
					if(quota>0){
						log.info("Quota at " + quota)
						if(ctx.getRequestPermissions().contains("/simple/v1")){
							log.info("Request endpoint /simple/v1 recognized")
							return true
						}else{
							ctx.stopFiltering();
							log.error("No permissions for this endpoint")
							ctx.setErrorCondition("lack of permission on this endpoint");
						}
					}else if(quota<1){
						ctx.stopFiltering();
						log.error("Empty quota.")
						ctx.setErrorCondition("being out of passes");
					}
				}
			}
	        return false;
		}
	}

    @Override
    Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
		
        // sets origin
		ctx.setRouteHost(new URL("http://localhost:8081/"));
		log.info("Set host to http://localhost:8081/");
		
        // sets custom uri to send to the server
        ctx.setRequestURI(""+ctx.getNonURIPath());
		log.info("At "+ctx.getNonURIPath());
		
		//drop the quota
		quota--;
		log.info("Quota is now "+quota);
    }

}



