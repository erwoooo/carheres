package com.example.maptest.mycartest.New;

import com.example.maptest.mycartest.Utils.agent.TreeNodeId;
import com.example.maptest.mycartest.Utils.agent.TreeNodeLabel;
import com.example.maptest.mycartest.Utils.agent.TreeNodePid;

import java.util.Arrays;

/**
 * Created by ${Author} on 2018/3/12.
 * Use to
 */

public class AgentlistBean {
    @TreeNodeId
    private int id;
    @TreeNodePid
    private int user_id;
    @TreeNodeLabel
    private String nickname;

    @Override
    public String toString() {
        return "AgentlistBean{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", username='" + nickname + '\'' +
                '}';
    }

    public AgentlistBean() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public AgentlistBean(int id, int user_id, String username) {
        this.id = id;
        this.user_id = user_id;
        this.nickname = username;
    }
}
