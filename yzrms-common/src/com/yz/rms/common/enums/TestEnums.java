/*
 * TestEnums.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-02 16:55:00
 */
package com.yz.rms.common.enums;

/**
 *
 * @author Qiu Dongyue <qdy@yzhtech.com>
 */
public enum TestEnums {
    PERSONAL(){
        @Override
        public String toString() {
            return "事假";
        }
    },
    SICK(){
        @Override
        public String toString() {
            return "病假";
        }
    },
    PAID_LEGAL(){
        @Override
        public String toString() {
            return "法定年假";
        }
    },
    PAID_INNER(){
        @Override
        public String toString() {
            return "内部年假";
        }
    },
    WEDDING(){
        @Override
        public String toString() {
            return "婚假";
        }
    },
    BIRTH(){
        @Override
        public String toString() {
            return "产假";
        }
    },
    NURSING(){
        @Override
        public String toString() {
            return "护理假";
        }
    },
    FUNERAL(){
        @Override
        public String toString() {
            return "吊唁假";
        }
    }
}
