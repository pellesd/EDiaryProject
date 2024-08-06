package hu.ocist.enaplo.login.ui.absenceLate

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import hu.ocist.enaplo.login.data.responses.LateResponse
import hu.ocist.enaplo.login.databinding.LateListRowBinding
import hu.ocist.enaplo.login.ui.HomeActivity
import hu.ocist.enaplo.login.ui.snackbar

class LateRecyclerViewAdapter(val fragment: LateFragment) :
    RecyclerView.Adapter<LateRecyclerViewAdapter.LateViewHolder>() {
    private val items = mutableListOf<LateResponse>()
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LateViewHolder {
        this.parent = parent
        return LateViewHolder(LateListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: LateViewHolder, position: Int) {
        val menuItem = items[position]
        if (position % 2 == 1) {
            holder.binding.line.setBackgroundColor(holder.binding.line.context.getColor(hu.ocist.enaplo.login.R.color.light))
        } else {
            holder.binding.line.setBackgroundColor(holder.binding.line.context.getColor(hu.ocist.enaplo.login.R.color.primer_content))
        }
        holder.binding.name.text = menuItem.string
        holder.binding.index.text = "${position+1}."
        if (menuItem.len != null && menuItem.len!! > 0)
            holder.binding.lateText.setText(menuItem.len.toString())

        holder.binding.lateText.tag = position

        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int = items.size

    fun update(menuItems: List<LateResponse>) {
        empty()
        items.addAll(menuItems)
        notifyDataSetChanged()
    }

    fun empty() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class LateViewHolder(
        val binding: LateListRowBinding
        ) : RecyclerView.ViewHolder(binding.root) {

        private val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val length: String = binding.lateText.text.toString()
                val position = binding.lateText.tag as Int? ?: return
                if (length.isEmpty() || length == "0")
                    fragment.setLate(items[position].int, null)
                else {
                    try {
                        val int = length.toInt()
                        if (int in 0..44)
                            fragment.setLate(items[position].int, int)
                        else {
                            binding.lateText.setText("")
                            fragment.requireView().snackbar("1 és 45 közötti számot adj meg!")
                            fragment.setLate(items[position].int, null)
                        }
                    } catch (_: Exception) {
                        binding.lateText.setText("")
                        fragment.requireView().snackbar("Csak számot adhatsz meg!")
                    }
                }
            }
        }

            init {
                binding.lateText.addTextChangedListener(textWatcher)

                binding.root.setOnClickListener {
                    (fragment.requireActivity() as HomeActivity).hideKeyboard(it)
                }
            }
        }
}