package com.squalala.ccpalgrieposte;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.squalala.ccpalgrieposte.common.CcpConstant;
import com.squalala.ccpalgrieposte.utils.ConnectionDetector;
import com.squalala.ccpalgrieposte.utils.FlushedInputStream;
import com.squalala.ccpalgrieposte.utils.Preferences;
import com.squalala.ccpalgrieposte.utils.Utils;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : LoginActivity.java
 * Date : 22 sept. 2014
 * 
 */
public class LoginActivity extends AppCompatActivity
	implements OnClickListener {
	
	@InjectView(R.id.editPassword) BootstrapEditText editPassword;
	@InjectView(R.id.editUsername) BootstrapEditText editUsername;
	@InjectView(R.id.btn_login) BootstrapButton btn_login;
	@InjectView(R.id.image_captcha) ImageView imgCaptcha;
	@InjectView(R.id.edit_captcha) BootstrapEditText editCaptcha; 
	@InjectView(R.id.fontLoad) FontAwesomeText fontLoad; 
	@InjectView(R.id.txt_no_serveur) TextView txtNoServeur; 
	
	private Preferences preferences;
	private Activity activity = this;
	private String cookieValue;
	
	private static final String MM_INSERT = "form1";
	
	private static final int NOMBRE_MAX_REQUETE = 2;
	
	private int nombreRequete = 0;
	private Connection.Response res = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		

		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		getSupportActionBar().setBackgroundDrawable(new
				ColorDrawable(Color.parseColor("#f4ec27"))); 
		
		ButterKnife.inject(this);
		
		btn_login.setOnClickListener(this);
		
		preferences = new Preferences(this);
		
		editUsername.setText(preferences.getUsername());
		
		new DownloadImage().execute();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Crouton.cancelAllCroutons();
	}
	
	public String getUsername() {
		return editUsername.getText().toString().trim();
	}
	
	public String getPassword() {
		return editPassword.getText().toString().trim();
	}
	
	public String getCaptcha() {
		return editCaptcha.getText().toString().trim();
	}
	
	class LoginTask extends AsyncTask<String, String, String> {
		
		private ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(activity);
			pDialog.setMessage("Patientez...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			try {
				
				res = Jsoup.connect(CcpConstant.URL_CONNEXION)
						.data(CcpConstant.TAG_COMPTE, getUsername())
						.data(CcpConstant.TAG_CODE, getPassword())
						.data(CcpConstant.TAG_CAPTCHA, getCaptcha())
						.data(CcpConstant.TAG_MM_INSERT, MM_INSERT)
						.cookie(CcpConstant.COOKIE_NAME, cookieValue)
						.followRedirects(true)
					    .method(Method.POST)
						.timeout(CcpConstant.TIME_OUT)
					    .execute();
			
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				return res.body().toString();
			}
			catch (NullPointerException e) {}
			
			return "error";
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			System.out.println(result);
			
			pDialog.dismiss();
			
			if (result.equals("error") && nombreRequete < NOMBRE_MAX_REQUETE) {
				Crouton.makeText(activity, "Veuillez vérifier votre connexion internet",
						Style.ALERT).show();
				nombreRequete++;
				new LoginTask().execute();
			} 
			else if (result.contains("Veuillez saisir le texte qui apparait sur l'image")) {
				Crouton.makeText(activity,
						"Mauvais captcha",
						Style.ALERT).show();
				new DownloadImage().execute();
			}
			else if (result.contains("de compte ou mot de passe incorrect")){ 
				Crouton.makeText(activity,
						"Numéro de compte ou mot de passe incorrect",
						Style.ALERT).show();
			}
			// Connexion réussie
			else if (result.contains("Bienvenue")) {
				Crouton.makeText(activity,
						"Connexion réussi",
						Style.CONFIRM).show();
				
				System.out.println("====================== DEBUG ======================");
				
				String url = res.url().toString();
				
				String idUrl = url.substring(url.indexOf("=") + 1,
						url.length());
				
				Document doc = Jsoup.parse(result);
				Elements elements =  doc.select("font[color=#330099]");
				
				preferences.setIdUrl(idUrl);
				preferences.setCookieValue(cookieValue);
				preferences.setUsername(getUsername());
				preferences.setCle(elements.get(1).text().trim());
				preferences.setLastVisit(elements.get(2).text().trim() + " à " + elements.get(3).text().trim());
				
				System.out.println("ELEM " + elements.get(1).text());
				System.out.println("ELEM " + elements.get(2).text() + " à " + elements.get(3).text());
				
				System.out.println("CODE ---> " + idUrl);
				
				System.out.println("====================== END DEBUG ======================");
				
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
				finish();
				
			}
			else {
				Crouton.makeText(activity,
						"Serveur indisponible, réessayer plus tard",
						Style.ALERT).show();
			}
				
			
		}
		
	}
	
	private class DownloadImage extends AsyncTask<String, String, String> {

		private Bitmap bitmap;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			fontLoad.setVisibility(View.VISIBLE);
			fontLoad.startRotate(activity, true, FontAwesomeText.AnimationSpeed.MEDIUM);
			imgCaptcha.setVisibility(View.GONE);
			btn_login.setVisibility(View.GONE);
		}
		
		@Override
		protected String doInBackground(String... params) {
			
			URL url;
			
			Connection.Response res = null;
			
			try {
				res = Jsoup.connect(CcpConstant.URL_CONNEXION)
					    .method(Method.GET)
					    .followRedirects(true)
						.timeout(CcpConstant.TIME_OUT)
					    .execute();
				
				cookieValue = Utils.getCookie(res.cookies());
				
				url = new URL(CcpConstant.URL_CAPTCHA);
				URLConnection connection = url.openConnection();
				connection.setDoInput(true);
				connection.setRequestProperty("Cookie", CcpConstant.COOKIE_NAME  + "=" + cookieValue);
				connection.connect();
				bitmap = BitmapFactory.decodeStream(new FlushedInputStream((InputStream) connection.getContent()));
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
				System.out.println("FAIl 1");
				return e.getMessage().toString();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("FAIl 2");
				return e.getMessage().toString();
			}
			
			return res.body().toString();
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			System.out.println("RESULT --> " + cookieValue);
			
			if (!ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
				fontLoad.setVisibility(View.GONE);
				Crouton.makeText(activity,
						"Vérifiez votre connexion internet",
						Style.ALERT).show();
				
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						finish();
					}
				}, 4000);
				
			}
			
			
			if (result.contains("SSL handshake")
					&& nombreRequete < NOMBRE_MAX_REQUETE) {
					nombreRequete++;
					Crouton.makeText(activity,
							"Tentative de connexion au serveur " + nombreRequete,
							Style.INFO).show();
					new DownloadImage().execute();
			}
			else if (result.contains("Connection closed by peer")
					&& nombreRequete < NOMBRE_MAX_REQUETE) {
					nombreRequete++;
					Crouton.makeText(activity,
							"Tentative de connexion au serveur " + nombreRequete,
							Style.INFO).show();
					new DownloadImage().execute();
			}
			else if (nombreRequete >= NOMBRE_MAX_REQUETE) {
				Crouton.makeText(activity,
						"Serveur indisponible, réessayer plus tard",
						Style.ALERT).show();
				txtNoServeur.setVisibility(View.VISIBLE);
				fontLoad.stopAnimation();
			}
			else if (cookieValue != null ){

				Log.e("LoginActvitiy", "here");
				 // On affiche le captcha
				 fontLoad.stopAnimation();
				 fontLoad.setVisibility(View.GONE);
				 editCaptcha.setVisibility(View.VISIBLE);
				 imgCaptcha.setVisibility(View.VISIBLE);
				 imgCaptcha.setImageBitmap(bitmap);
				 btn_login.setVisibility(View.VISIBLE);
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.btn_login:
			
			if (getPassword().length() == 4 && getUsername().length() > 0)
				new LoginTask().execute();
			else {
				Crouton.makeText(activity, "Vérifiez les champs",
						Style.ALERT).show();
			}
			
			break;

		default:
			break;
		}
	}

}
