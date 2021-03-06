package com.example.mvvmretrofit.model

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmretrofit.bean.ColorBean
import androidx.lifecycle.MutableLiveData
import com.example.mvvmretrofit.adapter.RvAdapter
import com.example.mvvmretrofit.databinding.ActivityMainBinding
import com.example.mvvmretrofit.db.ColorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class MainViewModel: ViewModel() {

    val listData: MutableLiveData<ArrayList<ColorBean>> = MutableLiveData<ArrayList<ColorBean>>()
    val ovf = ObservableField("")

    fun recycler(activity: Activity, binding: ActivityMainBinding) {

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)

    }

    fun data(context: Context, binding: ActivityMainBinding, colorRepository: ColorRepository) {

        val dataModel = DataModel()
        dataModel.getTitle(object : DataModel.Text {
            override fun getText(text: String?) {
                ovf.set(text)
            }
        })
        dataModel.getList(object : DataModel.Dynamic{
            override fun getList(arrayList: ArrayList<ColorBean>) {
                listData.value = arrayList
                adapter(context, binding)
                viewModelScope.launch(Dispatchers.IO) {
                    arrayList.forEach {
                        colorRepository.addColors(it)
                    }
                }
                Log.d("取得 DB List ", "${listData.value?.size}")
            }
        })
        colorRepository.closeDb()

    }

    private fun adapter(context: Context, binding: ActivityMainBinding) {

        val rvAdapter = RvAdapter(listData.value, context)
        binding.recyclerView.adapter = rvAdapter

    }

}