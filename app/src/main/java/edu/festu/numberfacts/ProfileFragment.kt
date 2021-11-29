package edu.festu.numberfacts

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.festu.numberfacts.databinding.FragmentProfileBinding
import edu.festu.numberfacts.model.FactViewModel
import kotlinx.coroutines.launch

/**
 * Фрагмент для показа простейшей пользовательской статистики
 * */
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        //Получение числа фактов в коллекции пользователя с помощью сопрограммы.
        //Как только список фактов будет получен из ViewModel, их количество будет показано в numFactsField
        lifecycleScope.launch {
            binding.emailField.text = Firebase.auth.currentUser?.email.toString()
            binding.loginField.text = Firebase.auth.currentUser?.displayName.toString()

            Firebase.auth.currentUser.let {
                    FactViewModel.get().factList.observe(viewLifecycleOwner, {
                        if (isAdded)
                            binding.numFactsField.text = String.format(getString(R.string.facts_saved),it.size)
                    })
                    FactViewModel.get().getCollection()
            }
        }
        binding.logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
        return binding.root
    }
}