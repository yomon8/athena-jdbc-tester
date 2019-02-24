import java.sql.*;
import java.util.Properties;

public class Main {
	public static void main(String[] args) {
		Connection conn = null;
		Statement statement = null;

		String region = System.getenv("REGION");
		if (region == null || region.isEmpty()) {
			System.out.println("Please set environment variable REGION");
			System.exit(1);
		}
		System.out.println(String.format("REGION -> %s", region));

		String sql = System.getenv("SQL");
		if (sql == null || sql.isEmpty()) {
			System.out.println("Please set environment variable SQL");
			System.exit(1);
		}
		System.out.println(String.format("SQL -> %s", sql));

		String outputLocation = System.getenv("OUTPUT_LOCATION");
		if (outputLocation == null || outputLocation.isEmpty()) {
			System.out.println("Please set environment variable OUTPUT_LOCATION");
			System.exit(1);
		}
		System.out.println(String.format("OUTPUT_LOCATION -> %s", outputLocation));

		String workgroup = System.getenv("WORKGROUP");
		if (workgroup == null || workgroup.isEmpty()) {
			workgroup = "primary";
			System.out.println(String.format("Use default WORKGROUP -> %s", workgroup));
		} else {
			System.out.println(String.format("WORKGROUP -> %s", workgroup));
		}

		try {
			String athenaUrl = String.format("jdbc:awsathena://%s.amazonaws.com:443", region);
			Class.forName("com.simba.athena.jdbc.Driver");
			Properties info = new Properties();
			info.put("AwsRegion", region);
			info.put("S3OutputLocation", outputLocation);
			info.put("Workgroup", workgroup);
			info.put("AwsCredentialsProviderClass",
					"com.simba.athena.amazonaws.auth.DefaultAWSCredentialsProviderChain");

			System.out.println("Connecting to Athena...");
			conn = DriverManager.getConnection(athenaUrl, info);

			System.out.println("Listing tables...");
			statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				ResultSetMetaData metadata = rs.getMetaData();
				int columnCount = metadata.getColumnCount();
				String row = "";
				for (int i = 1; i <= columnCount; i++) {
					if (i == columnCount) {
						row += rs.getString(i);
						break;
					}
					row += rs.getString(i) + ",";
				}
				System.out.println(row);
			}
			rs.close();
			conn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (statement != null)
					statement.close();
			} catch (Exception ex) {

			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("Finished connectivity test.");
	}
}
