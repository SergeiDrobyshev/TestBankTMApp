package by.sergei.testbanktmapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.sergei.testbanktmapp.databinding.FragmentMainBinding
import by.sergei.testbanktmapp.utils.Screens
import com.github.terrakok.cicerone.Router
import org.koin.android.ext.android.inject

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val router: Router by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners() {
        binding.curBYN.setOnClickListener {
            router.navigateTo(Screens.currencyBYN())
        }
        binding.curBasket.setOnClickListener {
            router.navigateTo(Screens.currencyBasket())
        }
        binding.curShedule.setOnClickListener {

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}