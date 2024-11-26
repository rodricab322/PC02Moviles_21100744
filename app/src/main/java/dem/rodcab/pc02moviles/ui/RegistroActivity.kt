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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dem.rodcab.pc02moviles.R
import dem.rodcab.pc02moviles.ui.model.UserModel

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etDNIRegistro: EditText = findViewById(R.id.etDNIRegistro)
        val etNombreRegistro: EditText = findViewById(R.id.etNombreRegistro)
        val etClaveRegistro: EditText = findViewById(R.id.etClaveRegistro)
        val etConfirmacionRegistro: EditText = findViewById(R.id.etConfirmacionRegistro)
        val btGuardarRegistro: Button = findViewById(R.id.btGuardarRegistro)
        val db = FirebaseFirestore.getInstance()

        btGuardarRegistro.setOnClickListener {
            val dni = etDNIRegistro.text.toString().trim()
            val nombre = etNombreRegistro.text.toString().trim()
            val clave = etClaveRegistro.text.toString().trim()
            val confirmacionClave = etConfirmacionRegistro.text.toString().trim()

            if (dni.isEmpty() || nombre.isEmpty() || clave.isEmpty() || confirmacionClave.isEmpty()) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Por favor, complete todos los campos",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (clave != confirmacionClave) {
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Las contraseñas no coinciden",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val userModel = UserModel(dni, nombre, clave)

            db.collection("PC02_Moviles")
                .document(dni)
                .set(userModel)
                .addOnCompleteListener {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Registro exitoso",
                        Snackbar.LENGTH_SHORT
                    ).show()


                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { error ->
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Error al guardar en Firestore: ${error.message}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
        }
    }
}