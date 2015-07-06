package com.squalala.ccpalgrieposte.common;

public class CcpConstant {
	
	
	public static final String[] MOIS = {"Jan.", "Fév.", "Mar.", "Avr.", "Mai.", "Jun.", "Jul.", "Aoû.", "Sep.", "Oct.", "Nov.", "Déc."};

	public static final int TIME_OUT = 10 * 2000; // 20 secondes

	public static final String COOKIE_NAME = "PHPSESSID";
	public static final String URL_CONNEXION = "https://eccp.poste.dz/indexbas.php";
	public static final String URL_CAPTCHA = "https://eccp.poste.dz/image.php";
	
	public static final String URL_EXTRAIT_COMPTE = "https://eccp.poste.dz/work1.php?cc=";
	public static final String URL_CHANGE_PASSWORD = "https://eccp.poste.dz/work4.php";
	public static final String URL_RELEVET_COMPTE = "https://eccp.poste.dz/work3.php";
	
	public static final String TAG_DAYS_DU = "d1";
	public static final String TAG_MONTH_DU = "d2";
	public static final String TAG_YEAR_DU = "d3";
	
	public static final String TAG_DAYS_A = "f1";
	public static final String TAG_MONTH_A = "f2";
	public static final String TAG_YEAR_A = "f3";
	
	public static final String TAG_SUBMIT = "Submit";
	public static final String TAG_ID_URL = "cc";
	
	public static final String TAG_COMPTE = "compte";
	public static final String TAG_CODE = "passe";
	public static final String TAG_CAPTCHA = "passe2";
	public static final String TAG_MM_INSERT = "MM_insert";
	public static final String TAG_NOMBRE_CARNET_POSTAUX = "nbr1";
	public static final String TAG_NOMBRE_CARNET_PAIEMENTS = "nbr2";
	
	public static final String TAG_ANCIEN_MDP = "passe";
	public static final String TAG_NOUVEAU_MDP = "passen";
	public static final String TAG_CONFIRME_NOUVEAU_MDP = "passecn";
	public static final String TAG_PHRASE_SECRETE = "phrase";
	
	public static final String URL_CARNET_CHEQUE = "https://eccp.poste.dz/work7.php";
	

}
