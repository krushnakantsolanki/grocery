package com.company.dreamgroceries.addorder.fragments

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.company.dreamgroceries.MyApp
import com.company.dreamgroceries.R
import com.company.dreamgroceries.addorder.ProductsViewModel
import com.company.dreamgroceries.data.Product
import com.company.dreamgroceries.data.Productimage
import com.company.dreamgroceries.databinding.FragmentProductDetailBinding
import com.company.dreamgroceries.extensions.getViewModelFactory
import com.company.dreamgroceries.home.ui.orders.ImagePagerAdapter
import com.company.dreamgroceries.utilities.CURR
import com.company.dreamgroceries.utilities.REQUEST_GET_PRODUCT_COUNT


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductDetailFragment : DialogFragment() {
    private lateinit var mBinding: FragmentProductDetailBinding
    private lateinit var dialogView: View

    val viewModel by lazy {
        requireActivity().getViewModelFactory<ProductsViewModel>()
    }

    // TODO: Rename and change types of parameters
    private var product: Product? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product = arguments?.getParcelable<Product>(ARG_PARAM1)
    }

    override fun onResume() {
        super.onResume()
        setDialogSize()
    }

    private fun setDialogSize() {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        /*dialog?.window?.apply {
            setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
        }*/
        val height = Resources.getSystem().displayMetrics.heightPixels
        val width = Resources.getSystem().displayMetrics.widthPixels

        dialog?.let {
            val params = dialog?.window?.attributes
            params?.let {
                params.width = width - resources.getDimensionPixelSize(R.dimen.dp_32)
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                dialog?.window?.attributes = params
            }


        }

        // dialog?.getWindow()?.setLayout(width * 0.9.toInt(), height * 0.6.toInt());
    }

    /*fun alignToBottom() {
        dialog.window.apply {
            setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
            decorView.apply {

                // Get screen width
                val displayMetrics = DisplayMetrics().apply {
                    windowManager.defaultDisplay.getMetrics(this)
                }

                setBackgroundColor(Color.WHITE) // I don't know why it is required, without it background of rootView is ignored (is transparent even if set in xml/runtime)
                minimumWidth = displayMetrics.widthPixels
                setPadding(0, 0, 0, 0)
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                invalidate()
            }
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentProductDetailBinding.inflate(inflater, container, false)

        return mBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAnimations()
        setClickListeners()
        initUi()

        product?.run {
            viewModel.getProductCount(id).observe(viewLifecycleOwner, Observer {
                it?.run {
                    mBinding.tvCount.text = this.toString()
                }
            })
        }


    }

    private fun initUi() {
        product?.let {
            mBinding.tvPrdTitle.text = it.name
            mBinding.tvDesc.text = it.description
            mBinding.tvPrice.text = "$CURR${it.mrp}"
            //mBinding.tvCurrentPage.setViewPager(mBinding.imageViewPager)
            if (it.productimages.isEmpty()) {
                addDefaultProductImage(it)?.let { it1 -> it.productimages.add(it1) }
            }
            mBinding.imageViewPager.adapter = ImagePagerAdapter(it.productimages)
            mBinding.tvCount.text = getInitialCount(it).toString()
            mBinding.tvCurrentPage.text = "${1}/${it.productimages.size}"
            mBinding.imageViewPager.registerOnPageChangeCallback(object :

                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mBinding.tvCurrentPage.text = "${position + 1}/${it.productimages.size}"
                }
            })
        }


    }

    private fun addDefaultProductImage(it: Product): Productimage? {
        return it.image?.let { it1 -> Productimage(it1, 0) }
    }

    private fun getInitialCount(product: Product): Int {
        return if (product.cart_count > 0) product.cart_count else 1
    }

    private fun setClickListeners() {
        mBinding.ivClose.setOnClickListener {
            dialog?.dismiss()
        }

        mBinding.ivAdd.setOnClickListener {
            val count = mBinding.tvCount.text.toString().toInt()
            mBinding.tvCount.setText((count + 1).toString())
        }

        mBinding.ivSub.setOnClickListener {
            val count = mBinding.tvCount.text.toString().toInt()
            if (count > 1)
                mBinding.tvCount.setText((count - 1).toString())
        }

        mBinding.btnAdd.setOnClickListener {
            product?.let {
                it.cart_count = mBinding.tvCount.text.toString().toInt()
                AsyncTask.execute { MyApp.getInstance().cartDao().insertProduct(it) }

            }


            val intent = Intent()
            intent.putExtra("data", true)
            targetFragment?.onActivityResult(
                REQUEST_GET_PRODUCT_COUNT,
                Activity.RESULT_OK,
                intent
            );

            dialog?.dismiss()
        }
    }

    private fun setAnimations() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.attributes?.windowAnimations = R.style.Prd_Dlg_Anim
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProductDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Product) =
            ProductDetailFragment()
                .apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_PARAM1, param1)

                    }
                }
    }
}
