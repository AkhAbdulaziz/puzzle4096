package uz.gita.puzzle4096

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val host = supportFragmentManager.findFragmentById(R.id.appNavHost) as NavHostFragment
        val graph = host.navController.navInflater.inflate(R.navigation.navigation)

        graph.startDestination = R.id.menuScreen
        host.navController.graph = graph
    }
}
