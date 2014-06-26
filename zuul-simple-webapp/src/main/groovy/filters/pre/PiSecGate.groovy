/**
 * Author: Luke Grey | productOps, Inc.
 *
 * if nothing in the chain has caused it to fail, and the request has permissions,
 * and one of those permissions are this filters endpoint,  and there are still calls left in the quota
 * then this request should be processed by this filter
 * however if this call doesnt have permission to use this, stop filtering
 * and if there is nothing left in the quota, stop filtering too
 *
 */
package filters.pre;
import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.ZuulFilterResult
import com.netflix.zuul.context.RequestContext
import com.netflix.zuul.context.Debug

import java.util.Map.Entry
import java.lang.Math

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class PiSecGateV2 extends ZuulFilter {

	private static final Logger log = LogManager.getLogger(PiSecGateV2.class)
	private static int quota = 50;
	private static int traffic = 0;
	private static long hostNum = 1;
	private static long lastTimeDecremented;

	@Override
	int filterOrder() {
		return 20
	}

	@Override
	String filterType() {
		return "pre"
	}

	@Override
	boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();

		if(ctx.getContinueFiltering()){
			if(!ctx.getRequestPermissions().isEmpty()){
				if(ctx.getURIVersionPath()=="/pi/v2"){
					if(trafficPassage()){
						log.info("Quota at " + quota)
						if(quota>0){
							if(ctx.getRequestPermissions().contains("/pi/v2")){
								log.info("Request endpoint /pi/v2 recognized")
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
					}else{
						ctx.stopFiltering();
						log.error("Traffic overflow.")
						ctx.setErrorCondition("too much traffic. Slow down, cowboy");
					}
				}
			}
		}
		return false;
	}

	@Override
	Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();

		// sets origin
		switch((hostNum%2)+1){
			case 1:
				ctx.setRouteHost(new URL("http://54.241.52.190:9090/"));
				log.info("Set host to http://54.241.52.190:9090/");
				break;
			case 2:
				ctx.setRouteHost(new URL("http://54.241.52.190:9091/"));
				log.info("Set host to http://54.241.52.190:9091/");
				break;
			default:
				log.error("No host chosen");
				break;
		}
		// sets custom uri to send to the server
		ctx.setRequestURI(""+ctx.getNonURIPath());
		log.info("At "+ctx.getNonURIPath());

		//drop the quota
		quota--;
		log.info("Quota is now "+quota);

		hostNum++;
		
		log.info("Traffic++")
		traffic++;

	}

	private boolean trafficPassage(){
		//update the traffic based on the current time and the last time it updated the traffic
		if(lastTimeDecremented!=null){
			int dec = (int)(Math.round((System.currentTimeMillis()-lastTimeDecremented))/1000)
			log.info("Traffic reduced by: "+dec);
			if(dec>0){
				traffic-=dec
				lastTimeDecremented=System.currentTimeMillis()
				if(traffic<0)
					traffic=0;
			}
		}else{
			lastTimeDecremented=System.currentTimeMillis()
		}
		//check if the traffic is passing
		log.info("Traffic at " + traffic)
		return traffic<5;
	}

}



