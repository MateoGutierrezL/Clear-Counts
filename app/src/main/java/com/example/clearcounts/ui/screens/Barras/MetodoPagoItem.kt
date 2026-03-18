package com.example.clearcounts.ui.screens.Barras

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clearcounts.data.local.database.entities.PaymentMethodEntity
import com.example.clearcounts.utils.PaymentMethodTranslator.getNombreTraducido

@Composable
fun MetodoPagoItem(
    metodo: PaymentMethodEntity,
    context: Context,
    onClick: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    val iconoId = remember(metodo.icono) {
        context.resources.getIdentifier(metodo.icono, "drawable", context.packageName)
    }

    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = Color.Black
        ),
        border = BorderStroke(0.3.dp, Color.Gray)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (iconoId != 0) {
                Icon(
                    painter = painterResource(id = iconoId),
                    contentDescription = metodo.nombre,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(top = 7.dp),
                    tint = Color.Unspecified
                )
            } else {
                // Inicial como fallback
                Text(
                    text = metodo.nombre.first().uppercase(),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 7.dp)
                )
            }

            Text(
                modifier = Modifier
                    .padding(start = 8.dp, top = 11.dp, bottom = 11.dp)
                    .weight(1f),
                text = metodo.getNombreTraducido(),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp
            )

            // Botón eliminar solo si no es default
            if (onDelete != null) {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.width(20.dp))
}