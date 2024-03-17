package com.example.homeshield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homeshield.ui.theme.HomeShieldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeShieldTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Device("Poarta", 15921, "Buftea", "locked", true)
//                    Greeting("HomeShield")
                }
            }
        }
    }
}

@Composable
fun Device(device: Device, modifier: Modifier = Modifier) {
    Surface(color = Color.White, modifier = modifier) {
        Row (
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painterResource(id = R.drawable.gate), null, modifier = Modifier
                .size(64.dp)
                .padding(15.dp))
            Column (
                modifier = modifier.padding(start = 24.dp, end = 128.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = device.name,
                    fontSize = 24.sp,
                    fontStyle = FontStyle.Normal,
                    modifier = modifier
                )
                Text(
                    text = device.location,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = modifier
                )
            }
            Box (contentAlignment = Alignment.Center) {
                Button(
                    onClick = {
                              device.locked = !device.locked
                    }, modifier = Modifier.padding(12.dp)

                ) {
//                    Icon(painterResource(id = R.drawable.lock), contentDescription = null)
                }
            }
        }
    }
}
//
//fun getLockImageVector(device: Device): ImageVector {
//    if (device.locked)
//        return
//    else
//        return Icons.Rounded.
//}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HomeShieldTheme {
        Device(Device("Poarta", 15921, "Buftea", "locked", true))
    }
}