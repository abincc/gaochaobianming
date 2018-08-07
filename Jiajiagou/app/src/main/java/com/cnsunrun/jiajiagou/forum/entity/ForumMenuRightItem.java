package com.cnsunrun.jiajiagou.forum.entity;

import java.util.List;

/**
 * 在此写用途
 * <p>
 * author:yyc
 * date: 2017-08-25 19:46
 */
public class ForumMenuRightItem {
    private int parentId;
    private List<ForumMenuRightItem.RightItem> mRightItems;

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public List<RightItem> getRightItems() {
        return mRightItems;
    }

    public void setRightItems(List<RightItem> rightItems) {
        mRightItems = rightItems;
    }

    public static class RightItem {
        private int id;
        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
