# load-manager

## load-manager 
Can gracefully handle page loading, loading errors, no data, timeout, retry and other scenario requirements. Use the Kotlin implementation

> reference the loadSir lib（https://github.com/KingJA/LoadSir).

## How to use ?

### 1、Global initialization ，recommend in Application#onCreate 

```kotlin
val inputCallBacks = hashSetOf(
    Error(R.layout.hc_layout_error_default),
    Loading(R.layout.hc_layout_loading_default),
    Empty(R.layout.hc_layout_empty_default),
    Timeout(R.layout.hc_layout_timeout_default)
)

LoadManager.install(inputCallBacks)
    .apply {
        setDefaultCallback(Success::class.java)
        setAnimateTime(300)
    }
```

### 2、In the page

#### 2.1 Call the global extension method using the target view to get a `LoadService` instance. 

call the method:
```kotlin
 `View.observe{//handle event}` 
```

```kotlin
private lateinit var loadService: LoadService
```

```kotlin
loadService = rvList.observe {
    LogUtils.i("onReload()")
}
```
#### 2.2 through `LoadService` instance to post notifications to update UI.

just like this:

```kotlin
loadService.showSuccess()
```

or call the next methods:


```kotlin
fun notify(event: Any) {
    when (event) {
        is INetTimeout -> notify(event)
        is Throwable -> notify(event)
        is INetError -> notify(event)
        is LoadCallback -> notify(event)
        else -> throw IllegalArgumentException("非法参数：$event")
    }
}

fun notify(event: LoadCallback) {
    showCallback(event::class.java)
}

fun notify(error: INetTimeout) {
    showCallback(Timeout::class.java)
}

fun notify(error: Throwable) {
    showCallback(Error::class.java)
}

fun notify(error: INetError) {
    showCallback(Error::class.java)
}
```

And finally, you can post different message(Sub class of LoadCallback) to change UI.


## Custom state

### 1. implements a sub class for LoadCallback ,and provide the UI to display when receive this custom type message

```kotlin
  class SearchEmpty : LoadCallback() {
            override fun onCreateView(): Int {
                return R.layout.layout_search_empty
            }
        }
```
### 2. add the implementation class to the list when init


```kotlin
val inputCallBacks = hashSetOf(
    Error(R.layout.hc_layout_error_default),
    Loading(R.layout.hc_layout_loading_default),
    Empty(R.layout.hc_layout_empty_default),
    Timeout(R.layout.hc_layout_timeout_default),
    `SearchEmpty()`
)

LoadManager.install(inputCallBacks)
    .apply {
        setDefaultCallback(Success::class.java)
        setAnimateTime(300)
    }
```

### 3. post this type event message to change to custom UI.

```kotlin
loadService.notify(SearchEmpty())
```

