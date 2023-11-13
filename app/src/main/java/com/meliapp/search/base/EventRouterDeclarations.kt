package com.meliapp.search.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal interface EventRouter<
        ViewEvent : EventRouter.ViewEvent,
        ViewModelEvent : EventRouter.ViewModelEvent,
        > {

    interface ViewEvent

    fun publishViewEvent(event: ViewEvent)

    interface ViewModelEvent

    val viewModelEvents: Flow<ViewModelEvent>
}

internal interface StatefulEventRouter<
        ViewEvent : EventRouter.ViewEvent,
        ViewModelEvent : EventRouter.ViewModelEvent,
        State : StatefulEventRouter.State,
        > : EventRouter<ViewEvent, ViewModelEvent> {

    interface State

    val viewModelState: StateFlow<State>
}
