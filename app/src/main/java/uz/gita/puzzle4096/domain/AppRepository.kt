package uz.gita.puzzle4096.domain

import android.content.Context
import uz.gita.puzzle4096.app.App
import uz.gita.puzzle4096.utils.show
import uz.gita.puzzle4096.utils.timber
import kotlin.random.Random

class AppRepository private constructor() {
    companion object {
        private lateinit var instance: AppRepository
        fun getRepository(): AppRepository {
            if (!::instance.isInitialized) {
                instance = AppRepository()
            }
            return instance
        }
    }

    private val pref = App.instance.getSharedPreferences("AppPref", Context.MODE_PRIVATE)

    var record: Long
        private set(value) = pref.edit().putLong("RECORD", value).apply()
        get() = pref.getLong("RECORD", 0)

    var lastScore: Long
        private set(value) = pref.edit().putLong("SCORE", value).apply()
        get() = pref.getLong("SCORE", 0)

    var isFirstRunning
        set(value) = pref.edit().putBoolean("firstRunning", value).apply()
        get() = pref.getBoolean("firstRunning", true)

    var lastVew
        private set(value) = pref.edit().putString("LAST_VIEW", value).apply()
        get() = pref.getString(
            "LAST_VIEW",
            "0#2#0#2#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0#0"
        )

    fun saveLastView() {
        var temp = StringBuilder("")
        for (i in 0 until 5) {
            for (j in 0 until 5) {
                temp.append("${array[i][j]}#")
            }
        }
        lastVew = temp.toString()
        lastScore = score
    }

    private var ADD_AMOUNT = 2
    private var score: Long = lastScore

    val array =
        arrayOf(
            arrayOf(0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0),
            arrayOf(0, 0, 0, 0, 0)
        )

    init {
        start()
    }

    private fun startBaseCode() {
        val list = ArrayList<Pair<Int, Int>>()
        for (i in array.indices) {
            for (j in array[i].indices) {
                if (array[i][j] == 0)
                    list.add(Pair(i, j))
            }
        }
        val index = Random.nextInt(0, list.size)
        var index2 = Random.nextInt(0, list.size)
        while (index2 == index) {
            index2 = Random.nextInt(0, list.size)
        }
        array[list[index].first][list[index].second] = ADD_AMOUNT
        array[list[index2].first][list[index2].second] = ADD_AMOUNT
    }

    private fun start() {
        if (isFirstRunning) {
            startBaseCode()
            isFirstRunning = false
        } else {
            var lastViewSplitList: List<String> = lastVew.toString().split("#")
            for (i in array.indices) {
                for (j in array[i].indices) {
                    array[i][j] = Integer.parseInt(lastViewSplitList.elementAt(i * array.size + j))
                }
            }
        }
    }

    private fun addNewAmount() {
        val list = ArrayList<Pair<Int, Int>>()
        for (i in array.indices) {
            for (j in array[i].indices) {
                if (array[i][j] == 0)
                    list.add(Pair(i, j))
            }
        }
        if (list.size != 0) {
            val index = Random.nextInt(0, list.size)
            array[list[index].first][list[index].second] = ADD_AMOUNT
        }
    }

    fun getMaxNumber(): Int {
        var maxNumber = 2
        var count = 0
        for (i in array.indices) {
            for (j in array[i].indices) {
                if (array[i][j] > maxNumber) {
                    maxNumber = array[i][j]
                }
                if (array[i][j] == ADD_AMOUNT) {
                    count++
                }
            }
        }
        return maxNumber
    }

    fun getScore(): Long {
        if (score > record) {
            record = score
        }
        return score
    }

    fun isWin(): Boolean {
        if (getMaxNumber() == 4096) {
            return true
        }
        return false
    }

    fun isNoWay(): Boolean {
        for (i in array.indices) {
            for (j in 0 until array[i].size - 1) {
                if (((array[i][j] == array[i][j + 1]) || array[i][j] == 0 || array[i][j + 1] == 0) ||
                    ((array[j][i] == array[j + 1][i]) || array[j][i] == 0 || array[j + 1][i] == 0)
                ) {
                    return false
                }
            }
        }
        return true
    }

    fun finish(): Boolean {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (j + 1 < 4 && array[i][j] == array[i][j + 1]) {
                    return true
                } else if (j - 1 >= 0 && array[i][j] == array[i][j - 1]) {
                    return true
                } else if (i + 1 < 4 && array[i][j] == array[i + 1][j]) {
                    return true
                } else if (i - 1 >= 0 && array[i][j] == array[i - 1][j]) {
                    return true
                }
            }
        }
        return false
    }

    fun reload() {
        for (i in array.indices) {
            for (j in array[i].indices) {
                array[i][j] = 0
            }
        }
        score = 0
        startBaseCode()
    }

    fun swipeLeft() {
        for (i in array.indices) {
            val list = ArrayList<Int>()
            for (j in array[i].indices) {
                if (array[i][j] != 0)
                    list.add(array[i][j])
            }
            timber(list.show())
            var index = 0
            while (index < list.size - 1) {
                if (list[index] == list[index + 1]) {
                    list[index] *= 2
                    score += list[index]
                    list.removeAt(index + 1)
                }
                index++
            }
            timber(list.show())
            for (j in array.indices) {
                if (j < list.size) array[i][j] = list[j]
                else array[i][j] = 0
            }
        }
        addNewAmount()
    }

    fun swipeRight() {
        for (i in array.indices) {
            val list = ArrayList<Int>()
            for (j in array[i].indices) {
                if (array[i][j] != 0)
                    list.add(array[i][j])
            }
            var index = list.size - 1
            while (index > 0) {
                if (list[index] == list[index - 1]) {
                    list[index] *= 2
                    score += list[index]
                    list.removeAt(index - 1)
                    index--
                }
                index--
            }
            for (j in array.indices) {
                if (j < array.size - list.size) array[i][j] = 0
                else array[i][j] = list[j - (array.size - list.size)]
            }
        }
        addNewAmount()
    }

    fun swipeUp() {
        for (i in array.indices) {
            val list = ArrayList<Int>()
            for (j in array[i].indices) {
                if (array[j][i] != 0)
                    list.add(array[j][i])
            }
            timber(list.show())
            var index = 0
            while (index < list.size - 1) {
                if (list[index] == list[index + 1]) {
                    list[index] *= 2
                    score += list[index]
                    list.removeAt(index + 1)
                }
                index++
            }
            timber(list.show())
            for (j in array.indices) {
                if (j < list.size) array[j][i] = list[j]
                else array[j][i] = 0
            }
        }
        addNewAmount()
    }

    fun swipeDown() {
        for (i in array.indices) {
            val list = ArrayList<Int>()
            for (j in array[i].indices) {
                if (array[j][i] != 0)
                    list.add(array[j][i])
            }
            var index = list.size - 1
            while (index > 0) {
                if (list[index] == list[index - 1]) {
                    list[index] *= 2
                    score += list[index]
                    list.removeAt(index - 1)
                    index--
                }
                index--
            }
            for (j in array.indices) {
                if (j < array.size - list.size) array[j][i] = 0
                else array[j][i] = list[j - (array.size - list.size)]
            }
        }
        addNewAmount()
    }
}
