import com.mobile.android_task.R

enum class NavigationItem(
    val title: String,
    val icon: Int
) {
    Home(
        icon = R.drawable.baseline_arrow_back_ios_24,
        title = "Home"
    ),
    Profile(
        icon = R.drawable.baseline_arrow_back_ios_24,
        title = "Profile"
    ),
    Premium(
        icon = R.drawable.baseline_arrow_back_ios_24,
        title = "Premium"
    ),
    Settings(
        icon = R.drawable.baseline_arrow_back_ios_24,
        title = "Settings"
    )
}