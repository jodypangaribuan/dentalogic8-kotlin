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
        title = "Healthy Tooth (Sound)",
        description = "No evidence of visible dental caries or enamel lesions.",
        severityLevel = "Normal",
        color = ColorHealthy,
    ),
    D1(
        code = "D1",
        title = "Initial Enamel Lesion",
        description = "First visual change in enamel (white/brown spot) seen only after prolonged air drying.",
        severityLevel = "Mild",
        color = ColorMild,
    ),
    D2(
        code = "D2",
        title = "Distinct Enamel Lesion",
        description = "Distinct visual change in enamel visible even when wet.",
        severityLevel = "Mild",
        color = ColorMild,
    ),
    D3(
        code = "D3",
        title = "Localized Enamel Breakdown",
        description = "Micro-cavitation in enamel without visible underlying dentin.",
        severityLevel = "Moderate",
        color = ColorModerate,
    ),
    D4(
        code = "D4",
        title = "Underlying Dentin Shadow",
        description = "Dark shadow of dentin shining through intact or minimally broken enamel.",
        severityLevel = "Moderate",
        color = ColorModerate,
    ),
    D5(
        code = "D5",
        title = "Distinct Cavity with Visible Dentin",
        description = "Cavitation exposing underlying dentin surface.",
        severityLevel = "Severe",
        color = ColorSevere,
    ),
    D6(
        code = "D6",
        title = "Extensive Cavity Involving Pulp",
        description = "Extensive distinct cavity involving pulp with high risk of infection.",
        severityLevel = "Critical",
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
