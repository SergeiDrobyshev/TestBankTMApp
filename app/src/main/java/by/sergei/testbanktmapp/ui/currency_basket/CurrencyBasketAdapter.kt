package by.sergei.testbanktmapp.ui.currency_basket

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.sergei.testbanktmapp.databinding.ItemBasketCurrencyRatesBinding

class CurrencyBasketAdapter: ListAdapter<CurrencyRatesForBasketListItem, CurrencyBasketViewHolder>(RateDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyBasketViewHolder {
        val binding =
            ItemBasketCurrencyRatesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyBasketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyBasketViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CurrencyBasketViewHolder(private val binding: ItemBasketCurrencyRatesBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: CurrencyRatesForBasketListItem) {
        binding.curNameBasket.text = item.currencyRate.curName
        binding.curOfficialRate.text = item.currencyRate.curOfficialRate.toString()
        binding.curDiffHardDate.text = item.diffRateByHardDatePercent.toString()
        binding.curDiffYesterday.text = item.diffRateByDateYesterdayPercent.toString()
    }
}

class RateDiffCallback : DiffUtil.ItemCallback<CurrencyRatesForBasketListItem>() {
    override fun areItemsTheSame(oldItem: CurrencyRatesForBasketListItem, newItem: CurrencyRatesForBasketListItem): Boolean {
        return oldItem.currencyRate.curId == newItem.currencyRate.curId
    }

    override fun areContentsTheSame(oldItem: CurrencyRatesForBasketListItem, newItem: CurrencyRatesForBasketListItem): Boolean {
        return oldItem == newItem
    }
}