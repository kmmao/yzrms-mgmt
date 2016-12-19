/*
 * SystemConfig.java
 * 
 * Copyright(c) 2007-2016 by Yingzhi Tech
 * All Rights Reserved
 * 
 * Created at 2016-08-05 10:56:15
 */
package com.yz.rms.common.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * 系统配置基础类
 *
 * @author 赵洪坤 <zhaohongkun@yzhtech.com>
 */
public class SystemConfig implements Serializable {

    private static final long serialVersionUID = -8418460495471076827L;
    /**
     * 属性ID
     */
    private String configId;
    /**
     * 属性值
     */
    private Double configValue;

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public Double getConfigValue() {
        return configValue;
    }

    public void setConfigValue(Double configValue) {
        this.configValue = configValue;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.configId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SystemConfig other = (SystemConfig) obj;
        if (!Objects.equals(this.configId, other.configId)) {
            return false;
        }
        return true;
    }

}
