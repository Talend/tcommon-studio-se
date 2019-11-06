// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2019 Talend Inc. - www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package routines.system;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

//TODO split to several classes by the level when have a clear requirement or design : job, component, connection
public class JobStructureCatcherUtils {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");

	// TODO split it as too big, even for storing the reference only which point
	// null
	public class JobStructureCatcherMessage {

		public String component_id;

		public String component_name;

		public Map<String, String> component_parameters;

		public String input_connectors;

		public String output_connectors;

		public Map<String, String> connector_name_2_connector_schema;

		public String job_name;

		public String job_id;

		public String job_version;

		public Long systemPid;

		public boolean current_connector_as_input;

		public String current_connector_type;

		public String current_connector;

		public String currrent_row_content;

		public long row_count;

		public long total_row_number;

		public long start_time;

		public long end_time;

		public String moment;

		public String status;
		
		public String source_component_id;
		public String source_component_name;
		public String target_component_id;
		public String target_component_name;
		public List<Map<String, String>> component_schema;
		
		public MessageType message_type;

		public JobStructureCatcherMessage(MessageType message_type) {
			this.message_type = message_type;
		}
		
	}
	
	public enum MessageType {
		
		AUDIT_JOB_START,
		AUDIT_JOB_END,
		AUDIT_COMPONENT_MESSAGE,
		AUDIT_CONNECTION_MESSAGE,
		
		RUNTIME_PARAMETER,
		RUNTIME_SCHEMA
		
	}

	java.util.List<JobStructureCatcherMessage> messages = java.util.Collections
			.synchronizedList(new java.util.ArrayList<JobStructureCatcherMessage>());

	public String job_name = "";

	public String job_id = "";

	public String job_version = "";

	public JobStructureCatcherUtils(String jobName, String jobId, String jobVersion) {
		this.job_name = jobName;
		this.job_id = jobId;
		this.job_version = jobVersion;
	}
	
	public void addComponentParameterMessage(String component_id, String component_name, Map<String, String> component_parameters) {
		JobStructureCatcherMessage scm = new JobStructureCatcherMessage(MessageType.RUNTIME_PARAMETER);
		scm.job_name = this.job_name;
		scm.job_id = this.job_id;
		scm.job_version = this.job_version;
		scm.component_id = component_id;
		scm.component_name = component_name;
		scm.systemPid = JobStructureCatcherUtils.getPid();
		scm.moment = sdf.format(new Date());
		
		scm.component_parameters = component_parameters;
		
		this.messages.add(scm);
	}
	
	public void addConnectionSchemaMessage(String source_component_id, String source_component_name, String target_component_id, String target_component_name, 
			String current_connector, List<Map<String, String>> component_schema) {
		JobStructureCatcherMessage scm = new JobStructureCatcherMessage(MessageType.RUNTIME_SCHEMA);
		scm.job_name = this.job_name;
		scm.job_id = this.job_id;
		scm.job_version = this.job_version;
		scm.systemPid = JobStructureCatcherUtils.getPid();
		scm.moment = sdf.format(new Date());
		
		scm.current_connector = current_connector;
		scm.component_schema = component_schema;
		scm.source_component_id = source_component_id;
		scm.source_component_name = source_component_name;
		scm.target_component_id = target_component_id;
		scm.target_component_name = target_component_name;
		
		this.messages.add(scm);
	}

	public void addConnectionMessage(String component_id, String component_name, boolean current_connector_as_input,
			String current_connector_type, String current_connector, long total_row_number, long start_time,
			long end_time) {
		JobStructureCatcherMessage scm = new JobStructureCatcherMessage(MessageType.AUDIT_CONNECTION_MESSAGE);
		scm.job_name = this.job_name;
		scm.job_id = this.job_id;
		scm.job_version = this.job_version;
		scm.component_id = component_id;
		scm.component_name = component_name;
		scm.systemPid = JobStructureCatcherUtils.getPid();
		scm.moment = sdf.format(new Date());
		
		scm.current_connector_as_input = current_connector_as_input;
		scm.current_connector_type = current_connector_type;
		scm.current_connector = current_connector;
		scm.total_row_number = total_row_number;
		scm.start_time = start_time;
		scm.end_time = end_time;
		
		this.messages.add(scm);
	}

	public void addCM(String component_id, String component_name) {
		JobStructureCatcherMessage scm = new JobStructureCatcherMessage(MessageType.AUDIT_COMPONENT_MESSAGE);
		scm.job_name = this.job_name;
		scm.job_id = this.job_id;
		scm.job_version = this.job_version;
		scm.component_id = component_id;
		scm.component_name = component_name;
		scm.systemPid = JobStructureCatcherUtils.getPid();
		scm.moment = sdf.format(new Date());
		
		this.messages.add(scm);
	}

	public void addJobStartMessage() {
		JobStructureCatcherMessage scm = new JobStructureCatcherMessage(MessageType.AUDIT_JOB_START);
		scm.job_name = this.job_name;
		scm.job_id = this.job_id;
		scm.job_version = this.job_version;
		scm.systemPid = JobStructureCatcherUtils.getPid();
		scm.moment = sdf.format(new Date());
		
		this.messages.add(scm);
	}

	public void addJobEndMessage(long start_time, long end_time, String status) {
		JobStructureCatcherMessage scm = new JobStructureCatcherMessage(MessageType.AUDIT_JOB_END);
		scm.job_name = this.job_name;
		scm.job_id = this.job_id;
		scm.job_version = this.job_version;
		scm.systemPid = JobStructureCatcherUtils.getPid();
		scm.moment = sdf.format(new Date());
		
		scm.start_time = start_time;
		scm.end_time = end_time;
		scm.status = status;
		
		this.messages.add(scm);
	}

	public java.util.List<JobStructureCatcherMessage> getMessages() {
		java.util.List<JobStructureCatcherMessage> messagesToSend = new java.util.ArrayList<JobStructureCatcherMessage>();
		synchronized (messages) {
			for (JobStructureCatcherMessage scm : messages) {
				messagesToSend.add(scm);
			}
			messages.clear();
		}
		return messagesToSend;
	}

	public static long getPid() {
		RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
		String[] mxNameTable = mx.getName().split("@");
		if (mxNameTable.length == 2) {
			return Long.parseLong(mxNameTable[0]);
		} else {
			return Thread.currentThread().getId();
		}
	}
}
