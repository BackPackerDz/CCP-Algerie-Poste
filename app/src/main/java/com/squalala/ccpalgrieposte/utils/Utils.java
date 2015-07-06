/**
 * 
 */
package com.squalala.ccpalgrieposte.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.squalala.ccpalgrieposte.common.CcpConstant;

import java.util.Map;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : Utils.java
 * Date : 23 sept. 2014
 * 
 */
public class Utils {
	
	public static double d;

	/**
	 *  Pour récupérer le cookie
	 */
	@SuppressWarnings("rawtypes")
	public static String getCookie(Map<String, String> map) {
		
		String cookie = null;
		
		for (Map.Entry entry : map.entrySet()) { 
			if (entry.getKey().equals(CcpConstant.COOKIE_NAME)) {
				cookie = String.valueOf(entry.getValue());
				System.out.println("#COOKIE --> " + cookie);
				return cookie;
			}
		} 
		return cookie;
	}
	
	public static boolean isNumeric(String str)  
	{   
	  try   
	  {   
	    d = Double.parseDouble(str);  
	  }   
	  catch(NumberFormatException nfe)  
	  {   
	    return false;   
	  }   
	  return true;   
	} 
	
	public static boolean isPackageInstalled(String packagename, Context context) {
	    PackageManager pm = context.getPackageManager();
	    try { 
	        pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
	        return true; 
	    } catch (NameNotFoundException e) {
	        return false; 
	    } 
	} 

}
