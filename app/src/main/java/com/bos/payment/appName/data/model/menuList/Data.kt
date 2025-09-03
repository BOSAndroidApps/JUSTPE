package com.bos.payment.appName.data.model.menuList

import com.google.gson.annotations.SerializedName


data class Data (

  @SerializedName("childMenuCode"  ) var childMenuCode  : String? = null,
  @SerializedName("menuText"       ) var menuText       : String? = null,
  @SerializedName("navigateURL"    ) var navigateURL    : String? = null,
  @SerializedName("parentMenuCode" ) var parentMenuCode : String? = null,
  @SerializedName("toolTip"        ) var toolTip        : String? = null,
  @SerializedName("description"    ) var description    : String? = null,
  @SerializedName("displayOrder"   ) var displayOrder   : String? = null,
  @SerializedName("icon"           ) var icon           : String? = null,
  var isExpanded: Boolean = false, // To track expansion state
  var childMenus: List<Data>? = null // To store child menus

)