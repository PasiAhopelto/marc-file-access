package ppa.marc;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

public class SpringConfigurationTest extends TestCase {

	public void testGrantedReaderBeanExistsThenInitialisationShouldSucceed() throws Exception {
		new ClassPathXmlApplicationContext(RecordReader.class.getName() + ".xml").getBean("marcFileReader");
	}

	public void testGrantedWriterBeanExistsThenInitialisationShouldSucceed() throws Exception {
		new ClassPathXmlApplicationContext(RecordWriter.class.getName() + ".xml").getBean("marcFileWriter");
	}
	
}
