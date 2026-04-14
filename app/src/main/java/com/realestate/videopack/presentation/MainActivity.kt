package com.realestate.videopack.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.realestate.videopack.navigation.AppNavHost
import com.realestate.videopack.presentation.theme.RealEstateVideoPackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RealEstateVideoPackTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}