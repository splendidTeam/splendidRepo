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
package com.baozun.nebula.sdk.utils;

import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.utilities.common.Validator;

/**
 * 某些场景下面,我们需要在后端代码里面拼接转换图片尺码, 比如发的邮件里面带有商品图片等等.
 * 
 * <p>
 * 因此,提取 {@link com.baozun.nebula.web.taglib.ImgUrlTag ImgUrlTag里面核心方法}
 * </p>
 * 
 * @author feilong
 * @author yimin.qiao
 * @version 1.2.2 2015年8月27日 下午3:51:08
 * @since 1.2.2
 */
public final class ImageUrlUtil{

    /** 原图尺寸. */
    private static final String SOURCE = "source";

    /**
     * 某些场景下面,我们需要在后端代码里面拼接转换图片尺码, 比如发的邮件里面带有商品图片等等.
     * 
     * <p>
     * 因此,提取 {@link com.baozun.nebula.web.taglib.ImgUrlTag ImgUrlTag里面核心方法}
     * </p>
     * 
     * <p>
     * 该方法返回完整的以imgBase开头的url，如果传入的imgUrl为空，则返回传入的默认url参数 (defaultImgUrl)
     * </p>
     *
     * @param imgBase
     *            ImgConstants.IMG_BASE
     * @param imgUrl
     *            the img url
     * @param size
     *            the size
     * @param defaultImgUrl
     *            ImgConstants.DEFAULT_IMG_URL
     * @return the string
     * @see com.baozun.nebula.web.taglib.ImgUrlTag#doStartTag()
     */
    public static String formatImage(String imgBase,String imgUrl,String size,String defaultImgUrl){
        StringBuffer sb = new StringBuffer();

        //为null使用默认图片
        if (StringUtils.isBlank(imgUrl)){
            if (Validator.isNotNullOrEmpty(defaultImgUrl)){
                sb.append(defaultImgUrl);
            }
        }else{
            if (Validator.isNotNullOrEmpty(imgBase)){
                sb.append(imgBase);
            }
            int index = imgUrl.lastIndexOf(".");
            int index2 = imgUrl.lastIndexOf("_");

            //如果找到了下划线 "_",截取下划线及之前的部分
            if (index2 != -1){
                sb.append(imgUrl.substring(0, index2));
            }
            //如果找不到下划线 "_",截取.之前的部分
            else{
                sb.append(imgUrl.substring(0, index));
            }

            if (!SOURCE.equals(size)){
                sb.append("_");
                sb.append(size);
            }
            sb.append(imgUrl.substring(index));
        }
        return sb.toString();
    }

    /**
     * 
     * @param imgUrl
     *            图片原始url
     * @param size
     *            需要拼接的规格
     * @return imgUrl拼接上size之后的url，size会加上下划线拼在imgUrl的扩展名和.之前
     */
    public static String formatImage(String imgUrl,String size){
        return formatImage(null, imgUrl, size, null);
    }
}
