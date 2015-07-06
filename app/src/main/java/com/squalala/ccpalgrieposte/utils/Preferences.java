/**
 * 
 */
package com.squalala.ccpalgrieposte.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : Preferences.java
 * Date : 22 sept. 2014
 * 
 */
public class Preferences {

	private SharedPreferences preferences;
	
	public Preferences(Context context) {
		preferences = context.getSharedPreferences("CCP", MODE_PRIVATE);
	}
	
	public void setUsername(String username) {
		preferences.edit().putString("username", username).commit();
	}
	
	public void setPassword(String password) {
		preferences.edit().putString("password", password).commit();
	}
	
	public void setIdUrl(String idUrl) {
		preferences.edit().putString("id_url", idUrl).commit();
	}
	
	public void setCookieValue(String cookieValue) {
		preferences.edit().putString("cookie", cookieValue).commit();
	}
	
	public void setCle(String cle) {
		preferences.edit().putString("cle", cle).commit();
	}
	
	public void setLastVisit(String lastVisit) {
		preferences.edit().putString("last_visit", lastVisit).commit();
	}
	
	public String getCle() {
		return preferences.getString("cle", null); 
	}
	
	public String getLastVisit() {
		return preferences.getString("last_visit", null); 
	}
	
	public String getCookieValue() {
		return preferences.getString("cookie", null); 
	}
	
	public String getIdUrl() {
		return preferences.getString("id_url", null); 
	}
	
	public String getUsername() {
		return preferences.getString("username", ""); 
	}
	
	public String getPassword() {
		return preferences.getString("password", null); 
	}
	

}
