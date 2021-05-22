package movingjin.tradingbot.batch.tickerHistory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dalgun
 * @since 2019-10-30
 */
@Slf4j
@Component
public class JobScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobExecutorImpl jobExecutorImpl;
    /*
    @Autowired
    private JobConfiguration jobConfiguration;
*/
    //@Scheduled(initialDelay = 10000, fixedDelay = 30000)
    @Scheduled(cron="0 05 00 * * ?")
    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    public void runJob() {

        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(confMap);

            jobExecutorImpl.execute();
    }

}
