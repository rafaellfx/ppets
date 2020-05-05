package br.com.rafaellfx.ppets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.rafaellfx.ppets.ui.lost.LostFragment

class LostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lost_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LostFragment.newInstance())
                .commitNow()
        }
    }
}
