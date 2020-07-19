package com.growatt.grohome;


import com.growatt.grohome.base.BaseView;

public interface IMainActivityView extends BaseView {
    /**
     * 设置文章数据
     *
     * @param list 文章list
     */
    void setArticleData(String list);

    /**
     * 显示文章失败
     *
     * @param errorMessage 失败信息
     */
    void showArticleError(String errorMessage);

    /**
     * 显示收藏成功
     *
     * @param successMessage 成功信息
     */
    void showCollectSuccess(String successMessage);

    /**
     * 显示收藏失败
     *
     * @param errorMessage 失败信息
     */
    void showCollectError(String errorMessage);

    /**
     * 显示未收藏成功
     *
     * @param successMessage 成功信息
     */
    void showUncollectSuccess(String successMessage);

    /**
     * 显示未收藏失败
     *
     * @param errorMessage 失败信息
     */
    void showUncollectError(String errorMessage);



}
