package ppa.marc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration that registers all record readers and writers as beans.
 * Use it directly:
 * <pre>
 * ApplicationContext context = new AnnotationConfigApplicationContext(MarcFileAccessConfiguration.class);
 * RecordReader recordReader = context.getBean("marcFileReader", RecordReader.class);
 * </pre>
 * or import it into an application's own configuration with
 * {@code @Import(MarcFileAccessConfiguration.class)} and inject the readers and
 * writers by name.
 * <p>
 * Available beans:
 * <ul>
 * <li>{@code marcFileReader} - {@link RecordReader} for binary UNIMARC records</li>
 * <li>{@code textFileReader} - {@link RecordReader} for textual records</li>
 * <li>{@code marcFileWriter} - {@link RecordWriter} for binary UNIMARC records</li>
 * <li>{@code textFileWriter} - {@link RecordWriter} for textual records</li>
 * </ul>
 * Note that the textual format is not from a MARC standard.
 */
@Configuration
@ComponentScan(basePackages = { "ppa.marc.reader", "ppa.marc.writer" })
public class MarcFileAccessConfiguration {
}
