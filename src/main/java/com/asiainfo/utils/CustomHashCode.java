package com.asiainfo.utils;

import com.asiainfo.utils.aelstad.keccakj.fips202.Shake256;

/***
 * 
 * @author jayhan
 *  1.当输入的字符串小于等于16位，hash结果出一个16位的随机字符串，必须保证输入相同的字符串，都是出相同的hash结果；
 *  2.当字符串超过16位，小于等于32位，统一hash出32位的字符串；超过32，小于等于64，hash出64位； 然后128,256 。。。。 等等
 *
 */

public class CustomHashCode {

	public String getHashCode(String input) {
		int strLength = input.length();
		if (strLength <= 16) {
			return getHashCode(input.toString(), 16);
		} else if (strLength <= 32) {
			return getHashCode(input.toString(), 32);
		} else if (strLength <= 64) {
			return getHashCode(input.toString(), 64);
		} else if (strLength <= 128) {
			return getHashCode(input.toString(), 128);
		} else if (strLength <= 256) {
			return getHashCode(input.toString(), 256);
		}else{
			return getHashCode(input.toString(), 512);
		}
	}

	public static void main(String[] args) {
		System.out.println(new CustomHashCode().getHashCode("1", 8));
		System.out.println(new CustomHashCode().getHashCode("2", 32));
		System.out.println(new CustomHashCode().getHashCode("3", 16));
	}

	public String getHashCode(String str, int length) {
		Shake256 sponge = new Shake256();
		// absorb data
		sponge.getAbsorbStream().write(str.getBytes());
		// squeeze digest
		// 可指定任意长度
		byte[] digest = new byte[length / 2];
		sponge.getSqueezeStream().read(digest);
		sponge.reset();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			int a = digest[i];
			if (a < 0)
				a += 256;
			if (a < 16)
				buf.append("0");
			buf.append(Integer.toHexString(a));
		}
		return new String(buf);
	}
}