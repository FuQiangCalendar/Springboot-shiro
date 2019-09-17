package com.fuq.demo.tool.string;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

/**
 * 字符串工具类
 * @author FuqCalendar
 * @date 2019年9月5日14:04:51
 *
 */
public class StringUtil extends StringUtils{
	/**
	 * 日志类
	 */
	private final static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StringUtil.class);

	public static Pattern PATTERN = Pattern.compile("[^0-9]");

	
	public static String arrayToDelimitedString(String arrs[], String split) {

		StringBuffer sf = new StringBuffer();
		int len = arrs.length;
		for (int i = 0; i < len; i++) {
			String str = arrs[i];
			sf.append(str).append(split);
			if (i != len - 1) {
				sf.append(split);
			}
		}
		// System.out.println("sf=="+sf.toString());
		return sf.toString();
	}

	/**
	 * 
	 * 对content的内容进行转换后，在作为oracle查询的条件字段值。使用/作为oracle的转义字符,比较合适。<br>
	 * 既能达到效果,而且java代码相对容易理解，建议这种使用方式<br>
	 * "%'" + content + "'%  ESCAPE '/' "这种拼接sql看起来也容易理解<br>
	 * 
	 * @param content
	 * @return
	 */
	public static String decodeSpecialCharsWhenLikeUseBackslash(String content) {
		// 单引号是oracle字符串的边界,oralce中用2个单引号代表1个单引号
		String afterDecode = content.replaceAll("'", "''");
		// 由于使用了/作为ESCAPE的转义特殊字符,所以需要对该字符进行转义
		// 这里的作用是将"a/a"转成"a//a"
		// afterDecode = afterDecode.replaceAll("/", "//");
		// 使用转义字符 /,对oracle特殊字符% 进行转义,只作为普通查询字符，不是模糊匹配
		// afterDecode = afterDecode.replaceAll("%", "/%");
		// 使用转义字符 /,对oracle特殊字符_ 进行转义,只作为普通查询字符，不是模糊匹配
		// afterDecode = afterDecode.replaceAll("_", "/_");
		return afterDecode;
	}
	
	private static final String regex_URL = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$";
	

    /**
     * 判断字符串是否为URL
     * @param urls 用户头像key
     * @return true:是URL、false:不是URL
     */
    public static boolean isHttpUrl(String urls) {
        boolean isurl = false;
//        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
//            + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式

        Pattern pat = Pattern.compile(regex_URL.trim());//比对
        Matcher mat = pat.matcher(urls.trim());
        isurl = mat.matches();//判断是否匹配
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }
	
	/**
	 * 对content的内容进行转换后，在作为oracle查询的条件字段值。使用\作为oracle的转义字符。<br>
	 * 这种做法也能达到目的，但不是好的做法，比较容易出错，而且代码很那看懂。<br>
	 * "%'" + content + "'%  ESCAPE '\' "这种拼接sql实际上是错误的.<br>
	 * "%'" + content + "'%  ESCAPE '\\' "这种拼接sql才是正确的<br>
	 * 
	 * @param content
	 * @return
	 */
	public static String decodeSpecialCharsWhenLikeUseSlash(String content) {
		// 单引号是oracle字符串的边界,oralce中用2个单引号代表1个单引号
		// String afterDecode = content.replaceAll("'", "''");
		// 由于使用了\作为ESCAPE的转义特殊字符,所以需要对该字符进行转义
		// 由于\在java和正则表达式中都是特殊字符,需要进行特殊处理
		// 这里的作用是将"a\a"转成"a\\a"
		content = content.replaceAll("\\\\", "\\\\\\\\");
		// 使用转义字符 \,对oracle特殊字符% 进行转义,只作为普通查询字符，不是模糊匹配
		content = content.replaceAll("%", "\\\\%");
		// 使用转义字符 \,对oracle特殊字符_ 进行转义,只作为普通查询字符，不是模糊匹配
		content = content.replaceAll("_", "\\\\_");
		return content;
	}

	private static final Pattern chineseCharacter = Pattern.compile("[\u4E00-\u9FA0]", Pattern.CANON_EQ);

	private static final Pattern letterAndChineseCharacter = Pattern.compile("[[\u4E00-\u9FA0]a-zA-Z]",
			Pattern.CANON_EQ);

	private static final Pattern pattern = Pattern.compile("(-|\\+|)[0-9]+(\\.|)[0-9]*");

	private static final Pattern isFullShape = Pattern.compile("[\\uFF00-\\uFFFF]");

	public static Matcher getMatcherByStr(String str){
		return  getPatternByStr("[\u4e00-\u9fa5]+").matcher(str);
	}
	/**
	 * 检查客串中是否包含汉字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean containsChiese(String str) {
		Matcher mathcer =getMatcherByStr(str);
		if (mathcer.find()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取26个字母
	 * 
	 * @return
	 */
	public static String[] get26Letter() {
		char a = 'A';
		String str[] = new String[26];
		for (int i = 0; i < 26; i++) {
			str[i] = a + "";
			a++;
		}
		return str;
	}



	public static boolean isNotBlank(String string) {
		return !isBlank(string);
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isBlank(String str) {
		if (null != str) {
			str = replaceBlank(str);
		}
		 return str == null || str.length() == 0;
	}

	/**
	 * 字符转成UTF-8编码
	 * 
	 * @param str
	 *            (%)
	 * @return
	 */
	public static String convertUTF8Charcter(String text) {
		if (containsChiese(text)){
			return text;}
		String p = "", str = text;
		if (str.indexOf("(%)") != -1) {
			int len = text.length();
			str = text.substring(0, len - 3);
			p = text.substring(len - 3, len);
		}
		try {
			if (StringUtil.isBlank(str)){
				str = "";}
			str = java.net.URLDecoder.decode(str, "UTF-8");
			return str + p;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	

	/**
	 * 获取字符串中的所有数字
	 * 
	 * @param string
	 * @return
	 *
	 */
	public static String getAllNumber(String string) {
		Matcher m = PATTERN.matcher(string);
		return m.replaceAll("").trim();
	}
	
	public static Pattern getPatternByStr(String str){
		return  Pattern.compile(str);
	}

	/**
	 * 获取字符串中的所有字符
	 * 
	 * @param string
	 * @return
	 *
	 */
	public static String getAllLetter(String string) {
		Matcher m = getPatternByStr("[^a-zA-Z]").matcher(string);
		return m.replaceAll("").trim();
	}

	/**
	 * 路径去空格
	 * 
	 * @param path
	 * @return
	 */
	public static String pathRemoveBlank(String path) {
		path = path.replaceAll("%20", " ");
		return path;
	}

	/**
	 * 判断是否为数字
	 * 
	 * @param str
	 * @return
	 *
	 */
	public static boolean isNumeric(String str) {
		if (StringUtil.isBlank(str)) {
			return false;
		}
		if ("0E-31".equals(str)) {
			return true;
		}
		if ("0E-15".equals(str)) {
			return true;
		}
		if ("0E-16".equals(str)) {
			return true;
		}
		return pattern.matcher(str.replaceAll(",", "")).matches();
	}

	public static boolean hasFullShape(String s) {
		Matcher m = isFullShape.matcher(s == null ? "" : s);
		if (m.find()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean hasChineseCharacter(String s) {
		Matcher m = chineseCharacter.matcher(s == null ? "" : s);
		if (m.find()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isLetterAndChineseCharacter(String s) {
		Matcher m = letterAndChineseCharacter.matcher(s == null ? "" : s);
		if (m.find()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 公共动态bean所需要的
	 * 
	 * @param column
	 * @return
	 */
	public static String convertLetterToNumStr(String column) {
		return /*CnbiConstants.BEAN_PROPERTY*/"index" + convertLetterToNum(column);
	}

	/**
	 * 字母转数字
	 * 
	 * @param column
	 * @return
	 */
	public static int convertLetterToNum(String column) {
		if (column.length() > 1){
			return 0;}
		for (int i = 0; i < 256; i++) {
			String columnName = convertNumToColumnName(i);
			if (columnName.equalsIgnoreCase(column)) {
				return i + 1;
			}
		}
		return -1;
	}

	/**
	 * 数字转字母
	 * 
	 * @param column
	 * @return
	 */
	public static String convertNumToLetter(int column) {
		column = column - 1;
		String result = "";
		for (; column >= 0; column = column / 26 - 1) {
			result = (char) ((char) (column % 26) + 'A') + result;
		}

		return result;
	}

	/**
	 * 数字转列名称
	 * 
	 * @param column
	 * @return
	 */
	public static String convertNumToColumnName(int column) {
		String result = "";
		for (; column >= 0; column = column / 26 - 1) {
			result = (char) ((char) (column % 26) + 'A') + result;
		}

		return result;
	}

	public static String parseNumber(String str) {
		if (str == null || str.equals("") || str.equals("0E-31") || str.equals("0E-15") || str.equals("0E-16")
				|| str.equals("N/A")) {
			return "0";
		}
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			char x = str.charAt(i);
			if (Character.isDigit(x) == true || x == '.' || x == '+' || x == '-') {
				result += x;
			}
		}
		return result;
	}

	/**
	 * 判断是否有百分符号
	 * 
	 * @param str
	 * @return
	 */
	public static boolean whetherContainsPercent(String str) {
		return str.indexOf("%") != -1;
	}

	/**
	 * 判断两个字符串中有,除exclude字符外count+1个相等的字符就定义为这两个字符相似
	 * 
	 * @param caption
	 * @param newCaption
	 * @return
	 */
	public static boolean getLikeFrom2Str(String caption, String newCaption, String[] exclude, int count) {
		String old = caption, news = newCaption;
		for (String string : exclude) {
			old = old.replace(string, "");
			news = news.replace(string, "");
		}
		int like = getLevenshteinDistance(old, news);
		if (like < news.length() - count) {
			LOGGER.info("【" + newCaption + "】中有【" + caption + "】,相似度为【" + like + "】");
			return true;
		} else {
			LOGGER.info("【" + newCaption + "】中没有【" + caption + "】,相似度为【" + like + "】");
			return false;
		}
	}

	/**
	 * 
	 * @param s
	 * @param t
	 * @return 值越大越不相似
	 */
	// 计算两个字符串的差异值
	public static int getLevenshteinDistance(CharSequence s, CharSequence t) {
		if (s == null || t == null) {
			// 容错，抛出的这个异常是表明在传参的时候，传递了一个不合法或不正确的参数。
			// 好像都这样用，illegal:非法。Argument:参数，证据。
			throw new IllegalArgumentException("Strings must not be null");
		}
		// 计算传入的两个字符串长度
		int n = s.length();
		int m = t.length();
		// 容错，直接返回结果。这个处理不错
		if (n == 0) {
			return m;
		} else if (m == 0) {
			return n;
		}
		// 这一步是根据字符串长短处理，处理后t为长字符串，s为短字符串，方便后面处理
		if (n > m) {
			CharSequence tmp = s;
			s = t;
			t = tmp;
			n = m;
			m = t.length();
		}
		// 开辟一个字符数组，这个n是短字符串的长度
		int p[] = new int[n + 1];
		int d[] = new int[n + 1];
		// 用于交换p和d的数组
		int _d[];
		int i;
		int j;
		char t_j;
		int cost;
		// 赋初值
		for (i = 0; i <= n; i++) {
			p[i] = i;
		}
		for (j = 1; j <= m; j++) {
			// t是字符串长的那个字符
			t_j = t.charAt(j - 1);
			d[0] = j;
			for (i = 1; i <= n; i++) {
				// 计算两个字符是否一样，一样返回0。
				cost = s.charAt(i - 1) == t_j ? 0 : 1;
				// 可以将d的字符数组全部赋值。
				d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
			}
			// 交换p和d
			_d = p;
			p = d;
			d = _d;
		}
		// 最后的一个值即为差异值
		return p[n];
	}

	/**
	 * 用来统计某个单词出现的个数
	 * 
	 * @param source
	 * @param word
	 * @return
	 */
	public static int countWord(String source, String word) {
		char[] word_arr = source.toCharArray();
		int word_num = 0; // 用来统计单词出现的次数
		char[] char_arr = word.toCharArray();
		for (int i = 0; i < word_arr.length; i++) {
			if (char_arr[0] == word_arr[i] && i + char_arr.length <= word_arr.length) {
				int m = i + 1;
				boolean flag = true;
				for (int j = 1; j < char_arr.length;) {
					if (word_arr[j++] != char_arr[m++]) {
						flag = false;
						break;
					}
				}
				if (flag) {
					word_num++;
				}
			}
		}
		return word_num;
	}

	/**
	 * 首字母转小写
	 * 
	 * @param s
	 * @return
	 */
	public static String toLowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0))){
			return s;}
		else{
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}}

	/**
	 * 首字母转大写
	 * 
	 * @param s
	 * @return
	 */
	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0))){
			return s;}
		else{
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}}

	/**
	 * 去除所有空格 回车 换行等…………
	 * 
	 * @param str
	 * @return 回车 \n 换行 \r
	 *
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Matcher m = getPatternByStr("\\s*|\t|\r|\n").matcher(str);
			dest = m.replaceAll("");
		}
		dest = dest.replace("\\n", "").replace("\t", "");
		return dest;
	}

	public static String replaceRN(String str) {
		String dest = "";
		if (str != null) {
			Matcher m = getPatternByStr("\\s*|\r|\n").matcher(str);
			dest = m.replaceAll("");
		}
		// dest = dest.replace("\\n", "").replace("\t", "");
		return dest;
	}

	/**
	 * 去除左边与右边的空格 与回车换行
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceLeftAndRightAndTabBlank(String str) {
		str = str.trim();
		str = str.substring(str.lastIndexOf(str.trim()));
		Matcher m =getPatternByStr("\\t|\r|\n").matcher(str);
		str = m.replaceAll("");
		return str;
	}

	// 1去掉左边空格name.replace(/^\s*/g,"");
	// 2去掉右边空格name.replace(/\s*$/g,"");
	// 3去掉前后空格smSmsModelName.replace(/(^\s*)|(\s*$)/g,"")
	public static String replace(String str) {// replaceLeftAndRightBlank
		str = str.trim();
		str = str.substring(str.lastIndexOf(str.trim()));
		Matcher m = getPatternByStr("\\t|\r|\n").matcher(str);
		str = m.replaceAll("");
		return str;
	}

	/**
	 * 分割成数字数组 String str = "13+41/44*49-31" [13,41,44,49,31]
	 */
	public static String[] getNumberArr(String str) {
		return str.split("\\p{Punct}");
	}

	/**
	 * 分割出符号数组 negative * String str = "13+41/44*49-31" [+,/,*,-]
	 */
	public static String[] getSym(String str) {
		return str.split("\\p{Digit}");
	}

	/**
	 * 字符串数组 转为 字符串格式 已逗号分隔 方便 sql 语句
	 * 
	 * @param 字符串数组
	 * @param prefix
	 *            需要添加的前缀
	 * @param suffix
	 *            需要添加的后缀 需要在添加
	 * @param tag
	 *            添加的标记
	 * @return 字符串 已 逗号分隔
	 **/
	public static String getArraytoSting(String[] strArr, String prefix, String suffix, String tag) {
		if (prefix == null){
			prefix = "";}
		if (suffix == null){
			suffix = "";}
		if (tag == null){
			tag = "";}
		StringBuffer sf = new StringBuffer();
		if (strArr.length != 0) {
			for (int i = 0, len = strArr.length; i < len; i++) {
				sf.append(prefix + tag + strArr[i]);
				if (i != len - 1) {
					sf.append(",");
				}
			}
		}
		return sf.toString();
	}

	/**
	 * @author FuqCalendar
	 * @date 2019年9月4日15:04:54
	 * @param str
	 *            需要操作的字符串
	 * @return String 通过正则表达式 返回截取需要的字符串
	 **/
	public static String splitStr(String str) {
		String regex = ".+\\/(.+)\\-";
		Pattern patt = Pattern.compile(regex);
		Matcher matcher = patt.matcher(str);
		if (matcher.find()) {
			return matcher.group(matcher.groupCount());
		} else {
			LOGGER.info("no match...");
			return str.split("/")[1].split("-")[0];
		}
	}

	/**
	 * 以传入的规则获取字符串数组
	 * @author FuqCalendar
	 * @date 2019年9月4日15:04:54
	 * @param item
	 * @param split
	 * @return
	 */
	public static String[] getItemArr(String item, String split) {
		return item.split("\\" + split);
	}

	/**
	 * 为字符增加单引号
	 * @author FuqCalendar
	 * @date 2019年9月4日15:04:54
	 * @param str
	 * @return
	 */
	public static String addSingleQuotes(String str) {
		str = "'" + str + "'";
		//if (str.contains(",") && !isInteger)str = str.replace(",", "','");
		return str;
	}

	/**
	 * 缓存key统一处理
	 * @author FuqCalendar
	 * @date 2019年9月4日15:04:54
	 * @param key
	 * @return
	 */
	public static String cacheKeyHandler(String key) {
		key = key.replace(".", "").replace(",", "").replace(/*CnbiConstants.FACT*/"fact", "")
				.replace(/*SymbolConstants.UNDERLINE*/"_", "").replace("as", "").replace("null", "").replace("~", "")
				.replace("@", "").replace(";", "").replace("LjavalangString", "");
		key = replaceBlank(key);
		return key;
	}

	public static String encoding(String msg, String encoding) {
		if (StringUtil.isBlank(encoding)) {
			encoding = "UTF-8"; //CnbiConstants.UTF8;
		}
		try {
			return java.net.URLEncoder.encode(msg, encoding);
		} catch (UnsupportedEncodingException e) {
			return msg;
		}
	}

	/**
	 * 正则验证 [//w//.//-]+@([//w//-]+//.)+[//w//-]+ ^[0-9]*[1-9][0-9]*$
	 * ^[a-zA-Z0-9_]+$ 只有字母和数字的： ^[a-zA-Z0-9_]+$
	 * ^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*((\\.
	 * [A-Za-z]{2,}){1}$) \\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*
	 * \d{3}-\d{8}|\d{4}-\d{7} 有中文验证的正则：[\u4e00-\u9fa5]+; \u4E00-\u9FA5
	 * @author FuqCalendar
	 * @date 2019年9月4日15:04:54
	 * @param text
	 * @param regex
	 */
	// w+([-+.]//w+)*@//w+([-.]//w+)*//.//w+([-.]//w+)*
	public static boolean validPattern(String text, String regex) {
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}

	/**
	 * 将年份或月份转化为大写
	 * @author FuqCalendar
	 * @date 2019年9月4日15:04:54
	 * @param value
	 *            转化的内容
	 * @param type
	 *            转化的类型 0 年 1 月
	 * @return
	 */
	public static String convertInt2String(String value, int type) {
		String result = "";
		String[] s = new String[] { "○", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二" };
		if (type == 0) { // 年
			for (int i = 0; i < value.length(); i++) {
				int index = Integer.valueOf(value.substring(i, i + 1));
				result += s[index];
			}
		} else if (type == 1) { // 月
			if (value.equals("00")) {
			} else if (value.startsWith("H")) {
			} else if (value.startsWith("Q")) {
				int m = Integer.valueOf(value.substring(1));
				result = s[m];
			} else {
				int m = Integer.valueOf(value);
				result = s[m];
			}
			// } else if (type == 2) { // 季度
			// int m = Integer.valueOf(value);
			// result = s[m] + "季度";
		} else { // 其他
			result = value;
		}
		return result;
	}

	/**
	 * 返回计算并转换后的字符串
	 * @author FuqCalendar
	 * @date 2019年9月4日15:04:54
	 * @param up
	 *            转换为大写
	 * @param num
	 *            仅数字
	 * @param month
	 *            月位置的期间
	 * @param add
	 *            差值
	 * @return
	 */
	public static String calOnMonth(boolean up, boolean num, String month, int add) {
		if (month.equals("00")) {
			if (!num) {
				month = "年度";
			} else {
				month = "";
			}
		} else if (month.matches("H\\d")) {
			int monthint = Integer.parseInt(month.substring(1));
			monthint += add;
			monthint %= 2;
			if (monthint <= 0) {
				monthint += 2;
			}
			if (monthint == 1) {
				month = "上半年";
			} else if (monthint == 2) {
				month = "下半年";
			}
		} else if (month.matches("Q\\d")) {
			int monthint = Integer.parseInt(month.substring(1));
			monthint += add;
			monthint %= 4;
			if (monthint <= 0) {
				monthint += 4;
			}
			month = monthint + "";
			if (up) {
				month = convertInt2String(month, 0);
			}
			if (!num) {
				month += "季度";
			}
		} else if (month.matches("[0|1][\\d]")) {
			int monthint = Integer.parseInt(month);
			monthint = monthint + add;
			monthint %= 12;
			if (monthint <= 0) {
				monthint += 12;
			}
			month = monthint + "";
			if (up) {
				month = convertInt2String(month, 1);
			}
			if (!num) {
				month = month + "月份";
			}
		}
		return month;
	}

	/**
	 * 标题分析
	 * @author FuqCalendar
	 * @date 2019年9月4日15:04:54
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @param title
	 *            标题
	 * @return 更改后的标题
	 */
	public static String getConvertedTitle(String fullname, String shotname, String comcode, String year, String month,
			String title) {
		if (title.contains("&")) {
			// 替换公司
			title = repTitle(title, "&A", fullname);// title.replace("&A",
													// fullname);
			title = repTitle(title, "&a", shotname);// title.replace("&a",
													// shotname);
			// 替换年份
			String upperYear = convertInt2String(year, 0);
			title = repTitle(title, "&B", upperYear + "年");// title.replace("&B",
															// upperYear + "年");
			title = repTitle(title, "&b", year + "年");// title.replace("&b",
														// year + "年");
			title = repTitle(title, "&E", year);// title.replace("&E", year);
			// 替换月份
			title = repTitle(title, "&C", calOnMonth(true, false, month, 0));// title.replace("&C",
																				// calOnMonth(true,
																				// false,
																				// month,
																				// 0));
			title = repTitle(title, "&c", calOnMonth(false, false, month, 0));// title.replace("&c",
																				// calOnMonth(false,
																				// false,
																				// month,
																				// 0));
			title = repTitle(title, "&e", calOnMonth(false, true, month, 0));// title.replace("&e",
																				// calOnMonth(false,
																				// true,
																				// month,
																				// 0));
			// 替换季度
			if (!month.equals("00") && month.matches("[\\d]{2}")) {
				int n = (Integer.valueOf(month) / 4) + 1;
				title = repTitle(title, "&D", convertInt2String(n + "", 0) + "季度");// title.replace("&D",
																					// convertInt2String(n
																					// +
																					// "",
																					// 0)
																					// +
																					// "季度");
				title = repTitle(title, "&d", n + "季度");// title.replace("&d", n
														// + "季度");
			}
		}
		// 计算相对期间
		if (title.contains("&-")) {
			Matcher m = getPatternByStr("&(\\-\\d+)([A-Za-z])").matcher(title);
			while (m.find()) {
				String v = m.group(1);
				String sign = m.group(2);
				String s = "&" + v + sign;
				// LOGGER.debug(s+" ; value : "+v+" ; sign :"+sign);
				if (sign.equals("B")) {
					int yearint = Integer.parseInt(year) + Integer.parseInt(v);
					title = repTitle(title, s, convertInt2String(yearint + "", 0) + "年");// title.replace(s,
																							// convertInt2String(yearint
																							// +
																							// "",
																							// 0)
																							// +
																							// "年");
				} else if (sign.equals("b")) {
					int yearint = Integer.parseInt(year) + Integer.parseInt(v);
					title = repTitle(title, s, yearint + "年");// title.replace(s,
																// yearint +
																// "年");
				} else if (sign.equals("C")) {
					title = repTitle(title, s, calOnMonth(true, false, month, Integer.parseInt(v))); // title.replace(s,
																										// calOnMonth(true,
																										// false,
																										// month,
																										// Integer.parseInt(v)));
				} else if (sign.equals("c")) {
					title = repTitle(title, s, calOnMonth(false, false, month, Integer.parseInt(v)));// title.replace(s,
																										// calOnMonth(false,
																										// false,
																										// month,
																										// Integer.parseInt(v)));
				} else if (sign.equals("E")) {
					int yearint = Integer.parseInt(year) + Integer.parseInt(v);
					title = repTitle(title, s, yearint + "");// title.replace(s,
																// yearint +
																// "");
				} else if (sign.equals("e")) {
					title = repTitle(title, s, calOnMonth(false, true, month, Integer.parseInt(v)));// title.replace(s,
																									// calOnMonth(false,
																									// true,
																									// month,
																									// Integer.parseInt(v)));
				}
			}
		}
		title = title.replaceAll("/&", "&");
		return title;
	}

	/**
	 * 替换(仅适用替换[^/]&[\w])
	 * @author FuqCalendar
	 * @date 2019年9月4日15:04:54
	 * @param str
	 *            原字符串
	 * @param reg
	 *            正则字符串
	 * @param tostr
	 *            替换字符串
	 * @return
	 */
	public static String repTitle(String str, String reg, String tostr) {
		StringBuffer bStr = new StringBuffer(str);
		Pattern pattern = Pattern.compile(reg);
		Matcher m = pattern.matcher(str);
		while (m.find()) {
			int s = m.start();
			int e = m.end();
			if (s != 0 && bStr.substring(s - 1, s).equals("/")) {
				continue;
			}
			bStr.replace(s, e, tostr);
		}
		return bStr.toString();
	}

	/**
	 * 如果是null或空的返回传入的默认值
	 * @author FuqCalendar
	 * @date 2019年9月4日15:04:54
	 * @param src
	 *            检查字符串
	 * @param defsrc
	 *            传入的默认字符串
	 * @return
	 */
	public static String getIfEmptyString(String src, String defsrc) {
		src = disposeString(src);
		if (src.equals("")) {
			src = defsrc;
		}
		return src;
	}

	/**
	 * 获取去空格字符串处理null
	 * @author FuqCalendar
	 * @date 2019年9月4日15:04:54
	 * @param src
	 * @return 返回去空格字符串 如果是null的返回""
	 */
	public static String disposeString(String src) {
		if (src == null) {
			src = "";
		}
		return src.trim();
	}
	
	public static String addComs(String str){
		if(!str.contains(",")){return addCom(str);}
		 String arr[] = str.split("\\,");
		 StringBuffer sf = new StringBuffer();
		for (int i=0,len = arr.length;i<len;i++) {
			sf.append(addCom(arr[i]));
			if(i != len -1){
				sf.append(",");
			}
		}
		return sf.toString();
	}
	
	
	public static String addCom(String str){
		return "'"+str+"'";
	}
	
	/**
	 * @author FuqCalendar
	 * @date 2019年9月4日15:04:54
	 * @Title: getCloToString
	 * @Description: 将Clob 类型转换为字符串
	 * @param @param clob
	 * @param @return
	 * @param @throws SQLException
	 * @param @throws IOException    参数
	 * @return String    返回类型
	 * @throws
	 */
	public static String getCloToString(Clob clob) throws SQLException,IOException{
			java.io.Reader is = clob.getCharacterStream();// 得到流   
			BufferedReader br = new BufferedReader(is);
			String s = br.readLine();
			StringBuffer sb = new StringBuffer();
			while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING   
			sb.append(s);
			s = br.readLine();
			}
			
			return sb.toString();
		}
	
		//提取字符串中的所有<imag>标签
		private static final String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
		
		/**
		 * @author FuqCalendar
		 * @date 2019年9月4日15:04:54
		 * @Title: collectImag
		 * @Description: 提取字符串中的所有imag标签
		 * @param @param htmlStr
		 * @param @return    参数
		 * @return List<String>    返回类型
		 * @throws
		 */
		public static List<String> collectImags (String htmlStr) {
			List<String> Imags = new ArrayList<>();
			Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
			Matcher m_image = p_image.matcher(htmlStr); 
			while (m_image.find()) {  
				Imags.add(m_image.group());
			}
			return Imags;
		}
		
		/**
		 * @author FuqCalendar
		 * @date 2019年9月4日15:04:54
		 * @Title: collectImag
		 * @Description: 提取字符串中的第一个imag标签
		 * @param @param htmlStr
		 * @param @return    参数
		 * @return List<String>    返回类型
		 * @throws
		 */
		public static String collectImag (String htmlStr) {
			Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
			Matcher m_image = p_image.matcher(htmlStr); 
			while (m_image.find()) {  
				return m_image.group();
			}
			return null;
		}
		
		//提取<imag>标签中的src
		private static final String srcEx_img = "src\\s*=\\s*\"?(.*?)(\"|>|\\s+)";
		
		/**
		 * @author FuqCalendar
		 * @date 2019年9月4日15:04:54
		 * @Title: collectSrc
		 * @Description: 返回src
		 * @param @param imgStr
		 * @param @return    参数
		 * @return String    返回类型
		 * @throws
		 */
		public static String collectSrc (String imgStr) {
			Pattern p_src = Pattern.compile(srcEx_img, Pattern.CASE_INSENSITIVE);
			Matcher m_src = p_src.matcher(imgStr); 
			while (m_src.find()) {  
				return m_src.group(1);
			}
			
			return null;
		}
	
}
