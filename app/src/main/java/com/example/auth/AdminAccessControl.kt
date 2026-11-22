package com.example.auth

object AdminAccessControl {
  const val SUPER_ADMIN_EMAIL = "mhossenali740@gmail.com"

  fun isSuperAdmin(email: String?): Boolean {
    val normalized = email?.trim() ?: return false
    return normalized.equals(SUPER_ADMIN_EMAIL, ignoreCase = true)
  }
}
