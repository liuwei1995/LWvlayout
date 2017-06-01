package com.liuwei1995.lwvlayout.entity;

import java.util.List;

/**
 * Created by linxins on 17-6-1.
 */

public class FindEntity {
    private int viewType;
    private String name;

    private List<String> tagList;
    private List<String> typeList;
    private List<String> menuList;

    public int getViewType() {

        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public List<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
    }

    public List<String> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<String> menuList) {
        this.menuList = menuList;
    }
}
