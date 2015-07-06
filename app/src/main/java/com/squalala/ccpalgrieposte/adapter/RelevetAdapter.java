package com.squalala.ccpalgrieposte.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squalala.ccpalgrieposte.R;
import com.squalala.ccpalgrieposte.model.RelevetRow;

import java.util.List;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : AllerSimpleAdapter.java
 * Date : 12 oct. 2014
 * 
 */
public class RelevetAdapter extends BaseAdapter {
	
	private Activity activity;
    private LayoutInflater inflater;
    private List<RelevetRow> listRelevet;
    
    public RelevetAdapter(Activity activity, List<RelevetRow> listVols) {
    	this.listRelevet = listVols;
    	this.activity = activity;
	}

	@Override
	public int getCount() {
		return listRelevet.size();
	}

	@Override
	public Object getItem(int position) {
		return listRelevet.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "NewApi", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		 if (inflater == null)
	            inflater = (LayoutInflater) activity
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        if (convertView == null)
	            convertView = inflater.inflate(R.layout.row_relevet_compte, null);
	    	
	        TextView date = (TextView) convertView.findViewById(R.id.date_relevet);
	        TextView codeOperation = (TextView) convertView.findViewById(R.id.code_operation_relevet);
	        TextView compte = (TextView) convertView.findViewById(R.id.compte_relevet);
	        TextView debit = (TextView) convertView.findViewById(R.id.debit_relevet);
	        TextView credit = (TextView) convertView.findViewById(R.id.credit_relevet);
	        TextView taxe = (TextView) convertView.findViewById(R.id.taxe_relevet);
	        TextView avoir = (TextView) convertView.findViewById(R.id.avoir_relevet);
	        TextView total = (TextView) convertView.findViewById(R.id.total);
	        
		    RelevetRow relevetRow = listRelevet.get(position);
		    
		    if (relevetRow.isLastRow()) {
		    	date.setVisibility(View.GONE);
		    	codeOperation.setVisibility(View.GONE);
		    	compte.setVisibility(View.GONE);
		    	taxe.setVisibility(View.GONE);
		    	avoir.setVisibility(View.GONE);
		    	
		    	total.setVisibility(View.VISIBLE);
		    }
		    else {
		    	
		    	total.setVisibility(View.GONE);
		    	
		    	date.setVisibility(View.VISIBLE);
		    	codeOperation.setVisibility(View.VISIBLE);
		    	compte.setVisibility(View.VISIBLE);
		    	taxe.setVisibility(View.VISIBLE);
		    	avoir.setVisibility(View.VISIBLE);
		    	
		    	date.setText(relevetRow.getDate());
		    	
		        codeOperation.setText(Html.fromHtml("<font color='#53869D'> Code opération : "
			  	    	+"</font>" + relevetRow.getCodeOperation()), TextView.BufferType.SPANNABLE);
		        compte.setText(Html.fromHtml("<font color='#53869D'> Compte : "
			  	    	+"</font>" + relevetRow.getCompte()), TextView.BufferType.SPANNABLE);
		        taxe.setText(Html.fromHtml("<font color='#53869D'> Taxe : "
			  	    	+"</font>" + relevetRow.getTaxe()), TextView.BufferType.SPANNABLE);
		        avoir.setText(Html.fromHtml("<font color='#53869D'> Avoir : "
			  	    	+"</font>" + relevetRow.getAvoir()), TextView.BufferType.SPANNABLE);
		    }
		    
		    debit.setText(Html.fromHtml("<font color='#53869D'> Débit : "
		  	    	+"</font>" + relevetRow.getDebit()), TextView.BufferType.SPANNABLE);
	        credit.setText(Html.fromHtml("<font color='#53869D'> Crédit : "
		  	    	+"</font>" + relevetRow.getCredit()), TextView.BufferType.SPANNABLE);
	    	
	    	
	        	
		return convertView;
	}

}
