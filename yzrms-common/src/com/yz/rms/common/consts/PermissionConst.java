/*
 * PermissionConst.java
 * 
 * Copyright(c) 2007-2015 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2015-10-23 10:13:45
 */
package com.yz.rms.common.consts;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Wu Jinghua <wjh@yzhtech.com>
 */
public enum PermissionConst {
    PRO_MGMT(){
      @Override
      public String toString(){
          return "项目管理";
      }
    },
    TEAM_MGMT(){
        @Override
        public String toString(){
            return "团队管理";
        }
    },
    EXPENSE_FORM_MGMT(){
      @Override
      public String toString(){
          return "报销单管理";
      }
    },
    STAT_MGMT(){
      @Override
      public String toString(){
          return "统计管理";
      }
    };//,
//    ADD_REIMBURSEMENT(){
//        @Override
//                public String toString() {
//                    return "填写报销单";
//                }
//    },
//    SUBMIT_REIMBURSEMENT(){
//        @Override
//                public String toString() {
//                    return "提交报销单";
//                }
//    },
//    FIRST_AUDIT_REIMBURSEMENT(){
//        @Override
//                public String toString() {
//                    return "报销单一审";
//                }
//    },
//    SECOND_AUDIT_REIMBURSEMENT(){
//        @Override
//                public String toString() {
//                    return "报销单二审";
//                }
//    },
//    CONFIRM_REIMBURSEMENT(){
//        @Override
//                public String toString() {
//                    return "确认报销单";
//                }
//    },
//    MARK_SEND(){
//        @Override
//                public String toString() {
//                    return "标记发送";
//                }
//    },
//    MARK_REMITTANCE(){
//        @Override
//                public String toString() {
//                    return "标记打款";
//                }
//    };
    @Override
    public abstract String toString();
    private static final PermissionConst[] ALL_PERMISSION = new PermissionConst[]{
        TEAM_MGMT,PRO_MGMT,EXPENSE_FORM_MGMT,STAT_MGMT
    };
    

    public static List<PermissionConst> getAllPermissions() {
        List<PermissionConst> pList = Arrays.asList(ALL_PERMISSION);
        return pList;
    }
}
