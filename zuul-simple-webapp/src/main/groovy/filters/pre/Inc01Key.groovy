package filters.pre;
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.ZuulFilterResult
import com.netflix.zuul.context.RequestContext
import com.netflix.zuul.context.Debug

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class Inc01Key extends ZuulFilter {

	private static final Logger log = LogManager.getLogger(Inc01Key.class);
	
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
		RequestContext ctx = RequestContext.getCurrentContext()
		
		if(ctx.getContinueFiltering()){
			if(ctx.getHasKey()){
				if(ctx.getPermissionKey()=="1234567890"){
					log.info("Request key 1234567890 recognized")
					return true;
				}
			}
		}
		return false;
    }

    @Override
    Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
		//ctx.addRequestPermission("/apache")
		//log.info("Added permission for /apache");
		ctx.addRequestPermission("/pi")
		log.info("Added permission for /pi");
		ctx.addRequestPermission("/simple")
		log.info("Added permission for /simple");
    }

}