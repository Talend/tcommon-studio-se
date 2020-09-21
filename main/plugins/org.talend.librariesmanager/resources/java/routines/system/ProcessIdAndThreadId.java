package routines.system;

import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.UUID;

public class ProcessIdAndThreadId {
	
	private static class PTId {
		String processId;
		String threadId;
	}

	private static final ThreadLocal<PTId> Id = new ThreadLocal<PTId>() {
		@Override
		protected PTId initialValue() {
			PTId id = new PTId();
			id.processId = getPid();
			id.threadId = UUID.randomUUID().toString();
			return id;
		}
		
	};
	
	private static String getPid() {
		RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
		String processName = mx.getName();
		try {
			return UUID.nameUUIDFromBytes(processName.getBytes("UTF8")).toString();
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}
	
	public static String getProcessId() {
		return Id.get().processId;
	}
	
	public static String getThreadId() {
		return Id.get().threadId;
	}
	
}
