package edu.festu.numberfacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import edu.festu.numberfacts.databinding.FragmentGetFactBinding
import edu.festu.numberfacts.model.Fact
import edu.festu.numberfacts.model.FactViewModel
import edu.festu.numberfacts.network.ApiService
import edu.festu.numberfacts.network.NetworkObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Фрагмент для получения нового факта.
 * Для доступа к сетевому ресурсу используется Retrofit
 * */
class GetFactFragment : Fragment() {
    private lateinit var fact:Fact
    private lateinit var binding: FragmentGetFactBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentGetFactBinding.inflate(inflater,container,false)
        binding.getFact.setOnClickListener {
            getFact()
        }
        binding.saveFact.setOnClickListener {
            save()
        }
        return binding.root
    }
    //Получение нового факта с помощью Retrofit. Для асинхронной работы используется enqueue
    private fun getFact() {
        NetworkObject.get().create(ApiService::class.java).getNumberFactAsync().enqueue(object :
            Callback<Fact> {
            override fun onResponse(call: Call<Fact>, response: Response<Fact>) {
                binding.factView.text = response.body()?.text
                fact = response.body()!!
            }

            override fun onFailure(call: Call<Fact>, t: Throwable) {
                binding.factView.text = getString(R.string.try_again)
                t.printStackTrace()
            }
        })
    }
    //Сохранение факта в коллекцию с помощью ViewModel. Помещено в сопрограмму
    private fun save(){
         lifecycleScope.launch {
             if(::fact.isInitialized)
            FactViewModel.get().addFact(fact)
         }
    }
}