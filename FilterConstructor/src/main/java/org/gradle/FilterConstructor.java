package org.gradle;

//import com.netflix.zuul.Config;

//import java.util.Map;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.Template;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
## Author: Luke Grey | ProductOps, Inc.
##
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
##  -key:()
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
	
    public FilterConstructor(HashMap<String,Object> filterValues){
        try{
        	Velocity.init();
        	
        	VelocityContext context = new VelocityContext();
        	
        	Template template =  null;
        
        	try{
        		template = Velocity.getTemplate("src/main/resources/org/gradle/"+filterValues.get("template")+".groovy.vm");
            
        	}catch( ResourceNotFoundException rnfe ){
        	
        		System.out.println("Example : error : cannot find template " 
        		+ filterValues.get("template")+".groovy.vm" );
            
        	}catch( ParseErrorException pee ){
        	
        		System.out.println("Example : Syntax error in template " 
        		+ filterValues.get("template")+".groovy.vm" + ":" + pee );
            
        	}

        	context.put("name", filterValues.get("name"));
			context.put("order", filterValues.get("order"));
			context.put("type", filterValues.get("type"));
			context.put("disabled", filterValues.get("disabled"));
			
        	switch((String) filterValues.get("template")){
        		case "secGate":
        			context.put("endpoint", filterValues.get("endpoint"));
        			context.put("host", filterValues.get("host"));
        			context.put("api", filterValues.get("api"));
        			break;
        		case "key":
        			context.put("key",filterValues.get("key"));
        			context.put("endpoints",filterValues.get("endpoints") );
        			break;
        		default:
        			
        			break;
        	}
        	
        	String fTemp= ((String)filterValues.get("template")).substring(0,1).toUpperCase()+
        			((String)filterValues.get("template")).substring(1);
        	
            Writer writer = new BufferedWriter(new PrintWriter(new FileOutputStream
            ("../zuul-simple-webapp/src/main/groovy/filters/"
            +filterValues.get("type")+"/"+filterValues.get("name")+ fTemp+".groovy")));

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
    	HashMap<String,Object> gate=new HashMap<String,Object>();
    	gate.put("template", "secGate");
    	gate.put("name", "Simple");
    	gate.put("order", "13");
    	gate.put("type", "pre");
    	gate.put("endpoint","/simple");
    	gate.put("host","http://localhost:8081/");
    	gate.put("api", "/SimpleAPIService");
    	gate.put("disabled", false);
    	
    	List<String> endpoints= new ArrayList<String>();
    	endpoints.add("/apache");
    	endpoints.add("/pi");
    	endpoints.add("/simple");
    	
    	HashMap<String,Object> keyFilter=new HashMap<String,Object>();
    	keyFilter.put("template","key");
    	keyFilter.put("name", "Inc01");
    	keyFilter.put("order","3");
    	keyFilter.put("type", "pre");
    	keyFilter.put("key","1234567890");
    	keyFilter.put("endpoints",endpoints);
    	keyFilter.put("disabled",false);
    	@SuppressWarnings("unused")
		FilterConstructor t = new FilterConstructor(keyFilter);
    }
    
}