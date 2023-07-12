package com.android.simple.common.cocktail.api.models

data class Info(
    val dialog_type: String,
    val latest_version: String,
    val package_name: String,
    val update_type: String,
    val website_title: String,
    val website_url: String
)