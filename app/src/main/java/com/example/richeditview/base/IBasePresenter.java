package com.example.richeditview.base;

public interface IBasePresenter<V extends IBaseView> {
    /**
     * 绑定View层,使用泛型的作用就体现在这里，因为这里的形参用的也是泛型类的泛型，所以可以初始化兼容多种类型
     * 注意，这个方法实际上是在V层通过P层对象来调用的，解绑操作也是
     */
    void attachView(V view);

    /**
     * 解除绑定
     */
    void detachView();
}
