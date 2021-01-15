package com.yogesh.tinderdemo

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.yogesh.tinderdemo.adapter.UserProfileAdapter
import com.yogesh.tinderdemo.database.UsersDatabase
import com.yogesh.tinderdemo.model.Result
import com.yogesh.tinderdemo.repository.UsersRepositoryImpl
import com.yogesh.tinderdemo.retrofit.RetrofitBuilder
import com.yogesh.tinderdemo.util.NetworkHelper
import com.yogesh.tinderdemo.viewmodel.MainViewModel
import com.yogesh.tinderdemo.viewmodel.MainViewModelFactory
import com.yuyakaido.android.cardstackview.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CardStackListener {

    private var mDirection: Direction = Direction.Top
    private lateinit var mProfileItem: Result
    private val mAdapter = UserProfileAdapter(this)
    private lateinit var layoutManager: CardStackLayoutManager
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpViewModel()
        observeViewModel()
    }

    private fun setUpViewModel() {
        showProgress()
        val networkHelper = NetworkHelper(this);
        val usersDao = UsersDatabase.getInstance(this).usersDao();

        viewModel = ViewModelProvider(
            this,
            MainViewModelFactory(
                networkHelper,
                UsersRepositoryImpl(usersDao, RetrofitBuilder.buildService())
            )
        )[MainViewModel::class.java]
        viewModel.onRefresh()

        layoutManager = CardStackLayoutManager(this, this).apply {
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            setOverlayInterpolator(LinearInterpolator())
        }

        stack_view.layoutManager = layoutManager
        stack_view.adapter = mAdapter
        stack_view.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun observeViewModel() {
        viewModel.usersResponse.observe(this, Observer {
            showUserProfile(it)
            hideProgress()
        })

        viewModel.errorResponse.observe(this, Observer {
            showErrorMessage(it)
            hideProgress()
        })
    }

    private fun showUserProfile(userList: List<Result>) {
        stack_view.visibility = View.VISIBLE
        hideProgress()
        mAdapter.submitList(userList)
    }

    private fun showErrorMessage(errorMessage: String?) {
        errorView.visibility = View.VISIBLE
        errorView.text = errorMessage
    }


    override fun onCardDisappeared(view: View?, position: Int) {
        mProfileItem = mAdapter.getItemFromAdapter(position)
        when (mDirection) {
            Direction.Right -> mProfileItem.extraInfo = getString(R.string.liked)
            Direction.Left -> mProfileItem.extraInfo = getString(R.string.unliked)
            else -> showToast(mDirection.name)
        }
        viewModel.updateUserProfile(mProfileItem)
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
        mDirection = direction!!
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
        if (mAdapter.itemCount - 1 == position) {
            viewModel.onRefresh()
        }
        mProfileItem = mAdapter.getItemFromAdapter(position)
        if (getString(R.string.liked) == mProfileItem.extraInfo) {
            tv_like_unlike.setBackgroundColor(Color.GREEN)
            tv_like_unlike.text = getString(R.string.liked)
            tv_like_unlike.visibility = View.VISIBLE
            rejectBtn.visibility = View.INVISIBLE
            acceptBtn.visibility = View.INVISIBLE
        } else if (getString(R.string.unliked) == mProfileItem.extraInfo) {
            tv_like_unlike.setBackgroundColor(Color.RED)
            tv_like_unlike.text = getString(R.string.unliked)
            tv_like_unlike.visibility = View.VISIBLE
            rejectBtn.visibility = View.INVISIBLE
            acceptBtn.visibility = View.INVISIBLE
        } else {
            tv_like_unlike.visibility = View.INVISIBLE
            rejectBtn.visibility = View.VISIBLE
            acceptBtn.visibility = View.VISIBLE
        }
    }

    override fun onCardRewound() {
        println(" onCardRewound ")
    }

    fun onAccept(view: View) {
        mDirection = Direction.Right
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Right).build()
        layoutManager.setSwipeAnimationSetting(setting)
        stack_view.swipe()
    }

    fun onrReject(view: View) {
        mDirection = Direction.Left
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(Direction.Left).build()
        layoutManager.setSwipeAnimationSetting(setting)
        stack_view.swipe()
    }

    private fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun showToast(name: String) {
        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
    }
}