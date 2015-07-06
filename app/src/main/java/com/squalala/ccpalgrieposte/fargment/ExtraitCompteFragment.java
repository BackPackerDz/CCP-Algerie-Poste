package com.squalala.ccpalgrieposte.fargment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.squalala.ccpalgrieposte.R;
import com.squalala.ccpalgrieposte.common.CcpConstant;
import com.squalala.ccpalgrieposte.utils.Preferences;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ExtraitCompteFragment.java
 * Date : 25 oct. 2014
 * 
 */
public class ExtraitCompteFragment extends Fragment {
	
	@InjectView(R.id.code_operation) TextView codeOperation;
	@InjectView(R.id.compte) TextView compte;
	@InjectView(R.id.debit) TextView debit;
	@InjectView(R.id.credit) TextView credit;
	@InjectView(R.id.taxe) TextView taxe;
	@InjectView(R.id.date_extrait) TextView dateExtrait;
	@InjectView(R.id.ancien_avoir) TextView ancienAvoir;
	@InjectView(R.id.nouvel_avoir) TextView nouvelAvoir;
	@InjectView(R.id.fontLoad) FontAwesomeText fontLoad; 
	
	private Preferences preferences;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_extrait_compte, container, false);
		ButterKnife.inject(this, rootView);
		

		preferences = new Preferences(getActivity());
		
		new LoadData().execute();
		
		return rootView;
	}
	
	
	private class LoadData extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			fontLoad.startRotate(getActivity(), true, FontAwesomeText.AnimationSpeed.MEDIUM);
		}

		@Override
		protected String doInBackground(String... params) {
			
			Connection.Response res = null;
			
			try {
				res = Jsoup.connect(CcpConstant.URL_EXTRAIT_COMPTE + preferences.getIdUrl())
						.cookie(CcpConstant.COOKIE_NAME, preferences.getCookieValue())
					    .method(Method.GET)
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
			
			Document doc = Jsoup.parse(result);
			Elements elements =  doc.select("strong font[color=#333399]");
			
			dateExtrait.setText(Html.fromHtml("<font color='#53869D'> Extrait Effectué le : "
		  	    	+"</font>" + elements.get(1).text()), TextView.BufferType.SPANNABLE);
			
			ancienAvoir.setText(Html.fromHtml("<font color='#53869D'> Ancien Avoir : "
		  	    	+"</font>" + elements.get(4).text()), TextView.BufferType.SPANNABLE);
			
			nouvelAvoir.setText(Html.fromHtml("<font color='#53869D'> Nouvel Avoir : "
		  	    	+"</font>" + elements.get(5).text()), TextView.BufferType.SPANNABLE);
			
			Elements elements2 =  doc.select("td[class^=xl]");
			
			codeOperation.setText(Html.fromHtml("<font color='#53869D'> Code opération : "
		  	    	+"</font>" + elements2.get(5).text()), TextView.BufferType.SPANNABLE);
			compte.setText(Html.fromHtml("<font color='#53869D'> Compte : "
		  	    	+"</font>" + elements2.get(6).text()), TextView.BufferType.SPANNABLE);
			debit.setText(Html.fromHtml("<font color='#53869D'> Débit : "
		  	    	+"</font>" + elements2.get(7).text()), TextView.BufferType.SPANNABLE);
			credit.setText(Html.fromHtml("<font color='#53869D'> Crédit : "
		  	    	+"</font>" + elements2.get(8).text()), TextView.BufferType.SPANNABLE);
			taxe.setText(Html.fromHtml("<font color='#53869D'> Taxe : "
		  	    	+"</font>" + elements2.get(9).text()), TextView.BufferType.SPANNABLE);
			
			fontLoad.stopAnimation();
		}
		
	}
	
	
	
	
	

}
