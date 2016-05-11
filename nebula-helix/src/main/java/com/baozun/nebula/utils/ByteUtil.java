package com.baozun.nebula.utils;

/**
 * Byte 工具类
 * 
 * @author 金鑫 2010-3-11 下午02:58:49
 * @since 1.0
 * @deprecated 目前没有使用,feilong已经移动到新的jar 通常不会单独使用这个类 by feilong
 */
@Deprecated
public final class ByteUtil{

	/** Don't let anyone instantiate this class. */
	private ByteUtil(){}

	/**
	 * 数字 字符数组
	 */
	private static final char[]		digit2char	= { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', };

	private static final String[]	hexDigits	= { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 字节数组,转成小写的16进制字符串<br>
	 * md5加密 使用这个
	 * 
	 * @param b
	 * @return
	 */
	public static String bytesToHexStringLowerCase(byte b[]){
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; ++i){
			resultSb.append(byteToHexStringLowerCase(b[i]));
		}
		return resultSb.toString();
	}

	// UpperCase
	// LowerCase
	public static String byteToHexStringLowerCase(byte b){
		int i = b;
		if (i < 0){
			i += 256;
		}
		int d1 = i / 16;
		int d2 = i % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 字节数组,转成大写的16进制字符串 <br>
	 * 网友gdpglc的思路
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bytesToHexStringUpperCase(byte[] bytes){
		if (null == bytes){
			throw new IllegalArgumentException("bytes不能为空");
		}
		char[] tmpData = new char[bytes.length << 1];
		for (int i = 0; i < bytes.length; ++i){
			int left = (bytes[i] & 0xF0) >> 4;
			tmpData[i << 1] = digit2char[left];
			int right = bytes[i] & 0x0F;
			tmpData[(i << 1) + 1] = digit2char[right];
		}
		return new String(tmpData);
	}

	/**
	 * 字节数组转换成16进制字符串
	 * 
	 * @param bytes
	 *            byte[]
	 * @return 16进制字符串
	 * @deprecated ("该方法性能不高,请使用ByteUtil.bytesToHexStringUpperCase(byte[] bytes)")
	 */
	@Deprecated
	public static String bytesToHexString_old(byte[] bytes){
		if (null == bytes){
			throw new IllegalArgumentException("bytes不能为空");
		}
		String returnValue = "";
		String hex = "";
		int length = bytes.length;
		for (int i = 0; i < length; ++i){
			hex = Integer.toHexString(bytes[i] & 0xFF);// 整数转成十六进制表示
			if (hex.length() == 1){
				hex = '0' + hex;
			}
			returnValue += hex;
		}
		// 转成大写
		return returnValue.toUpperCase();
	}

	// *****************************************************************************************************
	/**
	 * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
	 * 
	 * @param byte1
	 * @param byte2
	 * @return 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
	 */
	public static byte uniteBytes(byte byte1,byte byte2){
		byte _b0 = Byte.decode("0x" + new String(new byte[] { byte1 })).byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { byte2 })).byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}

	/**
	 * 16进制字符串转成字节数组
	 * 
	 * @param bytes
	 * @return 16进制字符串转成字节数组
	 */
	public static byte[] hexBytesToBytes(byte[] bytes){
		if (null != bytes){
			int length = bytes.length;
			if ((length % 2) != 0){
				throw new IllegalArgumentException("长度不是偶数,length is:" + length);
			}
			byte[] bytes2 = new byte[length / 2];
			String item = "";
			for (int n = 0; n < length; n += 2){
				item = new String(bytes, n, 2);
				// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节
				bytes2[n / 2] = (byte) Integer.parseInt(item, 16);
			}
			return bytes2;
		}
		return null;
	}
}