package github.projectgroup.receptoriaApp

import android.app.Application
import github.projectgroup.receptoriaApp.di.NetworkModule

class ReceptoriaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        NetworkModule.initialize(applicationContext)
    }
}