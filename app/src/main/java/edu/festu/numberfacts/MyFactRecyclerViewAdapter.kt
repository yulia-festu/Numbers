package edu.festu.numberfacts

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import edu.festu.numberfacts.databinding.FragmentItemBinding
import edu.festu.numberfacts.model.Fact

/**
 * Адаптер для RecycleView.
 * Хранит список значений (здесь - объекты типа Fact), предназначен для обработки их изменения.
 * Каждый элемент списка представляется с помощью viewHolder
 * */
class MyFactRecyclerViewAdapter: RecyclerView.Adapter<MyFactRecyclerViewAdapter.ViewHolder>() {

    private var facts: MutableList<Fact>

    init {
        facts = mutableListOf()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    // Здесь используется метод notifyDatasetChanged, но в большинстве случаев достаточно уведомлять об изменении конкретных элементов.
    fun setFacts(facts: MutableList<Fact>) {
        this.facts =facts
        notifyDataSetChanged()

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = facts[position]
        holder.numberView.text = item.number.toString()
        holder.textView.text = item.text
        holder.typeView.text = item.type
        holder.dateView.text = item.date
        holder.yearView.text = item.year
    }

    override fun getItemCount(): Int = facts.size
    /**
     * Внутренний класс для описания элемента списка.
     * */
    inner class ViewHolder(binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val numberView = binding.numberField
        val textView = binding.textField
        val typeView = binding.typeField
        val dateView = binding.dateField
        val yearView = binding.yearField

        override fun toString(): String {
            return super.toString() + " '" + textView.text + "'"
        }
    }

}