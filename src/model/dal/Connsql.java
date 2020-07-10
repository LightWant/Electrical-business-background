package model.dal;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.CallStack;

public class Connsql {
	private Connection con;
	private Statement statement;
	private PreparedStatement psta;
	private ResultSet res;
	private String year;
	
	public Connsql(String user, String password) {
		try {
			Class.forName(Configuration.driver);
			System.out.println("驱动加载成功!");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			con = DriverManager.getConnection(Configuration.url, user, password);
			System.out.println("连接数据库成功!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        year = sdf.format(date);
	}

	private boolean isin(String sql) {	
		try {
			statement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			res = statement.executeQuery(sql);

            if(res.next()) return true;
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return false;
	}
	
	private String getV(String str) {
		try {  
		    Integer.parseInt(str); 
		    Float.parseFloat(str); 
		    return str;  
		} catch (NumberFormatException e) {  
		    return "'"+str+"'";  
		}  
	}
	
	private String getWhere(String[] parms, String[] values) {
		String whereSql = "";
		
		if((parms == null && values == null) || (parms.length == 0 && values.length == 0)) {
			;
		}
		else {
			try {
				assert(parms.length == values.length);
			} catch(AssertionError e) {
				e.printStackTrace();
			}
			
//			if(isID(parms[0]) && isCODE(values[0])) values[0] = changeToID(values[0]);
			
			whereSql += " where " + parms[0] + "=" + getV(values[0]);
			for(int i = 1; i < parms.length; i++) {
//				if(isID(parms[i]) && isCODE(values[i])) values[i] = changeToID(values[i]);
				whereSql += " and " + parms[i] + "=" + getV(values[i]);
			}
		}
		
		return whereSql;
	}
	
//	private String changeToID(String val) {
//		val = val.substring(4);
//		while(val.startsWith("0"))
//			val = val.substring(1);
//		return val;
//	}
//	
//	private String changeToCODE(String val) {
//		return " CODE ";
//	}
//
//	private boolean isCODE(String val) {
//		if(val != null && val.length() == 8 && val.startsWith(year))
//			return true;
//		return false;
//	}
//
//	private boolean isID(String parm) {
//		return (parm.endsWith("ID") || parm.endsWith("iD") || 
//				parm.endsWith("Id") || parm.endsWith("id"));
//	}

	//执行查找类型的 sql 语句
	public String[][] get(String sql) {//
		String columns[][];
		
//		String[] tmp = sql.substring(sql.indexOf("select")+6, sql.indexOf("from")).split(",");
//		for(int i = 0; i < tmp.length; i++) {
//			if(isID(tmp[i])) {
//				sql.replace(tmp[i], changeToCODE(tmp[i]));
//			}
//		}
//		
//		if(sql.indexOf("where") != -1) {
//			tmp = sql.substring(sql.indexOf("where")+5).split("and");
//			for(int i = 0; i < tmp.length; i++) {
//				if(tmp[i].) {
//					sql.replace(tmp[i], changeToCODE(tmp[i]));
//				}
//			}
//		}
		
		
		try {
			statement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			res = statement.executeQuery(sql);
			int colnum = res.getMetaData().getColumnCount();
			//System.out.println("column"+colnum);
			res.last();
			int len = res.getRow();
			//System.out.println("len"+len);
			if(len <= 0) {
				return null;
			}
			res.first();
			
			columns = new String[len][];
			
			int i = 0;
			do {
				columns[i] = new String[colnum];
				for(int j = 1; j <= colnum; j++)
			        columns[i][j - 1] = res.getString(j);
			    i++;
			}while(res.next());

		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}
		
		return columns;
	}
	
	//查找，返回值第一维度是行数
	public String[][] select(String[] goalParms, String[] parms, String[] values, String tableName) {
//		for(int i = 0; i < goalParms.length; i++) {
//			if(isID(goalParms[i])) {
//				goalParms[i] = changeToCODE(goalParms[i]);
//			}
//		}
		
		String whereSql = getWhere(parms, values);
		String sql = "select ";
		
		if(goalParms == null || goalParms.length == 0) {
			sql += "* from ";
		}
		else {
			sql += goalParms[0];
			for(int i = 1; i < goalParms.length; i++) {
				sql += "," + goalParms[i];
			}
			sql += " from ";
		}
		sql += tableName + whereSql;
	//	System.out.println(sql);
		CallStack.printCallStatck();
		return get(sql);
	}
	
	/// 插入
	public int insert(String[] parms, String[] values, String tableName) {
		String whereSql = getWhere(parms, values);

		///若已经存在，先删除
		if(isin("select * from " + tableName + whereSql)) {
			try {
				psta = con.prepareStatement("delete from " + tableName + whereSql);
				psta.executeUpdate();
			} catch (Exception e) {
				return -1;//说明已经存在了该项
			}
		}
		
		try {
			String insertSql = "insert into " + tableName;
			insertSql += "(" + parms[0];
			for(int i = 1; i < parms.length; i++)
				insertSql += "," + parms[i];
			insertSql += ") values (?";
			for(int i = 1; i < values.length; i++)
				insertSql += ",?";
			insertSql += ")";
			//System.out.println("insert "+insertSql+"\nwhere "+whereSql);
			
			//System.out.println(insertSql);
			psta = con.prepareStatement(insertSql);
			
			for(int i = 0; i < values.length; i++) {
				psta.setString(i + 1, values[i]);
			}
			
			psta.executeUpdate();
		} catch (Exception e) {
			return -2;//插入失败
		}
		
		CallStack.printCallStatck();
		return 1;//插入成功；
	}
	
	//调用表值函数
	public String[][] selectTable(String[] goalParms, String[] parms,String functionName) {
//		for(int i = 0; i < goalParms.length; i++) {
//			if(isID(goalParms[i])) {
//				goalParms[i] = changeToCODE(goalParms[i]);
//			}
//		}
		
		String sql = "select ";
		if(goalParms == null || goalParms.length == 0) {
			sql += "* from "+functionName;
		}
		else {
			sql += goalParms[0];
			for(int i = 1; i < goalParms.length; i++) {
				sql += "," + goalParms[i];
			}
			sql += " from "+functionName;
		}
		sql += "(";
		if(parms!=null&&parms.length!=0) {
			sql+=getV(parms[0]);
			
			for(int i = 1;i<parms.length;i++) {
				sql += ","+getV(parms[i]);
			}
		}
		sql += ")";
		
		CallStack.printCallStatck();
		//System.out.println("selectTable sql = "+sql);
		return get(sql);//
	}
	//调用标量函数 change 
	public String selectScale(String[] parms,String functionName) {
		String sql = "select dbo."+functionName;
		
		sql += "(";
		if(parms!=null&&parms.length!=0) {
			sql+=getV(parms[0]);
			
			for(int i = 1;i<parms.length;i++) {
				sql += ","+getV(parms[i]);
			}
			
		}
		sql += ")";
		String [][] result = get(sql);
		if(result == null||result.length==0)
			return "0";
		else return  result[0][0];//
	}

	public boolean update(String[] setparms,String[] setvalues,String []parms,String[]values,String tableName) {
//		for(int i = 0; i < goalParms.length; i++) {
//			if(isID(goalParms[i])) {
//				goalParms[i] = changeToCODE(goalParms[i]);
//			}
//		}
		
		String sql = "";
		String wheresql = this.getWhere(parms, values);
		sql += "update "+tableName+" set "+setparms[0]+" = "+getV(setvalues[0]);
		for(int i = 1;i<setparms.length;i++) {
			sql += ","+setparms[i] +" = "+getV(setvalues[i]);
		}
		sql += wheresql;
		try {
			//执行update
			statement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			//对数据库的更新能及时响应，insert、delete不能，可以滚动查看
			statement.executeUpdate(sql);

		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}
		
		CallStack.printCallStatck();
		return true;
	}

	public boolean delete(String[]parms,String[]values,String tableNAME){
		String sql = "";
		String wheresql = this.getWhere(parms, values);
		
		sql += "delete from "+tableNAME+" "+wheresql;
		//System.out.println("delete sql:"+sql);
		try {
			statement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		
		}
		
		CallStack.printCallStatck();
		return true;
		
	}
	public void executeUYL(int pcID,int ylID,int ylnum,int cjID) {
		//System.out.println("conn executeUYL");
		//只能执行useYuanliao这一个存储过程
		CallableStatement callStmt = null;
		try {
			callStmt = con.prepareCall("{call useYuanliao(?,?,?,?)}");
			callStmt.setInt(1, pcID);
			callStmt.setInt(2, ylID);
			callStmt.setInt(3, ylnum);
			callStmt.setInt(4, cjID);
	        callStmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CallStack.printCallStatck();
	}
}


