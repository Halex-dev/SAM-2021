package com.halex.a2dfarming.`class`

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.halex.a2dfarming.R

class MyAdapter(private var context: Context, private var clickListener: ClickListener) : RecyclerView.Adapter<MyAdapter.MyadapterVh>() {

    private var items: List<ItemModel> = ArrayList<ItemModel>()

    fun setData(items: List<ItemModel>){
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyadapterVh {
        context = parent.context

        return MyadapterVh(LayoutInflater.from(context).inflate(R.layout.row_items, parent, false))
    }

    override fun onBindViewHolder(holder: MyadapterVh, position: Int) {
        val item = items[position]
        val pic = item.pic
        val data = item.data

        holder.ptView.setImageResource(pic)
        holder.tvQuantity.text = "Quantità: ${data.returnAmount()}"
        holder.tvLevel.text = "Livello: ${data.returnLevel()}"
        holder.tvCoin.text = "Coin\\s: ${data.pointSecond()}"
        holder.tvButton.text = "Compra:\n ${data.returnCostAmount()}"

        holder.tvButton.setOnClickListener{
            clickListener.ClickedItem(item)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    interface ClickListener{
        fun ClickedItem(item: ItemModel)
    }

    
    class MyadapterVh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ptView = itemView.findViewById<ImageView>(R.id.potionView)
        var tvQuantity = itemView.findViewById<TextView>(R.id.tvQuantity)
        var tvLevel = itemView.findViewById<TextView>(R.id.tvLevel)
        var tvCoin = itemView.findViewById<TextView>(R.id.tvCoin)
        var tvButton = itemView.findViewById<Button>(R.id.bPotion)
    }
}