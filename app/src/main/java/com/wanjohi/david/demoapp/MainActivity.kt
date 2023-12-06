package com.wanjohi.david.demoapp


import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.wanjohi.david.demoapp.ui.main.NavGraphs
import com.wanjohi.david.demoapp.ui.main.common.StandardScaffold
import com.wanjohi.david.demoapp.ui.theme.DemoAppTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DemoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val navHostEngine = rememberNavHostEngine()

                    StandardScaffold(
                        navController = navController
                    ) {
                        Box(modifier = Modifier.padding(it)){
                            DestinationsNavHost(
                                navGraph = NavGraphs.root,
                                navController= navController,
                                engine = navHostEngine
                            )

                        }
                    }
                }
            }
        }


    }


}
