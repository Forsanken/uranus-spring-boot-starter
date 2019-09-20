package com.uranus.framework.util;

import java.io.*;

public class SerializableUtil {
	/**
	 * 序列化
	 */
	public static byte[] serialize(Serializable serializable) throws Exception {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(serializable);
            return baos.toByteArray();
		} catch (Exception e) {
			throw e;
		} finally {
			if (baos != null) {
				baos.close();
			}
			if (oos != null) {
				oos.close();
			}
		}

	}

	/**
	 * 反序列化
	 */
	public static Object unserialize(byte[] bytes) throws Exception {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois =null;
		try {
			if(null == bytes){
				return null;
			}
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			throw e;
		} finally {
			if (ois != null) {
				ois.close();
			}
			if (bais != null) {
				bais.close();
			}
		}
	}

}
