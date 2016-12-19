/*
 * ExpenseItemEnum.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-09 12:47:04
 */
package com.yz.rms.common.enums;

/**
 *  报销项目Enum
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public enum ExpenseItemEnum {
    cityTraffic(){
        @Override
        public String toString() {
            return "市内交通费"; //To change body of generated methods, choose Tools | Templates.
        }
    
    },
    booksMaterials(){
        @Override
        public String toString() {
            return "图书资料费"; //To change body of generated methods, choose Tools | Templates.
        }
    
    },
    cityGasoline(){
        @Override
        public String toString() {
            return "室内汽油费"; //To change body of generated methods, choose Tools | Templates.
        }
    
    },
    copyBind(){
        @Override
        public String toString() {
            return "复印装订费"; //To change body of generated methods, choose Tools | Templates.
        }
    
    },
    entertain(){
        @Override
        public String toString() {
            return "室内招待费"; //To change body of generated methods, choose Tools | Templates.
        }
    
    },
    material(){
        @Override
        public String toString() {
            return "材料费"; //To change body of generated methods, choose Tools | Templates.
        }
    
    },
    train(){
        @Override
        public String toString() {
            return "培训费"; //To change body of generated methods, choose Tools | Templates.
        }
    
    },
    officeSupplies(){
        @Override
        public String toString() {
            return "办公用品费"; //To change body of generated methods, choose Tools | Templates.
        }
    
    },
    telephoneBill(){
        @Override
        public String toString() {
            return "电话费"; //To change body of generated methods, choose Tools | Templates.
        }
    
    },
    postage(){
        @Override
        public String toString() {
            return "邮费"; //To change body of generated methods, choose Tools | Templates.
        }
    
    },
    travelAccommodation(){
        @Override
        public String toString() {
            return "出差住宿费"; //To change body of generated methods, choose Tools | Templates.
        }
    
    },
    travelTraffic(){
        @Override
        public String toString() {
            return "出差交通费"; //To change body of generated methods, choose Tools | Templates.
        }
    
    },
    travelMeals(){
        @Override
        public String toString() {
            return "出差餐费"; //To change body of generated methods, choose Tools | Templates.
        }
    
    },
    travelAllowance(){
        @Override
        public String toString() {
            return "差旅补助"; //To change body of generated methods, choose Tools | Templates.
        }
    
    },
    spacePage(){
        @Override
        public String toString() {
            return "版面费"; //To change body of generated methods, choose Tools | Templates.
        }
    
    },
    conferences(){
        @Override
        public String toString() {
            return "会议费"; //To change body of generated methods, choose Tools | Templates.
        }
    
    },
    fieldOperation(){
        @Override
        public String toString() {
            return "外勤费"; //To change body of generated methods, choose Tools | Templates.
        }
    },
    amount(){
        @Override
        public String toString() {
            return "合计"; //To change body of generated methods, choose Tools | Templates.
        }
    }
}
