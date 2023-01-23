package com.yadav.pawdoption.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.yadav.pawdoption.MainActivity
import com.yadav.pawdoption.R
import com.yadav.pawdoption.model.ShelterPet
import com.yadav.pawdoption.persistence.FirebaseDatabaseSingleton
import com.yadav.pawdoption.persistence.PetDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.Instant

class UploadPet : AppCompatActivity() {
    var curFile: Uri? = null
    val imageRef = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_pet)

        setTitle("Pet Posting")

        val btnUploadPhoto = findViewById<Button>(R.id.btnUploadPhoto)
        btnUploadPhoto.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    pickImageFromGallery()
                }
            } else {
                pickImageFromGallery()
            }
        }



        val btnSubmit = findViewById<Button>(R.id.btnAnimalPostingSubmit)
        btnSubmit.setOnClickListener {
            // Pet name field reference
            val tilPetName = findViewById<TextInputLayout>(R.id.tilPetName)
            val tiPetName = findViewById<TextInputEditText>(R.id.tiPetName)

            // Pet age field reference
            val tilPetAge = findViewById<TextInputLayout>(R.id.tilPetAge)
            val tiPetAge = findViewById<TextInputEditText>(R.id.tiPetAge)

            // Radio group reference
            val rgBreed = findViewById<RadioGroup>(R.id.rgBreed)
            val selectedRadioButtonId = rgBreed.checkedRadioButtonId
            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)

            // Pet description field reference
            val tiPetDescription = findViewById<TextInputEditText>(R.id.tiPetDescription)
            val tilPetDescription = findViewById<TextInputLayout>(R.id.tilPetDescription)

            // Error text references
            val tvErrorPhoto = findViewById<TextView>(R.id.tvErrorPhoto)
            val tvErrorRadio = findViewById<TextView>(R.id.tvErrorRadio)

            // Validate
            val petName = tiPetName.text.toString()
            val petAge = tiPetAge.text.toString()
            val petDescription = tiPetDescription.text.toString()

            if (!isValid(petName, tilPetName, petAge, tilPetAge, petDescription,
                    tilPetDescription, tvErrorPhoto, tvErrorRadio, selectedRadioButtonId)) {
                return@setOnClickListener
            }

            val pet = ShelterPet(
                name = petName,
                age = petAge.toInt(),
                breed = selectedRadioButton.text.toString(),
                description = petDescription,
            )
            writeToDatabase("pet_${Instant.now()}", FirebaseDatabaseSingleton.getCurrentUid(), pet)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent);
            finish()
        }


    }

    private fun isValid(petName: String, tilPetName: TextInputLayout,
                        petAge: String, tilPetAge: TextInputLayout,
                        petDescription: String, tilPetDescription: TextInputLayout,
                        tvErrorPhoto: TextView, tvErrorRadio: TextView, checkedRadioButtonId: Int): Boolean {
        var isValid = true

        if (petName.equals("")) {
            tilPetName.setError("Name cannot be empty")
            isValid = false
        } else {
            tilPetName.setError(null)
        }

        if (petAge.equals("")) {
            tilPetAge.setError("Age cannot be empty")
            isValid = false
        } else {
            tilPetAge.setError(null)
        }

        if (petDescription.equals("")) {
            tilPetDescription.setError("Description cannot be empty")
            isValid = false
        } else {
            tilPetDescription.setError(null)
        }

        if (curFile == null) {
            tvErrorPhoto.text = "Choose a photo"
            isValid = false
        } else {
            tvErrorPhoto.text = ""
        }

        if (checkedRadioButtonId == -1) {
            tvErrorRadio.text = "Choose a breed"
            isValid = false
        } else {
            tvErrorRadio.text = ""
        }

        return isValid
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val image = findViewById<ImageView>(R.id.ivPetUpload)

            data?.data?.let {
                curFile = it
                image?.setImageURI(it)
            }

        }
    }

    private fun writeToDatabase(fileName: String, shelterId: String, pet: ShelterPet) = CoroutineScope(
        Dispatchers.IO).launch {
        try {
            curFile?.let {
                val downloadUrl = imageRef.child("images/$fileName").putFile(it).await().storage.downloadUrl.await()
                pet.imageURL.add(downloadUrl.toString())
                val petDAO = PetDAO()
                petDAO.postPet(shelterId, pet)
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Successfully added pet", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}