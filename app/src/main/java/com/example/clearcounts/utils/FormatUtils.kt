package com.example.clearcounts.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class ThousandSeparatorTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) return TransformedText(text, OffsetMapping.Identity)

        val formattedText = StringBuilder()
        val reversedText = originalText.reversed()

        for (i in reversedText.indices) {
            formattedText.append(reversedText[i])
            // Agregamos el apóstrofe cada 3 dígitos
            if ((i + 1) % 3 == 0 && i != reversedText.lastIndex) {
                formattedText.append("'")
            }
        }

        val finalOutput = formattedText.reverse().toString()

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                // Calculamos cuántos separadores hay antes de la posición actual
                val separatorsBefore = (offset - 1) / 3
                return offset + separatorsBefore.coerceAtLeast(0)
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Ajuste inverso para que el cursor no se trabe en el separador
                val separatorsBefore = offset / 4
                return (offset - separatorsBefore).coerceIn(0, originalText.length)
            }
        }

        return TransformedText(AnnotatedString(finalOutput), offsetMapping)
    }
}