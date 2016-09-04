package jp.keita.kagurazaka.rxproperty.sample.todo

import jp.keita.kagurazaka.rxproperty.RxProperty
import jp.keita.kagurazaka.rxproperty.sample.ViewModelBase
import rx.Observable

class TodoItemViewModel constructor(
        val model: TodoItem = TodoItem(false, "")
) : ViewModelBase() {
    val isDone: RxProperty<Boolean>
            = RxProperty(model.isDone, DISABLE_RAISE_ON_SUBSCRIBE)
            .asManaged()

    val title: RxProperty<String?>
            = RxProperty(model.title)
            .setValidator { if (it.isNullOrBlank()) "Blank isn't allowed." else null }
            .asManaged()

    val onHasErrorChanged: Observable<Boolean>
        get() = title.onHasErrorsChanged()

    init {
        isDone.asObservable()
                .subscribe {
                    model.isDone = it
                    TodoRepository.update(model)
                }.asManaged()

        title.asObservable()
                .subscribe {
                    model.title = it ?: ""
                }.asManaged()
    }
}