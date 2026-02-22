package com.example.nicht_raucher_app.milestones

data class Milestone(
    val id: String,
    val durationMillis: Long,
    val title: String,
    val medicalBenefit: String,   // Was passiert im Körper
    val motivationQuote: String,  // Motivationsspruch
) {
    // Rückwärtskompatibilität für MilestoneWorker (erwartet noch .body)
    val body: String get() = "$medicalBenefit\n💬 $motivationQuote"
}