package com.surya.util;

import com.surya.conf.JobConf;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class SQLUtil {
    private static final String AT_VISA_DOMAIN = "@VISA.COM";
    private static final String HIVE_JDBC_DRIVER_CLASS = "org.apache.hive.jdbc.HiveDriver";

    private static String getJDBCUrl(final JobConf conf) {
        String jdbcUrl;

        if("hive".equalsIgnoreCase(conf.getTargetExecEngine())) {
            jdbcUrl = "jdbc:hive2://" + conf.getServer() + ":" + conf.getPort()
                + "/default;principal=hive/" + conf.getServer() + AT_VISA_DOMAIN + ";auth=kerberos";
        } else {
            jdbcUrl = "jdbc:hive2://" + conf.getServer() + ":" + conf.getPort()
                + "/;principal=" + conf.getPrincipal() + "/"
                + conf.getServer() + AT_VISA_DOMAIN + ";auth=kerberos";
        }

        return jdbcUrl;
    }

    private static void initKerberosKeytab(final String username, final String keytabFile) throws IOException {
        Configuration conf = new Configuration();
        conf.set("hadoop.security.authentication", "Kerberos");
        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation.loginUserFromKeytab(username + AT_VISA_DOMAIN, keytabFile);
    }

    private static Connection getConnection(final JobConf conf, final String jdbcUrl)
        throws IOException, ClassNotFoundException, SQLException {
        // System.out.println("Inside getConnection method");

        initKerberosKeytab(conf.getHadoopUserName(), conf.getHadoopUserKeyTab());

        Class.forName(HIVE_JDBC_DRIVER_CLASS);

        System.out.println(new Date() + " :: Invoking getConnection on DriverManager ...");
        Connection conn = DriverManager.getConnection(jdbcUrl);
        System.out.println(new Date() + " :: Connection established successfully !!");

        // System.out.println("Exiting getConnection method. ");
        return conn;
    }

    public static Connection getConnection(final JobConf conf)
        throws IOException, ClassNotFoundException, SQLException {
        return getConnection(conf, getJDBCUrl(conf));
    }
}
