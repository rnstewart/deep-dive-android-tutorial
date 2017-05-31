package edu.cnm.bootcamp.russell.myapplication;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import edu.cnm.bootcamp.russell.myapplication.fragments.ListFragment;

public class AppActivity extends AppCompatActivity implements ListFragment.OnFragmentInteractionListener {
    ListFragment mListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        loadFragment();
    }

    private void loadFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mListFragment = ListFragment.newInstance();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, mListFragment);
        fragmentTransaction.commit();
    }

    private void removeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(mListFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onCloseClicked() {
        removeFragment();
    }
}
