package com.joinpay.esjob.config;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.joinpay.esjob.enums.JobTypeEnum;
import com.joinpay.esjob.utils.SpringUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(EsJobProperties.class)
public class EsJobConfig {
    @Autowired
    private EsJobProperties esJobProperties;

    @Bean
    public CoordinatorRegistryCenter coordinatorRegistryCenter(){
        ZookeeperRegistryCenter regCenter =new ZookeeperRegistryCenter(new ZookeeperConfiguration(esJobProperties.getZkhost(), esJobProperties.getNamespace()));
        regCenter.init();
        return regCenter;
    }

    @Bean
    @ConditionalOnBean(CoordinatorRegistryCenter.class)
    public void jobScheduler() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        ElasticJobListener listener = null;
        JobEventConfiguration jobEventRdbConfig = null;
        if (esJobProperties.getListener()!=null){
             listener = (ElasticJobListener) Class.forName(esJobProperties.getListener()).newInstance();
        }

        if (esJobProperties.isEnableEventLog()){
            HikariDataSource dataSource = new HikariDataSource();

            dataSource.setJdbcUrl(esJobProperties.getEventurl());
            dataSource.setUsername(esJobProperties.getEventuser());
            dataSource.setPassword(esJobProperties.getEventpass());
            jobEventRdbConfig = new JobEventRdbConfiguration(dataSource);
        }

        if (esJobProperties.getJobType()== JobTypeEnum.SIMPLE){
            // 定义作业核心配置
            JobCoreConfiguration simpleCoreConfig  = JobCoreConfiguration.newBuilder(esJobProperties.getJobName(), esJobProperties.getCron_exp(), esJobProperties.getShardingCount())
                    .shardingItemParameters(esJobProperties.getShardingItemParameters()).description(esJobProperties.getDescription())
                    .failover(esJobProperties.isFailover()).jobParameter(esJobProperties.getJobParameter()).misfire(esJobProperties.isMisfire())
                    .build();
            // 定义DATAFLOW类型配置
            SimpleJobConfiguration simpleJobConfig  = new SimpleJobConfiguration(simpleCoreConfig, esJobProperties.getJobClass());
            // 定义Lite作业根配置
            LiteJobConfiguration  simpleJobRootConfig  = LiteJobConfiguration.newBuilder(simpleJobConfig).build();

            if (listener==null){
                if (jobEventRdbConfig==null){
                    new JobScheduler(coordinatorRegistryCenter(), simpleJobRootConfig).init();
                }else{
                    new JobScheduler(coordinatorRegistryCenter(), simpleJobRootConfig,jobEventRdbConfig).init();
                }
            } else {
                if (jobEventRdbConfig==null){
                    new JobScheduler(coordinatorRegistryCenter(), simpleJobRootConfig, listener).init();
                }else {
                    new JobScheduler(coordinatorRegistryCenter(), simpleJobRootConfig, jobEventRdbConfig,listener).init();
                }
            }
        }else if (esJobProperties.getJobType()==JobTypeEnum.DATAFLOW){
            // 定义作业核心配置
            JobCoreConfiguration dataflowCoreConfig   = JobCoreConfiguration.newBuilder(esJobProperties.getJobName(), esJobProperties.getCron_exp(), esJobProperties.getShardingCount())
                    .shardingItemParameters(esJobProperties.getShardingItemParameters()).description(esJobProperties.getDescription())
                    .failover(esJobProperties.isFailover()).jobParameter(esJobProperties.getJobParameter()).misfire(esJobProperties.isMisfire())
                    .build();
            // 定义DATAFLOW类型配置
            DataflowJobConfiguration dataflowJobConfig   = new DataflowJobConfiguration(dataflowCoreConfig , esJobProperties.getJobClass(),esJobProperties.isStreamProcess());
            // 定义Lite作业根配置
            LiteJobConfiguration  dataflowJobRootConfig   = LiteJobConfiguration.newBuilder(dataflowJobConfig ).build();
            if (listener==null) {
                if (jobEventRdbConfig==null){
                    new JobScheduler(coordinatorRegistryCenter(), dataflowJobRootConfig).init();
                }else{
                    new JobScheduler(coordinatorRegistryCenter(), dataflowJobRootConfig,jobEventRdbConfig).init();
                }
            }
            else {
                if (jobEventRdbConfig==null){
                    new JobScheduler(coordinatorRegistryCenter(), dataflowJobRootConfig, listener).init();
                }else{
                    new JobScheduler(coordinatorRegistryCenter(), dataflowJobRootConfig, jobEventRdbConfig,listener).init();
                }
            }
        }else{
            // 定义作业核心配置
            JobCoreConfiguration scriptCoreConfig   = JobCoreConfiguration.newBuilder(esJobProperties.getJobName(), esJobProperties.getCron_exp(), esJobProperties.getShardingCount())
                    .shardingItemParameters(esJobProperties.getShardingItemParameters()).description(esJobProperties.getDescription())
                    .failover(esJobProperties.isFailover()).jobParameter(esJobProperties.getJobParameter()).misfire(esJobProperties.isMisfire())
                    .build();
            // 定义DATAFLOW类型配置
            ScriptJobConfiguration scriptJobConfig    = new ScriptJobConfiguration(scriptCoreConfig , esJobProperties.getScriptCommandLine());
            // 定义Lite作业根配置
            LiteJobConfiguration  scriptJobRootConfig    = LiteJobConfiguration.newBuilder(scriptJobConfig).build();

            if (listener==null){
                if (jobEventRdbConfig==null){
                    new JobScheduler(coordinatorRegistryCenter(), scriptJobRootConfig).init();
                }else{
                    new JobScheduler(coordinatorRegistryCenter(), scriptJobRootConfig,jobEventRdbConfig).init();
                }
            }
            else{
                if (jobEventRdbConfig==null){
                    new JobScheduler(coordinatorRegistryCenter(), scriptJobRootConfig,listener).init();
                }else{
                    new JobScheduler(coordinatorRegistryCenter(), scriptJobRootConfig,jobEventRdbConfig,listener).init();
                }
            }
        }
    }
}
