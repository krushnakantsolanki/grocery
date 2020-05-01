package com.fameget.dreamgroceries.login.fragments


import android.app.Service
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.fameget.dreamgroceries.R
import com.fameget.dreamgroceries.databinding.FragmentEnterOtpBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EnterOtpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EnterOtpFragment : Fragment(), View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {
    private lateinit var mBinding: FragmentEnterOtpBinding

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
        mBinding = FragmentEnterOtpBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setPINListeners()
        mBinding.ivBack.setOnClickListener { activity?.onBackPressed() }

        mBinding.btnNext.setOnClickListener {
            val action =
                EnterOtpFragmentDirections
                    .actionEnterOtpFragmentToEnterDetailsFragment()
            mBinding.btnNext.findNavController().navigate(action)
        }
    }

    /**
     * Shows soft keyboard.
     *
     * @param editText EditText which has focus
     */
    fun showSoftKeyboard(editText: EditText?) {
        if (editText == null) return

/*        val imm: InputMethodManager? =
            getSystemService<Any>(Service.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm.showSoftInput(editText, 0)*/
        val imm: InputMethodManager =
            activity?.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, 0)
    }

    /**
     * Sets focus on a specific EditText field.
     *
     * @param editText EditText to set focus on
     */
    fun setFocus(editText: EditText?) {
        if (editText == null) return
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.requestFocus()
    }

    private fun setPINListeners() {

        mBinding.pinHiddenEdittext.addTextChangedListener(this)

        mBinding.pinFirstEdittext.setOnFocusChangeListener(this)
        mBinding.pinSecondEdittext.setOnFocusChangeListener(this)
        mBinding.pinThirdEdittext.setOnFocusChangeListener(this)
        mBinding.pinForthEdittext.setOnFocusChangeListener(this)
        mBinding.pinFifthEdittext.setOnFocusChangeListener(this)
        mBinding.pinSixthEdittext.setOnFocusChangeListener(this)

        mBinding.pinFirstEdittext.setOnKeyListener(this)
        mBinding.pinSecondEdittext.setOnKeyListener(this)
        mBinding.pinThirdEdittext.setOnKeyListener(this)
        mBinding.pinForthEdittext.setOnKeyListener(this)
        mBinding.pinFifthEdittext.setOnKeyListener(this)
        mBinding.pinSixthEdittext.setOnKeyListener(this)

        mBinding.pinHiddenEdittext.setOnKeyListener(this)
    }


    /**
     * Hides soft keyboard.
     *
     * @param editText EditText which has focus
     */
    fun hideSoftKeyboard(editText: EditText?) {
        val imm: InputMethodManager =
            activity?.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText?.windowToken, 0)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EnterOtpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EnterOtpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        val id: Int = v!!.getId()
        when (id) {
            R.id.pin_first_edittext -> if (hasFocus) {
                setFocus(mBinding.pinHiddenEdittext)
                showSoftKeyboard(mBinding.pinHiddenEdittext)
            }
            R.id.pin_second_edittext -> if (hasFocus) {
                setFocus(mBinding.pinHiddenEdittext)
                showSoftKeyboard(mBinding.pinHiddenEdittext)
            }
            R.id.pin_third_edittext -> if (hasFocus) {
                setFocus(mBinding.pinHiddenEdittext)
                showSoftKeyboard(mBinding.pinHiddenEdittext)
            }
            R.id.pin_forth_edittext -> if (hasFocus) {
                setFocus(mBinding.pinHiddenEdittext)
                showSoftKeyboard(mBinding.pinHiddenEdittext)
            }
            R.id.pin_fifth_edittext -> if (hasFocus) {
                setFocus(mBinding.pinHiddenEdittext)
                showSoftKeyboard(mBinding.pinHiddenEdittext)
            }
            R.id.pin_sixth_edittext -> if (hasFocus) {
                setFocus(mBinding.pinHiddenEdittext)
                showSoftKeyboard(mBinding.pinHiddenEdittext)
            }
            else -> {
            }
        }
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.getAction() === KeyEvent.ACTION_DOWN) {
            val id: Int = v!!.getId()
            when (id) {
                mBinding.pinHiddenEdittext.id -> if (keyCode === android.view.KeyEvent.KEYCODE_DEL) {
                    if (mBinding.pinHiddenEdittext.getText()?.length === 6)
                        mBinding.pinSixthEdittext.setText("")
                    else if (mBinding.pinHiddenEdittext.getText()?.length === 5) mBinding.pinFifthEdittext.setText(
                        ""
                    )
                    else if (mBinding.pinHiddenEdittext.getText()?.length === 4
                    ) mBinding.pinForthEdittext.setText("")
                    else if (mBinding.pinHiddenEdittext.getText()?.length === 3
                    ) mBinding.pinThirdEdittext.setText("")
                    else if (mBinding.pinHiddenEdittext.getText()?.length === 2
                    ) mBinding.pinSecondEdittext.setText("")
                    else if (mBinding.pinHiddenEdittext.getText()?.length === 1
                    ) mBinding.pinFirstEdittext.setText("")
                    if (mBinding.pinHiddenEdittext.length() > 0) mBinding.pinHiddenEdittext.setText(
                        mBinding.pinHiddenEdittext.getText()
                            ?.subSequence(0, mBinding.pinHiddenEdittext.length() - 1)
                    )
                    return true
                }
                else -> return false
            }
        }
        return false
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if (s?.length == 0) {
            mBinding.pinFirstEdittext.setText("");
        } else if (s?.length == 1) {
            mBinding.pinFirstEdittext.setText(s.get(0) + "");
            mBinding.pinSecondEdittext.setText("");
            mBinding.pinThirdEdittext.setText("");
            mBinding.pinForthEdittext.setText("");
            mBinding.pinFifthEdittext.setText("");
            mBinding.pinSixthEdittext.setText("");
        } else if (s?.length == 2) {
            mBinding.pinSecondEdittext.setText(s.get(1) + "");
            mBinding.pinThirdEdittext.setText("");
            mBinding.pinForthEdittext.setText("");
            mBinding.pinFifthEdittext.setText("");
            mBinding.pinSixthEdittext.setText("");
        } else if (s?.length == 3) {
            mBinding.pinThirdEdittext.setText(s.get(2) + "");
            mBinding.pinForthEdittext.setText("");
            mBinding.pinFifthEdittext.setText("");
            mBinding.pinSixthEdittext.setText("");
        } else if (s?.length == 4) {
            mBinding.pinForthEdittext.setText(s.get(3) + "");
            mBinding.pinFifthEdittext.setText("");
            mBinding.pinSixthEdittext.setText("");
        } else if (s?.length == 5) {
            mBinding.pinFifthEdittext.setText(s.get(4) + "");
            mBinding.pinSixthEdittext.setText("");

        } else if (s?.length == 6) {
            mBinding.pinSixthEdittext.setText(s.get(5) + "");

            hideSoftKeyboard(mBinding.pinSixthEdittext);
        }
    }
}
