package com.example.vaultlab.ui.account

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.vaultlab.R
import com.example.vaultlab.data.SyntheticRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class AccountFragment : Fragment() {

    private val okHttpClient = OkHttpClient()

    private val pickMediaLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            uploadSelectedImage(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        val context = requireContext()
        val repository = SyntheticRepository(context)
        val user = repository.getCurrentUser()

        val tvHolderName: TextView = view.findViewById(R.id.tvHolderName)
        val tvAccountType: TextView = view.findViewById(R.id.tvAccountType)
        val tvAccountNumber: TextView = view.findViewById(R.id.tvAccountNumber)
        val tvRoutingNumber: TextView = view.findViewById(R.id.tvRoutingNumber)
        val tvBranchCode: TextView = view.findViewById(R.id.tvBranchCode)
        val btnUploadPhoto: Button = view.findViewById(R.id.btnUploadPhoto)

        tvHolderName.text = user.fullName
        tvAccountType.text = user.accountType
        tvAccountNumber.text = user.accountNumber
        tvRoutingNumber.text = user.routingNumber
        tvBranchCode.text = user.branchCode

        btnUploadPhoto.setOnClickListener {
            pickMediaLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        return view
    }

    private fun uploadSelectedImage(imageUri: Uri) {
        val appContext = context?.applicationContext ?: return

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val inputStream = appContext.contentResolver.openInputStream(imageUri)
                    ?: throw IOException("Unable to open image stream")

                val imageBytes = inputStream.use { it.readBytes() }

                // ضغط الصورة داخل ملف zip
                val zipBytes = createZipFromImage(imageBytes, "image.jpg")

                val mediaType = "application/zip".toMediaTypeOrNull()
                val requestBody = zipBytes.toRequestBody(mediaType)

                val multipartBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "image",
                        "profile_${System.currentTimeMillis()}.zip",
                        requestBody
                    )
                    .build()

                val request = Request.Builder()
                    .url("http://192.168.0.221:3000/upload")
                    .post(multipartBody)
                    .build()

                val response = okHttpClient.newCall(request).execute()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            appContext,
                            getString(R.string.upload_success),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            appContext,
                            getString(R.string.upload_failed, "HTTP ${response.code}"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        appContext,
                        getString(R.string.upload_failed, e.localizedMessage ?: "Network error"),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun createZipFromImage(imageBytes: ByteArray, zipEntryName: String): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        ZipOutputStream(byteArrayOutputStream).use { zipOut ->
            val zipEntry = ZipEntry(zipEntryName)
            zipOut.putNextEntry(zipEntry)
            zipOut.write(imageBytes)
            zipOut.closeEntry()
        }
        return byteArrayOutputStream.toByteArray()
    }
}
