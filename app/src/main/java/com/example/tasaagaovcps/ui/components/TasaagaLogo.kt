package com.example.tasaagaovcps.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.tasaagaovcps.R

@Composable
fun TasaagaLogo(
    modifier: Modifier = Modifier,
    showText: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // The New Logo Image
        Image(
            painter = painterResource(id = R.drawable.tasaaga_logo_new),
            contentDescription = "Tasaaga Logo",
            modifier = Modifier.size(80.dp)
        )
        
        if (showText) {
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "TASAAGA PRIMARY SCHOOL",
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = Color(0xFF111111),
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Rising To Succeed • Day & Boarding OVC",
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = Color(0xFFEE5A5A),
                    letterSpacing = 0.2.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TasaagaLogoPreview() {
    TasaagaLogo()
}
