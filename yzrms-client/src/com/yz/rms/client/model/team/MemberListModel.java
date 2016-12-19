/*
 * MemberListModel.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-06-28 15:47:21
 */
package com.yz.rms.client.model.team;

import com.nazca.ui.TriStateCellWrapper;
import com.nazca.ui.TristateCheckBox;
import com.nazca.ui.model.SimpleObjectListModel;
import com.yz.rms.common.model.wrap.MemberWrap;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 * 选择团队成员的modelList，里面的对象为MemberWrap
 */
public class MemberListModel extends SimpleObjectListModel<TriStateCellWrapper<MemberWrap>> {

    public void setNominalList(List<MemberWrap> dList) {
        List<TriStateCellWrapper<MemberWrap>> checkList = new ArrayList<>();
        for (MemberWrap d : dList) {
            checkList.add(new TriStateCellWrapper<>(d));
        }
        super.setObjectList(checkList);
    }

    public List<MemberWrap> getAllSelectedNominals() {
        List<MemberWrap> dList = new ArrayList<>();
        for (TriStateCellWrapper<MemberWrap> tcw : getObjectList()) {
            if (tcw.getState() == TristateCheckBox.State.CHECKED) {
                dList.add(tcw.getObj());
            }
        }
        return dList;
    }

    public void setReversSelected() {
        for (TriStateCellWrapper<MemberWrap> wrp : list) {
            if (wrp.getState() == TristateCheckBox.State.CHECKED) {
                wrp.setState(TristateCheckBox.State.UNCHECKED);
            } else {
                wrp.setState(TristateCheckBox.State.CHECKED);
            }
        }
        this.fireContentsChanged(this, 0, list.size() - 1);
    }

    public void setAllSelected() {
        for (TriStateCellWrapper<MemberWrap> wrp : list) {
            wrp.setState(TristateCheckBox.State.CHECKED);
        }
        this.fireContentsChanged(this, 0, list.size() - 1);
    }

    public void setAllUnselected() {
        for (TriStateCellWrapper<MemberWrap> wrp : list) {
            wrp.setState(TristateCheckBox.State.UNCHECKED);
        }
        this.fireContentsChanged(this, 0, list.size() - 1);
    }

    public void setSelected(List<MemberWrap> dList) {
        List<TriStateCellWrapper<MemberWrap>> checkList = new ArrayList<TriStateCellWrapper<MemberWrap>>();
        for (MemberWrap d : dList) {
            checkList.add(new TriStateCellWrapper<>(d));
        }
        for (TriStateCellWrapper<MemberWrap> wrp : list) {
            for (int i = 0; i < checkList.size(); i++) {
                TriStateCellWrapper<MemberWrap> obj = checkList.get(i);
                if (wrp.toString().equals(obj.toString())) {
                    wrp.setState(TristateCheckBox.State.CHECKED);
                }
            }
        }
        this.fireContentsChanged(this, 0, list.size() - 1);
    }
}
