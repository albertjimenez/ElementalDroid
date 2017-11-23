package com.cit.albertjimenez.elementaldroid.views

/**
 * Created by Albert Jiménez on 29/9/17 for Programming Mobile Devices.
 */
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.cit.albertjimenez.elementaldroid.R
import com.cit.albertjimenez.elementaldroid.ViewItemActivity
import com.cit.albertjimenez.elementaldroid.dao.Element
import com.cit.albertjimenez.elementaldroid.utils.initialLetter
import es.dmoral.toasty.Toasty
import org.jetbrains.anko.startActivity


class ReciclerAdapter(val names: MutableList<Element>, val email: String) : RecyclerView.Adapter<ReciclerAdapter.MyViewHolder>() {

    private var generator = ColorGenerator.MATERIAL

    override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {

        holder?.let {
            with(holder) {
                if (names.isNotEmpty()) {
                    title.text = names[position].title
                    subtitle.text = names[position].extract
                    letterMaterial.setImageDrawable(TextDrawable.builder()
                            .buildRound(names[position].title.initialLetter(), generator.randomColor))
                    itemView.setOnClickListener {
                        holder.view.context.startActivity<ViewItemActivity>("element" to names[position], "email" to email)
                    }

                }
            }
        }


    }

    override fun getItemCount(): Int = names.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_row, parent, false)
        return MyViewHolder(view)
    }

    class MyViewHolder : RecyclerView.ViewHolder, View.OnCreateContextMenuListener {
        val view: View

        constructor(view: View) : super(view) {
            this.view = view
            this.title = view.findViewById(R.id.element_name)
            this.subtitle = view.findViewById(R.id.element_subtext)
            this.letterMaterial = view.findViewById(R.id.letter)
            view.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(p0: ContextMenu?, p1: View?, p2: ContextMenu.ContextMenuInfo?) {
            val copy = p0?.add(Menu.NONE, 1, 1, "Action")
            copy?.setOnMenuItemClickListener {
                Toasty.info(p1!!.context, "You clicked me").show()
                return@setOnMenuItemClickListener true
            }
        }

        val title: TextView
        val letterMaterial: ImageView
        val subtitle: TextView
    }
}