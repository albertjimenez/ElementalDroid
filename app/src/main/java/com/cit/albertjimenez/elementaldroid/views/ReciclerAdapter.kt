package com.cit.albertjimenez.elementaldroid.views

/**
 * Created by Albert Jiménez on 29/9/17 for Programming Mobile Devices.
 */
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.cit.albertjimenez.elementaldroid.R
import com.cit.albertjimenez.elementaldroid.dao.Element
import com.cit.albertjimenez.elementaldroid.datastructures.DataManagerJ
import com.cit.albertjimenez.elementaldroid.utils.initialLetter
import es.dmoral.toasty.Toasty


class ReciclerAdapter(val names: MutableList<Element>, val email: String) : RecyclerView.Adapter<ReciclerAdapter.MyViewHolder>() {

    var dataManager: DataManagerJ = DataManagerJ.getInstance()
    var generator = ColorGenerator.MATERIAL

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        holder!!.title.text = names[position].title
        holder.letterMaterial.setImageDrawable(TextDrawable.builder()
                .buildRound(names[position].title.initialLetter(), generator.randomColor))
        holder.itemView.setOnClickListener {
            Toasty.info(holder.view.context, "Removed" + names[position].title + " position  $position").show()
            dataManager.removeElementByUser(email, names[position])
            notifyItemRemoved(position)
        }

    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_row, parent, false)
        return MyViewHolder(view)
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.element_name)
        val letterMaterial = view.findViewById<ImageView>(R.id.letter)
    }
}