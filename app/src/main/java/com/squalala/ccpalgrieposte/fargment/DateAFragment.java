package com.squalala.ccpalgrieposte.fargment;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.squalala.ccpalgrieposte.common.CcpConstant;

import java.util.Calendar;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : DateDialogFragment.java
 * Date : 10 oct. 2014
 * 
 */
public class DateAFragment extends DialogFragment 
	implements OnDateSetListener {
	
	private BootstrapButton btnDateArrive;
	private String date = null;
	private int year, month, day;
	
	
	public static DateAFragment newInstance(BootstrapButton arrive) {
		DateAFragment dateDialogFragment = new DateAFragment();
		dateDialogFragment.btnDateArrive  = arrive;
		return dateDialogFragment;
	}
	
	public String getDate() {
		return date;
	}
	
	public int getYear() {
		return year;
	}
	
	public int getMonth() {
		return month + 1;
	}
	
	public int getDay() {
		return day;
	}
	
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		final Calendar c = Calendar.getInstance();
		
		if (date == null ) {
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
		}
 
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		
		this.year = year;
		this.month = monthOfYear;
		this.day = dayOfMonth;
		
		String yearStr = String.valueOf(year);
		yearStr = yearStr.substring(2);
		
		date = dayOfMonth + "-" + CcpConstant.MOIS[monthOfYear] + "-" + yearStr;
		
		System.out.println("D : "  + day + " M : " + month + " Y : " + year);
		
		btnDateArrive.setText(date);
	}
	
	

}
