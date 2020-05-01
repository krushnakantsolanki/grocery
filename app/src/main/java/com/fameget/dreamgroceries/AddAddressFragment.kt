package com.fameget.dreamgroceries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.fameget.dreamgroceries.addorder.AddOrderActivity
import com.fameget.dreamgroceries.databinding.FragmentAddAddressBinding
import com.fameget.dreamgroceries.home.ui.profile.AddAddressActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddAddressFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddAddressFragment : Fragment() {
    private lateinit var mBinding: FragmentAddAddressBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentAddAddressBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBinding.btnNext.setOnClickListener {
            if (activity is AddOrderActivity) {
                val action = AddAddressFragmentDirections.actionAddAddressFragmentToThanksFragment()
                mBinding.btnNext.findNavController().navigate(action)
            } else if (activity is AddAddressActivity) {
                activity?.onBackPressed()
                activity?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }
        }

        mBinding.ivBack.setOnClickListener { activity?.onBackPressed() }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddAddressFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddAddressFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}