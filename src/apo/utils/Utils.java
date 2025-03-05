package apo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Lists;
import com.ibm.icu.text.Transliterator;

public class Utils {
	public static String key(String type, String id, Long userId, Long chatId) throws UnsupportedEncodingException, SQLException {
		String key = "/type=".concat(type).concat("/").concat(type).concat("=").concat(URLEncoder.encode(id, "UTF-8")).concat("/userId=").concat(userId.toString()).concat("/chatId=").concat(chatId.toString());
		if (key.length()>255)
			throw new SQLException("Key large > 255 chars");
		return key;
	}
	
	public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) {
	    Set<T> keys = new HashSet<T>();
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (Objects.equals(value, entry.getValue())) {
	            keys.add(entry.getKey());
	        }
	    }
	    return keys;
	}

	public static final Transliterator t = Transliterator.getInstance("Any-Latin; Latin-ASCII;");
	public static String debugTextConvert(String text) {
		return text != null ? t.transliterate(text).replaceAll("[^A-Za-z0-9 ',.]", "") : "***null***";
	}

	
	public static String debugTextURLConvert(String text) {
//		return text.replace(",", ",\n");
		try {
			return text != null ? URLEncoder.encode(text, "UTF-8") : "***null***";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "***text convert error***";
		}
	}
	
	
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();

	public static String randomString( int len ){
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	   return sb.toString();
	}
	
	public static final char[] ESCAPABLE = "[!\"#$%&'()*+,./:;<=>?@\\[\\\\\\]^_`{|}~-]".toCharArray();
	public static String mdEscape(String t) {
		String r = "";//Escaping.normalizeLabelContent(t);
		Set<Character> chars = new HashSet<>(Lists.charactersOf("[!\"#$%&'()*+,./:;<=>?@\\[\\\\\\]^_`{|}-]"));
		//Set<Character> chars = new HashSet<>(Lists.charactersOf("[!\"#$%&'()*+,./:;<=>?@\\[\\\\\\]^_`{|}~-]"));
		
		for (int i=0;i<t.length();i++) {
			char c = t.charAt(i);
			if (chars.contains(c)) {// Arrays.binarySearch(ESCAPABLE, c)>-1) {
				r+="\\"+c;
			} else {
				r+=c;
			}
		}
		/*
		r = t;
		for (char c:ESCAPABLE) {
			r = r.replaceAll(c+"", "\\"+c);
		}*/
		//r = r.replace(".", "zzz");
//		System.err.println(t + "   ---> " + r);
		return r;
//		return Escaping.normalizeReference(t);
	}

	public static String base64encode(String s) 
	{
		return Base64.getEncoder().encodeToString(s.getBytes(Charset.defaultCharset()));
	}
	
	public static String base64decode(String s) 
	{
		return new String(Base64.getDecoder().decode(s), Charset.defaultCharset());
	}
	
}
