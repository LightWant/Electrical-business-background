package model.dal;

/**
 * model的配置信息
 * 数据库配置信息
 * @author 25100
 *
 */
public class Configuration {
	public static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public static String url = "jdbc:sqlserver://localhost:1433;databaseName = foodfactory";
	public static String user = "yxw";
	public static String password = "123456";
}