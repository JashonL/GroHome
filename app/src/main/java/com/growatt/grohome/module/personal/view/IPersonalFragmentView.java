package com.growatt.grohome.module.personal.view;


import com.growatt.grohome.base.BaseView;
import com.growatt.grohome.bean.MessageBean;

import java.util.List;

public interface IPersonalFragmentView extends BaseView {

    void  onError(String msg);

    void setMessageCount(List<MessageBean>list);

}
