package by.sergei.testbanktmapp.utils

import by.sergei.testbanktmapp.ui.currency_basket.CurrencyBasketFragment
import by.sergei.testbanktmapp.ui.currency_rates.CurrencyRatesFragment
import by.sergei.testbanktmapp.ui.main.MainFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun currencyRates(): FragmentScreen = FragmentScreen { MainFragment.newInstance() }
    fun currencyBYN(): FragmentScreen = FragmentScreen { CurrencyRatesFragment.newInstance() }
    fun currencyBasket(): FragmentScreen = FragmentScreen { CurrencyBasketFragment.newInstance() }
    //fun currencyShedule():
}