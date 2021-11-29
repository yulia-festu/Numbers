package edu.festu.numberfacts

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.festu.numberfacts.databinding.ActivityLoginBinding

/*
 * Аутентификация средствами Firebase. В случае успешной авторизации переход в MainActivity.
 * */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        //будет использоваться аунтентификация с помощью Google
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) //ресурс генерируется сам, пересоздавать не надобно
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Действия по обработке результата аутентификации. Используется вместо устаревшего startActivityForResult
        val result = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, getString(R.string.somethig_wrong), Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
        val login = binding.login
        login.setOnClickListener {
            //Запуск аутентификации с помощью Google.
            result.launch(googleSignInClient.signInIntent)
        }
    }
    //Изменение обновление UI после аутентификации
    private fun updateUiWithUser() {
        val welcome = getString(R.string.welcome)

        Toast.makeText(
            applicationContext,
            "$welcome ${Firebase.auth.currentUser?.displayName}",
            Toast.LENGTH_LONG
        ).show()
        val i = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(i)
    }
    //Непосредственно проверка того, что аутентиикация прошла успешно.
    //Здесь стоит также обработать неудачный исход
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) {
                    if(it.isSuccessful)
                    {
                        updateUiWithUser()
                    }
            }
    }
}

