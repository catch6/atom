package org.openscada.opc.dcom.da;

import org.openscada.opc.dcom.common.KeyedResultSet;
import org.openscada.opc.dcom.common.ResultSet;

public interface IOPCDataCallback {

	public void dataChange(int transactionId, int serverGroupHandle, int masterQuality, int masterErrorCode, KeyedResultSet<Integer, ValueData> result);

	public void readComplete(int transactionId, int serverGroupHandle, int masterQuality, int masterErrorCode, KeyedResultSet<Integer, ValueData> result);

	public void writeComplete(int transactionId, int serverGroupHandle, int masterErrorCode, ResultSet<Integer> result);

	public void cancelComplete(int transactionId, int serverGroupHandle);

}
