package ppa.marc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ppa.marc.reader.TextFileReader;
import ppa.marc.reader.UnimarcFileReader;
import ppa.marc.writer.TextRecordWriter;
import ppa.marc.writer.UnimarcRecordWriter;

public class SpringConfigurationTest {

	@Test
	public void testAnnotationConfigurationProvidesAllReadersAndWriters() throws Exception {
		ApplicationContext context = new AnnotationConfigApplicationContext(MarcFileAccessConfiguration.class);
		assertInstanceOf(UnimarcFileReader.class, context.getBean("marcFileReader", RecordReader.class));
		assertInstanceOf(TextFileReader.class, context.getBean("textFileReader", RecordReader.class));
		assertInstanceOf(UnimarcRecordWriter.class, context.getBean("marcFileWriter", RecordWriter.class));
		assertInstanceOf(TextRecordWriter.class, context.getBean("textFileWriter", RecordWriter.class));
	}

	@Test
	public void testGrantedReaderBeanExistsThenXmlInitialisationShouldSucceed() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(RecordReader.class.getName() + ".xml");
		assertInstanceOf(UnimarcFileReader.class, context.getBean("marcFileReader"));
		assertInstanceOf(TextFileReader.class, context.getBean("textFileReader"));
	}

	@Test
	public void testGrantedWriterBeanExistsThenXmlInitialisationShouldSucceed() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(RecordWriter.class.getName() + ".xml");
		assertInstanceOf(UnimarcRecordWriter.class, context.getBean("marcFileWriter"));
		assertInstanceOf(TextRecordWriter.class, context.getBean("textFileWriter"));
	}

}
