# Chapter12
今天我们的示例是如何替换系统SharedPreferences的实现，实现这个主要为了替换一些第三方库的实现。

如果是自己应用内部的代码，还是建议直接使用类似[MMKV](https://github.com/Tencent/MMKV)等其他方案。

实现提示
====

替换Application的getSharedPreferences方法：
```
public class MyApplication extends Application {
  @Override
  public SharedPreferences getSharedPreferences(String name, int mode)
  {
     return SharedPreferencesImpl.getSharedPreferences(name, mode);
  }
}
```

记得还需要替换Activity的getSharedPreferences方法：
```
@Override
public SharedPreferences getSharedPreferences(String name, int mode) {
    return this.getApplicationContext().getSharedPreferences(name, mode);
}
```

核心优化
====
我们可以将项目中的SharedPreferencesImpl与系统的[SharedPreferencesImpl](http://androidxref.com/6.0.1_r10/xref/frameworks/base/core/java/android/app/SharedPreferencesImpl.java)做比较。

可以发现大部分代码都是一致的，核心的优化在于修改了apply的实现，将多个apply方法在内存中合并，而不是多次提交。

事实上这个只是一个简单的示例，通过自定义SharedPreferences实现，我们可以替换它的存储结构、增加加密等其他自定义功能。