package com.jiangxk.basics_algorithm

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.jiangxk.basics_algorithm.bean.SortBean
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashSet

class MainActivity : AppCompatActivity() {

    companion object {
        const val SLEEP_TIME = 10L
        const val LIST_SIZE = 100
    }

    private val saveList = mutableListOf<SortBean>()

    private var isSortting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {

        val valueList = arrayListOf<SortBean>()

        val tempList = arrayListOf<SortBean>()
        val size = LIST_SIZE
        for (i in 1..size) {
            val bubblingBean = SortBean((1 + i * 800 / size).toFloat(), false, false)
            tempList.add(bubblingBean)
        }
        val random = Random()
        val indexSet = LinkedHashSet<Int>()
        while (indexSet.size < size) {
            val index = random.nextInt(size)
            indexSet.add(index)
        }
        indexSet.forEach {
            val bubblingBean = tempList[it]
            valueList.add(bubblingBean)
            saveList.add(bubblingBean)
        }

        bubblingView.valueList.clear()
        bubblingView.valueList.addAll(valueList)

        btn_bubbling.setOnClickListener {
            if (isSortting) {
                return@setOnClickListener
            }
            bubblingView.valueList.clear()
            saveList.forEach {
                val sortBean = SortBean(it.value, false, false)
                bubblingView.valueList.add(sortBean)
            }
            bubblingView.postDelayed({
                if (!isSortting) {
                    bubblingSort()
                }
            }, 2000)
        }
        btn_merger.setOnClickListener {
            if (isSortting) {
                return@setOnClickListener
            }
            bubblingView.valueList.clear()
            saveList.forEach {
                val sortBean = SortBean(it.value, false, false)
                bubblingView.valueList.add(sortBean)
            }
            bubblingView.postDelayed({
                if (!isSortting) {
                    mergerSort()
                }
            }, 2000)

        }

        btn_quick.setOnClickListener {
            if (isSortting) {
                return@setOnClickListener
            }
            bubblingView.valueList.clear()
            saveList.forEach {
                val sortBean = SortBean(it.value, false, false)
                bubblingView.valueList.add(sortBean)
            }
            bubblingView.postDelayed({
                if (!isSortting) {
                    quickSort()
                }
            }, 2000)
        }
    }

    /**
     * 冒泡排序
     */
    private fun bubblingSort() {
        isSortting = true
        val size = bubblingView.valueList.size
        for (i in 0 until size - 1) {
            for (j in 0 until size - i - 1) {
                if (bubblingView.valueList[j].value > bubblingView.valueList[j + 1].value) {
                    val temp = bubblingView.valueList[j].value
                    bubblingView.valueList[j].value = bubblingView.valueList[j + 1].value
                    bubblingView.valueList[j + 1].value = temp
                }
                bubblingView.valueList[j].isComparing = true
                Thread.sleep(SLEEP_TIME)
                bubblingView.valueList[j].isComparing = false
            }
            bubblingView.valueList[size - i - 1].isCompleted = true
        }
        bubblingView.valueList[0].isCompleted = true
        Toast.makeText(this, "冒泡排序结束", Toast.LENGTH_LONG).show()
        isSortting = false
    }

    /**
     * 归并排序
     */
    private fun mergerSort() {
        isSortting = true
        sort(bubblingView.valueList, 0, bubblingView.valueList.size - 1)
        bubblingView.valueList.forEach {
            it.isComparing = false
            it.isCompleted = true
        }
        Toast.makeText(this, "归并排序结束", Toast.LENGTH_LONG).show()
        isSortting = false
    }

    private fun sort(valueList: MutableList<SortBean>, left: Int, right: Int) {
        if (left >= right) {
            return
        }
        val mid = (left + right) / 2
        sort(valueList, left, mid)
        sort(valueList, mid + 1, right)
        merge(valueList, left, mid, right)
    }

    private fun merge(valueList: MutableList<SortBean>, left: Int, mid: Int, right: Int) {
        val tmpArray = arrayOfNulls<SortBean>(valueList.size)
        var low = left
        var center = mid + 1
        var tmpLeft = left
        while (tmpLeft <= mid && center <= right) {
            if (valueList[tmpLeft].value > valueList[center].value) {
                tmpArray[low++] = valueList[center++]
            } else {
                tmpArray[low++] = valueList[tmpLeft++]
            }
//            bubblingView.valueList[tmpLeft].isComparing = true
            Thread.sleep(SLEEP_TIME)
//            bubblingView.valueList[tmpLeft].isComparing = false
        }
        while (tmpLeft <= mid) {
            tmpArray[low++] = valueList[tmpLeft++]
        }
        while (center <= right) {
            tmpArray[low++] = valueList[center++]
        }
        for (i in left until right + 1) {
            valueList[i] = tmpArray[i]!!
            bubblingView.valueList[i].isComparing = true
            Thread.sleep(SLEEP_TIME)
        }
    }

    private fun quickSort() {
        isSortting = true
        quick_Sort(bubblingView.valueList, 0, bubblingView.valueList.size - 1)
        bubblingView.valueList.forEach {
            it.isComparing = false
            it.isCompleted = true
        }
        Toast.makeText(this, "快速排序结束", Toast.LENGTH_LONG).show()
        isSortting = false
    }

    private fun quick_Sort(valueList: MutableList<SortBean>, start: Int, end: Int) {
        if (start > end) {
            return
        }
        var i = start
        var j = end
        val key = valueList[start].value

        while (i != j) {
            while (j > i && valueList[j].value >= key) {
                bubblingView.valueList[j].isComparing = true
                Thread.sleep(SLEEP_TIME)
                bubblingView.valueList[j].isComparing = false
                j--
            }
            while (i < j && valueList[i].value <= key) {
                bubblingView.valueList[i].isComparing = true
                Thread.sleep(SLEEP_TIME)
                bubblingView.valueList[i].isComparing = false
                i++
            }
            if (i < j) {
                val temp = valueList[i].value
                valueList[i].value = valueList[j].value
                valueList[j].value = temp

                bubblingView.valueList[i].isCompleted = true
                Thread.sleep(SLEEP_TIME)
            }
        }
        valueList[start].value = valueList[i].value
        valueList[i].value = key

        quick_Sort(valueList, start, i - 1)
        quick_Sort(valueList, i + 1, end)
    }

}
