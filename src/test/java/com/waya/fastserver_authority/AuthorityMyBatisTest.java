package com.waya.fastserver_authority;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.hutrace.handy.utils.code.SHA;

/**
 * Unit test for simple App.
 */
public class AuthorityMyBatisTest {
	
	public static void main(String[] args) throws Exception {
		test2();
		test3();
		test4();
	}
	
	public static void test4() {
		String str = "dwa/s\\daw";
		str = str.replace("\\", File.separator).replace("/", File.separator);
		System.out.println(str);
	}
	
	public static void test3() throws Exception {
		long start = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long now = System.currentTimeMillis();
		System.out.println((now - start) + ": " + sdf.parse("2020-05-25 23:59:59").getTime());
		long current = System.currentTimeMillis();
		long zero = current-(current+TimeZone.getDefault().getRawOffset())%(1000*3600*24) + 1000*3600*24 - 1;
		System.out.println((System.currentTimeMillis() - current) + ": " + zero);
	}
	
	public static void test2() {
		long current = System.currentTimeMillis();
		long one = 1000 * 3600 * 24;
		long two = 1000 * 3600 * 24;
		long three = TimeZone.getDefault().getRawOffset();
		long l1 = current / one;
		long l2 = l1 * two;
		long l3 = l2 - three;
		System.out.println(l3);
		System.out.println(current);
		System.out.println((System.currentTimeMillis() - current) + ": " + (l3 + two - 1));
	}
	
	public static void test1() throws Exception {
		System.out.println(SHA.sha1("莫西莫西"));
		System.out.println(SHA.sha256("莫西莫西"));
	}
	
}
