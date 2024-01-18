package by.sergei.testbanktmapp.ui.currency_rates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import by.sergei.testbanktmapp.databinding.ItemCurrencyBinding

class CurrencyAdapter : ListAdapter<CurrencyRateListItem, CurrencyViewHolder>(RateDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val binding =
            ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CurrencyViewHolder(private val binding: ItemCurrencyBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(rateItem: CurrencyRateListItem) {
        binding.curName.text = rateItem.currencyRate.curName
        binding.curScale.text = rateItem.currencyRate.curOfficialRate.toString()
        binding.curDynamic.text = rateItem.diffRate.toString()
    }
}

class RateDiffCallback : DiffUtil.ItemCallback<CurrencyRateListItem>() {
    override fun areItemsTheSame(oldItem: CurrencyRateListItem, newItem: CurrencyRateListItem): Boolean {
        return oldItem.currencyRate.curId == newItem.currencyRate.curId
    }

    override fun areContentsTheSame(oldItem: CurrencyRateListItem, newItem: CurrencyRateListItem): Boolean {
        return oldItem == newItem
    }
}