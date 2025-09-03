package com.bos.payment.appName.ui.view.Dashboard.dmt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.dmt.fetchBenificary.Data
import com.bos.payment.appName.databinding.ActivityDmtreciepintDetailsBinding
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.squareup.picasso.Picasso

class DMTReciepintDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDmtreciepintDetailsBinding
    private lateinit var model: Data
    private var mStash: MStash? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Set up view binding
        binding = ActivityDmtreciepintDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UI components
        initView()

        // Back button click listener
        binding.toolbar.ivBack.setOnClickListener { onBackPressed() }

        // Get the data passed from the Intent
        val beneficiaryData = intent.getSerializableExtra("BeneficiaryData") as? Data
        if (beneficiaryData != null) {
            model = beneficiaryData
            populateFields(model) // Populate the views with beneficiary data
        }
    }

    // Populate the fields with the model data
    private fun populateFields(model: Data) {
        binding.tvBankName.text = if (model.bankname.isNullOrEmpty()) "N/A" else model.bankname
        binding.tvIFSCCode.text = if (model.ifsc.isNullOrEmpty()) "N/A" else model.ifsc
        binding.tvAccNo.text = if (model.accno.isNullOrEmpty()) "N/A" else model.accno
        binding.tvRecieptName.text = if (model.name.isNullOrEmpty()) "N/A" else model.name
        binding.tvRecieptId.text = if (model.beneId.isNullOrEmpty()) "N/A" else model.beneId
        binding.tvBankId.text = if (model.bankid.isNullOrEmpty()) "N/A" else model.bankid
        binding.tvVerified.text = if (model.verified.isNullOrEmpty()) "N/A" else model.verified
    }

    // Initialize the view and load company logo
    private fun initView() {
        mStash = MStash.getInstance(this)
        try {
            val imageUrl = mStash?.getStringValue(Constants.CompanyLogo, "")

            // Use Picasso to load the logo into the toolbar
            Picasso.get()
                .load(imageUrl)
                .error(R.drawable.ic_error)  // Optional: error image if load fails
                .into(binding.toolbar.tvToolbarName) // Assuming ivCompanyLogo is your ImageView for the logo
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
