package com.uranus.framework.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * <pre>
 * 		<p>Title: 数值运算 </p>
 * 		<p> Description: </p>
 * 		<p>Company: chuangjia</p>
 * </pre>
 * 
 */
public class NumberUtil {

	private final static DecimalFormat DOUBLE_FORMAT_DEFAULT = new DecimalFormat("0.00");

	/**
	 * 
	 * @param data
	 *            需要被格式化的数据,可接收java的原生类型数据
	 * @param pattern
	 *            格式化pattern, 例如：0、0.00或0.00%
	 * @return 按照指定的pattern形式，格式化data后返回
	 */
	public static String format(double data, String pattern) {
		DecimalFormat formatter = new DecimalFormat(pattern);

		return formatter.format(data);
	}

	/**
	 * 
	 * @param data
	 *            需要被格式化的数据,可接收java的原生类型数据
	 * @return 按照指定的0.00形式，格式化data后返回
	 */
	public static String format(double data) {
		return DOUBLE_FORMAT_DEFAULT.format(data);
	}

	/**
	 * 
	 * @param data
	 *            需要被格式化的数据,可接收java的原生类型数据
	 * @param pattern
	 *            格式化pattern类型，例如：0、0.00或0.00%
	 * @return 需要被格式化的数据,可接收java的原生类型数据
	 */
	public static double formatToDouble(double data, String pattern) {
		return Double.valueOf(format(data, pattern));
	}

	/**
	 * 
	 * @param data
	 *            需要被格式化的数据,可接收java的原生类型数据
	 * @return 按照指定的0.00形式，格式化data后返回
	 */
	public static double formatToDouble(double data) {
		return Double.valueOf(format(data));
	}

	/**
	 * 
	 * <p>
	 * Title: formatToPecentByDefault
	 * </p>
	 * <p>
	 * Description:this is a method
	 * </p>
	 * 
	 * @param data
	 *            需要被格式化的数据
	 * @return 按照0.00%的形式格式化data数据后返回
	 */
	public static String formatToPecent(double data) {
		return format(data, "0.00%");
	}

	/**
	 * 实现一个BigInteger和int的求和
	 * 
	 * @param a
	 *            被加数，BigInteger对象
	 * @param b
	 *            加数，整形
	 * @return 返回相加后的结果
	 */
	public static BigInteger addBigInt(BigInteger a, int b) {
		return a.add(BigInteger.valueOf(b));
	}

	/**
	 * 实现一个BigInteger的自增1
	 * 
	 * @param a
	 *            被加数，BigInteger对象
	 * @return 返回a + 1 之后的结果
	 */
	public static BigInteger autoIncrement(BigInteger a) {
		return addBigInt(a, 1);
	}

	/**
	 * 
	 * @param v1
	 *            双精度被加数
	 * @param v2
	 *            双精度加数
	 * @return 返回 v1 + v2 之后的结果
	 */
	public static double add(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).doubleValue();
	}

	/**
	 * 
	 * @param v1
	 *            双精度被加数
	 * @param v2
	 *            双精度加数
	 * @param scale
	 *            对返回结果保留scale位小数
	 * @return v1 + v2 之后，保留scale位小数后返回
	 */
	public static double add(double v1, double v2, int scale) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).setScale(scale, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}

	/**
	 * 
	 * @param v1
	 *            双精度被减数
	 * @param v2
	 *            双精度减数
	 * @return 返回 v1 - v2 之后的结果
	 */
	public static double subtract(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 
	 * @param v1
	 *            双精度被减数
	 * @param v2
	 *            双精度减数
	 * @param scale
	 *            对返回结果保留scale位小数
	 * @return v1 - v2 之后，保留scale位小数后返回
	 */
	public static double subtract(double v1, double v2, int scale) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.subtract(b2).setScale(scale, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}

	/**
	 * 
	 * @param v1
	 *            双精度被乘数
	 * @param v2
	 *            双精度乘数
	 * @return 返回 v1 * v2 之后的结果
	 */
	public static double multiply(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 
	 * @param v1
	 *            双精度被乘数
	 * @param v2
	 *            双精度乘数
	 * @param scale
	 *            对返回结果保留scale位小数
	 * @return v1 * v2 之后，保留scale位小数后返回
	 */
	public static double multiply(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}

	/**
	 * 
	 * @param v1
	 *            双精度被除数
	 * @param v2
	 *            双精度除数
	 * @return 返回 v1 / v2 之后的结果
	 */
	public static double divide(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 
	 * @param v1
	 *            双精度被除数
	 * @param v2
	 *            双精度除数
	 * @param scale
	 *            对返回结果保留scale位小数
	 * @return v1 / v2 之后，保留scale位小数后返回
	 */
	public static double divide(double v1, double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}

		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 
	 * @param v1
	 *            被比较的双精度参数
	 * @param v2
	 *            用于比较的双精度参数
	 * @return 如果v1小于v2，则返回-1；如果v1大于v2，则返回1；如果相等，则返回0
	 */
	public static int compareTo(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.compareTo(b2);
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 提供精确的类型转换(Float)
	 * 
	 * @param v 双精度数
	 * @return float
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public static float convertsToFloat(double v) {
		BigDecimal b = new BigDecimal(v);
		return b.floatValue();
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 提供精确的类型转换(Int)不进行四舍五入
	 * 
	 * @param v 双精度数
	 * @return int
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public static int convertsToInt(double v) {
		BigDecimal b = new BigDecimal(v);
		return b.intValue();
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 提供精确的类型转换(Long)
	 * 
	 * @param v 双精度数
	 * @return long
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public static long convertsToLong(double v) {
		BigDecimal b = new BigDecimal(v);
		return b.longValue();
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 返回两个数中大的一个值
	 *
     * @param v1
     *          双精度数1
     * @param v2
     *          双精度数2
     * @return double
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public static double returnMax(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.max(b2).doubleValue();
	}

	/**
	 * 
	 * 功能描述: <br>
	 * 返回两个数中小的一个值
	 * 
	 * @param v1
     *          双精度数1
	 * @param v2
     *          双精度数2
	 * @return double
	 * @see [相关类/方法](可选)
	 * @since [产品/模块版本](可选)
	 */
	public static double returnMin(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.min(b2).doubleValue();
	}
}
