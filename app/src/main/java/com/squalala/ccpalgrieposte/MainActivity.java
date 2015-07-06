/**
 * 
 */
package com.squalala.ccpalgrieposte;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.squalala.ccpalgrieposte.fargment.AccueilFragment;
import com.squalala.ccpalgrieposte.fargment.CarnetChequeFragment;
import com.squalala.ccpalgrieposte.fargment.ExtraitCompteFragment;
import com.squalala.ccpalgrieposte.fargment.MotDePasseFragment;
import com.squalala.ccpalgrieposte.fargment.RelevetCompteFragment;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : MainActivity.java
 * Date : 25 oct. 2014
 * 
 */
public class MainActivity extends AppCompatActivity {

	private Drawer result = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);



		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);


		//Create the drawer
		result = new DrawerBuilder(this)
				//this layout have to contain child layouts
				.withActivity(this)
				.withToolbar(toolbar)
				.withActionBarDrawerToggleAnimated(true)
				.addDrawerItems(
						new PrimaryDrawerItem().withName(R.string.accueil).withIcon(FontAwesome.Icon.faw_home).withIdentifier(0),
						new PrimaryDrawerItem().withName(R.string.extrait_compte).withIcon(FontAwesome.Icon.faw_file_text).withIdentifier(1),
						new PrimaryDrawerItem().withName(R.string.relevet).withIcon(FontAwesome.Icon.faw_list_alt).withIdentifier(2),
						new PrimaryDrawerItem().withName(R.string.carnet).withIcon(FontAwesome.Icon.faw_credit_card).withIdentifier(3),
						new PrimaryDrawerItem().withName(R.string.mot_de_passe).withIcon(FontAwesome.Icon.faw_key).withIdentifier(4),
						new SecondaryDrawerItem().withName(R.string.contact).withIcon(FontAwesome.Icon.faw_envelope_o).withIdentifier(5)
				)
				.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
					@Override
					public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem drawerItem) {

						Fragment fragment = null;

						if (drawerItem != null && drawerItem instanceof Nameable) {

							switch (drawerItem.getIdentifier())
							{
								case 0:

									fragment = new AccueilFragment();
									getSupportActionBar().setTitle(((Nameable) drawerItem).getNameRes());
									getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

									break;

								case 1:

									fragment = new ExtraitCompteFragment();
									getSupportActionBar().setTitle(((Nameable) drawerItem).getNameRes());
									getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

									break;

								case 2:

									fragment = new RelevetCompteFragment();
									getSupportActionBar().setTitle(((Nameable) drawerItem).getNameRes());
									getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

									break;

								case 3:

									fragment = new CarnetChequeFragment();
									getSupportActionBar().setTitle(((Nameable) drawerItem).getNameRes());
									getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

									break;

								case 4:

									fragment = new MotDePasseFragment();
									getSupportActionBar().setTitle(((Nameable) drawerItem).getNameRes());
									getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

									break;

								case 5:

									Intent email = new Intent(Intent.ACTION_SEND);
									email.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@squalala.com"});
									email.putExtra(Intent.EXTRA_SUBJECT, "CCP ALGÉRIE POSTE");
									email.putExtra(Intent.EXTRA_TEXT, "");
									email.setType("message/rfc822");
									startActivity(Intent.createChooser(email, "Choisissez :"));

									break;
							}
						}




						return false;
					}
				})
				.withSavedInstance(savedInstanceState)
				.build();


		getSupportActionBar().setTitle(getString(R.string.accueil));
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AccueilFragment()).commit();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		//add the values which need to be saved from the drawer to the bundle
		outState = result.saveInstanceState(outState);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onBackPressed() {
		//handle the back press :D close the drawer first and if the drawer is closed close the activity
		if (result != null && result.isDrawerOpen()) {
			result.closeDrawer();
		} else {
			super.onBackPressed();
		}
	}
}
