/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baozun.nebula.utils.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utils.lang.CharsetType;
import com.feilong.core.lang.StringUtil;

/**
 * The Class HexUtil.
 *
 * @author feilong
 * @version 1.4.0 2015年8月24日 上午10:49:21
 * @see org.apache.commons.codec.binary.Hex
 * @since 1.4.0
 */
public class HexUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HexUtil.class);

    // [start]toHexStringUpperCase/toOriginal

    /**
     * 将原始字符串 转成 大写的HexString 网友gdpglc的思路.
     * 
     * @param original
     *            原始字符串
     * @param charsetName
     *            字符集,you can use {@link CharsetType}
     * @return the string
     * @see ByteUtil#bytesToHexStringUpperCase(byte[])
     * @deprecated will move
     */
    @Deprecated
    public static String toHexStringUpperCase(String original,String charsetName){
        String hexStringUpperCase = ByteUtil.bytesToHexStringUpperCase(StringUtil.getBytes(original, charsetName));
        LOGGER.debug("original:[{}],hexStringUpperCase:[{}]", original, hexStringUpperCase);
        return hexStringUpperCase;
    }

    /**
     * 将 转成 大写的HexString原始字符串.
     * 
     * @param hexStringUpperCase
     *            the hex string upper case
     * @param charsetName
     *            指定字符集,you can use {@link CharsetType}
     * @return the string
     * @see ByteUtil#hexBytesToBytes(byte[])
     * @deprecated will move
     */
    @Deprecated
    public static String toOriginal(String hexStringUpperCase,String charsetName){
        byte[] hexBytesToBytes = ByteUtil.hexBytesToBytes(hexStringUpperCase.getBytes());
        String original = StringUtil.newString(hexBytesToBytes, charsetName);
        LOGGER.debug("hexStringUpperCase:[{}],original:[{}]", hexStringUpperCase, original);
        return original;
    }
    // [end]
}
