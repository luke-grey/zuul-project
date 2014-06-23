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
 * Author: Luke Grey | ProductOps, Inc.
 *
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
        			context.put("quota", filterValues.get("quota"));
        			context.put("disabled", filterValues.get("disabled"));
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
            ("../../zuul-project/zuul-simple-webapp/src/main/groovy/filters/"
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
    	/*
    	gate.put("template", "secGate");
    	gate.put("name", "Pi");
    	gate.put("order", "20");
    	gate.put("type", "pre");
    	gate.put("endpoint","/pi");
    	gate.put("host","http://54.241.52.190:9090/");
    	gate.put("api", "/");
    	gate.put("quota", "5");
    	gate.put("disabled", false);
    	*/
    	gate.put("template", "secGate");
    	gate.put("name", "Apache");
    	gate.put("order", "21");
    	gate.put("type", "pre");
    	gate.put("endpoint","/apache");
    	gate.put("host","http://apache.org/");
    	gate.put("api", "/");
    	gate.put("quota", "3");
    	gate.put("disabled", false);
    	
    	List<String> endpoints= new ArrayList<String>();
    	endpoints.add("/apache");
    	
    	HashMap<String,Object> keyFilter=new HashMap<String,Object>();
    	keyFilter.put("template","key");
    	keyFilter.put("name", "ApacheRefer");
    	keyFilter.put("order","5");
    	keyFilter.put("type", "pre");
    	
    	keyFilter.put("key","1234567890");
    	keyFilter.put("endpoints",endpoints);
    	keyFilter.put("disabled",false);
    	
    	@SuppressWarnings("unused")
		FilterConstructor t = new FilterConstructor(gate);
    }
    
}