package com.bos.payment.appName.data.model.transactionreportsmodel

import java.io.File

data class RaiseTicketReq(
    val userCode: String,
    val serviceCode: String,
    val subject: String,
    val description: String,
    val priority: String,
    val adminCode: String,
    val imagePath1: String,
    val imagePath2: String,
    val imagePath3: String,
    val transactionID: String,
    val transactionSummary: String,
    val imageFile1: File?,
    val imageFile2: File?,
    val imageFile3: File?
)
