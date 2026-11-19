package ppa.marc;

import org.junit.jupiter.api.Test;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringConfigurationTest {

	@Test

	public void testGrantedReaderBeanExistsThenInitialisationShouldSucceed() throws Exception {
		new ClassPathXmlApplicationContext(RecordReader.class.getName() + ".xml").getBean("marcFileReader");
	}

	@Test

	public void testGrantedWriterBeanExistsThenInitialisationShouldSucceed() throws Exception {
		new ClassPathXmlApplicationContext(RecordWriter.class.getName() + ".xml").getBean("marcFileWriter");
	}
	
}
