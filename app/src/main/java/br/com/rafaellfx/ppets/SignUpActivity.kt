package br.com.rafaellfx.ppets

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.rafaellfx.ppets.ui.signup.SignUpFragment

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, SignUpFragment.newInstance())
                .commitNow()
        }
    }
}
