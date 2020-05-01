package com.example.myapplication.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.viewmodel.DetailViewModel
import com.example.myapplication.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    //View model instance
    lateinit var viewModel: DetailViewModel

    private var dogUuid = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Recuperou a instancia do viewModel
        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.refresh()

        observeViewModel()

        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid
        }
    }

    private fun observeViewModel() {
        viewModel.dogs.observe(this, Observer {
            text_view_dog_life_span.text = it.lifeSpan
            text_view_dog_name.text = it.dogBreed
            text_view_dog_temperament.text = it.temperament
            text_view_dog_purpose.text = it.breedGroup
        })
    }

}
