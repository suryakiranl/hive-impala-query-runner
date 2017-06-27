package com.surya.launcher;

import com.surya.async.AsyncResult;
import com.surya.async.QueryRunner;
import com.surya.conf.JobConf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainLauncher {

    public static void main(String[] args) {
        System.out.println("Start time: " + new Date());
        System.out.println(new Date() + " :: Inside main method");

        try {
            if(args.length < 6) {
                String message = "Invalid param count. Expected Params: \n" +
                    "1) Columns File \n" +
                    "2) Execution Engine (hive or impala) \n" +
                    "3) Columns per query \n" +
                    "4) Parallel Query Count \n" +
                    "5) Table (AUTH or CS) \n" +
                    "6) Partition WHERE clause \n" +
                    "7) YARN Queue \n" +
                    "8) User \n" +
                    "9) Keytab file path";
                throw new RuntimeException(message);
            }

            JobConf conf = new JobConf();

            conf.setColumnNamesFile(args[0]);
            conf.setTargetExecEngine(args[1].trim());
            if("hive".equalsIgnoreCase(conf.getTargetExecEngine())) {
                conf.setHadoopUserName("svchdc17p");
                conf.setHadoopUserKeyTab("/export/home/slaskar/17-user.keytab");

                 conf.setServer("sl73anahdp1041.visa.com");
                // conf.setServer("sl73anahdp47.visa.com");
                conf.setPort("10000");
            } else if("impala".equalsIgnoreCase(conf.getTargetExecEngine())) {
                conf.setHadoopUserName("slaskar");
                conf.setHadoopUserKeyTab("/export/home/slaskar/slaskar.keytab");

                conf.setServer("sl73caeapp10.visa.com");
                conf.setPort("21050");
                conf.setPrincipal("svcimpalaa");
            } else {
                throw new RuntimeException("Invalid execution engine");
            }


            conf.setColumnsPerQuery(Integer.parseInt(args[2]));
            conf.setParallelQueriesCount(Integer.parseInt(args[3]));
            String table = args[4];
            if("AUTH".equalsIgnoreCase(table)) {
                conf.setTableName("opedw.tcaef_auth_dtl_full");
            } else if("CS".equalsIgnoreCase(table)) {
                conf.setTableName("opedw.tcaef_cs_dtl_full");
            } else {
                throw new RuntimeException("Unsupported table name");
            }
            conf.setWhereClause(args[5]);
            if(args.length > 6) {
                conf.setYarnQueue(args[6]);
            }
            if(args.length > 7) {
                conf.setHadoopUserName(args[7]);
            }
            if(args.length > 8) {
                conf.setHadoopUserKeyTab(args[8]);
            }

            process(conf);
        } catch (SQLException | IOException | ClassNotFoundException | InterruptedException | ExecutionException | RuntimeException e) {
            e.printStackTrace();
            e.printStackTrace(System.err); // Also print the stack trace to Error log
            System.err.println("Exception when processing: " + e.getMessage());
            System.exit(1);
        } finally {
            System.out.println("End time: " + new Date());
        }
    }

    private static void process(JobConf conf) throws SQLException, IOException, ClassNotFoundException, ExecutionException, InterruptedException {
        System.out.println(new Date() + " :: Inside process method: " + conf);

        // Read columns from file
        List<String> columns = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(conf.getColumnNamesFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if(!line.isEmpty()) { // Only use non-empty lines
                    columns.add(line);
                }
            }
        }
        System.out.println(new Date() + " :: Lines read from file: " + columns.size());

        // Prepare runners as necessary from columns
        List<QueryRunner> queryRunners = new ArrayList<>();
        int start = 0;
        int end;
        while(start < columns.size()) {
            end = start + conf.getColumnsPerQuery();
            if(end > columns.size()) {
                end = columns.size();
            }
            List<String> columnsSubList = columns.subList(start, end);
            System.out.println(new Date() + " :: Sub list: " + columnsSubList);
            queryRunners.add(new QueryRunner(conf, columnsSubList));
            start = end;
        }
        System.out.println(new Date() + " :: Total number of runners: " + queryRunners.size());

        // Execute these queries in parallel.
        ExecutorService threadPool = Executors.newFixedThreadPool(conf.getParallelQueriesCount());
        Set<Future<AsyncResult>> results = new HashSet<>();
        for(QueryRunner runner: queryRunners) {
            results.add(threadPool.submit(runner));
        }
        threadPool.shutdown();

        // Go through the results
        for(Future<AsyncResult> future: results) {
            AsyncResult ar = future.get();
            System.out.println(new Date() + " :: ***Async Result: " + ar);
        }
    }
}
