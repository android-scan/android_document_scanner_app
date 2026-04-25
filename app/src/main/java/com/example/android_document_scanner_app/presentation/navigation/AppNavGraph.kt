package com.example.android_document_scanner_app.presentation.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android_document_scanner_app.presentation.camera.CameraScreen
import com.example.android_document_scanner_app.presentation.crop.CropScreen
import com.example.android_document_scanner_app.presentation.document.DocumentViewScreen
import com.example.android_document_scanner_app.presentation.edit.EditScreen
import com.example.android_document_scanner_app.presentation.home.HomeScreen
import com.example.android_document_scanner_app.presentation.onboarding.OnboardingScreen
import com.example.android_document_scanner_app.presentation.permissions.PermissionsScreen
import com.example.android_document_scanner_app.presentation.save.SaveBottomSheet
import com.example.android_document_scanner_app.presentation.settings.PaywallScreen
import com.example.android_document_scanner_app.presentation.settings.SettingsScreen
import com.example.android_document_scanner_app.presentation.splash.SplashScreen

object Routes {
    const val SPLASH          = "splash"
    const val ONBOARDING      = "onboarding"
    const val PERMISSIONS     = "permissions"
    const val HOME            = "home"
    const val CAMERA          = "camera"
    const val CROP            = "crop/{imagePath}"
    const val EDIT            = "edit/{imagePath}"
    const val SAVE            = "save/{imagePath}"
    const val DOCUMENT_VIEW   = "document_view/{documentId}"
    const val SETTINGS        = "settings"
    const val PAYWALL         = "paywall"

    fun crop(imagePath: String)          = "crop/${Uri.encode(imagePath)}"
    fun edit(imagePath: String)          = "edit/${Uri.encode(imagePath)}"
    fun save(imagePath: String)          = "save/${Uri.encode(imagePath)}"
    fun documentView(documentId: Long)   = "document_view/$documentId"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
            )
        }

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onNavigateToPermissions = { navController.navigate(Routes.PERMISSIONS) },
            )
        }

        composable(Routes.PERMISSIONS) {
            PermissionsScreen(
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                },
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToCamera    = { navController.navigate(Routes.CAMERA) },
                onNavigateToDocument  = { id -> navController.navigate(Routes.documentView(id)) },
                onNavigateToSettings  = { navController.navigate(Routes.SETTINGS) },
            )
        }

        composable(Routes.CAMERA) {
            CameraScreen(
                onNavigateToCrop = { imagePath -> navController.navigate(Routes.crop(imagePath)) },
                onNavigateBack   = { navController.popBackStack() },
            )
        }

        composable(
            route = Routes.CROP,
            arguments = listOf(navArgument("imagePath") { type = NavType.StringType }),
        ) { backStack ->
            val imagePath = backStack.arguments?.getString("imagePath") ?: ""
            CropScreen(
                imagePath        = imagePath,
                onNavigateToEdit = { path ->
                    navController.navigate(Routes.edit(path)) {
                        popUpTo(Routes.CROP) { inclusive = true }
                    }
                },
                onNavigateBack   = { navController.popBackStack() },
            )
        }

        composable(
            route = Routes.EDIT,
            arguments = listOf(navArgument("imagePath") { type = NavType.StringType }),
        ) { backStack ->
            val imagePath = backStack.arguments?.getString("imagePath") ?: ""
            EditScreen(
                imagePath        = imagePath,
                onNavigateToSave = { path -> navController.navigate(Routes.save(path)) },
                onNavigateBack   = { navController.popBackStack() },
            )
        }

        composable(
            route = Routes.SAVE,
            arguments = listOf(navArgument("imagePath") { type = NavType.StringType }),
        ) { backStack ->
            val imagePath = backStack.arguments?.getString("imagePath") ?: ""
            SaveBottomSheet(
                imagePath          = imagePath,
                onNavigateToHome   = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                },
                onNavigateBack     = { navController.popBackStack() },
            )
        }

        composable(
            route = Routes.DOCUMENT_VIEW,
            arguments = listOf(navArgument("documentId") { type = NavType.LongType }),
        ) { backStack ->
            val documentId = backStack.arguments?.getLong("documentId") ?: 0L
            DocumentViewScreen(
                documentId       = documentId,
                onNavigateToEdit = { path -> navController.navigate(Routes.edit(path)) },
                onNavigateBack   = { navController.popBackStack() },
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onNavigateToPaywall = { navController.navigate(Routes.PAYWALL) },
                onNavigateBack      = { navController.popBackStack() },
            )
        }

        composable(Routes.PAYWALL) {
            PaywallScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
