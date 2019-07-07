package com.example.appforcipi;

import java.util.List;

    class ProvidersBean {
        /**
         * id : 2
         * provider_name : Intercom
         */
        public ProvidersBean(){
        }

        public ProvidersBean(String id, String provider_name) {
            this.id = id;
            this.provider_name = provider_name;
        }

        private String id;
        private String provider_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProvider_name() {
            return provider_name;
        }

        public void setProvider_name(String provider_name) {
            this.provider_name = provider_name;
        }
    }
