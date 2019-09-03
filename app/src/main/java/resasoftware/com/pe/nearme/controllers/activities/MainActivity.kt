package resasoftware.com.pe.nearme.controllers.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import resasoftware.com.pe.nearme.R
import resasoftware.com.pe.nearme.controllers.fragments.AccountFragment
import resasoftware.com.pe.nearme.controllers.fragments.MapFragment
import resasoftware.com.pe.nearme.controllers.fragments.SettingsFragment
import android.content.DialogInterface
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.view.KeyEvent
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_settings.*
import resasoftware.com.pe.nearme.models.Preferences
import resasoftware.com.pe.nearme.models.User


class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        navigateTo(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navigateTo(navView.menu.findItem(R.id.navigation_map))
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.message_exit))
                .setMessage(getString(R.string.message_are_you_sure))
                .setNegativeButton(android.R.string.cancel, null)//sin listener
                .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->  this.finish()})
                .show()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }



    private fun getFragmentFor(item: MenuItem): Fragment {
        return when(item.itemId) {
            R.id.navigation_map -> MapFragment()
            R.id.navigation_account -> AccountFragment()
            R.id.navigation_settings -> SettingsFragment()
            else -> MapFragment()
        }
    }
    private fun navigateTo(item: MenuItem): Boolean {
        item.isChecked = true

        return supportFragmentManager
            .beginTransaction()
            .replace(R.id.content, getFragmentFor(item))
            .commit() > 0
    }
}
