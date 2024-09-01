package com.hell.demoSpringBatch.RestController;

import com.hell.demoSpringBatch.processor.BankTransactionItemAnalitycsProcessor;
import lombok.NoArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@NoArgsConstructor
public class JobRestController {

    @Autowired private JobLauncher jobLauncher;

    @Autowired private Job job;

    @Autowired BankTransactionItemAnalitycsProcessor analitycsProcessor;


    @GetMapping("/startJob")
    public BatchStatus loadStatus() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        Map<String, JobParameter> params = new HashMap<>();
        params.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(params);
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        while(jobExecution.isRunning()) {
            System.out.println("....");
        }
        return jobExecution.getStatus();
    }

    @GetMapping("/analytics")
    public Map<String, Double> analytics() {
        Map<String, Double> map = new HashMap<>();
        map.put("totalCedit", analitycsProcessor.getTotalCredit());
        map.put("totalDebit", analitycsProcessor.getTotalDebit());
        return map;
    }
}
