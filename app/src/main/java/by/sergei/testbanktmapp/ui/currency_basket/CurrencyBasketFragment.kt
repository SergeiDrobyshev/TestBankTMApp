package by.sergei.testbanktmapp.ui.currency_basket

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.sergei.testbanktmapp.R
import by.sergei.testbanktmapp.databinding.FragmentCurrencyBasketBinding
import by.sergei.testbanktmapp.utils.observeOnRepeatedLifeCycle
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class CurrencyBasketFragment : Fragment() {
    private lateinit var binding: FragmentCurrencyBasketBinding
    private val viewModel: CurrencyBasketViewModel by viewModel()
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val currencyBasketAdapter = CurrencyBasketAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrencyBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.currencyBasketLabel.text = context?.getString(R.string.currency_basket_label, LocalDate.now())
        binding.recyclerCurrenciesBasket.layoutManager = LinearLayoutManager(context)
        binding.recyclerCurrenciesBasket.adapter = currencyBasketAdapter
        binding.date.setOnClickListener {
            showDatePicker()
        }

        observeStateUi()
        binding.showBasket.setOnClickListener {
            val date = binding.date.text
            if (date.isNotEmpty()) {
                viewModel.refreshBasket(LocalDate.parse(date, formatter))
                binding.currencyBasketLabel.text = context?.getString(R.string.currency_basket_label, date)
            }
        }
    }

    private fun showDatePicker() {
        val localDate = LocalDate.now()
        val datePickerDialog = DatePickerDialog(
            requireContext(), { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth)
                val formattedDate = formatter.format(selectedDate)
                binding.date.setText(formattedDate)
            },
            localDate.year,
            localDate.monthValue - 1,
            localDate.dayOfMonth
        )
        datePickerDialog.show()
    }

    private fun observeStateUi() {
        lifecycleScope.observeOnRepeatedLifeCycle(
            lifecycleOwner = this,
            state = viewModel.uiState
        ) { result ->
            if (result != null) {
                if (result.isSuccess) {
                    val basket = result.getOrThrow()
                    currencyBasketAdapter.submitList(basket.ratesItems)
                    binding.basketRatesHeader.curNameBasket.text = basket.header.label
                    binding.basketRatesHeader.curOfficialRate.text = basket.header.rateBasketByDate.toString()
                    binding.basketRatesHeader.curDiffHardDate.text = basket.header.diffRateHardDate.toString()
                    binding.basketRatesHeader.curDiffYesterday.text = basket.header.diffRateByDateYesterday.toString()
                } else {
                    //todo visibility view show error
                }
            }
        }

        lifecycleScope.observeOnRepeatedLifeCycle(
            lifecycleOwner = this,
            state = viewModel.isLoading
        )
        { isLoading ->
            //todo binding.progressDialog.visibility = View.VISIBLE extension isVisible = true  // дабавить вью
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = CurrencyBasketFragment()
    }
}