package edu.festu.numberfacts.model

import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

/*
* ViewModel для списка фактов.
* Содержит методы для изменения списка фактов.
* В случае ошибок результат будет сохранен в errorMessage.
* В случае изменения данных все подписанные на ViewModel слушатели будут уведомлены об этом.
* В рамках приложения достаточно одной ViewModel, поэтому для работ с ней используется одиночка.
* */
class FactViewModel:ViewModel() {
    val factList = MutableLiveData<MutableList<Fact>>()
    val errorMessage = MutableLiveData<String>()
        /*Метод для получения списка фактов.
         Если пользователь аутентифицирован, то будет получена его коллекция фактов, о чем будут
         уведомлены слушатели ViewModel (метод postValue)
         */
    fun getCollection() {
        viewModelScope.launch {
            Firebase.auth.currentUser?.email?.let { it1 ->
                Firebase.firestore.collection(
                    it1
                ).get().addOnSuccessListener {
                    factList.postValue(it.toObjects(Fact::class.java))
                }.addOnFailureListener{
                    it.printStackTrace()
                    errorMessage.postValue(it.message)
                }
            }
        }
    }
    /*
    Метод для добавления фактов во viewModel с уведомлением слушателей
    */
    fun addFact(fact: Fact) {
        Firebase.auth.currentUser?.email?.let { it1 ->
            Firebase.firestore.collection(
                it1
            ).document(fact.hashCode().toString()).set(fact).addOnSuccessListener {
                this.factList.value?.add(fact)
                this.factList.postValue(this.factList.value)
            }.addOnFailureListener {
                it.printStackTrace()
                errorMessage.postValue(it.message)
            }
        }


    }
    /*
    Метод для удаления фактов во viewModel с уведомлением слушателей
    */
    fun remove(position: Int) {
        Firebase.auth.currentUser?.email?.let { it1 ->
            Firebase.firestore.collection(
                it1
            ).document(factList.value?.get(position).hashCode().toString()).delete().addOnSuccessListener {
                factList.value?.removeAt(position)
                factList.postValue(factList.value)
            }.addOnFailureListener{
                it.printStackTrace()
                errorMessage.postValue(it.message)
            }
        }
    }
    /*
      одиночка
    */
    companion object {
        private lateinit var sInstance: FactViewModel
        @MainThread
        fun get(): FactViewModel {
            sInstance = if (::sInstance.isInitialized) sInstance else FactViewModel()
            return sInstance
        }
    }

}