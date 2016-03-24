/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.taglib.mask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * 隐藏敏感数据部分位，1111********1111
 * @author D.C
 * @date 2015年10月14日 下午1:52:26
 */
public class StringMask {

    private List<UnmaskRange> ranges;
    private char maskCharacter;

    public StringMask(List<UnmaskRange> ranges, char maskCharacter) {
        this.ranges = ranges;
        this.maskCharacter = maskCharacter;
    }

    public String mask (String string) {
        if (string == null) {
            throw new RuntimeException("string is null");
        }
        char[] characters = string.toCharArray();
        char[] newCharacters = new char[characters.length];
        //do mask phase
        Arrays.fill(newCharacters, 0, newCharacters.length, maskCharacter);
        for (UnmaskRange range : ranges) {
        	int rangeLength = (range.getLength() <= string.length() && range.getLength() >= 0) ? range.getLength() : string.length();
            if (range.getMode() == UnmaskRange.START_MODE) {
                System.arraycopy(characters, 0, newCharacters, 0, rangeLength);
            } else {
                System.arraycopy(characters, characters.length - rangeLength, newCharacters, newCharacters.length - rangeLength, rangeLength);
            }
        }

        return new String(newCharacters);
    }

    public static void main( String[] args ) {
        ArrayList<UnmaskRange> ranges = new ArrayList<UnmaskRange>();
        ranges.add(new UnmaskRange(UnmaskRange.START_MODE,0));
		ranges.add(new UnmaskRange(UnmaskRange.END_MODE, 0));
        //ranges.add(new UnmaskRange(UnmaskRange.ENDTYPE, 4));
        StringMask mask = new StringMask(ranges, '*');
        System.out.println("Card: " + mask.mask( "宋超" ) );
        System.out.println("Card: " + mask.mask( "" ) );
    }
}
