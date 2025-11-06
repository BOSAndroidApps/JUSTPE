package com.bos.payment.appName.data.model.supportmanagement

data class NavParentItem(val title: String,
                         val children: List<String>,
                         var isExpanded: Boolean = false)
