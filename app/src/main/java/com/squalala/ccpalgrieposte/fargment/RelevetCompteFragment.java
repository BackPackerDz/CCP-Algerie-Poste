package com.squalala.ccpalgrieposte.fargment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.squalala.ccpalgrieposte.R;
import com.squalala.ccpalgrieposte.ResultReleveActivity;
import com.squalala.ccpalgrieposte.common.CcpConstant;
import com.squalala.ccpalgrieposte.utils.Preferences;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : RelevetCompteFragment.java
 * Date : 25 oct. 2014
 * 
 */
@SuppressLint("SimpleDateFormat")
public class RelevetCompteFragment extends Fragment 
	implements OnClickListener {
	
	@InjectView(R.id.btn_date_du) BootstrapButton btnDateDu;
	@InjectView(R.id.btn_date_a) BootstrapButton btnDateA;
	@InjectView(R.id.btn_recherche) BootstrapButton btnAffiche;
	
	private Preferences preferences;
	private DateAFragment daFragment;
	private DateDuFragment duFragment;

	private static final int NOMBRE_MAX_REQUETE = 2;
	
	private int nombreRequete = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_relevet_compte, container, false);
		ButterKnife.inject(this, rootView);
		
		preferences = new Preferences(getActivity());
		daFragment = DateAFragment.newInstance(btnDateA);
		duFragment = DateDuFragment.newInstance(btnDateDu);
		
		btnDateA.setOnClickListener(this);
		btnDateDu.setOnClickListener(this);
		btnAffiche.setOnClickListener(this);
		
		return rootView;
	}
	
	
	private int getDuYear() {
		return duFragment.getYear();
	}
	
	private int getDuMonth() {
		return duFragment.getMonth();
	}
	
	private int getDuDays() {
		return duFragment.getDay();
	}
	
	private int getADays() {
		return daFragment.getDay();
	}
	
	private int getAMonth() {
		return daFragment.getMonth();
	}
	
	private int getAYear() {
		return daFragment.getYear();
	}
	
	private class LoadData extends AsyncTask<String, String, String> {
		
		private ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Patientez...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			
			Connection.Response res = null;
			
			String yearDu = String.valueOf(getDuYear()).substring(2);
			String yearA = String.valueOf(getAYear()).substring(2);
			
			
			try {
				res = Jsoup.connect(CcpConstant.URL_RELEVET_COMPTE)
						.data(CcpConstant.TAG_DAYS_DU, String.valueOf(getDuDays()))
						.data(CcpConstant.TAG_MONTH_DU, String.valueOf(getDuMonth()))
						.data(CcpConstant.TAG_YEAR_DU, yearDu)
						.data(CcpConstant.TAG_DAYS_A, String.valueOf(getADays()))
						.data(CcpConstant.TAG_MONTH_A, String.valueOf(getAMonth()))
						.data(CcpConstant.TAG_YEAR_A, yearA)
						.data(CcpConstant.TAG_SUBMIT, "Afficher >>")
						.data(CcpConstant.TAG_MM_INSERT, "form2")
						.data(CcpConstant.TAG_ID_URL, preferences.getIdUrl())
						.cookie(CcpConstant.COOKIE_NAME, preferences.getCookieValue())
					    .method(Method.POST)
						.timeout(CcpConstant.TIME_OUT)
					    .execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				return res.body().toString();
			} catch (NullPointerException e) {
				e.printStackTrace();
				return e.getMessage().toString();
			}
			
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			
			if (result.contains("Read timed out") && nombreRequete < NOMBRE_MAX_REQUETE) {
				nombreRequete++;
				Crouton.makeText(getActivity(),
						"Tentative de connexion au serveur " + nombreRequete,
						Style.INFO).show();
				new LoadData().execute();
			}
			else if (nombreRequete >= NOMBRE_MAX_REQUETE) {
				Crouton.makeText(getActivity(),
						"Serveur indisponible, réessayer plus tard",
						Style.ALERT).show();
			} 
			else {
				Intent intent = new Intent(getActivity(), ResultReleveActivity.class);
				intent.putExtra("html", result);
				startActivity(intent);
				System.out.println(result);
			}
				
			
		}
		
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		
		case R.id.btn_date_du:
			
			duFragment.show(getActivity().getSupportFragmentManager(), "du");	
			
			break;
			
		case R.id.btn_date_a:
			
			daFragment.show(getActivity().getSupportFragmentManager(), "a");
			
			break;
			
			
		case R.id.btn_recherche:
			
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			
			boolean error = false;
			
			if (getDuYear() < 2012 || getAYear() < 2012) {
				error = true;
				Crouton.makeText(getActivity(), "La date ne doit pas être inférieure à 2012", Style.ALERT).show();
			}
 			
			if (getDuYear() > year) {
				error = true;
				Crouton.makeText(getActivity(), "Vérifiez l'année du premier champ !", Style.ALERT).show();
			}
			
			if (getAYear() > year) {
				error = true;
				Crouton.makeText(getActivity(), "Vérifiez l'année du deuxième champ !", Style.ALERT).show();
			}
			

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    	
			try {
				Date dateDu = sdf.parse(getDuYear() + "-" + getDuMonth() + "-" + getDuDays());
				Date dateA = sdf.parse(getAYear() + "-" + getAMonth() + "-" + getADays());

		    	System.out.println(sdf.format(dateDu));
		    	System.out.println(sdf.format(dateA));

		    	if(dateDu.compareTo(dateA)>0){
		    		System.out.println("Date1 is after Date2");
		    		error = true;
					Crouton.makeText(getActivity(), "La première date est supérieure à la deuxième",
							Style.ALERT).show();
		    	}else if(dateDu.compareTo(dateA)==0){
		    		error = true;
					Crouton.makeText(getActivity(), "La première date est la même que la deuxième",
							Style.ALERT).show();
		    	}
			} catch (ParseException e) {
				e.printStackTrace();
			}
	    	
			if (!error) {
				new LoadData().execute();
			}
			
			break;

		default:
			break;
		}
		
	}

}
