package uz.gita.puzzle4096.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.gita.puzzle4096.domain.AppRepository

class MainViewModel : ViewModel() {
    private val repository = AppRepository.getRepository()

    private val _arrayLiveData = MutableLiveData<Array<Array<Int>>>()
    val arrayLiveData: LiveData<Array<Array<Int>>> get() = _arrayLiveData

    init {
        _arrayLiveData.value = repository.array
    }

    fun reload() {
        repository.reload()
        _arrayLiveData.value = repository.array
    }

    fun swipeLeft() {
        repository.swipeLeft()
        _arrayLiveData.value = repository.array
    }

    fun swipeRight() {
        repository.swipeRight()
        _arrayLiveData.value = repository.array
    }

    fun swipeUp() {
        repository.swipeUp()
        _arrayLiveData.value = repository.array
    }

    fun swipeDown() {
        repository.swipeDown()
        _arrayLiveData.value = repository.array
    }

    fun isNoWay() : Boolean{
      return  repository.isNoWay()
    }

    fun getMaxNumber() : Int{
        return repository.getMaxNumber()
    }

    fun getScore() : Long{
        return repository.getScore()
    }

    fun getLastScore() : Long{
        return repository.lastScore
    }

    fun getRecord() : Long{
        return repository.record
    }

    fun saveLastView(){
        repository.saveLastView()
    }

    fun isWin() : Boolean{
      return  repository.isWin()
    }
}