package com.hell.demoSpringBatch.config;

import com.hell.demoSpringBatch.dao.BankTransaction;
import com.hell.demoSpringBatch.processor.BankTransactionItemAnalitycsProcessor;
import com.hell.demoSpringBatch.processor.BankTransactionItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private ItemReader<BankTransaction> bankTransactionItemReader;
    @Autowired private ItemWriter<BankTransaction> bankTransactionItemWriter;

    //@Autowired private ItemProcessor<BankTransaction, BankTransaction> bankTransactionItemProcessor;

    @Bean
    public Job bankJob() {
        Step step1 = stepBuilderFactory.get("step-load-data")
                .<BankTransaction, BankTransaction>chunk(100)
                .reader(bankTransactionItemReader)
                .processor(compositeItemProcessor())
                .writer(bankTransactionItemWriter)
                .build();

        return jobBuilderFactory.get("bank-data-loader-job").start(step1).build();
    }

    @Bean
    private CompositeItemProcessor<? super BankTransaction,? extends BankTransaction> compositeItemProcessor() {

        List<ItemProcessor<BankTransaction, BankTransaction>> itemProcessors = new ArrayList<>();
        itemProcessors.add(itemProcessor1());
        itemProcessors.add(itemProcessor2());

        CompositeItemProcessor<BankTransaction, BankTransaction> compositeItemProcessor =  new CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(itemProcessors);
        return compositeItemProcessor;
    }


    @Bean
    public BankTransactionItemProcessor itemProcessor1() {
        return new BankTransactionItemProcessor();
    }

    @Bean
    public BankTransactionItemAnalitycsProcessor itemProcessor2() {
        return new BankTransactionItemAnalitycsProcessor();
    }


    @Bean
    public FlatFileItemReader<BankTransaction> getItemReader (@Value("${inputFile}") Resource inputFile) {
        FlatFileItemReader<BankTransaction> fileItemReader = new FlatFileItemReader<>();
        fileItemReader.setName("FFIR1");
        fileItemReader.setLinesToSkip(1);
        fileItemReader.setResource(inputFile);
        fileItemReader.setLineMapper(lineMapper());
        return fileItemReader;
    }

    @Bean
    private LineMapper<BankTransaction> lineMapper() {
        DefaultLineMapper<BankTransaction> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");// per settare il delimitatore nel file CSV
        lineTokenizer.setStrict(false);//
        lineTokenizer.setNames("id", "accountID", "strdate", "transaction_type", "transaction_amount");
        BeanWrapperFieldSetMapper fieldSetMapper = new BeanWrapperFieldSetMapper();
        fieldSetMapper.setTargetType(BankTransaction.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }


}
