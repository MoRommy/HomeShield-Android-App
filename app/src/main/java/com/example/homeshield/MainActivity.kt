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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
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
                    BackgroundImage(devices = arrayOf(Device("Poarta", 15921, "Buftea", "locked", true)))

                }
            }
        }
    }
}

@Composable
fun BackgroundImage(devices: Array<Device>, modifier: Modifier = Modifier) {
    val backgroundImage = painterResource(id = R.drawable.background)
    Box (modifier) {
        Image(
            painter = backgroundImage,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.9F
            )
        DeviceLayout(device = devices[0], modifier.align(Alignment.Center))
    }
}

@Preview(showBackground = true)
@Composable
fun BackgroundPreview() {
    HomeShieldTheme {
        BackgroundImage(
            arrayOf(Device("Poarta", 15921, "Buftea", "locked", true))
        )
    }
}

@Composable
fun DeviceLayout(device: Device, modifier: Modifier = Modifier) {
    var lockState by remember { mutableStateOf(true) }
    val lockImage = getLockPainterResource(lockState)
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
                        lockState = device.locked
                    }, modifier = Modifier.padding(12.dp)

                ) {
                    Icon(lockImage, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun getLockPainterResource(isLocked: Boolean): Painter {
    return if (isLocked)
        painterResource(id = R.drawable.lock)
    else
        painterResource(id = R.drawable.unlock)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HomeShieldTheme {
        DeviceLayout(Device("Poarta", 15921, "Buftea", "locked", true))
    }
}