package band.effective.hdl.presentation

sealed interface MainEffect {
    data object OnOpenAuthScreen : MainEffect

    data object OnOpenContentScreen : MainEffect
}