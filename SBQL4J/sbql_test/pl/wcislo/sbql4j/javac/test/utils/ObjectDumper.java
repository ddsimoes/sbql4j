package pl.wcislo.sbql4j.javac.test.utils;

import java.util.List;
import java.util.Map;

public class ObjectDumper {
	public static String dump(Object qRes) {
		return dump(qRes, 0);
	}
	
	private static String dump(Object qRes, int offset) {
		StringBuilder sb = new StringBuilder();
		if(qRes instanceof Map) {
			Map<String, Object> struct = (Map<String, Object>) qRes;
			sb.append(dumpOffset(offset)).append("{\n");
			int nOffset = offset + 2;
			for(String key : struct.keySet()) {
				Object val = struct.get(key);
				sb.append(dumpOffset(nOffset));
				sb.append(key).append("=");
//				String valString;
				if(val instanceof List || val instanceof Map) {
					sb.append("\n").append(dump(val, nOffset));
				} else {
					sb.append(dump(val));
				}
//				sb.append(dump(val, offset+2));
			}
			sb.append(dumpOffset(offset)).append("}\n");
		} else if(qRes instanceof List) {
			List list = (List)qRes;
			sb.append(dumpOffset(offset));
			sb.append("[\n");
			for(Object val : list) {
				sb.append(dump(val, offset+2));
			}
			sb.append(dumpOffset(offset));
			sb.append("]\n");
		} else {
			sb.append(dumpOffset(offset));
			sb.append(qRes != null ? qRes.toString() : "null");
			sb.append("\n");
		}
		return sb.toString();
	}
	
	private static String dumpOffset(int offset) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<offset; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}
}
