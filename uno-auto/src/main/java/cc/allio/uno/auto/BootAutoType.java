/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */
package cc.allio.uno.auto;

/**
 * 注解类型
 *
 * @author L.cm
 */
public enum BootAutoType {
    /**
     * 注解处理的类型
     */
    COMPONENT("org.springframework.stereotype.Component", "org.springframework.boot.autoconfigure.EnableAutoConfiguration");

    private final String annotationName;
    private final String configureKey;

    BootAutoType(String annotationName, String configureKey) {
        this.annotationName = annotationName;
        this.configureKey = configureKey;
    }

    public final String getAnnotationName() {
        return annotationName;
    }

    public final String getConfigureKey() {
        return configureKey;
    }

}
