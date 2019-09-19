package com.example.maptest.mycartest.Utils.agent;

/**
 * Created by ${Author} on 2017/9/29.
 * Use to
 */

public class AgentBean {
    @TreeNodeId
    private int AgentId;
    @TreeNodePid
    private int SuperiorId;
    @TreeNodeLabel
    private String AgentName;

    public AgentBean() {
    }

    @Override
    public String toString() {
        return "AgentBean{" +
                "AgentId=" + AgentId +
                ", SuperiorId=" + SuperiorId +
                ", AgentName='" + AgentName + '\'' +
                '}';
    }

    public int getAgentId() {
        return AgentId;
    }

    public void setAgentId(int agentId) {
        AgentId = agentId;
    }

    public int getSuperiorId() {
        return SuperiorId;
    }

    public void setSuperiorId(int superiorId) {
        SuperiorId = superiorId;
    }

    public String getAgentName() {
        return AgentName;
    }

    public void setAgentName(String agentName) {
        AgentName = agentName;
    }

    public AgentBean(int agentId, int superiorId, String agentName) {
        AgentId = agentId;
        SuperiorId = superiorId;
        AgentName = agentName;
    }
}
