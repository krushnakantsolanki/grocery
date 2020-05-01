package com.fameget.dreamgroceries

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.fameget.dreamgroceries.databinding.FragmentManualOrderBinding
import com.fameget.dreamgroceries.utilities.ORD_DILVRY
import com.fameget.dreamgroceries.utilities.ORD_PICK
import com.google.android.material.textfield.TextInputLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ManualOrderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManualOrderFragment : Fragment() {
    private lateinit var manualFragmentListner: ManualFragmentListener
    private lateinit var mBinding: FragmentManualOrderBinding

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        manualFragmentListner = activity as ManualFragmentListener
    }

    interface ManualFragmentListener {
        fun getType(): Int?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentManualOrderBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        for (i in 1..5) {
            val view = LayoutInflater.from(activity).inflate(R.layout.add_item, null, false)
            view.findViewById<TextInputLayout>(R.id.tiItem).setHint("Enter item " + i)
            mBinding.llAddView.addView(view)
        }

        mBinding.tvAddMoreItems.setOnClickListener {
            val view = LayoutInflater.from(activity).inflate(R.layout.add_item, null, false)
            view.findViewById<TextInputLayout>(R.id.tiItem)
                .setHint("Enter item " + (mBinding.llAddView.childCount + 1))
            mBinding.llAddView.addView(view)
            mBinding.scrollView.post { mBinding.scrollView.fullScroll(View.FOCUS_DOWN) }
        }

        mBinding.viewNext.setOnClickListener {
            if (manualFragmentListner.getType() == ORD_PICK) {
                val action =
                    ManualOrderFragmentDirections.actionManualOrderFragmentToSchedulePickupFragment()
                mBinding.viewNext.findNavController().navigate(action)
            } else if (manualFragmentListner.getType() == ORD_DILVRY) {
                val action =
                    ManualOrderFragmentDirections.actionManualOrderFragmentToAddAddressFragment()
                mBinding.viewNext.findNavController().navigate(action)
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
         * @return A new instance of fragment ManualOrderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManualOrderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
