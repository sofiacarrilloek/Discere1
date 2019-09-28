package com.jhpat.discere;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

public class TabsActivity extends AppCompatActivity implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
String usuario,tipo,email,nombre,apellido;
int retorno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
cargarP();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        Toast.makeText(this,tipo,Toast.LENGTH_LONG).show();
    }

    // Métodos de la interfaz ActionBar.TabListener
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    // Métodos de la interfaz ViewPager.OnPageChangeListener
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        getSupportActionBar().setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * A placeholder fragment containing a simple view.

     public static class PlaceholderFragment extends Fragment {
     /**
     * The fragment argument representing the section number for this
     * fragment.

     private static final String ARG_SECTION_NUMBER = "section_number";

     public PlaceholderFragment() {
     }

     /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    /**
     public static PlaceholderFragment newInstance(int sectionNumber) {
     PlaceholderFragment fragment = new PlaceholderFragment();
     Bundle args = new Bundle();
     args.putInt(ARG_SECTION_NUMBER, sectionNumber);
     fragment.setArguments(args);
     return fragment;
     }

     }
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment tabFragment = null;
            if (tipo.equals("Coach")){
            switch (position){

                case 0:
                    tabFragment = new TabAFragment();

                    break;
                case 1:
                    tabFragment = new TabBFragment();
                    break;
            }
            }else{
                tabFragment = new TabAFragment();
            }
            return tabFragment;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            if (tipo.equals("Coach")){
                retorno =2;
            }else{
                retorno =1;
            }
            return retorno;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            String section = null;
            if (tipo.equals("Coach")){
                switch (position) {
                    case 0:

                        section = "FELLOW SESSION";
                        break;
                    case 1:
                        section = "SCHEDULE";

                        break;
                }
            }  else{
                section = "SCHEDULE";
                }


            return section;
        }
    }

    //Preferencias
    private  void cargarP()
    {
        SharedPreferences preferencia =getSharedPreferences("Credenciales", Context.MODE_PRIVATE);
        usuario= preferencia.getString("ID2", "NO EXISTE");
        tipo=preferencia.getString("TIPO2", "holaperro");
        email=preferencia.getString("EMAIL2","No hay email");
        nombre=preferencia.getString("NAME2","HOLA Crack");
        apellido=preferencia.getString("LAST_NAME2","hola");
    }//Fin cargar preferencias
}
