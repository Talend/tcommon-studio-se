// ============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2021 Talend Inc. - www.talend.com
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

//TODO split to several classes by the level when have a clear requirement or design : job, component, connection
public class JobStructureCatcherUtils {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");

	// TODO split it as too big, even for storing the reference only which point
	// null
	public class JobStructureCatcherMessage {

		public String component_id;
		
		public String component_label;

		public String component_name;

		public Map<String, String> component_parameters;

		public List<Map<String, String>> component_schema;

		public String input_connectors;

		public String output_connectors;

		public Map<String, String> connector_name_2_connector_schema;

		public String job_name;

		public String job_id;

		public String job_version;

		public boolean current_connector_as_input;

		public String current_connector_type;

		public String current_connector;

		public String currrent_row_content;
		
		public String sourceId;
		public String sourceLabel;
		public String sourceComponentName;
		public String targetId;
		public String targetLabel;
		public String targetComponentName;

		public long row_count;

		public long total_row_number;

		public long start_time;

		public long end_time;

		public String moment;

		public String status;
		
		public LogType log_type;
		
		//process uuid
		public String pid = ProcessIdAndThreadId.getProcessId();
				
		//thread uuid
		public String tid = ProcessIdAndThreadId.getThreadId();

		public JobStructureCatcherMessage() {
		}

	}
	
	public static enum LogType {
		JOBSTART,
		JOBEND,
		RUNCOMPONENT,
		FLOWINPUT,
		FLOWOUTPUT,
		PERFORMANCE,
		
		RUNTIMEPARAMETER,
		RUNTIMESCHEMA
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
		JobStructureCatcherMessage scm = new JobStructureCatcherMessage();
		scm.job_name = this.job_name;
		scm.job_id = this.job_id;
		scm.job_version = this.job_version;
		
		scm.component_id = component_id;
		scm.component_name = component_name;
		
		scm.component_parameters = component_parameters;
		
		scm.log_type = LogType.RUNTIMEPARAMETER;
		
		messages.add(scm);
	}
	
	public void addConnectionSchemaMessage(String source_component_id, String source_component_name, String target_component_id, String target_component_name, 
			String current_connector, List<Map<String, String>> component_schema) {
		JobStructureCatcherMessage scm = new JobStructureCatcherMessage();
		scm.job_name = this.job_name;
		scm.job_id = this.job_id;
		scm.job_version = this.job_version;
		
		scm.current_connector = current_connector;
		scm.sourceId = source_component_id;
		scm.sourceComponentName = source_component_name;
		scm.targetId = target_component_id;
		scm.targetComponentName = target_component_name;
		
		scm.component_schema = component_schema;
		
		scm.log_type = LogType.RUNTIMESCHEMA;
		
		messages.add(scm);
	}

	public void addConnectionMessage(String component_id, String component_label, String component_name, boolean current_connector_as_input,
			String current_connector_type, String current_connector, long total_row_number, long start_time,
			long end_time) {
		JobStructureCatcherMessage scm = new JobStructureCatcherMessage();
		scm.job_name = this.job_name;
		scm.job_id = this.job_id;
		scm.job_version = this.job_version;

		scm.component_id = component_id;
		scm.component_label = component_label;
		scm.component_name = component_name;
		scm.current_connector_as_input = current_connector_as_input;
		scm.current_connector_type = current_connector_type;
		scm.current_connector = current_connector;
		scm.total_row_number = total_row_number;
		scm.start_time = start_time;
		scm.end_time = end_time;
		
		if(current_connector_as_input) {
			scm.log_type = LogType.FLOWINPUT;
		} else {
			scm.log_type = LogType.FLOWOUTPUT;
		}
		
		messages.add(scm);
	}

	public void addCM(String component_id, String component_label, String component_name) {
		JobStructureCatcherMessage scm = new JobStructureCatcherMessage();
		scm.moment = sdf.format(new Date());
		
		scm.job_name = this.job_name;
		scm.job_id = this.job_id;
		scm.job_version = this.job_version;

		scm.component_id = component_id;
		scm.component_label = component_label;
		scm.component_name = component_name;
		
		scm.log_type = LogType.RUNCOMPONENT;
		
		messages.add(scm);
	}

	public void addJobStartMessage() {
		JobStructureCatcherMessage scm = new JobStructureCatcherMessage();
		scm.moment = sdf.format(new Date());
		
		scm.job_name = this.job_name;
		scm.job_id = this.job_id;
		scm.job_version = this.job_version;
		
		scm.log_type = LogType.JOBSTART;

		messages.add(scm);
	}

	public void addJobEndMessage(long start_time, long end_time, String status) {
		JobStructureCatcherMessage scm = new JobStructureCatcherMessage();
		scm.moment = sdf.format(new Date());
		
		scm.job_name = this.job_name;
		scm.job_id = this.job_id;
		scm.job_version = this.job_version;

		scm.status = (status == "" ? "end" : status);
		scm.start_time = start_time;
		scm.end_time = end_time;
		
		scm.log_type = LogType.JOBEND;
		
		messages.add(scm);
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

	public void addConnectionMessage4PerformanceMonitor(String current_connector, String sourceId, String sourceLabel,
			String sourceComponentName, String targetId, String targetLabel, String targetComponentName, int row_count,
			long start_time, long end_time) {
		JobStructureCatcherMessage scm = new JobStructureCatcherMessage();
		scm.job_name = this.job_name;
		scm.job_id = this.job_id;
		scm.job_version = this.job_version;
		
		scm.current_connector = current_connector;
		
		scm.sourceId = sourceId;
		scm.sourceLabel = sourceLabel;
		scm.sourceComponentName = sourceComponentName;
		
		scm.targetId = targetId;
		scm.targetLabel = targetLabel;
		scm.targetComponentName = targetComponentName;
		
		scm.row_count = row_count;
		scm.start_time = start_time;
		scm.end_time = end_time;
		
		scm.log_type = LogType.PERFORMANCE;
		
		messages.add(scm);
		
	}
}
