package com.joinpay.esjob.config;

import com.joinpay.esjob.enums.JobTypeEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "joinpay.esjob")
public class EsJobProperties {
    private String zkhost;
    private String namespace;
    private String cron_exp;
    private String jobName;
    private int shardingCount;
    private String description;
    private JobTypeEnum jobType;
    private boolean streamProcess;
    private String shardingItemParameters;
    private String jobParameter;
    private boolean failover=false;
    private boolean misfire=true;
    private String scriptCommandLine;
    private String jobClass;
    private String listener;
    private boolean enableEventLog;
    private String eventurl;
    private String eventuser;
    private String eventpass;

    public String getZkhost() {
        return zkhost;
    }

    public void setZkhost(String zkhost) {
        this.zkhost = zkhost;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getCron_exp() {
        return cron_exp;
    }

    public void setCron_exp(String cron_exp) {
        this.cron_exp = cron_exp;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getShardingCount() {
        return shardingCount;
    }

    public void setShardingCount(int shardingCount) {
        this.shardingCount = shardingCount;
    }

    public JobTypeEnum getJobType() {
        return jobType;
    }

    public void setJobType(JobTypeEnum jobType) {
        this.jobType = jobType;
    }

    public boolean isStreamProcess() {
        return streamProcess;
    }

    public void setStreamProcess(boolean streamProcess) {
        this.streamProcess = streamProcess;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShardingItemParameters() {
        return shardingItemParameters;
    }

    public void setShardingItemParameters(String shardingItemParameters) {
        this.shardingItemParameters = shardingItemParameters;
    }

    public String getJobParameter() {
        return jobParameter;
    }

    public void setJobParameter(String jobParameter) {
        this.jobParameter = jobParameter;
    }

    public boolean isFailover() {
        return failover;
    }

    public void setFailover(boolean failover) {
        this.failover = failover;
    }

    public boolean isMisfire() {
        return misfire;
    }

    public void setMisfire(boolean misfire) {
        this.misfire = misfire;
    }

    public String getScriptCommandLine() {
        return scriptCommandLine;
    }

    public void setScriptCommandLine(String scriptCommandLine) {
        this.scriptCommandLine = scriptCommandLine;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }

    public String getEventurl() {
        return eventurl;
    }

    public void setEventurl(String eventurl) {
        this.eventurl = eventurl;
    }

    public String getEventuser() {
        return eventuser;
    }

    public void setEventuser(String eventuser) {
        this.eventuser = eventuser;
    }

    public String getEventpass() {
        return eventpass;
    }

    public void setEventpass(String eventpass) {
        this.eventpass = eventpass;
    }

    public boolean isEnableEventLog() {
        return enableEventLog;
    }

    public void setEnableEventLog(boolean enableEventLog) {
        this.enableEventLog = enableEventLog;
    }
}
