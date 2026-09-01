package com.dentalogic.app.core

import androidx.compose.ui.graphics.Color
import com.dentalogic.app.ui.theme.ColorHealthy
import com.dentalogic.app.ui.theme.ColorMild
import com.dentalogic.app.ui.theme.ColorModerate
import com.dentalogic.app.ui.theme.ColorSevere

/**
 * Classification of dental caries condition (ICDAS based D0 - D6 scale).
 */
enum class DentalCondition(
    val code: String,
    val title: String,
    val description: String,
    val severityLevel: String,
    val color: Color,
) {
    D0(
        code = "D0",
        title = "Gigi Sehat (Sound)",
        description = "Tidak ada tanda karies atau lesi enamel yang terlihat.",
        severityLevel = "Normal",
        color = ColorHealthy,
    ),
    D1(
        code = "D1",
        title = "Lesi Enamel Awal",
        description = "Perubahan visual pertama pada enamel (white spot) saat permukaan dikeringkan.",
        severityLevel = "Ringan",
        color = ColorMild,
    ),
    D2(
        code = "D2",
        title = "Lesi Enamel Terlihat",
        description = "Perubahan visual enamel yang jelas terlihat bahkan dalam kondisi basah.",
        severityLevel = "Ringan",
        color = ColorMild,
    ),
    D3(
        code = "D3",
        title = "Kavitasi Enamel Mikro",
        description = "Kerusakan integritas permukaan enamel tanpa dentin yang terlihat jelas.",
        severityLevel = "Sedang",
        color = ColorModerate,
    ),
    D4(
        code = "D4",
        title = "Bayangan Gelap Dentin",
        description = "Bayangan dentin yang mendasari enamel dengan demineralisasi signifikan.",
        severityLevel = "Sedang",
        color = ColorModerate,
    ),
    D5(
        code = "D5",
        title = "Kavitas Dentin Nyata",
        description = "Kavitas nyata yang memperlihatkan lapisan dentin terbuka.",
        severityLevel = "Berat",
        color = ColorSevere,
    ),
    D6(
        code = "D6",
        title = "Kavitas Luas Melibatkan Pulpa",
        description = "Kavitas luas dan dalam mencapai pulpa dengan risiko infeksi tinggi.",
        severityLevel = "Kritis",
        color = ColorSevere,
    );

    companion object {
        fun fromClassId(id: Int): DentalCondition = when (id) {
            0 -> D0
            1 -> D1
            2 -> D2
            3 -> D3
            4 -> D4
            5 -> D5
            6 -> D6
            else -> D0
        }

        fun fromClassName(name: String): DentalCondition =
            entries.find { it.code.equals(name.trim(), ignoreCase = true) } ?: D0
    }
}
