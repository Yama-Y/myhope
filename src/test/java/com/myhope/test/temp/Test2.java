package com.myhope.test.temp;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Test2 {

	  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
		ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");
        String strs = "1+1*2+(10-(2*(5-3)*(2-1))-4)+10/(5-0)";  
        try {  
            System.out.println(jse.eval(strs).toString());  
        } catch (Exception t) {  
        }  
    } 
	
}
