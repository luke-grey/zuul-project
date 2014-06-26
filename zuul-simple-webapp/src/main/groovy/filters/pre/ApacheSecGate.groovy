package filters.pre;
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.ZuulFilterResult
import com.netflix.zuul.context.RequestContext
import com.netflix.zuul.context.Debug
import java.util.Map.Entry

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class ApacheSecGate extends ZuulFilter {

	private static final Logger log = LogManager.getLogger(ApacheSecGate.class)
	private static int quota = 3;
	
    @Override
    int filterOrder() {
        return 21
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
				if(ctx.getURIVersionPath()=="/apache"){
					if(quota>0){
						log.info("Quota at " + quota)
						if(ctx.getRequestPermissions().contains("/apache")){
							log.info("Request endpoint /apache recognized")
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
		ctx.setRouteHost(new URL("http://apache.org/"));
		log.info("Set host to http://apache.org/");
		
        // sets custom uri to send to the server
        ctx.setRequestURI("/");
		log.info("At /");
		
		//drop the quota
		quota--;
		log.info("Quota is now "+quota);
    }

}



