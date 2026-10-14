package online.codyuhi.tongantourism

import android.app.Application
import online.codyuhi.tongantourism.di.AppContainer
import online.codyuhi.tongantourism.di.DefaultAppContainer

class TonganTourismApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
