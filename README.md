# load-manager

load-manager 可以优雅地处理页面的加载中、加载错误、无数据、超时、重试等场景需求。使用 Kotlin 实现

参考了 loadSir（https://github.com/KingJA/LoadSir） 的思想。

## 使用步骤

### 全局 init ，建议在 Application#onCreate 里面完成 

```kotlin

        val inputCallBacks = hashSetOf(
            Error(R.layout.hc_layout_error_default),
            Loading(R.layout.hc_layout_loading_default),
            Empty(R.layout.hc_layout_empty_default),
            Timeout(R.layout.hc_layout_timeout_default),
            CustomLoadState.Search.Empty()
        )

        LoadManager.install(inputCallBacks)
            .apply {
                setDefaultCallback(Success::class.java)
                setAnimateTime(300)
            }
```

### 具体的页面使用

#### 使用 Target View，调用库中提供的全局方法 View.observe{//handle event}，获取 LoadService 实例 
```kotlin
    private lateinit var loadService: LoadService
```

```kotlin
        loadService = rvList.observe {
            LogUtils.i("onReload()")
        }
```
#### 通过 LoadService 实例，进行消息通知

```kotlin
loadService.showSuccess()

```

or 


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
