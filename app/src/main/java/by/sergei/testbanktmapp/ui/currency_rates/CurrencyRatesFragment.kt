package by.sergei.testbanktmapp.ui.currency_rates

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.sergei.testbanktmapp.R
import by.sergei.testbanktmapp.databinding.FragmentCurrencyRatesBinding
import by.sergei.testbanktmapp.utils.observeOnRepeatedLifeCycle
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyRatesFragment : Fragment() {
    private lateinit var binding: FragmentCurrencyRatesBinding
    private val viewModel: CurrencyRateViewModel by viewModel()
    private val currencyAdapter = CurrencyAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrencyRatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.currencyToday.text = context?.getString(R.string.currency_rates_today, viewModel.today)

        binding.recyclerCurrencies.layoutManager = LinearLayoutManager(context)
        binding.recyclerCurrencies.adapter = currencyAdapter

        observeStateUi()

        binding.update.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun observeStateUi() {
        lifecycleScope.observeOnRepeatedLifeCycle(
            lifecycleOwner = this,
            state = viewModel.uiState
        ) { result ->
            if (result.isSuccess) {
                currencyAdapter.submitList(
                    result.getOrThrow()
                )
            }else{
                //todo visibility view show error
            }
        }


        lifecycleScope.observeOnRepeatedLifeCycle(
            lifecycleOwner = this,
            state = viewModel.isLoading
        ) { isLoading ->
            //todo binding.progressDialog.visibility = View.VISIBLE extension isVisible = true  // дабавить вью
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CurrencyRatesFragment()
    }
}