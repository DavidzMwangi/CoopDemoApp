package com.wanjohi.david.demoapp.ui.main.login

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.siddroid.holi.colors.MaterialColor
import com.wanjohi.david.demoapp.R
import com.wanjohi.david.demoapp.data.models.Trace
import com.wanjohi.david.demoapp.ui.main.login.states.UploadState
import com.wanjohi.david.demoapp.utils.FileUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material3.TopAppBarDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Destination
fun UploadScreen(){
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
    val uploadViewModel:UploadViewModel = koinViewModel()

    val uploadState = uploadViewModel.uploadState

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap =  remember {
        mutableStateOf<Bitmap?>(null)
    }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->

        imageUri = uri
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver,it)

            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver,it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }

            bitmap.value?.let {  btm ->
                Image(bitmap = btm.asImageBitmap(),
                    contentDescription =null,
                    modifier = Modifier.size(200.dp))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialColor.BLUE_700,
                    contentColor = Color.White
                ),
                onClick = {
                launcher.launch("image/*")
            }) {
                Image(
                    painterResource(id = R.drawable.ic_action_picture),
                    contentDescription ="Pick Image",
                    modifier = Modifier.size(20.dp))

                Text(text = "Pick image",Modifier.padding(start = 10.dp), color = Color.White)
            }

            Button(
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialColor.BLUE_700,
                    contentColor = Color.White
                ),
                enabled = imageUri!=null,
                onClick = {
                    if (imageUri!=null){
                        val file = FileUtil.from(context, imageUri!!)
                        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)
                        uploadViewModel.attemptLogin(imagePart)
                    }else{
                        Toast.makeText(context,"Ensure you select an image before uploading",Toast.LENGTH_SHORT).show()
                    }

            }) {

                Image(
                    painterResource(id = R.drawable.ic_action_file_upload),
                    contentDescription ="Pick Image",
                    modifier = Modifier.size(20.dp))

                Text(text = "Upload",Modifier.padding(start = 10.dp), color = Color.White)
            }
        }

        ShowResults(uploadState = uploadState)
    }
}

@Composable
fun ShowResults(uploadState: State<UploadState>) {

    if (uploadState.value.isLoading){
        LinearProgressIndicator(
            modifier = Modifier
                .semantics(mergeDescendants = true) {}
                .padding(10.dp)
        )
    }

    if (uploadState.value.data.isNotEmpty()){
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Results",
            modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
            color = MaterialColor.WHITE,
            fontWeight = FontWeight.Bold,
        )
    }


    Spacer(modifier = Modifier.height(10.dp))

    LazyColumn(contentPadding = PaddingValues(4.dp) ){
        items(uploadState.value.data){
            TraceItem(trace = it)
        }

    }
}

@Composable
fun TraceItem(trace:Trace){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = { }),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                Text(
                    text = trace.filename,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialColor.GREY_900,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = trace.episode,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialColor.GREY_700,
                )

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "%.2f".format(trace.similarity),
                    color = MaterialColor.GREEN_400
                )
            }

        }
    }

}
