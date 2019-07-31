package com.pavmoroz.fast.sql;

import org.apache.log4j.Logger;

public class App {

    private static final Logger log = Logger.getLogger(App.class);

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                SQLBuilder sqlBuilder = new SQLBuilder();
                sqlBuilder.init(args[0]);
                sqlBuilder.build();
            } else {
                log.info("Specify the path to the data file!");
            }
        } catch (RuntimeException ex){
            log.info(ex.getMessage());
        } catch (Exception ex){
            log.warn(null, ex);
        }
    }
}
