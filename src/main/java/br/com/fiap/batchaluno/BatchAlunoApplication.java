package br.com.fiap.batchaluno;

import br.com.fiap.batchaluno.model.Aluno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableBatchProcessing
public class BatchAlunoApplication {

	Logger logger = LoggerFactory.getLogger(BatchAlunoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BatchAlunoApplication.class, args);
	}

    @Bean
    public FixedLengthTokenizer fixedLengthTokenizer() {
        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();

        tokenizer.setNames("nome", "num_cartao", "ra");
        tokenizer.setColumns(
            new Range(1, 41),
            new Range(42, 49),
            new Range(50, 55)
        );

        return tokenizer;
    }

	@Bean
    public FlatFileItemReader<Aluno> itemReader(@Value("${input.file}") Resource resource,
                                                FixedLengthTokenizer fixedLengthTokenizer) {
		logger.info("Leitura do arquivo {}", resource.getFilename());

        return new FlatFileItemReaderBuilder<Aluno>()
                .name("Read File")
                .resource(resource)
                .targetType(Aluno.class)
                .lineTokenizer(fixedLengthTokenizer)
                .build();
    }

	@Bean
    public ItemProcessor<Aluno, Aluno> itemProcessor() {
        return (aluno) -> aluno;
    }

	@Bean
    public MongoItemWriter<Aluno> itemWriter(MongoTemplate mongoTemplate) {

        logger.info("Persistindo os dados do arquivo na colletion {}", mongoTemplate.getCollectionName(Aluno.class));

        return new MongoItemWriterBuilder<Aluno>()
                .template(mongoTemplate)
                .build();
    }

	@Bean
    public Step step(StepBuilderFactory stepBuilderFactory,
					 ItemReader<Aluno> itemReader,
                     ItemProcessor<Aluno, Aluno> itemProcessor,
					 ItemWriter<Aluno> itemWriter) {
        return stepBuilderFactory.get("Step Chunk - Processar Arquivo")
                .<Aluno, Aluno>chunk(100)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, Step step) {
        return jobBuilderFactory.get("Job - Processar Arquivo Aluno")
                .start(step)
                .build();
    }
}
