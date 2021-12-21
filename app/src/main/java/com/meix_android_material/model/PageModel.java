package com.meix_android_material.model;

/**
 * author steven
 * date 2021/12/20
 * description
 */
public class PageModel {
    private int pageId;
    private String pageName;

    public int getPageId() {
        return pageId;
    }

    public void setPageId(int pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public PageModel(int pageId, String pageName) {
        this.pageId = pageId;
        this.pageName = pageName;
    }
}
