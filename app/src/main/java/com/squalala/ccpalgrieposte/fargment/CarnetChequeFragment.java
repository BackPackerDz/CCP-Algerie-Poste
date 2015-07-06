/**
 * 
 */
package com.squalala.ccpalgrieposte.fargment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.beardedhen.androidbootstrap.BootstrapButton;
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
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : CarnetChequeFragment.java
 * Date : 25 oct. 2014
 * 
 */
public class CarnetChequeFragment extends Fragment 
	implements OnClickListener {
	
	@InjectView(R.id.btn_commande_postaux) BootstrapButton btnCmdPostaux;
	@InjectView(R.id.btn_commande_paiement) BootstrapButton btnCmdPaiement;
	@InjectView(R.id.spinner_postaux) Spinner spinPostaux;
	@InjectView(R.id.spinner_paiement) Spinner spinPaiement;
	
	private Preferences preferences;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_carnet_cheque, container, false);
		ButterKnife.inject(this, rootView);
		

		
		btnCmdPaiement.setOnClickListener(this);
		btnCmdPostaux.setOnClickListener(this);
		
		preferences = new Preferences(getActivity());
		
		return rootView;
	}
	
	public String getNombrePostaux() {
		return spinPostaux.getSelectedItem().toString();
	}
	
	public String getNombrePaiements() {
		return spinPaiement.getSelectedItem().toString();
	}
	
	private class CommandePostaux extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			
			Connection.Response res = null;
			
			try {
				res = Jsoup.connect(CcpConstant.URL_CARNET_CHEQUE)
						.data(CcpConstant.TAG_NOMBRE_CARNET_POSTAUX, getNombrePostaux())
						.data(CcpConstant.TAG_SUBMIT, "Commander")
						.data(CcpConstant.TAG_ID_URL, preferences.getIdUrl())
						.data(CcpConstant.TAG_MM_INSERT, "form1")
						.cookie(CcpConstant.COOKIE_NAME, preferences.getCookieValue())
					    .method(Method.POST)
						.timeout(CcpConstant.TIME_OUT)
					    .execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
			if (!result.equals("error")) {
				Document doc = Jsoup.parse(result);
				Elements elements =  doc.select("font[color=#666666][size=2]");
				Crouton.makeText(getActivity(), elements.text().replace("...", ""), 
						Style.INFO).show();

			}
			
		}
		
	}
	
	private class CommandePaiement extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			Connection.Response res = null;
			
			try {
				res = Jsoup.connect(CcpConstant.URL_CARNET_CHEQUE)
						.data(CcpConstant.TAG_NOMBRE_CARNET_PAIEMENTS, getNombrePaiements())
						.data(CcpConstant.TAG_SUBMIT, "Commander")
						.data(CcpConstant.TAG_ID_URL, preferences.getIdUrl())
						.data(CcpConstant.TAG_MM_INSERT, "form1")
						.cookie(CcpConstant.COOKIE_NAME, preferences.getCookieValue())
					    .method(Method.POST)
					    .execute();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println(result);
			if (!result.equals("error")) {
				Document doc = Jsoup.parse(result);
				Elements elements =  doc.select("font[color=#666666][size=2]");
				Crouton.makeText(getActivity(), elements.text().replace("...", ""), 
						Style.INFO).show();

			}
		}
		
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.btn_commande_postaux:
			
			new CommandePostaux().execute();
			
			break;
			
		case R.id.btn_commande_paiement:
			
			new CommandePaiement().execute();
			
			break;

		default:
			break;
		}
		
	}
	
	
	

}
