package band.effective.hdl.presentation.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import band.effective.hdl.presentation.MainEffect
import band.effective.hdl.presentation.MainViewModel
import band.effective.hdl.presentation.ui.screens.welcom.WelcomeScreen
import band.effective.hdl.presentation.ui.screens.welcom.WelcomeViewModel

@Composable
fun NavigationHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val mainViewModel: MainViewModel = hiltViewModel()

    LaunchedEffect(key1 = Unit) {
        mainViewModel.effect.collect { effect ->
            when(effect) {
                MainEffect.OnOpenAuthScreen ->
                    navController.navigate(LeaderIdScreens.Auth.name)
                MainEffect.OnOpenContentScreen ->
                    navController.navigate(LeaderIdScreens.Content.name)
            }
        }
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = LeaderIdScreens.Auth.name
    ) {
        composable(LeaderIdScreens.Auth.name) {
            val welcomeViewModel: WelcomeViewModel = hiltViewModel()
            WelcomeScreen(viewModel = welcomeViewModel)
        }
        composable(LeaderIdScreens.Content.name) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Hello")
            }
        }
    }
}