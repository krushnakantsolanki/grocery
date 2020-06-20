package com.fameget.dreamgroceries.addorder.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.customviews.CFTextView
import com.fameget.dreamgroceries.databinding.FragmentThanksBinding
import com.fameget.dreamgroceries.home.ui.profile.AddAddressFragmentArgs
import com.fameget.dreamgroceries.utilities.BUN_1
import com.fameget.dreamgroceries.utilities.BUN_2
import com.fameget.dreamgroceries.utilities.ORD_DILVRY

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ThanksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ThanksFragment : Fragment() {
    private lateinit var thanksFragmentListner: ThanksFragmentListener
    private lateinit var mBidning: FragmentThanksBinding

    val args: ThanksFragmentArgs by navArgs()

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
        thanksFragmentListner = activity as ThanksFragmentListener
    }

    interface ThanksFragmentListener {
        fun getType(): Int?
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBidning = FragmentThanksBinding.inflate(inflater, container, false)
        return mBidning.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBidning.tvMyOrders.setOnClickListener {
            val intent = Intent()
            intent.putExtra(BUN_1, R.id.rlOrders)
            intent.putExtra(BUN_2, args.orderNo)
            activity?.setResult(RESULT_OK, intent)
            activity?.finish()
            activity?.overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }

        mBidning.tvContactSupport.setOnClickListener {
            val intent = Intent()
            intent.putExtra(BUN_1, R.id.rlSupport)
            activity?.setResult(RESULT_OK, intent)
            activity?.finish()
            activity?.overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }





        if (thanksFragmentListner.getType() == ORD_DILVRY)
            mBidning.root.findViewById<CFTextView>(R.id.tvInfo).visibility = View.VISIBLE
        else
            mBidning.root.findViewById<CFTextView>(R.id.tvInfo).visibility = View.GONE
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ThanksFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ThanksFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
