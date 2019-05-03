package main;

public class Config {
	public static final String dateFormatPattern = "mm:ss:SS";
	public static final String fullDateFormatPattern = "dd.MM.YYYY HH:mm:ss";
	public static final String EOD = "End of data";
	
	public static final String DBURL = "jdbc:mysql://localhost?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	public static final String USER = "codeme-user";
	public static final String PASSWORD = "codeme123";
	public static final String DBNAME = "abc";

	public static final String tableName = "consumers";
	public static final String col1name = "id";
	public static final String col1type = " INT AUTO_INCREMENT";
	public static final String col2name = "consumerid";
	public static final String col2type = " INT";
	public static final String col3name = "consumerworkdate";
	public static final String col3type = " BIGINT(20)";
	public static final String col4name = "dataNo";
	public static final String col4type = " VARCHAR(20)";
	public static final String primaryKey = col1name;
}