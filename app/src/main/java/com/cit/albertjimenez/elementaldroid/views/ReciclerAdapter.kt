package com.cit.albertjimenez.elementaldroid.views

/**
 * Created by Albert Jiménez on 29/9/17 for Programming Mobile Devices.
 */
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.cit.albertjimenez.elementaldroid.R
import com.cit.albertjimenez.elementaldroid.dao.Element
import com.cit.albertjimenez.elementaldroid.utils.initialLetter


class ReciclerAdapter(val names: MutableList<Element>) : RecyclerView.Adapter<ReciclerAdapter.MyViewHolder>() {
    var generator = ColorGenerator.MATERIAL
    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
        holder!!.title.text = names[position].name
        holder.view.setOnClickListener { Log.d("ELEMENT", "clicked " + position) }
        holder.letterMaterial.setImageDrawable(TextDrawable.builder()
                .buildRound(names[position].name.initialLetter(), generator.randomColor))


    }

    override fun getItemCount(): Int {
        return names.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent?.context).inflate(R.layout.list_row, parent, false)
        return MyViewHolder(view)
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.element_name)
        val letterMaterial = view.findViewById<ImageView>(R.id.letter)
    }
}