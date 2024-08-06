package hu.ocist.enaplo.login.ui.canteen

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.responses.CanteenResponse
import hu.ocist.enaplo.login.databinding.FoodMenuListRowBinding
import java.lang.Thread.enumerate

class CanteenRecyclerViewAdapter(
    val fragment: CanteenFragment
) :
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
        holder.binding.extra.text = menuItem.extra ?: ""

        holder.binding.edit.setOnClickListener {
            fragment.callAlertDialog(
                menuItem.id,
                menuItem.firstMeal,
                menuItem.secondMeal,
                menuItem.extra ?: "",
            )
        }

        holder.binding.delete.setOnClickListener {
            sureDeleteAlertDialog(
                fragment.requireContext().getString(R.string.youSureToDelete),
                2,
                menuItem.id,
                position
            )
        }
    }

    private fun sureDeleteAlertDialog(text: String, depth: Int, id: Int, position: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(fragment.requireContext())
            .setTitle(text)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.yes) { _, _ ->
                if (depth <= 1)
                    fragment.callDelete(id, position)
                else
                    sureDeleteAlertDialog(
                        fragment.requireContext().getString(R.string.sureSureDeleteMenu),
                        depth - 1,
                        id,
                        position
                    )
            }
        val dialog = builder.show()
        val textViewId: Int =
            dialog.context.resources.getIdentifier("android:id/alertTitle", null, null)
        val tv = dialog.findViewById<TextView>(textViewId)
        tv.setTextColor(R.color.seconder_content)
    }

    override fun getItemCount(): Int = items.size

    fun update(menuItems: List<CanteenResponse>) {
        empty()
        items.addAll(menuItems)
        notifyDataSetChanged()
    }

    fun addOrUpdateItem(menuItem: CanteenResponse) {
        for (i in  0 until items.size) {
            if (items[i].id == menuItem.id) {
                items[i].firstMeal = menuItem.firstMeal
                items[i].secondMeal = menuItem.secondMeal
                items[i].extra = menuItem.extra
                notifyItemChanged(i)
                return
            }
        }
        items.add(menuItem)
        notifyItemInserted(items.size - 1)
    }

    fun empty() {
        items.clear()
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        items.removeAt(position)
        notifyDataSetChanged()
    }

    inner class CanteenViewHolder(
        val binding: FoodMenuListRowBinding
        ) : RecyclerView.ViewHolder(binding.root)
}