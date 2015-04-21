package org.whale.ext.readWrite;

/**
 * <pre>
 * 读/写动态数据库 决策者
 * 根据DataSourceType是write/read 来决定是使用读/写数据库
 * 通过ThreadLocal绑定实现选择功能
 * </pre>
 * @author Zhang Kaitao
 *
 */
public class ReadWriteDataSourceDecision {
    
    public enum DataSourceType {
        write, read;
    }
    
    
    private static final ThreadLocal<DateSourceTypeAndFrom> holder = new ThreadLocal<DateSourceTypeAndFrom>();

    public static void markWrite(String fromClass) {
        holder.set(new DateSourceTypeAndFrom(DataSourceType.write, fromClass));
    }
    
    public static void markRead(String fromClass) {
        holder.set(new DateSourceTypeAndFrom(DataSourceType.read, fromClass));
    }
    
    public static void reset() {
        holder.set(null);
    }
    
    public static boolean isChoiceNone() {
        return null == holder.get(); 
    }
    
    public static String getFromClass(){
    	if(isChoiceNone())
    		return null;
    	return holder.get().getFromClass();
    }
    
    public static boolean isChoiceWrite() {
    	if(isChoiceNone())
    		return false;
        return DataSourceType.write == holder.get().getDataSourceType();
    }
    
    public static boolean isChoiceRead() {
    	if(isChoiceNone())
    		return false;
        return DataSourceType.read == holder.get().getDataSourceType();
    }
    
    public static DateSourceTypeAndFrom get(){
    	return holder.get();
    }
    
   public static class DateSourceTypeAndFrom{
	   
    	private DataSourceType dataSourceType;
    	
    	private String fromClass;
    	
    	private String dataSourceName;
    	
    	public DateSourceTypeAndFrom(){}
    	
    	public DateSourceTypeAndFrom(DataSourceType dataSourceType, String fromClass){
    		this.dataSourceType = dataSourceType;
    		this.fromClass = fromClass;
    	}

		public DataSourceType getDataSourceType() {
			return dataSourceType;
		}

		public void setDataSourceType(DataSourceType dataSourceType) {
			this.dataSourceType = dataSourceType;
		}

		public String getFromClass() {
			return fromClass;
		}

		public void setFromClass(String fromClass) {
			this.fromClass = fromClass;
		}

		public String getDataSourceName() {
			return dataSourceName;
		}

		public void setDataSourceName(String dataSourceName) {
			this.dataSourceName = dataSourceName;
		}

    }

}
