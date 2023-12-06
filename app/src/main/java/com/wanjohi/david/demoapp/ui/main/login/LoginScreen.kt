package com.wanjohi.david.demoapp.ui.main.login

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wanjohi.david.demoapp.R
import com.wanjohi.david.demoapp.ui.ui.Header
import com.wanjohi.david.demoapp.ui.ui.InputField
import com.wanjohi.david.demoapp.ui.ui.PasswordInput
import com.wanjohi.david.demoapp.ui.ui.SubmitButton

@SuppressLint("UnusedCrossfadeTargetStateParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(){
    val loginViewModel:LoginViewModel = koinViewModel()
    val loginState = loginViewModel.loginState
    val context = LocalContext.current
    val page by loginViewModel.page.observeAsState(Page.LOGIN)

    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            ){paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                ,
            ){
                Image(painter = painterResource(id = R.drawable.background_img), contentDescription ="Background Image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.matchParentSize()
                )
                Crossfade(targetState =page, label = "") {
                    when(page){
                        Page.LOGIN->{
                            EmailSignIn(loading = loginState.value.isLoading) { email: String, password: String ->
                                loginViewModel.attemptLogin(email,password)
                            }
                        }
                        Page.WELCOME->{
                            WelcomeScreen(loginState.value.data?.username?:"")
                        }
                    }
                }

                if (loginState.value.success) {
                    //load the next screen for showing the user
                    loginViewModel.changePage(Page.WELCOME)
                }
                if (loginState.value.error!=null){
                    Toast.makeText(context, loginState.value.error, Toast.LENGTH_SHORT).show()
                }
            }

        }

    }


}

@ExperimentalComposeUiApi
@Composable
private fun EmailSignIn(
    loading: Boolean = false,
    onDone: (String, String) -> Unit
) {
    val username = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisibility = rememberSaveable { mutableStateOf(false) }
    val passwordFocusRequester = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(username.value, password.value) {
        username.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    val modifier = Modifier
        .verticalScroll(rememberScrollState())
    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {
        Column(modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

            Image(painter = painterResource(id = R.drawable.logo),
                contentDescription ="Background Image",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
            InputField(valueState = username, enabled = !loading,labelId=R.string.username, onAction = KeyboardActions {
                passwordFocusRequester.requestFocus()
            })
            PasswordInput(
                modifier = Modifier.focusRequester(passwordFocusRequester),
                passwordState = password,
                enabled = !loading,
                passwordVisibility = passwordVisibility,
                onAction = KeyboardActions {
                    //The submit button is disabled unless the inputs are valid. wrap this in if statement to accomplish the same.
                    if (!valid) return@KeyboardActions
                    onDone(username.value.trim(), password.value.trim())
                    keyboardController?.hide()
                }
            )
            SubmitButton(
                textId = R.string.sign_in,
                loading = loading,
                validInputs = valid
            ) {
                onDone(username.value.trim(), password.value.trim())
                keyboardController?.hide()
            }

        }

    }


}

@Composable
fun WelcomeScreen(username: String) {
    Box(modifier = Modifier.fillMaxSize(),contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Image(painter = painterResource(id = R.drawable.logo),
                contentDescription ="Background Image",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
            Text(text = "Welcome $username to the new Co-op Bank app!")
        }
    }
}

