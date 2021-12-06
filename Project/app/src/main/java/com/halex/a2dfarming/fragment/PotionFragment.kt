package com.halex.a2dfarming.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.halex.a2dfarming.R
import com.halex.a2dfarming.`class`.ItemModel
import com.halex.a2dfarming.`class`.MyAdapter
import com.halex.a2dfarming.view.PointViewModel


class PotionFragment : Fragment(), MyAdapter.ClickListener {

    //ViewModel per l'aggiornamento dei punteggi
    private lateinit var viewModel: PointViewModel

    //RecyclerView
    private lateinit var MyAdapter : MyAdapter
    private lateinit var toast : Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyAdapter = MyAdapter(requireActivity(), this)
        viewModel =  ViewModelProvider(requireActivity()).get(PointViewModel::class.java)
        toast = Toast.makeText(requireActivity(), "Non hai abbastanza soldi!", Toast.LENGTH_SHORT)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,  savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_potion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        InitRecycler()
    }

    //Inizializzazione Recycler view
    private fun InitRecycler(){
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.itemRecylerView)
        MyAdapter.setData(viewModel.returnData())
        recyclerView?.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView?.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
        recyclerView?.adapter = MyAdapter
    }

    override fun ClickedItem(item: ItemModel) {
        if(!viewModel.addItem(item.name))
            toast.show()
        else
            MyAdapter.setData(viewModel.returnData())
    }

}