package com.system.lsp.printer;

public enum PrinterCommands {
    START {
        @Override
        public String zpl(String... params) {
            return "^XA";
        }

        @Override
        public String cpcl(String... params) {
            return null;
        }
    },

    END {
        @Override
        public String zpl(String... params) {
            return "^XZ";
        }

        @Override
        public String cpcl(String... params) {
            return null;
        }
    },
    FIELD_ORIGIN {
        /***
         * params
         * 0- X position dots
         * 1- Y position dots
         */
        @Override
        public String zpl(String... params) {
            return String.format("^FO%s,%s", params[0], params[1]);
        }

        @Override
        public String cpcl(String... params) {
            return null;
        }
    },
    FONT_FORMAT {
        /*
         * params
         * 0- Text format = N(Normal), B(Bold), I(Italic)
         * 1- Font size in dots
         * */
        @Override
        public String zpl(String... params) {
            return String.format("^A%s,%s,%s", params[0], params[1], params[1]);
        }

        @Override
        public String cpcl(String... params) {
            return null;
        }
    },
    START_FIELD {
        @Override
        public String zpl(String... params) {
            return "^FD";
        }

        @Override
        public String cpcl(String... params) {
            return null;
        }
    },
    END_FIELD {
        @Override
        public String zpl(String... params) {
            return "^FS";
        }

        @Override
        public String cpcl(String... params) {
            return null;
        }
    },
    ALIGN_TO_LEFT {
        /**
         * params
         * 0 - max dots per row available
         * */
        @Override
        public String zpl(String... params) {
            return String.format("^FB%s,1,0,L", params[0]);
        }

        @Override
        public String cpcl(String... params) {
            return null;
        }
    },
    ALIGN_TO_CENTER {
        /**
         * params
         * 0 - max dots per row available
         * */
        @Override
        public String zpl(String... params) {
            return String.format("^FB%s,1,0,C,0", params[0]);
        }

        @Override
        public String cpcl(String... params) {
            return null;
        }
    },
    ALIGN_TO_RIGHT {
        /**
         * params
         * 0 - max dots per row available
         * */
        @Override
        public String zpl(String... params) {
            return String.format("^FB%s,1,0,R", params[0]);
        }

        @Override
        public String cpcl(String... params) {
            return null;
        }
    },
    SKIP_LINE {
        @Override
        public String zpl(String... params) {
            return "\n";
        }

        @Override
        public String cpcl(String... params) {
            return "\n";
        }
    },
    FEED_PAPER {
        /*
         * params
         * 0 - lines
         * */
        @Override
        public String zpl(String... params) {
            return String.format("^LL%s", params[0]);
        }

        @Override
        public String cpcl(String... params) {
            return null;
        }
    };


    public abstract String zpl(String... params);

    public abstract String cpcl(String... params);
}
