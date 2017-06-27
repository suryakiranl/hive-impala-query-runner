package com.surya.conf;

public class JobConf {
    private String hadoopUserName;
    private String hadoopUserKeyTab;

    private String targetExecEngine;
    private String server;
    private String port;
    private String principal;

    private String columnNamesFile;
    private int parallelQueriesCount;
    private int columnsPerQuery;
    private String tableName;
    private String whereClause;
    private String yarnQueue;

    public String getHadoopUserName() {
        return hadoopUserName;
    }

    public void setHadoopUserName(String hadoopUserName) {
        this.hadoopUserName = hadoopUserName;
    }

    public String getHadoopUserKeyTab() {
        return hadoopUserKeyTab;
    }

    public void setHadoopUserKeyTab(String hadoopUserKeyTab) {
        this.hadoopUserKeyTab = hadoopUserKeyTab;
    }

    public String getColumnNamesFile() {
        return columnNamesFile;
    }

    public void setColumnNamesFile(String columnNamesFile) {
        this.columnNamesFile = columnNamesFile;
    }

    public int getParallelQueriesCount() {
        return parallelQueriesCount;
    }

    public void setParallelQueriesCount(int parallelQueriesCount) {
        this.parallelQueriesCount = parallelQueriesCount;
    }

    public int getColumnsPerQuery() {
        return columnsPerQuery;
    }

    public void setColumnsPerQuery(int columnsPerQuery) {
        this.columnsPerQuery = columnsPerQuery;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public void setWhereClause(String whereClause) {
        this.whereClause = whereClause;
    }

    public String getTargetExecEngine() {
        return targetExecEngine;
    }

    public void setTargetExecEngine(String targetExecEngine) {
        this.targetExecEngine = targetExecEngine;
    }

    @Override
    public String toString() {
        return "JobConf{" +
            "hadoopUserName='" + hadoopUserName + '\'' +
            ", hadoopUserKeyTab='" + hadoopUserKeyTab + '\'' +
            ", targetExecEngine='" + targetExecEngine + '\'' +
            ", server='" + server + '\'' +
            ", port='" + port + '\'' +
            ", principal='" + principal + '\'' +
            ", columnNamesFile='" + columnNamesFile + '\'' +
            ", parallelQueriesCount=" + parallelQueriesCount +
            ", columnsPerQuery=" + columnsPerQuery +
            ", tableName='" + tableName + '\'' +
            ", whereClause='" + whereClause + '\'' +
            ", yarnQueue='" + yarnQueue + '\'' +
            '}';
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getYarnQueue() {
        return yarnQueue;
    }

    public void setYarnQueue(String yarnQueue) {
        this.yarnQueue = yarnQueue;
    }

}
