public class MyUser {
    
    priate String uuid;
    
    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }
    
    // hashode here
    // equals here
    
    @Override
    public String toString() {
        return ReflectioToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
   
}
