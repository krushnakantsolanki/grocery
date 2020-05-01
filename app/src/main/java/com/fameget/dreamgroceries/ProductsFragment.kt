package com.fameget.dreamgroceries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fameget.dreamgroceries.databinding.FragmentProductsBinding
import com.fameget.dreamgroceries.home.ui.orders.CategoriesAdapter
import com.fameget.dreamgroceries.home.ui.orders.ProductClickListener
import com.fameget.dreamgroceries.home.ui.orders.ProductsAdapter
import kotlinx.android.synthetic.main.fragment_products.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductsFragment : Fragment(), ProductClickListener {
    private lateinit var mBinding: FragmentProductsBinding

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
        mBinding = FragmentProductsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mBinding.ivBack.setOnClickListener { activity?.onBackPressed() }

        mBinding.rvCategories.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mBinding.rvCategories.adapter = CategoriesAdapter(getDummyFilterList())


        mBinding.rvProducts.layoutManager =
            GridLayoutManager(activity, 2)
        mBinding.rvProducts.adapter = ProductsAdapter(this)

        mBinding.ivClose.setOnClickListener { mBinding.floatingView.visibility = View.GONE
            mBinding.viewDim.visibility = View.GONE
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
            mBinding.floatingView.visibility = View.GONE
            mBinding.viewDim.visibility = View.GONE
            mBinding.rlViewCart.visibility = View.VISIBLE
        }

        mBinding.tvViewCart.setOnClickListener {
            val action = ProductsFragmentDirections.actionProductsFragmentToViewCartFragment()
            mBinding.tvViewCart.findNavController().navigate(action)
        }


        // mBinding.floatingView.y = 0f


    }

    private fun getDummyFilterList(): List<String> {
        val list = ArrayList<String>()
        list.add("All")
        list.add("Flower")
        list.add("Grains")
        list.add("Milk Products")
        list.add("Beverages")
        list.add("Vegetables")
        list.add("Snacks")
        return list

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProductsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick() {
        /*val action = ProductsFragmentDirections.actionProductsFragmentToProductDetailFragment()
        mBinding.rvProducts.findNavController().navigate(action)*/

        /*val productDetailFragment: ProductDetailFragment = ProductDetailFragment.newInstance("", "")

        activity?.supportFragmentManager?.let { productDetailFragment.show(it, "fragment") }*/

        //startActivity(Intent(activity, ProductDetailActivity::class.java))
        /*mBinding.floatingView.animate()
            .translationYBy(120f)
            .translationY(0f)
            .setDuration(2000);*/

        mBinding.floatingView.visibility = View.VISIBLE
        mBinding.viewDim.visibility = View.VISIBLE

    }

    fun onBackPressed() {
        if (floatingView.visibility == View.VISIBLE) {
            floatingView.visibility = View.GONE
            viewDim.visibility = View.GONE
        }else{
            activity?.onBackPressed()
        }
    }


}
