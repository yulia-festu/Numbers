package edu.festu.numberfacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.RecyclerView
import edu.festu.numberfacts.databinding.FragmentItemListBinding
import edu.festu.numberfacts.model.FactViewModel


/**
 * Фрагмент, представляющий список фактов. Факты хранятся во ViewModel - при изменении списка фактов это будет показано в списке.
 */
class FactFragment : Fragment() {
    private val adapter = MyFactRecyclerViewAdapter()
    private lateinit var binding: FragmentItemListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentItemListBinding.inflate(inflater, container, false)
        binding.list.adapter = adapter
        binding.list.addItemDecoration(DividerItemDecoration(binding.list.context,DividerItemDecoration.VERTICAL))
        //Observe используется для обработки изменения списка фактов и сообщения об ошибке работы со списком фактов
        FactViewModel.get().factList.observe(viewLifecycleOwner, {
            adapter.setFacts(it)
        })
        FactViewModel.get().errorMessage.observe(viewLifecycleOwner,{
            Toast.makeText(context, "Error: $it", Toast.LENGTH_LONG).show()
        })
        //Используется для обработки свайпа влево для удаления
        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {

                val position = viewHolder.layoutPosition
                FactViewModel.get().remove(position)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.list)
        FactViewModel.get().getCollection()
        return binding.root
    }

}