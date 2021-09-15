package com.company.dreamgroceries.addorder.fragments

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.company.dreamgroceries.R
import com.company.dreamgroceries.addorder.ProductsViewModel
import com.company.dreamgroceries.base.BaseFragment
import com.company.dreamgroceries.data.Category
import com.company.dreamgroceries.data.CategoryResponse
import com.company.dreamgroceries.data.Product
import com.company.dreamgroceries.databinding.FragmentProductsBinding
import com.company.dreamgroceries.extensions.getViewModelFactory
import com.company.dreamgroceries.extensions.hideKeyboard
import com.company.dreamgroceries.home.ui.orders.CategoriesAdapter
import com.company.dreamgroceries.home.ui.orders.CategoryClickListener
import com.company.dreamgroceries.home.ui.orders.ProductClickListener
import com.company.dreamgroceries.home.ui.orders.ProductsAdapter
import com.company.dreamgroceries.utilities.CURR
import com.company.dreamgroceries.utilities.REQUEST_GET_PRODUCT_COUNT
import com.company.dreamgroceries.utilities.Utils
import com.company.dreamgroceries.webservices.NetStatus
import com.company.dreamgroceries.webservices.Resource
import com.company.dreamgroceries.webservices.Status
import kotlinx.android.synthetic.main.fragment_products.*
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductsFragment : BaseFragment(), ProductClickListener, CategoryClickListener {
    private val SEARCH: Int = 3
    private val WITH_CATEGORY: Int = 2
    private val NO_CATEGORY: Int = 1
    private lateinit var mProductsAdapter: ProductsAdapter
    private lateinit var mCategoryAdapter: CategoriesAdapter
    private lateinit var productsFragmentListener: ProductsFragmentListener
    private lateinit var mBinding: FragmentProductsBinding

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var timer: Timer = Timer()
    private val DELAY: Long = 600 // m

    private val coroutineScope = lifecycle.coroutineScope

    private val viewModel by lazy {
        requireActivity().getViewModelFactory<ProductsViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        productsFragmentListener = activity as ProductsFragmentListener
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
        viewModel.resetProductsReq()

        initUi()
        handleClicks()

        if (Utils.isNetworkConnected(requireContext()))
            makeAPICalls()
        else
            showNoInternetView()

        mBinding.edtItem.doAfterTextChanged {
            timer.cancel()
            timer = Timer()
            timer.schedule(
                object : TimerTask() {
                    override fun run() {
                        mBinding.edtItem.hideKeyboard()
                        if (!viewModel.productsReq.search.equals(it.toString())) {
                            viewModel.productsReq.search = it.toString()
                            if (Utils.isNetworkConnected(requireContext()))
                                viewModel.refresh()
                            else
                                showNoInternetView()
                        }
                    }
                },
                DELAY
            )
        }


        viewModel.getCartProducts().observe(requireActivity(), Observer {
            if (it.isNotEmpty()) {
                mBinding.tvItemCount.text = getString(R.string.items, it.size)
                mBinding.rlViewCart.visibility = View.VISIBLE
                mBinding.tvFinalPrice.text = "$CURR${Utils.getTotalPrice(it)}"
                mBinding.tvCartCount.text = it.size.toString()
                mBinding.tvCartCount.visibility = View.VISIBLE
            } else {
                mBinding.tvCartCount.visibility = View.GONE
                mBinding.rlViewCart.visibility = View.GONE
            }

        })

        mBinding.swipeLayout.setOnRefreshListener {
            if (Utils.isNetworkConnected(requireContext())) {
                viewModel.productsReq.category_id = getSelectedCategories()
                viewModel.refresh()
            } else {
                showNoInternetView()
            }

        }


    }

    private fun showNoInternetView() {
        mBinding.noData.tvTitle.text = getString(R.string.no_internet_connection)
        mBinding.noData.tvDesc.text = getString(R.string.semms_you_offline)
        mBinding.noData.btnNext.text = getString(R.string.reload)
        mBinding.llNoData.visibility = View.VISIBLE

        mBinding.noData.btnNext.setOnClickListener {
            if (Utils.isNetworkConnected(requireContext())) {
                mBinding.llNoData.visibility = View.GONE
                if (!swipeLayout.isRefreshing && mCategoryAdapter.getSelectedCategoriesId().size == 0 && TextUtils.isEmpty(
                        mBinding.edtItem.text.toString().trim()
                    )
                )
                    makeAPICalls()
                else {
                    viewModel.productsReq.category_id = mCategoryAdapter.getSelectedCategoriesId()
                    viewModel.refresh()
                }
            }
        }
    }

    private fun makeAPICalls() {
        viewModel.userPagedList.observe(viewLifecycleOwner, Observer {

            mProductsAdapter.submitList(it)


        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            mProductsAdapter.setNetworkState(it)
        })

        viewModel.initialLoad.observe(viewLifecycleOwner, Observer {
            if (it?.status == NetStatus.EMPTY) {
                handleNoData()
            } else {
                if (viewModel.productsReq.page == 1) {
                    if (mBinding.swipeLayout.isRefreshing) {
                        mBinding.swipeLayout.isRefreshing = false
                    }
                    //mBinding.tvNoData.visibility = View.GONE
                    mBinding.llChildNoData.visibility = View.GONE
                    mBinding.rvProducts.visibility = View.VISIBLE
                    mBinding.rvProducts.requestFocus()
                }
            }
        })

        viewModel.categories.observe(requireActivity(), Observer { handleCategories(it) })
    }


    private fun handleNoData() {
        if (viewModel.productsReq.page == 1) {
            if (!TextUtils.isEmpty(mBinding.edtItem.text)) {
                //mBinding.tvNoData.text = getString(R.string.no_products_found)
                showNoDataView(SEARCH)
            } else if (viewModel.productsReq.category_id.isEmpty()) {
                showNoDataView(NO_CATEGORY)
                //mBinding.tvNoData.text = getString(R.string.no_products_added)
            } else {
                showNoDataView(WITH_CATEGORY)
                //mBinding.tvNoData.text = getString(R.string.no_products_in_category)
            }
            //mBinding.tvNoData.visibility = View.VISIBLE
            mBinding.rvProducts.visibility = View.GONE
            //mBinding.tvNoData.requestFocus()
        }
    }

    private fun showNoDataView(type: Int) {
        mBinding.llChildNoData.visibility = View.VISIBLE
        mBinding.noData2.btnNext.visibility = View.GONE
        when (type) {
            NO_CATEGORY -> {
                setNoDataViewsText(
                    R.drawable.ic_no_products,
                    getString(R.string.no_products_uploaded),
                    getString(R.string.try_manual_ordering)
                )

            }
            WITH_CATEGORY -> {
                setNoDataViewsText(
                    R.drawable.ic_no_products,
                    getString(R.string.no_products_in_category),
                    getString(R.string.try_manual_ordering)
                )

            }

            SEARCH -> {
                setNoDataViewsText(
                    R.drawable.ic_no_products,
                    getString(R.string.no_products_found),
                    getString(R.string.try_manual_ordering)
                )

            }
        }

    }

    private fun setNoDataViewsText(
        icon: Int,
        title: String,
        desc: String
    ) {
        mBinding.noData2.ivView.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                icon
            )
        )
        mBinding.noData2.tvTitle.text = title
        mBinding.noData2.tvDesc.text = desc
    }

    private fun initUi() {
        mBinding.rvCategories.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        val gridLayoutManager = GridLayoutManager(activity, 2)
        gridLayoutManager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (mProductsAdapter.getItemViewType(position) == R.layout.item_product) {
                    return 1
                } else
                    return 2

            }
        })


        mBinding.rvProducts.layoutManager = gridLayoutManager

        mProductsAdapter = ProductsAdapter(this)
        mBinding.rvProducts.adapter = mProductsAdapter

        mCategoryAdapter = CategoriesAdapter(this)
        mBinding.rvCategories.adapter = mCategoryAdapter


    }

    private fun handleCategories(it: Resource<CategoryResponse>?) {
        it?.let { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    //hideProgress(null)
                    mCategoryAdapter.setList(it.data?.categoryList)

                }
                Status.ERROR -> {
                    //hideProgress(progressBar2)
                    Utils.showToast(requireContext(), resource.message)
                }
                Status.LOADING -> {
                    //  showProgress(null)
                }
            }
        }
    }

    private fun handleClicks() {
        mBinding.ivBack.setOnClickListener { activity?.onBackPressed() }



        mBinding.rlViewCart.setOnClickListener {
            val action =
                ProductsFragmentDirections.actionProductsFragmentToViewCartFragment()
            mBinding.tvViewCart.findNavController().navigate(action)
        }

        mBinding.ivCart.setOnClickListener {
            if (mBinding.rlViewCart.visibility == View.VISIBLE) {
                val action =
                    ProductsFragmentDirections.actionProductsFragmentToViewCartFragment()
                mBinding.tvViewCart.findNavController().navigate(action)
            }
        }
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

    interface ProductsFragmentListener {
        fun getMyFragmentManager(): FragmentManager?
    }

    override fun onClick(product: Product) {

        val fragment = ProductDetailFragment.newInstance(product)
        fragment.setTargetFragment(this, REQUEST_GET_PRODUCT_COUNT)
        productsFragmentListener.getMyFragmentManager()?.let {
            fragment.show(it, "")
        }

    }

    override fun retry() {
        viewModel.retry()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_GET_PRODUCT_COUNT && resultCode == RESULT_OK) {
            val isAnyData: Boolean? = data?.getBooleanExtra("data", false)
            isAnyData?.let {
                if (it)
                    mBinding.rlViewCart.visibility = View.VISIBLE
            }

        }
    }

    override fun onCategoryClicked(
        category: Category,
        position: Int
    ) {
        if (Utils.isNetworkConnected(requireContext())) {
            viewModel.productsReq.category_id = getSelectedCategories()
            viewModel.refresh()
        } else {
            showNoInternetView()
        }

    }

    private fun getSelectedCategories(): IntArray {
        return mCategoryAdapter.getSelectedCategoriesId()

    }


}
