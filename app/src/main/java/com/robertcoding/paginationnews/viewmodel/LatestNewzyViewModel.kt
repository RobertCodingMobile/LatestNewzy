package com.robertcoding.paginationnews.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robertcoding.common.CallerStatus
import com.robertcoding.domain.model.LatestNewsModel
import com.robertcoding.domain.repository.LatestNewsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class LatestNewzyViewModel(
    private val latestNewsRepository: LatestNewsRepository
) : ViewModel() {

    private val _latestNewzy: MutableStateFlow<CallerStatus<List<LatestNewsModel>>> =
        MutableStateFlow(CallerStatus.Loading)
    val latestNewzy: StateFlow<CallerStatus<List<LatestNewsModel>>> = _latestNewzy.asStateFlow()

    init {
        mergeLatestNews()
    }

    fun mergeLatestNews() {
        viewModelScope.launch {
            supervisorScope {
                val realEstateDeferred = async {
                    runCatching { latestNewsRepository.getLatestNews("realestate") }
                }
                val businessDeferred = async {
                    runCatching { latestNewsRepository.getLatestNews("business") }
                }
                val travelDeferred = async {
                    runCatching { latestNewsRepository.getLatestNews("travel") }
                }

                val realEstate = realEstateDeferred.await()
                val business = businessDeferred.await()
                val travel = travelDeferred.await()

                val merger = listOfNotNull(
                    realEstate.getOrNull(),
                    business.getOrNull(),
                    travel.getOrNull()
                ).filterIsInstance<CallerStatus.Success<List<LatestNewsModel>>>()
                    .flatMap { it.data }

                _latestNewzy.update { CallerStatus.Success(merger) }

            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        println("AICI, LatestNewzyViewModel onCleared")
    }

    // TODO: Create a basket of options for latest news.


}