package com.squalala.ccpalgrieposte.fargment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squalala.ccpalgrieposte.R;
import com.squalala.ccpalgrieposte.utils.Preferences;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : AccueilFragment.java
 * Date : 25 oct. 2014
 * 
 */
public class AccueilFragment extends Fragment {
	
	@InjectView(R.id.compte_ccp) TextView compteCcp;
	@InjectView(R.id.cle_ccp) TextView cleCcp;
	@InjectView(R.id.date_last_visit) TextView dateLastVisit;
	
	private Preferences preference;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_accueil, container, false);
		ButterKnife.inject(this, rootView);
		
		preference = new Preferences(getActivity());
		
		compteCcp.setText(Html.fromHtml("<font color='#53869D'> Compte CCP : "
		  	    	+"</font>" + preference.getUsername()), TextView.BufferType.SPANNABLE);
		cleCcp.setText(Html.fromHtml("<font color='#53869D'> Clé : "
		  	    	+"</font>" + preference.getCle()), TextView.BufferType.SPANNABLE);
		dateLastVisit.setText(Html.fromHtml("<font color='#53869D'> Date du dernier accès : "
		  	    	+"</font>" + preference.getLastVisit()), TextView.BufferType.SPANNABLE);
		
		return rootView;
	}
	
	
	

}
