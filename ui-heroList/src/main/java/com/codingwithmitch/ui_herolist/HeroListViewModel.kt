package com.codingwithmitch.ui_herolist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingwithmitch.core.domain.DataState
import com.codingwithmitch.core.domain.UIComponent
import com.codingwithmitch.dotainfo.hero_interactors.GetHeros
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HeroListViewModel
@Inject
constructor(
    private val getHeros: GetHeros,
): ViewModel(){

    private val TAG: String = "AppDebug"

    val state: MutableState<HeroListState> = mutableStateOf(HeroListState())

    init {
        onTriggerEvent(HeroListEvents.GetHeros)
    }

    fun onTriggerEvent(event: HeroListEvents){
        when(event){
            is HeroListEvents.GetHeros -> {
                getHeros()
            }
            is HeroListEvents.Error -> {
//                appendToMessageQueue(dataState.uiComponent)
            }
        }
    }

    private fun getHeros(){
        getHeros.execute().onEach { dataState ->
            when(dataState){
                is DataState.Loading -> {
                    state.value = state.value.copy(progressBarState = dataState.progressBarState)
                }
                is DataState.Data -> {
                    state.value = state.value.copy(heros = dataState.data?: listOf())
                }
                is DataState.Response -> {
                    when(dataState.uiComponent){
                        is UIComponent.Dialog -> {
                        }
                        is UIComponent.SnackBar -> {
                        }
                        is UIComponent.Toast -> {
                        }
                        is UIComponent.None -> {
                        }
                    }
//                    appendToMessageQueue(dataState.uiComponent)
                }
            }
        }.launchIn(viewModelScope)
    }
}











