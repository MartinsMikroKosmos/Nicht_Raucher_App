package com.example.nicht_raucher_app.ui.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmokingRooms
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Farbpalette im Eloqwnt-Stil
val CardBackground = Color(0xFFF7F7F7) // Sehr helles Grau/Off-white
val PrimaryAccent = Color(0xFF1A1A1A) // Tiefes Schwarz für Texte
val SecondaryText = Color(0xFF8E8E93) // iOS-Style Grau
val SuccessGreen = Color(0xFF4ADE80) // Ein frisches Grün für Fortschritt

@Composable
fun SobrietyCard(
    habitName: String = "Rauchfrei",
    days: Int = 14,
    moneySaved: String = "84,50 €",
    icon: ImageVector = Icons.Default.SmokingRooms
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            // Header: Icon und Titel
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(CardBackground),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = PrimaryAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = habitName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryAccent
                    )
                }

                // Status Badge (Grüner Punkt für "Aktiv")
                Surface(
                    shape = CircleShape,
                    color = SuccessGreen.copy(alpha = 0.1f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(SuccessGreen)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Haupt-Statistik (Die großen Tage)
            Column {
                Text(
                    text = "$days Tage",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-1).sp,
                    color = PrimaryAccent
                )
                Text(
                    text = "Glückwunsch! Du hälst durch.",
                    fontSize = 14.sp,
                    color = SecondaryText
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer: Sub-Stats (Bento-Stil)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Info Box 1: Ersparnis
                InfoSmallBox(
                    label = "Gespart",
                    value = moneySaved,
                    modifier = Modifier.weight(1f)
                )
                // Info Box 2: Gesundheit/Trend
                InfoSmallBox(
                    label = "Status",
                    value = "Verbessert",
                    icon = Icons.Default.TrendingUp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun InfoSmallBox(
    label: String,
    value: String,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(CardBackground)
            .padding(16.dp)
    ) {
        Text(text = label, fontSize = 12.sp, color = SecondaryText)
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = SuccessGreen
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryAccent
            )
        }
    }
}

@Preview
@Composable
private fun test() {
    SobrietyCard()
}