package by.sergei.testbanktmapp

import android.app.Application
import by.sergei.testbanktmapp.data.ApiClient
import by.sergei.testbanktmapp.model.CurrencyRatesRepository
import by.sergei.testbanktmapp.model.db.AppDatabase
import by.sergei.testbanktmapp.ui.currency_basket.CurrencyBasketInteractor
import by.sergei.testbanktmapp.ui.currency_basket.CurrencyBasketViewModel
import by.sergei.testbanktmapp.ui.currency_rates.CurrencyRateViewModel
import by.sergei.testbanktmapp.ui.currency_rates.CurrencyRatesInteractor
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }

    private val appModule = module() {
        single { Cicerone.create(Router()) }
        factory<NavigatorHolder> { get<Cicerone<Router>>().getNavigatorHolder() }
        factory<Router> { get<Cicerone<Router>>().router }

        single { AppDatabase.getAppDBInstance(this@App) }
        single { get<AppDatabase>().getAppDao() }

        single { ApiClient.build() }

        single<CurrencyRatesRepository> { CurrencyRatesRepository(get(), get()) }
        factory<CurrencyRatesInteractor> { CurrencyRatesInteractor(get()) }
        factory<CurrencyBasketInteractor> {CurrencyBasketInteractor(get())}

        viewModel { CurrencyRateViewModel(get()) }
        viewModel { CurrencyBasketViewModel(get()) }
    }
}