package com.example.maptest.mycartest.Bean;

/**
 * Created by ${Author} on 2017/11/25.
 * Use to
 */

public class QuerryOrderBean {
    private String _id;
    private int CommandType;
    private int CommandState;
    private String SendTime;
    private String CommandReturn;
    private String ReturnTime;
    private String OfflineContext;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getCommandType() {
        return CommandType;
    }

    public void setCommandType(int commandType) {
        CommandType = commandType;
    }

    public int getCommandState() {
        return CommandState;
    }

    public void setCommandState(int commandState) {
        CommandState = commandState;
    }

    public String getSendTime() {
        return SendTime;
    }

    public void setSendTime(String sendTime) {
        SendTime = sendTime;
    }

    public String getCommandReturn() {
        return CommandReturn;
    }

    public void setCommandReturn(String commandReturn) {
        CommandReturn = commandReturn;
    }

    public String getReturnTime() {
        return ReturnTime;
    }

    public void setReturnTime(String returnTime) {
        ReturnTime = returnTime;
    }

    public String getOfflineContext() {
        return OfflineContext;
    }

    public void setOfflineContext(String offlineContext) {
        OfflineContext = offlineContext;
    }

    @Override
    public String toString() {
        return "QuerryOrderBean{" +
                "_id='" + _id + '\'' +
                ", CommandType=" + CommandType +
                ", CommandState=" + CommandState +
                ", SendTime='" + SendTime + '\'' +
                ", CommandReturn='" + CommandReturn + '\'' +
                ", ReturnTime='" + ReturnTime + '\'' +
                ", OfflineContext='" + OfflineContext + '\'' +
                '}';
    }

    public QuerryOrderBean(String _id, int commandType, int commandState, String sendTime, String commandReturn, String returnTime, String offlineContext) {
        this._id = _id;
        CommandType = commandType;
        CommandState = commandState;
        SendTime = sendTime;
        CommandReturn = commandReturn;
        ReturnTime = returnTime;
        OfflineContext = offlineContext;
    }

    public QuerryOrderBean() {
    }
}
