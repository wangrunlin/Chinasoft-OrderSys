/**
 *  Copyright 2015 ChinaSoft International Ltd. All rights reserved.
 */
package com.chinasofti.util.sec;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * <p>
 * Title: Passport
 * </p>
 * <p>
 * Description:自定义的可逆加密算法，主要用于非超大容量的字符串加密，可以生成网络传输数据的令牌，不同时间执行加密算法获取到的加密结果不同
 * </p>
 * <p>
 * Copyright: Copyright (c) 2015
 * </p>
 * <p>
 * Company: ChinaSoft International Ltd.
 * </p>
 * 
 * @author etc
 * @version 1.0
 */
public class Passport {

	public Passport() {
		// TODO 自动生成构造函数存根
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成方法存根
		Passport passport = new Passport();
		// String txt = "中文文本";
		// String key = "chinasofti";
		// String jia_str = passport.passport_encrypt(txt, key);
		// String jie_str = passport.passport_decrypt(jia_str, key);
		// System.out.println("加密函数测试：" + jia_str);
		// System.out.println("解密函数测试：" + jie_str);

		System.out.println(passport.md5("admin"));

	}

	/**
	 * Md5加密
	 * 
	 * @param x
	 *            需要加密的字符串
	 * @return md5加密结果
	 * @throws Exception
	 */
	public String md5(String x) {
		// 获取摘要工具
		MessageDigest m = null;
		try {
			// MD5摘要工具
			m = MessageDigest.getInstance("MD5");
			// 更新被文搞描述的位元组
			m.update(x.getBytes("UTF8"));
			// 捕获不支持摘要异常
		} catch (NoSuchAlgorithmException e) {
			// 创建一个MD5消息文搞 的时候出错
			e.printStackTrace();
			// 捕获不支持字符集异常
		} catch (UnsupportedEncodingException e) {
			// 更新被文搞描述的位元组 的时候出错
			e.printStackTrace();
		}
		// 最后更新使用位元组的被叙述的排列,然后完成文摘计算
		byte s[] = m.digest();
		// System.out.println(s); // 输出加密后的位元组
		// 创建结果字符串缓冲
		StringBuilder result = new StringBuilder("");
		// 遍历文摘
		for (int i = 0; i < s.length; i++) {
			// 进行十六进制转换
			result.append(Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00)
					.substring(6));
		}
		// 返回加密结果
		return result.toString();

	}

	/**
	 * 本方法将字符串以 MIME BASE64 编码。此编码方式可以让中文字或者图片也能在网络上顺利传输。在 BASE64
	 * 编码后的字符串只包含英文字母大小写、阿拉伯数字、加号与反斜线，共 64 个基本字符，不包含其它特殊的字符， 因而才取名
	 * BASE64。编码后的字符串比原来的字符串长度再加 1/3 左右。更多的 BASE64 编码信息可以参考 RFC2045 文件之 6.8 节
	 * 
	 * @param txt
	 *            等待编码的原字串
	 * @return 编码后的结果
	 */
	public String base64_decode(String txt) {
		// 定义编码器
		BASE64Decoder base64_decode = new BASE64Decoder();
		// 定义结果字符串
		String str = "";
		try {
			// 获取加密结果
			str = new String(base64_decode.decodeBuffer(txt));
		} catch (IOException e) {
			// 如果有异常则输出异常信息
			e.printStackTrace();
		}
		// 返回编码结果
		return str;
	}

	/**
	 * Base64编码的方法
	 * 
	 * @param txt
	 *            要编码的字符串
	 * @return 编码结果
	 * */
	public String base64_encode(String txt) {
		// 创建编码器
		BASE64Encoder base64_encode = new BASE64Encoder();
		// 返回编码结果
		return base64_encode.encode(txt.getBytes());
	}

	/**
	 * Passport 加密方法
	 * 
	 * @param string
	 *            等待加密的原字串
	 * @param string
	 *            私有密匙(用于解密和加密)
	 * 
	 * @return string 原字串经过私有密匙加密后的结果
	 */
	public String passport_encrypt(String txt, String key) {
		// 创建随机工具
		Random random = new Random();
		// 使用随机数发生器产生 0~32000 的值
		String rad = String.valueOf(random.nextInt(32000));
		// 获取随机值的md5码
		String encrypt_key = md5(rad);

		// 变量初始化
		int ctr = 0;
		// 定义结果字符串缓冲
		StringBuilder tmp = new StringBuilder("");

		// 获取md5码的字符数组形式
		char encrypt_key_char[] = encrypt_key.toCharArray();
		// 获取初始文本的字符数组形式
		char txt_char[] = txt.toCharArray();
		// for 循环，$i 为从 0 开始，到小于 $txt 字串长度的整数
		for (int i = 0; i < txt.length(); i++) {
			// 如果 $ctr = $encrypt_key 的长度，则 $ctr 清零
			ctr = ctr == encrypt_key_char.length ? 0 : ctr;
			// $tmp 字串在末尾增加两位，其第一位内容为 $encrypt_key 的第 $ctr 位，
			// 第二位内容为 $txt 的第 $i 位与 $encrypt_key 的 $ctr 位取异或。然后 $ctr = $ctr + 1
			char tmp1 = txt_char[i];
			// 编码字符
			char tmp4 = encrypt_key_char[ctr];
			// 编码第二个字符
			char tmp2 = encrypt_key_char[ctr++];
			// 进行位运算
			char tmp3 = (char) (tmp1 ^ tmp2);
			// 添加结果数据
			tmp.append(tmp4 + "" + tmp3);
		}
		// 返回结果，结果为 passport_key() 函数返回值的 base65 编码结果
		return base64_encode(passport_key(tmp.toString(), key));

	}

	/**
	 * Passport 解密方法
	 * 
	 * @param string
	 *            加密后的字串
	 * @param string
	 *            私有密匙(用于解密和加密)
	 * 
	 * @return string 字串经过私有密匙解密后的结果
	 */
	public String passport_decrypt(String txt, String key) {

		// $txt 的结果为加密后的字串经过 base64 解码，然后与私有密匙一起，
		// 经过 passport_key() 函数处理后的返回值
		txt = passport_key(base64_decode(txt), key);
		// 变量初始化
		StringBuilder tmp = new StringBuilder("");
		// 获取字符串数组形式
		char txt_char[] = txt.toCharArray();
		// for 循环，$i 为从 0 开始，到小于 $txt 字串长度的整数
		for (int i = 0; i < txt.length(); i++) {
			// $tmp 字串在末尾增加一位，其内容为 $txt 的第 $i 位，
			// 与 $txt 的第 $i + 1 位取异或。然后 $i = $i + 1
			tmp.append((char) (txt_char[i] ^ txt_char[++i]));
		}

		// 返回 $tmp 的值作为结果
		return tmp.toString();

	}

	/**
	 * Passport 密匙处理方法
	 * 
	 * @param string
	 *            待加密或待解密的字串
	 * @param string
	 *            私有密匙(用于解密和加密)
	 * 
	 * @return string 处理后的密匙
	 */
	String passport_key(String txt, String encrypt_key) {

		// 将 $encrypt_key 赋为 $encrypt_key 经 md5() 后的值
		encrypt_key = md5(encrypt_key);
		// 变量初始化
		int ctr = 0;
		// 创建结果字符串缓冲
		StringBuilder tmp = new StringBuilder("");

		// 获取md5码字符数组形式
		char encrypt_key_char[] = encrypt_key.toCharArray();
		// 获取原文本字符数组表现形式
		char txt_char[] = txt.toCharArray();
		// for 循环，$i 为从 0 开始，到小于 $txt 字串长度的整数
		for (int i = 0; i < txt.length(); i++) {
			// 如果 $ctr = $encrypt_key 的长度，则 $ctr 清零
			ctr = ctr == encrypt_key.length() ? 0 : ctr;
			// $tmp 字串在末尾增加一位，其内容为 $txt 的第 $i 位，
			// 与 $encrypt_key 的第 $ctr + 1 位取异或。然后 $ctr = $ctr + 1
			char c = (char) (txt_char[i] ^ encrypt_key_char[ctr++]);
			// 追加结果
			tmp.append(c);
		}

		// 返回 $tmp 的值作为结果
		return tmp.toString();

	}

}
