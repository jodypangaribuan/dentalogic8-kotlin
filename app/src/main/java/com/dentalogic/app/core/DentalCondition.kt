package com.dentalogic.app.core

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.dentalogic.app.R
import com.dentalogic.app.ui.theme.ColorD0
import com.dentalogic.app.ui.theme.ColorD1
import com.dentalogic.app.ui.theme.ColorD2
import com.dentalogic.app.ui.theme.ColorD3
import com.dentalogic.app.ui.theme.ColorD4
import com.dentalogic.app.ui.theme.ColorD5
import com.dentalogic.app.ui.theme.ColorD6

/**
 * Classification of dental caries condition (ICDAS based D0 - D6 scale)
 * with color tokens harmonized to each condition's visual illustration badge.
 */
enum class DentalCondition(
    val code: String,
    val title: String,
    val description: String,
    val severityLevel: String,
    val color: Color,
    @DrawableRes val iconRes: Int,
) {
    D0(
        code = "D0",
        title = "Healthy Tooth (Sound)",
        description = "No evidence of visible dental caries or enamel lesions.",
        severityLevel = "Normal",
        color = ColorD0,
        iconRes = R.drawable.ic_d0,
    ),
    D1(
        code = "D1",
        title = "Initial Enamel Lesion",
        description = "First visual change in enamel (white/brown spot) seen only after prolonged air drying.",
        severityLevel = "Mild",
        color = ColorD1,
        iconRes = R.drawable.ic_d1,
    ),
    D2(
        code = "D2",
        title = "Distinct Enamel Lesion",
        description = "Distinct visual change in enamel visible even when wet.",
        severityLevel = "Mild",
        color = ColorD2,
        iconRes = R.drawable.ic_d2,
    ),
    D3(
        code = "D3",
        title = "Localized Enamel Breakdown",
        description = "Micro-cavitation in enamel without visible underlying dentin.",
        severityLevel = "Moderate",
        color = ColorD3,
        iconRes = R.drawable.ic_d3,
    ),
    D4(
        code = "D4",
        title = "Underlying Dentin Shadow",
        description = "Dark shadow of dentin shining through intact or minimally broken enamel.",
        severityLevel = "Moderate",
        color = ColorD4,
        iconRes = R.drawable.ic_d4,
    ),
    D5(
        code = "D5",
        title = "Distinct Cavity with Visible Dentin",
        description = "Cavitation exposing underlying dentin surface.",
        severityLevel = "Severe",
        color = ColorD5,
        iconRes = R.drawable.ic_d5,
    ),
    D6(
        code = "D6",
        title = "Extensive Cavity Involving Pulp",
        description = "Extensive distinct cavity involving pulp with high risk of infection.",
        severityLevel = "Critical",
        color = ColorD6,
        iconRes = R.drawable.ic_d6,
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
