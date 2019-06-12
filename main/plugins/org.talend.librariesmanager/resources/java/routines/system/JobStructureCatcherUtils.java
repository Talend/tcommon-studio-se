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

		public List<Map<String, String>> component_schema;

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

		public JobStructureCatcherMessage(String component_id, String component_name,
				Map<String, String> component_parameters, List<Map<String, String>> component_schema,
				String input_connectors, String output_connectors,
				Map<String, String> connector_name_2_connector_schema, String job_name, String job_id,
				String job_version, boolean current_connector_as_input, String current_connector_type,
				String current_connector, String currrent_row_content, long row_count, long total_row_number,
				long start_time, long end_time, String status) {
			this.component_id = component_id;
			this.component_name = component_name;
			this.component_parameters = component_parameters;
			this.component_schema = component_schema;
			this.input_connectors = input_connectors;
			this.output_connectors = output_connectors;
			this.connector_name_2_connector_schema = connector_name_2_connector_schema;

			this.job_name = job_name;
			this.job_version = job_version;
			this.job_id = job_id;
			this.systemPid = JobStructureCatcherUtils.getPid();

			this.current_connector_as_input = current_connector_as_input;
			this.current_connector_type = current_connector_type;
			this.current_connector = current_connector;
			this.currrent_row_content = currrent_row_content;
			this.row_count = row_count;
			this.total_row_number = total_row_number;
			this.start_time = start_time;
			this.end_time = end_time;

			this.moment = sdf.format(new Date());
			this.status = status;
		}

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

	public void addMessage(String component_id, String component_name, Map<String, String> component_parameters,
			List<Map<String, String>> component_schema, String input_connectors, String output_connectors,
			Map<String, String> connector_name_2_connector_schema, boolean current_connector_as_input,
			String current_connector_type, String current_connector, String currrent_row_content, long row_count,
			long total_row_number, long start_time, long end_time, String status) {
		JobStructureCatcherMessage scm = new JobStructureCatcherMessage(component_id, component_name,
				component_parameters, component_schema, input_connectors, output_connectors,
				connector_name_2_connector_schema, this.job_name, this.job_id, this.job_version,
				current_connector_as_input, current_connector_type, current_connector, currrent_row_content, row_count,
				total_row_number, start_time, end_time, status);
		messages.add(scm);
	}

	public void addConnectionMessage(String component_id, String component_name, boolean current_connector_as_input,
			String current_connector_type, String current_connector, long total_row_number, long start_time,
			long end_time) {
		this.addMessage(component_id, component_name, null, null, null, null, null, current_connector_as_input,
				current_connector_type, current_connector, null, 0, total_row_number, start_time, end_time, null);
	}

	public void addComponentMessage(String component_id, String component_name) {
		this.addMessage(component_id, component_name, null, null, null, null, null, false, null, null,
				null, 0, 0, 0, 0, null);
	}

	public void addJobStartMessage() {
		this.addMessage(null, null, null, null, null, null, null, false, null, null, null, 0, 0, 0, 0, null);
	}

	public void addJobEndMessage(long start_time, long end_time, String status) {
		this.addMessage(null, null, null, null, null, null, null, false, null, null, null, 0, 0, start_time, end_time,
				status == "" ? "end" : status);
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
