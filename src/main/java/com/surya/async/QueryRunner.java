package com.surya.async;

import com.surya.conf.JobConf;
import com.surya.util.SQLUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

public class QueryRunner implements Callable<AsyncResult>{
    private JobConf conf;
    List<String> columns;

    public QueryRunner(JobConf conf, List<String> columns) {
        this.conf = conf;
        this.columns = columns;
    }

    @Override
    public AsyncResult call() throws Exception {
        System.out.println(new Date() + " :: Inside call method for columns: " + columns);

        AsyncResult result = null;

        try (Connection conn = SQLUtil.getConnection(conf);
             Statement stmt = conn.createStatement()) {

            if("hive".equalsIgnoreCase(conf.getTargetExecEngine())) {
                String queueName = conf.getYarnQueue() == null ? "default" : conf.getYarnQueue();
                String sql = "set mapreduce.job.queuename=" + queueName;
                System.out.println(new Date() + " :: Running on queue: " + queueName);
                stmt.execute(sql);
            }

            try(ResultSet rs = stmt.executeQuery(prepareSQLForExec())) {
                while(rs.next()) {
                    long rowCount = rs.getLong(1);
                    result = new AsyncResult(this.columns, rowCount);
                    System.out.println(new Date() + " :: " + rowCount + " is the result for columns: " + columns);
                }
            }

        }

        return result;
    }

    private String prepareSQLForExec() {
        String sql = " select count(1) from " + conf.getTableName() +
            " where " + conf.getWhereClause() + " and ( ";

        boolean firstColumn = true;
        for(String colName: this.columns) {
            if(!firstColumn) {
                sql = sql + " or ";
            }
            sql = sql + colName + " is null ";
            firstColumn = false;
        }

        sql = sql + " )";

        final String TAG = " /* coalesce_check */ ";
        if("hive".equalsIgnoreCase(conf.getTargetExecEngine())) {
            sql = sql + " -- " + TAG;
        } else {
            sql = TAG + sql;
        }

        System.out.println(new Date() + " :: SQL to be executed: " + sql);

        return sql;
    }
}
