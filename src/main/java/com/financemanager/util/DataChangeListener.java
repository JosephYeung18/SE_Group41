package com.financemanager.util;

/**
 * 数据变更监听器接口
 */
public interface DataChangeListener {
    /**
     * 当数据变更时调用此方法
     */
    void onDataChanged();
}
