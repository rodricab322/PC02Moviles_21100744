package dem.rodcab.pc02moviles.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dem.rodcab.pc02moviles.MainActivity
import dem.rodcab.pc02moviles.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etDNILogin: EditText = findViewById(R.id.etDNILogin)
        val etClaveLogin: EditText = findViewById(R.id.etClaveLogin)
        val btLogin: Button = findViewById(R.id.btLogin)
        val btRegistrar: Button = findViewById(R.id.btRegistrar)
        val db = FirebaseFirestore.getInstance()

        btRegistrar.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        btLogin.setOnClickListener {
            val DNI = etDNILogin.text.toString().trim()
            val clave = etClaveLogin.text.toString().trim()


            if (DNI.isEmpty() || clave.isEmpty()) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Por favor ingresa el DNI y la clave",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }


            db.collection("PC02_Moviles")
                .document(DNI)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val storedClave = document.getString("clave")
                        if (storedClave == clave) {

                            Snackbar.make(
                                findViewById(android.R.id.content),
                                "Inicio de sesión exitoso",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {

                            Snackbar.make(
                                findViewById(android.R.id.content),
                                "Clave incorrecta",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "DNI no encontrado",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Error de conexión: ${exception.message}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
        }
    }
}