package com.fameget.dreamgroceries

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fameget.dreamgroceries.databinding.FragmentViewCartBinding
import com.fameget.dreamgroceries.home.ui.orders.AddressAdapter
import com.fameget.dreamgroceries.home.ui.orders.SchedulePickUpActivity
import com.fameget.dreamgroceries.home.ui.orders.ViewCartProductsAdapter
import com.fameget.dreamgroceries.home.ui.profile.AddAddressActivity
import com.fameget.dreamgroceries.utilities.ORD_PICK

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewCartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewCartFragment : Fragment() {
    private lateinit var viewCartFragmentListner: ViewCartFragmentListener
    private lateinit var mBinding: FragmentViewCartBinding

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
        viewCartFragmentListner = activity as ViewCartFragmentListener

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentViewCartBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mBinding.rvProducts.layoutManager =
            LinearLayoutManager(activity)
        mBinding.rvProducts.adapter = ViewCartProductsAdapter()

        mBinding.rvAddress.layoutManager =
            LinearLayoutManager(activity)
        mBinding.rvAddress.adapter = AddressAdapter()

        if (viewCartFragmentListner.getType() == ORD_PICK) {
            hideShowScdView(true)
            hideShowAddAddressView(false)
        } else {
            hideShowScdView(false)
            hideShowAddAddressView(true)
        }

        mBinding.tvScdPickUp.setOnClickListener {
            /*val action = ViewCartFragmentDirections.actionViewCartFragmentToSchedulePickupFragment()
            mBinding.tvScdPickUp.findNavController().navigate(action)*/
            val intent = Intent(activity, SchedulePickUpActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        mBinding.tvAddAddress.setOnClickListener {
            /*val action = ViewCartFragmentDirections.actionViewCartFragmentToAddAddressFragment()
            mBinding.tvAddAddress.findNavController().navigate(action)*/
            val intent = Intent(activity, AddAddressActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        mBinding.viewPayLater.setOnClickListener {
            val action = ViewCartFragmentDirections.actionViewCartFragmentToThanksFragment()
            mBinding.viewPayLater.findNavController().navigate(action)
        }

        mBinding.viewPayNow.setOnClickListener {
            val action = ViewCartFragmentDirections.actionViewCartFragmentToMakePaymentFragment()
            mBinding.viewPayLater.findNavController().navigate(action)
        }

        mBinding.ivBack.setOnClickListener { activity?.onBackPressed() }

    }

    private fun hideShowAddAddressView(show: Boolean) {
        if (show) {
            mBinding.rvAddress.visibility = View.VISIBLE
            mBinding.tvAChooseAddress.visibility = View.VISIBLE
            mBinding.tvAddAddress.visibility = View.VISIBLE
        } else {
            mBinding.rvAddress.visibility = View.GONE
            mBinding.tvAChooseAddress.visibility = View.GONE
            mBinding.tvAddAddress.visibility = View.GONE
        }
    }

    private fun hideShowScdView(show: Boolean) {
        if (show) {
            mBinding.tvScdPickUp.visibility = View.VISIBLE
            mBinding.tvScdPickUpLbl.visibility = View.VISIBLE
        } else {
            mBinding.tvScdPickUp.visibility = View.GONE
            mBinding.tvScdPickUpLbl.visibility = View.GONE
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewCartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ViewCartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    interface ViewCartFragmentListener {
        fun getType(): Int?
    }
}
