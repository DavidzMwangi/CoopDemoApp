package com.wanjohi.david.demoapp.ui.main.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material3.TopAppBarDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(){
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = "Uploads")
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Black
                ))
        },

    ){paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ){
            RequestContentPermission()
        }

    }

}
@Composable
fun RequestContentPermission() {
    val loginViewModel:LoginViewModel = koinViewModel()

    val uploadState = loginViewModel.uploadState

   
   
    Column(
        horizontalAlignment = Alignment.CenterHorizontally

    ) {


            Text(text = "rgrgrgrtgrt")
    }
}

