package com.company.dreamgroceries.addorder.fragments

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.company.dreamgroceries.R
import com.company.dreamgroceries.addorder.AddOrderActivity
import com.company.dreamgroceries.addorder.ManualOrderViewModel
import com.company.dreamgroceries.base.BaseFragment
import com.company.dreamgroceries.data.PlaceOrderResponse
import com.company.dreamgroceries.databinding.FragmentSchedulePickupBinding
import com.company.dreamgroceries.extensions.getViewModelFactory
import com.company.dreamgroceries.home.ui.orders.SchedulePickUpActivity
import com.company.dreamgroceries.utilities.BUN_1
import com.company.dreamgroceries.utilities.Utils
import com.company.dreamgroceries.webservices.Resource
import com.company.dreamgroceries.webservices.Status
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SchedulePickupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SchedulePickupFragment : BaseFragment() {
    private lateinit var mBinding: FragmentSchedulePickupBinding
    val args: SchedulePickupFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    val viewModel by lazy {
        requireActivity().getViewModelFactory<ManualOrderViewModel>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentSchedulePickupBinding.inflate(layoutInflater, container, false)
        return mBinding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is SchedulePickUpActivity) {
            mBinding.tvScdPickUp.text = getString(R.string.submit)
        }

        if (viewModel.pickTime != null) {
            mBinding.edtItem.setText(viewModel.pickTime)
        }

        mBinding.ivBack.setOnClickListener { activity?.onBackPressed() }

        mBinding.viewCalender.setOnClickListener { openCaleder() }

        mBinding.viewNext.setOnClickListener {
            if (activity is AddOrderActivity) {
                if (!mBinding.tiItem.isTilEmpty()) {
                    // placeOrder()
                    Utils.showDialog(
                        requireContext(),
                        getString(R.string.app_name),
                        getString(R.string.verify_order_texr),
                        getString(R.string.confirm_order),
                        getString(R.string.edit_order),
                        DialogInterface.OnClickListener { dialogInterface, i -> placeOrder() },
                        DialogInterface.OnClickListener { dialogInterface, i -> saveDataState() },
                        getString(R.string.cancel)

                    )

                }
            } else if (activity is SchedulePickUpActivity) {
                if (!mBinding.tiItem.isTilEmpty()) {
                    val intent = Intent()
                    intent.putExtra(BUN_1, mBinding.edtItem.text.toString())
                    activity?.setResult(RESULT_OK, intent)
                    activity?.onBackPressed()
                    activity?.overridePendingTransition(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right
                    )
                }
            }

        }
    }

    private fun saveDataState() {
        //activity?.onBackPressed()
        /* val action = SchedulePickupFragmentDirections.actionSchedulePickupFragmentToManualOrderFragment()
         mBinding.viewNext.findNavController().navigate(action)*/
        mBinding.viewNext.findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "key",
            viewModel.mTempList
        )
        mBinding.viewNext.findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "key2",
            viewModel.addInst
        )
        mBinding.viewNext.findNavController().popBackStack()

        viewModel.pickTime = mBinding.edtItem.text.toString()
    }

    private fun placeOrder() {
        viewModel.placePickUpOrder(
            viewModel.createPickUpOrderReq(
                args.productList,
                args.addInst,
                Utils.getTimeInSeconds(mBinding.edtItem.text.toString()).toString()
            )
        ).observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { handlePlaceOrder(it) })
    }

    private fun handlePlaceOrder(resource: Resource<PlaceOrderResponse>?) {
        when (resource?.status) {
            Status.SUCCESS -> {
                hideProgress(mBinding.progressBar)

                resource.data?.let {
                    val action =
                        SchedulePickupFragmentDirections.actionSchedulePickupFragmentToThanksFragment(
                            it.order_no
                        )
                    mBinding.viewNext.findNavController().navigate(action)
                }

                Utils.showToastShort(requireContext(), resource.data?.message)
            }
            Status.ERROR -> {

                hideProgress(mBinding.progressBar)
                Utils.showToast(requireContext(), resource.message)
            }
            Status.LOADING -> {
                showProgress(mBinding.progressBar)
            }
        }
    }

    private fun openCaleder() {
        val currentDate: Calendar = Calendar.getInstance()
        val date = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(
                    requireContext(),
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)
                        if (date.time.time > System.currentTimeMillis())
                            mBinding.edtItem.setText(formatDate(date.time))
                        else {
                            Utils.showToast(
                                requireContext(),
                                getString(R.string.pickup_time_not_valid)
                            )
                            openCaleder()
                        }
                    },
                    currentDate.get(Calendar.HOUR_OF_DAY),
                    currentDate.get(Calendar.MINUTE),
                    false
                ).show()
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DATE)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun formatDate(date: Date): String {
        val format = SimpleDateFormat("dd MMM yyyy hh:mm aa")
        return format.format(date)
    }


}
