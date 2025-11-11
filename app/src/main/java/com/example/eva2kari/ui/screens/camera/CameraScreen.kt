package com.example.eva2kari.ui.screens.camera

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.eva2kari.utils.FileUtils
import com.example.eva2kari.utils.PermissionHandler
import com.example.eva2kari.viewmodel.ProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.io.File

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    profileViewModel: ProfileViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val currentUser by profileViewModel.currentUser.collectAsState()

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var capturedImageFile by remember { mutableStateOf<File?>(null) }

    // Permisos de cámara
    val cameraPermissionState = rememberMultiplePermissionsState(
        listOf(android.Manifest.permission.CAMERA)
    )

    // Permisos de galería
    val storagePermissionState = rememberMultiplePermissionsState(
        PermissionHandler.STORAGE_PERMISSIONS.toList()
    )

    // Launcher para cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && capturedImageFile != null) {
            capturedImageFile?.let { file ->
                currentUser?.let { user ->
                    profileViewModel.updateUserImage(user.id, file.absolutePath)
                    navController.popBackStack()
                }
            }
        }
    }

    // Launcher para galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val file = FileUtils.uriToFile(context, it)
            file?.let { imageFile ->
                currentUser?.let { user ->
                    profileViewModel.updateUserImage(user.id, imageFile.absolutePath)
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Foto de Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Selecciona una opción",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de cámara
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (cameraPermissionState.allPermissionsGranted) {
                        try {
                            val file = FileUtils.createImageFile(context)
                            capturedImageFile = file
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                file
                            )
                            imageUri = uri
                            cameraLauncher.launch(uri)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        cameraPermissionState.launchMultiplePermissionRequest()
                    }
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Cámara",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column {
                        Text(
                            text = "Tomar Foto",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Captura una foto con la cámara",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Botón de galería
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (storagePermissionState.allPermissionsGranted) {
                        galleryLauncher.launch("image/*")
                    } else {
                        storagePermissionState.launchMultiplePermissionRequest()
                    }
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = "Galería",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column {
                        Text(
                            text = "Desde Galería",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Selecciona una foto existente",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Mostrar estado de permisos si fueron denegados
            if (cameraPermissionState.shouldShowRationale) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Se necesita permiso de cámara para tomar fotos",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }

            if (storagePermissionState.shouldShowRationale) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Se necesita permiso de almacenamiento para acceder a la galería",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}