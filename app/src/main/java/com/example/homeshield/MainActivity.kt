package com.example.homeshield

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()
        var user = firebaseAuth.currentUser
        var textView: TextView = findViewById(R.id.activityMainTextView)

        if (user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            user = firebaseAuth.currentUser
            if (user != null)
                textView.text = getString(R.string.user).plus(user.email)
            else
                Toast.makeText(this, "NULL user!", Toast.LENGTH_SHORT)
        }

        user = firebaseAuth.currentUser
        textView.text = getString(R.string.user).plus(user?.email)

        var signOutButton: Button = findViewById(R.id.signOutButton)
        signOutButton.setOnClickListener {
            firebaseAuth.signOut()
            finish();
            startActivity(getIntent())
        }


    }
}
//
//@Composable
//fun BackgroundImage(devices: Array<Device>, modifier: Modifier = Modifier) {
//    val backgroundImage = painterResource(id = R.drawable.background)
//    Box (modifier) {
//        Image(
//            painter = backgroundImage,
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            alpha = 0.9F
//            )
//        DeviceLayout(device = devices[0], modifier.align(Alignment.Center))
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun BackgroundPreview() {
//    HomeShieldTheme {
//        BackgroundImage(
//            arrayOf(Device("Poarta", 15921, "Buftea", "locked", true))
//        )
//    }
//}
//
//@Composable
//fun DeviceLayout(device: Device, modifier: Modifier = Modifier) {
//    var lockState by remember { mutableStateOf(true) }
//    val lockImage = getLockPainterResource(lockState)
//    Surface(color = Color.White, modifier = modifier) {
//        Row (
//            modifier = modifier,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Image(painterResource(id = R.drawable.gate), null, modifier = Modifier
//                .size(64.dp)
//                .padding(15.dp))
//            Column (
//                modifier = modifier.padding(start = 24.dp, end = 128.dp),
//                horizontalAlignment = Alignment.Start
//            ) {
//                Text(
//                    text = device.name,
//                    fontSize = 24.sp,
//                    fontStyle = FontStyle.Normal,
//                    modifier = modifier
//                )
//                Text(
//                    text = device.location,
//                    fontSize = 12.sp,
//                    fontStyle = FontStyle.Italic,
//                    modifier = modifier
//                )
//            }
//            Box (contentAlignment = Alignment.Center) {
//                Button(
//                    onClick = {
//                        device.locked = !device.locked
//                        lockState = device.locked
//                    }, modifier = Modifier.padding(12.dp)
//
//                ) {
//                    Icon(lockImage, contentDescription = null)
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun getLockPainterResource(isLocked: Boolean): Painter {
//    return if (isLocked)
//        painterResource(id = R.drawable.lock)
//    else
//        painterResource(id = R.drawable.unlock)
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    HomeShieldTheme {
//        DeviceLayout(Device("Poarta", 15921, "Buftea", "locked", true))
//    }
//}