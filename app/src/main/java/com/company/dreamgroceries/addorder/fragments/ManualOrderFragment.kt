package com.company.dreamgroceries.addorder.fragments

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.company.dreamgroceries.R
import com.company.dreamgroceries.addorder.ManualOrderViewModel
import com.company.dreamgroceries.customviews.CFEditText
import com.company.dreamgroceries.databinding.FragmentManualOrderBinding
import com.company.dreamgroceries.extensions.getViewModelFactory
import com.company.dreamgroceries.utilities.ORD_DILVRY
import com.company.dreamgroceries.utilities.ORD_PICK
import com.company.dreamgroceries.utilities.Utils
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
    private var mAddressCount: Int? = 0
    private lateinit var productList: java.util.ArrayList<String>
    private lateinit var manualFragmentListner: ManualFragmentListener
    private lateinit var mBinding: FragmentManualOrderBinding

    val viewModel by lazy {
        requireActivity().getViewModelFactory<ManualOrderViewModel>()
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* viewModel.productList.observe(requireActivity(), Observer {
             setInitialData(it)
         })*/

        val navController = mBinding.scrollView.findNavController();
// We use a String here, but any type that can be put in a Bundle is supported
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<List<String>>("key")
            ?.observe(viewLifecycleOwner,
                Observer { setInitialData(it) })

        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("key2")
            ?.observe(viewLifecycleOwner,
                Observer {
                    mBinding.edtInst.setText(it)
                    viewModel.addInst = ""
                })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        for (i in 1..5) {
            val view = LayoutInflater.from(activity).inflate(R.layout.add_item, null, false)
            view.findViewById<TextInputLayout>(R.id.tiItem).setHint("Enter item " + i)
            mBinding.llAddView.addView(view)
        }

        viewModel.getAddressCount().observe(viewLifecycleOwner, Observer {
            mAddressCount = it
        })

        mBinding.tvAddMoreItems.setOnClickListener {
            val view = LayoutInflater.from(activity).inflate(R.layout.add_item, null, false)
            view.findViewById<TextInputLayout>(R.id.tiItem)
                .setHint("Enter item " + (mBinding.llAddView.childCount + 1))
            mBinding.llAddView.addView(view)
            mBinding.scrollView.post {
                mBinding.scrollView.fullScroll(View.FOCUS_DOWN)
                view.findViewById<CFEditText>(R.id.edtItem).requestFocus()
            }
        }

        mBinding.viewNext.setOnClickListener {
            if (manualFragmentListner.getType() == ORD_PICK) {
                if (validateInputs()) {
                    viewModel.setValueToProductList(productList)
                    viewModel.addInst = mBinding.edtInst?.text.toString().trim()
                    val action =
                        ManualOrderFragmentDirections.actionManualOrderFragmentToSchedulePickupFragment(
                            productList.toTypedArray(),
                            mBinding.edtInst?.text.toString().trim()
                        )
                    mBinding.viewNext.findNavController().navigate(action)

                }
            } else if (manualFragmentListner.getType() == ORD_DILVRY) {
                if (validateInputs()) {
                    if (mAddressCount == 0) {
                        /*val intent = Intent(activity, AddAddressActivity::class.java)
                    startActivity(intent)*/
                        val action =
                            ManualOrderFragmentDirections.actionManualOrderFragmentToAddAddressFragment(
                                productList.toTypedArray(),
                                mBinding.edtInst?.text.toString().trim()
                            )
                        mBinding.viewNext.findNavController().navigate(action)

                    } else {
                        /*val intent = Intent(activity, AddressListActivity::class.java)
                        startActivity(intent)*/

                        val action =
                            ManualOrderFragmentDirections.actionManualOrderFragmentToAddressListFragment(
                                productList.toTypedArray(),
                                mBinding.edtInst?.text.toString().trim()
                            )
                        mBinding.viewNext.findNavController().navigate(action)

                    }
                    /*   activity?.overridePendingTransition(
                           R.anim.slide_in_right,
                           R.anim.slide_out_left
                       )*/
                }


                /*val action =
                    ManualOrderFragmentDirections.actionManualOrderFragmentToAddAddressFragment()
                mBinding.viewNext.findNavController().navigate(action)*/
            }
        }

        mBinding.ivBack.setOnClickListener { activity?.onBackPressed() }

        //   setInitialData()
    }

    private fun setInitialData(it: List<String>) {
        //   mBinding.llAddView.removeAllViews()
        if (it.isEmpty()) {
            for (i in 1..5) {
                val view = LayoutInflater.from(activity).inflate(R.layout.add_item, null, false)
                view.findViewById<TextInputLayout>(R.id.tiItem).setHint("Enter item " + i)
                mBinding.llAddView.addView(view)
            }
        } else {
            mBinding.llAddView.visibility = View.GONE
            mBinding.llAddView.removeAllViews()
            for (i in it.indices) {
                val view = LayoutInflater.from(activity).inflate(R.layout.add_item, null, false)
                val editText = view.findViewById<CFEditText>(R.id.edtItem)
                val tiItem = view.findViewById<TextInputLayout>(R.id.tiItem)
                tiItem.setHint("Enter item " + (i + 1))
                editText.setText(it[i])
                mBinding.llAddView.addView(view)
            }
            if (it.size < 5) {
                var diff = 5 - it.size
                if (diff > 0) {
                    while (diff > 0) {
                        val view =
                            LayoutInflater.from(activity).inflate(R.layout.add_item, null, false)
                        view.findViewById<TextInputLayout>(R.id.tiItem)
                            .setHint("Enter item " + (mBinding.llAddView.childCount + 1))
                        mBinding.llAddView.addView(view)
                        diff--
                    }
                }

            }
            mBinding.llAddView.visibility = View.VISIBLE
            viewModel.mTempList.clear()
        }


    }

    private fun validateInputs(): Boolean {
        productList = ArrayList<String>()
        for (x in 0 until mBinding.llAddView.childCount) {
            val text =
                ((mBinding.llAddView.getChildAt(x) as LinearLayout).get(0) as TextInputLayout).editText?.text.toString()
                    .trim()
            if (!TextUtils.isEmpty(text)) {
                productList.add(text)
            }
        }
        if (productList.isEmpty()) {
            Utils.showToastShort(
                requireContext(),
                getString(R.string.please_add_atleast_one_product)
            )
            return false
        } /*else {
            var diff = mBinding.llAddView.childCount - productList.size
            if (diff > 0) {
                while (diff > 0) {
                    mBinding.llAddView.removeViewAt(mBinding.llAddView.childCount - 1)
                    diff--
                }
            }
        }*/
        return true
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
