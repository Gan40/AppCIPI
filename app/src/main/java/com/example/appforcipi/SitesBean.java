package com.example.appforcipi;

public class SitesBean {
        public SitesBean(String id, String site, String alias) {
            this.id = id;
            this.site = site;
            this.alias = alias;
        }

        public SitesBean() {
        }

        /**
         * id : 1
         * site : https://www.facebook.com
         * alias : https://www.facebook.com
         * hashCode : aed92a64fde04639f83b7efc5f10b42d1fed2e3f
         */

        private String id;
        private String site;
        private String alias;
        private String hashCode;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public String getHashCode() {
        return hashCode;
    }

        public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }
}

