package routines.system;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.generic.IndexedRecord;
import org.joda.time.Instant;
import org.talend.components.api.component.runtime.Reader;
import org.talend.components.api.component.runtime.Source;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.common.avro.RootSchemaUtils;
import org.talend.components.common.component.runtime.RootRecordUtils;

/**
 * This is Decorator for {@link Reader}. It adds additional functionality for {@link Reader#getCurrent()} method of wrapped
 * Reader.
 * All other methods remain unchanged.
 * 
 * <p>
 * This class's {@link Reader#getCurrent()} method does following:
 * 1. Checks whether outgoing {@link IndexedRecord} is Root record
 * 2. Retrieves Flow variables (Out of band data) from Root record
 * 3. Fill runtime container with flow variables
 * 4. Unwrap and return Data Record
 */
public class FlowVariablesReader implements Reader<Object> {

    /**
     * Component {@link Reader}
     */
    private final Reader<?> wrappedReader;

    /**
     * Job runtime container
     */
    private final RuntimeContainer runtimeContainer;

    /**
     * Root schema of wrapped {@link Reader} output. It is retrieved from first record requested
     */
    private Schema rootSchema;

    /**
     * Flow variables schema (also called out of band schema). It is retrieved from first record requested
     */
    private Schema flowVariablesSchema;
    
    /**
     * Strategy for output data processing
     */
    private DataProcessor dataProcessor;

    /**
     * Constructor sets wrapped {@link Reader} and global map
     * 
     * @param reader component {@link Reader}
     * @param runtimeContainer runtime container
     */
    public FlowVariablesReader(Reader<?> reader, RuntimeContainer runtimeContainer) {
        this.wrappedReader = reader;
        this.runtimeContainer = runtimeContainer;
    }

    @Override
    public boolean start() throws IOException {
        return wrappedReader.start();
    }

    @Override
    public boolean advance() throws IOException {
        return wrappedReader.advance();
    }

    /**
     * This method does following:
     * 1. Checks whether outgoing {@link IndexedRecord} is Root record
     * 2. Retrieves Flow variables (Out of band data) from Root record
     * 3. Fill global map with flow variables
     * 4. Unwrap and return Data Record
     */
    @Override
    public Object getCurrent() throws NoSuchElementException {
        Object data = wrappedReader.getCurrent();
        if (rootSchema == null) {
            if (RootRecordUtils.isRootRecord(data)) {
                IndexedRecord rootRecord = (IndexedRecord) data;
                rootSchema = rootRecord.getSchema();
                flowVariablesSchema = RootSchemaUtils.getOutOfBandSchema(rootSchema);
                dataProcessor = new RootRecordProcessor();
            } else {
                dataProcessor = new NotRootRecordProcessor();
            }
        }
        return dataProcessor.processData(data);
    }

    @Override
    public Instant getCurrentTimestamp() throws NoSuchElementException {
        return wrappedReader.getCurrentTimestamp();
    }

    @Override
    public void close() throws IOException {
        wrappedReader.close();
    }

    @Override
    public Source getCurrentSource() {
        return wrappedReader.getCurrentSource();
    }

    @Override
    public Map<String, Object> getReturnValues() {
        return wrappedReader.getReturnValues();
    }

    /**
     * Processes output of wrapped {@link Reader#getCurrent()} method
     */
    private abstract class DataProcessor {

        protected abstract Object processData(Object data);
    }

    /**
     * Implements processing for not Root record data
     */
    private class NotRootRecordProcessor extends DataProcessor {

        /**
         * Returns input data without any processing. It is used, when output of wrapped record is not Root record
         * 
         * @return input data without changes
         */
        @Override
        protected Object processData(Object data) {
            return data;
        }

    }

    /**
     * Implements processing for Root record data
     */
    private class RootRecordProcessor extends DataProcessor {

        /**
         * Outer class is the only user of this class. Outer class should control that only {@link IndexedRecord} instances
         * (which should be also Root records) are passed to this method. No additional checks are performed here for performance
         * purposes
         * 
         * <p>
         * This method retrieves flow variables and put them into global map. Also unwraps and returns data {@link IndexedRecord}
         * 
         * @param data output of wrapped Record
         * @return Main data
         */
        @Override
        protected Object processData(Object data) {
            IndexedRecord record = (IndexedRecord) data;
            IndexedRecord flowVariablesRecord = (IndexedRecord) record.get(1);
            
            String componentId = runtimeContainer.getCurrentComponentId();
            for (Field field : flowVariablesSchema.getFields()) {
                String flowVariableKey = field.name();
                int fieldPos = field.pos();
                Object flowVariableValue = flowVariablesRecord.get(fieldPos);
                runtimeContainer.setComponentData(componentId, flowVariableKey, flowVariableValue);
            }
            
            Object mainData = record.get(0);
            return mainData;
        }

    }

}
