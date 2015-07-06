/**
 * 
 */
package com.squalala.ccpalgrieposte.fargment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.squalala.ccpalgrieposte.R;
import com.squalala.ccpalgrieposte.common.CcpConstant;
import com.squalala.ccpalgrieposte.utils.Preferences;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : MotDePasseFragment.java
 * Date : 25 oct. 2014
 * 
 */
public class MotDePasseFragment extends Fragment 
	implements OnClickListener {
	
	
	@InjectView(R.id.btn_change_mot_de_passe) BootstrapButton btnChangeMdp;
	@InjectView(R.id.edit_ancien_mdp) BootstrapEditText editAncienMdp;
	@InjectView(R.id.edit_nouveau_mdp) BootstrapEditText editNouveauMdp;
	@InjectView(R.id.edit_confirme_nouveau_mdp) BootstrapEditText editConfirmeMdp;
	@InjectView(R.id.edit_phrase_secrete) BootstrapEditText editPhraseSecrete;
	
	private Preferences preferences;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mot_de_passe, container, false);
		ButterKnife.inject(this, rootView);
		

		btnChangeMdp.setOnClickListener(this);
		
		preferences = new Preferences(getActivity());
		
		return rootView;
	}
	
	
	private String getAncienMdp() {
		return editAncienMdp.getText().toString().trim();
	}
	
	private String getNouveauMdp() {
		return editNouveauMdp.getText().toString().trim();
	}
	
	private String getConfirmeMdp() {
		return editConfirmeMdp.getText().toString().trim();
	}
	
	private String getPhraseSecreteMdp() {
		return editPhraseSecrete.getText().toString().trim();
	}
	
	
	private class ChangeMotDePasse extends AsyncTask<String, String, String> {

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
			
			try {
				res = Jsoup.connect(CcpConstant.URL_CHANGE_PASSWORD)
						.data(CcpConstant.TAG_ANCIEN_MDP , getAncienMdp())
						.data(CcpConstant.TAG_NOUVEAU_MDP, getNouveauMdp())
						.data(CcpConstant.TAG_CONFIRME_NOUVEAU_MDP, getConfirmeMdp())
						.data(CcpConstant.TAG_PHRASE_SECRETE, getPhraseSecreteMdp())
						.data(CcpConstant.TAG_ID_URL, preferences.getIdUrl())
						.data(CcpConstant.TAG_MM_INSERT, "form2")
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
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			System.out.println(result);
			pDialog.dismiss();
			
			if (result.contains("Mot de  passe incorrect")) {
				Crouton.makeText(getActivity(), "Mot de passe incorrect", 
						Style.ALERT).show();
				
			}
			else if (result.contains("Le nouveau mot de passe")) {
				Crouton.makeText(getActivity(), "Le nouveau mot de passe doit être différent de l’ancien", 
						Style.ALERT).show();
				
			}
			else if (result.contains("Mot de passe modifi")) {
				Crouton.makeText(getActivity(), "Mot de passe modifié !", 
						Style.CONFIRM).show();
				editAncienMdp.setText("");
				editConfirmeMdp.setText("");
				editNouveauMdp.setText("");
				editPhraseSecrete.setText("");
				
			}
			
		}
		
	}

	@Override
	public void onClick(View v) {
		
		boolean error = false;
		
		if (!(getAncienMdp().length() == 4)) {
			error = true;
			Crouton.makeText(getActivity(), "Vérifiez l'ancien mot de passe", Style.ALERT).show();
		}
		
		if (!(getNouveauMdp().length() == 4)) {
			error = true;
			Crouton.makeText(getActivity(), "Vérifiez le nouveau mot de passe", Style.ALERT).show();
		}
		
		if (!(getConfirmeMdp().length() == 4)) {
			error = true;
			Crouton.makeText(getActivity(), "Vérifiez les champs", Style.ALERT).show();
		}
		
		if (!getConfirmeMdp().equals(getNouveauMdp())) {
			error = true;
			Crouton.makeText(getActivity(), "La confirmation ne correspond pas au nouveau mot de passe",
					Style.ALERT).show();
		}
		
		if (getPhraseSecreteMdp().length() == 0) {
			error = true;
			Crouton.makeText(getActivity(), "Vérifiez votre phrase secrète", Style.ALERT).show();
		}
		
		if (!error)
			new ChangeMotDePasse().execute();
		
	}

}
