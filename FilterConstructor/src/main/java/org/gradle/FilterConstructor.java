package org.gradle;

//import com.netflix.zuul.Config;

//import java.util.Map;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.Template;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.*;

/**
## The type alternatives are pretty basic.
## It's a good idea to divide this thing into
## three different templates, or a template for each purpose:
## gate, transform, stat return, and debug. 
## The manager of the file just has to know what could go 
## into each type of filter.
## Basically, the program now needs to receive a packet, choose
## the matching template, parse that packet's data, and write the 
## filter.
##
## Still dunno how the data is being passed so, time to start a convention
## FilterTemplates, gateway.groovy.vm, transform.groovy.vm, stat.groovy.vm, debug.groovy.vm
## gateway:
##	-name:()
##  -type:()suggested pre
##  -order:()
##  -uri:()in form "/..."
##  -host:()
##  -key:()still unsure about
##  -daylimit:()
##  -monthlimit:()
## transform:
##  -name:()
##  -type:()suggested route
##  -order:()
##  -dont
##  -know
## stat:
##	-name:()
##  -type:()suggested post
##  -order:()
##  dunno
**/

public class FilterConstructor{
	
    public FilterConstructor(String templateFile){
    	
        try{
        	Velocity.init();  //"velo.properties");
        	
        	VelocityContext context = new VelocityContext();
        	
            //context.put("list");
        	Template template =  null;
        
        	try{
        		template = Velocity.getTemplate(templateFile);
            
        	}catch( ResourceNotFoundException rnfe ){
        	
        		System.out.println("Example : error : cannot find template " + templateFile );
            
        	}catch( ParseErrorException pee ){
        	
        		System.out.println("Example : Syntax error in template " + templateFile + ":" + pee );
            
        	}
        	//Config config = new Config("config.json");
        	
        	//for(Map.Entry<String,String> entry: config.getMap().entrySet() ){
        		//context.put(entry.getKey(), entry.getValue());
        	
        	//this bit is lame but i dont know where or how the data is coming at this yet.
        	String uri="/pi";
        	String host="http://54.241.52.190:9090/";
        	String filterType="pre";
        	String filterOrder="6";
        	String fileName="PiGate";
        	
        	context.put("uri", uri);
        	context.put("host", host);
        	context.put("type", filterType);
        	context.put("order", filterOrder);
        	context.put("name", fileName);
        	
        	//}
        	//this bit will also need to be changed to some cassandra... upload... thing
        	//or maybe also be a passed variable. who knows where these things will need to go
        	//i do, of course. any of the three filter folders in the Filter Persistence section of the diagram
            Writer writer = new BufferedWriter(new PrintWriter(new FileOutputStream
            		("../zuul-simple-webapp/src/main/groovy/filters/"+filterType+"/"+fileName+".groovy")));

            if ( template != null)
                    template.merge(context, writer);

            //flush and cleanup
            writer.flush();
            writer.close();
        }catch( Exception e ){
            System.out.println(e);
        }
    }
    public static void main(String[] args){
	
    	FilterConstructor t = new FilterConstructor("/src/main/resources/org/gradle/filter.groovy.vm");
    }
}