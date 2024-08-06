package hu.ocist.enaplo.login.ui.canteen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ocist.enaplo.login.data.responses.CanteenResponse
import hu.ocist.enaplo.login.databinding.FoodMenuListRowBinding

class CanteenRecyclerViewAdapter :
    RecyclerView.Adapter<CanteenRecyclerViewAdapter.CanteenViewHolder>() {
    private val items = mutableListOf<CanteenResponse>()
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CanteenViewHolder {
        this.parent = parent
        return CanteenViewHolder(FoodMenuListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CanteenViewHolder, position: Int) {
        val menuItem = items[position]
        holder.binding.number.text = "${position + 1}. Menü"
        holder.binding.firstMeal.text = menuItem.firstMeal
        holder.binding.secondMeal.text = menuItem.secondMeal
        holder.binding.extra.text = if(menuItem.extra != null) menuItem.extra else ""
    }

    override fun getItemCount(): Int = items.size

    fun update(menuItems: List<CanteenResponse>) {
        empty()
        items.addAll(menuItems)
        notifyDataSetChanged()
    }

    fun empty() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class CanteenViewHolder(
        val binding: FoodMenuListRowBinding
        ) : RecyclerView.ViewHolder(binding.root)
}