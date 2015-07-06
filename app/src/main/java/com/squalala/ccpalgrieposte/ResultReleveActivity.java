package com.squalala.ccpalgrieposte;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.ListView;

import com.squalala.ccpalgrieposte.adapter.RelevetAdapter;
import com.squalala.ccpalgrieposte.model.RelevetRow;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : ResultReleveActivity.java
 * Date : 26 oct. 2014
 * 
 */
public class ResultReleveActivity extends AppCompatActivity {
	
	@InjectView(R.id.list) ListView listView;
	
	private ArrayList<RelevetRow> listReleve = new ArrayList<RelevetRow>();
	private RelevetAdapter adapter;
	private String htmlToParse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_result_relevet);
		
		getSupportActionBar().setBackgroundDrawable(new  
				   ColorDrawable(Color.parseColor("#f4ec27"))); 
		
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		htmlToParse = getIntent().getStringExtra("html");
		
		ButterKnife.inject(this);
		
		adapter = new RelevetAdapter(this, listReleve);
		listView.setAdapter(adapter);
		
		new LoadItems().execute();
	}
	
	
	private class LoadItems extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected String doInBackground(String... params) {
			
			Document doc = Jsoup.parse(htmlToParse);
			
			Elements elements =  doc.select("td[class^=xl]");
			
			ArrayList<String> list = new ArrayList<String>();
			
			for ( int i = 0; i < elements.size(); i++ ) {
			//	System.out.println("RES # --> " +elements.get(i).text());
				if (!isContainsWord(elements.get(i).text()))
					list.add(elements.get(i).text());
			}
			
		//	System.out.println("TAILLE # --> " +list.size());
			
			int numberCellule = list.size() - 10;
			int u = 0;
			
			ArrayList<String> listRowReleve = new ArrayList<String>();
				
			for ( int i = 0; i < numberCellule; i++) {
				
				if ( u < 7) {
					System.out.println("RESULT -->" + list.get(i));
					listRowReleve.add(list.get(i));
				}
				
				u++;
				
				if ( u == 7) {
					
					RelevetRow relevetRow = new RelevetRow();
					
					relevetRow.setDate(listRowReleve.get(0));
					relevetRow.setCodeOperation(listRowReleve.get(1));
					relevetRow.setCompte(listRowReleve.get(2));
					relevetRow.setDebit(listRowReleve.get(3));
					relevetRow.setCredit(listRowReleve.get(4));
					relevetRow.setTaxe(listRowReleve.get(5));
					relevetRow.setAvoir(listRowReleve.get(6));
					
					listReleve.add(relevetRow);
					
					listRowReleve.clear();
					
					System.out.println("BOOM");
					u = 0;
				}
				
			}
			
			RelevetRow relevetRow = new RelevetRow();
			relevetRow.setLastRow(true);
			relevetRow.setDebit(list.get(list.size() - 7));
			relevetRow.setCredit(list.get(list.size() - 6));
			
			listReleve.add(relevetRow);
			
			System.out.println("RESULT ***-->" + list.get(list.size() - 7));
			System.out.println("RESULT ***-->" + list.get(list.size() - 6));
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			adapter.notifyDataSetChanged();
			setProgressBarIndeterminateVisibility(false);
		}
		
		
		
	}
	
	
	private static boolean isContainsWord(String element) {
		String []  words = {"Débit" , "Compte", "Avoir", "Taxe", "Code opération", "Date" , "Crédit" };
		
		for (String word : words)
			if (element.equals(word))
				return true;
			
			return false;
	}
	
	
	
	
	

}
