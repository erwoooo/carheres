package com.example.maptest.mycartest.New;

/**
 * Created by ${Author} on 2018/5/10.
 * Use to
 */

public class UpAgentBean {
    private data data;
    private meta meta;

    @Override
    public String toString() {
        return "UpAgentBean{" +
                "data=" + data +
                ", meta=" + meta +
                '}';
    }

    public data getData() {
        return data;
    }

    public void setData(data data) {
        this.data = data;
    }

    public meta getMeta() {
        return meta;
    }

    public void setMeta(meta meta) {
        this.meta = meta;
    }

    public UpAgentBean() {
    }

    public UpAgentBean(data data, meta meta) {
        this.data = data;
        this.meta = meta;
    }
    public static class data{
        private String address;
        private String linkman;
        private String nickname;
        private String telephone;
        private String username;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLinkman() {
            return linkman;
        }

        public void setLinkman(String linkman) {
            this.linkman = linkman;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public String toString() {
            return "data{" +
                    "address='" + address + '\'' +
                    ", linkman='" + linkman + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", telephone='" + telephone + '\'' +
                    ", username='" + username + '\'' +
                    '}';
        }

        public data() {
        }

        public data(String address, String linkman, String nickname, String telephone, String username) {
            this.address = address;
            this.linkman = linkman;
            this.nickname = nickname;
            this.telephone = telephone;
            this.username = username;
        }
    }
    public static class meta{

    }
}
