package com.squalala.ccpalgrieposte.model;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : RelevetRow.java
 * Date : 26 oct. 2014
 * 
 */
public class RelevetRow {
	
	private String date;
	private String codeOperation;
	private String compte;
	private String debit;
	private String credit;
	private String taxe;
	private String avoir;
	
	private boolean lastRow = false;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCodeOperation() {
		return codeOperation;
	}
	public void setCodeOperation(String codeOperation) {
		this.codeOperation = codeOperation;
	}
	public String getCompte() {
		return compte;
	}
	public void setCompte(String compte) {
		this.compte = compte;
	}
	public String getDebit() {
		return debit;
	}
	public void setDebit(String debit) {
		this.debit = debit;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getTaxe() {
		return taxe;
	}
	public void setTaxe(String taxe) {
		this.taxe = taxe;
	}
	public String getAvoir() {
		return avoir;
	}
	public void setAvoir(String avoir) {
		this.avoir = avoir;
	}
	public boolean isLastRow() {
		return lastRow;
	}
	public void setLastRow(boolean lastRow) {
		this.lastRow = lastRow;
	}

}
