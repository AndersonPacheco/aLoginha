package br.com.alojinha.ui.activity;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import br.com.alojinha.R;
import br.com.alojinha.databinding.ActivityMainBinding;
import br.com.alojinha.databinding.NavHeaderMainBinding;
import br.com.alojinha.ui.fragment.HomeFragment;
import br.com.alojinha.ui.fragment.SobreFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ActivityMainBinding binding;
    private NavHeaderMainBinding bindingNavHeaderMain;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setToolbar();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.appBarMain.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);

        bindingNavHeaderMain = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0));

        if(savedInstanceState == null){
            onNavigationItemSelected(binding.navView.getMenu().getItem(0));
        }

    }

    private void setToolbar() {
        setSupportActionBar(binding.appBarMain.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                this.mTitle = getResources().getString(R.string.title_alodjinha);
                break;
            case R.id.nav_sobre:
                fragment = new SobreFragment();
                this.mTitle = getResources().getString(R.string.title_sobre);
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager frgManager = getSupportFragmentManager();
            frgManager.beginTransaction().replace(binding.appBarMain.contentMain.contentFrame.getId(), fragment).commit();
        }

        menuItem.setChecked(true);

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(mTitle);
    }
}
