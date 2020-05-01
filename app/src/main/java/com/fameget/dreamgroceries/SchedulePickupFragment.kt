package com.fameget.dreamgroceries

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.fameget.dreamgroceries.addorder.AddOrderActivity
import com.fameget.dreamgroceries.databinding.FragmentSchedulePickupBinding
import com.fameget.dreamgroceries.home.ui.orders.SchedulePickUpActivity
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
class SchedulePickupFragment : Fragment() {
    private lateinit var mBinding: FragmentSchedulePickupBinding

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
        mBinding = FragmentSchedulePickupBinding.inflate(layoutInflater, container, false)
        return mBinding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mBinding.ivBack.setOnClickListener { activity?.onBackPressed() }

        mBinding.viewCalender.setOnClickListener { openCaleder() }

        mBinding.viewNext.setOnClickListener {
            if (activity is AddOrderActivity) {
                val action =
                    SchedulePickupFragmentDirections.actionSchedulePickupFragmentToThanksFragment()
                mBinding.viewNext.findNavController().navigate(action)
            } else if (activity is SchedulePickUpActivity) {
                activity?.onBackPressed()
                activity?.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
            }

        }
    }

    private fun openCaleder() {
        val currentDate: Calendar = Calendar.getInstance()
        val date = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(
                    requireContext(),
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)
                        mBinding.edtItem.setText(formatDate(date.time))
                    },
                    currentDate.get(Calendar.HOUR_OF_DAY),
                    currentDate.get(Calendar.MINUTE),
                    false
                ).show()
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DATE)
        ).show()
    }

    private fun formatDate(date: Date): String {
        val format = SimpleDateFormat("dd MMM yyyy HH:MM")
        return format.format(date)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SchedulePickupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SchedulePickupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
